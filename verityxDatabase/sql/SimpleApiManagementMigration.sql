-- 简化的API管理表迁移脚本
-- 执行时间：2025-06-19

USE verityx;
SET NAMES utf8mb4;

-- 清理之前的备份（如果存在）
DROP TABLE IF EXISTS api_management_backup;

-- 备份原表
CREATE TABLE api_management_backup AS SELECT * FROM api_management;

-- 添加新字段到现有表
ALTER TABLE api_management 
ADD COLUMN item_type varchar(20) NOT NULL DEFAULT 'api' COMMENT '项目类型：directory-目录, api-接口, case-用例' AFTER api_id,
ADD COLUMN parent_id int NULL COMMENT '父级ID，目录的父级为null，接口的父级为目录ID，用例的父级为接口ID' AFTER item_type;

-- 添加索引
ALTER TABLE api_management 
ADD INDEX idx_parent_id (parent_id),
ADD INDEX idx_item_type (item_type);

-- 更新现有数据，将所有现有记录标记为接口类型
UPDATE api_management SET item_type = 'api' WHERE item_type IS NULL OR item_type = '';

-- 验证迁移结果
SELECT 
    item_type,
    COUNT(*) as count
FROM api_management 
GROUP BY item_type;

-- 显示表结构
DESCRIBE api_management;
