package com.example.verityx.service;

import com.example.verityx.entity.TestStep;

import java.util.List;

public interface TestStepService {

    TestStep createTestStep(TestStep testStep);

    TestStep getTestStepById(int stepId);

    List<TestStep> getTestStepsByCaseId(int caseId);

    boolean updateTestStep(TestStep testStep);

    boolean deleteTestStep(int stepId);
}
