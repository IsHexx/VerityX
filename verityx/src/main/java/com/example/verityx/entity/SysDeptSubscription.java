package com.example.verityx.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 部门订阅实体类
 */
@Data
@TableName("sys_dept_subscription")
@Schema(description = "部门订阅实体")
public class SysDeptSubscription {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "规则ID")
    private Long ruleId;

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