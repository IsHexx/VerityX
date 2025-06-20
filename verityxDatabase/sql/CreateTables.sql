USE verityx;

-- 备份当前表
DROP TABLE IF EXISTS api_management_old_backup;
CREATE TABLE api_management_old_backup AS SELECT * FROM api_management;

-- 创建接口模块表
DROP TABLE IF EXISTS api_module;
CREATE TABLE api_module (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    project_id INT NOT NULL,
    sort_order INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_project_id (project_id),
    INDEX idx_status (status),
    UNIQUE KEY uk_name_project (name, project_id)
);

-- 创建接口信息表
DROP TABLE IF EXISTS api_endpoint;
CREATE TABLE api_endpoint (
    id INT AUTO_INCREMENT PRIMARY KEY,
    module_id INT NOT NULL,
    name VARCHAR(200) NOT NULL,
    path VARCHAR(500) NOT NULL,
    method VARCHAR(10) NOT NULL DEFAULT 'GET',
    description TEXT,
    is_temp BOOLEAN DEFAULT FALSE,
    mock_url VARCHAR(500),
    status VARCHAR(20) DEFAULT 'active',
    sort_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_module_id (module_id),
    INDEX idx_method (method),
    INDEX idx_status (status)
);

-- 创建接口测试用例表
DROP TABLE IF EXISTS api_test_case;
CREATE TABLE api_test_case (
    id INT AUTO_INCREMENT PRIMARY KEY,
    api_id INT NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    request_params JSON,
    expected_response JSON,
    status VARCHAR(20) DEFAULT 'active',
    sort_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_api_id (api_id),
    INDEX idx_status (status)
);
