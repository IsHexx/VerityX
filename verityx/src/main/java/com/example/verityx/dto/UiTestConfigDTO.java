package com.example.verityx.dto;

import com.example.verityx.entity.UiTestConfig;
import java.util.Date;

/**
 * UI测试配置DTO类
 */
public class UiTestConfigDTO {
    
    private Long id;
    private String configName;           // 配置名称
    private String configType;           // 配置类型: BROWSER/WAIT_TIME/SCREENSHOT/RETRY
    private String browserType;          // 浏览器类型: CHROME/FIREFOX/EDGE/SAFARI
    private String browserVersion;       // 浏览器版本
    private String deviceType;           // 设备类型: DESKTOP/MOBILE/TABLET
    private String deviceName;           // 设备名称
    private String platformName;         // 平台名称: WINDOWS/MAC/LINUX/ANDROID/IOS
    private String platformVersion;      // 平台版本
    private Integer defaultWaitTimeout;  // 默认等待超时时间（秒）
    private Integer pageLoadTimeout;     // 页面加载超时时间（秒）
    private Integer scriptTimeout;       // 脚本执行超时时间（秒）
    private String screenshotPolicy;     // 截图策略: ALWAYS/ON_FAILURE/NEVER
    private Integer retryCount;          // 失败重试次数
    private Integer retryInterval;       // 重试间隔（秒）
    private Boolean headless;            // 是否使用无头模式
    private String proxySettings;        // 代理设置，JSON格式
    private String capabilities;         // 自定义功能，JSON格式
    private Boolean isDefault;           // 是否为默认配置
    private Boolean isActive;            // 是否激活
    private Long createdBy;              // 创建人ID
    private String createdByName;        // 创建人名称
    private Date createdAt;              // 创建时间
    private Long updatedBy;              // 更新人ID
    private String updatedByName;        // 更新人名称
    private Date updatedAt;              // 更新时间
    
    public UiTestConfigDTO() {
    }
    
    public UiTestConfigDTO(UiTestConfig entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.configName = entity.getConfigName();
            this.configType = entity.getConfigType();
            this.browserType = entity.getBrowserType();
            this.browserVersion = entity.getBrowserVersion();
            this.deviceType = entity.getDeviceType();
            this.deviceName = entity.getDeviceName();
            this.platformName = entity.getPlatformName();
            this.platformVersion = entity.getPlatformVersion();
            this.defaultWaitTimeout = entity.getDefaultWaitTimeout();
            this.pageLoadTimeout = entity.getPageLoadTimeout();
            this.scriptTimeout = entity.getScriptTimeout();
            this.screenshotPolicy = entity.getScreenshotPolicy();
            this.retryCount = entity.getRetryCount();
            this.retryInterval = entity.getRetryInterval();
            this.headless = entity.getHeadless();
            this.proxySettings = entity.getProxySettings();
            this.capabilities = entity.getCapabilities();
            this.isDefault = entity.getIsDefault();
            this.isActive = entity.getIsActive();
            this.createdBy = entity.getCreatedBy();
            this.createdAt = entity.getCreatedAt();
            this.updatedBy = entity.getUpdatedBy();
            this.updatedAt = entity.getUpdatedAt();
        }
    }
    
    public UiTestConfig toEntity() {
        UiTestConfig entity = new UiTestConfig();
        entity.setId(this.id);
        entity.setConfigName(this.configName);
        entity.setConfigType(this.configType);
        entity.setBrowserType(this.browserType);
        entity.setBrowserVersion(this.browserVersion);
        entity.setDeviceType(this.deviceType);
        entity.setDeviceName(this.deviceName);
        entity.setPlatformName(this.platformName);
        entity.setPlatformVersion(this.platformVersion);
        entity.setDefaultWaitTimeout(this.defaultWaitTimeout);
        entity.setPageLoadTimeout(this.pageLoadTimeout);
        entity.setScriptTimeout(this.scriptTimeout);
        entity.setScreenshotPolicy(this.screenshotPolicy);
        entity.setRetryCount(this.retryCount);
        entity.setRetryInterval(this.retryInterval);
        entity.setHeadless(this.headless);
        entity.setProxySettings(this.proxySettings);
        entity.setCapabilities(this.capabilities);
        entity.setIsDefault(this.isDefault);
        entity.setIsActive(this.isActive);
        entity.setCreatedBy(this.createdBy);
        entity.setCreatedAt(this.createdAt);
        entity.setUpdatedBy(this.updatedBy);
        entity.setUpdatedAt(this.updatedAt);
        return entity;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getConfigName() {
        return configName;
    }
    
    public void setConfigName(String configName) {
        this.configName = configName;
    }
    
    public String getConfigType() {
        return configType;
    }
    
    public void setConfigType(String configType) {
        this.configType = configType;
    }
    
    public String getBrowserType() {
        return browserType;
    }
    
    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }
    
    public String getBrowserVersion() {
        return browserVersion;
    }
    
    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }
    
    public String getDeviceType() {
        return deviceType;
    }
    
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
    
    public String getDeviceName() {
        return deviceName;
    }
    
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    
    public String getPlatformName() {
        return platformName;
    }
    
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }
    
    public String getPlatformVersion() {
        return platformVersion;
    }
    
    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }
    
    public Integer getDefaultWaitTimeout() {
        return defaultWaitTimeout;
    }
    
    public void setDefaultWaitTimeout(Integer defaultWaitTimeout) {
        this.defaultWaitTimeout = defaultWaitTimeout;
    }
    
    public Integer getPageLoadTimeout() {
        return pageLoadTimeout;
    }
    
    public void setPageLoadTimeout(Integer pageLoadTimeout) {
        this.pageLoadTimeout = pageLoadTimeout;
    }
    
    public Integer getScriptTimeout() {
        return scriptTimeout;
    }
    
    public void setScriptTimeout(Integer scriptTimeout) {
        this.scriptTimeout = scriptTimeout;
    }
    
    public String getScreenshotPolicy() {
        return screenshotPolicy;
    }
    
    public void setScreenshotPolicy(String screenshotPolicy) {
        this.screenshotPolicy = screenshotPolicy;
    }
    
    public Integer getRetryCount() {
        return retryCount;
    }
    
    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }
    
    public Integer getRetryInterval() {
        return retryInterval;
    }
    
    public void setRetryInterval(Integer retryInterval) {
        this.retryInterval = retryInterval;
    }
    
    public Boolean getHeadless() {
        return headless;
    }
    
    public void setHeadless(Boolean headless) {
        this.headless = headless;
    }
    
    public String getProxySettings() {
        return proxySettings;
    }
    
    public void setProxySettings(String proxySettings) {
        this.proxySettings = proxySettings;
    }
    
    public String getCapabilities() {
        return capabilities;
    }
    
    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }
    
    public Boolean getIsDefault() {
        return isDefault;
    }
    
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Long getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
    
    public String getCreatedByName() {
        return createdByName;
    }
    
    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Long getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public String getUpdatedByName() {
        return updatedByName;
    }
    
    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
} 