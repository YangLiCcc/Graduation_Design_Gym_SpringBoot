package com.yang.springboot.param;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserInfoParam {

    private String nickname;

    private Boolean sex;

    private Integer age;

    private String phone;

    private String email;

    private String address;

    private String avatarUrl;

    private LocalDateTime createdTime;

    private LocalDateTime expiredTime;

}
