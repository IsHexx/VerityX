package com.example.verityx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysConfigDTO;
import com.example.verityx.dto.request.SysConfigCreateRequest;
import com.example.verityx.dto.request.SysConfigUpdateRequest;
import com.example.verityx.service.SysConfigService;
import com.example.verityx.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统参数配置控制器
 */
@Tag(name = "系统参数配置", description = "系统参数配置的增删改查接口")
@RestController
@RequestMapping("/api/sys/configs")
public class SysConfigController {

    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 分页查询系统配置列表
     */
    @Operation(summary = "分页查询系统配置列表", description = "根据条件分页查询系统配置列表")
    @GetMapping
    public Result<IPage<SysConfigDTO>> getConfigPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "参数名称") @RequestParam(required = false) String configName,
            @Parameter(description = "参数键名") @RequestParam(required = false) String configKey,
            @Parameter(description = "参数类型") @RequestParam(required = false) String configType) {
        
        IPage<SysConfigDTO> configPage = sysConfigService.getConfigPage(page, size, configName, configKey, configType);
        return Result.success(configPage);
    }

    /**
     * 获取系统配置详情
     */
    @Operation(summary = "获取系统配置详情", description = "根据配置ID获取系统配置详情")
    @GetMapping("/{id}")
    public Result<SysConfigDTO> getConfigById(@Parameter(description = "配置ID") @PathVariable Long id) {
        SysConfigDTO config = sysConfigService.getConfigById(id);
        return Result.success(config);
    }

    /**
     * 根据配置键名获取配置
     */
    @Operation(summary = "根据配置键名获取配置", description = "根据配置键名获取系统配置详情")
    @GetMapping("/key/{configKey}")
    public Result<SysConfigDTO> getConfigByKey(@Parameter(description = "配置键名") @PathVariable String configKey) {
        SysConfigDTO config = sysConfigService.getConfigByKey(configKey);
        return Result.success(config);
    }

    /**
     * 根据配置键名获取配置值
     */
    @Operation(summary = "根据配置键名获取配置值", description = "根据配置键名获取系统配置值")
    @GetMapping("/value/{configKey}")
    public Result<String> getConfigValue(@Parameter(description = "配置键名") @PathVariable String configKey) {
        String value = sysConfigService.getConfigValue(configKey);
        return Result.success(value);
    }

    /**
     * 获取多个配置键值对
     */
    @Operation(summary = "获取多个配置键值对", description = "根据配置键名列表获取多个系统配置键值对")
    @PostMapping("/values")
    public Result<Map<String, String>> getConfigMap(@RequestBody List<String> configKeys) {
        Map<String, String> configMap = sysConfigService.getConfigMap(configKeys);
        return Result.success(configMap);
    }

    /**
     * 创建系统配置
     */
    @Operation(summary = "创建系统配置", description = "创建新的系统配置")
    @PostMapping
    public Result<Long> createConfig(@Valid @RequestBody SysConfigCreateRequest request) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        Long configId = sysConfigService.createConfig(request, operatorId);
        return Result.success(configId);
    }

    /**
     * 更新系统配置
     */
    @Operation(summary = "更新系统配置", description = "更新系统配置信息")
    @PutMapping
    public Result<Boolean> updateConfig(@Valid @RequestBody SysConfigUpdateRequest request) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysConfigService.updateConfig(request, operatorId);
        return Result.success(result);
    }

    /**
     * 批量更新系统配置
     */
    @Operation(summary = "批量更新系统配置", description = "批量更新系统配置信息")
    @PutMapping("/batch")
    public Result<Boolean> batchUpdateConfig(@Valid @RequestBody List<SysConfigUpdateRequest> requests) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysConfigService.batchUpdateConfig(requests, operatorId);
        return Result.success(result);
    }

    /**
     * 删除系统配置
     */
    @Operation(summary = "删除系统配置", description = "根据配置ID删除系统配置")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteConfig(@Parameter(description = "配置ID") @PathVariable Long id) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysConfigService.deleteConfig(id, operatorId);
        return Result.success(result);
    }

    /**
     * 批量删除系统配置
     */
    @Operation(summary = "批量删除系统配置", description = "根据配置ID列表批量删除系统配置")
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteConfig(@RequestBody List<Long> ids) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysConfigService.batchDeleteConfig(ids, operatorId);
        return Result.success(result);
    }

    /**
     * 刷新系统配置缓存
     */
    @Operation(summary = "刷新系统配置缓存", description = "刷新系统配置缓存")
    @PostMapping("/refresh")
    public Result<Boolean> refreshCache() {
        boolean result = sysConfigService.refreshCache();
        return Result.success(result);
    }
} 