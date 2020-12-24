/*
Navicat MySQL Data Transfer

Source Server         : 111
Source Server Version : 80018
Source Host           : localhost:3306
Source Database       : javaweb

Target Server Type    : MYSQL
Target Server Version : 80018
File Encoding         : 65001

Date: 2020-07-12 10:15:28
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `belong`
-- ----------------------------
DROP TABLE IF EXISTS `belong`;
CREATE TABLE `belong` (
  `belongID` int(11) NOT NULL AUTO_INCREMENT,
  `resourceID` int(11) NOT NULL,
  `territoryID` int(11) NOT NULL,
  PRIMARY KEY (`belongID`),
  KEY `FK9` (`resourceID`),
  KEY `FK10` (`territoryID`),
  CONSTRAINT `FK10` FOREIGN KEY (`territoryID`) REFERENCES `territorytable` (`territoryID`),
  CONSTRAINT `FK9` FOREIGN KEY (`resourceID`) REFERENCES `resourcetable` (`resourceID`)
) ENGINE=InnoDB AUTO_INCREMENT=407 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of belong
-- ----------------------------
INSERT INTO `belong` VALUES ('346', '130', '1');
INSERT INTO `belong` VALUES ('347', '130', '2');
INSERT INTO `belong` VALUES ('348', '130', '3');
INSERT INTO `belong` VALUES ('349', '131', '1');
INSERT INTO `belong` VALUES ('350', '131', '4');
INSERT INTO `belong` VALUES ('351', '131', '5');
INSERT INTO `belong` VALUES ('352', '131', '6');
INSERT INTO `belong` VALUES ('353', '132', '1');
INSERT INTO `belong` VALUES ('354', '132', '2');
INSERT INTO `belong` VALUES ('355', '132', '4');
INSERT INTO `belong` VALUES ('356', '133', '2');
INSERT INTO `belong` VALUES ('357', '133', '3');
INSERT INTO `belong` VALUES ('358', '133', '5');
INSERT INTO `belong` VALUES ('359', '133', '6');
INSERT INTO `belong` VALUES ('360', '134', '1');
INSERT INTO `belong` VALUES ('361', '134', '2');
INSERT INTO `belong` VALUES ('362', '135', '1');
INSERT INTO `belong` VALUES ('363', '135', '2');
INSERT INTO `belong` VALUES ('364', '135', '3');
INSERT INTO `belong` VALUES ('365', '135', '4');
INSERT INTO `belong` VALUES ('366', '135', '5');
INSERT INTO `belong` VALUES ('367', '136', '2');
INSERT INTO `belong` VALUES ('368', '136', '3');
INSERT INTO `belong` VALUES ('369', '137', '1');
INSERT INTO `belong` VALUES ('370', '137', '2');
INSERT INTO `belong` VALUES ('371', '137', '4');
INSERT INTO `belong` VALUES ('372', '137', '5');
INSERT INTO `belong` VALUES ('373', '138', '1');
INSERT INTO `belong` VALUES ('374', '138', '4');
INSERT INTO `belong` VALUES ('375', '138', '8');
INSERT INTO `belong` VALUES ('376', '139', '1');
INSERT INTO `belong` VALUES ('377', '139', '3');
INSERT INTO `belong` VALUES ('378', '139', '4');
INSERT INTO `belong` VALUES ('379', '139', '5');
INSERT INTO `belong` VALUES ('380', '140', '1');
INSERT INTO `belong` VALUES ('381', '140', '3');
INSERT INTO `belong` VALUES ('382', '140', '4');
INSERT INTO `belong` VALUES ('383', '140', '6');
INSERT INTO `belong` VALUES ('384', '140', '7');
INSERT INTO `belong` VALUES ('385', '141', '1');
INSERT INTO `belong` VALUES ('386', '141', '2');
INSERT INTO `belong` VALUES ('387', '141', '7');
INSERT INTO `belong` VALUES ('388', '141', '8');
INSERT INTO `belong` VALUES ('389', '142', '1');
INSERT INTO `belong` VALUES ('390', '142', '2');
INSERT INTO `belong` VALUES ('391', '143', '1');
INSERT INTO `belong` VALUES ('392', '143', '2');
INSERT INTO `belong` VALUES ('393', '143', '3');
INSERT INTO `belong` VALUES ('394', '143', '4');
INSERT INTO `belong` VALUES ('395', '144', '1');
INSERT INTO `belong` VALUES ('396', '144', '4');
INSERT INTO `belong` VALUES ('397', '144', '5');
INSERT INTO `belong` VALUES ('398', '145', '1');
INSERT INTO `belong` VALUES ('399', '145', '4');
INSERT INTO `belong` VALUES ('400', '146', '1');
INSERT INTO `belong` VALUES ('403', '128', '1');
INSERT INTO `belong` VALUES ('404', '128', '3');
INSERT INTO `belong` VALUES ('405', '149', '1');
INSERT INTO `belong` VALUES ('406', '149', '3');

-- ----------------------------
-- Table structure for `discuss`
-- ----------------------------
DROP TABLE IF EXISTS `discuss`;
CREATE TABLE `discuss` (
  `discussID` int(11) NOT NULL AUTO_INCREMENT,
  `userID` int(11) NOT NULL,
  `resourceID` int(11) NOT NULL,
  `discussContent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`discussID`),
  KEY `FK6` (`userID`),
  KEY `FK7` (`resourceID`),
  CONSTRAINT `FK6` FOREIGN KEY (`userID`) REFERENCES `usertable` (`userID`),
  CONSTRAINT `FK7` FOREIGN KEY (`resourceID`) REFERENCES `resourcetable` (`resourceID`)
) ENGINE=InnoDB AUTO_INCREMENT=114 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of discuss
-- ----------------------------
INSERT INTO `discuss` VALUES ('106', '30', '128', '论文比较难懂。');
INSERT INTO `discuss` VALUES ('107', '33', '128', '还好吧！翻译一下，认真读一下。');
INSERT INTO `discuss` VALUES ('108', '31', '128', '楼上两位，这篇论文该如何开始学习呢？');
INSERT INTO `discuss` VALUES ('110', '31', '128', '哈哈');
INSERT INTO `discuss` VALUES ('112', '33', '128', '大家好！');
INSERT INTO `discuss` VALUES ('113', '33', '128', '论文里面有明显错误。');

-- ----------------------------
-- Table structure for `download`
-- ----------------------------
DROP TABLE IF EXISTS `download`;
CREATE TABLE `download` (
  `downLoadID` int(11) NOT NULL AUTO_INCREMENT,
  `userID` int(11) NOT NULL,
  `resourceID` int(11) NOT NULL,
  `downLoadTime` datetime NOT NULL,
  PRIMARY KEY (`downLoadID`),
  KEY `FK2` (`userID`),
  KEY `FK1` (`resourceID`),
  CONSTRAINT `FK1` FOREIGN KEY (`resourceID`) REFERENCES `resourcetable` (`resourceID`),
  CONSTRAINT `FK2` FOREIGN KEY (`userID`) REFERENCES `usertable` (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of download
-- ----------------------------
INSERT INTO `download` VALUES ('85', '30', '128', '2020-07-11 12:25:29');
INSERT INTO `download` VALUES ('86', '30', '128', '2020-07-11 12:30:38');
INSERT INTO `download` VALUES ('87', '33', '128', '2020-07-11 12:30:48');
INSERT INTO `download` VALUES ('88', '33', '128', '2020-07-11 12:31:09');
INSERT INTO `download` VALUES ('89', '30', '128', '2020-07-11 12:31:17');
INSERT INTO `download` VALUES ('90', '30', '141', '2020-07-11 12:32:17');
INSERT INTO `download` VALUES ('91', '30', '136', '2020-07-11 12:32:37');
INSERT INTO `download` VALUES ('92', '33', '130', '2020-07-11 12:32:53');

-- ----------------------------
-- Table structure for `relation`
-- ----------------------------
DROP TABLE IF EXISTS `relation`;
CREATE TABLE `relation` (
  `resourceIDOne` int(11) NOT NULL,
  `resourceIDTwo` int(11) NOT NULL,
  PRIMARY KEY (`resourceIDOne`,`resourceIDTwo`),
  KEY `FK11` (`resourceIDTwo`),
  CONSTRAINT `FK11` FOREIGN KEY (`resourceIDTwo`) REFERENCES `resourcetable` (`resourceID`),
  CONSTRAINT `FK12` FOREIGN KEY (`resourceIDOne`) REFERENCES `resourcetable` (`resourceID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of relation
-- ----------------------------
INSERT INTO `relation` VALUES ('128', '130');
INSERT INTO `relation` VALUES ('135', '130');
INSERT INTO `relation` VALUES ('140', '130');
INSERT INTO `relation` VALUES ('128', '131');
INSERT INTO `relation` VALUES ('134', '131');
INSERT INTO `relation` VALUES ('140', '131');
INSERT INTO `relation` VALUES ('128', '132');
INSERT INTO `relation` VALUES ('134', '132');
INSERT INTO `relation` VALUES ('135', '132');
INSERT INTO `relation` VALUES ('141', '132');
INSERT INTO `relation` VALUES ('128', '133');
INSERT INTO `relation` VALUES ('140', '133');
INSERT INTO `relation` VALUES ('141', '133');
INSERT INTO `relation` VALUES ('137', '134');
INSERT INTO `relation` VALUES ('139', '134');
INSERT INTO `relation` VALUES ('142', '134');
INSERT INTO `relation` VALUES ('143', '134');
INSERT INTO `relation` VALUES ('147', '134');
INSERT INTO `relation` VALUES ('136', '135');
INSERT INTO `relation` VALUES ('137', '135');
INSERT INTO `relation` VALUES ('138', '135');
INSERT INTO `relation` VALUES ('139', '135');
INSERT INTO `relation` VALUES ('144', '135');
INSERT INTO `relation` VALUES ('128', '136');
INSERT INTO `relation` VALUES ('141', '136');
INSERT INTO `relation` VALUES ('149', '136');
INSERT INTO `relation` VALUES ('149', '137');
INSERT INTO `relation` VALUES ('128', '138');
INSERT INTO `relation` VALUES ('140', '138');
INSERT INTO `relation` VALUES ('140', '139');
INSERT INTO `relation` VALUES ('141', '139');
INSERT INTO `relation` VALUES ('145', '140');
INSERT INTO `relation` VALUES ('142', '141');
INSERT INTO `relation` VALUES ('143', '141');
INSERT INTO `relation` VALUES ('144', '141');
INSERT INTO `relation` VALUES ('128', '142');
INSERT INTO `relation` VALUES ('128', '144');
INSERT INTO `relation` VALUES ('128', '145');
INSERT INTO `relation` VALUES ('148', '146');
INSERT INTO `relation` VALUES ('128', '148');

-- ----------------------------
-- Table structure for `resourcetable`
-- ----------------------------
DROP TABLE IF EXISTS `resourcetable`;
CREATE TABLE `resourcetable` (
  `resourceID` int(11) NOT NULL AUTO_INCREMENT,
  `resourceName` varchar(255) NOT NULL,
  `resourceType` int(11) NOT NULL,
  `resourceLocation` varchar(255) NOT NULL,
  `visitVolume` int(11) NOT NULL DEFAULT '0',
  `uploadTime` timestamp NOT NULL,
  `introduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `userID` int(11) NOT NULL,
  PRIMARY KEY (`resourceID`),
  UNIQUE KEY `differ` (`resourceName`,`resourceLocation`) USING BTREE,
  KEY `FK` (`userID`),
  CONSTRAINT `FK` FOREIGN KEY (`userID`) REFERENCES `usertable` (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=150 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of resourcetable
-- ----------------------------
INSERT INTO `resourcetable` VALUES ('128', '论文1.txt', '1', 'uploadresource\\论文1.txt', '0', '2020-07-11 11:56:40', '论文1', '30');
INSERT INTO `resourcetable` VALUES ('130', '代码1.txt', '2', 'uploadresource\\代码1.txt', '0', '2020-07-11 11:57:39', '代码1', '30');
INSERT INTO `resourcetable` VALUES ('131', '代码2.txt', '2', 'uploadresource\\代码2.txt', '0', '2020-07-11 11:58:14', '代码2', '30');
INSERT INTO `resourcetable` VALUES ('132', '数据集1.rar', '3', 'uploadresource\\数据集1.rar', '0', '2020-07-11 11:58:42', '数据集1', '30');
INSERT INTO `resourcetable` VALUES ('133', '数据集2.rar', '3', 'uploadresource\\数据集2.rar', '0', '2020-07-11 11:59:04', '数据集2', '30');
INSERT INTO `resourcetable` VALUES ('134', '论文3.txt', '1', 'uploadresource\\论文3.txt', '0', '2020-07-11 12:00:37', '论文3', '31');
INSERT INTO `resourcetable` VALUES ('135', '论文4.txt', '1', 'uploadresource\\论文4.txt', '0', '2020-07-11 12:01:11', '论文4', '31');
INSERT INTO `resourcetable` VALUES ('136', '代码3.txt', '2', 'uploadresource\\代码3.txt', '0', '2020-07-11 12:01:37', '代码3', '31');
INSERT INTO `resourcetable` VALUES ('137', '代码4.txt', '2', 'uploadresource\\代码4.txt', '0', '2020-07-11 12:02:07', '代码4', '31');
INSERT INTO `resourcetable` VALUES ('138', '数据集3.rar', '3', 'uploadresource\\数据集3.rar', '0', '2020-07-11 12:02:55', '数据集3', '31');
INSERT INTO `resourcetable` VALUES ('139', '数据集4.rar', '3', 'uploadresource\\数据集4.rar', '0', '2020-07-11 12:03:55', '数据集4', '31');
INSERT INTO `resourcetable` VALUES ('140', '论文5.txt', '1', 'uploadresource\\论文5.txt', '0', '2020-07-11 12:06:39', '论文5', '33');
INSERT INTO `resourcetable` VALUES ('141', '论文6.txt', '1', 'uploadresource\\论文6.txt', '0', '2020-07-11 12:07:18', '论文6', '33');
INSERT INTO `resourcetable` VALUES ('142', '代码5.txt', '2', 'uploadresource\\代码5.txt', '0', '2020-07-11 12:07:47', '代码5', '33');
INSERT INTO `resourcetable` VALUES ('143', '代码6.txt', '2', 'uploadresource\\代码6.txt', '0', '2020-07-11 12:08:17', '代码6', '33');
INSERT INTO `resourcetable` VALUES ('144', '数据集5.rar', '3', 'uploadresource\\数据集5.rar', '0', '2020-07-11 12:08:43', '数据集5', '33');
INSERT INTO `resourcetable` VALUES ('145', '数据集6.rar', '3', 'uploadresource\\数据集6.rar', '0', '2020-07-11 12:09:06', null, '33');
INSERT INTO `resourcetable` VALUES ('146', '论文7.txt', '1', 'uploadresource\\论文7.txt', '0', '2020-07-11 12:10:44', null, '30');
INSERT INTO `resourcetable` VALUES ('147', '代码7.txt', '2', 'uploadresource\\代码7.txt', '0', '2020-07-11 12:11:05', null, '30');
INSERT INTO `resourcetable` VALUES ('148', '数据集7.rar', '3', 'uploadresource\\数据集7.rar', '0', '2020-07-11 12:11:16', null, '30');
INSERT INTO `resourcetable` VALUES ('149', '论文2.txt', '1', 'uploadresource\\论文2.txt', '0', '2020-07-12 01:42:26', null, '30');

-- ----------------------------
-- Table structure for `territorytable`
-- ----------------------------
DROP TABLE IF EXISTS `territorytable`;
CREATE TABLE `territorytable` (
  `territoryID` int(11) NOT NULL AUTO_INCREMENT,
  `territoryName` varchar(255) NOT NULL,
  `territoryIntroduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  PRIMARY KEY (`territoryID`),
  UNIQUE KEY `differ1` (`territoryName`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of territorytable
-- ----------------------------
INSERT INTO `territorytable` VALUES ('1', '信息检索', '信息检索（Information Retrieval）是指信息按一定的方式组织起来，并根据信息用户的需要找出有关的信息的过程和技术。狭义的信息检索就是信息检索过程的后半部分，即从信息集合中找出所需要的信息的过程，也就是我们常说的信息查寻（Information Search 或Information Seek）。一般情况下，信息检索指的就是广义的信息检索。');
INSERT INTO `territorytable` VALUES ('2', '文本摘要', 'Text Summarization');
INSERT INTO `territorytable` VALUES ('3', '推荐系统', '推荐系统是利用电子商务网站向客户提供商品信息和建议，帮助用户决定应该购买什么产品，模拟销售人员帮助客户完成购买过程。个性化推荐是根据用户的兴趣特点和购买行为，向用户推荐用户感兴趣的信息和商品。');
INSERT INTO `territorytable` VALUES ('4', '社交分析', 'Social Analysis');
INSERT INTO `territorytable` VALUES ('5', '情感分析', '文本情感分析，又称意见挖掘、倾向性分析等。简单而言,是对带有情感色彩的主观性文本进行分析、处理、归纳和推理的过程。');
INSERT INTO `territorytable` VALUES ('6', '对话系统', 'Conversational Systems');
INSERT INTO `territorytable` VALUES ('7', '问答系统', '问答系统(Question Answering System, QA)是信息检索系统的一种高级形式，它能用准确、简洁的自然语言回答用户用自然语言提出的问题。');
INSERT INTO `territorytable` VALUES ('8', '知识图谱', '知识图谱（Knowledge Graph），在图书情报界称为知识域可视化或知识领域映射地图，是显示知识发展进程与结构关系的一系列各种不同的图形，用可视化技术描述知识资源及其载体，挖掘、分析、构建、绘制和显示知识及它们之间的相互联系。');

-- ----------------------------
-- Table structure for `usertable`
-- ----------------------------
DROP TABLE IF EXISTS `usertable`;
CREATE TABLE `usertable` (
  `userID` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `userName` varchar(255) NOT NULL,
  `userType` int(255) NOT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`userID`),
  UNIQUE KEY `differ` (`userName`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of usertable
-- ----------------------------
INSERT INTO `usertable` VALUES ('5', '9b59b1ef4c9614c70fec7d08ca9de07b', 'admm', '2', '男', '', '');
INSERT INTO `usertable` VALUES ('27', 'b86d32dbf9d7e6338db7b47fc1fb805e', 'admmm', '2', '女', null, null);
INSERT INTO `usertable` VALUES ('30', 'fdea199d71b110a3533e55b6f25dee46', 'zzw', '1', '男', 'zzw@qq.com', '');
INSERT INTO `usertable` VALUES ('31', '94012378b72cf8ee6a9259202ff07c29', 'zzww', '1', '女', 'zzww@qq.com', '');
INSERT INTO `usertable` VALUES ('32', 'b09c600fddc573f117449b3723f23d64', 'adm', '2', '男', null, null);
INSERT INTO `usertable` VALUES ('33', 'ee11cbb19052e40b07aac0ca060c23ee', 'user', '1', '男', 'user@123.com', '17634447292');
