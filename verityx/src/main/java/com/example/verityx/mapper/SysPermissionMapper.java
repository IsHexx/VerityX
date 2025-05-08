package com.example.verityx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.verityx.dto.SysPermissionDTO;
import com.example.verityx.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统权限Mapper接口
 */
@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
    
    /**
     * 查询所有权限列表
     *
     * @return 权限列表
     */
    List<SysPermissionDTO> selectAllPermissions();
    
    /**
     * 根据权限类型查询权限列表
     *
     * @param permissionType 权限类型
     * @return 权限列表
     */
    List<SysPermissionDTO> selectPermissionsByType(@Param("permissionType") String permissionType);
    
    /**
     * 根据ID查询权限详情
     *
     * @param permissionId 权限ID
     * @return 权限详情
     */
    SysPermissionDTO selectPermissionById(@Param("permissionId") Long permissionId);
    
    /**
     * 根据权限编码查询权限
     *
     * @param permissionCode 权限编码
     * @return 权限信息
     */
    SysPermission selectByPermissionCode(@Param("permissionCode") String permissionCode);
    
    /**
     * 根据父ID查询权限列表
     *
     * @param parentId 父ID
     * @return 权限列表
     */
    List<SysPermissionDTO> selectPermissionsByParentId(@Param("parentId") Long parentId);
    
    /**
     * 根据角色ID查询权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<SysPermissionDTO> selectPermissionsByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 根据用户ID查询权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<SysPermissionDTO> selectPermissionsByUserId(@Param("userId") Long userId);
    
    /**
     * 查询菜单权限列表
     *
     * @return 菜单权限列表
     */
    List<SysPermissionDTO> selectMenuPermissions();
    
    /**
     * 根据用户ID查询菜单权限列表
     *
     * @param userId 用户ID
     * @return 菜单权限列表
     */
    List<SysPermissionDTO> selectMenuPermissionsByUserId(@Param("userId") Long userId);
} 