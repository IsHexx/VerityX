package com.example.verityx.mapper;

import com.example.verityx.entity.TestCase;

import java.util.List;

public interface TestCaseMapper {

    int insertTestCase(TestCase testCase);

    TestCase selectTestCaseById(int caseId);

    List<TestCase> selectAllTestCases();

    int updateTestCase(TestCase testCase);

    int deleteTestCase(int caseId);
}
