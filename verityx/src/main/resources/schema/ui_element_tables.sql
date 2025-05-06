-- UI元素分组表
CREATE TABLE IF NOT EXISTS ui_element_group (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    group_name VARCHAR(100) NOT NULL COMMENT '分组名称',
    description VARCHAR(500) COMMENT '分组描述',
    element_count INT DEFAULT 0 COMMENT '元素数量',
    created_by VARCHAR(50) COMMENT '创建人',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME COMMENT '更新时间'
) COMMENT='UI元素分组表';

-- UI元素表
CREATE TABLE IF NOT EXISTS ui_element (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    element_name VARCHAR(100) NOT NULL COMMENT '元素名称',
    group_id BIGINT COMMENT '分组ID',
    locator_type VARCHAR(50) NOT NULL COMMENT '定位器类型 (ID、NAME、XPATH、CSS等)',
    locator_value VARCHAR(500) NOT NULL COMMENT '定位器值',
    description VARCHAR(500) COMMENT '元素描述',
    screenshot_path VARCHAR(255) COMMENT '截图路径',
    created_by VARCHAR(50) COMMENT '创建人',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME COMMENT '更新时间',
    FOREIGN KEY (group_id) REFERENCES ui_element_group(id) ON DELETE SET NULL
) COMMENT='UI元素表';

-- 添加索引
CREATE INDEX idx_ui_element_group_id ON ui_element(group_id);
CREATE INDEX idx_ui_element_name ON ui_element(element_name);
CREATE INDEX idx_ui_element_locator_type ON ui_element(locator_type); 