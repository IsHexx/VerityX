package com.example.verityx.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 单点登录配置请求DTO
 */
@Data
@Schema(description = "单点登录配置请求DTO")
public class SysSsoConfigRequest {

    @Schema(description = "主键ID，更新时必填")
    private Long id;

    @NotBlank(message = "配置名称不能为空")
    @Size(max = 100, message = "配置名称长度不能超过100")
    @Schema(description = "配置名称", required = true)
    private String name;

    @NotBlank(message = "类型不能为空")
    @Size(max = 20, message = "类型长度不能超过20")
    @Schema(description = "类型：LDAP、OAUTH、SAML", required = true)
    private String type;

    @Size(max = 50, message = "提供商长度不能超过50")
    @Schema(description = "提供商（如Google、Microsoft等）")
    private String provider;

    @Size(max = 255, message = "服务器URL长度不能超过255")
    @Schema(description = "服务器URL")
    private String serverUrl;

    @Size(max = 100, message = "域名长度不能超过100")
    @Schema(description = "域名")
    private String domain;

    @Size(max = 255, message = "客户端ID长度不能超过255")
    @Schema(description = "客户端ID")
    private String clientId;

    @Size(max = 255, message = "客户端密钥长度不能超过255")
    @Schema(description = "客户端密钥")
    private String clientSecret;

    @Schema(description = "证书内容")
    private String certificate;

    @Size(max = 255, message = "LDAP的Base DN长度不能超过255")
    @Schema(description = "LDAP的Base DN")
    private String baseDn;

    @Size(max = 50, message = "用户名属性长度不能超过50")
    @Schema(description = "用户名属性")
    private String usernameAttribute;

    @Size(max = 50, message = "邮箱属性长度不能超过50")
    @Schema(description = "邮箱属性")
    private String emailAttribute;

    @Schema(description = "附加配置（JSON格式）")
    private String config;

    @NotNull(message = "状态不能为空")
    @Schema(description = "状态：0-禁用，1-启用", required = true)
    private Boolean status;

    @NotNull(message = "是否默认配置不能为空")
    @Schema(description = "是否默认配置：0-否，1-是", required = true)
    private Boolean isDefault;
} 