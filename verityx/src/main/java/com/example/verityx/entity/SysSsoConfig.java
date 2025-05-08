package com.example.verityx.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 单点登录配置实体类
 */
@Data
@TableName("sys_sso_config")
@Schema(description = "单点登录配置实体")
public class SysSsoConfig {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "配置名称")
    private String name;

    @Schema(description = "类型：LDAP、OAUTH、SAML")
    private String type;

    @Schema(description = "提供商（如Google、Microsoft等）")
    private String provider;

    @Schema(description = "服务器URL")
    private String serverUrl;

    @Schema(description = "域名")
    private String domain;

    @Schema(description = "客户端ID")
    private String clientId;

    @Schema(description = "客户端密钥")
    private String clientSecret;

    @Schema(description = "证书内容")
    private String certificate;

    @Schema(description = "LDAP的Base DN")
    private String baseDn;

    @Schema(description = "用户名属性")
    private String usernameAttribute;

    @Schema(description = "邮箱属性")
    private String emailAttribute;

    @Schema(description = "附加配置（JSON格式）")
    private String config;

    @Schema(description = "状态：0-禁用，1-启用")
    private Boolean status;

    @Schema(description = "是否默认配置：0-否，1-是")
    private Boolean isDefault;

    @Schema(description = "创建人ID")
    private Long creatorId;

    @Schema(description = "更新人ID")
    private Long updaterId;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "是否删除：0-未删除，1-已删除")
    @TableLogic
    private Boolean deleted;
} 