-- 创建API环境配置表
CREATE TABLE `api_environment` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `env_name` varchar(100) NOT NULL COMMENT '环境名称：开发环境、测试环境、生产环境等',
  `base_url` varchar(255) NOT NULL COMMENT '环境基础URL，如：https://dev-api.example.com',
  `description` varchar(500) DEFAULT NULL COMMENT '环境描述',
  `is_default` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否为默认环境：0-否，1-是',
  `global_headers` text DEFAULT NULL COMMENT '全局请求头，JSON格式',
  `global_params` text DEFAULT NULL COMMENT '全局参数，JSON格式',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_env_name` (`env_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='API环境配置表';

-- 添加索引
CREATE INDEX `idx_is_default` ON `api_environment` (`is_default`);

-- 插入一些初始数据
INSERT INTO `api_environment` (
  `env_name`, `base_url`, `description`, `is_default`, 
  `global_headers`, `global_params`, `create_time`, `update_time`
) VALUES (
  '开发环境', 
  'http://localhost:8089', 
  '本地开发测试环境', 
  1, 
  '{\"Content-Type\":\"application/json\"}', 
  '{\"version\":\"v1\",\"debug\":\"true\"}', 
  NOW(), 
  NOW()
);

INSERT INTO `api_environment` (
  `env_name`, `base_url`, `description`, `is_default`, 
  `global_headers`, `global_params`, `create_time`, `update_time`
) VALUES (
  '测试环境', 
  'http://test-api.example.com', 
  '供测试人员使用的环境', 
  0, 
  '{\"Content-Type\":\"application/json\",\"X-API-Key\":\"test-api-key\"}', 
  '{\"version\":\"v1\"}', 
  NOW(), 
  NOW()
);

INSERT INTO `api_environment` (
  `env_name`, `base_url`, `description`, `is_default`, 
  `global_headers`, `global_params`, `create_time`, `update_time`
) VALUES (
  '生产环境', 
  'https://api.example.com', 
  '生产正式环境', 
  0, 
  '{\"Content-Type\":\"application/json\",\"X-API-Key\":\"prod-api-key\"}', 
  '{\"version\":\"v1\"}', 
  NOW(), 
  NOW()
); 