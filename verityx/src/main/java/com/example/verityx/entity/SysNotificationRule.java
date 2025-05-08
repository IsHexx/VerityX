package com.example.verityx.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知规则实体类
 */
@Data
@TableName("sys_notification_rule")
@Schema(description = "通知规则实体")
public class SysNotificationRule {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "规则名称")
    private String name;

    @Schema(description = "规则编码")
    private String code;

    @Schema(description = "事件类型（TEST_PLAN-测试计划, BUG-缺陷, REPORT-报告等）")
    private String eventType;

    @Schema(description = "事件动作（CREATE-创建, UPDATE-更新, DELETE-删除等）")
    private String eventAction;

    @Schema(description = "模板ID")
    private Long templateId;

    @Schema(description = "通知方式（EMAIL-邮件, SMS-短信, SYSTEM-系统通知）")
    private String notifyType;

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