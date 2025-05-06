package com.example.verityx.dto;

import java.util.Date;

/**
 * UI测试执行步骤DTO
 */
public class UiTestExecutionStepDTO {
    
    private Long id;
    private Long detailId;
    private Integer stepIndex;
    private String stepName;
    private String actionType;
    private String target;
    private String inputValue;
    private String expectedResult;
    private String actualResult;
    private String status;
    private Integer duration;
    private String screenshotBefore;
    private String screenshotAfter;
    private String screenshotPath;
    private String errorMessage;
    private Date timestamp;
    
    // 用于前端展示的附加字段
    private String statusType;
    private String actionTypeText;
    private String durationText;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getDetailId() {
        return detailId;
    }
    
    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }
    
    public Integer getStepIndex() {
        return stepIndex;
    }
    
    public void setStepIndex(Integer stepIndex) {
        this.stepIndex = stepIndex;
    }
    
    public String getStepName() {
        return stepName;
    }
    
    public void setStepName(String stepName) {
        this.stepName = stepName;
    }
    
    public String getActionType() {
        return actionType;
    }
    
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
    
    public String getTarget() {
        return target;
    }
    
    public void setTarget(String target) {
        this.target = target;
    }
    
    public String getInputValue() {
        return inputValue;
    }
    
    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }
    
    public String getExpectedResult() {
        return expectedResult;
    }
    
    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }
    
    public String getActualResult() {
        return actualResult;
    }
    
    public void setActualResult(String actualResult) {
        this.actualResult = actualResult;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Integer getDuration() {
        return duration;
    }
    
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    
    public String getScreenshotBefore() {
        return screenshotBefore;
    }
    
    public void setScreenshotBefore(String screenshotBefore) {
        this.screenshotBefore = screenshotBefore;
    }
    
    public String getScreenshotAfter() {
        return screenshotAfter;
    }
    
    public void setScreenshotAfter(String screenshotAfter) {
        this.screenshotAfter = screenshotAfter;
    }
    
    public String getScreenshotPath() {
        return screenshotPath;
    }
    
    public void setScreenshotPath(String screenshotPath) {
        this.screenshotPath = screenshotPath;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getStatusType() {
        return statusType;
    }
    
    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }
    
    public String getActionTypeText() {
        return actionTypeText;
    }
    
    public void setActionTypeText(String actionTypeText) {
        this.actionTypeText = actionTypeText;
    }
    
    public String getDurationText() {
        return durationText;
    }
    
    public void setDurationText(String durationText) {
        this.durationText = durationText;
    }
} 