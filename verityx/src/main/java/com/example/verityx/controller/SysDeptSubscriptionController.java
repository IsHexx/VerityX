package com.example.verityx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysDeptSubscriptionDTO;
import com.example.verityx.dto.request.SysDeptSubscriptionRequest;
import com.example.verityx.service.SysDeptSubscriptionService;
import com.example.verityx.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门订阅控制器
 */
@Tag(name = "部门订阅管理", description = "部门订阅的增删改查接口")
@RestController
@RequestMapping("/api/sys/dept-subscriptions")
public class SysDeptSubscriptionController {

    @Autowired
    private SysDeptSubscriptionService deptSubscriptionService;

    /**
     * 分页查询部门订阅列表
     */
    @Operation(summary = "分页查询部门订阅列表", description = "根据条件分页查询部门订阅列表")
    @GetMapping
    public Result<IPage<SysDeptSubscriptionDTO>> getDeptSubscriptionPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "部门ID") @RequestParam(required = false) Long deptId,
            @Parameter(description = "规则ID") @RequestParam(required = false) Long ruleId,
            @Parameter(description = "状态") @RequestParam(required = false) Boolean status) {
        
        IPage<SysDeptSubscriptionDTO> pageResult = deptSubscriptionService.getDeptSubscriptionPage(page, size, deptId, ruleId, status);
        return Result.success(pageResult);
    }

    /**
     * 获取部门订阅详情
     */
    @Operation(summary = "获取部门订阅详情", description = "根据部门订阅ID获取详情")
    @GetMapping("/{id}")
    public Result<SysDeptSubscriptionDTO> getDeptSubscriptionById(@Parameter(description = "订阅ID") @PathVariable Long id) {
        SysDeptSubscriptionDTO deptSubscription = deptSubscriptionService.getDeptSubscriptionById(id);
        return Result.success(deptSubscription);
    }

    /**
     * 根据部门ID获取订阅列表
     */
    @Operation(summary = "根据部门ID获取订阅列表", description = "根据部门ID获取该部门的所有订阅")
    @GetMapping("/dept/{deptId}")
    public Result<List<SysDeptSubscriptionDTO>> getDeptSubscriptionByDeptId(@Parameter(description = "部门ID") @PathVariable Long deptId) {
        List<SysDeptSubscriptionDTO> deptSubscriptions = deptSubscriptionService.getDeptSubscriptionByDeptId(deptId);
        return Result.success(deptSubscriptions);
    }

    /**
     * 根据规则ID获取部门订阅列表
     */
    @Operation(summary = "根据规则ID获取部门订阅列表", description = "根据规则ID获取订阅该规则的所有部门")
    @GetMapping("/rule/{ruleId}")
    public Result<List<SysDeptSubscriptionDTO>> getDeptSubscriptionByRuleId(@Parameter(description = "规则ID") @PathVariable Long ruleId) {
        List<SysDeptSubscriptionDTO> deptSubscriptions = deptSubscriptionService.getDeptSubscriptionByRuleId(ruleId);
        return Result.success(deptSubscriptions);
    }

    /**
     * 创建部门订阅
     */
    @Operation(summary = "创建部门订阅", description = "创建新的部门订阅")
    @PostMapping
    public Result<Long> createDeptSubscription(@Valid @RequestBody SysDeptSubscriptionRequest request) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        Long id = deptSubscriptionService.createDeptSubscription(request, operatorId);
        return Result.success(id);
    }

    /**
     * 更新部门订阅
     */
    @Operation(summary = "更新部门订阅", description = "更新部门订阅信息")
    @PutMapping
    public Result<Boolean> updateDeptSubscription(@Valid @RequestBody SysDeptSubscriptionRequest request) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = deptSubscriptionService.updateDeptSubscription(request, operatorId);
        return Result.success(result);
    }

    /**
     * 删除部门订阅
     */
    @Operation(summary = "删除部门订阅", description = "根据部门订阅ID删除")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteDeptSubscription(@Parameter(description = "订阅ID") @PathVariable Long id) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = deptSubscriptionService.deleteDeptSubscription(id, operatorId);
        return Result.success(result);
    }

    /**
     * 批量删除部门订阅
     */
    @Operation(summary = "批量删除部门订阅", description = "根据部门订阅ID列表批量删除")
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteDeptSubscription(@RequestBody List<Long> ids) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = deptSubscriptionService.batchDeleteDeptSubscription(ids, operatorId);
        return Result.success(result);
    }

    /**
     * 修改部门订阅状态
     */
    @Operation(summary = "修改部门订阅状态", description = "启用或禁用部门订阅")
    @PutMapping("/{id}/status")
    public Result<Boolean> updateDeptSubscriptionStatus(
            @Parameter(description = "订阅ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Boolean status) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = deptSubscriptionService.updateDeptSubscriptionStatus(id, status, operatorId);
        return Result.success(result);
    }
}