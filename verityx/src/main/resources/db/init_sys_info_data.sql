-- 清空表数据（如果需要重新插入）
-- TRUNCATE TABLE `sys_version_info`;
-- TRUNCATE TABLE `sys_license`;
-- TRUNCATE TABLE `sys_upgrade_record`;
-- TRUNCATE TABLE `sys_about`;

-- 系统版本信息初始化
INSERT INTO `sys_version_info` (`version`, `release_date`, `release_notes`, `is_current`, `create_time`) 
VALUES ('1.0.0', '2023-10-01 00:00:00', 'VerityX测试平台初始版本发布，包含基础功能。', 0, NOW());

INSERT INTO `sys_version_info` (`version`, `release_date`, `release_notes`, `is_current`, `create_time`) 
VALUES ('1.1.0', '2023-11-15 00:00:00', '新增UI测试模块，优化用户界面体验。', 0, NOW());

INSERT INTO `sys_version_info` (`version`, `release_date`, `release_notes`, `is_current`, `create_time`) 
VALUES ('1.2.0', '2024-01-10 00:00:00', '新增接口测试功能，支持RESTful API测试。增强报告展示。', 0, NOW());

INSERT INTO `sys_version_info` (`version`, `release_date`, `release_notes`, `is_current`, `create_time`) 
VALUES ('1.3.0', '2024-03-20 00:00:00', '新增测试套件管理，支持测试用例批量执行和排序。\n增强元素定位器管理功能。\n优化数据展示和筛选。', 1, NOW());

-- 系统许可证初始化
INSERT INTO `sys_license` (`license_key`, `license_type`, `issued_to`, `issued_by`, `issue_date`, `expiration_date`, `max_users`, `features`, `status`, `remarks`, `create_time`) 
VALUES ('VERITYX-FREE-2023', '免费版', '公共用户', 'VerityX Inc.', '2023-10-01 00:00:00', '2024-10-01 00:00:00', 10, '{"ui_test": true, "api_test": true, "performance_test": false, "mobile_test": false}', '有效', '免费使用版本，功能有限制', NOW());

INSERT INTO `sys_license` (`license_key`, `license_type`, `issued_to`, `issued_by`, `issue_date`, `expiration_date`, `max_users`, `features`, `status`, `remarks`, `create_time`) 
VALUES ('VERITYX-STANDARD-2024', '标准版', 'VerityX测试团队', 'VerityX Inc.', '2024-01-01 00:00:00', '2025-01-01 00:00:00', 50, '{"ui_test": true, "api_test": true, "performance_test": true, "mobile_test": false}', '有效', '标准许可证，支持大部分功能', NOW());

INSERT INTO `sys_license` (`license_key`, `license_type`, `issued_to`, `issued_by`, `issue_date`, `expiration_date`, `max_users`, `features`, `status`, `remarks`, `create_time`) 
VALUES ('VERITYX-ENTERPRISE-UNLIMITED', '企业版', 'VerityX企业用户', 'VerityX Inc.', '2024-03-15 00:00:00', NULL, NULL, '{"ui_test": true, "api_test": true, "performance_test": true, "mobile_test": true, "integration": true, "advanced_reporting": true}', '有效', '企业无限制版本，支持所有功能特性，无用户数限制，永久有效', NOW());

-- 系统升级记录
INSERT INTO `sys_upgrade_record` (`from_version`, `to_version`, `upgrade_time`, `upgrade_user`, `status`, `error_message`, `backup_id`, `duration`, `remarks`, `create_time`) 
VALUES ('1.0.0', '1.1.0', '2023-11-15 10:30:00', 'admin', '成功', NULL, 1, 120, '正常升级，无异常', NOW());

INSERT INTO `sys_upgrade_record` (`from_version`, `to_version`, `upgrade_time`, `upgrade_user`, `status`, `error_message`, `backup_id`, `duration`, `remarks`, `create_time`) 
VALUES ('1.1.0', '1.2.0', '2024-01-10 14:45:00', 'admin', '成功', NULL, 2, 180, '更新数据结构，迁移测试数据', NOW());

INSERT INTO `sys_upgrade_record` (`from_version`, `to_version`, `upgrade_time`, `upgrade_user`, `status`, `error_message`, `backup_id`, `duration`, `remarks`, `create_time`) 
VALUES ('1.2.0', '1.3.0', '2024-03-20 09:15:00', 'admin', '成功', NULL, 3, 210, '重大版本升级，增加了新功能模块', NOW());

-- 系统基本信息
INSERT INTO `sys_about` (`system_name`, `system_logo`, `company_name`, `company_website`, `contact_email`, `copyright`, `description`, `terms_of_service`, `privacy_policy`, `create_time`) 
VALUES (
  'VerityX测试平台', 
  '/assets/images/logo.png', 
  'VerityX科技有限公司', 
  'https://www.verityx.com', 
  'support@verityx.com', 
  'Copyright © 2024 VerityX科技有限公司. 保留所有权利。', 
  'VerityX是一个全功能自动化测试平台，专为测试团队设计，支持UI测试、API测试和性能测试。提供丰富的测试管理功能，帮助团队提高测试效率和质量。',
  '## VerityX服务条款\n\n### 1. 接受条款\n使用VerityX测试平台服务，表示您同意受本服务条款的约束。如果您不接受这些条款，请勿使用我们的服务。\n\n### 2. 服务描述\nVerityX提供自动化测试平台服务，允许用户创建、管理和执行测试用例。我们可能会不时更改、暂停或终止服务的部分或全部。\n\n### 3. 用户账户\n您负责维护账户安全，并对所有使用您账户的活动负责。\n\n### 4. 用户行为\n您同意不会使用服务进行任何非法或未经授权的活动。\n\n### 5. 知识产权\nVerityX及其许可方拥有服务的所有权利和知识产权。\n\n### 6. 免责声明\n服务按"现状"提供，不提供任何明示或暗示的保证。\n\n### 7. 责任限制\n在法律允许的最大范围内，VerityX不对任何损失或损害承担责任。\n\n### 8. 条款变更\n我们可能会修改这些条款，并将在服务中发布通知。继续使用服务表示您接受修改后的条款。\n\n### 9. 管辖法律\n本条款受中国法律管辖并按其解释，不考虑冲突法原则。',
  '## VerityX隐私政策\n\n### 1. 信息收集\n我们收集您提供给我们的信息，如注册信息、测试数据等，以及使用我们服务时自动生成的信息。\n\n### 2. 信息使用\n我们使用收集的信息来提供、维护和改进我们的服务，以及开发新服务。\n\n### 3. 信息共享\n除非有您的同意，或法律要求，或以下情况，我们不会与第三方共享您的个人信息：\n- 与我们的服务提供商共享\n- 出于业务转让目的\n- 保护VerityX的权利和财产\n\n### 4. 数据安全\n我们采取合理措施保护您的信息不被未经授权的访问或披露。\n\n### 5. 数据存储\n您的数据存储在您选择的区域内的安全服务器上。\n\n### 6. 您的权利\n您可以访问、更正或删除您的个人信息。\n\n### 7. Cookie使用\n我们使用Cookie和类似技术来提供和支持我们的服务。\n\n### 8. 隐私政策变更\n我们可能会不时更新此隐私政策，并通过服务通知您任何变更。\n\n### 9. 联系我们\n如果您对此隐私政策有任何问题，请联系我们：support@verityx.com',
  NOW()
); 