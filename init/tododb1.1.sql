/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50612 (5.6.12-enterprise-commercial-advanced)
 Source Host           : localhost:3306
 Source Schema         : tododb

 Target Server Type    : MySQL
 Target Server Version : 50612 (5.6.12-enterprise-commercial-advanced)
 File Encoding         : 65001

 Date: 09/06/2025 16:46:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for hibernate_sequence
-- ----------------------------
DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence`  (
  `next_val` bigint(20) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of hibernate_sequence
-- ----------------------------
INSERT INTO `hibernate_sequence` VALUES (34);

-- ----------------------------
-- Table structure for todo_item
-- ----------------------------
DROP TABLE IF EXISTS `todo_item`;
CREATE TABLE `todo_item`  (
  `id` bigint(20) NOT NULL,
  `category` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `complete` bit(1) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `user_id` bigint(11) NULL DEFAULT NULL,
  `uid` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  INDEX `FKrsicsyxd8jle3un25vixe965j`(`uid`) USING BTREE,
  CONSTRAINT `FKrsicsyxd8jle3un25vixe965j` FOREIGN KEY (`uid`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of todo_item
-- ----------------------------
INSERT INTO `todo_item` VALUES (1, 'test', b'1', '测试1', 0, NULL);
INSERT INTO `todo_item` VALUES (2, 'test', b'1', '测试2', 0, NULL);
INSERT INTO `todo_item` VALUES (6, 'ttt', b'1', 'test', 0, NULL);
INSERT INTO `todo_item` VALUES (7, 'ttt', b'1', 'test', 0, NULL);
INSERT INTO `todo_item` VALUES (10, 'aaa', b'1', 'posttest', 9, NULL);
INSERT INTO `todo_item` VALUES (14, 'aaa', b'0', 'posttest', 9, NULL);
INSERT INTO `todo_item` VALUES (15, 'aaa', b'0', 'updatetest', 9, NULL);
INSERT INTO `todo_item` VALUES (16, 'test', b'0', 'testaddpost', 9, NULL);
INSERT INTO `todo_item` VALUES (17, 'test', b'0', 'WebAddTest', 9, NULL);
INSERT INTO `todo_item` VALUES (19, 'test', b'0', '注册用户', 18, NULL);
INSERT INTO `todo_item` VALUES (20, 'notest', b'1', '再注册一次', 18, NULL);
INSERT INTO `todo_item` VALUES (21, 'test', b'1', 'updatetest', 8, NULL);
INSERT INTO `todo_item` VALUES (23, '124', b'1', '测试一下添加任务功能', 18, NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `role` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  `deleter_id` bigint(11) NULL DEFAULT NULL COMMENT '删除者ID（外键？见下文说明）',
  `deleted` bit(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (0, 'test', '123456', 'admin', b'0', NULL, b'0');
INSERT INTO `user` VALUES (8, 'updatedUser', '654321', 'user', b'0', NULL, b'0');
INSERT INTO `user` VALUES (9, 'testb', '123456', 'admin', b'0', NULL, b'0');
INSERT INTO `user` VALUES (18, 'reguser', '123456', 'user', b'0', NULL, b'0');
INSERT INTO `user` VALUES (22, 'retest', '123456', 'user', b'0', NULL, b'0');
INSERT INTO `user` VALUES (24, 'newUser', '123456', 'user', b'1', NULL, b'0');
INSERT INTO `user` VALUES (33, 'yy', '1', 'user', b'1', 9, NULL);

SET FOREIGN_KEY_CHECKS = 1;
