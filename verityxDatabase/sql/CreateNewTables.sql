-- 创建新的API管理表结构
USE verityx;
SET NAMES utf8mb4;

-- 1. 接口模块表（目录）
CREATE TABLE IF NOT EXISTS api_module (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    name VARCHAR(100) NOT NULL COMMENT '模块/目录名称',
    description TEXT COMMENT '模块描述',
    project_id INT NOT NULL COMMENT '项目ID',
    sort_order INT DEFAULT 0 COMMENT '排序顺序',
    status VARCHAR(20) DEFAULT 'active' COMMENT '状态：active-启用, inactive-禁用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_project_id (project_id),
    INDEX idx_status (status),
    UNIQUE KEY uk_name_project (name, project_id)
) COMMENT '接口模块表';

-- 2. 接口信息表
CREATE TABLE IF NOT EXISTS api_endpoint (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    module_id INT NOT NULL COMMENT '所属模块ID',
    name VARCHAR(200) NOT NULL COMMENT '接口名称',
    path VARCHAR(500) NOT NULL COMMENT '接口路径',
    method VARCHAR(10) NOT NULL DEFAULT 'GET' COMMENT '请求方法',
    description TEXT COMMENT '接口描述',
    is_temp BOOLEAN DEFAULT FALSE COMMENT '是否为临时接口',
    mock_url VARCHAR(500) COMMENT 'Mock地址',
    status VARCHAR(20) DEFAULT 'active' COMMENT '状态',
    sort_order INT DEFAULT 0 COMMENT '排序顺序',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_module_id (module_id),
    INDEX idx_method (method),
    INDEX idx_status (status)
) COMMENT '接口信息表';

-- 3. 请求参数表
CREATE TABLE IF NOT EXISTS api_param (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    api_id INT NOT NULL COMMENT '所属接口ID',
    name VARCHAR(100) NOT NULL COMMENT '参数名',
    type VARCHAR(50) NOT NULL DEFAULT 'string' COMMENT '参数类型',
    param_in VARCHAR(20) NOT NULL DEFAULT 'query' COMMENT '参数位置：query, path, header, body',
    required BOOLEAN DEFAULT FALSE COMMENT '是否必填',
    description TEXT COMMENT '参数描述',
    example VARCHAR(500) COMMENT '示例值',
    default_value VARCHAR(500) COMMENT '默认值',
    sort_order INT DEFAULT 0 COMMENT '排序顺序',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_api_id (api_id),
    INDEX idx_param_in (param_in)
) COMMENT '请求参数表';

-- 4. 响应示例表
CREATE TABLE IF NOT EXISTS api_response (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    api_id INT NOT NULL COMMENT '所属接口ID',
    status_code INT NOT NULL DEFAULT 200 COMMENT '响应码',
    description TEXT COMMENT '响应描述',
    example JSON COMMENT '响应示例内容（JSON格式）',
    headers JSON COMMENT '响应头示例（JSON格式）',
    is_default BOOLEAN DEFAULT FALSE COMMENT '是否为默认响应',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_api_id (api_id),
    INDEX idx_status_code (status_code)
) COMMENT '响应示例表';

-- 5. 接口测试用例表
CREATE TABLE IF NOT EXISTS api_test_case (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    api_id INT NOT NULL COMMENT '所属接口ID',
    name VARCHAR(200) NOT NULL COMMENT '用例名称',
    description TEXT COMMENT '用例描述',
    request_params JSON COMMENT '请求参数（JSON格式）',
    expected_response JSON COMMENT '期望响应（JSON格式）',
    status VARCHAR(20) DEFAULT 'active' COMMENT '状态',
    sort_order INT DEFAULT 0 COMMENT '排序顺序',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_api_id (api_id),
    INDEX idx_status (status)
) COMMENT '接口测试用例表';
