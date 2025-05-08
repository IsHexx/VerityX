package com.example.verityx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.verityx.dto.SysNotificationRuleDTO;
import com.example.verityx.dto.request.SysNotificationRuleRequest;
import com.example.verityx.entity.SysNotificationRule;
import com.example.verityx.mapper.SysNotificationRuleMapper;
import com.example.verityx.mapper.SysNotificationTemplateMapper;
import com.example.verityx.service.SysNotificationRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 通知规则服务实现类
 */
@Slf4j
@Service
public class SysNotificationRuleServiceImpl extends ServiceImpl<SysNotificationRuleMapper, SysNotificationRule> implements SysNotificationRuleService {

    @Autowired
    private SysNotificationRuleMapper ruleMapper;

    @Autowired
    private SysNotificationTemplateMapper templateMapper;

    @Override
    public IPage<SysNotificationRuleDTO> getRulePage(int page, int size, String name, String code, String eventType, String eventAction, Boolean status) {
        Page<SysNotificationRule> pageParam = new Page<>(page, size);
        return ruleMapper.selectRulePage(pageParam, name, code, eventType, eventAction, status);
    }

    @Override
    public SysNotificationRuleDTO getRuleById(Long id) {
        return ruleMapper.selectRuleById(id);
    }

    @Override
    public SysNotificationRuleDTO getRuleByCode(String code) {
        return ruleMapper.selectRuleByCode(code);
    }

    @Override
    public List<SysNotificationRuleDTO> getRulesByEvent(String eventType, String eventAction) {
        return ruleMapper.selectRulesByEvent(eventType, eventAction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRule(SysNotificationRuleRequest request, Long operatorId) {
        // 检查规则编码是否已存在
        SysNotificationRuleDTO existRule = ruleMapper.selectRuleByCode(request.getCode());
        if (existRule != null) {
            throw new RuntimeException("规则编码已存在");
        }

        // 检查模板是否存在
        if (templateMapper.selectTemplateById(request.getTemplateId()) == null) {
            throw new RuntimeException("模板不存在");
        }

        SysNotificationRule rule = new SysNotificationRule();
        BeanUtils.copyProperties(request, rule);
        rule.setCreatorId(operatorId);
        rule.setUpdaterId(operatorId);
        
        save(rule);
        return rule.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRule(SysNotificationRuleRequest request, Long operatorId) {
        if (request.getId() == null) {
            throw new RuntimeException("规则ID不能为空");
        }
        
        SysNotificationRule rule = getById(request.getId());
        if (rule == null) {
            throw new RuntimeException("规则不存在");
        }
        
        // 如果修改了编码，需要检查新编码是否已存在
        if (!rule.getCode().equals(request.getCode())) {
            SysNotificationRuleDTO existRule = ruleMapper.selectRuleByCode(request.getCode());
            if (existRule != null) {
                throw new RuntimeException("规则编码已存在");
            }
        }
        
        // 检查模板是否存在
        if (templateMapper.selectTemplateById(request.getTemplateId()) == null) {
            throw new RuntimeException("模板不存在");
        }
        
        BeanUtils.copyProperties(request, rule);
        rule.setUpdaterId(operatorId);
        
        return updateById(rule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRule(Long id, Long operatorId) {
        // 检查是否有用户或部门订阅了该规则
        // 实际项目中应该检查订阅表是否有使用该规则的记录
        // 如果有引用，应该抛出异常或返回false，禁止删除
        
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteRule(List<Long> ids, Long operatorId) {
        // 检查是否有用户或部门订阅了这些规则
        // 实际项目中应该检查订阅表是否有使用这些规则的记录
        // 如果有引用，应该抛出异常或返回false，禁止删除
        
        return removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRuleStatus(Long id, Boolean status, Long operatorId) {
        SysNotificationRule rule = getById(id);
        if (rule == null) {
            throw new RuntimeException("规则不存在");
        }
        
        rule.setStatus(status);
        rule.setUpdaterId(operatorId);
        return updateById(rule);
    }
} 