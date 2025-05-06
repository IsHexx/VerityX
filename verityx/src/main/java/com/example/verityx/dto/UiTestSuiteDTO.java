package com.example.verityx.dto;

import java.util.Date;
import java.util.List;

public class UiTestSuiteDTO {
    private Long id;
    private String suiteName;
    private Long projectId;
    private String description;
    private Boolean executeInParallel;
    private Integer maxParallelCount;
    private String createdBy;
    private Date createdAt;
    private Date updatedAt;

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

    // 并发配置类
    public static class ConcurrencyConfig {
        private Boolean executeInParallel;
        private Integer maxParallelCount;
        
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

    // 定时任务配置类
    public static class ScheduleConfig {
        private Boolean enabled;         // 是否启用定时任务
        private String cronExpression;   // cron表达式
        private String timezone;         // 时区
        private String description;      // 任务描述
        
        public Boolean getEnabled() {
            return enabled;
        }
        
        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }
        
        public String getCronExpression() {
            return cronExpression;
        }
        
        public void setCronExpression(String cronExpression) {
            this.cronExpression = cronExpression;
        }
        
        public String getTimezone() {
            return timezone;
        }
        
        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }
        
        public String getDescription() {
            return description;
        }
        
        public void setDescription(String description) {
            this.description = description;
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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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
} 