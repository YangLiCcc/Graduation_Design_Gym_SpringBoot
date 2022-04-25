package com.yang.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.springboot.common.Constants;
import com.yang.springboot.common.lang.Result;
import com.yang.springboot.entity.Locker;
import com.yang.springboot.entity.User;
import com.yang.springboot.mapper.LockerDao;
import com.yang.springboot.param.LockerSaveParam;
import com.yang.springboot.param.LockerUseParam;
import com.yang.springboot.service.LockerService;
import com.yang.springboot.utils.RedisCache;
import com.yang.springboot.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LambCcc
 * @since 2022-04-14
 */
@Service
public class LockerServiceImpl extends ServiceImpl<LockerDao, Locker> implements LockerService {

    private final RedisCache redisCache;

    @Autowired
    public LockerServiceImpl(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Override
    public Result getLockerPage(Integer currentPage, Integer pageSize, String name, String sex, String area) {
        Page<Locker> page = new Page<>(currentPage, pageSize);
        // 根据查询条件进行分页查询
        LambdaQueryWrapper<Locker> lockerLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 判断条件是否为空 若不为空则进行条件拼接
        if (StrUtil.isNotEmpty(name)) {
            lockerLambdaQueryWrapper.like(Locker::getName, name);
        }
        if (StrUtil.isNotEmpty(sex)) {
            Boolean sexFlag = sex.equals("男");
            lockerLambdaQueryWrapper.eq(Locker::getSex, sexFlag);
        }
        if (StrUtil.isNotEmpty(area)) {
            lockerLambdaQueryWrapper.like(Locker::getArea, area);
        }
        // 将查询到的数据存入Redis
        redisCache.setCacheObject("LockerList: ", list(), 1, TimeUnit.DAYS);

        return new Result(Constants.CODE_200, "获取分页数据成功!", page(page, lockerLambdaQueryWrapper));
    }

    @Override
    public Result insertLocker(LockerSaveParam lockerSaveParam) {
        Locker locker = new Locker();
        BeanUtil.copyProperties(lockerSaveParam, locker);
        locker.setCreatedTime(LocalDateTime.now());
        if (save(locker)) {
            return new Result(Constants.CODE_200, "新建存储柜成功!", null);
        }

        return new Result(Constants.CODE_500, "新建存储柜失败!", null);
    }

    @Override
    public Result deleteLockerById(Long id) {
        Locker locker = getById(id);
        locker.setDeletedTime(LocalDateTime.now());
        // 假删除 更改删除状态为true 即已删除
        locker.setIsDeleted(true);
        locker.setStatus(false);
        if (updateById(locker)) {
            return new Result(Constants.CODE_200, "删除存储柜成功!", null);
        }

        return new Result(Constants.CODE_500, "删除存储柜失败!", null);
    }

    // 未完成
    @Override
    public Result deleteLockerBatchByIds(List<Long> ids) {
        LambdaQueryWrapper<Locker> lockerLambdaQueryWrapper = new LambdaQueryWrapper<>();
        lockerLambdaQueryWrapper.eq(Locker::getId, ids);
        Map<String, Object> map = getMap(lockerLambdaQueryWrapper);

        // updateBatchById()
        return new Result(Constants.CODE_500, "批量删除失败!", map);
    }

    @Override
    public Result updateLocker(LockerSaveParam lockerSaveParam) {
        Locker locker = getById(lockerSaveParam.getId());
        BeanUtil.copyProperties(lockerSaveParam, locker);
        // 获取当前操作用户的id
        User user = UserUtils.getUserInfo();
        locker.setModifiedTime(lockerSaveParam.getModifiedTime());
        locker.setModifiedBy(user.getId());
        if (updateById(locker)) {
            return new Result(Constants.CODE_200, "更新存储柜信息成功!", null);
        }

        return new Result(Constants.CODE_500, "更新存储柜信息失败!", null);
    }

    @Override
    public Result useLockerByUserId(LockerUseParam lockerUseParam) {

        Locker locker = getById(lockerUseParam.getId());
        locker.setUsedBy(lockerUseParam.getUserId());
        locker.setUseTime(lockerUseParam.getUseTime());
        locker.setReturnTime(lockerUseParam.getReturnTime());
        locker.setStatus(false);
        if (updateById(locker)) {
            return new Result(Constants.CODE_200, "借用储物柜成功!请按时归还!", null);
        }

        return new Result(Constants.CODE_500, "借用储物柜失败!请稍后再试!", null);
    }

    @Override
    public Result getUnusedLockerList() {
        // 获取查询用户的性别 根据用户性别查询相应存储柜
        User user = UserUtils.getUserInfo();
        LambdaQueryWrapper<Locker> lockerLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 查询条件 status为true 即可用状态
        lockerLambdaQueryWrapper.eq(Locker::getStatus, true);
        // 查询条件 sex为user的sex
        lockerLambdaQueryWrapper.eq(Locker::getSex, user.getSex());

        return new Result(Constants.CODE_200, "获取可用存储柜成功", list(lockerLambdaQueryWrapper));
    }

}
