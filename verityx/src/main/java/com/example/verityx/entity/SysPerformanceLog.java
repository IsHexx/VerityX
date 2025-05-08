package com.example.verityx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 系统性能日志实体类
 */
@TableName("sys_performance_log")
public class SysPerformanceLog {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 资源类型（CPU, MEMORY, DISK, NETWORK等）
     */
    @TableField("resource_type")
    private String resourceType;
    
    /**
     * 指标名称
     */
    @TableField("metric_name")
    private String metricName;
    
    /**
     * 指标值
     */
    @TableField("metric_value")
    private String metricValue;
    
    /**
     * 操作用户ID
     */
    @TableField("user_id")
    private Long userId;
    
    /**
     * 操作用户名
     */
    @TableField("username")
    private String username;
    
    /**
     * IP地址
     */
    @TableField("ip_address")
    private String ipAddress;
    
    /**
     * 日志等级
     */
    @TableField("log_level")
    private String logLevel;
    
    /**
     * 记录时间
     */
    @TableField("record_time")
    private LocalDateTime recordTime;
    
    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;
    
    /**
     * 创建时间
     */
    @TableField("create_time")
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