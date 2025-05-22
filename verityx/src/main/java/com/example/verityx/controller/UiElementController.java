package com.example.verityx.controller;

import com.example.verityx.dto.PageResult;
import com.example.verityx.dto.UiElementDTO;
import com.example.verityx.dto.UiElementGroupDTO;
import com.example.verityx.service.UiElementGroupService;
import com.example.verityx.service.UiElementService;
import com.example.verityx.util.Result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * UI元素控制器
 */
@Tag(name = "UI元素定位器管理", description = "UI元素定位器相关接口")
@RestController
@RequestMapping("/api/ui-elements")
public class UiElementController {
    
    private static final Logger logger = LoggerFactory.getLogger(UiElementController.class);

    @Autowired
    private UiElementService uiElementService;
    
    @Autowired
    private UiElementGroupService uiElementGroupService;

    @Operation(summary = "获取UI元素列表", description = "分页获取UI元素列表，支持按关键词、分组和定位器类型筛选")
        @GetMapping("/list")    public Result<PageResult<UiElementDTO>> getElementList(            @Parameter(description = "页码") @RequestParam(value = "page", defaultValue = "1") Integer page,            @Parameter(description = "每页大小") @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,            @Parameter(description = "搜索关键词") @RequestParam(value = "keyword", required = false) String keyword,            @Parameter(description = "分组ID") @RequestParam(value = "groupId", required = false) Long groupId,            @Parameter(description = "定位器类型") @RequestParam(value = "locatorType", required = false) String locatorType,            @Parameter(description = "项目ID") @RequestParam(value = "projectId", required = false) Integer projectId) {                logger.info("接收到获取UI元素列表请求, 参数: page={}, pageSize={}, keyword={}, groupId={}, locatorType={}, projectId={}",                      page, pageSize, keyword, groupId, locatorType, projectId);                PageResult<UiElementDTO> result = uiElementService.getElementList(page, pageSize, keyword, groupId, locatorType, projectId);
        
        logger.info("UI元素列表查询结果: 总数={}, 当前页={}, 每页条数={}", 
                    result.getTotal(), result.getPage(), result.getPageSize());
        
        return Result.success(result);
    }
    
    @Operation(summary = "获取UI元素详情", description = "根据ID获取UI元素详情")
        @GetMapping("/{id}")    public Result<UiElementDTO> getElementById(            @PathVariable("id") Long id,            @Parameter(description = "项目ID") @RequestParam(value = "projectId", required = false) Integer projectId) {        logger.info("接收到获取UI元素详情请求, ID: {}, projectId: {}", id, projectId);                UiElementDTO element = uiElementService.getElementById(id, projectId);
        
        logger.info("UI元素详情查询成功, ID: {}, 元素名称: {}", id, element.getElementName());
        
        return Result.success(element);
    }
    
    @Operation(summary = "创建UI元素", description = "创建新的UI元素")
    @PostMapping("/create")
    public Result<Long> createElement(@RequestBody @Valid UiElementDTO request) {
        logger.info("接收到创建UI元素请求, 元素名称: {}, 定位器类型: {}", 
                    request.getElementName(), request.getLocatorType());
        
        Long id = uiElementService.createElement(request);
        
        logger.info("UI元素创建成功, 新生成的ID: {}", id);
        
        return Result.success(id);
    }
    
    @Operation(summary = "更新UI元素", description = "更新UI元素信息")
    @PutMapping("/update/{id}")
    public Result<Void> updateElement(@PathVariable("id") Long id, @RequestBody @Valid UiElementDTO request) {
        logger.info("接收到更新UI元素请求, ID: {}, 元素名称: {}", id, request.getElementName());
        
        uiElementService.updateElement(id, request);
        
        logger.info("UI元素更新成功, ID: {}", id);
        
        return Result.success();
    }
    
    @Operation(summary = "删除UI元素", description = "删除UI元素")
        @DeleteMapping("/delete/{id}")    public Result<Void> deleteElement(            @PathVariable("id") Long id,            @Parameter(description = "项目ID") @RequestParam(value = "projectId", required = false) Integer projectId) {        logger.info("接收到删除UI元素请求, ID: {}, projectId: {}", id, projectId);                uiElementService.deleteElement(id, projectId);
        
        logger.info("UI元素删除成功, ID: {}", id);
        
        return Result.success();
    }
    
    @Operation(summary = "验证元素定位器", description = "验证元素定位器是否有效")
    @PostMapping("/validate")
    public Result<UiElementDTO.ValidateResponse> validateLocator(
            @RequestBody @Valid UiElementDTO.ValidateRequest request) {
        
        logger.info("接收到验证元素定位器请求, 定位器类型: {}, URL: {}", 
                    request.getLocatorType(), request.getUrl());
        
        UiElementDTO.ValidateResponse response = uiElementService.validateLocator(request);
        
        logger.info("元素定位器验证结果: {}, 消息: {}", response.isValid(), response.getMessage());
        
        return Result.success(response);
    }
    
    @Operation(summary = "上传元素截图", description = "上传元素截图")
        @PostMapping("/{id}/screenshot")    public Result<String> uploadElementScreenshot(            @PathVariable("id") Long id,             @RequestParam("file") MultipartFile file,            @Parameter(description = "项目ID") @RequestParam(value = "projectId", required = false) Integer projectId) {                logger.info("接收到上传元素截图请求, 元素ID: {}, 文件名: {}, projectId: {}",                      id, file.getOriginalFilename(), projectId);                String path = uiElementService.uploadElementScreenshot(id, file, projectId);
        
        logger.info("元素截图上传成功, 元素ID: {}, 保存路径: {}", id, path);
        
        return Result.success(path);
    }
    
    @Operation(summary = "获取元素分组列表", description = "获取所有元素分组")
        @GetMapping("/groups")    public Result<List<UiElementGroupDTO>> getElementGroups(            @Parameter(description = "项目ID") @RequestParam(value = "projectId", required = false) Integer projectId) {        logger.info("接收到获取元素分组列表请求, projectId: {}", projectId);                List<UiElementGroupDTO> groups = uiElementGroupService.getElementGroups(projectId);
        
        logger.info("元素分组列表查询成功, 数量: {}", groups.size());
        
        return Result.success(groups);
    }
    
    @Operation(summary = "创建元素分组", description = "创建新的元素分组")
    @PostMapping("/groups/create")
    public Result<Long> createElementGroup(@RequestBody @Valid UiElementGroupDTO request) {
        logger.info("接收到创建元素分组请求, 分组名称: {}", request.getGroupName());
        
        Long id = uiElementGroupService.createElementGroup(request);
        
        logger.info("元素分组创建成功, 新生成的ID: {}", id);
        
        return Result.success(id);
    }
    
    @Operation(summary = "更新元素分组", description = "更新元素分组信息")
    @PutMapping("/groups/update/{id}")
    public Result<Void> updateElementGroup(@PathVariable("id") Long id, @RequestBody @Valid UiElementGroupDTO request) {
        logger.info("接收到更新元素分组请求, ID: {}, 分组名称: {}", id, request.getGroupName());
        
        uiElementGroupService.updateElementGroup(id, request);
        
        logger.info("元素分组更新成功, ID: {}", id);
        
        return Result.success();
    }
    
    @Operation(summary = "删除元素分组", description = "删除元素分组")
    @DeleteMapping("/groups/delete/{id}")
    public Result<Boolean> deleteElementGroup(
            @PathVariable("id") Long id,
            @Parameter(description = "项目ID") @RequestParam(value = "projectId", required = false) Integer projectId) {
        logger.info("接收到删除元素分组请求, ID: {}, projectId: {}", id, projectId);
        
        boolean success = uiElementGroupService.deleteElementGroup(id, projectId);
        
        if (success) {
            logger.info("元素分组删除成功, ID: {}", id);
        } else {
            logger.warn("元素分组删除失败: 分组中还有元素, ID: {}", id);
        }
        
        return Result.success(success);
    }
}