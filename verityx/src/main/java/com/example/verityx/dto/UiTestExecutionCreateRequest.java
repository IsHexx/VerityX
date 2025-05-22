package com.example.verityx.dto;

import java.util.List;

/**
 * UI测试执行创建请求DTO
 */
public class UiTestExecutionCreateRequest {
    
    private String executionName;
    private String executionType; // case/suite
    private Long executionSourceId; // 用例ID或套件ID
    private String environment; // 浏览器类型
    private String executionConfig; // 执行配置（JSON格式）
    private List<Long> caseIds; // 当执行类型为case时，可能包含多个用例ID
    private Integer priority; // 优先级，用于执行队列排序
    private Long projectId; // 项目ID

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

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getExecutionConfig() {
        return executionConfig;
    }

    public void setExecutionConfig(String executionConfig) {
        this.executionConfig = executionConfig;
    }

    public List<Long> getCaseIds() {
        return caseIds;
    }

    public void setCaseIds(List<Long> caseIds) {
        this.caseIds = caseIds;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
} 