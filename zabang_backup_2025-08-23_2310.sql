-- MySQL dump 10.13  Distrib 8.4.0, for macos13.2 (arm64)
--
-- Host: localhost    Database: zabang
-- ------------------------------------------------------
-- Server version	9.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `zabang`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `zabang` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `zabang`;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `post_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `content` text NOT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_cmt_user` (`user_id`),
  KEY `idx_comments_post_alive` (`post_id`,`is_deleted`,`id`),
  CONSTRAINT `fk_cmt_post` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_cmt_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
INSERT INTO `comments` VALUES (1,1,2,'참여하고 싶습니다! 어떻게 하면 되나요?',0,'2025-08-19 05:28:27','2025-08-19 05:28:27'),(2,2,1,'시원한 쌀국수 추천이요~',0,'2025-08-19 05:28:27','2025-08-19 05:28:27'),(3,9,1,'참여하고 싶어요!',0,'2025-08-20 16:24:35','2025-08-20 16:24:35'),(4,9,1,'참여하고 싶어요!',0,'2025-08-20 16:26:49','2025-08-20 16:26:49'),(5,9,1,'참여하고 싶어요!',0,'2025-08-20 16:30:33','2025-08-20 16:30:33'),(6,9,1,'참여하고 싶어요!',0,'2025-08-20 16:31:30','2025-08-20 16:31:30'),(7,9,1,'참여하고 싶어요!',0,'2025-08-20 16:40:36','2025-08-20 16:40:36'),(8,9,1,'시간 가능하면 바로 참여하고 싶어요~',1,'2025-08-20 16:46:30','2025-08-20 16:47:00'),(9,8,1,'수정된 댓글 내용',1,'2025-08-21 18:03:51','2025-08-21 18:08:23');
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_image`
--

DROP TABLE IF EXISTS `post_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_image` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `post_id` bigint NOT NULL,
  `url` varchar(500) NOT NULL,
  `ord` int NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_img_post_ord_id` (`post_id`,`ord`,`id`),
  CONSTRAINT `fk_img_post` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_image`
--

LOCK TABLES `post_image` WRITE;
/*!40000 ALTER TABLE `post_image` DISABLE KEYS */;
INSERT INTO `post_image` VALUES (1,1,'https://example.com/ramen.jpg',0,'2025-08-19 05:26:56'),(2,2,'https://example.com/cat.jpg',0,'2025-08-19 05:26:56'),(3,6,'https://example.com/a.jpg',0,'2025-08-20 10:18:13'),(4,6,'https://example.com/b.jpg',1,'2025-08-20 10:18:13'),(5,7,'https://example.com/a.jpg',0,'2025-08-20 11:00:45'),(6,7,'https://example.com/b.jpg',1,'2025-08-20 11:00:45'),(7,9,'https://example.com/a.jpg',0,'2025-08-20 14:59:24'),(8,9,'https://example.com/b.jpg',1,'2025-08-20 14:59:24'),(9,11,'https://example.com/a.jpg',0,'2025-08-20 15:06:57'),(10,11,'https://example.com/b.jpg',1,'2025-08-20 15:06:57'),(11,13,'https://example.com/ramen.jpg',0,'2025-08-21 17:52:36'),(12,14,'https://example.com/ramen.jpg',0,'2025-08-21 18:10:12');
/*!40000 ALTER TABLE `post_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `posts`
--

DROP TABLE IF EXISTS `posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `posts` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `category` enum('FREE','GROUP_BUY') NOT NULL,
  `title` varchar(100) NOT NULL,
  `content` longtext NOT NULL,
  `area_tag` varchar(10) NOT NULL,
  `status` enum('OPEN','CLOSED') NOT NULL DEFAULT 'OPEN',
  `view_count` int NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_posts_user` (`user_id`),
  KEY `idx_posts_filter` (`deleted_at`,`category`,`area_tag`,`id`),
  CONSTRAINT `fk_posts_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `posts`
--

LOCK TABLES `posts` WRITE;
/*!40000 ALTER TABLE `posts` DISABLE KEYS */;
INSERT INTO `posts` VALUES (1,1,'GROUP_BUY','수정된 제목','라면 박스 공구 인원 모아요~','3구역','CLOSED',3,'2025-08-19 05:23:15','2025-08-21 17:55:47',NULL),(2,2,'FREE','맛집 추천','점심 메뉴 추천 부탁드립니다.','2구역','OPEN',0,'2025-08-19 05:23:15','2025-08-19 05:23:15',NULL),(3,1,'FREE','제목','본문','3구역','OPEN',0,'2025-08-20 01:27:52','2025-08-20 01:27:52',NULL),(4,1,'FREE','A-최신순테스트','본문 A','3구역','OPEN',2,'2025-08-20 03:24:58','2025-08-20 03:27:26','2025-08-20 03:27:26'),(5,1,'GROUP_BUY','B-최신이여야함','본문 B','2구역','OPEN',0,'2025-08-20 03:25:13','2025-08-20 03:25:13',NULL),(6,1,'GROUP_BUY','수정된 제목','본문','3구역','CLOSED',5,'2025-08-20 10:18:13','2025-08-20 11:03:16','2025-08-20 11:03:16'),(7,1,'GROUP_BUY','수정된 제목','본문 입니다','3구역','CLOSED',3,'2025-08-20 11:00:46','2025-08-20 11:04:20','2025-08-20 11:04:20'),(8,1,'FREE','이미지 없음','본문','2구역','OPEN',0,'2025-08-20 11:01:12','2025-08-20 11:01:12',NULL),(9,1,'GROUP_BUY','수정된 제목','본문 입니다','3구역','CLOSED',4,'2025-08-20 14:59:24','2025-08-20 15:00:53','2025-08-20 15:00:53'),(10,1,'FREE','이미지 없음','본문','2구역','OPEN',0,'2025-08-20 14:59:33','2025-08-20 14:59:33',NULL),(11,1,'GROUP_BUY','베밥 텐 테스트','본문 A','3구역','OPEN',0,'2025-08-20 15:06:58','2025-08-20 15:06:58',NULL),(12,1,'FREE','이미지 없음','본문 B','2구역','OPEN',0,'2025-08-20 15:07:32','2025-08-20 15:07:32',NULL),(13,1,'GROUP_BUY','3구역 라면 공구','같이 사요~','3구역','OPEN',0,'2025-08-21 17:52:36','2025-08-21 17:52:36',NULL),(14,1,'GROUP_BUY','제목 수정','같이 사요~','6구역','CLOSED',1,'2025-08-21 18:10:12','2025-08-21 18:12:09','2025-08-21 18:12:09');
/*!40000 ALTER TABLE `posts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nickname` varchar(50) NOT NULL,
  `role` varchar(20) NOT NULL DEFAULT 'USER',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nickname` (`nickname`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'채연','USER','2025-08-19 05:18:57'),(2,'정연','USER','2025-08-19 05:18:57');
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

-- Dump completed on 2025-08-23 23:11:16
