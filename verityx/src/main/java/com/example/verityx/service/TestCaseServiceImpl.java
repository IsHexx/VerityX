package com.example.verityx.service.impl;

import com.example.verityx.entity.TestCase;
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
    public boolean updateTestCase(TestCase testCase) {
        return testCaseMapper.updateTestCase(testCase) > 0;
    }

    @Override
    public boolean deleteTestCase(int caseId) {
        return testCaseMapper.deleteTestCase(caseId) > 0;
    }
}
