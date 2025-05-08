package com.example.verityx.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 系统备份请求DTO类
 */
public class SysBackupRequest {
    
    /**
     * 备份名称
     */
    @NotBlank(message = "备份名称不能为空")
    @Size(max = 100, message = "备份名称长度不能超过100个字符")
    private String backupName;
    
    /**
     * 备份类型（AUTO-自动备份, MANUAL-手动备份）
     */
    private String backupType = "MANUAL";
    
    /**
     * 备份描述
     */
    @Size(max = 500, message = "备份描述长度不能超过500个字符")
    private String description;
    
    /**
     * 备注
     */
    @Size(max = 255, message = "备注长度不能超过255个字符")
    private String remark;
    
    // Getters and Setters
    public String getBackupName() {
        return backupName;
    }
    
    public void setBackupName(String backupName) {
        this.backupName = backupName;
    }
    
    public String getBackupType() {
        return backupType;
    }
    
    public void setBackupType(String backupType) {
        this.backupType = backupType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    @Override
    public String toString() {
        return "SysBackupRequest{" +
                "backupName='" + backupName + '\'' +
                ", backupType='" + backupType + '\'' +
                ", description='" + description + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
} 