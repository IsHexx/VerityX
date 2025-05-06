-- UI测试用例表
CREATE TABLE ui_test_case (
    id INT AUTO_INCREMENT COMMENT 'UI测试用例ID，自增主键' PRIMARY KEY,
    case_id VARCHAR(50) NOT NULL COMMENT '用例编号，如UI-001',
    case_title VARCHAR(255) NOT NULL COMMENT '用例标题',
    project_id INT NOT NULL COMMENT '所属项目ID',
    importance_level VARCHAR(50) NOT NULL COMMENT '用例优先级，如P0、P1、P2、P3',
    environment VARCHAR(100) NOT NULL COMMENT '测试环境，如Chrome、Firefox、Edge、Safari',
    precondition TEXT NULL COMMENT '前置条件',
    description TEXT NULL COMMENT '用例描述',
    timeout INT NOT NULL DEFAULT 30 COMMENT '超时时间(秒)',
    retry_count INT NOT NULL DEFAULT 1 COMMENT '失败重试次数',
    screenshot_policy VARCHAR(50) NOT NULL DEFAULT 'failureOnly' COMMENT '截图策略：failureOnly(仅失败时)、everyStep(每个步骤)、none(不截图)',
    case_status VARCHAR(50) NOT NULL DEFAULT '未执行' COMMENT '执行状态：已执行、未执行、执行中',
    last_result VARCHAR(50) NULL DEFAULT '未执行' COMMENT '最后执行结果：通过、失败、部分通过、未执行、执行中',
    last_run_at TIMESTAMP NULL COMMENT '最后执行时间',
    created_by VARCHAR(255) NOT NULL COMMENT '创建人',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT ui_test_case_id UNIQUE (id),
    CONSTRAINT ui_test_case_case_id UNIQUE (case_id)
) COMMENT 'UI测试用例表';

-- UI测试步骤表
CREATE TABLE ui_test_step (
    id INT AUTO_INCREMENT COMMENT '步骤ID，自增主键' PRIMARY KEY,
    case_id VARCHAR(50) NOT NULL COMMENT '关联用例ID',
    step_index INT NOT NULL COMMENT '步骤序号',
    action_type VARCHAR(50) NOT NULL COMMENT '操作类型：click、input、select、getText、assertText、assertExists、wait、navigate等',
    locator_type VARCHAR(50) NULL COMMENT '元素定位类型：id、name、css、xpath、linkText等',
    locator_value VARCHAR(255) NULL COMMENT '元素定位值',
    input_value TEXT NULL COMMENT '输入/期望值',
    wait_time INT NULL COMMENT '等待时间(毫秒)',
    target_url VARCHAR(255) NULL COMMENT '目标URL',
    description VARCHAR(255) NULL COMMENT '步骤描述',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT ui_test_step_id UNIQUE (id),
    INDEX idx_ui_test_step_case_id (case_id)
) COMMENT 'UI测试步骤表';

-- UI测试参数表
CREATE TABLE ui_test_parameter (
    id INT AUTO_INCREMENT COMMENT '参数ID，自增主键' PRIMARY KEY,
    case_id VARCHAR(50) NOT NULL COMMENT '关联用例ID',
    param_name VARCHAR(100) NOT NULL COMMENT '参数名',
    param_value TEXT NOT NULL COMMENT '参数值',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT ui_test_parameter_id UNIQUE (id),
    INDEX idx_ui_test_parameter_case_id (case_id)
) COMMENT 'UI测试参数表';

-- UI测试执行记录表
CREATE TABLE ui_test_execution (
    id INT AUTO_INCREMENT COMMENT '执行记录ID，自增主键' PRIMARY KEY,
    case_id VARCHAR(50) NOT NULL COMMENT '关联用例ID',
    execution_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '执行时间',
    execution_status VARCHAR(50) NOT NULL COMMENT '执行状态：成功、失败、部分成功、已中断',
    execution_duration INT NULL COMMENT '执行时长(秒)',
    browser VARCHAR(50) NOT NULL COMMENT '浏览器',
    browser_version VARCHAR(50) NULL COMMENT '浏览器版本',
    executor VARCHAR(255) NOT NULL COMMENT '执行人',
    error_message TEXT NULL COMMENT '错误信息',
    screenshot_path TEXT NULL COMMENT '截图路径(JSON格式存储多个路径)',
    log_path VARCHAR(255) NULL COMMENT '日志文件路径',
    CONSTRAINT ui_test_execution_id UNIQUE (id),
    INDEX idx_ui_test_execution_case_id (case_id)
) COMMENT 'UI测试执行记录表';

-- UI测试步骤执行记录表
CREATE TABLE ui_test_step_execution (
    id INT AUTO_INCREMENT COMMENT '步骤执行ID，自增主键' PRIMARY KEY,
    execution_id INT NOT NULL COMMENT '关联执行记录ID',
    step_id INT NOT NULL COMMENT '关联步骤ID',
    execution_status VARCHAR(50) NOT NULL COMMENT '执行状态：成功、失败、跳过',
    execution_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '执行时间',
    execution_duration INT NULL COMMENT '执行时长(毫秒)',
    actual_result TEXT NULL COMMENT '实际结果',
    error_message TEXT NULL COMMENT '错误信息',
    screenshot_path VARCHAR(255) NULL COMMENT '截图路径',
    CONSTRAINT ui_test_step_execution_id UNIQUE (id),
    INDEX idx_ui_test_step_execution_execution_id (execution_id)
) COMMENT 'UI测试步骤执行记录表';

-- UI测试元素定位器表
CREATE TABLE ui_test_locator (
    id INT AUTO_INCREMENT COMMENT '定位器ID，自增主键' PRIMARY KEY,
    element_name VARCHAR(100) NOT NULL COMMENT '元素名称',
    page_name VARCHAR(100) NOT NULL COMMENT '所属页面',
    project_id INT NOT NULL COMMENT '所属项目ID',
    locator_type VARCHAR(50) NOT NULL COMMENT '定位类型：id、name、css、xpath、linkText等',
    locator_value VARCHAR(255) NOT NULL COMMENT '定位值',
    description VARCHAR(255) NULL COMMENT '元素描述',
    screenshot_path VARCHAR(255) NULL COMMENT '元素截图路径',
    created_by VARCHAR(255) NOT NULL COMMENT '创建人',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT ui_test_locator_id UNIQUE (id),
    UNIQUE INDEX idx_ui_test_locator_unique (element_name, page_name, project_id)
) COMMENT 'UI测试元素定位器表';

-- UI测试套件表
CREATE TABLE ui_test_suite (
    id INT AUTO_INCREMENT COMMENT '套件ID，自增主键' PRIMARY KEY,
    suite_name VARCHAR(100) NOT NULL COMMENT '套件名称',
    project_id INT NOT NULL COMMENT '所属项目ID',
    description TEXT NULL COMMENT '套件描述',
    execute_in_parallel BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否并行执行',
    max_parallel_count INT NULL COMMENT '最大并行数',
    created_by VARCHAR(255) NOT NULL COMMENT '创建人',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT ui_test_suite_id UNIQUE (id),
    UNIQUE INDEX idx_ui_test_suite_name_project (suite_name, project_id)
) COMMENT 'UI测试套件表';

-- UI测试套件用例关联表
CREATE TABLE ui_test_suite_case (
    id INT AUTO_INCREMENT COMMENT '关联ID，自增主键' PRIMARY KEY,
    suite_id INT NOT NULL COMMENT '套件ID',
    case_id VARCHAR(50) NOT NULL COMMENT '用例ID',
    execution_order INT NOT NULL COMMENT '执行顺序',
    CONSTRAINT ui_test_suite_case_id UNIQUE (id),
    UNIQUE INDEX idx_ui_test_suite_case (suite_id, case_id)
) COMMENT 'UI测试套件用例关联表'; 