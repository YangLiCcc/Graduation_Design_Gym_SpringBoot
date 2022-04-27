package com.yang.springboot.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.springboot.common.Constants;
import com.yang.springboot.common.lang.Result;
import com.yang.springboot.entity.Repair;
import com.yang.springboot.entity.User;
import com.yang.springboot.mapper.RepairDao;
import com.yang.springboot.service.RepairService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.springboot.utils.UserUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LambCcc
 * @since 2022-04-14
 */
@Service
public class RepairServiceImpl extends ServiceImpl<RepairDao, Repair> implements RepairService {

    @Override
    public Result getRepairPage(Integer currentPage, Integer pageSize, String type, String name) {
        Page<Repair> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Repair> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(type)) {
            lambdaQueryWrapper.like(Repair::getType, type);
        }
        if (StrUtil.isNotEmpty(name)) {
            lambdaQueryWrapper.like(Repair::getName, name);
        }

        return new Result(Constants.CODE_200, "获取分页信息成功!", page(page, lambdaQueryWrapper));
    }

    @Override
    public Result insertRecord(Repair repair) {
        User user = UserUtils.getUserInfo();
        repair.setSubmitBy(user.getId());
        repair.setSubmitTime(LocalDateTime.now());
        if (save(repair)) {
            return new Result(Constants.CODE_200, "新增维修记录成功!", null);
        }

        return new Result(Constants.CODE_500, "新增维修记录失败!", null);
    }

    @Override
    public Result updateRecordById(Repair repair) {
        User user = UserUtils.getUserInfo();
        repair.setRepairBy(user.getId());
        if (updateById(repair)) {
            return new Result(Constants.CODE_200, "更新维修记录成功!", null);
        }

        return new Result(Constants.CODE_500, "更新维修记录失败!", null);
    }


}
