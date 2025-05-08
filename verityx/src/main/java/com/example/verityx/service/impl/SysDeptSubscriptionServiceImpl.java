package com.example.verityx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.verityx.dto.SysDeptSubscriptionDTO;
import com.example.verityx.dto.request.SysDeptSubscriptionRequest;
import com.example.verityx.entity.SysDeptSubscription;
import com.example.verityx.mapper.SysDeptMapper;
import com.example.verityx.mapper.SysDeptSubscriptionMapper;
import com.example.verityx.mapper.SysNotificationRuleMapper;
import com.example.verityx.service.SysDeptSubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 部门订阅服务实现类
 */
@Slf4j
@Service
public class SysDeptSubscriptionServiceImpl extends ServiceImpl<SysDeptSubscriptionMapper, SysDeptSubscription> implements SysDeptSubscriptionService {

    @Autowired
    private SysDeptSubscriptionMapper deptSubscriptionMapper;

    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysNotificationRuleMapper ruleMapper;

    @Override
    public IPage<SysDeptSubscriptionDTO> getDeptSubscriptionPage(int page, int size, Long deptId, Long ruleId, Boolean status) {
        Page<SysDeptSubscription> pageParam = new Page<>(page, size);
        return deptSubscriptionMapper.selectDeptSubscriptionPage(pageParam, deptId, ruleId, status);
    }

    @Override
    public SysDeptSubscriptionDTO getDeptSubscriptionById(Long id) {
        return deptSubscriptionMapper.selectDeptSubscriptionById(id);
    }

    @Override
    public List<SysDeptSubscriptionDTO> getDeptSubscriptionByDeptId(Long deptId) {
        return deptSubscriptionMapper.selectDeptSubscriptionByDeptId(deptId);
    }

    @Override
    public List<SysDeptSubscriptionDTO> getDeptSubscriptionByRuleId(Long ruleId) {
        return deptSubscriptionMapper.selectDeptSubscriptionByRuleId(ruleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDeptSubscription(SysDeptSubscriptionRequest request, Long operatorId) {
        // 检查部门是否存在
        if (deptMapper.selectById(request.getDeptId()) == null) {
            throw new RuntimeException("部门不存在");
        }

        // 检查规则是否存在
        if (ruleMapper.selectRuleById(request.getRuleId()) == null) {
            throw new RuntimeException("规则不存在");
        }

        // 检查是否已存在相同的订阅
        SysDeptSubscriptionDTO existSubscription = deptSubscriptionMapper.selectDeptSubscriptionByDeptIdAndRuleId(
                request.getDeptId(), request.getRuleId());
        if (existSubscription != null) {
            throw new RuntimeException("该部门已订阅此规则");
        }

        SysDeptSubscription deptSubscription = new SysDeptSubscription();
        BeanUtils.copyProperties(request, deptSubscription);
        deptSubscription.setCreatorId(operatorId);
        deptSubscription.setUpdaterId(operatorId);
        
        save(deptSubscription);
        return deptSubscription.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDeptSubscription(SysDeptSubscriptionRequest request, Long operatorId) {
        if (request.getId() == null) {
            throw new RuntimeException("订阅ID不能为空");
        }
        
        SysDeptSubscription deptSubscription = getById(request.getId());
        if (deptSubscription == null) {
            throw new RuntimeException("订阅不存在");
        }
        
        // 如果修改了部门或规则，需要检查是否已存在相同的订阅
        if (request.getDeptId() != null && request.getRuleId() != null 
                && (deptSubscription.getDeptId() != request.getDeptId() 
                || deptSubscription.getRuleId() != request.getRuleId())) {
            
            SysDeptSubscriptionDTO existSubscription = deptSubscriptionMapper.selectDeptSubscriptionByDeptIdAndRuleId(
                    request.getDeptId(), request.getRuleId());
            if (existSubscription != null) {
                throw new RuntimeException("该部门已订阅此规则");
            }
            
            // 检查部门是否存在
            if (deptMapper.selectById(request.getDeptId()) == null) {
                throw new RuntimeException("部门不存在");
            }

            // 检查规则是否存在
            if (ruleMapper.selectRuleById(request.getRuleId()) == null) {
                throw new RuntimeException("规则不存在");
            }
        }
        
        BeanUtils.copyProperties(request, deptSubscription);
        deptSubscription.setUpdaterId(operatorId);
        
        return updateById(deptSubscription);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDeptSubscription(Long id, Long operatorId) {
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteDeptSubscription(List<Long> ids, Long operatorId) {
        return removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDeptSubscriptionStatus(Long id, Boolean status, Long operatorId) {
        SysDeptSubscription deptSubscription = getById(id);
        if (deptSubscription == null) {
            throw new RuntimeException("订阅不存在");
        }
        
        deptSubscription.setStatus(status);
        deptSubscription.setUpdaterId(operatorId);
        return updateById(deptSubscription);
    }
} 