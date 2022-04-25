package com.yang.springboot.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录所需的参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginParam {

    private String username; // 用户名
    private String password; // 密码
    private Integer roleId; // 用户职别(权限)
    private String captchaCode; // 验证码
    private String uuid; // 随机生成的uuid

}
