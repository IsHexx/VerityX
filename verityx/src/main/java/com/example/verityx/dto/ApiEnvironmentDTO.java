package com.example.verityx.dto;

import java.util.Date;
import java.util.List;

/**
 * API环境配置DTO
 */
public class ApiEnvironmentDTO {
    
    private Integer id;
    private String envName;
    private String baseUrl;
    private String description;
    private Boolean isDefault;
    private String globalHeaders;
    private String globalParams;
    private String projectId;
    private List<Integer> ids; // 用于批量操作

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getGlobalHeaders() {
        return globalHeaders;
    }

    public void setGlobalHeaders(String globalHeaders) {
        this.globalHeaders = globalHeaders;
    }

    public String getGlobalParams() {
        return globalParams;
    }

    public void setGlobalParams(String globalParams) {
        this.globalParams = globalParams;
    }
    
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
} 