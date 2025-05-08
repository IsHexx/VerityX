package com.example.verityx.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

/**
 * 系统基本信息请求DTO类
 */
public class SysAboutRequest {
    /**
     * 主键ID（更新时使用）
     */
    private Long id;
    
    /**
     * 系统名称
     */
    @NotBlank(message = "系统名称不能为空")
    private String systemName;
    
    /**
     * 系统Logo路径
     */
    private String systemLogo;
    
    /**
     * 公司名称
     */
    private String companyName;
    
    /**
     * 公司网站
     */
    private String companyWebsite;
    
    /**
     * 联系邮箱
     */
    @Email(message = "联系邮箱格式不正确")
    private String contactEmail;
    
    /**
     * 版权信息
     */
    private String copyright;
    
    /**
     * 系统描述
     */
    private String description;
    
    /**
     * 服务条款
     */
    private String termsOfService;
    
    /**
     * 隐私政策
     */
    private String privacyPolicy;
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getSystemName() {
        return systemName;
    }
    
    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
    
    public String getSystemLogo() {
        return systemLogo;
    }
    
    public void setSystemLogo(String systemLogo) {
        this.systemLogo = systemLogo;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public String getCompanyWebsite() {
        return companyWebsite;
    }
    
    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }
    
    public String getContactEmail() {
        return contactEmail;
    }
    
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
    
    public String getCopyright() {
        return copyright;
    }
    
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getTermsOfService() {
        return termsOfService;
    }
    
    public void setTermsOfService(String termsOfService) {
        this.termsOfService = termsOfService;
    }
    
    public String getPrivacyPolicy() {
        return privacyPolicy;
    }
    
    public void setPrivacyPolicy(String privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }
    
    @Override
    public String toString() {
        return "SysAboutRequest{" +
                "id=" + id +
                ", systemName='" + systemName + '\'' +
                ", systemLogo='" + systemLogo + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyWebsite='" + companyWebsite + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", copyright='" + copyright + '\'' +
                ", description='" + description + '\'' +
                ", termsOfService='" + termsOfService + '\'' +
                ", privacyPolicy='" + privacyPolicy + '\'' +
                '}';
    }
} 