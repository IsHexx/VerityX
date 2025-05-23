package com.example.verityx.entity;

import java.time.LocalDateTime;

public class TestReport {

    private Integer reportId;             // 报告ID
    private String reportTitle;           // 报告标题
    private Integer createdBy;            // 创建人ID
    private LocalDateTime createdAt;      // 创建时间
    private Integer planId;               // 所属测试计划ID
    private String testVersion;           // 测试版本
    private String participants;          // 参与人员
    private Integer responsiblePerson;    // 负责人
    private String summary;               // 报告概括
    private String conclusion;            // 测试结论
    private String attachments;           // 附件链接或路径
    private String relatedCases;          // 关联用例ID列表
    private String remainingBugs;         // 遗留Bug ID列表
    private String projectId;             // 项目ID

    // Getters and Setters

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public String getTestVersion() {
        return testVersion;
    }

    public void setTestVersion(String testVersion) {
        this.testVersion = testVersion;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public Integer getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(Integer responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getRelatedCases() {
        return relatedCases;
    }

    public void setRelatedCases(String relatedCases) {
        this.relatedCases = relatedCases;
    }

    public String getRemainingBugs() {
        return remainingBugs;
    }

    public void setRemainingBugs(String remainingBugs) {
        this.remainingBugs = remainingBugs;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
