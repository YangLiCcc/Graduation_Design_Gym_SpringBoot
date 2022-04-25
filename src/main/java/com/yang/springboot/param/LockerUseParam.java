package com.yang.springboot.param;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LockerUseParam {

    private Long id;

    private Long userId;

    private LocalDateTime useTime;

    private LocalDateTime returnTime;

}
