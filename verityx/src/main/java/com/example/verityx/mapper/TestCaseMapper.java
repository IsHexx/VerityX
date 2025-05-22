package com.example.verityx.mapper;

import com.example.verityx.entity.TestCase;
import com.example.verityx.entity.TestPlan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TestCaseMapper {

    int insertTestCase(TestCase testCase);

    TestCase selectTestCaseById(int caseId);

    List<TestCase> selectAllTestCases();

    int updateTestCase(TestCase testCase);

    int deleteTestCase(int caseId);

    // 新增分页查询方法
    List<TestCase> selectTestCasesWithPagination(
        @Param("pageSize") int limit, 
        @Param("offset") int offset, 
        @Param("caseStatus") String caseStatus,
        @Param("keyword") String keyword,
        @Param("projectId") String projectId
    );

    // 新增获取总记录数方法
    int selectTestCaseCount(
        @Param("caseStatus") String caseStatus,
        @Param("keyword") String keyword,
        @Param("projectId") String projectId
    );
}
