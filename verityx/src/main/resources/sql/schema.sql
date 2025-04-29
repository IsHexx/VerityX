-- API测试报告表
CREATE TABLE IF NOT EXISTS api_test_report (
    id INT AUTO_INCREMENT PRIMARY KEY,
    api_name VARCHAR(255) NOT NULL COMMENT '接口名称',
    url VARCHAR(255) NOT NULL COMMENT '接口地址',
    method VARCHAR(20) NOT NULL COMMENT '请求方法',
    status VARCHAR(20) NOT NULL COMMENT '状态(success/failed)',
    response_time INT COMMENT '响应时间(ms)',
    execution_time DATETIME NOT NULL COMMENT '执行时间',
    executor VARCHAR(50) COMMENT '执行人',
    headers TEXT COMMENT '请求头(JSON格式)',
    params TEXT COMMENT '请求参数(JSON格式)',
    response TEXT COMMENT '响应结果(JSON格式)',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT 'API测试报告表'; 