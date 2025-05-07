-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.4.5 - MySQL Community Server - GPL
-- Server OS:                    Linux
-- HeidiSQL Version:             12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for tmdt
CREATE DATABASE IF NOT EXISTS `tmdt` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `tmdt`;

-- Dumping structure for table tmdt.carts
CREATE TABLE IF NOT EXISTS `carts` (
  `cart_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `product_id` int NOT NULL,
  `quantity` int NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`cart_id`),
  UNIQUE KEY `user_id` (`user_id`,`product_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `carts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `carts_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE,
  CONSTRAINT `carts_chk_1` CHECK ((`quantity` > 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.carts: ~0 rows (approximately)
DELETE FROM `carts`;

-- Dumping structure for table tmdt.categories
CREATE TABLE IF NOT EXISTS `categories` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` text,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.categories: ~0 rows (approximately)
DELETE FROM `categories`;
INSERT INTO `categories` (`category_id`, `name`, `description`) VALUES
	(1, 'bánh ngọt', 'mặc định'),
	(2, 'bánh mì', 'mặc định'),
	(3, 'bánh tươi', 'mặc định'),
	(4, 'bánh sinh nhật', 'mặc định');

-- Dumping structure for table tmdt.comment
CREATE TABLE IF NOT EXISTS `comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `avatar_url` varchar(255) DEFAULT NULL,
  `content` text,
  `created_at` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.comment: ~2 rows (approximately)
DELETE FROM `comment`;
INSERT INTO `comment` (`id`, `avatar_url`, `content`, `created_at`, `username`) VALUES
	(1, 'tandeptrai', NULL, '2025-05-06 11:03:03', 'tandeptrai'),
	(2, 'tandeptrai', 'Sản phẩm này được đấy', '2025-05-06 11:03:15', 'tandeptrai'),
	(3, 'tandeptrai', 'Sản phẩm này được đấy', '2025-05-06 11:03:17', 'tandeptrai');

-- Dumping structure for table tmdt.orders
CREATE TABLE IF NOT EXISTS `orders` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `store_id` int NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `status` enum('pending','confirmed','shipped','delivered','canceled') NOT NULL,
  `payment_method` enum('cod','online') NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`order_id`),
  KEY `user_id` (`user_id`),
  KEY `store_id` (`store_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT,
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`) ON DELETE RESTRICT,
  CONSTRAINT `orders_chk_1` CHECK ((`total_amount` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.orders: ~0 rows (approximately)
DELETE FROM `orders`;

-- Dumping structure for table tmdt.order_details
CREATE TABLE IF NOT EXISTS `order_details` (
  `order_detail_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `product_id` int NOT NULL,
  `quantity` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `customization` text,
  PRIMARY KEY (`order_detail_id`),
  KEY `order_id` (`order_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `order_details_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE,
  CONSTRAINT `order_details_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE RESTRICT,
  CONSTRAINT `order_details_chk_1` CHECK ((`quantity` > 0)),
  CONSTRAINT `order_details_chk_2` CHECK ((`price` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.order_details: ~0 rows (approximately)
DELETE FROM `order_details`;

-- Dumping structure for table tmdt.order_promotions
CREATE TABLE IF NOT EXISTS `order_promotions` (
  `order_promotion_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `promotion_id` int NOT NULL,
  PRIMARY KEY (`order_promotion_id`),
  UNIQUE KEY `order_id` (`order_id`,`promotion_id`),
  KEY `promotion_id` (`promotion_id`),
  CONSTRAINT `order_promotions_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE,
  CONSTRAINT `order_promotions_ibfk_2` FOREIGN KEY (`promotion_id`) REFERENCES `promotions` (`promotion_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.order_promotions: ~0 rows (approximately)
DELETE FROM `order_promotions`;

-- Dumping structure for table tmdt.products
CREATE TABLE IF NOT EXISTS `products` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(150) NOT NULL,
  `description` text,
  `price` decimal(10,2) NOT NULL,
  `stock` int NOT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `category_id` int NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`product_id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `products_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`) ON DELETE RESTRICT,
  CONSTRAINT `products_chk_1` CHECK ((`price` >= 0)),
  CONSTRAINT `products_chk_2` CHECK ((`stock` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.products: ~0 rows (approximately)
DELETE FROM `products`;
INSERT INTO `products` (`product_id`, `name`, `description`, `price`, `stock`, `image_url`, `category_id`, `created_at`, `updated_at`) VALUES
	(20, 'Flan Pudding Trà Xanh', 'Sản phẩm ngon tuyệt!', 15000.00, 10, 'https://product.hstatic.net/1000104153/product/flan_pudding_tra_xanh_bd9cf52aba374a52b647af883587abbc_grande.jpg', 1, '2025-05-07 14:39:38', '2025-05-07 14:39:38'),
	(21, 'Flan Pudding Chocolate', 'Sản phẩm ngon tuyệt!', 15000.00, 10, 'https://product.hstatic.net/1000104153/product/flan_pudding_chocolate_2746e93548334140aa616e258d2e30fa_grande.jpg', 1, '2025-05-07 14:39:38', '2025-05-07 14:39:38'),
	(22, 'Rainbow Macaron', 'Sản phẩm ngon tuyệt!', 50000.00, 10, 'https://product.hstatic.net/1000104153/product/24_b81e174555ae494ea8e431ee8f1bce2b_grande.png', 1, '2025-05-07 14:39:38', '2025-05-07 14:39:38'),
	(23, 'Croffle Chocolate Đen', 'Sản phẩm ngon tuyệt!', 23000.00, 10, 'https://product.hstatic.net/1000104153/product/croffle_chocolate_den_0fc695e6cb0a4124b6d68d4470b32ba5_grande.jpg', 1, '2025-05-07 14:39:38', '2025-05-07 14:39:38'),
	(24, 'Bánh Cuộn Chocolate', 'Sản phẩm ngon tuyệt!', 15000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_cuon_chocolate_976736d4bd134f65949a7c17b5061648_grande.jpg', 1, '2025-05-07 14:39:38', '2025-05-07 14:39:38'),
	(25, 'Bánh ốc quế kem', 'Sản phẩm ngon tuyệt!', 15000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_oc_que_kem_80404fd5a8ca49a0b0a1d8cd652492a5_grande.jpg', 1, '2025-05-07 14:39:38', '2025-05-07 14:39:38'),
	(26, 'Bánh donut chocolate đen', 'Sản phẩm ngon tuyệt!', 17000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_donut_chocolate_den_05fa3ff0e45d428d80374c2014958fe1_grande.jpg', 1, '2025-05-07 14:39:38', '2025-05-07 14:39:38'),
	(27, 'Bánh donut chocolate trắng', 'Sản phẩm ngon tuyệt!', 17000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_donut_chocolate_trang_6a4209aec9184bb3be2393e93a99aa55_grande.jpg', 1, '2025-05-07 14:39:38', '2025-05-07 14:39:38'),
	(28, 'Bánh Donut trà xanh', 'Sản phẩm ngon tuyệt!', 17000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_donut_tra_xanh_568d9d4d9e504af0857ab40982af93f0_grande.jpg', 1, '2025-05-07 14:39:38', '2025-05-07 14:39:38'),
	(29, 'Bánh Donut dâu', 'Sản phẩm ngon tuyệt!', 17000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_donut_dau_0494546683f248aaa4cb598c8853b4d1_grande.jpg', 1, '2025-05-07 14:39:38', '2025-05-07 14:39:38'),
	(30, 'Bánh Gateaux de basque mini', 'Sản phẩm ngon tuyệt!', 18000.00, 10, 'https://product.hstatic.net/1000104153/product/gateaux_de_basque_d70292b8bdc648f0946e42441b953fee_grande.jpg', 1, '2025-05-07 14:39:38', '2025-05-07 14:39:38'),
	(31, 'Bánh dừa nho trứng muối', 'Sản phẩm ngon tuyệt!', 18000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-dua-nho-trung-muoi_07d2ebbe827646eda76a3cb79cc91c3e_grande.png', 1, '2025-05-07 14:39:38', '2025-05-07 14:39:38'),
	(32, 'Bánh chuối cake', 'Sản phẩm ngon tuyệt!', 24000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_cake_chuoi_b37a0d0006044a9ab384157a97d682b0_grande.jpg', 1, '2025-05-07 14:39:38', '2025-05-07 14:39:38'),
	(33, 'Bánh Butter Raisins', 'Sản phẩm ngon tuyệt!', 30000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-butter-raisins_7104cb207a284a6aa78e52b77852d88b_grande.png', 1, '2025-05-07 14:39:38', '2025-05-07 14:39:38'),
	(34, 'Bánh Delicacy Coconut cake', 'Sản phẩm ngon tuyệt!', 30000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-delicacy-coconut-cake_c9c87c108a5849bb916806476b8a940a_grande.png', 1, '2025-05-07 14:39:38', '2025-05-07 14:39:38'),
	(35, 'Bánh Madeleine', 'Sản phẩm ngon tuyệt!', 10000.00, 10, 'https://product.hstatic.net/1000104153/product/madeleine-vani_327175c128034aed885e376f621a3fc3_grande.png', 1, '2025-05-07 14:39:38', '2025-05-07 14:39:38'),
	(36, 'Bánh Madeleine Chocolate', 'Sản phẩm ngon tuyệt!', 10000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-madeleine-chocolate_d6b8a4a4a1d142939338019774f182db_grande.png', 1, '2025-05-07 14:39:38', '2025-05-07 14:39:38'),
	(37, 'Creme Caramel', 'Sản phẩm ngon tuyệt!', 10000.00, 10, 'https://product.hstatic.net/1000104153/product/caramel_0009b29de5c049f0bc60b64592d03ffd_grande.jpg', 1, '2025-05-07 14:39:38', '2025-05-07 14:39:38'),
	(38, 'Sponge cake chữ nhật', 'Sản phẩm ngon tuyệt!', 15000.00, 10, 'https://product.hstatic.net/1000104153/product/spong-cake-chu-nhat_b4d2efdb7f9640408c67a9590575c5b2_grande.png', 1, '2025-05-07 14:39:39', '2025-05-07 14:39:39'),
	(39, 'Bánh hamburger 3pcs', 'Sản phẩm ngon tuyệt!', 15000.00, 10, 'https://product.hstatic.net/1000104153/product/13_983f4abd084141b6afbaaa0567d88f75_grande.png', 2, '2025-05-07 14:41:16', '2025-05-07 14:41:16'),
	(40, 'Bánh mì Hàn Quốc mè đen', 'Sản phẩm ngon tuyệt!', 30000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-my-han-quoc-me-den_3055a3c4a8384158be57025db7d40169_grande.png', 2, '2025-05-07 14:41:16', '2025-05-07 14:41:16'),
	(41, 'Bánh mỳ baguette', 'Sản phẩm ngon tuyệt!', 14000.00, 10, 'https://product.hstatic.net/1000104153/product/baguette_8b1506303c57446cb46c6ad8fc5d9b5a_grande.jpg', 2, '2025-05-07 14:41:16', '2025-05-07 14:41:16'),
	(42, 'Bánh mỳ Brioche', 'Sản phẩm ngon tuyệt!', 20000.00, 10, 'https://product.hstatic.net/1000104153/product/brioche_1fe61245a8c94e0da7cd5ceb47c7c5a2_grande.jpg', 2, '2025-05-07 14:41:16', '2025-05-07 14:41:16'),
	(43, 'Bánh mỳ đen', 'Sản phẩm ngon tuyệt!', 15000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-my-den_ac5853a1b23f4ad8814bc7327e26e11d_grande.png', 2, '2025-05-07 14:41:16', '2025-05-07 14:41:16'),
	(44, 'Bánh mỳ gối', 'Sản phẩm ngon tuyệt!', 26000.00, 10, 'https://product.hstatic.net/1000104153/product/gato-bass-mini__5__445f605e3ce444ffa38e02414ede4a9e_grande.png', 2, '2025-05-07 14:41:17', '2025-05-07 14:41:17'),
	(45, 'Bánh Mỳ Gối Bí Đỏ', 'Sản phẩm ngon tuyệt!', 30000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_mi_goi_bi_do_a737a635dd33411eba9002d4a93ec925_grande.jpg', 2, '2025-05-07 14:41:17', '2025-05-07 14:41:17'),
	(46, 'Bánh mỳ gối đen', 'Sản phẩm ngon tuyệt!', 28000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-mi-goi-den-1_e5f7881eac4240609dd0fbfe02ddaef9_grande.png', 2, '2025-05-07 14:41:17', '2025-05-07 14:41:17'),
	(47, 'Bánh Mỳ Gối Gạo lứt', 'Sản phẩm ngon tuyệt!', 30000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_mi_goi_gao_lut_d12732b07cd34e61a2a9ed154f078280_grande.jpg', 2, '2025-05-07 14:41:17', '2025-05-07 14:41:17'),
	(48, 'Bánh Mỳ Gối Khoai Lang', 'Sản phẩm ngon tuyệt!', 30000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_mi_goi_khoai_lang_caccc655f0144431b35595e98b57e984_grande.jpg', 2, '2025-05-07 14:41:17', '2025-05-07 14:41:17'),
	(49, 'Bánh mỳ gối men tự nhiên', 'Sản phẩm ngon tuyệt!', 28000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-mi-goi-men-tu-nhien_534e3074f4f94e13b8bfe185df4f5595_grande.png', 2, '2025-05-07 14:41:17', '2025-05-07 14:41:17'),
	(50, 'Bánh mỳ hoa cúc đậu đỏ', 'Sản phẩm ngon tuyệt!', 16500.00, 10, 'https://product.hstatic.net/1000104153/product/banh-my-hoa-cuc-dau-do_1ea03b557b504af59569d60527c8d5e3_grande.png', 2, '2025-05-07 14:41:17', '2025-05-07 14:41:17'),
	(51, 'Bánh mỳ hoa cúc lá dứa', 'Sản phẩm ngon tuyệt!', 16500.00, 10, 'https://product.hstatic.net/1000104153/product/banh-mi-hoa-cuc-la-dua_a99d7ec505e44021ba71e7d2d2179d04_grande.png', 2, '2025-05-07 14:41:17', '2025-05-07 14:41:17'),
	(52, 'Bánh mỳ ngũ cốc', 'Sản phẩm ngon tuyệt!', 15000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-my-ngu-coc_078fd032747947438459fdf6fb073276_grande.png', 2, '2025-05-07 14:41:17', '2025-05-07 14:41:17'),
	(53, 'Bánh mỳ nhỏ', 'Sản phẩm ngon tuyệt!', 3000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-my-nho_6e37f27463984d64b07733cf7c31c1ed_grande.jpg', 2, '2025-05-07 14:41:17', '2025-05-07 14:41:17'),
	(54, 'Bánh mỳ Sandwich', 'Sản phẩm ngon tuyệt!', 6000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-my-sandwich_3af059202a3e4a2fb31d28bb771fb5b0_grande.jpg', 2, '2025-05-07 14:41:17', '2025-05-07 14:41:17'),
	(55, 'Bánh mỳ sấy khô Raisin Finger', 'Sản phẩm ngon tuyệt!', 14000.00, 10, 'https://product.hstatic.net/1000104153/product/raisin_finger_2f3b272bed664d61a84da8d606603133_grande.png', 2, '2025-05-07 14:41:17', '2025-05-07 14:41:17'),
	(56, 'Biscotte bơ đường', 'Sản phẩm ngon tuyệt!', 22000.00, 10, 'https://product.hstatic.net/1000104153/product/biscot_toi_2b626bd367944c5190a46aad2fe2589d_grande.jpg', 2, '2025-05-07 14:41:17', '2025-05-07 14:41:17'),
	(57, 'Biscotte gừng', 'Sản phẩm ngon tuyệt!', 22000.00, 10, 'https://product.hstatic.net/1000104153/product/biscot_gung_af41286a8c274868b61881cb6d58e949_grande.jpg', 2, '2025-05-07 14:41:17', '2025-05-07 14:41:17'),
	(58, 'Oregano stick', 'Sản phẩm ngon tuyệt!', 33000.00, 10, 'https://product.hstatic.net/1000104153/product/oregano-stick_918b4452556442a1950f8add8d38f22f_grande.png', 2, '2025-05-07 14:41:17', '2025-05-07 14:41:17'),
	(59, 'Bánh Croissant', 'Sản phẩm ngon tuyệt!', 16000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_croissant_48cb95788ca14f08bd311bb0caf5b2b7_grande.jpg', 3, '2025-05-07 14:42:20', '2025-05-07 14:42:20'),
	(60, 'Bánh Cuộn Ruốc', 'Sản phẩm ngon tuyệt!', 20000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_cuon_1_be2c9ecae17b45989ad96f612527c869_grande.jpg', 3, '2025-05-07 14:42:20', '2025-05-07 14:42:20'),
	(61, 'Bánh mỳ nhân thịt phomai', 'Sản phẩm ngon tuyệt!', 20000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-my-nhan-thit-phomai_751d0dbe800c46feb1d67c4c4c67df33_grande.png', 3, '2025-05-07 14:42:20', '2025-05-07 14:42:20'),
	(62, 'Bánh mỳ nhân xúc xích phomai', 'Sản phẩm ngon tuyệt!', 18000.00, 10, 'https://product.hstatic.net/1000104153/product/52_ad9e6cc7eaca46179ed3d01f85265d3d_grande.png', 3, '2025-05-07 14:42:20', '2025-05-07 14:42:20'),
	(63, 'Bánh mỳ ruốc', 'Sản phẩm ngon tuyệt!', 15000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-my-ruoc_d06bdb081195415aba7bae74659b3038_grande.png', 3, '2025-05-07 14:42:20', '2025-05-07 14:42:20'),
	(64, 'Bánh Pain aux chocolate', 'Sản phẩm ngon tuyệt!', 16000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_pain_aux_chocolate_cc4b191927e046acbf4f588077d1ee2e_grande.jpg', 3, '2025-05-07 14:42:20', '2025-05-07 14:42:20'),
	(65, 'Bánh Pain aux raisins', 'Sản phẩm ngon tuyệt!', 16000.00, 10, 'https://product.hstatic.net/1000104153/product/c91f7343936a3a34637b_2fa36cb39abc47bf8f19ee824231b58b_grande.jpg', 3, '2025-05-07 14:42:20', '2025-05-07 14:42:20'),
	(66, 'Bánh Paté chaud', 'Sản phẩm ngon tuyệt!', 25000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_pate_chaud_b69a770c299d4dc38339ce551299bade_grande.jpg', 3, '2025-05-07 14:42:20', '2025-05-07 14:42:20'),
	(67, 'Bông lan trứng muối', 'Sản phẩm ngon tuyệt!', 29000.00, 10, 'https://product.hstatic.net/1000104153/product/bong_lan_trung_muoi_27703bc84bc64cd3acebd635de4cbe1c_grande.jpg', 3, '2025-05-07 14:42:20', '2025-05-07 14:42:20'),
	(68, 'Feuillete Au Chocolat', 'Sản phẩm ngon tuyệt!', 20000.00, 10, 'https://product.hstatic.net/1000104153/product/mini_feuillete_au_chocolat__9017912914f44bcdb894f292d4ef19a1_grande.jpg', 3, '2025-05-07 14:42:20', '2025-05-07 14:42:20'),
	(69, 'Floss Pork Bread', 'Sản phẩm ngon tuyệt!', 12000.00, 10, 'https://product.hstatic.net/1000104153/product/floss_pork_bread_ad4edc79b1f847c595eaf84f3f08f9b8_grande.jpg', 3, '2025-05-07 14:42:20', '2025-05-07 14:42:20'),
	(70, 'Ruốc Cheese Croffle', 'Sản phẩm ngon tuyệt!', 17000.00, 10, 'https://product.hstatic.net/1000104153/product/ruoc_cheese_croffle_4090781f15cc483a823683792a1ebc9a_grande.jpg', 3, '2025-05-07 14:42:20', '2025-05-07 14:42:20'),
	(71, 'Smoked Sausage Bread', 'Sản phẩm ngon tuyệt!', 20000.00, 10, 'https://product.hstatic.net/1000104153/product/mini_smoked_sausage_bread__7ddc6323b03b4706add38ff55a671aa6_grande.jpg', 3, '2025-05-07 14:42:20', '2025-05-07 14:42:20'),
	(72, 'Yonsei Bread', 'Sản phẩm ngon tuyệt!', 12000.00, 10, 'https://product.hstatic.net/1000104153/product/yonsei_bread_4ded2b0a39644bf897df46f6a8e3bb48_grande.jpg', 3, '2025-05-07 14:42:20', '2025-05-07 14:42:20'),
	(73, 'Hawaii mousse', 'Sản phẩm ngon tuyệt!', 295000.00, 10, 'https://product.hstatic.net/1000104153/product/hawaii_mousse_0b7634f35012441cacaf833c24b4a793_grande.png', 4, '2025-05-07 14:43:38', '2025-05-07 14:43:38'),
	(74, 'Tiramisu cake', 'Sản phẩm ngon tuyệt!', 325000.00, 10, 'https://product.hstatic.net/1000104153/product/tiramisu_cake_16a01603c84a4217826a59da6c6f6cfd_grande.jpg', 4, '2025-05-07 14:43:38', '2025-05-07 14:43:38'),
	(75, 'Passion fruit & chocolate cake', 'Sản phẩm ngon tuyệt!', 315000.00, 10, 'https://product.hstatic.net/1000104153/product/passion_fruit___chocolate_cake_81a17ce02408407b8ac29d61cd7abd9b_grande.png', 4, '2025-05-07 14:43:38', '2025-05-07 14:43:38'),
	(76, 'Black Forest cake', 'Sản phẩm ngon tuyệt!', 295000.00, 10, 'https://product.hstatic.net/1000104153/product/060b4193-9b9c-45db-9d42-aaca342699ad_eaebe617eb7e4039992a2fccc3457d76_grande.jpg', 4, '2025-05-07 14:43:38', '2025-05-07 14:43:38'),
	(77, 'Mango mousse', 'Sản phẩm ngon tuyệt!', 295000.00, 10, 'https://product.hstatic.net/1000104153/product/mango_mousse_2025_7f665dc999544e8ab9c7c20d3635b75b_grande.jpg', 4, '2025-05-07 14:43:38', '2025-05-07 14:43:38'),
	(78, 'White cheese and caramel cake', 'Sản phẩm ngon tuyệt!', 325000.00, 10, 'https://product.hstatic.net/1000104153/product/white_cheese_and_caramel_cake_2024_00779bd73b1e4e73b1f987fc1c3d148f_grande.jpg', 4, '2025-05-07 14:43:39', '2025-05-07 14:43:39'),
	(79, 'Mango cheese cake', 'Sản phẩm ngon tuyệt!', 335000.00, 10, 'https://product.hstatic.net/1000104153/product/mango_cheese_cake_1a48de49feea4aa79b53b9e992125e4c_grande.jpg', 4, '2025-05-07 14:43:39', '2025-05-07 14:43:39'),
	(80, 'Passion fruit mousse', 'Sản phẩm ngon tuyệt!', 285000.00, 10, 'https://product.hstatic.net/1000104153/product/dd525367-1d09-4c51-9e38-d8625bbadd0a_a9424dae1a8e46859efdbf10dc8b20e2_grande.jpg', 4, '2025-05-07 14:43:39', '2025-05-07 14:43:39'),
	(81, 'Seasonal Fruit cake', 'Sản phẩm ngon tuyệt!', 295000.00, 10, 'https://product.hstatic.net/1000104153/product/seasonal_fruit_cake_2e127af76a234fe9b261d154309456ed_grande.png', 4, '2025-05-07 14:43:39', '2025-05-07 14:43:39'),
	(82, 'Tropical Forest cake', 'Sản phẩm ngon tuyệt!', 315000.00, 10, 'https://product.hstatic.net/1000104153/product/tropical_forest_cake_748bf1e0840b49afa81a57b6a9aae165_grande.png', 4, '2025-05-07 14:43:39', '2025-05-07 14:43:39'),
	(83, 'Red velvet cake', 'Sản phẩm ngon tuyệt!', 315000.00, 10, 'https://product.hstatic.net/1000104153/product/red_velvet_cake_04dccd0577264305bf1b9a6b59f25a39_grande.jpg', 4, '2025-05-07 14:43:39', '2025-05-07 14:43:39'),
	(84, 'Kaolé cake', 'Sản phẩm ngon tuyệt!', 325000.00, 10, 'https://product.hstatic.net/1000104153/product/kaole_cake_2b2640a38c9d462f8acb0de7d1c84342_grande.jpg', 4, '2025-05-07 14:43:39', '2025-05-07 14:43:39'),
	(85, 'Green tea cheese cake', 'Sản phẩm ngon tuyệt!', 275000.00, 10, 'https://product.hstatic.net/1000104153/product/green_tea_cheese_cake_2fcf366c3e214cfeb6c44e847059e273_grande.png', 4, '2025-05-07 14:43:39', '2025-05-07 14:43:39'),
	(86, 'Bánh Sữa chua Phô mai', 'Sản phẩm ngon tuyệt!', 335000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_sua_chua_pho_mai_47fe700fae3d43e68f11307b48a5e8bf_grande.jpg', 4, '2025-05-07 14:43:39', '2025-05-07 14:43:39'),
	(87, 'Coconut & Strawberry charlotte', 'Sản phẩm ngon tuyệt!', 295000.00, 10, 'https://product.hstatic.net/1000104153/product/01fc0e8c-cb46-4aad-bd65-5aacafa89c82_de7189e29f7f414ba03d1c0d5f1a4959_grande.jpg', 4, '2025-05-07 14:43:39', '2025-05-07 14:43:39'),
	(88, 'Chocolate & Strawberry charlotte', 'Sản phẩm ngon tuyệt!', 295000.00, 10, 'https://product.hstatic.net/1000104153/product/chocolate___strawberry_charlotte_b8e54856d0704deeb93f9d09ffa125e4_grande.png', 4, '2025-05-07 14:43:39', '2025-05-07 14:43:39'),
	(89, 'Bánh Phúc bồn tử & Sữa chua', 'Sản phẩm ngon tuyệt!', 335000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_phuc_bon_tu___sua_chua_30ae569119b24747a9ab7930f4f3e5b4_grande.jpg', 4, '2025-05-07 14:43:39', '2025-05-07 14:43:39'),
	(90, 'Vanilla Cheese Cake', 'Sản phẩm ngon tuyệt!', 295000.00, 10, 'https://product.hstatic.net/1000104153/product/vanilla_cheese_cake_6_pax_cdee19e640b645378df936cef0281fc2_grande.jpg', 4, '2025-05-07 14:43:39', '2025-05-07 14:43:39'),
	(91, 'Socola Cake', 'Sản phẩm ngon tuyệt!', 300000.00, 10, 'https://product.hstatic.net/1000104153/product/socola_cake_dd9dfa9425e846c0950d6978d7a2981d_grande.jpg', 4, '2025-05-07 14:43:39', '2025-05-07 14:43:39'),
	(92, 'Mango Fruit Cake', 'Sản phẩm ngon tuyệt!', 285000.00, 10, 'https://product.hstatic.net/1000104153/product/mango_fruit_cake_031fc926091b4698a1a7099521ad8764_grande.jpg', 4, '2025-05-07 14:43:39', '2025-05-07 14:43:39');

-- Dumping structure for table tmdt.promotions
CREATE TABLE IF NOT EXISTS `promotions` (
  `promotion_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(150) NOT NULL,
  `description` text,
  `discount_percentage` decimal(5,2) NOT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `created_by` int NOT NULL,
  PRIMARY KEY (`promotion_id`),
  KEY `created_by` (`created_by`),
  CONSTRAINT `promotions_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT,
  CONSTRAINT `promotions_chk_1` CHECK ((`discount_percentage` between 0 and 100))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.promotions: ~0 rows (approximately)
DELETE FROM `promotions`;

-- Dumping structure for table tmdt.reports
CREATE TABLE IF NOT EXISTS `reports` (
  `report_id` int NOT NULL AUTO_INCREMENT,
  `store_id` int NOT NULL,
  `total_revenue` decimal(10,2) NOT NULL,
  `report_type` enum('daily','monthly','quarterly') NOT NULL,
  `report_date` date NOT NULL,
  PRIMARY KEY (`report_id`),
  KEY `store_id` (`store_id`),
  CONSTRAINT `reports_ibfk_1` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`) ON DELETE RESTRICT,
  CONSTRAINT `reports_chk_1` CHECK ((`total_revenue` >= 0))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.reports: ~0 rows (approximately)
DELETE FROM `reports`;

-- Dumping structure for table tmdt.reviews
CREATE TABLE IF NOT EXISTS `reviews` (
  `review_id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `user_id` int NOT NULL,
  `rating` int NOT NULL,
  `comment` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`review_id`),
  UNIQUE KEY `product_id` (`product_id`,`user_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `reviews_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE,
  CONSTRAINT `reviews_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `reviews_chk_1` CHECK ((`rating` between 1 and 5))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.reviews: ~0 rows (approximately)
DELETE FROM `reviews`;

-- Dumping structure for table tmdt.stores
CREATE TABLE IF NOT EXISTS `stores` (
  `store_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(150) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `manager_id` int NOT NULL,
  PRIMARY KEY (`store_id`),
  KEY `manager_id` (`manager_id`),
  CONSTRAINT `stores_ibfk_1` FOREIGN KEY (`manager_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.stores: ~0 rows (approximately)
DELETE FROM `stores`;

-- Dumping structure for table tmdt.users
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(150) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `role` enum('customer','manager','admin') NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.users: ~0 rows (approximately)
DELETE FROM `users`;

-- Dumping structure for table tmdt.user_profiles
CREATE TABLE IF NOT EXISTS `user_profiles` (
  `profile_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `full_name` varchar(150) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`profile_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `user_profiles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.user_profiles: ~0 rows (approximately)
DELETE FROM `user_profiles`;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
