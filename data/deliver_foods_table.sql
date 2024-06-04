DROP TABLE IF EXISTS `delivers`;
CREATE TABLE `delivers` (
  `deliver_id` INTEGER NOT NULL, 
  `trans_type` VARCHAR(255), 
  `license` VARCHAR(255), 
  `user_id` INTEGER, 
  PRIMARY KEY (`deliver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `delivers` (`deliver_id`, `trans_type`, `license`, `user_id`) VALUES 
(1, 'M', 'AAA-1234', 3),
(2, 'M', 'BBB-5678', 5),
(3, 'C', 'CCC-9527', 6);

DROP TABLE IF EXISTS `foods`;
CREATE TABLE `foods` (
  `food_id` INTEGER NOT NULL, 
  `food_name` VARCHAR(255), 
  `food_type` VARCHAR(255), 
  `price` INTEGER, 
  `stock` INTEGER, 
  PRIMARY KEY (`food_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `foods` (`food_id`, `food_name`, `food_type`, `price`, `stock`) VALUES 
(1, '紅茶', 'drinks', 25, 50),
(2, '綠茶', 'drinks', 20, 60),
(3, '冬瓜茶', 'drinks', 35, 45),
(4, '沙士', 'drinks', 30, 20),
(5, '滷肉飯', 'staple', 50, 60),
(6, '肉臊飯', 'staple', 40, 55),
(7, '雞絲飯', 'staple', 35, 30),
(8, '鍋燒意麵', 'staple', 60, 50),
(9, '陽春麵', 'staple', 30, 25),
(10, '刀削麵', 'staple', 45, 32),
(11, '炒豆干', 'side', 25, 20),
(12, '燙青菜', 'side', 35, 30),
(13, '綜合鹵味', 'side', 45, 23),
(14, '滷蛋', 'side', 25, 37),
(15, '豆芽菜', 'side', 15, 78);

DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items` (
  `quantity` INTEGER, 
  `order_id` INTEGER NOT NULL, 
  `food_id` INTEGER NOT NULL,   
  PRIMARY KEY (`order_id`, `food_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `order_id` INTEGER NOT NULL, 
  `create_at` VARCHAR(255), 
  `state` VARCHAR(255), 
  `user_id` INTEGER, 
  `deliver_id` INTEGER,  
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` INTEGER NOT NULL, 
  `user_name` VARCHAR(255), 
  `password` VARCHAR(255), 
  `email` VARCHAR(255), 
  `address` VARCHAR(255), 
  `phone` VARCHAR(255), 
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `users` (`user_id`, `user_name`, `password`, `email`, `address`, `phone`) VALUES 
(1, 'admin', 'admin', NULL, NULL, NULL),
(2, 'user', 'user', 'user@npu.edu.tw', 'user id 2 address', '0919114514'),
(3, 'deliver', 'deliver', 'deliver@npu.edu.tw', 'deliver id 3 address', '0924810931'),
(4, 'alice', 'npu', 'alice@alpha.io', 'alice address', '0947179961'),
(5, 'bob', 'npu', 'beta@beta.io', 'beta address', '0905733150'),
(6, 'charlie', 'npu', 'charlie@unicorn.yt', 'Candy Mountain', '0989989913'),
(7, 'java', 'npu', 'java@npu.edu.tw', 'null', '0912345678');
