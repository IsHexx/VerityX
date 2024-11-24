package com.example.verityx.entity;

import java.time.LocalDateTime;

public class ApiManagement {

    private Integer apiId;                // 接口ID
    private String apiDirectory;          // 接口目录
    private String apiName;               // 接口名称
    private String relatedTestCases;      // 关联接口用例ID列表
    private String requestMethod;         // 请求方法
    private String apiPath;               // 接口路径
    private String status;                // 接口状态
    private LocalDateTime createdAt;      // 创建时间
    private LocalDateTime updatedAt;      // 更新时间
    private String mockUrl;               // MOCK地址
    private String responseBody;          // 返回Body示例

    // Getters and Setters

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public String getApiDirectory() {
        return apiDirectory;
    }

    public void setApiDirectory(String apiDirectory) {
        this.apiDirectory = apiDirectory;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getRelatedTestCases() {
        return relatedTestCases;
    }

    public void setRelatedTestCases(String relatedTestCases) {
        this.relatedTestCases = relatedTestCases;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
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

    public String getMockUrl() {
        return mockUrl;
    }

    public void setMockUrl(String mockUrl) {
        this.mockUrl = mockUrl;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}
