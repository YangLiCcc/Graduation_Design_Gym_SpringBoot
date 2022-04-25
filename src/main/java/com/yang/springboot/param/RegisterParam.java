package com.yang.springboot.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterParam {

    private Integer roleId;

    private String username;

    private String nickname;

    private String password;

    private Boolean sex;

    private Integer age;

    private String phone;

    private String email;

    private String address;

    private String verifyCode;

    private String uuid;

    private String captchaCode;

}
