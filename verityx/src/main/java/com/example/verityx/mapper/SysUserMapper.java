package com.example.verityx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.verityx.dto.SysUserDTO;
import com.example.verityx.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统用户Mapper接口
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    
    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUser selectByUsername(@Param("username") String username);
    
    /**
     * 分页查询用户列表
     *
     * @param page     分页参数
     * @param username 用户名
     * @param phone    手机号
     * @param deptId   部门ID
     * @param status   状态
     * @return 用户列表
     */
    IPage<SysUserDTO> selectUserPage(Page<SysUserDTO> page, @Param("username") String username,
                                     @Param("phone") String phone, @Param("deptId") Long deptId,
                                     @Param("status") Boolean status);
    
    /**
     * 根据ID查询用户详情
     *
     * @param userId 用户ID
     * @return 用户详情
     */
    SysUserDTO selectUserById(@Param("userId") Long userId);
    
    /**
     * 根据用户ID查询角色ID列表
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);
    
    /**
     * 根据用户ID查询所有权限
     *
     * @param userId 用户ID
     * @return 权限编码列表
     */
    List<String> selectPermissionCodesByUserId(@Param("userId") Long userId);
    
    /**
     * 根据部门ID查询用户列表
     *
     * @param deptId 部门ID
     * @return 用户列表
     */
    List<SysUserDTO> selectUsersByDeptId(@Param("deptId") Long deptId);
} 