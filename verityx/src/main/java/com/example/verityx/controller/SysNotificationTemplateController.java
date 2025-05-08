package com.example.verityx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysNotificationTemplateDTO;
import com.example.verityx.dto.request.SysNotificationTemplateRequest;
import com.example.verityx.service.SysNotificationTemplateService;
import com.example.verityx.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知模板控制器
 */
@Tag(name = "通知模板管理", description = "通知模板的增删改查接口")
@RestController
@RequestMapping("/api/sys/notification-templates")
public class SysNotificationTemplateController {

    @Autowired
    private SysNotificationTemplateService templateService;

    /**
     * 分页查询通知模板列表
     */
    @Operation(summary = "分页查询通知模板列表", description = "根据条件分页查询通知模板列表")
    @GetMapping
    public Result<IPage<SysNotificationTemplateDTO>> getTemplatePage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "模板名称") @RequestParam(required = false) String name,
            @Parameter(description = "模板编码") @RequestParam(required = false) String code,
            @Parameter(description = "模板类型") @RequestParam(required = false) String type,
            @Parameter(description = "状态") @RequestParam(required = false) Boolean status) {
        
        IPage<SysNotificationTemplateDTO> pageResult = templateService.getTemplatePage(page, size, name, code, type, status);
        return Result.success(pageResult);
    }

    /**
     * 获取通知模板详情
     */
    @Operation(summary = "获取通知模板详情", description = "根据通知模板ID获取详情")
    @GetMapping("/{id}")
    public Result<SysNotificationTemplateDTO> getTemplateById(@Parameter(description = "模板ID") @PathVariable Long id) {
        SysNotificationTemplateDTO template = templateService.getTemplateById(id);
        return Result.success(template);
    }

    /**
     * 根据编码获取通知模板
     */
    @Operation(summary = "根据编码获取通知模板", description = "根据通知模板编码获取详情")
    @GetMapping("/code/{code}")
    public Result<SysNotificationTemplateDTO> getTemplateByCode(@Parameter(description = "模板编码") @PathVariable String code) {
        SysNotificationTemplateDTO template = templateService.getTemplateByCode(code);
        return Result.success(template);
    }

    /**
     * 创建通知模板
     */
    @Operation(summary = "创建通知模板", description = "创建新的通知模板")
    @PostMapping
    public Result<Long> createTemplate(@Valid @RequestBody SysNotificationTemplateRequest request) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        Long id = templateService.createTemplate(request, operatorId);
        return Result.success(id);
    }

    /**
     * 更新通知模板
     */
    @Operation(summary = "更新通知模板", description = "更新通知模板信息")
    @PutMapping
    public Result<Boolean> updateTemplate(@Valid @RequestBody SysNotificationTemplateRequest request) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = templateService.updateTemplate(request, operatorId);
        return Result.success(result);
    }

    /**
     * 删除通知模板
     */
    @Operation(summary = "删除通知模板", description = "根据通知模板ID删除")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteTemplate(@Parameter(description = "模板ID") @PathVariable Long id) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = templateService.deleteTemplate(id, operatorId);
        return Result.success(result);
    }

    /**
     * 批量删除通知模板
     */
    @Operation(summary = "批量删除通知模板", description = "根据通知模板ID列表批量删除")
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteTemplate(@RequestBody List<Long> ids) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = templateService.batchDeleteTemplate(ids, operatorId);
        return Result.success(result);
    }

    /**
     * 修改通知模板状态
     */
    @Operation(summary = "修改通知模板状态", description = "启用或禁用通知模板")
    @PutMapping("/{id}/status")
    public Result<Boolean> updateTemplateStatus(
            @Parameter(description = "模板ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Boolean status) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = templateService.updateTemplateStatus(id, status, operatorId);
        return Result.success(result);
    }
} 