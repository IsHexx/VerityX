package com.example.verityx.service;

import java.util.List;

import com.example.verityx.dto.PageResult;
import com.example.verityx.dto.UiTestExecutionCreateRequest;
import com.example.verityx.dto.UiTestExecutionDTO;
import com.example.verityx.dto.UiTestExecutionDetailDTO;
import com.example.verityx.dto.UiTestExecutionLogDTO;
import com.example.verityx.dto.UiTestExecutionStepDTO;

/**
 * UI测试执行服务接口
 */
public interface UiTestExecutionService {

    /**
     * 创建执行记录
     *
     * @param request 创建请求
     * @param executor 执行人
     * @return 执行ID
     */
    Long createExecution(UiTestExecutionCreateRequest request, String executor);

    /**
     * 获取执行详情
     *
     * @param id 执行ID
     * @return 执行详情
     */
    UiTestExecutionDTO getExecutionById(Long id);

    /**
     * 分页查询执行记录列表
     *
     * @param page 页码
     * @param pageSize 页大小
     * @param keyword 关键字
     * @param status 状态
     * @param executionType 执行类型
     * @param projectId 项目ID
     * @return 分页结果
     */
    PageResult<UiTestExecutionDTO> getExecutionList(int page, int pageSize, String keyword, String status, String executionType, Long projectId);

    /**
     * 修改执行状态
     *
     * @param id 执行ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateExecutionStatus(Long id, String status);

    /**
     * 开始执行
     *
     * @param id 执行ID
     * @return 是否成功
     */
    boolean startExecution(Long id);

    /**
     * 完成执行
     *
     * @param id 执行ID
     * @param success 是否成功
     * @param errorMessage 错误信息
     * @return 是否成功
     */
    boolean finishExecution(Long id, boolean success, String errorMessage);

    /**
     * 中止执行
     *
     * @param id 执行ID
     * @param reason 原因
     * @return 是否成功
     */
    boolean abortExecution(Long id, String reason);

    /**
     * 删除执行记录
     *
     * @param id 执行ID
     * @return 是否成功
     */
    boolean deleteExecution(Long id);

    /**
     * 获取执行用例详情列表
     *
     * @param executionId 执行ID
     * @return 执行用例详情列表
     */
    List<UiTestExecutionDetailDTO> getExecutionDetails(Long executionId);

    /**
     * 获取执行用例详情
     *
     * @param detailId 详情ID
     * @return 执行用例详情
     */
    UiTestExecutionDetailDTO getExecutionDetail(Long detailId);

    /**
     * 更新执行用例详情状态
     *
     * @param detailId 详情ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateDetailStatus(Long detailId, String status);

    /**
     * 开始执行用例
     *
     * @param detailId 详情ID
     * @return 是否成功
     */
    boolean startDetail(Long detailId);

    /**
     * 完成执行用例
     *
     * @param detailId 详情ID
     * @param success 是否成功
     * @param errorMessage 错误信息
     * @return 是否成功
     */
    boolean finishDetail(Long detailId, boolean success, String errorMessage);

    /**
     * 获取执行步骤列表
     *
     * @param detailId 详情ID
     * @return 执行步骤列表
     */
    List<UiTestExecutionStepDTO> getExecutionSteps(Long detailId);

    /**
     * 记录执行步骤结果
     *
     * @param step 步骤结果
     * @return 步骤ID
     */
    Long recordStepResult(UiTestExecutionStepDTO step);

    /**
     * 获取执行日志
     *
     * @param executionId 执行ID
     * @param logLevel 日志级别
     * @param page 页码
     * @param pageSize 页大小
     * @return 分页结果
     */
    PageResult<UiTestExecutionLogDTO> getExecutionLogs(Long executionId, String logLevel, int page, int pageSize);

    /**
     * 获取用例执行日志
     *
     * @param detailId 详情ID
     * @return 日志列表
     */
    List<UiTestExecutionLogDTO> getDetailLogs(Long detailId);

    /**
     * 记录日志
     *
     * @param log 日志记录
     * @return 日志ID
     */
    Long recordLog(UiTestExecutionLogDTO log);

    /**
     * 获取执行队列
     *
     * @param status 状态
     * @param page 页码
     * @param pageSize 页大小
     * @param projectId 项目ID
     * @return 分页结果
     */
    PageResult<UiTestExecutionDTO> getExecutionQueue(String status, int page, int pageSize, Long projectId);

    /**
     * 获取下一个等待执行的任务
     *
     * @return 执行记录
     */
    UiTestExecutionDTO getNextWaitingExecution();
}