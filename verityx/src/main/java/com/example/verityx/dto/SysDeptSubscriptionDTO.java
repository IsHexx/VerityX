package com.example.verityx.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 部门订阅DTO
 */
@Data
@Schema(description = "部门订阅DTO")
public class SysDeptSubscriptionDTO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "规则ID")
    private Long ruleId;

    @Schema(description = "规则名称")
    private String ruleName;

    @Schema(description = "通知方式（EMAIL-邮件, SMS-短信, SYSTEM-系统通知）")
    private String notifyType;

    @Schema(description = "状态：0-禁用，1-启用")
    private Boolean status;

    @Schema(description = "创建人ID")
    private Long creatorId;

    @Schema(description = "创建人姓名")
    private String creatorName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
} 