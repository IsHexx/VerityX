# VerityX 测试平台

VerityX 是一个综合性测试管理平台，用于管理测试用例、接口测试、缺陷跟踪和测试报告等。该平台由前端（Vue.js）和后端（Spring Boot）两部分组成。

## 系统要求

- JDK 17 或更高版本
- Maven 3.6.3 或更高版本
- MySQL 8.0 或更高版本
- Node.js 16.x 或更高版本
- npm 8.x 或更高版本

## 数据库配置

1. 创建一个名为 `verityx` 的数据库：

```sql
CREATE DATABASE verityx CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

2. 使用 `verityxDatabase/CreateTable.sql` 文件中的 SQL 语句创建表结构：

```bash
mysql -u root -p verityx < verityxDatabase/CreateTable.sql
```

或者通过 MySQL 客户端工具导入 SQL 文件。

## 后端项目启动

1. 配置数据库连接

编辑 `verityx/src/main/resources/application.properties` 文件，确保数据库连接信息正确：

```properties
spring.datasource.url: jdbc:mysql://localhost:3306/verityx?useSSL=true&serverTimezone=UTC
spring.datasource.username: root
spring.datasource.password: 12345
```

根据你的实际数据库用户名和密码修改上述配置。

2. 构建并运行项目

```bash
# 进入后端项目目录
cd verityx

# 清理旧编译文件
mvn clean

# 重新编译
mvn compile

# 直接运行
mvn spring-boot:run
```

后端服务将在 `http://localhost:8089` 运行。

## 前端项目启动

1. 安装依赖

```bash
# 进入前端项目目录
cd front

# 安装依赖
npm install
```

2. 启动开发服务器

```bash
# 启动开发服务器
npm run serve
```

前端应用将在 `http://localhost:8080` 运行。

## 默认用户

系统默认包含一个管理员用户：

- 用户名：admin
- 密码：123456 (如果你忘记了密码，可以通过下面的 SQL 语句重置)

如果忘记了密码，可以执行以下 SQL 语句重置为 `123456`：

```sql
UPDATE user_login 
SET password = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi' 
WHERE username = 'admin';
```

## 项目结构

### 后端结构

- `verityx/src/main/java/com/example/verityx/controller`: 控制器层，处理 HTTP 请求
- `verityx/src/main/java/com/example/verityx/service`: 服务层，实现业务逻辑
- `verityx/src/main/java/com/example/verityx/entity`: 实体类，对应数据库表
- `verityx/src/main/java/com/example/verityx/mapper`: Mapper 接口，数据库操作
- `verityx/src/main/resources/mapper`: MyBatis XML 映射文件

### 前端结构

- `front/src/views`: 页面组件
- `front/src/components`: 可复用组件
- `front/src/api`: API 调用接口
- `front/src/router`: 路由配置
- `front/src/store`: Vuex 状态管理

## API 文档

API 文档可以通过 Swagger UI 访问：`http://localhost:8089/swagger-ui.html`

## 常见问题

1. 如果启动时报端口被占用错误，可以修改 `application.properties` 中的 `server.port` 属性。
2. 如果数据库连接失败，请检查数据库用户名、密码和数据库名称是否正确。
3. 如果前端无法连接后端 API，请检查 `front/vue.config.js` 中的代理配置是否正确。 