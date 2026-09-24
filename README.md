# User Center

> 一个基于 Vue 3 与 Spring Boot 的前后端分离用户管理系统，覆盖账号认证、Session 登录态、用户信息脱敏和管理员权限控制。

![Java](https://img.shields.io/badge/Java-8-E76F00?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.6.13-6DB33F?style=flat-square)
![Vue](https://img.shields.io/badge/Vue-3.2.13-42B883?style=flat-square)
![TypeScript](https://img.shields.io/badge/TypeScript-4.5-3178C6?style=flat-square)
![MySQL](https://img.shields.io/badge/MySQL-5.7%2B-4479A1?style=flat-square)

## 项目简介

User Center 是一个完整的用户管理学习项目。前端负责注册、登录、用户展示与后台管理界面，后端负责账号校验、密码加密、Session 登录态、权限判断、用户数据脱敏和统一异常响应。

项目采用单仓库管理前后端，适合用于理解一套基础用户系统从页面交互、接口调用到数据库持久化的完整链路。

## 核心能力

| 模块 | 主要能力 |
| --- | --- |
| 账号认证 | 用户注册、登录、注销与登录状态恢复 |
| 参数校验 | 校验账号长度、密码长度、特殊字符、重复账号和两次密码一致性 |
| 密码保护 | 使用盐值与 MD5 摘要保存密码，接口响应不返回密码字段 |
| 用户管理 | 获取当前用户、按用户名检索用户、删除指定用户 |
| 权限控制 | 区分普通用户与管理员，限制用户检索和删除操作 |
| 接口治理 | 统一响应结构、业务错误码、全局异常处理与 Knife4j 文档 |

## 技术架构

```text
浏览器
  │
  ├─ 页面与状态：Vue 3 + TypeScript + Pinia + Ant Design Vue
  │
  └─ HTTP / Cookie Session
        │
        ▼
Spring Boot 2.6.13  (:8080, /api)
  ├─ Controller      接收请求与权限检查
  ├─ Service         注册、登录与用户脱敏
  ├─ MyBatis-Plus    数据访问与逻辑删除
  ├─ MySQL           用户数据持久化
  └─ Knife4j         OpenAPI 接口文档
```

### 前端

- Vue 3、TypeScript 与 Vue Router 构建单页应用。
- Pinia 保存当前登录用户状态。
- Ant Design Vue 提供表单、导航和管理表格组件。
- Axios 统一发送 API 请求并携带 Session Cookie。
- Vue CLI 开发代理将 `/api` 请求转发到后端服务。

### 后端

- Spring Boot 提供 REST API 与 Web MVC 配置。
- MyBatis-Plus 完成用户数据查询、保存和逻辑删除。
- `HttpSession` 保存登录用户，Service 层返回脱敏后的用户信息。
- `BaseResponse`、`ErrorCode` 和全局异常处理器统一接口返回。
- Knife4j 与 Springdoc 提供可交互接口文档。

## 页面与接口

### 页面路由

| 路由 | 页面 | 说明 |
| --- | --- | --- |
| `/` | 首页 | 展示当前用户和项目入口 |
| `/user/login` | 登录页 | 用户账号登录 |
| `/user/register` | 注册页 | 创建用户账号 |
| `/admin/userManage` | 用户管理页 | 管理员检索和删除用户 |

### 后端接口

以下路径均以 `http://localhost:8080/api` 为基础地址：

| 方法 | 路径 | 权限 | 说明 |
| --- | --- | --- | --- |
| `POST` | `/user/register` | 公开 | 注册用户 |
| `POST` | `/user/login` | 公开 | 登录并创建 Session |
| `POST` | `/user/logout` | 已登录 | 注销当前用户 |
| `GET` | `/user/current` | 已登录 | 获取当前登录用户 |
| `GET` | `/user/search` | 管理员 | 按用户名检索用户 |
| `GET` | `/user/delete` | 管理员 | 删除指定用户 |

更完整的请求参数和响应结构可在后端启动后访问 Knife4j 文档查看。

## 项目结构

```text
user-center/
├─ user-center-frontend-vue/
│  ├─ public/                       # 静态资源
│  └─ src/
│     ├─ api/                       # 用户接口封装
│     ├─ components/                # 公共组件
│     ├─ layouts/                   # 页面布局
│     ├─ pages/                     # 首页、认证页和管理页
│     ├─ router/                    # 路由配置
│     ├─ store/                     # Pinia 登录用户状态
│     └─ request.ts                 # Axios 实例与响应拦截
├─ user-center-backend/
│  ├─ src/main/java/org/wobushi041/centerbackend/
│  │  ├─ common/                    # 统一响应与错误码
│  │  ├─ config/                    # CORS 与接口文档配置
│  │  ├─ controller/                # 用户接口
│  │  ├─ exception/                 # 业务异常与全局处理
│  │  ├─ mapper/                    # MyBatis-Plus Mapper
│  │  ├─ model/                     # 用户实体与请求对象
│  │  └─ service/                   # 用户业务接口与实现
│  └─ src/main/resources/
│     ├─ application-template.yml   # 脱敏配置模板
│     ├─ mapper/                    # MyBatis XML
│     └─ sql/scheme.sql             # 用户表结构
└─ README.md                         # 项目总览
```

## 本地运行

以下命令以 Windows PowerShell 为例。

### 1. 克隆项目

```powershell
git clone git@github.com:wobushi041/user-center.git
cd .\user-center
```

### 2. 准备运行环境

| 环境 | 要求 |
| --- | --- |
| JDK | 8 |
| Maven | 3.6+ |
| Node.js | 建议使用当前 LTS 版本 |
| MySQL | 5.7+，推荐 8.x |

### 3. 初始化数据库

在 MySQL 客户端执行：

```sql
CREATE DATABASE IF NOT EXISTS `center-backend`;
USE `center-backend`;
```

随后执行建表脚本：

`user-center-backend/src/main/resources/sql/scheme.sql`

### 4. 配置并启动后端

```powershell
cd .\user-center-backend
Copy-Item .\src\main\resources\application-template.yml .\src\main\resources\application.yml
```

编辑 `application.yml`，填写本地 MySQL 用户名和密码，然后启动：

```powershell
mvn spring-boot:run
```

后端启动后可访问：

- API 基础地址：`http://localhost:8080/api`
- Knife4j 文档：`http://localhost:8080/api/doc.html`
- OpenAPI 数据：`http://localhost:8080/api/v3/api-docs`

### 5. 启动前端

打开另一个终端，从仓库根目录执行：

```powershell
cd .\user-center-frontend-vue
npm install
npm run serve
```

前端默认访问地址为 `http://localhost:3000`。开发环境会将 `/api` 请求代理到 `http://localhost:8080`。

## 构建与验证

后端需要使用 JDK 8：

```powershell
cd .\user-center-backend
mvn -DskipTests package
mvn -DskipTests=false -Dtest=UserServiceImplTest test
```

前端检查命令：

```powershell
cd .\user-center-frontend-vue
npm run lint
npm run build
```

## 配置与安全

- 本地数据库凭据保存在 `application.yml`，该文件已加入 Git 忽略规则。
- 仓库只提交 `application-template.yml`，其中不包含真实账号和密码。
- 前端 API 默认使用同源地址；跨域部署时通过 `VUE_APP_API_BASE_URL` 指定后端地址。
- 前端请求需要携带 Cookie，部署到不同域名时需同步检查后端 CORS 与 Session Cookie 配置。
- 当前密码方案用于学习基础认证流程；生产系统应改用 BCrypt、Argon2 等专用密码哈希算法。

## 开发规范

- 类、接口、方法、字段和依赖使用规范化中文 Javadoc。
- Service 接口描述业务契约，ServiceImpl 描述具体实现机制。
- 保持中文与英文、数字、标识符之间的半角空格。
- 调整注释或格式时，不修改现有业务字符串、配置键和业务逻辑。

## 子项目文档

- [前端说明](user-center-frontend-vue/README.md)
- [后端说明](user-center-backend/README.md)
