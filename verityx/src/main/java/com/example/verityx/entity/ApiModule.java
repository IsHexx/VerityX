package com.example.verityx.entity;

import java.time.LocalDateTime;

/**
 * 接口模块实体类
 */
public class ApiModule {

    private Integer id;                   // 主键
    private String name;                  // 模块/目录名称
    private String description;           // 模块描述
    private Integer projectId;            // 项目ID
    private Integer sortOrder;            // 排序顺序
    private String status;                // 状态：active-启用, inactive-禁用
    private LocalDateTime createdAt;      // 创建时间
    private LocalDateTime updatedAt;      // 更新时间

    // 构造函数
    public ApiModule() {}

    public ApiModule(String name, String description, Integer projectId) {
        this.name = name;
        this.description = description;
        this.projectId = projectId;
        this.status = "active";
        this.sortOrder = 0;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "ApiModule{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", projectId=" + projectId +
                ", sortOrder=" + sortOrder +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
