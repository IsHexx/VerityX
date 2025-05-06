package com.example.verityx.service.impl;

import com.example.verityx.dto.UiTestConfigDTO;
import com.example.verityx.entity.UiTestConfig;
import com.example.verityx.mapper.UiTestConfigMapper;
import com.example.verityx.service.UiTestConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * UI测试配置Service实现类
 */
@Service
public class UiTestConfigServiceImpl implements UiTestConfigService {

    @Autowired
    private UiTestConfigMapper uiTestConfigMapper;

    @Override
    @Transactional
    public UiTestConfigDTO createConfig(UiTestConfigDTO configDTO) {
        UiTestConfig config = configDTO.toEntity();
        
        // 设置创建和更新时间
        Date now = new Date();
        config.setCreatedAt(now);
        config.setUpdatedAt(now);
        
        // 如果设置为默认配置，则重置同类型的其他默认配置
        if (Boolean.TRUE.equals(config.getIsDefault())) {
            uiTestConfigMapper.resetDefaultConfig(config.getConfigType());
        }
        
        uiTestConfigMapper.insert(config);
        return new UiTestConfigDTO(config);
    }

    @Override
    @Transactional
    public UiTestConfigDTO updateConfig(UiTestConfigDTO configDTO) {
        UiTestConfig config = configDTO.toEntity();
        
        // 设置更新时间
        config.setUpdatedAt(new Date());
        
        // 如果设置为默认配置，则重置同类型的其他默认配置
        if (Boolean.TRUE.equals(config.getIsDefault())) {
            uiTestConfigMapper.resetDefaultConfig(config.getConfigType());
        }
        
        uiTestConfigMapper.update(config);
        return getConfigById(config.getId());
    }

    @Override
    public UiTestConfigDTO getConfigById(Long id) {
        UiTestConfig config = uiTestConfigMapper.selectById(id);
        return config != null ? new UiTestConfigDTO(config) : null;
    }

    @Override
    public List<UiTestConfigDTO> getConfigList(String configType, String configName) {
        List<UiTestConfig> configs = uiTestConfigMapper.selectList(configType, configName);
        return configs.stream()
                .map(UiTestConfigDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean deleteConfig(Long id) {
        // 检查是否为默认配置
        UiTestConfig config = uiTestConfigMapper.selectById(id);
        if (config == null) {
            return false;
        }
        
        int result = uiTestConfigMapper.deleteById(id);
        return result > 0;
    }

    @Override
    @Transactional
    public UiTestConfigDTO setDefaultConfig(Long id) {
        UiTestConfig config = uiTestConfigMapper.selectById(id);
        if (config == null) {
            return null;
        }
        
        // 重置同类型的默认配置
        uiTestConfigMapper.resetDefaultConfig(config.getConfigType());
        
        // 设置当前配置为默认配置
        config.setIsDefault(true);
        config.setUpdatedAt(new Date());
        uiTestConfigMapper.update(config);
        
        return new UiTestConfigDTO(config);
    }

    @Override
    public UiTestConfigDTO getDefaultConfig(String configType) {
        UiTestConfig config = uiTestConfigMapper.selectDefaultConfig(configType);
        return config != null ? new UiTestConfigDTO(config) : null;
    }

    @Override
    @Transactional
    public UiTestConfigDTO toggleConfigStatus(Long id, boolean active) {
        UiTestConfig config = uiTestConfigMapper.selectById(id);
        if (config == null) {
            return null;
        }
        
        config.setIsActive(active);
        config.setUpdatedAt(new Date());
        uiTestConfigMapper.update(config);
        
        return new UiTestConfigDTO(config);
    }

    @Override
    public Map<String, Integer> getConfigTypeStats() {
        Map<String, Integer> stats = new HashMap<>();
        List<String> configTypes = Arrays.asList("BROWSER", "WAIT_TIME", "SCREENSHOT", "RETRY");
        
        for (String type : configTypes) {
            int count = uiTestConfigMapper.countByType(type);
            stats.put(type, count);
        }
        
        return stats;
    }
} 