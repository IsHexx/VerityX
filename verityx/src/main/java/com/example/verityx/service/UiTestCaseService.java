package com.example.verityx.service;

import com.example.verityx.dto.UiTestCaseDTO;

import java.util.List;
import java.util.Map;

/**
 * UI测试用例服务接口
 */
public interface UiTestCaseService {
    /**
     * 创建UI测试用例
     *
     * @param uiTestCaseDTO UI测试用例数据传输对象
     * @return 创建的用例ID
     */
    String createUiTestCase(UiTestCaseDTO uiTestCaseDTO);
    
    /**
     * 更新UI测试用例
     *
     * @param uiTestCaseDTO UI测试用例数据传输对象
     * @return 是否更新成功
     */
    boolean updateUiTestCase(UiTestCaseDTO uiTestCaseDTO);
    
    /**
     * 删除UI测试用例
     *
     * @param caseId 用例ID
     * @return 是否删除成功
     */
    boolean deleteUiTestCase(String caseId);
    
    /**
     * 获取UI测试用例详情
     *
     * @param caseId 用例ID
     * @return UI测试用例数据传输对象
     */
    UiTestCaseDTO getUiTestCaseDetail(String caseId);
    
    /**
     * 分页查询UI测试用例
     *
     * @param keyword 搜索关键字
     * @param status 用例状态 (all_cases, executed, unexecuted)
     * @param page 页码
     * @param pageSize 每页大小
     * @return 包含列表和总数的Map
     */
    Map<String, Object> getUiTestCaseList(String keyword, String status, int page, int pageSize);
    
    /**
     * 执行UI测试用例
     *
     * @param caseId 用例ID
     * @return 执行结果ID
     */
    String executeUiTestCase(String caseId);
} 