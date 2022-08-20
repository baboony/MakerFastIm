package com.maker.utils;


import lombok.Data;

/**
 * @author lucky winner
 * 消息实现类
 */

public class MessageType {

    /**
     * 心跳码
     */
    public final static Integer HEARTBEAT_CODE = 1;

    /**
     * 客户端收到消息返回ACK码
     */
    public final static Integer CLIENTELE_ACK = 201;

    /**
     * 服务端返回给客户端发送消息成功回执码
     */
    public final static Integer SERVICE_ACK = 202;

}
