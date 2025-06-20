-- 清理不再使用的旧表
-- 执行时间：2025-06-19
-- 说明：删除旧的API管理相关表，保留新的规范化表结构

USE verityx;
SET NAMES utf8mb4;

-- ==================== 当前使用的表（不要删除） ====================
-- 新的API管理系统使用的表：
-- 1. api_module          - 接口模块表（目录）
-- 2. api_endpoint        - 接口信息表
-- 3. api_param           - 接口参数表
-- 4. api_response        - 响应示例表
-- 5. api_tag             - 接口标签表
-- 6. api_endpoint_tag    - 接口标签关联表
-- 7. api_test_case       - 接口测试用例表

-- ==================== 备份重要数据 ====================
-- 在删除前，确保重要数据已经迁移到新表中

-- 检查新表是否有数据
SELECT 
    'api_module' as table_name, COUNT(*) as record_count FROM api_module
UNION ALL
SELECT 
    'api_endpoint' as table_name, COUNT(*) as record_count FROM api_endpoint
UNION ALL
SELECT 
    'api_test_case' as table_name, COUNT(*) as record_count FROM api_test_case;

-- ==================== 删除旧表 ====================

-- 1. 删除旧的API管理表（已被新的规范化表结构替代）
DROP TABLE IF EXISTS api_management;
DROP TABLE IF EXISTS api_management_old;
DROP TABLE IF EXISTS api_management_new;
DROP TABLE IF EXISTS api_management_backup;
DROP TABLE IF EXISTS api_management_old_backup;

-- 2. 删除旧的查询参数表（功能已整合到api_param表）
DROP TABLE IF EXISTS api_query_params;

-- 3. 删除旧的请求体参数表（功能已整合到api_param表）
DROP TABLE IF EXISTS api_request_body;

-- 4. 删除旧的自动化测试表（如果不再使用）
-- 注意：如果还在使用自动化测试功能，请保留这个表
-- DROP TABLE IF EXISTS api_automation;

-- ==================== 验证清理结果 ====================

-- 显示剩余的API相关表
SHOW TABLES LIKE 'api_%';

-- 显示新表的记录统计
SELECT 
    'api_module' as table_name, COUNT(*) as record_count FROM api_module
UNION ALL
SELECT 
    'api_endpoint' as table_name, COUNT(*) as record_count FROM api_endpoint
UNION ALL
SELECT 
    'api_param' as table_name, COUNT(*) as record_count FROM api_param
UNION ALL
SELECT 
    'api_response' as table_name, COUNT(*) as record_count FROM api_response
UNION ALL
SELECT 
    'api_tag' as table_name, COUNT(*) as record_count FROM api_tag
UNION ALL
SELECT 
    'api_endpoint_tag' as table_name, COUNT(*) as record_count FROM api_endpoint_tag
UNION ALL
SELECT 
    'api_test_case' as table_name, COUNT(*) as record_count FROM api_test_case;

-- ==================== 清理说明 ====================
/*
已删除的旧表：
1. api_management - 旧的统一API管理表，已被规范化的多表结构替代
2. api_management_* - 各种备份和临时表
3. api_query_params - 旧的查询参数表，功能已整合到api_param表
4. api_request_body - 旧的请求体参数表，功能已整合到api_param表

保留的新表：
1. api_module - 接口模块/目录管理
2. api_endpoint - 接口信息管理
3. api_param - 统一的参数管理（支持多种参数类型）
4. api_response - 响应示例管理
5. api_tag - 接口标签管理
6. api_endpoint_tag - 接口标签关联
7. api_test_case - 接口测试用例管理

新的表结构优势：
- 规范化设计，避免数据冗余
- 支持完整的API生命周期管理
- 更好的扩展性和维护性
- 符合企业级API管理系统标准
*/
