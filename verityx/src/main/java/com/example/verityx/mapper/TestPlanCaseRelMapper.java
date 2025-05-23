package com.example.verityx.mapper;

import com.example.verityx.entity.TestPlanCaseRel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 测试计划与测试用例关联Mapper接口
 */
@Mapper
public interface TestPlanCaseRelMapper {
    
    /**
     * 插入测试计划与测试用例关联记录
     * 
     * @param relation 关联记录
     * @return 影响行数
     */
    int insert(TestPlanCaseRel relation);
    
    /**
     * 批量插入测试计划与测试用例关联记录
     * 
     * @param relations 关联记录列表
     * @return 影响行数
     */
    int batchInsert(@Param("relations") List<TestPlanCaseRel> relations);
    
    /**
     * 根据测试计划ID查询关联的测试用例ID列表
     * 
     * @param planId 测试计划ID
     * @return 测试用例ID列表
     */
    List<Integer> selectCaseIdsByPlanId(@Param("planId") Integer planId);
    
    /**
     * 根据测试计划ID查询关联记录
     * 
     * @param planId 测试计划ID
     * @return 关联记录列表
     */
    List<TestPlanCaseRel> selectByPlanId(@Param("planId") Integer planId);
    
    /**
     * 根据测试用例ID查询关联记录
     * 
     * @param caseId 测试用例ID
     * @return 关联记录列表
     */
    List<TestPlanCaseRel> selectByCaseId(@Param("caseId") Integer caseId);
    
    /**
     * 根据测试计划ID删除所有关联记录
     * 
     * @param planId 测试计划ID
     * @return 影响行数
     */
    int deleteByPlanId(@Param("planId") Integer planId);
    
    /**
     * 根据测试计划ID和测试用例ID删除关联记录
     * 
     * @param planId 测试计划ID
     * @param caseId 测试用例ID
     * @return 影响行数
     */
    int deleteByPlanIdAndCaseId(@Param("planId") Integer planId, @Param("caseId") Integer caseId);
}
