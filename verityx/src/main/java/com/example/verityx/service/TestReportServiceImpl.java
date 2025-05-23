package com.example.verityx.service;

import com.example.verityx.entity.Bug;
import com.example.verityx.entity.TestCase;
import com.example.verityx.entity.TestPlan;
import com.example.verityx.entity.TestReport;
import com.example.verityx.mapper.BugMapper;
import com.example.verityx.mapper.TestCaseMapper;
import com.example.verityx.mapper.TestPlanMapper;
import com.example.verityx.mapper.TestReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestReportServiceImpl implements TestReportService {

    @Autowired
    private TestReportMapper testReportMapper;

    @Autowired
    private TestPlanMapper testPlanMapper;

    @Autowired
    private TestCaseMapper testCaseMapper;

    @Autowired
    private BugMapper bugMapper;

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
    public List<TestReport> getTestReportsWithPagination(int pageSize, int offset, String keyword, String projectId) {
        System.out.println("====== 分页查询测试报告 ======");
        return testReportMapper.selectTestReportsWithPagination(pageSize, offset, keyword, projectId);
    }

    @Override
    public int getTestReportCount(String keyword, String projectId) {
        return testReportMapper.selectTestReportCount(keyword, projectId);
    }

    @Override
    public boolean updateTestReport(TestReport report) {
        return testReportMapper.updateTestReport(report) > 0;
    }

    @Override
    public boolean deleteTestReport(int reportId) {
        return testReportMapper.deleteTestReport(reportId) > 0;
    }

    @Override
    public TestPlan getAssociatedTestPlan(int reportId) {
        TestReport report = testReportMapper.selectTestReportById(reportId);
        if (report == null || report.getPlanId() == null) {
            return null;
        }

        return testPlanMapper.selectTestPlanById(report.getPlanId());
    }

    @Override
    public List<TestCase> getAssociatedTestCases(int reportId) {
        TestReport report = testReportMapper.selectTestReportById(reportId);
        if (report == null || report.getPlanId() == null) {
            return new ArrayList<>();
        }

        // 获取测试计划关联的测试用例
        List<Integer> caseIds = testReportMapper.selectRelatedCaseIds(reportId);
        if (caseIds == null || caseIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<TestCase> testCases = new ArrayList<>();
        for (Integer caseId : caseIds) {
            TestCase testCase = testCaseMapper.selectTestCaseById(caseId);
            if (testCase != null) {
                testCases.add(testCase);
            }
        }

        return testCases;
    }

    @Override
    public List<Bug> getAssociatedBugs(int reportId) {
        TestReport report = testReportMapper.selectTestReportById(reportId);
        if (report == null) {
            return new ArrayList<>();
        }

        // 获取测试报告关联的缺陷
        List<Bug> bugs = new ArrayList<>();

        // 如果报告关联了测试计划，获取计划相关的缺陷
        if (report.getPlanId() != null) {
            bugs.addAll(bugMapper.selectBugsByPlanId(report.getPlanId()));
        }

        // 获取报告中关联的测试用例相关的缺陷
        List<Integer> caseIds = testReportMapper.selectRelatedCaseIds(reportId);
        if (caseIds != null && !caseIds.isEmpty()) {
            for (Integer caseId : caseIds) {
                bugs.addAll(bugMapper.selectBugsByCaseId(caseId));
            }
        }

        return bugs;
    }

    @Override
    public Map<String, Object> getAggregatedReportInfo(int reportId) {
        Map<String, Object> result = new HashMap<>();

        // 获取测试报告基本信息
        TestReport report = testReportMapper.selectTestReportById(reportId);
        if (report == null) {
            return result;
        }

        result.put("report", report);

        // 获取关联的测试计划
        TestPlan testPlan = getAssociatedTestPlan(reportId);
        result.put("testPlan", testPlan);

        // 获取关联的测试用例
        List<TestCase> testCases = getAssociatedTestCases(reportId);
        result.put("testCases", testCases);

        // 获取关联的缺陷
        List<Bug> bugs = getAssociatedBugs(reportId);
        result.put("bugs", bugs);

        // 统计信息
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalCases", testCases.size());

        // 统计用例执行结果
        int passedCases = 0;
        int failedCases = 0;
        int blockedCases = 0;

        for (TestCase testCase : testCases) {
            String executionResult = testCase.getExecutionResult();
            if ("通过".equals(executionResult)) {
                passedCases++;
            } else if ("失败".equals(executionResult)) {
                failedCases++;
            } else if ("阻塞".equals(executionResult)) {
                blockedCases++;
            }
        }

        statistics.put("passedCases", passedCases);
        statistics.put("failedCases", failedCases);
        statistics.put("blockedCases", blockedCases);
        statistics.put("bugCount", bugs.size());

        result.put("statistics", statistics);

        return result;
    }

    @Override
    @Transactional
    public TestReport generateReportFromTestPlan(int planId, String reportTitle, int createdBy) {
        // 验证测试计划是否存在
        TestPlan testPlan = testPlanMapper.selectTestPlanById(planId);
        if (testPlan == null) {
            throw new RuntimeException("测试计划不存在");
        }

        // 创建测试报告
        TestReport report = new TestReport();
        report.setReportTitle(reportTitle);
        report.setPlanId(planId);
        report.setCreatedBy(createdBy);
        report.setCreatedAt(LocalDateTime.now());
        report.setTestVersion(testPlan.getPlanVersion());
        report.setProjectId(testPlan.getProjectId() != null ? testPlan.getProjectId().toString() : null);

        // 获取测试计划关联的测试用例
        List<TestCase> testCases = testCaseMapper.selectTestCasesByPlanId(planId);

        // 获取测试计划关联的缺陷
        List<Bug> bugs = bugMapper.selectBugsByPlanId(planId);

        // 设置报告摘要信息
        StringBuilder summary = new StringBuilder();
        summary.append("测试计划: ").append(testPlan.getPlanName()).append("\n");
        summary.append("测试版本: ").append(testPlan.getPlanVersion()).append("\n");
        summary.append("测试用例总数: ").append(testCases.size()).append("\n");
        summary.append("缺陷总数: ").append(bugs.size()).append("\n");

        report.setSummary(summary.toString());

        // 设置关联的用例ID列表
        if (!testCases.isEmpty()) {
            StringBuilder caseIds = new StringBuilder();
            for (TestCase testCase : testCases) {
                caseIds.append(testCase.getCaseId()).append(",");
            }
            report.setRelatedCases(caseIds.toString());
        }

        // 设置遗留的Bug ID列表
        if (!bugs.isEmpty()) {
            StringBuilder bugIds = new StringBuilder();
            for (Bug bug : bugs) {
                if (!"已修复".equals(bug.getStatus())) {
                    bugIds.append(bug.getBugId()).append(",");
                }
            }
            report.setRemainingBugs(bugIds.toString());
        }

        // 保存测试报告
        testReportMapper.insertTestReport(report);

        return report;
    }
}
