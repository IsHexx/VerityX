package com.example.verityx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysUserSubscriptionDTO;
import com.example.verityx.dto.request.SysUserSubscriptionRequest;
import com.example.verityx.service.SysUserSubscriptionService;
import com.example.verityx.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户订阅控制器
 */
@Tag(name = "用户订阅管理", description = "用户订阅的增删改查接口")
@RestController
@RequestMapping("/api/sys/user-subscriptions")
public class SysUserSubscriptionController {

    @Autowired
    private SysUserSubscriptionService userSubscriptionService;

    /**
     * 分页查询用户订阅列表
     */
    @Operation(summary = "分页查询用户订阅列表", description = "根据条件分页查询用户订阅列表")
    @GetMapping
    public Result<IPage<SysUserSubscriptionDTO>> getUserSubscriptionPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "规则ID") @RequestParam(required = false) Long ruleId,
            @Parameter(description = "状态") @RequestParam(required = false) Boolean status) {
        
        IPage<SysUserSubscriptionDTO> pageResult = userSubscriptionService.getUserSubscriptionPage(page, size, userId, ruleId, status);
        return Result.success(pageResult);
    }

    /**
     * 获取用户订阅详情
     */
    @Operation(summary = "获取用户订阅详情", description = "根据用户订阅ID获取详情")
    @GetMapping("/{id}")
    public Result<SysUserSubscriptionDTO> getUserSubscriptionById(@Parameter(description = "订阅ID") @PathVariable Long id) {
        SysUserSubscriptionDTO userSubscription = userSubscriptionService.getUserSubscriptionById(id);
        return Result.success(userSubscription);
    }

    /**
     * 根据用户ID获取订阅列表
     */
    @Operation(summary = "根据用户ID获取订阅列表", description = "根据用户ID获取该用户的所有订阅")
    @GetMapping("/user/{userId}")
    public Result<List<SysUserSubscriptionDTO>> getUserSubscriptionByUserId(@Parameter(description = "用户ID") @PathVariable Long userId) {
        List<SysUserSubscriptionDTO> userSubscriptions = userSubscriptionService.getUserSubscriptionByUserId(userId);
        return Result.success(userSubscriptions);
    }

    /**
     * 根据规则ID获取用户订阅列表
     */
    @Operation(summary = "根据规则ID获取用户订阅列表", description = "根据规则ID获取订阅该规则的所有用户")
    @GetMapping("/rule/{ruleId}")
    public Result<List<SysUserSubscriptionDTO>> getUserSubscriptionByRuleId(@Parameter(description = "规则ID") @PathVariable Long ruleId) {
        List<SysUserSubscriptionDTO> userSubscriptions = userSubscriptionService.getUserSubscriptionByRuleId(ruleId);
        return Result.success(userSubscriptions);
    }

    /**
     * 创建用户订阅
     */
    @Operation(summary = "创建用户订阅", description = "创建新的用户订阅")
    @PostMapping
    public Result<Long> createUserSubscription(@Valid @RequestBody SysUserSubscriptionRequest request) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        Long id = userSubscriptionService.createUserSubscription(request, operatorId);
        return Result.success(id);
    }

    /**
     * 更新用户订阅
     */
    @Operation(summary = "更新用户订阅", description = "更新用户订阅信息")
    @PutMapping
    public Result<Boolean> updateUserSubscription(@Valid @RequestBody SysUserSubscriptionRequest request) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = userSubscriptionService.updateUserSubscription(request, operatorId);
        return Result.success(result);
    }

    /**
     * 删除用户订阅
     */
    @Operation(summary = "删除用户订阅", description = "根据用户订阅ID删除")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteUserSubscription(@Parameter(description = "订阅ID") @PathVariable Long id) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = userSubscriptionService.deleteUserSubscription(id, operatorId);
        return Result.success(result);
    }

    /**
     * 批量删除用户订阅
     */
    @Operation(summary = "批量删除用户订阅", description = "根据用户订阅ID列表批量删除")
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteUserSubscription(@RequestBody List<Long> ids) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = userSubscriptionService.batchDeleteUserSubscription(ids, operatorId);
        return Result.success(result);
    }

    /**
     * 修改用户订阅状态
     */
    @Operation(summary = "修改用户订阅状态", description = "启用或禁用用户订阅")
    @PutMapping("/{id}/status")
    public Result<Boolean> updateUserSubscriptionStatus(
            @Parameter(description = "订阅ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Boolean status) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = userSubscriptionService.updateUserSubscriptionStatus(id, status, operatorId);
        return Result.success(result);
    }
} 