package com.example.verityx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库迁移控制器
 * 注意：此控制器仅用于开发环境，生产环境应使用正规的数据库迁移工具
 */
@RestController
@RequestMapping("/api/db-migration")
public class DbMigrationController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 添加project_id列到ui_test_config表
     * @return 执行结果
     */
    @GetMapping("/add-project-id-to-ui-test-config")
    public Map<String, Object> addProjectIdToUiTestConfig() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 检查列是否已存在
            boolean columnExists = checkIfColumnExists("ui_test_config", "project_id");
            
            if (columnExists) {
                result.put("success", true);
                result.put("message", "project_id列已存在，无需添加");
                return result;
            }
            
            // 添加project_id列
            jdbcTemplate.execute("ALTER TABLE ui_test_config ADD COLUMN project_id INT(11) DEFAULT NULL COMMENT '所属项目ID' AFTER is_active");
            
            // 添加索引
            jdbcTemplate.execute("ALTER TABLE ui_test_config ADD INDEX idx_project_id (project_id)");
            
            // 更新现有记录
            jdbcTemplate.execute("UPDATE ui_test_config SET project_id = 1 WHERE project_id IS NULL");
            
            result.put("success", true);
            result.put("message", "成功添加project_id列到ui_test_config表");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "添加project_id列失败: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * 初始化默认UI测试配置
     * @return 执行结果
     */
    @GetMapping("/init-default-ui-test-configs")
    public Map<String, Object> initDefaultUiTestConfigs() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 检查是否已存在默认浏览器配置
            Integer browserCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM ui_test_config WHERE config_type = 'BROWSER' AND is_default = 1", 
                Integer.class);
            
            if (browserCount == null || browserCount == 0) {
                jdbcTemplate.update(
                    "INSERT INTO ui_test_config (config_name, config_type, browser_type, browser_version, device_type, " +
                    "device_name, platform_name, platform_version, is_default, is_active, project_id, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())",
                    "默认Chrome配置", "BROWSER", "CHROME", "latest", "DESKTOP", 
                    "Desktop", "WINDOWS", "10", 1, 1, 1
                );
                result.put("browser_config", "已创建默认浏览器配置");
            } else {
                result.put("browser_config", "默认浏览器配置已存在");
            }
            
            // 检查是否已存在默认等待时间配置
            Integer waitTimeCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM ui_test_config WHERE config_type = 'WAIT_TIME' AND is_default = 1", 
                Integer.class);
            
            if (waitTimeCount == null || waitTimeCount == 0) {
                jdbcTemplate.update(
                    "INSERT INTO ui_test_config (config_name, config_type, default_wait_timeout, page_load_timeout, " +
                    "script_timeout, is_default, is_active, project_id, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())",
                    "默认等待时间配置", "WAIT_TIME", 30, 60, 30, 1, 1, 1
                );
                result.put("wait_time_config", "已创建默认等待时间配置");
            } else {
                result.put("wait_time_config", "默认等待时间配置已存在");
            }
            
            // 检查是否已存在默认截图策略配置
            Integer screenshotCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM ui_test_config WHERE config_type = 'SCREENSHOT' AND is_default = 1", 
                Integer.class);
            
            if (screenshotCount == null || screenshotCount == 0) {
                jdbcTemplate.update(
                    "INSERT INTO ui_test_config (config_name, config_type, screenshot_policy, " +
                    "is_default, is_active, project_id, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())",
                    "默认截图策略配置", "SCREENSHOT", "ON_FAILURE", 1, 1, 1
                );
                result.put("screenshot_config", "已创建默认截图策略配置");
            } else {
                result.put("screenshot_config", "默认截图策略配置已存在");
            }
            
            // 检查是否已存在默认重试配置
            Integer retryCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM ui_test_config WHERE config_type = 'RETRY' AND is_default = 1", 
                Integer.class);
            
            if (retryCount == null || retryCount == 0) {
                jdbcTemplate.update(
                    "INSERT INTO ui_test_config (config_name, config_type, retry_count, retry_interval, " +
                    "is_default, is_active, project_id, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), NOW())",
                    "默认重试配置", "RETRY", 3, 5, 1, 1, 1
                );
                result.put("retry_config", "已创建默认重试配置");
            } else {
                result.put("retry_config", "默认重试配置已存在");
            }
            
            result.put("success", true);
            result.put("message", "成功初始化默认UI测试配置");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "初始化默认UI测试配置失败: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    
    /**
     * 检查列是否存在
     * @param tableName 表名
     * @param columnName 列名
     * @return 是否存在
     */
    private boolean checkIfColumnExists(String tableName, String columnName) {
        try {
            String sql = "SELECT COUNT(*) FROM information_schema.columns " +
                         "WHERE table_name = ? AND column_name = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName, columnName);
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
