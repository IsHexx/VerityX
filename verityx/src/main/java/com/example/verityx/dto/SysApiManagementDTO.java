package com.example.verityx.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * API管理DTO
 */
@Data
@Schema(description = "API管理DTO")
public class SysApiManagementDTO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "API名称")
    private String name;

    @Schema(description = "API描述")
    private String description;

    @Schema(description = "API密钥")
    private String apiKey;

    @Schema(description = "回调URL")
    private String callbackUrl;

    @Schema(description = "权限范围（逗号分隔）")
    private String permissions;

    @Schema(description = "速率限制（每小时请求数）")
    private Integer rateLimit;

    @Schema(description = "过期日期")
    private LocalDateTime expirationDate;

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