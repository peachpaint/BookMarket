CREATE DATABASE  IF NOT EXISTS `bookdb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `bookdb`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: bookdb
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
-- Table structure for table `book`
--

DROP TABLE IF EXISTS `book`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `book` (
  `book_id` varchar(64) COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `unit_price` decimal(19,2) DEFAULT NULL,
  `author` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description` text COLLATE utf8mb4_general_ci,
  `publisher` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `category` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `units_in_stock` bigint NOT NULL,
  `release_date` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `condition` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `file_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `book`
--

LOCK TABLES `book` WRITE;
/*!40000 ALTER TABLE `book` DISABLE KEYS */;
INSERT INTO `book` VALUES ('ISBN1234','자바스크립트 입문',30000.00,'조현영','자바스크립트의 기초부터 심화까지 핵심 문법을 학습한 후 12가지 프로그램을 만들며 학습한 내용을 확인할 수 있습니다. 문법 학습과 실습이 적절히 섞여 있어 프로그램을 만드는 방법을 재미있게 익힐 수 있습니다.','길벗','IT',1000,'2024/02/20','Old','ISBN1234.jpg'),('ISBN1235','파이썬의 정석',29800.00,'조용주,임좌상','4차 산업혁명의 핵심인 머신러닝, 사물 인터넷(IoT), 데이터 분석 등 다양한 분야에 활용되는 직관적이고 간결한 문법의 파이썬 프로그래밍 언어를 최신 트렌드에 맞게 예제 중심으로 학습할 수 있습니다.','길벗','IT',1000,'2023/01/10','Old','ISBN1235.jpg'),('ISBN1236','안드로이드 프로그래밍',25000.00,'송미영','안드로이드의 기본 개념을 체계적으로 익히고, 이를 실습 예제를 통해 익힙니다. 기본 개념과 사용법을 스스로 실전에 적용하는 방법을 학습한 다음 실습 예제와 응용 예제를 통해 실전 프로젝트 응용력을 키웁니다.','길벗','IT',1000,'2023/06/30','Old','ISBN1236.jpg');
/*!40000 ALTER TABLE `book` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `member_id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '회원 아이디',
  `passwd` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '암호화된 비밀번호',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '회원 이름',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '전화번호',
  `city` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '거주지',
  `sex` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '성별',
  `role` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'USER' COMMENT '권한 (USER, ADMIN)',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '가입일시',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
  PRIMARY KEY (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='회원 정보';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES ('Admin','$2a$10$X5wFWtliAnKZzdSk9UphZuQQJQp9s8.KqpqKCW3aWp7nR3OWqKz4m','관리자','010-0000-0000','서울시','남성','ADMIN','2025-11-24 00:09:53','2025-11-24 00:09:53');
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-24  9:24:13
