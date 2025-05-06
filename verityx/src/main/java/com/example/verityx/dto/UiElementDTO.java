package com.example.verityx.dto;

import java.util.Date;

/**
 * UI元素定位器DTO
 */
public class UiElementDTO {
    private Long elementId;
    private String elementName;
    private Long groupId;
    private String groupName;
    private String locatorType;
    private String locatorValue;
    private String description;
    private String screenshotPath;
    private String createdBy;
    private Date createdAt;
    private Date updatedAt;

    // 验证定位器的请求类
    public static class ValidateRequest {
        private String locatorType;
        private String locatorValue;
        private String url;
        
        public String getLocatorType() {
            return locatorType;
        }
        
        public void setLocatorType(String locatorType) {
            this.locatorType = locatorType;
        }
        
        public String getLocatorValue() {
            return locatorValue;
        }
        
        public void setLocatorValue(String locatorValue) {
            this.locatorValue = locatorValue;
        }
        
        public String getUrl() {
            return url;
        }
        
        public void setUrl(String url) {
            this.url = url;
        }
    }
    
    // 验证定位器的响应类
    public static class ValidateResponse {
        private boolean valid;
        private String message;
        private String screenshotData;
        
        public boolean isValid() {
            return valid;
        }
        
        public void setValid(boolean valid) {
            this.valid = valid;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public String getScreenshotData() {
            return screenshotData;
        }
        
        public void setScreenshotData(String screenshotData) {
            this.screenshotData = screenshotData;
        }
    }

    // Getters and Setters
    public Long getElementId() {
        return elementId;
    }

    public void setElementId(Long elementId) {
        this.elementId = elementId;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getLocatorType() {
        return locatorType;
    }

    public void setLocatorType(String locatorType) {
        this.locatorType = locatorType;
    }

    public String getLocatorValue() {
        return locatorValue;
    }

    public void setLocatorValue(String locatorValue) {
        this.locatorValue = locatorValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScreenshotPath() {
        return screenshotPath;
    }

    public void setScreenshotPath(String screenshotPath) {
        this.screenshotPath = screenshotPath;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
} 