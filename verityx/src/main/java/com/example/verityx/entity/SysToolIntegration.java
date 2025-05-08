package com.example.verityx.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 第三方工具集成实体类
 */
@Data
@TableName("sys_tool_integration")
@Schema(description = "第三方工具集成实体")
public class SysToolIntegration {

    @TableId(value = "id", type = IdType.AUTO)
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

    @Schema(description = "密码（加密存储）")
    private String password;

    @Schema(description = "访问令牌")
    private String token;

    @Schema(description = "附加配置（JSON格式）")
    private String config;

    @Schema(description = "状态：0-禁用，1-启用")
    private Boolean status;

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