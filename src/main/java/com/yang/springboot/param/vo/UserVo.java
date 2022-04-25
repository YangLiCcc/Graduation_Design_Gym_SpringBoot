package com.yang.springboot.param.vo;

import com.yang.springboot.entity.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVo {

    private Long id;

    private String username;

    private String nickname;

    private Boolean sex;

    private String avatarUrl;

    private LocalDateTime createdTime;

    private LocalDateTime expiredTime;

    private Boolean status;

    private Boolean isExpired;

}
