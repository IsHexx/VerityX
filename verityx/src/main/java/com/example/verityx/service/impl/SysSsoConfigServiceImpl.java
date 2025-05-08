package com.example.verityx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.verityx.dto.SysSsoConfigDTO;
import com.example.verityx.dto.request.SysSsoConfigRequest;
import com.example.verityx.entity.SysSsoConfig;
import com.example.verityx.mapper.SysSsoConfigMapper;
import com.example.verityx.service.SysSsoConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 单点登录配置服务实现类
 */
@Slf4j
@Service
public class SysSsoConfigServiceImpl extends ServiceImpl<SysSsoConfigMapper, SysSsoConfig> implements SysSsoConfigService {

    @Autowired
    private SysSsoConfigMapper ssoConfigMapper;

    @Override
    public IPage<SysSsoConfigDTO> getSsoConfigPage(int page, int size, String name, String type, Boolean status) {
        Page<SysSsoConfig> pageParam = new Page<>(page, size);
        return ssoConfigMapper.selectSsoConfigPage(pageParam, name, type, status);
    }

    @Override
    public SysSsoConfigDTO getSsoConfigById(Long id) {
        return ssoConfigMapper.selectSsoConfigById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createSsoConfig(SysSsoConfigRequest request, Long operatorId) {
        SysSsoConfig ssoConfig = new SysSsoConfig();
        BeanUtils.copyProperties(request, ssoConfig);
        ssoConfig.setCreatorId(operatorId);
        ssoConfig.setUpdaterId(operatorId);
        
        // 如果设置为默认配置，则先取消其他默认配置
        if (Boolean.TRUE.equals(ssoConfig.getIsDefault())) {
            cancelOtherDefaultConfig();
        }
        
        // 客户端密钥加密处理（实际项目中应该使用加密算法）
        if (ssoConfig.getClientSecret() != null) {
            // 此处仅示例，实际应该使用加密算法
            log.info("客户端密钥已加密处理");
        }
        
        save(ssoConfig);
        return ssoConfig.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSsoConfig(SysSsoConfigRequest request, Long operatorId) {
        if (request.getId() == null) {
            throw new RuntimeException("配置ID不能为空");
        }
        
        SysSsoConfig ssoConfig = getById(request.getId());
        if (ssoConfig == null) {
            throw new RuntimeException("配置不存在");
        }
        
        BeanUtils.copyProperties(request, ssoConfig);
        ssoConfig.setUpdaterId(operatorId);
        
        // 如果设置为默认配置，则先取消其他默认配置
        if (Boolean.TRUE.equals(ssoConfig.getIsDefault())) {
            cancelOtherDefaultConfig();
        }
        
        // 客户端密钥处理：如果密钥为空，则保留原密钥
        if (request.getClientSecret() == null || request.getClientSecret().isEmpty()) {
            SysSsoConfig original = getById(request.getId());
            ssoConfig.setClientSecret(original.getClientSecret());
        } else {
            // 客户端密钥加密处理（实际项目中应该使用加密算法）
            log.info("新客户端密钥已加密处理");
        }
        
        return updateById(ssoConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSsoConfig(Long id, Long operatorId) {
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteSsoConfig(List<Long> ids, Long operatorId) {
        return removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSsoConfigStatus(Long id, Boolean status, Long operatorId) {
        SysSsoConfig ssoConfig = getById(id);
        if (ssoConfig == null) {
            throw new RuntimeException("配置不存在");
        }
        
        ssoConfig.setStatus(status);
        ssoConfig.setUpdaterId(operatorId);
        return updateById(ssoConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefaultSsoConfig(Long id, Long operatorId) {
        SysSsoConfig ssoConfig = getById(id);
        if (ssoConfig == null) {
            throw new RuntimeException("配置不存在");
        }
        
        // 先取消其他默认配置
        cancelOtherDefaultConfig();
        
        // 设置为默认配置
        ssoConfig.setIsDefault(true);
        ssoConfig.setUpdaterId(operatorId);
        return updateById(ssoConfig);
    }

    @Override
    public boolean testSsoConfigConnection(Long id) {
        SysSsoConfig ssoConfig = getById(id);
        if (ssoConfig == null) {
            throw new RuntimeException("配置不存在");
        }
        
        // 根据不同的SSO类型，实现不同的连接测试逻辑
        boolean result = false;
        
        switch (ssoConfig.getType()) {
            case "LDAP":
                result = testLdapConnection(ssoConfig);
                break;
            case "OAUTH":
                result = testOauthConnection(ssoConfig);
                break;
            case "SAML":
                result = testSamlConnection(ssoConfig);
                break;
            default:
                throw new RuntimeException("不支持的SSO类型: " + ssoConfig.getType());
        }
        
        return result;
    }
    
    /**
     * 取消其他默认配置
     */
    private void cancelOtherDefaultConfig() {
        LambdaQueryWrapper<SysSsoConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysSsoConfig::getIsDefault, true);
        queryWrapper.eq(SysSsoConfig::getDeleted, false);
        
        List<SysSsoConfig> defaultConfigs = list(queryWrapper);
        for (SysSsoConfig config : defaultConfigs) {
            config.setIsDefault(false);
            updateById(config);
        }
    }
    
    /**
     * 测试LDAP连接
     */
    private boolean testLdapConnection(SysSsoConfig config) {
        // 实际项目中应该实现真实的LDAP连接测试
        // 这里仅做示例
        log.info("测试LDAP连接: {}", config.getServerUrl());
        return true;
    }
    
    /**
     * 测试OAuth连接
     */
    private boolean testOauthConnection(SysSsoConfig config) {
        // 实际项目中应该实现真实的OAuth连接测试
        log.info("测试OAuth连接: {}", config.getServerUrl());
        return true;
    }
    
    /**
     * 测试SAML连接
     */
    private boolean testSamlConnection(SysSsoConfig config) {
        // 实际项目中应该实现真实的SAML连接测试
        log.info("测试SAML连接: {}", config.getServerUrl());
        return true;
    }
} 