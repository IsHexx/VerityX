package com.example.verityx.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知模板实体类
 */
@Data
@TableName("sys_notification_template")
@Schema(description = "通知模板实体")
public class SysNotificationTemplate {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "模板名称")
    private String name;

    @Schema(description = "模板编码")
    private String code;

    @Schema(description = "模板类型（EMAIL-邮件, SMS-短信, SYSTEM-系统通知）")
    private String type;

    @Schema(description = "模板标题")
    private String title;

    @Schema(description = "模板内容")
    private String content;

    @Schema(description = "模板参数（JSON格式）")
    private String params;

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