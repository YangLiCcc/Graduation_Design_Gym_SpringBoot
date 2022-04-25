package com.yang.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author LambCcc
 * @since 2022-04-14
 */
@Data
@TableName("sys_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private String registerVerifyCode;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户权限
     */
    @TableField("role_id")
    private Integer roleId;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 密码
     */
    @TableField("password")
    @JsonIgnore
    private String password;

    /**
     * 年龄
     */
    @TableField("age")
    private Integer age;

    /**
     * 性别
     */
    @TableField("sex")
    private Boolean sex;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 地址
     */
    @TableField("address")
    private String address;

    /**
     * 头像(URL地址)
     */
    @TableField("avatar_url")
    private String avatarUrl;

    /**
     * 用户状态
     */
    @TableField("status")
    private Boolean status;

    /**
     * 注册时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 创建者id
     */
    @TableField("created_by")
    private Long createdBy;

    /**
     * 到期时间
     */
    @TableField("expired_time")
    private LocalDateTime expiredTime;

    /**
     * 是否到期
     */
    @TableField("is_expired")
    private Boolean isExpired;

    /**
     * 修改时间
     */
    @TableField("modified_time")
    private LocalDateTime modifiedTime;

    /**
     * 修改者id
     */
    @TableField("modified_by")
    private Long modifiedBy;

}
