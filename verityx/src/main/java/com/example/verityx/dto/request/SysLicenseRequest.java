package com.example.verityx.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 系统许可证信息请求DTO类
 */
public class SysLicenseRequest {
    /**
     * 主键ID（更新时使用）
     */
    private Long id;
    
    /**
     * 许可证密钥
     */
    @NotBlank(message = "许可证密钥不能为空")
    private String licenseKey;
    
    /**
     * 许可证类型
     */
    @NotBlank(message = "许可证类型不能为空")
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
    @NotNull(message = "颁发日期不能为空")
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
    @NotBlank(message = "状态不能为空")
    private String status;
    
    /**
     * 备注
     */
    private String remarks;
    
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
    
    @Override
    public String toString() {
        return "SysLicenseRequest{" +
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
                '}';
    }
}