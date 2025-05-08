package com.example.verityx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.verityx.dto.SysConfigDTO;
import com.example.verityx.dto.request.SysConfigCreateRequest;
import com.example.verityx.dto.request.SysConfigUpdateRequest;
import com.example.verityx.entity.SysConfig;
import com.example.verityx.mapper.SysConfigMapper;
import com.example.verityx.service.SysConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统参数配置服务实现类
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

    @Autowired
    private SysConfigMapper configMapper;

    @Override
    public IPage<SysConfigDTO> getConfigPage(int page, int size, String configName, String configKey, String configType) {
        // 构建查询条件
        LambdaQueryWrapper<SysConfig> queryWrapper = new LambdaQueryWrapper<>();
        
        // 根据条件过滤
        if (StringUtils.hasText(configName)) {
            queryWrapper.like(SysConfig::getConfigName, configName);
        }
        if (StringUtils.hasText(configKey)) {
            queryWrapper.like(SysConfig::getConfigKey, configKey);
        }
        if (StringUtils.hasText(configType)) {
            queryWrapper.eq(SysConfig::getConfigType, configType);
        }
        
        // 按创建时间倒序排序
        queryWrapper.orderByDesc(SysConfig::getCreatedAt);
        
        // 执行分页查询
        Page<SysConfig> configPage = new Page<>(page, size);
        Page<SysConfig> resultPage = configMapper.selectPage(configPage, queryWrapper);
        
        // 转换为DTO对象
        Page<SysConfigDTO> dtoPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        List<SysConfigDTO> dtoList = resultPage.getRecords().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        dtoPage.setRecords(dtoList);
        
        return dtoPage;
    }

    @Override
    public SysConfigDTO getConfigById(Long id) {
        SysConfig config = configMapper.selectById(id);
        return config != null ? convertToDTO(config) : null;
    }

    @Override
    public SysConfigDTO getConfigByKey(String configKey) {
        LambdaQueryWrapper<SysConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysConfig::getConfigKey, configKey);
        queryWrapper.eq(SysConfig::getStatus, true);
        
        SysConfig config = configMapper.selectOne(queryWrapper);
        return config != null ? convertToDTO(config) : null;
    }

    @Override
    public String getConfigValue(String configKey) {
        SysConfigDTO config = getConfigByKey(configKey);
        return config != null ? config.getConfigValue() : "";
    }

    @Override
    public Map<String, String> getConfigMap(List<String> configKeys) {
        Map<String, String> result = new HashMap<>();
        
        // 如果键名列表为空，返回空Map
        if (configKeys == null || configKeys.isEmpty()) {
            return result;
        }
        
        // 构建查询条件
        LambdaQueryWrapper<SysConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysConfig::getConfigKey, configKeys);
        queryWrapper.eq(SysConfig::getStatus, true);
        
        // 查询配置列表
        List<SysConfig> configList = configMapper.selectList(queryWrapper);
        
        // 转换为Map
        for (SysConfig config : configList) {
            result.put(config.getConfigKey(), config.getConfigValue());
        }
        
        return result;
    }

    @Override
    @Transactional
    public Long createConfig(SysConfigCreateRequest request, Long operator) {
        // 检查键名是否已存在
        LambdaQueryWrapper<SysConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysConfig::getConfigKey, request.getConfigKey());
        if (configMapper.selectCount(queryWrapper) > 0) {
            throw new RuntimeException("配置键名已存在");
        }
        
        // 创建实体并保存
        SysConfig config = new SysConfig();
        BeanUtils.copyProperties(request, config);
        config.setCreatedAt(LocalDateTime.now());
        config.setCreatedBy(operator);
        config.setUpdatedAt(LocalDateTime.now());
        config.setUpdatedBy(operator);
        
        configMapper.insert(config);
        
        return config.getId();
    }

    @Override
    @Transactional
    public boolean updateConfig(SysConfigUpdateRequest request, Long operator) {
        // 检查配置是否存在
        SysConfig existConfig = configMapper.selectById(request.getId());
        if (existConfig == null) {
            throw new RuntimeException("配置不存在");
        }
        
        // 如果修改了键名，检查新键名是否已存在
        if (!existConfig.getConfigKey().equals(request.getConfigKey())) {
            LambdaQueryWrapper<SysConfig> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysConfig::getConfigKey, request.getConfigKey())
                     .ne(SysConfig::getId, request.getId());
            if (configMapper.selectCount(queryWrapper) > 0) {
                throw new RuntimeException("配置键名已存在");
            }
        }
        
        // 更新配置
        SysConfig config = new SysConfig();
        BeanUtils.copyProperties(request, config);
        config.setUpdatedAt(LocalDateTime.now());
        config.setUpdatedBy(operator);
        
        int rows = configMapper.updateById(config);
        
        return rows > 0;
    }

    @Override
    @Transactional
    public boolean batchUpdateConfig(List<SysConfigUpdateRequest> configs, Long operator) {
        if (configs == null || configs.isEmpty()) {
            return false;
        }
        
        for (SysConfigUpdateRequest request : configs) {
            updateConfig(request, operator);
        }
        
        return true;
    }

    @Override
    @Transactional
    public boolean deleteConfig(Long id, Long operator) {
        // 检查配置是否存在
        SysConfig config = configMapper.selectById(id);
        if (config == null) {
            throw new RuntimeException("配置不存在");
        }
        
        // 检查是否为系统内置配置
        if (config.getIsSystem()) {
            throw new RuntimeException("系统内置配置不能删除");
        }
        
        int rows = configMapper.deleteById(id);
        
        return rows > 0;
    }

    @Override
    @Transactional
    public boolean batchDeleteConfig(List<Long> ids, Long operator) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        // 检查是否包含系统内置配置
        LambdaQueryWrapper<SysConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysConfig::getId, ids);
        queryWrapper.eq(SysConfig::getIsSystem, true);
        if (configMapper.selectCount(queryWrapper) > 0) {
            throw new RuntimeException("不能删除系统内置配置");
        }
        
        int rows = configMapper.deleteBatchIds(ids);
        
        return rows > 0;
    }

    @Override
    public boolean refreshCache() {
        // 这里可以实现缓存刷新的逻辑
        // 如果使用了Redis或其他缓存，可以在这里清除相关缓存
        return true;
    }
    
    /**
     * 将实体转换为DTO
     */
    private SysConfigDTO convertToDTO(SysConfig config) {
        if (config == null) {
            return null;
        }
        
        SysConfigDTO dto = new SysConfigDTO();
        BeanUtils.copyProperties(config, dto);
        
        // 这里可以处理一些特殊字段，如创建者、更新者等信息
        // 如果需要查询用户名等信息，可以在这里处理
        
        return dto;
    }
} 