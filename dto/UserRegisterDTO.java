package com.example.system.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRegisterDTO {

    private String username;  // 用户名
    private String password;  // 密码
    private String gender;    // 性别
    private String phone;     // 电话
    private String address;   // 地址
    private String role;      // 角色
    private LocalDateTime createdAt;  // 创建时间
}
