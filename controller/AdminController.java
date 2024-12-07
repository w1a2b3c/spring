package com.example.system.controller;

import com.example.system.dto.AdminLoginDTO;
import com.example.system.dto.AdminRegisterDTO;
import com.example.system.dto.PageDTO;
import com.example.system.entity.Admin;
import com.example.system.mapper.AdminMapper;
import com.example.system.result.Result;
import com.example.system.service.AdminService;
import com.example.system.service.AnnouncementService;
import com.example.system.vo.AdminLoginVO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:63442")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // 注入 AdminMapper
    @Autowired
    private AdminMapper adminMapper;

    /**
     * 登录
     */
    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:63342")
    public Result<AdminLoginVO> login(@RequestBody AdminLoginDTO adminLoginDTO) {
        log.info("管理员登录：{}", adminLoginDTO);

        // 调用登录服务进行认证
        Admin admin = adminService.login(adminLoginDTO);

        // 判断是否找到了对应的管理员
        if (admin == null) {
            // 如果没有找到对应的管理员，返回 "用户不存在"
            return Result.failure("用户不存在");
        }

        // 直接返回登录的管理员信息，无需生成 JWT
        AdminLoginVO adminLoginVO = AdminLoginVO.builder()
                .id((int) admin.getId())
                .build();

        // 返回成功结果
        return Result.success(adminLoginVO);
    }

    /**
     * 管理员注册
     */
    @PostMapping("/register")
    @CrossOrigin(origins = "http://localhost:63342")
    public Result<String> register(@RequestBody AdminRegisterDTO adminRegisterDTO) {
        log.info("管理员注册：{}", adminRegisterDTO);  // 打印接收到的 DTO，检查是否正确
        if (adminRegisterDTO == null) {
            log.error("接收到的 AdminRegisterDTO 为空");
        } else {
            log.info("接收到的 AdminRegisterDTO: {}", adminRegisterDTO);
        }

        // 校验用户名是否已经存在
        if (adminService.existsByUsername(adminRegisterDTO.getUsername())) {
            return Result.failure("用户名已存在");
        }

        // 创建新管理员
        Admin admin = new Admin();
        admin.setUsername(adminRegisterDTO.getUsername());  // 赋值用户名
        admin.setPassword(adminRegisterDTO.getPassword());  // 请注意密码加密的处理
        admin.setRole("admin");  // 默认角色为管理员

        // 打印即将注册的管理员信息，确保值正确
        log.info("即将注册的管理员信息：{}", admin);

        // 注册管理员
        adminService.register(admin);

        // 注册成功，返回响应给前端
        return Result.success("注册成功");
    }

    // 分页查询管理员
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getAdminsPage(PageDTO pageDTO) {
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
            List<Admin> admins = adminMapper.pageQuery(pageDTO);

            // 5. 输出查询到的管理员数据
            if (admins != null && !admins.isEmpty()) {
                System.out.println("查询到的管理员数据: " + admins);
                response.put("status", "success");
                response.put("admins", admins);  // 返回查询结果
            } else {
                System.out.println("没有查询到管理员数据！");
                response.put("status", "fail");
                response.put("message", "没有查询到管理员数据");
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

    @GetMapping("/name")
    @CrossOrigin(origins = "http://localhost:63342")
    public Result<List<Admin>> selectByName(String name) {
        log.info("管理姓名模糊查询: {}", name);
        try {
            List<Admin> admins = adminService.selectByName(name); // 确保返回的是 List<Admin>
            return Result.success(admins);
        } catch (Exception e) {
            log.error("查询管理员失败", e);
            return Result.failure("查询失败");  // 返回失败信息
        }
    }

    @PostMapping("/insert")
    @CrossOrigin(origins = "http://localhost:63342")
    public String insert(@RequestBody Admin admin) {
        log.info("增加管理员信息{}", admin);
        if(admin.getUsername()==null)return "success";
        adminService.insert(admin);
        return "success"; // 返回一个简单的成功消息
    }

    @PutMapping("/update/{id}")
    @CrossOrigin(origins = "http://localhost:63342")
    public Result update(@PathVariable Long id, @RequestBody Map<String, Object> updateData) {
        log.info("管理员数据的更新: {}", updateData);

        // 1. 根据 id 查找管理员
        Admin admin = adminService.selectById(id);
        if (admin == null) {
            return Result.failure("管理员不存在");
        }

        // 2. 根据请求中的字段进行更新
        if (updateData.containsKey("username") && updateData.get("username") != null) {
            admin.setUsername(updateData.get("username").toString());
        }
        if (updateData.containsKey("password") && updateData.get("password") != null) {
            admin.setPassword(updateData.get("password").toString());
        }
        if (updateData.containsKey("role") && updateData.get("role") != null) {
            admin.setRole(updateData.get("role").toString());
        }

        // 3. 调用服务层更新管理员信息
        adminService.update(admin);

        // 4. 返回成功结果
        return Result.success("管理员信息更新成功");
    }

    @DeleteMapping("/delete/{id}")
    @CrossOrigin(origins = "http://localhost:63342")  // 允许前端跨域
    public Result deleteAdmin(@PathVariable Long id) {
        log.info("正在删除管理员，ID: {}", id);

        // 调用服务层删除管理员
        boolean isDeleted = adminService.deleteById(id);

        // 根据删除结果返回响应
        if (isDeleted) {
            return Result.success("管理员删除成功");
        } else {
            return Result.failure("删除管理员失败，管理员不存在或其他错误");
        }
    }
}
