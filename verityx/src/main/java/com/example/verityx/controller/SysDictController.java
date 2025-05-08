package com.example.verityx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysDictDataDTO;
import com.example.verityx.dto.SysDictTypeDTO;
import com.example.verityx.dto.request.SysDictDataCreateRequest;
import com.example.verityx.dto.request.SysDictDataUpdateRequest;
import com.example.verityx.dto.request.SysDictTypeCreateRequest;
import com.example.verityx.dto.request.SysDictTypeUpdateRequest;
import com.example.verityx.service.SysDictService;
import com.example.verityx.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典管理控制器
 */
@Tag(name = "字典管理", description = "字典类型和字典数据的增删改查接口")
@RestController
@RequestMapping("/api/sys/dict")
public class SysDictController {

    @Autowired
    private SysDictService sysDictService;

    // 字典类型相关API

    /**
     * 分页查询字典类型列表
     */
    @Operation(summary = "分页查询字典类型列表", description = "根据条件分页查询字典类型列表")
    @GetMapping("/types")
    public Result<IPage<SysDictTypeDTO>> getDictTypePage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "字典名称") @RequestParam(required = false) String dictName,
            @Parameter(description = "字典类型") @RequestParam(required = false) String dictType,
            @Parameter(description = "状态") @RequestParam(required = false) Boolean status) {
        
        IPage<SysDictTypeDTO> typePage = sysDictService.getDictTypePage(page, size, dictName, dictType, status);
        return Result.success(typePage);
    }

    /**
     * 获取所有字典类型列表
     */
    @Operation(summary = "获取所有字典类型列表", description = "获取所有可用的字典类型列表")
    @GetMapping("/types/all")
    public Result<List<SysDictTypeDTO>> getAllDictTypes() {
        List<SysDictTypeDTO> typeList = sysDictService.getAllDictTypes();
        return Result.success(typeList);
    }

    /**
     * 获取字典类型详情
     */
    @Operation(summary = "获取字典类型详情", description = "根据字典类型ID获取字典类型详情")
    @GetMapping("/types/{id}")
    public Result<SysDictTypeDTO> getDictTypeById(@Parameter(description = "字典类型ID") @PathVariable Long id) {
        SysDictTypeDTO type = sysDictService.getDictTypeById(id);
        return Result.success(type);
    }

    /**
     * 根据类型获取字典类型
     */
    @Operation(summary = "根据类型获取字典类型", description = "根据字典类型标识获取字典类型详情")
    @GetMapping("/types/type/{dictType}")
    public Result<SysDictTypeDTO> getDictTypeByType(@Parameter(description = "字典类型标识") @PathVariable String dictType) {
        SysDictTypeDTO type = sysDictService.getDictTypeByType(dictType);
        return Result.success(type);
    }

    /**
     * 创建字典类型
     */
    @Operation(summary = "创建字典类型", description = "创建新的字典类型")
    @PostMapping("/types")
    public Result<Long> createDictType(@Valid @RequestBody SysDictTypeCreateRequest request) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        Long typeId = sysDictService.createDictType(request, operatorId);
        return Result.success(typeId);
    }

    /**
     * 更新字典类型
     */
    @Operation(summary = "更新字典类型", description = "更新字典类型信息")
    @PutMapping("/types")
    public Result<Boolean> updateDictType(@Valid @RequestBody SysDictTypeUpdateRequest request) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysDictService.updateDictType(request, operatorId);
        return Result.success(result);
    }

    /**
     * 删除字典类型
     */
    @Operation(summary = "删除字典类型", description = "根据字典类型ID删除字典类型")
    @DeleteMapping("/types/{id}")
    public Result<Boolean> deleteDictType(@Parameter(description = "字典类型ID") @PathVariable Long id) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysDictService.deleteDictType(id, operatorId);
        return Result.success(result);
    }

    /**
     * 批量删除字典类型
     */
    @Operation(summary = "批量删除字典类型", description = "根据字典类型ID列表批量删除字典类型")
    @DeleteMapping("/types/batch")
    public Result<Boolean> batchDeleteDictType(@RequestBody List<Long> ids) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysDictService.batchDeleteDictType(ids, operatorId);
        return Result.success(result);
    }

    // 字典数据相关API

    /**
     * 分页查询字典数据列表
     */
    @Operation(summary = "分页查询字典数据列表", description = "根据条件分页查询字典数据列表")
    @GetMapping("/data")
    public Result<IPage<SysDictDataDTO>> getDictDataPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "字典类型") @RequestParam(required = false) String dictType,
            @Parameter(description = "字典标签") @RequestParam(required = false) String dictLabel,
            @Parameter(description = "状态") @RequestParam(required = false) Boolean status) {
        
        IPage<SysDictDataDTO> dataPage = sysDictService.getDictDataPage(page, size, dictType, dictLabel, status);
        return Result.success(dataPage);
    }

    /**
     * 根据字典类型获取字典数据列表
     */
    @Operation(summary = "根据字典类型获取字典数据列表", description = "根据字典类型获取字典数据列表")
    @GetMapping("/data/type/{dictType}")
    public Result<List<SysDictDataDTO>> getDictDataByType(@Parameter(description = "字典类型") @PathVariable String dictType) {
        List<SysDictDataDTO> dataList = sysDictService.getDictDataByType(dictType);
        return Result.success(dataList);
    }

    /**
     * 获取字典数据详情
     */
    @Operation(summary = "获取字典数据详情", description = "根据字典数据ID获取字典数据详情")
    @GetMapping("/data/{id}")
    public Result<SysDictDataDTO> getDictDataById(@Parameter(description = "字典数据ID") @PathVariable Long id) {
        SysDictDataDTO data = sysDictService.getDictDataById(id);
        return Result.success(data);
    }

    /**
     * 创建字典数据
     */
    @Operation(summary = "创建字典数据", description = "创建新的字典数据")
    @PostMapping("/data")
    public Result<Long> createDictData(@Valid @RequestBody SysDictDataCreateRequest request) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        Long dataId = sysDictService.createDictData(request, operatorId);
        return Result.success(dataId);
    }

    /**
     * 更新字典数据
     */
    @Operation(summary = "更新字典数据", description = "更新字典数据信息")
    @PutMapping("/data")
    public Result<Boolean> updateDictData(@Valid @RequestBody SysDictDataUpdateRequest request) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysDictService.updateDictData(request, operatorId);
        return Result.success(result);
    }

    /**
     * 删除字典数据
     */
    @Operation(summary = "删除字典数据", description = "根据字典数据ID删除字典数据")
    @DeleteMapping("/data/{id}")
    public Result<Boolean> deleteDictData(@Parameter(description = "字典数据ID") @PathVariable Long id) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysDictService.deleteDictData(id, operatorId);
        return Result.success(result);
    }

    /**
     * 批量删除字典数据
     */
    @Operation(summary = "批量删除字典数据", description = "根据字典数据ID列表批量删除字典数据")
    @DeleteMapping("/data/batch")
    public Result<Boolean> batchDeleteDictData(@RequestBody List<Long> ids) {
        // 这里暂时使用1作为操作人ID，实际应该从登录用户中获取
        Long operatorId = 1L;
        boolean result = sysDictService.batchDeleteDictData(ids, operatorId);
        return Result.success(result);
    }
} 