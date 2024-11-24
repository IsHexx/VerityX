package com.example.verityx.controller;

import com.example.verityx.entity.ApiAutomation;
import com.example.verityx.service.ApiAutomationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/automation")
@Tag(name = "API自动化场景", description = "API自动化场景管理接口")
public class ApiAutomationController {

    @Autowired
    private ApiAutomationService apiAutomationService;

    @Operation(summary = "创建场景", description = "创建一个新的API自动化场景")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "创建成功"),
            @ApiResponse(responseCode = "400", description = "请求参数错误"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping
    public ApiAutomation createScene(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "场景信息", required = true)
            @RequestBody ApiAutomation scene) {
        return apiAutomationService.createScene(scene);
    }

    @Operation(summary = "查询场景", description = "根据ID查询特定场景")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查询成功"),
            @ApiResponse(responseCode = "404", description = "场景不存在"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping("/{id}")
    public ApiAutomation getSceneById(
            @Parameter(description = "场景ID", required = true, example = "1")
            @PathVariable int id) {
        return apiAutomationService.getSceneById(id);
    }

    @Operation(summary = "查询所有场景", description = "获取所有API自动化场景列表")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查询成功"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @GetMapping
    public List<ApiAutomation> getAllScenes() {
        return apiAutomationService.getAllScenes();
    }

    @Operation(summary = "更新场景", description = "根据ID更新特定场景信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "更新成功"),
            @ApiResponse(responseCode = "400", description = "请求参数错误"),
            @ApiResponse(responseCode = "404", description = "场景不存在"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PutMapping("/{id}")
    public boolean updateScene(
            @Parameter(description = "场景ID", required = true, example = "1")
            @PathVariable int id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "更新的场景信息", required = true)
            @RequestBody ApiAutomation scene) {
        scene.setSceneId(id);
        return apiAutomationService.updateScene(scene);
    }

    @Operation(summary = "删除场景", description = "根据ID删除特定场景")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "删除成功"),
            @ApiResponse(responseCode = "404", description = "场景不存在"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @DeleteMapping("/{id}")
    public boolean deleteScene(
            @Parameter(description = "场景ID", required = true, example = "1")
            @PathVariable int id) {
        return apiAutomationService.deleteScene(id);
    }
}