package com.example.verityx.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 第三方工具集成请求DTO
 */
@Data
@Schema(description = "第三方工具集成请求DTO")
public class SysToolIntegrationRequest {

    @Schema(description = "主键ID，更新时必填")
    private Long id;

    @NotBlank(message = "集成名称不能为空")
    @Size(max = 100, message = "集成名称长度不能超过100")
    @Schema(description = "集成名称", required = true)
    private String name;

    @NotBlank(message = "集成类型不能为空")
    @Size(max = 50, message = "集成类型长度不能超过50")
    @Schema(description = "集成类型（JIRA, GITHUB, GITLAB, JENKINS等）", required = true)
    private String type;

    @NotBlank(message = "服务地址不能为空")
    @Size(max = 255, message = "服务地址长度不能超过255")
    @Schema(description = "服务地址", required = true)
    private String url;

    @Size(max = 255, message = "API密钥长度不能超过255")
    @Schema(description = "API密钥")
    private String apiKey;

    @Size(max = 100, message = "用户名长度不能超过100")
    @Schema(description = "用户名")
    private String username;

    @Size(max = 255, message = "密码长度不能超过255")
    @Schema(description = "密码")
    private String password;

    @Size(max = 255, message = "访问令牌长度不能超过255")
    @Schema(description = "访问令牌")
    private String token;

    @Schema(description = "附加配置（JSON格式）")
    private String config;

    @NotNull(message = "状态不能为空")
    @Schema(description = "状态：0-禁用，1-启用", required = true)
    private Boolean status;
} 