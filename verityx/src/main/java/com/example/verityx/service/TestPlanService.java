package com.example.verityx.service;

import com.example.verityx.entity.TestCase;
import com.example.verityx.entity.TestPlan;
import com.example.verityx.entity.TestPlanCaseRel;

import java.util.List;

public interface TestPlanService {

    TestPlan createTestPlan(TestPlan testPlan);

    TestPlan getTestPlanById(Integer planId);

    List<TestPlan> getAllTestPlans();

    List<TestPlan> getTestPlansByProjectId(Integer projectId);

    List<TestPlan> getTestPlansWithPagination(int pageSize, int offset, String status, Integer projectId);

    int getTestPlanCount(String status, Integer projectId);

    boolean updateTestPlan(TestPlan testPlan);

    boolean deleteTestPlan(Integer planId);

    /**
     * 关联测试用例到测试计划
     *
     * @param planId 测试计划ID
     * @param caseIds 测试用例ID列表
     * @return 是否成功
     */
    boolean associateTestCases(Integer planId, List<Integer> caseIds);

    /**
     * 从测试计划中移除测试用例
     *
     * @param planId 测试计划ID
     * @param caseId 测试用例ID
     * @return 是否成功
     */
    boolean removeTestCase(Integer planId, Integer caseId);

    /**
     * 获取测试计划关联的测试用例列表
     *
     * @param planId 测试计划ID
     * @return 测试用例列表
     */
    List<TestCase> getAssociatedTestCases(Integer planId);

    /**
     * 获取测试计划关联的测试用例关系列表
     *
     * @param planId 测试计划ID
     * @return 关联关系列表
     */
    List<TestPlanCaseRel> getTestPlanCaseRels(Integer planId);
}
