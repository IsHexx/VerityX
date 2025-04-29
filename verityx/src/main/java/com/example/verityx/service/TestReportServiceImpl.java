package com.example.verityx.service;

import com.example.verityx.entity.TestPlan;
import com.example.verityx.entity.TestReport;
import com.example.verityx.mapper.TestReportMapper;
import com.example.verityx.service.TestReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestReportServiceImpl implements TestReportService {

    @Autowired
    private TestReportMapper testReportMapper;

    @Override
    public TestReport createTestReport(TestReport report) {
        testReportMapper.insertTestReport(report);
        return report;
    }

    @Override
    public TestReport getTestReportById(int reportId) {
        return testReportMapper.selectTestReportById(reportId);
    }

    @Override
    public List<TestReport> getAllTestReports() {
        return testReportMapper.selectAllTestReports();
    }

    @Override
    public List<TestReport> getTestReportsWithPagination(int pageSize, int offset) {
        System.out.println("====== 分页查询测试报告 ======");
        return testReportMapper.selectTestReportsWithPagination(pageSize, offset);
    }
    @Override
    public int getTestReportCount() {
        return testReportMapper.selectTestReportCount();
    }

    @Override
    public boolean updateTestReport(TestReport report) {
        return testReportMapper.updateTestReport(report) > 0;
    }

    @Override
    public boolean deleteTestReport(int reportId) {
        return testReportMapper.deleteTestReport(reportId) > 0;
    }
}
