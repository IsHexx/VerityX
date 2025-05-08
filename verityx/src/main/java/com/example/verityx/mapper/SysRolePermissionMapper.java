package com.example.verityx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.verityx.entity.SysRolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 角色权限关联Mapper接口
 */
@Mapper
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {
    
    /**
     * 根据角色ID删除角色权限关联
     *
     * @param roleId 角色ID
     * @return 影响行数
     */
    int deleteByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 批量保存角色权限关联
     *
     * @param roleId        角色ID
     * @param permissionIds 权限ID数组
     * @return 影响行数
     */
    int batchInsert(@Param("roleId") Long roleId, @Param("permissionIds") Long[] permissionIds, @Param("createdBy") Long createdBy);
} 