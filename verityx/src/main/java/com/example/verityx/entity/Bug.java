package com.example.verityx.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Bug实体")
@TableName("bug")
public class Bug {

    public Integer getBugId() {
        return bugId;
    }

    public void setBugId(Integer bugId) {
        this.bugId = bugId;
    }

    @Schema(description = "缺陷ID，自增主键")
    private Integer bugId;

    @Schema(description = "Bug标题")
    private String bugTitle;

    @Schema(description = "Bug级别（例如：低、中、高）")
    private String level;

    @Schema(description = "缺陷严重级别（例如：轻微、严重、致命）")
    private String severity;

    @Schema(description = "创建人（用户ID）")
    private String createdBy;

    @Schema(description = "创建日期")
    private LocalDateTime createdAt;

    @Schema(description = "指派给（用户ID）")
    private Integer assignedTo;

    @Schema(description = "Bug类型（例如：功能、性能、UI）")
    private String bugType;

    @Schema(description = "状态（例如：open、in progress、closed）")
    private String status;

    @Schema(description = "所属测试计划ID")
    private Integer planId;

    @Schema(description = "所属测试用例ID")
    private Integer caseId;

    @Schema(description = "所属项目ID")
    private Integer projectId;

    @Schema(description = "操作系统")
    private String operatingSystem;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "重现步骤")
    private String reproductionSteps;

    @Schema(description = "附件链接或路径")
    private String attachments;

}
