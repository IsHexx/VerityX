package com.example.verityx.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.verityx.entity.UiTestExecutionDetail;

/**
 * UI测试执行详情Mapper接口
 */
@Mapper
public interface UiTestExecutionDetailMapper {
    
    /**
     * 插入一条执行详情记录
     * 
     * @param detail 执行详情记录对象
     * @return 影响的行数
     */
    int insert(UiTestExecutionDetail detail);
    
    /**
     * 批量插入执行详情记录
     * 
     * @param details 执行详情记录列表
     * @return 影响的行数
     */
    int batchInsert(List<UiTestExecutionDetail> details);
    
    /**
     * 根据ID查询执行详情记录
     * 
     * @param id 详情ID
     * @return 执行详情记录对象
     */
    UiTestExecutionDetail selectById(@Param("id") Long id);
    
    /**
     * 更新执行详情记录
     * 
     * @param detail 执行详情记录对象
     * @return 影响的行数
     */
    int update(UiTestExecutionDetail detail);
    
    /**
     * 更新执行详情状态
     * 
     * @param id 详情ID
     * @param status 状态
     * @return 影响的行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    
    /**
     * 更新步骤统计信息
     * 
     * @param id 详情ID
     * @param stepCount 步骤总数
     * @param stepPassed 通过步骤数
     * @param stepFailed 失败步骤数
     * @return 影响的行数
     */
    int updateStepCounts(@Param("id") Long id, 
            @Param("stepCount") int stepCount, 
            @Param("stepPassed") int stepPassed, 
            @Param("stepFailed") int stepFailed);
    
    /**
     * 根据执行ID查询详情列表
     * 
     * @param executionId 执行ID
     * @return 执行详情记录列表
     */
    List<UiTestExecutionDetail> selectByExecutionId(@Param("executionId") Long executionId);
    
    /**
     * 根据执行ID和状态查询详情列表
     * 
     * @param executionId 执行ID
     * @param status 状态
     * @return 执行详情记录列表
     */
    List<UiTestExecutionDetail> selectByExecutionIdAndStatus(
            @Param("executionId") Long executionId, 
            @Param("status") String status);
    
    /**
     * 根据用例ID查询最近的执行详情记录
     * 
     * @param caseId 用例ID
     * @param limit 限制数量
     * @return 执行详情记录列表
     */
    List<UiTestExecutionDetail> selectRecentByCaseId(
            @Param("caseId") Long caseId, 
            @Param("limit") int limit);
    
    /**
     * 根据执行ID删除详情记录
     * 
     * @param executionId 执行ID
     * @return 影响的行数
     */
    int deleteByExecutionId(@Param("executionId") Long executionId);
} 