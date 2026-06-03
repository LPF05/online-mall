-- 使用数据库
USE online_mall;

-- 插入测试用户数据
INSERT INTO user (username, password, email, balance, created_at, updated_at) VALUES
('张三', '123456', 'zhangsan@example.com', 1000.00, NOW(), NOW()),
('李四', '123456', 'lisi@example.com', 500.00, NOW(), NOW()),
('王五', '123456', 'wangwu@example.com', 2000.00, NOW(), NOW());

-- 插入测试商品数据
INSERT INTO product (name, description, price, stock, category, image_url, view_count, sales_count, created_at, updated_at) VALUES
('iPhone 15 Pro', '苹果最新旗舰手机，A17 Pro芯片', 8999.00, 100, '电子产品', 'https://via.placeholder.com/200', 0, 0, NOW(), NOW()),
('MacBook Pro 14', 'M3 Pro芯片，专业级笔记本', 14999.00, 50, '电子产品', 'https://via.placeholder.com/200', 0, 0, NOW(), NOW()),
('AirPods Pro 2', '主动降噪，空间音频', 1899.00, 200, '电子产品', 'https://via.placeholder.com/200', 0, 0, NOW(), NOW()),
('小米14 Ultra', '徕卡影像，骁龙8 Gen3', 5999.00, 150, '电子产品', 'https://via.placeholder.com/200', 0, 0, NOW(), NOW()),
('机械键盘', 'Cherry青轴，104键', 499.00, 300, '生活', 'https://via.placeholder.com/200', 0, 0, NOW(), NOW()),
('无线鼠标', '罗技MX Master 3S', 699.00, 250, '生活', 'https://via.placeholder.com/200', 0, 0, NOW(), NOW());
