package com.example.verityx.service;

import com.example.verityx.dto.PageResult;
import com.example.verityx.dto.UiTestSuiteDTO;

import java.util.List;

public interface UiTestSuiteService {
    
    /**
     * 分页获取UI测试套件列表
     *
     * @param page     页码
     * @param pageSize 每页大小
     * @param keyword  搜索关键词
     * @param status   套件状态
     * @param projectId 项目ID
     * @return 分页测试套件列表
     */
    PageResult<UiTestSuiteDTO> getUiTestSuiteList(Integer page, Integer pageSize, String keyword, String status, Long projectId);
    
    /**
     * 根据ID获取UI测试套件详情
     *
     * @param id 测试套件ID
     * @param projectId 项目ID
     * @return 测试套件详情
     */
    UiTestSuiteDTO getUiTestSuiteById(Long id, Long projectId);
    
    /**
     * 创建UI测试套件
     *
     * @param uiTestSuiteDTO 测试套件信息
     * @return 新创建的测试套件ID
     */
    Long createUiTestSuite(UiTestSuiteDTO uiTestSuiteDTO);
    
    /**
     * 更新UI测试套件
     *
     * @param id            测试套件ID
     * @param uiTestSuiteDTO 更新的测试套件信息
     */
    void updateUiTestSuite(Long id, UiTestSuiteDTO uiTestSuiteDTO);
    
    /**
     * 删除UI测试套件
     *
     * @param id 测试套件ID
     * @param projectId 项目ID
     */
    void deleteUiTestSuite(Long id, Long projectId);
    
    /**
     * 执行UI测试套件
     *
     * @param id 测试套件ID
     */
    void executeUiTestSuite(Long id);
    
    /**
     * 获取套件中的测试用例
     *
     * @param suiteId 测试套件ID
     * @return 测试用例列表
     */
    List<UiTestSuiteDTO.CaseInfo> getSuiteCases(Long suiteId);
    
    /**
     * 更新套件中的测试用例
     *
     * @param suiteId 测试套件ID
     * @param caseIds 测试用例ID列表
     */
    void updateSuiteCases(Long suiteId, List<Long> caseIds);
    
    /**
     * 更新测试套件的并发执行配置
     *
     * @param suiteId 测试套件ID
     * @param concurrencyConfig 并发配置信息
     */
    void updateConcurrencyConfig(Long suiteId, UiTestSuiteDTO.ConcurrencyConfig concurrencyConfig);
    
    /**
     * 获取测试套件的定时任务配置
     *
     * @param suiteId 测试套件ID
     * @return 定时任务配置信息
     */
    UiTestSuiteDTO.ScheduleConfig getScheduleConfig(Long suiteId);
    
    /**
     * 保存测试套件的定时任务配置
     *
     * @param suiteId 测试套件ID
     * @param scheduleConfig 定时任务配置信息
     */
    void saveScheduleConfig(Long suiteId, UiTestSuiteDTO.ScheduleConfig scheduleConfig);
    
    /**
     * 删除测试套件的定时任务配置
     *
     * @param suiteId 测试套件ID
     */
    void deleteScheduleConfig(Long suiteId);
} 