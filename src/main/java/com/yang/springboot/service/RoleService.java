package com.yang.springboot.service;

import com.yang.springboot.common.lang.Result;
import com.yang.springboot.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LambCcc
 * @since 2022-04-14
 */
public interface RoleService extends IService<Role> {

    Result getRoleList();
}
