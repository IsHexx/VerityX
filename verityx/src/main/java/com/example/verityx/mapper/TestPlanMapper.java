package com.example.verityx.mapper;

import com.example.verityx.entity.TestPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TestPlanMapper {

    int insertTestPlan(TestPlan testPlan);

    TestPlan selectTestPlanById(int planId);

    List<TestPlan> selectAllTestPlans();

    int updateTestPlan(TestPlan testPlan);

    int deleteTestPlan(int planId);

    // 新增分页查询方法
    List<TestPlan> selectTestPlansWithPagination(@Param("pageSize") int limit, @Param("offset") int offset, @Param("status") String  status);

    // 新增获取总记录数方法
    int selectTestPlanCount(String  status);
}
