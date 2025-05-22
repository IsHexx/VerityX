package com.example.verityx.service;

import com.example.verityx.entity.ApiEnvironment;

import java.util.List;
import java.util.Map;

public interface ApiEnvironmentService {

    /**
     * 创建环境配置
     */
    ApiEnvironment createEnvironment(ApiEnvironment environment);
    
    /**
     * 获取环境配置详情
     */
    ApiEnvironment getEnvironmentById(Integer id);
    
    /**
     * 获取所有环境配置
     */
    List<ApiEnvironment> getAllEnvironments(String projectId);
    
    /**
     * 分页获取环境配置
     */
    Map<String, Object> getEnvironmentsByPage(String keyword, String projectId, int page, int pageSize);
    
    /**
     * 更新环境配置
     */
    boolean updateEnvironment(ApiEnvironment environment);
    
    /**
     * 删除环境配置
     */
    boolean deleteEnvironment(Integer id);
    
    /**
     * 批量删除环境配置
     */
    boolean batchDeleteEnvironments(List<Integer> ids);
    
    /**
     * 获取默认环境配置
     */
    ApiEnvironment getDefaultEnvironment(String projectId);
    
    /**
     * 设置默认环境配置
     */
    boolean setDefaultEnvironment(Integer id, String projectId);
} 