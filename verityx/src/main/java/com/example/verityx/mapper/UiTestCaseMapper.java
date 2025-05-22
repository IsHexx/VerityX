package com.example.verityx.mapper;

import com.example.verityx.entity.UiTestCase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UI测试用例Mapper接口
 */
@Mapper
public interface UiTestCaseMapper {
    /**
     * 创建UI测试用例
     *
     * @param uiTestCase UI测试用例对象
     * @return 影响行数
     */
    int insert(UiTestCase uiTestCase);
    
    /**
     * 更新UI测试用例
     *
     * @param uiTestCase UI测试用例对象
     * @return 影响行数
     */
    int update(UiTestCase uiTestCase);
    
    /**
     * 删除UI测试用例
     *
     * @param caseId 用例ID
     * @param projectId 项目ID (可选)
     * @return 影响行数
     */
    int delete(@Param("caseId") String caseId, @Param("projectId") Integer projectId);
    
    /**
     * 根据ID查询UI测试用例
     *
     * @param caseId 用例ID
     * @param projectId 项目ID (可选)
     * @return UI测试用例对象
     */
    UiTestCase selectByCaseId(@Param("caseId") String caseId, @Param("projectId") Integer projectId);
    
    /**
     * 根据ID查询UI测试用例
     *
     * @param id 用例ID（数字ID）
     * @param projectId 项目ID (可选)
     * @return UI测试用例对象
     */
    UiTestCase selectById(@Param("id") Integer id, @Param("projectId") Integer projectId);
    
    /**
     * 分页查询UI测试用例
     *
     * @param keyword 搜索关键字
     * @param status 用例状态
     * @param projectId 项目ID (可选)
     * @param offset 偏移量
     * @param pageSize 页面大小
     * @return UI测试用例列表
     */
    List<UiTestCase> selectByPage(
            @Param("keyword") String keyword,
            @Param("status") String status,
            @Param("projectId") Integer projectId,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);
    
    /**
     * 查询UI测试用例总数
     *
     * @param keyword 搜索关键字
     * @param status 用例状态
     * @param projectId 项目ID (可选)
     * @return UI测试用例总数
     */
    int countTotal(
            @Param("keyword") String keyword,
            @Param("status") String status,
            @Param("projectId") Integer projectId);
    
    /**
     * 生成新的用例ID
     *
     * @return 用例ID
     */
    String generateCaseId();
    
    /**
     * 更新用例执行状态
     *
     * @param caseId 用例ID
     * @param caseStatus 用例状态
     * @param lastResult 最后执行结果
     * @return 影响行数
     */
    int updateExecutionStatus(
            @Param("caseId") String caseId, 
            @Param("caseStatus") String caseStatus, 
            @Param("lastResult") String lastResult);

    /**
     * 根据ID查询测试用例
     *
     * @param id 测试用例ID
     * @return 测试用例信息
     */
    UiTestCase selectById(@Param("id") Long id);
    
    /**
     * 根据ID列表查询测试用例
     *
     * @param ids 测试用例ID列表
     * @return 测试用例列表
     */
    List<UiTestCase> selectByIds(@Param("list") List<Long> ids);
    
    /**
     * 分页查询测试用例列表
     *
     * @param keyword 搜索关键词
     * @param status 用例状态
     * @param offset 偏移量
     * @param pageSize 页大小
     * @return 测试用例列表
     */
    List<UiTestCase> selectByPage(@Param("keyword") String keyword, 
                                 @Param("status") String status,
                                 @Param("offset") Integer offset, 
                                 @Param("pageSize") Integer pageSize);
    
    /**
     * 查询未在指定套件中的测试用例
     *
     * @param suiteId 套件ID
     * @param keyword 搜索关键词
     * @param offset 偏移量
     * @param pageSize 页大小
     * @return 测试用例列表
     */
    List<UiTestCase> selectNotInSuite(@Param("suiteId") Long suiteId, 
                                     @Param("keyword") String keyword,
                                     @Param("offset") Integer offset, 
                                     @Param("pageSize") Integer pageSize);
    
    /**
     * 统计符合条件的测试用例数量
     *
     * @param keyword 搜索关键词
     * @param status 用例状态
     * @return 测试用例数量
     */
    Integer countTestCases(@Param("keyword") String keyword, @Param("status") String status);
    
    /**
     * 统计未在指定套件中的测试用例数量
     *
     * @param suiteId 套件ID
     * @param keyword 搜索关键词
     * @return 测试用例数量
     */
    Integer countNotInSuite(@Param("suiteId") Long suiteId, @Param("keyword") String keyword);
    
    /**
     * 更新测试用例
     *
     * @param uiTestCase 测试用例信息
     * @return 影响行数
     */
    int updateById(UiTestCase uiTestCase);
    
    /**
     * 删除测试用例
     *
     * @param id 测试用例ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
} 