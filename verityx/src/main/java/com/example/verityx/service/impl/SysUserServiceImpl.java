package com.example.verityx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.verityx.dto.SysUserDTO;
import com.example.verityx.dto.request.SysUserCreateRequest;
import com.example.verityx.dto.request.SysUserPasswordRequest;
import com.example.verityx.dto.request.SysUserUpdateRequest;
import com.example.verityx.entity.SysUser;
import com.example.verityx.entity.SysUserRole;
import com.example.verityx.mapper.SysUserMapper;
import com.example.verityx.mapper.SysUserRoleMapper;
import com.example.verityx.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统用户服务实现类
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    
    private final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);
    
    @Autowired
    private SysUserMapper sysUserMapper;
    
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Override
    public IPage<SysUserDTO> getUserPage(int page, int size, String username, String phone, Long deptId, Boolean status) {
        Page<SysUserDTO> pageParam = new Page<>(page, size);
        return sysUserMapper.selectUserPage(pageParam, username, phone, deptId, status);
    }
    
    @Override
    public SysUserDTO getUserById(Long id) {
        return sysUserMapper.selectUserById(id);
    }
    
    @Override
    public SysUser getUserByUsername(String username) {
        return sysUserMapper.selectByUsername(username);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUser(SysUserCreateRequest request, Long operator) {
        // 检查用户名是否已存在
        SysUser existUser = sysUserMapper.selectByUsername(request.getUsername());
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 创建用户
        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRealName(request.getRealName());
        user.setNickname(request.getNickname());
        user.setGender(request.getGender());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAvatar(request.getAvatar());
        user.setDeptId(request.getDeptId());
        user.setIsLocked(false);
        user.setIsActive(true);
        user.setRemark(request.getRemark());
        user.setCreatedBy(operator);
        user.setCreatedAt(LocalDateTime.now());
        
        sysUserMapper.insert(user);
        
        // 分配角色
        if (request.getRoleIds() != null && request.getRoleIds().length > 0) {
            sysUserRoleMapper.batchInsert(user.getId(), request.getRoleIds(), operator);
        }
        
        return user.getId();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(SysUserUpdateRequest request, Long operator) {
        // 检查用户是否存在
        SysUser user = sysUserMapper.selectById(request.getId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 更新用户信息
        user.setRealName(request.getRealName());
        user.setNickname(request.getNickname());
        user.setGender(request.getGender());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAvatar(request.getAvatar());
        user.setDeptId(request.getDeptId());
        user.setRemark(request.getRemark());
        user.setUpdatedBy(operator);
        user.setUpdatedAt(LocalDateTime.now());
        
        int rows = sysUserMapper.updateById(user);
        
        // 更新角色
        if (request.getRoleIds() != null && request.getRoleIds().length > 0) {
            sysUserRoleMapper.deleteByUserId(user.getId());
            sysUserRoleMapper.batchInsert(user.getId(), request.getRoleIds(), operator);
        }
        
        return rows > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long id, Long operator) {
        // 检查用户是否存在
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 删除用户角色关联
        sysUserRoleMapper.deleteByUserId(id);
        
        // 删除用户
        int rows = sysUserMapper.deleteById(id);
        
        return rows > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteUser(List<Long> ids, Long operator) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        // 删除用户角色关联
        for (Long id : ids) {
            sysUserRoleMapper.deleteByUserId(id);
        }
        
        // 批量删除用户
        int rows = sysUserMapper.deleteBatchIds(ids);
        
        return rows > 0;
    }
    
    @Override
    public boolean updateUserStatus(Long id, Boolean status, Long operator) {
        // 检查用户是否存在
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 更新用户状态
        user.setIsActive(status);
        user.setUpdatedBy(operator);
        user.setUpdatedAt(LocalDateTime.now());
        
        int rows = sysUserMapper.updateById(user);
        
        return rows > 0;
    }
    
    @Override
    public boolean resetPassword(SysUserPasswordRequest request, Long operator) {
        // 检查用户是否存在
        SysUser user = sysUserMapper.selectById(request.getUserId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 重置用户密码
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedBy(operator);
        user.setUpdatedAt(LocalDateTime.now());
        
        int rows = sysUserMapper.updateById(user);
        
        return rows > 0;
    }
    
    @Override
    public List<String> getUserPermissions(Long userId) {
        return sysUserMapper.selectPermissionCodesByUserId(userId);
    }
    
    @Override
    public List<SysUserDTO> getUsersByDeptId(Long deptId) {
        return sysUserMapper.selectUsersByDeptId(deptId);
    }
} 