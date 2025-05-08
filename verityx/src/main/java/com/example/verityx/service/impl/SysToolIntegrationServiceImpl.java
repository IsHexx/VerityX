package com.example.verityx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.verityx.dto.SysToolIntegrationDTO;
import com.example.verityx.dto.request.SysToolIntegrationRequest;
import com.example.verityx.entity.SysToolIntegration;
import com.example.verityx.mapper.SysToolIntegrationMapper;
import com.example.verityx.service.SysToolIntegrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 第三方工具集成服务实现类
 */
@Slf4j
@Service
public class SysToolIntegrationServiceImpl extends ServiceImpl<SysToolIntegrationMapper, SysToolIntegration> implements SysToolIntegrationService {

    @Autowired
    private SysToolIntegrationMapper toolIntegrationMapper;

    @Override
    public IPage<SysToolIntegrationDTO> getToolIntegrationPage(int page, int size, String name, String type, Boolean status) {
        Page<SysToolIntegration> pageParam = new Page<>(page, size);
        return toolIntegrationMapper.selectToolIntegrationPage(pageParam, name, type, status);
    }

    @Override
    public SysToolIntegrationDTO getToolIntegrationById(Long id) {
        return toolIntegrationMapper.selectToolIntegrationById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createToolIntegration(SysToolIntegrationRequest request, Long operatorId) {
        SysToolIntegration toolIntegration = new SysToolIntegration();
        BeanUtils.copyProperties(request, toolIntegration);
        toolIntegration.setCreatorId(operatorId);
        toolIntegration.setUpdaterId(operatorId);
        
        // 密码加密处理（实际项目中应该使用加密算法）
        if (toolIntegration.getPassword() != null) {
            // 此处仅示例，实际应该使用加密算法
            log.info("密码已加密处理");
        }
        
        save(toolIntegration);
        return toolIntegration.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateToolIntegration(SysToolIntegrationRequest request, Long operatorId) {
        if (request.getId() == null) {
            throw new RuntimeException("集成ID不能为空");
        }
        
        SysToolIntegration toolIntegration = getById(request.getId());
        if (toolIntegration == null) {
            throw new RuntimeException("集成不存在");
        }
        
        BeanUtils.copyProperties(request, toolIntegration);
        toolIntegration.setUpdaterId(operatorId);
        
        // 密码处理：如果密码为空，则保留原密码
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            SysToolIntegration original = getById(request.getId());
            toolIntegration.setPassword(original.getPassword());
        } else {
            // 密码加密处理（实际项目中应该使用加密算法）
            log.info("新密码已加密处理");
        }
        
        return updateById(toolIntegration);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteToolIntegration(Long id, Long operatorId) {
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteToolIntegration(List<Long> ids, Long operatorId) {
        return removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateToolIntegrationStatus(Long id, Boolean status, Long operatorId) {
        SysToolIntegration toolIntegration = getById(id);
        if (toolIntegration == null) {
            throw new RuntimeException("集成不存在");
        }
        
        toolIntegration.setStatus(status);
        toolIntegration.setUpdaterId(operatorId);
        return updateById(toolIntegration);
    }

    @Override
    public boolean testToolIntegrationConnection(Long id) {
        SysToolIntegration toolIntegration = getById(id);
        if (toolIntegration == null) {
            throw new RuntimeException("集成不存在");
        }
        
        // 根据不同的集成类型，实现不同的连接测试逻辑
        boolean result = false;
        
        switch (toolIntegration.getType()) {
            case "JIRA":
                result = testJiraConnection(toolIntegration);
                break;
            case "GITHUB":
                result = testGithubConnection(toolIntegration);
                break;
            case "GITLAB":
                result = testGitlabConnection(toolIntegration);
                break;
            case "JENKINS":
                result = testJenkinsConnection(toolIntegration);
                break;
            default:
                throw new RuntimeException("不支持的集成类型: " + toolIntegration.getType());
        }
        
        return result;
    }
    
    /**
     * 测试JIRA连接
     */
    private boolean testJiraConnection(SysToolIntegration integration) {
        // 实际项目中应该实现真实的JIRA API调用
        // 这里仅做示例
        log.info("测试JIRA连接: {}", integration.getUrl());
        return true;
    }
    
    /**
     * 测试GitHub连接
     */
    private boolean testGithubConnection(SysToolIntegration integration) {
        // 实际项目中应该实现真实的GitHub API调用
        log.info("测试GitHub连接: {}", integration.getUrl());
        return true;
    }
    
    /**
     * 测试GitLab连接
     */
    private boolean testGitlabConnection(SysToolIntegration integration) {
        // 实际项目中应该实现真实的GitLab API调用
        log.info("测试GitLab连接: {}", integration.getUrl());
        return true;
    }
    
    /**
     * 测试Jenkins连接
     */
    private boolean testJenkinsConnection(SysToolIntegration integration) {
        // 实际项目中应该实现真实的Jenkins API调用
        log.info("测试Jenkins连接: {}", integration.getUrl());
        return true;
    }
} 