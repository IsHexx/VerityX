package com.example.verityx.dto;

import java.time.LocalDateTime;

/**
 * 系统许可证信息DTO类
 */
public class SysLicenseDTO {
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 许可证密钥
     */
    private String licenseKey;
    
    /**
     * 许可证类型
     */
    private String licenseType;
    
    /**
     * 颁发给
     */
    private String issuedTo;
    
    /**
     * 颁发者
     */
    private String issuedBy;
    
    /**
     * 颁发日期
     */
    private LocalDateTime issueDate;
    
    /**
     * 过期日期
     */
    private LocalDateTime expirationDate;
    
    /**
     * 最大用户数
     */
    private Integer maxUsers;
    
    /**
     * 授权功能列表
     */
    private String features;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 备注
     */
    private String remarks;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 是否过期
     */
    private Boolean expired;
    
    /**
     * 剩余天数
     */
    private Integer remainingDays;
    
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
    
    public Boolean getExpired() {
        return expired;
    }
    
    public void setExpired(Boolean expired) {
        this.expired = expired;
    }
    
    public Integer getRemainingDays() {
        return remainingDays;
    }
    
    public void setRemainingDays(Integer remainingDays) {
        this.remainingDays = remainingDays;
    }
    
    @Override
    public String toString() {
        return "SysLicenseDTO{" +
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
                ", expired=" + expired +
                ", remainingDays=" + remainingDays +
                '}';
    }
} 