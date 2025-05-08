package com.example.verityx.controller;

import com.example.verityx.dto.SysPermissionDTO;
import com.example.verityx.entity.SysPermission;
import com.example.verityx.service.SysPermissionService;
import com.example.verityx.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统权限管理控制器
 */
@Tag(name = "系统权限管理", description = "系统权限的增删改查接口")
@RestController
@RequestMapping("/api/sys/permissions")
public class SysPermissionController {

    @Autowired
    private SysPermissionService sysPermissionService;

    /**
     * 获取所有权限列表
     */
    @Operation(summary = "获取所有权限列表", description = "获取所有权限列表，树形结构")
    @GetMapping
    public Result<List<SysPermissionDTO>> getAllPermissions() {
        List<SysPermissionDTO> permissions = sysPermissionService.getAllPermissions();
        return Result.success(permissions);
    }

    /**
     * 根据权限类型获取权限列表
     */
    @Operation(summary = "根据权限类型获取权限列表", description = "根据权限类型获取权限列表")
    @GetMapping("/type/{permissionType}")
    public Result<List<SysPermissionDTO>> getPermissionsByType(
            @Parameter(description = "权限类型：menu菜单、button按钮、api接口") 
            @PathVariable String permissionType) {
        List<SysPermissionDTO> permissions = sysPermissionService.getPermissionsByType(permissionType);
        return Result.success(permissions);
    }

    /**
     * 获取权限详情
     */
    @Operation(summary = "获取权限详情", description = "根据权限ID获取权限详情")
    @GetMapping("/{id}")
    public Result<SysPermissionDTO> getPermissionById(@Parameter(description = "权限ID") @PathVariable Long id) {
        SysPermissionDTO permission = sysPermissionService.getPermissionById(id);
        return Result.success(permission);
    }

    /**
     * 创建权限
     */
    @Operation(summary = "创建权限", description = "创建新权限")
    @PostMapping
    public Result<Long> createPermission(@Valid @RequestBody SysPermission permission) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        Long permissionId = sysPermissionService.createPermission(permission, operatorId);
        return Result.success(permissionId);
    }

    /**
     * 更新权限
     */
    @Operation(summary = "更新权限", description = "更新权限信息")
    @PutMapping
    public Result<Boolean> updatePermission(@Valid @RequestBody SysPermission permission) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysPermissionService.updatePermission(permission, operatorId);
        return Result.success(result);
    }

    /**
     * 删除权限
     */
    @Operation(summary = "删除权限", description = "根据权限ID删除权限")
    @DeleteMapping("/{id}")
    public Result<Boolean> deletePermission(@Parameter(description = "权限ID") @PathVariable Long id) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysPermissionService.deletePermission(id, operatorId);
        return Result.success(result);
    }

    /**
     * 根据父ID获取权限列表
     */
    @Operation(summary = "根据父ID获取权限列表", description = "根据父ID获取子权限列表")
    @GetMapping("/parent/{parentId}")
    public Result<List<SysPermissionDTO>> getPermissionsByParentId(
            @Parameter(description = "父权限ID") @PathVariable Long parentId) {
        List<SysPermissionDTO> permissions = sysPermissionService.getPermissionsByParentId(parentId);
        return Result.success(permissions);
    }

    /**
     * 根据角色ID获取权限列表
     */
    @Operation(summary = "根据角色ID获取权限列表", description = "根据角色ID获取该角色的权限列表")
    @GetMapping("/role/{roleId}")
    public Result<List<SysPermissionDTO>> getPermissionsByRoleId(
            @Parameter(description = "角色ID") @PathVariable Long roleId) {
        List<SysPermissionDTO> permissions = sysPermissionService.getPermissionsByRoleId(roleId);
        return Result.success(permissions);
    }

    /**
     * 根据用户ID获取权限列表
     */
    @Operation(summary = "根据用户ID获取权限列表", description = "根据用户ID获取该用户的权限列表")
    @GetMapping("/user/{userId}")
    public Result<List<SysPermissionDTO>> getPermissionsByUserId(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        List<SysPermissionDTO> permissions = sysPermissionService.getPermissionsByUserId(userId);
        return Result.success(permissions);
    }

    /**
     * 获取菜单权限列表
     */
    @Operation(summary = "获取菜单权限列表", description = "获取所有菜单权限列表，树形结构")
    @GetMapping("/menus")
    public Result<List<SysPermissionDTO>> getMenuPermissions() {
        List<SysPermissionDTO> menus = sysPermissionService.getMenuPermissions();
        return Result.success(menus);
    }

    /**
     * 根据用户ID获取菜单权限列表
     */
    @Operation(summary = "根据用户ID获取菜单权限列表", description = "根据用户ID获取该用户的菜单权限列表，树形结构")
    @GetMapping("/menus/user/{userId}")
    public Result<List<SysPermissionDTO>> getMenuPermissionsByUserId(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        List<SysPermissionDTO> menus = sysPermissionService.getMenuPermissionsByUserId(userId);
        return Result.success(menus);
    }
} 