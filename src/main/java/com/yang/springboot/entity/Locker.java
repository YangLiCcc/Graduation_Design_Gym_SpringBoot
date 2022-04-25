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
@TableName("sys_locker")
public class Locker implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 储物柜名
     */
    @TableField("name")
    private String name;

    /**
     * 性别
     */
    @TableField("sex")
    private Boolean sex;

    /**
     * 所在区域
     */
    @TableField("area")
    private String area;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

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

    @TableField("is_deleted")
    private Boolean isDeleted;

    /**
     * 储物柜状态
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

    @TableField("modified_time")
    private LocalDateTime modifiedTime;

    @TableField("modified_by")
    private Long modifiedBy;


}
