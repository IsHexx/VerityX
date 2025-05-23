package com.example.verityx.service;

import com.example.verityx.entity.TestCase;
import com.example.verityx.entity.TestPlan;
import com.example.verityx.entity.TestPlanCaseRel;
import com.example.verityx.mapper.TestCaseMapper;
import com.example.verityx.mapper.TestPlanCaseRelMapper;
import com.example.verityx.mapper.TestPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestPlanServiceImpl implements TestPlanService {

    @Autowired
    private TestPlanMapper testPlanMapper;

    @Autowired
    private TestPlanCaseRelMapper testPlanCaseRelMapper;

    @Autowired
    private TestCaseMapper testCaseMapper;

    @Override
    public TestPlan createTestPlan(TestPlan testPlan) {
        System.out.println("====== 开始创建计划 ======");
        System.out.println("创建计划: " + testPlan);
        testPlanMapper.insertTestPlan(testPlan);
        return testPlan;
    }

    @Override
    public TestPlan getTestPlanById(Integer planId) {
        return testPlanMapper.selectTestPlanById(planId);
    }

    @Override
    public List<TestPlan> getAllTestPlans() {
        return testPlanMapper.selectAllTestPlans();
    }

    @Override
    public List<TestPlan> getTestPlansByProjectId(Integer projectId) {
        return testPlanMapper.selectTestPlansByProjectId(projectId);
    }

    @Override
    public List<TestPlan> getTestPlansWithPagination(int pageSize, int offset, String status, Integer projectId) {
        System.out.println("====== 分页查询测试计划 ======");
        System.out.println("项目ID: " + projectId);
        return testPlanMapper.selectTestPlansWithPagination(pageSize, offset, status, projectId);
    }

    @Override
    public int getTestPlanCount(String status, Integer projectId) {
        return testPlanMapper.selectTestPlanCount(status, projectId);
    }

    @Override
    public boolean updateTestPlan(TestPlan testPlan) {
        return testPlanMapper.updateTestPlan(testPlan) > 0;
    }

    @Override
    public boolean deleteTestPlan(Integer planId) {
        return testPlanMapper.deleteTestPlan(planId) > 0;
    }

    @Override
    @Transactional
    public boolean associateTestCases(Integer planId, List<Integer> caseIds) {
        // 验证测试计划是否存在
        TestPlan testPlan = testPlanMapper.selectTestPlanById(planId);
        if (testPlan == null) {
            return false;
        }

        try {
            // 先删除原有关联
            testPlanCaseRelMapper.deleteByPlanId(planId);

            // 如果没有新的用例ID，则直接返回
            if (caseIds == null || caseIds.isEmpty()) {
                return true;
            }

            // 创建新的关联记录
            List<TestPlanCaseRel> relations = new ArrayList<>();
            for (int i = 0; i < caseIds.size(); i++) {
                TestPlanCaseRel relation = new TestPlanCaseRel();
                relation.setPlanId(planId);
                relation.setCaseId(caseIds.get(i));
                relation.setOrderIndex(i + 1); // 顺序从1开始
                relations.add(relation);
            }

            // 批量插入关联记录
            testPlanCaseRelMapper.batchInsert(relations);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeTestCase(Integer planId, Integer caseId) {
        return testPlanCaseRelMapper.deleteByPlanIdAndCaseId(planId, caseId) > 0;
    }

    @Override
    public List<TestCase> getAssociatedTestCases(Integer planId) {
        // 获取关联的测试用例ID列表
        List<Integer> caseIds = testPlanCaseRelMapper.selectCaseIdsByPlanId(planId);
        if (caseIds == null || caseIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 查询测试用例详情
        List<TestCase> testCases = new ArrayList<>();
        for (Integer caseId : caseIds) {
            TestCase testCase = testCaseMapper.selectTestCaseById(caseId);
            if (testCase != null) {
                testCases.add(testCase);
            }
        }

        return testCases;
    }

    @Override
    public List<TestPlanCaseRel> getTestPlanCaseRels(Integer planId) {
        return testPlanCaseRelMapper.selectByPlanId(planId);
    }
}
