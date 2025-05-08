package com.example.verityx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.verityx.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户角色关联Mapper接口
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    
    /**
     * 根据用户ID删除用户角色关联
     *
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteByUserId(@Param("userId") Long userId);
    
    /**
     * 批量保存用户角色关联
     *
     * @param userId  用户ID
     * @param roleIds 角色ID数组
     * @return 影响行数
     */
    int batchInsert(@Param("userId") Long userId, @Param("roleIds") Long[] roleIds, @Param("createdBy") Long createdBy);
} 