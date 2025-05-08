-- 通知模板表
CREATE TABLE `sys_notification_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '模板名称',
  `code` varchar(50) NOT NULL COMMENT '模板编码',
  `type` varchar(50) NOT NULL COMMENT '模板类型（EMAIL-邮件, SMS-短信, SYSTEM-系统通知）',
  `title` varchar(200) NOT NULL COMMENT '模板标题',
  `content` text NOT NULL COMMENT '模板内容',
  `params` varchar(1000) DEFAULT NULL COMMENT '模板参数（JSON格式）',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `updater_id` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`,`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知模板表';

-- 通知规则表
CREATE TABLE `sys_notification_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '规则名称',
  `code` varchar(50) NOT NULL COMMENT '规则编码',
  `event_type` varchar(50) NOT NULL COMMENT '事件类型（TEST_PLAN-测试计划, BUG-缺陷, REPORT-报告等）',
  `event_action` varchar(50) NOT NULL COMMENT '事件动作（CREATE-创建, UPDATE-更新, DELETE-删除等）',
  `template_id` bigint(20) NOT NULL COMMENT '模板ID',
  `notify_type` varchar(50) NOT NULL COMMENT '通知方式（EMAIL-邮件, SMS-短信, SYSTEM-系统通知）',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `updater_id` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`,`deleted`),
  KEY `idx_event` (`event_type`,`event_action`,`status`,`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知规则表';

-- 用户订阅表
CREATE TABLE `sys_user_subscription` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `rule_id` bigint(20) NOT NULL COMMENT '规则ID',
  `notify_type` varchar(50) NOT NULL COMMENT '通知方式（EMAIL-邮件, SMS-短信, SYSTEM-系统通知）',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `updater_id` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_rule` (`user_id`,`rule_id`,`deleted`),
  KEY `idx_rule` (`rule_id`,`status`,`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户订阅表';

-- 部门订阅表
CREATE TABLE `sys_dept_subscription` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  `rule_id` bigint(20) NOT NULL COMMENT '规则ID',
  `notify_type` varchar(50) NOT NULL COMMENT '通知方式（EMAIL-邮件, SMS-短信, SYSTEM-系统通知）',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `updater_id` bigint(20) DEFAULT NULL COMMENT '更新人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_dept_rule` (`dept_id`,`rule_id`,`deleted`),
  KEY `idx_rule` (`rule_id`,`status`,`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门订阅表'; 