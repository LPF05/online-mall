# Online Mall - 综合网上购物商城

一个基于 Spring Boot + Vue 3 的 B2C 电商平台，采用前后端分离架构，实现电商核心业务功能。

## 目录

- [项目概述](#项目概述)
- [核心功能](#核心功能)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [环境要求](#环境要求)
- [快速开始](#快速开始)
- [配置说明](#配置说明)
- [API 文档](#api-文档)
- [数据库设计](#数据库设计)
- [常见问题](#常见问题)

## 项目概述

Online Mall 是一个精简版 B2C 电商项目，遵循第一性原理设计，聚焦电商核心业务：用户、商品、购物车、订单。项目采用前后端分离架构，后端提供 RESTful API，前端基于 Vue 3 构建 SPA 应用，适合课程设计和技术学习。

### 主要特性

- 前后端分离，RESTful API 设计
- JWT 无状态认证，Spring Security 权限控制
- 商品搜索、排序、价格区间筛选（后端 SQL 实现）
- 限时特卖、折扣标签、热销排行
- 收货地址管理、商品收藏、用户评价
- 浏览记录去重（唯一索引 + ON DUPLICATE KEY UPDATE）
- 访问日志采集与基础统计
- 管理员后台（商品管理、订单发货）
- 响应式布局，适配桌面端与移动端

## 核心功能

### 用户系统
- 用户注册（用户名、邮箱、密码）
- 用户登录（邮箱 + 密码，JWT Token 认证）
- 个人信息查看与修改
- 账户余额管理
- 积分系统（1 元 = 1 积分）

### 商品系统
- 商品列表（分页、分类筛选）
- 商品搜索（关键词）
- 商品排序（价格升/降序、销量）
- 价格区间筛选（最低价、最高价）
- 限时特卖商品推荐
- 最新商品推荐
- 热销商品排行
- 商品详情页（浏览记录自动记录）

### 购物车系统
- 添加商品到购物车（同商品自动叠加数量）
- 修改购物车商品数量
- 删除购物车商品
- 清空购物车
- 购物车结算

### 订单系统
- 创建订单（从购物车选择商品）
- 订单支付（余额支付）
- 取消订单（待付款状态）
- 确认收货（已发货状态）
- 查看订单列表与详情
- 管理员发货

### 收货地址
- 新增收货地址
- 编辑/删除地址
- 设置默认地址
- 首个地址自动设为默认

### 商品收藏
- 收藏/取消收藏商品
- 查看收藏列表

### 商品评价
- 对已完成订单的商品发表评价
- 评分（1-5 星）+ 文字评论
- 商品详情页展示评价列表
- 防重复评价

### 浏览记录
- 自动记录浏览历史
- 同一商品重复浏览自动合并（更新时间）
- 查看个人浏览历史

### 访问日志
- 页面访问日志自动采集
- 基础访问统计（PV、UV）

### 管理员后台
- 商品管理（增删改查）
- 订单管理（发货、状态筛选）
- 用户管理

## 技术栈

### 后端

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 4.0.6 | 后端框架 |
| Spring Security | 同 Boot | 认证授权 |
| MyBatis | 3.0.3 | ORM 框架 |
| MySQL | 8.0 | 关系型数据库 |
| JWT (jjwt) | 0.12.3 | Token 认证 |
| Lombok | 最新 | 简化代码 |
| Hutool | 5.8.27 | 工具类库 |
| Maven | - | 构建工具 |

### 前端

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | 3.4.21 | 前端框架 |
| TypeScript | 5.4.2 | 类型安全 |
| Vite | 5.1.6 | 构建工具 |
| Vue Router | 4.3.0 | 路由管理 |
| Pinia | 2.1.7 | 状态管理 |
| Element Plus | 2.14.1 | UI 组件库 |
| Axios | 1.6.7 | HTTP 客户端 |
| Sass | 1.72.0 | CSS 预处理器 |

### 基础设施

| 技术 | 用途 |
|------|------|
| Docker Compose | MySQL 环境编排 |
| Playwright | E2E 测试 |

## 项目结构

```
online-mall/
├── src/main/java/com/yoimiya/onlinemall/   # 后端源码
│   ├── common/          # 通用响应封装 (Result)
│   ├── config/          # 配置类 (Security, MyBatis, CORS)
│   ├── controller/      # 控制器层 (REST API)
│   ├── dto/             # 数据传输对象
│   ├── entity/          # 实体类
│   ├── exception/       # 全局异常处理
│   ├── interceptor/     # 拦截器 (访问日志)
│   ├── mapper/          # MyBatis Mapper
│   ├── security/        # JWT 工具与过滤器
│   └── service/         # 业务逻辑层
├── src/main/resources/
│   └── application.properties   # 后端配置
├── frontend/              # 前端源码
│   ├── src/
│   │   ├── api/          # API 请求封装
│   │   ├── assets/       # 静态资源 (样式、图片)
│   │   ├── components/   # 公共组件
│   │   ├── router/       # 路由配置
│   │   ├── stores/       # Pinia 状态管理
│   │   └── views/        # 页面组件
│   ├── index.html        # 入口 HTML
│   ├── package.json      # 前端依赖
│   └── vite.config.ts    # Vite 配置
├── init.sql              # 数据库建表脚本
├── data.sql              # 测试数据脚本
├── update_tables.sql     # 数据库增量更新脚本
├── docker-compose.yml    # Docker 编排
└── pom.xml               # Maven 配置
```

## 环境要求

| 依赖 | 最低版本 | 说明 |
|------|---------|------|
| JDK | 17+ | 后端运行环境 |
| Node.js | 18+ | 前端构建运行 |
| Maven | 3.8+ | 后端构建工具 |
| Docker | 20+ | 容器化运行 MySQL |
| Docker Compose | 2.0+ | 编排 Docker 容器 |

## 快速开始

### 1. 克隆项目

```bash
git clone <repository-url>
cd online-mall
```

### 2. 启动 MySQL

```bash
docker-compose up -d
```

等待 MySQL 容器启动完成（约 10-20 秒），可通过以下命令确认：

```bash
docker exec online-mall-mysql mysql -u mall -pmall123456 -e "SELECT 1"
```

### 3. 初始化数据库

```bash
# 1) 将 SQL 文件复制到容器
docker cp init.sql online-mall-mysql:/tmp/init.sql
docker cp data.sql online-mall-mysql:/tmp/data.sql

# 2) 在容器内执行
docker exec online-mall-mysql mysql -u mall -pmall123456 online_mall -e "source /tmp/init.sql"
docker exec online-mall-mysql mysql -u mall -pmall123456 online_mall -e "source /tmp/data.sql"
```

### 4. 安装前端依赖

```bash
cd frontend
npm install
```

### 5. 启动后端

```bash
# 在项目根目录执行
cd online-mall
mvn spring-boot:run
```

后端启动在 `http://localhost:8080`

### 6. 启动前端

```bash
# 在 frontend 目录执行
cd frontend
npm run dev
```

前端启动在 `http://localhost:3000`，自动代理 `/api` 请求到后端。

### 7. 访问应用

浏览器打开 `http://localhost:3000`

**测试账号：**

| 用户名 | 邮箱 | 密码 | 余额 |
|--------|------|------|------|
| 张三 | zhangsan@example.com | 123456 | ¥1,500 |
| 李四 | lisi@example.com | 123456 | ¥800 |
| 小李 | xiaoli@example.com | 123456 | ¥8,800 |
| 管理员 | admin@example.com | 123456 | 0 |

> 所有测试账号密码均为 `123456`

## 配置说明

### 后端配置 (`application.properties`)

```properties
# 服务端口
server.port=8080

# 数据库连接
spring.datasource.url=jdbc:mysql://localhost:3306/online_mall?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=mall
spring.datasource.password=mall123456

# MyBatis
mybatis.type-aliases-package=com.yoimiya.onlinemall.entity
mybatis.configuration.map-underscore-to-camel-case=true

# JWT
jwt.secret=online-mall-jwt-secret-key-for-course-design-2024
jwt.expiration=86400000  # Token 有效期 24 小时（毫秒）
```

### 前端配置 (`vite.config.ts`)

```typescript
export default defineConfig({
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',  // 后端地址
        changeOrigin: true
      }
    }
  }
})
```

### Docker 配置 (`docker-compose.yml`)

| 参数 | 默认值 | 说明 |
|------|--------|------|
| MYSQL_ROOT_PASSWORD | root123456 | Root 密码 |
| MYSQL_DATABASE | online_mall | 数据库名 |
| MYSQL_USER | mall | 用户名 |
| MYSQL_PASSWORD | mall123456 | 用户密码 |
| 端口映射 | 3306:3306 | MySQL 端口 |

## API 文档

### 认证相关 `/api/auth`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/api/auth/register` | 用户注册 | 否 |
| POST | `/api/auth/login` | 用户登录 | 否 |

### 商品相关 `/api/products`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/api/products` | 商品列表（支持 keyword/category/sort/minPrice/maxPrice 参数） | 否 |
| GET | `/api/products/{id}` | 商品详情 | 否 |
| GET | `/api/products/hot` | 热销商品 | 否 |
| GET | `/api/products/sale` | 特卖商品 | 否 |
| GET | `/api/products/latest` | 最新商品 | 否 |

### 购物车 `/api/cart`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/api/cart` | 查看购物车 | 是 |
| POST | `/api/cart/add` | 添加商品 | 是 |
| PUT | `/api/cart/update` | 更新数量 | 是 |
| DELETE | `/api/cart/remove` | 删除商品 | 是 |
| DELETE | `/api/cart/clear` | 清空购物车 | 是 |

### 订单 `/api/orders`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/api/orders` | 订单列表 | 是 |
| GET | `/api/orders/{orderNo}` | 订单详情 | 是 |
| POST | `/api/orders` | 创建订单 | 是 |
| PUT | `/api/orders/{orderNo}/pay` | 支付订单 | 是 |
| PUT | `/api/orders/{orderNo}/cancel` | 取消订单 | 是 |
| PUT | `/api/orders/{orderNo}/confirm` | 确认收货 | 是 |

### 收货地址 `/api/addresses`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/api/addresses` | 地址列表 | 是 |
| POST | `/api/addresses` | 新增地址 | 是 |
| PUT | `/api/addresses/{id}` | 更新地址 | 是 |
| DELETE | `/api/addresses/{id}` | 删除地址 | 是 |
| PUT | `/api/addresses/{id}/default` | 设为默认 | 是 |

### 收藏 `/api/favorites`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/api/favorites` | 收藏列表 | 是 |
| POST | `/api/favorites/{productId}` | 添加收藏 | 是 |
| DELETE | `/api/favorites/{productId}` | 取消收藏 | 是 |

### 评价 `/api/reviews`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/api/reviews/product/{id}` | 商品评价列表 | 否 |
| POST | `/api/reviews` | 发表评价 | 是 |
| GET | `/api/reviews/my` | 我的评价 | 是 |

### 浏览记录 `/api/browse`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/api/browse/history` | 浏览历史 | 是 |

### 用户 `/api/user`

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/api/user/profile` | 个人信息 | 是 |
| PUT | `/api/user/profile` | 更新信息 | 是 |

## 数据库设计

### ER 关系

```
user ──1:1── cart
user ──1:N── order
user ──1:N── favorite ──N:1── product
user ──1:N── browse_history ──N:1── product
user ──1:N── address
user ──1:N── review ──N:1── product
```

### 表结构

| 表名 | 说明 | 关键字段 |
|------|------|---------|
| `user` | 用户表 | id, username, email, password, balance, role, points |
| `product` | 商品表 | id, name, price, stock, category, is_on_sale, original_price |
| `cart` | 购物车表 | id, user_id, items(JSON) |
| `order` | 订单表 | id, order_no, user_id, items(JSON), total_amount, status |
| `favorite` | 收藏表 | id, user_id, product_id (唯一索引) |
| `browse_history` | 浏览记录表 | id, user_id, product_id (唯一索引) |
| `address` | 收货地址表 | id, user_id, receiver_name, phone, detail_address, is_default |
| `review` | 评价表 | id, user_id, product_id, order_no, rating, content |
| `access_log` | 访问日志表 | id, user_id, page_url, ip, session_id |

### 订单状态流转

```
0(待支付) → 1(已付款/待发货) → 2(已发货/待收货) → 3(已完成)
0(待支付) → 4(已取消)
```

## 常见问题

### Q: 后端启动报数据库连接失败？

确认 MySQL 容器已启动并完成初始化：

```bash
docker-compose ps
docker exec online-mall-mysql mysql -u mall -pmall123456 -e "SELECT 1"
```

如果刚启动容器，等待 10-20 秒后重试。

### Q: 前端页面空白或 API 请求 404？

1. 确认后端已启动在 `http://localhost:8080`
2. 确认前端开发服务器已启动在 `http://localhost:3000`
3. Vite 代理配置会将 `/api` 请求转发到后端，不要直接访问 8080 端口

### Q: 端口 8080 被占用？

```powershell
# Windows - 查找并终止占用端口的进程
netstat -ano | findstr :8080
taskkill /PID <进程ID> /F
```

```bash
# Linux/macOS
lsof -i :8080
kill -9 <PID>
```

### Q: 如何重置数据库？

```bash
# 停止并删除容器和数据卷
docker-compose down -v

# 重新启动
docker-compose up -d

# 重新初始化
docker cp init.sql online-mall-mysql:/tmp/init.sql
docker cp data.sql online-mall-mysql:/tmp/data.sql
docker exec online-mall-mysql mysql -u mall -pmall123456 online_mall -e "source /tmp/init.sql"
docker exec online-mall-mysql mysql -u mall -pmall123456 online_mall -e "source /tmp/data.sql"
```

### Q: 如何添加管理员账号？

在数据库中执行：

```sql
UPDATE user SET role = 'ADMIN' WHERE username = '你的用户名';
```

管理员可访问 `/admin` 页面进行商品和订单管理。

### Q: 商品图片无法加载？

商品图片使用 AI 图片生成 API，首次加载可能需要几秒。如果持续无法加载，检查网络连接。

### Q: 如何修改 JWT 密钥？

编辑 `src/main/resources/application.properties`：

```properties
jwt.secret=your-custom-secret-key
jwt.expiration=86400000
```

修改后需重启后端服务，已登录用户的 Token 将失效。

## License

本项目仅供学习与课程设计使用。
