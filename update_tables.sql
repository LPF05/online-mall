USE online_mall;

-- 更新用户表，添加 phone 和 role 字段
ALTER TABLE user 
ADD COLUMN IF NOT EXISTS phone VARCHAR(20) COMMENT '手机号' AFTER email,
ADD COLUMN IF NOT EXISTS role VARCHAR(20) DEFAULT 'USER' COMMENT '用户角色：USER-普通用户，ADMIN-管理员' AFTER balance;

-- 更新 order 表的状态字段注释
ALTER TABLE `order` 
MODIFY COLUMN status TINYINT DEFAULT 0 COMMENT '订单状态 0-待付款 1-已付款 2-已发货 3-已完成 4-已取消';

-- 将已有的管理员用户设置为 ADMIN 角色
UPDATE user SET role = 'ADMIN' WHERE username = '管理员' OR email = 'admin@example.com';

-- 创建收藏表
CREATE TABLE IF NOT EXISTS favorite (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  product_id BIGINT NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_user_product (user_id, product_id)
);
