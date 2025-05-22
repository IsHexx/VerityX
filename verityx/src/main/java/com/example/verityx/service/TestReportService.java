package com.example.verityx.service;

import com.example.verityx.entity.TestPlan;
import com.example.verityx.entity.TestReport;
import java.util.List;

public interface TestReportService {

    TestReport createTestReport(TestReport report);

    TestReport getTestReportById(int reportId);

    List<TestReport> getAllTestReports();

    List<TestReport> getTestReportsWithPagination(int pageSize, int offset, String keyword, String projectId);

    int getTestReportCount(String keyword, String projectId);

    boolean updateTestReport(TestReport report);

    boolean deleteTestReport(int reportId);
}
