package com.example.system.controller;

import com.example.system.dto.PageDTO;
import com.example.system.dto.UserLoginDTO;
import com.example.system.dto.UserRegisterDTO;
import com.example.system.entity.Admin;
import com.example.system.entity.User;
import com.example.system.mapper.UserMapper;
import com.example.system.service.UserService;
import com.example.system.result.Result;
import com.example.system.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:63443")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:63342")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户登录：{}", userLoginDTO);

        // 使用 UserLoginDTO 调用 UserService 中的 login 方法
        User user = userService.login(userLoginDTO);

        // 判断是否找到了对应的用户
        if (user == null) {
            return Result.failure("用户不存在或密码错误");
        }

        // 返回包含 user.id 的 UserLoginVO 对象
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())  // 使用 long 类型的 id
                .build();

        return Result.success(userLoginVO);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @CrossOrigin(origins = "http://localhost:63342")
    public Result<String> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("用户注册：{}", userRegisterDTO);  // 打印接收到的 DTO，检查是否正确
        if (userRegisterDTO == null) {
            log.error("接收到的 UserRegisterDTO 为空");
        } else {
            log.info("接收到的 UserRegisterDTO: {}", userRegisterDTO);
        }

        // 校验用户名是否已经存在
        if (userService.existsByUsername(userRegisterDTO.getUsername())) {
            return Result.failure("用户名已存在");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setPassword(userRegisterDTO.getPassword());
        user.setRole("resident");  // 默认角色为住户
        user.setGender(userRegisterDTO.getGender());  // 确保设置性别
        user.setPhone(userRegisterDTO.getPhone());  // 确保设置电话
        user.setAddress(userRegisterDTO.getAddress());  // 确保设置地址


        // 打印即将注册的用户信息，确保值正确
        log.info("即将注册的用户信息：{}", user);

        // 注册用户
        userService.register(user);

        // 注册成功，返回响应给前端
        return Result.success("注册成功");
    }



    /**
     * 用户分页查询
     */
    // 分页查询用户
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getUsersPage(PageDTO pageDTO) {
        // 用于保存返回的调试信息
        Map<String, Object> response = new HashMap<>();

        try {
            // 1. 输出前端传入的分页参数
            System.out.println("前端传入的参数：");
            System.out.println("currentPage = " + pageDTO.getCurrentPage());
            System.out.println("pageSize = " + pageDTO.getPageSize());

            // 2. 确保分页参数有效
            if (pageDTO.getCurrentPage() <= 0) {
                pageDTO.setCurrentPage(1);  // 默认页码为 1
                System.out.println("currentPage 参数无效，已设置为默认值 1");
            }
            if (pageDTO.getPageSize() <= 0) {
                pageDTO.setPageSize(5);  // 默认每页记录数为 5
                System.out.println("pageSize 参数无效，已设置为默认值 5");
            }

            // 3. 输出分页查询请求的参数
            System.out.println("分页查询请求参数: currentPage = " + pageDTO.getCurrentPage() + ", pageSize = " + pageDTO.getPageSize());

            // 4. 调用分页查询方法
            List<User> users = userMapper.pageQuery(pageDTO);

            // 5. 输出查询到的用户数据
            if (users != null && !users.isEmpty()) {
                System.out.println("查询到的用户数据: " + users);
                response.put("status", "success");
                response.put("users", users);  // 返回查询结果
            } else {
                System.out.println("没有查询到用户数据！");
                response.put("status", "fail");
                response.put("message", "没有查询到用户数据");
            }

        } catch (Exception e) {
            // 6. 异常处理
            System.out.println("发生异常：" + e.getMessage());
            response.put("status", "error");
            response.put("message", "服务器内部错误");
        }

        // 7. 输出返回的响应数据
        System.out.println("返回给前端的数据: " + response);

        // 8. 返回分页结果和调试信息
        return ResponseEntity.ok(response);
    }


    /**
     * 用户名模糊查询
     */
    @GetMapping("/name")
    @CrossOrigin(origins = "http://localhost:63342")
    public Result<List<Admin>> selectByName(@RequestParam String name) {
        log.info("用户姓名模糊查询{}", name);
        List<Admin> users = userService.selectByName(name);
        return Result.success(users);
    }

    /**
     * 新增用户
     */


    /**
     * 更新用户信息
     */
    @PutMapping("/update/{id}")
    @CrossOrigin(origins = "http://localhost:63342")
    public Result update(@PathVariable Long id, @RequestBody User user) {
        log.info("更新用户信息: {}", user);

        // 查找用户
        User existingUser = userService.selectById(id);
        if (existingUser == null) {
            return Result.failure("用户不存在");
        }

        // 验证角色值（仅允许 'resident' 或 'buyer'）
        String newRole = user.getRole();
        if (newRole != null && !newRole.isEmpty()) {
            if (!newRole.equals("resident") && !newRole.equals("buyer")) {
                return Result.failure("无效的角色值，必须为 'resident' 或 'buyer'");
            }
        }

        // 逐个字段检查并更新
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            existingUser.setUsername(user.getUsername());  // 更新用户名
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(user.getPassword());  // 更新密码（加密处理需要在此处进行）
        }
        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            existingUser.setPhone(user.getPhone());  // 更新电话
        }
        if (user.getAddress() != null && !user.getAddress().isEmpty()) {
            existingUser.setAddress(user.getAddress());  // 更新住址
        }
        if (newRole != null && !newRole.isEmpty()) {
            existingUser.setRole(newRole);  // 更新角色
        }

        // 执行更新操作
        boolean updateSuccess = userService.update(existingUser);
        if (updateSuccess) {
            return Result.success("用户信息更新成功");
        } else {
            return Result.failure("更新失败，请稍后再试");
        }
    }


    /**
     * 删除用户
     */
    @DeleteMapping("/delete/{id}")
    @CrossOrigin(origins = "http://localhost:63342")
    public Result delete(@PathVariable Long id) {
        log.info("删除用户信息{}", id);

        try {
            // 删除用户
            boolean isDeleted = userService.deleteById(id);
            if (isDeleted) {
                return Result.success("用户删除成功");
            } else {
                return Result.failure("用户删除失败，用户不存在");
            }
        } catch (RuntimeException e) {
            // 异常处理，返回错误消息
            return Result.failure("删除失败: " + e.getMessage());
        }
    }

}
