package com.example.verityx.entity;

import java.util.Date;

/**
 * API环境配置实体类
 */
public class ApiEnvironment {
    
    private Integer id;
    private String envName;         // 环境名称：开发环境、测试环境、生产环境等
    private String baseUrl;         // 环境基础URL，如：https://dev-api.example.com
    private String description;     // 环境描述
    private Boolean isDefault;      // 是否为默认环境
    private String globalHeaders;   // 全局请求头，JSON格式
    private String globalParams;    // 全局参数，JSON格式
    private Date createTime;        // 创建时间
    private Date updateTime;        // 更新时间

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "ApiEnvironment{" +
                "id=" + id +
                ", envName='" + envName + '\'' +
                ", baseUrl='" + baseUrl + '\'' +
                ", description='" + description + '\'' +
                ", isDefault=" + isDefault +
                ", globalHeaders='" + globalHeaders + '\'' +
                ", globalParams='" + globalParams + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
} 