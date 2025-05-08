package com.example.verityx.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.verityx.dto.SysApiManagementDTO;
import com.example.verityx.dto.request.SysApiManagementRequest;
import com.example.verityx.entity.SysApiManagement;
import com.example.verityx.mapper.SysApiManagementMapper;
import com.example.verityx.service.SysApiManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * API管理服务实现类
 */
@Slf4j
@Service
public class SysApiManagementServiceImpl extends ServiceImpl<SysApiManagementMapper, SysApiManagement> implements SysApiManagementService {

    @Autowired
    private SysApiManagementMapper apiManagementMapper;

    @Override
    public IPage<SysApiManagementDTO> getApiManagementPage(int page, int size, String name, Boolean status) {
        Page<SysApiManagement> pageParam = new Page<>(page, size);
        return apiManagementMapper.selectApiManagementPage(pageParam, name, status);
    }

    @Override
    public SysApiManagementDTO getApiManagementById(Long id) {
        return apiManagementMapper.selectApiManagementById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createApiManagement(SysApiManagementRequest request, Long operatorId) {
        SysApiManagement apiManagement = new SysApiManagement();
        BeanUtils.copyProperties(request, apiManagement);
        apiManagement.setCreatorId(operatorId);
        apiManagement.setUpdaterId(operatorId);
        
        // 生成API密钥和密钥
        String apiKey = generateApiKey();
        String secret = generateSecret();
        
        apiManagement.setApiKey(apiKey);
        apiManagement.setSecret(secret);
        
        save(apiManagement);
        return apiManagement.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateApiManagement(SysApiManagementRequest request, Long operatorId) {
        if (request.getId() == null) {
            throw new RuntimeException("API ID不能为空");
        }
        
        SysApiManagement apiManagement = getById(request.getId());
        if (apiManagement == null) {
            throw new RuntimeException("API不存在");
        }
        
        BeanUtils.copyProperties(request, apiManagement);
        apiManagement.setUpdaterId(operatorId);
        
        return updateById(apiManagement);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteApiManagement(Long id, Long operatorId) {
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteApiManagement(List<Long> ids, Long operatorId) {
        return removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateApiManagementStatus(Long id, Boolean status, Long operatorId) {
        SysApiManagement apiManagement = getById(id);
        if (apiManagement == null) {
            throw new RuntimeException("API不存在");
        }
        
        apiManagement.setStatus(status);
        apiManagement.setUpdaterId(operatorId);
        return updateById(apiManagement);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String regenerateApiKey(Long id, Long operatorId) {
        SysApiManagement apiManagement = getById(id);
        if (apiManagement == null) {
            throw new RuntimeException("API不存在");
        }
        
        // 生成新的API密钥和密钥
        String apiKey = generateApiKey();
        String secret = generateSecret();
        
        apiManagement.setApiKey(apiKey);
        apiManagement.setSecret(secret);
        apiManagement.setUpdaterId(operatorId);
        
        updateById(apiManagement);
        return apiKey;
    }
    
    /**
     * 生成API密钥
     */
    private String generateApiKey() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }
    
    /**
     * 生成API密钥对应的密钥
     */
    private String generateSecret() {
        return UUID.randomUUID().toString().replace("-", "");
    }
} 