package com.example.verityx.dto.request;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统日志查询请求DTO类
 */
public class SysLogQueryRequest {
    
    /**
     * 日志类型（operation-操作日志, error-异常日志, performance-性能日志）
     */
    private String logType;
    
    /**
     * 日志级别列表
     */
    private List<String> levels;
    
    /**
     * 单个日志级别（用于某些查询）
     */
    private String logLevel;
    
    /**
     * 资源类型（用于性能日志查询，如CPU, MEMORY, DISK等）
     */
    private String resourceType;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 操作类型
     */
    private String operation;
    
    /**
     * 请求方法
     */
    private String requestMethod;
    
    /**
     * 状态（0失败 1成功）
     */
    private Integer status;
    
    /**
     * 关键词
     */
    private String keyword;
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 排序字段
     */
    private String sortField;
    
    /**
     * 排序方式（asc升序 desc降序）
     */
    private String sortOrder;
    
    // Getters and Setters
    public String getLogType() {
        return logType;
    }
    
    public void setLogType(String logType) {
        this.logType = logType;
    }
    
    public List<String> getLevels() {
        return levels;
    }
    
    public void setLevels(List<String> levels) {
        this.levels = levels;
    }
    
    public String getLogLevel() {
        return logLevel;
    }
    
    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }
    
    public String getResourceType() {
        return resourceType;
    }
    
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getOperation() {
        return operation;
    }
    
    public void setOperation(String operation) {
        this.operation = operation;
    }
    
    public String getRequestMethod() {
        return requestMethod;
    }
    
    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public String getKeyword() {
        return keyword;
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public String getSortField() {
        return sortField;
    }
    
    public void setSortField(String sortField) {
        this.sortField = sortField;
    }
    
    public String getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    @Override
    public String toString() {
        return "SysLogQueryRequest{" +
                "logType='" + logType + '\'' +
                ", levels=" + levels +
                ", logLevel='" + logLevel + '\'' +
                ", resourceType='" + resourceType + '\'' +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", operation='" + operation + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", status=" + status +
                ", keyword='" + keyword + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", sortField='" + sortField + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                '}';
    }
}