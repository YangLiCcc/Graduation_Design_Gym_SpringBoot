package com.yang.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yang.springboot.entity.User;
import com.yang.springboot.mapper.MenuDao;
import com.yang.springboot.mapper.UserDao;
import com.yang.springboot.param.LoginUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDao userDao;

    private final MenuDao menuDao;

    public UserDetailsServiceImpl(UserDao userDao, MenuDao menuDao) {
        this.userDao = userDao;
        this.menuDao = menuDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查找id
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, username);
        lambdaQueryWrapper.eq(User::getStatus, true);
        User user = userDao.selectOne(lambdaQueryWrapper);
        if (Objects.isNull(user)) {
            throw new RuntimeException("登录失败");
        }

        // 根据用户查询权限信息 添加到LoginUser中
        // List<String> permKey 表示该权限可以访问的接口
        // 主要是后端鉴权使用的
        List<String> permission = menuDao.selectPermissionsByUserRoleId(user.getRoleId());

        return new LoginUser(user, permission);
    }
}
