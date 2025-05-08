package com.example.verityx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysUserDTO;
import com.example.verityx.dto.request.SysUserCreateRequest;
import com.example.verityx.dto.request.SysUserPasswordRequest;
import com.example.verityx.dto.request.SysUserUpdateRequest;
import com.example.verityx.entity.SysUser;

import java.util.List;

/**
 * 系统用户服务接口
 */
public interface SysUserService {
    
    /**
     * 分页查询用户列表
     *
     * @param page     页码
     * @param size     每页大小
     * @param username 用户名
     * @param phone    手机号
     * @param deptId   部门ID
     * @param status   状态
     * @return 用户列表分页信息
     */
    IPage<SysUserDTO> getUserPage(int page, int size, String username, String phone, Long deptId, Boolean status);
    
    /**
     * 根据ID查询用户详情
     *
     * @param id 用户ID
     * @return 用户详情
     */
    SysUserDTO getUserById(Long id);
    
    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUser getUserByUsername(String username);
    
    /**
     * 创建用户
     *
     * @param request  创建用户请求
     * @param operator 操作人ID
     * @return 用户ID
     */
    Long createUser(SysUserCreateRequest request, Long operator);
    
    /**
     * 更新用户
     *
     * @param request  更新用户请求
     * @param operator 操作人ID
     * @return 是否成功
     */
    boolean updateUser(SysUserUpdateRequest request, Long operator);
    
    /**
     * 删除用户
     *
     * @param id       用户ID
     * @param operator 操作人ID
     * @return 是否成功
     */
    boolean deleteUser(Long id, Long operator);
    
    /**
     * 批量删除用户
     *
     * @param ids      用户ID列表
     * @param operator 操作人ID
     * @return 是否成功
     */
    boolean batchDeleteUser(List<Long> ids, Long operator);
    
    /**
     * 修改用户状态
     *
     * @param id       用户ID
     * @param status   状态
     * @param operator 操作人ID
     * @return 是否成功
     */
    boolean updateUserStatus(Long id, Boolean status, Long operator);
    
    /**
     * 重置用户密码
     *
     * @param request  重置密码请求
     * @param operator 操作人ID
     * @return 是否成功
     */
    boolean resetPassword(SysUserPasswordRequest request, Long operator);
    
    /**
     * 获取用户权限列表
     *
     * @param userId 用户ID
     * @return 权限编码列表
     */
    List<String> getUserPermissions(Long userId);
    
    /**
     * 根据部门ID查询用户列表
     *
     * @param deptId 部门ID
     * @return 用户列表
     */
    List<SysUserDTO> getUsersByDeptId(Long deptId);
}