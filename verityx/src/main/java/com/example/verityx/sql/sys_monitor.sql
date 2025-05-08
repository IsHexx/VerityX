-- 系统监控数据表
CREATE TABLE IF NOT EXISTS `sys_monitor_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `monitor_type` varchar(50) NOT NULL COMMENT '监控类型（SERVER-服务器, DATABASE-数据库, TEST_ENGINE-测试引擎）',
  `metric_name` varchar(50) NOT NULL COMMENT '指标名称',
  `metric_value` varchar(255) NOT NULL COMMENT '指标值',
  `status` varchar(20) NOT NULL COMMENT '状态（NORMAL-正常, WARNING-警告, ERROR-错误）',
  `collect_time` datetime NOT NULL COMMENT '采集时间',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_monitor_type` (`monitor_type`),
  INDEX `idx_collect_time` (`collect_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统监控数据表';

-- 系统操作日志表
CREATE TABLE IF NOT EXISTS `sys_operation_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '操作用户ID',
  `username` varchar(50) DEFAULT NULL COMMENT '操作用户名',
  `operation` varchar(100) NOT NULL COMMENT '操作行为',
  `method` varchar(255) DEFAULT NULL COMMENT '请求方法',
  `request_url` varchar(255) DEFAULT NULL COMMENT '请求URL',
  `request_method` varchar(10) DEFAULT NULL COMMENT '请求方式（GET, POST等）',
  `request_params` text DEFAULT NULL COMMENT '请求参数',
  `request_ip` varchar(50) DEFAULT NULL COMMENT '操作IP',
  `request_time` int DEFAULT NULL COMMENT '请求耗时（毫秒）',
  `status` int DEFAULT NULL COMMENT '操作状态（0失败 1成功）',
  `error_msg` text DEFAULT NULL COMMENT '错误消息',
  `operation_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_operation_time` (`operation_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统操作日志表';

-- 系统异常日志表
CREATE TABLE IF NOT EXISTS `sys_error_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '操作用户ID',
  `username` varchar(50) DEFAULT NULL COMMENT '操作用户名',
  `request_url` varchar(255) DEFAULT NULL COMMENT '请求URL',
  `request_method` varchar(10) DEFAULT NULL COMMENT '请求方式（GET, POST等）',
  `request_params` text DEFAULT NULL COMMENT '请求参数',
  `request_ip` varchar(50) DEFAULT NULL COMMENT '操作IP',
  `error_msg` text DEFAULT NULL COMMENT '错误消息',
  `stack_trace` text DEFAULT NULL COMMENT '异常堆栈',
  `error_time` datetime NOT NULL COMMENT '异常时间',
  PRIMARY KEY (`id`),
  INDEX `idx_error_time` (`error_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统异常日志表';

-- 数据库备份记录表
CREATE TABLE IF NOT EXISTS `sys_backup` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `backup_name` varchar(100) NOT NULL COMMENT '备份名称',
  `file_name` varchar(255) NOT NULL COMMENT '备份文件名',
  `file_path` varchar(255) NOT NULL COMMENT '备份文件路径',
  `file_size` bigint(20) NOT NULL COMMENT '备份文件大小（字节）',
  `backup_type` varchar(20) NOT NULL COMMENT '备份类型（AUTO-自动备份, MANUAL-手动备份）',
  `status` varchar(20) NOT NULL COMMENT '备份状态（SUCCESS-成功, FAILED-失败, RESTORING-恢复中）',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据库备份记录表';

-- 系统配置表（已存在，在这里添加系统监控相关的配置）
-- 下面仅作为参考，实际根据现有表结构调整
-- INSERT INTO `sys_config` (`config_name`, `config_key`, `config_value`, `config_type`, `is_system`, `status`, `remark`) VALUES
-- ('备份自动执行', 'system.backup.auto.enabled', 'true', 'boolean', 1, 1, '是否启用自动备份'),
-- ('备份频率', 'system.backup.frequency', 'daily', 'select', 1, 1, '备份频率(daily-每天,weekly-每周,monthly-每月)'),
-- ('备份时间', 'system.backup.time', '02:00', 'time', 1, 1, '备份执行时间'),
-- ('保留备份数量', 'system.backup.keep.count', '7', 'number', 1, 1, '保留的备份文件数量'),
-- ('备份存储位置', 'system.backup.location', '/data/backups', 'text', 1, 1, '备份文件存储路径'),
-- ('自动清理数据', 'system.cleanup.auto.enabled', 'true', 'boolean', 1, 1, '是否启用自动清理'),
-- ('清理频率', 'system.cleanup.frequency', 'monthly', 'select', 1, 1, '清理频率(weekly-每周,monthly-每月,quarterly-每季度)'),
-- ('报告保留天数', 'system.cleanup.report.days', '365', 'number', 1, 1, '测试报告保留天数'),
-- ('日志保留天数', 'system.cleanup.log.days', '180', 'number', 1, 1, '操作日志保留天数'),
-- ('监控数据保留天数', 'system.cleanup.monitor.days', '30', 'number', 1, 1, '监控数据保留天数'),
-- ('临时文件自动清理', 'system.cleanup.temp.enabled', 'true', 'boolean', 1, 1, '是否启用临时文件自动清理'),
-- ('临时文件清理频率', 'system.cleanup.temp.frequency', 'daily', 'select', 1, 1, '临时文件清理频率(daily-每天,weekly-每周,monthly-每月)'),
-- ('报告自动归档', 'system.archive.auto.enabled', 'true', 'boolean', 1, 1, '是否启用报告自动归档'),
-- ('归档阈值天数', 'system.archive.days', '90', 'number', 1, 1, '报告归档阈值天数'),
-- ('归档存储位置', 'system.archive.location', '/data/archives', 'text', 1, 1, '归档文件存储路径'); 