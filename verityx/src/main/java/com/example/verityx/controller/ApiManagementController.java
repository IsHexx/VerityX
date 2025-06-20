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

// 暂时禁用旧的控制器，使用新的NewApiManagementController
// @RestController
// @RequestMapping("/api/apis")
@Tag(name = "接口管理", description = "接口管理相关接口")
public class ApiManagementController {

    @Autowired
    private ApiManagementService apiManagementService;

    // 创建接口
    @Operation(summary = "创建接口", description = "创建新的接口")
    @PostMapping
    public ApiResponse<ApiManagement> createApi(@RequestBody ApiManagement api) {
        System.out.println("====== 创建接口请求 ======");
        System.out.println("接收到的API数据: " + api);
        System.out.println("API目录: " + api.getApiDirectory());
        System.out.println("API名称: " + api.getApiName());
        System.out.println("项目ID: " + api.getProjectId());
        System.out.println("请求方法: " + api.getRequestMethod());
        System.out.println("API路径: " + api.getApiPath());
        System.out.println("状态: " + api.getStatus());

        try {
            ApiManagement createdApi = apiManagementService.createApi(api);
            return ApiResponse.success(createdApi);
        } catch (Exception e) {
            System.err.println("创建接口失败: " + e.getMessage());
            return ApiResponse.error(500, "创建接口失败: " + e.getMessage());
        }
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
        System.out.println("====== 更新接口请求 ======");
        System.out.println("接口ID: " + id);
        System.out.println("更新数据: " + api);

        try {
            api.setApiId(id);
            boolean updated = apiManagementService.updateApi(api);
            if (updated) {
                return ApiResponse.success(true);
            }
            return ApiResponse.error(400, "更新失败");
        } catch (Exception e) {
            System.err.println("更新接口失败: " + e.getMessage());
            return ApiResponse.error(500, "更新接口失败: " + e.getMessage());
        }
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

    // 新增：创建目录
    @Operation(summary = "创建目录", description = "创建新的接口目录")
    @PostMapping("/directories")
    public ApiResponse<ApiManagement> createDirectory(@RequestBody Map<String, String> request) {
        try {
            String directoryName = request.get("directoryName");
            String projectId = request.get("projectId");
            String description = request.get("description");

            if (directoryName == null || directoryName.trim().isEmpty()) {
                return ApiResponse.error(400, "目录名称不能为空");
            }

            ApiManagement directory = apiManagementService.createDirectory(directoryName, projectId, description);
            if (directory != null) {
                return ApiResponse.success(directory);
            } else {
                return ApiResponse.error(500, "目录创建失败");
            }
        } catch (Exception e) {
            return ApiResponse.error(500, "创建目录失败: " + e.getMessage());
        }
    }

    // 新增：删除目录
    @Operation(summary = "删除目录", description = "根据ID删除目录")
    @DeleteMapping("/directories/{directoryId}")
    public ApiResponse<Boolean> deleteDirectory(@PathVariable int directoryId) {
        try {
            boolean deleted = apiManagementService.deleteDirectory(directoryId);
            if (deleted) {
                return ApiResponse.success(true);
            } else {
                return ApiResponse.error(404, "目录不存在");
            }
        } catch (Exception e) {
            return ApiResponse.error(500, "删除目录失败: " + e.getMessage());
        }
    }

    // 新增：获取目录列表
    @Operation(summary = "获取目录列表", description = "获取项目下的所有目录")
    @GetMapping("/directories")
    public ApiResponse<List<ApiManagement>> getDirectories(@RequestParam String projectId) {
        try {
            List<ApiManagement> directories = apiManagementService.getDirectories(projectId);
            return ApiResponse.success(directories);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取目录列表失败: " + e.getMessage());
        }
    }

    // 新增：获取树形结构
    @Operation(summary = "获取树形结构", description = "获取项目的完整树形结构")
    @GetMapping("/tree")
    public ApiResponse<List<ApiManagement>> getTreeStructure(@RequestParam String projectId) {
        try {
            List<ApiManagement> treeData = apiManagementService.getTreeStructure(projectId);
            return ApiResponse.success(treeData);
        } catch (Exception e) {
            return ApiResponse.error(500, "获取树形结构失败: " + e.getMessage());
        }
    }
}
