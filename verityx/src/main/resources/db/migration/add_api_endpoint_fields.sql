-- 为 api_endpoint 表添加新字段
ALTER TABLE api_endpoint 
ADD COLUMN owner VARCHAR(100) COMMENT '责任人',
ADD COLUMN tags VARCHAR(500) COMMENT '标签',
ADD COLUMN pre_url VARCHAR(500) COMMENT '前置URL',
ADD COLUMN path_params TEXT COMMENT '路径参数（JSON格式）',
ADD COLUMN query_params TEXT COMMENT '查询参数（JSON格式）',
ADD COLUMN header_params TEXT COMMENT '请求头参数（JSON格式）';
