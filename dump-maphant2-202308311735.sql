-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: dawoony.com    Database: maphant2
-- ------------------------------------------------------
-- Server version	8.0.32

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
-- Table structure for table `banner`
--

DROP TABLE IF EXISTS `banner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `banner` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) DEFAULT NULL,
  `company` varchar(100) NOT NULL,
  `images_url` text,
  `url` text,
  `pay` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `block`
--

DROP TABLE IF EXISTS `block`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `block` (
  `blocker_id` int NOT NULL,
  `blocked_id` int NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`blocker_id`,`blocked_id`),
  KEY `blocked_id` (`blocked_id`),
  CONSTRAINT `block_ibfk_1` FOREIGN KEY (`blocker_id`) REFERENCES `user` (`id`),
  CONSTRAINT `block_ibfk_2` FOREIGN KEY (`blocked_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `board`
--

DROP TABLE IF EXISTS `board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board` (
  `id` int NOT NULL AUTO_INCREMENT,
  `parent_id` int DEFAULT NULL COMMENT 'Q&A 답변글에서 질문글 가리킴',
  `category_id` int NOT NULL COMMENT '속한 계열',
  `user_id` int NOT NULL COMMENT '작성자',
  `type_id` int NOT NULL COMMENT '게시판 종류',
  `title` varchar(127) NOT NULL,
  `body` text NOT NULL,
  `state` int NOT NULL COMMENT '0 = 유지, 1 = 삭제, 차단(신고 누적) = 2, 유저 제재 시 블락 = 3',
  `is_hide` tinyint NOT NULL DEFAULT '0' COMMENT '다른 유저에게 숨김(1), 보임(0)',
  `is_complete` tinyint NOT NULL DEFAULT '0' COMMENT 'Q&A 답글에서 채택 여부(1: 채택, 0: 아직 안함)',
  `is_anonymous` tinyint NOT NULL DEFAULT '0' COMMENT '익명 여부 (1: 익명, 0: 공개)',
  `created_at` datetime NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  `comment_cnt` int NOT NULL,
  `like_cnt` int NOT NULL,
  `report_cnt` int NOT NULL,
  `images_url` text,
  PRIMARY KEY (`id`),
  KEY `parent_id` (`parent_id`),
  KEY `category_id` (`category_id`),
  KEY `user_id` (`user_id`),
  KEY `type_id` (`type_id`),
  CONSTRAINT `board_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `board` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `board_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `board_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `board_ibfk_4` FOREIGN KEY (`type_id`) REFERENCES `board_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=793 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `trg_at_update_type_qna` BEFORE INSERT ON `board` FOR EACH ROW BEGIN
	
	IF NEW.parent_id IS NOT NULL THEN
	
		SET NEW.type_id = 7;
	
	END IF;
	
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `trg_at_insert_board` AFTER INSERT ON `board` FOR EACH ROW BEGIN
	DECLARE rowcount INT;

	SELECT COUNT(*) INTO rowcount FROM user_count WHERE user_id =NEW.user_id ;

	IF (NEW.state = 0) THEN
		IF (rowcount > 0) THEN 
			CASE 
				
				WHEN (NEW.is_hide = 1) THEN 
					
					UPDATE user_count 
					SET board_cnt = board_cnt + 1, board_anonymous_hide_cnt = board_anonymous_hide_cnt + 1 WHERE user_id = NEW.user_id;
				
				WHEN (NEW.is_anonymous = 1) THEN
					
					UPDATE user_count 
					SET board_cnt = board_cnt + 1, board_anonymous_hide_cnt = board_anonymous_hide_cnt + 1 WHERE user_id = NEW.user_id;
					
					UPDATE board_cnt_by_category
					SET cnt = cnt + 1 WHERE category_id = NEW.category_id AND board_type_id = NEW.type_id;
				
					UPDATE board_type 
					SET post_cnt = post_cnt + 1 WHERE id = NEW.type_id;
				
				ELSE
					
					UPDATE user_count 
					SET board_cnt = board_cnt + 1 WHERE user_id = NEW.user_id;
					
					UPDATE board_cnt_by_category 
					SET cnt = cnt + 1 WHERE category_id = NEW.category_id AND board_type_id = NEW.type_id;
				
					UPDATE board_type 
					SET post_cnt = post_cnt + 1 WHERE id = NEW.type_id;
				
				END CASE;
				
		ELSE
		
			CASE
				
				WHEN (NEW.is_hide = 1) THEN
				
					INSERT INTO user_count (user_id, board_cnt, board_anonymous_hide_cnt) 
						VALUES (NEW.user_id, 1, 1);
				
				WHEN (NEW.is_anonymous = 1) THEN
				
					INSERT INTO user_count (user_id, board_cnt, board_anonymous_hide_cnt) 
						VALUES (NEW.user_id, 1, 1);
				
					UPDATE board_cnt_by_category
					SET cnt = cnt + 1 WHERE category_id = NEW.category_id AND board_type_id = NEW.type_id;
				
					UPDATE board_type 
					SET post_cnt = post_cnt + 1 WHERE id = NEW.type_id;
				
				ELSE
					
					INSERT INTO user_count (user_id, board_cnt) 
						VALUES (NEW.user_id, 1);
					
					UPDATE board_cnt_by_category 
					SET cnt = cnt + 1 WHERE category_id = NEW.category_id AND board_type_id = NEW.type_id;
				
					UPDATE board_type 
					SET post_cnt = post_cnt + 1 WHERE id = NEW.type_id;
			
				END CASE;
			
			END IF;
	
		END IF;
		
	END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `trg_at_update_board` AFTER UPDATE ON `board` FOR EACH ROW BEGIN
	
	IF(NEW.state != 0) THEN
	
		CASE
			
			WHEN (OLD.state = 0 AND OLD.is_hide = 1 AND NEW.is_hide = 1) THEN
				
				UPDATE user_count 
				SET board_anonymous_hide_cnt = board_anonymous_hide_cnt WHERE user_id = NEW.user_id;
			
			WHEN (OLD.state = 0 AND OLD.is_anonymous = 1 AND NEW.is_anonymous = 1) THEN
					
				UPDATE user_count 
				SET board_cnt = board_cnt - 1 WHERE user_id = NEW.user_id;
			
				UPDATE board_cnt_by_category
				SET cnt = cnt - 1 WHERE category_id = NEW.category_id AND board_type_id = NEW.type_id;
			
				UPDATE board_type 
				SET post_cnt = post_cnt - 1 WHERE id = NEW.type_id;
			
			WHEN (OLD.state = 0) THEN
					
				UPDATE user_count 
				SET board_cnt = board_cnt - 1 WHERE user_id = NEW.user_id;
			
				UPDATE board_cnt_by_category
				SET cnt = cnt - 1 WHERE category_id = NEW.category_id AND board_type_id = NEW.type_id;
			
				UPDATE board_type 
				SET post_cnt = post_cnt - 1 WHERE id = NEW.type_id;
			
			WHEN (OLD.state != 0 AND OLD.is_hide = 0 AND NEW.is_hide = 1) THEN
				
				IF (OLD.is_anonymous = 0) THEN
					
					UPDATE user_count 
					SET board_anonymous_hide_cnt = board_anonymous_hide_cnt + 1 WHERE user_id = NEW.user_id;
				
				END IF;
			
			WHEN (OLD.state != 0 AND OLD.is_hide = 1 AND NEW.is_hide = 0) THEN
			
				IF (OLD.is_anonymous = 0) THEN
				
					UPDATE user_count 
					SET board_anonymous_hide_cnt = board_anonymous_hide_cnt - 1 WHERE user_id = NEW.user_id;
				
				END IF;
			
			WHEN (OLD.state != 0 AND OLD.is_anonymous = 0 AND NEW.is_anonymous = 1) THEN
			
				IF (OLD.is_hide = 0) THEN
				
					UPDATE user_count 
					SET board_anonymous_hide_cnt = board_anonymous_hide_cnt + 1 WHERE user_id = NEW.user_id;
				
				END IF;
			
			WHEN (OLD.state != 0 AND OLD.is_anonymous = 1 AND NEW.is_anonymous = 0) THEN
				
				IF (OLD.is_hide = 0) THEN
					
					UPDATE user_count 
					SET board_anonymous_hide_cnt = board_anonymous_hide_cnt - 1 WHERE user_id = NEW.user_id;
				
				END IF;
			
			ELSE
			
				UPDATE user_count 
				SET board_cnt = board_cnt WHERE user_id = NEW.user_id;
				
		END CASE;
	
	ELSEIF (NEW.state = 0) THEN
	
		CASE
			
			WHEN (OLD.state != 0 AND OLD.is_hide = 1 AND NEW.is_hide = 1) THEN 
			
					UPDATE user_count 
					SET board_cnt = board_cnt WHERE user_id = NEW.user_id;
			
			WHEN (OLD.is_hide = 0 AND NEW.is_hide = 1) THEN 
					
				IF (NEW.is_anonymous = 1) THEN
				
					UPDATE user_count 
					SET board_cnt = board_cnt - 1 WHERE user_id = NEW.user_id;
			
					UPDATE board_cnt_by_category
					SET cnt = cnt - 1 WHERE category_id = NEW.category_id AND board_type_id = NEW.type_id;
			
					UPDATE board_type 
					SET post_cnt = post_cnt - 1 WHERE id = NEW.type_id;
				
				ELSE 
				
					UPDATE user_count 
					SET board_cnt = board_cnt - 1, board_anonymous_hide_cnt = board_anonymous_hide_cnt + 1 WHERE user_id = NEW.user_id;
			
					UPDATE board_cnt_by_category
					SET cnt = cnt - 1 WHERE category_id = NEW.category_id AND board_type_id = NEW.type_id;
			
					UPDATE board_type 
					SET post_cnt = post_cnt - 1 WHERE id = NEW.type_id;
				
				END IF;
			
			WHEN (OLD.is_hide = 1 AND NEW.is_hide = 0) THEN 
					
				IF (NEW.is_anonymous = 0) THEN
				
					UPDATE user_count 
					SET board_cnt = board_cnt + 1, board_anonymous_hide_cnt = board_anonymous_hide_cnt - 1 WHERE user_id = NEW.user_id;
				
					UPDATE board_cnt_by_category
					SET cnt = cnt + 1 WHERE category_id = NEW.category_id AND board_type_id = NEW.type_id;
				
					UPDATE board_type 
					SET post_cnt = post_cnt + 1 WHERE id = NEW.type_id;
				
				ELSE
				
					UPDATE user_count 
					SET board_cnt = board_cnt + 1 WHERE user_id = NEW.user_id;
				
					UPDATE board_cnt_by_category
					SET cnt = cnt + 1 WHERE category_id = NEW.category_id AND board_type_id = NEW.type_id;
				
					UPDATE board_type 
					SET post_cnt = post_cnt + 1 WHERE id = NEW.type_id;
				
				END IF;
			
			WHEN (OLD.is_anonymous = 0 AND NEW.is_anonymous = 1) THEN
					
				IF (NEW.is_hide = 1) THEN
				
					UPDATE user_count 
					SET board_cnt = board_cnt WHERE user_id = NEW.user_id;
				
				ELSE
				
					UPDATE user_count 
					SET board_anonymous_hide_cnt = board_anonymous_hide_cnt + 1 WHERE user_id = NEW.user_id;
				
				END IF;
				
			WHEN (OLD.is_anonymous = 1 AND NEW.is_anonymous = 0) THEN
					
				IF (NEW.is_hide = 0) THEN
				
					UPDATE user_count 
					SET board_anonymous_hide_cnt = board_anonymous_hide_cnt - 1 WHERE user_id = NEW.user_id;
				
				ELSE
				
					UPDATE user_count 
					SET board_cnt = board_cnt WHERE user_id = NEW.user_id;
				
				END IF;
			
			WHEN (OLD.state != 0) THEN 
			
				UPDATE user_count 
				SET board_cnt = board_cnt + 1 WHERE user_id = NEW.user_id;
					
				UPDATE board_cnt_by_category 
				SET cnt = cnt + 1 WHERE category_id = NEW.category_id AND board_type_id = NEW.type_id;
				
				UPDATE board_type 
				SET post_cnt = post_cnt + 1 WHERE id = NEW.type_id;
			
			ELSE
			
				UPDATE user_count 
				SET board_cnt = board_cnt WHERE user_id = NEW.user_id;
				
		END CASE;

	END IF;
	
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `board_cnt_by_category`
--

DROP TABLE IF EXISTS `board_cnt_by_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_cnt_by_category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category_id` int NOT NULL,
  `board_type_id` int NOT NULL,
  `cnt` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `board_cnt_by_category_UN` (`category_id`,`board_type_id`),
  KEY `board_cnt_by_category_FK_1` (`board_type_id`),
  CONSTRAINT `board_cnt_by_category_FK` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `board_cnt_by_category_FK_1` FOREIGN KEY (`board_type_id`) REFERENCES `board_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=855 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `board_like`
--

DROP TABLE IF EXISTS `board_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_like` (
  `id` int NOT NULL AUTO_INCREMENT,
  `board_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `board_like_UN` (`board_id`,`user_id`),
  KEY `user_id` (`user_id`),
  KEY `board_like_board_id_IDX` (`board_id`) USING BTREE,
  CONSTRAINT `board_like_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `board_like_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=133 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `plus_board_like_cnt` AFTER INSERT ON `board_like` FOR EACH ROW BEGIN
	UPDATE board
	SET like_cnt = like_cnt + 1 WHERE id = NEW.board_id;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `minus_board_like_cnt` AFTER DELETE ON `board_like` FOR EACH ROW BEGIN
	UPDATE board
	SET like_cnt = like_cnt - 1 WHERE id = OLD.board_id AND like_cnt > 0;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `board_notice`
--

DROP TABLE IF EXISTS `board_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_notice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `board_type` int NOT NULL,
  `title` varchar(100) NOT NULL,
  `body` text NOT NULL,
  `images_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `created_at` datetime NOT NULL,
  `modified_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `board_notice_FK` (`board_type`),
  CONSTRAINT `board_notice_FK` FOREIGN KEY (`board_type`) REFERENCES `board_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `board_qna`
--

DROP TABLE IF EXISTS `board_qna`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_qna` (
  `id` int NOT NULL AUTO_INCREMENT,
  `question_id` int NOT NULL COMMENT 'board의 질문 글 id',
  `answer_id` int NOT NULL COMMENT 'board의 채택 글 id',
  `complete_at` datetime NOT NULL COMMENT '채택 시간',
  PRIMARY KEY (`id`),
  UNIQUE KEY `board_qna_UN` (`question_id`),
  KEY `board_qna_FK_1` (`answer_id`),
  CONSTRAINT `board_qna_FK` FOREIGN KEY (`question_id`) REFERENCES `board` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `board_qna_FK_1` FOREIGN KEY (`answer_id`) REFERENCES `board` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `board_report`
--

DROP TABLE IF EXISTS `board_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_report` (
  `id` int NOT NULL AUTO_INCREMENT,
  `board_id` int NOT NULL,
  `user_id` int NOT NULL COMMENT '신고자',
  `report_id` int NOT NULL,
  `reported_at` datetime NOT NULL,
  `state` int NOT NULL DEFAULT '0' COMMENT '0: 처리전, 1: 처리 완료',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `board_report_FK` (`report_id`),
  KEY `board_report_FK_1` (`board_id`),
  CONSTRAINT `board_report_FK` FOREIGN KEY (`report_id`) REFERENCES `report` (`id`),
  CONSTRAINT `board_report_FK_1` FOREIGN KEY (`board_id`) REFERENCES `board` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `board_report_FK_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `trg_at_insert_board_report` AFTER INSERT ON `board_report` FOR EACH ROW BEGIN 
    DECLARE cnt INT;

    UPDATE board
    SET report_cnt = report_cnt + 1
    WHERE id = NEW.board_id;
   
       SELECT report_cnt INTO cnt FROM board WHERE id = NEW.board_id;

    IF cnt >= 10 THEN
        UPDATE board
        SET state = 2
        WHERE id = NEW.board_id;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `board_tag`
--

DROP TABLE IF EXISTS `board_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_tag` (
  `id` int NOT NULL AUTO_INCREMENT,
  `board_id` int NOT NULL,
  `tag_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `board_tag_FK` (`board_id`),
  KEY `board_tag_FK_1` (`tag_id`),
  CONSTRAINT `board_tag_FK` FOREIGN KEY (`board_id`) REFERENCES `board` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `board_tag_FK_1` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=301 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `board_type`
--

DROP TABLE IF EXISTS `board_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(31) NOT NULL,
  `post_cnt` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` int NOT NULL,
  `name` varchar(63) NOT NULL,
  `user_cnt` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `category_UN` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `parent_id` int DEFAULT NULL,
  `board_id` int NOT NULL,
  `body` text NOT NULL,
  `is_anonymous` tinyint NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `like_cnt` int NOT NULL DEFAULT '0',
  `report_cnt` int NOT NULL DEFAULT '0',
  `state` tinyint NOT NULL DEFAULT '0' COMMENT '0 = 유지, 1 = 삭제, 차단(신고 누적) = 2, 유저 제재 시 블락 = 3',
  `modified_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `parent_id` (`parent_id`),
  KEY `board_id` (`board_id`),
  KEY `comment_FK` (`user_id`),
  CONSTRAINT `comment_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `comment` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`board_id`) REFERENCES `board` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=740 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `trg_at_insert_comment` AFTER INSERT ON `comment` FOR EACH ROW BEGIN
	DECLARE rowcount INT;
	
	SELECT count(*) into rowcount FROM user_count WHERE user_id = NEW.user_id;
	
	IF (NEW.state = 0) THEN
		IF (rowcount > 0) THEN
			IF (NEW.is_anonymous = 1) THEN
				UPDATE user_count
				SET comment_cnt = comment_cnt + 1, comment_anonymous_cnt = comment_anonymous_cnt + 1 WHERE user_id = NEW.user_id;
			ELSE
				UPDATE user_count
				SET comment_cnt = comment_cnt + 1 WHERE user_id = NEW.user_id;
			END IF;
		ELSE
			IF (NEW.is_anonymous = 1) THEN
				INSERT INTO user_count(user_id, comment_cnt, comment_anonymous_cnt) VALUES (NEW.user_id, 1, 1);
			ELSE
				INSERT INTO user_count(user_id, comment_cnt) VALUES (NEW.user_id, 1);
			END IF;
		END IF;
		UPDATE board
		SET comment_cnt = comment_cnt + 1 WHERE id = NEW.board_id;
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `trg_at_update_comment` AFTER UPDATE ON `comment` FOR EACH ROW BEGIN 	
	IF (NEW.state != 0) THEN
		IF (OLD.is_anonymous = 0) THEN
			UPDATE user_count
			SET comment_cnt = comment_cnt - 1 WHERE user_id = NEW.user_id;
		ELSE
			UPDATE user_count
			SET comment_cnt = comment_cnt - 1, comment_anonymous_cnt = comment_anonymous_cnt - 1 WHERE user_id = NEW.user_id;
		END IF;
	
		UPDATE board
		SET comment_cnt = comment_cnt - 1 WHERE id = NEW.board_id;
	END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `comment_like`
--

DROP TABLE IF EXISTS `comment_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment_like` (
  `comment_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`comment_id`,`user_id`),
  KEY `user_id` (`user_id`),
  KEY `comment_like_comment_id_IDX` (`comment_id`) USING BTREE,
  CONSTRAINT `comment_like_ibfk_1` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `comment_like_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `plus_comment_like_cnt` AFTER INSERT ON `comment_like` FOR EACH ROW BEGIN 
	UPDATE comment
	SET like_cnt = like_cnt + 1 WHERE id = NEW.comment_id;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `minus_comment_like_cnt` AFTER DELETE ON `comment_like` FOR EACH ROW BEGIN 
	UPDATE comment
	SET like_cnt = like_cnt - 1 WHERE id = OLD.comment_id AND like_cnt > 0;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `comment_report`
--

DROP TABLE IF EXISTS `comment_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment_report` (
  `comment_id` int NOT NULL,
  `user_id` int NOT NULL,
  `report_id` int NOT NULL,
  `id` int NOT NULL AUTO_INCREMENT,
  `reported_at` datetime NOT NULL,
  `state` int NOT NULL DEFAULT '0' COMMENT '0: 처리전, 1: 처리 완료',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `comment_report_FK` (`report_id`),
  KEY `comment_report_FK_1` (`comment_id`),
  CONSTRAINT `comment_report_FK` FOREIGN KEY (`report_id`) REFERENCES `report` (`id`),
  CONSTRAINT `comment_report_FK_1` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `comment_report_FK_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `plus_comment_report_cnt` AFTER INSERT ON `comment_report` FOR EACH ROW BEGIN 
    DECLARE cnt INT;

    UPDATE comment
    SET report_cnt = report_cnt + 1
    WHERE id = NEW.comment_id;
   
    SELECT report_cnt INTO cnt FROM comment WHERE id = NEW.comment_id;

    IF cnt >= 10 THEN
        UPDATE comment
        SET state = 2
        WHERE id = NEW.comment_id;

    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `minus_comment_report_cnt` AFTER DELETE ON `comment_report` FOR EACH ROW BEGIN 
	UPDATE comment
	SET report_cnt = report_cnt - 1 WHERE id = OLD.comment_id AND report_cnt > 0;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `config`
--

DROP TABLE IF EXISTS `config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config` (
  `user_id` int NOT NULL,
  `config` json DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `config_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dm`
--

DROP TABLE IF EXISTS `dm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dm` (
  `id` int NOT NULL AUTO_INCREMENT,
  `is_from_sender` bit(1) DEFAULT NULL,
  `content` text NOT NULL,
  `time` datetime NOT NULL,
  `is_read` tinyint NOT NULL DEFAULT '0',
  `room_id` int DEFAULT NULL,
  `visible` enum('NOBODY','ONLY_SENDER','ONLY_RECEIVER','BOTH') NOT NULL DEFAULT 'BOTH',
  PRIMARY KEY (`id`),
  KEY `fk_room_dm` (`room_id`),
  CONSTRAINT `fk_room_dm` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=550 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `log_alert`
--

DROP TABLE IF EXISTS `log_alert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_alert` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `title` varchar(127) NOT NULL,
  `body` text NOT NULL,
  `etc` json DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `is_read` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `log_alert_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=300 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `log_login`
--

DROP TABLE IF EXISTS `log_login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_login` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `ip` varchar(127) NOT NULL,
  `time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `log_login_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=524 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `log_upload`
--

DROP TABLE IF EXISTS `log_upload`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_upload` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `file_size` int NOT NULL,
  `url` varchar(255) NOT NULL,
  `upload_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `log_upload_FK` (`user_id`),
  CONSTRAINT `log_upload_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=315 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `log_user`
--

DROP TABLE IF EXISTS `log_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `withdraw_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `major`
--

DROP TABLE IF EXISTS `major`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `major` (
  `id` int NOT NULL,
  `name` varchar(127) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `major_UN` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `poll`
--

DROP TABLE IF EXISTS `poll`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `poll` (
  `id` int NOT NULL AUTO_INCREMENT,
  `board_id` int DEFAULT NULL,
  `title` varchar(63) DEFAULT NULL,
  `expire_date` datetime DEFAULT NULL,
  `state` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `poll_FK` (`board_id`),
  CONSTRAINT `poll_FK` FOREIGN KEY (`board_id`) REFERENCES `board` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=116 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `poll_option`
--

DROP TABLE IF EXISTS `poll_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `poll_option` (
  `id` int NOT NULL AUTO_INCREMENT,
  `poll_id` int DEFAULT NULL,
  `option_name` varchar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `option_cnt` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `poll_option_FK` (`poll_id`),
  CONSTRAINT `poll_option_FK` FOREIGN KEY (`poll_id`) REFERENCES `poll` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=140 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `poll_user`
--

DROP TABLE IF EXISTS `poll_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `poll_user` (
  `user_id` int NOT NULL,
  `poll_id` int NOT NULL,
  `poll_option` int DEFAULT NULL,
  `created_at` datetime NOT NULL,
  PRIMARY KEY (`user_id`,`poll_id`),
  KEY `poll_user_ibfk_2` (`poll_id`),
  KEY `poll_user_ibfk_3` (`poll_option`),
  CONSTRAINT `poll_user_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `poll_user_ibfk_2` FOREIGN KEY (`poll_id`) REFERENCES `poll` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `poll_user_ibfk_3` FOREIGN KEY (`poll_option`) REFERENCES `poll_option` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `trg_at_poll_option_cnt_insert` AFTER INSERT ON `poll_user` FOR EACH ROW BEGIN
		
		UPDATE poll_option
		SET option_cnt = option_cnt + 1 WHERE id = NEW.poll_option AND poll_id = NEW.poll_id;
		
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `trg_at_poll_option_cnt_update` AFTER UPDATE ON `poll_user` FOR EACH ROW BEGIN
		
	IF OLD.user_id = NEW.user_id AND OLD.poll_id = NEW.poll_id THEN 
		
		UPDATE poll_option 
		SET option_cnt = option_cnt - 1 WHERE id = OLD.poll_option;
	
		UPDATE poll_option 
		SET option_cnt = option_cnt + 1 WHERE id = NEW.poll_option;
	
	END IF;
		
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `profile`
--

DROP TABLE IF EXISTS `profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `profile` (
  `user_id` int NOT NULL,
  `body` text,
  `profile_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'https://upload.wikimedia.org/wikipedia/commons/2/2c/Default_pfp.svg' COMMENT '프로필 이미지 s3 url',
  PRIMARY KEY (`user_id`),
  CONSTRAINT `profile_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report`
--

DROP TABLE IF EXISTS `report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '신고 항목 이름',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='신고 항목';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `id` int NOT NULL AUTO_INCREMENT,
  `last_content` text,
  `sender_id` int DEFAULT NULL,
  `receiver_id` int DEFAULT NULL,
  `time` datetime NOT NULL,
  `sender_is_deleted` tinyint NOT NULL DEFAULT '0',
  `receiver_is_deleted` tinyint NOT NULL DEFAULT '0',
  `sender_unread_count` int NOT NULL DEFAULT '0',
  `receiver_unread_count` int NOT NULL DEFAULT '0',
  `sender_dm_cursor` int DEFAULT '0',
  `receiver_dm_cursor` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sender_id_2` (`sender_id`,`receiver_id`),
  KEY `sender_id` (`sender_id`),
  KEY `receiver_id` (`receiver_id`),
  CONSTRAINT `fk_room_receiver` FOREIGN KEY (`receiver_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_room_sender` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`),
  CONSTRAINT `room_ibfk_1` FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `room_ibfk_2` FOREIGN KEY (`receiver_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `search_word`
--

DROP TABLE IF EXISTS `search_word`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `search_word` (
  `id` int NOT NULL AUTO_INCREMENT,
  `word` char(2) DEFAULT NULL,
  `df` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `word` (`word`),
  UNIQUE KEY `word_2` (`word`)
) ENGINE=InnoDB AUTO_INCREMENT=74158 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `search_word_inverse`
--

DROP TABLE IF EXISTS `search_word_inverse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `search_word_inverse` (
  `board_id` int NOT NULL,
  `search_word_id` int NOT NULL,
  `tf` int DEFAULT NULL,
  `idf` double DEFAULT NULL,
  PRIMARY KEY (`board_id`,`search_word_id`),
  KEY `search_word_inverse_ibfk_2` (`search_word_id`),
  CONSTRAINT `search_word_inverse_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `search_word_inverse_ibfk_2` FOREIGN KEY (`search_word_id`) REFERENCES `search_word` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `service_question`
--

DROP TABLE IF EXISTS `service_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_question` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `title` varchar(100) NOT NULL,
  `content` text,
  `created_at` datetime NOT NULL,
  `answer_email` varchar(255) NOT NULL,
  `images_url` text,
  `status` char(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `service_question_FK` (`user_id`),
  CONSTRAINT `service_question_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tag` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `category_id` int NOT NULL,
  `cnt` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tag_UN` (`category_id`,`name`),
  CONSTRAINT `tag_FK` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=344 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `univ`
--

DROP TABLE IF EXISTS `univ`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `univ` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(127) NOT NULL,
  `link` varchar(127) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=446 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `univ_id` int DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` char(60) NOT NULL,
  `nickname` varchar(15) DEFAULT NULL,
  `name` varchar(31) NOT NULL,
  `sno` varchar(31) NOT NULL,
  `ph_num` char(13) DEFAULT NULL,
  `state` int NOT NULL COMMENT '0 = 미인증, 1 = 인증, 2 = 제재, 3 = 탈퇴',
  `role` char(7) NOT NULL,
  `agreed_at` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `lastmodified_at` datetime DEFAULT NULL,
  `withdraw_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nickname` (`nickname`),
  KEY `univ_id` (`univ_id`),
  CONSTRAINT `user_FK` FOREIGN KEY (`univ_id`) REFERENCES `univ` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=253 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`maphant`@`%`*/ /*!50003 TRIGGER `user_AFTER_INSERT` AFTER INSERT ON `user` FOR EACH ROW BEGIN
		INSERT INTO profile (user_id)
		VALUES (NEW.id);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `user_bookmark`
--

DROP TABLE IF EXISTS `user_bookmark`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_bookmark` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `board_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_bookmark_UN` (`user_id`,`board_id`),
  KEY `user_bookmark_FK` (`user_id`),
  KEY `user_bookmark_FK_1` (`board_id`),
  CONSTRAINT `user_bookmark_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_bookmark_FK_1` FOREIGN KEY (`board_id`) REFERENCES `board` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=148 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_category_major`
--

DROP TABLE IF EXISTS `user_category_major`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_category_major` (
  `user_id` int NOT NULL,
  `category_id` int NOT NULL,
  `major_id` int NOT NULL,
  PRIMARY KEY (`user_id`,`category_id`,`major_id`),
  KEY `user_id` (`user_id`),
  KEY `FK_user_category_major_category` (`category_id`),
  KEY `FK_user_category_major_major` (`major_id`),
  CONSTRAINT `FK_user_category_major_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_user_category_major_major` FOREIGN KEY (`major_id`) REFERENCES `major` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_category_major_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `plus_category_cnt` AFTER INSERT ON `user_category_major` FOR EACH ROW BEGIN
	UPDATE category
	SET user_cnt = user_cnt + 1 WHERE id = NEW.category_id;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `minus_category_cnt` AFTER DELETE ON `user_category_major` FOR EACH ROW BEGIN 
	UPDATE category
	SET user_cnt = user_cnt - 1 WHERE id = OLD.category_id;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `user_count`
--

DROP TABLE IF EXISTS `user_count`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_count` (
  `user_id` int NOT NULL,
  `board_cnt` int NOT NULL DEFAULT '0',
  `board_anonymous_hide_cnt` int NOT NULL DEFAULT '0',
  `comment_cnt` int NOT NULL DEFAULT '0',
  `comment_anonymous_cnt` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`),
  CONSTRAINT `user_count_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_device`
--

DROP TABLE IF EXISTS `user_device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_device` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `fcm_token` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `fcm_token` (`fcm_token`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `user_device_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_report`
--

DROP TABLE IF EXISTS `user_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_report` (
  `id` int NOT NULL AUTO_INCREMENT,
  `deadline_at` datetime NOT NULL COMMENT '제재 기한',
  `sanction_reason` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '제재 사유',
  `user_id` int NOT NULL COMMENT '제재 대상',
  PRIMARY KEY (`id`),
  KEY `user_report_FK` (`user_id`),
  CONSTRAINT `user_report_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_token`
--

DROP TABLE IF EXISTS `user_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_token` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `public_key` char(48) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_token_UN` (`public_key`),
  KEY `user_token_FK` (`user_id`),
  CONSTRAINT `user_token_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=460 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'maphant2'
--
/*!50106 SET @save_time_zone= @@TIME_ZONE */ ;
/*!50106 DROP EVENT IF EXISTS `free_user_sanction` */;
DELIMITER ;;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;;
/*!50003 SET character_set_client  = utf8mb4 */ ;;
/*!50003 SET character_set_results = utf8mb4 */ ;;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;;
/*!50003 SET @saved_time_zone      = @@time_zone */ ;;
/*!50003 SET time_zone             = 'Asia/Seoul' */ ;;
/*!50106 CREATE*/ /*!50117 DEFINER=`root`@`%`*/ /*!50106 EVENT `free_user_sanction` ON SCHEDULE EVERY 1 DAY STARTS '2023-08-02 00:00:00' ON COMPLETION NOT PRESERVE ENABLE DO CALL set_user_state() */ ;;
/*!50003 SET time_zone             = @saved_time_zone */ ;;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;;
/*!50003 SET character_set_client  = @saved_cs_client */ ;;
/*!50003 SET character_set_results = @saved_cs_results */ ;;
/*!50003 SET collation_connection  = @saved_col_connection */ ;;
DELIMITER ;
/*!50106 SET TIME_ZONE= @save_time_zone */ ;

--
-- Dumping routines for database 'maphant2'
--
/*!50003 DROP FUNCTION IF EXISTS `hot` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `hot`(ups INT, date_created DATETIME) RETURNS float
BEGIN
    DECLARE s INT;
    DECLARE order_val FLOAT;
    DECLARE sign_val INT;
    DECLARE seconds FLOAT;
    DECLARE random_val FLOAT; 

    IF ups > 1 THEN
        SET order_val = LOG(ups, 10);
    ELSE
        SET order_val = 0;
    END IF;

    SET sign_val = CASE WHEN ups > 0 THEN 1 ELSE 0 END;
    SET seconds = UNIX_TIMESTAMP(date_created) - 1134028003;

    RETURN ROUND(sign_val * order_val + seconds / 45000, 7);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `set_user_state` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`maphant`@`%` PROCEDURE `set_user_state`()
BEGIN
	update board 
	set state = 0
	where user_id in (select a.id
						from (select id from user where state=2) as a
						    left join
						    (select sanctioned_user_ids.id
						     from (select id from user where state=2) as sanctioned_user_ids
						              join user_report on sanctioned_user_ids.id = user_report.id
						     where user_report.deadline_at > now()) as b
						on a.id = b.id)
    and report_cnt = 0;
   
	update user
	set state = 1
	where id in (select a.id
				from (select id from user where state=2) as a
				    left join
				    (select sanctioned_user_ids.id
				     from (select id from user where state=2) as sanctioned_user_ids
				              join user_report on sanctioned_user_ids.id = user_report.id
				     where user_report.deadline_at > now()) as b
				on a.id = b.id);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `USER_PROCEDURE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `USER_PROCEDURE`(
    IN IN_UNIV_ID INT,
    IN IN_EMAIL VARCHAR(255),
    IN IN_PASSWORD CHAR(60),
    IN IN_NICKNAME VARCHAR(15),
    IN IN_NAME VARCHAR(31),
    IN IN_SNO VARCHAR(31),
    IN IN_STATE INT,
    IN IN_ROLE CHAR(7),
    IN IN_AGREED_AT DATETIME,
    IN IN_CREATED_AT DATETIME,
    IN IN_LASTMODIFIED_AT DATETIME,
    IN IN_WITHDRAW_AT DATETIME,
    INOUT OUT_ERROR_CODE BOOLEAN,
    INOUT OUT_ERROR_MESSAGE VARCHAR(50)
)
BEGIN
    DECLARE is_already_registered INT;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET OUT_ERROR_CODE = TRUE;
        SET OUT_ERROR_MESSAGE = 'FAILED';
    END;

    SELECT COUNT(*) INTO is_already_registered
    FROM `user`
    WHERE (state IN (1, 2) OR DATEDIFF(NOW(), withdraw_at) < 30)
    AND email = IN_EMAIL;

    IF is_already_registered = 0 THEN
        INSERT INTO `user` (`univ_id`, `email`, `password`, `nickname`, `name`, `sno`, `state`, `role`, `agreed_at`, `created_at`, `lastmodified_at`, `withdraw_at`) VALUES (IN_UNIV_ID, IN_EMAIL, IN_PASSWORD, IN_NICKNAME, IN_NAME, IN_SNO, IN_STATE, IN_ROLE, NOW(), NOW(), NOW(), IN_WITHDRAW_AT);
        SET OUT_ERROR_CODE = FALSE;
        SET OUT_ERROR_MESSAGE = 'SUCCESS';
    ELSE
        SET OUT_ERROR_CODE = TRUE;
        SET OUT_ERROR_MESSAGE = 'USER ALREADY EXISTS';
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-31 17:35:40
