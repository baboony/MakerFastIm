package com.maker.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 王俊程
 * @since 2022-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 雪花ID
     */
      @TableId(value = "id", type = IdType.ASSIGN_ID)
      @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 用户ID（必须唯一）
     */
    private String userId;

    /**
     * 用户名
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String faceUrl;

    /**
     * 用户扩展类 使用JSON字符串
     */
    private String ex;

    /**
     * 0-黑名单 1-正常
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
