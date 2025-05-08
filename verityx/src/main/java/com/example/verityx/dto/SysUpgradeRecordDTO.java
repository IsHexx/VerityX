package com.example.verityx.dto;

import java.time.LocalDateTime;

/**
 * 系统升级记录DTO类
 */
public class SysUpgradeRecordDTO {
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 原版本号
     */
    private String fromVersion;
    
    /**
     * 升级到版本号
     */
    private String toVersion;
    
    /**
     * 升级时间
     */
    private LocalDateTime upgradeTime;
    
    /**
     * 执行升级的用户
     */
    private String upgradeUser;
    
    /**
     * 升级状态
     */
    private String status;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 关联的备份ID
     */
    private Long backupId;
    
    /**
     * 升级耗时（秒）
     */
    private Integer duration;
    
    /**
     * 备注
     */
    private String remarks;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 备份信息
     */
    private SysBackupDTO backup;
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFromVersion() {
        return fromVersion;
    }
    
    public void setFromVersion(String fromVersion) {
        this.fromVersion = fromVersion;
    }
    
    public String getToVersion() {
        return toVersion;
    }
    
    public void setToVersion(String toVersion) {
        this.toVersion = toVersion;
    }
    
    public LocalDateTime getUpgradeTime() {
        return upgradeTime;
    }
    
    public void setUpgradeTime(LocalDateTime upgradeTime) {
        this.upgradeTime = upgradeTime;
    }
    
    public String getUpgradeUser() {
        return upgradeUser;
    }
    
    public void setUpgradeUser(String upgradeUser) {
        this.upgradeUser = upgradeUser;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public Long getBackupId() {
        return backupId;
    }
    
    public void setBackupId(Long backupId) {
        this.backupId = backupId;
    }
    
    public Integer getDuration() {
        return duration;
    }
    
    public void setDuration(Integer duration) {
        this.duration = duration;
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
    
    public SysBackupDTO getBackup() {
        return backup;
    }
    
    public void setBackup(SysBackupDTO backup) {
        this.backup = backup;
    }
    
    @Override
    public String toString() {
        return "SysUpgradeRecordDTO{" +
                "id=" + id +
                ", fromVersion='" + fromVersion + '\'' +
                ", toVersion='" + toVersion + '\'' +
                ", upgradeTime=" + upgradeTime +
                ", upgradeUser='" + upgradeUser + '\'' +
                ", status='" + status + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", backupId=" + backupId +
                ", duration=" + duration +
                ", remarks='" + remarks + '\'' +
                ", createTime=" + createTime +
                ", backup=" + backup +
                '}';
    }
} 