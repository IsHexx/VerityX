package com.example.verityx.controller;

import com.example.verityx.dto.ApiResponse;
import com.example.verityx.entity.ApiManagement;
import com.example.verityx.entity.TestPlan;
import com.example.verityx.service.ApiManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/apis")
@Tag(name = "接口管理", description = "接口管理相关接口")
public class ApiManagementController {

    @Autowired
    private ApiManagementService apiManagementService;

    // 创建接口
    @Operation(summary = "创建接口", description = "创建新的接口")
    @PostMapping
    public ApiManagement createApi(@RequestBody ApiManagement api) {
        return apiManagementService.createApi(api);
    }

    // 查询单个接口
    @Operation(summary = "查询单个接口", description = "根据ID查询接口详情")
    @GetMapping("/{id}")
    public ApiResponse<ApiManagement> getApiById(@PathVariable int id) {
        ApiManagement apis = apiManagementService.getApiById(id);
        if (apis != null) {
            return ApiResponse.success(apis);
        }
        return ApiResponse.error(404, "接口未找到");
    }

    // 查询所有接口
    @Operation(summary = "查询所有接口", description = "查询所有接口")
    @GetMapping
    public ApiResponse<List<ApiManagement>> getAllApis(
            @RequestParam(required = false) String projectId) {
        List<ApiManagement> apis = apiManagementService.getAllApis(projectId);
        return ApiResponse.success(apis);
    }
    
    // 分页获取接口
    @Operation(summary = "分页查询接口", description = "根据分页参数查询接口")
    @GetMapping("/list")
    public ApiResponse<Map<String, Object>> getApisWithPagination(
            @RequestParam int page, 
            @RequestParam int pageSize, 
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String projectId) {

        int offset = (page - 1) * pageSize;
        System.out.println("pageSize是:" + pageSize);
        System.out.println("offset:" + offset);
        System.out.println("projectId:" + projectId);
        
        List<ApiManagement> apis = apiManagementService.getApisWithPagination(pageSize, offset, keyword, projectId);
        int total = apiManagementService.getApiCount(keyword, projectId); // 获取总记录数
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", apis);
        response.put("total", total);
        response.put("page", page);
        response.put("pageSize", pageSize);

        return ApiResponse.success(response);
    }

    // 更新接口
    @Operation(summary = "更新接口", description = "根据ID更新接口")
    @PutMapping("/{id}")
    public ApiResponse<Boolean> updateApi(@PathVariable int id, @RequestBody ApiManagement api) {
        api.setApiId(id);
        boolean updated = apiManagementService.updateApi(api);
        if (updated) {
            return ApiResponse.success(true);
        }
        return ApiResponse.error(400, "更新失败");
    }

    // 删除接口
    @Operation(summary = "删除接口", description = "根据ID删除接口")
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteApi(@PathVariable int id) {
        boolean deleted = apiManagementService.deleteApi(id);
        if (deleted) {
            return ApiResponse.success(true);
        }
        return ApiResponse.error(400, "删除失败");
    }
}
