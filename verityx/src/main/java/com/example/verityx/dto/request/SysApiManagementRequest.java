package com.example.verityx.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * API管理请求DTO
 */
@Data
@Schema(description = "API管理请求DTO")
public class SysApiManagementRequest {

    @Schema(description = "主键ID，更新时必填")
    private Long id;

    @NotBlank(message = "API名称不能为空")
    @Size(max = 100, message = "API名称长度不能超过100")
    @Schema(description = "API名称", required = true)
    private String name;

    @Size(max = 255, message = "API描述长度不能超过255")
    @Schema(description = "API描述")
    private String description;

    @Size(max = 255, message = "回调URL长度不能超过255")
    @Schema(description = "回调URL")
    private String callbackUrl;

    @Size(max = 500, message = "权限范围长度不能超过500")
    @Schema(description = "权限范围（逗号分隔）")
    private String permissions;

    @Schema(description = "速率限制（每小时请求数）")
    private Integer rateLimit;

    @Schema(description = "过期日期")
    private LocalDateTime expirationDate;

    @NotNull(message = "状态不能为空")
    @Schema(description = "状态：0-禁用，1-启用", required = true)
    private Boolean status;
} 