package com.example.verityx.dto;

import java.util.Date;
import java.util.List;

/**
 * UI测试报告详情DTO
 */
public class UiTestReportDetailDTO {
    
    private Long id;
    private Long reportId;
    private Long executionDetailId;
    private String caseName;
    private String caseType;
    private String caseResult;
    private Integer duration;
    private Integer totalSteps;
    private Integer passedSteps;
    private Integer failedSteps;
    private Integer skippedSteps;
    private String errorMessage;
    private List<String> screenshotUrls;
    private Date startTime;
    private Date endTime;
    
    // 附加字段，用于前端展示
    private String durationText;
    private String resultType;
    private List<UiTestStepResultDTO> steps;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Long getExecutionDetailId() {
        return executionDetailId;
    }

    public void setExecutionDetailId(Long executionDetailId) {
        this.executionDetailId = executionDetailId;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getCaseResult() {
        return caseResult;
    }

    public void setCaseResult(String caseResult) {
        this.caseResult = caseResult;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(Integer totalSteps) {
        this.totalSteps = totalSteps;
    }

    public Integer getPassedSteps() {
        return passedSteps;
    }

    public void setPassedSteps(Integer passedSteps) {
        this.passedSteps = passedSteps;
    }

    public Integer getFailedSteps() {
        return failedSteps;
    }

    public void setFailedSteps(Integer failedSteps) {
        this.failedSteps = failedSteps;
    }

    public Integer getSkippedSteps() {
        return skippedSteps;
    }

    public void setSkippedSteps(Integer skippedSteps) {
        this.skippedSteps = skippedSteps;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<String> getScreenshotUrls() {
        return screenshotUrls;
    }

    public void setScreenshotUrls(List<String> screenshotUrls) {
        this.screenshotUrls = screenshotUrls;
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

    public String getDurationText() {
        return durationText;
    }

    public void setDurationText(String durationText) {
        this.durationText = durationText;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public List<UiTestStepResultDTO> getSteps() {
        return steps;
    }

    public void setSteps(List<UiTestStepResultDTO> steps) {
        this.steps = steps;
    }
} 