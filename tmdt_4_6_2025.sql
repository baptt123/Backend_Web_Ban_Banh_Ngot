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
DROP DATABASE IF EXISTS `tmdt`;
CREATE DATABASE IF NOT EXISTS `tmdt` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `tmdt`;

-- Dumping structure for table tmdt.categories
DROP TABLE IF EXISTS `categories`;
CREATE TABLE IF NOT EXISTS `categories` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` text,
  `store_id` int DEFAULT NULL,
  `deleted` int NOT NULL,
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `name` (`name`),
  KEY `FKswh8ov7e46aj6bf053xla2c6b` (`store_id`),
  CONSTRAINT `FKswh8ov7e46aj6bf053xla2c6b` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.categories: ~4 rows (approximately)
DELETE FROM `categories`;
INSERT INTO `categories` (`category_id`, `name`, `description`, `store_id`, `deleted`) VALUES
	(1, 'bánh ngọt', 'Các loại bánh ngọt đa dạng', 2, 0),
	(2, 'bánh mì', 'Các loại bánh mì truyền thống và hiện đại', 1, 1),
	(3, 'bánh tươi', 'Bánh tươi mới mỗi ngày', 1, 0),
	(4, 'bánh sinh nhật', 'Bánh sinh nhật tùy chỉnh', 1, 0),
	(5, 'bánh healthy', 'Bánh healthy phù hợp với người ăn kiêng', 1, 0);

-- Dumping structure for table tmdt.comments
DROP TABLE IF EXISTS `comments`;
CREATE TABLE IF NOT EXISTS `comments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `product_id` int DEFAULT NULL,
  `content` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.comments: ~5 rows (approximately)
DELETE FROM `comments`;
INSERT INTO `comments` (`id`, `user_id`, `product_id`, `content`, `created_at`) VALUES
	(1, 1, 8, 'Sản phẩm này được đấy', '2025-05-06 11:03:03'),
	(2, 1, 20, 'Sản phẩm này được đấy', '2025-05-06 11:03:15'),
	(3, 1, 20, 'Sản phẩm này được đấy', '2025-05-06 11:03:17'),
	(5, 1, 1, 'èwef', '2025-05-23 20:00:49'),
	(6, 1, 1, 'f2ef2f', '2025-05-23 20:00:52'),
	(7, 1, 1, 'dsgsdg', '2025-05-24 13:28:40');

-- Dumping structure for table tmdt.complaints
DROP TABLE IF EXISTS `complaints`;
CREATE TABLE IF NOT EXISTS `complaints` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `customer_id` int NOT NULL,
  `complaint_text` text NOT NULL,
  `admin_response` text,
  `status` enum('pending','in_progress','resolved','rejected') DEFAULT 'pending',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `complaints_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `complaints_ibfk_2` FOREIGN KEY (`customer_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.complaints: ~0 rows (approximately)
DELETE FROM `complaints`;

-- Dumping structure for table tmdt.orders
DROP TABLE IF EXISTS `orders`;
CREATE TABLE IF NOT EXISTS `orders` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `store_id` int NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `status` enum('PENDING','CONFIRMED','SHIPPED','DELIVERED','CANCELED') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `payment_method` enum('COD','ONLINE') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`order_id`),
  KEY `user_id` (`user_id`),
  KEY `store_id` (`store_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `orders_chk_1` CHECK ((`total_amount` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.orders: ~3 rows (approximately)
DELETE FROM `orders`;
INSERT INTO `orders` (`order_id`, `user_id`, `store_id`, `total_amount`, `status`, `payment_method`, `created_at`, `updated_at`) VALUES
	(1, 1, 1, 45000.00, 'CONFIRMED', 'COD', '2025-05-10 10:00:00', '2025-05-10 10:30:00'),
	(2, 1, 1, 135000.00, 'PENDING', 'ONLINE', '2025-05-11 14:00:00', '2025-05-11 14:00:00'),
	(3, 1, 1, 590000.00, 'SHIPPED', 'COD', '2025-05-12 09:00:00', '2025-05-12 15:00:00');

-- Dumping structure for table tmdt.order_details
DROP TABLE IF EXISTS `order_details`;
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
  CONSTRAINT `order_details_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `order_details_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `order_details_chk_1` CHECK ((`quantity` > 0)),
  CONSTRAINT `order_details_chk_2` CHECK ((`price` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.order_details: ~6 rows (approximately)
DELETE FROM `order_details`;
INSERT INTO `order_details` (`order_detail_id`, `order_id`, `product_id`, `quantity`, `price`, `customization`) VALUES
	(1, 1, 1, 2, 15000.00, 'Add extra cream'),
	(2, 1, 5, 1, 15000.00, NULL),
	(3, 2, 3, 2, 50000.00, 'Assorted colors'),
	(4, 2, 7, 2, 17000.00, NULL),
	(5, 3, 54, 2, 295000.00, 'Add birthday message'),
	(6, 3, 16, 1, 10000.00, NULL);

-- Dumping structure for table tmdt.order_promotions
DROP TABLE IF EXISTS `order_promotions`;
CREATE TABLE IF NOT EXISTS `order_promotions` (
  `order_promotion_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `promotion_id` int NOT NULL,
  PRIMARY KEY (`order_promotion_id`),
  UNIQUE KEY `order_id_promotion_id` (`order_id`,`promotion_id`),
  KEY `promotion_id` (`promotion_id`),
  CONSTRAINT `order_promotions_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `order_promotions_ibfk_2` FOREIGN KEY (`promotion_id`) REFERENCES `promotions` (`promotion_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.order_promotions: ~2 rows (approximately)
DELETE FROM `order_promotions`;
INSERT INTO `order_promotions` (`order_promotion_id`, `order_id`, `promotion_id`) VALUES
	(1, 2, 3),
	(2, 3, 2);

-- Dumping structure for table tmdt.products
DROP TABLE IF EXISTS `products`;
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
  `store_id` int DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  KEY `category_id` (`category_id`),
  KEY `FKgcyffheofvmy2x5l78xam63mc` (`store_id`),
  CONSTRAINT `FKgcyffheofvmy2x5l78xam63mc` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`),
  CONSTRAINT `products_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `products_chk_1` CHECK ((`price` >= 0)),
  CONSTRAINT `products_chk_2` CHECK ((`stock` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.products: ~73 rows (approximately)
DELETE FROM `products`;
INSERT INTO `products` (`product_id`, `name`, `description`, `price`, `stock`, `image_url`, `category_id`, `created_at`, `updated_at`, `store_id`) VALUES
	(1, 'Flan Pudding Trà Xanh', 'Sản phẩm ngon tuyệt!', 15000.00, 10, 'https://product.hstatic.net/1000104153/product/flan_pudding_tra_xanh_bd9cf52aba374a52b647af883587abbc_grande.jpg', 1, '2025-05-07 14:39:38', '2025-06-02 10:51:20', 1),
	(2, 'Flan Pudding Chocolate', 'Sản phẩm ngon tuyệt!', 15000.00, 10, 'https://product.hstatic.net/1000104153/product/flan_pudding_chocolate_2746e93548334140aa616e258d2e30fa_grande.jpg', 1, '2025-05-07 14:39:38', '2025-06-02 10:50:43', 1),
	(3, 'Rainbow Macaron', 'Sản phẩm ngon tuyệt!', 50000.00, 10, 'https://product.hstatic.net/1000104153/product/24_b81e174555ae494ea8e431ee8f1bce2b_grande.png', 1, '2025-05-07 14:39:38', '2025-06-02 10:50:45', 1),
	(4, 'Croffle Chocolate Đen', 'Sản phẩm ngon tuyệt!', 23000.00, 10, 'https://product.hstatic.net/1000104153/product/croffle_chocolate_den_0fc695e6cb0a4124b6d68d4470b32ba5_grande.jpg', 1, '2025-05-07 14:39:38', '2025-06-02 10:50:46', 1),
	(5, 'Bánh Cuộn Chocolate', 'Sản phẩm ngon tuyệt!', 15000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_cuon_chocolate_976736d4bd134f65949a7c17b5061648_grande.jpg', 1, '2025-05-07 14:39:38', '2025-06-02 10:51:20', 1),
	(6, 'Bánh ốc quế kem', 'Sản phẩm ngon tuyệt!', 15000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_oc_que_kem_80404fd5a8ca49a0b0a1d8cd652492a5_grande.jpg', 1, '2025-05-07 14:39:38', '2025-06-02 10:51:20', 1),
	(7, 'Bánh donut chocolate đen', 'Sản phẩm ngon tuyệt!', 17000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_donut_chocolate_den_05fa3ff0e45d428d80374c2014958fe1_grande.jpg', 1, '2025-05-07 14:39:38', '2025-06-02 10:51:20', 1),
	(8, 'Bánh donut chocolate trắng', 'Sản phẩm ngon tuyệt!', 17000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_donut_chocolate_trang_6a4209aec9184bb3be2393e93a99aa55_grande.jpg', 1, '2025-05-07 14:39:38', '2025-06-02 10:51:20', 1),
	(9, 'Bánh Donut trà xanh', 'Sản phẩm ngon tuyệt!', 17000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_donut_tra_xanh_568d9d4d9e504af0857ab40982af93f0_grande.jpg', 1, '2025-05-07 14:39:38', '2025-06-02 10:51:20', 1),
	(10, 'Bánh Donut dâu', 'Sản phẩm ngon tuyệt!', 17000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_donut_dau_0494546683f248aaa4cb598c8853b4d1_grande.jpg', 1, '2025-05-07 14:39:38', '2025-06-02 10:51:20', 1),
	(11, 'Bánh Gateaux de basque mini', 'Sản phẩm ngon tuyệt!', 18000.00, 10, 'https://product.hstatic.net/1000104153/product/gateaux_de_basque_d70292b8bdc648f0946e42441b953fee_grande.jpg', 1, '2025-05-07 14:39:38', '2025-06-02 10:51:20', 1),
	(12, 'Bánh dừa nho trứng muối', 'Sản phẩm ngon tuyệt!', 18000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-dua-nho-trung-muoi_07d2ebbe827646eda76a3cb79cc91c3e_grande.png', 1, '2025-05-07 14:39:38', '2025-06-02 10:51:20', 1),
	(13, 'Bánh chuối cake', 'Sản phẩm ngon tuyệt!', 24000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_cake_chuoi_b37a0d0006044a9ab384157a97d682b0_grande.jpg', 1, '2025-05-07 14:39:38', '2025-06-02 10:51:20', 1),
	(14, 'Bánh Butter Raisins', 'Sản phẩm ngon tuyệt!', 30000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-butter-raisins_7104cb207a284a6aa78e52b77852d88b_grande.png', 1, '2025-05-07 14:39:38', '2025-06-02 10:51:20', 1),
	(15, 'Bánh Delicacy Coconut cake', 'Sản phẩm ngon tuyệt!', 30000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-delicacy-coconut-cake_c9c87c108a5849bb916806476b8a940a_grande.png', 1, '2025-05-07 14:39:38', '2025-06-02 10:51:20', 1),
	(16, 'Bánh Madeleine', 'Sản phẩm ngon tuyệt!', 10000.00, 10, 'https://product.hstatic.net/1000104153/product/madeleine-vani_327175c128034aed885e376f621a3fc3_grande.png', 1, '2025-05-07 14:39:38', '2025-06-02 10:51:20', 1),
	(17, 'Bánh Madeleine Chocolate', 'Sản phẩm ngon tuyệt!', 10000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-madeleine-chocolate_d6b8a4a4a1d142939338019774f182db_grande.png', 1, '2025-05-07 14:39:38', '2025-06-02 10:51:20', 1),
	(18, 'Creme Caramel', 'Sản phẩm ngon tuyệt!', 10000.00, 10, 'https://product.hstatic.net/1000104153/product/caramel_0009b29de5c049f0bc60b64592d03ffd_grande.jpg', 1, '2025-05-07 14:39:38', '2025-06-02 10:51:20', 1),
	(19, 'Sponge cake chữ nhật', 'Sản phẩm ngon tuyệt!', 15000.00, 10, 'https://product.hstatic.net/1000104153/product/spong-cake-chu-nhat_b4d2efdb7f9640408c67a9590575c5b2_grande.png', 1, '2025-05-07 14:39:39', '2025-06-02 10:51:20', 1),
	(20, 'Bánh hamburger 3pcs', 'Sản phẩm ngon tuyệt!', 15000.00, 10, 'https://product.hstatic.net/1000104153/product/13_983f4abd084141b6afbaaa0567d88f75_grande.png', 2, '2025-05-07 14:41:16', '2025-06-02 10:51:20', 1),
	(21, 'Bánh mì Hàn Quốc mè đen', 'Sản phẩm ngon tuyệt!', 30000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-my-han-quoc-me-den_3055a3c4a8384158be57025db7d40169_grande.png', 2, '2025-05-07 14:41:16', '2025-06-02 10:51:20', 1),
	(22, 'Bánh mỳ baguette', 'Sản phẩm ngon tuyệt!', 14000.00, 10, 'https://product.hstatic.net/1000104153/product/baguette_8b1506303c57446cb46c6ad8fc5d9b5a_grande.jpg', 2, '2025-05-07 14:41:16', '2025-06-02 10:51:20', 1),
	(23, 'Bánh mỳ Brioche', 'Sản phẩm ngon tuyệt!', 20000.00, 10, 'https://product.hstatic.net/1000104153/product/brioche_1fe61245a8c94e0da7cd5ceb47c7c5a2_grande.jpg', 2, '2025-05-07 14:41:16', '2025-06-02 10:51:20', 1),
	(24, 'Bánh mỳ đen', 'Sản phẩm ngon tuyệt!', 15000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-my-den_ac5853a1b23f4ad8814bc7327e26e11d_grande.png', 2, '2025-05-07 14:41:16', '2025-06-02 10:51:20', 1),
	(25, 'Bánh mỳ gối', 'Sản phẩm ngon tuyệt!', 26000.00, 10, 'https://product.hstatic.net/1000104153/product/gato-bass-mini__5__445f605e3ce444ffa38e02414ede4a9e_grande.png', 2, '2025-05-07 14:41:17', '2025-06-02 10:51:20', 1),
	(26, 'Bánh Mỳ Gối Bí Đỏ', 'Sản phẩm ngon tuyệt!', 30000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_mi_goi_bi_do_a737a635dd33411eba9002d4a93ec925_grande.jpg', 2, '2025-05-07 14:41:17', '2025-06-02 10:51:20', 1),
	(27, 'Bánh mỳ gối đen', 'Sản phẩm ngon tuyệt!', 28000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-mi-goi-den-1_e5f7881eac4240609dd0fbfe02ddaef9_grande.png', 2, '2025-05-07 14:41:17', '2025-06-02 10:51:20', 1),
	(28, 'Bánh Mỳ Gối Gạo lứt', 'Sản phẩm ngon tuyệt!', 30000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_mi_goi_gao_lut_d12732b07cd34e61a2a9ed154f078280_grande.jpg', 2, '2025-05-07 14:41:17', '2025-06-02 10:51:20', 1),
	(29, 'Bánh Mỳ Gối Khoai Lang', 'Sản phẩm ngon tuyệt!', 30000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_mi_goi_khoai_lang_caccc655f0144431b35595e98b57e984_grande.jpg', 2, '2025-05-07 14:41:17', '2025-06-02 10:51:20', 1),
	(30, 'Bánh mỳ gối men tự nhiên', 'Sản phẩm ngon tuyệt!', 28000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-mi-goi-men-tu-nhien_534e3074f4f94e13b8bfe185df4f5595_grande.png', 2, '2025-05-07 14:41:17', '2025-06-02 10:51:20', 1),
	(31, 'Bánh mỳ hoa cúc đậu đỏ', 'Sản phẩm ngon tuyệt!', 16500.00, 10, 'https://product.hstatic.net/1000104153/product/banh-my-hoa-cuc-dau-do_1ea03b557b504af59569d60527c8d5e3_grande.png', 2, '2025-05-07 14:41:17', '2025-06-02 10:51:20', 1),
	(32, 'Bánh mỳ hoa cúc lá dứa', 'Sản phẩm ngon tuyệt!', 16500.00, 10, 'https://product.hstatic.net/1000104153/product/banh-mi-hoa-cuc-la-dua_a99d7ec505e44021ba71e7d2d2179d04_grande.png', 2, '2025-05-07 14:41:17', '2025-06-02 10:51:20', 1),
	(33, 'Bánh mỳ ngũ cốc', 'Sản phẩm ngon tuyệt!', 15000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-my-ngu-coc_078fd032747947438459fdf6fb073276_grande.png', 2, '2025-05-07 14:41:17', '2025-06-02 10:51:20', 1),
	(34, 'Bánh mỳ nhỏ', 'Sản phẩm ngon tuyệt!', 3000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-my-nho_6e37f27463984d64b07733cf7c31c1ed_grande.jpg', 2, '2025-05-07 14:41:17', '2025-06-02 10:51:20', 1),
	(35, 'Bánh mỳ Sandwich', 'Sản phẩm ngon tuyệt!', 6000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-my-sandwich_3af059202a3e4a2fb31d28bb771fb5b0_grande.jpg', 2, '2025-05-07 14:41:17', '2025-06-02 10:51:20', 1),
	(36, 'Bánh mỳ sấy khô Raisin Finger', 'Sản phẩm ngon tuyệt!', 14000.00, 10, 'https://product.hstatic.net/1000104153/product/raisin_finger_2f3b272bed664d61a84da8d606603133_grande.png', 2, '2025-05-07 14:41:17', '2025-06-02 10:51:20', 1),
	(37, 'Biscotte bơ đường', 'Sản phẩm ngon tuyệt!', 22000.00, 10, 'https://product.hstatic.net/1000104153/product/biscot_toi_2b626bd367944c5190a46aad2fe2589d_grande.jpg', 2, '2025-05-07 14:41:17', '2025-06-02 10:51:20', 1),
	(38, 'Biscotte gừng', 'Sản phẩm ngon tuyệt!', 22000.00, 10, 'https://product.hstatic.net/1000104153/product/biscot_gung_af41286a8c274868b61881cb6d58e949_grande.jpg', 2, '2025-05-07 14:41:17', '2025-06-02 10:51:20', 1),
	(39, 'Oregano stick', 'Sản phẩm ngon tuyệt!', 33000.00, 10, 'https://product.hstatic.net/1000104153/product/oregano-stick_918b4452556442a1950f8add8d38f22f_grande.png', 2, '2025-05-07 14:41:17', '2025-06-02 10:51:20', 1),
	(40, 'Bánh Croissant', 'Sản phẩm ngon tuyệt!', 16000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_croissant_48cb95788ca14f08bd311bb0caf5b2b7_grande.jpg', 3, '2025-05-07 14:42:20', '2025-06-02 10:51:20', 1),
	(41, 'Bánh Cuộn Ruốc', 'Sản phẩm ngon tuyệt!', 20000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_cuon_1_be2c9ecae17b45989ad96f612527c869_grande.jpg', 3, '2025-05-07 14:42:20', '2025-06-02 10:51:20', 1),
	(42, 'Bánh mỳ nhân thịt phomai', 'Sản phẩm ngon tuyệt!', 20000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-my-nhan-thit-phomai_751d0dbe800c46feb1d67c4c4c67df33_grande.png', 3, '2025-05-07 14:42:20', '2025-06-02 10:51:20', 1),
	(43, 'Bánh mỳ nhân xúc xích phomai', 'Sản phẩm ngon tuyệt!', 18000.00, 10, 'https://product.hstatic.net/1000104153/product/52_ad9e6cc7eaca46179ed3d01f85265d3d_grande.png', 3, '2025-05-07 14:42:20', '2025-06-02 10:51:20', 1),
	(44, 'Bánh mỳ ruốc', 'Sản phẩm ngon tuyệt!', 15000.00, 10, 'https://product.hstatic.net/1000104153/product/banh-my-ruoc_d06bdb081195415aba7bae74659b3038_grande.png', 3, '2025-05-07 14:42:20', '2025-06-02 10:51:20', 1),
	(45, 'Bánh Pain aux chocolate', 'Sản phẩm ngon tuyệt!', 16000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_pain_aux_chocolate_cc4b191927e046acbf4f588077d1ee2e_grande.jpg', 3, '2025-05-07 14:42:20', '2025-06-02 10:51:20', 1),
	(46, 'Bánh Pain aux raisins', 'Sản phẩm ngon tuyệt!', 16000.00, 10, 'https://product.hstatic.net/1000104153/product/c91f7343936a3a34637b_2fa36cb39abc47bf8f19ee824231b58b_grande.jpg', 3, '2025-05-07 14:42:20', '2025-06-02 10:51:20', 1),
	(47, 'Bánh Paté chaud', 'Sản phẩm ngon tuyệt!', 25000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_pate_chaud_b69a770c299d4dc38339ce551299bade_grande.jpg', 3, '2025-05-07 14:42:20', '2025-06-02 10:51:20', 1),
	(48, 'Bông lan trứng muối', 'Sản phẩm ngon tuyệt!', 29000.00, 10, 'https://product.hstatic.net/1000104153/product/bong_lan_trung_muoi_27703bc84bc64cd3acebd635de4cbe1c_grande.jpg', 3, '2025-05-07 14:42:20', '2025-06-02 10:51:20', 1),
	(49, 'Feuillete Au Chocolat', 'Sản phẩm ngon tuyệt!', 20000.00, 10, 'https://product.hstatic.net/1000104153/product/mini_feuillete_au_chocolat__9017912914f44bcdb894f292d4ef19a1_grande.jpg', 3, '2025-05-07 14:42:20', '2025-06-02 10:51:20', 1),
	(50, 'Floss Pork Bread', 'Sản phẩm ngon tuyệt!', 12000.00, 10, 'https://product.hstatic.net/1000104153/product/floss_pork_bread_ad4edc79b1f847c595eaf84f3f08f9b8_grande.jpg', 3, '2025-05-07 14:42:20', '2025-06-02 10:51:20', 1),
	(51, 'Ruốc Cheese Croffle', 'Sản phẩm ngon tuyệt!', 17000.00, 10, 'https://product.hstatic.net/1000104153/product/ruoc_cheese_croffle_4090781f15cc483a823683792a1ebc9a_grande.jpg', 3, '2025-05-07 14:42:20', '2025-06-02 10:51:20', 1),
	(52, 'Smoked Sausage Bread', 'Sản phẩm ngon tuyệt!', 20000.00, 10, 'https://product.hstatic.net/1000104153/product/mini_smoked_sausage_bread__7ddc6323b03b4706add38ff55a671aa6_grande.jpg', 3, '2025-05-07 14:42:20', '2025-06-02 10:51:20', 1),
	(53, 'Yonsei Bread', 'Sản phẩm ngon tuyệt!', 12000.00, 10, 'https://product.hstatic.net/1000104153/product/yonsei_bread_4ded2b0a39644bf897df46f6a8e3bb48_grande.jpg', 3, '2025-05-07 14:42:20', '2025-06-02 10:51:20', 1),
	(54, 'Hawaii mousse', 'Sản phẩm ngon tuyệt!', 295000.00, 10, 'https://product.hstatic.net/1000104153/product/hawaii_mousse_0b7634f35012441cacaf833c24b4a793_grande.png', 4, '2025-05-07 14:43:38', '2025-06-02 10:51:20', 1),
	(55, 'Tiramisu cake', 'Sản phẩm ngon tuyệt!', 325000.00, 10, 'https://product.hstatic.net/1000104153/product/tiramisu_cake_16a01603c84a4217826a59da6c6f6cfd_grande.jpg', 4, '2025-05-07 14:43:38', '2025-06-02 10:51:20', 1),
	(56, 'Passion fruit & chocolate cake', 'Sản phẩm ngon tuyệt!', 315000.00, 10, 'https://product.hstatic.net/1000104153/product/passion_fruit___chocolate_cake_81a17ce02408407b8ac29d61cd7abd9b_grande.png', 4, '2025-05-07 14:43:38', '2025-06-02 10:51:20', 1),
	(57, 'Black Forest cake', 'Sản phẩm ngon tuyệt!', 295000.00, 10, 'https://product.hstatic.net/1000104153/product/060b4193-9b9c-45db-9d42-aaca342699ad_eaebe617eb7e4039992a2fccc3457d76_grande.jpg', 4, '2025-05-07 14:43:38', '2025-06-02 10:51:20', 1),
	(58, 'Mango mousse', 'Sản phẩm ngon tuyệt!', 295000.00, 10, 'https://product.hstatic.net/1000104153/product/mango_mousse_2025_7f665dc999544e8ab9c7c20d3635b75b_grande.jpg', 4, '2025-05-07 14:43:38', '2025-06-02 10:51:20', 1),
	(59, 'White cheese and caramel cake', 'Sản phẩm ngon tuyệt!', 325000.00, 10, 'https://product.hstatic.net/1000104153/product/white_cheese_and_caramel_cake_2024_00779bd73b1e4e73b1f987fc1c3d148f_grande.jpg', 4, '2025-05-07 14:43:39', '2025-06-02 10:51:20', 1),
	(60, 'Mango cheese cake', 'Sản phẩm ngon tuyệt!', 335000.00, 10, 'https://product.hstatic.net/1000104153/product/mango_cheese_cake_1a48de49feea4aa79b53b9e992125e4c_grande.jpg', 4, '2025-05-07 14:43:39', '2025-06-02 10:51:20', 1),
	(61, 'Passion fruit mousse', 'Sản phẩm ngon tuyệt!', 285000.00, 10, 'https://product.hstatic.net/1000104153/product/dd525367-1d09-4c51-9e38-d8625bbadd0a_a9424dae1a8e46859efdbf10dc8b20e2_grande.jpg', 4, '2025-05-07 14:43:39', '2025-06-02 10:51:20', 1),
	(62, 'Seasonal Fruit cake', 'Sản phẩm ngon tuyệt!', 295000.00, 10, 'https://product.hstatic.net/1000104153/product/seasonal_fruit_cake_2e127af76a234fe9b261d154309456ed_grande.png', 4, '2025-05-07 14:43:39', '2025-06-02 10:51:20', 1),
	(63, 'Tropical Forest cake', 'Sản phẩm ngon tuyệt!', 315000.00, 10, 'https://product.hstatic.net/1000104153/product/tropical_forest_cake_748bf1e0840b49afa81a57b6a9aae165_grande.png', 4, '2025-05-07 14:43:39', '2025-06-02 10:51:20', 1),
	(64, 'Red velvet cake', 'Sản phẩm ngon tuyệt!', 315000.00, 10, 'https://product.hstatic.net/1000104153/product/red_velvet_cake_04dccd0577264305bf1b9a6b59f25a39_grande.jpg', 4, '2025-05-07 14:43:39', '2025-06-02 10:51:20', 1),
	(65, 'Kaolé cake', 'Sản phẩm ngon tuyệt!', 325000.00, 10, 'https://product.hstatic.net/1000104153/product/kaole_cake_2b2640a38c9d462f8acb0de7d1c84342_grande.jpg', 4, '2025-05-07 14:43:39', '2025-06-02 10:51:20', 1),
	(66, 'Green tea cheese cake', 'Sản phẩm ngon tuyệt!', 275000.00, 10, 'https://product.hstatic.net/1000104153/product/green_tea_cheese_cake_2fcf366c3e214cfeb6c44e847059e273_grande.png', 4, '2025-05-07 14:43:39', '2025-06-02 10:51:20', 1),
	(67, 'Bánh Sữa chua Phô mai', 'Sản phẩm ngon tuyệt!', 335000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_sua_chua_pho_mai_47fe700fae3d43e68f11307b48a5e8bf_grande.jpg', 4, '2025-05-07 14:43:39', '2025-06-02 10:51:20', 1),
	(68, 'Coconut & Strawberry charlotte', 'Sản phẩm ngon tuyệt!', 295000.00, 10, 'https://product.hstatic.net/1000104153/product/01fc0e8c-cb46-4aad-bd65-5aacafa89c82_de7189e29f7f414ba03d1c0d5f1a4959_grande.jpg', 4, '2025-05-07 14:43:39', '2025-06-02 10:51:20', 1),
	(69, 'Chocolate & Strawberry charlotte', 'Sản phẩm ngon tuyệt!', 295000.00, 10, 'https://product.hstatic.net/1000104153/product/chocolate___strawberry_charlotte_b8e54856d0704deeb93f9d09ffa125e4_grande.png', 4, '2025-05-07 14:43:39', '2025-06-02 10:51:20', 1),
	(70, 'Bánh Phúc bồn tử & Sữa chua', 'Sản phẩm ngon tuyệt!', 335000.00, 10, 'https://product.hstatic.net/1000104153/product/banh_phuc_bon_tu___sua_chua_30ae569119b24747a9ab7930f4f3e5b4_grande.jpg', 4, '2025-05-07 14:43:39', '2025-06-02 10:51:20', 1),
	(71, 'Vanilla Cheese Cake', 'Sản phẩm ngon tuyệt!', 295000.00, 10, 'https://product.hstatic.net/1000104153/product/vanilla_cheese_cake_6_pax_cdee19e640b645378df936cef0281fc2_grande.jpg', 4, '2025-05-07 14:43:39', '2025-06-02 10:51:20', 1),
	(72, 'Socola Cake', 'Sản phẩm ngon tuyệt!', 300000.00, 10, 'https://product.hstatic.net/1000104153/product/socola_cake_dd9dfa9425e846c0950d6978d7a2981d_grande.jpg', 4, '2025-05-07 14:43:39', '2025-06-02 10:51:20', 1),
	(73, 'Mango Fruit Cake', 'Sản phẩm ngon tuyệt!', 285000.00, 10, 'https://product.hstatic.net/1000104153/product/mango_fruit_cake_031fc926091b4698a1a7099521ad8764_grande.jpg', 4, '2025-05-07 14:43:39', '2025-06-02 10:51:20', 1);

-- Dumping structure for table tmdt.promotions
DROP TABLE IF EXISTS `promotions`;
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
  CONSTRAINT `promotions_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `promotions_chk_1` CHECK ((`discount_percentage` between 0 and 100)),
  CONSTRAINT `promotions_chk_2` CHECK ((`end_date` > `start_date`))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.promotions: ~5 rows (approximately)
DELETE FROM `promotions`;
INSERT INTO `promotions` (`promotion_id`, `name`, `description`, `discount_percentage`, `start_date`, `end_date`, `created_by`) VALUES
	(1, 'Summer Sweet Deal', 'Get a discount on all cakes and pastries for the summer season!', 15.00, '2025-06-01 00:00:00', '2025-08-31 23:59:59', 1),
	(2, 'Birthday Bash Promo', 'Celebrate your birthday with 20% off any birthday cake!', 20.00, '2025-05-10 00:00:00', '2025-12-31 23:59:59', 1),
	(3, 'Fresh Bread Bonanza', 'Buy any bread and get 10% off your entire order.', 10.00, '2025-05-15 00:00:00', '2025-06-15 23:59:59', 1),
	(4, 'Midweek Macaron Madness', 'Enjoy 25% off all macarons every Wednesday!', 25.00, '2025-05-14 00:00:00', '2025-07-30 23:59:59', 1),
	(5, 'Loyalty Discount', 'Exclusive 30% off for returning customers on orders over 500,000 VND.', 30.00, '2025-07-01 00:00:00', '2025-09-30 23:59:59', 1);

-- Dumping structure for table tmdt.ratings
DROP TABLE IF EXISTS `ratings`;
CREATE TABLE IF NOT EXISTS `ratings` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `user_id` int NOT NULL,
  `rating` int NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `product_id` (`product_id`,`user_id`) USING BTREE,
  KEY `user_id` (`user_id`),
  CONSTRAINT `ratings_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ratings_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ratings_chk_1` CHECK ((`rating` between 1 and 5))
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.ratings: ~6 rows (approximately)
DELETE FROM `ratings`;
INSERT INTO `ratings` (`id`, `product_id`, `user_id`, `rating`, `created_at`) VALUES
	(1, 7, 1, 5, '2025-05-09 12:30:12'),
	(2, 12, 1, 3, '2025-05-09 12:30:27'),
	(3, 15, 1, 2, '2025-05-09 19:30:36'),
	(4, 14, 1, 3, '2025-05-09 19:30:44'),
	(5, 31, 1, 1, '2025-05-09 19:30:55'),
	(6, 1, 1, 2, '2025-05-23 19:58:24');

-- Dumping structure for table tmdt.reports
DROP TABLE IF EXISTS `reports`;
CREATE TABLE IF NOT EXISTS `reports` (
  `report_id` int NOT NULL AUTO_INCREMENT,
  `store_id` int NOT NULL,
  `total_revenue` decimal(10,2) NOT NULL,
  `report_type` enum('daily','monthly','quarterly') NOT NULL,
  `report_date` date NOT NULL,
  PRIMARY KEY (`report_id`),
  KEY `store_id` (`store_id`),
  CONSTRAINT `reports_ibfk_1` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `reports_chk_1` CHECK ((`total_revenue` >= 0))
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.reports: ~0 rows (approximately)
DELETE FROM `reports`;
INSERT INTO `reports` (`report_id`, `store_id`, `total_revenue`, `report_type`, `report_date`) VALUES
	(1, 2, 1000000.00, 'monthly', '2025-05-09');

-- Dumping structure for table tmdt.reset_password_token
DROP TABLE IF EXISTS `reset_password_token`;
CREATE TABLE IF NOT EXISTS `reset_password_token` (
  `id` int NOT NULL AUTO_INCREMENT,
  `expiry_date` datetime(6) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_48tx38u7rrhldrpgxswjwyfhg` (`user_id`),
  CONSTRAINT `FKlu8wgvb3mkbxir0sls596u7np` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.reset_password_token: ~0 rows (approximately)
DELETE FROM `reset_password_token`;

-- Dumping structure for table tmdt.roles
DROP TABLE IF EXISTS `roles`;
CREATE TABLE IF NOT EXISTS `roles` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.roles: ~3 rows (approximately)
DELETE FROM `roles`;
INSERT INTO `roles` (`role_id`, `name`) VALUES
	(3, 'admin'),
	(1, 'customer'),
	(2, 'manager');

-- Dumping structure for table tmdt.stores
DROP TABLE IF EXISTS `stores`;
CREATE TABLE IF NOT EXISTS `stores` (
  `store_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(150) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `manager_id` int NOT NULL,
  PRIMARY KEY (`store_id`),
  KEY `manager_id` (`manager_id`),
  CONSTRAINT `stores_ibfk_1` FOREIGN KEY (`manager_id`) REFERENCES `users` (`user_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.stores: ~2 rows (approximately)
DELETE FROM `stores`;
INSERT INTO `stores` (`store_id`, `name`, `address`, `phone`, `manager_id`) VALUES
	(1, 'Bán bánh ngọt tuyệt vời', 'chưa xác định', '0948362715', 1),
	(2, 'Bánh ngọt tuyệt vời', 'chưa xác định', '0948362715', 1);

-- Dumping structure for table tmdt.store_promotions
DROP TABLE IF EXISTS `store_promotions`;
CREATE TABLE IF NOT EXISTS `store_promotions` (
  `store_promotion_id` int NOT NULL AUTO_INCREMENT,
  `store_id` int NOT NULL,
  `promotion_id` int NOT NULL,
  PRIMARY KEY (`store_promotion_id`),
  UNIQUE KEY `store_id_promotion_id` (`store_id`,`promotion_id`),
  KEY `promotion_id` (`promotion_id`),
  CONSTRAINT `store_promotions_ibfk_1` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `store_promotions_ibfk_2` FOREIGN KEY (`promotion_id`) REFERENCES `promotions` (`promotion_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.store_promotions: ~2 rows (approximately)
DELETE FROM `store_promotions`;
INSERT INTO `store_promotions` (`store_promotion_id`, `store_id`, `promotion_id`) VALUES
	(2, 1, 1),
	(1, 1, 4),
	(3, 2, 5);

-- Dumping structure for table tmdt.users
DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(150) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `role_id` int NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `active` bit(1) NOT NULL,
  `google_id` varchar(255) DEFAULT NULL,
  `store_id` int DEFAULT NULL,
  `deleted` int NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  KEY `role_id` (`role_id`),
  KEY `FK7wra86jadsraitoewujbjj1pd` (`store_id`),
  CONSTRAINT `FK7wra86jadsraitoewujbjj1pd` FOREIGN KEY (`store_id`) REFERENCES `stores` (`store_id`),
  CONSTRAINT `users_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.users: ~3 rows (approximately)
DELETE FROM `users`;
INSERT INTO `users` (`user_id`, `username`, `password`, `email`, `phone`, `role_id`, `created_at`, `updated_at`, `active`, `google_id`, `store_id`, `deleted`) VALUES
	(1, 'tantt121', '3ba1b06022e7eccb725a22f879f975c4b6985d55bc2b1be3148f3ecb84385c38', 'nttan101103@gmail.com', '0917234765', 1, '2025-05-09 12:07:21', '2025-06-02 11:29:17', b'0', 'eqfeqfdeqw', 1, 0),
	(2, 'tandeptrai123', '$2a$10$IPSQfgtskDe.i7NDDDi3EuNGSTKc0scXhocKWRYsNTmIXTd5d2RC6', 'ngoken102@gmail.com', NULL, 1, '2025-05-16 21:17:41', '2025-06-02 11:29:18', b'0', 'qểqreqreq', 1, 0),
	(3, 'baptan123', '$2a$10$BAqF3q.lRsxd.Oi2dAdZ6ed7RZqSxwOCLD4rA/Wx7Ha2xWwI/BlQG', 'nsieu5547@gmail.com', NULL, 1, '2025-05-20 21:11:20', '2025-06-02 11:29:19', b'1', 'qểqreqreqr', 1, 0);

-- Dumping structure for table tmdt.user_profiles
DROP TABLE IF EXISTS `user_profiles`;
CREATE TABLE IF NOT EXISTS `user_profiles` (
  `profile_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `full_name` varchar(150) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`profile_id`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `user_profiles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.user_profiles: ~1 rows (approximately)
DELETE FROM `user_profiles`;
INSERT INTO `user_profiles` (`profile_id`, `user_id`, `full_name`, `address`, `birth_date`, `avatar_url`) VALUES
	(1, 1, 'Thanh Tân', 'chưa xác định', '2025-05-09', 'https://res.cloudinary.com/dii6xe3bq/image/upload/w_1000,ar_1:1,c_fill,g_auto,e_art:hokusai/v1746793217/avatar_atowzg.jpg');

-- Dumping structure for table tmdt.verification_token
DROP TABLE IF EXISTS `verification_token`;
CREATE TABLE IF NOT EXISTS `verification_token` (
  `id` int NOT NULL AUTO_INCREMENT,
  `expiry_date` datetime(6) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_q6jibbenp7o9v6tq178xg88hg` (`user_id`),
  CONSTRAINT `FK3asw9wnv76uxu3kr1ekq4i1ld` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table tmdt.verification_token: ~1 rows (approximately)
DELETE FROM `verification_token`;
INSERT INTO `verification_token` (`id`, `expiry_date`, `token`, `user_id`) VALUES
	(1, '2025-05-17 21:17:41.204833', 'cad7ee71-f1bf-4bb8-918b-c7fdfa2fab4f', 2);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
