package com.example.verityx.controller;

import com.example.verityx.dto.ApiResponse;
import com.example.verityx.entity.ApiManagement;
import com.example.verityx.entity.TestPlan;
import com.example.verityx.service.ApiManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apis")
public class ApiManagementController {

    @Autowired
    private ApiManagementService apiManagementService;

    // 创建接口
    @PostMapping
    public ApiManagement createApi(@RequestBody ApiManagement api) {
        return apiManagementService.createApi(api);
    }

    // 查询单个接口
    @GetMapping("/{id}")
    public ApiResponse<ApiManagement> getApiById(@PathVariable int id) {
        ApiManagement apis = apiManagementService.getApiById(id);
        if (apis != null) {
            return ApiResponse.success(apis);
        }
        return ApiResponse.error(404, "测试计划未找到");
    }

    // 查询所有接口
    @GetMapping
    public ApiResponse<List<ApiManagement>> getAllApis() {
        List<ApiManagement> apis = apiManagementService.getAllApis();
        return ApiResponse.success(apis);
    }

    // 更新接口
    @PutMapping("/{id}")
    public boolean updateApi(@PathVariable int id, @RequestBody ApiManagement api) {
        api.setApiId(id);
        return apiManagementService.updateApi(api);
    }

    // 删除接口
    @DeleteMapping("/{id}")
    public boolean deleteApi(@PathVariable int id) {
        return apiManagementService.deleteApi(id);
    }
}
