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
 * @since 2022-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_message_info")
public class MessageInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 雪花ID
     */
      @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 发送人ID
     */
    private String sendId;

    /**
     * 接收人ID
     */
    private String formId;

    /**
     * 群ID
     */
    private Long groupId;

    /**
     * 类型 1-私聊 2-群聊
     */
    private Integer type;

    /**
     * 发送内容
     */
    private String content;

    /**
     * 消息ID
     */
    private String msgId;

    /**
     * 时间戳
     */
    private Long msgTime;

    /**
     * 发送状态 0-失败 1-离线 2-成功
     */
    private Integer msgStatus;

    /**
     * 读取状态 0-未读  1-已读
     */
    private Integer viewStatus;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


}
