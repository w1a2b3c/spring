package com.example.system.controller;

import com.example.system.dto.HouseDTO;
import com.example.system.dto.PageDTO;
import com.example.system.entity.House;
import com.example.system.service.HouseService;
import com.example.system.service.UserService;
import com.example.system.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/house")
@CrossOrigin(origins = "http://localhost:8081")
public class HouseController {

    @Autowired
    private HouseService houseService;  // 注入房屋服务层
    @Autowired
    private UserService userService;   // 注入用户服务层

    // 获取房屋分页信息
    @GetMapping("/page")
    public ResponseEntity<Map<String, Object>> getHousePage(PageDTO pageDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 确保分页参数有效
            if (pageDTO.getCurrentPage() <= 0) {
                pageDTO.setCurrentPage(1);  // 默认页码为 1
            }
            if (pageDTO.getPageSize() <= 0) {
                pageDTO.setPageSize(5);  // 默认每页记录数为 5
            }

            List<House> houses = houseService.getHousesByPage(pageDTO);  // 获取分页房屋数据

            if (houses != null && !houses.isEmpty()) {
                response.put("status", "success");
                response.put("houses", houses);  // 返回查询结果
            } else {
                response.put("status", "fail");
                response.put("message", "没有查询到房屋数据");
            }

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "服务器内部错误");
        }

        // 设置响应头为 UTF-8 编码
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=UTF-8");

        return ResponseEntity.ok().headers(headers).body(response);
    }

    // 添加房屋信息
    @PostMapping("/add")
    public Result<String> addHouse(@RequestBody HouseDTO houseDTO) {
        log.info("添加房屋信息：{}", houseDTO);

        // 检查传入的参数是否为空
        if (houseDTO == null) {
            log.error("接收到的 HouseDTO 为空");
            return Result.failure("请求参数为空");
        }

        // 校验房屋信息是否合法
        if (houseDTO.getPrice() <= 0) {
            return Result.failure("价格无效");
        }

        // 校验房东ID是否有效
        if (houseDTO.getOwnerId() == 0L || userService.selectById(houseDTO.getOwnerId()) == null) {
            return Result.failure("无效的房东ID");
        }
        // 创建房屋对象
        House house = new House();

        // 将接收到的前端数据赋值给房屋对象
        house.setHouseNumber(houseDTO.getHouseNumber()); // 赋值房屋编号
        house.setArea(houseDTO.getArea());               // 赋值房屋面积
        house.setPrice(houseDTO.getPrice());             // 赋值房屋价格
        house.setOwnerId(houseDTO.getOwnerId());         // 赋值房东ID
        house.setStatus(houseDTO.getStatus());
        // 调用服务层的方法保存房屋信息
        houseService.addHouse(house);

        return Result.success("房屋信息添加成功");
    }

    // 更新房屋信息
    @PutMapping("/update/{id}")
    public Result<String> updateHouse(@PathVariable Long id, @RequestBody HouseDTO houseDTO) {
        log.info("更新房屋信息，ID: {}, 新信息: {}", id, houseDTO);

        // 根据 ID 查找房屋
        House house = houseService.getHouseById(id);
        if (house == null) {
            return Result.failure("房屋不存在");
        }

        // 更新房屋信息
        house.setPrice(houseDTO.getPrice());
        house.setOwnerId(houseDTO.getOwnerId());
        house.setStatus(houseDTO.getStatus());

        // 调用服务层更新房屋信息
        houseService.updateHouse(house);

        return Result.success("房屋信息更新成功");
    }

    // 删除房屋
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteHouse(@PathVariable Long id) {
        log.info("删除房屋，ID: {}", id);

        // 调用服务层删除房屋
        boolean isDeleted = houseService.deleteHouse(id);

        if (isDeleted) {
            return Result.success("房屋删除成功");
        } else {
            return Result.failure("删除房屋失败，房屋不存在或其他错误");
        }
    }
}
