-- 添加默认UI测试配置

-- 检查是否已存在默认浏览器配置
INSERT INTO ui_test_config (
    config_name, config_type, browser_type, browser_version, device_type, 
    device_name, platform_name, platform_version, is_default, is_active, 
    project_id, created_at, updated_at
)
SELECT 
    '默认Chrome配置', 'BROWSER', 'CHROME', 'latest', 'DESKTOP', 
    'Desktop', 'WINDOWS', '10', 1, 1, 
    1, NOW(), NOW()
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM ui_test_config 
    WHERE config_type = 'BROWSER' AND is_default = 1
);

-- 检查是否已存在默认等待时间配置
INSERT INTO ui_test_config (
    config_name, config_type, default_wait_timeout, page_load_timeout, 
    script_timeout, is_default, is_active, project_id, created_at, updated_at
)
SELECT 
    '默认等待时间配置', 'WAIT_TIME', 30, 60, 
    30, 1, 1, 1, NOW(), NOW()
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM ui_test_config 
    WHERE config_type = 'WAIT_TIME' AND is_default = 1
);

-- 检查是否已存在默认截图策略配置
INSERT INTO ui_test_config (
    config_name, config_type, screenshot_policy, 
    is_default, is_active, project_id, created_at, updated_at
)
SELECT 
    '默认截图策略配置', 'SCREENSHOT', 'ON_FAILURE', 
    1, 1, 1, NOW(), NOW()
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM ui_test_config 
    WHERE config_type = 'SCREENSHOT' AND is_default = 1
);

-- 检查是否已存在默认重试配置
INSERT INTO ui_test_config (
    config_name, config_type, retry_count, retry_interval, 
    is_default, is_active, project_id, created_at, updated_at
)
SELECT 
    '默认重试配置', 'RETRY', 3, 5, 
    1, 1, 1, NOW(), NOW()
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM ui_test_config 
    WHERE config_type = 'RETRY' AND is_default = 1
);
