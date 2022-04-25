package com.yang.springboot.utils;

import com.yang.springboot.entity.User;
import com.yang.springboot.param.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {

    public static User getUserInfo() {
        // 从SecurityContextHolder中获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 获取用户的基本信息
        return loginUser.getUser();
    }

}
