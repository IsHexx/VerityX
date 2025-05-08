package com.example.verityx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysNotificationRuleDTO;
import com.example.verityx.dto.request.SysNotificationRuleRequest;

import java.util.List;

/**
 * 通知规则服务接口
 */
public interface SysNotificationRuleService {

    /**
     * 分页查询通知规则列表
     *
     * @param page        页码
     * @param size        每页大小
     * @param name        规则名称
     * @param code        规则编码
     * @param eventType   事件类型
     * @param eventAction 事件动作
     * @param status      状态
     * @return 分页数据
     */
    IPage<SysNotificationRuleDTO> getRulePage(int page, int size, String name, String code, String eventType, String eventAction, Boolean status);

    /**
     * 根据ID查询通知规则详情
     *
     * @param id 规则ID
     * @return 规则详情
     */
    SysNotificationRuleDTO getRuleById(Long id);

    /**
     * 根据编码查询通知规则
     *
     * @param code 规则编码
     * @return 规则详情
     */
    SysNotificationRuleDTO getRuleByCode(String code);

    /**
     * 根据事件类型和事件动作查询规则列表
     *
     * @param eventType   事件类型
     * @param eventAction 事件动作
     * @return 规则列表
     */
    List<SysNotificationRuleDTO> getRulesByEvent(String eventType, String eventAction);

    /**
     * 创建通知规则
     *
     * @param request    规则请求对象
     * @param operatorId 操作人ID
     * @return 新增规则ID
     */
    Long createRule(SysNotificationRuleRequest request, Long operatorId);

    /**
     * 更新通知规则
     *
     * @param request    规则请求对象
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean updateRule(SysNotificationRuleRequest request, Long operatorId);

    /**
     * 删除通知规则
     *
     * @param id         规则ID
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean deleteRule(Long id, Long operatorId);

    /**
     * 批量删除通知规则
     *
     * @param ids        规则ID列表
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean batchDeleteRule(List<Long> ids, Long operatorId);

    /**
     * 修改通知规则状态
     *
     * @param id         规则ID
     * @param status     状态
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean updateRuleStatus(Long id, Boolean status, Long operatorId);
} 