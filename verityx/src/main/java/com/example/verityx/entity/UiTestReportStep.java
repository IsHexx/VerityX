package com.example.verityx.entity;

import java.util.Date;

/**
 * UI测试报告步骤实体类
 */
public class UiTestReportStep {
    
    private Long id;
    private Long reportDetailId;
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
    private String errorMessage;
    private Date executedAt;
    private Date createdAt;
    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportDetailId() {
        return reportDetailId;
    }

    public void setReportDetailId(Long reportDetailId) {
        this.reportDetailId = reportDetailId;
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Date getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(Date executedAt) {
        this.executedAt = executedAt;
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