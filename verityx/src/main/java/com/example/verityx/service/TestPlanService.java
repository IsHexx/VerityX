package com.example.verityx.service;

import com.example.verityx.entity.TestPlan;

import java.util.List;

public interface TestPlanService {

    TestPlan createTestPlan(TestPlan testPlan);

    TestPlan getTestPlanById(Integer planId);

    List<TestPlan> getAllTestPlans();

    List<TestPlan> getTestPlansWithPagination(int pageSize, int offset, String status);

    int getTestPlanCount(String status);

    boolean updateTestPlan(TestPlan testPlan);

    boolean deleteTestPlan(Integer planId);
}
