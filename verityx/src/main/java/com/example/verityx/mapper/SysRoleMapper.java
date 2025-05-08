package com.example.verityx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.verityx.dto.SysRoleDTO;
import com.example.verityx.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统角色Mapper接口
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
    
    /**
     * 分页查询角色列表
     *
     * @param page     分页参数
     * @param roleName 角色名称
     * @param status   状态
     * @return 角色列表
     */
    IPage<SysRoleDTO> selectRolePage(Page<SysRoleDTO> page, @Param("roleName") String roleName,
                                     @Param("status") Boolean status);
    
    /**
     * 查询所有角色列表
     *
     * @return 角色列表
     */
    List<SysRoleDTO> selectAllRoles();
    
    /**
     * 根据ID查询角色详情
     *
     * @param roleId 角色ID
     * @return 角色详情
     */
    SysRoleDTO selectRoleById(@Param("roleId") Long roleId);
    
    /**
     * 根据角色编码查询角色
     *
     * @param roleCode 角色编码
     * @return 角色信息
     */
    SysRole selectByRoleCode(@Param("roleCode") String roleCode);
    
    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRoleDTO> selectRolesByUserId(@Param("userId") Long userId);
    
    /**
     * 根据角色ID查询权限ID列表
     *
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    List<Long> selectPermissionIdsByRoleId(@Param("roleId") Long roleId);
} 