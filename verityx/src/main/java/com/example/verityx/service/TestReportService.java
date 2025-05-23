package com.example.verityx.service;

import com.example.verityx.entity.Bug;
import com.example.verityx.entity.TestCase;
import com.example.verityx.entity.TestPlan;
import com.example.verityx.entity.TestReport;
import java.util.List;
import java.util.Map;

public interface TestReportService {

    /**
     * 创建测试报告
     */
    TestReport createTestReport(TestReport report);

    /**
     * 获取测试报告详情
     */
    TestReport getTestReportById(int reportId);

    /**
     * 获取所有测试报告
     */
    List<TestReport> getAllTestReports();

    /**
     * 分页获取测试报告
     */
    List<TestReport> getTestReportsWithPagination(int pageSize, int offset, String keyword, String projectId);

    /**
     * 获取测试报告总数
     */
    int getTestReportCount(String keyword, String projectId);

    /**
     * 更新测试报告
     */
    boolean updateTestReport(TestReport report);

    /**
     * 删除测试报告
     */
    boolean deleteTestReport(int reportId);

    /**
     * 获取测试报告关联的测试计划
     */
    TestPlan getAssociatedTestPlan(int reportId);

    /**
     * 获取测试报告关联的测试用例列表
     */
    List<TestCase> getAssociatedTestCases(int reportId);

    /**
     * 获取测试报告关联的缺陷列表
     */
    List<Bug> getAssociatedBugs(int reportId);

    /**
     * 获取测试报告的聚合信息
     * 包括测试计划、测试用例和缺陷信息
     */
    Map<String, Object> getAggregatedReportInfo(int reportId);

    /**
     * 根据测试计划自动生成测试报告
     */
    TestReport generateReportFromTestPlan(int planId, String reportTitle, int createdBy);
}
