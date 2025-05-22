package com.example.verityx.service;

import com.example.verityx.entity.TestCase;


import java.util.List;

public interface TestCaseService {

    TestCase createTestCase(TestCase testCase);

    TestCase getTestCaseById(int caseId);

    List<TestCase> getAllTestCases();

    List<TestCase> getTestCasesWithPagination(int pageSize, int offset, String caseStatus, String keyword, String projectId);

    int getTestCaseCount(String caseStatus, String keyword, String projectId);

    boolean updateTestCase(TestCase testCase);

    boolean deleteTestCase(int caseId);
}
