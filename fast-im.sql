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

 Date: 25/08/2022 05:47:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_chat_message
-- ----------------------------
DROP TABLE IF EXISTS `t_chat_message`;
CREATE TABLE `t_chat_message`  (
  `id` bigint(20) NOT NULL COMMENT '雪花ID',
  `type` int(5) NOT NULL COMMENT '类型：0-文字 1-图片 2-视频 3-卡片数据 4-订单数据   101-送达消息 102-已读消息 103-心跳 104-客户端ACK 105-服务端ACK',
  `come` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发送者ID',
  `go` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收者ID（私聊必填）',
  `group_go` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '群聊ID（群聊必填）',
  `mode` int(1) NOT NULL COMMENT '类型 0-私聊 1-群聊',
  `msg` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息内容 （文字消息）',
  `msg_data` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'JSON字符串 除了文字消息，其他数据都会装入到该参数中',
  `msg_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息ID  由前端生成UUID 保证唯一值',
  `msg_time` bigint(13) NOT NULL COMMENT '发送时间  由前端获取当前时间戳',
  `msg_status` int(1) NOT NULL COMMENT '消息状态 0-失败 1-已送达 2-已接收未读 3-已收到已读 4-离线消息',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_chat_message
-- ----------------------------
INSERT INTO `t_chat_message` VALUES (1562532764317499393, 0, '1', '2', NULL, 0, '你好~ ', NULL, 'f61d42e4-de0c-49d4-8418-c069de010984', 1661371816135, 1, '2022-08-25 04:10:16');
INSERT INTO `t_chat_message` VALUES (1562532790896803842, 0, '2', '1', NULL, 0, '嗯呢~', NULL, '9d276545-2c66-4712-a17e-aefb1ffd8223', 1661371822463, 1, '2022-08-25 04:10:22');
INSERT INTO `t_chat_message` VALUES (1562533212206149634, 0, '1', '2', NULL, 0, '嗯呢~测试下loadding', NULL, 'b00d6abb-02f9-4bbe-bf89-7a584589aff2', 1661371916897, 1, '2022-08-25 04:12:03');
INSERT INTO `t_chat_message` VALUES (1562538706006192130, 0, '1', '2', NULL, 0, '2222', NULL, '9bdff879-8d83-4480-917a-ce6ec5aba2dd', 1661373232715, 3, '2022-08-25 04:33:53');
INSERT INTO `t_chat_message` VALUES (1562538833504645121, 0, '1', '2', NULL, 0, '3333', NULL, '3a9ec7f9-52c8-4fb8-8581-917e5d872948', 1661373263144, 3, '2022-08-25 04:34:23');
INSERT INTO `t_chat_message` VALUES (1562539509450289153, 0, '2', '1', NULL, 0, 'hhhhhhh', NULL, '8893a224-7874-4e84-a785-456b647cce47', 1661373424300, 1, '2022-08-25 04:37:04');
INSERT INTO `t_chat_message` VALUES (1562539617160015874, 0, '1', '2', NULL, 0, '嗯呢', NULL, 'd39c38a4-1da0-4524-b6b9-1d7a92735246', 1661373449980, 1, '2022-08-25 04:37:30');
INSERT INTO `t_chat_message` VALUES (1562539669710450690, 0, '2', '1', NULL, 0, '是嘛', NULL, '87f33e53-ca4c-44ac-a5ed-12ce2fd47932', 1661373462499, 1, '2022-08-25 04:37:43');

-- ----------------------------
-- Table structure for t_conversation_info
-- ----------------------------
DROP TABLE IF EXISTS `t_conversation_info`;
CREATE TABLE `t_conversation_info`  (
  `id` bigint(20) NOT NULL COMMENT '雪花ID',
  `send_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发送人ID',
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
INSERT INTO `t_conversation_info` VALUES (1561079229075795970, '1', '2', '1562539669710450690', 1661373462499, 0, 60287, '2022-08-21 03:54:26', '2022-08-25 04:37:43');

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
