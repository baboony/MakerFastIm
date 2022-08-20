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
@TableName("t_conversation_info")
public class ConversationInfo implements Serializable {

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
     * 消息ID
     */
    private Long lastMsgId;

    /**
     * 最后消息发送时间
     */
    private Long lastTime;

    /**
     * 会话类型 类型 1-私聊 2-群聊
     */
    private Integer lastType;

    /**
     * 未读数 默认0
     */
    private Integer unreadCount;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


}
