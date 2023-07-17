/*
 Navicat Premium Data Transfer

 Source Server         : z-alimy
 Source Server Type    : MySQL
 Source Server Version : 80024
 Source Host           : 120.26.91.154:3306
 Source Schema         : sandwich

 Target Server Type    : MySQL
 Target Server Version : 80024
 File Encoding         : 65001

 Date: 22/08/2022 08:50:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_animal
-- ----------------------------
DROP TABLE IF EXISTS `t_animal`;
CREATE TABLE `t_animal`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '动物名称',
  `age` int(0) NULL DEFAULT NULL COMMENT '动物年龄',
  `color` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '毛发颜色',
  `type_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属种类Id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 557 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '动物信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_animal
-- ----------------------------
INSERT INTO `t_animal` VALUES (545, '111', 212, '321', '321321');
INSERT INTO `t_animal` VALUES (546, '111', 212, '321', NULL);
INSERT INTO `t_animal` VALUES (547, '111', 212, '321', NULL);
INSERT INTO `t_animal` VALUES (548, '111', 212, '321', NULL);
INSERT INTO `t_animal` VALUES (550, '111', 212, '321', NULL);
INSERT INTO `t_animal` VALUES (551, '111', 212, '321', NULL);
INSERT INTO `t_animal` VALUES (552, '111', 212, '321', NULL);
INSERT INTO `t_animal` VALUES (553, '111', 212, '321', NULL);

-- ----------------------------
-- Table structure for t_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `t_dict_data`;
CREATE TABLE `t_dict_data`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id主键',
  `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典数据值',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典类型',
  `status` int(0) NULL DEFAULT 0 COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_dict_data
-- ----------------------------

-- ----------------------------
-- Table structure for t_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `t_dict_type`;
CREATE TABLE `t_dict_type`  (
  `dict_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '子典类型',
  `status` int(0) NULL DEFAULT 0 COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_dict_type
-- ----------------------------
INSERT INTO `t_dict_type` VALUES ('1511990628469334018', '测试字段', '类型A', 0, '', '2022-04-07 16:53:52', '', NULL, 'test');
INSERT INTO `t_dict_type` VALUES ('1511990641119354882', '测试字段', '类型A', 0, '', '2022-04-07 16:53:55', '', NULL, 'test');
INSERT INTO `t_dict_type` VALUES ('1511990644323803138', '测试字段', '类型A', 0, '', '2022-04-07 16:53:56', '', NULL, 'test');
INSERT INTO `t_dict_type` VALUES ('1511990671326732289', '测试字段', '类型A', 0, '', '2022-04-07 16:54:02', '', NULL, 'test');
INSERT INTO `t_dict_type` VALUES ('1511993238744150017', '测试字段', '类型A', 0, '', '2022-04-07 17:04:14', '', NULL, 'test');
INSERT INTO `t_dict_type` VALUES ('1511993353164763138', '测试字段', '类型A', 0, '', '2022-04-07 17:04:42', '', NULL, 'test');
INSERT INTO `t_dict_type` VALUES ('1511993368796934145', '测试字段', '类型A', 0, '', '2022-04-07 17:04:45', '', NULL, 'test');
INSERT INTO `t_dict_type` VALUES ('1511993401667694593', '测试字段', '类型A', 0, '', '2022-04-07 17:04:53', '', NULL, 'test');
INSERT INTO `t_dict_type` VALUES ('1511993435989684226', '测试字段', '类型A', 0, '', '2022-04-07 17:05:01', '', NULL, 'test');
INSERT INTO `t_dict_type` VALUES ('1511993452666236930', '测试字段', '类型A', 0, '', '2022-04-07 17:05:05', '', NULL, 'test');
INSERT INTO `t_dict_type` VALUES ('1511993468361322497', '测试字段', '类型A', 0, '', '2022-04-07 17:05:09', '', NULL, 'test');
INSERT INTO `t_dict_type` VALUES ('1511993483355959298', '测试字段', '类型A', 0, '', '2022-04-07 17:05:13', '', NULL, 'test');
INSERT INTO `t_dict_type` VALUES ('1513705675415556097', '', '', 0, '', '2022-04-12 10:28:51', '', NULL, '');

-- ----------------------------
-- Table structure for t_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission`  (
  `id` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_permission
-- ----------------------------

-- ----------------------------
-- Table structure for t_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_resource`;
CREATE TABLE `t_resource`  (
  `id` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_resource
-- ----------------------------

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `id` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role
-- ----------------------------

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`  (
  `id` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------

-- ----------------------------
-- Table structure for t_view
-- ----------------------------
DROP TABLE IF EXISTS `t_view`;
CREATE TABLE `t_view`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip',
  `area` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地区',
  `view_time` datetime(0) NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_view
-- ----------------------------
INSERT INTO `t_view` VALUES ('1529331111099834369', '访客', '223.104.244.80', NULL, '2022-05-25 13:18:45');
INSERT INTO `t_view` VALUES ('1529334029015547905', '访客', '127.0.0.1', '0|0|0|内网IP|内网IP', '2022-05-25 13:30:21');
INSERT INTO `t_view` VALUES ('1529347988987518977', '访客', '122.224.233.68', NULL, '2022-05-25 14:25:49');
INSERT INTO `t_view` VALUES ('1529348034147590145', '访客', '122.224.233.68', NULL, '2022-05-25 14:26:00');
INSERT INTO `t_view` VALUES ('1529377376319774721', '访客', '122.224.233.68', NULL, '2022-05-25 16:22:36');
INSERT INTO `t_view` VALUES ('1529378284249448449', '访客', '127.0.0.1', '0|0|0|内网IP|内网IP', '2022-05-25 16:26:12');
INSERT INTO `t_view` VALUES ('1529384456778883073', '访客', '223.104.244.80', NULL, '2022-05-25 16:50:44');
INSERT INTO `t_view` VALUES ('1529385272941412353', '访客', '127.0.0.1', NULL, '2022-05-25 16:53:58');
INSERT INTO `t_view` VALUES ('1529386777807319042', '访客', '127.0.0.1', NULL, '2022-05-25 16:59:57');
INSERT INTO `t_view` VALUES ('1529387026403721218', '访客', '127.0.0.1', NULL, '2022-05-25 17:00:56');
INSERT INTO `t_view` VALUES ('1529388712497168386', '访客', '127.0.0.1', '0|0|0|内网IP|内网IP', '2022-05-25 17:07:38');
INSERT INTO `t_view` VALUES ('1529389317638795266', '访客', '127.0.0.1', NULL, '2022-05-25 17:10:03');
INSERT INTO `t_view` VALUES ('1529643592436609026', '访客', '127.0.0.1', NULL, '2022-05-26 10:00:26');
INSERT INTO `t_view` VALUES ('1529644211109031938', '访客', '127.0.0.1', NULL, '2022-05-26 10:02:53');
INSERT INTO `t_view` VALUES ('1529644216146391042', '访客', '0:0:0:0:0:0:0:1', NULL, '2022-05-26 10:02:55');
INSERT INTO `t_view` VALUES ('1529644465418072065', '访客', '0:0:0:0:0:0:0:1', NULL, '2022-05-26 10:03:55');
INSERT INTO `t_view` VALUES ('1529644631755780097', '访客', '192.168.8.68', NULL, '2022-05-26 10:04:34');
INSERT INTO `t_view` VALUES ('1529647427833982978', '访客', '192.168.8.68', '0|0|0|内网IP|内网IP', '2022-05-26 10:15:41');
INSERT INTO `t_view` VALUES ('1529647826267729921', '访客', '192.168.8.68', NULL, '2022-05-26 10:17:16');
INSERT INTO `t_view` VALUES ('1529648886549983233', '访客', '192.168.8.68', NULL, '2022-05-26 10:21:29');
INSERT INTO `t_view` VALUES ('1529649032708894722', '访客', '192.168.8.68', NULL, '2022-05-26 10:22:04');
INSERT INTO `t_view` VALUES ('1529649256940580865', '访客', '192.168.8.68', NULL, '2022-05-26 10:22:57');
INSERT INTO `t_view` VALUES ('1529651576084541442', '访客', '192.168.8.68', NULL, '2022-05-26 10:32:10');
INSERT INTO `t_view` VALUES ('1529651783153135618', '访客', '192.168.8.68', '0|0|0|内网IP|内网IP', '2022-05-26 10:32:59');
INSERT INTO `t_view` VALUES ('1529653284504252417', '访客', '192.168.8.68', NULL, '2022-05-26 10:38:57');
INSERT INTO `t_view` VALUES ('1529653574611632130', '访客', '192.168.8.68', NULL, '2022-05-26 10:40:06');
INSERT INTO `t_view` VALUES ('1529654227270545410', '访客', '192.168.8.68', NULL, '2022-05-26 10:42:42');
INSERT INTO `t_view` VALUES ('1529654591122206722', '访客', '192.168.8.68', NULL, '2022-05-26 10:44:09');
INSERT INTO `t_view` VALUES ('1529657595250479105', '访客', '192.168.8.68', '内网IP', '2022-05-26 10:56:05');
INSERT INTO `t_view` VALUES ('1529657802121981953', '访客', '192.168.8.68', '内网IP', '2022-05-26 10:56:54');
INSERT INTO `t_view` VALUES ('1529658274030276610', '访客', '122.224.233.68', '中国浙江杭州', '2022-05-26 10:58:47');
INSERT INTO `t_view` VALUES ('1529658436144320514', '访客', '122.224.233.68', '中国浙江杭州', '2022-05-26 10:59:26');
INSERT INTO `t_view` VALUES ('1529658758182981634', '访客', '223.104.160.15', '中国浙江', '2022-05-26 11:00:42');
INSERT INTO `t_view` VALUES ('1529700486298456066', '访客', '223.104.160.15', '中国浙江', '2022-05-26 13:46:31');
INSERT INTO `t_view` VALUES ('1529703184965214210', '访客', '0:0:0:0:0:0:0:1', '内网IP', '2022-05-26 13:57:14');
INSERT INTO `t_view` VALUES ('1529703478520356866', '访客', '0:0:0:0:0:0:0:1', '内网IP', '2022-05-26 13:58:24');
INSERT INTO `t_view` VALUES ('1529704286058094593', '访客', '0:0:0:0:0:0:0:1', '内网IP', '2022-05-26 14:01:37');
INSERT INTO `t_view` VALUES ('1529712291708129282', '访客', '122.224.233.68', '中国浙江杭州', '2022-05-26 14:33:26');
INSERT INTO `t_view` VALUES ('1529716548213559298', '访客', '122.224.233.68', '中国浙江杭州', '2022-05-26 14:50:21');
INSERT INTO `t_view` VALUES ('1529716667675725826', '访客', '122.224.233.68', '中国浙江杭州', '2022-05-26 14:50:49');
INSERT INTO `t_view` VALUES ('1529718056804044802', '访客', '171.13.14.76', '中国河南洛阳', '2022-05-26 14:56:20');
INSERT INTO `t_view` VALUES ('1529719095364046850', '访客', '223.104.160.15', '中国浙江', '2022-05-26 15:00:28');
INSERT INTO `t_view` VALUES ('1529719123159699458', '访客', '223.104.160.15', '中国浙江', '2022-05-26 15:00:34');
INSERT INTO `t_view` VALUES ('1529719358585982978', '访客', '223.104.160.15', '中国浙江', '2022-05-26 15:01:31');
INSERT INTO `t_view` VALUES ('1529719483827900417', '访客', '35.207.160.131', '德国法兰克福', '2022-05-26 15:02:00');
INSERT INTO `t_view` VALUES ('1529719486562586625', '访客', '35.207.160.131', '德国法兰克福', '2022-05-26 15:02:01');
INSERT INTO `t_view` VALUES ('1529738997563019265', '访客', '223.104.160.15', '中国浙江', '2022-05-26 16:19:33');
INSERT INTO `t_view` VALUES ('1529744083236311041', '访客', '223.104.160.15', '中国浙江', '2022-05-26 16:39:45');
INSERT INTO `t_view` VALUES ('1529836120610590721', '访客', '36.27.114.218', '中国浙江杭州', '2022-05-26 22:45:29');
INSERT INTO `t_view` VALUES ('1529899041248137217', '访客', '116.179.37.122', '中国山西阳泉', '2022-05-27 02:55:30');
INSERT INTO `t_view` VALUES ('1529899048789495810', '访客', '116.179.37.121', '中国山西阳泉', '2022-05-27 02:55:32');
INSERT INTO `t_view` VALUES ('1529945302340882434', '访客', '66.249.64.140', '美国南卡罗来纳', '2022-05-27 05:59:20');
INSERT INTO `t_view` VALUES ('1529988487658229761', '访客', '122.224.233.68', '中国浙江杭州', '2022-05-27 08:50:56');
INSERT INTO `t_view` VALUES ('1530538487643525121', '访客', '180.163.220.4', '中国上海', '2022-05-28 21:16:26');
INSERT INTO `t_view` VALUES ('1530538612692504577', '访客', '180.163.220.67', '中国上海', '2022-05-28 21:16:56');
INSERT INTO `t_view` VALUES ('1530547339042639874', '访客', '36.27.114.218', '中国浙江杭州', '2022-05-28 21:51:36');
INSERT INTO `t_view` VALUES ('1531092886837153793', '访客', '122.224.233.68', '中国浙江杭州', '2022-05-30 09:59:25');
INSERT INTO `t_view` VALUES ('1531152556444504066', '访客', '122.224.233.68', '中国浙江杭州', '2022-05-30 13:56:32');
INSERT INTO `t_view` VALUES ('1531182532652847105', '访客', '122.224.233.68', '中国浙江杭州', '2022-05-30 15:55:38');
INSERT INTO `t_view` VALUES ('1531443112970178562', '访客', '122.224.233.68', '中国浙江杭州', '2022-05-31 09:11:06');
INSERT INTO `t_view` VALUES ('1531443190514470914', '访客', '122.224.233.68', '中国浙江杭州', '2022-05-31 09:11:24');
INSERT INTO `t_view` VALUES ('1531443275201662977', '访客', '122.224.233.68', '中国浙江杭州', '2022-05-31 09:11:44');
INSERT INTO `t_view` VALUES ('1531532924133724162', '访客', '122.224.233.68', '中国浙江杭州', '2022-05-31 15:07:58');
INSERT INTO `t_view` VALUES ('1531532975300038658', '访客', '122.224.233.68', '中国浙江杭州', '2022-05-31 15:08:10');
INSERT INTO `t_view` VALUES ('1531738991115522049', '访客', '66.249.66.41', '美国南卡罗来纳', '2022-06-01 04:46:48');
INSERT INTO `t_view` VALUES ('1531738995142053889', '访客', '66.249.66.150', '美国南卡罗来纳', '2022-06-01 04:46:49');
INSERT INTO `t_view` VALUES ('1531790267765379073', '访客', '40.77.202.108', '美国华盛顿', '2022-06-01 08:10:34');
INSERT INTO `t_view` VALUES ('1531820676804923394', '访客', '122.224.233.68', '中国浙江杭州', '2022-06-01 10:11:24');
INSERT INTO `t_view` VALUES ('1531898749525053442', '访客', '122.224.233.68', '中国浙江杭州', '2022-06-01 15:21:38');
INSERT INTO `t_view` VALUES ('1532227112466796545', '访客', '122.224.233.68', '中国浙江杭州', '2022-06-02 13:06:26');
INSERT INTO `t_view` VALUES ('1532227184948563969', '访客', '122.224.233.68', '中国浙江杭州', '2022-06-02 13:06:43');
INSERT INTO `t_view` VALUES ('1532488187712000002', '访客', '66.249.68.3', '美国加利福尼亚山景', '2022-06-03 06:23:51');
INSERT INTO `t_view` VALUES ('1532515669068632065', '访客', '173.252.87.23', '美国德克萨斯', '2022-06-03 08:13:03');
INSERT INTO `t_view` VALUES ('1533305990002135041', '访客', '115.197.240.153', '中国浙江杭州', '2022-06-05 12:33:30');
INSERT INTO `t_view` VALUES ('1533623561612054530', '访客', '122.224.233.68', '中国浙江杭州', '2022-06-06 09:35:25');
INSERT INTO `t_view` VALUES ('1533728122242748418', '访客', '122.224.233.68', '中国浙江杭州', '2022-06-06 16:30:54');
INSERT INTO `t_view` VALUES ('1533728197626974209', '访客', '122.224.233.68', '中国浙江杭州', '2022-06-06 16:31:12');
INSERT INTO `t_view` VALUES ('1534085425127116801', '访客', '122.224.233.68', '中国浙江杭州', '2022-06-07 16:10:42');
INSERT INTO `t_view` VALUES ('1535455932376305665', '访客', '66.249.68.29', '美国加利福尼亚山景', '2022-06-11 10:56:36');
INSERT INTO `t_view` VALUES ('1535456178586144769', '访客', '66.249.68.3', '美国加利福尼亚山景', '2022-06-11 10:57:35');
INSERT INTO `t_view` VALUES ('1535707977377271809', '访客', '54.147.108.125', '美国弗吉尼亚阿什本', '2022-06-12 03:38:09');
INSERT INTO `t_view` VALUES ('1550410070545620993', '访客', '122.224.233.68', '中国浙江杭州', '2022-07-22 17:19:01');
INSERT INTO `t_view` VALUES ('1551113494845153281', '访客', '66.249.68.29', '美国加利福尼亚山景', '2022-07-24 15:54:10');
INSERT INTO `t_view` VALUES ('1551136143969312769', '访客', '66.249.68.29', '美国加利福尼亚山景', '2022-07-24 17:24:10');
INSERT INTO `t_view` VALUES ('1551158780862881793', '访客', '66.249.68.29', '美国加利福尼亚山景', '2022-07-24 18:54:07');
INSERT INTO `t_view` VALUES ('1551458079173730305', '访客', '122.224.233.68', '中国浙江杭州', '2022-07-25 14:43:25');
INSERT INTO `t_view` VALUES ('1551625007980830721', '访客', '27.115.124.109', '中国上海', '2022-07-26 01:46:44');
INSERT INTO `t_view` VALUES ('1551625094819700737', '访客', '27.115.124.109', '中国上海', '2022-07-26 01:47:05');
INSERT INTO `t_view` VALUES ('1551625960444354561', '访客', '171.13.14.76', '中国河南洛阳', '2022-07-26 01:50:31');
INSERT INTO `t_view` VALUES ('1551795618149523457', '访客', '122.224.233.68', '中国浙江杭州', '2022-07-26 13:04:41');
INSERT INTO `t_view` VALUES ('1551795673484976130', '访客', '122.224.233.68', '中国浙江杭州', '2022-07-26 13:04:54');
INSERT INTO `t_view` VALUES ('1552062934250643458', '访客', '40.77.190.77', '美国伊利诺伊芝加哥', '2022-07-27 06:46:54');
INSERT INTO `t_view` VALUES ('1552094665901039617', '访客', '27.115.124.97', '中国上海', '2022-07-27 08:53:00');
INSERT INTO `t_view` VALUES ('1552172134461038593', '访客', '123.160.221.29', '中国河南郑州', '2022-07-27 14:00:49');
INSERT INTO `t_view` VALUES ('1552172193109991425', '访客', '123.160.221.31', '中国河南郑州', '2022-07-27 14:01:03');
INSERT INTO `t_view` VALUES ('1552276366715510785', '访客', '66.249.68.3', '美国加利福尼亚山景', '2022-07-27 20:55:00');
INSERT INTO `t_view` VALUES ('1552489937038729217', '访客', '122.224.233.68', '中国浙江杭州', '2022-07-28 11:03:40');
INSERT INTO `t_view` VALUES ('1552538880644734978', '访客', '122.224.233.68', '中国浙江杭州', '2022-07-28 14:18:09');
INSERT INTO `t_view` VALUES ('1552563923655319553', '访客', '122.224.233.68', '中国浙江杭州', '2022-07-28 15:57:39');
INSERT INTO `t_view` VALUES ('1553550240354656258', '访客', '27.115.124.45', '中国上海', '2022-07-31 09:16:56');
INSERT INTO `t_view` VALUES ('1553550335124955137', '访客', '27.115.124.38', '中国上海', '2022-07-31 09:17:18');
INSERT INTO `t_view` VALUES ('1553939575151255553', '访客', '122.224.233.68', '中国浙江杭州', '2022-08-01 11:04:00');
INSERT INTO `t_view` VALUES ('1553997222030336001', '访客', '122.224.233.68', '中国浙江杭州', '2022-08-01 14:53:04');
INSERT INTO `t_view` VALUES ('1554217764658339841', '访客', '1.192.195.11', '中国河南郑州', '2022-08-02 05:29:26');
INSERT INTO `t_view` VALUES ('1554273895120363521', '访客', '122.224.233.68', '中国浙江杭州', '2022-08-02 09:12:28');
INSERT INTO `t_view` VALUES ('1554855060663427073', '访客', '123.160.221.31', '中国河南郑州', '2022-08-03 23:41:49');
INSERT INTO `t_view` VALUES ('1554855079651041281', '访客', '123.160.221.31', '中国河南郑州', '2022-08-03 23:41:53');
INSERT INTO `t_view` VALUES ('1555027448088748033', '访客', '122.224.233.68', '中国浙江杭州', '2022-08-04 11:06:49');
INSERT INTO `t_view` VALUES ('1555027494129623042', '访客', '122.224.233.68', '中国浙江杭州', '2022-08-04 11:07:00');
INSERT INTO `t_view` VALUES ('1555079059846717441', '访客', '122.224.233.68', '中国浙江杭州', '2022-08-04 14:31:54');
INSERT INTO `t_view` VALUES ('1555092733734936578', '访客', '122.224.233.68', '中国浙江杭州', '2022-08-04 15:26:15');
INSERT INTO `t_view` VALUES ('1555431751165140993', '访客', '122.224.233.68', '中国浙江杭州', '2022-08-05 13:53:23');
INSERT INTO `t_view` VALUES ('1556208732538396674', '访客', '40.77.189.96', '美国伊利诺伊芝加哥', '2022-08-07 17:20:49');
INSERT INTO `t_view` VALUES ('1556324282975379457', '访客', '66.249.68.3', '美国加利福尼亚山景', '2022-08-08 00:59:59');
INSERT INTO `t_view` VALUES ('1556417605178220546', '访客', '66.249.68.29', '美国加利福尼亚山景', '2022-08-08 07:10:49');
INSERT INTO `t_view` VALUES ('1556617935283281922', '访客', '27.115.124.69', '中国上海', '2022-08-08 20:26:51');
INSERT INTO `t_view` VALUES ('1557236344735916034', '访客', '123.160.221.28', '中国河南郑州', '2022-08-10 13:24:11');
INSERT INTO `t_view` VALUES ('1557461839092113410', '访客', '66.249.68.29', '美国加利福尼亚山景', '2022-08-11 04:20:13');
INSERT INTO `t_view` VALUES ('1557504190791540738', '访客', '152.136.165.44', '中国北京', '2022-08-11 07:08:31');
INSERT INTO `t_view` VALUES ('1557884877356916738', '访客', '1.192.195.5', '中国河南郑州', '2022-08-12 08:21:14');
INSERT INTO `t_view` VALUES ('1558419613884080129', '访客', '220.173.208.212', '中国广西南宁', '2022-08-13 19:46:05');
INSERT INTO `t_view` VALUES ('1558463252538580994', '访客', '1.192.195.8', '中国河南郑州', '2022-08-13 22:39:29');
INSERT INTO `t_view` VALUES ('1558473275062870017', '访客', '66.249.68.3', '美国加利福尼亚山景', '2022-08-13 23:19:18');
INSERT INTO `t_view` VALUES ('1559001962116014082', '访客', '122.224.233.68', '中国浙江杭州', '2022-08-15 10:20:07');
INSERT INTO `t_view` VALUES ('1559091006493679618', '访客', '27.115.124.41', '中国上海', '2022-08-15 16:13:57');
INSERT INTO `t_view` VALUES ('1559255937377624065', '访客', '123.160.221.24', '中国河南郑州', '2022-08-16 03:09:20');
INSERT INTO `t_view` VALUES ('1559644062260125697', '访客', '27.115.124.2', '中国上海', '2022-08-17 04:51:36');
INSERT INTO `t_view` VALUES ('1560212285607108610', '访客', '27.115.124.104', '中国上海', '2022-08-18 18:29:31');
INSERT INTO `t_view` VALUES ('1560613765593948162', '访客', '123.160.221.28', '中国河南郑州', '2022-08-19 21:04:51');
INSERT INTO `t_view` VALUES ('1560613792588488705', '访客', '123.160.221.27', '中国河南郑州', '2022-08-19 21:04:58');
INSERT INTO `t_view` VALUES ('1560949853567049729', '访客', '123.160.221.28', '中国河南郑州', '2022-08-20 19:20:21');
INSERT INTO `t_view` VALUES ('1560949936433913857', '访客', '123.160.221.28', '中国河南郑州', '2022-08-20 19:20:41');
INSERT INTO `t_view` VALUES ('1561416182879215618', '访客', '66.249.68.7', '美国加利福尼亚山景', '2022-08-22 02:13:22');
INSERT INTO `t_view` VALUES ('1561416183848099842', '访客', '66.249.68.7', '美国加利福尼亚山景', '2022-08-22 02:13:23');

-- ----------------------------
-- Table structure for user_innodb
-- ----------------------------
DROP TABLE IF EXISTS `user_innodb`;
CREATE TABLE `user_innodb`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `age` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_age`(`age`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_innodb
-- ----------------------------
INSERT INTO `user_innodb` VALUES (1, 'grgd', 9);
INSERT INTO `user_innodb` VALUES (2, 'htyr', 10);

SET FOREIGN_KEY_CHECKS = 1;
