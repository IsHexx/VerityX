package com.example.verityx.dto;

import java.util.Date;

/**
 * UI测试执行DTO
 */
public class UiTestExecutionDTO {
    
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

    // 用于前端展示的附加字段
    private String statusText;
    private String statusType;
    private String durationText;
    private Double successRate;

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

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getDurationText() {
        return durationText;
    }

    public void setDurationText(String durationText) {
        this.durationText = durationText;
    }

    public Double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(Double successRate) {
        this.successRate = successRate;
    }
} 