package com.example.verityx.mapper;

import com.example.verityx.entity.TestPlan;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestPlanMapper {

    int insertTestPlan(TestPlan testPlan);

    TestPlan selectTestPlanById(int planId);

    List<TestPlan> selectAllTestPlans();

    int updateTestPlan(TestPlan testPlan);

    int deleteTestPlan(int planId);
}
