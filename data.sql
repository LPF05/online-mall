USE online_mall;

TRUNCATE TABLE access_log;
TRUNCATE TABLE browse_history;
TRUNCATE TABLE `order`;
TRUNCATE TABLE cart;
TRUNCATE TABLE product;
TRUNCATE TABLE user;

INSERT INTO user (username, password, email, balance, created_at, updated_at) VALUES
('张三', '$2a$10$lszfqRMDdpnlvzz2x1yn2OdGCznPMMr8k/fkoA3jBeajPGQfITLni', 'zhangsan@example.com', 1500.00, NOW(), NOW()),
('李四', '$2a$10$lszfqRMDdpnlvzz2x1yn2OdGCznPMMr8k/fkoA3jBeajPGQfITLni', 'lisi@example.com', 800.00, NOW(), NOW()),
('王五', '$2a$10$lszfqRMDdpnlvzz2x1yn2OdGCznPMMr8k/fkoA3jBeajPGQfITLni', 'wangwu@example.com', 3000.00, NOW(), NOW()),
('赵六', '$2a$10$lszfqRMDdpnlvzz2x1yn2OdGCznPMMr8k/fkoA3jBeajPGQfITLni', 'zhaoliu@example.com', 500.00, NOW(), NOW()),
('陈七', '$2a$10$lszfqRMDdpnlvzz2x1yn2OdGCznPMMr8k/fkoA3jBeajPGQfITLni', 'chenqi@example.com', 2500.00, NOW(), NOW()),
('小明', '$2a$10$lszfqRMDdpnlvzz2x1yn2OdGCznPMMr8k/fkoA3jBeajPGQfITLni', 'xiaoming@example.com', 5000.00, NOW(), NOW()),
('小红', '$2a$10$lszfqRMDdpnlvzz2x1yn2OdGCznPMMr8k/fkoA3jBeajPGQfITLni', 'xiaohong@example.com', 1200.00, NOW(), NOW()),
('小李', '$2a$10$lszfqRMDdpnlvzz2x1yn2OdGCznPMMr8k/fkoA3jBeajPGQfITLni', 'xiaoli@example.com', 8800.00, NOW(), NOW());

INSERT INTO product (name, description, price, stock, category, image_url, view_count, sales_count, created_at, updated_at) VALUES

('iPhone 15 Pro', '苹果最新旗舰手机，A17 Pro芯片，钛金属边框，超瓷晶面板，支持USB-C接口', 8999.00, 100, '电子产品', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=smartphone%20on%20white%20marble%20surface%20titanium%20blue%20luxury%20minimalist%20product%20photography&image_size=square_hd', 1520, 328, NOW(), NOW()),
('MacBook Pro 14', 'M3 Pro芯片，18小时续航，Liquid Retina XDR显示屏，18GB统一内存', 14999.00, 50, '电子产品', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=laptop%20on%20clean%20desk%20silver%20space%20gray%20minimalist%20product%20photography%20white%20background&image_size=square_hd', 890, 156, NOW(), NOW()),
('AirPods Pro 2', '主动降噪，空间音频，自适应通透模式，H2芯片', 1899.00, 200, '电子产品', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=wireless%20earbuds%20white%20charging%20case%20clean%20minimalist%20product%20photography%20soft%20lighting&image_size=square_hd', 2100, 560, NOW(), NOW()),
('小米14 Ultra', '徕卡影像，骁龙8 Gen3，1英寸大底主摄，5000mAh大电池', 5999.00, 150, '电子产品', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=modern%20smartphone%20black%20ceramic%20back%20camera%20module%20clean%20product%20photography%20white%20background&image_size=square_hd', 1800, 420, NOW(), NOW()),
('iPad Pro 12.9', 'M2芯片，Liquid Retina XDR，512GB存储，支持Apple Pencil', 9299.00, 80, '电子产品', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=tablet%20with%20stylus%20silver%20slim%20design%20clean%20product%20photography%20white%20background&image_size=square_hd', 950, 180, NOW(), NOW()),
('索尼WH-1000XM5', '无线降噪耳机，30小时续航，多点连接，快速充电', 2999.00, 120, '电子产品', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=over-ear%20headphones%20black%20premium%20noise%20canceling%20clean%20product%20photography%20white%20background&image_size=square_hd', 1200, 290, NOW(), NOW()),
('华为Mate60 Pro', '麒麟9000S芯片，卫星通话，超高速通信', 6999.00, 85, '电子产品', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=smartphone%20emerald%20green%20curved%20screen%20premium%20clean%20product%20photography%20white%20background&image_size=square_hd', 2300, 510, NOW(), NOW()),
('三星Galaxy S24 Ultra', '钛金属框架，2亿像素镜头，Galaxy AI', 9699.00, 60, '电子产品', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=titanium%20smartphone%20violet%20s-pen%20premium%20clean%20product%20photography%20white%20background&image_size=square_hd', 1600, 380, NOW(), NOW()),

('优衣库摇粒绒外套', '保暖舒适，轻便易洗，多色可选，男女同款', 299.00, 300, '服装', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=fleece%20jacket%20folded%20neatly%20beige%20cozy%20casual%20wear%20clean%20product%20photography%20white%20background&image_size=square_hd', 3500, 890, NOW(), NOW()),
('Nike Air Max', '经典运动鞋，气垫缓震，舒适耐穿，时尚百搭', 899.00, 200, '服装', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=sneakers%20white%20air%20cushion%20sole%20sport%20shoes%20clean%20product%20photography%20white%20background&image_size=square_hd', 4200, 1200, NOW(), NOW()),
('Adidas运动套装', '透气速干，适合运动，简约设计，多色可选', 599.00, 180, '服装', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=sportswear%20set%20black%20three%20stripes%20athletic%20outfit%20clean%20product%20photography%20white%20background&image_size=square_hd', 2800, 680, NOW(), NOW()),
('UNIQLO UT系列T恤', '纯棉材质，舒适透气，印花设计，多款式', 99.00, 500, '服装', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=graphic%20t-shirt%20white%20cotton%20casual%20clean%20product%20photography%20folded&image_size=square_hd', 5600, 1800, NOW(), NOW()),
('Levis牛仔裤', '经典版型，舒适耐穿，百搭时尚', 499.00, 250, '服装', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=denim%20jeans%20classic%20blue%20folded%20clean%20product%20photography%20white%20background&image_size=square_hd', 3100, 750, NOW(), NOW()),
('Gap卫衣', '加绒保暖，休闲舒适，简约大方', 399.00, 180, '服装', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=hoodie%20gray%20fleece%20lined%20casual%20comfort%20clean%20product%20photography%20white%20background&image_size=square_hd', 2400, 620, NOW(), NOW()),

('三只松鼠坚果礼盒', '多种坚果组合，健康美味，送礼佳品', 168.00, 400, '食品', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=nuts%20gift%20box%20assorted%20almonds%20cashews%20walnuts%20red%20packaging%20clean%20product%20photography&image_size=square_hd', 3200, 950, NOW(), NOW()),
('星巴克咖啡豆', '阿拉比卡咖啡豆，香醇浓郁，中度烘焙', 128.00, 350, '食品', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=coffee%20beans%20bag%20starbucks%20dark%20roast%20arabica%20clean%20product%20photography%20warm%20lighting&image_size=square_hd', 4500, 1300, NOW(), NOW()),
('进口巧克力礼盒', '比利时进口，丝滑细腻，多种口味', 88.00, 450, '食品', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=chocolate%20gift%20box%20belgian%20pralines%20elegant%20gold%20packaging%20clean%20product%20photography&image_size=square_hd', 2100, 520, NOW(), NOW()),
('新鲜有机苹果', '山东烟台苹果，脆甜多汁，自然成熟', 58.00, 600, '食品', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=fresh%20red%20apples%20organic%20crisp%20juicy%20clean%20product%20photography%20bright%20lighting&image_size=square_hd', 3800, 1100, NOW(), NOW()),
('三只松鼠芒果干', '泰国芒果，酸甜可口，无添加', 35.00, 800, '食品', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=dried%20mango%20slices%20golden%20sweet%20snack%20packaging%20clean%20product%20photography&image_size=square_hd', 4200, 1250, NOW(), NOW()),
('良品铺子猪肉脯', '精选猪肉，肉质紧实，口感鲜美', 45.00, 500, '食品', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=pork%20jerky%20slices%20savory%20snack%20sealed%20packaging%20clean%20product%20photography&image_size=square_hd', 3600, 980, NOW(), NOW()),
('农夫山泉矿泉水', '天然矿泉水，口感清甜，550ml*24瓶', 28.00, 1000, '食品', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=mineral%20water%20bottle%20crystal%20clear%20pure%20clean%20product%20photography%20refreshing&image_size=square_hd', 8500, 2500, NOW(), NOW()),

('宜家拉克边桌', '简约设计，实用百搭，易于组装', 79.00, 500, '家居', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=minimalist%20side%20table%20white%20scandinavian%20design%20clean%20product%20photography&image_size=square_hd', 2800, 720, NOW(), NOW()),
('小米空气净化器', '高效除醛，静音设计，智能控制', 899.00, 200, '家居', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=air%20purifier%20white%20cylindrical%20modern%20home%20appliance%20clean%20product%20photography&image_size=square_hd', 1600, 420, NOW(), NOW()),
('智能台灯', '护眼灯，无蓝光危害，学习必备，可调节亮度', 159.00, 400, '家居', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=desk%20lamp%20modern%20LED%20adjustable%20warm%20light%20clean%20product%20photography%20white&image_size=square_hd', 3200, 850, NOW(), NOW()),
('收纳箱套装', '大容量，可折叠，透明可视，多规格', 49.00, 600, '家居', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=storage%20boxes%20clear%20plastic%20stackable%20organized%20clean%20product%20photography%20white&image_size=square_hd', 2200, 620, NOW(), NOW()),
('乳胶枕头', '泰国乳胶，透气舒适，护颈设计', 299.00, 150, '家居', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=latex%20pillow%20white%20ergonomic%20comfort%20clean%20product%20photography%20soft%20lighting&image_size=square_hd', 1800, 460, NOW(), NOW()),
('四件套床品', '纯棉材质，亲肤舒适，多色可选', 399.00, 200, '家居', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=bedding%20set%20cotton%20white%20elegant%20neatly%20folded%20clean%20product%20photography&image_size=square_hd', 2100, 580, NOW(), NOW()),
('智能门锁', '指纹解锁，密码开锁，远程控制', 1299.00, 80, '家居', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=smart%20door%20lock%20fingerprint%20digital%20keypad%20black%20modern%20clean%20product%20photography&image_size=square_hd', 950, 220, NOW(), NOW()),

('SK-II神仙水', '护肤精华，改善肤质，提亮肤色', 1590.00, 100, '美妆', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=skincare%20essence%20red%20bottle%20premium%20serum%20clean%20product%20photography%20elegant&image_size=square_hd', 1200, 280, NOW(), NOW()),
('MAC口红套装', '持久显色，滋润不拔干，多色可选', 399.00, 200, '美妆', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=lipstick%20set%20multiple%20shades%20red%20pink%20nude%20cosmetics%20clean%20product%20photography&image_size=square_hd', 3800, 980, NOW(), NOW()),
('兰蔻小黑瓶', '修护精华，激活肌肤，深层滋养', 1080.00, 120, '美妆', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=serum%20bottle%20dark%20glass%20premium%20skincare%20clean%20product%20photography%20luxury&image_size=square_hd', 1500, 360, NOW(), NOW()),
('雅诗兰黛眼霜', '小棕瓶眼霜，淡化黑眼圈，紧致眼周', 590.00, 180, '美妆', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=eye%20cream%20brown%20jar%20premium%20skincare%20clean%20product%20photography%20elegant&image_size=square_hd', 2200, 540, NOW(), NOW()),
('完美日记眼影盘', '十二色眼影，粉质细腻，易上色', 89.00, 400, '美妆', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=eyeshadow%20palette%20colorful%12%20shades%20cosmetics%20clean%20product%20photography%20vibrant&image_size=square_hd', 3500, 920, NOW(), NOW()),
('花西子蜜粉', '定妆持久，控油防水，自然服帖', 199.00, 250, '美妆', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=setting%20powder%20elegant%20chinese%20style%20packaging%20cosmetics%20clean%20product%20photography&image_size=square_hd', 2800, 700, NOW(), NOW()),

('活着', '余华经典作品，讲述普通人的故事，感动千万读者', 39.00, 800, '图书', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=book%20cover%20chinese%20novel%20literary%20fiction%20clean%20product%20photography%20white%20background&image_size=square_hd', 4200, 1200, NOW(), NOW()),
('三体', '刘慈欣科幻巨著，雨果奖获奖作品，中国科幻里程碑', 59.00, 600, '图书', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=science%20fiction%20book%20cover%20space%20universe%20dark%20blue%20clean%20product%20photography&image_size=square_hd', 5600, 1600, NOW(), NOW()),
('百年孤独', '马尔克斯代表作，魔幻现实主义经典', 68.00, 450, '图书', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=classic%20novel%20book%20cover%20literary%20masterpiece%20warm%20tones%20clean%20product%20photography&image_size=square_hd', 3200, 880, NOW(), NOW()),
('人类简史', '尤瓦尔·赫拉利作品，从认知革命到人工智能', 78.00, 500, '图书', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=non-fiction%20book%20cover%20history%20humanity%20modern%20design%20clean%20product%20photography&image_size=square_hd', 4100, 1150, NOW(), NOW()),
('明朝那些事儿', '当年明月作品，通俗历史读物，幽默风趣', 198.00, 300, '图书', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=chinese%20history%20book%20series%20set%20colorful%20covers%20clean%20product%20photography&image_size=square_hd', 6200, 1700, NOW(), NOW()),
('小王子', '圣埃克苏佩里作品，献给曾经是孩子的大人们', 32.00, 700, '图书', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=the%20little%20prince%20book%20cover%20stars%20illustration%20dreamy%20clean%20product%20photography&image_size=square_hd', 3800, 1050, NOW(), NOW()),

('机械键盘', 'Cherry青轴，104键，RGB背光，铝合金面板', 499.00, 300, '生活', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=mechanical%20keyboard%20RGB%20backlit%20black%20aluminum%20gaming%20clean%20product%20photography&image_size=square_hd', 2400, 620, NOW(), NOW()),
('无线鼠标', '罗技MX Master 3S，静音按键，快充技术', 699.00, 250, '生活', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=wireless%20mouse%20ergonomic%20dark%20gray%20premium%20logitech%20clean%20product%20photography&image_size=square_hd', 1900, 480, NOW(), NOW()),
('充电宝', '20000mAh，支持无线充电，快充协议', 199.00, 400, '生活', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=power%20bank%20black%20slim%20portable%20charger%20USB-C%20clean%20product%20photography&image_size=square_hd', 3500, 900, NOW(), NOW()),
('数据线套装', 'Type-C数据线，快充，编织材质，1.5米', 35.00, 600, '生活', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=USB-C%20cable%20bundle%20braided%20nylon%20fast%20charging%20clean%20product%20photography&image_size=square_hd', 4800, 1350, NOW(), NOW()),
('护眼显示器', '27英寸，4K分辨率，IPS面板，低蓝光', 1999.00, 80, '生活', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=monitor%2027%20inch%204K%20slim%20bezel%20IPS%20display%20clean%20product%20photography&image_size=square_hd', 1200, 280, NOW(), NOW()),
('蓝牙音箱', '便携迷你，重低音，防水设计', 199.00, 350, '生活', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=bluetooth%20speaker%20portable%20compact%20waterproof%20black%20clean%20product%20photography&image_size=square_hd', 2800, 720, NOW(), NOW()),

('儿童玩具车', '遥控汽车，四驱越野，充电续航', 199.00, 200, '玩具', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=RC%20car%20toy%20red%20off-road%20remote%20control%20clean%20product%20photography%20fun&image_size=square_hd', 1800, 450, NOW(), NOW()),
('乐高积木', '创意拼插，益智玩具，适合6-12岁', 299.00, 150, '玩具', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=LEGO%20bricks%20colorful%20building%20blocks%20creative%20play%20clean%20product%20photography&image_size=square_hd', 2200, 560, NOW(), NOW()),
('芭比娃娃', '时尚公主，精美服饰，配件丰富', 159.00, 280, '玩具', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=fashion%20doll%20blonde%20pink%20dress%20accessories%20clean%20product%20photography&image_size=square_hd', 1500, 380, NOW(), NOW()),
('拼图玩具', '1000片拼图，风景图案，益智解压', 69.00, 400, '玩具', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=jigsaw%20puzzle%201000%20pieces%20landscape%20scenery%20box%20clean%20product%20photography&image_size=square_hd', 2800, 720, NOW(), NOW()),

('运动水壶', '不锈钢材质，真空保温，大容量', 89.00, 300, '户外', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=sports%20water%20bottle%20stainless%20steel%20insulated%20silver%20clean%20product%20photography&image_size=square_hd', 2100, 540, NOW(), NOW()),
('登山背包', '40L大容量，防水面料，透气背负', 399.00, 120, '户外', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=hiking%20backpack%2040L%20green%20waterproof%20outdoor%20gear%20clean%20product%20photography&image_size=square_hd', 1500, 360, NOW(), NOW()),
('户外帐篷', '双人帐篷，防雨防晒，易搭建', 299.00, 150, '户外', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=camping%20tent%20two-person%20green%20waterproof%20outdoor%20clean%20product%20photography&image_size=square_hd', 1800, 420, NOW(), NOW()),
('运动手表', '心率监测，GPS定位，50米防水', 599.00, 180, '户外', 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=sports%20watch%20fitness%20tracker%20black%20GPS%20waterproof%20clean%20product%20photography&image_size=square_hd', 1600, 380, NOW(), NOW());