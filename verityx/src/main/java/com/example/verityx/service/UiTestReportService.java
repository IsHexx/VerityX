package com.example.verityx.service;

import java.util.List;

import com.example.verityx.dto.PageResult;
import com.example.verityx.dto.UiTestReportCreateRequest;
import com.example.verityx.dto.UiTestReportDTO;
import com.example.verityx.dto.UiTestReportDetailDTO;
import com.example.verityx.dto.UiTestStepResultDTO;

/**
 * UI测试报告服务接口
 */
public interface UiTestReportService {

    /**
     * 创建测试报告
     *
     * @param request 创建请求
     * @return 报告ID
     */
    Long createReport(UiTestReportCreateRequest request);

    /**
     * 更新测试报告
     *
     * @param id 报告ID
     * @param request 创建请求
     * @return 是否成功
     */
    boolean updateReport(Long id, UiTestReportCreateRequest request);

    /**
     * 获取测试报告
     *
     * @param id 报告ID
     * @return 测试报告DTO
     */
    UiTestReportDTO getReportById(Long id);

    /**
     * 根据执行ID获取测试报告
     *
     * @param executionId 执行ID
     * @return 测试报告DTO
     */
    UiTestReportDTO getReportByExecutionId(Long executionId);

    /**
     * 获取测试报告列表
     *
     * @param page 页码
     * @param pageSize 页大小
     * @param keyword 关键字
     * @param reportType 报告类型
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param status 报告状态(passed/failed)
     * @param projectId 项目ID
     * @return 分页结果
     */
    PageResult<UiTestReportDTO> getReportList(
            int page, int pageSize, String keyword, String reportType, String startDate, String endDate, String status, Long projectId);

    /**
     * 删除测试报告
     *
     * @param id 报告ID
     * @return 是否成功
     */
    boolean deleteReport(Long id);

    /**
     * 批量删除测试报告
     *
     * @param ids ID列表
     * @return 是否成功
     */
    boolean batchDeleteReports(List<Long> ids);

    /**
     * 根据执行ID删除测试报告
     *
     * @param executionId 执行ID
     * @return 是否成功
     */
    boolean deleteReportByExecutionId(Long executionId);

    /**
     * 获取测试报告详情列表
     *
     * @param reportId 报告ID
     * @param page 页码
     * @param pageSize 页大小
     * @param result 测试结果
     * @return 分页结果
     */
    PageResult<UiTestReportDetailDTO> getReportDetails(
            Long reportId, int page, int pageSize, String result);

    /**
     * 获取测试报告详情
     *
     * @param detailId 详情ID
     * @return 测试报告详情DTO
     */
    UiTestReportDetailDTO getReportDetail(Long detailId);

    /**
     * 获取测试步骤列表
     *
     * @param detailId 详情ID
     * @return 测试步骤结果列表
     */
    List<UiTestStepResultDTO> getReportSteps(Long detailId);

    /**
     * 从测试执行生成报告
     *
     * @param executionId 执行ID
     * @return 报告ID
     */
    Long generateReportFromExecution(Long executionId);

    /**
     * 导出测试报告
     *
     * @param id 报告ID
     * @param format 导出格式
     * @return 导出的文件路径
     */
    String exportReport(Long id, String format);
}