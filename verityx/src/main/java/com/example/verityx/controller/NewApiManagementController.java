package com.example.verityx.controller;

import com.example.verityx.entity.ApiModule;
import com.example.verityx.entity.ApiEndpoint;
import com.example.verityx.entity.ApiTestCase;
import com.example.verityx.service.NewApiManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新的API管理控制器
 */
@RestController
@RequestMapping("/api/v2")
@CrossOrigin(origins = "http://localhost:8080")
public class NewApiManagementController {

    @Autowired
    private NewApiManagementService newApiManagementService;

    // ==================== 模块管理 ====================

    /**
     * 创建模块
     */
    @PostMapping("/modules")
    public Map<String, Object> createModule(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            ApiModule module = new ApiModule();
            module.setName((String) request.get("name"));
            module.setDescription((String) request.get("description"));
            module.setProjectId(Integer.valueOf(request.get("projectId").toString()));

            boolean success = newApiManagementService.createModule(module);
            if (success) {
                response.put("code", 200);
                response.put("message", "模块创建成功");
                response.put("data", module);
            } else {
                response.put("code", 500);
                response.put("message", "模块创建失败");
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "模块创建失败：" + e.getMessage());
        }
        return response;
    }

    /**
     * 获取项目下的所有模块
     */
    @GetMapping("/modules")
    public Map<String, Object> getModules(@RequestParam String projectId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ApiModule> modules = newApiManagementService.getModulesByProjectId(projectId);
            response.put("code", 200);
            response.put("message", "获取成功");
            response.put("data", modules);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "获取失败：" + e.getMessage());
        }
        return response;
    }

    /**
     * 更新模块
     */
    @PutMapping("/modules/{id}")
    public Map<String, Object> updateModule(@PathVariable int id, @RequestBody ApiModule module) {
        Map<String, Object> response = new HashMap<>();
        try {
            module.setId(id);
            boolean success = newApiManagementService.updateModule(module);
            if (success) {
                response.put("code", 200);
                response.put("message", "模块更新成功");
            } else {
                response.put("code", 500);
                response.put("message", "模块更新失败");
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "模块更新失败：" + e.getMessage());
        }
        return response;
    }

    /**
     * 删除模块
     */
    @DeleteMapping("/modules/{id}")
    public Map<String, Object> deleteModule(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = newApiManagementService.deleteModule(id);
            if (success) {
                response.put("code", 200);
                response.put("message", "模块删除成功");
            } else {
                response.put("code", 500);
                response.put("message", "模块删除失败");
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "模块删除失败：" + e.getMessage());
        }
        return response;
    }

    // ==================== 接口管理 ====================

    /**
     * 创建接口
     */
    @PostMapping("/endpoints")
    public Map<String, Object> createEndpoint(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            ApiEndpoint endpoint = new ApiEndpoint();
            endpoint.setModuleId(Integer.valueOf(request.get("moduleId").toString()));
            endpoint.setName((String) request.get("name"));
            endpoint.setPath((String) request.get("path"));
            endpoint.setMethod((String) request.get("method"));
            endpoint.setDescription((String) request.get("description"));

            boolean success = newApiManagementService.createEndpoint(endpoint);
            if (success) {
                response.put("code", 200);
                response.put("message", "接口创建成功");
                response.put("data", endpoint);
            } else {
                response.put("code", 500);
                response.put("message", "接口创建失败");
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "接口创建失败：" + e.getMessage());
        }
        return response;
    }

    /**
     * 获取模块下的所有接口
     */
    @GetMapping("/modules/{moduleId}/endpoints")
    public Map<String, Object> getEndpointsByModule(@PathVariable int moduleId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ApiEndpoint> endpoints = newApiManagementService.getEndpointsByModuleId(moduleId);
            response.put("code", 200);
            response.put("message", "获取成功");
            response.put("data", endpoints);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "获取失败：" + e.getMessage());
        }
        return response;
    }

    /**
     * 获取单个接口详情
     */
    @GetMapping("/endpoints/{id}")
    public Map<String, Object> getEndpointById(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            ApiEndpoint endpoint = newApiManagementService.getEndpointById(id);
            if (endpoint != null) {
                response.put("code", 200);
                response.put("message", "获取成功");
                response.put("data", endpoint);
            } else {
                response.put("code", 404);
                response.put("message", "接口不存在");
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "获取失败：" + e.getMessage());
        }
        return response;
    }

    /**
     * 更新接口
     */
    @PutMapping("/endpoints/{id}")
    public Map<String, Object> updateEndpoint(@PathVariable int id, @RequestBody ApiEndpoint endpoint) {
        Map<String, Object> response = new HashMap<>();
        try {
            endpoint.setId(id);
            boolean success = newApiManagementService.updateEndpoint(endpoint);
            if (success) {
                response.put("code", 200);
                response.put("message", "接口更新成功");
            } else {
                response.put("code", 500);
                response.put("message", "接口更新失败");
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "接口更新失败：" + e.getMessage());
        }
        return response;
    }

    /**
     * 删除接口
     */
    @DeleteMapping("/endpoints/{id}")
    public Map<String, Object> deleteEndpoint(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = newApiManagementService.deleteEndpoint(id);
            if (success) {
                response.put("code", 200);
                response.put("message", "接口删除成功");
            } else {
                response.put("code", 500);
                response.put("message", "接口删除失败");
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "接口删除失败：" + e.getMessage());
        }
        return response;
    }

    // ==================== 树形结构 ====================

    /**
     * 获取项目的完整树形结构
     */
    @GetMapping("/tree")
    public Map<String, Object> getTreeStructure(@RequestParam String projectId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Map<String, Object>> treeData = newApiManagementService.getTreeStructure(projectId);
            response.put("code", 200);
            response.put("message", "获取成功");
            response.put("data", treeData);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "获取失败：" + e.getMessage());
        }
        return response;
    }
}
