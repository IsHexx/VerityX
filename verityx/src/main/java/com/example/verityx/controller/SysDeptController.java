package com.example.verityx.controller;

import com.example.verityx.dto.SysDeptDTO;
import com.example.verityx.entity.SysDept;
import com.example.verityx.service.SysDeptService;
import com.example.verityx.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统部门管理控制器
 */
@Tag(name = "系统部门管理", description = "系统部门的增删改查接口")
@RestController
@RequestMapping("/api/sys/depts")
public class SysDeptController {

    @Autowired
    private SysDeptService sysDeptService;

    /**
     * 获取所有部门列表
     */
    @Operation(summary = "获取所有部门列表", description = "获取所有部门列表，树形结构")
    @GetMapping
    public Result<List<SysDeptDTO>> getAllDepts() {
        List<SysDeptDTO> depts = sysDeptService.getAllDepts();
        return Result.success(depts);
    }

    /**
     * 获取部门详情
     */
    @Operation(summary = "获取部门详情", description = "根据部门ID获取部门详情")
    @GetMapping("/{id}")
    public Result<SysDeptDTO> getDeptById(@Parameter(description = "部门ID") @PathVariable Long id) {
        SysDeptDTO dept = sysDeptService.getDeptById(id);
        return Result.success(dept);
    }

    /**
     * 创建部门
     */
    @Operation(summary = "创建部门", description = "创建新部门")
    @PostMapping
    public Result<Long> createDept(@Valid @RequestBody SysDept dept) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        Long deptId = sysDeptService.createDept(dept, operatorId);
        return Result.success(deptId);
    }

    /**
     * 更新部门
     */
    @Operation(summary = "更新部门", description = "更新部门信息")
    @PutMapping
    public Result<Boolean> updateDept(@Valid @RequestBody SysDept dept) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysDeptService.updateDept(dept, operatorId);
        return Result.success(result);
    }

    /**
     * 删除部门
     */
    @Operation(summary = "删除部门", description = "根据部门ID删除部门")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteDept(@Parameter(description = "部门ID") @PathVariable Long id) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysDeptService.deleteDept(id, operatorId);
        return Result.success(result);
    }

    /**
     * 修改部门状态
     */
    @Operation(summary = "修改部门状态", description = "启用或禁用部门")
    @PutMapping("/{id}/status")
    public Result<Boolean> updateDeptStatus(
            @Parameter(description = "部门ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestParam Boolean status) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysDeptService.updateDeptStatus(id, status, operatorId);
        return Result.success(result);
    }

    /**
     * 根据父ID获取部门列表
     */
    @Operation(summary = "根据父ID获取部门列表", description = "根据父ID获取子部门列表")
    @GetMapping("/parent/{parentId}")
    public Result<List<SysDeptDTO>> getDeptsByParentId(
            @Parameter(description = "父部门ID") @PathVariable Long parentId) {
        List<SysDeptDTO> depts = sysDeptService.getDeptsByParentId(parentId);
        return Result.success(depts);
    }

    /**
     * 根据用户ID获取部门列表
     */
    @Operation(summary = "根据用户ID获取部门列表", description = "根据用户ID获取该用户的部门列表")
    @GetMapping("/user/{userId}")
    public Result<List<SysDeptDTO>> getDeptsByUserId(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        List<SysDeptDTO> depts = sysDeptService.getDeptsByUserId(userId);
        return Result.success(depts);
    }

    /**
     * 查询部门及其下级部门ID列表
     */
    @Operation(summary = "查询部门及其下级部门ID列表", description = "根据部门ID查询该部门及其所有下级部门的ID列表")
    @GetMapping("/{id}/children/ids")
    public Result<List<Long>> getChildDeptIds(
            @Parameter(description = "部门ID") @PathVariable Long id) {
        List<Long> deptIds = sysDeptService.getChildDeptIds(id);
        return Result.success(deptIds);
    }

    /**
     * 查询用户的主部门
     */
    @Operation(summary = "查询用户的主部门", description = "根据用户ID查询该用户的主部门信息")
    @GetMapping("/user/{userId}/primary")
    public Result<SysDeptDTO> getPrimaryDeptByUserId(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        SysDeptDTO dept = sysDeptService.getPrimaryDeptByUserId(userId);
        return Result.success(dept);
    }
} 