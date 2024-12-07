package com.example.system.entity;

import lombok.Data;

@Data
public class House {

    private Long id;             // 房屋ID
    private String houseNumber;  // 房屋编号
    private double area;         // 房屋面积
    private double price;        // 房屋价格
    private String status;       // 房屋状态
    private Long ownerId;        // 房东ID

    // Getter 和 Setter 方法（Lombok 自动生成）

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }






    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }







    // 获取房主ID
    public Long getOwnerId() {
        return ownerId;
    }

    // 设置房主ID
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
