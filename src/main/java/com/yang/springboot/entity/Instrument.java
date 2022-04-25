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
@TableName("sys_instrument")
public class Instrument implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 器械类型id
     */
    @TableField("type_id")
    private Long typeId;

    /**
     * 器械名称
     */
    @TableField("name")
    private String name;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    @TableField("created_by")
    private Long createdBy;

    /**
     * 维修时间
     */
    @TableField("repair_time")
    private LocalDateTime repairTime;

    /**
     * 删除时间
     */
    @TableField("deleted_time")
    private LocalDateTime deletedTime;

    @TableField("deleted_by")
    private Long deletedBy;

    @TableField("is_deleted")
    private Boolean isDeleted;

    /**
     * 修改时间
     */
    @TableField("modified_time")
    private LocalDateTime modifiedTime;

    @TableField("modified_by")
    private Long modifiedBy;

    /**
     * 器械状态
     */
    @TableField("status")
    private Boolean status;

    /**
     * 借用者id
     */
    @TableField("used_by")
    private Long usedBy;

    /**
     * 借用时间
     */
    @TableField("use_time")
    private LocalDateTime useTime;

    /**
     * 归还时间
     */
    @TableField("return_time")
    private LocalDateTime returnTime;

    /**
     * 申请维修人员id
     */
    @TableField("submit_repair_by")
    private Long submitRepairBy;

    /**
     * 维修人员id
     */
    @TableField("repair_by")
    private Long repairBy;


}
