package com.example.verityx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysDeptSubscriptionDTO;
import com.example.verityx.dto.request.SysDeptSubscriptionRequest;

import java.util.List;

/**
 * 部门订阅服务接口
 */
public interface SysDeptSubscriptionService {

    /**
     * 分页查询部门订阅列表
     *
     * @param page     页码
     * @param size     每页大小
     * @param deptId   部门ID
     * @param ruleId   规则ID
     * @param status   状态
     * @return 分页数据
     */
    IPage<SysDeptSubscriptionDTO> getDeptSubscriptionPage(int page, int size, Long deptId, Long ruleId, Boolean status);

    /**
     * 根据ID查询部门订阅详情
     *
     * @param id 订阅ID
     * @return 订阅详情
     */
    SysDeptSubscriptionDTO getDeptSubscriptionById(Long id);

    /**
     * 根据部门ID查询订阅列表
     *
     * @param deptId 部门ID
     * @return 订阅列表
     */
    List<SysDeptSubscriptionDTO> getDeptSubscriptionByDeptId(Long deptId);

    /**
     * 根据规则ID查询订阅的部门列表
     *
     * @param ruleId 规则ID
     * @return 订阅列表
     */
    List<SysDeptSubscriptionDTO> getDeptSubscriptionByRuleId(Long ruleId);

    /**
     * 创建部门订阅
     *
     * @param request    订阅请求对象
     * @param operatorId 操作人ID
     * @return 新增订阅ID
     */
    Long createDeptSubscription(SysDeptSubscriptionRequest request, Long operatorId);

    /**
     * 更新部门订阅
     *
     * @param request    订阅请求对象
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean updateDeptSubscription(SysDeptSubscriptionRequest request, Long operatorId);

    /**
     * 删除部门订阅
     *
     * @param id         订阅ID
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean deleteDeptSubscription(Long id, Long operatorId);

    /**
     * 批量删除部门订阅
     *
     * @param ids        订阅ID列表
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean batchDeleteDeptSubscription(List<Long> ids, Long operatorId);

    /**
     * 修改部门订阅状态
     *
     * @param id         订阅ID
     * @param status     状态
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean updateDeptSubscriptionStatus(Long id, Boolean status, Long operatorId);
} 