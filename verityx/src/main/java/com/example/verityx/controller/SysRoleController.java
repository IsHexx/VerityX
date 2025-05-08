package com.example.verityx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysRoleDTO;
import com.example.verityx.entity.SysRole;
import com.example.verityx.service.SysRoleService;
import com.example.verityx.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统角色管理控制器
 */
@Tag(name = "系统角色管理", description = "系统角色的增删改查接口")
@RestController
@RequestMapping("/api/sys/roles")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 分页查询角色列表
     */
    @Operation(summary = "分页查询角色列表", description = "根据条件分页查询角色列表")
    @GetMapping
    public Result<IPage<SysRoleDTO>> getRolePage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "角色名称") @RequestParam(required = false) String roleName,
            @Parameter(description = "状态") @RequestParam(required = false) Boolean status) {
        
        IPage<SysRoleDTO> rolePage = sysRoleService.getRolePage(page, size, roleName, status);
        return Result.success(rolePage);
    }

    /**
     * 获取所有角色列表
     */
    @Operation(summary = "获取所有角色列表", description = "获取所有角色列表，不分页")
    @GetMapping("/all")
    public Result<List<SysRoleDTO>> getAllRoles() {
        List<SysRoleDTO> roles = sysRoleService.getAllRoles();
        return Result.success(roles);
    }

    /**
     * 获取角色详情
     */
    @Operation(summary = "获取角色详情", description = "根据角色ID获取角色详情")
    @GetMapping("/{id}")
    public Result<SysRoleDTO> getRoleById(@Parameter(description = "角色ID") @PathVariable Long id) {
        SysRoleDTO role = sysRoleService.getRoleById(id);
        return Result.success(role);
    }

    /**
     * 创建角色
     */
    @Operation(summary = "创建角色", description = "创建新角色")
    @PostMapping
    public Result<Long> createRole(@Valid @RequestBody SysRole role) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        Long roleId = sysRoleService.createRole(role, operatorId);
        return Result.success(roleId);
    }

    /**
     * 更新角色
     */
    @Operation(summary = "更新角色", description = "更新角色信息")
    @PutMapping
    public Result<Boolean> updateRole(@Valid @RequestBody SysRole role) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysRoleService.updateRole(role, operatorId);
        return Result.success(result);
    }

    /**
     * 删除角色
     */
    @Operation(summary = "删除角色", description = "根据角色ID删除角色")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteRole(@Parameter(description = "角色ID") @PathVariable Long id) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysRoleService.deleteRole(id, operatorId);
        return Result.success(result);
    }

    /**
     * 批量删除角色
     */
    @Operation(summary = "批量删除角色", description = "根据角色ID列表批量删除角色")
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteRole(@RequestBody List<Long> ids) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysRoleService.batchDeleteRole(ids, operatorId);
        return Result.success(result);
    }

    /**
     * 修改角色状态
     */
    @Operation(summary = "修改角色状态", description = "启用或禁用角色")
    @PutMapping("/{id}/status")
    public Result<Boolean> updateRoleStatus(
            @Parameter(description = "角色ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Boolean status) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysRoleService.updateRoleStatus(id, status, operatorId);
        return Result.success(result);
    }

    /**
     * 分配角色权限
     */
    @Operation(summary = "分配角色权限", description = "为角色分配权限")
    @PostMapping("/{id}/permissions")
    public Result<Boolean> assignRolePermissions(
            @Parameter(description = "角色ID") @PathVariable Long id,
            @RequestBody List<Long> permissionIds) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysRoleService.assignRolePermissions(id, permissionIds, operatorId);
        return Result.success(result);
    }

    /**
     * 获取角色权限ID列表
     */
    @Operation(summary = "获取角色权限ID列表", description = "根据角色ID获取该角色的权限ID列表")
    @GetMapping("/{id}/permissions")
    public Result<List<Long>> getRolePermissionIds(@Parameter(description = "角色ID") @PathVariable Long id) {
        List<Long> permissionIds = sysRoleService.getRolePermissionIds(id);
        return Result.success(permissionIds);
    }

    /**
     * 根据用户ID获取角色列表
     */
    @Operation(summary = "根据用户ID获取角色列表", description = "根据用户ID获取该用户的角色列表")
    @GetMapping("/user/{userId}")
    public Result<List<SysRoleDTO>> getRolesByUserId(@Parameter(description = "用户ID") @PathVariable Long userId) {
        List<SysRoleDTO> roles = sysRoleService.getRolesByUserId(userId);
        return Result.success(roles);
    }
}