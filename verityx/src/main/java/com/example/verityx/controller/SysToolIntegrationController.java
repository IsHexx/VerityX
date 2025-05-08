package com.example.verityx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysToolIntegrationDTO;
import com.example.verityx.dto.request.SysToolIntegrationRequest;
import com.example.verityx.service.SysToolIntegrationService;
import com.example.verityx.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 第三方工具集成控制器
 */
@Tag(name = "第三方工具集成管理", description = "第三方工具集成的增删改查接口")
@RestController
@RequestMapping("/api/sys/tool-integrations")
public class SysToolIntegrationController {

    @Autowired
    private SysToolIntegrationService toolIntegrationService;

    /**
     * 分页查询工具集成列表
     */
    @Operation(summary = "分页查询工具集成列表", description = "根据条件分页查询工具集成列表")
    @GetMapping
    public Result<IPage<SysToolIntegrationDTO>> getToolIntegrationPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "集成名称") @RequestParam(required = false) String name,
            @Parameter(description = "集成类型") @RequestParam(required = false) String type,
            @Parameter(description = "状态") @RequestParam(required = false) Boolean status) {
        
        IPage<SysToolIntegrationDTO> pageResult = toolIntegrationService.getToolIntegrationPage(page, size, name, type, status);
        return Result.success(pageResult);
    }

    /**
     * 获取工具集成详情
     */
    @Operation(summary = "获取工具集成详情", description = "根据工具集成ID获取详情")
    @GetMapping("/{id}")
    public Result<SysToolIntegrationDTO> getToolIntegrationById(@Parameter(description = "集成ID") @PathVariable Long id) {
        SysToolIntegrationDTO integration = toolIntegrationService.getToolIntegrationById(id);
        return Result.success(integration);
    }

    /**
     * 创建工具集成
     */
    @Operation(summary = "创建工具集成", description = "创建新的工具集成")
    @PostMapping
    public Result<Long> createToolIntegration(@Valid @RequestBody SysToolIntegrationRequest request) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        Long id = toolIntegrationService.createToolIntegration(request, operatorId);
        return Result.success(id);
    }

    /**
     * 更新工具集成
     */
    @Operation(summary = "更新工具集成", description = "更新工具集成信息")
    @PutMapping
    public Result<Boolean> updateToolIntegration(@Valid @RequestBody SysToolIntegrationRequest request) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = toolIntegrationService.updateToolIntegration(request, operatorId);
        return Result.success(result);
    }

    /**
     * 删除工具集成
     */
    @Operation(summary = "删除工具集成", description = "根据工具集成ID删除")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteToolIntegration(@Parameter(description = "集成ID") @PathVariable Long id) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = toolIntegrationService.deleteToolIntegration(id, operatorId);
        return Result.success(result);
    }

    /**
     * 批量删除工具集成
     */
    @Operation(summary = "批量删除工具集成", description = "根据工具集成ID列表批量删除")
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteToolIntegration(@RequestBody List<Long> ids) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = toolIntegrationService.batchDeleteToolIntegration(ids, operatorId);
        return Result.success(result);
    }

    /**
     * 修改工具集成状态
     */
    @Operation(summary = "修改工具集成状态", description = "启用或禁用工具集成")
    @PutMapping("/{id}/status")
    public Result<Boolean> updateToolIntegrationStatus(
            @Parameter(description = "集成ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Boolean status) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = toolIntegrationService.updateToolIntegrationStatus(id, status, operatorId);
        return Result.success(result);
    }

    /**
     * 测试工具集成连接
     */
    @Operation(summary = "测试工具集成连接", description = "测试工具集成的连接是否正常")
    @PostMapping("/{id}/test-connection")
    public Result<Boolean> testToolIntegrationConnection(@Parameter(description = "集成ID") @PathVariable Long id) {
        boolean result = toolIntegrationService.testToolIntegrationConnection(id);
        return Result.success(result);
    }
} 