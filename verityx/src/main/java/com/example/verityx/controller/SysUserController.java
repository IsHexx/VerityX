package com.example.verityx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysUserDTO;
import com.example.verityx.dto.request.SysUserCreateRequest;
import com.example.verityx.dto.request.SysUserPasswordRequest;
import com.example.verityx.dto.request.SysUserUpdateRequest;
import com.example.verityx.service.SysUserService;
import com.example.verityx.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统用户管理控制器
 */
@Tag(name = "系统用户管理", description = "系统用户的增删改查接口")
@RestController
@RequestMapping("/api/sys/users")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 分页查询用户列表
     */
    @Operation(summary = "分页查询用户列表", description = "根据条件分页查询用户列表")
    @GetMapping
    public Result<IPage<SysUserDTO>> getUserPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "手机号") @RequestParam(required = false) String phone,
            @Parameter(description = "部门ID") @RequestParam(required = false) Long deptId,
            @Parameter(description = "状态") @RequestParam(required = false) Boolean status) {
        
        IPage<SysUserDTO> userPage = sysUserService.getUserPage(page, size, username, phone, deptId, status);
        return Result.success(userPage);
    }

    /**
     * 获取用户详情
     */
    @Operation(summary = "获取用户详情", description = "根据用户ID获取用户详情")
    @GetMapping("/{id}")
    public Result<SysUserDTO> getUserById(@Parameter(description = "用户ID") @PathVariable Long id) {
        SysUserDTO user = sysUserService.getUserById(id);
        return Result.success(user);
    }

    /**
     * 创建用户
     */
    @Operation(summary = "创建用户", description = "创建新用户")
    @PostMapping
    public Result<Long> createUser(@Valid @RequestBody SysUserCreateRequest request) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        Long userId = sysUserService.createUser(request, operatorId);
        return Result.success(userId);
    }

    /**
     * 更新用户
     */
    @Operation(summary = "更新用户", description = "更新用户信息")
    @PutMapping
    public Result<Boolean> updateUser(@Valid @RequestBody SysUserUpdateRequest request) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysUserService.updateUser(request, operatorId);
        return Result.success(result);
    }

    /**
     * 删除用户
     */
    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteUser(@Parameter(description = "用户ID") @PathVariable Long id) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysUserService.deleteUser(id, operatorId);
        return Result.success(result);
    }

    /**
     * 批量删除用户
     */
    @Operation(summary = "批量删除用户", description = "根据用户ID列表批量删除用户")
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteUser(@RequestBody List<Long> ids) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysUserService.batchDeleteUser(ids, operatorId);
        return Result.success(result);
    }

    /**
     * 修改用户状态
     */
    @Operation(summary = "修改用户状态", description = "启用或禁用用户")
    @PutMapping("/{id}/status")
    public Result<Boolean> updateUserStatus(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Boolean status) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysUserService.updateUserStatus(id, status, operatorId);
        return Result.success(result);
    }

    /**
     * 重置用户密码
     */
    @Operation(summary = "重置用户密码", description = "重置用户密码")
    @PutMapping("/reset-password")
    public Result<Boolean> resetPassword(@Valid @RequestBody SysUserPasswordRequest request) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysUserService.resetPassword(request, operatorId);
        return Result.success(result);
    }

    /**
     * 获取用户权限列表
     */
    @Operation(summary = "获取用户权限列表", description = "根据用户ID获取用户权限编码列表")
    @GetMapping("/{id}/permissions")
    public Result<List<String>> getUserPermissions(@Parameter(description = "用户ID") @PathVariable Long id) {
        List<String> permissions = sysUserService.getUserPermissions(id);
        return Result.success(permissions);
    }

    /**
     * 根据部门ID获取用户列表
     */
    @Operation(summary = "根据部门ID获取用户列表", description = "根据部门ID获取该部门下的所有用户")
    @GetMapping("/dept/{deptId}")
    public Result<List<SysUserDTO>> getUsersByDeptId(@Parameter(description = "部门ID") @PathVariable Long deptId) {
        List<SysUserDTO> users = sysUserService.getUsersByDeptId(deptId);
        return Result.success(users);
    }
} 