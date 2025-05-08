package com.example.verityx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysNotificationRuleDTO;
import com.example.verityx.dto.request.SysNotificationRuleRequest;
import com.example.verityx.service.SysNotificationRuleService;
import com.example.verityx.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知规则控制器
 */
@Tag(name = "通知规则管理", description = "通知规则的增删改查接口")
@RestController
@RequestMapping("/api/sys/notification-rules")
public class SysNotificationRuleController {

    @Autowired
    private SysNotificationRuleService ruleService;

    /**
     * 分页查询通知规则列表
     */
    @Operation(summary = "分页查询通知规则列表", description = "根据条件分页查询通知规则列表")
    @GetMapping
    public Result<IPage<SysNotificationRuleDTO>> getRulePage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "规则名称") @RequestParam(required = false) String name,
            @Parameter(description = "规则编码") @RequestParam(required = false) String code,
            @Parameter(description = "事件类型") @RequestParam(required = false) String eventType,
            @Parameter(description = "事件动作") @RequestParam(required = false) String eventAction,
            @Parameter(description = "状态") @RequestParam(required = false) Boolean status) {
        
        IPage<SysNotificationRuleDTO> pageResult = ruleService.getRulePage(page, size, name, code, eventType, eventAction, status);
        return Result.success(pageResult);
    }

    /**
     * 获取通知规则详情
     */
    @Operation(summary = "获取通知规则详情", description = "根据通知规则ID获取详情")
    @GetMapping("/{id}")
    public Result<SysNotificationRuleDTO> getRuleById(@Parameter(description = "规则ID") @PathVariable Long id) {
        SysNotificationRuleDTO rule = ruleService.getRuleById(id);
        return Result.success(rule);
    }

    /**
     * 根据编码获取通知规则
     */
    @Operation(summary = "根据编码获取通知规则", description = "根据通知规则编码获取详情")
    @GetMapping("/code/{code}")
    public Result<SysNotificationRuleDTO> getRuleByCode(@Parameter(description = "规则编码") @PathVariable String code) {
        SysNotificationRuleDTO rule = ruleService.getRuleByCode(code);
        return Result.success(rule);
    }

    /**
     * 根据事件类型和事件动作获取规则列表
     */
    @Operation(summary = "根据事件类型和事件动作获取规则列表", description = "根据事件类型和事件动作获取启用的规则列表")
    @GetMapping("/event")
    public Result<List<SysNotificationRuleDTO>> getRulesByEvent(
            @Parameter(description = "事件类型") @RequestParam String eventType,
            @Parameter(description = "事件动作") @RequestParam String eventAction) {
        List<SysNotificationRuleDTO> rules = ruleService.getRulesByEvent(eventType, eventAction);
        return Result.success(rules);
    }

    /**
     * 创建通知规则
     */
    @Operation(summary = "创建通知规则", description = "创建新的通知规则")
    @PostMapping
    public Result<Long> createRule(@Valid @RequestBody SysNotificationRuleRequest request) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        Long id = ruleService.createRule(request, operatorId);
        return Result.success(id);
    }

    /**
     * 更新通知规则
     */
    @Operation(summary = "更新通知规则", description = "更新通知规则信息")
    @PutMapping
    public Result<Boolean> updateRule(@Valid @RequestBody SysNotificationRuleRequest request) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = ruleService.updateRule(request, operatorId);
        return Result.success(result);
    }

    /**
     * 删除通知规则
     */
    @Operation(summary = "删除通知规则", description = "根据通知规则ID删除")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteRule(@Parameter(description = "规则ID") @PathVariable Long id) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = ruleService.deleteRule(id, operatorId);
        return Result.success(result);
    }

    /**
     * 批量删除通知规则
     */
    @Operation(summary = "批量删除通知规则", description = "根据通知规则ID列表批量删除")
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteRule(@RequestBody List<Long> ids) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = ruleService.batchDeleteRule(ids, operatorId);
        return Result.success(result);
    }

    /**
     * 修改通知规则状态
     */
    @Operation(summary = "修改通知规则状态", description = "启用或禁用通知规则")
    @PutMapping("/{id}/status")
    public Result<Boolean> updateRuleStatus(
            @Parameter(description = "规则ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Boolean status) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = ruleService.updateRuleStatus(id, status, operatorId);
        return Result.success(result);
    }
} 