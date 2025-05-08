package com.example.verityx.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 第三方工具集成DTO
 */
@Data
@Schema(description = "第三方工具集成DTO")
public class SysToolIntegrationDTO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "集成名称")
    private String name;

    @Schema(description = "集成类型（JIRA, GITHUB, GITLAB, JENKINS等）")
    private String type;

    @Schema(description = "服务地址")
    private String url;

    @Schema(description = "API密钥")
    private String apiKey;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "访问令牌")
    private String token;

    @Schema(description = "附加配置（JSON格式）")
    private String config;

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