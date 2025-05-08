package com.example.verityx.dto;

import java.time.LocalDateTime;

/**
 * 系统监控数据DTO类
 */
public class SysMonitorDataDTO {
    private Long id;
    private String monitorType;
    private String metricName;
    private String metricValue;
    private String status;
    private LocalDateTime collectTime;
    private LocalDateTime createdAt;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getMonitorType() {
        return monitorType;
    }
    
    public void setMonitorType(String monitorType) {
        this.monitorType = monitorType;
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getCollectTime() {
        return collectTime;
    }
    
    public void setCollectTime(LocalDateTime collectTime) {
        this.collectTime = collectTime;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
} 