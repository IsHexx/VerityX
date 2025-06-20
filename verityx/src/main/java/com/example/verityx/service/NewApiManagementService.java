package com.example.verityx.service;

import com.example.verityx.entity.ApiModule;
import com.example.verityx.entity.ApiEndpoint;
import com.example.verityx.entity.ApiTestCase;

import java.util.List;
import java.util.Map;

/**
 * 新的API管理服务接口
 */
public interface NewApiManagementService {

    // ==================== 模块管理 ====================
    
    /**
     * 创建模块
     */
    boolean createModule(ApiModule module);
    
    /**
     * 获取项目下的所有模块
     */
    List<ApiModule> getModulesByProjectId(String projectId);
    
    /**
     * 更新模块
     */
    boolean updateModule(ApiModule module);
    
    /**
     * 删除模块（级联删除下面的接口和用例）
     */
    boolean deleteModule(int moduleId);
    
    /**
     * 根据ID获取模块
     */
    ApiModule getModuleById(int moduleId);

    // ==================== 接口管理 ====================
    
    /**
     * 创建接口
     */
    boolean createEndpoint(ApiEndpoint endpoint);
    
    /**
     * 获取模块下的所有接口
     */
    List<ApiEndpoint> getEndpointsByModuleId(int moduleId);
    
    /**
     * 获取项目下的所有接口
     */
    List<ApiEndpoint> getEndpointsByProjectId(String projectId);
    
    /**
     * 更新接口
     */
    boolean updateEndpoint(ApiEndpoint endpoint);
    
    /**
     * 删除接口（级联删除下面的用例）
     */
    boolean deleteEndpoint(int endpointId);
    
    /**
     * 根据ID获取接口
     */
    ApiEndpoint getEndpointById(int endpointId);

    // ==================== 测试用例管理 ====================
    
    /**
     * 创建测试用例
     */
    boolean createTestCase(ApiTestCase testCase);
    
    /**
     * 获取接口下的所有测试用例
     */
    List<ApiTestCase> getTestCasesByApiId(int apiId);
    
    /**
     * 更新测试用例
     */
    boolean updateTestCase(ApiTestCase testCase);
    
    /**
     * 删除测试用例
     */
    boolean deleteTestCase(int testCaseId);
    
    /**
     * 根据ID获取测试用例
     */
    ApiTestCase getTestCaseById(int testCaseId);

    // ==================== 树形结构 ====================
    
    /**
     * 获取项目的完整树形结构
     */
    List<Map<String, Object>> getTreeStructure(String projectId);
}
