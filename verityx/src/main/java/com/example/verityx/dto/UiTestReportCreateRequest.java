package com.example.verityx.dto;

import java.util.Date;

/**
 * UI测试报告创建请求
 */
public class UiTestReportCreateRequest {

    private Long executionId;
    private String reportName;
    private String reportType;
    private String environment;
    private String browser;
    private Date startTime;
    private Date endTime;
    private Integer duration;
    private Integer totalCases;
    private Integer passedCases;
    private Integer failedCases;
    private Integer skippedCases;
    private String executor;
    private String reportContent;

    public Long getExecutionId() {
        return executionId;
    }

    public void setExecutionId(Long executionId) {
        this.executionId = executionId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getTotalCases() {
        return totalCases;
    }

    public void setTotalCases(Integer totalCases) {
        this.totalCases = totalCases;
    }

    public Integer getPassedCases() {
        return passedCases;
    }

    public void setPassedCases(Integer passedCases) {
        this.passedCases = passedCases;
    }

    public Integer getFailedCases() {
        return failedCases;
    }

    public void setFailedCases(Integer failedCases) {
        this.failedCases = failedCases;
    }

    public Integer getSkippedCases() {
        return skippedCases;
    }

    public void setSkippedCases(Integer skippedCases) {
        this.skippedCases = skippedCases;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }
} 