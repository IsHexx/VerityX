package com.example.verityx.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.verityx.entity.UiTestExecutionStep;

/**
 * UI测试执行步骤Mapper接口
 */
@Mapper
public interface UiTestExecutionStepMapper {
    
    /**
     * 插入一条执行步骤记录
     * 
     * @param step 执行步骤记录对象
     * @return 影响的行数
     */
    int insert(UiTestExecutionStep step);
    
    /**
     * 批量插入执行步骤记录
     * 
     * @param steps 执行步骤记录列表
     * @return 影响的行数
     */
    int batchInsert(List<UiTestExecutionStep> steps);
    
    /**
     * 根据ID查询执行步骤记录
     * 
     * @param id 步骤ID
     * @return 执行步骤记录对象
     */
    UiTestExecutionStep selectById(@Param("id") Long id);
    
    /**
     * 更新执行步骤记录
     * 
     * @param step 执行步骤记录对象
     * @return 影响的行数
     */
    int update(UiTestExecutionStep step);
    
    /**
     * 更新执行步骤状态
     * 
     * @param id 步骤ID
     * @param status 状态
     * @return 影响的行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    
    /**
     * 根据详情ID查询步骤记录
     * 
     * @param detailId 详情ID
     * @return 执行步骤记录列表
     */
    List<UiTestExecutionStep> selectByDetailId(@Param("detailId") Long detailId);
    
    /**
     * 根据详情ID和状态查询步骤记录
     * 
     * @param detailId 详情ID
     * @param status 状态
     * @return 执行步骤记录列表
     */
    List<UiTestExecutionStep> selectByDetailIdAndStatus(
            @Param("detailId") Long detailId, 
            @Param("status") String status);
    
    /**
     * 根据详情ID查询通过的步骤数
     * 
     * @param detailId 详情ID
     * @return 通过的步骤数
     */
    int countPassedSteps(@Param("detailId") Long detailId);
    
    /**
     * 根据详情ID查询失败的步骤数
     * 
     * @param detailId 详情ID
     * @return 失败的步骤数
     */
    int countFailedSteps(@Param("detailId") Long detailId);
    
    /**
     * 根据详情ID查询步骤总数
     * 
     * @param detailId 详情ID
     * @return 步骤总数
     */
    int countSteps(@Param("detailId") Long detailId);
    
    /**
     * 根据详情ID删除步骤记录
     * 
     * @param detailId 详情ID
     * @return 影响的行数
     */
    int deleteByDetailId(@Param("detailId") Long detailId);
} 