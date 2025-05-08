package com.example.verityx.service;

import com.example.verityx.dto.SysDeptDTO;
import com.example.verityx.entity.SysDept;

import java.util.List;

/**
 * 系统部门服务接口
 */
public interface SysDeptService {
    
    /**
     * 查询所有部门列表
     *
     * @return 部门列表
     */
    List<SysDeptDTO> getAllDepts();
    
    /**
     * 根据ID查询部门详情
     *
     * @param id 部门ID
     * @return 部门详情
     */
    SysDeptDTO getDeptById(Long id);
    
    /**
     * 根据部门编码查询部门
     *
     * @param deptCode 部门编码
     * @return 部门信息
     */
    SysDept getDeptByCode(String deptCode);
    
    /**
     * 创建部门
     *
     * @param dept     部门信息
     * @param operator 操作人ID
     * @return 部门ID
     */
    Long createDept(SysDept dept, Long operator);
    
    /**
     * 更新部门
     *
     * @param dept     部门信息
     * @param operator 操作人ID
     * @return 是否成功
     */
    boolean updateDept(SysDept dept, Long operator);
    
    /**
     * 删除部门
     *
     * @param id       部门ID
     * @param operator 操作人ID
     * @return 是否成功
     */
    boolean deleteDept(Long id, Long operator);
    
    /**
     * 修改部门状态
     *
     * @param id       部门ID
     * @param status   状态
     * @param operator 操作人ID
     * @return 是否成功
     */
    boolean updateDeptStatus(Long id, Boolean status, Long operator);
    
    /**
     * 根据父ID查询部门列表
     *
     * @param parentId 父ID
     * @return 部门列表
     */
    List<SysDeptDTO> getDeptsByParentId(Long parentId);
    
    /**
     * 根据用户ID查询部门列表
     *
     * @param userId 用户ID
     * @return 部门列表
     */
    List<SysDeptDTO> getDeptsByUserId(Long userId);
    
    /**
     * 查询部门及其下级部门ID列表
     *
     * @param deptId 部门ID
     * @return 部门ID列表
     */
    List<Long> getChildDeptIds(Long deptId);
    
    /**
     * 查询用户的主部门
     *
     * @param userId 用户ID
     * @return 主部门信息
     */
    SysDeptDTO getPrimaryDeptByUserId(Long userId);
} 