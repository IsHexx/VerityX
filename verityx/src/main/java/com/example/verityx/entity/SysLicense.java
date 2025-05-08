package com.example.verityx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 系统许可证信息实体类
 */
@TableName("sys_license")
public class SysLicense {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 许可证密钥
     */
    @TableField("license_key")
    private String licenseKey;
    
    /**
     * 许可证类型
     */
    @TableField("license_type")
    private String licenseType;
    
    /**
     * 颁发给
     */
    @TableField("issued_to")
    private String issuedTo;
    
    /**
     * 颁发者
     */
    @TableField("issued_by")
    private String issuedBy;
    
    /**
     * 颁发日期
     */
    @TableField("issue_date")
    private LocalDateTime issueDate;
    
    /**
     * 过期日期
     */
    @TableField("expiration_date")
    private LocalDateTime expirationDate;
    
    /**
     * 最大用户数
     */
    @TableField("max_users")
    private Integer maxUsers;
    
    /**
     * 授权功能列表
     */
    @TableField("features")
    private String features;
    
    /**
     * 状态
     */
    @TableField("status")
    private String status;
    
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
    
    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getLicenseKey() {
        return licenseKey;
    }
    
    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }
    
    public String getLicenseType() {
        return licenseType;
    }
    
    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }
    
    public String getIssuedTo() {
        return issuedTo;
    }
    
    public void setIssuedTo(String issuedTo) {
        this.issuedTo = issuedTo;
    }
    
    public String getIssuedBy() {
        return issuedBy;
    }
    
    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }
    
    public LocalDateTime getIssueDate() {
        return issueDate;
    }
    
    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }
    
    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }
    
    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }
    
    public Integer getMaxUsers() {
        return maxUsers;
    }
    
    public void setMaxUsers(Integer maxUsers) {
        this.maxUsers = maxUsers;
    }
    
    public String getFeatures() {
        return features;
    }
    
    public void setFeatures(String features) {
        this.features = features;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
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
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    @Override
    public String toString() {
        return "SysLicense{" +
                "id=" + id +
                ", licenseKey='" + licenseKey + '\'' +
                ", licenseType='" + licenseType + '\'' +
                ", issuedTo='" + issuedTo + '\'' +
                ", issuedBy='" + issuedBy + '\'' +
                ", issueDate=" + issueDate +
                ", expirationDate=" + expirationDate +
                ", maxUsers=" + maxUsers +
                ", features='" + features + '\'' +
                ", status='" + status + '\'' +
                ", remarks='" + remarks + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
} 