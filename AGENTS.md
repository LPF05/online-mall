# Online Mall - B2C 电商项目

## 项目简介

这是一个精简版的 B2C 电商项目，用于课程设计。遵循第一性原理，实现电商核心功能，避免过度抽象。

## 技术栈

### 后端

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 4.0.6 | 后端框架 |
| Spring Security | 同 Boot | 认证授权 |
| MyBatis | 3.0.3 | ORM 框架 |
| MySQL | 8.0 | 关系型数据库 |
| JWT | 0.12.3 | Token 认证 |
| Lombok | 最新 | 简化代码 |
| Hutool | 5.8.27 | 工具类库 |
| Docker Compose | - | 环境编排 |
| JDK | 21 | 运行环境 |

### 前端

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | 3.4 | 前端框架 |
| TypeScript | 5.4 | 类型安全 |
| Vite | 5.1 | 构建工具 |
| Vue Router | 4.3 | 路由管理 |
| Pinia | 2.1 | 状态管理 |
| Element Plus | 2.14 | UI 组件库 |
| Axios | 1.6 | HTTP 客户端 |
| Sass | 1.72 | CSS 预处理器 |

## 如何启动

### 1. 启动 Docker 环境

```bash
docker-compose up -d
```

### 2. 初始化数据库

```bash
# 将 SQL 文件复制到容器内执行
docker cp init.sql online-mall-mysql:/tmp/init.sql
docker cp data.sql online-mall-mysql:/tmp/data.sql
docker exec online-mall-mysql mysql -u mall -pmall123456 online_mall -e "source /tmp/init.sql"
docker exec online-mall-mysql mysql -u mall -pmall123456 online_mall -e "source /tmp/data.sql"
```

### 3. 启动后端

```bash
mvn spring-boot:run
```

### 4. 启动前端

```bash
cd frontend
npm install
npm run dev
```

访问 `http://localhost:3000`

## 功能模块

- 用户系统：注册、登录、个人信息、余额、积分
- 商品系统：列表、搜索、排序、价格筛选、特卖、热销
- 购物车：添加、修改数量、删除、清空、同商品叠加
- 订单系统：创建、支付、取消、确认收货、管理员发货
- 收货地址：增删改、设默认
- 商品收藏：收藏/取消、收藏列表
- 商品评价：评分+评论、防重复
- 浏览记录：自动记录、去重合并
- 访问日志：PV/UV 统计
- 管理员后台：商品管理、订单管理

## 数据库表

| 表名 | 说明 |
|------|------|
| user | 用户（含 role、phone、points） |
| product | 商品（含 is_on_sale、original_price） |
| cart | 购物车（items JSON） |
| order | 订单（items JSON，status 0-4） |
| favorite | 收藏（user_id + product_id 唯一索引） |
| browse_history | 浏览记录（user_id + product_id 唯一索引，ON DUPLICATE KEY UPDATE） |
| address | 收货地址（含 is_default） |
| review | 评价（含 rating、order_no） |
| access_log | 访问日志 |
