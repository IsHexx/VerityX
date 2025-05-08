package com.example.verityx.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户订阅请求DTO
 */
@Data
@Schema(description = "用户订阅请求DTO")
public class SysUserSubscriptionRequest {

    @Schema(description = "主键ID，更新时必填")
    private Long id;

    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", required = true)
    private Long userId;

    @NotNull(message = "规则ID不能为空")
    @Schema(description = "规则ID", required = true)
    private Long ruleId;

    @NotBlank(message = "通知方式不能为空")
    @Size(max = 50, message = "通知方式长度不能超过50")
    @Schema(description = "通知方式（EMAIL-邮件, SMS-短信, SYSTEM-系统通知）", required = true)
    private String notifyType;

    @NotNull(message = "状态不能为空")
    @Schema(description = "状态：0-禁用，1-启用", required = true)
    private Boolean status;
} 