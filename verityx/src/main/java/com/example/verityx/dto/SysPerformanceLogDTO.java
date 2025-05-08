package com.example.verityx.dto;

import java.time.LocalDateTime;

/**
 * 系统性能日志DTO类
 */
public class SysPerformanceLogDTO {
    private Long id;
    private String resourceType;
    private String metricName;
    private String metricValue;
    private Long userId;
    private String username;
    private String ipAddress;
    private String logLevel;
    private LocalDateTime recordTime;
    private String remarks;
    private LocalDateTime createTime;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getResourceType() {
        return resourceType;
    }
    
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
    
    public String getMetricName() {
        return metricName;
    }
    
    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }
    
    public String getMetricValue() {
        return metricValue;
    }
    
    public void setMetricValue(String metricValue) {
        this.metricValue = metricValue;
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
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getLogLevel() {
        return logLevel;
    }
    
    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }
    
    public LocalDateTime getRecordTime() {
        return recordTime;
    }
    
    public void setRecordTime(LocalDateTime recordTime) {
        this.recordTime = recordTime;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
} 