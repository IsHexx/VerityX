package com.example.verityx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.verityx.dto.SysDeptSubscriptionDTO;
import com.example.verityx.entity.SysDeptSubscription;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门订阅Mapper接口
 */
@Mapper
public interface SysDeptSubscriptionMapper extends BaseMapper<SysDeptSubscription> {
    
    /**
     * 分页查询部门订阅列表
     *
     * @param page     分页参数
     * @param deptId   部门ID
     * @param ruleId   规则ID
     * @param status   状态
     * @return 部门订阅列表
     */
    IPage<SysDeptSubscriptionDTO> selectDeptSubscriptionPage(
            Page<SysDeptSubscription> page,
            @Param("deptId") Long deptId,
            @Param("ruleId") Long ruleId,
            @Param("status") Boolean status);
    
    /**
     * 根据ID查询部门订阅详情
     *
     * @param id 订阅ID
     * @return 订阅详情
     */
    SysDeptSubscriptionDTO selectDeptSubscriptionById(@Param("id") Long id);
    
    /**
     * 根据部门ID查询订阅列表
     *
     * @param deptId 部门ID
     * @return 订阅列表
     */
    List<SysDeptSubscriptionDTO> selectDeptSubscriptionByDeptId(@Param("deptId") Long deptId);
    
    /**
     * 根据规则ID查询订阅的部门列表
     *
     * @param ruleId 规则ID
     * @return 订阅列表
     */
    List<SysDeptSubscriptionDTO> selectDeptSubscriptionByRuleId(@Param("ruleId") Long ruleId);
    
    /**
     * 根据部门ID和规则ID查询订阅
     *
     * @param deptId 部门ID
     * @param ruleId 规则ID
     * @return 订阅详情
     */
    SysDeptSubscriptionDTO selectDeptSubscriptionByDeptIdAndRuleId(
            @Param("deptId") Long deptId,
            @Param("ruleId") Long ruleId);
} 