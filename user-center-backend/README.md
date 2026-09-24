# User Center 后端

User Center 的 Spring Boot 服务，负责用户注册、登录、注销、会话查询和管理员操作，并提供统一响应、异常处理与 Knife4j 接口文档。项目整体介绍见[仓库根 README](../README.md)。

作者：wobushi041

## 技术与服务

| 组件 | 职责 |
| --- | --- |
| Java 8、Spring Boot 2.6.13 | Web 服务与应用基础设施 |
| MyBatis-Plus 3.5.2 | 用户数据访问与逻辑删除 |
| MySQL | 用户账号与资料持久化 |
| Spring Session | 登录会话抽象 |
| Knife4j、Springdoc | OpenAPI 文档与调试页面 |
| Maven | 依赖管理、测试与构建 |

HTTP 服务默认监听 `8080`，统一上下文路径为 `/api`。端口、数据库、会话、逻辑删除、接口文档和 CORS 配置均以 [`application-template.yml`](src/main/resources/application-template.yml) 为模板。

## 本地启动

以下步骤以 Windows PowerShell、`user-center-backend` 目录为起点。

1. 准备 JDK 8、Maven 3.6+ 和 MySQL 5.7+。
2. 在 MySQL 中创建 `center-backend` 数据库，再执行 [`src/main/resources/sql/scheme.sql`](src/main/resources/sql/scheme.sql) 创建用户表。
3. 如果本地尚无 `src/main/resources/application.yml`，复制配置模板：

   ```powershell
   Copy-Item .\src\main\resources\application-template.yml .\src\main\resources\application.yml
   ```

4. 在本地 `application.yml` 中填写数据库连接信息，然后启动服务：

   ```powershell
   mvn spring-boot:run
   ```

服务启动后可访问：

- API 基础地址：`http://localhost:8080/api`
- Knife4j 文档：`http://localhost:8080/api/doc.html`
- OpenAPI 数据：`http://localhost:8080/api/v3/api-docs`

需要联调页面时，再按[前端 README](../user-center-frontend-vue/README.md)启动客户端。

## 主要接口

下表路径均以 `/api` 为前缀。实际请求字段、响应结构与权限规则以控制器和接口文档为准。

| 方法与路径 | 用途 | 访问要求 |
| --- | --- | --- |
| `POST /user/register` | 注册用户并返回用户 ID | 公开 |
| `POST /user/login` | 校验账号密码并建立会话 | 公开 |
| `POST /user/logout` | 注销当前会话 | 已登录 |
| `GET /user/current` | 获取当前登录用户 | 已登录 |
| `GET /user/search` | 按用户名检索用户 | 管理员 |
| `GET /user/delete?id=...` | 逻辑删除指定用户 | 管理员 |

## 源码位置

```text
src/main/
├─ java/org/wobushi041/centerbackend/
│  ├─ common/       # 统一响应与错误码
│  ├─ config/       # CORS 与 Knife4j 配置
│  ├─ constant/     # 用户与权限常量
│  ├─ controller/   # 用户 HTTP 接口
│  ├─ exception/    # 业务异常与全局处理
│  ├─ mapper/       # MyBatis-Plus Mapper
│  ├─ model/        # 用户实体与请求对象
│  └─ service/      # 用户业务接口与实现
└─ resources/
   ├─ application-template.yml
   ├─ mapper/UserMapper.xml
   └─ sql/scheme.sql
```

## 构建与测试

```powershell
mvn -DskipTests package
mvn -DskipTests=false -Dtest=UserServiceImplTest test
```

项目以 Java 8 为目标版本。若系统安装了多个 JDK，请在运行 Maven 前确认 `java -version` 与 `mvn -version` 使用同一个 JDK 8 环境。

## 配置与安全

- `application.yml` 用于本地真实配置，已由仓库忽略；不要提交数据库账号或密码。
- 仓库仅维护脱敏的 `application-template.yml`，新增配置时应同步补充安全占位值。
- 修改前端地址后，应同步更新 `cors.allowed-origins`。
- 登录请求依赖 Cookie 会话，跨域部署时需同时核对 CORS、Cookie 和 HTTPS 设置。
- 当前密码处理适合演示基础认证流程，生产环境应改用 BCrypt、Argon2 等专用密码哈希算法。

## 相关文档

- [项目总览](../README.md)
- [前端说明](../user-center-frontend-vue/README.md)
