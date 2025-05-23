package com.example.verityx.mapper;

import com.example.verityx.entity.TestCase;
import com.example.verityx.entity.TestPlan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TestCaseMapper {

    /**
     * 插入测试用例
     */
    int insertTestCase(TestCase testCase);

    /**
     * 根据ID查询测试用例
     */
    TestCase selectTestCaseById(int caseId);

    /**
     * 查询所有测试用例
     */
    List<TestCase> selectAllTestCases();

    /**
     * 更新测试用例
     */
    int updateTestCase(TestCase testCase);

    /**
     * 删除测试用例
     */
    int deleteTestCase(int caseId);

    /**
     * 分页查询测试用例
     */
    List<TestCase> selectTestCasesWithPagination(
        @Param("pageSize") int limit,
        @Param("offset") int offset,
        @Param("caseStatus") String caseStatus,
        @Param("keyword") String keyword,
        @Param("projectId") String projectId
    );

    /**
     * 获取测试用例总数
     */
    int selectTestCaseCount(
        @Param("caseStatus") String caseStatus,
        @Param("keyword") String keyword,
        @Param("projectId") String projectId
    );

    /**
     * 根据测试计划ID查询关联的测试用例
     */
    List<TestCase> selectTestCasesByPlanId(@Param("planId") int planId);
}
