package com.example.verityx.service.impl;

import com.example.verityx.entity.TestCase;
import com.example.verityx.entity.TestPlan;
import com.example.verityx.mapper.TestCaseMapper;
import com.example.verityx.service.TestCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestCaseServiceImpl implements TestCaseService {

    @Autowired
    private TestCaseMapper testCaseMapper;

    @Override
    public TestCase createTestCase(TestCase testCase) {
        System.out.println("传递的参数是"+testCase);
        testCaseMapper.insertTestCase(testCase);
        return testCase;
    }

    @Override
    public TestCase getTestCaseById(int caseId) {
        return testCaseMapper.selectTestCaseById(caseId);
    }

    @Override
    public List<TestCase> getAllTestCases() {
        return testCaseMapper.selectAllTestCases();
    }

    @Override
    public List<TestCase> getTestCasesWithPagination(int pageSize, int offset, String caseStatus) {
        System.out.println("====== 分页查询测试用例 ======");
        return testCaseMapper.selectTestCasesWithPagination(pageSize, offset, caseStatus);
    }

    @Override
    public int getTestCaseCount(String caseStatus) {
        return testCaseMapper.selectTestCaseCount(caseStatus);
    }

    @Override
    public boolean updateTestCase(TestCase testCase) {
        return testCaseMapper.updateTestCase(testCase) > 0;
    }

    @Override
    public boolean deleteTestCase(int caseId) {
        return testCaseMapper.deleteTestCase(caseId) > 0;
    }
}
