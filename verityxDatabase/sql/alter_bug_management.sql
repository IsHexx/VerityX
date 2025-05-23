-- 添加case_id字段到bug_management表
ALTER TABLE bug_management
ADD COLUMN case_id INT NULL COMMENT '所属测试用例ID' AFTER plan_id;

-- 添加索引
CREATE INDEX idx_bug_case_id ON bug_management(case_id);
CREATE INDEX idx_bug_plan_id ON bug_management(plan_id);
