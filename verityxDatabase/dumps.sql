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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '账户ID',
  `code` varchar(50) NOT NULL COMMENT '账户编码',
  `name` varchar(100) NOT NULL COMMENT '账户名称',
  `type` varchar(20) NOT NULL COMMENT '类型(cash-现金,bank-银行,receivable-应收,payable-应付,other-其他)',
  `balance` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '余额',
  `currency` varchar(10) NOT NULL DEFAULT 'CNY' COMMENT '币种',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否激活',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='账户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

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
INSERT INTO `api_environment` VALUES (1,'开发环境','http://localhost:8089','本地开发测试环境',1,'{\"Content-Type\":\"application/json\"}','{\"version\":\"v1\",\"debug\":\"true\"}','2025-04-29 16:55:58','2025-04-29 16:55:58'),(2,'测试环境','http://test-api.example.com','供测试人员使用的环境',0,'{\"Content-Type\":\"application/json\",\"X-API-Key\":\"test-api-key\"}','{\"version\":\"v1\"}','2025-04-29 16:55:58','2025-04-29 16:55:58'),(3,'生产环境','https://api.example.com','生产正式环境',0,'{\"Content-Type\":\"application/json\",\"X-API-Key\":\"prod-api-key\"}','{\"version\":\"v1\"}','2025-04-29 16:55:58','2025-04-29 16:55:58'),(6,'预发布环境','http://staging-api.example.com','用于预发布阶段的API测试环境',0,'{\"Content-Type\":\"application/json\",\"Authorization\":\"Bearer staging-token\"}','{\"version\":\"v1\",\"debug\":\"true\"}','2025-04-29 16:59:05','2025-04-29 16:59:05');
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
INSERT INTO `api_management` VALUES (1,'用户管理','获取用户信息','1001,1002','GET','/user/info','active','2024-11-28 01:44:55','2024-11-28 01:44:55','http://mock.example.com/user/info','{\"user_id\": 1, \"username\": \"张三\"}',1),(2,'订单管理','创建订单','1003,1004','POST','/order/create','active','2024-11-28 01:44:55','2024-11-28 01:44:55','http://mock.example.com/order/create','{\"order_id\": 123, \"status\": \"created\"}',1),(3,'商品管理','查询商品列表','1005,1006','GET','/product/list','active','2024-11-28 01:44:55','2024-11-28 01:44:55','http://mock.example.com/product/list','[{\"product_id\": 1, \"name\": \"商品1\"}, {\"product_id\": 2, \"name\": \"商品2\"}]',2),(4,'支付管理','支付订单','1007,1008','POST','/payment/pay','inactive','2024-11-28 01:44:55','2024-11-28 01:44:55','http://mock.example.com/payment/pay','{\"payment_id\": 456, \"status\": \"paid\"}',2),(5,'消息管理','发送消息','1009,1010','POST','/message/send','active','2024-11-28 01:44:55','2024-11-28 01:44:55','http://mock.example.com/message/send','{\"message_id\": 789, \"content\": \"Hello, World!\"}',3),(6,'文件管理','上传文件','1011,1012','POST','/file/upload','active','2024-11-28 01:44:55','2024-11-28 01:44:55','http://mock.example.com/file/upload','{\"file_id\": 101, \"filename\": \"example.pdf\"}',3),(7,'权限管理','获取权限列表','1013,1014','GET','/permission/list','inactive','2024-11-28 01:44:55','2024-11-28 01:44:55','http://mock.example.com/permission/list','[{\"permission_id\": 1, \"name\": \"查看\"}]',4),(8,'角色管理','创建角色','1015,1016','POST','/role/create','active','2024-11-28 01:44:55','2024-11-28 01:44:55','http://mock.example.com/role/create','{\"role_id\": 202, \"name\": \"管理员\"}',4),(9,'日志管理','查询日志','1017,1018','GET','/log/query','active','2024-11-28 01:44:55','2024-11-28 01:44:55','http://mock.example.com/log/query','{\"log_id\": 303, \"content\": \"操作日志\"}',5),(10,'设置管理','更新设置','1019,1020','POST','/setting/update','inactive','2024-11-28 01:44:55','2024-11-28 01:44:55','http://mock.example.com/setting/update','{\"setting_id\": 404, \"value\": \"新设置\"}',5);
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
INSERT INTO `api_test_report` VALUES (1,'用户登录API','http://localhost:8089/api/user/login','POST','成功',120,'2023-05-15 02:30:00','测试人员1','{\"Content-Type\": \"application/json\"}','{\"username\": \"admin\", \"password\": \"123456\"}','{\"code\": 200, \"message\": \"登录成功\", \"data\": {\"token\": \"xxx\"}}','2025-04-29 14:38:09','2025-04-29 14:38:09'),(2,'用户登录API','http://localhost:8089/api/user/login','POST','成功',120,'2023-05-15 02:30:00','测试人员1','{\"Content-Type\": \"application/json\"}','{\"username\": \"admin\", \"password\": \"123456\"}','{\"code\": 200, \"message\": \"登录成功\", \"data\": {\"token\": \"xxx\"}}','2025-04-29 16:14:25','2025-04-29 16:14:25'),(3,'测试API','http://localhost:8089/','POST','成功',120,'2023-05-15 02:30:00','测试人员1','{\"Content-Type\": \"application/json\"}','{\"username\": \"admin\", \"password\": \"123456\"}','{\"code\": 200, \"message\": \"登录成功\", \"data\": {\"token\": \"xxx\"}}','2025-04-29 16:51:30','2025-04-29 16:51:30');
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
INSERT INTO `bug_management` VALUES (1,'Bug1','1','阻塞','11','2024-11-26 09:59:06',NULL,'功能问题','已修复',NULL,1,NULL,NULL,NULL,NULL),(2,'Bug2','2','严重','11','2024-11-26 09:59:06',NULL,'性能问题','已确认',NULL,1,NULL,NULL,NULL,NULL),(3,'Bug3','3','一般','11','2024-11-26 09:59:06',NULL,'功能问题','已确认',NULL,1,NULL,NULL,NULL,NULL),(4,'Bug4','4','轻微','11','2024-11-26 09:59:06',NULL,'功能问题','已确认',NULL,1,NULL,NULL,NULL,NULL),(7,'Bug7','3','一般','11','2024-11-26 09:59:06',NULL,'接口问题','已确认',NULL,1,NULL,NULL,NULL,NULL),(8,'Bug8','4','轻微','11','2024-11-26 09:59:06',NULL,'功能问题','已确认',NULL,1,NULL,NULL,NULL,NULL),(9,'Bug9','1','严重','11','2024-11-26 09:59:06',NULL,'功能问题','已确认',NULL,1,NULL,NULL,NULL,NULL),(10,'Bug10','1','阻塞','11','2024-11-26 09:59:06',NULL,'安全问题','已确认',NULL,1,NULL,NULL,NULL,NULL),(11,'Bug11','1','阻塞','11','2024-11-26 09:59:06',NULL,'功能问题','待修复',NULL,1,NULL,NULL,NULL,NULL),(12,'阿斯顿','priority1','阻塞','阿达','2024-11-26 08:00:00',NULL,'兼容性问题','已创建',222,NULL,NULL,NULL,NULL,NULL),(13,'缺陷新增测试1','priority1','阻塞','张武','2024-11-26 08:00:00',NULL,'功能问题','已创建',222,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `bug_management` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '客户ID',
  `code` varchar(50) NOT NULL COMMENT '客户编码',
  `name` varchar(100) NOT NULL COMMENT '客户名称',
  `contact_person` varchar(50) DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT '电子邮箱',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `bank_name` varchar(100) DEFAULT NULL COMMENT '开户银行',
  `bank_account` varchar(50) DEFAULT NULL COMMENT '银行账号',
  `tax_no` varchar(50) DEFAULT NULL COMMENT '税号',
  `credit_limit` decimal(12,2) DEFAULT '0.00' COMMENT '信用额度',
  `status` varchar(20) NOT NULL DEFAULT 'active' COMMENT '状态(active-激活,inactive-禁用)',
  `level` varchar(20) DEFAULT 'normal' COMMENT '等级(normal-普通,vip-重要,blacklist-黑名单)',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='客户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `financial_transaction`
--

DROP TABLE IF EXISTS `financial_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `financial_transaction` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '流水ID',
  `transaction_no` varchar(50) NOT NULL COMMENT '流水编号',
  `account_id` int NOT NULL COMMENT '账户ID',
  `amount` decimal(12,2) NOT NULL COMMENT '金额(正收入负支出)',
  `balance` decimal(12,2) NOT NULL COMMENT '当前余额',
  `transaction_date` date NOT NULL COMMENT '交易日期',
  `type` varchar(20) NOT NULL COMMENT '类型(receipt-收款,payment-付款,transfer-转账,adjustment-调整)',
  `reference_no` varchar(50) DEFAULT NULL COMMENT '关联单据号',
  `reference_type` varchar(50) DEFAULT NULL COMMENT '关联单据类型',
  `operator_id` int NOT NULL COMMENT '操作人ID',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `transaction_no` (`transaction_no`),
  KEY `account_id` (`account_id`),
  KEY `operator_id` (`operator_id`),
  CONSTRAINT `financial_transaction_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `financial_transaction_ibfk_2` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='财务流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `financial_transaction`
--

LOCK TABLES `financial_transaction` WRITE;
/*!40000 ALTER TABLE `financial_transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `financial_transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '库存ID',
  `product_id` int NOT NULL COMMENT '产品ID',
  `variant_id` int DEFAULT NULL COMMENT '变体ID',
  `warehouse_id` int NOT NULL COMMENT '仓库ID',
  `quantity` int NOT NULL DEFAULT '0' COMMENT '库存数量',
  `location` varchar(50) DEFAULT NULL COMMENT '库位',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `product_variant_warehouse` (`product_id`,`variant_id`,`warehouse_id`),
  KEY `variant_id` (`variant_id`),
  KEY `warehouse_id` (`warehouse_id`),
  CONSTRAINT `inventory_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `inventory_ibfk_2` FOREIGN KEY (`variant_id`) REFERENCES `product_variant` (`id`) ON DELETE CASCADE,
  CONSTRAINT `inventory_ibfk_3` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouse` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='库存表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory_check`
--

DROP TABLE IF EXISTS `inventory_check`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory_check` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '盘点ID',
  `check_no` varchar(50) NOT NULL COMMENT '盘点单号',
  `warehouse_id` int NOT NULL COMMENT '仓库ID',
  `status` varchar(20) NOT NULL DEFAULT 'draft' COMMENT '状态(draft-草稿,ongoing-进行中,completed-完成)',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `created_by` int NOT NULL COMMENT '创建人ID',
  `checked_by` int DEFAULT NULL COMMENT '盘点人ID',
  `approved_by` int DEFAULT NULL COMMENT '审核人ID',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `check_no` (`check_no`),
  KEY `warehouse_id` (`warehouse_id`),
  KEY `created_by` (`created_by`),
  KEY `checked_by` (`checked_by`),
  KEY `approved_by` (`approved_by`),
  CONSTRAINT `inventory_check_ibfk_1` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouse` (`id`),
  CONSTRAINT `inventory_check_ibfk_2` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`),
  CONSTRAINT `inventory_check_ibfk_3` FOREIGN KEY (`checked_by`) REFERENCES `user` (`id`),
  CONSTRAINT `inventory_check_ibfk_4` FOREIGN KEY (`approved_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='库存盘点表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_check`
--

LOCK TABLES `inventory_check` WRITE;
/*!40000 ALTER TABLE `inventory_check` DISABLE KEYS */;
/*!40000 ALTER TABLE `inventory_check` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory_check_item`
--

DROP TABLE IF EXISTS `inventory_check_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory_check_item` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '盘点明细ID',
  `check_id` int NOT NULL COMMENT '盘点ID',
  `product_id` int NOT NULL COMMENT '产品ID',
  `variant_id` int DEFAULT NULL COMMENT '变体ID',
  `system_quantity` int NOT NULL DEFAULT '0' COMMENT '系统数量',
  `actual_quantity` int DEFAULT NULL COMMENT '实际数量',
  `difference` int DEFAULT NULL COMMENT '差异数量',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `check_id` (`check_id`),
  KEY `product_id` (`product_id`),
  KEY `variant_id` (`variant_id`),
  CONSTRAINT `inventory_check_item_ibfk_1` FOREIGN KEY (`check_id`) REFERENCES `inventory_check` (`id`) ON DELETE CASCADE,
  CONSTRAINT `inventory_check_item_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `inventory_check_item_ibfk_3` FOREIGN KEY (`variant_id`) REFERENCES `product_variant` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='库存盘点明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_check_item`
--

LOCK TABLES `inventory_check_item` WRITE;
/*!40000 ALTER TABLE `inventory_check_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `inventory_check_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory_transaction`
--

DROP TABLE IF EXISTS `inventory_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory_transaction` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '流水ID',
  `transaction_no` varchar(50) NOT NULL COMMENT '流水编号',
  `product_id` int NOT NULL COMMENT '产品ID',
  `variant_id` int DEFAULT NULL COMMENT '变体ID',
  `warehouse_id` int NOT NULL COMMENT '仓库ID',
  `quantity` int NOT NULL COMMENT '数量(正入库负出库)',
  `type` varchar(20) NOT NULL COMMENT '类型(purchase-采购,sales-销售,return-退货,transfer-调拨,inventory-盘点)',
  `reference_no` varchar(50) DEFAULT NULL COMMENT '关联单据号',
  `reference_type` varchar(50) DEFAULT NULL COMMENT '关联单据类型',
  `operator_id` int NOT NULL COMMENT '操作人ID',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `transaction_no` (`transaction_no`),
  KEY `product_id` (`product_id`),
  KEY `variant_id` (`variant_id`),
  KEY `warehouse_id` (`warehouse_id`),
  KEY `operator_id` (`operator_id`),
  CONSTRAINT `inventory_transaction_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `inventory_transaction_ibfk_2` FOREIGN KEY (`variant_id`) REFERENCES `product_variant` (`id`),
  CONSTRAINT `inventory_transaction_ibfk_3` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouse` (`id`),
  CONSTRAINT `inventory_transaction_ibfk_4` FOREIGN KEY (`operator_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='库存流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_transaction`
--

LOCK TABLES `inventory_transaction` WRITE;
/*!40000 ALTER TABLE `inventory_transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `inventory_transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoice` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '发票ID',
  `invoice_no` varchar(50) NOT NULL COMMENT '发票单号',
  `type` varchar(20) NOT NULL COMMENT '类型(income-收入,expense-支出)',
  `party_type` varchar(20) NOT NULL COMMENT '对象类型(customer-客户,supplier-供应商,other-其他)',
  `party_id` int DEFAULT NULL COMMENT '对象ID',
  `amount` decimal(12,2) NOT NULL COMMENT '金额',
  `tax_amount` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '税额',
  `total_amount` decimal(12,2) NOT NULL COMMENT '总金额',
  `invoice_date` date NOT NULL COMMENT '开票日期',
  `reference_type` varchar(20) DEFAULT NULL COMMENT '关联单据类型',
  `reference_id` int DEFAULT NULL COMMENT '关联单据ID',
  `status` varchar(20) NOT NULL DEFAULT 'draft' COMMENT '状态(draft-草稿,confirmed-已确认,cancelled-已取消)',
  `created_by` int NOT NULL COMMENT '创建人ID',
  `confirmed_by` int DEFAULT NULL COMMENT '确认人ID',
  `confirmed_at` datetime DEFAULT NULL COMMENT '确认时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `invoice_no` (`invoice_no`),
  KEY `created_by` (`created_by`),
  KEY `confirmed_by` (`confirmed_by`),
  CONSTRAINT `invoice_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`),
  CONSTRAINT `invoice_ibfk_2` FOREIGN KEY (`confirmed_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='发票表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `operation_log`
--

DROP TABLE IF EXISTS `operation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `operation_log` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` int DEFAULT NULL COMMENT '用户ID',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `module` varchar(50) NOT NULL COMMENT '模块',
  `action` varchar(50) NOT NULL COMMENT '操作',
  `content` text COMMENT '内容',
  `status` varchar(20) NOT NULL DEFAULT 'success' COMMENT '状态(success-成功,failure-失败)',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `operation_log_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `operation_log`
--

LOCK TABLES `operation_log` WRITE;
/*!40000 ALTER TABLE `operation_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `operation_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '收付款ID',
  `payment_no` varchar(50) NOT NULL COMMENT '收付款单号',
  `type` varchar(20) NOT NULL COMMENT '类型(receipt-收款,payment-付款)',
  `party_type` varchar(20) NOT NULL COMMENT '对象类型(customer-客户,supplier-供应商,employee-员工,other-其他)',
  `party_id` int DEFAULT NULL COMMENT '对象ID',
  `amount` decimal(12,2) NOT NULL COMMENT '金额',
  `payment_method` varchar(20) NOT NULL COMMENT '付款方式(cash-现金,bank-银行转账,wechat-微信,alipay-支付宝,other-其他)',
  `reference_type` varchar(20) DEFAULT NULL COMMENT '关联单据类型',
  `reference_id` int DEFAULT NULL COMMENT '关联单据ID',
  `payment_date` date NOT NULL COMMENT '付款日期',
  `account_no` varchar(50) DEFAULT NULL COMMENT '收付款账号',
  `account_name` varchar(100) DEFAULT NULL COMMENT '收付款账户名',
  `bank_name` varchar(100) DEFAULT NULL COMMENT '银行名称',
  `transaction_no` varchar(100) DEFAULT NULL COMMENT '交易流水号',
  `status` varchar(20) NOT NULL DEFAULT 'draft' COMMENT '状态(draft-草稿,confirmed-已确认,cancelled-已取消)',
  `created_by` int NOT NULL COMMENT '创建人ID',
  `confirmed_by` int DEFAULT NULL COMMENT '确认人ID',
  `confirmed_at` datetime DEFAULT NULL COMMENT '确认时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `payment_no` (`payment_no`),
  KEY `created_by` (`created_by`),
  KEY `confirmed_by` (`confirmed_by`),
  CONSTRAINT `payment_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`),
  CONSTRAINT `payment_ibfk_2` FOREIGN KEY (`confirmed_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='收付款表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '产品ID',
  `code` varchar(50) NOT NULL COMMENT '产品编码',
  `name` varchar(100) NOT NULL COMMENT '产品名称',
  `category_id` int NOT NULL COMMENT '分类ID',
  `description` text COMMENT '产品描述',
  `cost_price` decimal(10,2) NOT NULL COMMENT '成本价',
  `selling_price` decimal(10,2) NOT NULL COMMENT '销售价',
  `unit` varchar(20) NOT NULL COMMENT '单位',
  `weight` decimal(10,2) DEFAULT NULL COMMENT '重量(kg)',
  `volume` decimal(10,2) DEFAULT NULL COMMENT '体积(m³)',
  `barcode` varchar(50) DEFAULT NULL COMMENT '条形码',
  `has_variants` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否有变体',
  `min_stock` int DEFAULT '0' COMMENT '最小库存',
  `max_stock` int DEFAULT '0' COMMENT '最大库存',
  `status` varchar(20) NOT NULL DEFAULT 'active' COMMENT '状态(active-上架,inactive-下架)',
  `created_by` int DEFAULT NULL COMMENT '创建人ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`),
  KEY `category_id` (`category_id`),
  KEY `created_by` (`created_by`),
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `product_category` (`id`),
  CONSTRAINT `product_ibfk_2` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='产品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_category`
--

DROP TABLE IF EXISTS `product_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_category` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(100) NOT NULL COMMENT '分类名称',
  `parent_id` int DEFAULT NULL COMMENT '父分类ID',
  `level` int NOT NULL DEFAULT '1' COMMENT '分类层级',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否激活',
  `description` varchar(255) DEFAULT NULL COMMENT '分类描述',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `parent_id` (`parent_id`),
  CONSTRAINT `product_category_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `product_category` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='产品分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_category`
--

LOCK TABLES `product_category` WRITE;
/*!40000 ALTER TABLE `product_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_image`
--

DROP TABLE IF EXISTS `product_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_image` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `product_id` int NOT NULL COMMENT '产品ID',
  `variant_id` int DEFAULT NULL COMMENT '变体ID',
  `image_url` varchar(255) NOT NULL COMMENT '图片URL',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序',
  `is_main` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否主图',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  KEY `variant_id` (`variant_id`),
  CONSTRAINT `product_image_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE,
  CONSTRAINT `product_image_ibfk_2` FOREIGN KEY (`variant_id`) REFERENCES `product_variant` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='产品图片表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_image`
--

LOCK TABLES `product_image` WRITE;
/*!40000 ALTER TABLE `product_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_variant`
--

DROP TABLE IF EXISTS `product_variant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_variant` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '变体ID',
  `product_id` int NOT NULL COMMENT '产品ID',
  `sku` varchar(50) NOT NULL COMMENT 'SKU编码',
  `variant_name` varchar(100) NOT NULL COMMENT '变体名称',
  `attributes` json DEFAULT NULL COMMENT '属性JSON(颜色、尺寸等)',
  `cost_price` decimal(10,2) NOT NULL COMMENT '成本价',
  `selling_price` decimal(10,2) NOT NULL COMMENT '销售价',
  `stock` int NOT NULL DEFAULT '0' COMMENT '库存',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否激活',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sku` (`sku`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `product_variant_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='产品变体表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_variant`
--

LOCK TABLES `product_variant` WRITE;
/*!40000 ALTER TABLE `product_variant` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_variant` ENABLE KEYS */;
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
INSERT INTO `project` VALUES (1,'项目A','这是一个关于人工智能的研究项目。','2023-01-01','2023-06-30','completed'),(2,'项目B','开发一个全新的电子商务平台。','2023-02-15','2023-12-31','ongoing'),(3,'项目C','研究可持续能源解决方案。','2023-03-10',NULL,'pending'),(4,'项目D','设计一款移动支付应用。','2023-04-05','2023-11-15','ongoing'),(5,'项目E','创建一个虚拟现实游戏。','2023-05-20','2024-02-28','pending'),(6,'项目F','开发一个智能家庭管理系统。','2023-06-01','2023-12-31','completed'),(7,'项目G','研究量子计算的应用。','2023-07-15',NULL,'completed'),(8,'项目H','建立一个大数据分析平台。','2023-08-10','2024-02-01','ongoing'),(12,'测试项目21','测试项目21','2024-11-20','2024-11-21','completed');
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_user`
--

DROP TABLE IF EXISTS `project_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `project_id` int NOT NULL,
  `user_id` bigint NOT NULL,
  `role` varchar(20) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_project_user` (`project_id`,`user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `project_user_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`),
  CONSTRAINT `project_user_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_user`
--

LOCK TABLES `project_user` WRITE;
/*!40000 ALTER TABLE `project_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `project_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_order`
--

DROP TABLE IF EXISTS `purchase_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_order` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '采购单ID',
  `order_no` varchar(50) NOT NULL COMMENT '采购单号',
  `supplier_id` int NOT NULL COMMENT '供应商ID',
  `warehouse_id` int NOT NULL COMMENT '仓库ID',
  `order_date` date NOT NULL COMMENT '采购日期',
  `expect_date` date DEFAULT NULL COMMENT '预计到货日期',
  `total_amount` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '总金额',
  `discount_amount` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '折扣金额',
  `tax_amount` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '税额',
  `final_amount` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '最终金额',
  `payment_status` varchar(20) NOT NULL DEFAULT 'unpaid' COMMENT '付款状态(unpaid-未付款,partial-部分付款,paid-已付款)',
  `delivery_status` varchar(20) NOT NULL DEFAULT 'undelivered' COMMENT '送货状态(undelivered-未送货,partial-部分送货,delivered-已送货)',
  `status` varchar(20) NOT NULL DEFAULT 'draft' COMMENT '状态(draft-草稿,pending-待审核,approved-已审核,rejected-已拒绝,completed-已完成,cancelled-已取消)',
  `created_by` int NOT NULL COMMENT '创建人ID',
  `approved_by` int DEFAULT NULL COMMENT '审核人ID',
  `approved_at` datetime DEFAULT NULL COMMENT '审核时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no` (`order_no`),
  KEY `supplier_id` (`supplier_id`),
  KEY `warehouse_id` (`warehouse_id`),
  KEY `created_by` (`created_by`),
  KEY `approved_by` (`approved_by`),
  CONSTRAINT `purchase_order_ibfk_1` FOREIGN KEY (`supplier_id`) REFERENCES `supplier` (`id`),
  CONSTRAINT `purchase_order_ibfk_2` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouse` (`id`),
  CONSTRAINT `purchase_order_ibfk_3` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`),
  CONSTRAINT `purchase_order_ibfk_4` FOREIGN KEY (`approved_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='采购单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_order`
--

LOCK TABLES `purchase_order` WRITE;
/*!40000 ALTER TABLE `purchase_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchase_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_order_item`
--

DROP TABLE IF EXISTS `purchase_order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_order_item` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '采购单明细ID',
  `order_id` int NOT NULL COMMENT '采购单ID',
  `product_id` int NOT NULL COMMENT '产品ID',
  `variant_id` int DEFAULT NULL COMMENT '变体ID',
  `quantity` int NOT NULL COMMENT '数量',
  `received_quantity` int NOT NULL DEFAULT '0' COMMENT '已收数量',
  `unit_price` decimal(10,2) NOT NULL COMMENT '单价',
  `tax_rate` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '税率(%)',
  `tax_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '税额',
  `discount_rate` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '折扣率(%)',
  `discount_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '折扣金额',
  `total_amount` decimal(10,2) NOT NULL COMMENT '总金额',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  KEY `product_id` (`product_id`),
  KEY `variant_id` (`variant_id`),
  CONSTRAINT `purchase_order_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `purchase_order` (`id`) ON DELETE CASCADE,
  CONSTRAINT `purchase_order_item_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `purchase_order_item_ibfk_3` FOREIGN KEY (`variant_id`) REFERENCES `product_variant` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='采购单明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_order_item`
--

LOCK TABLES `purchase_order_item` WRITE;
/*!40000 ALTER TABLE `purchase_order_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchase_order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_receipt`
--

DROP TABLE IF EXISTS `purchase_receipt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_receipt` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '入库单ID',
  `receipt_no` varchar(50) NOT NULL COMMENT '入库单号',
  `order_id` int NOT NULL COMMENT '采购单ID',
  `warehouse_id` int NOT NULL COMMENT '仓库ID',
  `receipt_date` date NOT NULL COMMENT '入库日期',
  `total_quantity` int NOT NULL DEFAULT '0' COMMENT '入库总数量',
  `status` varchar(20) NOT NULL DEFAULT 'draft' COMMENT '状态(draft-草稿,completed-已完成,cancelled-已取消)',
  `created_by` int NOT NULL COMMENT '创建人ID',
  `approved_by` int DEFAULT NULL COMMENT '审核人ID',
  `approved_at` datetime DEFAULT NULL COMMENT '审核时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `receipt_no` (`receipt_no`),
  KEY `order_id` (`order_id`),
  KEY `warehouse_id` (`warehouse_id`),
  KEY `created_by` (`created_by`),
  KEY `approved_by` (`approved_by`),
  CONSTRAINT `purchase_receipt_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `purchase_order` (`id`),
  CONSTRAINT `purchase_receipt_ibfk_2` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouse` (`id`),
  CONSTRAINT `purchase_receipt_ibfk_3` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`),
  CONSTRAINT `purchase_receipt_ibfk_4` FOREIGN KEY (`approved_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='采购入库表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_receipt`
--

LOCK TABLES `purchase_receipt` WRITE;
/*!40000 ALTER TABLE `purchase_receipt` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchase_receipt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_receipt_item`
--

DROP TABLE IF EXISTS `purchase_receipt_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_receipt_item` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '入库明细ID',
  `receipt_id` int NOT NULL COMMENT '入库单ID',
  `order_item_id` int NOT NULL COMMENT '采购单明细ID',
  `product_id` int NOT NULL COMMENT '产品ID',
  `variant_id` int DEFAULT NULL COMMENT '变体ID',
  `quantity` int NOT NULL COMMENT '入库数量',
  `location` varchar(50) DEFAULT NULL COMMENT '库位',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `receipt_id` (`receipt_id`),
  KEY `order_item_id` (`order_item_id`),
  KEY `product_id` (`product_id`),
  KEY `variant_id` (`variant_id`),
  CONSTRAINT `purchase_receipt_item_ibfk_1` FOREIGN KEY (`receipt_id`) REFERENCES `purchase_receipt` (`id`) ON DELETE CASCADE,
  CONSTRAINT `purchase_receipt_item_ibfk_2` FOREIGN KEY (`order_item_id`) REFERENCES `purchase_order_item` (`id`),
  CONSTRAINT `purchase_receipt_item_ibfk_3` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `purchase_receipt_item_ibfk_4` FOREIGN KEY (`variant_id`) REFERENCES `product_variant` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='采购入库明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_receipt_item`
--

LOCK TABLES `purchase_receipt_item` WRITE;
/*!40000 ALTER TABLE `purchase_receipt_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchase_receipt_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales_order`
--

DROP TABLE IF EXISTS `sales_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales_order` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '销售单ID',
  `order_no` varchar(50) NOT NULL COMMENT '销售单号',
  `customer_id` int NOT NULL COMMENT '客户ID',
  `warehouse_id` int NOT NULL COMMENT '仓库ID',
  `order_date` date NOT NULL COMMENT '销售日期',
  `delivery_date` date DEFAULT NULL COMMENT '交付日期',
  `total_amount` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '总金额',
  `discount_amount` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '折扣金额',
  `tax_amount` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '税额',
  `final_amount` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '最终金额',
  `payment_status` varchar(20) NOT NULL DEFAULT 'unpaid' COMMENT '付款状态(unpaid-未付款,partial-部分付款,paid-已付款)',
  `delivery_status` varchar(20) NOT NULL DEFAULT 'undelivered' COMMENT '送货状态(undelivered-未送货,partial-部分送货,delivered-已送货)',
  `status` varchar(20) NOT NULL DEFAULT 'draft' COMMENT '状态(draft-草稿,pending-待审核,approved-已审核,rejected-已拒绝,completed-已完成,cancelled-已取消)',
  `created_by` int NOT NULL COMMENT '创建人ID',
  `approved_by` int DEFAULT NULL COMMENT '审核人ID',
  `approved_at` datetime DEFAULT NULL COMMENT '审核时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no` (`order_no`),
  KEY `customer_id` (`customer_id`),
  KEY `warehouse_id` (`warehouse_id`),
  KEY `created_by` (`created_by`),
  KEY `approved_by` (`approved_by`),
  CONSTRAINT `sales_order_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
  CONSTRAINT `sales_order_ibfk_2` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouse` (`id`),
  CONSTRAINT `sales_order_ibfk_3` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`),
  CONSTRAINT `sales_order_ibfk_4` FOREIGN KEY (`approved_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='销售单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_order`
--

LOCK TABLES `sales_order` WRITE;
/*!40000 ALTER TABLE `sales_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `sales_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales_order_item`
--

DROP TABLE IF EXISTS `sales_order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales_order_item` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '销售单明细ID',
  `order_id` int NOT NULL COMMENT '销售单ID',
  `product_id` int NOT NULL COMMENT '产品ID',
  `variant_id` int DEFAULT NULL COMMENT '变体ID',
  `quantity` int NOT NULL COMMENT '数量',
  `shipped_quantity` int NOT NULL DEFAULT '0' COMMENT '已发数量',
  `unit_price` decimal(10,2) NOT NULL COMMENT '单价',
  `tax_rate` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '税率(%)',
  `tax_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '税额',
  `discount_rate` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '折扣率(%)',
  `discount_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '折扣金额',
  `total_amount` decimal(10,2) NOT NULL COMMENT '总金额',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  KEY `product_id` (`product_id`),
  KEY `variant_id` (`variant_id`),
  CONSTRAINT `sales_order_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `sales_order` (`id`) ON DELETE CASCADE,
  CONSTRAINT `sales_order_item_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `sales_order_item_ibfk_3` FOREIGN KEY (`variant_id`) REFERENCES `product_variant` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='销售单明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_order_item`
--

LOCK TABLES `sales_order_item` WRITE;
/*!40000 ALTER TABLE `sales_order_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `sales_order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales_shipment`
--

DROP TABLE IF EXISTS `sales_shipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales_shipment` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '出库单ID',
  `shipment_no` varchar(50) NOT NULL COMMENT '出库单号',
  `order_id` int NOT NULL COMMENT '销售单ID',
  `warehouse_id` int NOT NULL COMMENT '仓库ID',
  `shipment_date` date NOT NULL COMMENT '出库日期',
  `total_quantity` int NOT NULL DEFAULT '0' COMMENT '出库总数量',
  `status` varchar(20) NOT NULL DEFAULT 'draft' COMMENT '状态(draft-草稿,completed-已完成,cancelled-已取消)',
  `logistics_company` varchar(100) DEFAULT NULL COMMENT '物流公司',
  `logistics_no` varchar(50) DEFAULT NULL COMMENT '物流单号',
  `created_by` int NOT NULL COMMENT '创建人ID',
  `approved_by` int DEFAULT NULL COMMENT '审核人ID',
  `approved_at` datetime DEFAULT NULL COMMENT '审核时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `shipment_no` (`shipment_no`),
  KEY `order_id` (`order_id`),
  KEY `warehouse_id` (`warehouse_id`),
  KEY `created_by` (`created_by`),
  KEY `approved_by` (`approved_by`),
  CONSTRAINT `sales_shipment_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `sales_order` (`id`),
  CONSTRAINT `sales_shipment_ibfk_2` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouse` (`id`),
  CONSTRAINT `sales_shipment_ibfk_3` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`),
  CONSTRAINT `sales_shipment_ibfk_4` FOREIGN KEY (`approved_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='销售出库表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_shipment`
--

LOCK TABLES `sales_shipment` WRITE;
/*!40000 ALTER TABLE `sales_shipment` DISABLE KEYS */;
/*!40000 ALTER TABLE `sales_shipment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales_shipment_item`
--

DROP TABLE IF EXISTS `sales_shipment_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales_shipment_item` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '出库明细ID',
  `shipment_id` int NOT NULL COMMENT '出库单ID',
  `order_item_id` int NOT NULL COMMENT '销售单明细ID',
  `product_id` int NOT NULL COMMENT '产品ID',
  `variant_id` int DEFAULT NULL COMMENT '变体ID',
  `quantity` int NOT NULL COMMENT '出库数量',
  `location` varchar(50) DEFAULT NULL COMMENT '库位',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `shipment_id` (`shipment_id`),
  KEY `order_item_id` (`order_item_id`),
  KEY `product_id` (`product_id`),
  KEY `variant_id` (`variant_id`),
  CONSTRAINT `sales_shipment_item_ibfk_1` FOREIGN KEY (`shipment_id`) REFERENCES `sales_shipment` (`id`) ON DELETE CASCADE,
  CONSTRAINT `sales_shipment_item_ibfk_2` FOREIGN KEY (`order_item_id`) REFERENCES `sales_order_item` (`id`),
  CONSTRAINT `sales_shipment_item_ibfk_3` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `sales_shipment_item_ibfk_4` FOREIGN KEY (`variant_id`) REFERENCES `product_variant` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='销售出库明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_shipment_item`
--

LOCK TABLES `sales_shipment_item` WRITE;
/*!40000 ALTER TABLE `sales_shipment_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `sales_shipment_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier`
--

DROP TABLE IF EXISTS `supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '供应商ID',
  `code` varchar(50) NOT NULL COMMENT '供应商编码',
  `name` varchar(100) NOT NULL COMMENT '供应商名称',
  `contact_person` varchar(50) DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT '电子邮箱',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `bank_name` varchar(100) DEFAULT NULL COMMENT '开户银行',
  `bank_account` varchar(50) DEFAULT NULL COMMENT '银行账号',
  `tax_no` varchar(50) DEFAULT NULL COMMENT '税号',
  `status` varchar(20) NOT NULL DEFAULT 'active' COMMENT '状态(active-激活,inactive-禁用)',
  `level` varchar(20) DEFAULT 'normal' COMMENT '等级(normal-普通,vip-重要,blacklist-黑名单)',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='供应商表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier`
--

LOCK TABLES `supplier` WRITE;
/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_about`
--

DROP TABLE IF EXISTS `sys_about`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_about` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `system_name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '系统名称',
  `system_logo` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '系统Logo路径',
  `company_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公司名称',
  `company_website` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公司网站',
  `contact_email` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系邮箱',
  `copyright` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '版权信息',
  `description` text COLLATE utf8mb4_general_ci COMMENT '系统描述',
  `terms_of_service` text COLLATE utf8mb4_general_ci COMMENT '服务条款',
  `privacy_policy` text COLLATE utf8mb4_general_ci COMMENT '隐私政策',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统基本信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_about`
--

LOCK TABLES `sys_about` WRITE;
/*!40000 ALTER TABLE `sys_about` DISABLE KEYS */;
INSERT INTO `sys_about` VALUES (1,'VerityX测试平台','/assets/images/logo.png','VerityX Inc.','https://www.verityx.com','support@verityx.com','Copyright © 2024 VerityX Inc. All rights reserved.','VerityX是一个全功能的自动化测试平台，提供测试管理、接口测试、UI测试和系统设置等完整解决方案。\n\n平台特点：\n- 全面的测试管理功能，包括测试计划、用例管理和缺陷跟踪\n- 强大的接口测试能力，支持各种API协议\n- 先进的UI测试自动化，包含元素定位器、测试套件和丰富的测试报告\n- 灵活的用户权限管理和系统配置','# VerityX测试平台服务条款\n\n## 1. 服务说明\nVerityX测试平台（以下简称\"平台\"）是由VerityX Inc.（以下简称\"我们\"）提供的自动化测试平台，用于帮助用户进行软件测试管理、接口测试和UI测试等工作。\n\n## 2. 使用条件\n使用本平台，用户需同意以下条款：\n- 用户需持有有效的许可证\n- 不得将平台用于任何非法目的\n- 不得尝试破解、反编译或修改平台代码\n- 不得将许可证转让给第三方使用\n\n## 3. 服务可用性\n我们会尽力确保平台的可用性，但不保证服务不会中断或无错误。我们保留进行必要维护的权利，可能导致临时服务中断。\n\n## 4. 数据安全\n我们重视用户数据安全，采取合理措施保护用户数据。用户应自行备份重要数据，我们不对数据丢失负责。\n\n## 5. 知识产权\n平台及其内容的所有知识产权归VerityX Inc.所有。用户仅获得使用权，不得侵犯相关知识产权。\n\n## 6. 责任限制\n在法律允许的最大范围内，我们对使用平台产生的任何直接或间接损失不承担责任。\n\n## 7. 条款变更\n我们保留随时修改服务条款的权利。条款变更后，继续使用平台即视为接受新条款。\n\n## 8. 适用法律\n本条款受中华人民共和国法律管辖，任何争议应通过友好协商解决。','# VerityX测试平台隐私政策\n\n## 1. 信息收集\n我们可能收集以下用户信息：\n- 账户信息：用户名、邮箱、密码等\n- 使用数据：使用习惯、功能偏好等\n- 测试数据：用户在平台上创建和存储的测试数据\n\n## 2. 信息使用\n我们使用收集的信息：\n- 提供、维护和改进平台服务\n- 发送服务通知和更新\n- 提供技术支持\n- 进行产品分析和优化\n\n## 3. 信息共享\n除以下情况外，我们不会与第三方共享用户信息：\n- 获得用户明确授权\n- 遵守法律法规要求\n- 保护VerityX或用户的权利和安全\n\n## 4. 信息安全\n我们采取合理的安全措施保护用户信息，包括加密传输、安全存储等，但无法保证绝对安全。\n\n## 5. 数据保留\n我们在提供服务所需的时间内保留用户信息，或遵循法律法规的要求。\n\n## 6. 用户权利\n用户有权：\n- 访问和更正个人信息\n- 删除账户及相关数据\n- 选择退出某些数据收集\n\n## 7. Cookie使用\n我们使用Cookie和类似技术改善用户体验。用户可通过浏览器设置控制Cookie。\n\n## 8. 隐私政策更新\n我们可能不时更新本隐私政策，并通过平台通知用户重大变更。\n\n## 9. 联系方式\n如有隐私相关问题，请联系：support@verityx.com','2025-05-08 18:46:47','2025-05-08 18:46:47'),(2,'VerityX测试平台','/assets/images/logo.png','VerityX科技有限公司','https://www.verityx.com','support@verityx.com','Copyright © 2024 VerityX科技有限公司. 保留所有权利。','VerityX是一个全功能自动化测试平台，专为测试团队设计，支持UI测试、API测试和性能测试。提供丰富的测试管理功能，帮助团队提高测试效率和质量。','## VerityX服务条款\n\n### 1. 接受条款\n使用VerityX测试平台服务，表示您同意受本服务条款的约束。如果您不接受这些条款，请勿使用我们的服务。\n\n### 2. 服务描述\nVerityX提供自动化测试平台服务，允许用户创建、管理和执行测试用例。我们可能会不时更改、暂停或终止服务的部分或全部。\n\n### 3. 用户账户\n您负责维护账户安全，并对所有使用您账户的活动负责。\n\n### 4. 用户行为\n您同意不会使用服务进行任何非法或未经授权的活动。\n\n### 5. 知识产权\nVerityX及其许可方拥有服务的所有权利和知识产权。\n\n### 6. 免责声明\n服务按\"现状\"提供，不提供任何明示或暗示的保证。\n\n### 7. 责任限制\n在法律允许的最大范围内，VerityX不对任何损失或损害承担责任。\n\n### 8. 条款变更\n我们可能会修改这些条款，并将在服务中发布通知。继续使用服务表示您接受修改后的条款。\n\n### 9. 管辖法律\n本条款受中国法律管辖并按其解释，不考虑冲突法原则。','## VerityX隐私政策\n\n### 1. 信息收集\n我们收集您提供给我们的信息，如注册信息、测试数据等，以及使用我们服务时自动生成的信息。\n\n### 2. 信息使用\n我们使用收集的信息来提供、维护和改进我们的服务，以及开发新服务。\n\n### 3. 信息共享\n除非有您的同意，或法律要求，或以下情况，我们不会与第三方共享您的个人信息：\n- 与我们的服务提供商共享\n- 出于业务转让目的\n- 保护VerityX的权利和财产\n\n### 4. 数据安全\n我们采取合理措施保护您的信息不被未经授权的访问或披露。\n\n### 5. 数据存储\n您的数据存储在您选择的区域内的安全服务器上。\n\n### 6. 您的权利\n您可以访问、更正或删除您的个人信息。\n\n### 7. Cookie使用\n我们使用Cookie和类似技术来提供和支持我们的服务。\n\n### 8. 隐私政策变更\n我们可能会不时更新此隐私政策，并通过服务通知您任何变更。\n\n### 9. 联系我们\n如果您对此隐私政策有任何问题，请联系我们：support@verityx.com','2025-05-08 20:38:36',NULL);
/*!40000 ALTER TABLE `sys_about` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_api_management`
--

DROP TABLE IF EXISTS `sys_api_management`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_api_management` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT 'API名称',
  `description` varchar(255) DEFAULT NULL COMMENT 'API描述',
  `api_key` varchar(255) NOT NULL COMMENT 'API密钥',
  `secret` varchar(255) DEFAULT NULL COMMENT 'API密钥对应的密钥',
  `callback_url` varchar(255) DEFAULT NULL COMMENT '回调URL',
  `permissions` varchar(500) DEFAULT NULL COMMENT '权限范围（逗号分隔）',
  `rate_limit` int DEFAULT '0' COMMENT '速率限制（每小时请求数）',
  `expiration_date` datetime DEFAULT NULL COMMENT '过期日期',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `creator_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `updater_id` bigint DEFAULT NULL COMMENT '更新人ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`,`deleted`),
  UNIQUE KEY `uk_api_key` (`api_key`,`deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='API管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_api_management`
--

LOCK TABLES `sys_api_management` WRITE;
/*!40000 ALTER TABLE `sys_api_management` DISABLE KEYS */;
INSERT INTO `sys_api_management` VALUES (1,'ceshi1','ceshi1 API密钥','AA5AE38DA92F402D','6ff3bb861daf44f3834f6c17e17853c3',NULL,NULL,0,NULL,1,1,1,NULL,NULL,1);
/*!40000 ALTER TABLE `sys_api_management` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_backup`
--

DROP TABLE IF EXISTS `sys_backup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_backup` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `backup_name` varchar(100) NOT NULL COMMENT '备份名称',
  `file_name` varchar(255) NOT NULL COMMENT '备份文件名',
  `file_path` varchar(255) NOT NULL COMMENT '备份文件路径',
  `file_size` bigint NOT NULL COMMENT '备份文件大小（字节）',
  `backup_type` varchar(20) NOT NULL COMMENT '备份类型（AUTO-自动备份, MANUAL-手动备份）',
  `status` varchar(20) NOT NULL COMMENT '备份状态（SUCCESS-成功, FAILED-失败, RESTORING-恢复中）',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `created_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据库备份记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_backup`
--

LOCK TABLES `sys_backup` WRITE;
/*!40000 ALTER TABLE `sys_backup` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_backup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_cleanup_config`
--

DROP TABLE IF EXISTS `sys_cleanup_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_cleanup_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_key` varchar(100) NOT NULL COMMENT '配置键',
  `config_value` varchar(255) NOT NULL COMMENT '配置值',
  `config_group` varchar(50) NOT NULL COMMENT '配置组（DATA_CLEANUP-数据清理, TEMP_CLEANUP-临时文件清理, ARCHIVE-归档设置）',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `updater_id` bigint DEFAULT NULL COMMENT '更新人ID',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_key_group` (`config_key`,`config_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统清理配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_cleanup_config`
--

LOCK TABLES `sys_cleanup_config` WRITE;
/*!40000 ALTER TABLE `sys_cleanup_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_cleanup_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_cleanup_log`
--

DROP TABLE IF EXISTS `sys_cleanup_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_cleanup_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `cleanup_type` varchar(50) NOT NULL COMMENT '清理类型（DATA_CLEANUP-数据清理, TEMP_CLEANUP-临时文件清理, ARCHIVE-归档）',
  `detail` text NOT NULL COMMENT '清理详情',
  `affected_rows` int DEFAULT '0' COMMENT '影响行数',
  `status` varchar(20) NOT NULL COMMENT '状态（SUCCESS-成功, FAILED-失败）',
  `error_message` text COMMENT '错误信息',
  `executor_id` bigint DEFAULT NULL COMMENT '执行人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_type_time` (`cleanup_type`,`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统清理记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_cleanup_log`
--

LOCK TABLES `sys_cleanup_log` WRITE;
/*!40000 ALTER TABLE `sys_cleanup_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_cleanup_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_config`
--

DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_name` varchar(100) NOT NULL COMMENT '参数名称',
  `config_key` varchar(100) NOT NULL COMMENT '参数键名',
  `config_value` varchar(500) NOT NULL COMMENT '参数键值',
  `config_type` varchar(20) DEFAULT 'text' COMMENT '配置类型（text、number、boolean、json等）',
  `is_system` tinyint(1) DEFAULT '0' COMMENT '是否系统内置（0否 1是）',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态（0停用 1正常）',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` bigint DEFAULT NULL COMMENT '创建者',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `updated_by` bigint DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_config_key` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统参数配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_config`
--

LOCK TABLES `sys_config` WRITE;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT INTO `sys_config` VALUES (1,'备份自动执行','system.backup.auto.enabled','true','boolean',1,1,'是否启用自动备份','2025-05-08 15:53:35',NULL,'2025-05-08 15:53:35',NULL),(2,'备份频率','system.backup.frequency','daily','select',1,1,'备份频率(daily-每天,weekly-每周,monthly-每月)','2025-05-08 15:53:35',NULL,'2025-05-08 15:53:35',NULL),(3,'备份时间','system.backup.time','02:00','time',1,1,'备份执行时间','2025-05-08 15:53:35',NULL,'2025-05-08 15:53:35',NULL),(4,'保留备份数量','system.backup.keep.count','7','number',1,1,'保留的备份文件数量','2025-05-08 15:53:35',NULL,'2025-05-08 15:53:35',NULL),(5,'备份存储位置','system.backup.location','/data/backups','text',1,1,'备份文件存储路径','2025-05-08 15:53:35',NULL,'2025-05-08 15:53:35',NULL),(6,'自动清理数据','system.cleanup.auto.enabled','true','boolean',1,1,'是否启用自动清理','2025-05-08 15:53:35',NULL,'2025-05-08 15:53:35',NULL),(7,'清理频率','system.cleanup.frequency','monthly','select',1,1,'清理频率(weekly-每周,monthly-每月,quarterly-每季度)','2025-05-08 15:53:35',NULL,'2025-05-08 15:53:35',NULL),(8,'报告保留天数','system.cleanup.report.days','365','number',1,1,'测试报告保留天数','2025-05-08 15:53:35',NULL,'2025-05-08 15:53:35',NULL),(9,'日志保留天数','system.cleanup.log.days','180','number',1,1,'操作日志保留天数','2025-05-08 15:53:35',NULL,'2025-05-08 15:53:35',NULL),(10,'监控数据保留天数','system.cleanup.monitor.days','30','number',1,1,'监控数据保留天数','2025-05-08 15:53:35',NULL,'2025-05-08 15:53:35',NULL),(11,'临时文件自动清理','system.cleanup.temp.enabled','true','boolean',1,1,'是否启用临时文件自动清理','2025-05-08 15:53:35',NULL,'2025-05-08 15:53:35',NULL),(12,'临时文件清理频率','system.cleanup.temp.frequency','daily','select',1,1,'临时文件清理频率(daily-每天,weekly-每周,monthly-每月)','2025-05-08 15:53:35',NULL,'2025-05-08 15:53:35',NULL),(13,'报告自动归档','system.archive.auto.enabled','true','boolean',1,1,'是否启用报告自动归档','2025-05-08 15:53:35',NULL,'2025-05-08 15:53:35',NULL),(14,'归档阈值天数','system.archive.days','90','number',1,1,'报告归档阈值天数','2025-05-08 15:53:35',NULL,'2025-05-08 15:53:35',NULL),(15,'归档存储位置','system.archive.location','/data/archives','text',1,1,'归档文件存储路径','2025-05-08 15:53:35',NULL,'2025-05-08 15:53:35',NULL);
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_data_backup`
--

DROP TABLE IF EXISTS `sys_data_backup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_data_backup` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `backup_name` varchar(255) NOT NULL COMMENT '备份名称',
  `file_path` varchar(500) NOT NULL COMMENT '文件路径',
  `file_size` bigint NOT NULL COMMENT '文件大小（字节）',
  `backup_type` varchar(50) NOT NULL COMMENT '备份类型（AUTO-自动备份, MANUAL-手动备份）',
  `status` varchar(50) NOT NULL COMMENT '状态（RUNNING-进行中, SUCCESS-成功, FAILED-失败）',
  `error_message` text COMMENT '错误信息',
  `creator_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_type_time` (`backup_type`,`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据备份表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_data_backup`
--

LOCK TABLES `sys_data_backup` WRITE;
/*!40000 ALTER TABLE `sys_data_backup` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_data_backup` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept`
--

LOCK TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT INTO `sys_dept` VALUES (1,'系统管理部','ADMIN',NULL,1,'Admin',NULL,NULL,1,'系统管理部门',1,'2025-05-07 10:38:06',NULL,NULL),(100,'sdfs','1111111',1,0,'','','',1,NULL,1,'2025-05-07 16:57:39',1,'2025-05-07 17:23:27');
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dept_subscription`
--

DROP TABLE IF EXISTS `sys_dept_subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dept_subscription` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  `rule_id` bigint NOT NULL COMMENT '规则ID',
  `notify_type` varchar(50) NOT NULL COMMENT '通知方式（EMAIL-邮件, SMS-短信, SYSTEM-系统通知）',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `creator_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `updater_id` bigint DEFAULT NULL COMMENT '更新人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_dept_rule` (`dept_id`,`rule_id`,`deleted`),
  KEY `idx_rule` (`rule_id`,`status`,`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门订阅表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept_subscription`
--

LOCK TABLES `sys_dept_subscription` WRITE;
/*!40000 ALTER TABLE `sys_dept_subscription` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_dept_subscription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_item`
--

DROP TABLE IF EXISTS `sys_dict_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dict_type_id` bigint NOT NULL COMMENT '字典类型ID',
  `dict_label` varchar(100) NOT NULL COMMENT '字典标签',
  `dict_value` varchar(100) NOT NULL COMMENT '字典键值',
  `sort` int DEFAULT '0' COMMENT '排序',
  `is_default` tinyint(1) DEFAULT '0' COMMENT '是否默认',
  `is_system` tinyint(1) DEFAULT '0' COMMENT '是否系统内置',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态（true正常 false停用）',
  `css_class` varchar(100) DEFAULT NULL COMMENT 'CSS样式',
  `list_class` varchar(100) DEFAULT NULL COMMENT '表格回显样式',
  `description` varchar(500) DEFAULT NULL COMMENT '备注',
  `created_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `dict_type_id` (`dict_type_id`),
  CONSTRAINT `sys_dict_item_ibfk_1` FOREIGN KEY (`dict_type_id`) REFERENCES `sys_dict_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_item`
--

LOCK TABLES `sys_dict_item` WRITE;
/*!40000 ALTER TABLE `sys_dict_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_dict_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_type`
--

DROP TABLE IF EXISTS `sys_dict_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dict_type` varchar(100) NOT NULL COMMENT '字典类型编码',
  `dict_name` varchar(100) NOT NULL COMMENT '字典类型名称',
  `is_system` tinyint(1) DEFAULT '0' COMMENT '是否系统内置',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态（true正常 false停用）',
  `description` varchar(500) DEFAULT NULL COMMENT '备注',
  `created_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_dict_type` (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_type`
--

LOCK TABLES `sys_dict_type` WRITE;
/*!40000 ALTER TABLE `sys_dict_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_dict_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_error_log`
--

DROP TABLE IF EXISTS `sys_error_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_error_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint DEFAULT NULL COMMENT '操作用户ID',
  `username` varchar(50) DEFAULT NULL COMMENT '操作用户名',
  `request_url` varchar(255) DEFAULT NULL COMMENT '请求URL',
  `request_method` varchar(10) DEFAULT NULL COMMENT '请求方式（GET, POST等）',
  `request_params` text COMMENT '请求参数',
  `request_ip` varchar(50) DEFAULT NULL COMMENT '操作IP',
  `error_msg` text COMMENT '错误消息',
  `stack_trace` text COMMENT '异常堆栈',
  `error_time` datetime NOT NULL COMMENT '异常时间',
  PRIMARY KEY (`id`),
  KEY `idx_error_time` (`error_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统异常日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_error_log`
--

LOCK TABLES `sys_error_log` WRITE;
/*!40000 ALTER TABLE `sys_error_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_error_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_license`
--

DROP TABLE IF EXISTS `sys_license`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_license` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `license_key` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '许可证密钥',
  `license_type` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '许可证类型（例如：试用版、标准版、企业版）',
  `issued_to` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '颁发给（公司或个人名称）',
  `issued_by` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '颁发者',
  `issue_date` datetime NOT NULL COMMENT '颁发日期',
  `expiration_date` datetime DEFAULT NULL COMMENT '过期日期（NULL表示永久有效）',
  `max_users` int DEFAULT NULL COMMENT '最大用户数',
  `features` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权功能列表（JSON格式）',
  `status` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态（有效、过期、禁用）',
  `remarks` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统许可证信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_license`
--

LOCK TABLES `sys_license` WRITE;
/*!40000 ALTER TABLE `sys_license` DISABLE KEYS */;
INSERT INTO `sys_license` VALUES (1,'VERITYX-TRIAL-2024-12345','试用版','VerityX测试用户','VerityX Inc.','2024-01-01 00:00:00','2024-03-31 23:59:59',5,'{\"advancedReporting\":false,\"multipleEnvironments\":false,\"cicdIntegration\":false,\"apiAutomation\":true,\"uiAutomation\":true,\"dataImportExport\":false}','有效','30天试用版许可证','2025-05-08 18:46:47','2025-05-08 18:46:47'),(2,'VERITYX-STD-2024-67890','标准版','ABC测试公司','VerityX Inc.','2024-01-15 00:00:00','2025-01-14 23:59:59',20,'{\"advancedReporting\":false,\"multipleEnvironments\":true,\"cicdIntegration\":false,\"apiAutomation\":true,\"uiAutomation\":true,\"dataImportExport\":true}','有效','标准版年度许可证','2025-05-08 18:46:47','2025-05-08 18:46:47'),(3,'VERITYX-PRO-2024-24680','专业版','DEF科技有限公司','VerityX Inc.','2024-02-01 00:00:00','2025-01-31 23:59:59',50,'{\"advancedReporting\":true,\"multipleEnvironments\":true,\"cicdIntegration\":true,\"apiAutomation\":true,\"uiAutomation\":true,\"dataImportExport\":true}','有效','专业版年度许可证','2025-05-08 18:46:47','2025-05-08 18:46:47'),(4,'VERITYX-ENT-2024-13579','企业版','GHI集团','VerityX Inc.','2024-02-15 00:00:00',NULL,0,'{\"advancedReporting\":true,\"multipleEnvironments\":true,\"cicdIntegration\":true,\"apiAutomation\":true,\"uiAutomation\":true,\"dataImportExport\":true}','有效','企业版永久许可证，无用户数限制','2025-05-08 18:46:47','2025-05-08 18:46:47'),(5,'VERITYX-FREE-2023','免费版','公共用户','VerityX Inc.','2023-10-01 00:00:00','2024-10-01 00:00:00',10,'{\"ui_test\": true, \"api_test\": true, \"performance_test\": false, \"mobile_test\": false}','有效','免费使用版本，功能有限制','2025-05-08 20:38:36',NULL),(6,'VERITYX-STANDARD-2024','标准版','VerityX测试团队','VerityX Inc.','2024-01-01 00:00:00','2025-01-01 00:00:00',50,'{\"ui_test\": true, \"api_test\": true, \"performance_test\": true, \"mobile_test\": false}','有效','标准许可证，支持大部分功能','2025-05-08 20:38:36',NULL),(7,'VERITYX-ENTERPRISE-UNLIMITED','企业版','VerityX企业用户','VerityX Inc.','2024-03-15 00:00:00',NULL,NULL,'{\"ui_test\": true, \"api_test\": true, \"performance_test\": true, \"mobile_test\": true, \"integration\": true, \"advanced_reporting\": true}','有效','企业无限制版本，支持所有功能特性，无用户数限制，永久有效','2025-05-08 20:38:36',NULL);
/*!40000 ALTER TABLE `sys_license` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_monitor_data`
--

DROP TABLE IF EXISTS `sys_monitor_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_monitor_data` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `monitor_type` varchar(50) NOT NULL COMMENT '监控类型（SERVER-服务器, DATABASE-数据库, TEST_ENGINE-测试引擎）',
  `metric_name` varchar(100) NOT NULL COMMENT '指标名称',
  `metric_value` varchar(100) NOT NULL COMMENT '指标值',
  `status` varchar(20) NOT NULL COMMENT '状态（NORMAL-正常, WARNING-警告, ERROR-错误）',
  `timestamp` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
  PRIMARY KEY (`id`),
  KEY `idx_type_time` (`monitor_type`,`timestamp`),
  KEY `idx_metric` (`metric_name`,`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统监控数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_monitor_data`
--

LOCK TABLES `sys_monitor_data` WRITE;
/*!40000 ALTER TABLE `sys_monitor_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_monitor_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_notification_rule`
--

DROP TABLE IF EXISTS `sys_notification_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_notification_rule` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '规则名称',
  `code` varchar(50) NOT NULL COMMENT '规则编码',
  `event_type` varchar(50) NOT NULL COMMENT '事件类型（TEST_PLAN-测试计划, BUG-缺陷, REPORT-报告等）',
  `event_action` varchar(50) NOT NULL COMMENT '事件动作（CREATE-创建, UPDATE-更新, DELETE-删除等）',
  `template_id` bigint NOT NULL COMMENT '模板ID',
  `notify_type` varchar(50) NOT NULL COMMENT '通知方式（EMAIL-邮件, SMS-短信, SYSTEM-系统通知）',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `creator_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `updater_id` bigint DEFAULT NULL COMMENT '更新人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`,`deleted`),
  KEY `idx_event` (`event_type`,`event_action`,`status`,`deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通知规则表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_notification_rule`
--

LOCK TABLES `sys_notification_rule` WRITE;
/*!40000 ALTER TABLE `sys_notification_rule` DISABLE KEYS */;
INSERT INTO `sys_notification_rule` VALUES (1,'测试计划创建通知规则','TEST_PLAN_CREATE_RULE','TEST_PLAN','CREATE',1,'EMAIL',1,1,1,'2025-05-08 11:35:19','2025-05-08 12:24:46',1),(2,'2342','4252','TESTPLAN','UPDATE',4,'email',1,1,1,'2025-05-08 12:28:08','2025-05-08 12:28:08',0),(3,'2342','2452','BUG','CREATE',1,'email',1,1,1,'2025-05-08 12:28:30','2025-05-08 12:28:30',0),(4,'wer','werw','TEST_PLAN','CREATE',1,'email',1,1,1,'2025-05-08 12:30:16','2025-05-08 12:30:23',1),(5,'fsfdsf','sdfs','REPORT','EXECUTE',3,'email,alert,message',0,1,1,'2025-05-08 12:30:46','2025-05-08 12:30:46',0),(6,'测试计划创建通知规则','TEST_PLAN_CREATE_RULE','TEST_PLAN','CREATE',1,'EMAIL',1,1,1,'2025-05-08 12:32:03','2025-05-08 12:32:03',0);
/*!40000 ALTER TABLE `sys_notification_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_notification_template`
--

DROP TABLE IF EXISTS `sys_notification_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_notification_template` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '模板名称',
  `code` varchar(50) NOT NULL COMMENT '模板编码',
  `type` varchar(50) NOT NULL COMMENT '模板类型（EMAIL-邮件, SMS-短信, SYSTEM-系统通知）',
  `title` varchar(200) NOT NULL COMMENT '模板标题',
  `content` text NOT NULL COMMENT '模板内容',
  `params` varchar(1000) DEFAULT NULL COMMENT '模板参数（JSON格式）',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `creator_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `updater_id` bigint DEFAULT NULL COMMENT '更新人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_code` (`code`,`deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通知模板表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_notification_template`
--

LOCK TABLES `sys_notification_template` WRITE;
/*!40000 ALTER TABLE `sys_notification_template` DISABLE KEYS */;
INSERT INTO `sys_notification_template` VALUES (1,'测试计划创建通知','TEST_PLAN_CREATE','EMAIL','新测试计划已创建','您好 {userName}，\n\n新的测试计划 {testPlanName} 已创建。\n\n详情请查看：{testPlanUrl}','{\"userName\":\"用户名\",\"testPlanName\":\"测试计划名称\",\"testPlanUrl\":\"测试计划链接\"}',1,1,1,'2025-05-08 11:35:07','2025-05-08 11:35:07',0),(2,'新模板','EMAIL_TEMPLATE_1746675333610','EMAIL','新主题','234234234','{}',1,1,1,'2025-05-08 11:35:37','2025-05-08 11:35:37',0),(3,'新模板','SMS_TEMPLATE_1746675341634','SMS','新主题','254353{testPlanName}','{}',1,1,1,'2025-05-08 11:35:46','2025-05-08 11:35:46',0),(4,'新模板','SYSTEM_TEMPLATE_1746675359239','SYSTEM','','342t 3t3{testPlanName}{testPlanName}','{}',1,1,1,'2025-05-08 11:36:09','2025-05-08 11:36:09',0);
/*!40000 ALTER TABLE `sys_notification_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_operation_log`
--

DROP TABLE IF EXISTS `sys_operation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_operation_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `log_type` varchar(50) NOT NULL COMMENT '日志类型（OPERATION-操作日志, ERROR-异常日志, PERFORMANCE-性能日志）',
  `log_level` varchar(20) NOT NULL COMMENT '日志级别（INFO, WARN, ERROR, DEBUG）',
  `module` varchar(100) NOT NULL COMMENT '操作模块',
  `content` text NOT NULL COMMENT '日志内容',
  `user_id` bigint DEFAULT NULL COMMENT '操作用户ID',
  `username` varchar(100) DEFAULT NULL COMMENT '操作用户名',
  `ip_address` varchar(100) DEFAULT NULL COMMENT '操作IP地址',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `trace_id` varchar(100) DEFAULT NULL COMMENT '追踪ID',
  `extra_info` text COMMENT '额外信息（JSON格式）',
  PRIMARY KEY (`id`),
  KEY `idx_type_level` (`log_type`,`log_level`),
  KEY `idx_user_time` (`user_id`,`create_time`),
  KEY `idx_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_operation_log`
--

LOCK TABLES `sys_operation_log` WRITE;
/*!40000 ALTER TABLE `sys_operation_log` DISABLE KEYS */;
INSERT INTO `sys_operation_log` VALUES (1,'LOGIN','INFO','AUTH','用户登录成功',1001,'admin','192.168.1.100','2025-05-08 10:15:23','trace-001','{\"browser\":\"Chrome\",\"os\":\"Windows\"}'),(2,'OPERATION','INFO','USER_MGMT','创建新用户：user5',1002,'user1','192.168.1.101','2025-05-08 11:20:35','trace-002','{\"action\":\"create\",\"target\":\"user\"}'),(3,'QUERY','DEBUG','DATA_ANALYSIS','执行数据查询：SELECT * FROM user_stats',1003,'user2','192.168.1.102','2025-05-08 12:30:45','trace-003','{\"query_time\":\"120ms\",\"result_count\":156}'),(4,'OPERATION','WARNING','CONFIG','修改系统配置：max_connections=200',1001,'admin','192.168.1.100','2025-05-08 13:45:12','trace-004','{\"old_value\":100,\"new_value\":200}'),(5,'ERROR','ERROR','API','API调用失败：/api/v1/data',1004,'user3','192.168.1.103','2025-05-08 14:05:33','trace-005','{\"error_code\":500,\"message\":\"内部服务器错误\"}'),(6,'EXPORT','INFO','REPORT','导出报表：月度销售报表',1005,'user4','192.168.1.104','2025-05-08 15:12:48','trace-006','{\"format\":\"xlsx\",\"records\":523}'),(7,'IMPORT','INFO','DATA_MGMT','导入数据：客户信息表',1002,'user1','192.168.1.101','2025-05-08 16:25:10','trace-007','{\"format\":\"csv\",\"records\":89,\"errors\":0}'),(8,'LOGOUT','INFO','AUTH','用户注销',1001,'admin','192.168.1.100','2025-05-08 17:30:22','trace-008','{\"session_duration\":\"7h15m\"}'),(9,'OPERATION','CRITICAL','SECURITY','检测到异常登录尝试',1003,'user2','192.168.1.102','2025-05-08 18:45:37','trace-009','{\"attempts\":5,\"ip\":\"203.0.113.45\"}'),(10,'SCHEDULED','INFO','BACKUP','系统备份完成',1006,'user5','192.168.1.105','2025-05-08 19:55:49','trace-010','{\"size\":\"2.3GB\",\"duration\":\"15m\",\"status\":\"success\"}');
/*!40000 ALTER TABLE `sys_operation_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_param`
--

DROP TABLE IF EXISTS `sys_param`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_param` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `param_key` varchar(100) NOT NULL COMMENT '参数键名',
  `param_value` varchar(500) NOT NULL COMMENT '参数值',
  `param_name` varchar(100) NOT NULL COMMENT '参数名称',
  `param_type` varchar(10) DEFAULT 'string' COMMENT '参数类型（string, number, boolean, json）',
  `is_system` tinyint(1) DEFAULT '0' COMMENT '是否系统内置参数',
  `description` varchar(500) DEFAULT NULL COMMENT '参数描述',
  `created_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `updated_by` bigint DEFAULT NULL COMMENT '更新人ID',
  `updated_at` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_param_key` (`param_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统参数配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_param`
--

LOCK TABLES `sys_param` WRITE;
/*!40000 ALTER TABLE `sys_param` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_param` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_performance_log`
--

DROP TABLE IF EXISTS `sys_performance_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_performance_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `resource_type` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '资源类型（CPU, MEMORY, DISK, NETWORK等）',
  `metric_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '指标名称',
  `metric_value` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '指标值',
  `user_id` bigint DEFAULT NULL COMMENT '操作用户ID',
  `username` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作用户名',
  `ip_address` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'IP地址',
  `log_level` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日志等级',
  `record_time` datetime DEFAULT NULL COMMENT '记录时间',
  `remarks` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_record_time` (`record_time`),
  KEY `idx_resource_type` (`resource_type`),
  KEY `idx_log_level` (`log_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统性能日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_performance_log`
--

LOCK TABLES `sys_performance_log` WRITE;
/*!40000 ALTER TABLE `sys_performance_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_performance_log` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_permission`
--

LOCK TABLES `sys_permission` WRITE;
/*!40000 ALTER TABLE `sys_permission` DISABLE KEYS */;
INSERT INTO `sys_permission` VALUES (1,'系统设置','system:settings','menu',NULL,'/system',NULL,'setting',100,1,1,1,'系统设置菜单',1,'2025-05-07 10:38:06',NULL,NULL),(2,'用户管理','system:user','menu',1,'/system/user','system/user/index','user',1,1,1,1,'用户管理菜单',1,'2025-05-07 10:38:06',NULL,NULL),(3,'用户查询','system:user:list','button',18,NULL,NULL,'',1,1,1,1,'用户查询按钮',1,'2025-05-07 10:38:06',1,'2025-05-07 17:27:14'),(4,'用户新增','system:user:add','button',2,NULL,NULL,NULL,2,1,1,1,'用户新增按钮',1,'2025-05-07 10:38:06',NULL,NULL),(5,'用户修改','system:user:edit','button',2,NULL,NULL,NULL,3,1,1,1,'用户修改按钮',1,'2025-05-07 10:38:06',NULL,NULL),(6,'用户删除','system:user:delete','button',2,NULL,NULL,NULL,4,1,1,1,'用户删除按钮',1,'2025-05-07 10:38:06',NULL,NULL),(7,'重置密码','system:user:reset','button',2,NULL,NULL,NULL,5,1,1,1,'重置密码按钮',1,'2025-05-07 10:38:06',NULL,NULL),(8,'角色管理','system:role','menu',1,'/system/role','system/role/index','peoples',2,1,1,1,'角色管理菜单',1,'2025-05-07 10:38:06',NULL,NULL),(9,'角色查询','system:role:list','button',8,NULL,NULL,NULL,1,1,1,1,'角色查询按钮',1,'2025-05-07 10:38:06',NULL,NULL),(10,'角色新增','system:role:add','button',8,NULL,NULL,NULL,2,1,1,1,'角色新增按钮',1,'2025-05-07 10:38:06',NULL,NULL),(11,'角色修改','system:role:edit','button',8,NULL,NULL,NULL,3,1,1,1,'角色修改按钮',1,'2025-05-07 10:38:06',NULL,NULL),(12,'角色删除','system:role:delete','button',8,NULL,NULL,NULL,4,1,1,1,'角色删除按钮',1,'2025-05-07 10:38:06',NULL,NULL),(13,'角色授权','system:role:authorize','button',8,NULL,NULL,NULL,5,1,1,1,'角色授权按钮',1,'2025-05-07 10:38:06',NULL,NULL),(14,'权限管理','system:permission','menu',1,'/system/permission','system/permission/index','tree',3,1,1,1,'权限管理菜单',1,'2025-05-07 10:38:06',NULL,NULL),(15,'权限查询','system:permission:list','button',14,NULL,NULL,NULL,1,1,1,1,'权限查询按钮',1,'2025-05-07 10:38:06',NULL,NULL),(16,'权限新增','system:permission:add','button',14,NULL,NULL,NULL,2,1,1,1,'权限新增按钮',1,'2025-05-07 10:38:06',NULL,NULL),(17,'权限修改','system:permission:edit','button',14,NULL,NULL,NULL,3,1,1,1,'权限修改按钮',1,'2025-05-07 10:38:06',NULL,NULL),(18,'权限删除','system:permission:delete','button',14,NULL,NULL,NULL,4,1,1,1,'权限删除按钮',1,'2025-05-07 10:38:06',NULL,NULL),(19,'部门管理','system:dept','menu',1,'/system/dept','system/dept/index','tree-table',4,1,1,1,'部门管理菜单',1,'2025-05-07 10:38:06',NULL,NULL),(20,'部门查询','system:dept:list','button',19,NULL,NULL,NULL,1,1,1,1,'部门查询按钮',1,'2025-05-07 10:38:06',NULL,NULL),(21,'部门新增','system:dept:add','button',19,NULL,NULL,NULL,2,1,1,1,'部门新增按钮',1,'2025-05-07 10:38:06',NULL,NULL),(22,'部门修改','system:dept:edit','button',19,NULL,NULL,NULL,3,1,1,1,'部门修改按钮',1,'2025-05-07 10:38:06',NULL,NULL),(23,'部门删除','system:dept:delete','button',19,NULL,NULL,NULL,4,1,1,1,'部门删除按钮',1,'2025-05-07 10:38:06',NULL,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'超级管理员','SUPER_ADMIN','系统超级管理员，拥有所有权限',1,1,1,1,'2025-05-07 10:38:06',NULL,NULL);
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
INSERT INTO `sys_role_permission` VALUES (100,1,1,1,'2025-05-07 10:38:06'),(101,1,2,1,'2025-05-07 10:38:06'),(102,1,8,1,'2025-05-07 10:38:06'),(103,1,14,1,'2025-05-07 10:38:06'),(104,1,19,1,'2025-05-07 10:38:06'),(105,1,3,1,'2025-05-07 10:38:06'),(106,1,4,1,'2025-05-07 10:38:06'),(107,1,5,1,'2025-05-07 10:38:06'),(108,1,6,1,'2025-05-07 10:38:06'),(109,1,7,1,'2025-05-07 10:38:06'),(110,1,9,1,'2025-05-07 10:38:06'),(111,1,10,1,'2025-05-07 10:38:06'),(112,1,11,1,'2025-05-07 10:38:06'),(113,1,12,1,'2025-05-07 10:38:06'),(114,1,13,1,'2025-05-07 10:38:06'),(115,1,15,1,'2025-05-07 10:38:06'),(116,1,16,1,'2025-05-07 10:38:06'),(117,1,17,1,'2025-05-07 10:38:06'),(118,1,18,1,'2025-05-07 10:38:06'),(119,1,20,1,'2025-05-07 10:38:06'),(120,1,21,1,'2025-05-07 10:38:06'),(121,1,22,1,'2025-05-07 10:38:06'),(122,1,23,1,'2025-05-07 10:38:06');
/*!40000 ALTER TABLE `sys_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_sso_config`
--

DROP TABLE IF EXISTS `sys_sso_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_sso_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '配置名称',
  `type` varchar(20) NOT NULL COMMENT '类型：LDAP、OAUTH、SAML',
  `provider` varchar(50) DEFAULT NULL COMMENT '提供商（如Google、Microsoft等）',
  `server_url` varchar(255) DEFAULT NULL COMMENT '服务器URL',
  `domain` varchar(100) DEFAULT NULL COMMENT '域名',
  `client_id` varchar(255) DEFAULT NULL COMMENT '客户端ID',
  `client_secret` varchar(255) DEFAULT NULL COMMENT '客户端密钥',
  `certificate` text COMMENT '证书内容',
  `base_dn` varchar(255) DEFAULT NULL COMMENT 'LDAP的Base DN',
  `username_attribute` varchar(50) DEFAULT 'uid' COMMENT '用户名属性',
  `email_attribute` varchar(50) DEFAULT 'mail' COMMENT '邮箱属性',
  `config` text COMMENT '附加配置（JSON格式）',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `is_default` tinyint(1) DEFAULT '0' COMMENT '是否默认配置：0-否，1-是',
  `creator_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `updater_id` bigint DEFAULT NULL COMMENT '更新人ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`,`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='单点登录配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_sso_config`
--

LOCK TABLES `sys_sso_config` WRITE;
/*!40000 ALTER TABLE `sys_sso_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_sso_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_tool_integration`
--

DROP TABLE IF EXISTS `sys_tool_integration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_tool_integration` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '集成名称',
  `type` varchar(50) NOT NULL COMMENT '集成类型（JIRA, GITHUB, GITLAB, JENKINS等）',
  `url` varchar(255) NOT NULL COMMENT '服务地址',
  `api_key` varchar(255) DEFAULT NULL COMMENT 'API密钥',
  `username` varchar(100) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码（加密存储）',
  `token` varchar(255) DEFAULT NULL COMMENT '访问令牌',
  `config` text COMMENT '附加配置（JSON格式）',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `creator_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `updater_id` bigint DEFAULT NULL COMMENT '更新人ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name_type` (`name`,`type`,`deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='第三方工具集成表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_tool_integration`
--

LOCK TABLES `sys_tool_integration` WRITE;
/*!40000 ALTER TABLE `sys_tool_integration` DISABLE KEYS */;
INSERT INTO `sys_tool_integration` VALUES (1,'JIRA集成','JIRA','https://jira.example.com','jira-api-key','jira-user','jira-password',NULL,'{\"defaultProject\":\"PROJA\"}',1,1,1,NULL,NULL,0);
/*!40000 ALTER TABLE `sys_tool_integration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_upgrade_record`
--

DROP TABLE IF EXISTS `sys_upgrade_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_upgrade_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `from_version` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '原版本号',
  `to_version` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '升级到版本号',
  `upgrade_time` datetime NOT NULL COMMENT '升级时间',
  `upgrade_user` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '执行升级的用户',
  `status` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '升级状态（成功、失败、进行中）',
  `error_message` text COLLATE utf8mb4_general_ci COMMENT '错误信息',
  `backup_id` bigint DEFAULT NULL COMMENT '关联的备份ID',
  `duration` int DEFAULT NULL COMMENT '升级耗时（秒）',
  `remarks` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统升级记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_upgrade_record`
--

LOCK TABLES `sys_upgrade_record` WRITE;
/*!40000 ALTER TABLE `sys_upgrade_record` DISABLE KEYS */;
INSERT INTO `sys_upgrade_record` VALUES (1,'1.0.0','1.1.0','2023-11-15 10:30:00','admin','成功',NULL,1,120,'正常升级，无异常','2025-05-08 20:38:36'),(2,'1.1.0','1.2.0','2024-01-10 14:45:00','admin','成功',NULL,2,180,'更新数据结构，迁移测试数据','2025-05-08 20:38:36'),(3,'1.2.0','1.3.0','2024-03-20 09:15:00','admin','成功',NULL,3,210,'重大版本升级，增加了新功能模块','2025-05-08 20:38:36');
/*!40000 ALTER TABLE `sys_upgrade_record` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','$2a$10$uPNjJ05jb1L.dnsMKvYNqe8fcqEGMR0zjY.YzFT2TvA4a9Xl9J3wS','系统管理员','Admin',0,'admin@example.com',NULL,NULL,1,0,1,NULL,NULL,NULL,1,'2025-05-07 10:38:06',NULL,NULL),(103,'234','$2a$10$gJPmzNhXRcR77t.AKx7Zx.ACu0FoFWkv7Xn0.FgE7R2dL8g5m4iPO','2341421','',0,'','','',NULL,0,1,NULL,NULL,'',1,'2025-05-07 17:20:23',1,'2025-05-07 17:20:29');
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
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (100,1,1,1,'2025-05-07 10:38:06');
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_subscription`
--

DROP TABLE IF EXISTS `sys_user_subscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_subscription` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `rule_id` bigint NOT NULL COMMENT '规则ID',
  `notify_type` varchar(50) NOT NULL COMMENT '通知方式（EMAIL-邮件, SMS-短信, SYSTEM-系统通知）',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `creator_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `updater_id` bigint DEFAULT NULL COMMENT '更新人ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_rule` (`user_id`,`rule_id`,`deleted`),
  KEY `idx_rule` (`rule_id`,`status`,`deleted`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户订阅表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_subscription`
--

LOCK TABLES `sys_user_subscription` WRITE;
/*!40000 ALTER TABLE `sys_user_subscription` DISABLE KEYS */;
INSERT INTO `sys_user_subscription` VALUES (1,1,1,'EMAIL',1,1,1,'2025-05-08 11:46:44','2025-05-08 11:46:44',0),(2,1,6,'email',1,1,1,'2025-05-08 12:55:29','2025-05-08 12:55:29',0);
/*!40000 ALTER TABLE `sys_user_subscription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_version_info`
--

DROP TABLE IF EXISTS `sys_version_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_version_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `version` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '系统版本号',
  `release_date` datetime NOT NULL COMMENT '发布日期',
  `release_notes` text COLLATE utf8mb4_general_ci COMMENT '版本说明',
  `is_current` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为当前版本（1是，0否）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_version` (`version`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统版本信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_version_info`
--

LOCK TABLES `sys_version_info` WRITE;
/*!40000 ALTER TABLE `sys_version_info` DISABLE KEYS */;
INSERT INTO `sys_version_info` VALUES (1,'1.0.0','2023-01-15 00:00:00','初始版本发布\n- 测试管理基础功能\n- 简单的接口测试功能\n- 用户与权限管理基础功能',0,'2025-05-08 18:46:47','2025-05-08 18:46:47'),(2,'1.1.0','2023-03-20 00:00:00','功能更新：\n- 完善测试管理模块\n- 增强接口测试功能\n- 新增UI测试基础功能\n- 项目管理基础功能',0,'2025-05-08 18:46:47','2025-05-08 18:46:47'),(3,'1.2.0','2023-06-10 00:00:00','功能更新：\n- UI测试用例管理完善\n- 测试套件管理\n- 元素定位器管理\n- 系统参数配置',0,'2025-05-08 18:46:47','2025-05-08 18:46:47'),(4,'1.3.0','2023-09-05 00:00:00','功能更新：\n- 测试执行模块优化\n- 测试报告功能增强\n- 数据字典管理\n- 邮件服务配置',0,'2025-05-08 18:46:47','2025-05-08 18:46:47'),(5,'2.0.0','2024-01-10 00:00:00','重大版本更新：\n- 全新的UI界面设计\n- 测试管理模块全面升级\n- 接口测试功能全面增强\n- UI测试执行引擎优化\n- 系统性能大幅提升',1,'2025-05-08 18:46:47','2025-05-08 18:46:47');
/*!40000 ALTER TABLE `sys_version_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_setting`
--

DROP TABLE IF EXISTS `system_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_setting` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '设置ID',
  `category` varchar(50) NOT NULL COMMENT '分类',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `code` varchar(100) NOT NULL COMMENT '代码',
  `value` text COMMENT '值',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `is_system` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否系统参数',
  `created_by` int DEFAULT NULL COMMENT '创建人ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`),
  KEY `created_by` (`created_by`),
  CONSTRAINT `system_setting_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统设置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_setting`
--

LOCK TABLES `system_setting` WRITE;
/*!40000 ALTER TABLE `system_setting` DISABLE KEYS */;
/*!40000 ALTER TABLE `system_setting` ENABLE KEYS */;
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
INSERT INTO `test_case` VALUES (2,'测试用例1','1','已登录成功','已评审','张三',NULL,'2024-11-25 09:25:10',NULL,'功能测试',NULL,NULL,NULL,NULL,1),(3,'测试用例2','2','已登录成功','未评审','李四',NULL,'2024-11-25 09:25:10',NULL,'功能测试',NULL,NULL,NULL,NULL,1),(4,'测试用例3','3','已登录成功','已评审','王五',NULL,'2024-11-25 09:25:10',NULL,'功能测试',NULL,NULL,NULL,NULL,1),(5,'测试用例4','4','已登录成功','已评审','赵六',NULL,'2024-11-25 09:25:10',NULL,'接口测试',NULL,NULL,NULL,NULL,1),(6,'测试用例5','1','已登录成功','已评审','11',NULL,'2024-11-25 09:25:10',NULL,'性能测试',NULL,NULL,NULL,NULL,1),(7,'测试用例6','1','已登录成功','已评审','11',NULL,'2024-11-25 09:25:10',NULL,'安全测试',NULL,NULL,NULL,NULL,1),(8,'测试用例7','1','已登录成功','待评审','11',NULL,'2024-11-25 09:25:10',NULL,'兼容性测试',NULL,NULL,NULL,NULL,1),(9,'测试用例8','1','已登录成功','待评审','11',NULL,'2024-11-25 09:25:10',NULL,'兼容性测试',NULL,NULL,NULL,NULL,1),(10,'测试用例9','1','已登录成功','待评审','11',NULL,'2024-11-25 09:25:10',NULL,'兼容性测试',NULL,NULL,NULL,NULL,1),(11,'测试用例10','2','已登录成功','待评审','11',NULL,'2024-11-25 09:25:10',NULL,'兼容性测试',NULL,NULL,NULL,NULL,1),(12,'测试用例11','3','已登录成功','待评审','11',NULL,'2024-11-25 09:25:10',NULL,'兼容性测试',NULL,NULL,NULL,NULL,1),(13,'十分的','4',NULL,'待评审','fs',NULL,'2024-11-26 08:00:00',NULL,'功能测试',NULL,NULL,NULL,NULL,1);
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
INSERT INTO `test_plan` VALUES (1,'Plan_1','Description for Plan_1',29,'Active','Participant_1','2024-10-17','2024-11-03','2024-11-14 16:00:00','2024-11-14 16:00:00','v1'),(2,'Plan_2','Description for Plan_222222222222222222',75,'Completed','Participant_2','2024-10-25','2024-11-18','2024-11-14 16:00:00','2024-11-25 07:01:01','v22222222222222'),(9,'测试计划3','',1,'01','',NULL,NULL,'2024-11-25 07:02:48','2024-11-25 07:02:48',''),(10,'测试计划4','',1,'','',NULL,NULL,'2024-11-25 07:02:54','2024-11-25 07:02:54',''),(11,'测试计划5','',1,'','',NULL,NULL,'2024-11-25 07:03:00','2024-11-25 07:03:00',''),(12,'测试计划6','',1,'','',NULL,NULL,'2024-11-25 07:03:05','2024-11-25 07:03:05',''),(13,'测试计划7','',1,'','',NULL,NULL,'2024-11-25 07:03:10','2024-11-25 07:03:10',''),(14,'测试计划8','',1,'','',NULL,NULL,'2024-11-25 07:03:15','2024-11-25 07:03:15',''),(15,'测试计划9','',1,'','',NULL,NULL,'2024-11-25 07:03:19','2024-11-25 07:03:19',''),(17,'测试计划11','',1,'','',NULL,NULL,'2024-11-25 07:03:28','2024-11-25 07:03:28',''),(18,'测试计划12','',1,'','',NULL,NULL,'2024-11-25 07:04:19','2024-11-25 07:04:19',''),(19,'Test Plan 3','This is a description for Test Plan 1.',101,'pending','1,2,3','2024-11-20','2024-11-30','2025-04-28 08:53:27','2025-04-28 08:53:27','v1.0');
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
INSERT INTO `test_report` VALUES (1,'报告标题1',1,'2023-04-01 02:00:00',101,'1.0.0','张三,李四',2,'报告概括1','测试结论1','附件1.pdf','1001,1002','2001,2002',1),(2,'报告标题222222',2,'2023-04-02 03:00:00',102,'1.0.1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,2),(3,'报告标题3',3,'2023-04-03 04:00:00',103,'1.0.2','孙七,周八',4,'报告概括3','测试结论3','附件3.pdf','1005,1006','2005,2006',3),(4,'报告标题4',4,'2023-04-04 05:00:00',104,'1.0.3','吴九,郑十',5,'报告概括4','测试结论4','附件4.pdf','1007,1008','2007,2008',4),(5,'报告标题5',5,'2023-04-05 06:00:00',105,'1.0.4','钱十一,周十二',6,'报告概括5','测试结论5','附件5.pdf','1009,1010','2009,2010',5),(7,'报告标题7',7,'2023-04-07 08:00:00',107,'1.0.6','李十五,周十六',8,'报告概括7','测试结论7','附件7.pdf','1013,1014','2013,2014',7),(8,'报告标题8',8,'2023-04-08 09:00:00',108,'1.0.7','陈十七,吴十八',9,'报告概括8','测试结论8','附件8.pdf','1015,1016','2015,2016',8),(9,'报告标题9',9,'2023-04-09 10:00:00',109,'1.0.8','杨十九,郑二十',10,'报告概括9','测试结论9','附件9.pdf','1017,1018','2017,2018',9),(10,'报告标题10',10,'2023-04-10 11:00:00',110,'1.0.9','刘二十一,王二十二',11,'报告概括10','测试结论10','附件10.pdf','1019,1020','2019,2020',10),(11,'报告标题11',11,'2023-04-11 12:00:00',111,'1.1.0','张二十三,李二十四',12,'报告概括11','测试结论11','附件11.pdf','1021,1022','2021,2022',11),(12,'报告标题12',12,'2023-04-12 13:00:00',112,'1.1.1','王二十五,赵二十六',13,'报告概括12','测试结论12','附件12.pdf','1023,1024','2023,2024',12);
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
INSERT INTO `ui_element` VALUES (3,'tdfgd',1,'CSS','ngghg',NULL,NULL,'当前用户','2025-05-06 06:14:01',NULL);
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
INSERT INTO `ui_element_group` VALUES (1,'sdfsf','',1,'当前用户','2025-05-06 06:05:39','2025-05-06 14:14:09');
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
INSERT INTO `ui_test_case` VALUES (1,'UI-001','用户登录功能测试',1,'P1','Chrome','系统已部署且可访问','验证用户使用有效凭据可以成功登录系统',30,1,'failureOnly','已执行','通过','2025-05-06 02:20:35','admin','2025-04-30 07:17:22','2025-05-06 02:20:35');
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
INSERT INTO `ui_test_config` VALUES (1,'默认Chrome配置','BROWSER','CHROME','latest','DESKTOP',NULL,'WINDOWS',NULL,10,30,30,NULL,NULL,NULL,0,NULL,NULL,1,1,NULL,'2025-05-06 17:19:18',NULL,'2025-05-06 17:19:18'),(2,'默认等待时间配置','WAIT_TIME',NULL,NULL,NULL,NULL,NULL,NULL,10,30,30,NULL,NULL,NULL,0,NULL,NULL,1,1,NULL,'2025-05-06 17:19:18',NULL,'2025-05-06 17:19:18'),(3,'默认截图策略配置','SCREENSHOT',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'ON_FAILURE',NULL,NULL,0,NULL,NULL,1,1,NULL,'2025-05-06 17:19:18',NULL,'2025-05-06 17:19:18'),(4,'默认重试配置','RETRY',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2,5,0,NULL,NULL,1,1,NULL,'2025-05-06 17:19:18',NULL,'2025-05-06 17:19:18'),(5,'Firefox浏览器配置','BROWSER','FIREFOX','latest','DESKTOP',NULL,'WINDOWS',NULL,15,30,30,NULL,NULL,NULL,0,NULL,NULL,0,1,NULL,'2025-05-06 09:25:56',NULL,'2025-05-06 09:25:56'),(6,'gj','BROWSER','CHROME','2342','DESKTOP',NULL,'WINDOWS','',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,0,0,NULL,'2025-05-06 09:32:39',NULL,'2025-05-06 09:47:25'),(7,'fr','BROWSER','EDGE','sdfsf','DESKTOP',NULL,'WINDOWS','',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,0,1,NULL,'2025-05-06 09:47:35',NULL,'2025-05-06 09:47:35');
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
INSERT INTO `ui_test_execution` VALUES (1,'测试执行01','case',1,'测试执行01','waiting','admin','chrome',NULL,NULL,NULL,0,0,1,'{\"screenshotOnFailure\": true, \"retryCount\": 1}','2025-05-06 15:05:03','2025-05-06 15:05:03');
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
INSERT INTO `ui_test_execution_detail` VALUES (1,1,1,'待获取','waiting',NULL,NULL,NULL,NULL,NULL,0,0,0,0,'2025-05-06 15:05:03','2025-05-06 15:05:03');
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
INSERT INTO `ui_test_execution_queue` VALUES (1,1,0,'waiting','2025-05-06 07:05:04',NULL,NULL,NULL,'2025-05-06 15:05:03','2025-05-06 15:05:03');
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
INSERT INTO `ui_test_parameter` VALUES (1,'UI-001','username','admin','2025-04-30 07:17:22',NULL),(2,'UI-001','password','123456','2025-04-30 07:17:22',NULL);
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
INSERT INTO `ui_test_report` VALUES (1,101,'登录功能自动化测试报告','用例执行','Chrome','Chrome 95.0','2023-05-15 02:30:00','2023-05-15 02:35:10',310000,5,4,1,0,80,'测试人员1','{\"summary\":\"登录功能自动化测试报告\",\"details\":\"详细内容...\"}',NULL,'已生成','2025-05-06 08:25:32','2025-05-06 08:25:32','2025-05-06 08:25:32'),(2,101,'登录功能自动化测试报告333','用例执行','Chrome','Chrome 95.0','2023-05-15 02:30:00','2023-05-15 02:35:10',310000,5,5,0,0,100,'测试人员1','{\"summary\":\"登录功能自动化测试报告\",\"details\":\"详细内容...\"}',NULL,'已生成','2025-05-06 08:33:13','2025-05-06 08:33:13','2025-05-06 08:33:13');
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
INSERT INTO `ui_test_step` VALUES (1,'UI-001',1,'navigate',NULL,NULL,NULL,NULL,'http://localhost:8080/login','打开登录页面','2025-04-30 07:17:22',NULL),(2,'UI-001',2,'input','id','username','admin',NULL,NULL,'输入用户名','2025-04-30 07:17:22',NULL),(3,'UI-001',3,'input','id','password','123456',NULL,NULL,'输入密码','2025-04-30 07:17:22',NULL),(4,'UI-001',4,'click','css','button[type=\'submit\']',NULL,NULL,NULL,'点击登录按钮','2025-04-30 07:17:22',NULL),(5,'UI-001',5,'assertText','css','.welcome-message','欢迎回来',NULL,NULL,'验证登录成功后的欢迎信息','2025-04-30 07:17:22',NULL);
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
INSERT INTO `ui_test_suite` VALUES (1,'22',1,'',0,NULL,'当前用户','2025-05-05 19:09:10',NULL,0,'未执行',NULL,'手动','中',NULL,30,0,NULL),(2,'12313',1,'',0,NULL,'当前用户','2025-05-05 19:09:34','2025-05-05 20:28:48',0,'未执行',NULL,'手动','中',NULL,30,0,NULL),(3,'dsdfsfsf',1,'',0,NULL,'当前用户','2025-05-05 19:10:52','2025-05-05 19:39:14',0,'未执行',NULL,'手动','中',NULL,30,0,NULL);
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
INSERT INTO `user` VALUES (1,'Tony',26);
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
INSERT INTO `user_login` VALUES (3,'admin','$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi',NULL,NULL,NULL,'2025-05-14 08:53:19',1);
/*!40000 ALTER TABLE `user_login` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warehouse`
--

DROP TABLE IF EXISTS `warehouse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warehouse` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '仓库ID',
  `name` varchar(100) NOT NULL COMMENT '仓库名称',
  `code` varchar(50) NOT NULL COMMENT '仓库编码',
  `address` varchar(255) DEFAULT NULL COMMENT '仓库地址',
  `manager_id` int DEFAULT NULL COMMENT '仓库管理员ID',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `is_active` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否激活',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`),
  KEY `manager_id` (`manager_id`),
  CONSTRAINT `warehouse_ibfk_1` FOREIGN KEY (`manager_id`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='仓库表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warehouse`
--

LOCK TABLES `warehouse` WRITE;
/*!40000 ALTER TABLE `warehouse` DISABLE KEYS */;
/*!40000 ALTER TABLE `warehouse` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-14 17:33:38
