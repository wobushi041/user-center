# User Center 用户中心

User Center 是一个前后端分离的用户管理项目，提供用户注册、登录、注销、当前用户查询，以及管理员用户检索和删除能力。仓库包含 Vue 3 前端和 Spring Boot 后端，适合作为用户认证、Session 登录态与基础权限控制的学习项目。

## 功能

| 模块 | 能力 |
| --- | --- |
| 用户认证 | 注册、登录、注销和 Session 登录态维护 |
| 用户信息 | 获取当前登录用户并返回脱敏信息 |
| 后台管理 | 管理员按用户名检索和删除用户 |
| 统一响应 | 统一响应对象、错误码和全局异常处理 |
| 接口文档 | 集成 Knife4j / OpenAPI 文档 |

## 技术栈

| 前端 | 后端 |
| --- | --- |
| Vue 3、TypeScript、Vue Router | Java 8、Spring Boot 2.6.13 |
| Ant Design Vue、Pinia | MyBatis-Plus 3.5.2、MySQL |
| Axios、Vue CLI 5 | Knife4j 4.4.0、Maven |

## 项目结构

```text
user-center/
├─ user-center-frontend-vue/   # Vue 3 前端
├─ user-center-backend/        # Spring Boot 后端
├─ 代码规范.md                  # Java 注释与格式规范
└─ README.md                    # 项目总览
```

子项目的补充说明见：

- [前端 README](user-center-frontend-vue/README.md)
- [后端 README](user-center-backend/README.md)
- [代码规范](代码规范.md)

## 本地启动

### 1. 准备环境

- JDK 8
- Maven 3.6+
- Node.js 与 npm
- MySQL 5.7+（推荐 MySQL 8）

### 2. 初始化数据库

在 MySQL 客户端中创建数据库并执行后端 SQL 文件：

```sql
CREATE DATABASE IF NOT EXISTS `center-backend`;
USE `center-backend`;
```

随后执行 [`user-center-backend/src/main/resources/sql/scheme.sql`](user-center-backend/src/main/resources/sql/scheme.sql)。

### 3. 配置并启动后端

进入 `user-center-backend`，从模板创建本地配置：

```powershell
Copy-Item .\src\main\resources\application-template.yml .\src\main\resources\application.yml
```

修改 `application.yml` 中的数据库账号和密码，然后启动服务：

```powershell
mvn spring-boot:run
```

默认地址：

- API：`http://localhost:8080/api`
- Knife4j：`http://localhost:8080/api/doc.html`
- OpenAPI：`http://localhost:8080/api/v3/api-docs`

`application.yml` 包含本地凭据，已加入 Git 忽略规则，请勿提交真实账号或密码。

### 4. 启动前端

在另一个终端进入 `user-center-frontend-vue`：

```powershell
npm install
npm run serve
```

前端开发服务器默认运行在 `http://localhost:3000`，通过 Vue CLI 开发代理访问后端 `/api`。

## 主要接口

以下接口均使用 `/api` 作为服务前缀：

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| `POST` | `/user/register` | 注册用户 |
| `POST` | `/user/login` | 登录用户 |
| `POST` | `/user/logout` | 注销当前用户 |
| `GET` | `/user/current` | 获取当前登录用户 |
| `GET` | `/user/search` | 管理员检索用户 |
| `GET` | `/user/delete` | 管理员删除用户 |

## 开发约定

后端 Java 文档注释、字段说明、测试注释和格式要求统一遵循根目录的 [`代码规范.md`](代码规范.md)。修改业务代码时，应保持 Service 接口描述业务契约、ServiceImpl 描述具体实现机制，并避免在日志、配置键或字符串常量中引入格式化改动。
