package com.yang.springboot.service;

import com.yang.springboot.common.lang.Result;
import com.yang.springboot.entity.Locker;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.springboot.param.LockerSaveParam;
import com.yang.springboot.param.LockerUseParam;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LambCcc
 * @since 2022-04-14
 */
public interface LockerService extends IService<Locker> {

    Result getLockerPage(Integer currentPage, Integer pageSize, String name, String sex, String area);

    Result insertLocker(LockerSaveParam lockerSaveParam);

    Result deleteLockerById(Long id);

    Result deleteLockerBatchByIds(List<Long> ids);

    Result updateLocker(LockerSaveParam lockerSaveParam);

    Result useLockerByUserId(LockerUseParam lockerUseParam);

    Result getUnusedLockerList();
}
