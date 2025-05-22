package com.example.verityx.controller;

import com.example.verityx.dto.ApiTestReportDTO;
import com.example.verityx.entity.ApiTestReport;
import com.example.verityx.service.ApiTestReportService;
import com.example.verityx.util.JsonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/apitestreports")
@Tag(name = "API测试报告管理", description = "API测试报告的增删改查接口")
public class ApiTestReportController {

    @Autowired
    private ApiTestReportService apiTestReportService;

    /**
     * 分页获取API测试报告列表
     */
    @GetMapping("/list")
    @Operation(summary = "分页查询API测试报告", description = "根据关键字、状态和分页参数查询API测试报告")
    public JsonResult getReportsList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "projectId", required = false) String projectId) {
        
        Map<String, Object> result = apiTestReportService.getReportsByPage(keyword, status, projectId, page, pageSize);
        return JsonResult.success(result);
    }

    /**
     * 获取指定ID的API测试报告
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取单个API测试报告", description = "根据ID获取API测试报告详情")
    public JsonResult getReportById(@PathVariable("id") Integer id) {
        ApiTestReport report = apiTestReportService.getReportById(id);
        if (report == null) {
            return JsonResult.error("测试报告不存在");
        }
        return JsonResult.success(report);
    }

    /**
     * 创建API测试报告
     */
    @PostMapping
    @Operation(summary = "创建API测试报告", description = "创建新的API测试报告")
    public JsonResult createReport(@RequestBody ApiTestReport report) {
        ApiTestReport createdReport = apiTestReportService.createReport(report);
        return JsonResult.success(createdReport);
    }

    /**
     * 更新API测试报告
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新API测试报告", description = "根据ID更新API测试报告")
    public JsonResult updateReport(@PathVariable("id") Integer id, @RequestBody ApiTestReport report) {
        report.setId(id);
        boolean result = apiTestReportService.updateReport(report);
        if (result) {
            return JsonResult.success("更新成功");
        } else {
            return JsonResult.error("更新失败");
        }
    }

    /**
     * 删除API测试报告
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除API测试报告", description = "根据ID删除API测试报告")
    public JsonResult deleteReport(@PathVariable("id") Integer id) {
        boolean result = apiTestReportService.deleteReport(id);
        if (result) {
            return JsonResult.success("删除成功");
        } else {
            return JsonResult.error("删除失败");
        }
    }

    /**
     * 批量删除API测试报告
     */
    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除API测试报告", description = "批量删除API测试报告")
    public JsonResult batchDelete(@RequestBody List<Integer> ids) {
        boolean result = apiTestReportService.batchDeleteReports(ids);
        if (result) {
            return JsonResult.success("批量删除成功");
        } else {
            return JsonResult.error("批量删除失败");
        }
    }

    /**
     * 导出API测试报告
     */
    @GetMapping("/{id}/export")
    @Operation(summary = "导出API测试报告", description = "将API测试报告导出为Markdown文件")
    public ResponseEntity<Resource> exportReport(@PathVariable("id") Integer id) {
        ApiTestReport report = apiTestReportService.getReportById(id);
        if (report == null) {
            return ResponseEntity.notFound().build();
        }

        // 生成报告内容
        StringBuilder sb = new StringBuilder();
        sb.append("# API测试报告\n\n");
        sb.append("## 基本信息\n\n");
        sb.append("- 接口名称: ").append(report.getApiName()).append("\n");
        sb.append("- 接口地址: ").append(report.getUrl()).append("\n");
        sb.append("- 请求方法: ").append(report.getMethod()).append("\n");
        sb.append("- 执行状态: ").append(report.getStatus()).append("\n");
        sb.append("- 响应时间: ").append(report.getResponseTime()).append("ms\n");
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sb.append("- 执行时间: ").append(sdf.format(report.getExecutionTime())).append("\n");
        sb.append("- 执行人员: ").append(report.getExecutor()).append("\n\n");
        
        sb.append("## 请求详情\n\n");
        sb.append("### 请求头\n");
        sb.append("```json\n").append(report.getHeaders()).append("\n```\n\n");
        
        sb.append("### 请求参数\n");
        sb.append("```json\n").append(report.getParams()).append("\n```\n\n");
        
        sb.append("## 响应结果\n\n");
        sb.append("```json\n").append(report.getResponse()).append("\n```\n");

        byte[] reportBytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        ByteArrayResource resource = new ByteArrayResource(reportBytes);

        String filename = "ApiTestReport_" + id + ".md";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(reportBytes.length)
                .body(resource);
    }
} 