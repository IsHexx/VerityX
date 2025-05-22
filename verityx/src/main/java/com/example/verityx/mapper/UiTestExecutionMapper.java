package com.example.verityx.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.verityx.entity.UiTestExecution;

/**
 * UI测试执行Mapper接口
 */
@Mapper
public interface UiTestExecutionMapper {
    
    /**
     * 插入一条执行记录
     * 
     * @param execution 执行记录对象
     * @return 影响的行数
     */
    int insert(UiTestExecution execution);
    
    /**
     * 根据ID查询执行记录
     * 
     * @param id 执行ID
     * @return 执行记录对象
     */
    UiTestExecution selectById(@Param("id") Long id);
    
    /**
     * 更新执行记录
     * 
     * @param execution 执行记录对象
     * @return 影响的行数
     */
    int update(UiTestExecution execution);
    
    /**
     * 更新执行状态
     * 
     * @param id 执行ID
     * @param status 状态
     * @return 影响的行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    
    /**
     * 更新执行结果统计
     * 
     * @param id 执行ID
     * @param successCount 成功数
     * @param failCount 失败数
     * @param totalCount 总数
     * @return 影响的行数
     */
    int updateCounts(@Param("id") Long id, @Param("successCount") int successCount, 
            @Param("failCount") int failCount, @Param("totalCount") int totalCount);
    
    /**
     * 分页查询执行记录列表
     * 
     * @param keyword 关键字
     * @param status 状态
     * @param executionType 执行类型
     * @param offset 偏移量
     * @param pageSize 页大小
     * @param projectId 项目ID
     * @return 执行记录列表
     */
    List<UiTestExecution> selectByPage(@Param("keyword") String keyword, 
            @Param("status") String status, 
            @Param("executionType") String executionType,
            @Param("offset") int offset, 
            @Param("pageSize") int pageSize,
            @Param("projectId") Long projectId);
            
    /**
     * 统计符合条件的执行记录总数
     * 
     * @param keyword 关键字
     * @param status 状态
     * @param executionType 执行类型
     * @param projectId 项目ID
     * @return 记录总数
     */
    int countByCondition(@Param("keyword") String keyword, 
            @Param("status") String status, 
            @Param("executionType") String executionType,
            @Param("projectId") Long projectId);
    
    /**
     * 根据执行类型和来源ID查询最近的执行记录
     * 
     * @param executionType 执行类型
     * @param executionSourceId 执行来源ID
     * @param limit 限制数量
     * @return 执行记录列表
     */
    List<UiTestExecution> selectRecentBySource(
            @Param("executionType") String executionType,
            @Param("executionSourceId") Long executionSourceId,
            @Param("limit") int limit);
    
    /**
     * 删除执行记录
     * 
     * @param id 执行ID
     * @return 影响的行数
     */
    int deleteById(@Param("id") Long id);
} 