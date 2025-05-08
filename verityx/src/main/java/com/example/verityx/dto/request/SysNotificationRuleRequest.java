package com.example.verityx.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 通知规则请求DTO
 */
@Data
@Schema(description = "通知规则请求DTO")
public class SysNotificationRuleRequest {

    @Schema(description = "主键ID，更新时必填")
    private Long id;

    @NotBlank(message = "规则名称不能为空")
    @Size(max = 100, message = "规则名称长度不能超过100")
    @Schema(description = "规则名称", required = true)
    private String name;

    @NotBlank(message = "规则编码不能为空")
    @Size(max = 50, message = "规则编码长度不能超过50")
    @Schema(description = "规则编码", required = true)
    private String code;

    @NotBlank(message = "事件类型不能为空")
    @Size(max = 50, message = "事件类型长度不能超过50")
    @Schema(description = "事件类型（TEST_PLAN-测试计划, BUG-缺陷, REPORT-报告等）", required = true)
    private String eventType;

    @NotBlank(message = "事件动作不能为空")
    @Size(max = 50, message = "事件动作长度不能超过50")
    @Schema(description = "事件动作（CREATE-创建, UPDATE-更新, DELETE-删除等）", required = true)
    private String eventAction;

    @NotNull(message = "模板ID不能为空")
    @Schema(description = "模板ID", required = true)
    private Long templateId;

    @NotBlank(message = "通知方式不能为空")
    @Size(max = 50, message = "通知方式长度不能超过50")
    @Schema(description = "通知方式（EMAIL-邮件, SMS-短信, SYSTEM-系统通知）", required = true)
    private String notifyType;

    @NotNull(message = "状态不能为空")
    @Schema(description = "状态：0-禁用，1-启用", required = true)
    private Boolean status;
} 