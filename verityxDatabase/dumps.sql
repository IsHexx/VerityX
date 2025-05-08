-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: verityx
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `api_automation`
--

DROP TABLE IF EXISTS `api_automation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `api_automation` (
  `scene_id` int NOT NULL AUTO_INCREMENT COMMENT '场景ID，自增主键',
  `directory` varchar(255) NOT NULL COMMENT '所属目录',
  `scene_name` varchar(255) NOT NULL COMMENT '场景名称',
  `step_method` varchar(255) NOT NULL COMMENT '步骤方法',
  `step_name` varchar(255) NOT NULL COMMENT '步骤名称',
  `remark` text COMMENT '备注',
  `project_id` int NOT NULL,
  PRIMARY KEY (`scene_id`),
  UNIQUE KEY `scene_id` (`scene_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='接口自动化表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_automation`
--

LOCK TABLES `api_automation` WRITE;
/*!40000 ALTER TABLE `api_automation` DISABLE KEYS */;
/*!40000 ALTER TABLE `api_automation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_environment`
--

DROP TABLE IF EXISTS `api_environment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `api_environment` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `env_name` varchar(100) NOT NULL COMMENT '环境名称：开发环境、测试环境、生产环境等',
  `base_url` varchar(255) NOT NULL COMMENT '环境基础URL，如：https://dev-api.example.com',
  `description` varchar(500) DEFAULT NULL COMMENT '环境描述',
  `is_default` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为默认环境：0-否，1-是',
  `global_headers` text COMMENT '全局请求头，JSON格式',
  `global_params` text COMMENT '全局参数，JSON格式',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_env_name` (`env_name`),
  KEY `idx_is_default` (`is_default`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='API环境配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_environment`
--

LOCK TABLES `api_environment` WRITE;
/*!40000 ALTER TABLE `api_environment` DISABLE KEYS */;
INSERT INTO `api_environment` (`id`, `env_name`, `base_url`, `description`, `is_default`, `global_headers`, `global_params`, `create_time`, `update_time`) VALUES (1,'开发环境','http://localhost:8089','本地开发测试环境',1,'{\"Content-Type\":\"application/json\"}','{\"version\":\"v1\",\"debug\":\"true\"}','2025-04-29 16:55:58','2025-04-29 16:55:58'),(2,'测试环境','http://test-api.example.com','供测试人员使用的环境',0,'{\"Content-Type\":\"application/json\",\"X-API-Key\":\"test-api-key\"}','{\"version\":\"v1\"}','2025-04-29 16:55:58','2025-04-29 16:55:58'),(3,'生产环境','https://api.example.com','生产正式环境',0,'{\"Content-Type\":\"application/json\",\"X-API-Key\":\"prod-api-key\"}','{\"version\":\"v1\"}','2025-04-29 16:55:58','2025-04-29 16:55:58'),(6,'预发布环境','http://staging-api.example.com','用于预发布阶段的API测试环境',0,'{\"Content-Type\":\"application/json\",\"Authorization\":\"Bearer staging-token\"}','{\"version\":\"v1\",\"debug\":\"true\"}','2025-04-29 16:59:05','2025-04-29 16:59:05');
/*!40000 ALTER TABLE `api_environment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_management`
--

DROP TABLE IF EXISTS `api_management`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `api_management` (
  `api_id` int NOT NULL AUTO_INCREMENT COMMENT '接口ID，自增主键',
  `api_directory` varchar(255) NOT NULL COMMENT '接口目录',
  `api_name` varchar(255) NOT NULL COMMENT '接口名称',
  `related_test_cases` text COMMENT '关联接口用例ID列表',
  `request_method` varchar(50) NOT NULL COMMENT '请求方法（例如：GET, POST）',
  `api_path` varchar(255) NOT NULL COMMENT '接口路径',
  `status` varchar(50) NOT NULL COMMENT '接口状态（例如：active, inactive）',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `mock_url` varchar(255) DEFAULT NULL COMMENT 'MOCK地址',
  `response_body` text COMMENT '返回Body示例',
  `project_id` int NOT NULL,
  PRIMARY KEY (`api_id`),
  UNIQUE KEY `api_id` (`api_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='接口管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_management`
--

LOCK TABLES `api_management` WRITE;
/*!40000 ALTER TABLE `api_management` DISABLE KEYS */;
INSERT INTO `api_management` (`api_id`, `api_directory`, `api_name`, `related_test_cases`, `request_method`, `api_path`, `status`, `created_at`, `updated_at`, `mock_url`, `response_body`, `project_id`) VALUES (1,'用户管理','获取用户信息','1001,1002','GET','/user/info','active','2024-11-28 01:44:55','2024-11-28 01:44:55','http://mock.example.com/user/info','{\"user_id\": 1, \"username\": \"张三\"}',1),(2,'订单管理','创建订单','1003,1004','POST','/order/create','active','2024-11-28 01:44:55','2024-11-28 01:44:55','http://mock.example.com/order/create','{\"order_id\": 123, \"status\": \"created\"}',1),(3,'商品管理','查询商品列表','1005,1006','GET','/product/list','active','2024-11-28 01:44:55','2024-11-28 01:44:55','http://mock.example.com/product/list','[{\"product_id\": 1, \"name\": \"商品1\"}, {\"product_id\": 2, \"name\": \"商品2\"}]',2),(4,'支付管理','支付订单','1007,1008','POST','/payment/pay','inactive','2024-11-28 01:44:55','2024-11-28 01:44:55','http://mock.example.com/payment/pay','{\"payment_id\": 456, \"status\": \"paid\"}',2),(5,'消息管理','发送消息','1009,1010','POST','/message/send','active','2024-11-28 01:44:55','2024-11-28 01:44:55','http://mock.example.com/message/send','{\"message_id\": 789, \"content\": \"Hello, World!\"}',3),(6,'文件管理','上传文件','1011,1012','POST','/file/upload','active','2024-11-28 01:44:55','2024-11-28 01:44:55','http://mock.example.com/file/upload','{\"file_id\": 101, \"filename\": \"example.pdf\"}',3),(7,'权限管理','获取权限列表','1013,1014','GET','/permission/list','inactive','2024-11-28 01:44:55','2024-11-28 01:44:55','http://mock.example.com/permission/list','[{\"permission_id\": 1, \"name\": \"查看\"}]',4),(8,'角色管理','创建角色','1015,1016','POST','/role/create','active','2024-11-28 01:44:55','2024-11-28 01:44:55','http://mock.example.com/role/create','{\"role_id\": 202, \"name\": \"管理员\"}',4),(9,'日志管理','查询日志','1017,1018','GET','/log/query','active','2024-11-28 01:44:55','2024-11-28 01:44:55','http://mock.example.com/log/query','{\"log_id\": 303, \"content\": \"操作日志\"}',5),(10,'设置管理','更新设置','1019,1020','POST','/setting/update','inactive','2024-11-28 01:44:55','2024-11-28 01:44:55','http://mock.example.com/setting/update','{\"setting_id\": 404, \"value\": \"新设置\"}',5);
/*!40000 ALTER TABLE `api_management` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_query_params`
--

DROP TABLE IF EXISTS `api_query_params`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `api_query_params` (
  `param_id` int NOT NULL AUTO_INCREMENT COMMENT '查询参数ID，自增主键',
  `api_id` int NOT NULL COMMENT '关联接口ID',
  `parameter` varchar(255) NOT NULL COMMENT '查询参数名称',
  `param_type` varchar(50) NOT NULL COMMENT '参数类型（例如：string, int）',
  `description` text COMMENT '参数说明',
  `project_id` int NOT NULL,
  PRIMARY KEY (`param_id`),
  UNIQUE KEY `param_id` (`param_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='接口查询参数表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_query_params`
--

LOCK TABLES `api_query_params` WRITE;
/*!40000 ALTER TABLE `api_query_params` DISABLE KEYS */;
/*!40000 ALTER TABLE `api_query_params` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_request_body`
--

DROP TABLE IF EXISTS `api_request_body`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `api_request_body` (
  `body_id` int NOT NULL AUTO_INCREMENT COMMENT '请求Body ID，自增主键',
  `api_id` int NOT NULL COMMENT '关联接口ID',
  `parameter` varchar(255) NOT NULL COMMENT '请求Body参数名称',
  `description` text COMMENT '参数说明',
  `project_id` int NOT NULL,
  PRIMARY KEY (`body_id`),
  UNIQUE KEY `body_id` (`body_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='接口请求Body表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_request_body`
--

LOCK TABLES `api_request_body` WRITE;
/*!40000 ALTER TABLE `api_request_body` DISABLE KEYS */;
/*!40000 ALTER TABLE `api_request_body` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_test_report`
--

DROP TABLE IF EXISTS `api_test_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `api_test_report` (
  `id` int NOT NULL AUTO_INCREMENT,
  `api_name` varchar(255) NOT NULL COMMENT '接口名称',
  `url` varchar(255) NOT NULL COMMENT '接口地址',
  `method` varchar(20) NOT NULL COMMENT '请求方法',
  `status` varchar(20) NOT NULL COMMENT '状态(success/failed)',
  `response_time` int DEFAULT NULL COMMENT '响应时间(ms)',
  `execution_time` datetime NOT NULL COMMENT '执行时间',
  `executor` varchar(50) DEFAULT NULL COMMENT '执行人',
  `headers` text COMMENT '请求头(JSON格式)',
  `params` text COMMENT '请求参数(JSON格式)',
  `response` text COMMENT '响应结果(JSON格式)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='API测试报告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_test_report`
--

LOCK TABLES `api_test_report` WRITE;
/*!40000 ALTER TABLE `api_test_report` DISABLE KEYS */;
INSERT INTO `api_test_report` (`id`, `api_name`, `url`, `method`, `status`, `response_time`, `execution_time`, `executor`, `headers`, `params`, `response`, `create_time`, `update_time`) VALUES (1,'用户登录API','http://localhost:8089/api/user/login','POST','成功',120,'2023-05-15 02:30:00','测试人员1','{\"Content-Type\": \"application/json\"}','{\"username\": \"admin\", \"password\": \"123456\"}','{\"code\": 200, \"message\": \"登录成功\", \"data\": {\"token\": \"xxx\"}}','2025-04-29 14:38:09','2025-04-29 14:38:09'),(2,'用户登录API','http://localhost:8089/api/user/login','POST','成功',120,'2023-05-15 02:30:00','测试人员1','{\"Content-Type\": \"application/json\"}','{\"username\": \"admin\", \"password\": \"123456\"}','{\"code\": 200, \"message\": \"登录成功\", \"data\": {\"token\": \"xxx\"}}','2025-04-29 16:14:25','2025-04-29 16:14:25'),(3,'测试API','http://localhost:8089/','POST','成功',120,'2023-05-15 02:30:00','测试人员1','{\"Content-Type\": \"application/json\"}','{\"username\": \"admin\", \"password\": \"123456\"}','{\"code\": 200, \"message\": \"登录成功\", \"data\": {\"token\": \"xxx\"}}','2025-04-29 16:51:30','2025-04-29 16:51:30');
/*!40000 ALTER TABLE `api_test_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bug_management`
--

DROP TABLE IF EXISTS `bug_management`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bug_management` (
  `bug_id` int NOT NULL AUTO_INCREMENT COMMENT '缺陷ID，自增主键',
  `bug_title` varchar(255) NOT NULL COMMENT 'Bug标题',
  `level` varchar(50) NOT NULL COMMENT 'Bug级别（例如：低、中、高）',
  `severity` varchar(50) NOT NULL COMMENT '缺陷严重级别（例如：轻微、严重、致命）',
  `created_by` varchar(10) NOT NULL COMMENT '创建人（用户ID）',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `assigned_to` int DEFAULT NULL COMMENT '指派给（用户ID）',
  `bug_type` varchar(50) NOT NULL COMMENT 'Bug类型（例如：功能、性能、UI）',
  `status` varchar(50) NOT NULL COMMENT '状态（例如：open、in progress、closed）',
  `plan_id` int DEFAULT NULL COMMENT '所属测试计划ID',
  `project_id` int DEFAULT NULL COMMENT '所属项目ID',
  `operating_system` varchar(50) DEFAULT NULL COMMENT '操作系统',
  `browser` varchar(50) DEFAULT NULL COMMENT '浏览器',
  `reproduction_steps` text COMMENT '重现步骤',
  `attachments` text COMMENT '附件链接或路径',
  PRIMARY KEY (`bug_id`),
  UNIQUE KEY `bug_id` (`bug_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='缺陷管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bug_management`
--

LOCK TABLES `bug_management` WRITE;
/*!40000 ALTER TABLE `bug_management` DISABLE KEYS */;
INSERT INTO `bug_management` (`bug_id`, `bug_title`, `level`, `severity`, `created_by`, `created_at`, `assigned_to`, `bug_type`, `status`, `plan_id`, `project_id`, `operating_system`, `browser`, `reproduction_steps`, `attachments`) VALUES (1,'Bug1','1','阻塞','11','2024-11-26 09:59:06',NULL,'功能问题','已修复',NULL,1,NULL,NULL,NULL,NULL),(2,'Bug2','2','严重','11','2024-11-26 09:59:06',NULL,'性能问题','已确认',NULL,1,NULL,NULL,NULL,NULL),(3,'Bug3','3','一般','11','2024-11-26 09:59:06',NULL,'功能问题','已确认',NULL,1,NULL,NULL,NULL,NULL),(4,'Bug4','4','轻微','11','2024-11-26 09:59:06',NULL,'功能问题','已确认',NULL,1,NULL,NULL,NULL,NULL),(7,'Bug7','3','一般','11','2024-11-26 09:59:06',NULL,'接口问题','已确认',NULL,1,NULL,NULL,NULL,NULL),(8,'Bug8','4','轻微','11','2024-11-26 09:59:06',NULL,'功能问题','已确认',NULL,1,NULL,NULL,NULL,NULL),(9,'Bug9','1','严重','11','2024-11-26 09:59:06',NULL,'功能问题','已确认',NULL,1,NULL,NULL,NULL,NULL),(10,'Bug10','1','阻塞','11','2024-11-26 09:59:06',NULL,'安全问题','已确认',NULL,1,NULL,NULL,NULL,NULL),(11,'Bug11','1','阻塞','11','2024-11-26 09:59:06',NULL,'功能问题','待修复',NULL,1,NULL,NULL,NULL,NULL),(12,'阿斯顿','priority1','阻塞','阿达','2024-11-26 08:00:00',NULL,'兼容性问题','已创建',222,NULL,NULL,NULL,NULL,NULL),(13,'缺陷新增测试1','priority1','阻塞','张武','2024-11-26 08:00:00',NULL,'功能问题','已创建',222,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `bug_management` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project` (
  `project_id` int NOT NULL AUTO_INCREMENT COMMENT '项目ID，自增主键',
  `project_name` varchar(255) NOT NULL COMMENT '项目名称',
  `description` text COMMENT '项目描述',
  `start_date` date NOT NULL COMMENT '项目开始日期',
  `end_date` date DEFAULT NULL COMMENT '项目结束日期',
  `status` varchar(50) NOT NULL COMMENT '项目状态（例如：pending、ongoing、completed）',
  PRIMARY KEY (`project_id`),
  UNIQUE KEY `project_id` (`project_id`),
  UNIQUE KEY `project_name` (`project_name`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='项目表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO `project` (`project_id`, `project_name`, `description`, `start_date`, `end_date`, `status`) VALUES (1,'项目A','这是一个关于人工智能的研究项目。','2023-01-01','2023-06-30','completed'),(2,'项目B','开发一个全新的电子商务平台。','2023-02-15','2023-12-31','ongoing'),(3,'项目C','研究可持续能源解决方案。','2023-03-10',NULL,'pending'),(4,'项目D','设计一款移动支付应用。','2023-04-05','2023-11-15','ongoing'),(5,'项目E','创建一个虚拟现实游戏。','2023-05-20','2024-02-28','pending'),(6,'项目F','开发一个智能家庭管理系统。','2023-06-01','2023-12-31','completed'),(7,'项目G','研究量子计算的应用。','2023-07-15',NULL,'completed'),(8,'项目H','建立一个大数据分析平台。','2023-08-10','2024-02-01','ongoing'),(12,'测试项目21','测试项目21','2024-11-20','2024-11-21','completed');
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dept`
--

DROP TABLE IF EXISTS `sys_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dept` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `dept_name` varchar(100) NOT NULL COMMENT '部门名称',
  `dept_code` varchar(50) DEFAULT NULL COMMENT '部门编码',
  `parent_id` bigint DEFAULT NULL COMMENT '父部门ID',
  `sort` int DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(50) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `is_active` tinyint(1) DEFAULT '1' COMMENT '部门状态（0停用 1正常）',
  `description` varchar(500) DEFAULT NULL COMMENT '部门描述',
  `created_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_dept_code` (`dept_code`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept`
--

LOCK TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT INTO `sys_dept` (`id`, `dept_name`, `dept_code`, `parent_id`, `sort`, `leader`, `phone`, `email`, `is_active`, `description`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (1,'系统管理部','ADMIN',NULL,1,'Admin',NULL,NULL,1,'系统管理部门',1,'2025-05-07 10:38:06',NULL,NULL);
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_permission`
--

DROP TABLE IF EXISTS `sys_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `permission_name` varchar(100) NOT NULL COMMENT '权限名称',
  `permission_code` varchar(100) NOT NULL COMMENT '权限编码',
  `permission_type` varchar(20) NOT NULL COMMENT '权限类型：menu菜单、button按钮、api接口',
  `parent_id` bigint DEFAULT NULL COMMENT '父级权限ID',
  `permission_path` varchar(255) DEFAULT NULL COMMENT '权限路径',
  `component` varchar(255) DEFAULT NULL COMMENT '权限组件',
  `icon` varchar(100) DEFAULT NULL COMMENT '权限图标',
  `sort` int DEFAULT '0' COMMENT '显示顺序',
  `is_active` tinyint(1) DEFAULT '1' COMMENT '状态：0禁用、1启用',
  `is_visible` tinyint(1) DEFAULT '1' COMMENT '是否显示：0隐藏、1显示',
  `is_system` tinyint(1) DEFAULT '0' COMMENT '是否系统内置',
  `description` varchar(500) DEFAULT NULL COMMENT '权限描述',
  `created_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_code` (`permission_code`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_permission_type` (`permission_type`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_permission`
--

LOCK TABLES `sys_permission` WRITE;
/*!40000 ALTER TABLE `sys_permission` DISABLE KEYS */;
INSERT INTO `sys_permission` (`id`, `permission_name`, `permission_code`, `permission_type`, `parent_id`, `permission_path`, `component`, `icon`, `sort`, `is_active`, `is_visible`, `is_system`, `description`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (1,'系统设置','system:settings','menu',NULL,'/system',NULL,'setting',100,1,1,1,'系统设置菜单',1,'2025-05-07 10:38:06',NULL,NULL),(2,'用户管理','system:user','menu',1,'/system/user','system/user/index','user',1,1,1,1,'用户管理菜单',1,'2025-05-07 10:38:06',NULL,NULL),(3,'用户查询','system:user:list','button',2,NULL,NULL,NULL,1,1,1,1,'用户查询按钮',1,'2025-05-07 10:38:06',NULL,NULL),(4,'用户新增','system:user:add','button',2,NULL,NULL,NULL,2,1,1,1,'用户新增按钮',1,'2025-05-07 10:38:06',NULL,NULL),(5,'用户修改','system:user:edit','button',2,NULL,NULL,NULL,3,1,1,1,'用户修改按钮',1,'2025-05-07 10:38:06',NULL,NULL),(6,'用户删除','system:user:delete','button',2,NULL,NULL,NULL,4,1,1,1,'用户删除按钮',1,'2025-05-07 10:38:06',NULL,NULL),(7,'重置密码','system:user:reset','button',2,NULL,NULL,NULL,5,1,1,1,'重置密码按钮',1,'2025-05-07 10:38:06',NULL,NULL),(8,'角色管理','system:role','menu',1,'/system/role','system/role/index','peoples',2,1,1,1,'角色管理菜单',1,'2025-05-07 10:38:06',NULL,NULL),(9,'角色查询','system:role:list','button',8,NULL,NULL,NULL,1,1,1,1,'角色查询按钮',1,'2025-05-07 10:38:06',NULL,NULL),(10,'角色新增','system:role:add','button',8,NULL,NULL,NULL,2,1,1,1,'角色新增按钮',1,'2025-05-07 10:38:06',NULL,NULL),(11,'角色修改','system:role:edit','button',8,NULL,NULL,NULL,3,1,1,1,'角色修改按钮',1,'2025-05-07 10:38:06',NULL,NULL),(12,'角色删除','system:role:delete','button',8,NULL,NULL,NULL,4,1,1,1,'角色删除按钮',1,'2025-05-07 10:38:06',NULL,NULL),(13,'角色授权','system:role:authorize','button',8,NULL,NULL,NULL,5,1,1,1,'角色授权按钮',1,'2025-05-07 10:38:06',NULL,NULL),(14,'权限管理','system:permission','menu',1,'/system/permission','system/permission/index','tree',3,1,1,1,'权限管理菜单',1,'2025-05-07 10:38:06',NULL,NULL),(15,'权限查询','system:permission:list','button',14,NULL,NULL,NULL,1,1,1,1,'权限查询按钮',1,'2025-05-07 10:38:06',NULL,NULL),(16,'权限新增','system:permission:add','button',14,NULL,NULL,NULL,2,1,1,1,'权限新增按钮',1,'2025-05-07 10:38:06',NULL,NULL),(17,'权限修改','system:permission:edit','button',14,NULL,NULL,NULL,3,1,1,1,'权限修改按钮',1,'2025-05-07 10:38:06',NULL,NULL),(18,'权限删除','system:permission:delete','button',14,NULL,NULL,NULL,4,1,1,1,'权限删除按钮',1,'2025-05-07 10:38:06',NULL,NULL),(19,'部门管理','system:dept','menu',1,'/system/dept','system/dept/index','tree-table',4,1,1,1,'部门管理菜单',1,'2025-05-07 10:38:06',NULL,NULL),(20,'部门查询','system:dept:list','button',19,NULL,NULL,NULL,1,1,1,1,'部门查询按钮',1,'2025-05-07 10:38:06',NULL,NULL),(21,'部门新增','system:dept:add','button',19,NULL,NULL,NULL,2,1,1,1,'部门新增按钮',1,'2025-05-07 10:38:06',NULL,NULL),(22,'部门修改','system:dept:edit','button',19,NULL,NULL,NULL,3,1,1,1,'部门修改按钮',1,'2025-05-07 10:38:06',NULL,NULL),(23,'部门删除','system:dept:delete','button',19,NULL,NULL,NULL,4,1,1,1,'部门删除按钮',1,'2025-05-07 10:38:06',NULL,NULL);
/*!40000 ALTER TABLE `sys_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称',
  `role_code` varchar(50) NOT NULL COMMENT '角色编码',
  `description` varchar(500) DEFAULT NULL COMMENT '角色描述',
  `is_system` tinyint(1) DEFAULT '0' COMMENT '是否系统内置',
  `is_active` tinyint(1) DEFAULT '1' COMMENT '是否启用',
  `sort` int DEFAULT '0' COMMENT '显示顺序',
  `created_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `description`, `is_system`, `is_active`, `sort`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (1,'超级管理员','SUPER_ADMIN','系统超级管理员，拥有所有权限',1,1,1,1,'2025-05-07 10:38:06',NULL,NULL);
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_permission`
--

DROP TABLE IF EXISTS `sys_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `permission_id` bigint NOT NULL COMMENT '权限ID',
  `created_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`,`permission_id`),
  KEY `idx_permission_id` (`permission_id`),
  CONSTRAINT `fk_rp_permission_id` FOREIGN KEY (`permission_id`) REFERENCES `sys_permission` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_rp_role_id` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=131 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色权限关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_permission`
--

LOCK TABLES `sys_role_permission` WRITE;
/*!40000 ALTER TABLE `sys_role_permission` DISABLE KEYS */;
INSERT INTO `sys_role_permission` (`id`, `role_id`, `permission_id`, `created_by`, `created_at`) VALUES (100,1,1,1,'2025-05-07 10:38:06'),(101,1,2,1,'2025-05-07 10:38:06'),(102,1,8,1,'2025-05-07 10:38:06'),(103,1,14,1,'2025-05-07 10:38:06'),(104,1,19,1,'2025-05-07 10:38:06'),(105,1,3,1,'2025-05-07 10:38:06'),(106,1,4,1,'2025-05-07 10:38:06'),(107,1,5,1,'2025-05-07 10:38:06'),(108,1,6,1,'2025-05-07 10:38:06'),(109,1,7,1,'2025-05-07 10:38:06'),(110,1,9,1,'2025-05-07 10:38:06'),(111,1,10,1,'2025-05-07 10:38:06'),(112,1,11,1,'2025-05-07 10:38:06'),(113,1,12,1,'2025-05-07 10:38:06'),(114,1,13,1,'2025-05-07 10:38:06'),(115,1,15,1,'2025-05-07 10:38:06'),(116,1,16,1,'2025-05-07 10:38:06'),(117,1,17,1,'2025-05-07 10:38:06'),(118,1,18,1,'2025-05-07 10:38:06'),(119,1,20,1,'2025-05-07 10:38:06'),(120,1,21,1,'2025-05-07 10:38:06'),(121,1,22,1,'2025-05-07 10:38:06'),(122,1,23,1,'2025-05-07 10:38:06');
/*!40000 ALTER TABLE `sys_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `nickname` varchar(50) DEFAULT NULL COMMENT '用户昵称',
  `gender` tinyint(1) DEFAULT '2' COMMENT '性别（0男 1女 2未知）',
  `email` varchar(100) DEFAULT NULL COMMENT '用户邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像地址',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `is_locked` tinyint(1) DEFAULT '0' COMMENT '是否锁定（0正常 1锁定）',
  `is_active` tinyint(1) DEFAULT '1' COMMENT '是否启用（0禁用 1启用）',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `created_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_dept_id` (`dept_id`),
  CONSTRAINT `fk_user_dept` FOREIGN KEY (`dept_id`) REFERENCES `sys_dept` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `nickname`, `gender`, `email`, `phone`, `avatar`, `dept_id`, `is_locked`, `is_active`, `last_login_ip`, `last_login_time`, `remark`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (1,'admin','$2a$10$uPNjJ05jb1L.dnsMKvYNqe8fcqEGMR0zjY.YzFT2TvA4a9Xl9J3wS','系统管理员','Admin',0,'admin@example.com',NULL,NULL,1,0,1,NULL,NULL,NULL,1,'2025-05-07 10:38:06',NULL,NULL);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `created_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`,`role_id`),
  KEY `idx_role_id` (`role_id`),
  CONSTRAINT `fk_ur_role_id` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_ur_user_id` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `created_by`, `created_at`) VALUES (100,1,1,1,'2025-05-07 10:38:06');
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_case`
--

DROP TABLE IF EXISTS `test_case`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_case` (
  `case_id` int NOT NULL AUTO_INCREMENT COMMENT '用例ID，自增主键',
  `case_title` varchar(255) NOT NULL COMMENT '用例标题',
  `importance_level` varchar(50) NOT NULL COMMENT '用例重要级别（例如：高、中、低）',
  `preconditions` text COMMENT '前置条件',
  `case_status` varchar(10) DEFAULT NULL,
  `created_by` varchar(255) NOT NULL COMMENT '创建人（用户ID）',
  `execution_time` timestamp NULL DEFAULT NULL COMMENT '执行时间',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `last_modified_by` int DEFAULT NULL COMMENT '最后修改者（用户ID）',
  `case_type` varchar(50) NOT NULL COMMENT '用例类型（例如：功能、性能、接口）',
  `descirption` varchar(255) DEFAULT NULL COMMENT '用例详情',
  `execution_result` varchar(50) DEFAULT NULL COMMENT '执行结果（例如：通过、失败、阻塞）',
  `remarks` text COMMENT '备注',
  `attachments` text COMMENT '附件链接或路径',
  `project_id` int NOT NULL,
  PRIMARY KEY (`case_id`),
  UNIQUE KEY `case_id` (`case_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='测试用例表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_case`
--

LOCK TABLES `test_case` WRITE;
/*!40000 ALTER TABLE `test_case` DISABLE KEYS */;
INSERT INTO `test_case` (`case_id`, `case_title`, `importance_level`, `preconditions`, `case_status`, `created_by`, `execution_time`, `created_at`, `last_modified_by`, `case_type`, `descirption`, `execution_result`, `remarks`, `attachments`, `project_id`) VALUES (2,'测试用例1','1','已登录成功','已评审','张三',NULL,'2024-11-25 09:25:10',NULL,'功能测试',NULL,NULL,NULL,NULL,1),(3,'测试用例2','2','已登录成功','未评审','李四',NULL,'2024-11-25 09:25:10',NULL,'功能测试',NULL,NULL,NULL,NULL,1),(4,'测试用例3','3','已登录成功','已评审','王五',NULL,'2024-11-25 09:25:10',NULL,'功能测试',NULL,NULL,NULL,NULL,1),(5,'测试用例4','4','已登录成功','已评审','赵六',NULL,'2024-11-25 09:25:10',NULL,'接口测试',NULL,NULL,NULL,NULL,1),(6,'测试用例5','1','已登录成功','已评审','11',NULL,'2024-11-25 09:25:10',NULL,'性能测试',NULL,NULL,NULL,NULL,1),(7,'测试用例6','1','已登录成功','已评审','11',NULL,'2024-11-25 09:25:10',NULL,'安全测试',NULL,NULL,NULL,NULL,1),(8,'测试用例7','1','已登录成功','待评审','11',NULL,'2024-11-25 09:25:10',NULL,'兼容性测试',NULL,NULL,NULL,NULL,1),(9,'测试用例8','1','已登录成功','待评审','11',NULL,'2024-11-25 09:25:10',NULL,'兼容性测试',NULL,NULL,NULL,NULL,1),(10,'测试用例9','1','已登录成功','待评审','11',NULL,'2024-11-25 09:25:10',NULL,'兼容性测试',NULL,NULL,NULL,NULL,1),(11,'测试用例10','2','已登录成功','待评审','11',NULL,'2024-11-25 09:25:10',NULL,'兼容性测试',NULL,NULL,NULL,NULL,1),(12,'测试用例11','3','已登录成功','待评审','11',NULL,'2024-11-25 09:25:10',NULL,'兼容性测试',NULL,NULL,NULL,NULL,1),(13,'十分的','4',NULL,'待评审','fs',NULL,'2024-11-26 08:00:00',NULL,'功能测试',NULL,NULL,NULL,NULL,1);
/*!40000 ALTER TABLE `test_case` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_plan`
--

DROP TABLE IF EXISTS `test_plan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_plan` (
  `plan_id` int NOT NULL AUTO_INCREMENT COMMENT '测试计划ID，自增主键',
  `plan_name` varchar(255) NOT NULL COMMENT '测试计划名称',
  `description` text COMMENT '测试计划描述',
  `project_id` int NOT NULL COMMENT '关联项目版本',
  `status` varchar(50) NOT NULL COMMENT '测试计划状态（例如：pending、in progress、completed）',
  `participants` varchar(255) DEFAULT NULL COMMENT '参与人（存储用户ID列表，用逗号分隔）',
  `start_date` date DEFAULT NULL COMMENT '测试计划开始日期',
  `end_date` date DEFAULT NULL COMMENT '测试计划结束日期',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `plan_version` varchar(50) NOT NULL COMMENT '计划版本',
  PRIMARY KEY (`plan_id`),
  UNIQUE KEY `plan_id` (`plan_id`),
  UNIQUE KEY `plan_name` (`plan_name`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='测试计划表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_plan`
--

LOCK TABLES `test_plan` WRITE;
/*!40000 ALTER TABLE `test_plan` DISABLE KEYS */;
INSERT INTO `test_plan` (`plan_id`, `plan_name`, `description`, `project_id`, `status`, `participants`, `start_date`, `end_date`, `created_at`, `updated_at`, `plan_version`) VALUES (1,'Plan_1','Description for Plan_1',29,'Active','Participant_1','2024-10-17','2024-11-03','2024-11-14 16:00:00','2024-11-14 16:00:00','v1'),(2,'Plan_2','Description for Plan_222222222222222222',75,'Completed','Participant_2','2024-10-25','2024-11-18','2024-11-14 16:00:00','2024-11-25 07:01:01','v22222222222222'),(9,'测试计划3','',1,'01','',NULL,NULL,'2024-11-25 07:02:48','2024-11-25 07:02:48',''),(10,'测试计划4','',1,'','',NULL,NULL,'2024-11-25 07:02:54','2024-11-25 07:02:54',''),(11,'测试计划5','',1,'','',NULL,NULL,'2024-11-25 07:03:00','2024-11-25 07:03:00',''),(12,'测试计划6','',1,'','',NULL,NULL,'2024-11-25 07:03:05','2024-11-25 07:03:05',''),(13,'测试计划7','',1,'','',NULL,NULL,'2024-11-25 07:03:10','2024-11-25 07:03:10',''),(14,'测试计划8','',1,'','',NULL,NULL,'2024-11-25 07:03:15','2024-11-25 07:03:15',''),(15,'测试计划9','',1,'','',NULL,NULL,'2024-11-25 07:03:19','2024-11-25 07:03:19',''),(17,'测试计划11','',1,'','',NULL,NULL,'2024-11-25 07:03:28','2024-11-25 07:03:28',''),(18,'测试计划12','',1,'','',NULL,NULL,'2024-11-25 07:04:19','2024-11-25 07:04:19',''),(19,'Test Plan 3','This is a description for Test Plan 1.',101,'pending','1,2,3','2024-11-20','2024-11-30','2025-04-28 08:53:27','2025-04-28 08:53:27','v1.0');
/*!40000 ALTER TABLE `test_plan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_report`
--

DROP TABLE IF EXISTS `test_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_report` (
  `report_id` int NOT NULL AUTO_INCREMENT COMMENT '报告ID，自增主键',
  `report_title` varchar(255) NOT NULL COMMENT '报告标题',
  `created_by` int NOT NULL COMMENT '创建人（用户ID）',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `plan_id` int DEFAULT NULL COMMENT '所属测试计划ID',
  `test_version` varchar(50) NOT NULL COMMENT '测试版本',
  `participants` text COMMENT '参与人员',
  `responsible_person` int DEFAULT NULL COMMENT '负责人（用户ID）',
  `summary` text COMMENT '报告概括',
  `conclusion` text COMMENT '测试结论',
  `attachments` text COMMENT '附件链接或路径',
  `related_cases` text COMMENT '关联的用例ID列表',
  `remaining_bugs` text COMMENT '遗留的Bug ID列表',
  `project_id` int NOT NULL,
  PRIMARY KEY (`report_id`),
  UNIQUE KEY `report_id` (`report_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='测试报告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_report`
--

LOCK TABLES `test_report` WRITE;
/*!40000 ALTER TABLE `test_report` DISABLE KEYS */;
INSERT INTO `test_report` (`report_id`, `report_title`, `created_by`, `created_at`, `plan_id`, `test_version`, `participants`, `responsible_person`, `summary`, `conclusion`, `attachments`, `related_cases`, `remaining_bugs`, `project_id`) VALUES (1,'报告标题1',1,'2023-04-01 02:00:00',101,'1.0.0','张三,李四',2,'报告概括1','测试结论1','附件1.pdf','1001,1002','2001,2002',1),(2,'报告标题222222',2,'2023-04-02 03:00:00',102,'1.0.1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,2),(3,'报告标题3',3,'2023-04-03 04:00:00',103,'1.0.2','孙七,周八',4,'报告概括3','测试结论3','附件3.pdf','1005,1006','2005,2006',3),(4,'报告标题4',4,'2023-04-04 05:00:00',104,'1.0.3','吴九,郑十',5,'报告概括4','测试结论4','附件4.pdf','1007,1008','2007,2008',4),(5,'报告标题5',5,'2023-04-05 06:00:00',105,'1.0.4','钱十一,周十二',6,'报告概括5','测试结论5','附件5.pdf','1009,1010','2009,2010',5),(7,'报告标题7',7,'2023-04-07 08:00:00',107,'1.0.6','李十五,周十六',8,'报告概括7','测试结论7','附件7.pdf','1013,1014','2013,2014',7),(8,'报告标题8',8,'2023-04-08 09:00:00',108,'1.0.7','陈十七,吴十八',9,'报告概括8','测试结论8','附件8.pdf','1015,1016','2015,2016',8),(9,'报告标题9',9,'2023-04-09 10:00:00',109,'1.0.8','杨十九,郑二十',10,'报告概括9','测试结论9','附件9.pdf','1017,1018','2017,2018',9),(10,'报告标题10',10,'2023-04-10 11:00:00',110,'1.0.9','刘二十一,王二十二',11,'报告概括10','测试结论10','附件10.pdf','1019,1020','2019,2020',10),(11,'报告标题11',11,'2023-04-11 12:00:00',111,'1.1.0','张二十三,李二十四',12,'报告概括11','测试结论11','附件11.pdf','1021,1022','2021,2022',11),(12,'报告标题12',12,'2023-04-12 13:00:00',112,'1.1.1','王二十五,赵二十六',13,'报告概括12','测试结论12','附件12.pdf','1023,1024','2023,2024',12);
/*!40000 ALTER TABLE `test_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_step`
--

DROP TABLE IF EXISTS `test_step`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_step` (
  `step_id` int NOT NULL AUTO_INCREMENT COMMENT '步骤ID，自增主键',
  `case_id` int NOT NULL COMMENT '关联用例ID',
  `step_description` text NOT NULL COMMENT '用例步骤描述',
  `expected_result` text NOT NULL COMMENT '预期结果',
  `actual_result` text COMMENT '实际结果',
  `project_id` int NOT NULL,
  PRIMARY KEY (`step_id`),
  UNIQUE KEY `step_id` (`step_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='测试步骤表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_step`
--

LOCK TABLES `test_step` WRITE;
/*!40000 ALTER TABLE `test_step` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_step` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ui_element`
--

DROP TABLE IF EXISTS `ui_element`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ui_element` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `element_name` varchar(100) NOT NULL COMMENT '元素名称',
  `group_id` bigint DEFAULT NULL COMMENT '分组ID',
  `locator_type` varchar(50) NOT NULL COMMENT '定位器类型 (ID、NAME、XPATH、CSS等)',
  `locator_value` varchar(500) NOT NULL COMMENT '定位器值',
  `description` varchar(500) DEFAULT NULL COMMENT '元素描述',
  `screenshot_path` varchar(255) DEFAULT NULL COMMENT '截图路径',
  `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_ui_element_group_id` (`group_id`),
  KEY `idx_ui_element_name` (`element_name`),
  KEY `idx_ui_element_locator_type` (`locator_type`),
  CONSTRAINT `ui_element_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `ui_element_group` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='UI元素表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ui_element`
--

LOCK TABLES `ui_element` WRITE;
/*!40000 ALTER TABLE `ui_element` DISABLE KEYS */;
INSERT INTO `ui_element` (`id`, `element_name`, `group_id`, `locator_type`, `locator_value`, `description`, `screenshot_path`, `created_by`, `created_at`, `updated_at`) VALUES (3,'tdfgd',1,'CSS','ngghg',NULL,NULL,'当前用户','2025-05-06 06:14:01',NULL);
/*!40000 ALTER TABLE `ui_element` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ui_element_group`
--

DROP TABLE IF EXISTS `ui_element_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ui_element_group` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `group_name` varchar(100) NOT NULL COMMENT '分组名称',
  `description` varchar(500) DEFAULT NULL COMMENT '分组描述',
  `element_count` int DEFAULT '0' COMMENT '元素数量',
  `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='UI元素分组表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ui_element_group`
--

LOCK TABLES `ui_element_group` WRITE;
/*!40000 ALTER TABLE `ui_element_group` DISABLE KEYS */;
INSERT INTO `ui_element_group` (`id`, `group_name`, `description`, `element_count`, `created_by`, `created_at`, `updated_at`) VALUES (1,'sdfsf','',1,'当前用户','2025-05-06 06:05:39','2025-05-06 14:14:09');
/*!40000 ALTER TABLE `ui_element_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ui_test_case`
--

DROP TABLE IF EXISTS `ui_test_case`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ui_test_case` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'UI测试用例ID，自增主键',
  `case_id` varchar(50) NOT NULL COMMENT '用例编号，如UI-001',
  `case_title` varchar(255) NOT NULL COMMENT '用例标题',
  `project_id` int NOT NULL COMMENT '所属项目ID',
  `importance_level` varchar(50) NOT NULL COMMENT '用例优先级，如P0、P1、P2、P3',
  `environment` varchar(100) NOT NULL COMMENT '测试环境，如Chrome、Firefox、Edge、Safari',
  `precondition` text COMMENT '前置条件',
  `description` text COMMENT '用例描述',
  `timeout` int NOT NULL DEFAULT '30' COMMENT '超时时间(秒)',
  `retry_count` int NOT NULL DEFAULT '1' COMMENT '失败重试次数',
  `screenshot_policy` varchar(50) NOT NULL DEFAULT 'failureOnly' COMMENT '截图策略：failureOnly(仅失败时)、everyStep(每个步骤)、none(不截图)',
  `case_status` varchar(50) NOT NULL DEFAULT '未执行' COMMENT '执行状态：已执行、未执行、执行中',
  `last_result` varchar(50) DEFAULT '未执行' COMMENT '最后执行结果：通过、失败、部分通过、未执行、执行中',
  `last_run_at` timestamp NULL DEFAULT NULL COMMENT '最后执行时间',
  `created_by` varchar(255) NOT NULL COMMENT '创建人',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ui_test_case_id` (`id`),
  UNIQUE KEY `ui_test_case_case_id` (`case_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='UI测试用例表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ui_test_case`
--

LOCK TABLES `ui_test_case` WRITE;
/*!40000 ALTER TABLE `ui_test_case` DISABLE KEYS */;
INSERT INTO `ui_test_case` (`id`, `case_id`, `case_title`, `project_id`, `importance_level`, `environment`, `precondition`, `description`, `timeout`, `retry_count`, `screenshot_policy`, `case_status`, `last_result`, `last_run_at`, `created_by`, `created_at`, `updated_at`) VALUES (1,'UI-001','用户登录功能测试',1,'P1','Chrome','系统已部署且可访问','验证用户使用有效凭据可以成功登录系统',30,1,'failureOnly','已执行','通过','2025-05-06 02:20:35','admin','2025-04-30 07:17:22','2025-05-06 02:20:35');
/*!40000 ALTER TABLE `ui_test_case` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ui_test_config`
--

DROP TABLE IF EXISTS `ui_test_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ui_test_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_name` varchar(100) NOT NULL COMMENT '配置名称',
  `config_type` varchar(20) NOT NULL COMMENT '配置类型: BROWSER/WAIT_TIME/SCREENSHOT/RETRY',
  `browser_type` varchar(20) DEFAULT NULL COMMENT '浏览器类型: CHROME/FIREFOX/EDGE/SAFARI',
  `browser_version` varchar(20) DEFAULT NULL COMMENT '浏览器版本',
  `device_type` varchar(20) DEFAULT NULL COMMENT '设备类型: DESKTOP/MOBILE/TABLET',
  `device_name` varchar(50) DEFAULT NULL COMMENT '设备名称',
  `platform_name` varchar(20) DEFAULT NULL COMMENT '平台名称: WINDOWS/MAC/LINUX/ANDROID/IOS',
  `platform_version` varchar(20) DEFAULT NULL COMMENT '平台版本',
  `default_wait_timeout` int DEFAULT NULL COMMENT '默认等待超时时间（秒）',
  `page_load_timeout` int DEFAULT NULL COMMENT '页面加载超时时间（秒）',
  `script_timeout` int DEFAULT NULL COMMENT '脚本执行超时时间（秒）',
  `screenshot_policy` varchar(20) DEFAULT NULL COMMENT '截图策略: ALWAYS/ON_FAILURE/NEVER',
  `retry_count` int DEFAULT NULL COMMENT '失败重试次数',
  `retry_interval` int DEFAULT NULL COMMENT '重试间隔（秒）',
  `headless` tinyint(1) DEFAULT '0' COMMENT '是否使用无头模式',
  `proxy_settings` text COMMENT '代理设置，JSON格式',
  `capabilities` text COMMENT '自定义功能，JSON格式',
  `is_default` tinyint(1) DEFAULT '0' COMMENT '是否为默认配置',
  `is_active` tinyint(1) DEFAULT '1' COMMENT '是否激活',
  `created_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_config_type` (`config_type`),
  KEY `idx_is_default` (`is_default`),
  KEY `idx_is_active` (`is_active`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='UI测试配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ui_test_config`
--

LOCK TABLES `ui_test_config` WRITE;
/*!40000 ALTER TABLE `ui_test_config` DISABLE KEYS */;
INSERT INTO `ui_test_config` (`id`, `config_name`, `config_type`, `browser_type`, `browser_version`, `device_type`, `device_name`, `platform_name`, `platform_version`, `default_wait_timeout`, `page_load_timeout`, `script_timeout`, `screenshot_policy`, `retry_count`, `retry_interval`, `headless`, `proxy_settings`, `capabilities`, `is_default`, `is_active`, `created_by`, `created_at`, `updated_by`, `updated_at`) VALUES (1,'默认Chrome配置','BROWSER','CHROME','latest','DESKTOP',NULL,'WINDOWS',NULL,10,30,30,NULL,NULL,NULL,0,NULL,NULL,1,1,NULL,'2025-05-06 17:19:18',NULL,'2025-05-06 17:19:18'),(2,'默认等待时间配置','WAIT_TIME',NULL,NULL,NULL,NULL,NULL,NULL,10,30,30,NULL,NULL,NULL,0,NULL,NULL,1,1,NULL,'2025-05-06 17:19:18',NULL,'2025-05-06 17:19:18'),(3,'默认截图策略配置','SCREENSHOT',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'ON_FAILURE',NULL,NULL,0,NULL,NULL,1,1,NULL,'2025-05-06 17:19:18',NULL,'2025-05-06 17:19:18'),(4,'默认重试配置','RETRY',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,5,0,NULL,NULL,1,1,NULL,'2025-05-06 17:19:18',NULL,'2025-05-06 17:19:18'),(5,'Firefox浏览器配置','BROWSER','FIREFOX','latest','DESKTOP',NULL,'WINDOWS',NULL,15,30,30,NULL,NULL,NULL,0,NULL,NULL,0,1,NULL,'2025-05-06 09:25:56',NULL,'2025-05-06 09:25:56'),(6,'gj','BROWSER','CHROME','2342','DESKTOP',NULL,'WINDOWS','',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,0,0,NULL,'2025-05-06 09:32:39',NULL,'2025-05-06 09:47:25'),(7,'fr','BROWSER','EDGE','sdfsf','DESKTOP',NULL,'WINDOWS','',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,0,1,NULL,'2025-05-06 09:47:35',NULL,'2025-05-06 09:47:35');
/*!40000 ALTER TABLE `ui_test_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ui_test_execution`
--

DROP TABLE IF EXISTS `ui_test_execution`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ui_test_execution` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '执行ID',
  `execution_name` varchar(100) NOT NULL COMMENT '执行名称',
  `execution_type` varchar(20) NOT NULL COMMENT '执行类型：单用例/测试套件',
  `execution_source_id` bigint NOT NULL COMMENT '执行来源ID（用例ID或套件ID）',
  `execution_source_name` varchar(100) NOT NULL COMMENT '执行来源名称（用例名称或套件名称）',
  `status` varchar(20) NOT NULL COMMENT '执行状态：waiting/running/completed/failed/aborted',
  `executor` varchar(50) NOT NULL COMMENT '执行人',
  `environment` varchar(50) NOT NULL COMMENT '执行环境（浏览器类型）',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `duration` int DEFAULT NULL COMMENT '执行时长（秒）',
  `success_count` int DEFAULT '0' COMMENT '成功用例数',
  `fail_count` int DEFAULT '0' COMMENT '失败用例数',
  `total_count` int DEFAULT '0' COMMENT '总用例数',
  `execution_config` text COMMENT '执行配置（JSON格式）',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_execution_type` (`execution_type`),
  KEY `idx_execution_source_id` (`execution_source_id`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='UI测试执行记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ui_test_execution`
--

LOCK TABLES `ui_test_execution` WRITE;
/*!40000 ALTER TABLE `ui_test_execution` DISABLE KEYS */;
INSERT INTO `ui_test_execution` (`id`, `execution_name`, `execution_type`, `execution_source_id`, `execution_source_name`, `status`, `executor`, `environment`, `start_time`, `end_time`, `duration`, `success_count`, `fail_count`, `total_count`, `execution_config`, `created_at`, `updated_at`) VALUES (1,'测试执行01','case',1,'测试执行01','waiting','admin','chrome',NULL,NULL,NULL,0,0,1,'{\"screenshotOnFailure\": true, \"retryCount\": 1}','2025-05-06 15:05:03','2025-05-06 15:05:03');
/*!40000 ALTER TABLE `ui_test_execution` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ui_test_execution_detail`
--

DROP TABLE IF EXISTS `ui_test_execution_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ui_test_execution_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '详情ID',
  `execution_id` bigint NOT NULL COMMENT '执行ID',
  `case_id` bigint NOT NULL COMMENT '用例ID',
  `case_name` varchar(100) NOT NULL COMMENT '用例名称',
  `status` varchar(20) NOT NULL COMMENT '状态：waiting/running/passed/failed/skipped',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `duration` int DEFAULT NULL COMMENT '执行时长（秒）',
  `error_message` text COMMENT '错误信息',
  `screenshot_path` varchar(255) DEFAULT NULL COMMENT '失败截图路径',
  `retry_count` int DEFAULT '0' COMMENT '重试次数',
  `step_count` int DEFAULT '0' COMMENT '步骤总数',
  `step_passed` int DEFAULT '0' COMMENT '通过步骤数',
  `step_failed` int DEFAULT '0' COMMENT '失败步骤数',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_execution_id` (`execution_id`),
  KEY `idx_case_id` (`case_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='UI测试执行详情表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ui_test_execution_detail`
--

LOCK TABLES `ui_test_execution_detail` WRITE;
/*!40000 ALTER TABLE `ui_test_execution_detail` DISABLE KEYS */;
INSERT INTO `ui_test_execution_detail` (`id`, `execution_id`, `case_id`, `case_name`, `status`, `start_time`, `end_time`, `duration`, `error_message`, `screenshot_path`, `retry_count`, `step_count`, `step_passed`, `step_failed`, `created_at`, `updated_at`) VALUES (1,1,1,'待获取','waiting',NULL,NULL,NULL,NULL,NULL,0,0,0,0,'2025-05-06 15:05:03','2025-05-06 15:05:03');
/*!40000 ALTER TABLE `ui_test_execution_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ui_test_execution_log`
--

DROP TABLE IF EXISTS `ui_test_execution_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ui_test_execution_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `execution_id` bigint NOT NULL COMMENT '执行ID',
  `detail_id` bigint DEFAULT NULL COMMENT '详情ID',
  `log_level` varchar(10) NOT NULL COMMENT '日志级别：info/warning/error/debug',
  `log_content` text NOT NULL COMMENT '日志内容',
  `step_index` int DEFAULT NULL COMMENT '步骤索引',
  `step_name` varchar(100) DEFAULT NULL COMMENT '步骤名称',
  `timestamp` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '日志时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_execution_id` (`execution_id`),
  KEY `idx_detail_id` (`detail_id`),
  KEY `idx_log_level` (`log_level`),
  KEY `idx_timestamp` (`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='UI测试执行日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ui_test_execution_log`
--

LOCK TABLES `ui_test_execution_log` WRITE;
/*!40000 ALTER TABLE `ui_test_execution_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `ui_test_execution_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ui_test_execution_queue`
--

DROP TABLE IF EXISTS `ui_test_execution_queue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ui_test_execution_queue` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '队列ID',
  `execution_id` bigint NOT NULL COMMENT '执行ID',
  `priority` int DEFAULT '0' COMMENT '优先级',
  `status` varchar(20) NOT NULL COMMENT '状态：waiting/processing/completed/failed',
  `queue_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '进入队列时间',
  `start_time` datetime DEFAULT NULL COMMENT '开始处理时间',
  `end_time` datetime DEFAULT NULL COMMENT '处理完成时间',
  `worker_id` varchar(50) DEFAULT NULL COMMENT '执行工作节点标识',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_execution_id` (`execution_id`),
  KEY `idx_status` (`status`),
  KEY `idx_priority` (`priority`),
  KEY `idx_queue_time` (`queue_time`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='UI测试执行队列表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ui_test_execution_queue`
--

LOCK TABLES `ui_test_execution_queue` WRITE;
/*!40000 ALTER TABLE `ui_test_execution_queue` DISABLE KEYS */;
INSERT INTO `ui_test_execution_queue` (`id`, `execution_id`, `priority`, `status`, `queue_time`, `start_time`, `end_time`, `worker_id`, `created_at`, `updated_at`) VALUES (1,1,0,'waiting','2025-05-06 07:05:04',NULL,NULL,NULL,'2025-05-06 15:05:03','2025-05-06 15:05:03');
/*!40000 ALTER TABLE `ui_test_execution_queue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ui_test_execution_step`
--

DROP TABLE IF EXISTS `ui_test_execution_step`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ui_test_execution_step` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '步骤结果ID',
  `detail_id` bigint NOT NULL COMMENT '执行详情ID',
  `step_index` int NOT NULL COMMENT '步骤索引',
  `step_name` varchar(100) NOT NULL COMMENT '步骤名称',
  `action_type` varchar(50) NOT NULL COMMENT '操作类型',
  `target` text COMMENT '操作目标',
  `input_value` text COMMENT '输入值',
  `expected_result` text COMMENT '预期结果',
  `actual_result` text COMMENT '实际结果',
  `status` varchar(20) NOT NULL COMMENT '状态：passed/failed/skipped',
  `screenshot_path` varchar(255) DEFAULT NULL COMMENT '步骤截图路径',
  `error_message` text COMMENT '错误信息',
  `duration` int DEFAULT NULL COMMENT '步骤执行时长（毫秒）',
  `timestamp` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '执行时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_detail_id` (`detail_id`),
  KEY `idx_step_index` (`step_index`),
  KEY `idx_status` (`status`),
  KEY `idx_timestamp` (`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='UI测试执行步骤结果表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ui_test_execution_step`
--

LOCK TABLES `ui_test_execution_step` WRITE;
/*!40000 ALTER TABLE `ui_test_execution_step` DISABLE KEYS */;
/*!40000 ALTER TABLE `ui_test_execution_step` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ui_test_locator`
--

DROP TABLE IF EXISTS `ui_test_locator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ui_test_locator` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '定位器ID，自增主键',
  `element_name` varchar(100) NOT NULL COMMENT '元素名称',
  `page_name` varchar(100) NOT NULL COMMENT '所属页面',
  `project_id` int NOT NULL COMMENT '所属项目ID',
  `locator_type` varchar(50) NOT NULL COMMENT '定位类型：id、name、css、xpath、linkText等',
  `locator_value` varchar(255) NOT NULL COMMENT '定位值',
  `description` varchar(255) DEFAULT NULL COMMENT '元素描述',
  `screenshot_path` varchar(255) DEFAULT NULL COMMENT '元素截图路径',
  `created_by` varchar(255) NOT NULL COMMENT '创建人',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ui_test_locator_id` (`id`),
  UNIQUE KEY `idx_ui_test_locator_unique` (`element_name`,`page_name`,`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='UI测试元素定位器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ui_test_locator`
--

LOCK TABLES `ui_test_locator` WRITE;
/*!40000 ALTER TABLE `ui_test_locator` DISABLE KEYS */;
/*!40000 ALTER TABLE `ui_test_locator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ui_test_parameter`
--

DROP TABLE IF EXISTS `ui_test_parameter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ui_test_parameter` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '参数ID，自增主键',
  `case_id` varchar(50) NOT NULL COMMENT '关联用例ID',
  `param_name` varchar(100) NOT NULL COMMENT '参数名',
  `param_value` text NOT NULL COMMENT '参数值',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ui_test_parameter_id` (`id`),
  KEY `idx_ui_test_parameter_case_id` (`case_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='UI测试参数表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ui_test_parameter`
--

LOCK TABLES `ui_test_parameter` WRITE;
/*!40000 ALTER TABLE `ui_test_parameter` DISABLE KEYS */;
INSERT INTO `ui_test_parameter` (`id`, `case_id`, `param_name`, `param_value`, `created_at`, `updated_at`) VALUES (1,'UI-001','username','admin','2025-04-30 07:17:22',NULL),(2,'UI-001','password','123456','2025-04-30 07:17:22',NULL);
/*!40000 ALTER TABLE `ui_test_parameter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ui_test_report`
--

DROP TABLE IF EXISTS `ui_test_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ui_test_report` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '报告ID',
  `execution_id` bigint DEFAULT NULL COMMENT '执行ID',
  `report_name` varchar(255) NOT NULL COMMENT '报告名称',
  `report_type` varchar(50) DEFAULT NULL COMMENT '报告类型',
  `environment` varchar(100) DEFAULT NULL COMMENT '执行环境',
  `browser` varchar(100) DEFAULT NULL COMMENT '浏览器',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `duration` int DEFAULT NULL COMMENT '执行时长(毫秒)',
  `total_cases` int DEFAULT '0' COMMENT '总用例数',
  `passed_cases` int DEFAULT '0' COMMENT '通过用例数',
  `failed_cases` int DEFAULT '0' COMMENT '失败用例数',
  `skipped_cases` int DEFAULT '0' COMMENT '跳过用例数',
  `pass_rate` double DEFAULT NULL COMMENT '通过率',
  `executor` varchar(100) DEFAULT NULL COMMENT '执行人',
  `report_content` text COMMENT '报告内容',
  `report_url` varchar(255) DEFAULT NULL COMMENT '报告URL',
  `report_status` varchar(50) DEFAULT NULL COMMENT '报告状态',
  `generated_time` datetime DEFAULT NULL COMMENT '生成时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_execution_id` (`execution_id`),
  KEY `idx_report_name` (`report_name`),
  KEY `idx_generated_time` (`generated_time`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='UI测试报告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ui_test_report`
--

LOCK TABLES `ui_test_report` WRITE;
/*!40000 ALTER TABLE `ui_test_report` DISABLE KEYS */;
INSERT INTO `ui_test_report` (`id`, `execution_id`, `report_name`, `report_type`, `environment`, `browser`, `start_time`, `end_time`, `duration`, `total_cases`, `passed_cases`, `failed_cases`, `skipped_cases`, `pass_rate`, `executor`, `report_content`, `report_url`, `report_status`, `generated_time`, `created_at`, `updated_at`) VALUES (1,101,'登录功能自动化测试报告','用例执行','Chrome','Chrome 95.0','2023-05-15 02:30:00','2023-05-15 02:35:10',310000,5,4,1,0,80,'测试人员1','{\"summary\":\"登录功能自动化测试报告\",\"details\":\"详细内容...\"}',NULL,'已生成','2025-05-06 08:25:32','2025-05-06 08:25:32','2025-05-06 08:25:32'),(2,101,'登录功能自动化测试报告333','用例执行','Chrome','Chrome 95.0','2023-05-15 02:30:00','2023-05-15 02:35:10',310000,5,5,0,0,100,'测试人员1','{\"summary\":\"登录功能自动化测试报告\",\"details\":\"详细内容...\"}',NULL,'已生成','2025-05-06 08:33:13','2025-05-06 08:33:13','2025-05-06 08:33:13');
/*!40000 ALTER TABLE `ui_test_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ui_test_report_detail`
--

DROP TABLE IF EXISTS `ui_test_report_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ui_test_report_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '详情ID',
  `report_id` bigint NOT NULL COMMENT '报告ID',
  `execution_detail_id` bigint DEFAULT NULL COMMENT '执行详情ID',
  `case_name` varchar(255) NOT NULL COMMENT '用例名称',
  `case_type` varchar(50) DEFAULT NULL COMMENT '用例类型',
  `case_result` varchar(50) NOT NULL COMMENT '执行结果',
  `duration` int DEFAULT NULL COMMENT '执行时长(毫秒)',
  `total_steps` int DEFAULT '0' COMMENT '总步骤数',
  `passed_steps` int DEFAULT '0' COMMENT '通过步骤数',
  `failed_steps` int DEFAULT '0' COMMENT '失败步骤数',
  `skipped_steps` int DEFAULT '0' COMMENT '跳过步骤数',
  `error_message` text COMMENT '错误信息',
  `screenshot_urls` text COMMENT '截图URL列表',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_report_id` (`report_id`),
  KEY `idx_execution_detail_id` (`execution_detail_id`),
  KEY `idx_case_result` (`case_result`),
  CONSTRAINT `fk_report_detail_report_id` FOREIGN KEY (`report_id`) REFERENCES `ui_test_report` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='UI测试报告详情表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ui_test_report_detail`
--

LOCK TABLES `ui_test_report_detail` WRITE;
/*!40000 ALTER TABLE `ui_test_report_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `ui_test_report_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ui_test_report_step`
--

DROP TABLE IF EXISTS `ui_test_report_step`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ui_test_report_step` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '步骤ID',
  `report_detail_id` bigint NOT NULL COMMENT '报告详情ID',
  `step_index` int NOT NULL COMMENT '步骤序号',
  `step_name` varchar(255) NOT NULL COMMENT '步骤名称',
  `action_type` varchar(50) DEFAULT NULL COMMENT '操作类型',
  `target` varchar(255) DEFAULT NULL COMMENT '操作目标',
  `input_value` text COMMENT '输入值',
  `expected_result` text COMMENT '期望结果',
  `actual_result` text COMMENT '实际结果',
  `status` varchar(50) NOT NULL COMMENT '执行状态',
  `duration` int DEFAULT NULL COMMENT '执行时长(毫秒)',
  `screenshot_before` varchar(255) DEFAULT NULL COMMENT '执行前截图',
  `screenshot_after` varchar(255) DEFAULT NULL COMMENT '执行后截图',
  `error_message` text COMMENT '错误信息',
  `executed_at` datetime DEFAULT NULL COMMENT '执行时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_report_detail_id` (`report_detail_id`),
  KEY `idx_step_index` (`step_index`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_report_step_detail_id` FOREIGN KEY (`report_detail_id`) REFERENCES `ui_test_report_detail` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='UI测试报告步骤表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ui_test_report_step`
--

LOCK TABLES `ui_test_report_step` WRITE;
/*!40000 ALTER TABLE `ui_test_report_step` DISABLE KEYS */;
/*!40000 ALTER TABLE `ui_test_report_step` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ui_test_step`
--

DROP TABLE IF EXISTS `ui_test_step`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ui_test_step` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '步骤ID，自增主键',
  `case_id` varchar(50) NOT NULL COMMENT '关联用例ID',
  `step_index` int NOT NULL COMMENT '步骤序号',
  `action_type` varchar(50) NOT NULL COMMENT '操作类型：click、input、select、getText、assertText、assertExists、wait、navigate等',
  `locator_type` varchar(50) DEFAULT NULL COMMENT '元素定位类型：id、name、css、xpath、linkText等',
  `locator_value` varchar(255) DEFAULT NULL COMMENT '元素定位值',
  `input_value` text COMMENT '输入/期望值',
  `wait_time` int DEFAULT NULL COMMENT '等待时间(毫秒)',
  `target_url` varchar(255) DEFAULT NULL COMMENT '目标URL',
  `description` varchar(255) DEFAULT NULL COMMENT '步骤描述',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ui_test_step_id` (`id`),
  KEY `idx_ui_test_step_case_id` (`case_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='UI测试步骤表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ui_test_step`
--

LOCK TABLES `ui_test_step` WRITE;
/*!40000 ALTER TABLE `ui_test_step` DISABLE KEYS */;
INSERT INTO `ui_test_step` (`id`, `case_id`, `step_index`, `action_type`, `locator_type`, `locator_value`, `input_value`, `wait_time`, `target_url`, `description`, `created_at`, `updated_at`) VALUES (1,'UI-001',1,'navigate',NULL,NULL,NULL,NULL,'http://localhost:8080/login','打开登录页面','2025-04-30 07:17:22',NULL),(2,'UI-001',2,'input','id','username','admin',NULL,NULL,'输入用户名','2025-04-30 07:17:22',NULL),(3,'UI-001',3,'input','id','password','123456',NULL,NULL,'输入密码','2025-04-30 07:17:22',NULL),(4,'UI-001',4,'click','css','button[type=\'submit\']',NULL,NULL,NULL,'点击登录按钮','2025-04-30 07:17:22',NULL),(5,'UI-001',5,'assertText','css','.welcome-message','欢迎回来',NULL,NULL,'验证登录成功后的欢迎信息','2025-04-30 07:17:22',NULL);
/*!40000 ALTER TABLE `ui_test_step` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ui_test_step_execution`
--

DROP TABLE IF EXISTS `ui_test_step_execution`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ui_test_step_execution` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '步骤执行ID，自增主键',
  `execution_id` int NOT NULL COMMENT '关联执行记录ID',
  `step_id` int NOT NULL COMMENT '关联步骤ID',
  `execution_status` varchar(50) NOT NULL COMMENT '执行状态：成功、失败、跳过',
  `execution_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '执行时间',
  `execution_duration` int DEFAULT NULL COMMENT '执行时长(毫秒)',
  `actual_result` text COMMENT '实际结果',
  `error_message` text COMMENT '错误信息',
  `screenshot_path` varchar(255) DEFAULT NULL COMMENT '截图路径',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ui_test_step_execution_id` (`id`),
  KEY `idx_ui_test_step_execution_execution_id` (`execution_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='UI测试步骤执行记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ui_test_step_execution`
--

LOCK TABLES `ui_test_step_execution` WRITE;
/*!40000 ALTER TABLE `ui_test_step_execution` DISABLE KEYS */;
/*!40000 ALTER TABLE `ui_test_step_execution` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ui_test_suite`
--

DROP TABLE IF EXISTS `ui_test_suite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ui_test_suite` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '套件ID，自增主键',
  `suite_name` varchar(100) NOT NULL COMMENT '套件名称',
  `project_id` int NOT NULL COMMENT '所属项目ID',
  `description` text COMMENT '套件描述',
  `execute_in_parallel` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否并行执行',
  `max_parallel_count` int DEFAULT NULL COMMENT '最大并行数',
  `created_by` varchar(255) NOT NULL COMMENT '创建人',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `case_count` int DEFAULT '0' COMMENT '用例数量',
  `last_result` varchar(50) DEFAULT '未执行' COMMENT '最后执行结果',
  `last_run_at` timestamp NULL DEFAULT NULL COMMENT '最后执行时间',
  `trigger_mode` varchar(50) DEFAULT '手动' COMMENT '触发方式',
  `concurrency_level` varchar(50) DEFAULT '中' COMMENT '并发级别',
  `environment` varchar(100) DEFAULT NULL COMMENT '执行环境',
  `timeout` int DEFAULT '30' COMMENT '超时时间(秒)',
  `retry_count` int DEFAULT '0' COMMENT '重试次数',
  `screenshot_policy` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ui_test_suite_id` (`id`),
  UNIQUE KEY `idx_ui_test_suite_name_project` (`suite_name`,`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='UI测试套件表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ui_test_suite`
--

LOCK TABLES `ui_test_suite` WRITE;
/*!40000 ALTER TABLE `ui_test_suite` DISABLE KEYS */;
INSERT INTO `ui_test_suite` (`id`, `suite_name`, `project_id`, `description`, `execute_in_parallel`, `max_parallel_count`, `created_by`, `created_at`, `updated_at`, `case_count`, `last_result`, `last_run_at`, `trigger_mode`, `concurrency_level`, `environment`, `timeout`, `retry_count`, `screenshot_policy`) VALUES (1,'22',1,'',0,NULL,'当前用户','2025-05-05 19:09:10',NULL,0,'未执行',NULL,'手动','中',NULL,30,0,NULL),(2,'12313',1,'',0,NULL,'当前用户','2025-05-05 19:09:34','2025-05-05 20:28:48',0,'未执行',NULL,'手动','中',NULL,30,0,NULL),(3,'dsdfsfsf',1,'',0,NULL,'当前用户','2025-05-05 19:10:52','2025-05-05 19:39:14',0,'未执行',NULL,'手动','中',NULL,30,0,NULL);
/*!40000 ALTER TABLE `ui_test_suite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ui_test_suite_case`
--

DROP TABLE IF EXISTS `ui_test_suite_case`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ui_test_suite_case` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '关联ID，自增主键',
  `suite_id` int NOT NULL COMMENT '套件ID',
  `case_id` varchar(50) NOT NULL COMMENT '用例ID',
  `execution_order` int NOT NULL COMMENT '执行顺序',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ui_test_suite_case_id` (`id`),
  UNIQUE KEY `idx_ui_test_suite_case` (`suite_id`,`case_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='UI测试套件用例关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ui_test_suite_case`
--

LOCK TABLES `ui_test_suite_case` WRITE;
/*!40000 ALTER TABLE `ui_test_suite_case` DISABLE KEYS */;
/*!40000 ALTER TABLE `ui_test_suite_case` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ui_test_suite_case_rel`
--

DROP TABLE IF EXISTS `ui_test_suite_case_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ui_test_suite_case_rel` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '关联ID，自增主键',
  `suite_id` int NOT NULL COMMENT '套件ID',
  `case_id` int NOT NULL COMMENT '用例ID',
  `order_index` int NOT NULL COMMENT '执行顺序',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ui_test_suite_case_rel_id` (`id`),
  KEY `idx_ui_test_suite_case_rel_suite_id` (`suite_id`),
  KEY `idx_ui_test_suite_case_rel_case_id` (`case_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='UI测试套件与测试用例关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ui_test_suite_case_rel`
--

LOCK TABLES `ui_test_suite_case_rel` WRITE;
/*!40000 ALTER TABLE `ui_test_suite_case_rel` DISABLE KEYS */;
/*!40000 ALTER TABLE `ui_test_suite_case_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `age` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`id`, `name`, `age`) VALUES (1,'Tony',26);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_login`
--

DROP TABLE IF EXISTS `user_login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_login` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码（建议加密存储）',
  `email` varchar(255) DEFAULT NULL COMMENT '用户邮箱',
  `phone_number` varchar(20) DEFAULT NULL COMMENT '用户手机号',
  `created_at` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `last_login_at` timestamp NULL DEFAULT NULL COMMENT '上次登录时间',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否激活，0未激活，1激活',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_login`
--

LOCK TABLES `user_login` WRITE;
/*!40000 ALTER TABLE `user_login` DISABLE KEYS */;
INSERT INTO `user_login` (`id`, `username`, `password`, `email`, `phone_number`, `created_at`, `last_login_at`, `is_active`) VALUES (3,'admin','$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi',NULL,NULL,NULL,'2025-05-06 02:16:04',1);
/*!40000 ALTER TABLE `user_login` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-07 14:51:05
