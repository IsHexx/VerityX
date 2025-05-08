package com.example.verityx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysUserSubscriptionDTO;
import com.example.verityx.dto.request.SysUserSubscriptionRequest;

import java.util.List;

/**
 * 用户订阅服务接口
 */
public interface SysUserSubscriptionService {

    /**
     * 分页查询用户订阅列表
     *
     * @param page     页码
     * @param size     每页大小
     * @param userId   用户ID
     * @param ruleId   规则ID
     * @param status   状态
     * @return 分页数据
     */
    IPage<SysUserSubscriptionDTO> getUserSubscriptionPage(int page, int size, Long userId, Long ruleId, Boolean status);

    /**
     * 根据ID查询用户订阅详情
     *
     * @param id 订阅ID
     * @return 订阅详情
     */
    SysUserSubscriptionDTO getUserSubscriptionById(Long id);

    /**
     * 根据用户ID查询订阅列表
     *
     * @param userId 用户ID
     * @return 订阅列表
     */
    List<SysUserSubscriptionDTO> getUserSubscriptionByUserId(Long userId);

    /**
     * 根据规则ID查询订阅的用户列表
     *
     * @param ruleId 规则ID
     * @return 订阅列表
     */
    List<SysUserSubscriptionDTO> getUserSubscriptionByRuleId(Long ruleId);

    /**
     * 创建用户订阅
     *
     * @param request    订阅请求对象
     * @param operatorId 操作人ID
     * @return 新增订阅ID
     */
    Long createUserSubscription(SysUserSubscriptionRequest request, Long operatorId);

    /**
     * 更新用户订阅
     *
     * @param request    订阅请求对象
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean updateUserSubscription(SysUserSubscriptionRequest request, Long operatorId);

    /**
     * 删除用户订阅
     *
     * @param id         订阅ID
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean deleteUserSubscription(Long id, Long operatorId);

    /**
     * 批量删除用户订阅
     *
     * @param ids        订阅ID列表
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean batchDeleteUserSubscription(List<Long> ids, Long operatorId);

    /**
     * 修改用户订阅状态
     *
     * @param id         订阅ID
     * @param status     状态
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean updateUserSubscriptionStatus(Long id, Boolean status, Long operatorId);
} 