-- 系统参数配置表
CREATE TABLE `sys_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_name` varchar(100) NOT NULL COMMENT '参数名称',
  `config_key` varchar(100) NOT NULL COMMENT '参数键名',
  `config_value` varchar(500) NOT NULL COMMENT '参数键值',
  `config_type` varchar(20) DEFAULT 'text' COMMENT '配置类型（text、number、boolean、json等）',
  `is_system` tinyint(1) DEFAULT '0' COMMENT '是否系统内置（0否 1是）',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态（0停用 1正常）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint DEFAULT NULL COMMENT '创建者',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_config_key` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统参数配置表';

-- 字典类型表
CREATE TABLE `sys_dict_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典类型ID',
  `dict_name` varchar(100) NOT NULL COMMENT '字典名称',
  `dict_type` varchar(100) NOT NULL COMMENT '字典类型',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态（0停用 1正常）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint DEFAULT NULL COMMENT '创建者',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`),
  UNIQUE KEY `dict_type` (`dict_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典类型表';

-- 字典数据表
CREATE TABLE `sys_dict_data` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典数据ID',
  `dict_type` varchar(100) NOT NULL COMMENT '字典类型',
  `dict_label` varchar(100) NOT NULL COMMENT '字典标签',
  `dict_value` varchar(100) NOT NULL COMMENT '字典键值',
  `dict_sort` int DEFAULT '0' COMMENT '字典排序',
  `css_class` varchar(100) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) DEFAULT NULL COMMENT '表格回显样式',
  `is_default` tinyint(1) DEFAULT '0' COMMENT '是否默认（0否 1是）',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态（0停用 1正常）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint DEFAULT NULL COMMENT '创建者',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典数据表';

-- 初始化系统配置数据
INSERT INTO `sys_config` (`config_name`, `config_key`, `config_value`, `config_type`, `is_system`, `status`, `remark`) VALUES 
('系统名称', 'sys.name', 'VerityX测试平台', 'text', 1, 1, '系统名称，显示在页面顶部'),
('系统版本', 'sys.version', '1.0.0', 'text', 1, 1, '系统版本号'),
('默认密码', 'sys.default.password', '123456', 'text', 1, 1, '用户默认密码'),
('账号自助注册', 'sys.account.register', 'true', 'boolean', 1, 1, '是否开启账号自助注册功能（true开启，false关闭）'),
('首页路径', 'sys.index.path', '/dashboard', 'text', 1, 1, '首页路径'),
('允许上传的文件类型', 'sys.upload.allowed.types', 'jpg,jpeg,png,gif,doc,docx,xls,xlsx,pdf', 'text', 1, 1, '允许上传的文件类型，多个用逗号分隔'),
('最大上传文件大小', 'sys.upload.max.size', '10', 'number', 1, 1, '最大上传文件大小（MB）'),
('会话超时时间', 'sys.session.timeout', '30', 'number', 1, 1, '会话超时时间（分钟）'),
('邮件发送服务器', 'mail.smtp.host', 'smtp.example.com', 'text', 1, 0, '邮件发送服务器地址'),
('邮件发送端口', 'mail.smtp.port', '465', 'number', 1, 0, '邮件发送服务器端口'),
('邮件发送用户名', 'mail.smtp.username', 'system@example.com', 'text', 1, 0, '邮件发送用户名'),
('邮件发送密码', 'mail.smtp.password', 'password', 'text', 1, 0, '邮件发送密码或授权码'),
('启用SSL', 'mail.smtp.ssl.enable', 'true', 'boolean', 1, 0, '邮件服务器是否启用SSL');

-- 初始化字典类型数据
INSERT INTO `sys_dict_type` (`dict_name`, `dict_type`, `status`, `remark`) VALUES 
('是否类型', 'sys_yes_no', 1, '系统是否类型'),
('状态类型', 'sys_status', 1, '系统状态类型'),
('BUG级别', 'bug_level', 1, 'BUG级别'),
('BUG严重度', 'bug_severity', 1, 'BUG严重程度'),
('BUG类型', 'bug_type', 1, 'BUG类型'),
('BUG状态', 'bug_status', 1, 'BUG状态'),
('用户性别', 'sys_user_sex', 1, '用户性别类型');

-- 初始化字典数据
INSERT INTO `sys_dict_data` (`dict_type`, `dict_label`, `dict_value`, `dict_sort`, `is_default`, `status`, `remark`) VALUES 
('sys_yes_no', '是', 'Y', 1, 1, 1, '系统默认是'),
('sys_yes_no', '否', 'N', 2, 0, 1, '系统默认否'),
('sys_status', '正常', '1', 1, 1, 1, '正常状态'),
('sys_status', '停用', '0', 2, 0, 1, '停用状态'),
('bug_level', '优先级1', 'priority1', 1, 0, 1, '最高优先级'),
('bug_level', '优先级2', 'priority2', 2, 0, 1, '高优先级'),
('bug_level', '优先级3', 'priority3', 3, 1, 1, '中优先级'),
('bug_level', '优先级4', 'priority4', 4, 0, 1, '低优先级'),
('bug_severity', '阻塞', '阻塞', 1, 0, 1, '阻塞'),
('bug_severity', '严重', '严重', 2, 0, 1, '严重'),
('bug_severity', '一般', '一般', 3, 1, 1, '一般'),
('bug_severity', '轻微', '轻微', 4, 0, 1, '轻微'),
('bug_type', '功能问题', '功能问题', 1, 1, 1, '功能问题'),
('bug_type', '接口问题', '接口问题', 2, 0, 1, '接口问题'),
('bug_type', '性能问题', '性能问题', 3, 0, 1, '性能问题'),
('bug_type', '安全问题', '安全问题', 4, 0, 1, '安全问题'),
('bug_type', 'UI问题', 'UI问题', 5, 0, 1, 'UI问题'),
('bug_type', '兼容性问题', '兼容性问题', 6, 0, 1, '兼容性问题'),
('bug_status', '已创建', '已创建', 1, 1, 1, '已创建'),
('bug_status', '已确认', '已确认', 2, 0, 1, '已确认'),
('bug_status', '待修复', '待修复', 3, 0, 1, '待修复'),
('bug_status', '修复中', '修复中', 4, 0, 1, '修复中'),
('bug_status', '已修复', '已修复', 5, 0, 1, '已修复'),
('bug_status', '已关闭', '已关闭', 6, 0, 1, '已关闭'),
('bug_status', '已拒绝', '已拒绝', 7, 0, 1, '已拒绝'),
('sys_user_sex', '男', '0', 1, 1, 1, '男性'),
('sys_user_sex', '女', '1', 2, 0, 1, '女性'),
('sys_user_sex', '未知', '2', 3, 0, 1, '未知性别'); 