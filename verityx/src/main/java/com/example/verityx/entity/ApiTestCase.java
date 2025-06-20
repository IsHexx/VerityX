package com.example.verityx.entity;

import java.time.LocalDateTime;

/**
 * 接口测试用例实体类
 */
public class ApiTestCase {

    private Integer id;                   // 主键
    private Integer apiId;                // 所属接口ID
    private String name;                  // 用例名称
    private String description;           // 用例描述
    private String requestParams;         // 请求参数（JSON格式）
    private String expectedResponse;      // 期望响应（JSON格式）
    private String status;                // 状态
    private Integer sortOrder;            // 排序顺序
    private LocalDateTime createdAt;      // 创建时间
    private LocalDateTime updatedAt;      // 更新时间

    // 构造函数
    public ApiTestCase() {}

    public ApiTestCase(Integer apiId, String name) {
        this.apiId = apiId;
        this.name = name;
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

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
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

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getExpectedResponse() {
        return expectedResponse;
    }

    public void setExpectedResponse(String expectedResponse) {
        this.expectedResponse = expectedResponse;
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

    @Override
    public String toString() {
        return "ApiTestCase{" +
                "id=" + id +
                ", apiId=" + apiId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", requestParams='" + requestParams + '\'' +
                ", expectedResponse='" + expectedResponse + '\'' +
                ", status='" + status + '\'' +
                ", sortOrder=" + sortOrder +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
