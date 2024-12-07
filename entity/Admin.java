package com.example.system.entity;

import lombok.Data;

@Data
public class Admin {

  private long id;           // 管理员ID
  private String username;   // 账号（用户名）
  private String password;   // 密码
  private String role;       // 角色

  // 获取管理员ID
  public long getId() {
    return id;
  }

  // 设置管理员ID
  public void setId(long id) {
    this.id = id;
  }

  // 获取用户名
  public String getUsername() {
    return username;
  }

  // 设置用户名
  public void setUsername(String username) {
    this.username = username;  // 正确赋值
  }

  // 获取密码
  public String getPassword() {
    return password;
  }

  // 设置密码
  public void setPassword(String password) {
    this.password = password;
  }

  // 获取角色
  public String getRole() {
    return role;
  }

  // 设置角色
  public void setRole(String role) {
    this.role = role;  // 正确赋值
  }
}
