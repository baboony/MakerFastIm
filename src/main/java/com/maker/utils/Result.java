package com.maker.utils;

import lombok.Data;

import java.util.Date;

/**
 * @author 王俊程
 * 全局返回值
 */
@Data
public class Result<T> {

    /**
     * 成功或失败 true or false
     */
    private boolean success;

    /**
     * 返回code
     */
    private Integer code;

    /**
     * 操作结果
     */
    private String msg;

    /**
     * 返回的结果
     */
    private T data;

    /**
     * 操作时间
     */
    private String time;

    public Result() {
    }

    private Result(boolean success, int code, String msg, T data) {
        this.success = success;
        this.code = code;
        this.data = data;
        this.msg = msg;
        time = TimerUtils.format(new Date(), TimerUtils.YYYYMMDDHHMMSS);
    }

    /**
     * 直接返回成功，不携带数据
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> ok() {
        return new Result(true, ResultEnums.SUCCESS.getCode(), ResultEnums.SUCCESS.getMsg(), null);
    }

    /**
     * 直接返回成功 携带数据
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> ok(T data) {
        return new Result(true, ResultEnums.SUCCESS.getCode(), ResultEnums.SUCCESS.getMsg(), data);
    }

    public static <T> Result<T> ok(String msg) {
        return new Result(true, 200, msg, null);
    }

    /**
     * 返回成功，返回的code和msg通过ResultEnums获取
     *
     * @param enums 枚举
     * @param <T>
     * @return
     */
    public static <T> Result<T> ok(ResultEnums enums) {
        return new Result(true, enums.getCode(), enums.getMsg(), null);
    }

    public static <T> Result<T> ok(ResultEnums enums, T data) {
        return new Result(true, enums.getCode(), enums.getMsg(), data);
    }

    /**
     * 和上面的东西差不多，就是返回结果调换了
     *
     * @param <T>
     * @return
     */

    public static <T> Result<T> fail() {
        return new Result(false, ResultEnums.FAIL.getCode(), ResultEnums.FAIL.getMsg(), null);
    }

    public static <T> Result<T> fail(T data) {
        return new Result(false, ResultEnums.FAIL.getCode(), ResultEnums.FAIL.getMsg(), data);
    }

    public static <T> Result<T> fail(String msg) {
        return new Result(false, 401, msg, null);
    }

    public static <T> Result<T> fail(String msg, T data) {
        return new Result(false, 401, msg, data);
    }


    public static <T> Result<T> fail(ResultEnums enums) {
        return new Result(false, enums.getCode(), enums.getMsg(), null);
    }

    public static <T> Result<T> fail(ResultEnums enums, T data) {
        return new Result(false, enums.getCode(), enums.getMsg(), data);
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", time=" + time +
                ", data=" + data +
                '}';
    }
}
