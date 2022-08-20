package com.maker.utils;

/**
 * @author lucky winner
 */

public enum ResultEnums {

    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),
    /**
     * 操作失败
     */
    FAIL(501, "操作失败"),

    /**
     * 内部服务器代码出错
     */
    SYSTEM_ERROR(500, "服务器出错");

    private Integer code;
    private String msg;

    ResultEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
