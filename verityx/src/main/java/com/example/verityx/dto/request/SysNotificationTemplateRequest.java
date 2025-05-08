package com.example.verityx.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 通知模板请求DTO
 */
@Data
@Schema(description = "通知模板请求DTO")
public class SysNotificationTemplateRequest {

    @Schema(description = "主键ID，更新时必填")
    private Long id;

    @NotBlank(message = "模板名称不能为空")
    @Size(max = 100, message = "模板名称长度不能超过100")
    @Schema(description = "模板名称", required = true)
    private String name;

    @NotBlank(message = "模板编码不能为空")
    @Size(max = 50, message = "模板编码长度不能超过50")
    @Schema(description = "模板编码", required = true)
    private String code;

    @NotBlank(message = "模板类型不能为空")
    @Size(max = 50, message = "模板类型长度不能超过50")
    @Schema(description = "模板类型（EMAIL-邮件, SMS-短信, SYSTEM-系统通知）", required = true)
    private String type;

    @NotBlank(message = "模板标题不能为空")
    @Size(max = 200, message = "模板标题长度不能超过200")
    @Schema(description = "模板标题", required = true)
    private String title;

    @NotBlank(message = "模板内容不能为空")
    @Size(max = 2000, message = "模板内容长度不能超过2000")
    @Schema(description = "模板内容", required = true)
    private String content;

    @Schema(description = "模板参数（JSON格式）")
    private String params;

    @NotNull(message = "状态不能为空")
    @Schema(description = "状态：0-禁用，1-启用", required = true)
    private Boolean status;
} 