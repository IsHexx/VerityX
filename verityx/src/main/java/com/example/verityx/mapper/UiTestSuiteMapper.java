package com.example.verityx.mapper;

import com.example.verityx.entity.UiTestSuite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UI测试套件Mapper接口
 */
@Mapper
public interface UiTestSuiteMapper {
    
    /**
     * 根据ID查询测试套件
     *
     * @param id 测试套件ID
     * @param projectId 项目ID
     * @return 测试套件信息
     */
    UiTestSuite selectById(@Param("id") Long id, @Param("projectId") Long projectId);
    
    /**
     * 分页查询测试套件列表
     *
     * @param keyword 搜索关键词
     * @param status 套件状态
     * @param offset 偏移量
     * @param pageSize 页大小
     * @param projectId 项目ID
     * @return 测试套件列表
     */
    List<UiTestSuite> selectByPage(@Param("keyword") String keyword, 
                                  @Param("status") String status,
                                  @Param("offset") Integer offset, 
                                  @Param("pageSize") Integer pageSize,
                                  @Param("projectId") Long projectId);
    
    /**
     * 统计符合条件的测试套件数量
     *
     * @param keyword 搜索关键词
     * @param status 套件状态
     * @param projectId 项目ID
     * @return 测试套件数量
     */
    Integer countTestSuites(@Param("keyword") String keyword, 
                           @Param("status") String status,
                           @Param("projectId") Long projectId);
    
    /**
     * 插入测试套件
     *
     * @param uiTestSuite 测试套件信息
     * @return 影响行数
     */
    int insert(UiTestSuite uiTestSuite);
    
    /**
     * 更新测试套件
     *
     * @param uiTestSuite 测试套件信息
     * @return 影响行数
     */
    int updateById(UiTestSuite uiTestSuite);
    
    /**
     * 删除测试套件
     *
     * @param id 测试套件ID
     * @param projectId 项目ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id, @Param("projectId") Long projectId);
} 