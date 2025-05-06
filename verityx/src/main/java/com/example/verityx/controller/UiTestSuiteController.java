package com.example.verityx.controller;

import com.example.verityx.dto.PageResult;
import com.example.verityx.dto.Result;
import com.example.verityx.dto.UiTestSuiteDTO;
import com.example.verityx.service.UiTestSuiteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "UI测试套件管理", description = "UI测试套件的增删改查接口")
@RestController
@RequestMapping("/api/uitestsuites")
public class UiTestSuiteController {

    @Autowired
    private UiTestSuiteService uiTestSuiteService;

    @Operation(summary = "分页查询UI测试套件", description = "根据关键字、状态和分页参数查询UI测试套件")
    @GetMapping("/list")
    public Result<PageResult<UiTestSuiteDTO>> getUiTestSuiteList(
            @Parameter(description = "页码") @RequestParam(value = "page", defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @Parameter(description = "搜索关键词") @RequestParam(value = "keyword", required = false) String keyword,
            @Parameter(description = "套件状态") @RequestParam(value = "status", required = false) String status) {
        
        PageResult<UiTestSuiteDTO> result = uiTestSuiteService.getUiTestSuiteList(page, pageSize, keyword, status);
        return Result.success(result);
    }

    @Operation(summary = "获取UI测试套件详情", description = "根据ID获取UI测试套件详情")
    @GetMapping("/{id}")
    public Result<UiTestSuiteDTO> getUiTestSuiteDetail(@PathVariable("id") Long id) {
        UiTestSuiteDTO uiTestSuite = uiTestSuiteService.getUiTestSuiteById(id);
        return Result.success(uiTestSuite);
    }

    @Operation(summary = "创建UI测试套件", description = "创建新的UI测试套件")
    @PostMapping("/create")
    public Result<Long> createUiTestSuite(@RequestBody @Valid UiTestSuiteDTO request) {
        Long id = uiTestSuiteService.createUiTestSuite(request);
        return Result.success(id);
    }

    @Operation(summary = "更新UI测试套件", description = "更新UI测试套件信息")
    @PutMapping("/update/{id}")
    public Result<Void> updateUiTestSuite(@PathVariable("id") Long id, @RequestBody @Valid UiTestSuiteDTO request) {
        uiTestSuiteService.updateUiTestSuite(id, request);
        return Result.success();
    }

    @Operation(summary = "删除UI测试套件", description = "删除UI测试套件")
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteUiTestSuite(@PathVariable("id") Long id) {
        uiTestSuiteService.deleteUiTestSuite(id);
        return Result.success();
    }

    @Operation(summary = "执行UI测试套件", description = "执行指定的UI测试套件")
    @PostMapping("/execute/{id}")
    public Result<Void> executeUiTestSuite(@PathVariable("id") Long id) {
        uiTestSuiteService.executeUiTestSuite(id);
        return Result.success();
    }
    
    @Operation(summary = "获取套件中的测试用例", description = "获取指定套件中包含的测试用例")
    @GetMapping("/{id}/cases")
    public Result<List<UiTestSuiteDTO.CaseInfo>> getSuiteCases(@PathVariable("id") Long id) {
        List<UiTestSuiteDTO.CaseInfo> cases = uiTestSuiteService.getSuiteCases(id);
        return Result.success(cases);
    }
    
    @Operation(summary = "更新套件中的测试用例", description = "更新套件中包含的测试用例")
    @PutMapping("/{id}/cases")
    public Result<Void> updateSuiteCases(
            @PathVariable("id") Long id, 
            @RequestBody UiTestSuiteDTO.UpdateCasesRequest request) {
        uiTestSuiteService.updateSuiteCases(id, request.getCaseIds());
        return Result.success();
    }
} 