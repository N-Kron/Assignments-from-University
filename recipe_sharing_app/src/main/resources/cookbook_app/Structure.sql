-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: 93.182.151.4    Database: cookbook
-- ------------------------------------------------------
-- Server version	11.1.0-MariaDB

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
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `comment_user` varchar(32) NOT NULL,
  `comment_recipe` int(11) NOT NULL,
  `comment_id` int(11) NOT NULL AUTO_INCREMENT,
  `comment_text` varchar(512) NOT NULL,
  `comment_shared` tinyint(1) NOT NULL,
  PRIMARY KEY (`comment_id`),
  KEY `comment_user` (`comment_user`),
  KEY `comment_recipe` (`comment_recipe`),
  CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`comment_user`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`comment_recipe`) REFERENCES `recipe` (`recipe_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `content`
--

DROP TABLE IF EXISTS `content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `content` (
  `content_recipe` int(11) NOT NULL,
  `content_ingr` varchar(64) NOT NULL,
  `content_value` double DEFAULT NULL,
  PRIMARY KEY (`content_recipe`,`content_ingr`),
  KEY `content_ingr` (`content_ingr`),
  CONSTRAINT `content_ibfk_1` FOREIGN KEY (`content_ingr`) REFERENCES `ingredient` (`ingr_name`) ON DELETE CASCADE,
  CONSTRAINT `content_ibfk_2` FOREIGN KEY (`content_recipe`) REFERENCES `recipe` (`recipe_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dinner`
--

DROP TABLE IF EXISTS `dinner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dinner` (
  `dinner_id` int(11) NOT NULL AUTO_INCREMENT,
  `dinner_week` varchar(32) NOT NULL,
  `dinner_day` int(11) NOT NULL,
  `dinner_user` varchar(32) NOT NULL,
  `dinner_recipe` int(11) NOT NULL,
  `dinner_portion` int(11) NOT NULL,
  PRIMARY KEY (`dinner_id`),
  KEY `dinner_recipe` (`dinner_recipe`),
  KEY `dinner_to_weeks_fk` (`dinner_week`,`dinner_user`),
  CONSTRAINT `dinner_ibfk_1` FOREIGN KEY (`dinner_recipe`) REFERENCES `recipe` (`recipe_id`) ON DELETE CASCADE,
  CONSTRAINT `dinner_to_weeks_fk` FOREIGN KEY (`dinner_week`, `dinner_user`) REFERENCES `weeks` (`weeks_name`, `weeks_user`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=143 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `favorite`
--

DROP TABLE IF EXISTS `favorite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `favorite` (
  `target_user` varchar(32) NOT NULL,
  `favorite_recipe` int(11) NOT NULL,
  PRIMARY KEY (`target_user`,`favorite_recipe`),
  KEY `favorite_recipe` (`favorite_recipe`),
  CONSTRAINT `favorite_ibfk_1` FOREIGN KEY (`target_user`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `favorite_ibfk_2` FOREIGN KEY (`favorite_recipe`) REFERENCES `recipe` (`recipe_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `help_article`
--

DROP TABLE IF EXISTS `help_article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `help_article` (
  `article_name` varchar(128) NOT NULL,
  `article_text` text NOT NULL,
  PRIMARY KEY (`article_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ingredient`
--

DROP TABLE IF EXISTS `ingredient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ingredient` (
  `ingr_name` varchar(64) NOT NULL,
  `ingr_unit` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`ingr_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `recipe`
--

DROP TABLE IF EXISTS `recipe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recipe` (
  `recipe_id` int(11) NOT NULL AUTO_INCREMENT,
  `recipe_name` varchar(64) NOT NULL,
  `short_description` varchar(512) DEFAULT NULL,
  `description` varchar(2048) NOT NULL,
  `base_portion` int(11) NOT NULL,
  PRIMARY KEY (`recipe_id`)
) ENGINE=InnoDB AUTO_INCREMENT=305 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `shop_list`
--

DROP TABLE IF EXISTS `shop_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shop_list` (
  `shop_week` varchar(32) NOT NULL,
  `shop_ingr` varchar(64) NOT NULL,
  `shop_value` double DEFAULT NULL,
  `shop_user` varchar(32) NOT NULL,
  PRIMARY KEY (`shop_week`,`shop_ingr`),
  KEY `shop_ingr` (`shop_ingr`),
  KEY `shop_list_to_weeks_fk` (`shop_week`,`shop_user`),
  CONSTRAINT `shop_list_ibfk_1` FOREIGN KEY (`shop_ingr`) REFERENCES `ingredient` (`ingr_name`),
  CONSTRAINT `shop_list_to_weeks_fk` FOREIGN KEY (`shop_week`, `shop_user`) REFERENCES `weeks` (`weeks_name`, `weeks_user`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `suggestion`
--

DROP TABLE IF EXISTS `suggestion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suggestion` (
  `suggestion_id` int(11) NOT NULL AUTO_INCREMENT,
  `suggestion_sender` varchar(32) NOT NULL,
  `suggestion_recipient` varchar(32) NOT NULL,
  `suggestion_recipe` int(11) NOT NULL,
  `suggestion_message` varchar(512) NOT NULL,
  `suggestion_seen` tinyint(1) NOT NULL,
  PRIMARY KEY (`suggestion_id`),
  KEY `suggestion_sender` (`suggestion_sender`),
  KEY `suggestion_recipient` (`suggestion_recipient`),
  KEY `suggestion_recipe` (`suggestion_recipe`),
  CONSTRAINT `suggestion_ibfk_1` FOREIGN KEY (`suggestion_sender`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `suggestion_ibfk_2` FOREIGN KEY (`suggestion_recipient`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `suggestion_ibfk_3` FOREIGN KEY (`suggestion_recipe`) REFERENCES `recipe` (`recipe_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag` (
  `tag_id` int(11) NOT NULL AUTO_INCREMENT,
  `tag_keyword` varchar(32) NOT NULL,
  `tag_user` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`tag_id`),
  KEY `tag_user` (`tag_user`),
  CONSTRAINT `tag_ibfk_1` FOREIGN KEY (`tag_user`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=111 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tagging`
--

DROP TABLE IF EXISTS `tagging`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tagging` (
  `tagged_recipe` int(11) NOT NULL,
  `applied_tag` int(11) NOT NULL,
  PRIMARY KEY (`tagged_recipe`,`applied_tag`),
  KEY `applied_tag` (`applied_tag`),
  CONSTRAINT `tagging_ibfk_1` FOREIGN KEY (`applied_tag`) REFERENCES `tag` (`tag_id`) ON DELETE CASCADE,
  CONSTRAINT `tagging_ibfk_2` FOREIGN KEY (`tagged_recipe`) REFERENCES `recipe` (`recipe_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` varchar(32) NOT NULL,
  `user_name` varchar(64) NOT NULL,
  `user_pwd` char(40) NOT NULL,
  `user_privileged` tinyint(1) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `weeks`
--

DROP TABLE IF EXISTS `weeks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `weeks` (
  `weeks_user` varchar(32) NOT NULL,
  `weeks_name` varchar(32) NOT NULL,
  PRIMARY KEY (`weeks_name`,`weeks_user`),
  KEY `weeks_user` (`weeks_user`),
  CONSTRAINT `weeks_ibfk_1` FOREIGN KEY (`weeks_user`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-05-29 14:03:29
