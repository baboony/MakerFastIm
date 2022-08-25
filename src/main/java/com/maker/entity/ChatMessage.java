package com.maker.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author 王俊程
 * @since 2022-08-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_chat_message")
public class ChatMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 雪花ID
     */
      @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 类型：0-文字 1-图片 2-视频 3-卡片数据 4-订单数据   101-送达消息 102-已读消息 103-心跳 104-客户端ACK 105-服务端ACK 106-已读回应
     */
    private Integer type;

    /**
     * 发送者ID
     */
    private String come;

    /**
     * 接收者ID（私聊必填）
     */
    private String go;

    /**
     * 群聊ID（群聊必填）
     */
    private String groupGo;

    /**
     * 类型 0-私聊 1-群聊
     */
    private Integer mode;

    /**
     * 消息内容 （文字消息）
     */
    private String msg;

    /**
     * JSON字符串 除了文字消息，其他数据都会装入到该参数中
     */
    private String msgData;

    /**
     * 消息ID  由前端生成UUID 保证唯一值
     */
    private String msgId;

    /**
     * 发送时间  由前端获取当前时间戳
     */
    private Long msgTime;

    /**
     * 消息状态 0-失败 1-已送达未读 2-已送达已读  3-离线消息
     */
    private Integer msgStatus;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


}
