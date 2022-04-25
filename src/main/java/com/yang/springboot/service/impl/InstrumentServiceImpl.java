package com.yang.springboot.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yang.springboot.common.Constants;
import com.yang.springboot.common.lang.Result;
import com.yang.springboot.entity.Instrument;
import com.yang.springboot.entity.User;
import com.yang.springboot.mapper.InstrumentDao;
import com.yang.springboot.service.InstrumentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.springboot.utils.RedisCache;
import com.yang.springboot.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LambCcc
 * @since 2022-04-14
 */
@Service
public class InstrumentServiceImpl extends ServiceImpl<InstrumentDao, Instrument> implements InstrumentService {

    private final InstrumentDao instrumentDao;

    private final RedisCache redisCache;

    @Autowired
    public InstrumentServiceImpl(InstrumentDao dao, RedisCache redisCache) {
        this.instrumentDao = dao;
        this.redisCache = redisCache;
    }


    @Override
    public Result getInstrumentPage(Integer currentPage, Integer pageSize, String name) {
        Page<Instrument> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Instrument> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(name)) {
            lambdaQueryWrapper.like(Instrument::getName, name);
        }

        return new Result(Constants.CODE_200, "获取分页信息成功!", page(page, lambdaQueryWrapper));
    }

    @Override
    public Result getUnusedInstrument() {
        LambdaQueryWrapper<Instrument> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Instrument::getStatus, true);

        return new Result(Constants.CODE_200, "获取可用器械信息成功!", list(lambdaQueryWrapper));
    }

    @Override
    public Result insertInstrument(Instrument instrument) {
        // 获取当前操作用户id
        User user = UserUtils.getUserInfo();
        instrument.setCreatedBy(user.getId());
        instrument.setCreatedTime(LocalDateTime.now());
        if (save(instrument)) {
            return new Result(Constants.CODE_200, "新增器械成功!", null);
        }

        return new Result(Constants.CODE_500, "新增器械失败!", null);
    }

    @Override
    public Result updateInstrument(Instrument instrument) {
        // 获取当前操作用户id
        User user = UserUtils.getUserInfo();
        instrument.setModifiedBy(user.getId());
        instrument.setModifiedTime(LocalDateTime.now());
        if (updateById(instrument)) {
            return new Result(Constants.CODE_200, "修改器械成功!", null);
        }

        return new Result(Constants.CODE_500, "修改器械失败!", null);
    }

    @Override
    public Result deleteInstrumentById(Long id) {
        // 获取当前操作用户id
        User user = UserUtils.getUserInfo();
        Instrument instrument = getById(id);
        instrument.setDeletedBy(user.getId());
        instrument.setDeletedTime(LocalDateTime.now());
        instrument.setIsDeleted(true);
        instrument.setStatus(false);
        if (updateById(instrument)) {
            return new Result(Constants.CODE_200, "删除器械成功!", null);
        }

        return new Result(Constants.CODE_500, "删除器械失败!", null);
    }

    @Override
    public Result deleteInstrumentBatchByIds(List<Long> ids) {
        return null;
    }

}
