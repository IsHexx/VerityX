-- 迁移到新的API管理表结构
-- 执行时间：2025-06-19

USE verityx;
SET NAMES utf8mb4;

-- 备份当前表
DROP TABLE IF EXISTS api_management_old_backup;
CREATE TABLE api_management_old_backup AS SELECT * FROM api_management;

-- 1. 创建新的表结构
-- 接口模块表（目录）
DROP TABLE IF EXISTS api_module;
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

-- 接口信息表
DROP TABLE IF EXISTS api_endpoint;
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
    
    INDEX idx_module_id (module_id),
    INDEX idx_method (method),
    INDEX idx_status (status)
) COMMENT '接口信息表';

-- 接口测试用例表
DROP TABLE IF EXISTS api_test_case;
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
    
    INDEX idx_api_id (api_id),
    INDEX idx_status (status)
) COMMENT '接口测试用例表';

-- 2. 迁移数据
-- 迁移模块数据（从directory类型的记录）
INSERT INTO api_module (name, description, project_id, created_at, updated_at)
SELECT 
    api_name as name,
    COALESCE(description, CONCAT('模块：', api_name)) as description,
    project_id,
    created_at,
    updated_at
FROM api_management_old_backup 
WHERE item_type = 'directory'
ORDER BY api_id;

-- 迁移接口数据（从api类型的记录）
INSERT INTO api_endpoint (module_id, name, path, method, description, mock_url, status, created_at, updated_at)
SELECT 
    m.id as module_id,
    old.api_name as name,
    COALESCE(old.api_path, '/api/default') as path,
    COALESCE(old.request_method, 'GET') as method,
    old.description,
    old.mock_url,
    old.status,
    old.created_at,
    old.updated_at
FROM api_management_old_backup old
JOIN api_module m ON m.name = (
    SELECT dir.api_name 
    FROM api_management_old_backup dir 
    WHERE dir.item_type = 'directory' 
    AND dir.api_id = old.parent_id
    LIMIT 1
) AND m.project_id = old.project_id
WHERE old.item_type = 'api' AND old.parent_id IS NOT NULL;

-- 处理没有父级的接口（如果有的话）
INSERT INTO api_endpoint (module_id, name, path, method, description, mock_url, status, created_at, updated_at)
SELECT 
    (SELECT id FROM api_module WHERE name = '默认模块' AND project_id = old.project_id LIMIT 1) as module_id,
    old.api_name as name,
    COALESCE(old.api_path, '/api/default') as path,
    COALESCE(old.request_method, 'GET') as method,
    old.description,
    old.mock_url,
    old.status,
    old.created_at,
    old.updated_at
FROM api_management_old_backup old
WHERE old.item_type = 'api' AND old.parent_id IS NULL;

-- 如果有没有父级的接口，先创建默认模块
INSERT IGNORE INTO api_module (name, description, project_id)
SELECT DISTINCT '默认模块', '系统默认模块', project_id
FROM api_management_old_backup 
WHERE item_type = 'api' AND parent_id IS NULL;

-- 3. 验证迁移结果
SELECT 
    '模块统计' as type,
    COUNT(*) as count
FROM api_module
UNION ALL
SELECT 
    '接口统计' as type,
    COUNT(*) as count
FROM api_endpoint
UNION ALL
SELECT 
    '用例统计' as type,
    COUNT(*) as count
FROM api_test_case;

-- 显示模块和接口的对应关系
SELECT 
    m.id as module_id,
    m.name as module_name,
    COUNT(e.id) as api_count
FROM api_module m
LEFT JOIN api_endpoint e ON e.module_id = m.id
GROUP BY m.id, m.name
ORDER BY m.id;
