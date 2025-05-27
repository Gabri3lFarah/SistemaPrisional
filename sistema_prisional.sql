-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: sistema_prisional
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `dados_prisioneiro`
--

DROP TABLE IF EXISTS `dados_prisioneiro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dados_prisioneiro` (
  `prisioneiro_id` bigint NOT NULL,
  `cpf` varchar(11) DEFAULT NULL,
  `telefone_contato` varchar(15) DEFAULT NULL,
  `advogado_nome` varchar(100) DEFAULT NULL,
  `advogado_telefone` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`prisioneiro_id`),
  CONSTRAINT `dados_prisioneiro_ibfk_1` FOREIGN KEY (`prisioneiro_id`) REFERENCES `prisioneiros` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dados_prisioneiro`
--

LOCK TABLES `dados_prisioneiro` WRITE;
/*!40000 ALTER TABLE `dados_prisioneiro` DISABLE KEYS */;
INSERT INTO `dados_prisioneiro` VALUES (1,'12345678901','(48)91234-5678','Dr. Zé Zueiro','(48)97654-3210'),(2,'23456789012','(48)92345-6789','Dr. Tadeu Trap','(48)98765-4321'),(3,'34567890123','(48)93456-7890','Dr. Cosme Cômico','(48)97654-3211'),(4,'45678901234','(48)94567-8901','Dr. Bolinha Brincalhão','(48)96543-2109'),(5,'56789012345','(48)95678-9012','Dr. Trapaca Alegre','(48)95432-1098'),(6,'67890123456','(48)96789-0123','Dr. Rufino Risonho','(48)94321-0987'),(7,'78901234567','(48)97890-1234','Dr. Joca Jocale','(48)93210-9876'),(8,'89012345678','(48)98901-2345','Dr. Pipo Piradinho','(48)92109-8765'),(9,'90123456789','(48)99012-3456','Dr. Quico Quirino','(48)91098-7654'),(10,'01234567890','(48)90123-4567','Dr. Totó Tropeço','(48)90987-6543'),(11,'11234567890','(48)91234-5678','Dr. Zeca Zombeteiro','(48)90876-5432'),(12,'21234567890','(48)92345-6789','Dr. Téo Tromba','(48)90765-4321'),(13,'31234567890','(48)93456-7890','Dr. Cosmo Cômico','(48)90654-3210'),(14,'41234567890','(48)94567-8901','Dr. Benito Berrante','(48)90543-2109'),(15,'51234567890','(48)95678-9012','Dr. Valdemar Vadio','(48)90432-1098'),(16,'61234567890','(48)96789-0123','Dr. Flávio Fanfarrão','(48)90321-0987'),(17,'71234567890','(48)97890-1234','Dr. Rogério Risadinha','(48)90210-9876'),(18,'81234567890','(48)98901-2345','Dr. Max Maroto','(48)90109-8765'),(19,'91234567890','(48)99012-3456','Dr. Leandro Lelé','(48)90098-7654'),(20,'02234567890','(48)90123-4567','Dr. Bebeto Balla na Agulha','(48)89987-6543');
/*!40000 ALTER TABLE `dados_prisioneiro` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `myentity`
--

DROP TABLE IF EXISTS `myentity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `myentity` (
  `id` bigint NOT NULL,
  `field` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `myentity`
--

LOCK TABLES `myentity` WRITE;
/*!40000 ALTER TABLE `myentity` DISABLE KEYS */;
/*!40000 ALTER TABLE `myentity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `myentity_seq`
--

DROP TABLE IF EXISTS `myentity_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `myentity_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `myentity_seq`
--

LOCK TABLES `myentity_seq` WRITE;
/*!40000 ALTER TABLE `myentity_seq` DISABLE KEYS */;
INSERT INTO `myentity_seq` VALUES (1);
/*!40000 ALTER TABLE `myentity_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prisioneiros`
--

DROP TABLE IF EXISTS `prisioneiros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prisioneiros` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prisioneiros`
--

LOCK TABLES `prisioneiros` WRITE;
/*!40000 ALTER TABLE `prisioneiros` DISABLE KEYS */;
INSERT INTO `prisioneiros` VALUES (1,'Zé Bolinha','Prisioneiro 1'),(2,'Tadeu Trapaça','Prisioneiro 2'),(3,'Neco do Caos','Prisioneiro 3'),(4,'Cosme Comédia','Prisioneiro 4'),(5,'Bolinha Fanfarrão','Prisioneiro 5'),(6,'Florêncio do Riso','Prisioneiro 6'),(7,'Maneco Maluquinho','Prisioneiro 7'),(8,'Quintino Quack','Prisioneiro 8'),(9,'Gervásio Gargalhada','Prisioneiro 9'),(10,'Juarez Julgante','Prisioneiro 10'),(11,'Ortábio Ousado','Prisioneiro 11'),(12,'Raimundo Risadinha','Prisioneiro 12'),(13,'Toinho Trombudo','Prisioneiro 13'),(14,'Zulmar Zen','Prisioneiro 14'),(15,'Teodoro Tintilante','Prisioneiro 15'),(16,'Marquinhos Maluco','Prisioneiro 16'),(17,'Crispim Cômico','Prisioneiro 17'),(18,'Sérgio Sarado','Prisioneiro 18'),(19,'Alfredo Alegrete','Prisioneiro 19'),(20,'Beto Bizarro','Prisioneiro 20'),(21,'Zé Bolinha','Prisioneiro 21'),(22,'Tadeu Trapaça','Prisioneiro 22'),(23,'Neco do Caos','Prisioneiro 23'),(24,'Cosme Comédia','Prisioneiro 24'),(25,'Bolinha Fanfarrão','Prisioneiro 25'),(26,'Florêncio do Riso','Prisioneiro 26'),(27,'Maneco Maluquinho','Prisioneiro 27'),(28,'Quintino Quack','Prisioneiro 28'),(29,'Gervásio Gargalhada','Prisioneiro 29'),(30,'Juarez Julgante','Prisioneiro 30'),(31,'Ortábio Ousado','Prisioneiro 31'),(32,'Raimundo Risadinha','Prisioneiro 32'),(33,'Toinho Trombudo','Prisioneiro 33'),(34,'Zulmar Zen','Prisioneiro 34'),(35,'Teodoro Tintilante','Prisioneiro 35'),(36,'Marquinhos Maluco','Prisioneiro 36'),(37,'Crispim Cômico','Prisioneiro 37'),(38,'Sérgio Sarado','Prisioneiro 38'),(39,'Alfredo Alegrete','Prisioneiro 39'),(40,'Beto Bizarro','Prisioneiro 40'),(41,'Velho Viril','Prisioneiro 41');
/*!40000 ALTER TABLE `prisioneiros` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prisioneiros_seq`
--

DROP TABLE IF EXISTS `prisioneiros_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prisioneiros_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prisioneiros_seq`
--

LOCK TABLES `prisioneiros_seq` WRITE;
/*!40000 ALTER TABLE `prisioneiros_seq` DISABLE KEYS */;
INSERT INTO `prisioneiros_seq` VALUES (1);
/*!40000 ALTER TABLE `prisioneiros_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prisoner`
--

DROP TABLE IF EXISTS `prisoner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prisoner` (
  `id` bigint NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prisoner`
--

LOCK TABLES `prisoner` WRITE;
/*!40000 ALTER TABLE `prisoner` DISABLE KEYS */;
/*!40000 ALTER TABLE `prisoner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prisoner_seq`
--

DROP TABLE IF EXISTS `prisoner_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prisoner_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prisoner_seq`
--

LOCK TABLES `prisoner_seq` WRITE;
/*!40000 ALTER TABLE `prisoner_seq` DISABLE KEYS */;
INSERT INTO `prisoner_seq` VALUES (1);
/*!40000 ALTER TABLE `prisoner_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visitantes`
--

DROP TABLE IF EXISTS `visitantes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `visitantes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `telefone` varchar(15) DEFAULT NULL,
  `cpf` varchar(11) DEFAULT NULL,
  `endereco` varchar(150) DEFAULT NULL,
  `codigo_autorizacao` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visitantes`
--

LOCK TABLES `visitantes` WRITE;
/*!40000 ALTER TABLE `visitantes` DISABLE KEYS */;
/*!40000 ALTER TABLE `visitantes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `visitas`
--

DROP TABLE IF EXISTS `visitas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `visitas` (
  `id` int NOT NULL AUTO_INCREMENT,
  `prisioneiro_id` int NOT NULL,
  `data_visita` date NOT NULL,
  `nome_visitante` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `prisioneiro_id` (`prisioneiro_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `visitas`
--

LOCK TABLES `visitas` WRITE;
/*!40000 ALTER TABLE `visitas` DISABLE KEYS */;
/*!40000 ALTER TABLE `visitas` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-27 16:26:55
