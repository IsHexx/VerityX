package com.example.verityx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.verityx.dto.SysNotificationRuleDTO;
import com.example.verityx.entity.SysNotificationRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 通知规则Mapper接口
 */
@Mapper
public interface SysNotificationRuleMapper extends BaseMapper<SysNotificationRule> {
    
    /**
     * 分页查询通知规则列表
     *
     * @param page        分页参数
     * @param name        规则名称
     * @param code        规则编码
     * @param eventType   事件类型
     * @param eventAction 事件动作
     * @param status      状态
     * @return 规则列表
     */
    IPage<SysNotificationRuleDTO> selectRulePage(
            Page<SysNotificationRule> page,
            @Param("name") String name,
            @Param("code") String code,
            @Param("eventType") String eventType,
            @Param("eventAction") String eventAction,
            @Param("status") Boolean status);
    
    /**
     * 根据ID查询通知规则详情
     *
     * @param id 规则ID
     * @return 规则详情
     */
    SysNotificationRuleDTO selectRuleById(@Param("id") Long id);
    
    /**
     * 根据规则编码查询规则
     *
     * @param code 规则编码
     * @return 规则详情
     */
    SysNotificationRuleDTO selectRuleByCode(@Param("code") String code);
    
    /**
     * 根据事件类型和事件动作查询启用的规则列表
     *
     * @param eventType   事件类型
     * @param eventAction 事件动作
     * @return 规则列表
     */
    List<SysNotificationRuleDTO> selectRulesByEvent(
            @Param("eventType") String eventType,
            @Param("eventAction") String eventAction);
} 