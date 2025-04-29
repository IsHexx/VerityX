package com.example.verityx.service;

import com.example.verityx.entity.TestStep;
import com.example.verityx.mapper.TestStepMapper;
import com.example.verityx.service.TestStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestStepServiceImpl implements TestStepService {

    @Autowired
    private TestStepMapper testStepMapper;

    @Override
    public TestStep createTestStep(TestStep testStep) {
        testStepMapper.insertTestStep(testStep);
        return testStep;
    }

    @Override
    public TestStep getTestStepById(int stepId) {
        return testStepMapper.selectTestStepById(stepId);
    }

    @Override
    public List<TestStep> getTestStepsByCaseId(int caseId) {
        return testStepMapper.selectTestStepsByCaseId(caseId);
    }

    @Override
    public boolean updateTestStep(TestStep testStep) {
        return testStepMapper.updateTestStep(testStep) > 0;
    }

    @Override
    public boolean deleteTestStep(int stepId) {
        return testStepMapper.deleteTestStep(stepId) > 0;
    }
}
