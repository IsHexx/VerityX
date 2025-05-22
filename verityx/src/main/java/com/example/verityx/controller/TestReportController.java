package com.example.verityx.controller;

import com.example.verityx.dto.ApiResponse;
import com.example.verityx.entity.Bug;
import com.example.verityx.entity.TestPlan;
import com.example.verityx.entity.TestReport;
import com.example.verityx.service.TestReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/testreports")
@Tag(name = "测试报告", description = "测试报告管理")
public class TestReportController {

    @Autowired
    private TestReportService testReportService;

    @Operation(summary = "创建测试报告", description = "创建新的测试报告")
    // 创建测试报告
    @PostMapping
    public TestReport createTestReport(@RequestBody TestReport report) {
        return testReportService.createTestReport(report);
    }

    // 查询单个测试报告
    @Operation(summary = "查询单个测试报告详情", description = "根据报告ID查询单个测试报告的详情信息")
    @GetMapping("/{id}")
    public TestReport getTestReportById(@PathVariable int id) {
        return testReportService.getTestReportById(id);
    }

    // 查询所有测试报告
    @Operation(summary = "查询所有测试报告", description = "查询所有测试报告")
    @GetMapping
    public ApiResponse<List<TestReport>> getAllTestReports() {
        List<TestReport> testReport= testReportService.getAllTestReports();
        return ApiResponse.success(testReport);

    }

    // 分页获取测试报告
    @Operation(summary = "分页查询测试报告", description = "根据分页参数查询测试报告")
    @GetMapping("/list")
    public ApiResponse<Map<String, Object>> getTestPlansWithPagination(
            @RequestParam int page, 
            @RequestParam int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String projectId) {

        int offset = (page - 1) * pageSize;
        System.out.println("pageSize是:" + pageSize);
        System.out.println("offset:" + offset);
        System.out.println("projectId:" + projectId);
        
        List<TestReport> testReports = testReportService.getTestReportsWithPagination(pageSize, offset, keyword, projectId);
        int total = testReportService.getTestReportCount(keyword, projectId); // 获取总记录数
        Map<String, Object> response = new HashMap<>();
        response.put("data", testReports);
        response.put("total", total);
        response.put("page", page);
        response.put("pageSize", pageSize);

        return ApiResponse.success(response);
    }

    // 更新测试报告
    @Operation(summary = "更新测试报告", description = "根据测试报告ID更新测试报告")
    @PutMapping("/{id}")
    public ApiResponse<TestReport>  updateTestReport(@PathVariable int id, @RequestBody TestReport report) {
        report.setReportId(id);
        boolean updated = testReportService.updateTestReport(report);
        return updated ? ApiResponse.success(report) : ApiResponse.error(400, "更新失败");
    }

    // 删除测试报告
    @Operation(summary = "删除测试报告", description = "根据测试报告ID删除测试报告")
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean>  deleteTestReport(@PathVariable int id) {
        boolean deleted = testReportService.deleteTestReport(id);
        if (deleted) {
            return ApiResponse.success(true);
        }
        return ApiResponse.error(400, "删除失败");
    }
}
