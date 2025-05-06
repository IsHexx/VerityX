package com.example.verityx.entity;

import java.util.Date;

/**
 * UI测试配置实体类
 */
public class UiTestConfig {
    
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
    private Date createdAt;              // 创建时间
    private Long updatedBy;              // 更新人ID
    private Date updatedAt;              // 更新时间
    
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
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
} 