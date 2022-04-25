package com.yang.springboot.controller;

import com.yang.springboot.common.lang.Result;
import com.yang.springboot.entity.User;
import com.yang.springboot.param.LoginParam;
import com.yang.springboot.param.RegisterParam;
import com.yang.springboot.param.UserInfoParam;
import com.yang.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LambCcc
 * @since 2022-04-14
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result userLogin(@RequestBody LoginParam loginParam, @CookieValue("captchaCode") String uuid) {
        loginParam.setUuid(uuid);
        return userService.login(loginParam);
    }

    @PostMapping("/register")
    public Result userRegister(@RequestBody RegisterParam registerParam, @CookieValue("captchaCode") String uuid) {
        registerParam.setUuid(uuid);
        return userService.register(registerParam);
    }

    @PostMapping("/logout")
    public Result userLogout() {
        return userService.logout();
    }

    @GetMapping("/info/get")
    public Result getUserInfo() {
        return userService.getUserInfo();
    }

    @PostMapping("/info/update")
    public Result updateUserInfoById(@RequestBody UserInfoParam userInfoParam, @RequestHeader String token) {
        return userService.updateUserInfoById(userInfoParam, token);
    }

    @GetMapping("/get/page/{type}")
    public Result getUserPage(@PathVariable String type,
                              @RequestParam Integer currentPage,
                              @RequestParam Integer pageSize,
                              @RequestParam(defaultValue = "") String username,
                              @RequestParam(defaultValue = "") String nickname,
                              @RequestParam(defaultValue = "") String phone) {
        return userService.getUserPage(type, currentPage, pageSize, username, nickname, phone);
    }

    @PostMapping("/insert")
    public Result insertUser(@RequestBody User user) {
        return userService.insertUser(user);
    }

    @PostMapping("/delete/{id}")
    public Result deleteUserById(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }

    @PostMapping("/delete/batch/{ids}")
    public Result deleteUserByIds(@PathVariable List<Long> ids) {
        return userService.deleteBatchByUserIds(ids);
    }

    @PostMapping("/info/change/status")
    public Result changeUserStatus(Long id, Boolean status) {
        return userService.changeUserStatus(id, status);
    }

    @PostMapping("/info/change/password")
    public Result changeUserPassword(Long id, String password) {
        return userService.changeUserPassword(id, password);
    }

}
