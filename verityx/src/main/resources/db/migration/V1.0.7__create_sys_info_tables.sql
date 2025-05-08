-- 系统版本信息表
CREATE TABLE IF NOT EXISTS `sys_version_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `version` varchar(50) NOT NULL COMMENT '系统版本号',
  `release_date` datetime NOT NULL COMMENT '发布日期',
  `release_notes` text COMMENT '版本说明',
  `is_current` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为当前版本（1是，0否）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_version` (`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统版本信息表';

-- 系统许可证信息表
CREATE TABLE IF NOT EXISTS `sys_license` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `license_key` varchar(255) NOT NULL COMMENT '许可证密钥',
  `license_type` varchar(50) NOT NULL COMMENT '许可证类型（例如：试用版、标准版、企业版）',
  `issued_to` varchar(100) COMMENT '颁发给（公司或个人名称）',
  `issued_by` varchar(100) COMMENT '颁发者',
  `issue_date` datetime NOT NULL COMMENT '颁发日期',
  `expiration_date` datetime COMMENT '过期日期（NULL表示永久有效）',
  `max_users` int COMMENT '最大用户数',
  `features` varchar(500) COMMENT '授权功能列表（JSON格式）',
  `status` varchar(20) NOT NULL COMMENT '状态（有效、过期、禁用）',
  `remarks` varchar(500) COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统许可证信息表';

-- 系统升级记录表
CREATE TABLE IF NOT EXISTS `sys_upgrade_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `from_version` varchar(50) NOT NULL COMMENT '原版本号',
  `to_version` varchar(50) NOT NULL COMMENT '升级到版本号',
  `upgrade_time` datetime NOT NULL COMMENT '升级时间',
  `upgrade_user` varchar(100) COMMENT '执行升级的用户',
  `status` varchar(20) NOT NULL COMMENT '升级状态（成功、失败、进行中）',
  `error_message` text COMMENT '错误信息',
  `backup_id` bigint COMMENT '关联的备份ID',
  `duration` int COMMENT '升级耗时（秒）',
  `remarks` varchar(500) COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统升级记录表';

-- 系统基本信息表
CREATE TABLE IF NOT EXISTS `sys_about` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `system_name` varchar(100) NOT NULL COMMENT '系统名称',
  `system_logo` varchar(255) COMMENT '系统Logo路径',
  `company_name` varchar(100) COMMENT '公司名称',
  `company_website` varchar(255) COMMENT '公司网站',
  `contact_email` varchar(100) COMMENT '联系邮箱',
  `copyright` varchar(255) COMMENT '版权信息',
  `description` text COMMENT '系统描述',
  `terms_of_service` text COMMENT '服务条款',
  `privacy_policy` text COMMENT '隐私政策',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统基本信息表';

-- 初始化系统版本信息
INSERT INTO `sys_version_info` (`version`, `release_date`, `release_notes`, `is_current`, `create_time`) 
VALUES ('1.0.0', NOW(), '初始版本发布', 1, NOW());

-- 初始化系统基本信息
INSERT INTO `sys_about` (`system_name`, `company_name`, `copyright`, `description`, `create_time`) 
VALUES ('VerityX测试平台', 'VerityX Inc.', 'Copyright © 2025 VerityX Inc. All rights reserved.', 'VerityX是一个完整的自动化测试平台，支持UI测试、API测试和性能测试，提供丰富的测试管理功能。', NOW()); 