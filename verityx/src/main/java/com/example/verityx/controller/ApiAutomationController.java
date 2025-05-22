package com.example.verityx.controller;

import com.example.verityx.dto.ApiResponse;
import com.example.verityx.entity.ApiAutomation;
import com.example.verityx.service.ApiAutomationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/automation")
@Tag(name = "API自动化场景", description = "API自动化场景管理接口")
public class ApiAutomationController {

    @Autowired
    private ApiAutomationService apiAutomationService;

    @Operation(summary = "创建场景", description = "创建一个新的API自动化场景")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "创建成功"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "请求参数错误"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping
    public ApiResponse<ApiAutomation> createScene(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "场景信息", required = true)
            @RequestBody ApiAutomation scene) {
        ApiAutomation createdScene = apiAutomationService.createScene(scene);
        return ApiResponse.success(createdScene);
    }

    @Operation(summary = "获取场景详情", description = "根据ID获取API自动化场景详情")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "获取成功"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "场景不存在"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/{id}")
    public ApiResponse<ApiAutomation> getSceneById(
            @Parameter(description = "场景ID", required = true)
            @PathVariable("id") int id) {
        ApiAutomation scene = apiAutomationService.getSceneById(id);
        if (scene == null) {
            return ApiResponse.error(404, "场景不存在");
        }
        return ApiResponse.success(scene);
    }

    @Operation(summary = "获取所有场景", description = "获取所有API自动化场景")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "获取成功"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping
    public ApiResponse<List<ApiAutomation>> getAllScenes(
            @RequestParam(value = "projectId", required = false) String projectId) {
        List<ApiAutomation> scenes = apiAutomationService.getAllScenes(projectId);
        return ApiResponse.success(scenes);
    }

    @Operation(summary = "分页查询场景", description = "根据分页参数查询场景")
    @GetMapping("/list")
    public ApiResponse<Map<String, Object>> getScenesWithPagination(
            @RequestParam int page, 
            @RequestParam int pageSize, 
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String projectId) {

        int offset = (page - 1) * pageSize;
        System.out.println("pageSize是:" + pageSize);
        System.out.println("offset:" + offset);
        System.out.println("projectId:" + projectId);
        
        // 暂时返回空结果，因为服务层方法不匹配
        // List<ApiAutomation> scenes = apiAutomationService.getScenesWithPagination(pageSize, offset, keyword, projectId);
        // int total = apiAutomationService.getSceneCount(keyword, projectId); // 获取总记录数
        
        // 返回空结果
        List<ApiAutomation> scenes = new ArrayList<>();
        int total = 0;
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", scenes);
        response.put("total", total);
        response.put("page", page);
        response.put("pageSize", pageSize);

        return ApiResponse.success(response);
    }

    @Operation(summary = "更新场景", description = "更新API自动化场景")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "更新成功"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "场景不存在"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PutMapping("/{id}")
    public ApiResponse<Void> updateScene(
            @Parameter(description = "场景ID", required = true)
            @PathVariable("id") int id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "场景信息", required = true)
            @RequestBody ApiAutomation scene) {
        // 确保ID一致
        scene.setSceneId(id);
        boolean result = apiAutomationService.updateScene(scene);
        if (!result) {
            return ApiResponse.error(404, "场景不存在或更新失败");
        }
        return ApiResponse.success(null);
    }

    @Operation(summary = "删除场景", description = "删除API自动化场景")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "删除成功"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "场景不存在"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteScene(
            @Parameter(description = "场景ID", required = true)
            @PathVariable("id") int id) {
        boolean result = apiAutomationService.deleteScene(id);
        if (!result) {
            return ApiResponse.error(404, "场景不存在或删除失败");
        }
        return ApiResponse.success(null);
    }
}