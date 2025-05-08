package com.example.verityx.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 系统清理请求DTO类
 */
public class SysCleanupRequest {
    
    /**
     * 清理类型（temp-临时文件, log-日志, report-测试报告）
     */
    @NotNull(message = "清理类型不能为空")
    private String cleanupType;
    
    /**
     * 保留天数
     */
    @Min(value = 1, message = "保留天数不能小于1")
    @Max(value = 3650, message = "保留天数不能超过3650")
    private Integer keepDays;
    
    /**
     * 清理路径，用于临时文件清理
     */
    private String cleanupPath;
    
    /**
     * 归档路径，用于测试报告归档
     */
    private String archivePath;
    
    /**
     * 是否清理测试报告
     */
    private boolean cleanupReports;
    
    /**
     * 是否清理日志
     */
    private boolean cleanupLogs;
    
    /**
     * 是否清理监控数据
     */
    private boolean cleanupMonitoringData;
    
    /**
     * 是否清理临时文件
     */
    private boolean cleanupTempFiles;
    
    /**
     * 保留测试报告天数
     */
    @Min(value = 1, message = "保留测试报告天数不能小于1")
    @Max(value = 3650, message = "保留测试报告天数不能超过3650")
    private Integer keepReportDays;
    
    /**
     * 保留日志天数
     */
    @Min(value = 1, message = "保留日志天数不能小于1")
    @Max(value = 1825, message = "保留日志天数不能超过1825")
    private Integer keepLogDays;
    
    /**
     * 保留监控数据天数
     */
    @Min(value = 1, message = "保留监控数据天数不能小于1")
    @Max(value = 365, message = "保留监控数据天数不能超过365")
    private Integer keepMonitoringDays;
    
    /**
     * 临时文件清理路径
     */
    private String tempCleanupPath;
    
    /**
     * 是否自动归档
     */
    private boolean autoArchive;
    
    /**
     * 归档阈值天数
     */
    @Min(value = 30, message = "归档阈值天数不能小于30")
    @Max(value = 365, message = "归档阈值天数不能超过365")
    private Integer archiveDays;
    
    /**
     * 归档存储位置
     */
    private String archiveLocation;
    
    // Getters and Setters
    public String getCleanupType() {
        return cleanupType;
    }
    
    public void setCleanupType(String cleanupType) {
        this.cleanupType = cleanupType;
    }
    
    public Integer getKeepDays() {
        return keepDays;
    }
    
    public void setKeepDays(Integer keepDays) {
        this.keepDays = keepDays;
    }
    
    public String getCleanupPath() {
        return cleanupPath;
    }
    
    public void setCleanupPath(String cleanupPath) {
        this.cleanupPath = cleanupPath;
    }
    
    public String getArchivePath() {
        return archivePath;
    }
    
    public void setArchivePath(String archivePath) {
        this.archivePath = archivePath;
    }
    
    public boolean isCleanupReports() {
        return cleanupReports;
    }
    
    public void setCleanupReports(boolean cleanupReports) {
        this.cleanupReports = cleanupReports;
    }
    
    public boolean isCleanupLogs() {
        return cleanupLogs;
    }
    
    public void setCleanupLogs(boolean cleanupLogs) {
        this.cleanupLogs = cleanupLogs;
    }
    
    public boolean isCleanupMonitoringData() {
        return cleanupMonitoringData;
    }
    
    public void setCleanupMonitoringData(boolean cleanupMonitoringData) {
        this.cleanupMonitoringData = cleanupMonitoringData;
    }
    
    public boolean isCleanupTempFiles() {
        return cleanupTempFiles;
    }
    
    public void setCleanupTempFiles(boolean cleanupTempFiles) {
        this.cleanupTempFiles = cleanupTempFiles;
    }
    
    public Integer getKeepReportDays() {
        return keepReportDays;
    }
    
    public void setKeepReportDays(Integer keepReportDays) {
        this.keepReportDays = keepReportDays;
    }
    
    public Integer getKeepLogDays() {
        return keepLogDays;
    }
    
    public void setKeepLogDays(Integer keepLogDays) {
        this.keepLogDays = keepLogDays;
    }
    
    public Integer getKeepMonitoringDays() {
        return keepMonitoringDays;
    }
    
    public void setKeepMonitoringDays(Integer keepMonitoringDays) {
        this.keepMonitoringDays = keepMonitoringDays;
    }
    
    public String getTempCleanupPath() {
        return tempCleanupPath;
    }
    
    public void setTempCleanupPath(String tempCleanupPath) {
        this.tempCleanupPath = tempCleanupPath;
    }
    
    public boolean isAutoArchive() {
        return autoArchive;
    }
    
    public void setAutoArchive(boolean autoArchive) {
        this.autoArchive = autoArchive;
    }
    
    public Integer getArchiveDays() {
        return archiveDays;
    }
    
    public void setArchiveDays(Integer archiveDays) {
        this.archiveDays = archiveDays;
    }
    
    public String getArchiveLocation() {
        return archiveLocation;
    }
    
    public void setArchiveLocation(String archiveLocation) {
        this.archiveLocation = archiveLocation;
    }
    
    @Override
    public String toString() {
        return "SysCleanupRequest{" +
                "cleanupType='" + cleanupType + '\'' +
                ", keepDays=" + keepDays +
                ", cleanupPath='" + cleanupPath + '\'' +
                ", archivePath='" + archivePath + '\'' +
                ", cleanupReports=" + cleanupReports +
                ", cleanupLogs=" + cleanupLogs +
                ", cleanupMonitoringData=" + cleanupMonitoringData +
                ", cleanupTempFiles=" + cleanupTempFiles +
                ", keepReportDays=" + keepReportDays +
                ", keepLogDays=" + keepLogDays +
                ", keepMonitoringDays=" + keepMonitoringDays +
                ", tempCleanupPath='" + tempCleanupPath + '\'' +
                ", autoArchive=" + autoArchive +
                ", archiveDays=" + archiveDays +
                ", archiveLocation='" + archiveLocation + '\'' +
                '}';
    }
} 