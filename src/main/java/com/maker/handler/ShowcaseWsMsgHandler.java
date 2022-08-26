package com.maker.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.maker.Const;
import com.maker.ShowcaseServerConfig;
import com.maker.entity.ChatMessage;
import com.maker.entity.ConversationInfo;
import com.maker.entity.UserInfo;
import com.maker.service.ChatMessageService;
import com.maker.service.ConversationInfoService;
import com.maker.service.UserInfoService;
import com.maker.utils.MessageResult;
import com.maker.utils.RedisUtils;
import com.maker.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.common.starter.annotation.TioServerMsgHandler;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.common.WsSessionContext;
import org.tio.websocket.server.handler.IWsMsgHandler;
import org.tio.websocket.starter.TioWebSocketServerAutoConfiguration;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

@TioServerMsgHandler
@Component
@Slf4j
public class ShowcaseWsMsgHandler implements IWsMsgHandler {

    @Autowired
    private UserInfoService userInfoService;


    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private ConversationInfoService conversationInfoService;

    @Autowired
    private TioWebSocketServerAutoConfiguration tioWebSocketServerAutoConfiguration;


    private static Map<String, ChannelContext> userSocketMap = new ConcurrentHashMap<>();

    private static List<JSONObject> undelivered = new LinkedList<>();

    /**
     * 握手时走这个方法，业务可以在这里获取cookie，request参数等
     */
    @Override
    public HttpResponse handshake(HttpRequest request, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        return httpResponse;
    }

    /**
     * 群发器
     *
     * @param httpRequest
     * @param httpResponse
     * @param channelContext
     * @throws Exception
     * @author tanyaowu
     */
    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        //校验用户
        UserInfo one = userInfoService.getOne(new QueryWrapper<UserInfo>().eq("user_id", httpRequest.getParam("user_id")));
        if (one != null) {
            Tio.bindUser(channelContext, one.getUserId());
            log.info("绑定用户: {}", one);
            Tio.bindGroup(channelContext, Const.GROUP_ID);
        } else {
            WsResponse wsResponse = WsResponse.fromText(JSON.toJSONString(MessageResult.fail("用户不存在")), ShowcaseServerConfig.CHARSET);
            Tio.sendToUser(channelContext.tioConfig, httpRequest.getParam("user_id"), wsResponse);
            Tio.remove(channelContext, "用户不存在");
        }
    }

    /**
     * 字节消息（binaryType = arraybuffer）过来后会走这个方法
     */
    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        return null;
    }

    /**
     * 当客户端发close flag时，会走这个方法
     */
    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        Tio.remove(channelContext, "receive close flag");
        return null;
    }

    /**
     * 字符消息（binaryType = blob）过来后会走这个方法
     */

    @Override
    public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {
        WsSessionContext wsSessionContext = (WsSessionContext) channelContext.get();
        //获取websocket握手包
        HttpRequest httpRequest = wsSessionContext.getHandshakeRequest();
        //转换消息
        ChatMessage chatMessage = null;
        try {
            chatMessage = JSON.parseObject(text, ChatMessage.class);
        } catch (Exception e) {
            return JSON.toJSONString(Result.fail("消息格式错误，非JSON字符串"));
        }

        //获取消息类型
        Integer type = chatMessage.getType();
        //消息ID
        String msgId = chatMessage.getMsgId();
        //发送人
        String come = channelContext.userid;
        //接收者ID
        String go = chatMessage.getGo();
        //如果是心跳则返回Null
        if (type == 103) {
            return null;
        }

        //客户端收到消息返回通知
        if (clientAck(type, msgId)) {
            return null;
        }

        //已读消息回应
        if (clientReadMsgAck(channelContext, chatMessage, type)) {
            return null;
        }
        //0-私聊 1-群聊
        Integer mode = chatMessage.getMode();
        if (mode != 0) {
            return JSON.toJSONString(MessageResult.fail("暂不支持该模式"));
        }

        if (StringUtils.isEmpty(come) || StringUtils.isEmpty(go)) {
            return JSON.toJSONString(MessageResult.fail("发送者和接收者不能为空"));
        }
        //送达通知
        noticeOfDelivery(channelContext, come, msgId);

        //类型：0-文字 1-图片 2-视频 3-等等   101-送达消息 102-已读消息 103-心跳 104-客户端ACK 105-服务端ACK
        switch (type) {
            case 0:
                textHandler(channelContext, chatMessage, come, go);
                break;
            case 1:
                imageHandler(channelContext, chatMessage, go);
                break;
            default:
                return JSON.toJSONString(MessageResult.fail("不支持该类型"));
        }
        //设置发送人
        chatMessage.setCome(come);
        //存储消息
        chatMessageService.save(chatMessage);
        //设置最新会话
        setConversationInfo(come, channelContext, chatMessage);
        return null;
    }

    /**
     * 接收方收到消息  如果是当前页面返回已读的状态   数据库更新已读并且发送给发送方已读标识
     * @param channelContext
     * @param chatMessage
     * @param type
     * @return
     */
    private boolean clientReadMsgAck(ChannelContext channelContext, ChatMessage chatMessage, Integer type) {
        //已读消息回应
        if (type == 106) {
            JSONArray objects = JSON.parseArray(chatMessage.getMsgData());
            if (objects == null) {
                return true;
            }
            for (int i = 0; i < objects.size(); i++) {
                //已读消息回应
                String tMsgId = objects.getString(i);
                //更新数据库状态
                ChatMessage one = chatMessageService.getOne(new QueryWrapper<ChatMessage>().eq("msg_id", tMsgId));
                one.setMsgStatus(2);
                chatMessageService.updateById(one);
                //发送已读回执
                MessageResult result = MessageResult.success("已读", one.getMsgId(), 106);
                WsResponse ackWsResponse = WsResponse.fromText(JSON.toJSONString(result), ShowcaseServerConfig.CHARSET);
                Boolean delivered = Tio.sendToUser(channelContext.tioConfig, one.getCome(), ackWsResponse);
                log.info("用户{}是否返回送达已读通知: {}", one.getCome(), delivered);
                return true;
            }
        }
        return false;
    }


    /**
     * 客户端收到消息   更新数据库状态
     * @param type
     * @param msgId
     * @return
     */
    private boolean clientAck(Integer type, String msgId) {
        //客户端收到消息返回通知
        if (type == 104) {
            ChatMessage one = chatMessageService.getOne(new QueryWrapper<ChatMessage>().eq("msg_id", msgId));
            if (one != null) {
                one.setMsgStatus(1);
                chatMessageService.updateById(one);
            }
            return true;
        }
        return false;
    }

    /**
     * 图片消息处理
     * @param channelContext
     * @param chatMessage
     * @param go
     */
    private static void imageHandler(ChannelContext channelContext, ChatMessage chatMessage, String go) {
        WsResponse wsResponse = WsResponse.fromText(JSON.toJSONString(chatMessage), ShowcaseServerConfig.CHARSET);
        Boolean send = Tio.sendToUser(channelContext.tioConfig, go, wsResponse);
        if (!send) {
            chatMessage.setMsgStatus(3);
        } else {
            chatMessage.setMsgStatus(1);
        }
    }

    /**
     * 文本消息处理
     * @param channelContext
     * @param chatMessage
     * @param come
     * @param go
     */
    private void textHandler(ChannelContext channelContext, ChatMessage chatMessage, String come, String go) {
        WsResponse wsResponse = WsResponse.fromText(JSON.toJSONString(chatMessage), ShowcaseServerConfig.CHARSET);
        Boolean send = Tio.sendToUser(channelContext.tioConfig, go, wsResponse);
        if (!send) {
            chatMessage.setMsgStatus(3);
        } else {
            chatMessage.setMsgStatus(1);
        }

    }

    /**
     * 设置最新会话
     * @param userId
     * @param channelContext
     * @param msg
     */
    private void setConversationInfo(String userId, ChannelContext channelContext, ChatMessage msg) {
        //会话管理
        Boolean nullConversationInfo = true;
        ConversationInfo conversationInfo = conversationInfoService.getOne(new QueryWrapper<ConversationInfo>().eq("send_id", userId).or().eq("form_id", userId));
        if (conversationInfo == null) {
            conversationInfo = new ConversationInfo();
            conversationInfo.setSendId(msg.getCome());
            conversationInfo.setFormId(msg.getGo());
            conversationInfo.setLastMsgId(msg.getId());
            conversationInfo.setLastTime(msg.getMsgTime());
            conversationInfo.setLastType(msg.getType());
            conversationInfo.setUnreadCount(1);
        } else {
            nullConversationInfo = false;
            conversationInfo.setLastMsgId(msg.getId());
            conversationInfo.setLastTime(msg.getMsgTime());
            conversationInfo.setLastType(msg.getType());
            //设置未读数
            conversationInfo.setUnreadCount(conversationInfo.getUnreadCount() + 1);

        }

        conversationInfoService.saveOrUpdate(conversationInfo);
        if (nullConversationInfo) {
            //如果是新增会话
            //回执消息
            MessageResult result = MessageResult.success("有新的会话消息~", String.valueOf(conversationInfo.getLastMsgId()), 107, conversationInfo);
            WsResponse ackWsResponse = WsResponse.fromText(JSON.toJSONString(result), ShowcaseServerConfig.CHARSET);
            Boolean send = Tio.sendToUser(channelContext.tioConfig, conversationInfo.getSendId(), ackWsResponse);
            log.info("{} 有新的会话消息通知发送是否成功 {}", conversationInfo.getSendId(), send);

            Boolean sendForm = Tio.sendToUser(channelContext.tioConfig, conversationInfo.getFormId(), ackWsResponse);
            log.info("{} 有新的会话消息通知发送是否成功 {}", conversationInfo.getFormId(), sendForm);
        }else {
            //更新会话
            //回执消息
            MessageResult result = MessageResult.success("会话消息改变啦~", String.valueOf(conversationInfo.getLastMsgId()), 108, conversationInfo);
            WsResponse ackWsResponse = WsResponse.fromText(JSON.toJSONString(result), ShowcaseServerConfig.CHARSET);
            Boolean send = Tio.sendToUser(channelContext.tioConfig, conversationInfo.getSendId(), ackWsResponse);
            log.info("{} 有新的会话消息通知发送是否成功 {}", conversationInfo.getSendId(), send);

            Boolean sendForm = Tio.sendToUser(channelContext.tioConfig, conversationInfo.getFormId(), ackWsResponse);
            log.info("{} 有新的会话消息通知发送是否成功 {}", conversationInfo.getFormId(), sendForm);
        }
    }

    /**
     * 送达通知，（服务器收到该消息返回结果）  如果没有返回客户端会一直发来问
     *
     * @param channelContext
     * @param userId
     * @param msgId
     */
    private void noticeOfDelivery(ChannelContext channelContext, String userId, String msgId) {
        //回执消息
        MessageResult result = MessageResult.success("已送达", msgId, 101);
        WsResponse ackWsResponse = WsResponse.fromText(JSON.toJSONString(result), ShowcaseServerConfig.CHARSET);
        Boolean delivered = Tio.sendToUser(channelContext.tioConfig, userId, ackWsResponse);
        log.info("用户{}是否返回送达通知: {}", userId, delivered);
    }

}
