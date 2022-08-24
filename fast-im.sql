/*
 Navicat Premium Data Transfer

 Source Server         : ETI
 Source Server Type    : MySQL
 Source Server Version : 50651
 Source Host           : localhost:3306
 Source Schema         : fast-im

 Target Server Type    : MySQL
 Target Server Version : 50651
 File Encoding         : 65001

 Date: 22/08/2022 13:48:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_conversation_info
-- ----------------------------
DROP TABLE IF EXISTS `t_conversation_info`;
CREATE TABLE `t_conversation_info`  (
  `id` bigint(20) NOT NULL COMMENT '雪花ID',
  `send_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发送人ID',
  `nick_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收人昵称',
  `ex` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户扩展类 使用JSON字符串',
  `face_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `form_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接收人ID',
  `last_msg_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息ID',
  `last_time` bigint(13) NULL DEFAULT NULL COMMENT '最后消息发送时间',
  `last_type` int(1) NULL DEFAULT NULL COMMENT '会话类型 类型 1-私聊 2-群聊',
  `unread_count` int(11) NOT NULL DEFAULT 0 COMMENT '未读数 默认0',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_conversation_info
-- ----------------------------
INSERT INTO `t_conversation_info` VALUES (1561079229075795970, '1', NULL, NULL, NULL, '2', '1561397404908736514', 1661101125349, 1, 60265, '2022-08-21 03:54:26', '2022-08-22 00:58:45');

-- ----------------------------
-- Table structure for t_message_info
-- ----------------------------
DROP TABLE IF EXISTS `t_message_info`;
CREATE TABLE `t_message_info`  (
  `id` bigint(20) NOT NULL COMMENT '雪花ID',
  `send_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发送人ID',
  `form_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收人ID',
  `group_id` bigint(20) NULL DEFAULT NULL COMMENT '群ID',
  `type` int(1) NOT NULL COMMENT '类型 1-私聊 2-群聊',
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发送内容',
  `msg_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息ID',
  `msg_time` bigint(13) NOT NULL COMMENT '时间戳',
  `msg_status` int(1) NOT NULL COMMENT '发送状态 0-失败 1-离线 2-成功 ',
  `view_status` int(1) NOT NULL COMMENT '读取状态 0-未读  1-已读',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_message_info
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_info
-- ----------------------------
DROP TABLE IF EXISTS `t_user_info`;
CREATE TABLE `t_user_info`  (
  `id` bigint(20) NOT NULL COMMENT '雪花ID',
  `user_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID（必须唯一）',
  `nick_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `face_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `ex` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户扩展类 使用JSON字符串',
  `status` int(1) NOT NULL COMMENT '0-黑名单 1-正常',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_user_info
-- ----------------------------
INSERT INTO `t_user_info` VALUES (1561011049351475202, '1', '测试用户1', 'https://thirdwx.qlogo.cn/mmopen/vi_32/KU3mW0MNM04SHMRLiaicMBEQianSUNoZvKrFksBR1yFiclATuA1RejzXFoxbnCO9icicBiayrsWfQqVT24IjGJPdwXHkw/132', NULL, 1, '2022-08-20 23:23:31', '2022-08-20 23:23:31');
INSERT INTO `t_user_info` VALUES (1561011368525426689, '2', '会话2', 'https://thirdwx.qlogo.cn/mmopen/vi_32/nG7f0M5zSkYWnKlm9wuOb1VClrPIugH031T5K3ZiceJRtlsicF25XVsSqZwqseTdQRdzOo75Vp7sY0FTK4Yicic6wA/132', NULL, 1, '2022-08-20 23:24:47', '2022-08-20 23:24:47');

SET FOREIGN_KEY_CHECKS = 1;
