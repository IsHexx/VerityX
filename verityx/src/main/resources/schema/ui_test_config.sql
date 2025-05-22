-- 创建UI测试配置表
CREATE TABLE IF NOT EXISTS `ui_test_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_name` varchar(100) NOT NULL COMMENT '配置名称',
  `config_type` varchar(20) NOT NULL COMMENT '配置类型: BROWSER/WAIT_TIME/SCREENSHOT/RETRY',
  `browser_type` varchar(20) DEFAULT NULL COMMENT '浏览器类型: CHROME/FIREFOX/EDGE/SAFARI',
  `browser_version` varchar(20) DEFAULT NULL COMMENT '浏览器版本',
  `device_type` varchar(20) DEFAULT NULL COMMENT '设备类型: DESKTOP/MOBILE/TABLET',
  `device_name` varchar(50) DEFAULT NULL COMMENT '设备名称',
  `platform_name` varchar(20) DEFAULT NULL COMMENT '平台名称: WINDOWS/MAC/LINUX/ANDROID/IOS',
  `platform_version` varchar(20) DEFAULT NULL COMMENT '平台版本',
  `default_wait_timeout` int(11) DEFAULT NULL COMMENT '默认等待超时时间（秒）',
  `page_load_timeout` int(11) DEFAULT NULL COMMENT '页面加载超时时间（秒）',
  `script_timeout` int(11) DEFAULT NULL COMMENT '脚本执行超时时间（秒）',
  `screenshot_policy` varchar(20) DEFAULT NULL COMMENT '截图策略: ALWAYS/ON_FAILURE/NEVER',
  `retry_count` int(11) DEFAULT NULL COMMENT '失败重试次数',
  `retry_interval` int(11) DEFAULT NULL COMMENT '重试间隔（秒）',
  `headless` tinyint(1) DEFAULT 0 COMMENT '是否使用无头模式',
  `proxy_settings` text DEFAULT NULL COMMENT '代理设置，JSON格式',
  `capabilities` text DEFAULT NULL COMMENT '自定义功能，JSON格式',
  `is_default` tinyint(1) DEFAULT 0 COMMENT '是否为默认配置',
  `is_active` tinyint(1) DEFAULT 1 COMMENT '是否激活',
  `project_id` int(11) DEFAULT NULL COMMENT '所属项目ID',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_config_type` (`config_type`),
  KEY `idx_is_default` (`is_default`),
  KEY `idx_is_active` (`is_active`),
  KEY `idx_project_id` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='UI测试配置表';

-- 创建默认配置数据
INSERT INTO `ui_test_config` (
  `config_name`, `config_type`, `browser_type`, `browser_version`, `device_type`,
  `platform_name`, `default_wait_timeout`, `page_load_timeout`, `script_timeout`,
  `is_default`, `is_active`, `created_at`, `updated_at`
) VALUES
-- 默认浏览器配置
(
  '默认Chrome配置', 'BROWSER', 'CHROME', 'latest', 'DESKTOP',
  'WINDOWS', 10, 30, 30,
  1, 1, NOW(), NOW()
),
-- 默认等待时间配置
(
  '默认等待时间配置', 'WAIT_TIME', NULL, NULL, NULL,
  NULL, 10, 30, 30,
  1, 1, NOW(), NOW()
),
-- 默认截图策略配置
(
  '默认截图策略配置', 'SCREENSHOT', NULL, NULL, NULL,
  NULL, NULL, NULL, NULL,
  1, 1, NOW(), NOW()
),
-- 默认重试配置
(
  '默认重试配置', 'RETRY', NULL, NULL, NULL,
  NULL, NULL, NULL, NULL,
  1, 1, NOW(), NOW()
);

-- 更新默认截图策略
UPDATE `ui_test_config`
SET `screenshot_policy` = 'ON_FAILURE'
WHERE `config_type` = 'SCREENSHOT' AND `is_default` = 1;

-- 更新默认重试配置
UPDATE `ui_test_config`
SET `retry_count` = 2, `retry_interval` = 5
WHERE `config_type` = 'RETRY' AND `is_default` = 1;