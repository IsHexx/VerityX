-- 测试计划与测试用例关联表
CREATE TABLE test_plan_case_rel (
    id INT AUTO_INCREMENT COMMENT '关联ID，自增主键' PRIMARY KEY,
    plan_id INT NOT NULL COMMENT '测试计划ID',
    case_id INT NOT NULL COMMENT '测试用例ID',
    order_index INT NOT NULL COMMENT '执行顺序',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT test_plan_case_rel_id UNIQUE (id),
    UNIQUE INDEX idx_test_plan_case (plan_id, case_id),
    INDEX idx_test_case_plan (case_id, plan_id)
) COMMENT '测试计划与测试用例关联表';
