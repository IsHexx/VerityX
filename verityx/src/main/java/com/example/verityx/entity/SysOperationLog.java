package com.example.verityx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

import java.time.LocalDateTime;

/**
 * 系统操作日志实体类
 */
@TableName("sys_operation_log")
public class SysOperationLog {
    
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 日志类型
     */
    @TableField("log_type")
    private String logType;
    
    /**
     * 日志级别
     */
    @TableField("log_level")
    private String logLevel;
    
    /**
     * 操作模块
     */
    @TableField("module")
    private String module;
    
    /**
     * 日志内容
     */
    @TableField("content")
    private String content;
    
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
     * 操作IP地址
     */
    @TableField("ip_address")
    private String ipAddress;
    
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    
    /**
     * 追踪ID
     */
    @TableField("trace_id")
    private String traceId;
    
    /**
     * 额外信息
     */
    @TableField("extra_info")
    private String extraInfo;
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getLogType() {
        return logType;
    }
    
    public void setLogType(String logType) {
        this.logType = logType;
    }
    
    public String getLogLevel() {
        return logLevel;
    }
    
    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }
    
    public String getModule() {
        return module;
    }
    
    public void setModule(String module) {
        this.module = module;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
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
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public String getTraceId() {
        return traceId;
    }
    
    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
    
    public String getExtraInfo() {
        return extraInfo;
    }
    
    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }
    
    @Override
    public String toString() {
        return "SysOperationLog{" +
                "id=" + id +
                ", logType='" + logType + '\'' +
                ", logLevel='" + logLevel + '\'' +
                ", module='" + module + '\'' +
                ", content='" + content + '\'' +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", createTime=" + createTime +
                ", traceId='" + traceId + '\'' +
                ", extraInfo='" + extraInfo + '\'' +
                '}';
    }
} 