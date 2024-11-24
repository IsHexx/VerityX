package com.example.verityx.service;

import com.example.verityx.entity.TestReport;
import java.util.List;

public interface TestReportService {

    TestReport createTestReport(TestReport report);

    TestReport getTestReportById(int reportId);

    List<TestReport> getAllTestReports();

    boolean updateTestReport(TestReport report);

    boolean deleteTestReport(int reportId);
}
