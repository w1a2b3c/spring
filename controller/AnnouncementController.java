package com.example.system.controller;

import com.example.system.dto.PageDTO;
import com.example.system.entity.Announcement;
import com.example.system.service.AnnouncementService;
import com.example.system.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/announcement")
@CrossOrigin(origins = "http://localhost:8081")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    /**
     * 分页查询公告
     */
    @GetMapping("/page")
    public Result<Map<String, Object>> getAnnouncementsPage(PageDTO pageDTO) {
        log.info("接收到分页查询参数：currentPage = {}, pageSize = {}", pageDTO.getCurrentPage(), pageDTO.getPageSize());

        Map<String, Object> response = new HashMap<>();
        try {
            // 确保分页参数有效
            if (pageDTO.getCurrentPage() <= 0) {
                pageDTO.setCurrentPage(1);  // 默认页码为 1
                log.info("currentPage 参数无效，已设置为默认值 1");
            }
            if (pageDTO.getPageSize() <= 0) {
                pageDTO.setPageSize(5);  // 默认每页记录数为 5
                log.info("pageSize 参数无效，已设置为默认值 5");
            }

            // 获取公告分页数据
            List<Announcement> announcements = announcementService.pageQuery(pageDTO);

            if (announcements != null && !announcements.isEmpty()) {
                response.put("status", "success");
                response.put("announcements", announcements);  // 返回查询结果
            } else {
                response.put("status", "fail");
                response.put("message", "没有查询到公告数据");
            }

        } catch (Exception e) {
            log.error("发生异常：", e);
            response.put("status", "error");
            response.put("message", "服务器内部错误，请稍后重试");
        }

        return Result.success(response);
    }




    /**
     * 根据ID获取公告
     */
    @GetMapping("/get/{id}")
    public Result<Announcement> getAnnouncementById(@PathVariable Long id) {
        log.info("查询公告，ID: {}", id);
        try {
            Announcement announcement = announcementService.selectById(id);
            if (announcement != null) {
                return Result.success(announcement);
            } else {
                return Result.failure("公告不存在");
            }
        } catch (Exception e) {
            log.error("查询公告失败", e);
            return Result.failure("查询失败");
        }
    }

    /**
     * 添加公告
     */
    @PostMapping("/insert")
    public Result<String> addAnnouncement(@RequestBody Announcement announcement) {
        log.info("添加公告：{}", announcement);
        try {
            announcementService.insert(announcement);
            return Result.success("公告添加成功");
        } catch (Exception e) {
            log.error("添加公告失败", e);
            return Result.failure("公告添加失败");
        }
    }



    /**
     * 删除公告
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteAnnouncement(@PathVariable Long id) {
        log.info("删除公告，ID: {}", id);
        try {
            boolean isDeleted = announcementService.deleteById(id);
            if (isDeleted) {
                return Result.success("公告删除成功");
            } else {
                return Result.failure("公告删除失败，公告不存在");
            }
        } catch (Exception e) {
            log.error("删除公告失败", e);
            return Result.failure("删除失败");
        }
    }
}
