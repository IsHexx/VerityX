package com.example.verityx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

import java.time.LocalDateTime;

/**
 * 系统监控数据实体类
 */
@TableName("sys_monitor_data")
public class SysMonitorData {
    
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 监控类型（SERVER-服务器, DATABASE-数据库, TEST_ENGINE-测试引擎）
     */
    private String monitorType;
    
    /**
     * 指标名称
     */
    private String metricName;
    
    /**
     * 指标值
     */
    private String metricValue;
    
    /**
     * 状态（NORMAL-正常, WARNING-警告, ERROR-错误）
     */
    private String status;
    
    /**
     * 采集时间
     */
    @TableField("timestamp")
    private LocalDateTime collectTime;
    
    /**
     * 创建时间
     */
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
    
    @Override
    public String toString() {
        return "SysMonitorData{" +
                "id=" + id +
                ", monitorType='" + monitorType + '\'' +
                ", metricName='" + metricName + '\'' +
                ", metricValue='" + metricValue + '\'' +
                ", status='" + status + '\'' +
                ", collectTime=" + collectTime +
                ", createdAt=" + createdAt +
                '}';
    }
} 