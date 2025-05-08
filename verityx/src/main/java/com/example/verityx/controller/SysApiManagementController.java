package com.example.verityx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysApiManagementDTO;
import com.example.verityx.dto.request.SysApiManagementRequest;
import com.example.verityx.service.SysApiManagementService;
import com.example.verityx.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API管理控制器
 */
@Tag(name = "API管理", description = "API密钥管理的增删改查接口")
@RestController
@RequestMapping("/api/sys/api-management")
public class SysApiManagementController {

    @Autowired
    private SysApiManagementService apiManagementService;

    /**
     * 分页查询API管理列表
     */
    @Operation(summary = "分页查询API管理列表", description = "根据条件分页查询API管理列表")
    @GetMapping
    public Result<IPage<SysApiManagementDTO>> getApiManagementPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "API名称") @RequestParam(required = false) String name,
            @Parameter(description = "状态") @RequestParam(required = false) Boolean status) {
        
        IPage<SysApiManagementDTO> pageResult = apiManagementService.getApiManagementPage(page, size, name, status);
        return Result.success(pageResult);
    }

    /**
     * 获取API管理详情
     */
    @Operation(summary = "获取API管理详情", description = "根据API ID获取详情")
    @GetMapping("/{id}")
    public Result<SysApiManagementDTO> getApiManagementById(@Parameter(description = "API ID") @PathVariable Long id) {
        SysApiManagementDTO apiManagement = apiManagementService.getApiManagementById(id);
        return Result.success(apiManagement);
    }

    /**
     * 创建API管理
     */
    @Operation(summary = "创建API管理", description = "创建新的API密钥")
    @PostMapping
    public Result<Long> createApiManagement(@Valid @RequestBody SysApiManagementRequest request) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        Long id = apiManagementService.createApiManagement(request, operatorId);
        return Result.success(id);
    }

    /**
     * 更新API管理
     */
    @Operation(summary = "更新API管理", description = "更新API密钥信息")
    @PutMapping
    public Result<Boolean> updateApiManagement(@Valid @RequestBody SysApiManagementRequest request) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = apiManagementService.updateApiManagement(request, operatorId);
        return Result.success(result);
    }

    /**
     * 删除API管理
     */
    @Operation(summary = "删除API管理", description = "根据API ID删除")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteApiManagement(@Parameter(description = "API ID") @PathVariable Long id) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = apiManagementService.deleteApiManagement(id, operatorId);
        return Result.success(result);
    }

    /**
     * 批量删除API管理
     */
    @Operation(summary = "批量删除API管理", description = "根据API ID列表批量删除")
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteApiManagement(@RequestBody List<Long> ids) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = apiManagementService.batchDeleteApiManagement(ids, operatorId);
        return Result.success(result);
    }

    /**
     * 修改API管理状态
     */
    @Operation(summary = "修改API管理状态", description = "启用或禁用API密钥")
    @PutMapping("/{id}/status")
    public Result<Boolean> updateApiManagementStatus(
            @Parameter(description = "API ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Boolean status) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = apiManagementService.updateApiManagementStatus(id, status, operatorId);
        return Result.success(result);
    }

    /**
     * 重新生成API密钥
     */
    @Operation(summary = "重新生成API密钥", description = "重新生成API密钥和密钥")
    @PostMapping("/{id}/regenerate")
    public Result<String> regenerateApiKey(@Parameter(description = "API ID") @PathVariable Long id) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        String apiKey = apiManagementService.regenerateApiKey(id, operatorId);
        return Result.success(apiKey);
    }
} 