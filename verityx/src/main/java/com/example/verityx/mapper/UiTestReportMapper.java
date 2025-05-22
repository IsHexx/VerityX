package com.example.verityx.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.verityx.entity.UiTestReport;

/**
 * UI测试报告Mapper接口
 */
@Mapper
public interface UiTestReportMapper {

    /**
     * 插入测试报告
     *
     * @param report 测试报告对象
     * @return 影响的行数
     */
    int insert(UiTestReport report);

    /**
     * 根据ID查询测试报告
     *
     * @param id 测试报告ID
     * @return 测试报告对象
     */
    UiTestReport selectById(@Param("id") Long id);

    /**
     * 根据执行ID查询测试报告
     *
     * @param executionId 执行ID
     * @return 测试报告对象
     */
    UiTestReport selectByExecutionId(@Param("executionId") Long executionId);

    /**
     * 更新测试报告
     *
     * @param report 测试报告对象
     * @return 影响的行数
     */
    int update(UiTestReport report);

    /**
     * 删除测试报告
     *
     * @param id 测试报告ID
     * @return 影响的行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据执行ID删除测试报告
     *
     * @param executionId 执行ID
     * @return 影响的行数
     */
    int deleteByExecutionId(@Param("executionId") Long executionId);

    /**
     * 分页查询测试报告
     *
     * @param keyword 关键字
     * @param reportType 报告类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param passRateCondition 通过率条件
     * @param offset 偏移量
     * @param pageSize 页大小
     * @param projectId 项目ID
     * @return 测试报告列表
     */
    List<UiTestReport> selectByPage(
            @Param("keyword") String keyword,
            @Param("reportType") String reportType,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("passRateCondition") String passRateCondition,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize,
            @Param("projectId") Long projectId);

    /**
     * 统计测试报告总数
     *
     * @param keyword 关键字
     * @param reportType 报告类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param passRateCondition 通过率条件
     * @param projectId 项目ID
     * @return 总数
     */
    int countByCondition(
            @Param("keyword") String keyword,
            @Param("reportType") String reportType,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("passRateCondition") String passRateCondition,
            @Param("projectId") Long projectId);

    /**
     * 批量删除测试报告
     *
     * @param ids ID列表
     * @return 影响的行数
     */
    int batchDelete(@Param("ids") List<Long> ids);
}