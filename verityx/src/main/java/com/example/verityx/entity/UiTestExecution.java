package com.example.verityx.entity;

import java.util.Date;

/**
 * UI测试执行记录实体类
 */
public class UiTestExecution {
    
    private Long id;
    private String executionName;
    private String executionType;
    private Long executionSourceId;
    private String executionSourceName;
    private String status;
    private String executor;
    private String environment;
    private Date startTime;
    private Date endTime;
    private Integer duration;
    private Integer successCount;
    private Integer failCount;
    private Integer totalCount;
    private String executionConfig;
    private Date createdAt;
    private Date updatedAt;
    private Long projectId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExecutionName() {
        return executionName;
    }

    public void setExecutionName(String executionName) {
        this.executionName = executionName;
    }

    public String getExecutionType() {
        return executionType;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }

    public Long getExecutionSourceId() {
        return executionSourceId;
    }

    public void setExecutionSourceId(Long executionSourceId) {
        this.executionSourceId = executionSourceId;
    }

    public String getExecutionSourceName() {
        return executionSourceName;
    }

    public void setExecutionSourceName(String executionSourceName) {
        this.executionSourceName = executionSourceName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getExecutionConfig() {
        return executionConfig;
    }

    public void setExecutionConfig(String executionConfig) {
        this.executionConfig = executionConfig;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
} 