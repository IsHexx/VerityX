package com.example.verityx.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 测试计划与测试用例关联实体
 */
@Data
@Schema(description = "测试计划与测试用例关联实体")
@TableName("test_plan_case_rel")
public class TestPlanCaseRel {

    @Schema(description = "关联ID")
    private Integer id;

    @Schema(description = "测试计划ID")
    private Integer planId;

    @Schema(description = "测试用例ID")
    private Integer caseId;

    @Schema(description = "执行顺序")
    private Integer orderIndex;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}
