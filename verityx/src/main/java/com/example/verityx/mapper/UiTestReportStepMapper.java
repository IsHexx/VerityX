package com.example.verityx.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.verityx.entity.UiTestReportStep;

/**
 * UI测试报告步骤Mapper接口
 */
@Mapper
public interface UiTestReportStepMapper {
    
    /**
     * 插入测试报告步骤
     * 
     * @param step 测试报告步骤对象
     * @return 影响的行数
     */
    int insert(UiTestReportStep step);
    
    /**
     * 批量插入测试报告步骤
     * 
     * @param steps 测试报告步骤列表
     * @return 影响的行数
     */
    int batchInsert(@Param("steps") List<UiTestReportStep> steps);
    
    /**
     * 根据ID查询测试报告步骤
     * 
     * @param id 测试报告步骤ID
     * @return 测试报告步骤对象
     */
    UiTestReportStep selectById(@Param("id") Long id);
    
    /**
     * 根据报告详情ID查询测试报告步骤列表
     * 
     * @param reportDetailId 测试报告详情ID
     * @return 测试报告步骤列表
     */
    List<UiTestReportStep> selectByReportDetailId(@Param("reportDetailId") Long reportDetailId);
    
    /**
     * 根据报告详情ID和步骤索引查询测试报告步骤
     * 
     * @param reportDetailId 测试报告详情ID
     * @param stepIndex 步骤索引
     * @return 测试报告步骤对象
     */
    UiTestReportStep selectByReportDetailIdAndStepIndex(
            @Param("reportDetailId") Long reportDetailId,
            @Param("stepIndex") Integer stepIndex);
    
    /**
     * 更新测试报告步骤
     * 
     * @param step 测试报告步骤对象
     * @return 影响的行数
     */
    int update(UiTestReportStep step);
    
    /**
     * 删除测试报告步骤
     * 
     * @param id 测试报告步骤ID
     * @return 影响的行数
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据报告详情ID删除测试报告步骤
     * 
     * @param reportDetailId 测试报告详情ID
     * @return 影响的行数
     */
    int deleteByReportDetailId(@Param("reportDetailId") Long reportDetailId);
    
    /**
     * 根据报告ID删除测试报告步骤
     * 
     * @param reportId 测试报告ID
     * @return 影响的行数
     */
    int deleteByReportId(@Param("reportId") Long reportId);
} 