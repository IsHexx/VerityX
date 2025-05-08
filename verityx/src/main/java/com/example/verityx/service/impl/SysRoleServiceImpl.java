package com.example.verityx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.verityx.dto.SysRoleDTO;
import com.example.verityx.entity.SysRole;
import com.example.verityx.entity.SysRolePermission;
import com.example.verityx.mapper.SysRoleMapper;
import com.example.verityx.mapper.SysRolePermissionMapper;
import com.example.verityx.mapper.SysUserRoleMapper;
import com.example.verityx.service.SysRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统角色服务实现类
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    
    private final Logger logger = LoggerFactory.getLogger(SysRoleServiceImpl.class);
    
    @Autowired
    private SysRoleMapper sysRoleMapper;
    
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;
    
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    
    @Override
    public IPage<SysRoleDTO> getRolePage(int page, int size, String roleName, Boolean status) {
        Page<SysRoleDTO> pageParam = new Page<>(page, size);
        return sysRoleMapper.selectRolePage(pageParam, roleName, status);
    }
    
    @Override
    public List<SysRoleDTO> getAllRoles() {
        return sysRoleMapper.selectAllRoles();
    }
    
    @Override
    public SysRoleDTO getRoleById(Long id) {
        return sysRoleMapper.selectRoleById(id);
    }
    
    @Override
    public SysRole getRoleByCode(String roleCode) {
        return sysRoleMapper.selectByRoleCode(roleCode);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRole(SysRole role, Long operator) {
        // 检查角色编码是否已存在
        SysRole existRole = sysRoleMapper.selectByRoleCode(role.getRoleCode());
        if (existRole != null) {
            throw new RuntimeException("角色编码已存在");
        }
        
        // 设置创建信息
        role.setCreatedBy(operator);
        role.setCreatedAt(LocalDateTime.now());
        
        sysRoleMapper.insert(role);
        
        return role.getId();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(SysRole role, Long operator) {
        // 检查角色是否存在
        SysRole existRole = sysRoleMapper.selectById(role.getId());
        if (existRole == null) {
            throw new RuntimeException("角色不存在");
        }
        
        // 如果角色编码变更，需要检查新编码是否已存在
        if (!existRole.getRoleCode().equals(role.getRoleCode())) {
            SysRole codeExistRole = sysRoleMapper.selectByRoleCode(role.getRoleCode());
            if (codeExistRole != null) {
                throw new RuntimeException("角色编码已存在");
            }
        }
        
        // 设置更新信息
        role.setUpdatedBy(operator);
        role.setUpdatedAt(LocalDateTime.now());
        
        int rows = sysRoleMapper.updateById(role);
        
        return rows > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(Long id, Long operator) {
        // 检查角色是否存在
        SysRole role = sysRoleMapper.selectById(id);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }
        
        // 检查是否系统内置角色
        if (role.getIsSystem()) {
            throw new RuntimeException("系统内置角色不允许删除");
        }
        
        // 删除角色权限关联
        sysRolePermissionMapper.deleteByRoleId(id);
        
        // 删除用户角色关联
        // 注意: 这可能导致用户没有任何角色，需要业务上确保合理性
        LambdaQueryWrapper<SysRolePermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRolePermission::getRoleId, id);
        sysRolePermissionMapper.delete(wrapper);
        
        // 删除角色
        int rows = sysRoleMapper.deleteById(id);
        
        return rows > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteRole(List<Long> ids, Long operator) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        // 检查是否包含系统内置角色
        for (Long id : ids) {
            SysRole role = sysRoleMapper.selectById(id);
            if (role != null && role.getIsSystem()) {
                throw new RuntimeException("包含系统内置角色，不允许删除");
            }
        }
        
        // 删除角色权限关联
        for (Long id : ids) {
            sysRolePermissionMapper.deleteByRoleId(id);
        }
        
        // 删除用户角色关联
        // 同样需要注意用户可能没有任何角色的情况
        
        // 批量删除角色
        int rows = sysRoleMapper.deleteBatchIds(ids);
        
        return rows > 0;
    }
    
    @Override
    public boolean updateRoleStatus(Long id, Boolean status, Long operator) {
        // 检查角色是否存在
        SysRole role = sysRoleMapper.selectById(id);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }
        
        // 更新角色状态
        role.setIsActive(status);
        role.setUpdatedBy(operator);
        role.setUpdatedAt(LocalDateTime.now());
        
        int rows = sysRoleMapper.updateById(role);
        
        return rows > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRolePermissions(Long roleId, List<Long> permissionIds, Long operator) {
        // 检查角色是否存在
        SysRole role = sysRoleMapper.selectById(roleId);
        if (role == null) {
            throw new RuntimeException("角色不存在");
        }
        
        // 先删除原有的角色权限关联
        sysRolePermissionMapper.deleteByRoleId(roleId);
        
        // 如果有新的权限，则批量添加
        if (permissionIds != null && !permissionIds.isEmpty()) {
            Long[] permissionIdArray = permissionIds.toArray(new Long[0]);
            sysRolePermissionMapper.batchInsert(roleId, permissionIdArray, operator);
        }
        
        return true;
    }
    
    @Override
    public List<Long> getRolePermissionIds(Long roleId) {
        return sysRoleMapper.selectPermissionIdsByRoleId(roleId);
    }
    
    @Override
    public List<SysRoleDTO> getRolesByUserId(Long userId) {
        return sysRoleMapper.selectRolesByUserId(userId);
    }
} 