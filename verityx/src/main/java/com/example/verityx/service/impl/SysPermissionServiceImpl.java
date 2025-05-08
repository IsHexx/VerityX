package com.example.verityx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.verityx.dto.SysPermissionDTO;
import com.example.verityx.entity.SysPermission;
import com.example.verityx.mapper.SysPermissionMapper;
import com.example.verityx.mapper.SysRolePermissionMapper;
import com.example.verityx.service.SysPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统权限服务实现类
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {
    
    private final Logger logger = LoggerFactory.getLogger(SysPermissionServiceImpl.class);
    
    @Autowired
    private SysPermissionMapper sysPermissionMapper;
    
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;
    
    @Override
    public List<SysPermissionDTO> getAllPermissions() {
        return sysPermissionMapper.selectAllPermissions();
    }
    
    @Override
    public List<SysPermissionDTO> getPermissionsByType(String permissionType) {
        return sysPermissionMapper.selectPermissionsByType(permissionType);
    }
    
    @Override
    public SysPermissionDTO getPermissionById(Long id) {
        return sysPermissionMapper.selectPermissionById(id);
    }
    
    @Override
    public SysPermission getPermissionByCode(String permissionCode) {
        return sysPermissionMapper.selectByPermissionCode(permissionCode);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createPermission(SysPermission permission, Long operator) {
        // 检查权限编码是否已存在
        SysPermission existPermission = sysPermissionMapper.selectByPermissionCode(permission.getPermissionCode());
        if (existPermission != null) {
            throw new RuntimeException("权限编码已存在");
        }
        
        // 如果有父级，检查父级是否存在
        if (permission.getParentId() != null) {
            SysPermission parentPermission = sysPermissionMapper.selectById(permission.getParentId());
            if (parentPermission == null) {
                throw new RuntimeException("父级权限不存在");
            }
        }
        
        // 设置创建信息
        permission.setCreatedBy(operator);
        permission.setCreatedAt(LocalDateTime.now());
        
        sysPermissionMapper.insert(permission);
        
        return permission.getId();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePermission(SysPermission permission, Long operator) {
        // 检查权限是否存在
        SysPermission existPermission = sysPermissionMapper.selectById(permission.getId());
        if (existPermission == null) {
            throw new RuntimeException("权限不存在");
        }
        
        // 如果权限编码变更，需要检查新编码是否已存在
        if (!existPermission.getPermissionCode().equals(permission.getPermissionCode())) {
            SysPermission codeExistPermission = sysPermissionMapper.selectByPermissionCode(permission.getPermissionCode());
            if (codeExistPermission != null) {
                throw new RuntimeException("权限编码已存在");
            }
        }
        
        // 如果更改了父级ID，检查父级是否存在
        if (permission.getParentId() != null && 
            (existPermission.getParentId() == null || !existPermission.getParentId().equals(permission.getParentId()))) {
            SysPermission parentPermission = sysPermissionMapper.selectById(permission.getParentId());
            if (parentPermission == null) {
                throw new RuntimeException("父级权限不存在");
            }
            
            // 防止将自己设为自己的父级
            if (permission.getId().equals(permission.getParentId())) {
                throw new RuntimeException("不能将自己设为自己的父级");
            }
            
            // 防止循环依赖（比如A->B->C->A）
            // 这里实现比较复杂，可以通过递归方式实现
            // 或者更简单的方法：禁止将子级设为父级
        }
        
        // 设置更新信息
        permission.setUpdatedBy(operator);
        permission.setUpdatedAt(LocalDateTime.now());
        
        int rows = sysPermissionMapper.updateById(permission);
        
        return rows > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePermission(Long id, Long operator) {
        // 检查权限是否存在
        SysPermission permission = sysPermissionMapper.selectById(id);
        if (permission == null) {
            throw new RuntimeException("权限不存在");
        }
        
        // 检查是否系统内置权限
        if (permission.getIsSystem()) {
            throw new RuntimeException("系统内置权限不允许删除");
        }
        
        // 检查是否有子权限
        List<SysPermissionDTO> children = sysPermissionMapper.selectPermissionsByParentId(id);
        if (children != null && !children.isEmpty()) {
            throw new RuntimeException("存在子权限，无法删除");
        }
        
        // 删除角色权限关联
        // 这可能导致角色没有相关权限，需要业务上确保合理性
        sysRolePermissionMapper.delete(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<com.example.verityx.entity.SysRolePermission>()
                .eq(com.example.verityx.entity.SysRolePermission::getPermissionId, id)
        );
        
        // 删除权限
        int rows = sysPermissionMapper.deleteById(id);
        
        return rows > 0;
    }
    
    @Override
    public List<SysPermissionDTO> getPermissionsByParentId(Long parentId) {
        return sysPermissionMapper.selectPermissionsByParentId(parentId);
    }
    
    @Override
    public List<SysPermissionDTO> getPermissionsByRoleId(Long roleId) {
        return sysPermissionMapper.selectPermissionsByRoleId(roleId);
    }
    
    @Override
    public List<SysPermissionDTO> getPermissionsByUserId(Long userId) {
        return sysPermissionMapper.selectPermissionsByUserId(userId);
    }
    
    @Override
    public List<SysPermissionDTO> getMenuPermissions() {
        return sysPermissionMapper.selectMenuPermissions();
    }
    
    @Override
    public List<SysPermissionDTO> getMenuPermissionsByUserId(Long userId) {
        return sysPermissionMapper.selectMenuPermissionsByUserId(userId);
    }
} 