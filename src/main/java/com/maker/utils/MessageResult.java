package com.maker.utils;

import lombok.Data;

@Data
public class MessageResult {

    private Integer code;
    private String msg;

    private String msgId;

    private Integer type;

    public MessageResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public MessageResult(Integer code, String msg, String msgId, Integer type) {
        this.code = code;
        this.msg = msg;
        this.msgId = msgId;
        this.type = type;
    }

    public static MessageResult success() {
        return new MessageResult(0, "操作成功");
    }

    public static MessageResult success(String msg) {
        return new MessageResult(0, msg);
    }

    public static MessageResult success(String msgId, Integer type) {
        return new MessageResult(0, "操作成功", msgId, type);
    }

    public static MessageResult success(String msg, String msgId, Integer type) {
        return new MessageResult(0, msg, msgId, type);
    }

    public static MessageResult fail() {
        return new MessageResult(1, "操作失败");
    }

    public static MessageResult fail(String msgId, Integer type) {
        return new MessageResult(1, "操作失败", msgId, type);
    }

    public static MessageResult fail(String msg, String msgId, Integer type) {
        return new MessageResult(1, msg, msgId, type);
    }

    public static MessageResult fail(String msg) {
        return new MessageResult(1, msg);
    }
}
