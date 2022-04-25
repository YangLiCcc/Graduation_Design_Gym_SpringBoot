package com.yang.springboot.param;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LockerSaveParam {

    private Long id;

    private String name;

    private Boolean sex;

    private String area;

    private LocalDateTime createdTime;

    private LocalDateTime repairTime;

    private LocalDateTime deletedTime;

    private LocalDateTime modifiedTime;

    private Boolean status;

}
