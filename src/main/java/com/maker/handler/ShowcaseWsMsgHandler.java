package com.maker.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maker.Const;
import com.maker.ShowcaseServerConfig;
import com.maker.entity.ConversationInfo;
import com.maker.entity.MessageInfo;
import com.maker.entity.UserInfo;
import com.maker.service.ConversationInfoService;
import com.maker.service.MessageInfoService;
import com.maker.service.UserInfoService;
import com.maker.utils.MessageType;
import com.maker.utils.RedisUtils;
import com.maker.utils.Result;
import jodd.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tio.common.starter.annotation.TioServerMsgHandler;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.TioConfig;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.server.TioServer;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.common.WsSessionContext;
import org.tio.websocket.server.WsServerStarter;
import org.tio.websocket.server.handler.IWsMsgHandler;
import org.tio.websocket.starter.TioWebSocketServerAutoConfiguration;
import org.tio.websocket.starter.TioWebSocketServerBootstrap;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

@TioServerMsgHandler
@Component
@Slf4j
public class ShowcaseWsMsgHandler implements IWsMsgHandler {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private MessageInfoService messageInfoService;

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

        UserInfo one = userInfoService.getOne(new QueryWrapper<UserInfo>().eq("user_id", httpRequest.getParam("user_id")));
        if (one != null) {
            Tio.bindUser(channelContext, one.getUserId());
            log.info("绑定用户: {}", one);
            Tio.bindGroup(channelContext, Const.GROUP_ID);
        } else {
            JSONObject object = new JSONObject();
            object.put("code", 502);
            object.put("msg", "用户不存在");
            WsResponse wsResponse = WsResponse.fromText(JSON.toJSONString(object), ShowcaseServerConfig.CHARSET);
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
        JSONObject messageInfo = null;
        try {
            messageInfo = JSON.parseObject(text);
        } catch (Exception e) {
//            return Result.fail("消息格式错误，非JSON字符串");
            JSONObject object = new JSONObject();
            object.put("code", 401);
            object.put("msg", "消息格式错误，非JSON字符串");
            return JSON.toJSONString(object);
        }

        //消息类型
        Integer type = messageInfo.getInteger("type");
        //如果是心跳
        if (type.equals(MessageType.HEARTBEAT_CODE)) {
            return null;
        }

        messageInfo.remove("loadding");
        //发送人
        String userId = channelContext.userid;
        //接收人
        String formId = messageInfo.getString("formId");
        //消息ID
        String msgId = messageInfo.getString("msgId");
        //客户端收到消息返回通知
        if (type.equals(MessageType.CLIENTELE_ACK)) {
            MessageInfo one = messageInfoService.getOne(new QueryWrapper<MessageInfo>().eq("msg_id", msgId));
            if (one != null) {
                one.setViewStatus(1);
                messageInfoService.updateById(one);
            }
            return null;
        }
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(formId)) {
            JSONObject object = new JSONObject();
            object.put("code", 401);
            object.put("msg", "发送人和接收人不能为空");
            return JSON.toJSONString(object);
        }
        MessageInfo msg = new MessageInfo();
        msg.setSendId(userId);
        msg.setFormId(formId);
        msg.setMsgId(msgId);
        msg.setMsgTime(messageInfo.getLong("msgTime"));
        //送达通知
        noticeOfDelivery(channelContext, userId, msgId);

        switch (type) {
            case 2:
                msg.setType(1);
                msg.setViewStatus(0);
                msg.setContent(JSON.toJSONString(messageInfo));
                WsResponse wsResponse = WsResponse.fromText(JSON.toJSONString(msg), ShowcaseServerConfig.CHARSET);
                Boolean send = Tio.sendToUser(channelContext.tioConfig, formId, wsResponse);
                if (!send) {
                    msg.setMsgStatus(1);
                } else {
                    msg.setMsgStatus(2);
                }
                messageInfoService.save(msg);
                setConversationInfo(userId, formId, msg);
                break;
            case 3:
                break;
            default:
                JSONObject object = new JSONObject();
                object.put("code", 401);
                object.put("msg", "消息格式错误，非JSON字符串");
                return JSON.toJSONString(object);
        }
        return null;
    }

    private void setConversationInfo(String userId, String formId, MessageInfo msg) {
        //会话管理
        ConversationInfo conversationInfo = conversationInfoService.getOne(new QueryWrapper<ConversationInfo>().eq("send_id", userId).or().eq("form_id",userId));
        if (conversationInfo == null) {
            conversationInfo = new ConversationInfo();
            conversationInfo.setSendId(msg.getSendId());
            conversationInfo.setFormId(msg.getFormId());
            conversationInfo.setLastMsgId(msg.getId());
            conversationInfo.setLastTime(msg.getMsgTime());
            conversationInfo.setLastType(msg.getType());
            conversationInfo.setUnreadCount(1);
        }else {
            conversationInfo.setLastMsgId(msg.getId());
            conversationInfo.setLastTime(msg.getMsgTime());
            conversationInfo.setLastType(msg.getType());
            //设置未读数
            conversationInfo.setUnreadCount(conversationInfo.getUnreadCount() + 1);

        }
        conversationInfoService.saveOrUpdate(conversationInfo);

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
        JSONObject ackData = new JSONObject();
        ackData.put("code", 0);
        ackData.put("type", MessageType.SERVICE_ACK);
        ackData.put("msgId", msgId);
        ackData.put("msg", "已送达");
        WsResponse ackWsResponse = WsResponse.fromText(JSON.toJSONString(ackData), ShowcaseServerConfig.CHARSET);
        Boolean delivered = Tio.sendToUser(channelContext.tioConfig, userId, ackWsResponse);
        log.info("用户{}是否返回送达通知: {}", userId, delivered);
    }
}
