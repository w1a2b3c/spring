package com.example.system.dto;

import lombok.Data;

@Data
public class UserLoginDTO {

    private long userId;          // 用户ID
    private String username;      // 用户名
    private String password;      // 密码
    private String gender;        // 性别
    private String phone;         // 联系电话
    private String address;       // 住址
    private String role;          // 用户角色（住户 / 买家等）
    private String createdAt;     // 创建时间
    private String updatedAt;     // 更新时间

    public String getAccount() {
        return username;
    }

}
