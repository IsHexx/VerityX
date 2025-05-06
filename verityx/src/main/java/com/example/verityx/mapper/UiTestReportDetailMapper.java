package com.example.verityx.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.verityx.entity.UiTestReportDetail;

/**
 * UI测试报告详情Mapper接口
 */
@Mapper
public interface UiTestReportDetailMapper {
    
    /**
     * 插入测试报告详情
     * 
     * @param detail 测试报告详情对象
     * @return 影响的行数
     */
    int insert(UiTestReportDetail detail);
    
    /**
     * 批量插入测试报告详情
     * 
     * @param details 测试报告详情列表
     * @return 影响的行数
     */
    int batchInsert(@Param("details") List<UiTestReportDetail> details);
    
    /**
     * 根据ID查询测试报告详情
     * 
     * @param id 测试报告详情ID
     * @return 测试报告详情对象
     */
    UiTestReportDetail selectById(@Param("id") Long id);
    
    /**
     * 根据报告ID查询测试报告详情列表
     * 
     * @param reportId 测试报告ID
     * @return 测试报告详情列表
     */
    List<UiTestReportDetail> selectByReportId(@Param("reportId") Long reportId);
    
    /**
     * 根据执行详情ID查询测试报告详情
     * 
     * @param executionDetailId 执行详情ID
     * @return 测试报告详情对象
     */
    UiTestReportDetail selectByExecutionDetailId(@Param("executionDetailId") Long executionDetailId);
    
    /**
     * 更新测试报告详情
     * 
     * @param detail 测试报告详情对象
     * @return 影响的行数
     */
    int update(UiTestReportDetail detail);
    
    /**
     * 删除测试报告详情
     * 
     * @param id 测试报告详情ID
     * @return 影响的行数
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据报告ID删除测试报告详情
     * 
     * @param reportId 测试报告ID
     * @return 影响的行数
     */
    int deleteByReportId(@Param("reportId") Long reportId);
    
    /**
     * 分页查询测试报告详情
     * 
     * @param reportId 报告ID
     * @param caseResult 测试结果
     * @param offset 偏移量
     * @param pageSize 页大小
     * @return 测试报告详情列表
     */
    List<UiTestReportDetail> selectByPage(
            @Param("reportId") Long reportId,
            @Param("caseResult") String caseResult,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);
    
    /**
     * 统计测试报告详情总数
     * 
     * @param reportId 报告ID
     * @param caseResult 测试结果
     * @return 总数
     */
    int countByCondition(
            @Param("reportId") Long reportId,
            @Param("caseResult") String caseResult);
} 