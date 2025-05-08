package com.example.verityx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysRoleDTO;
import com.example.verityx.entity.SysRole;

import java.util.List;

/**
 * 系统角色服务接口
 */
public interface SysRoleService {
    
    /**
     * 分页查询角色列表
     *
     * @param page     页码
     * @param size     每页大小
     * @param roleName 角色名称
     * @param status   状态
     * @return 角色列表分页信息
     */
    IPage<SysRoleDTO> getRolePage(int page, int size, String roleName, Boolean status);
    
    /**
     * 查询所有角色列表
     *
     * @return 角色列表
     */
    List<SysRoleDTO> getAllRoles();
    
    /**
     * 根据ID查询角色详情
     *
     * @param id 角色ID
     * @return 角色详情
     */
    SysRoleDTO getRoleById(Long id);
    
    /**
     * 根据角色编码查询角色
     *
     * @param roleCode 角色编码
     * @return 角色信息
     */
    SysRole getRoleByCode(String roleCode);
    
    /**
     * 创建角色
     *
     * @param role     角色信息
     * @param operator 操作人ID
     * @return 角色ID
     */
    Long createRole(SysRole role, Long operator);
    
    /**
     * 更新角色
     *
     * @param role     角色信息
     * @param operator 操作人ID
     * @return 是否成功
     */
    boolean updateRole(SysRole role, Long operator);
    
    /**
     * 删除角色
     *
     * @param id       角色ID
     * @param operator 操作人ID
     * @return 是否成功
     */
    boolean deleteRole(Long id, Long operator);
    
    /**
     * 批量删除角色
     *
     * @param ids      角色ID列表
     * @param operator 操作人ID
     * @return 是否成功
     */
    boolean batchDeleteRole(List<Long> ids, Long operator);
    
    /**
     * 修改角色状态
     *
     * @param id       角色ID
     * @param status   状态
     * @param operator 操作人ID
     * @return 是否成功
     */
    boolean updateRoleStatus(Long id, Boolean status, Long operator);
    
    /**
     * 分配角色权限
     *
     * @param roleId        角色ID
     * @param permissionIds 权限ID列表
     * @param operator      操作人ID
     * @return 是否成功
     */
    boolean assignRolePermissions(Long roleId, List<Long> permissionIds, Long operator);
    
    /**
     * 获取角色的权限ID列表
     *
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    List<Long> getRolePermissionIds(Long roleId);
    
    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRoleDTO> getRolesByUserId(Long userId);
}