package com.example.verityx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 系统升级记录实体类
 */
@TableName("sys_upgrade_record")
public class SysUpgradeRecord {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 原版本号
     */
    @TableField("from_version")
    private String fromVersion;
    
    /**
     * 升级到版本号
     */
    @TableField("to_version")
    private String toVersion;
    
    /**
     * 升级时间
     */
    @TableField("upgrade_time")
    private LocalDateTime upgradeTime;
    
    /**
     * 执行升级的用户
     */
    @TableField("upgrade_user")
    private String upgradeUser;
    
    /**
     * 升级状态
     */
    @TableField("status")
    private String status;
    
    /**
     * 错误信息
     */
    @TableField("error_message")
    private String errorMessage;
    
    /**
     * 关联的备份ID
     */
    @TableField("backup_id")
    private Long backupId;
    
    /**
     * 升级耗时（秒）
     */
    @TableField("duration")
    private Integer duration;
    
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
    
    @Override
    public String toString() {
        return "SysUpgradeRecord{" +
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
                '}';
    }
} 