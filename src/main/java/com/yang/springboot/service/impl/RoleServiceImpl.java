package com.yang.springboot.service.impl;

import com.yang.springboot.common.Constants;
import com.yang.springboot.common.lang.Result;
import com.yang.springboot.entity.Role;
import com.yang.springboot.mapper.RoleDao;
import com.yang.springboot.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.springboot.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {

    private final RoleDao roleDao;

    private final RedisCache redisCache;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao, RedisCache redisCache) {
        this.roleDao = roleDao;
        this.redisCache = redisCache;
    }

    @Override
    public Result getRoleList() {
        List<Role> list = list();
        if (Objects.isNull(list)) {
            return new Result(Constants.CODE_500, "获取权限列表失败!", null);
        }
        redisCache.setCacheObject("RoleList:", list, 1, TimeUnit.HOURS);
        return new Result(Constants.CODE_200, "获取权限列表成功!", list);
    }
}
