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

    // 按项目ID查询测试计划
    List<TestPlan> selectTestPlansByProjectId(@Param("projectId") Integer projectId);

    int updateTestPlan(TestPlan testPlan);

    int deleteTestPlan(int planId);

    // 分页查询方法，添加projectId参数
    List<TestPlan> selectTestPlansWithPagination(
        @Param("pageSize") int limit, 
        @Param("offset") int offset, 
        @Param("status") String status,
        @Param("projectId") Integer projectId
    );

    // 获取总记录数方法，添加projectId参数
    int selectTestPlanCount(
        @Param("status") String status,
        @Param("projectId") Integer projectId
    );
}
