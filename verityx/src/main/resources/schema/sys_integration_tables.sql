-- 第三方工具集成表
CREATE TABLE IF NOT EXISTS `sys_tool_integration` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '集成名称',
  `type` varchar(50) NOT NULL COMMENT '集成类型（JIRA, GITHUB, GITLAB, JENKINS等）',
  `url` varchar(255) NOT NULL COMMENT '服务地址',
  `api_key` varchar(255) COMMENT 'API密钥',
  `username` varchar(100) COMMENT '用户名',
  `password` varchar(255) COMMENT '密码（加密存储）',
  `token` varchar(255) COMMENT '访问令牌',
  `config` text COMMENT '附加配置（JSON格式）',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `creator_id` bigint(20) COMMENT '创建人ID',
  `updater_id` bigint(20) COMMENT '更新人ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name_type` (`name`, `type`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第三方工具集成表';

-- API管理表
CREATE TABLE IF NOT EXISTS `sys_api_management` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT 'API名称',
  `description` varchar(255) COMMENT 'API描述',
  `api_key` varchar(255) NOT NULL COMMENT 'API密钥',
  `secret` varchar(255) COMMENT 'API密钥对应的密钥',
  `callback_url` varchar(255) COMMENT '回调URL',
  `permissions` varchar(500) COMMENT '权限范围（逗号分隔）',
  `rate_limit` int(11) DEFAULT 0 COMMENT '速率限制（每小时请求数）',
  `expiration_date` datetime COMMENT '过期日期',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `creator_id` bigint(20) COMMENT '创建人ID',
  `updater_id` bigint(20) COMMENT '更新人ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`, `deleted`),
  UNIQUE KEY `uk_api_key` (`api_key`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='API管理表';

-- 单点登录配置表
CREATE TABLE IF NOT EXISTS `sys_sso_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '配置名称',
  `type` varchar(20) NOT NULL COMMENT '类型：LDAP、OAUTH、SAML',
  `provider` varchar(50) COMMENT '提供商（如Google、Microsoft等）',
  `server_url` varchar(255) COMMENT '服务器URL',
  `domain` varchar(100) COMMENT '域名',
  `client_id` varchar(255) COMMENT '客户端ID',
  `client_secret` varchar(255) COMMENT '客户端密钥',
  `certificate` text COMMENT '证书内容',
  `base_dn` varchar(255) COMMENT 'LDAP的Base DN',
  `username_attribute` varchar(50) DEFAULT 'uid' COMMENT '用户名属性',
  `email_attribute` varchar(50) DEFAULT 'mail' COMMENT '邮箱属性',
  `config` text COMMENT '附加配置（JSON格式）',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `is_default` tinyint(1) DEFAULT 0 COMMENT '是否默认配置：0-否，1-是',
  `creator_id` bigint(20) COMMENT '创建人ID',
  `updater_id` bigint(20) COMMENT '更新人ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='单点登录配置表'; 