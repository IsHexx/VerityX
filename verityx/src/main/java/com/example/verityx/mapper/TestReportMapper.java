package com.example.verityx.mapper;

import com.example.verityx.entity.TestReport;
import java.util.List;

public interface TestReportMapper {

    // 插入测试报告
    int insertTestReport(TestReport report);

    // 根据 reportId 查询测试报告
    TestReport selectTestReportById(int reportId);

    // 查询所有测试报告
    List<TestReport> selectAllTestReports();

    // 更新测试报告
    int updateTestReport(TestReport report);

    // 删除测试报告
    int deleteTestReport(int reportId);
}
