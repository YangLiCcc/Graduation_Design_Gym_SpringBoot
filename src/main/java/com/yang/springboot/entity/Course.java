package com.yang.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("sys_course")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程名
     */
    @TableField("name")
    private String name;

    /**
     * 教练id
     */
    @TableField("couch_id")
    private Long couchId;

    /**
     * 学员id
     */
    @TableField("student_id")
    private Long studentId;

    /**
     * 课程描述
     */
    @TableField("description")
    private String description;

    /**
     * 课程内容
     */
    @TableField("content")
    private String content;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 修改时间
     */
    @TableField("modified_time")
    private LocalDateTime modifiedTime;

    /**
     * 课程状态
     */
    @TableField("status")
    private Boolean status;

    /**
     * 发布者
     */
    @TableField("created_by")
    private Long createdBy;

    /**
     * 修改者
     */
    @TableField("modified_by")
    private Long modifiedBy;


}
