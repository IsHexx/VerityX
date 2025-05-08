package com.example.verityx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.verityx.dto.SysUserSubscriptionDTO;
import com.example.verityx.entity.SysUserSubscription;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户订阅Mapper接口
 */
@Mapper
public interface SysUserSubscriptionMapper extends BaseMapper<SysUserSubscription> {
    
    /**
     * 分页查询用户订阅列表
     *
     * @param page     分页参数
     * @param userId   用户ID
     * @param ruleId   规则ID
     * @param status   状态
     * @return 用户订阅列表
     */
    IPage<SysUserSubscriptionDTO> selectUserSubscriptionPage(
            Page<SysUserSubscription> page,
            @Param("userId") Long userId,
            @Param("ruleId") Long ruleId,
            @Param("status") Boolean status);
    
    /**
     * 根据ID查询用户订阅详情
     *
     * @param id 订阅ID
     * @return 订阅详情
     */
    SysUserSubscriptionDTO selectUserSubscriptionById(@Param("id") Long id);
    
    /**
     * 根据用户ID查询订阅列表
     *
     * @param userId 用户ID
     * @return 订阅列表
     */
    List<SysUserSubscriptionDTO> selectUserSubscriptionByUserId(@Param("userId") Long userId);
    
    /**
     * 根据规则ID查询订阅的用户列表
     *
     * @param ruleId 规则ID
     * @return 订阅列表
     */
    List<SysUserSubscriptionDTO> selectUserSubscriptionByRuleId(@Param("ruleId") Long ruleId);
    
    /**
     * 根据用户ID和规则ID查询订阅
     *
     * @param userId 用户ID
     * @param ruleId 规则ID
     * @return 订阅详情
     */
    SysUserSubscriptionDTO selectUserSubscriptionByUserIdAndRuleId(
            @Param("userId") Long userId,
            @Param("ruleId") Long ruleId);
}