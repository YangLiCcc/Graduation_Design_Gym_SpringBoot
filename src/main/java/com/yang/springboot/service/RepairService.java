package com.yang.springboot.service;

import com.yang.springboot.common.lang.Result;
import com.yang.springboot.entity.Repair;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LambCcc
 * @since 2022-04-14
 */
public interface RepairService extends IService<Repair> {

    Result getRepairPage(Integer currentPage, Integer pageSize, String type, String name);

    Result insertRecord(Repair repair);

    Result updateRecordById(Repair repair);

}
