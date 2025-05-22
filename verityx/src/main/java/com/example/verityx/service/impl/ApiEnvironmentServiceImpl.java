package com.example.verityx.service.impl;

import com.example.verityx.entity.ApiEnvironment;
import com.example.verityx.mapper.ApiEnvironmentMapper;
import com.example.verityx.service.ApiEnvironmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiEnvironmentServiceImpl implements ApiEnvironmentService {

    @Autowired
    private ApiEnvironmentMapper apiEnvironmentMapper;

    @Override
    @Transactional
    public ApiEnvironment createEnvironment(ApiEnvironment environment) {
        // 如果设置为默认环境，需要清除其他环境的默认状态
        if (environment.getIsDefault() != null && environment.getIsDefault()) {
            apiEnvironmentMapper.clearDefaultStatus(environment.getProjectId());
        }
        apiEnvironmentMapper.insertEnvironment(environment);
        return environment;
    }

    @Override
    public ApiEnvironment getEnvironmentById(Integer id) {
        return apiEnvironmentMapper.selectEnvironmentById(id);
    }

    @Override
    public List<ApiEnvironment> getAllEnvironments(String projectId) {
        return apiEnvironmentMapper.selectAllEnvironments(projectId);
    }

    @Override
    public Map<String, Object> getEnvironmentsByPage(String keyword, String projectId, int page, int pageSize) {
        Map<String, Object> result = new HashMap<>();
        
        // 计算偏移量
        int offset = (page - 1) * pageSize;
        
        // 查询数据
        List<ApiEnvironment> environments = apiEnvironmentMapper.selectEnvironmentsByPage(keyword, projectId, offset, pageSize);
        int total = apiEnvironmentMapper.countEnvironments(keyword, projectId);
        
        result.put("data", environments);
        result.put("total", total);
        result.put("page", page);
        result.put("pageSize", pageSize);
        
        return result;
    }

    @Override
    @Transactional
    public boolean updateEnvironment(ApiEnvironment environment) {
        // 如果设置为默认环境，需要清除其他环境的默认状态
        if (environment.getIsDefault() != null && environment.getIsDefault()) {
            apiEnvironmentMapper.clearDefaultStatus(environment.getProjectId());
        }
        return apiEnvironmentMapper.updateEnvironment(environment) > 0;
    }

    @Override
    public boolean deleteEnvironment(Integer id) {
        // 检查是否为默认环境，如果是则不允许删除
        ApiEnvironment environment = apiEnvironmentMapper.selectEnvironmentById(id);
        if (environment != null && environment.getIsDefault()) {
            return false; // 默认环境不能删除
        }
        return apiEnvironmentMapper.deleteEnvironment(id) > 0;
    }
    
    @Override
    public boolean batchDeleteEnvironments(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        // 不允许删除默认环境，需要先检查
        for (Integer id : ids) {
            ApiEnvironment environment = apiEnvironmentMapper.selectEnvironmentById(id);
            if (environment != null && environment.getIsDefault()) {
                return false; // 默认环境不能删除
            }
        }
        return apiEnvironmentMapper.batchDeleteEnvironments(ids) > 0;
    }
    
    @Override
    public ApiEnvironment getDefaultEnvironment(String projectId) {
        return apiEnvironmentMapper.selectDefaultEnvironment(projectId);
    }
    
    @Override
    @Transactional
    public boolean setDefaultEnvironment(Integer id, String projectId) {
        // 先清除所有默认状态
        apiEnvironmentMapper.clearDefaultStatus(projectId);
        // 再设置指定ID为默认
        return apiEnvironmentMapper.setDefaultEnvironment(id) > 0;
    }
} 