-- 更新api_management表，添加接口参数字段
-- 执行时间：2025-06-18

USE verityx;

-- 添加新字段到api_management表
ALTER TABLE api_management 
ADD COLUMN path_params TEXT NULL COMMENT 'Path参数（JSON格式）' AFTER project_id,
ADD COLUMN query_params TEXT NULL COMMENT 'Query参数（JSON格式）' AFTER path_params,
ADD COLUMN header_params TEXT NULL COMMENT 'Header参数（JSON格式）' AFTER query_params,
ADD COLUMN description TEXT NULL COMMENT '接口描述' AFTER header_params;

-- 验证表结构
DESCRIBE api_management;

-- 显示更新后的表结构
SHOW CREATE TABLE api_management;
