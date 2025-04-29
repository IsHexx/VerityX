package com.example.verityx.controller;

import com.example.verityx.dto.ApiEnvironmentDTO;
import com.example.verityx.entity.ApiEnvironment;
import com.example.verityx.service.ApiEnvironmentService;
import com.example.verityx.util.JsonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/environments")
@Tag(name = "API环境配置管理", description = "API环境配置的增删改查接口")
public class ApiEnvironmentController {

    @Autowired
    private ApiEnvironmentService apiEnvironmentService;

    /**
     * 分页获取API环境配置列表
     */
    @GetMapping("/list")
    @Operation(summary = "分页查询环境配置", description = "根据关键字和分页参数查询环境配置")
    public JsonResult getEnvironmentsList(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "keyword", required = false) String keyword) {
        
        Map<String, Object> result = apiEnvironmentService.getEnvironmentsByPage(keyword, page, pageSize);
        return JsonResult.success(result);
    }

    /**
     * 获取所有API环境配置（用于下拉选择）
     */
    @GetMapping
    @Operation(summary = "获取所有环境配置", description = "获取所有环境配置，用于下拉选择")
    public JsonResult getAllEnvironments() {
        List<ApiEnvironment> environments = apiEnvironmentService.getAllEnvironments();
        return JsonResult.success(environments);
    }

    /**
     * 获取指定ID的API环境配置
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取单个环境配置", description = "根据ID获取环境配置详情")
    public JsonResult getEnvironmentById(@PathVariable("id") Integer id) {
        ApiEnvironment environment = apiEnvironmentService.getEnvironmentById(id);
        if (environment == null) {
            return JsonResult.error("环境配置不存在");
        }
        return JsonResult.success(environment);
    }

    /**
     * 获取默认API环境配置
     */
    @GetMapping("/default")
    @Operation(summary = "获取默认环境配置", description = "获取当前设置为默认的环境配置")
    public JsonResult getDefaultEnvironment() {
        ApiEnvironment environment = apiEnvironmentService.getDefaultEnvironment();
        if (environment == null) {
            return JsonResult.error("暂无默认环境配置");
        }
        return JsonResult.success(environment);
    }

    /**
     * 创建API环境配置
     */
    @PostMapping
    @Operation(summary = "创建环境配置", description = "创建新的环境配置")
    public JsonResult createEnvironment(@RequestBody ApiEnvironmentDTO environmentDTO) {
        ApiEnvironment environment = new ApiEnvironment();
        // 将DTO转换为实体
        environment.setEnvName(environmentDTO.getEnvName());
        environment.setBaseUrl(environmentDTO.getBaseUrl());
        environment.setDescription(environmentDTO.getDescription());
        environment.setIsDefault(environmentDTO.getIsDefault());
        environment.setGlobalHeaders(environmentDTO.getGlobalHeaders());
        environment.setGlobalParams(environmentDTO.getGlobalParams());
        
        ApiEnvironment createdEnvironment = apiEnvironmentService.createEnvironment(environment);
        return JsonResult.success(createdEnvironment);
    }

    /**
     * 更新API环境配置
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新环境配置", description = "根据ID更新环境配置")
    public JsonResult updateEnvironment(@PathVariable("id") Integer id, @RequestBody ApiEnvironmentDTO environmentDTO) {
        ApiEnvironment existingEnvironment = apiEnvironmentService.getEnvironmentById(id);
        if (existingEnvironment == null) {
            return JsonResult.error("环境配置不存在");
        }

        // 更新属性
        existingEnvironment.setEnvName(environmentDTO.getEnvName());
        existingEnvironment.setBaseUrl(environmentDTO.getBaseUrl());
        existingEnvironment.setDescription(environmentDTO.getDescription());
        existingEnvironment.setIsDefault(environmentDTO.getIsDefault());
        existingEnvironment.setGlobalHeaders(environmentDTO.getGlobalHeaders());
        existingEnvironment.setGlobalParams(environmentDTO.getGlobalParams());

        boolean result = apiEnvironmentService.updateEnvironment(existingEnvironment);
        if (result) {
            return JsonResult.success(existingEnvironment);
        } else {
            return JsonResult.error("更新失败");
        }
    }

    /**
     * 设置默认API环境配置
     */
    @PutMapping("/{id}/default")
    @Operation(summary = "设置默认环境", description = "将指定ID的环境设置为默认环境")
    public JsonResult setDefaultEnvironment(@PathVariable("id") Integer id) {
        boolean result = apiEnvironmentService.setDefaultEnvironment(id);
        if (result) {
            return JsonResult.success("设置默认环境成功");
        } else {
            return JsonResult.error("设置默认环境失败");
        }
    }

    /**
     * 删除API环境配置
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除环境配置", description = "根据ID删除环境配置")
    public JsonResult deleteEnvironment(@PathVariable("id") Integer id) {
        boolean result = apiEnvironmentService.deleteEnvironment(id);
        if (result) {
            return JsonResult.success("删除成功");
        } else {
            return JsonResult.error("删除失败，可能是默认环境无法删除");
        }
    }

    /**
     * 批量删除API环境配置
     */
    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除环境配置", description = "批量删除多个环境配置")
    public JsonResult batchDeleteEnvironments(@RequestBody ApiEnvironmentDTO environmentDTO) {
        boolean result = apiEnvironmentService.batchDeleteEnvironments(environmentDTO.getIds());
        if (result) {
            return JsonResult.success("批量删除成功");
        } else {
            return JsonResult.error("批量删除失败，可能包含默认环境无法删除");
        }
    }
} 