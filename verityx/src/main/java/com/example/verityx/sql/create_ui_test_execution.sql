-- UI测试执行记录表
CREATE TABLE IF NOT EXISTS `ui_test_execution` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '执行ID',
  `execution_name` VARCHAR(100) NOT NULL COMMENT '执行名称',
  `execution_type` VARCHAR(20) NOT NULL COMMENT '执行类型：单用例/测试套件',
  `execution_source_id` BIGINT NOT NULL COMMENT '执行来源ID（用例ID或套件ID）',
  `execution_source_name` VARCHAR(100) NOT NULL COMMENT '执行来源名称（用例名称或套件名称）',
  `status` VARCHAR(20) NOT NULL COMMENT '执行状态：waiting/running/completed/failed/aborted',
  `executor` VARCHAR(50) NOT NULL COMMENT '执行人',
  `environment` VARCHAR(50) NOT NULL COMMENT '执行环境（浏览器类型）',
  `start_time` DATETIME COMMENT '开始时间',
  `end_time` DATETIME COMMENT '结束时间',
  `duration` INT COMMENT '执行时长（秒）',
  `success_count` INT DEFAULT 0 COMMENT '成功用例数',
  `fail_count` INT DEFAULT 0 COMMENT '失败用例数',
  `total_count` INT DEFAULT 0 COMMENT '总用例数',
  `execution_config` TEXT COMMENT '执行配置（JSON格式）',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_status` (`status`),
  INDEX `idx_execution_type` (`execution_type`),
  INDEX `idx_execution_source_id` (`execution_source_id`),
  INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='UI测试执行记录表';

-- UI测试执行详情表
CREATE TABLE IF NOT EXISTS `ui_test_execution_detail` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '详情ID',
  `execution_id` BIGINT NOT NULL COMMENT '执行ID',
  `case_id` BIGINT NOT NULL COMMENT '用例ID',
  `case_name` VARCHAR(100) NOT NULL COMMENT '用例名称',
  `status` VARCHAR(20) NOT NULL COMMENT '状态：waiting/running/passed/failed/skipped',
  `start_time` DATETIME COMMENT '开始时间',
  `end_time` DATETIME COMMENT '结束时间',
  `duration` INT COMMENT '执行时长（秒）',
  `error_message` TEXT COMMENT '错误信息',
  `screenshot_path` VARCHAR(255) COMMENT '失败截图路径',
  `retry_count` INT DEFAULT 0 COMMENT '重试次数',
  `step_count` INT DEFAULT 0 COMMENT '步骤总数',
  `step_passed` INT DEFAULT 0 COMMENT '通过步骤数',
  `step_failed` INT DEFAULT 0 COMMENT '失败步骤数',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_execution_id` (`execution_id`),
  INDEX `idx_case_id` (`case_id`),
  INDEX `idx_status` (`status`),
  CONSTRAINT `fk_execution_detail_execution_id` FOREIGN KEY (`execution_id`) REFERENCES `ui_test_execution` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='UI测试执行详情表';

-- UI测试执行日志表
CREATE TABLE IF NOT EXISTS `ui_test_execution_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `execution_id` BIGINT NOT NULL COMMENT '执行ID',
  `detail_id` BIGINT COMMENT '详情ID',
  `log_level` VARCHAR(10) NOT NULL COMMENT '日志级别：info/warning/error/debug',
  `log_content` TEXT NOT NULL COMMENT '日志内容',
  `step_index` INT COMMENT '步骤索引',
  `step_name` VARCHAR(100) COMMENT '步骤名称',
  `timestamp` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '日志时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_execution_id` (`execution_id`),
  INDEX `idx_detail_id` (`detail_id`),
  INDEX `idx_log_level` (`log_level`),
  INDEX `idx_timestamp` (`timestamp`),
  CONSTRAINT `fk_execution_log_execution_id` FOREIGN KEY (`execution_id`) REFERENCES `ui_test_execution` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_execution_log_detail_id` FOREIGN KEY (`detail_id`) REFERENCES `ui_test_execution_detail` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='UI测试执行日志表';

-- UI测试执行队列表
CREATE TABLE IF NOT EXISTS `ui_test_execution_queue` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '队列ID',
  `execution_id` BIGINT NOT NULL COMMENT '执行ID',
  `priority` INT DEFAULT 0 COMMENT '优先级',
  `status` VARCHAR(20) NOT NULL COMMENT '状态：waiting/processing/completed/failed',
  `queue_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '进入队列时间',
  `start_time` DATETIME COMMENT '开始处理时间',
  `end_time` DATETIME COMMENT '处理完成时间',
  `worker_id` VARCHAR(50) COMMENT '执行工作节点标识',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idx_execution_id` (`execution_id`),
  INDEX `idx_status` (`status`),
  INDEX `idx_priority` (`priority`),
  INDEX `idx_queue_time` (`queue_time`),
  CONSTRAINT `fk_execution_queue_execution_id` FOREIGN KEY (`execution_id`) REFERENCES `ui_test_execution` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='UI测试执行队列表';

-- UI测试执行步骤结果表
CREATE TABLE IF NOT EXISTS `ui_test_execution_step` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '步骤结果ID',
  `detail_id` BIGINT NOT NULL COMMENT '执行详情ID',
  `step_index` INT NOT NULL COMMENT '步骤索引',
  `step_name` VARCHAR(100) NOT NULL COMMENT '步骤名称',
  `action_type` VARCHAR(50) NOT NULL COMMENT '操作类型',
  `target` TEXT COMMENT '操作目标',
  `input_value` TEXT COMMENT '输入值',
  `expected_result` TEXT COMMENT '预期结果',
  `actual_result` TEXT COMMENT '实际结果',
  `status` VARCHAR(20) NOT NULL COMMENT '状态：passed/failed/skipped',
  `screenshot_path` VARCHAR(255) COMMENT '步骤截图路径',
  `error_message` TEXT COMMENT '错误信息',
  `duration` INT COMMENT '步骤执行时长（毫秒）',
  `timestamp` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '执行时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_detail_id` (`detail_id`),
  INDEX `idx_step_index` (`step_index`),
  INDEX `idx_status` (`status`),
  INDEX `idx_timestamp` (`timestamp`),
  CONSTRAINT `fk_execution_step_detail_id` FOREIGN KEY (`detail_id`) REFERENCES `ui_test_execution_detail` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='UI测试执行步骤结果表'; 