package com.example.verityx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.verityx.dto.SysUserSubscriptionDTO;
import com.example.verityx.dto.request.SysUserSubscriptionRequest;
import com.example.verityx.entity.SysUserSubscription;
import com.example.verityx.mapper.SysNotificationRuleMapper;
import com.example.verityx.mapper.SysUserMapper;
import com.example.verityx.mapper.SysUserSubscriptionMapper;
import com.example.verityx.service.SysUserSubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户订阅服务实现类
 */
@Slf4j
@Service
public class SysUserSubscriptionServiceImpl extends ServiceImpl<SysUserSubscriptionMapper, SysUserSubscription> implements SysUserSubscriptionService {

    @Autowired
    private SysUserSubscriptionMapper userSubscriptionMapper;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysNotificationRuleMapper ruleMapper;

    @Override
    public IPage<SysUserSubscriptionDTO> getUserSubscriptionPage(int page, int size, Long userId, Long ruleId, Boolean status) {
        Page<SysUserSubscription> pageParam = new Page<>(page, size);
        return userSubscriptionMapper.selectUserSubscriptionPage(pageParam, userId, ruleId, status);
    }

    @Override
    public SysUserSubscriptionDTO getUserSubscriptionById(Long id) {
        return userSubscriptionMapper.selectUserSubscriptionById(id);
    }

    @Override
    public List<SysUserSubscriptionDTO> getUserSubscriptionByUserId(Long userId) {
        return userSubscriptionMapper.selectUserSubscriptionByUserId(userId);
    }

    @Override
    public List<SysUserSubscriptionDTO> getUserSubscriptionByRuleId(Long ruleId) {
        return userSubscriptionMapper.selectUserSubscriptionByRuleId(ruleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUserSubscription(SysUserSubscriptionRequest request, Long operatorId) {
        // 检查用户是否存在
        if (userMapper.selectById(request.getUserId()) == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查规则是否存在
        if (ruleMapper.selectRuleById(request.getRuleId()) == null) {
            throw new RuntimeException("规则不存在");
        }

        // 检查是否已存在相同的订阅
        SysUserSubscriptionDTO existSubscription = userSubscriptionMapper.selectUserSubscriptionByUserIdAndRuleId(
                request.getUserId(), request.getRuleId());
        if (existSubscription != null) {
            throw new RuntimeException("该用户已订阅此规则");
        }

        SysUserSubscription userSubscription = new SysUserSubscription();
        BeanUtils.copyProperties(request, userSubscription);
        userSubscription.setCreatorId(operatorId);
        userSubscription.setUpdaterId(operatorId);
        
        save(userSubscription);
        return userSubscription.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserSubscription(SysUserSubscriptionRequest request, Long operatorId) {
        if (request.getId() == null) {
            throw new RuntimeException("订阅ID不能为空");
        }
        
        SysUserSubscription userSubscription = getById(request.getId());
        if (userSubscription == null) {
            throw new RuntimeException("订阅不存在");
        }
        
        // 如果修改了用户或规则，需要检查是否已存在相同的订阅
        if (request.getUserId() != null && request.getRuleId() != null 
                && (userSubscription.getUserId() != request.getUserId() 
                || userSubscription.getRuleId() != request.getRuleId())) {
            
            SysUserSubscriptionDTO existSubscription = userSubscriptionMapper.selectUserSubscriptionByUserIdAndRuleId(
                    request.getUserId(), request.getRuleId());
            if (existSubscription != null) {
                throw new RuntimeException("该用户已订阅此规则");
            }
            
            // 检查用户是否存在
            if (userMapper.selectById(request.getUserId()) == null) {
                throw new RuntimeException("用户不存在");
            }

            // 检查规则是否存在
            if (ruleMapper.selectRuleById(request.getRuleId()) == null) {
                throw new RuntimeException("规则不存在");
            }
        }
        
        BeanUtils.copyProperties(request, userSubscription);
        userSubscription.setUpdaterId(operatorId);
        
        return updateById(userSubscription);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserSubscription(Long id, Long operatorId) {
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteUserSubscription(List<Long> ids, Long operatorId) {
        return removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserSubscriptionStatus(Long id, Boolean status, Long operatorId) {
        SysUserSubscription userSubscription = getById(id);
        if (userSubscription == null) {
            throw new RuntimeException("订阅不存在");
        }
        
        userSubscription.setStatus(status);
        userSubscription.setUpdaterId(operatorId);
        return updateById(userSubscription);
    }
} 