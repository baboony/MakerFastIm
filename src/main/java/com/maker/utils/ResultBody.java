package com.maker.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Date;

/**
 * @author lucky winner
 */
@Data
public class ResultBody {


    /**
     * 类型：
     *  1.心跳
     *  2.私聊
     *  3.群聊
     *  4.图片
     *  5.视频
     *  6.其他（自定义）
     *
     *  101 拉取私聊消息（默认十条）
     */
    private Integer type;

    /**
     * 用户ID 全局唯一
     */
    private String userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String faceUrl;

    /**
     * 用户信息（其他信息扩展类）
     */
    private String userInfoEx;

    /**
     * 接收人(用户ID或组ID)
     * 如果type=2 那么会发送到私聊
     * 如果type=3 那么会发送到群聊
     */
    private String formId;

    /**
     * 具体内容  JSON字符串
     */
    private String content;


    /**
     * 发送时间
     */
    private Date createTime;



}
