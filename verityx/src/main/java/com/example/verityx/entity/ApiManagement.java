package com.example.verityx.entity;

import java.time.LocalDateTime;

public class ApiManagement {

    private Integer apiId;                // ID，自增主键
    private String itemType;              // 项目类型：directory-目录, api-接口, case-用例
    private Integer parentId;             // 父级ID
    private String apiDirectory;          // 接口目录（仅接口类型使用）
    private String apiName;               // 名称（目录名/接口名/用例名）
    private String relatedTestCases;      // 关联接口用例ID列表
    private String requestMethod;         // 请求方法（仅接口类型使用）
    private String apiPath;               // 接口路径（仅接口类型使用）
    private String status;                // 状态
    private LocalDateTime createdAt;      // 创建时间
    private LocalDateTime updatedAt;      // 更新时间
    private String mockUrl;               // MOCK地址（仅接口类型使用）
    private String responseBody;          // 返回Body示例（仅接口类型使用）
    private Integer projectId;            // 项目ID
    private String pathParams;            // Path参数（JSON格式）（仅接口类型使用）
    private String queryParams;           // Query参数（JSON格式）（仅接口类型使用）
    private String headerParams;          // Header参数（JSON格式）（仅接口类型使用）
    private String description;           // 描述

    // Getters and Setters

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
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

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ApiManagement{" +
                "apiId=" + apiId +
                ", itemType='" + itemType + '\'' +
                ", parentId=" + parentId +
                ", apiDirectory='" + apiDirectory + '\'' +
                ", apiName='" + apiName + '\'' +
                ", relatedTestCases='" + relatedTestCases + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", apiPath='" + apiPath + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", mockUrl='" + mockUrl + '\'' +
                ", responseBody='" + responseBody + '\'' +
                ", projectId=" + projectId +
                ", pathParams='" + pathParams + '\'' +
                ", queryParams='" + queryParams + '\'' +
                ", headerParams='" + headerParams + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
