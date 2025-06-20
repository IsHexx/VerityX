create table api_automation
(
    scene_id    int auto_increment comment '场景ID，自增主键'
        primary key,
    directory   varchar(255) not null comment '所属目录',
    scene_name  varchar(255) not null comment '场景名称',
    step_method varchar(255) not null comment '步骤方法',
    step_name   varchar(255) not null comment '步骤名称',
    remark      text         null comment '备注',
    project_id  int          not null,
    constraint scene_id
        unique (scene_id)
)
    comment '接口自动化表';

create table api_management
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
    constraint api_id
        unique (api_id),
    index idx_parent_id (parent_id),
    index idx_item_type (item_type),
    index idx_project_id (project_id)
)
    comment '接口管理表（统一存储目录、接口、用例）';

create table api_query_params
(
    param_id    int auto_increment comment '查询参数ID，自增主键'
        primary key,
    api_id      int          not null comment '关联接口ID',
    parameter   varchar(255) not null comment '查询参数名称',
    param_type  varchar(50)  not null comment '参数类型（例如：string, int）',
    description text         null comment '参数说明',
    project_id  int          not null,
    constraint param_id
        unique (param_id)
)
    comment '接口查询参数表';

create table api_request_body
(
    body_id     int auto_increment comment '请求Body ID，自增主键'
        primary key,
    api_id      int          not null comment '关联接口ID',
    parameter   varchar(255) not null comment '请求Body参数名称',
    description text         null comment '参数说明',
    project_id  int          not null,
    constraint body_id
        unique (body_id)
)
    comment '接口请求Body表';

create table bug_management
(
    bug_id             int auto_increment comment '缺陷ID，自增主键'
        primary key,
    bug_title          varchar(255)                        not null comment 'Bug标题',
    level              varchar(50)                         not null comment 'Bug级别（例如：低、中、高）',
    severity           varchar(50)                         not null comment '缺陷严重级别（例如：轻微、严重、致命）',
    created_by         varchar(10)                         not null comment '创建人（用户ID）',
    created_at         timestamp default CURRENT_TIMESTAMP not null comment '创建日期',
    assigned_to        int                                 null comment '指派给（用户ID）',
    bug_type           varchar(50)                         not null comment 'Bug类型（例如：功能、性能、UI）',
    status             varchar(50)                         not null comment '状态（例如：open、in progress、closed）',
    plan_id            int                                 null comment '所属测试计划ID',
    project_id         int                                 null comment '所属项目ID',
    operating_system   varchar(50)                         null comment '操作系统',
    browser            varchar(50)                         null comment '浏览器',
    reproduction_steps text                                null comment '重现步骤',
    attachments        text                                null comment '附件链接或路径',
    constraint bug_id
        unique (bug_id)
)
    comment '缺陷管理表';

create table project
(
    project_id   int auto_increment comment '项目ID，自增主键'
        primary key,
    project_name varchar(255) not null comment '项目名称',
    description  text         null comment '项目描述',
    start_date   date         not null comment '项目开始日期',
    end_date     date         null comment '项目结束日期',
    status       varchar(50)  not null comment '项目状态（例如：pending、ongoing、completed）',
    constraint project_id
        unique (project_id),
    constraint project_name
        unique (project_name)
)
    comment '项目表';

create table test_case
(
    case_id          int auto_increment comment '用例ID，自增主键'
        primary key,
    case_title       varchar(255)                        not null comment '用例标题',
    importance_level varchar(50)                         not null comment '用例重要级别（例如：高、中、低）',
    preconditions    text                                null comment '前置条件',
    case_status      varchar(10)                         null,
    created_by       varchar(255)                        not null comment '创建人（用户ID）',
    execution_time   timestamp                           null comment '执行时间',
    created_at       timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    last_modified_by int                                 null comment '最后修改者（用户ID）',
    case_type        varchar(50)                         not null comment '用例类型（例如：功能、性能、接口）',
    descirption      varchar(255)                        null comment '用例详情',
    execution_result varchar(50)                         null comment '执行结果（例如：通过、失败、阻塞）',
    remarks          text                                null comment '备注',
    attachments      text                                null comment '附件链接或路径',
    project_id       int                                 not null,
    constraint case_id
        unique (case_id)
)
    comment '测试用例表';

create table test_plan
(
    plan_id      int auto_increment comment '测试计划ID，自增主键'
        primary key,
    plan_name    varchar(255)                        not null comment '测试计划名称',
    description  text                                null comment '测试计划描述',
    project_id   int                                 not null comment '关联项目版本',
    status       varchar(50)                         not null comment '测试计划状态（例如：pending、in progress、completed）',
    participants varchar(255)                        null comment '参与人（存储用户ID列表，用逗号分隔）',
    start_date   date                                null comment '测试计划开始日期',
    end_date     date                                null comment '测试计划结束日期',
    created_at   timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at   timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    plan_version varchar(50)                         not null comment '计划版本',
    constraint plan_id
        unique (plan_id),
    constraint plan_name
        unique (plan_name)
)
    comment '测试计划表';

create table test_report
(
    report_id          int auto_increment comment '报告ID，自增主键'
        primary key,
    report_title       varchar(255)                        not null comment '报告标题',
    created_by         int                                 not null comment '创建人（用户ID）',
    created_at         timestamp default CURRENT_TIMESTAMP not null comment '创建日期',
    plan_id            int                                 null comment '所属测试计划ID',
    test_version       varchar(50)                         not null comment '测试版本',
    participants       text                                null comment '参与人员',
    responsible_person int                                 null comment '负责人（用户ID）',
    summary            text                                null comment '报告概括',
    conclusion         text                                null comment '测试结论',
    attachments        text                                null comment '附件链接或路径',
    related_cases      text                                null comment '关联的用例ID列表',
    remaining_bugs     text                                null comment '遗留的Bug ID列表',
    project_id         int                                 not null,
    constraint report_id
        unique (report_id)
)
    comment '测试报告表';

create table test_step
(
    step_id          int auto_increment comment '步骤ID，自增主键'
        primary key,
    case_id          int  not null comment '关联用例ID',
    step_description text not null comment '用例步骤描述',
    expected_result  text not null comment '预期结果',
    actual_result    text null comment '实际结果',
    project_id       int  not null,
    constraint step_id
        unique (step_id)
)
    comment '测试步骤表';

create table user
(
    id   int auto_increment
        primary key,
    name varchar(50) not null,
    age  int         not null
);

create table user_login
(
    id            int auto_increment
        primary key,
    username      varchar(255)         not null comment '用户名',
    password      varchar(255)         not null comment '密码（建议加密存储）',
    email         varchar(255)         null comment '用户邮箱',
    phone_number  varchar(20)          null comment '用户手机号',
    created_at    timestamp            null comment '创建时间',
    last_login_at timestamp            null comment '上次登录时间',
    is_active     tinyint(1) default 1 not null comment '是否激活，0未激活，1激活',
    constraint id
        unique (id)
)
    comment '用户表';

