package com.example.verityx.dto;

import java.util.Date;
import java.util.List;

/**
 * UI测试报告DTO
 */
public class UiTestReportDTO {
    
    private Long id;
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
    private Double passRate;
    private String executor;
    private String reportUrl;
    private String reportStatus;
    private Date generatedTime;
    private Date createdAt;
    
    // 附加字段，用于前端展示
    private String durationText;
    private String statusText;
    private String statusType;
    private List<UiTestReportDetailDTO> details;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Double getPassRate() {
        return passRate;
    }

    public void setPassRate(Double passRate) {
        this.passRate = passRate;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public Date getGeneratedTime() {
        return generatedTime;
    }

    public void setGeneratedTime(Date generatedTime) {
        this.generatedTime = generatedTime;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getDurationText() {
        return durationText;
    }

    public void setDurationText(String durationText) {
        this.durationText = durationText;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public List<UiTestReportDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<UiTestReportDetailDTO> details) {
        this.details = details;
    }
} 