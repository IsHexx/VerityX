-- 重新设计的API管理表结构
-- 基于模块化的API接口管理系统
-- 执行时间：2025-06-19

USE verityx;
SET NAMES utf8mb4;

-- 删除旧表（备份后）
DROP TABLE IF EXISTS api_management_old;
RENAME TABLE api_management TO api_management_old;

-- 1. 接口模块表（目录）
CREATE TABLE api_module (
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
CREATE TABLE api_endpoint (
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
    
    FOREIGN KEY (module_id) REFERENCES api_module(id) ON DELETE CASCADE,
    INDEX idx_module_id (module_id),
    INDEX idx_method (method),
    INDEX idx_status (status)
) COMMENT '接口信息表';

-- 3. 请求参数表
CREATE TABLE api_param (
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
    
    FOREIGN KEY (api_id) REFERENCES api_endpoint(id) ON DELETE CASCADE,
    INDEX idx_api_id (api_id),
    INDEX idx_param_in (param_in)
) COMMENT '请求参数表';

-- 4. 响应示例表
CREATE TABLE api_response (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    api_id INT NOT NULL COMMENT '所属接口ID',
    status_code INT NOT NULL DEFAULT 200 COMMENT '响应码',
    description TEXT COMMENT '响应描述',
    example JSON COMMENT '响应示例内容（JSON格式）',
    headers JSON COMMENT '响应头示例（JSON格式）',
    is_default BOOLEAN DEFAULT FALSE COMMENT '是否为默认响应',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    FOREIGN KEY (api_id) REFERENCES api_endpoint(id) ON DELETE CASCADE,
    INDEX idx_api_id (api_id),
    INDEX idx_status_code (status_code)
) COMMENT '响应示例表';

-- 5. 接口标签表
CREATE TABLE api_tag (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    name VARCHAR(50) NOT NULL COMMENT '标签名',
    color VARCHAR(20) DEFAULT '#409EFF' COMMENT '标签颜色',
    description TEXT COMMENT '标签描述',
    project_id INT NOT NULL COMMENT '项目ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    INDEX idx_project_id (project_id),
    UNIQUE KEY uk_name_project (name, project_id)
) COMMENT '接口标签表';

-- 6. 接口标签关联表
CREATE TABLE api_endpoint_tag (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    api_id INT NOT NULL COMMENT '接口ID',
    tag_id INT NOT NULL COMMENT '标签ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    FOREIGN KEY (api_id) REFERENCES api_endpoint(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES api_tag(id) ON DELETE CASCADE,
    UNIQUE KEY uk_api_tag (api_id, tag_id)
) COMMENT '接口标签关联表';

-- 7. 接口测试用例表
CREATE TABLE api_test_case (
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
    
    FOREIGN KEY (api_id) REFERENCES api_endpoint(id) ON DELETE CASCADE,
    INDEX idx_api_id (api_id),
    INDEX idx_status (status)
) COMMENT '接口测试用例表';

-- 插入初始数据：从旧表迁移模块数据
INSERT INTO api_module (name, description, project_id, created_at, updated_at)
SELECT DISTINCT 
    COALESCE(api_directory, '默认模块') as name,
    CONCAT('模块：', COALESCE(api_directory, '默认模块')) as description,
    project_id,
    MIN(created_at) as created_at,
    MAX(updated_at) as updated_at
FROM api_management_old 
WHERE item_type = 'api'
GROUP BY COALESCE(api_directory, '默认模块'), project_id;

-- 插入初始数据：从旧表迁移接口数据
INSERT INTO api_endpoint (module_id, name, path, method, description, mock_url, status, created_at, updated_at)
SELECT 
    m.id as module_id,
    old.api_name as name,
    old.api_path as path,
    old.request_method as method,
    old.description,
    old.mock_url,
    old.status,
    old.created_at,
    old.updated_at
FROM api_management_old old
JOIN api_module m ON m.name = COALESCE(old.api_directory, '默认模块') AND m.project_id = old.project_id
WHERE old.item_type = 'api';

-- 验证迁移结果
SELECT 
    '模块统计' as type,
    COUNT(*) as count
FROM api_module
UNION ALL
SELECT 
    '接口统计' as type,
    COUNT(*) as count
FROM api_endpoint;

-- 显示模块和接口的对应关系
SELECT 
    m.id as module_id,
    m.name as module_name,
    COUNT(e.id) as api_count
FROM api_module m
LEFT JOIN api_endpoint e ON e.module_id = m.id
GROUP BY m.id, m.name
ORDER BY m.id;
