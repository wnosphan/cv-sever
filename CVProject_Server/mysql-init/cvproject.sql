-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: cvproject
-- ------------------------------------------------------
-- Server version	8.3.0

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
-- Table structure for table `apply_position`
--

DROP TABLE IF EXISTS `apply_position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `apply_position` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apply_position`
--

LOCK TABLES `apply_position` WRITE;
/*!40000 ALTER TABLE `apply_position` DISABLE KEYS */;
/*!40000 ALTER TABLE `apply_position` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cv`
--

DROP TABLE IF EXISTS `cv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cv` (
  `id` int NOT NULL AUTO_INCREMENT,
  `full_name` varchar(45) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `skill` varchar(45) DEFAULT NULL,
  `university` varchar(45) DEFAULT NULL,
  `training_system` varchar(45) DEFAULT NULL,
  `create_by` int DEFAULT NULL,
  `gpa` varchar(45) DEFAULT NULL,
  `apply_position` varchar(45) DEFAULT NULL,
  `link_cv` varchar(45) DEFAULT NULL,
  `status` varchar(45) DEFAULT 'INPROGRESS',
  PRIMARY KEY (`id`),
  KEY `idUser_idx` (`create_by`),
  CONSTRAINT `createby` FOREIGN KEY (`create_by`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cv`
--

LOCK TABLES `cv` WRITE;
/*!40000 ALTER TABLE `cv` DISABLE KEYS */;
/*!40000 ALTER TABLE `cv` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `after_insert_skill` AFTER INSERT ON `cv` FOR EACH ROW BEGIN
  -- Biến để lưu từng skill sau khi cắt chuỗi
  DECLARE skill_name VARCHAR(255);
  -- Biến để lưu vị trí của dấu phẩy
  DECLARE comma_index INT;
  -- Biến để lưu chuỗi skill chưa được xử lý
  DECLARE remaining_string VARCHAR(255) DEFAULT NEW.skill;
 -- Lưu tên của trường đại học từ cột university của bảng cv
  DECLARE uni_name VARCHAR(255);
  -- Biến để kiểm tra xem tên trường đã tồn tại trong bảng university chưa
  DECLARE uni_exists INT;
   -- Biến để lưu tên vị trí ứng tuyển từ cột position của bảng cv
  DECLARE position_name VARCHAR(255);
  -- Biến để kiểm tra xem tên vị trí ứng tuyển đã tồn tại trong bảng apply_position chưa
  DECLARE position_exists INT;
  -- Vòng lặp để xử lý chuỗi skill
  WHILE LOCATE(',', remaining_string) > 0 DO
    -- Tìm vị trí của dấu phẩy đầu tiên
    SET comma_index = LOCATE(',', remaining_string);
    -- Cắt lấy skill từ đầu chuỗi đến dấu phẩy
    SET skill_name = TRIM(SUBSTRING(remaining_string, 1, comma_index - 1));
    -- Cập nhật lại chuỗi còn lại sau khi cắt
    SET remaining_string = SUBSTRING(remaining_string, comma_index + 1);
    
    -- Kiểm tra skill có tồn tại trong bảng skill hay không
    IF NOT EXISTS (SELECT * FROM skill WHERE skill_item = skill_name) THEN
      -- Nếu không tồn tại, thêm skill mới vào bảng
      INSERT INTO skill (skill_item) VALUES (skill_name);
    END IF;
  END WHILE;
  
  -- Xử lý cho skill cuối cùng (không có dấu phẩy)
  IF TRIM(remaining_string) != '' THEN
    SET skill_name = TRIM(remaining_string);
    IF NOT EXISTS (SELECT * FROM skill WHERE skill_item = skill_name) THEN
      INSERT INTO skill (skill_item) VALUES (skill_name);
    END IF;
  END IF;
  -- uni
 
  
  -- Lấy tên trường từ cột university của bảng cv
  SET uni_name = TRIM(NEW.university);
  
  -- Kiểm tra xem tên trường đã tồn tại trong bảng university hay chưa
  SELECT COUNT(*) INTO uni_exists FROM university WHERE name = uni_name;
  
  -- Nếu tên trường chưa tồn tại, thêm nó vào bảng university
  IF uni_exists = 0 THEN
    INSERT INTO university (name) VALUES (uni_name);
  END IF;
  -- Lấy tên vị trí ứng tuyển từ cột position của bảng cv
  SET position_name = TRIM(NEW.apply_position);
  
  -- Kiểm tra xem tên vị trí ứng tuyển đã tồn tại trong bảng apply_position hay chưa
  SELECT COUNT(*) INTO position_exists FROM apply_position WHERE name = position_name;
  
  -- Nếu tên vị trí ứng tuyển chưa tồn tại, thêm nó vào bảng apply_position
  IF position_exists = 0 THEN
    INSERT INTO apply_position (name) VALUES (position_name);
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `cv_AFTER_UPDATE` AFTER UPDATE ON `cv` FOR EACH ROW BEGIN
-- Biến để lưu từng skill sau khi cắt chuỗi
  DECLARE skill_name VARCHAR(255);
  -- Biến để lưu vị trí của dấu phẩy
  DECLARE comma_index INT;
  -- Biến để lưu chuỗi skill chưa được xử lý
  DECLARE remaining_string VARCHAR(255) DEFAULT NEW.skill;
 -- Lưu tên của trường đại học từ cột university của bảng cv
  DECLARE uni_name VARCHAR(255);
  -- Biến để kiểm tra xem tên trường đã tồn tại trong bảng university chưa
  DECLARE uni_exists INT;
   -- Biến để lưu tên vị trí ứng tuyển từ cột position của bảng cv
  DECLARE position_name VARCHAR(255);
  -- Biến để kiểm tra xem tên vị trí ứng tuyển đã tồn tại trong bảng apply_position chưa
  DECLARE position_exists INT;
  -- Vòng lặp để xử lý chuỗi skill
  WHILE LOCATE(',', remaining_string) > 0 DO
    -- Tìm vị trí của dấu phẩy đầu tiên
    SET comma_index = LOCATE(',', remaining_string);
    -- Cắt lấy skill từ đầu chuỗi đến dấu phẩy
    SET skill_name = TRIM(SUBSTRING(remaining_string, 1, comma_index - 1));
    -- Cập nhật lại chuỗi còn lại sau khi cắt
    SET remaining_string = SUBSTRING(remaining_string, comma_index + 1);
    
    -- Kiểm tra skill có tồn tại trong bảng skill hay không
    IF NOT EXISTS (SELECT * FROM skill WHERE skill_item = skill_name) THEN
      -- Nếu không tồn tại, thêm skill mới vào bảng
      INSERT INTO skill (skill_item) VALUES (skill_name);
    END IF;
  END WHILE;
  
  -- Xử lý cho skill cuối cùng (không có dấu phẩy)
  IF TRIM(remaining_string) != '' THEN
    SET skill_name = TRIM(remaining_string);
    IF NOT EXISTS (SELECT * FROM skill WHERE skill_item = skill_name) THEN
      INSERT INTO skill (skill_item) VALUES (skill_name);
    END IF;
  END IF;
  -- uni
 
  
  -- Lấy tên trường từ cột university của bảng cv
  SET uni_name = TRIM(NEW.university);
  
  -- Kiểm tra xem tên trường đã tồn tại trong bảng university hay chưa
  SELECT COUNT(*) INTO uni_exists FROM university WHERE name = uni_name;
  
  -- Nếu tên trường chưa tồn tại, thêm nó vào bảng university
  IF uni_exists = 0 THEN
    INSERT INTO university (name) VALUES (uni_name);
  END IF;
  -- Lấy tên vị trí ứng tuyển từ cột position của bảng cv
  SET position_name = TRIM(NEW.apply_position);
  
  -- Kiểm tra xem tên vị trí ứng tuyển đã tồn tại trong bảng apply_position hay chưa
  SELECT COUNT(*) INTO position_exists FROM apply_position WHERE name = position_name;
  
  -- Nếu tên vị trí ứng tuyển chưa tồn tại, thêm nó vào bảng apply_position
  IF position_exists = 0 THEN
    INSERT INTO apply_position (name) VALUES (position_name);
  END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `skill`
--

DROP TABLE IF EXISTS `skill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `skill` (
  `id` int NOT NULL AUTO_INCREMENT,
  `skill_item` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `skill`
--

LOCK TABLES `skill` WRITE;
/*!40000 ALTER TABLE `skill` DISABLE KEYS */;
/*!40000 ALTER TABLE `skill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `university`
--

DROP TABLE IF EXISTS `university`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `university` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `university`
--

LOCK TABLES `university` WRITE;
/*!40000 ALTER TABLE `university` DISABLE KEYS */;
/*!40000 ALTER TABLE `university` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(45) DEFAULT NULL,
  `user_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'cvproject'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-16 23:10:45
