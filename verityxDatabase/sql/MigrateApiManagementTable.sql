-- 迁移api_management表，支持目录、接口、用例统一管理
-- 执行时间：2025-06-19

USE verityx;

-- 设置字符集
SET NAMES utf8mb4;

-- 备份原表
CREATE TABLE api_management_backup AS SELECT * FROM api_management;

-- 创建新的表结构
DROP TABLE IF EXISTS api_management_new;
CREATE TABLE api_management_new
(
    api_id             int auto_increment comment 'ID，自增主键'
        primary key,
    item_type          varchar(20)                         not null comment '项目类型：directory-目录, api-接口, case-用例',
    parent_id          int                                 null comment '父级ID，目录的父级为null，接口的父级为目录ID，用例的父级为接口ID',
    api_directory      varchar(255)                        null comment '接口目录（仅接口类型使用）',
    api_name           varchar(255)                        not null comment '名称（目录名/接口名/用例名）',
    related_test_cases text                                null comment '关联接口用例ID列表',
    request_method     varchar(50)                         null comment '请求方法（例如：GET, POST）（仅接口类型使用）',
    api_path           varchar(255)                        null comment '接口路径（仅接口类型使用）',
    status             varchar(50)                         not null default 'active' comment '状态（例如：active, inactive）',
    created_at         timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at         timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    mock_url           varchar(255)                        null comment 'MOCK地址（仅接口类型使用）',
    response_body      text                                null comment '返回Body示例（仅接口类型使用）',
    project_id         int                                 not null,
    path_params        text                                null comment 'Path参数（JSON格式）（仅接口类型使用）',
    query_params       text                                null comment 'Query参数（JSON格式）（仅接口类型使用）',
    header_params      text                                null comment 'Header参数（JSON格式）（仅接口类型使用）',
    description        text                                null comment '描述',
    creator_id         int                                 null comment '创建人ID',
    creator_name       varchar(100)                        null comment '创建人姓名',
    updater_id         int                                 null comment '修改者ID',
    updater_name       varchar(100)                        null comment '修改者姓名',
    constraint api_id
        unique (api_id),
    index idx_parent_id (parent_id),
    index idx_item_type (item_type),
    index idx_project_id (project_id)
)
    comment '接口管理表（统一存储目录、接口、用例）';

-- 迁移数据：先创建目录
INSERT INTO api_management_new (item_type, parent_id, api_name, status, project_id, created_at, updated_at, description)
SELECT DISTINCT 
    'directory' as item_type,
    NULL as parent_id,
    api_directory as api_name,
    'active' as status,
    project_id,
    MIN(created_at) as created_at,
    MAX(updated_at) as updated_at,
    CONCAT('目录：', CONVERT(api_directory USING utf8mb4)) as description
FROM api_management_backup 
WHERE api_directory IS NOT NULL AND api_directory != ''
GROUP BY api_directory, project_id;

-- 迁移数据：创建接口，关联到对应目录
INSERT INTO api_management_new (
    item_type, parent_id, api_directory, api_name, related_test_cases, 
    request_method, api_path, status, created_at, updated_at, 
    mock_url, response_body, project_id, path_params, query_params, 
    header_params, description
)
SELECT 
    'api' as item_type,
    d.api_id as parent_id,
    old.api_directory,
    old.api_name,
    old.related_test_cases,
    old.request_method,
    old.api_path,
    old.status,
    old.created_at,
    old.updated_at,
    old.mock_url,
    old.response_body,
    old.project_id,
    old.path_params,
    old.query_params,
    old.header_params,
    old.description
FROM api_management_backup old
LEFT JOIN api_management_new d ON d.api_name = old.api_directory AND d.item_type = 'directory' AND d.project_id = old.project_id;

-- 替换原表
DROP TABLE api_management;
RENAME TABLE api_management_new TO api_management;

-- 验证迁移结果
SELECT 
    item_type,
    COUNT(*) as count,
    GROUP_CONCAT(DISTINCT project_id) as project_ids
FROM api_management 
GROUP BY item_type;

-- 显示表结构
DESCRIBE api_management;
