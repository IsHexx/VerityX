package com.example.verityx.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.verityx.dto.PageResult;
import com.example.verityx.util.Result;
import com.example.verityx.dto.UiTestReportCreateRequest;
import com.example.verityx.dto.UiTestReportDTO;
import com.example.verityx.dto.UiTestReportDetailDTO;
import com.example.verityx.dto.UiTestStepResultDTO;
import com.example.verityx.service.UiTestReportService;

/**
 * UI测试报告控制器
 */
@RestController
@RequestMapping("/api/ui-test-reports")
public class UiTestReportController {
    
    @Autowired
    private UiTestReportService uiTestReportService;
    
    /**
     * 创建测试报告
     * 
     * @param request 创建请求
     * @return 报告ID
     */
    @PostMapping
    public Result<Long> createReport(@RequestBody UiTestReportCreateRequest request) {
        Long id = uiTestReportService.createReport(request);
        return Result.success(id);
    }
    
    /**
     * 从执行生成报告
     * 
     * @param executionId 执行ID
     * @return 报告ID
     */
    @PostMapping("/generate/{executionId}")
    public Result<Long> generateReportFromExecution(@PathVariable("executionId") Long executionId) {
        Long reportId = uiTestReportService.generateReportFromExecution(executionId);
        if (reportId != null) {
            return Result.success(reportId);
        } else {
            return Result.error(500, "生成报告失败");
        }
    }
    
    /**
     * 获取测试报告
     * 
     * @param id 报告ID
     * @return 测试报告
     */
    @GetMapping("/{id}")
    public Result<UiTestReportDTO> getReportById(@PathVariable("id") Long id) {
        UiTestReportDTO report = uiTestReportService.getReportById(id);
        if (report != null) {
            return Result.success(report);
        } else {
            return Result.error(404, "报告不存在");
        }
    }
    
    /**
     * 根据执行ID获取测试报告
     * 
     * @param executionId 执行ID
     * @return 测试报告
     */
    @GetMapping("/execution/{executionId}")
    public Result<UiTestReportDTO> getReportByExecutionId(@PathVariable("executionId") Long executionId) {
        UiTestReportDTO report = uiTestReportService.getReportByExecutionId(executionId);
        if (report != null) {
            return Result.success(report);
        } else {
            return Result.error(404, "报告不存在");
        }
    }
    
    /**
     * 更新测试报告
     * 
     * @param id 报告ID
     * @param request 更新请求
     * @return 成功标志
     */
    @PutMapping("/{id}")
    public Result<Void> updateReport(@PathVariable("id") Long id, @RequestBody UiTestReportCreateRequest request) {
        boolean success = uiTestReportService.updateReport(id, request);
        if (success) {
            return Result.success();
        } else {
            return Result.error(404, "报告不存在或更新失败");
        }
    }
    
    /**
     * 删除测试报告
     * 
     * @param id 报告ID
     * @return 成功标志
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteReport(@PathVariable("id") Long id) {
        boolean success = uiTestReportService.deleteReport(id);
        if (success) {
            return Result.success();
        } else {
            return Result.error(404, "报告不存在或删除失败");
        }
    }
    
    /**
     * 批量删除测试报告
     * 
     * @param ids ID列表
     * @return 成功标志
     */
    @PostMapping("/batch-delete")
    public Result<Void> batchDeleteReports(@RequestBody List<Long> ids) {
        boolean success = uiTestReportService.batchDeleteReports(ids);
        if (success) {
            return Result.success();
        } else {
            return Result.error(500, "批量删除失败");
        }
    }
    
    /**
     * 获取测试报告列表
     * 
     * @param page 页码
     * @param pageSize 页大小
     * @param keyword 关键字
     * @param reportType 报告类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param status 报告状态(passed/failed)
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result<PageResult<UiTestReportDTO>> getReportList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "reportType", required = false) String reportType,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "status", required = false) String status) {
        
        PageResult<UiTestReportDTO> result = uiTestReportService.getReportList(
                page, pageSize, keyword, reportType, startDate, endDate, status);
        
        return Result.success(result);
    }
    
    /**
     * 获取测试报告详情列表
     * 
     * @param reportId 报告ID
     * @param page 页码
     * @param pageSize 页大小
     * @param result 测试结果
     * @return 分页结果
     */
    @GetMapping("/{reportId}/details")
    public Result<PageResult<UiTestReportDetailDTO>> getReportDetails(
            @PathVariable("reportId") Long reportId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "result", required = false) String result) {
        
        PageResult<UiTestReportDetailDTO> details = uiTestReportService.getReportDetails(
                reportId, page, pageSize, result);
        
        return Result.success(details);
    }
    
    /**
     * 获取测试报告详情
     * 
     * @param detailId 详情ID
     * @return 测试报告详情
     */
    @GetMapping("/details/{detailId}")
    public Result<UiTestReportDetailDTO> getReportDetail(@PathVariable("detailId") Long detailId) {
        UiTestReportDetailDTO detail = uiTestReportService.getReportDetail(detailId);
        if (detail != null) {
            return Result.success(detail);
        } else {
            return Result.error(404, "报告详情不存在");
        }
    }
    
    /**
     * 获取测试步骤列表
     * 
     * @param detailId 详情ID
     * @return 测试步骤结果列表
     */
    @GetMapping("/details/{detailId}/steps")
    public Result<List<UiTestStepResultDTO>> getReportSteps(@PathVariable("detailId") Long detailId) {
        List<UiTestStepResultDTO> steps = uiTestReportService.getReportSteps(detailId);
        return Result.success(steps);
    }
    
    /**
     * 导出测试报告
     * 
     * @param id 报告ID
     * @param format 导出格式
     * @return 导出的文件URL
     */
    @GetMapping("/{id}/export")
    public Result<String> exportReport(
            @PathVariable("id") Long id,
            @RequestParam(value = "format", defaultValue = "html") String format) {
        
        String exportUrl = uiTestReportService.exportReport(id, format);
        if (exportUrl != null) {
            return Result.success(exportUrl);
        } else {
            return Result.error(404, "报告不存在或导出失败");
        }
    }
} 