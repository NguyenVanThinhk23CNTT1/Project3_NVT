-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: nvt_quanlynhatro
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `amenities`
--

DROP TABLE IF EXISTS `amenities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `amenities` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amenity_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `amenity_desc` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `amenity_name` (`amenity_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `amenities`
--

LOCK TABLES `amenities` WRITE;
/*!40000 ALTER TABLE `amenities` DISABLE KEYS */;
/*!40000 ALTER TABLE `amenities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bill_items`
--

DROP TABLE IF EXISTS `bill_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bill_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bill_id` bigint NOT NULL,
  `service_id` bigint DEFAULT NULL,
  `item_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `quantity` decimal(12,2) NOT NULL DEFAULT '1.00',
  `unit_price` decimal(12,2) NOT NULL DEFAULT '0.00',
  `amount` decimal(12,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id`),
  KEY `bill_id` (`bill_id`),
  KEY `service_id` (`service_id`),
  CONSTRAINT `bill_items_ibfk_1` FOREIGN KEY (`bill_id`) REFERENCES `bills` (`id`),
  CONSTRAINT `bill_items_ibfk_2` FOREIGN KEY (`service_id`) REFERENCES `services` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill_items`
--

LOCK TABLES `bill_items` WRITE;
/*!40000 ALTER TABLE `bill_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `bill_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bills`
--

DROP TABLE IF EXISTS `bills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bills` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `contract_id` bigint NOT NULL,
  `room_id` bigint NOT NULL,
  `bill_month` int NOT NULL,
  `bill_year` int NOT NULL,
  `rent_amount` decimal(38,2) NOT NULL,
  `electric_amount` decimal(38,2) NOT NULL,
  `water_amount` decimal(38,2) NOT NULL,
  `service_amount` decimal(38,2) NOT NULL,
  `discount` decimal(38,2) NOT NULL,
  `total_amount` decimal(38,2) NOT NULL,
  `due_date` date DEFAULT NULL,
  `status` enum('UNPAID','PAID','OVERDUE') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'UNPAID',
  `note` text COLLATE utf8mb4_unicode_ci,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_bill_contract_month_year` (`contract_id`,`bill_month`,`bill_year`),
  KEY `room_id` (`room_id`),
  CONSTRAINT `bills_ibfk_1` FOREIGN KEY (`contract_id`) REFERENCES `contracts` (`id`),
  CONSTRAINT `bills_ibfk_2` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bills`
--

LOCK TABLES `bills` WRITE;
/*!40000 ALTER TABLE `bills` DISABLE KEYS */;
INSERT INTO `bills` VALUES (1,1,1,11,2025,2800000.00,1470000.00,225000.00,0.00,0.00,4495000.00,'2025-11-10','PAID',NULL,'2025-12-03 16:11:36'),(2,3,5,12,2025,4000000.00,458500.00,495000.00,0.00,0.00,4953500.00,'2025-12-10','PAID',NULL,'2025-12-04 21:28:17'),(3,4,4,10,2025,4500000.00,192500.00,390000.00,0.00,0.00,5082500.00,'2025-10-10','UNPAID',NULL,'2025-12-14 15:04:30'),(4,4,4,11,2025,4500000.00,73500.00,180000.00,0.00,0.00,4753500.00,'2025-11-10','UNPAID',NULL,'2025-12-14 15:04:38');
/*!40000 ALTER TABLE `bills` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `booking_requests`
--

DROP TABLE IF EXISTS `booking_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking_requests` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `admin_note` text COLLATE utf8mb4_unicode_ci,
  `created_at` datetime(6) DEFAULT NULL,
  `full_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `note` text COLLATE utf8mb4_unicode_ci,
  `phone` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `room_id` bigint NOT NULL,
  `status` enum('APPROVED','CONTACTED','NEW','REJECTED') COLLATE utf8mb4_unicode_ci NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking_requests`
--

LOCK TABLES `booking_requests` WRITE;
/*!40000 ALTER TABLE `booking_requests` DISABLE KEYS */;
INSERT INTO `booking_requests` VALUES (1,'','2025-12-10 00:40:05.693139','Nguyễn Văn Kiên','Chiều mai xem phòng nhé','0343841111',1,'NEW','2025-12-10 00:40:18.985097');
/*!40000 ALTER TABLE `booking_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contract_members`
--

DROP TABLE IF EXISTS `contract_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contract_members` (
  `contract_id` bigint NOT NULL,
  `tenant_id` bigint NOT NULL,
  `relation` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `move_in_date` date DEFAULT NULL,
  `move_out_date` date DEFAULT NULL,
  `is_primary` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`contract_id`,`tenant_id`),
  KEY `idx_cm_contract` (`contract_id`),
  KEY `idx_cm_tenant` (`tenant_id`),
  CONSTRAINT `contract_members_ibfk_1` FOREIGN KEY (`contract_id`) REFERENCES `contracts` (`id`),
  CONSTRAINT `contract_members_ibfk_2` FOREIGN KEY (`tenant_id`) REFERENCES `tenants` (`id`),
  CONSTRAINT `fk_cm_contract` FOREIGN KEY (`contract_id`) REFERENCES `contracts` (`id`),
  CONSTRAINT `fk_cm_tenant` FOREIGN KEY (`tenant_id`) REFERENCES `tenants` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contract_members`
--

LOCK TABLES `contract_members` WRITE;
/*!40000 ALTER TABLE `contract_members` DISABLE KEYS */;
INSERT INTO `contract_members` VALUES (1,1,'Đại diện','2025-11-05',NULL,1,'2025-12-04 21:08:39'),(2,2,'Đại diện','2025-11-16',NULL,1,'2025-12-04 21:24:35'),(3,4,'Đại diện','2025-05-04',NULL,1,'2025-12-04 21:25:02'),(4,3,'Đại diện','2025-10-10',NULL,1,'2025-12-14 15:00:58');
/*!40000 ALTER TABLE `contract_members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contracts`
--

DROP TABLE IF EXISTS `contracts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contracts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `contract_code` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `room_id` bigint NOT NULL,
  `tenant_id` bigint NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date DEFAULT NULL,
  `deposit` decimal(38,2) NOT NULL,
  `rent_price` decimal(38,2) NOT NULL,
  `status` enum('ACTIVE','ENDED','CANCELLED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  `note` text COLLATE utf8mb4_unicode_ci,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `contract_code` (`contract_code`),
  KEY `tenant_id` (`tenant_id`),
  KEY `idx_contract_room` (`room_id`),
  KEY `idx_contract_status` (`status`),
  CONSTRAINT `contracts_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`),
  CONSTRAINT `contracts_ibfk_2` FOREIGN KEY (`tenant_id`) REFERENCES `tenants` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contracts`
--

LOCK TABLES `contracts` WRITE;
/*!40000 ALTER TABLE `contracts` DISABLE KEYS */;
INSERT INTO `contracts` VALUES (1,'HD101',1,1,'2025-11-05',NULL,3000000.00,2800000.00,'ACTIVE','Quét QR để thanh toán tiền phòng','2025-12-03 15:54:27','2025-12-06 08:32:13'),(2,'HD102',3,2,'2025-11-16',NULL,2000000.00,2000000.00,'ACTIVE','Quét QR để thanh toán tiền phòng','2025-12-04 21:24:36','2025-12-06 08:32:11'),(3,'HD201',5,4,'2025-05-04',NULL,3000000.00,4000000.00,'ACTIVE','Quét QR để thanh toán tiền phòng','2025-12-04 21:25:02','2025-12-10 09:02:36'),(4,'HD103',4,3,'2025-10-10',NULL,3000000.00,4500000.00,'ACTIVE',NULL,'2025-12-14 15:00:58','2025-12-14 15:00:58');
/*!40000 ALTER TABLE `contracts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedbacks`
--

DROP TABLE IF EXISTS `feedbacks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feedbacks` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint DEFAULT NULL,
  `room_id` bigint NOT NULL,
  `title` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('NEW','IN_PROGRESS','DONE','REJECTED') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NEW',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `admin_note` text COLLATE utf8mb4_unicode_ci,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `tenant_id` (`tenant_id`),
  KEY `room_id` (`room_id`),
  CONSTRAINT `feedbacks_ibfk_1` FOREIGN KEY (`tenant_id`) REFERENCES `tenants` (`id`),
  CONSTRAINT `feedbacks_ibfk_2` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedbacks`
--

LOCK TABLES `feedbacks` WRITE;
/*!40000 ALTER TABLE `feedbacks` DISABLE KEYS */;
INSERT INTO `feedbacks` VALUES (1,NULL,1,'Nước yếu','Nước bẩn chảy yếu có khi mất','NEW','2025-12-10 00:28:46','Đã xử lý','2025-12-12 19:07:54.777287'),(2,NULL,4,'Hỏng điện xệ cửa vệ sinh','Từ ngày 6/12/2025 ảnh hưởng việc sinh hoạt khá nhiều','NEW','2025-12-12 18:57:30','Đã gọi thợ xử lý vào 8h sáng ngày 13/12/2025','2025-12-12 19:07:43.054687');
/*!40000 ALTER TABLE `feedbacks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hostels`
--

DROP TABLE IF EXISTS `hostels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hostels` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hostels`
--

LOCK TABLES `hostels` WRITE;
/*!40000 ALTER TABLE `hostels` DISABLE KEYS */;
INSERT INTO `hostels` VALUES (1,'Chung cư mini ParkCity','Phố Ngô Đình Mẫn La Khê Hà Đông','0343841426','2025-12-04 22:33:51','2025-12-04 22:33:51'),(2,'Nhà trọ Nhật Anh','Số 2 Phố Vũ Ngọc Phan Láng Hạ Đống Đa','0986578577','2025-12-04 22:34:29','2025-12-04 22:34:29');
/*!40000 ALTER TABLE `hostels` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `meter_readings`
--

DROP TABLE IF EXISTS `meter_readings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meter_readings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `room_id` bigint NOT NULL,
  `bill_month` int NOT NULL,
  `bill_year` int NOT NULL,
  `electric_old` int NOT NULL DEFAULT '0',
  `electric_new` int NOT NULL DEFAULT '0',
  `water_old` int NOT NULL DEFAULT '0',
  `water_new` int NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_meter_room_month_year` (`room_id`,`bill_month`,`bill_year`),
  CONSTRAINT `meter_readings_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `meter_readings`
--

LOCK TABLES `meter_readings` WRITE;
/*!40000 ALTER TABLE `meter_readings` DISABLE KEYS */;
INSERT INTO `meter_readings` VALUES (1,1,11,2025,1001470,1001890,15,30,'2025-12-03 15:58:45'),(2,5,12,2025,657,788,56,89,'2025-12-04 21:25:32'),(3,4,10,2025,123,178,24,50,'2025-12-14 15:03:08'),(4,4,11,2025,178,199,50,62,'2025-12-14 15:03:49');
/*!40000 ALTER TABLE `meter_readings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bill_id` bigint NOT NULL,
  `paid_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `amount` decimal(38,2) NOT NULL,
  `method` enum('CASH','BANK','MOMO','VNPAY') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'CASH',
  `transaction_code` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` enum('SUCCESS','FAILED','PENDING') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SUCCESS',
  `payer_email` varchar(120) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `bill_id` (`bill_id`),
  CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`bill_id`) REFERENCES `bills` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (1,1,'2025-12-12 22:34:32',4495000.00,'CASH','CASH-REQ-1765553671780','SUCCESS','thinhvannguyen113@gmail.com'),(2,1,'2025-12-12 22:45:42',4495000.00,'CASH',NULL,'SUCCESS','thinhvanngiuen113@gmail.com'),(3,1,'2025-12-12 22:46:40',4495000.00,'CASH',NULL,'SUCCESS','thinhvannguyen113@gmail.com'),(4,1,'2025-12-12 22:56:11',4495000.00,'CASH','CASH-REQ-1765553671780','SUCCESS','thinhvannguyen113@gmail.com'),(5,1,'2025-12-12 22:57:21',4495000.00,'CASH','CASH-REQ-1765553671780','SUCCESS','thinhvannguyen113@gmail.com'),(6,2,'2025-12-12 22:59:40',4953500.00,'CASH','CASH-REQ-1765555179568','SUCCESS','thinhvannguyen113@gmail.com'),(7,2,'2025-12-12 23:25:27',4953500.00,'CASH','CASH-REQ-1765555179568','SUCCESS','thinhvannguyen113@gmail.com'),(8,2,'2025-12-13 07:30:13',4953500.00,'CASH','CASH-REQ-1765555179568','SUCCESS','thinhvannguyen113@gmail.com');
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_amenities`
--

DROP TABLE IF EXISTS `room_amenities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_amenities` (
  `room_id` bigint NOT NULL,
  `amenity_id` bigint NOT NULL,
  PRIMARY KEY (`room_id`,`amenity_id`),
  KEY `amenity_id` (`amenity_id`),
  CONSTRAINT `room_amenities_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`),
  CONSTRAINT `room_amenities_ibfk_2` FOREIGN KEY (`amenity_id`) REFERENCES `amenities` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_amenities`
--

LOCK TABLES `room_amenities` WRITE;
/*!40000 ALTER TABLE `room_amenities` DISABLE KEYS */;
/*!40000 ALTER TABLE `room_amenities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rooms` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `room_code` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `room_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `floor` int DEFAULT '1',
  `area` decimal(38,2) DEFAULT NULL,
  `max_people` int DEFAULT '2',
  `rent_price` decimal(38,2) NOT NULL,
  `deposit_default` decimal(38,2) DEFAULT NULL,
  `status` enum('EMPTY','RENTING','REPAIR') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'EMPTY',
  `description` text COLLATE utf8mb4_unicode_ci,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `hostel_id` bigint DEFAULT NULL,
  `image_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `room_code` (`room_code`),
  KEY `idx_room_hostel` (`hostel_id`),
  CONSTRAINT `fk_room_hostel` FOREIGN KEY (`hostel_id`) REFERENCES `hostels` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
INSERT INTO `rooms` VALUES (1,'001','P101',1,45.20,2,2800000.00,3000000.00,'EMPTY','Đầy đủ thiết bị','2025-12-03 15:37:45','2025-12-10 08:53:51',1,'/uploads/rooms/7c810c4c561647239b4a5ffc4809364e_1765331631426.jpg'),(3,'002','P102',2,20.00,2,2000000.00,2000000.00,'EMPTY',NULL,'2025-12-04 12:34:15','2025-12-10 08:54:04',1,'/uploads/rooms/119f825c208c4e74bd60d67eabb508f1_1765331643947.jpg'),(4,'003','P103',1,60.00,2,4500000.00,3000000.00,'RENTING','Mọi thứ đều tốt đầy đủ','2025-12-04 21:23:05','2025-12-14 15:00:58',1,'/uploads/rooms/f82664c2c04941b4ac4bb4af72b93de3_1765331657782.jpg'),(5,'004','P104',1,50.00,2,4000000.00,3000000.00,'EMPTY','Very good 10đ','2025-12-04 21:23:49','2025-12-11 22:52:38',1,'/uploads/rooms/0dfdeab787b34628b6e98cb916d1163c_1765331692588.jpg'),(7,'005','P201',2,40.00,1,3000000.00,2500000.00,'EMPTY','Tốt','2025-12-04 22:35:19','2025-12-10 08:55:20',1,'/uploads/rooms/f8e2921deef94432af7777b79a3d3dba_1765331719802.jpg'),(8,'101','P101',1,40.00,1,3500000.00,2500000.00,'EMPTY','Tốt','2025-12-04 22:36:34','2025-12-10 09:01:34',2,'/uploads/rooms/f1d8035147c9440e9ae551e5be146db1_1765331748975.jpg'),(9,'201','P201',2,45.00,1,3000000.00,2500000.00,'EMPTY','Phòng decor theo sở thích các bạn nữ giản dị mộc mạc','2025-12-10 08:57:32','2025-12-10 08:57:32',2,'/uploads/rooms/758252a75aa74558b4a62679b3d02059_1765331851671.jpg'),(10,'301','P301',3,60.00,2,5000000.00,4000000.00,'EMPTY','Phòng trọ master decor theo phong cách mùa thu mát mẻ liên hệ ngay hotline để đặt phòng','2025-12-10 08:59:16','2025-12-10 08:59:16',2,'/uploads/rooms/3bda2ec80bc34598bae53f3dd4934d78_1765331955744.jpg'),(11,'202','P202',2,40.00,1,3500000.00,3000000.00,'EMPTY','Đầy đủ trang thiết bị tiện nghi sống','2025-12-20 07:36:04','2025-12-20 07:36:04',2,'/uploads/rooms/032de7b274154aa38017572950cf98f7_1766190963655.jpg'),(12,'203','P203',2,25.00,1,3000000.00,2000000.00,'EMPTY','Đầy đủ tiện nghi','2025-12-20 07:41:41','2025-12-20 07:41:41',2,'/uploads/rooms/faa0005a3b7a4f018dd37bfb35928f70_1766191301170.jpg'),(13,'204','P204',2,35.00,1,3700000.00,3000000.00,'EMPTY','Đầy đủ tiện nghi','2025-12-20 07:42:25','2025-12-20 07:42:25',2,'/uploads/rooms/0da208802bc84db4b7ba95a542dab75e_1766191344840.jpg'),(14,'205','P205',2,40.00,1,3600000.00,3000000.00,'EMPTY',NULL,'2025-12-20 07:43:34','2025-12-20 07:43:34',2,'/uploads/rooms/c5ae9ea9331c4f498d3239156e5d38fe_1766191413924.jpg'),(15,'206','P206',2,30.00,1,2900000.00,2500000.00,'EMPTY',NULL,'2025-12-20 07:44:35','2025-12-20 07:44:35',2,'/uploads/rooms/a83c6d965b6a4277bf56867b92cbb7dc_1766191474780.jpg'),(16,'302','P302',3,45.00,1,4000000.00,3000000.00,'EMPTY',NULL,'2025-12-20 07:45:42','2025-12-20 07:45:42',2,'/uploads/rooms/bf38fa0155f7477ea3749922e121c44c_1766191541632.jpg'),(17,'303','P303',3,40.00,2,4500000.00,4000000.00,'EMPTY',NULL,'2025-12-20 07:46:12','2025-12-20 07:46:12',2,'/uploads/rooms/e4f6f2aa0bc74c2a9636c48f7916b60c_1766191572285.jpg'),(18,'304','P304',3,30.00,1,3300000.00,2500000.00,'EMPTY',NULL,'2025-12-20 07:47:00','2025-12-20 07:47:00',2,'/uploads/rooms/812f95a66ba64df5b0c4c65682280d31_1766191619589.jpg'),(19,'305','P305',3,50.00,1,4500000.00,3500000.00,'EMPTY',NULL,'2025-12-20 07:47:29','2025-12-20 07:47:29',2,'/uploads/rooms/f42980996417401cbcdce3499193e692_1766191649045.jpg'),(20,'306','P306',3,36.00,1,4000000.00,3500000.00,'EMPTY',NULL,'2025-12-20 07:47:58','2025-12-20 07:47:58',2,'/uploads/rooms/856e5843a6c542138e5e9f79a984e3de_1766191677522.jpg');
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `services`
--

DROP TABLE IF EXISTS `services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `services` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `service_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `unit` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `unit_price` decimal(12,2) NOT NULL,
  `type` enum('FIXED','METER') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'FIXED',
  `is_active` tinyint NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `service_name` (`service_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `services`
--

LOCK TABLES `services` WRITE;
/*!40000 ALTER TABLE `services` DISABLE KEYS */;
INSERT INTO `services` VALUES (1,'Điện','kWh',3500.00,'METER',1),(2,'Nước','m3',15000.00,'METER',1);
/*!40000 ALTER TABLE `services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tenants`
--

DROP TABLE IF EXISTS `tenants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tenants` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `full_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cccd` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `cccd` (`cccd`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `tenants_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tenants`
--

LOCK TABLES `tenants` WRITE;
/*!40000 ALTER TABLE `tenants` DISABLE KEYS */;
INSERT INTO `tenants` VALUES (1,'Nguyễn Văn Thịnh','0343841426','001205001222',NULL,'86 Street Pho Tua',NULL,'ACTIVE','2025-12-03 15:46:36'),(2,'Nguyễn Văn Kiên','0912151234','001201001223','2005-06-06','25 Street Vu Tong Phan',NULL,'ACTIVE','2025-12-04 21:19:34'),(3,'Nguyễn Xuân Hinh','0986566546','001201001224','2006-06-16','25 Street Ngô Đình Mẫn',NULL,'ACTIVE','2025-12-04 21:20:31'),(4,'Long Chín Ngón','0986566666','001201001225','2004-03-03','10 Street Vu Tong Phan',NULL,'ACTIVE','2025-12-04 21:21:33');
/*!40000 ALTER TABLE `tenants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `full_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '1',
  `status` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','$2a$10$VSxR5k76ELYoKqwxLgH27OIPFptm5iYwh6xAmwIAzOGThIPYb2diu','Administrator',NULL,'admin@nvt.local','ADMIN',1,'ACTIVE','2025-12-03 16:58:03','2025-12-03 16:58:03');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-24 15:13:12
