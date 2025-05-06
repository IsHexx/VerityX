package com.example.verityx.controller;

import com.example.verityx.dto.UiTestCaseDTO;
import com.example.verityx.service.UiTestCaseService;
import com.example.verityx.util.JsonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * UI测试用例控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/uitestcases")
@Tag(name = "UI测试用例管理", description = "UI测试用例的增删改查接口")
public class UiTestCaseController {
    private final UiTestCaseService uiTestCaseService;

    public UiTestCaseController(UiTestCaseService uiTestCaseService) {
        this.uiTestCaseService = uiTestCaseService;
    }

    @PostMapping
    @Operation(summary = "创建UI测试用例", description = "创建新的UI测试用例")
    public JsonResult createUiTestCase(@RequestBody UiTestCaseDTO uiTestCaseDTO) {
        try {
            String caseId = uiTestCaseService.createUiTestCase(uiTestCaseDTO);
            return JsonResult.success("创建成功", caseId);
        } catch (Exception e) {
            log.error("创建UI测试用例失败", e);
            return JsonResult.error("创建失败: " + e.getMessage());
        }
    }

    @PutMapping("/{caseId}")
    @Operation(summary = "更新UI测试用例", description = "更新现有UI测试用例")
    public JsonResult updateUiTestCase(
            @Parameter(description = "用例ID") @PathVariable String caseId,
            @RequestBody UiTestCaseDTO uiTestCaseDTO) {
        try {
            uiTestCaseDTO.setCaseId(caseId);
            boolean success = uiTestCaseService.updateUiTestCase(uiTestCaseDTO);
            
            if (success) {
                return JsonResult.success("更新成功");
            } else {
                return JsonResult.error("更新失败: 用例不存在");
            }
        } catch (Exception e) {
            log.error("更新UI测试用例失败", e);
            return JsonResult.error("更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{caseId}")
    @Operation(summary = "删除UI测试用例", description = "删除UI测试用例")
    public JsonResult deleteUiTestCase(
            @Parameter(description = "用例ID") @PathVariable String caseId) {
        try {
            boolean success = uiTestCaseService.deleteUiTestCase(caseId);
            
            if (success) {
                return JsonResult.success("删除成功");
            } else {
                return JsonResult.error("删除失败: 用例不存在");
            }
        } catch (Exception e) {
            log.error("删除UI测试用例失败", e);
            return JsonResult.error("删除失败: " + e.getMessage());
        }
    }

    @GetMapping("/{caseId}")
    @Operation(summary = "获取UI测试用例详情", description = "根据ID获取UI测试用例详情")
    public JsonResult getUiTestCaseDetail(
            @Parameter(description = "用例ID") @PathVariable String caseId) {
        try {
            UiTestCaseDTO uiTestCaseDTO = uiTestCaseService.getUiTestCaseDetail(caseId);
            
            if (uiTestCaseDTO != null) {
                return JsonResult.success(uiTestCaseDTO);
            } else {
                return JsonResult.error("获取失败: 用例不存在");
            }
        } catch (Exception e) {
            log.error("获取UI测试用例详情失败", e);
            return JsonResult.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    @Operation(summary = "分页查询UI测试用例", description = "根据查询条件分页查询UI测试用例")
    public JsonResult getUiTestCaseList(
            @Parameter(description = "搜索关键字") @RequestParam(required = false) String keyword,
            @Parameter(description = "用例状态: all_cases, executed, unexecuted") @RequestParam(required = false, defaultValue = "all_cases") String status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        try {
            Map<String, Object> result = uiTestCaseService.getUiTestCaseList(keyword, status, page, pageSize);
            return JsonResult.success(result);
        } catch (Exception e) {
            log.error("查询UI测试用例列表失败", e);
            return JsonResult.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/{caseId}/execute")
    @Operation(summary = "执行UI测试用例", description = "执行指定的UI测试用例")
    public JsonResult executeUiTestCase(
            @Parameter(description = "用例ID") @PathVariable String caseId) {
        try {
            String executionId = uiTestCaseService.executeUiTestCase(caseId);
            
            if (executionId != null) {
                return JsonResult.success("执行成功", executionId);
            } else {
                return JsonResult.error("执行失败: 用例不存在");
            }
        } catch (Exception e) {
            log.error("执行UI测试用例失败", e);
            return JsonResult.error("执行失败: " + e.getMessage());
        }
    }
} 