package com.maker.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.maker.Const;
import com.maker.ShowcaseServerConfig;
import com.maker.entity.UserInfo;
import com.maker.service.UserInfoService;
import com.maker.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tio.common.starter.annotation.TioServerMsgHandler;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.TioConfig;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.common.WsSessionContext;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@TioServerMsgHandler
@Component
@Slf4j
public class ShowcaseWsMsgHandler implements IWsMsgHandler {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserInfoService userInfoService;

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
        if (one != null){
            Tio.bindUser(channelContext, one.getUserId());
            log.info("绑定用户: {}",one);
            Tio.bindGroup(channelContext, Const.GROUP_ID);
        }else {
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
            JSONObject object = new JSONObject();
            object.put("code", 501);
            object.put("msg", "消息格式错误，非JSON字符串");
            return JSON.toJSONString(object);
        }

        //消息类型
        Integer type = messageInfo.getInteger("type");
        //如果是心跳
        if (type == 1) {
            return null;
        }

        //发送人
        String userId = channelContext.userid;
        //接收人
        String formId = messageInfo.getString("formId");
        //接收人
        String msgId = messageInfo.getString("msgId");
        //客户端收到消息返回通知
        if (type == 201) {
            log.info("用户{}收到消息了 消息ID: {}", userId, msgId);
            //TODO 客户端收到消息返回ack  确保客户端收到消息了
            for (JSONObject object : undelivered) {
                String messageId = object.getString("msgId");
                if (Objects.equals(messageId, msgId)){
                    undelivered.remove(object);
                }

            }
            return null;
        }

        //送达通知
        noticeOfDelivery(channelContext, userId, msgId);
        switch (type){
            case 2:
                WsResponse wsResponse = WsResponse.fromText(JSON.toJSONString(messageInfo), ShowcaseServerConfig.CHARSET);
                Boolean send = Tio.sendToUser(channelContext.tioConfig, formId, wsResponse);
                if (!send){
                    JSONObject object = new JSONObject();
                    object.put("tioConfig", channelContext.tioConfig);
                    object.put("wsResponse", wsResponse);
                    object.put("formId", formId);
                    object.put("msgId", msgId);
                    undelivered.add(object);
                }
                break;
            case 3:
                break;
            default:
                JSONObject object = new JSONObject();
                object.put("code", 501);
                object.put("msg", "type字段不正确");
                return JSON.toJSONString(object);
        }
        return null;
    }

    /**
     * 送达通知，（服务器收到该消息返回结果）  如果没有返回客户端会一直发来问
     * @param channelContext
     * @param userId
     * @param msgId
     */
    private void noticeOfDelivery(ChannelContext channelContext, String userId, String msgId) {
        //回执消息
        JSONObject ackData = new JSONObject();
        ackData.put("code", 0);
        ackData.put("type", 101);
        ackData.put("msgId", msgId);
        ackData.put("msg", "已送达");
        WsResponse ackWsResponse = WsResponse.fromText(JSON.toJSONString(ackData), ShowcaseServerConfig.CHARSET);
        Boolean delivered = Tio.sendToUser(channelContext.tioConfig, userId, ackWsResponse);
        log.info("用户{}是否返回送达通知: {}",userId, delivered);
    }

    @Scheduled(cron = "0/5 * * * * ?")
    private void configureTasks() {
        for (JSONObject object : undelivered) {
            TioConfig tioConfig = object.getObject("tioConfig", TioConfig.class);
            WsResponse wsResponse = object.getObject("wsResponse", WsResponse.class);
            String formId = object.getString("formId");
            log.info("{}未送达", formId);
            Boolean send = Tio.sendToUser(tioConfig, formId, wsResponse);
            log.info("用户{}重发消息是否成功 {}", formId, send);
        }
    }
}
