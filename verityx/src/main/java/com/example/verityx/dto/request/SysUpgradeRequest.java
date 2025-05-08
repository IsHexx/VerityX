package com.example.verityx.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 系统升级请求DTO类
 */
public class SysUpgradeRequest {
    /**
     * 升级到版本号
     */
    @NotBlank(message = "升级到版本号不能为空")
    private String toVersion;
    
    /**
     * 执行升级的用户
     */
    private String upgradeUser;
    
    /**
     * 是否创建备份
     */
    @NotNull(message = "是否创建备份不能为空")
    private Boolean createBackup;
    
    /**
     * 备份说明
     */
    private String backupRemark;
    
    /**
     * 备注
     */
    private String remarks;
    
    // Getters and Setters
    
    public String getToVersion() {
        return toVersion;
    }
    
    public void setToVersion(String toVersion) {
        this.toVersion = toVersion;
    }
    
    public String getUpgradeUser() {
        return upgradeUser;
    }
    
    public void setUpgradeUser(String upgradeUser) {
        this.upgradeUser = upgradeUser;
    }
    
    public Boolean getCreateBackup() {
        return createBackup;
    }
    
    public void setCreateBackup(Boolean createBackup) {
        this.createBackup = createBackup;
    }
    
    public String getBackupRemark() {
        return backupRemark;
    }
    
    public void setBackupRemark(String backupRemark) {
        this.backupRemark = backupRemark;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    @Override
    public String toString() {
        return "SysUpgradeRequest{" +
                "toVersion='" + toVersion + '\'' +
                ", upgradeUser='" + upgradeUser + '\'' +
                ", createBackup=" + createBackup +
                ", backupRemark='" + backupRemark + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
} 