package com.example.verityx.controller;

import com.example.verityx.dto.PageResult;
import com.example.verityx.dto.Result;
import com.example.verityx.dto.UiTestSuiteDTO;
import com.example.verityx.service.UiTestSuiteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "UI测试套件管理", description = "UI测试套件的增删改查接口")
@RestController
@RequestMapping("/api/uitestsuites")
public class UiTestSuiteController {
    
    private static final Logger logger = LoggerFactory.getLogger(UiTestSuiteController.class);

    @Autowired
    private UiTestSuiteService uiTestSuiteService;

    @Operation(summary = "分页查询UI测试套件", description = "根据关键字、状态和分页参数查询UI测试套件")
    @GetMapping("/list")
    public Result<PageResult<UiTestSuiteDTO>> getUiTestSuiteList(
            @Parameter(description = "页码") @RequestParam(value = "page", defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @Parameter(description = "搜索关键词") @RequestParam(value = "keyword", required = false) String keyword,
            @Parameter(description = "套件状态") @RequestParam(value = "status", required = false) String status) {
        
        logger.info("接收到获取UI测试套件列表请求, 参数: page={}, pageSize={}, keyword={}, status={}", page, pageSize, keyword, status);
        PageResult<UiTestSuiteDTO> result = uiTestSuiteService.getUiTestSuiteList(page, pageSize, keyword, status);
        logger.info("UI测试套件列表查询结果: 总数={}, 当前页={}, 每页条数={}", result.getTotal(), result.getPage(), result.getPageSize());
        return Result.success(result);
    }

    @Operation(summary = "获取UI测试套件详情", description = "根据ID获取UI测试套件详情")
    @GetMapping("/{id}")
    public Result<UiTestSuiteDTO> getUiTestSuiteDetail(@PathVariable("id") Long id) {
        logger.info("接收到获取UI测试套件详情请求, ID: {}", id);
        UiTestSuiteDTO uiTestSuite = uiTestSuiteService.getUiTestSuiteById(id);
        logger.info("UI测试套件详情查询成功, ID: {}, 套件名称: {}", id, uiTestSuite.getSuiteName());
        return Result.success(uiTestSuite);
    }

    @Operation(summary = "创建UI测试套件", description = "创建新的UI测试套件")
    @PostMapping("/create")
    public Result<Long> createUiTestSuite(@RequestBody @Valid UiTestSuiteDTO request) {
        logger.info("接收到创建UI测试套件请求, 套件名称: {}, 项目ID: {}", request.getSuiteName(), request.getProjectId());
        // 记录请求中的projectId类型，帮助排查类型转换问题
        logger.debug("请求中projectId的类型: {}, 值: {}", request.getProjectId() != null ? request.getProjectId().getClass().getName() : "null", request.getProjectId());
        
        Long id = uiTestSuiteService.createUiTestSuite(request);
        logger.info("UI测试套件创建成功, 新生成的ID: {}", id);
        return Result.success(id);
    }

    @Operation(summary = "更新UI测试套件", description = "更新UI测试套件信息")
    @PutMapping("/update/{id}")
    public Result<Void> updateUiTestSuite(@PathVariable("id") Long id, @RequestBody @Valid UiTestSuiteDTO request) {
        logger.info("接收到更新UI测试套件请求, ID: {}, 套件名称: {}", id, request.getSuiteName());
        uiTestSuiteService.updateUiTestSuite(id, request);
        logger.info("UI测试套件更新成功, ID: {}", id);
        return Result.success();
    }

    @Operation(summary = "删除UI测试套件", description = "删除UI测试套件")
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteUiTestSuite(@PathVariable("id") Long id) {
        logger.info("接收到删除UI测试套件请求, ID: {}", id);
        uiTestSuiteService.deleteUiTestSuite(id);
        logger.info("UI测试套件删除成功, ID: {}", id);
        return Result.success();
    }

    @Operation(summary = "执行UI测试套件", description = "执行指定的UI测试套件")
    @PostMapping("/execute/{id}")
    public Result<Void> executeUiTestSuite(@PathVariable("id") Long id) {
        logger.info("接收到执行UI测试套件请求, ID: {}", id);
        uiTestSuiteService.executeUiTestSuite(id);
        logger.info("UI测试套件执行命令已发送, ID: {}", id);
        return Result.success();
    }
    
    @Operation(summary = "获取套件中的测试用例", description = "获取指定套件中包含的测试用例")
    @GetMapping("/{id}/cases")
    public Result<List<UiTestSuiteDTO.CaseInfo>> getSuiteCases(@PathVariable("id") Long id) {
        logger.info("接收到获取UI测试套件关联用例请求, 套件ID: {}", id);
        List<UiTestSuiteDTO.CaseInfo> cases = uiTestSuiteService.getSuiteCases(id);
        logger.info("UI测试套件关联用例查询成功, 套件ID: {}, 关联用例数量: {}", id, cases.size());
        return Result.success(cases);
    }
    
    @Operation(summary = "更新套件中的测试用例", description = "更新套件中包含的测试用例")
    @PutMapping("/{id}/cases")
    public Result<Void> updateSuiteCases(
            @PathVariable("id") Long id, 
            @RequestBody UiTestSuiteDTO.UpdateCasesRequest request) {
        logger.info("接收到更新UI测试套件关联用例请求, 套件ID: {}, 用例数量: {}", id, 
                   request.getCaseIds() != null ? request.getCaseIds().size() : 0);
        uiTestSuiteService.updateSuiteCases(id, request.getCaseIds());
        logger.info("UI测试套件关联用例更新成功, 套件ID: {}", id);
        return Result.success();
    }
    
    @Operation(summary = "更新并发配置", description = "更新UI测试套件的并发执行配置")
    @PutMapping("/{id}/concurrency")
    public Result<Void> updateConcurrencyConfig(
            @PathVariable("id") Long id,
            @RequestBody UiTestSuiteDTO.ConcurrencyConfig request) {
        logger.info("接收到更新UI测试套件并发配置请求, 套件ID: {}", id);
        uiTestSuiteService.updateConcurrencyConfig(id, request);
        logger.info("UI测试套件并发配置更新成功, 套件ID: {}", id);
        return Result.success();
    }
    
    @Operation(summary = "获取定时任务配置", description = "获取UI测试套件的定时任务配置")
    @GetMapping("/{id}/schedule")
    public Result<UiTestSuiteDTO.ScheduleConfig> getScheduleConfig(@PathVariable("id") Long id) {
        logger.info("接收到获取UI测试套件定时任务配置请求, 套件ID: {}", id);
        UiTestSuiteDTO.ScheduleConfig config = uiTestSuiteService.getScheduleConfig(id);
        logger.info("UI测试套件定时任务配置获取成功, 套件ID: {}", id);
        return Result.success(config);
    }
    
    @Operation(summary = "保存定时任务配置", description = "创建或更新UI测试套件的定时任务配置")
    @PostMapping("/{id}/schedule")
    public Result<Void> saveScheduleConfig(
            @PathVariable("id") Long id,
            @RequestBody UiTestSuiteDTO.ScheduleConfig request) {
        logger.info("接收到保存UI测试套件定时任务配置请求, 套件ID: {}", id);
        uiTestSuiteService.saveScheduleConfig(id, request);
        logger.info("UI测试套件定时任务配置保存成功, 套件ID: {}", id);
        return Result.success();
    }
    
    @Operation(summary = "删除定时任务配置", description = "删除UI测试套件的定时任务配置")
    @DeleteMapping("/{id}/schedule")
    public Result<Void> deleteScheduleConfig(@PathVariable("id") Long id) {
        logger.info("接收到删除UI测试套件定时任务配置请求, 套件ID: {}", id);
        uiTestSuiteService.deleteScheduleConfig(id);
        logger.info("UI测试套件定时任务配置删除成功, 套件ID: {}", id);
        return Result.success();
    }
} 