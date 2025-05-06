package com.example.verityx.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.verityx.entity.UiTestExecutionLog;

/**
 * UI测试执行日志Mapper接口
 */
@Mapper
public interface UiTestExecutionLogMapper {
    
    /**
     * 插入一条执行日志记录
     * 
     * @param log 执行日志记录对象
     * @return 影响的行数
     */
    int insert(UiTestExecutionLog log);
    
    /**
     * 批量插入执行日志记录
     * 
     * @param logs 执行日志记录列表
     * @return 影响的行数
     */
    int batchInsert(List<UiTestExecutionLog> logs);
    
    /**
     * 根据执行ID查询日志记录
     * 
     * @param executionId 执行ID
     * @return 执行日志记录列表
     */
    List<UiTestExecutionLog> selectByExecutionId(@Param("executionId") Long executionId);
    
    /**
     * 根据详情ID查询日志记录
     * 
     * @param detailId 详情ID
     * @return 执行日志记录列表
     */
    List<UiTestExecutionLog> selectByDetailId(@Param("detailId") Long detailId);
    
    /**
     * 根据日志级别查询日志记录
     * 
     * @param executionId 执行ID
     * @param logLevel 日志级别
     * @return 执行日志记录列表
     */
    List<UiTestExecutionLog> selectByExecutionIdAndLogLevel(
            @Param("executionId") Long executionId, 
            @Param("logLevel") String logLevel);
    
    /**
     * 根据执行ID分页查询日志记录
     * 
     * @param executionId 执行ID
     * @param logLevel 日志级别
     * @param offset 偏移量
     * @param pageSize 页大小
     * @return 执行日志记录列表
     */
    List<UiTestExecutionLog> selectByExecutionIdWithPage(
            @Param("executionId") Long executionId, 
            @Param("logLevel") String logLevel,
            @Param("offset") int offset, 
            @Param("pageSize") int pageSize);
    
    /**
     * 查询日志记录总数
     * 
     * @param executionId 执行ID
     * @param logLevel 日志级别
     * @return 记录总数
     */
    int countByExecutionId(
            @Param("executionId") Long executionId, 
            @Param("logLevel") String logLevel);
    
    /**
     * 根据执行ID删除日志记录
     * 
     * @param executionId 执行ID
     * @return 影响的行数
     */
    int deleteByExecutionId(@Param("executionId") Long executionId);
    
    /**
     * 根据详情ID删除日志记录
     * 
     * @param detailId 详情ID
     * @return 影响的行数
     */
    int deleteByDetailId(@Param("detailId") Long detailId);
}