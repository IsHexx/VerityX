package com.example.verityx.controller;

import com.example.verityx.dto.UiTestConfigDTO;
import com.example.verityx.service.UiTestConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UI测试配置Controller
 */
@RestController
@RequestMapping("/api/ui-test-config")
public class UiTestConfigController {

    @Autowired
    private UiTestConfigService uiTestConfigService;

    /**
     * 创建UI测试配置
     * @param configDTO UI测试配置DTO
     * @return 创建结果
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createConfig(@RequestBody UiTestConfigDTO configDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            UiTestConfigDTO createdConfig = uiTestConfigService.createConfig(configDTO);
            response.put("success", true);
            response.put("message", "配置创建成功");
            response.put("data", createdConfig);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "配置创建失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 更新UI测试配置
     * @param id 配置ID
     * @param configDTO UI测试配置DTO
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateConfig(@PathVariable Long id, @RequestBody UiTestConfigDTO configDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            configDTO.setId(id);
            UiTestConfigDTO updatedConfig = uiTestConfigService.updateConfig(configDTO);

            if (updatedConfig == null) {
                response.put("success", false);
                response.put("message", "配置不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response.put("success", true);
            response.put("message", "配置更新成功");
            response.put("data", updatedConfig);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "配置更新失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 获取UI测试配置详情
     * @param id 配置ID
     * @return 配置详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getConfigById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            UiTestConfigDTO config = uiTestConfigService.getConfigById(id);

            if (config == null) {
                response.put("success", false);
                response.put("message", "配置不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response.put("success", true);
            response.put("data", config);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取配置失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 获取UI测试配置列表
     * @param configType 配置类型
     * @param configName 配置名称
     * @param projectId 项目ID
     * @return 配置列表
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getConfigList(
            @RequestParam(required = false) String configType,
            @RequestParam(required = false) String configName,
            @RequestParam(required = false) Integer projectId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<UiTestConfigDTO> configs = uiTestConfigService.getConfigList(configType, configName, projectId);
            response.put("success", true);
            response.put("data", configs);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取配置列表失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 删除UI测试配置
     * @param id 配置ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteConfig(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = uiTestConfigService.deleteConfig(id);

            if (!success) {
                response.put("success", false);
                response.put("message", "配置不存在或删除失败");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response.put("success", true);
            response.put("message", "配置删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "配置删除失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 设置默认配置
     * @param id 配置ID
     * @return 设置结果
     */
    @PutMapping("/{id}/set-default")
    public ResponseEntity<Map<String, Object>> setDefaultConfig(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            UiTestConfigDTO config = uiTestConfigService.setDefaultConfig(id);

            if (config == null) {
                response.put("success", false);
                response.put("message", "配置不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response.put("success", true);
            response.put("message", "设置默认配置成功");
            response.put("data", config);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "设置默认配置失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 获取默认配置
     * @param configType 配置类型
     * @param projectId 项目ID（可选）
     * @return 默认配置信息
     */
    @GetMapping("/default")
    public ResponseEntity<Map<String, Object>> getDefaultConfig(
            @RequestParam(required = true) String configType,
            @RequestParam(required = false) Integer projectId) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (configType == null || configType.isEmpty()) {
                response.put("success", false);
                response.put("message", "配置类型不能为空");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            UiTestConfigDTO config = uiTestConfigService.getDefaultConfig(configType, projectId);

            if (config == null) {
                // 找不到默认配置时，返回成功但数据为null，而不是404错误
                response.put("success", true);
                response.put("message", "尚未设置默认配置，请在配置列表中选择一个配置并设为默认");
                response.put("data", null);
                return ResponseEntity.ok(response);
            }

            // 如果提供了项目ID，检查配置是否属于该项目
            if (projectId != null && config.getProjectId() != null && !config.getProjectId().equals(projectId)) {
                // 如果配置不属于当前项目，尝试查找当前项目的默认配置
                List<UiTestConfigDTO> projectConfigs = uiTestConfigService.getConfigList(configType, null, projectId);
                UiTestConfigDTO projectDefaultConfig = projectConfigs.stream()
                        .filter(c -> Boolean.TRUE.equals(c.getIsDefault()))
                        .findFirst()
                        .orElse(null);

                if (projectDefaultConfig != null) {
                    config = projectDefaultConfig;
                }
            }

            response.put("success", true);
            response.put("data", config);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取默认配置失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 启用/禁用配置
     * @param id 配置ID
     * @param active 是否激活
     * @return 更新结果
     */
    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<Map<String, Object>> toggleConfigStatus(
            @PathVariable Long id,
            @RequestParam boolean active) {
        Map<String, Object> response = new HashMap<>();
        try {
            UiTestConfigDTO config = uiTestConfigService.toggleConfigStatus(id, active);

            if (config == null) {
                response.put("success", false);
                response.put("message", "配置不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response.put("success", true);
            response.put("message", active ? "配置已启用" : "配置已禁用");
            response.put("data", config);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新配置状态失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 获取配置类型统计
     * @return 统计信息
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getConfigTypeStats() {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Integer> stats = uiTestConfigService.getConfigTypeStats();
            response.put("success", true);
            response.put("data", stats);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取统计信息失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}