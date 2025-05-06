-- 创建UI测试报告表
CREATE TABLE IF NOT EXISTS `ui_test_report` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '报告ID',
  `execution_id` BIGINT(20) NULL COMMENT '执行ID',
  `report_name` VARCHAR(255) NOT NULL COMMENT '报告名称',
  `report_type` VARCHAR(50) NULL COMMENT '报告类型',
  `environment` VARCHAR(100) NULL COMMENT '执行环境',
  `browser` VARCHAR(100) NULL COMMENT '浏览器',
  `start_time` DATETIME NULL COMMENT '开始时间',
  `end_time` DATETIME NULL COMMENT '结束时间',
  `duration` INT(11) NULL COMMENT '执行时长(毫秒)',
  `total_cases` INT(11) NULL DEFAULT 0 COMMENT '总用例数',
  `passed_cases` INT(11) NULL DEFAULT 0 COMMENT '通过用例数',
  `failed_cases` INT(11) NULL DEFAULT 0 COMMENT '失败用例数',
  `skipped_cases` INT(11) NULL DEFAULT 0 COMMENT '跳过用例数',
  `pass_rate` DOUBLE NULL COMMENT '通过率',
  `executor` VARCHAR(100) NULL COMMENT '执行人',
  `report_content` TEXT NULL COMMENT '报告内容',
  `report_url` VARCHAR(255) NULL COMMENT '报告URL',
  `report_status` VARCHAR(50) NULL COMMENT '报告状态',
  `generated_time` DATETIME NULL COMMENT '生成时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_execution_id` (`execution_id`),
  INDEX `idx_report_name` (`report_name`),
  INDEX `idx_generated_time` (`generated_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='UI测试报告表';

-- 创建UI测试报告详情表
CREATE TABLE IF NOT EXISTS `ui_test_report_detail` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '详情ID',
  `report_id` BIGINT(20) NOT NULL COMMENT '报告ID',
  `execution_detail_id` BIGINT(20) NULL COMMENT '执行详情ID',
  `case_name` VARCHAR(255) NOT NULL COMMENT '用例名称',
  `case_type` VARCHAR(50) NULL COMMENT '用例类型',
  `case_result` VARCHAR(50) NOT NULL COMMENT '执行结果',
  `duration` INT(11) NULL COMMENT '执行时长(毫秒)',
  `total_steps` INT(11) NULL DEFAULT 0 COMMENT '总步骤数',
  `passed_steps` INT(11) NULL DEFAULT 0 COMMENT '通过步骤数',
  `failed_steps` INT(11) NULL DEFAULT 0 COMMENT '失败步骤数',
  `skipped_steps` INT(11) NULL DEFAULT 0 COMMENT '跳过步骤数',
  `error_message` TEXT NULL COMMENT '错误信息',
  `screenshot_urls` TEXT NULL COMMENT '截图URL列表',
  `start_time` DATETIME NULL COMMENT '开始时间',
  `end_time` DATETIME NULL COMMENT '结束时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_report_id` (`report_id`),
  INDEX `idx_execution_detail_id` (`execution_detail_id`),
  INDEX `idx_case_result` (`case_result`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='UI测试报告详情表';

-- 创建UI测试报告步骤表
CREATE TABLE IF NOT EXISTS `ui_test_report_step` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '步骤ID',
  `report_detail_id` BIGINT(20) NOT NULL COMMENT '报告详情ID',
  `step_index` INT(11) NOT NULL COMMENT '步骤序号',
  `step_name` VARCHAR(255) NOT NULL COMMENT '步骤名称',
  `action_type` VARCHAR(50) NULL COMMENT '操作类型',
  `target` VARCHAR(255) NULL COMMENT '操作目标',
  `input_value` TEXT NULL COMMENT '输入值',
  `expected_result` TEXT NULL COMMENT '期望结果',
  `actual_result` TEXT NULL COMMENT '实际结果',
  `status` VARCHAR(50) NOT NULL COMMENT '执行状态',
  `duration` INT(11) NULL COMMENT '执行时长(毫秒)',
  `screenshot_before` VARCHAR(255) NULL COMMENT '执行前截图',
  `screenshot_after` VARCHAR(255) NULL COMMENT '执行后截图',
  `error_message` TEXT NULL COMMENT '错误信息',
  `executed_at` DATETIME NULL COMMENT '执行时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_report_detail_id` (`report_detail_id`),
  INDEX `idx_step_index` (`step_index`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='UI测试报告步骤表';

-- 添加外键约束
ALTER TABLE `ui_test_report_detail` 
ADD CONSTRAINT `fk_report_detail_report_id` FOREIGN KEY (`report_id`) REFERENCES `ui_test_report` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `ui_test_report_step` 
ADD CONSTRAINT `fk_report_step_detail_id` FOREIGN KEY (`report_detail_id`) REFERENCES `ui_test_report_detail` (`id`) ON DELETE CASCADE ON UPDATE CASCADE; 