package com.example.verityx.controller;

import com.example.verityx.entity.TestReport;
import com.example.verityx.service.TestReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class TestReportController {

    @Autowired
    private TestReportService testReportService;

    // 创建测试报告
    @PostMapping
    public TestReport createTestReport(@RequestBody TestReport report) {
        return testReportService.createTestReport(report);
    }

    // 查询单个测试报告
    @GetMapping("/{id}")
    public TestReport getTestReportById(@PathVariable int id) {
        return testReportService.getTestReportById(id);
    }

    // 查询所有测试报告
    @GetMapping
    public List<TestReport> getAllTestReports() {
        return testReportService.getAllTestReports();
    }

    // 更新测试报告
    @PutMapping("/{id}")
    public boolean updateTestReport(@PathVariable int id, @RequestBody TestReport report) {
        report.setReportId(id);
        return testReportService.updateTestReport(report);
    }

    // 删除测试报告
    @DeleteMapping("/{id}")
    public boolean deleteTestReport(@PathVariable int id) {
        return testReportService.deleteTestReport(id);
    }
}
