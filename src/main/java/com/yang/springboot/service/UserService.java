package com.yang.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yang.springboot.common.lang.Result;
import com.yang.springboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yang.springboot.param.LoginParam;
import com.yang.springboot.param.RegisterParam;
import com.yang.springboot.param.UserInfoParam;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LambCcc
 * @since 2022-04-14
 */
public interface UserService extends IService<User> {

    Result getUserPage(String type, Integer currentPage, Integer pageSize, String username, String nickname, String phone);

    Result deleteUserById(Long id);

    Result deleteBatchByUserIds(List<Long> ids);

    Result login(LoginParam loginParam);

    Result logout();

    Result getUserInfo();

    Result updateUserInfoById(UserInfoParam userInfoParam, String token);

    Result insertUser(User user);

    Result changeUserStatus(Long id, Boolean status);

    Result changeUserPassword(Long id, String password);

    Result register(RegisterParam registerParam);

    List<Long> listUserIds(LambdaQueryWrapper<User> lambdaQueryWrapper);
}
