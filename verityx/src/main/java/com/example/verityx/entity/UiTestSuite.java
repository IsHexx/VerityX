package com.example.verityx.entity;

import java.util.Date;

/**
 * UI测试套件实体类
 */
public class UiTestSuite {
    private Long id;
    private String suiteName;
    private Integer caseCount;
    private String lastResult;
    private String createdBy;
    private Date createdAt;
    private Date updatedAt;
    private Date lastRunAt;
    private String triggerMode;
    private String projectId;
    private String concurrencyLevel;
    private String environment;
    private Integer timeout;
    private Integer retryCount;
    private String screenshotPolicy;
    private String description;
    private Boolean executeInParallel;
    private Integer maxParallelCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public Integer getCaseCount() {
        return caseCount;
    }

    public void setCaseCount(Integer caseCount) {
        this.caseCount = caseCount;
    }

    public String getLastResult() {
        return lastResult;
    }

    public void setLastResult(String lastResult) {
        this.lastResult = lastResult;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public Date getLastRunAt() {
        return lastRunAt;
    }

    public void setLastRunAt(Date lastRunAt) {
        this.lastRunAt = lastRunAt;
    }

    public String getTriggerMode() {
        return triggerMode;
    }

    public void setTriggerMode(String triggerMode) {
        this.triggerMode = triggerMode;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getConcurrencyLevel() {
        return concurrencyLevel;
    }

    public void setConcurrencyLevel(String concurrencyLevel) {
        this.concurrencyLevel = concurrencyLevel;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public String getScreenshotPolicy() {
        return screenshotPolicy;
    }

    public void setScreenshotPolicy(String screenshotPolicy) {
        this.screenshotPolicy = screenshotPolicy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getExecuteInParallel() {
        return executeInParallel;
    }

    public void setExecuteInParallel(Boolean executeInParallel) {
        this.executeInParallel = executeInParallel;
    }

    public Integer getMaxParallelCount() {
        return maxParallelCount;
    }

    public void setMaxParallelCount(Integer maxParallelCount) {
        this.maxParallelCount = maxParallelCount;
    }
} 