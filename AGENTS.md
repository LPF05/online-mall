# Online Mall - B2C 电商项目

## 项目简介

这是一个精简版的 B2C 电商项目，用于课程设计。遵循第一性原理，实现电商核心功能，避免过度抽象。

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 4.0.6 | 后端框架 |
| Spring Security | 同 Boot | 认证授权 |
| MyBatis | 3.0.3 | ORM 框架 |
| MySQL | 8.0 | 关系型数据库 |
| JWT | 0.12.x | Token 认证 |
| Lombok | 最新 | 简化代码 |
| Hutool | 最新 | 工具类库 |
| Docker Compose | - | 环境编排 |
| JDK | 21 | 运行环境 |

注：Redis 和 Elasticsearch 暂时不启用，等需要时再添加

## 项目状态

- ✅ 项目基础框架搭建完成
- ✅ MyBatis 集成成功
- ✅ Hello World 测试接口可用
- ✅ Docker Compose 环境配置完成（仅 MySQL）
- ✅ 数据库初始化脚本完成
- ⏳ 后续功能待开发

## 如何启动

### 1. 启动 Docker 环境

```bash
docker-compose up -d
```

### 2. 初始化数据库

#### 方式一：使用 MySQL 客户端命令行

```bash
# 执行建表脚本
mysql -h 127.0.0.1 -P 3306 -u mall -pmall123456 < init.sql

# 执行测试数据脚本
mysql -h 127.0.0.1 -P 3306 -u mall -pmall123456 < data.sql
```

#### 方式二：进入 Docker 容器执行

```bash
# 执行建表脚本
docker exec -i online-mall-mysql mysql -u mall -pmall123456 < init.sql

# 执行测试数据脚本
docker exec -i online-mall-mysql mysql -u mall -pmall123456 < data.sql
```

### 3. 启动后端服务

```bash
mvn spring-boot:run
```

## 需求分析（第一性原理）

从第一性原理思考，电商核心需要：用户、商品、购物车、订单

### 用户系统

- 用户注册（用户名、邮箱、密码）
- 用户登录（邮箱 + 密码）
- 账户余额管理
- 用户信息查询

### 商品系统

- 商品 CRUD
- 商品分类筛选
- Elasticsearch 全文搜索
- 商品热度排行（按浏览量、销量）

### 购物车系统

- 添加商品到购物车
- 修改购物车商品数量
- 删除购物车商品
- 查看购物车

### 订单系统

- 创建订单（从购物车）
- 订单支付（余额支付）
- 查看订单列表
- 查看订单详情

### 浏览记录

- 记录用户浏览历史
- 查看个人浏览历史

### 日志采集与分析

- 页面访问日志采集
- 基础访问统计（PV、UV）

## 数据库表设计

### user（用户表）
- id, username, password, email, balance, created_at, updated_at

### product（商品表）
- id, name, description, price, stock, category, image_url, view_count, sales_count, created_at, updated_at

### cart（购物车表）
- id, user_id, items(JSON), created_at, updated_at

### order（订单表）
- id, order_no, user_id, items(JSON), total_amount, status, created_at, updated_at

### browse_history（浏览记录表）
- id, user_id, product_id, created_at

### access_log（访问日志表）
- id, user_id, page_url, ip, user_agent, session_id, created_at

## TODO List

- [x] 数据库表设计
- [x] 技术栈确定
- [ ] Docker Compose 环境配置
- [ ] 项目基础架构搭建
- [ ] 用户系统实现
- [ ] 商品系统实现
- [ ] 购物车系统实现
- [ ] 订单系统实现
- [ ] 浏览记录实现
- [ ] 日志采集与分析实现
- [ ] Elasticsearch 集成
- [ ] 前端页面开发
