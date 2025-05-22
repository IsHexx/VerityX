package com.example.verityx.service;

import com.example.verityx.dto.UiTestConfigDTO;
import com.example.verityx.entity.UiTestConfig;

import java.util.List;
import java.util.Map;

/**
 * UI测试配置Service接口
 */
public interface UiTestConfigService {

    /**
     * 创建UI测试配置
     * @param configDTO UI测试配置DTO
     * @return 创建后的配置DTO
     */
    UiTestConfigDTO createConfig(UiTestConfigDTO configDTO);

    /**
     * 更新UI测试配置
     * @param configDTO UI测试配置DTO
     * @return 更新后的配置DTO
     */
    UiTestConfigDTO updateConfig(UiTestConfigDTO configDTO);

    /**
     * 根据ID查询UI测试配置
     * @param id 配置ID
     * @return UI测试配置DTO
     */
    UiTestConfigDTO getConfigById(Long id);

    /**
     * 获取配置列表
     * @param configType 配置类型
     * @param configName 配置名称（模糊查询）
     * @param projectId 项目ID
     * @return UI测试配置DTO列表
     */
    List<UiTestConfigDTO> getConfigList(String configType, String configName, Integer projectId);

    /**
     * 根据ID删除UI测试配置
     * @param id 配置ID
     * @return 是否删除成功
     */
    boolean deleteConfig(Long id);

    /**
     * 设置默认配置
     * @param id 配置ID
     * @return 设置后的配置DTO
     */
    UiTestConfigDTO setDefaultConfig(Long id);

    /**
     * 获取默认配置
     * @param configType 配置类型
     * @param projectId 项目ID（可选）
     * @return 默认配置DTO
     */
    UiTestConfigDTO getDefaultConfig(String configType, Integer projectId);

    /**
     * 启用/禁用配置
     * @param id 配置ID
     * @param active 是否激活
     * @return 更新后的配置DTO
     */
    UiTestConfigDTO toggleConfigStatus(Long id, boolean active);

    /**
     * 获取配置类型统计
     * @return 各配置类型的数量统计
     */
    Map<String, Integer> getConfigTypeStats();
}