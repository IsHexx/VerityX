package com.example.verityx.service;

import com.example.verityx.dto.SysPermissionDTO;
import com.example.verityx.entity.SysPermission;

import java.util.List;

/**
 * 系统权限服务接口
 */
public interface SysPermissionService {
    
    /**
     * 查询所有权限列表
     *
     * @return 权限列表
     */
    List<SysPermissionDTO> getAllPermissions();
    
    /**
     * 根据权限类型查询权限列表
     *
     * @param permissionType 权限类型
     * @return 权限列表
     */
    List<SysPermissionDTO> getPermissionsByType(String permissionType);
    
    /**
     * 根据ID查询权限详情
     *
     * @param id 权限ID
     * @return 权限详情
     */
    SysPermissionDTO getPermissionById(Long id);
    
    /**
     * 根据权限编码查询权限
     *
     * @param permissionCode 权限编码
     * @return 权限信息
     */
    SysPermission getPermissionByCode(String permissionCode);
    
    /**
     * 创建权限
     *
     * @param permission 权限信息
     * @param operator   操作人ID
     * @return 权限ID
     */
    Long createPermission(SysPermission permission, Long operator);
    
    /**
     * 更新权限
     *
     * @param permission 权限信息
     * @param operator   操作人ID
     * @return 是否成功
     */
    boolean updatePermission(SysPermission permission, Long operator);
    
    /**
     * 删除权限
     *
     * @param id       权限ID
     * @param operator 操作人ID
     * @return 是否成功
     */
    boolean deletePermission(Long id, Long operator);
    
    /**
     * 根据父ID查询权限列表
     *
     * @param parentId 父ID
     * @return 权限列表
     */
    List<SysPermissionDTO> getPermissionsByParentId(Long parentId);
    
    /**
     * 根据角色ID查询权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<SysPermissionDTO> getPermissionsByRoleId(Long roleId);
    
    /**
     * 根据用户ID查询权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<SysPermissionDTO> getPermissionsByUserId(Long userId);
    
    /**
     * 查询菜单权限列表
     *
     * @return 菜单权限列表
     */
    List<SysPermissionDTO> getMenuPermissions();
    
    /**
     * 根据用户ID查询菜单权限列表
     *
     * @param userId 用户ID
     * @return 菜单权限列表
     */
    List<SysPermissionDTO> getMenuPermissionsByUserId(Long userId);
} 