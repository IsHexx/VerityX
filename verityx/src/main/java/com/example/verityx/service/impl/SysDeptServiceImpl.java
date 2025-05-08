package com.example.verityx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.verityx.dto.SysDeptDTO;
import com.example.verityx.entity.SysDept;
import com.example.verityx.mapper.SysDeptMapper;
import com.example.verityx.service.SysDeptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统部门服务实现类
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {
    
    private final Logger logger = LoggerFactory.getLogger(SysDeptServiceImpl.class);
    
    @Autowired
    private SysDeptMapper sysDeptMapper;
    
    @Override
    public List<SysDeptDTO> getAllDepts() {
        return sysDeptMapper.selectAllDepts();
    }
    
    @Override
    public SysDeptDTO getDeptById(Long id) {
        return sysDeptMapper.selectDeptById(id);
    }
    
    @Override
    public SysDept getDeptByCode(String deptCode) {
        return sysDeptMapper.selectByDeptCode(deptCode);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createDept(SysDept dept, Long operator) {
        // 检查部门编码是否已存在
        SysDept existDept = sysDeptMapper.selectByDeptCode(dept.getDeptCode());
        if (existDept != null) {
            throw new RuntimeException("部门编码已存在");
        }
        
        // 如果有父级，检查父级是否存在
        if (dept.getParentId() != null) {
            SysDept parentDept = sysDeptMapper.selectById(dept.getParentId());
            if (parentDept == null) {
                throw new RuntimeException("父级部门不存在");
            }
        }
        
        // 设置创建信息
        dept.setCreatedBy(operator);
        dept.setCreatedAt(LocalDateTime.now());
        
        sysDeptMapper.insert(dept);
        
        return dept.getId();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDept(SysDept dept, Long operator) {
        // 检查部门是否存在
        SysDept existDept = sysDeptMapper.selectById(dept.getId());
        if (existDept == null) {
            throw new RuntimeException("部门不存在");
        }
        
        // 如果部门编码变更，需要检查新编码是否已存在
        if (!existDept.getDeptCode().equals(dept.getDeptCode())) {
            SysDept codeExistDept = sysDeptMapper.selectByDeptCode(dept.getDeptCode());
            if (codeExistDept != null) {
                throw new RuntimeException("部门编码已存在");
            }
        }
        
        // 如果更改了父级ID，检查父级是否存在
        if (dept.getParentId() != null && 
            (existDept.getParentId() == null || !existDept.getParentId().equals(dept.getParentId()))) {
            SysDept parentDept = sysDeptMapper.selectById(dept.getParentId());
            if (parentDept == null) {
                throw new RuntimeException("父级部门不存在");
            }
            
            // 防止将自己设为自己的父级
            if (dept.getId().equals(dept.getParentId())) {
                throw new RuntimeException("不能将自己设为自己的父级");
            }
            
            // 防止将自己的子部门设为自己的父级
            List<Long> childDeptIds = sysDeptMapper.selectChildDeptIds(dept.getId());
            if (childDeptIds != null && childDeptIds.contains(dept.getParentId())) {
                throw new RuntimeException("不能将子部门设为父级部门");
            }
        }
        
        // 设置更新信息
        dept.setUpdatedBy(operator);
        dept.setUpdatedAt(LocalDateTime.now());
        
        int rows = sysDeptMapper.updateById(dept);
        
        return rows > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDept(Long id, Long operator) {
        // 检查部门是否存在
        SysDept dept = sysDeptMapper.selectById(id);
        if (dept == null) {
            throw new RuntimeException("部门不存在");
        }
        
        // 检查是否有子部门
        List<SysDeptDTO> children = sysDeptMapper.selectDeptsByParentId(id);
        if (children != null && !children.isEmpty()) {
            throw new RuntimeException("存在子部门，无法删除");
        }
        
        // 检查部门下是否有用户
        // 这里需要加入检查部门下是否有用户的逻辑
        // 如果有用户，需要提示用户先删除或转移用户
        
        // 删除部门
        int rows = sysDeptMapper.deleteById(id);
        
        return rows > 0;
    }
    
    @Override
    public boolean updateDeptStatus(Long id, Boolean status, Long operator) {
        // 检查部门是否存在
        SysDept dept = sysDeptMapper.selectById(id);
        if (dept == null) {
            throw new RuntimeException("部门不存在");
        }
        
        // 更新部门状态
        dept.setIsActive(status);
        dept.setUpdatedBy(operator);
        dept.setUpdatedAt(LocalDateTime.now());
        
        int rows = sysDeptMapper.updateById(dept);
        
        return rows > 0;
    }
    
    @Override
    public List<SysDeptDTO> getDeptsByParentId(Long parentId) {
        return sysDeptMapper.selectDeptsByParentId(parentId);
    }
    
    @Override
    public List<SysDeptDTO> getDeptsByUserId(Long userId) {
        return sysDeptMapper.selectDeptsByUserId(userId);
    }
    
    @Override
    public List<Long> getChildDeptIds(Long deptId) {
        return sysDeptMapper.selectChildDeptIds(deptId);
    }
    
    @Override
    public SysDeptDTO getPrimaryDeptByUserId(Long userId) {
        return sysDeptMapper.selectPrimaryDeptByUserId(userId);
    }
} 