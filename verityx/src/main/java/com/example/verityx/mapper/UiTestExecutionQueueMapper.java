package com.example.verityx.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.verityx.entity.UiTestExecutionQueue;

/**
 * UI测试执行队列Mapper接口
 */
@Mapper
public interface UiTestExecutionQueueMapper {
    
    /**
     * 插入一条执行队列记录
     * 
     * @param queue 执行队列记录对象
     * @return 影响的行数
     */
    int insert(UiTestExecutionQueue queue);
    
    /**
     * 根据ID查询队列记录
     * 
     * @param id 队列ID
     * @return 执行队列记录对象
     */
    UiTestExecutionQueue selectById(@Param("id") Long id);
    
    /**
     * 根据执行ID查询队列记录
     * 
     * @param executionId 执行ID
     * @return 执行队列记录对象
     */
    UiTestExecutionQueue selectByExecutionId(@Param("executionId") Long executionId);
    
    /**
     * 更新执行队列记录
     * 
     * @param queue 执行队列记录对象
     * @return 影响的行数
     */
    int update(UiTestExecutionQueue queue);
    
    /**
     * 更新执行队列状态
     * 
     * @param id 队列ID
     * @param status 状态
     * @return 影响的行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    
    /**
     * 获取下一个等待执行的队列记录
     * 
     * @return 执行队列记录对象
     */
    UiTestExecutionQueue selectNextWaiting();
    
    /**
     * 查询所有等待执行的队列记录
     * 
     * @return 执行队列记录列表
     */
    List<UiTestExecutionQueue> selectAllWaiting();
    
    /**
     * 查询所有正在处理的队列记录
     * 
     * @return 执行队列记录列表
     */
    List<UiTestExecutionQueue> selectAllProcessing();
    
    /**
     * 分页查询队列记录
     * 
     * @param status 状态
     * @param offset 偏移量
     * @param pageSize 页大小
     * @return 执行队列记录列表
     */
    List<UiTestExecutionQueue> selectByPage(
            @Param("status") String status,
            @Param("offset") int offset, 
            @Param("pageSize") int pageSize);
    
    /**
     * 查询队列记录总数
     * 
     * @param status 状态
     * @return 记录总数
     */
    int countByStatus(@Param("status") String status);
    
    /**
     * 删除队列记录
     * 
     * @param id 队列ID
     * @return 影响的行数
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据执行ID删除队列记录
     * 
     * @param executionId 执行ID
     * @return 影响的行数
     */
    int deleteByExecutionId(@Param("executionId") Long executionId);
}