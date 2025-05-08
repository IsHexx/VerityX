package com.example.verityx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysSsoConfigDTO;
import com.example.verityx.dto.request.SysSsoConfigRequest;
import com.example.verityx.service.SysSsoConfigService;
import com.example.verityx.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 单点登录配置控制器
 */
@Tag(name = "单点登录配置管理", description = "单点登录配置的增删改查接口")
@RestController
@RequestMapping("/api/sys/sso-configs")
public class SysSsoConfigController {

    @Autowired
    private SysSsoConfigService ssoConfigService;

    /**
     * 分页查询单点登录配置列表
     */
    @Operation(summary = "分页查询单点登录配置列表", description = "根据条件分页查询单点登录配置列表")
    @GetMapping
    public Result<IPage<SysSsoConfigDTO>> getSsoConfigPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "配置名称") @RequestParam(required = false) String name,
            @Parameter(description = "配置类型") @RequestParam(required = false) String type,
            @Parameter(description = "状态") @RequestParam(required = false) Boolean status) {
        
        IPage<SysSsoConfigDTO> pageResult = ssoConfigService.getSsoConfigPage(page, size, name, type, status);
        return Result.success(pageResult);
    }

    /**
     * 获取单点登录配置详情
     */
    @Operation(summary = "获取单点登录配置详情", description = "根据配置ID获取详情")
    @GetMapping("/{id}")
    public Result<SysSsoConfigDTO> getSsoConfigById(@Parameter(description = "配置ID") @PathVariable Long id) {
        SysSsoConfigDTO ssoConfig = ssoConfigService.getSsoConfigById(id);
        return Result.success(ssoConfig);
    }

    /**
     * 创建单点登录配置
     */
    @Operation(summary = "创建单点登录配置", description = "创建新的单点登录配置")
    @PostMapping
    public Result<Long> createSsoConfig(@Valid @RequestBody SysSsoConfigRequest request) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        Long id = ssoConfigService.createSsoConfig(request, operatorId);
        return Result.success(id);
    }

    /**
     * 更新单点登录配置
     */
    @Operation(summary = "更新单点登录配置", description = "更新单点登录配置信息")
    @PutMapping
    public Result<Boolean> updateSsoConfig(@Valid @RequestBody SysSsoConfigRequest request) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = ssoConfigService.updateSsoConfig(request, operatorId);
        return Result.success(result);
    }

    /**
     * 删除单点登录配置
     */
    @Operation(summary = "删除单点登录配置", description = "根据配置ID删除")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteSsoConfig(@Parameter(description = "配置ID") @PathVariable Long id) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = ssoConfigService.deleteSsoConfig(id, operatorId);
        return Result.success(result);
    }

    /**
     * 批量删除单点登录配置
     */
    @Operation(summary = "批量删除单点登录配置", description = "根据配置ID列表批量删除")
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteSsoConfig(@RequestBody List<Long> ids) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = ssoConfigService.batchDeleteSsoConfig(ids, operatorId);
        return Result.success(result);
    }

    /**
     * 修改单点登录配置状态
     */
    @Operation(summary = "修改单点登录配置状态", description = "启用或禁用单点登录配置")
    @PutMapping("/{id}/status")
    public Result<Boolean> updateSsoConfigStatus(
            @Parameter(description = "配置ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Boolean status) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = ssoConfigService.updateSsoConfigStatus(id, status, operatorId);
        return Result.success(result);
    }

    /**
     * 设置默认单点登录配置
     */
    @Operation(summary = "设置默认单点登录配置", description = "设置指定配置为默认配置")
    @PutMapping("/{id}/default")
    public Result<Boolean> setDefaultSsoConfig(@Parameter(description = "配置ID") @PathVariable Long id) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = ssoConfigService.setDefaultSsoConfig(id, operatorId);
        return Result.success(result);
    }

    /**
     * 测试单点登录配置连接
     */
    @Operation(summary = "测试单点登录配置连接", description = "测试单点登录配置的连接是否正常")
    @PostMapping("/{id}/test-connection")
    public Result<Boolean> testSsoConfigConnection(@Parameter(description = "配置ID") @PathVariable Long id) {
        boolean result = ssoConfigService.testSsoConfigConnection(id);
        return Result.success(result);
    }
} 