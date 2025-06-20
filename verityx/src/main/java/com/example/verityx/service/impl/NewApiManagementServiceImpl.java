package com.example.verityx.service.impl;

import com.example.verityx.entity.ApiModule;
import com.example.verityx.entity.ApiEndpoint;
import com.example.verityx.entity.ApiTestCase;
import com.example.verityx.mapper.ApiModuleMapper;
import com.example.verityx.mapper.ApiEndpointMapper;
import com.example.verityx.mapper.ApiTestCaseMapper;
import com.example.verityx.service.NewApiManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新的API管理服务实现类
 */
@Service
public class NewApiManagementServiceImpl implements NewApiManagementService {

    @Autowired
    private ApiModuleMapper apiModuleMapper;

    @Autowired
    private ApiEndpointMapper apiEndpointMapper;

    @Autowired
    private ApiTestCaseMapper apiTestCaseMapper;

    // ==================== 模块管理 ====================

    @Override
    public boolean createModule(ApiModule module) {
        // 检查模块名是否已存在
        int count = apiModuleMapper.countByNameAndProjectId(module.getName(), String.valueOf(module.getProjectId()));
        if (count > 0) {
            throw new RuntimeException("模块名称已存在");
        }
        return apiModuleMapper.insertModule(module) > 0;
    }

    @Override
    public List<ApiModule> getModulesByProjectId(String projectId) {
        return apiModuleMapper.selectModulesByProjectId(projectId);
    }

    @Override
    public boolean updateModule(ApiModule module) {
        return apiModuleMapper.updateModule(module) > 0;
    }

    @Override
    @Transactional
    public boolean deleteModule(int moduleId) {
        // 级联删除：先删除模块下的所有接口和用例
        List<ApiEndpoint> endpoints = apiEndpointMapper.selectEndpointsByModuleId(moduleId);
        for (ApiEndpoint endpoint : endpoints) {
            // 删除接口下的所有用例
            apiTestCaseMapper.deleteTestCasesByApiId(endpoint.getId());
        }
        
        // 删除模块下的所有接口
        apiEndpointMapper.deleteEndpointsByModuleId(moduleId);
        
        // 删除模块本身
        return apiModuleMapper.deleteModule(moduleId) > 0;
    }

    @Override
    public ApiModule getModuleById(int moduleId) {
        return apiModuleMapper.selectModuleById(moduleId);
    }

    // ==================== 接口管理 ====================

    @Override
    public boolean createEndpoint(ApiEndpoint endpoint) {
        return apiEndpointMapper.insertEndpoint(endpoint) > 0;
    }

    @Override
    public List<ApiEndpoint> getEndpointsByModuleId(int moduleId) {
        return apiEndpointMapper.selectEndpointsByModuleId(moduleId);
    }

    @Override
    public List<ApiEndpoint> getEndpointsByProjectId(String projectId) {
        return apiEndpointMapper.selectEndpointsByProjectId(projectId);
    }

    @Override
    public boolean updateEndpoint(ApiEndpoint endpoint) {
        return apiEndpointMapper.updateEndpoint(endpoint) > 0;
    }

    @Override
    @Transactional
    public boolean deleteEndpoint(int endpointId) {
        // 先删除接口下的所有用例
        apiTestCaseMapper.deleteTestCasesByApiId(endpointId);
        
        // 删除接口本身
        return apiEndpointMapper.deleteEndpoint(endpointId) > 0;
    }

    @Override
    public ApiEndpoint getEndpointById(int endpointId) {
        return apiEndpointMapper.selectEndpointById(endpointId);
    }

    // ==================== 测试用例管理 ====================

    @Override
    public boolean createTestCase(ApiTestCase testCase) {
        return apiTestCaseMapper.insertTestCase(testCase) > 0;
    }

    @Override
    public List<ApiTestCase> getTestCasesByApiId(int apiId) {
        return apiTestCaseMapper.selectTestCasesByApiId(apiId);
    }

    @Override
    public boolean updateTestCase(ApiTestCase testCase) {
        return apiTestCaseMapper.updateTestCase(testCase) > 0;
    }

    @Override
    public boolean deleteTestCase(int testCaseId) {
        return apiTestCaseMapper.deleteTestCase(testCaseId) > 0;
    }

    @Override
    public ApiTestCase getTestCaseById(int testCaseId) {
        return apiTestCaseMapper.selectTestCaseById(testCaseId);
    }

    // ==================== 树形结构 ====================

    @Override
    public List<Map<String, Object>> getTreeStructure(String projectId) {
        List<Map<String, Object>> result = new ArrayList<>();
        
        // 获取所有模块
        List<ApiModule> modules = getModulesByProjectId(projectId);
        
        for (ApiModule module : modules) {
            Map<String, Object> moduleNode = new HashMap<>();
            moduleNode.put("id", module.getId());
            moduleNode.put("name", module.getName());
            moduleNode.put("type", "module");
            moduleNode.put("description", module.getDescription());
            moduleNode.put("status", module.getStatus());
            
            // 获取模块下的所有接口
            List<ApiEndpoint> endpoints = getEndpointsByModuleId(module.getId());
            List<Map<String, Object>> endpointNodes = new ArrayList<>();
            
            for (ApiEndpoint endpoint : endpoints) {
                Map<String, Object> endpointNode = new HashMap<>();
                endpointNode.put("id", endpoint.getId());
                endpointNode.put("name", endpoint.getName());
                endpointNode.put("type", "endpoint");
                endpointNode.put("path", endpoint.getPath());
                endpointNode.put("method", endpoint.getMethod());
                endpointNode.put("description", endpoint.getDescription());
                endpointNode.put("status", endpoint.getStatus());
                
                // 获取接口下的所有测试用例
                List<ApiTestCase> testCases = getTestCasesByApiId(endpoint.getId());
                List<Map<String, Object>> testCaseNodes = new ArrayList<>();
                
                for (ApiTestCase testCase : testCases) {
                    Map<String, Object> testCaseNode = new HashMap<>();
                    testCaseNode.put("id", testCase.getId());
                    testCaseNode.put("name", testCase.getName());
                    testCaseNode.put("type", "testcase");
                    testCaseNode.put("description", testCase.getDescription());
                    testCaseNode.put("status", testCase.getStatus());
                    testCaseNodes.add(testCaseNode);
                }
                
                endpointNode.put("children", testCaseNodes);
                endpointNodes.add(endpointNode);
            }
            
            moduleNode.put("children", endpointNodes);
            result.add(moduleNode);
        }
        
        return result;
    }
}
