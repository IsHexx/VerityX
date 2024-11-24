package com.example.verityx.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import lombok.ToString;

@Schema(description = "项目实体")
@ToString
public class Project {

    @Schema(description = "项目ID")
    private Integer projectId;        // 项目ID
    @Schema(description = "项目名称")
    private String projectName;       // 项目名称
    @Schema(description = "项目描述")
    private String description;       // 项目描述
    @Schema(description = "项目开始日期")
    private String startDate;         // 项目开始日期
    @Schema(description = "项目结束日期")
    private String endDate;           // 项目结束日期
    @Schema(description = "项目状态")
    private String status;            // 项目状态


    // Getters and Setters

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
