package com.example.verityx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.verityx.dto.SysDeptDTO;
import com.example.verityx.entity.SysDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统部门Mapper接口
 */
@Mapper
public interface SysDeptMapper extends BaseMapper<SysDept> {
    
    /**
     * 查询所有部门列表
     *
     * @return 部门列表
     */
    List<SysDeptDTO> selectAllDepts();
    
    /**
     * 根据ID查询部门详情
     *
     * @param deptId 部门ID
     * @return 部门详情
     */
    SysDeptDTO selectDeptById(@Param("deptId") Long deptId);
    
    /**
     * 根据部门编码查询部门
     *
     * @param deptCode 部门编码
     * @return 部门信息
     */
    SysDept selectByDeptCode(@Param("deptCode") String deptCode);
    
    /**
     * 根据父ID查询部门列表
     *
     * @param parentId 父ID
     * @return 部门列表
     */
    List<SysDeptDTO> selectDeptsByParentId(@Param("parentId") Long parentId);
    
    /**
     * 根据用户ID查询部门列表
     *
     * @param userId 用户ID
     * @return 部门列表
     */
    List<SysDeptDTO> selectDeptsByUserId(@Param("userId") Long userId);
    
    /**
     * 查询部门及其下级部门ID列表
     *
     * @param deptId 部门ID
     * @return 部门ID列表
     */
    List<Long> selectChildDeptIds(@Param("deptId") Long deptId);
    
    /**
     * 查询用户的主部门
     *
     * @param userId 用户ID
     * @return 主部门信息
     */
    SysDeptDTO selectPrimaryDeptByUserId(@Param("userId") Long userId);
} 