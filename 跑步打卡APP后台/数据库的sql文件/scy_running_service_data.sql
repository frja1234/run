/*
 Navicat Premium Data Transfer

 Source Server         : Ubuntu_Docker_Mysql_Server
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : 192.168.232.131:3306
 Source Schema         : scy_running_service

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 25/08/2021 15:31:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_class
-- ----------------------------
DROP TABLE IF EXISTS `tb_class`;
CREATE TABLE `tb_class`  (
  `class_id` int NOT NULL AUTO_INCREMENT COMMENT '班级id',
  `class_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '班级名称',
  `class_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '班级代码',
  `college_id` int NULL DEFAULT NULL COMMENT '学院id',
  PRIMARY KEY (`class_id`) USING BTREE,
  INDEX `FK_COLLEGE_CLASS_02`(`college_id`) USING BTREE,
  CONSTRAINT `FK_COLLEGE_CLASS_02` FOREIGN KEY (`college_id`) REFERENCES `tb_college` (`college_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_class
-- ----------------------------
INSERT INTO `tb_class` VALUES (1, '2021软件3班', '202113', 2);
INSERT INTO `tb_class` VALUES (3, '2021软件2班', '202112', 2);
INSERT INTO `tb_class` VALUES (4, '2020计算机1班', '202021', 1);
INSERT INTO `tb_class` VALUES (5, '2020计算机2班', '202022', 1);
INSERT INTO `tb_class` VALUES (6, '2020计算机3班', '202023', 1);
INSERT INTO `tb_class` VALUES (7, '2020马克思1班', '202051', 3);
INSERT INTO `tb_class` VALUES (8, '2020马克思2班', '202052', 3);
INSERT INTO `tb_class` VALUES (9, '2020马克思3班', '202053', 3);

-- ----------------------------
-- Table structure for tb_college
-- ----------------------------
DROP TABLE IF EXISTS `tb_college`;
CREATE TABLE `tb_college`  (
  `college_id` int NOT NULL AUTO_INCREMENT COMMENT '学院id',
  `college_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '学院名称',
  `college_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '学院代码',
  `college_introduction` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学院简介',
  PRIMARY KEY (`college_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_college
-- ----------------------------
INSERT INTO `tb_college` VALUES (1, '计算机学院', '23', '学院有本科、硕士到博士（后）完整的人才培养体系。拥有软件工程博士后科研流动站，软件工程一级学科博士点，计算机科学与技术一级学科硕士点和电子信息专业学位工程硕士授权点（计算机技术、软件工程、人工智能、大数据技术与工程、网络与信息安全等五个领域）；目前招生计算机科学与技术（含师范和非师范）、网络工程、人工智能等本科专业。其中，计算机科学与技术是国家级一流本科专业建设点，广东省首批IT名牌专业和省特色专业，网络工程是广东省特色专业，软件工程是广东省高等学校“珠江学者”设岗学科和广州市教育局重点学科。');
INSERT INTO `tb_college` VALUES (2, '软件学院', '24', '软件学院现在开设软件工程、软件工程（中外联合培养）等2个本科专业，并招收电子信息（软件工程专业方向）专业学位硕士研究生。2019年软件工程专业获批广东省重点专业，软件工程专业是华南师范大学第一个顺利通过IEET国际教育认证的专业，软件工程专业是全国首批“国家一流本科专业”。');
INSERT INTO `tb_college` VALUES (3, '马克思主义学院', '03', '华南师范大学马克思主义学院，起始于1957年建立的华南师范大学马列主义教研室，改革开放后发展成为思想教育管理学系（思教系），2000年整合德育教研室成立思想政治理论课教学部，2012年正式挂牌成立马克思主义学院。学院主要负责全校本科生、研究生思想政治理论课的教学、马克思主义理论学科建设，承担马克思主义理论博士生、硕士生、本科生和思想政治教育（师范）本科生的人才培养任务。');
INSERT INTO `tb_college` VALUES (4, '环境学院', '12', '环境学院拥有环境科学、环境工程两个本科专业，环境科学与工程一级硕士点、生态学一级博士点、环境化学二级博士点和环境科学与工程一级学科博士后流动站，拥有学士、硕士、博士到博士后的完整人才培养体系。现有在读本科生413人、硕士生114人、博士生24人。华南师范大学环境学院环境科学与生态学学科入选ESI全球前1%，工程学进入ESI全球前5‰。软科发布2020“软科世界一流学科排名”，华南师范大学环境学院相关的上榜学科有环境科学与工程、化学工程。');
INSERT INTO `tb_college` VALUES (5, '经济与管理学院', '33', '学院现设经济学、金融学、国际经济与贸易、工商管理、人力资源管理、会计学、电子商务、信息管理学等8个学系，设有经济研究所和马克思主义经济学研究中心、经济行为科学重点实验室3个实体研究机构；还拥有MBA教育中心、经济学和管理学省级实验教学示范中心。');

-- ----------------------------
-- Table structure for tb_permission
-- ----------------------------
DROP TABLE IF EXISTS `tb_permission`;
CREATE TABLE `tb_permission`  (
  `permission_id` int NOT NULL AUTO_INCREMENT COMMENT '权限id',
  `permission_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限名称',
  `permission_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '资源路径',
  `create_time` date NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` int NULL DEFAULT NULL COMMENT '创建人',
  `update_time` date NULL DEFAULT NULL COMMENT '修改时间',
  `update_user` int NULL DEFAULT NULL COMMENT '修改人',
  `parent_permission_id` int NULL DEFAULT NULL COMMENT '父级权限id',
  `def_flag` int NOT NULL COMMENT '删除标志',
  PRIMARY KEY (`permission_id`) USING BTREE,
  INDEX `FK_Reference_7`(`parent_permission_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 76 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_permission
-- ----------------------------
INSERT INTO `tb_permission` VALUES (1, '用户管理', '/tb-user', '2021-07-12', 1, '2021-08-24', 1, NULL, 0);
INSERT INTO `tb_permission` VALUES (41, '系统管理', '/tb-sys', NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `tb_permission` VALUES (42, '角色管理', '/tb-sys/tb-role', NULL, NULL, NULL, NULL, 41, 0);
INSERT INTO `tb_permission` VALUES (43, '权限管理', '/tb-sys/tb-permission', NULL, NULL, NULL, NULL, 41, 0);
INSERT INTO `tb_permission` VALUES (44, '查看权限', '/tb-sys/tb-permission/index', NULL, NULL, NULL, NULL, 43, 0);
INSERT INTO `tb_permission` VALUES (45, '新增权限', '/tb-sys/tb-permission/insert', NULL, NULL, NULL, NULL, 43, 0);
INSERT INTO `tb_permission` VALUES (46, '修改权限', '/tb-sys/tb-permission/update', NULL, NULL, NULL, NULL, 43, 0);
INSERT INTO `tb_permission` VALUES (47, '查看角色', '/tb-sys/tb-role/index', NULL, NULL, NULL, NULL, 42, 0);
INSERT INTO `tb_permission` VALUES (48, '新增角色', '/tb-sys/tb-role/insert', NULL, NULL, NULL, NULL, 42, 0);
INSERT INTO `tb_permission` VALUES (49, '修改角色', '/tb-sys/tb-role/update', NULL, NULL, NULL, NULL, 42, 0);
INSERT INTO `tb_permission` VALUES (50, '角色授权', '/tb-sys/tb-role/grant', NULL, NULL, NULL, NULL, 42, 0);
INSERT INTO `tb_permission` VALUES (51, '学生管理', '/tb-user/student', NULL, NULL, NULL, NULL, 1, 0);
INSERT INTO `tb_permission` VALUES (52, '教师管理', '/tb-user/teacher', NULL, NULL, NULL, NULL, 1, 0);
INSERT INTO `tb_permission` VALUES (53, '管理员管理', '/tb-user/admin', NULL, NULL, NULL, NULL, 1, 0);
INSERT INTO `tb_permission` VALUES (54, '查看学生', '/tb-user/student/index', NULL, NULL, NULL, NULL, 51, 0);
INSERT INTO `tb_permission` VALUES (55, '新增学生', '/tb-user/student/insert', NULL, NULL, NULL, NULL, 51, 0);
INSERT INTO `tb_permission` VALUES (56, '修改学生', '/tb-user/student/update', NULL, NULL, NULL, NULL, 51, 0);
INSERT INTO `tb_permission` VALUES (57, '查看教师', '/tb-user/teacher/index', NULL, NULL, NULL, NULL, 52, 0);
INSERT INTO `tb_permission` VALUES (58, '新增教师', '/tb-user/teacher/insert', NULL, NULL, NULL, NULL, 52, 0);
INSERT INTO `tb_permission` VALUES (59, '修改教师', '/tb-user/teacher/update', NULL, NULL, NULL, NULL, 52, 0);
INSERT INTO `tb_permission` VALUES (60, '查看管理员', '/tb-user/admin/index', NULL, NULL, NULL, NULL, 53, 0);
INSERT INTO `tb_permission` VALUES (61, '新增管理员', '/tb-user/admin/insert', NULL, NULL, NULL, NULL, 53, 0);
INSERT INTO `tb_permission` VALUES (62, '修改管理员', '/tb-user/admin/update', NULL, NULL, NULL, NULL, 53, 0);
INSERT INTO `tb_permission` VALUES (63, '教研机构管理', '/tb-edu', NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `tb_permission` VALUES (64, '学院管理', '/tb-edu/tb-college', NULL, NULL, '2021-08-24', NULL, 63, 0);
INSERT INTO `tb_permission` VALUES (65, '班级管理', '/tb-edu/tb-class', NULL, NULL, NULL, NULL, 63, 0);
INSERT INTO `tb_permission` VALUES (66, '查询学院', '/tb-edu/tb-college/index', NULL, NULL, NULL, NULL, 64, 0);
INSERT INTO `tb_permission` VALUES (67, '新增学院', '/tb-edu/tb-college/insert', NULL, NULL, NULL, NULL, 64, 0);
INSERT INTO `tb_permission` VALUES (68, '修改学院', '/tb-edu/tb-college/update', NULL, NULL, NULL, NULL, 64, 0);
INSERT INTO `tb_permission` VALUES (69, '查看班级', '/tb-edu/tb-class/index', NULL, NULL, NULL, NULL, 65, 0);
INSERT INTO `tb_permission` VALUES (70, '新增班级', '/tb-edu/tb-class/insert', NULL, NULL, NULL, NULL, 65, 0);
INSERT INTO `tb_permission` VALUES (71, '修改班级', '/tb-edu/tb-class/update', NULL, NULL, NULL, NULL, 65, 0);
INSERT INTO `tb_permission` VALUES (72, '打卡任务管理', '/tb-task', NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `tb_permission` VALUES (73, '查看打卡任务', '/tb-task/index', NULL, NULL, NULL, NULL, 72, 0);
INSERT INTO `tb_permission` VALUES (74, '新增打卡任务(发布新任务)', '/tb-task/insert', NULL, NULL, NULL, NULL, 72, 0);
INSERT INTO `tb_permission` VALUES (75, '修改打卡任务', '/tb-task/update', NULL, NULL, NULL, NULL, 72, 0);

-- ----------------------------
-- Table structure for tb_punch_clock
-- ----------------------------
DROP TABLE IF EXISTS `tb_punch_clock`;
CREATE TABLE `tb_punch_clock`  (
  `punch_clock_id` int NOT NULL AUTO_INCREMENT COMMENT '打卡id',
  `user_id` int NULL DEFAULT NULL COMMENT '打卡人(学生id)',
  `run_total_length` int NOT NULL COMMENT '累积总路程',
  `punch_clock_state` int NOT NULL COMMENT '打卡状态',
  `run_total_time` int NULL DEFAULT NULL COMMENT '累积总时长',
  `punch_clock_start_time` datetime NULL DEFAULT NULL COMMENT '打卡开始时间',
  `punch_clock_end_time` datetime NULL DEFAULT NULL COMMENT '打卡结束时间',
  `create_time` date NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` int NULL DEFAULT NULL COMMENT '创建人',
  `update_time` date NULL DEFAULT NULL COMMENT '修改时间',
  `update_user` int NULL DEFAULT NULL COMMENT '修改人',
  `task_id` int NULL DEFAULT NULL COMMENT '所属任务id',
  PRIMARY KEY (`punch_clock_id`) USING BTREE,
  INDEX `FK_Reference_5`(`user_id`) USING BTREE,
  INDEX `FK_Reference_Task_Punch_Clock_1`(`task_id`) USING BTREE,
  CONSTRAINT `FK_Reference_5` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_Reference_Task_Punch_Clock_1` FOREIGN KEY (`task_id`) REFERENCES `tb_task` (`task_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_punch_clock
-- ----------------------------
INSERT INTO `tb_punch_clock` VALUES (5, 8, 41, 0, 0, NULL, NULL, NULL, NULL, '2021-08-23', NULL, 6);
INSERT INTO `tb_punch_clock` VALUES (18, 8, 0, 0, 0, NULL, NULL, NULL, NULL, NULL, NULL, 10);

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role`  (
  `role_id` int NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `create_time` date NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` int NULL DEFAULT NULL COMMENT '创建人',
  `update_time` date NULL DEFAULT NULL COMMENT '修改时间',
  `update_user` int NULL DEFAULT NULL COMMENT '修改人',
  `role_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色代码',
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `def_flag` int NOT NULL COMMENT '删除标志',
  `token` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色令牌',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` VALUES (1, '管理员', '2021-07-08', 1, '2021-07-08', 1, 'A001', '系统管理员', 0, 'admin');
INSERT INTO `tb_role` VALUES (2, '学生', '2021-07-08', 1, '2021-07-08', 1, 'S001', '学生用户', 0, 'student');
INSERT INTO `tb_role` VALUES (3, '教师', '2021-07-08', 1, '2021-07-08', 1, 'T001', '教师用户', 0, 'teacher');

-- ----------------------------
-- Table structure for tb_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_permission`;
CREATE TABLE `tb_role_permission`  (
  `role_id` int NOT NULL COMMENT '角色id',
  `permission_id` int NOT NULL COMMENT '权限id',
  PRIMARY KEY (`role_id`, `permission_id`) USING BTREE,
  INDEX `FK_Reference_3`(`permission_id`) USING BTREE,
  CONSTRAINT `FK_Reference_2` FOREIGN KEY (`role_id`) REFERENCES `tb_role` (`role_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_Reference_3` FOREIGN KEY (`permission_id`) REFERENCES `tb_permission` (`permission_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role_permission
-- ----------------------------
INSERT INTO `tb_role_permission` VALUES (2, 1);
INSERT INTO `tb_role_permission` VALUES (1, 44);
INSERT INTO `tb_role_permission` VALUES (1, 45);
INSERT INTO `tb_role_permission` VALUES (1, 46);
INSERT INTO `tb_role_permission` VALUES (1, 47);
INSERT INTO `tb_role_permission` VALUES (1, 48);
INSERT INTO `tb_role_permission` VALUES (1, 49);
INSERT INTO `tb_role_permission` VALUES (1, 50);
INSERT INTO `tb_role_permission` VALUES (1, 54);
INSERT INTO `tb_role_permission` VALUES (3, 54);
INSERT INTO `tb_role_permission` VALUES (1, 55);
INSERT INTO `tb_role_permission` VALUES (3, 55);
INSERT INTO `tb_role_permission` VALUES (1, 56);
INSERT INTO `tb_role_permission` VALUES (3, 56);
INSERT INTO `tb_role_permission` VALUES (1, 57);
INSERT INTO `tb_role_permission` VALUES (3, 57);
INSERT INTO `tb_role_permission` VALUES (1, 58);
INSERT INTO `tb_role_permission` VALUES (3, 58);
INSERT INTO `tb_role_permission` VALUES (1, 59);
INSERT INTO `tb_role_permission` VALUES (3, 59);
INSERT INTO `tb_role_permission` VALUES (1, 60);
INSERT INTO `tb_role_permission` VALUES (1, 61);
INSERT INTO `tb_role_permission` VALUES (1, 62);
INSERT INTO `tb_role_permission` VALUES (1, 66);
INSERT INTO `tb_role_permission` VALUES (3, 66);
INSERT INTO `tb_role_permission` VALUES (1, 67);
INSERT INTO `tb_role_permission` VALUES (3, 67);
INSERT INTO `tb_role_permission` VALUES (1, 68);
INSERT INTO `tb_role_permission` VALUES (3, 68);
INSERT INTO `tb_role_permission` VALUES (1, 69);
INSERT INTO `tb_role_permission` VALUES (3, 69);
INSERT INTO `tb_role_permission` VALUES (1, 70);
INSERT INTO `tb_role_permission` VALUES (3, 70);
INSERT INTO `tb_role_permission` VALUES (1, 71);
INSERT INTO `tb_role_permission` VALUES (3, 71);
INSERT INTO `tb_role_permission` VALUES (1, 73);
INSERT INTO `tb_role_permission` VALUES (3, 73);
INSERT INTO `tb_role_permission` VALUES (1, 74);
INSERT INTO `tb_role_permission` VALUES (3, 74);
INSERT INTO `tb_role_permission` VALUES (1, 75);
INSERT INTO `tb_role_permission` VALUES (3, 75);

-- ----------------------------
-- Table structure for tb_task
-- ----------------------------
DROP TABLE IF EXISTS `tb_task`;
CREATE TABLE `tb_task`  (
  `task_id` int NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `task_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务名称',
  `task_require_distance` int NOT NULL COMMENT '要求路程',
  `task_require_time` int NOT NULL COMMENT '要求时间',
  `task_deadline` date NOT NULL COMMENT '截止时间',
  `create_time` date NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` int NULL DEFAULT NULL COMMENT '创建人',
  `update_time` date NULL DEFAULT NULL COMMENT '修改时间',
  `update_user` int NULL DEFAULT NULL COMMENT '修改人',
  `is_overtime` int NOT NULL COMMENT '是否已经过期，1为过期，0为ing',
  `select_classes` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '展示选择的班级',
  PRIMARY KEY (`task_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_task
-- ----------------------------
INSERT INTO `tb_task` VALUES (6, '2021年8月23日软件学院running任务', 2500, 25, '2021-08-24', NULL, NULL, NULL, NULL, 1, '2021软件3班,2021软件2班,');
INSERT INTO `tb_task` VALUES (10, '2021-8-25跑步任务', 4500, 45, '2021-08-25', NULL, NULL, NULL, NULL, 0, '2021软件2班,2021软件3班,');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  `user_nick_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `user_qq` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'QQ号',
  `user_vx` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信号',
  `user_cell_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号',
  `user_password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录密码',
  `create_time` date NULL DEFAULT NULL COMMENT '创建时间',
  `create_user` int NULL DEFAULT NULL COMMENT '创建人',
  `update_time` date NULL DEFAULT NULL COMMENT '修改时间',
  `update_user` int NULL DEFAULT NULL COMMENT '修改人',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最近一次登录时间',
  `last_login_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '最近一次登录IP',
  `last_login_mode` int NULL DEFAULT NULL COMMENT '最近一次登录模式(qq/vx/手机号)',
  `def_flag` int NOT NULL COMMENT '删除标志',
  `user_num` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学号/教工号',
  `user_real_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户真实姓名',
  `teacher_id` int NULL DEFAULT NULL,
  `img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `class_id` int NULL DEFAULT NULL COMMENT '班级id',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `FK_Reference_4`(`role_id`) USING BTREE,
  INDEX `FK_Reference_Teacher`(`teacher_id`) USING BTREE,
  INDEX `FK_USER_CLASS_02`(`class_id`) USING BTREE,
  CONSTRAINT `FK_Reference_4` FOREIGN KEY (`role_id`) REFERENCES `tb_role` (`role_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_Reference_Teacher` FOREIGN KEY (`teacher_id`) REFERENCES `tb_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FK_USER_CLASS_02` FOREIGN KEY (`class_id`) REFERENCES `tb_class` (`class_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, 1, 'admin', '100690142', '200810923', '13542899715', '123456', '2021-07-09', 1, '2021-07-09', 1, '2021-08-25 15:01:34', '127.0.0.1', 2, 0, 'scy_admin', 'scy管理', NULL, '/headIcon/1628670402482.png', NULL);
INSERT INTO `tb_user` VALUES (3, 3, 'James', NULL, NULL, '13342897562', '123456', NULL, NULL, NULL, NULL, '2021-08-24 17:24:19', '127.0.0.1', NULL, 0, '20162406', '詹姆斯', NULL, '/headIcon/1628566066374.png', NULL);
INSERT INTO `tb_user` VALUES (8, 2, NULL, NULL, NULL, '13342675512', '123456', NULL, NULL, NULL, NULL, '2021-08-24 15:50:00', '183.237.176.199', NULL, 0, '202124131308', '李宇文', 3, '/headIcon/1629443991279.png', 1);

SET FOREIGN_KEY_CHECKS = 1;
