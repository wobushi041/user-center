# User Center 前端

User Center 的 Vue 3 客户端，提供用户注册、登录、登录状态展示和管理员用户管理页面。项目总览与后端启动说明见[仓库根 README](../README.md)。

作者：wobushi041

## 页面与权限

| 页面 | 路由 | 用途 | 访问要求 |
| --- | --- | --- | --- |
| 首页 | `/` | 展示系统首页与当前登录状态 | 公开 |
| 用户登录 | `/user/login` | 登录账号并建立会话 | 公开 |
| 用户注册 | `/user/register` | 创建用户账号 | 公开 |
| 用户管理 | `/admin/userManage` | 按用户名检索和删除用户 | 管理员 |

路由定义以 [`src/router/index.ts`](src/router/index.ts) 为准。管理员页面同时经过 [`src/access.ts`](src/access.ts) 的前端路由校验；最终权限仍由后端接口判定。

## 技术与目录

- Vue 3、TypeScript 和 Vue Router 构成页面与路由基础。
- Ant Design Vue 提供界面组件，Pinia 管理当前登录用户状态。
- Axios 封装 HTTP 请求，并统一处理登录失效后的页面跳转。
- Vue CLI 负责本地开发、代码检查和生产构建。

```text
src/
├─ api/          # 用户接口封装
├─ components/   # 公共页面组件
├─ layouts/      # 全局页面布局
├─ pages/        # 首页、账号与管理页面
├─ router/       # 路由定义
├─ store/        # Pinia 登录状态
├─ utils/        # 通用工具
├─ access.ts     # 路由权限校验
└─ request.ts    # Axios 实例与响应拦截
```

## 本地开发

准备 Node.js 和 npm，并先按[根 README](../README.md#本地运行)启动后端。在 `user-center-frontend-vue` 目录运行：

```powershell
npm install
npm run serve
```

前端默认监听 `http://localhost:3000`。开发服务器会将 `/api` 请求代理到 `http://localhost:8080`，代理规则位于 [`vue.config.js`](vue.config.js)。

## 请求与部署配置

前端请求默认使用同源地址。前后端跨域部署时，通过构建环境变量指定后端域名：

```dotenv
VUE_APP_API_BASE_URL=https://api.example.com
```

接口路径本身已包含 `/api` 前缀，因此变量通常填写协议与域名即可。请求会携带 Cookie；跨域部署时还需同步配置后端 `cors.allowed-origins`，并检查浏览器的 Cookie 与 HTTPS 策略。不要将密钥、账号或密码写入前端环境变量。

## 可用命令

| 命令 | 用途 |
| --- | --- |
| `npm run serve` | 启动本地开发服务器 |
| `npm run build` | 生成生产构建产物 |
| `npm run lint` | 检查并修复可自动处理的代码问题 |

## 相关文档

- [项目总览](../README.md)
- [后端说明](../user-center-backend/README.md)
