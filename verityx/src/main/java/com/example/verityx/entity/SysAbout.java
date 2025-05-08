package com.example.verityx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 系统基本信息实体类
 */
@TableName("sys_about")
public class SysAbout {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 系统名称
     */
    @TableField("system_name")
    private String systemName;
    
    /**
     * 系统Logo路径
     */
    @TableField("system_logo")
    private String systemLogo;
    
    /**
     * 公司名称
     */
    @TableField("company_name")
    private String companyName;
    
    /**
     * 公司网站
     */
    @TableField("company_website")
    private String companyWebsite;
    
    /**
     * 联系邮箱
     */
    @TableField("contact_email")
    private String contactEmail;
    
    /**
     * 版权信息
     */
    @TableField("copyright")
    private String copyright;
    
    /**
     * 系统描述
     */
    @TableField("description")
    private String description;
    
    /**
     * 服务条款
     */
    @TableField("terms_of_service")
    private String termsOfService;
    
    /**
     * 隐私政策
     */
    @TableField("privacy_policy")
    private String privacyPolicy;
    
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
        return "SysAbout{" +
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
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
} 