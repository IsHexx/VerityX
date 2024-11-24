package com.example.verityx.mapper;

import com.example.verityx.entity.TestStep;

import java.util.List;

public interface TestStepMapper {

    int insertTestStep(TestStep testStep);

    TestStep selectTestStepById(int stepId);

    List<TestStep> selectTestStepsByCaseId(int caseId);

    int updateTestStep(TestStep testStep);

    int deleteTestStep(int stepId);
}
