package com.example.verityx.service;

import com.example.verityx.entity.TestPlan;
import com.example.verityx.mapper.TestPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestPlanServiceImpl implements TestPlanService {

    @Autowired
    private TestPlanMapper testPlanMapper;

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
    public List<TestPlan> getTestPlansWithPagination(int pageSize, int offset, String status) {
        System.out.println("====== 分页查询测试计划 ======");
        return testPlanMapper.selectTestPlansWithPagination(pageSize, offset, status);
    }

    @Override
    public int getTestPlanCount(String status) {
        return testPlanMapper.selectTestPlanCount(status);
    }

    @Override
    public boolean updateTestPlan(TestPlan testPlan) {
        return testPlanMapper.updateTestPlan(testPlan) > 0;
    }

    @Override
    public boolean deleteTestPlan(Integer planId) {
        return testPlanMapper.deleteTestPlan(planId) > 0;
    }
}
