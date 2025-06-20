-- 修复目录结构，确保所有接口都有对应的目录
-- 执行时间：2025-06-19

USE verityx;
SET NAMES utf8mb4;

-- 1. 为所有不存在的api_directory创建真实的目录记录
INSERT INTO api_management (item_type, parent_id, api_directory, api_name, request_method, api_path, status, project_id, description, created_at, updated_at)
SELECT DISTINCT
    'directory' as item_type,
    NULL as parent_id,
    '' as api_directory,
    api_directory as api_name,
    '' as request_method,
    '' as api_path,
    'active' as status,
    project_id,
    CONCAT('目录：', api_directory) as description,
    NOW() as created_at,
    NOW() as updated_at
FROM api_management
WHERE item_type = 'api'
  AND api_directory IS NOT NULL
  AND api_directory != ''
  AND api_directory NOT IN (
    SELECT api_name FROM api_management WHERE item_type = 'directory'
  );

-- 2. 更新所有接口的parent_id，关联到对应的目录
UPDATE api_management api
JOIN api_management dir ON dir.item_type = 'directory' AND dir.api_name = api.api_directory
SET api.parent_id = dir.api_id
WHERE api.item_type = 'api' AND api.parent_id IS NULL;

-- 3. 处理没有api_directory的接口，将它们归到"默认目录"
-- 先创建默认目录（如果不存在）
INSERT IGNORE INTO api_management (item_type, parent_id, api_directory, api_name, request_method, api_path, status, project_id, description, created_at, updated_at)
VALUES ('directory', NULL, '', '默认目录', '', '', 'active', 1, '目录：默认目录', NOW(), NOW());

-- 更新没有目录的接口
UPDATE api_management api
JOIN api_management dir ON dir.item_type = 'directory' AND dir.api_name = '默认目录'
SET api.parent_id = dir.api_id,
    api.api_directory = '默认目录'
WHERE api.item_type = 'api' 
  AND (api.api_directory IS NULL OR api.api_directory = '' OR api.parent_id IS NULL);

-- 4. 验证修复结果
SELECT 
    '目录统计' as type,
    COUNT(*) as count
FROM api_management 
WHERE item_type = 'directory'
UNION ALL
SELECT 
    '有父级的接口' as type,
    COUNT(*) as count
FROM api_management 
WHERE item_type = 'api' AND parent_id IS NOT NULL
UNION ALL
SELECT 
    '无父级的接口' as type,
    COUNT(*) as count
FROM api_management 
WHERE item_type = 'api' AND parent_id IS NULL;

-- 5. 显示目录和接口的对应关系
SELECT 
    d.api_id as dir_id,
    d.api_name as directory_name,
    COUNT(a.api_id) as api_count
FROM api_management d
LEFT JOIN api_management a ON a.parent_id = d.api_id AND a.item_type = 'api'
WHERE d.item_type = 'directory'
GROUP BY d.api_id, d.api_name
ORDER BY d.api_id;
