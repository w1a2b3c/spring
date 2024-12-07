package com.example.system.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Announcement {

    private long id;                // 公告ID
    private String title;           // 公告标题
    private String content;         // 公告内容
    private Date createdAt;         // 创建时间，修改为数据库字段对应的名称

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createdAt;
    }

    public void setCreateTime(Date createTime) {
        this.createdAt = createTime;
    }


}
