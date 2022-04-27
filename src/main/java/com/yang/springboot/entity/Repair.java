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
@TableName("sys_repair")
public class Repair implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 维修物品名称
     */
    @TableField("name")
    private String name;

    /**
     * 维修物品类型
     */
    @TableField("type")
    private String type;

    /**
     * 提交时间
     */
    @TableField("submit_time")
    private LocalDateTime submitTime;

    /**
     * 申请维修人员
     */
    @TableField("submit_by")
    private Long submitBy;

    /**
     * 维修状态
     */
    @TableField("status")
    private Boolean status;

    /**
     * 维修时间
     */
    @TableField("repair_time")
    private LocalDateTime repairTime;

    /**
     * 维修结束时间
     */
    @TableField("repair_done_time")
    private LocalDateTime repairDoneTime;

    /**
     * 维修人员
     */
    @TableField("repair_by")
    private Long repairBy;


}
