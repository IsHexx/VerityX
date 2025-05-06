package com.example.verityx.dto;

import java.util.Date;
import java.util.List;

public class UiTestSuiteDTO {
    private Long id;
    private String suiteName;
    private String suiteStatus;
    private Integer caseCount;
    private String lastResult;
    private String createdBy;
    private Date createdAt;
    private Date lastRunAt;
    private String triggerMode;
    private String projectId;
    private String concurrencyLevel;
    private String environment;
    private Integer timeout;
    private Integer retryCount;
    private String screenshotPolicy;
    private String description;

    // 用于用例管理的内部类
    public static class CaseInfo {
        private Long caseId;
        private String caseTitle;
        private String importanceLevel;
        private Integer orderIndex;

        public Long getCaseId() {
            return caseId;
        }

        public void setCaseId(Long caseId) {
            this.caseId = caseId;
        }

        public String getCaseTitle() {
            return caseTitle;
        }

        public void setCaseTitle(String caseTitle) {
            this.caseTitle = caseTitle;
        }

        public String getImportanceLevel() {
            return importanceLevel;
        }

        public void setImportanceLevel(String importanceLevel) {
            this.importanceLevel = importanceLevel;
        }

        public Integer getOrderIndex() {
            return orderIndex;
        }

        public void setOrderIndex(Integer orderIndex) {
            this.orderIndex = orderIndex;
        }
    }

    // 更新用例请求类
    public static class UpdateCasesRequest {
        private List<Long> caseIds;

        public List<Long> getCaseIds() {
            return caseIds;
        }

        public void setCaseIds(List<Long> caseIds) {
            this.caseIds = caseIds;
        }
    }

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

    public String getSuiteStatus() {
        return suiteStatus;
    }

    public void setSuiteStatus(String suiteStatus) {
        this.suiteStatus = suiteStatus;
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
} 