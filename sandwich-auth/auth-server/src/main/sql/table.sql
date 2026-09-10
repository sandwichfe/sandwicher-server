/*
 Navicat Premium Data Transfer

 Source Server         : txyun
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : 49.235.149.110:3306
 Source Schema         : sandwich

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 22/02/2025 23:07:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_authority
-- ----------------------------
DROP TABLE IF EXISTS `t_authority`;
CREATE TABLE `t_authority`  (
                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单自增ID',
                                `name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '菜单名称',
                                `menu_pid` bigint NOT NULL COMMENT '父菜单ID',
                                `url` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '跳转URL',
                                `authority` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '所需权限',
                                `sort` tinyint NULL DEFAULT NULL COMMENT '排序',
                                `type` tinyint NOT NULL COMMENT '0:菜单,1:接口',
                                `deleted` tinyint(1) NOT NULL COMMENT '0:启用,1:删除',
                                `create_time` datetime NOT NULL COMMENT '创建时间',
                                `create_user_id` bigint NOT NULL COMMENT '创建人',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_authority
-- ----------------------------
INSERT INTO `t_authority` VALUES (1, '系统管理', 0, '/system', 'system', 0, 0, 0, '2022-03-25 23:52:03', 1);
INSERT INTO `t_authority` VALUES (2, 'app', 0, '/**', 'app', 1, 1, 0, '2023-06-20 15:18:49', 1);
INSERT INTO `t_authority` VALUES (3, 'web', 0, '/**', 'web', 2, 1, 0, '2023-06-20 15:19:12', 1);

-- ----------------------------
-- Table structure for t_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `t_dict_data`;
DROP TABLE IF EXISTS `t_dict_item`;
CREATE TABLE `t_dict_item`  (
                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典项自增ID',
                                `dict_type_id` bigint NOT NULL COMMENT '字典类型ID',
                                `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '展示标签',
                                `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典值',
                                `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
                                `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
                                `sort` int NULL DEFAULT 0 COMMENT '显示顺序',
                                `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志（0存在 1删除）',
                                PRIMARY KEY (`id`) USING BTREE,
                                INDEX `idx_dict_type_id` (`dict_type_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典项表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_dict_item
-- ----------------------------

-- ----------------------------
-- Table structure for t_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `t_dict_type`;
CREATE TABLE `t_dict_type`  (
                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典类型自增ID',
                                `type_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型编码',
                                `type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型名称',
                                `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                `create_by` bigint NULL DEFAULT NULL COMMENT '创建人',
                                `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                `update_by` bigint NULL DEFAULT NULL COMMENT '更新人',
                                `sort` int NULL DEFAULT 0 COMMENT '显示顺序',
                                `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志（0存在 1删除）',
                                PRIMARY KEY (`id`) USING BTREE,
                                INDEX `idx_type_code` (`type_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_dict_type
-- ----------------------------
-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '角色名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '角色描述',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '角色信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES (1, 'admin', '管理员', '2023-06-20 15:20:42', '2023-06-20 15:20:42');

-- ----------------------------
-- Table structure for t_role_authority
-- ----------------------------
DROP TABLE IF EXISTS `t_role_authority`;
CREATE TABLE `t_role_authority`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色菜单关联表自增ID',
                                     `role_id` bigint NOT NULL COMMENT '角色ID',
                                     `authority_id` bigint NOT NULL COMMENT '权限菜单ID',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单多对多关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role_authority
-- ----------------------------
INSERT INTO `t_role_authority` VALUES (1, 1, 1);
INSERT INTO `t_role_authority` VALUES (2, 1, 2);
INSERT INTO `t_role_authority` VALUES (3, 1, 3);

-- ----------------------------
-- Table structure for t_third_account
-- ----------------------------
DROP TABLE IF EXISTS `t_third_account`;
CREATE TABLE `t_third_account`  (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id',
                                    `user_id` bigint NULL DEFAULT NULL COMMENT '用户表主键',
                                    `unique_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '三方登录唯一id',
                                    `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '三方登录类型',
                                    `blog` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '博客地址',
                                    `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '地址',
                                    `create_time` datetime NULL DEFAULT NULL COMMENT '绑定时间',
                                    `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '三方登录账户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_third_account
-- ----------------------------

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户名、昵称',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '密码',
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '邮箱',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '头像地址',
  `deleted` tinyint(1) NULL DEFAULT NULL COMMENT '是否已删除',
  `source_from` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户来源',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '基础用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, '云逸', 'admin', '$2a$10$K7nVcC.75YZSZU1Fq6G6buYujG.dolGYGPboh7eQbtkdFmB0EfN5K', '17683906991', '17683906991@163.com', NULL, 0, 'system', '2023-06-20 15:20:42', '2023-06-20 15:20:42');

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`  (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `role_id` bigint NULL DEFAULT NULL COMMENT '角色ID',
                                `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
                                PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES (1, 1, 1);

-- ----------------------------
-- Table structure for t_view
-- ----------------------------
DROP TABLE IF EXISTS `t_view`;
CREATE TABLE `t_view`  (
                           `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
                           `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
                           `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip',
                           `area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地区',
                           `view_time` datetime NULL DEFAULT NULL COMMENT '访问时间',
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_view
-- ----------------------------
INSERT INTO `t_view` VALUES ('1608002339413733377', '访客', '180.101.244.15', '中国江苏南京', '2022-12-28 15:30:08');
INSERT INTO `t_view` VALUES ('1608042528517435394', '访客', '59.83.208.105', '中国江苏南京', '2022-12-28 18:09:49');
INSERT INTO `t_view` VALUES ('1623297056208629761', '访客', '220.196.160.154', '中国上海', '2023-02-08 20:25:52');

SET FOREIGN_KEY_CHECKS = 1;
