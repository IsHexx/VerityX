package com.example.verityx.mapper;

import com.example.verityx.entity.TestPlan;
import com.example.verityx.entity.TestReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TestReportMapper {

    /**
     * 插入测试报告
     */
    int insertTestReport(TestReport report);

    /**
     * 根据 reportId 查询测试报告
     */
    TestReport selectTestReportById(int reportId);

    /**
     * 查询所有测试报告
     */
    List<TestReport> selectAllTestReports();

    /**
     * 分页查询测试报告
     */
    List<TestReport> selectTestReportsWithPagination(
        @Param("pageSize") int limit,
        @Param("offset") int offset,
        @Param("keyword") String keyword,
        @Param("projectId") String projectId
    );

    /**
     * 获取测试报告总数
     */
    int selectTestReportCount(
        @Param("keyword") String keyword,
        @Param("projectId") String projectId
    );

    /**
     * 更新测试报告
     */
    int updateTestReport(TestReport report);

    /**
     * 删除测试报告
     */
    int deleteTestReport(int reportId);

    /**
     * 获取测试报告关联的测试用例ID列表
     */
    List<Integer> selectRelatedCaseIds(int reportId);
}
