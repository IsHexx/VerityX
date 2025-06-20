package com.example.verityx.entity;

import java.time.LocalDateTime;

/**
 * 接口信息实体类
 */
public class ApiEndpoint {

    private Integer id;                   // 主键
    private Integer moduleId;             // 所属模块ID
    private String name;                  // 接口名称
    private String path;                  // 接口路径
    private String method;                // 请求方法
    private String description;           // 接口描述
    private Boolean isTemp;               // 是否为临时接口
    private String mockUrl;               // Mock地址
    private String status;                // 状态
    private Integer sortOrder;            // 排序顺序
    private String owner;                 // 责任人
    private String tags;                  // 标签
    private String preUrl;                // 前置URL
    private String pathParams;            // 路径参数（JSON格式）
    private String queryParams;           // 查询参数（JSON格式）
    private String headerParams;          // 请求头参数（JSON格式）
    private LocalDateTime createdAt;      // 创建时间
    private LocalDateTime updatedAt;      // 更新时间

    // 构造函数
    public ApiEndpoint() {}

    public ApiEndpoint(Integer moduleId, String name, String path, String method) {
        this.moduleId = moduleId;
        this.name = name;
        this.path = path;
        this.method = method;
        this.status = "active";
        this.isTemp = false;
        this.sortOrder = 0;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsTemp() {
        return isTemp;
    }

    public void setIsTemp(Boolean isTemp) {
        this.isTemp = isTemp;
    }

    public String getMockUrl() {
        return mockUrl;
    }

    public void setMockUrl(String mockUrl) {
        this.mockUrl = mockUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPreUrl() {
        return preUrl;
    }

    public void setPreUrl(String preUrl) {
        this.preUrl = preUrl;
    }

    public String getPathParams() {
        return pathParams;
    }

    public void setPathParams(String pathParams) {
        this.pathParams = pathParams;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    public String getHeaderParams() {
        return headerParams;
    }

    public void setHeaderParams(String headerParams) {
        this.headerParams = headerParams;
    }

    @Override
    public String toString() {
        return "ApiEndpoint{" +
                "id=" + id +
                ", moduleId=" + moduleId +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", method='" + method + '\'' +
                ", description='" + description + '\'' +
                ", isTemp=" + isTemp +
                ", mockUrl='" + mockUrl + '\'' +
                ", status='" + status + '\'' +
                ", sortOrder=" + sortOrder +
                ", owner='" + owner + '\'' +
                ", tags='" + tags + '\'' +
                ", preUrl='" + preUrl + '\'' +
                ", pathParams='" + pathParams + '\'' +
                ", queryParams='" + queryParams + '\'' +
                ", headerParams='" + headerParams + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
