/*
 Navicat MySQL Data Transfer

 Source Server         : docker-local
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : localhost:3306
 Source Schema         : iacaa20

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 07/04/2021 14:41:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_check_link
-- ----------------------------
DROP TABLE IF EXISTS `t_check_link`;
CREATE TABLE `t_check_link`  (
  `id` int(8) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `task_id` int(7) NULL DEFAULT NULL COMMENT '对应课程目标',
  `name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `target_score` double NULL DEFAULT NULL COMMENT '目标成绩',
  `average_score` double NULL DEFAULT NULL COMMENT '平均成绩',
  `mix` double NULL DEFAULT NULL COMMENT '占比',
  `created_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `c_task`(`task_id`) USING BTREE,
  CONSTRAINT `c_task` FOREIGN KEY (`task_id`) REFERENCES `t_course_task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 62 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_check_link
-- ----------------------------
INSERT INTO `t_check_link` VALUES (42, 26, '期末考试', 100, 75, 0.7, '2021-03-31 11:36:58', '2021-03-31 11:37:31');
INSERT INTO `t_check_link` VALUES (43, 26, '日常考勤', 100, 88, 0.3, '2021-03-31 11:36:58', '2021-03-31 11:37:31');
INSERT INTO `t_check_link` VALUES (44, 27, '期末考试', 100, 75, 0.7, '2021-03-31 11:37:13', '2021-03-31 19:45:27');
INSERT INTO `t_check_link` VALUES (45, 27, '日常考勤', 100, 85, 0.3, '2021-03-31 11:37:13', '2021-03-31 19:45:27');
INSERT INTO `t_check_link` VALUES (46, 34, '期中考试', 50, 44, 0.5, '2021-04-06 10:05:18', '2021-04-06 10:07:01');
INSERT INTO `t_check_link` VALUES (47, 34, '期末考试', 50, 30, 0.5, '2021-04-06 10:05:18', '2021-04-06 10:07:01');
INSERT INTO `t_check_link` VALUES (48, 35, '期末考试', 100, 35, 1, '2021-04-06 10:05:37', '2021-04-06 10:07:09');
INSERT INTO `t_check_link` VALUES (49, 28, '日常作业', 50, 42, 0.3, '2021-04-06 10:05:55', '2021-04-06 10:07:17');
INSERT INTO `t_check_link` VALUES (50, 28, '期末考试', 100, 88, 0.7, '2021-04-06 10:05:55', '2021-04-06 10:07:17');
INSERT INTO `t_check_link` VALUES (51, 29, '期末考试', 100, 88, 0.7, '2021-04-06 10:06:20', '2021-04-06 10:07:26');
INSERT INTO `t_check_link` VALUES (52, 29, '日常考勤', 100, 87, 0.3, '2021-04-06 10:06:20', '2021-04-06 10:07:26');
INSERT INTO `t_check_link` VALUES (53, 36, '期末考试', 100, 66, 0.7, '2021-04-06 10:06:48', '2021-04-06 14:09:29');
INSERT INTO `t_check_link` VALUES (54, 36, '期中考试', 100, 78, 0.3, '2021-04-06 10:06:48', '2021-04-06 14:09:29');
INSERT INTO `t_check_link` VALUES (55, 56, '日常作业', 100, 78, 0.5, '2021-04-07 10:31:47', '2021-04-07 10:32:21');
INSERT INTO `t_check_link` VALUES (56, 56, '期末考试', 100, 88, 0.5, '2021-04-07 10:31:47', '2021-04-07 10:32:21');
INSERT INTO `t_check_link` VALUES (57, 58, '日常作业', 100, 88, 0.4, '2021-04-07 10:32:03', '2021-04-07 10:33:49');
INSERT INTO `t_check_link` VALUES (58, 58, '期末考试', 100, 86, 0.6, '2021-04-07 10:32:03', '2021-04-07 10:33:49');
INSERT INTO `t_check_link` VALUES (59, 57, '期中考试', 100, 90, 0.5, '2021-04-07 10:32:50', '2021-04-07 10:33:40');
INSERT INTO `t_check_link` VALUES (60, 57, '期末考试', 100, 85, 0.5, '2021-04-07 10:32:50', '2021-04-07 10:33:40');
INSERT INTO `t_check_link` VALUES (61, 59, '期末考试', 100, 88, 1, '2021-04-07 10:34:06', '2021-04-07 10:34:18');

-- ----------------------------
-- Table structure for t_course
-- ----------------------------
DROP TABLE IF EXISTS `t_course`;
CREATE TABLE `t_course`  (
  `id` int(5) NOT NULL COMMENT '唯一标识',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '内容',
  `image` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `edit_user_id` int(5) NULL DEFAULT NULL COMMENT '管理账户',
  `edit_status` int(1) NULL DEFAULT NULL COMMENT '课程编辑状态',
  `created_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `course_id`(`id`) USING BTREE,
  INDEX `teacherUser`(`edit_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_course
-- ----------------------------
INSERT INTO `t_course` VALUES (1, '数据结构', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (2, '计算机组成原理', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (3, '专业英语', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (5, '操作系统', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (6, '计算机网络', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (7, '面向对象程序设计', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (8, '面向对象需求分析与建模', '', NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (9, '微机原理与接口技术', '', NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (10, '数据库原理', '', NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (11, 'Web前端开发技术', '', NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (12, '算法设计与分析', '', NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (13, '软件工程及项目管理', '', NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (14, '软件测试技术', '', NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (15, '专业方向课程一', '', NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (16, '专业方向课程二', '', NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (17, '计算机应用技术课程实验', '', NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (18, '程序设计基础实验', '', NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (20, '数据结构课程实验', '', NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (21, '面向对象程序设计实验', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (22, '微机原理与接口技术课程实验', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (23, '数据库原理课程实验', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (24, 'Web前端开发技术实验', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (25, '算法设计与分析课程实验', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (26, '软件工程及项目管理课程实验', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (27, '软件测试技术课程实验', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (28, '面向对象程序设计课程设计', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (29, '工程综合实训（一）', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (30, '数据结构课程设计', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (31, '工程综合实训（二）', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (32, '毕业实习', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (33, '毕业设计', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (34, '中国近代史纲要', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (35, '思想道德修养与法律基础', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (36, '毛泽东思想和中国特色社会主义理论体系概论', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (37, '马克思主义基本原理', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (38, '体育（1-4）', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (39, '军事理论', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (40, '大学英语（1-3）', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (41, '形势与政策', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (42, '《职业生涯规划》（一）（二）', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (43, '《大学生就业指导教育》（一）（二）', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (44, '创新创业类', NULL, 5, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (45, '大学物理（1-2）', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (46, '大学物理实验', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (47, '高等数学（理工）B1、B2', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (48, '线性代数', NULL, 5, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (49, '概率与数理统计', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (50, '离散数学', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (51, '计算机应用技术', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');
INSERT INTO `t_course` VALUES (52, '程序设计基础', NULL, NULL, NULL, '2021-03-23 15:23:00', '2021-03-23 15:23:00');

-- ----------------------------
-- Table structure for t_course_target
-- ----------------------------
DROP TABLE IF EXISTS `t_course_target`;
CREATE TABLE `t_course_target`  (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `course_id` int(5) NULL DEFAULT NULL COMMENT '关联课程',
  `target_id` int(5) NULL DEFAULT NULL COMMENT '关联指标点',
  `mix` double NULL DEFAULT NULL COMMENT '关联比例',
  `created_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `xcourse`(`course_id`) USING BTREE,
  INDEX `xtarget`(`target_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 269 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_course_target
-- ----------------------------
INSERT INTO `t_course_target` VALUES (227, 1, 230, 0.4, '2021-03-31 11:35:32', '2021-03-31 11:35:32');
INSERT INTO `t_course_target` VALUES (228, 5, 230, 0.4, '2021-03-31 11:35:32', '2021-03-31 11:35:32');
INSERT INTO `t_course_target` VALUES (229, 6, 230, 0.2, '2021-03-31 11:35:32', '2021-03-31 11:35:32');
INSERT INTO `t_course_target` VALUES (230, 11, 231, 0.3, '2021-03-31 11:35:57', '2021-03-31 11:35:57');
INSERT INTO `t_course_target` VALUES (231, 12, 231, 0.3, '2021-03-31 11:35:57', '2021-03-31 11:35:57');
INSERT INTO `t_course_target` VALUES (232, 15, 231, 0.4, '2021-03-31 11:35:57', '2021-03-31 11:35:57');
INSERT INTO `t_course_target` VALUES (233, 1, 228, 0.3, '2021-03-31 19:26:38', '2021-03-31 19:27:06');
INSERT INTO `t_course_target` VALUES (234, 6, 228, 0.4, '2021-03-31 19:27:06', '2021-03-31 19:27:06');
INSERT INTO `t_course_target` VALUES (235, 5, 228, 0.3, '2021-03-31 19:27:06', '2021-03-31 19:27:06');
INSERT INTO `t_course_target` VALUES (236, 1, 229, 0.5, '2021-03-31 19:56:02', '2021-04-06 09:23:30');
INSERT INTO `t_course_target` VALUES (237, 3, 226, 1, '2021-03-31 19:56:13', '2021-03-31 19:56:13');
INSERT INTO `t_course_target` VALUES (238, 6, 224, 1, '2021-03-31 19:56:21', '2021-03-31 19:56:21');
INSERT INTO `t_course_target` VALUES (239, 7, 225, 1, '2021-03-31 19:56:28', '2021-03-31 19:56:28');
INSERT INTO `t_course_target` VALUES (240, 14, 222, 0.5, '2021-03-31 19:56:38', '2021-04-06 09:24:24');
INSERT INTO `t_course_target` VALUES (241, 7, 220, 1, '2021-03-31 19:56:45', '2021-03-31 19:56:45');
INSERT INTO `t_course_target` VALUES (242, 6, 221, 1, '2021-03-31 19:56:49', '2021-03-31 19:56:49');
INSERT INTO `t_course_target` VALUES (243, 52, 201, 0.3, '2021-04-01 18:24:52', '2021-04-01 18:24:52');
INSERT INTO `t_course_target` VALUES (244, 51, 201, 0.3, '2021-04-01 18:24:52', '2021-04-01 18:24:52');
INSERT INTO `t_course_target` VALUES (245, 50, 201, 0.2, '2021-04-01 18:24:52', '2021-04-01 18:24:52');
INSERT INTO `t_course_target` VALUES (246, 33, 201, 0.2, '2021-04-01 18:24:52', '2021-04-01 18:24:52');
INSERT INTO `t_course_target` VALUES (247, 5, 229, 0.2, '2021-04-06 09:23:30', '2021-04-06 09:23:30');
INSERT INTO `t_course_target` VALUES (248, 7, 229, 0.3, '2021-04-06 09:23:30', '2021-04-06 09:23:30');
INSERT INTO `t_course_target` VALUES (249, 3, 227, 0.3, '2021-04-06 09:23:57', '2021-04-06 09:23:57');
INSERT INTO `t_course_target` VALUES (250, 18, 227, 0.3, '2021-04-06 09:23:57', '2021-04-06 09:23:57');
INSERT INTO `t_course_target` VALUES (251, 7, 227, 0.3, '2021-04-06 09:23:57', '2021-04-06 09:23:57');
INSERT INTO `t_course_target` VALUES (252, 20, 227, 0.1, '2021-04-06 09:23:57', '2021-04-06 09:23:57');
INSERT INTO `t_course_target` VALUES (253, 18, 222, 0.5, '2021-04-06 09:24:24', '2021-04-06 09:24:24');
INSERT INTO `t_course_target` VALUES (254, 6, 223, 0.2, '2021-04-06 09:24:45', '2021-04-06 09:24:45');
INSERT INTO `t_course_target` VALUES (255, 20, 223, 0.3, '2021-04-06 09:24:45', '2021-04-06 09:24:45');
INSERT INTO `t_course_target` VALUES (256, 13, 223, 0.5, '2021-04-06 09:24:45', '2021-04-06 09:24:45');
INSERT INTO `t_course_target` VALUES (257, 25, 217, 0.4, '2021-04-06 09:25:15', '2021-04-06 09:25:15');
INSERT INTO `t_course_target` VALUES (258, 8, 217, 0.3, '2021-04-06 09:25:15', '2021-04-06 09:25:15');
INSERT INTO `t_course_target` VALUES (259, 23, 217, 0.3, '2021-04-06 09:25:15', '2021-04-06 09:25:15');
INSERT INTO `t_course_target` VALUES (260, 22, 218, 1, '2021-04-06 09:25:23', '2021-04-06 09:25:23');
INSERT INTO `t_course_target` VALUES (261, 24, 219, 1, '2021-04-06 09:25:29', '2021-04-06 09:25:29');
INSERT INTO `t_course_target` VALUES (262, 51, 215, 0.3, '2021-04-06 09:25:47', '2021-04-06 09:25:47');
INSERT INTO `t_course_target` VALUES (263, 7, 215, 0.5, '2021-04-06 09:25:47', '2021-04-06 09:25:47');
INSERT INTO `t_course_target` VALUES (264, 6, 215, 0.2, '2021-04-06 09:25:47', '2021-04-06 09:25:47');
INSERT INTO `t_course_target` VALUES (265, 32, 216, 1, '2021-04-06 09:25:57', '2021-04-06 09:25:57');
INSERT INTO `t_course_target` VALUES (266, 7, 212, 0.3, '2021-04-06 09:26:15', '2021-04-06 09:26:15');
INSERT INTO `t_course_target` VALUES (267, 29, 212, 0.2, '2021-04-06 09:26:15', '2021-04-06 09:26:15');
INSERT INTO `t_course_target` VALUES (268, 42, 212, 0.5, '2021-04-06 09:26:15', '2021-04-06 09:26:15');

-- ----------------------------
-- Table structure for t_course_task
-- ----------------------------
DROP TABLE IF EXISTS `t_course_task`;
CREATE TABLE `t_course_task`  (
  `id` int(7) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `describes` varchar(600) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `course_id` int(5) NOT NULL COMMENT '关联课程',
  `target_id` int(5) NOT NULL COMMENT '关联指标点',
  `mix` double NULL DEFAULT NULL COMMENT '占比',
  `year` int(4) NULL DEFAULT NULL COMMENT '年份',
  `sys_grade` double(10, 3) NULL DEFAULT 0.000 COMMENT '系统录入成绩',
  `stu_grade` double(10, 3) NULL DEFAULT 0.000 COMMENT '学生评价成绩',
  `created_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `tar_course`(`course_id`) USING BTREE,
  INDEX `tar`(`target_id`) USING BTREE,
  CONSTRAINT `courseKey` FOREIGN KEY (`course_id`) REFERENCES `t_course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `targetKey` FOREIGN KEY (`target_id`) REFERENCES `t_target` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 60 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_course_task
-- ----------------------------
INSERT INTO `t_course_task` VALUES (26, '撒旦大苏打啊实打实的啊实打实的', 11, 231, 0.5, 2021, 0.815, 0.560, '2021-03-31 11:36:39', '2021-03-31 11:36:39');
INSERT INTO `t_course_task` VALUES (27, '啊实打实打打萨达萨达是大师的', 11, 231, 0.5, 2021, 0.800, 0.600, '2021-03-31 11:36:39', '2021-03-31 11:36:39');
INSERT INTO `t_course_task` VALUES (28, '阿三大苏打实打实的', 15, 231, 1, 2021, 0.860, 0.571, '2021-03-31 20:10:03', '2021-03-31 20:10:03');
INSERT INTO `t_course_task` VALUES (29, '啊实打实大声的', 3, 226, 1, 2021, 0.875, 0.493, '2021-03-31 20:10:21', '2021-03-31 20:10:21');
INSERT INTO `t_course_task` VALUES (30, '安师大撒手但', 5, 230, 1, 2021, 0.000, 0.587, '2021-03-31 20:10:57', '2021-04-06 09:50:31');
INSERT INTO `t_course_task` VALUES (31, '爱仕达大大苏打', 5, 228, 1, 2021, 0.000, 0.620, '2021-03-31 20:10:57', '2021-04-06 09:50:31');
INSERT INTO `t_course_task` VALUES (33, '安师大大苏打', 12, 231, 1, 2021, 0.000, 0.571, '2021-03-31 20:11:21', '2021-03-31 20:11:21');
INSERT INTO `t_course_task` VALUES (34, '购货方更好地发挥对方灌灌灌灌', 24, 219, 1, 2021, 0.740, 0.543, '2021-04-06 09:26:45', '2021-04-06 09:26:45');
INSERT INTO `t_course_task` VALUES (35, '电饭锅电饭锅当官的风格的风格的', 42, 212, 0.5, 2021, 0.350, 0.614, '2021-04-06 09:26:58', '2021-04-06 13:37:31');
INSERT INTO `t_course_task` VALUES (36, '好看好看好看好几款', 29, 212, 0.5, 2021, 0.720, 0.600, '2021-04-06 09:27:36', '2021-04-06 09:27:36');
INSERT INTO `t_course_task` VALUES (37, '和健康会尽快汇款后即可将积极', 29, 212, 0.5, 2021, 0.000, 0.582, '2021-04-06 09:27:36', '2021-04-06 09:27:36');
INSERT INTO `t_course_task` VALUES (38, '个符合规定鬼地方个大概', 22, 218, 1, 2021, 0.000, 0.693, '2021-04-06 09:27:48', '2021-04-06 09:27:48');
INSERT INTO `t_course_task` VALUES (39, '如果是的士速递根深蒂固', 5, 229, 1, 2021, 0.000, 0.662, '2021-04-06 09:50:31', '2021-04-06 09:50:31');
INSERT INTO `t_course_task` VALUES (40, '付水电费水电费水电费水电费', 23, 217, 1, 2021, 0.000, 0.636, '2021-04-06 09:50:45', '2021-04-06 09:50:45');
INSERT INTO `t_course_task` VALUES (41, '大叔大婶多撒多所所所', 1, 230, 0.5, 2021, 0.000, 0.625, '2021-04-06 09:51:29', '2021-04-06 09:51:29');
INSERT INTO `t_course_task` VALUES (42, '广东省分公司感受到根深蒂固', 1, 230, 0.5, 2021, 0.000, 0.677, '2021-04-06 09:51:29', '2021-04-06 09:51:29');
INSERT INTO `t_course_task` VALUES (43, '大叔大婶大所大所多所', 1, 228, 1, 2021, 0.000, 0.650, '2021-04-06 09:51:29', '2021-04-06 09:51:29');
INSERT INTO `t_course_task` VALUES (44, '递四方速递多多多多多', 1, 229, 1, 2021, 0.000, 0.543, '2021-04-06 09:51:29', '2021-04-06 09:51:29');
INSERT INTO `t_course_task` VALUES (45, '师德师风所多付多多多多', 20, 227, 1, 2021, 0.000, 0.610, '2021-04-06 10:03:35', '2021-04-06 10:03:42');
INSERT INTO `t_course_task` VALUES (46, '发发呆多所付所多付所多多多多', 20, 223, 1, 2021, 0.000, 0.564, '2021-04-06 10:03:35', '2021-04-06 10:03:42');
INSERT INTO `t_course_task` VALUES (47, '咕咕咕咕咕所过付所过所事实上', 32, 216, 0.5, 2021, 0.000, 0.722, '2021-04-06 10:04:06', '2021-04-06 10:04:06');
INSERT INTO `t_course_task` VALUES (48, '恢诡谲怪好几个号结构化', 32, 216, 0.5, 2021, 0.000, 0.600, '2021-04-06 10:04:06', '2021-04-06 10:04:06');
INSERT INTO `t_course_task` VALUES (49, '广发华福过或过过过过过', 33, 201, 1, 2021, 0.000, 0.618, '2021-04-06 10:04:22', '2021-04-06 10:04:22');
INSERT INTO `t_course_task` VALUES (50, '贵得很过或过过过过过过过过', 50, 201, 0.5, 2021, 0.000, 0.620, '2021-04-06 10:04:44', '2021-04-06 10:04:44');
INSERT INTO `t_course_task` VALUES (51, '可激活或或或或军扩或军扩或军', 50, 201, 0.5, 2021, 0.000, 0.585, '2021-04-06 10:04:44', '2021-04-06 10:04:44');
INSERT INTO `t_course_task` VALUES (52, '大叔大婶大撒所多', 42, 212, 0.5, 2021, 0.000, 0.600, '2021-04-06 13:37:31', '2021-04-06 13:37:31');
INSERT INTO `t_course_task` VALUES (53, '奥术大师大厦所所所所实打实', 7, 212, 0.3, 2021, 0.000, 0.625, '2021-04-06 13:39:12', '2021-04-06 13:39:12');
INSERT INTO `t_course_task` VALUES (54, '艾斯德斯手术室', 7, 212, 0.3, 2021, 0.000, 0.683, '2021-04-06 13:39:12', '2021-04-06 13:39:12');
INSERT INTO `t_course_task` VALUES (55, '按时大大啊啊啊啊', 7, 212, 0.4, 2021, 0.000, 0.525, '2021-04-06 13:39:12', '2021-04-06 13:39:12');
INSERT INTO `t_course_task` VALUES (56, '打撒所大所大所大所多', 13, 223, 0.5, 2021, 0.830, 0.371, '2021-04-07 10:30:53', '2021-04-07 10:30:53');
INSERT INTO `t_course_task` VALUES (57, '的点点滴滴丶的点点滴滴丶哒哒哒哒哒哒哒哒哒哒哒哒的点点滴滴丶的点点滴滴丶ddddddd', 13, 223, 0.5, 2021, 0.875, 0.300, '2021-04-07 10:30:53', '2021-04-07 10:30:53');
INSERT INTO `t_course_task` VALUES (58, '大萨达撒撒所鞍山市所所所', 14, 222, 0.5, 2021, 0.870, 0.475, '2021-04-07 10:31:17', '2021-04-07 10:31:17');
INSERT INTO `t_course_task` VALUES (59, '哒哒哒哒哒哒多多多大撒', 14, 222, 0.5, 2021, 0.880, 0.444, '2021-04-07 10:31:17', '2021-04-07 10:31:17');

-- ----------------------------
-- Table structure for t_grad_requirement
-- ----------------------------
DROP TABLE IF EXISTS `t_grad_requirement`;
CREATE TABLE `t_grad_requirement`  (
  `id` int(3) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '毕业要求',
  `discrible` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `year` int(4) NULL DEFAULT NULL COMMENT '年份',
  `sys_grade` double(10, 3) NULL DEFAULT 0.000 COMMENT '系统计算成绩',
  `stu_grade` double(10, 3) NULL DEFAULT 0.000 COMMENT '学生评价成绩',
  `created_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_grad_requirement
-- ----------------------------
INSERT INTO `t_grad_requirement` VALUES (21, '工程知识', '能够将数学、自然科学、工程基础和专业知识用于解决复杂软件工程问题。', 2021, 0.000, 0.690, '2021-03-23 15:07:02', '2021-03-23 15:07:41');
INSERT INTO `t_grad_requirement` VALUES (22, '问题分析', '能够应用数学、自然科学和工程科学的基本原理，识别、表达、并通过文献研究分析复杂软件工程问题，以获得有效结论。', 2021, 0.000, 0.550, '2021-03-23 15:08:00', '2021-03-23 15:08:23');
INSERT INTO `t_grad_requirement` VALUES (23, '设计/开发解决方案', '能够设计针对复杂软件工程问题的解决方案，设计满足特定需求的软件系统、可复用模块或组件，并能够在设计环节中体现创新意识，考虑社会、健康、安全、法律、文化以及环境等因素。', 2021, 0.000, 0.550, '2021-03-23 15:08:37', '2021-03-23 15:09:08');
INSERT INTO `t_grad_requirement` VALUES (24, '研究', '能够基于科学原理并采用科学方法对复杂软件工程问题进行研究，包括建立软件模型、设计实验、分析与解释数据，并通过信息综合得到合理有效的结论。', 2021, 0.053, 0.550, '2021-03-23 15:09:26', '2021-03-23 15:09:48');
INSERT INTO `t_grad_requirement` VALUES (25, '使用现代工具', '能够针对复杂软件工程问题，开发、选择与使用恰当的技术、资源、现代工程工具和信息技术工具，利用形式化方法完成复杂软件工程问题的预测与模拟，并能够理解其局限性。', 2021, 0.000, 0.550, '2021-03-23 15:10:06', '2021-03-23 15:10:30');
INSERT INTO `t_grad_requirement` VALUES (26, '工程与社会', '能够基于工程相关领域背景知识进行合理分析，评价专业工程实践和复杂软件工程问题解决方案对社会、健康、安全、法律以及文化的影响，并理解应承担的责任。', 2021, 0.247, 0.480, '2021-03-23 15:10:44', '2021-03-23 15:11:10');
INSERT INTO `t_grad_requirement` VALUES (27, '环境和可持续发展', '能够理解和评价针对复杂软件工程问题的专业工程实践对环境、社会可持续发展的影响。', 2021, 0.000, 0.480, '2021-03-23 15:11:24', '2021-03-23 15:11:44');
INSERT INTO `t_grad_requirement` VALUES (28, '职业规范', '具有人文社会科学素养、社会责任感，能够在软件工程实践中理解并遵守软件工程职业道德和规范，履行责任。', 2021, 0.432, 0.700, '2021-03-23 15:12:03', '2021-03-23 15:12:25');
INSERT INTO `t_grad_requirement` VALUES (29, '个人和团队', '能够在多学科背景下的软件项目团队中承担个体、团队成员以及负责人的角色。', 2021, 0.000, 0.700, '2021-03-23 15:12:42', '2021-03-23 15:13:00');
INSERT INTO `t_grad_requirement` VALUES (30, '沟通', '能够就复杂软件工程问题与业界同行及社会公众进行有效沟通和交流，包括撰写报告和设计文稿、陈述发言、清晰表达或回应指令，并具备一定的国际视野，能够在跨文化背景下进行沟通和交流。', 2021, 0.438, 0.480, '2021-03-23 15:13:16', '2021-03-23 15:13:37');
INSERT INTO `t_grad_requirement` VALUES (31, '项目管理', '理解并掌握复杂软件工程项目管理原理与经济决策方法，并能在多学科环境中应用，具有一定的软件项目管理能力。', 2021, 0.000, 0.690, '2021-03-23 15:13:54', '2021-03-23 15:14:13');
INSERT INTO `t_grad_requirement` VALUES (32, '终身学习', '具有自主学习和终身学习的意识，有不断学习和适应发展的能力。', 2021, 0.293, 0.800, '2021-03-23 15:14:26', '2021-03-23 15:14:42');
INSERT INTO `t_grad_requirement` VALUES (33, '终身学习', '具有自主学习和终身学习的意识，有不断学习和适应发展的能力。', 2020, 0.000, 0.800, '2020-02-01 16:05:45', '2020-02-01 16:05:45');
INSERT INTO `t_grad_requirement` VALUES (34, '项目管理', '理解并掌握复杂软件工程项目管理原理与经济决策方法，并能在多学科环境中应用，具有一定的软件项目管理能力。', 2020, 0.000, 0.690, '2020-02-01 16:06:27', '2020-02-01 16:06:27');

-- ----------------------------
-- Table structure for t_stu_evaluation
-- ----------------------------
DROP TABLE IF EXISTS `t_stu_evaluation`;
CREATE TABLE `t_stu_evaluation`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `course_task` int(7) NULL DEFAULT NULL COMMENT '课程目标',
  `ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'id地址',
  `stu_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '学号',
  `score` int(1) NULL DEFAULT 0 COMMENT '成绩',
  `created_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `courseTaskKey`(`course_task`) USING BTREE,
  INDEX `scoreKey`(`score`) USING BTREE,
  INDEX `stuKey`(`stu_no`) USING BTREE,
  CONSTRAINT `courseFkey` FOREIGN KEY (`course_task`) REFERENCES `t_course_task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 426 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_stu_evaluation
-- ----------------------------
INSERT INTO `t_stu_evaluation` VALUES (7, 54, '172.26.8.91', '12313123', 4, '2021-04-06 18:34:54', '2021-04-06 18:34:54');
INSERT INTO `t_stu_evaluation` VALUES (8, 33, '172.26.8.91', '12313123', 3, '2021-04-06 18:34:54', '2021-04-06 18:34:54');
INSERT INTO `t_stu_evaluation` VALUES (9, 45, '172.26.8.91', '12313123', 3, '2021-04-06 18:34:54', '2021-04-06 18:34:54');
INSERT INTO `t_stu_evaluation` VALUES (10, 42, '172.26.8.91', '131323123', 3, '2021-04-06 19:02:03', '2021-04-06 19:02:03');
INSERT INTO `t_stu_evaluation` VALUES (11, 52, '172.26.8.91', '131323123', 3, '2021-04-06 19:02:03', '2021-04-06 19:02:03');
INSERT INTO `t_stu_evaluation` VALUES (12, 43, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:03', '2021-04-06 19:02:03');
INSERT INTO `t_stu_evaluation` VALUES (13, 41, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:08', '2021-04-06 19:02:08');
INSERT INTO `t_stu_evaluation` VALUES (14, 44, '172.26.8.91', '131323123', 2, '2021-04-06 19:02:08', '2021-04-06 19:02:08');
INSERT INTO `t_stu_evaluation` VALUES (15, 43, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:08', '2021-04-06 19:02:08');
INSERT INTO `t_stu_evaluation` VALUES (16, 26, '172.26.8.91', '131323123', 3, '2021-04-06 19:02:11', '2021-04-06 19:02:11');
INSERT INTO `t_stu_evaluation` VALUES (17, 46, '172.26.8.91', '131323123', 3, '2021-04-06 19:02:11', '2021-04-06 19:02:11');
INSERT INTO `t_stu_evaluation` VALUES (18, 43, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:11', '2021-04-06 19:02:11');
INSERT INTO `t_stu_evaluation` VALUES (19, 38, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:15', '2021-04-06 19:02:15');
INSERT INTO `t_stu_evaluation` VALUES (20, 46, '172.26.8.91', '131323123', 3, '2021-04-06 19:02:15', '2021-04-06 19:02:15');
INSERT INTO `t_stu_evaluation` VALUES (21, 43, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:15', '2021-04-06 19:02:15');
INSERT INTO `t_stu_evaluation` VALUES (22, 38, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:18', '2021-04-06 19:02:18');
INSERT INTO `t_stu_evaluation` VALUES (23, 42, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:18', '2021-04-06 19:02:18');
INSERT INTO `t_stu_evaluation` VALUES (24, 50, '172.26.8.91', '131323123', 3, '2021-04-06 19:02:18', '2021-04-06 19:02:18');
INSERT INTO `t_stu_evaluation` VALUES (25, 48, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:21', '2021-04-06 19:02:21');
INSERT INTO `t_stu_evaluation` VALUES (26, 27, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:21', '2021-04-06 19:02:21');
INSERT INTO `t_stu_evaluation` VALUES (27, 47, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:21', '2021-04-06 19:02:21');
INSERT INTO `t_stu_evaluation` VALUES (28, 28, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:24', '2021-04-06 19:02:24');
INSERT INTO `t_stu_evaluation` VALUES (29, 55, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:24', '2021-04-06 19:02:24');
INSERT INTO `t_stu_evaluation` VALUES (30, 40, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:24', '2021-04-06 19:02:24');
INSERT INTO `t_stu_evaluation` VALUES (31, 53, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:27', '2021-04-06 19:02:27');
INSERT INTO `t_stu_evaluation` VALUES (32, 36, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:27', '2021-04-06 19:02:27');
INSERT INTO `t_stu_evaluation` VALUES (33, 44, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:27', '2021-04-06 19:02:27');
INSERT INTO `t_stu_evaluation` VALUES (34, 53, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:29', '2021-04-06 19:02:29');
INSERT INTO `t_stu_evaluation` VALUES (35, 42, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:29', '2021-04-06 19:02:29');
INSERT INTO `t_stu_evaluation` VALUES (36, 50, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:29', '2021-04-06 19:02:29');
INSERT INTO `t_stu_evaluation` VALUES (37, 28, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:32', '2021-04-06 19:02:32');
INSERT INTO `t_stu_evaluation` VALUES (38, 39, '172.26.8.91', '131323123', 3, '2021-04-06 19:02:32', '2021-04-06 19:02:32');
INSERT INTO `t_stu_evaluation` VALUES (39, 36, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:32', '2021-04-06 19:02:32');
INSERT INTO `t_stu_evaluation` VALUES (40, 49, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:39', '2021-04-06 19:02:39');
INSERT INTO `t_stu_evaluation` VALUES (41, 47, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:39', '2021-04-06 19:02:39');
INSERT INTO `t_stu_evaluation` VALUES (42, 38, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:39', '2021-04-06 19:02:39');
INSERT INTO `t_stu_evaluation` VALUES (43, 27, '172.26.8.91', '131323123', 3, '2021-04-06 19:02:42', '2021-04-06 19:02:42');
INSERT INTO `t_stu_evaluation` VALUES (44, 52, '172.26.8.91', '131323123', 3, '2021-04-06 19:02:42', '2021-04-06 19:02:42');
INSERT INTO `t_stu_evaluation` VALUES (45, 38, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:42', '2021-04-06 19:02:42');
INSERT INTO `t_stu_evaluation` VALUES (46, 44, '172.26.8.91', '131323123', 2, '2021-04-06 19:02:46', '2021-04-06 19:02:46');
INSERT INTO `t_stu_evaluation` VALUES (47, 29, '172.26.8.91', '131323123', 2, '2021-04-06 19:02:46', '2021-04-06 19:02:46');
INSERT INTO `t_stu_evaluation` VALUES (48, 37, '172.26.8.91', '131323123', 3, '2021-04-06 19:02:46', '2021-04-06 19:02:46');
INSERT INTO `t_stu_evaluation` VALUES (49, 35, '172.26.8.91', '131323123', 2, '2021-04-06 19:02:49', '2021-04-06 19:02:49');
INSERT INTO `t_stu_evaluation` VALUES (50, 36, '172.26.8.91', '131323123', 3, '2021-04-06 19:02:49', '2021-04-06 19:02:49');
INSERT INTO `t_stu_evaluation` VALUES (51, 30, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:49', '2021-04-06 19:02:49');
INSERT INTO `t_stu_evaluation` VALUES (52, 40, '172.26.8.91', '131323123', 3, '2021-04-06 19:02:51', '2021-04-06 19:02:51');
INSERT INTO `t_stu_evaluation` VALUES (53, 50, '172.26.8.91', '131323123', 3, '2021-04-06 19:02:51', '2021-04-06 19:02:51');
INSERT INTO `t_stu_evaluation` VALUES (54, 54, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:51', '2021-04-06 19:02:51');
INSERT INTO `t_stu_evaluation` VALUES (55, 53, '172.26.8.91', '131323123', 3, '2021-04-06 19:02:54', '2021-04-06 19:02:54');
INSERT INTO `t_stu_evaluation` VALUES (56, 50, '172.26.8.91', '131323123', 3, '2021-04-06 19:02:54', '2021-04-06 19:02:54');
INSERT INTO `t_stu_evaluation` VALUES (57, 28, '172.26.8.91', '131323123', 3, '2021-04-06 19:02:54', '2021-04-06 19:02:54');
INSERT INTO `t_stu_evaluation` VALUES (58, 41, '172.26.8.91', '131323123', 3, '2021-04-06 19:02:57', '2021-04-06 19:02:57');
INSERT INTO `t_stu_evaluation` VALUES (59, 29, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:57', '2021-04-06 19:02:57');
INSERT INTO `t_stu_evaluation` VALUES (60, 39, '172.26.8.91', '131323123', 4, '2021-04-06 19:02:57', '2021-04-06 19:02:57');
INSERT INTO `t_stu_evaluation` VALUES (61, 26, '172.26.8.91', '131323123', 3, '2021-04-06 19:03:00', '2021-04-06 19:03:00');
INSERT INTO `t_stu_evaluation` VALUES (62, 45, '172.26.8.91', '131323123', 4, '2021-04-06 19:03:00', '2021-04-06 19:03:00');
INSERT INTO `t_stu_evaluation` VALUES (63, 28, '172.26.8.91', '131323123', 4, '2021-04-06 19:03:00', '2021-04-06 19:03:00');
INSERT INTO `t_stu_evaluation` VALUES (64, 34, '172.26.8.91', '131323123', 3, '2021-04-06 19:03:03', '2021-04-06 19:03:03');
INSERT INTO `t_stu_evaluation` VALUES (65, 53, '172.26.8.91', '131323123', 4, '2021-04-06 19:03:03', '2021-04-06 19:03:03');
INSERT INTO `t_stu_evaluation` VALUES (66, 54, '172.26.8.91', '131323123', 3, '2021-04-06 19:03:03', '2021-04-06 19:03:03');
INSERT INTO `t_stu_evaluation` VALUES (67, 53, '172.26.8.91', '131323123', 3, '2021-04-06 19:03:06', '2021-04-06 19:03:06');
INSERT INTO `t_stu_evaluation` VALUES (68, 45, '172.26.8.91', '131323123', 4, '2021-04-06 19:03:06', '2021-04-06 19:03:06');
INSERT INTO `t_stu_evaluation` VALUES (69, 35, '172.26.8.91', '131323123', 4, '2021-04-06 19:03:06', '2021-04-06 19:03:06');
INSERT INTO `t_stu_evaluation` VALUES (70, 27, '172.26.8.91', '131323123', 4, '2021-04-06 19:03:08', '2021-04-06 19:03:08');
INSERT INTO `t_stu_evaluation` VALUES (71, 45, '172.26.8.91', '131323123', 4, '2021-04-06 19:03:08', '2021-04-06 19:03:08');
INSERT INTO `t_stu_evaluation` VALUES (72, 36, '172.26.8.91', '131323123', 4, '2021-04-06 19:03:08', '2021-04-06 19:03:08');
INSERT INTO `t_stu_evaluation` VALUES (73, 33, '172.26.8.91', '131323123', 3, '2021-04-06 19:03:36', '2021-04-06 19:03:36');
INSERT INTO `t_stu_evaluation` VALUES (74, 41, '172.26.8.91', '131323123', 4, '2021-04-06 19:03:36', '2021-04-06 19:03:36');
INSERT INTO `t_stu_evaluation` VALUES (75, 43, '172.26.8.91', '131323123', 4, '2021-04-06 19:03:36', '2021-04-06 19:03:36');
INSERT INTO `t_stu_evaluation` VALUES (76, 47, '172.26.8.91', '131323123', 4, '2021-04-06 19:03:38', '2021-04-06 19:03:38');
INSERT INTO `t_stu_evaluation` VALUES (77, 35, '172.26.8.91', '131323123', 4, '2021-04-06 19:03:38', '2021-04-06 19:03:38');
INSERT INTO `t_stu_evaluation` VALUES (78, 36, '172.26.8.91', '131323123', 4, '2021-04-06 19:03:38', '2021-04-06 19:03:38');
INSERT INTO `t_stu_evaluation` VALUES (79, 39, '172.26.8.91', '131323123', 4, '2021-04-06 19:03:42', '2021-04-06 19:03:42');
INSERT INTO `t_stu_evaluation` VALUES (80, 28, '172.26.8.91', '131323123', 3, '2021-04-06 19:03:42', '2021-04-06 19:03:42');
INSERT INTO `t_stu_evaluation` VALUES (81, 40, '172.26.8.91', '131323123', 3, '2021-04-06 19:03:42', '2021-04-06 19:03:42');
INSERT INTO `t_stu_evaluation` VALUES (82, 38, '172.26.8.91', '131323123', 4, '2021-04-06 19:03:45', '2021-04-06 19:03:45');
INSERT INTO `t_stu_evaluation` VALUES (83, 33, '172.26.8.91', '131323123', 3, '2021-04-06 19:03:45', '2021-04-06 19:03:45');
INSERT INTO `t_stu_evaluation` VALUES (84, 41, '172.26.8.91', '131323123', 4, '2021-04-06 19:03:45', '2021-04-06 19:03:45');
INSERT INTO `t_stu_evaluation` VALUES (85, 47, '172.26.8.91', '131323123', 4, '2021-04-06 19:03:48', '2021-04-06 19:03:48');
INSERT INTO `t_stu_evaluation` VALUES (86, 30, '172.26.8.91', '131323123', 3, '2021-04-06 19:03:48', '2021-04-06 19:03:48');
INSERT INTO `t_stu_evaluation` VALUES (87, 26, '172.26.8.91', '131323123', 4, '2021-04-06 19:03:48', '2021-04-06 19:03:48');
INSERT INTO `t_stu_evaluation` VALUES (88, 55, '172.26.8.91', '131323123', 3, '2021-04-06 19:03:52', '2021-04-06 19:03:52');
INSERT INTO `t_stu_evaluation` VALUES (89, 45, '172.26.8.91', '131323123', 4, '2021-04-06 19:03:52', '2021-04-06 19:03:52');
INSERT INTO `t_stu_evaluation` VALUES (90, 47, '172.26.8.91', '131323123', 4, '2021-04-06 19:03:52', '2021-04-06 19:03:52');
INSERT INTO `t_stu_evaluation` VALUES (91, 48, '172.26.8.91', '131323123', 3, '2021-04-06 19:03:55', '2021-04-06 19:03:55');
INSERT INTO `t_stu_evaluation` VALUES (92, 40, '172.26.8.91', '131323123', 4, '2021-04-06 19:03:55', '2021-04-06 19:03:55');
INSERT INTO `t_stu_evaluation` VALUES (93, 45, '172.26.8.91', '131323123', 4, '2021-04-06 19:03:55', '2021-04-06 19:03:55');
INSERT INTO `t_stu_evaluation` VALUES (94, 51, '172.26.8.91', '131323123', 3, '2021-04-06 19:04:08', '2021-04-06 19:04:08');
INSERT INTO `t_stu_evaluation` VALUES (95, 28, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:08', '2021-04-06 19:04:08');
INSERT INTO `t_stu_evaluation` VALUES (96, 45, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:08', '2021-04-06 19:04:08');
INSERT INTO `t_stu_evaluation` VALUES (97, 26, '172.26.8.91', '131323123', 3, '2021-04-06 19:04:15', '2021-04-06 19:04:15');
INSERT INTO `t_stu_evaluation` VALUES (98, 53, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:15', '2021-04-06 19:04:15');
INSERT INTO `t_stu_evaluation` VALUES (99, 41, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:15', '2021-04-06 19:04:15');
INSERT INTO `t_stu_evaluation` VALUES (100, 49, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:18', '2021-04-06 19:04:18');
INSERT INTO `t_stu_evaluation` VALUES (101, 54, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:18', '2021-04-06 19:04:18');
INSERT INTO `t_stu_evaluation` VALUES (102, 31, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:18', '2021-04-06 19:04:18');
INSERT INTO `t_stu_evaluation` VALUES (103, 37, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:21', '2021-04-06 19:04:21');
INSERT INTO `t_stu_evaluation` VALUES (104, 54, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:21', '2021-04-06 19:04:21');
INSERT INTO `t_stu_evaluation` VALUES (105, 27, '172.26.8.91', '131323123', 3, '2021-04-06 19:04:21', '2021-04-06 19:04:21');
INSERT INTO `t_stu_evaluation` VALUES (106, 33, '172.26.8.91', '131323123', 3, '2021-04-06 19:04:23', '2021-04-06 19:04:23');
INSERT INTO `t_stu_evaluation` VALUES (107, 31, '172.26.8.91', '131323123', 2, '2021-04-06 19:04:23', '2021-04-06 19:04:23');
INSERT INTO `t_stu_evaluation` VALUES (108, 55, '172.26.8.91', '131323123', 3, '2021-04-06 19:04:23', '2021-04-06 19:04:23');
INSERT INTO `t_stu_evaluation` VALUES (109, 27, '172.26.8.91', '131323123', 2, '2021-04-06 19:04:26', '2021-04-06 19:04:26');
INSERT INTO `t_stu_evaluation` VALUES (110, 46, '172.26.8.91', '131323123', 3, '2021-04-06 19:04:26', '2021-04-06 19:04:26');
INSERT INTO `t_stu_evaluation` VALUES (111, 36, '172.26.8.91', '131323123', 3, '2021-04-06 19:04:26', '2021-04-06 19:04:26');
INSERT INTO `t_stu_evaluation` VALUES (112, 29, '172.26.8.91', '131323123', 3, '2021-04-06 19:04:29', '2021-04-06 19:04:29');
INSERT INTO `t_stu_evaluation` VALUES (113, 39, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:29', '2021-04-06 19:04:29');
INSERT INTO `t_stu_evaluation` VALUES (114, 49, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:29', '2021-04-06 19:04:29');
INSERT INTO `t_stu_evaluation` VALUES (115, 49, '172.26.8.91', '131323123', 2, '2021-04-06 19:04:32', '2021-04-06 19:04:32');
INSERT INTO `t_stu_evaluation` VALUES (116, 45, '172.26.8.91', '131323123', 3, '2021-04-06 19:04:32', '2021-04-06 19:04:32');
INSERT INTO `t_stu_evaluation` VALUES (117, 47, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:32', '2021-04-06 19:04:32');
INSERT INTO `t_stu_evaluation` VALUES (118, 52, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:35', '2021-04-06 19:04:35');
INSERT INTO `t_stu_evaluation` VALUES (119, 53, '172.26.8.91', '131323123', 3, '2021-04-06 19:04:35', '2021-04-06 19:04:35');
INSERT INTO `t_stu_evaluation` VALUES (120, 38, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:35', '2021-04-06 19:04:35');
INSERT INTO `t_stu_evaluation` VALUES (121, 46, '172.26.8.91', '131323123', 3, '2021-04-06 19:04:38', '2021-04-06 19:04:38');
INSERT INTO `t_stu_evaluation` VALUES (122, 47, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:38', '2021-04-06 19:04:38');
INSERT INTO `t_stu_evaluation` VALUES (123, 45, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:38', '2021-04-06 19:04:38');
INSERT INTO `t_stu_evaluation` VALUES (124, 29, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:41', '2021-04-06 19:04:41');
INSERT INTO `t_stu_evaluation` VALUES (125, 39, '172.26.8.91', '131323123', 2, '2021-04-06 19:04:41', '2021-04-06 19:04:41');
INSERT INTO `t_stu_evaluation` VALUES (126, 49, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:41', '2021-04-06 19:04:41');
INSERT INTO `t_stu_evaluation` VALUES (127, 42, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:43', '2021-04-06 19:04:43');
INSERT INTO `t_stu_evaluation` VALUES (128, 46, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:43', '2021-04-06 19:04:43');
INSERT INTO `t_stu_evaluation` VALUES (129, 53, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:43', '2021-04-06 19:04:43');
INSERT INTO `t_stu_evaluation` VALUES (130, 45, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:46', '2021-04-06 19:04:46');
INSERT INTO `t_stu_evaluation` VALUES (131, 47, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:46', '2021-04-06 19:04:46');
INSERT INTO `t_stu_evaluation` VALUES (132, 30, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:46', '2021-04-06 19:04:46');
INSERT INTO `t_stu_evaluation` VALUES (133, 33, '172.26.8.91', '131323123', 2, '2021-04-06 19:04:48', '2021-04-06 19:04:48');
INSERT INTO `t_stu_evaluation` VALUES (134, 53, '172.26.8.91', '131323123', 3, '2021-04-06 19:04:48', '2021-04-06 19:04:48');
INSERT INTO `t_stu_evaluation` VALUES (135, 55, '172.26.8.91', '131323123', 2, '2021-04-06 19:04:48', '2021-04-06 19:04:48');
INSERT INTO `t_stu_evaluation` VALUES (136, 30, '172.26.8.91', '131323123', 3, '2021-04-06 19:04:51', '2021-04-06 19:04:51');
INSERT INTO `t_stu_evaluation` VALUES (137, 36, '172.26.8.91', '131323123', 3, '2021-04-06 19:04:51', '2021-04-06 19:04:51');
INSERT INTO `t_stu_evaluation` VALUES (138, 26, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:51', '2021-04-06 19:04:51');
INSERT INTO `t_stu_evaluation` VALUES (139, 33, '172.26.8.91', '131323123', 2, '2021-04-06 19:04:54', '2021-04-06 19:04:54');
INSERT INTO `t_stu_evaluation` VALUES (140, 51, '172.26.8.91', '131323123', 3, '2021-04-06 19:04:54', '2021-04-06 19:04:54');
INSERT INTO `t_stu_evaluation` VALUES (141, 44, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:54', '2021-04-06 19:04:54');
INSERT INTO `t_stu_evaluation` VALUES (142, 55, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:57', '2021-04-06 19:04:57');
INSERT INTO `t_stu_evaluation` VALUES (143, 36, '172.26.8.91', '131323123', 2, '2021-04-06 19:04:57', '2021-04-06 19:04:57');
INSERT INTO `t_stu_evaluation` VALUES (144, 44, '172.26.8.91', '131323123', 4, '2021-04-06 19:04:57', '2021-04-06 19:04:57');
INSERT INTO `t_stu_evaluation` VALUES (145, 49, '172.26.8.91', '131323123', 3, '2021-04-06 19:05:02', '2021-04-06 19:05:02');
INSERT INTO `t_stu_evaluation` VALUES (146, 38, '172.26.8.91', '131323123', 4, '2021-04-06 19:05:02', '2021-04-06 19:05:02');
INSERT INTO `t_stu_evaluation` VALUES (147, 51, '172.26.8.91', '131323123', 4, '2021-04-06 19:05:02', '2021-04-06 19:05:02');
INSERT INTO `t_stu_evaluation` VALUES (148, 40, '172.26.8.91', '131323123', 3, '2021-04-06 19:05:05', '2021-04-06 19:05:05');
INSERT INTO `t_stu_evaluation` VALUES (149, 35, '172.26.8.91', '131323123', 4, '2021-04-06 19:05:05', '2021-04-06 19:05:05');
INSERT INTO `t_stu_evaluation` VALUES (150, 47, '172.26.8.91', '131323123', 4, '2021-04-06 19:05:05', '2021-04-06 19:05:05');
INSERT INTO `t_stu_evaluation` VALUES (151, 55, '172.26.8.91', '131323123', 4, '2021-04-06 19:05:09', '2021-04-06 19:05:09');
INSERT INTO `t_stu_evaluation` VALUES (152, 35, '172.26.8.91', '131323123', 3, '2021-04-06 19:05:09', '2021-04-06 19:05:09');
INSERT INTO `t_stu_evaluation` VALUES (153, 47, '172.26.8.91', '131323123', 4, '2021-04-06 19:05:09', '2021-04-06 19:05:09');
INSERT INTO `t_stu_evaluation` VALUES (154, 31, '172.26.8.91', '131323123', 4, '2021-04-06 19:05:12', '2021-04-06 19:05:12');
INSERT INTO `t_stu_evaluation` VALUES (155, 47, '172.26.8.91', '131323123', 3, '2021-04-06 19:05:12', '2021-04-06 19:05:12');
INSERT INTO `t_stu_evaluation` VALUES (156, 36, '172.26.8.91', '131323123', 4, '2021-04-06 19:05:12', '2021-04-06 19:05:12');
INSERT INTO `t_stu_evaluation` VALUES (157, 44, '172.26.8.91', '12312222323', 3, '2021-04-06 19:34:14', '2021-04-06 19:34:14');
INSERT INTO `t_stu_evaluation` VALUES (158, 46, '172.26.8.91', '12312222323', 3, '2021-04-06 19:34:14', '2021-04-06 19:34:14');
INSERT INTO `t_stu_evaluation` VALUES (159, 39, '172.26.8.91', '12312222323', 4, '2021-04-06 19:34:14', '2021-04-06 19:34:14');
INSERT INTO `t_stu_evaluation` VALUES (160, 28, '172.26.8.91', '12312222323', 3, '2021-04-06 19:34:14', '2021-04-06 19:34:14');
INSERT INTO `t_stu_evaluation` VALUES (161, 50, '172.26.8.91', '12312222323', 4, '2021-04-06 19:34:14', '2021-04-06 19:34:14');
INSERT INTO `t_stu_evaluation` VALUES (162, 37, '172.26.8.91', '1231222222', 3, '2021-04-06 19:37:38', '2021-04-06 19:37:38');
INSERT INTO `t_stu_evaluation` VALUES (163, 53, '172.26.8.91', '1231222222', 4, '2021-04-06 19:37:38', '2021-04-06 19:37:38');
INSERT INTO `t_stu_evaluation` VALUES (164, 48, '172.26.8.91', '1231222222', 4, '2021-04-06 19:37:38', '2021-04-06 19:37:38');
INSERT INTO `t_stu_evaluation` VALUES (165, 51, '172.26.8.91', '1231222222', 3, '2021-04-06 19:37:38', '2021-04-06 19:37:38');
INSERT INTO `t_stu_evaluation` VALUES (166, 46, '172.26.8.91', '1231222222', 4, '2021-04-06 19:37:38', '2021-04-06 19:37:38');
INSERT INTO `t_stu_evaluation` VALUES (167, 50, '172.26.8.91', '1231222222', 5, '2021-04-06 19:37:38', '2021-04-06 19:37:38');
INSERT INTO `t_stu_evaluation` VALUES (168, 40, '172.26.8.91', '1231222222', 3, '2021-04-06 19:37:38', '2021-04-06 19:37:38');
INSERT INTO `t_stu_evaluation` VALUES (169, 29, '172.26.8.91', '1231222222', 4, '2021-04-06 19:37:38', '2021-04-06 19:37:38');
INSERT INTO `t_stu_evaluation` VALUES (170, 26, '172.26.8.91', '1231222222', 4, '2021-04-06 19:37:53', '2021-04-06 19:37:53');
INSERT INTO `t_stu_evaluation` VALUES (171, 29, '172.26.8.91', '1231222222', 3, '2021-04-06 19:37:53', '2021-04-06 19:37:53');
INSERT INTO `t_stu_evaluation` VALUES (172, 30, '172.26.8.91', '1231222222', 4, '2021-04-06 19:37:53', '2021-04-06 19:37:53');
INSERT INTO `t_stu_evaluation` VALUES (173, 27, '172.26.8.91', '1231222222', 4, '2021-04-06 19:37:53', '2021-04-06 19:37:53');
INSERT INTO `t_stu_evaluation` VALUES (174, 35, '172.26.8.91', '1231222222', 3, '2021-04-06 19:37:53', '2021-04-06 19:37:53');
INSERT INTO `t_stu_evaluation` VALUES (175, 47, '172.26.8.91', '1231222222', 4, '2021-04-06 19:37:53', '2021-04-06 19:37:53');
INSERT INTO `t_stu_evaluation` VALUES (176, 33, '172.26.8.91', '1231222222', 4, '2021-04-06 19:37:53', '2021-04-06 19:37:53');
INSERT INTO `t_stu_evaluation` VALUES (177, 53, '172.26.8.91', '1231222222', 3, '2021-04-06 19:37:53', '2021-04-06 19:37:53');
INSERT INTO `t_stu_evaluation` VALUES (178, 33, '172.26.8.91', '2123122', 3, '2021-04-06 19:38:55', '2021-04-06 19:38:55');
INSERT INTO `t_stu_evaluation` VALUES (179, 44, '172.26.8.91', '2123122', 3, '2021-04-06 19:38:55', '2021-04-06 19:38:55');
INSERT INTO `t_stu_evaluation` VALUES (180, 26, '172.26.8.91', '2123122', 3, '2021-04-06 19:38:55', '2021-04-06 19:38:55');
INSERT INTO `t_stu_evaluation` VALUES (181, 43, '172.26.8.91', '2123122', 4, '2021-04-06 19:38:55', '2021-04-06 19:38:55');
INSERT INTO `t_stu_evaluation` VALUES (182, 29, '172.26.8.91', '2123122', 3, '2021-04-06 19:38:55', '2021-04-06 19:38:55');
INSERT INTO `t_stu_evaluation` VALUES (183, 51, '172.26.8.91', '2123122', 3, '2021-04-06 19:38:55', '2021-04-06 19:38:55');
INSERT INTO `t_stu_evaluation` VALUES (184, 34, '172.26.8.91', '2123122', 4, '2021-04-06 19:38:55', '2021-04-06 19:38:55');
INSERT INTO `t_stu_evaluation` VALUES (185, 28, '172.26.8.91', '2123122', 3, '2021-04-06 19:38:55', '2021-04-06 19:38:55');
INSERT INTO `t_stu_evaluation` VALUES (186, 26, '172.26.8.91', '2123122', 4, '2021-04-06 19:39:08', '2021-04-06 19:39:08');
INSERT INTO `t_stu_evaluation` VALUES (187, 55, '172.26.8.91', '2123122', 3, '2021-04-06 19:39:08', '2021-04-06 19:39:08');
INSERT INTO `t_stu_evaluation` VALUES (188, 29, '172.26.8.91', '2123122', 4, '2021-04-06 19:39:08', '2021-04-06 19:39:08');
INSERT INTO `t_stu_evaluation` VALUES (189, 48, '172.26.8.91', '2123122', 4, '2021-04-06 19:39:08', '2021-04-06 19:39:08');
INSERT INTO `t_stu_evaluation` VALUES (190, 36, '172.26.8.91', '2123122', 3, '2021-04-06 19:39:08', '2021-04-06 19:39:08');
INSERT INTO `t_stu_evaluation` VALUES (191, 44, '172.26.8.91', '2123122', 3, '2021-04-06 19:39:08', '2021-04-06 19:39:08');
INSERT INTO `t_stu_evaluation` VALUES (192, 39, '172.26.8.91', '2123122', 4, '2021-04-06 19:39:08', '2021-04-06 19:39:08');
INSERT INTO `t_stu_evaluation` VALUES (193, 51, '172.26.8.91', '2123122', 3, '2021-04-06 19:39:08', '2021-04-06 19:39:08');
INSERT INTO `t_stu_evaluation` VALUES (194, 47, '172.26.8.91', '21212313123', 3, '2021-04-07 10:35:11', '2021-04-07 10:35:11');
INSERT INTO `t_stu_evaluation` VALUES (195, 54, '172.26.8.91', '21212313123', 4, '2021-04-07 10:35:11', '2021-04-07 10:35:11');
INSERT INTO `t_stu_evaluation` VALUES (196, 59, '172.26.8.91', '21212313123', 3, '2021-04-07 10:35:11', '2021-04-07 10:35:11');
INSERT INTO `t_stu_evaluation` VALUES (197, 37, '172.26.8.91', '21212313123', 4, '2021-04-07 10:35:11', '2021-04-07 10:35:11');
INSERT INTO `t_stu_evaluation` VALUES (198, 30, '172.26.8.91', '21212313123', 4, '2021-04-07 10:35:11', '2021-04-07 10:35:11');
INSERT INTO `t_stu_evaluation` VALUES (199, 43, '172.26.8.91', '21212313123', 4, '2021-04-07 10:35:11', '2021-04-07 10:35:11');
INSERT INTO `t_stu_evaluation` VALUES (200, 35, '172.26.8.91', '21212313123', 4, '2021-04-07 10:35:11', '2021-04-07 10:35:11');
INSERT INTO `t_stu_evaluation` VALUES (201, 42, '172.26.8.91', '21212313123', 4, '2021-04-07 10:35:11', '2021-04-07 10:35:11');
INSERT INTO `t_stu_evaluation` VALUES (202, 51, '172.26.8.91', '21212313123', 4, '2021-04-07 10:35:37', '2021-04-07 10:35:37');
INSERT INTO `t_stu_evaluation` VALUES (203, 30, '172.26.8.91', '21212313123', 4, '2021-04-07 10:35:37', '2021-04-07 10:35:37');
INSERT INTO `t_stu_evaluation` VALUES (204, 28, '172.26.8.91', '21212313123', 3, '2021-04-07 10:35:37', '2021-04-07 10:35:37');
INSERT INTO `t_stu_evaluation` VALUES (205, 48, '172.26.8.91', '21212313123', 3, '2021-04-07 10:35:37', '2021-04-07 10:35:37');
INSERT INTO `t_stu_evaluation` VALUES (206, 49, '172.26.8.91', '21212313123', 2, '2021-04-07 10:35:37', '2021-04-07 10:35:37');
INSERT INTO `t_stu_evaluation` VALUES (207, 45, '172.26.8.91', '21212313123', 2, '2021-04-07 10:35:37', '2021-04-07 10:35:37');
INSERT INTO `t_stu_evaluation` VALUES (208, 36, '172.26.8.91', '21212313123', 4, '2021-04-07 10:35:37', '2021-04-07 10:35:37');
INSERT INTO `t_stu_evaluation` VALUES (209, 42, '172.26.8.91', '21212313123', 4, '2021-04-07 10:35:37', '2021-04-07 10:35:37');
INSERT INTO `t_stu_evaluation` VALUES (210, 47, '172.26.8.91', '21212313123', 3, '2021-04-07 10:35:47', '2021-04-07 10:35:47');
INSERT INTO `t_stu_evaluation` VALUES (211, 35, '172.26.8.91', '21212313123', 3, '2021-04-07 10:35:47', '2021-04-07 10:35:47');
INSERT INTO `t_stu_evaluation` VALUES (212, 30, '172.26.8.91', '21212313123', 4, '2021-04-07 10:35:47', '2021-04-07 10:35:47');
INSERT INTO `t_stu_evaluation` VALUES (213, 27, '172.26.8.91', '21212313123', 4, '2021-04-07 10:35:47', '2021-04-07 10:35:47');
INSERT INTO `t_stu_evaluation` VALUES (214, 28, '172.26.8.91', '21212313123', 3, '2021-04-07 10:35:47', '2021-04-07 10:35:47');
INSERT INTO `t_stu_evaluation` VALUES (215, 34, '172.26.8.91', '21212313123', 4, '2021-04-07 10:35:47', '2021-04-07 10:35:47');
INSERT INTO `t_stu_evaluation` VALUES (216, 39, '172.26.8.91', '21212313123', 3, '2021-04-07 10:35:47', '2021-04-07 10:35:47');
INSERT INTO `t_stu_evaluation` VALUES (217, 55, '172.26.8.91', '21212313123', 4, '2021-04-07 10:35:47', '2021-04-07 10:35:47');
INSERT INTO `t_stu_evaluation` VALUES (218, 27, '172.26.8.91', '21212313123', 2, '2021-04-07 10:36:06', '2021-04-07 10:36:06');
INSERT INTO `t_stu_evaluation` VALUES (219, 35, '172.26.8.91', '21212313123', 4, '2021-04-07 10:36:06', '2021-04-07 10:36:06');
INSERT INTO `t_stu_evaluation` VALUES (220, 38, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:06', '2021-04-07 10:36:06');
INSERT INTO `t_stu_evaluation` VALUES (221, 37, '172.26.8.91', '21212313123', 5, '2021-04-07 10:36:06', '2021-04-07 10:36:06');
INSERT INTO `t_stu_evaluation` VALUES (222, 54, '172.26.8.91', '21212313123', 5, '2021-04-07 10:36:06', '2021-04-07 10:36:06');
INSERT INTO `t_stu_evaluation` VALUES (223, 49, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:06', '2021-04-07 10:36:06');
INSERT INTO `t_stu_evaluation` VALUES (224, 39, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:06', '2021-04-07 10:36:06');
INSERT INTO `t_stu_evaluation` VALUES (225, 48, '172.26.8.91', '21212313123', 2, '2021-04-07 10:36:06', '2021-04-07 10:36:06');
INSERT INTO `t_stu_evaluation` VALUES (226, 40, '172.26.8.91', '21212313123', 2, '2021-04-07 10:36:13', '2021-04-07 10:36:13');
INSERT INTO `t_stu_evaluation` VALUES (227, 48, '172.26.8.91', '21212313123', 2, '2021-04-07 10:36:13', '2021-04-07 10:36:13');
INSERT INTO `t_stu_evaluation` VALUES (228, 27, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:13', '2021-04-07 10:36:13');
INSERT INTO `t_stu_evaluation` VALUES (229, 30, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:13', '2021-04-07 10:36:13');
INSERT INTO `t_stu_evaluation` VALUES (230, 36, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:13', '2021-04-07 10:36:13');
INSERT INTO `t_stu_evaluation` VALUES (231, 52, '172.26.8.91', '21212313123', 4, '2021-04-07 10:36:13', '2021-04-07 10:36:13');
INSERT INTO `t_stu_evaluation` VALUES (232, 39, '172.26.8.91', '21212313123', 4, '2021-04-07 10:36:13', '2021-04-07 10:36:13');
INSERT INTO `t_stu_evaluation` VALUES (233, 38, '172.26.8.91', '21212313123', 2, '2021-04-07 10:36:13', '2021-04-07 10:36:13');
INSERT INTO `t_stu_evaluation` VALUES (234, 45, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:20', '2021-04-07 10:36:20');
INSERT INTO `t_stu_evaluation` VALUES (235, 26, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:20', '2021-04-07 10:36:20');
INSERT INTO `t_stu_evaluation` VALUES (236, 31, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:20', '2021-04-07 10:36:20');
INSERT INTO `t_stu_evaluation` VALUES (237, 59, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:20', '2021-04-07 10:36:20');
INSERT INTO `t_stu_evaluation` VALUES (238, 50, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:20', '2021-04-07 10:36:20');
INSERT INTO `t_stu_evaluation` VALUES (239, 42, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:20', '2021-04-07 10:36:20');
INSERT INTO `t_stu_evaluation` VALUES (240, 58, '172.26.8.91', '21212313123', 2, '2021-04-07 10:36:20', '2021-04-07 10:36:20');
INSERT INTO `t_stu_evaluation` VALUES (241, 54, '172.26.8.91', '21212313123', 4, '2021-04-07 10:36:20', '2021-04-07 10:36:20');
INSERT INTO `t_stu_evaluation` VALUES (242, 53, '172.26.8.91', '21212313123', 2, '2021-04-07 10:36:27', '2021-04-07 10:36:27');
INSERT INTO `t_stu_evaluation` VALUES (243, 42, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:27', '2021-04-07 10:36:27');
INSERT INTO `t_stu_evaluation` VALUES (244, 36, '172.26.8.91', '21212313123', 2, '2021-04-07 10:36:27', '2021-04-07 10:36:27');
INSERT INTO `t_stu_evaluation` VALUES (245, 31, '172.26.8.91', '21212313123', 4, '2021-04-07 10:36:27', '2021-04-07 10:36:27');
INSERT INTO `t_stu_evaluation` VALUES (246, 26, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:27', '2021-04-07 10:36:27');
INSERT INTO `t_stu_evaluation` VALUES (247, 40, '172.26.8.91', '21212313123', 4, '2021-04-07 10:36:27', '2021-04-07 10:36:27');
INSERT INTO `t_stu_evaluation` VALUES (248, 45, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:27', '2021-04-07 10:36:27');
INSERT INTO `t_stu_evaluation` VALUES (249, 34, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:27', '2021-04-07 10:36:27');
INSERT INTO `t_stu_evaluation` VALUES (250, 45, '172.26.8.91', '21212313123', 2, '2021-04-07 10:36:34', '2021-04-07 10:36:34');
INSERT INTO `t_stu_evaluation` VALUES (251, 42, '172.26.8.91', '21212313123', 4, '2021-04-07 10:36:34', '2021-04-07 10:36:34');
INSERT INTO `t_stu_evaluation` VALUES (252, 27, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:34', '2021-04-07 10:36:34');
INSERT INTO `t_stu_evaluation` VALUES (253, 36, '172.26.8.91', '21212313123', 4, '2021-04-07 10:36:34', '2021-04-07 10:36:34');
INSERT INTO `t_stu_evaluation` VALUES (254, 55, '172.26.8.91', '21212313123', 2, '2021-04-07 10:36:34', '2021-04-07 10:36:34');
INSERT INTO `t_stu_evaluation` VALUES (255, 52, '172.26.8.91', '21212313123', 4, '2021-04-07 10:36:34', '2021-04-07 10:36:34');
INSERT INTO `t_stu_evaluation` VALUES (256, 43, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:34', '2021-04-07 10:36:34');
INSERT INTO `t_stu_evaluation` VALUES (257, 53, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:34', '2021-04-07 10:36:34');
INSERT INTO `t_stu_evaluation` VALUES (258, 36, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:40', '2021-04-07 10:36:40');
INSERT INTO `t_stu_evaluation` VALUES (259, 49, '172.26.8.91', '21212313123', 4, '2021-04-07 10:36:40', '2021-04-07 10:36:40');
INSERT INTO `t_stu_evaluation` VALUES (260, 48, '172.26.8.91', '21212313123', 4, '2021-04-07 10:36:40', '2021-04-07 10:36:40');
INSERT INTO `t_stu_evaluation` VALUES (261, 59, '172.26.8.91', '21212313123', 2, '2021-04-07 10:36:40', '2021-04-07 10:36:40');
INSERT INTO `t_stu_evaluation` VALUES (262, 50, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:40', '2021-04-07 10:36:40');
INSERT INTO `t_stu_evaluation` VALUES (263, 54, '172.26.8.91', '21212313123', 4, '2021-04-07 10:36:40', '2021-04-07 10:36:40');
INSERT INTO `t_stu_evaluation` VALUES (264, 34, '172.26.8.91', '21212313123', 4, '2021-04-07 10:36:40', '2021-04-07 10:36:40');
INSERT INTO `t_stu_evaluation` VALUES (265, 31, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:40', '2021-04-07 10:36:40');
INSERT INTO `t_stu_evaluation` VALUES (266, 47, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:49', '2021-04-07 10:36:49');
INSERT INTO `t_stu_evaluation` VALUES (267, 34, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:49', '2021-04-07 10:36:49');
INSERT INTO `t_stu_evaluation` VALUES (268, 40, '172.26.8.91', '21212313123', 4, '2021-04-07 10:36:49', '2021-04-07 10:36:49');
INSERT INTO `t_stu_evaluation` VALUES (269, 37, '172.26.8.91', '21212313123', 2, '2021-04-07 10:36:49', '2021-04-07 10:36:49');
INSERT INTO `t_stu_evaluation` VALUES (270, 39, '172.26.8.91', '21212313123', 4, '2021-04-07 10:36:49', '2021-04-07 10:36:49');
INSERT INTO `t_stu_evaluation` VALUES (271, 42, '172.26.8.91', '21212313123', 4, '2021-04-07 10:36:49', '2021-04-07 10:36:49');
INSERT INTO `t_stu_evaluation` VALUES (272, 59, '172.26.8.91', '21212313123', 3, '2021-04-07 10:36:49', '2021-04-07 10:36:49');
INSERT INTO `t_stu_evaluation` VALUES (273, 36, '172.26.8.91', '21212313123', 4, '2021-04-07 10:36:49', '2021-04-07 10:36:49');
INSERT INTO `t_stu_evaluation` VALUES (274, 26, '172.26.8.91', '3223434333', 2, '2021-04-07 10:37:47', '2021-04-07 10:37:47');
INSERT INTO `t_stu_evaluation` VALUES (275, 44, '172.26.8.91', '3223434333', 3, '2021-04-07 10:37:47', '2021-04-07 10:37:47');
INSERT INTO `t_stu_evaluation` VALUES (276, 53, '172.26.8.91', '3223434333', 4, '2021-04-07 10:37:47', '2021-04-07 10:37:47');
INSERT INTO `t_stu_evaluation` VALUES (277, 33, '172.26.8.91', '3223434333', 2, '2021-04-07 10:37:47', '2021-04-07 10:37:47');
INSERT INTO `t_stu_evaluation` VALUES (278, 59, '172.26.8.91', '3223434333', 3, '2021-04-07 10:37:47', '2021-04-07 10:37:47');
INSERT INTO `t_stu_evaluation` VALUES (279, 41, '172.26.8.91', '3223434333', 4, '2021-04-07 10:37:47', '2021-04-07 10:37:47');
INSERT INTO `t_stu_evaluation` VALUES (280, 49, '172.26.8.91', '3223434333', 3, '2021-04-07 10:37:47', '2021-04-07 10:37:47');
INSERT INTO `t_stu_evaluation` VALUES (281, 31, '172.26.8.91', '3223434333', 4, '2021-04-07 10:37:47', '2021-04-07 10:37:47');
INSERT INTO `t_stu_evaluation` VALUES (282, 58, '172.26.8.91', '3223434333', 2, '2021-04-07 10:37:54', '2021-04-07 10:37:54');
INSERT INTO `t_stu_evaluation` VALUES (283, 33, '172.26.8.91', '3223434333', 4, '2021-04-07 10:37:54', '2021-04-07 10:37:54');
INSERT INTO `t_stu_evaluation` VALUES (284, 34, '172.26.8.91', '3223434333', 4, '2021-04-07 10:37:54', '2021-04-07 10:37:54');
INSERT INTO `t_stu_evaluation` VALUES (285, 56, '172.26.8.91', '3223434333', 2, '2021-04-07 10:37:54', '2021-04-07 10:37:54');
INSERT INTO `t_stu_evaluation` VALUES (286, 38, '172.26.8.91', '3223434333', 4, '2021-04-07 10:37:54', '2021-04-07 10:37:54');
INSERT INTO `t_stu_evaluation` VALUES (287, 35, '172.26.8.91', '3223434333', 3, '2021-04-07 10:37:54', '2021-04-07 10:37:54');
INSERT INTO `t_stu_evaluation` VALUES (288, 45, '172.26.8.91', '3223434333', 4, '2021-04-07 10:37:54', '2021-04-07 10:37:54');
INSERT INTO `t_stu_evaluation` VALUES (289, 39, '172.26.8.91', '3223434333', 4, '2021-04-07 10:37:54', '2021-04-07 10:37:54');
INSERT INTO `t_stu_evaluation` VALUES (290, 46, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:00', '2021-04-07 10:38:00');
INSERT INTO `t_stu_evaluation` VALUES (291, 48, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:00', '2021-04-07 10:38:00');
INSERT INTO `t_stu_evaluation` VALUES (292, 31, '172.26.8.91', '3223434333', 4, '2021-04-07 10:38:00', '2021-04-07 10:38:00');
INSERT INTO `t_stu_evaluation` VALUES (293, 55, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:00', '2021-04-07 10:38:00');
INSERT INTO `t_stu_evaluation` VALUES (294, 45, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:00', '2021-04-07 10:38:00');
INSERT INTO `t_stu_evaluation` VALUES (295, 30, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:00', '2021-04-07 10:38:00');
INSERT INTO `t_stu_evaluation` VALUES (296, 36, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:00', '2021-04-07 10:38:00');
INSERT INTO `t_stu_evaluation` VALUES (297, 44, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:00', '2021-04-07 10:38:00');
INSERT INTO `t_stu_evaluation` VALUES (298, 35, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:07', '2021-04-07 10:38:07');
INSERT INTO `t_stu_evaluation` VALUES (299, 31, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:07', '2021-04-07 10:38:07');
INSERT INTO `t_stu_evaluation` VALUES (300, 58, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:07', '2021-04-07 10:38:07');
INSERT INTO `t_stu_evaluation` VALUES (301, 51, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:07', '2021-04-07 10:38:07');
INSERT INTO `t_stu_evaluation` VALUES (302, 43, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:07', '2021-04-07 10:38:07');
INSERT INTO `t_stu_evaluation` VALUES (303, 34, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:07', '2021-04-07 10:38:07');
INSERT INTO `t_stu_evaluation` VALUES (304, 52, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:07', '2021-04-07 10:38:07');
INSERT INTO `t_stu_evaluation` VALUES (305, 56, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:07', '2021-04-07 10:38:07');
INSERT INTO `t_stu_evaluation` VALUES (306, 59, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:14', '2021-04-07 10:38:14');
INSERT INTO `t_stu_evaluation` VALUES (307, 43, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:14', '2021-04-07 10:38:14');
INSERT INTO `t_stu_evaluation` VALUES (308, 38, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:14', '2021-04-07 10:38:14');
INSERT INTO `t_stu_evaluation` VALUES (309, 39, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:14', '2021-04-07 10:38:14');
INSERT INTO `t_stu_evaluation` VALUES (310, 51, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:14', '2021-04-07 10:38:14');
INSERT INTO `t_stu_evaluation` VALUES (311, 52, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:14', '2021-04-07 10:38:14');
INSERT INTO `t_stu_evaluation` VALUES (312, 55, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:14', '2021-04-07 10:38:14');
INSERT INTO `t_stu_evaluation` VALUES (313, 56, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:14', '2021-04-07 10:38:14');
INSERT INTO `t_stu_evaluation` VALUES (314, 39, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:20', '2021-04-07 10:38:20');
INSERT INTO `t_stu_evaluation` VALUES (315, 48, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:20', '2021-04-07 10:38:20');
INSERT INTO `t_stu_evaluation` VALUES (316, 58, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:20', '2021-04-07 10:38:20');
INSERT INTO `t_stu_evaluation` VALUES (317, 52, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:20', '2021-04-07 10:38:20');
INSERT INTO `t_stu_evaluation` VALUES (318, 56, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:20', '2021-04-07 10:38:20');
INSERT INTO `t_stu_evaluation` VALUES (319, 34, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:20', '2021-04-07 10:38:20');
INSERT INTO `t_stu_evaluation` VALUES (320, 55, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:20', '2021-04-07 10:38:20');
INSERT INTO `t_stu_evaluation` VALUES (321, 38, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:20', '2021-04-07 10:38:20');
INSERT INTO `t_stu_evaluation` VALUES (322, 42, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:27', '2021-04-07 10:38:27');
INSERT INTO `t_stu_evaluation` VALUES (323, 29, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:27', '2021-04-07 10:38:27');
INSERT INTO `t_stu_evaluation` VALUES (324, 38, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:27', '2021-04-07 10:38:27');
INSERT INTO `t_stu_evaluation` VALUES (325, 34, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:27', '2021-04-07 10:38:27');
INSERT INTO `t_stu_evaluation` VALUES (326, 48, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:27', '2021-04-07 10:38:27');
INSERT INTO `t_stu_evaluation` VALUES (327, 47, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:27', '2021-04-07 10:38:27');
INSERT INTO `t_stu_evaluation` VALUES (328, 33, '172.26.8.91', '3223434333', 4, '2021-04-07 10:38:27', '2021-04-07 10:38:27');
INSERT INTO `t_stu_evaluation` VALUES (329, 30, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:27', '2021-04-07 10:38:27');
INSERT INTO `t_stu_evaluation` VALUES (330, 29, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:34', '2021-04-07 10:38:34');
INSERT INTO `t_stu_evaluation` VALUES (331, 37, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:34', '2021-04-07 10:38:34');
INSERT INTO `t_stu_evaluation` VALUES (332, 51, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:34', '2021-04-07 10:38:34');
INSERT INTO `t_stu_evaluation` VALUES (333, 58, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:34', '2021-04-07 10:38:34');
INSERT INTO `t_stu_evaluation` VALUES (334, 28, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:34', '2021-04-07 10:38:34');
INSERT INTO `t_stu_evaluation` VALUES (335, 35, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:34', '2021-04-07 10:38:34');
INSERT INTO `t_stu_evaluation` VALUES (336, 39, '172.26.8.91', '3223434333', 4, '2021-04-07 10:38:34', '2021-04-07 10:38:34');
INSERT INTO `t_stu_evaluation` VALUES (337, 38, '172.26.8.91', '3223434333', 4, '2021-04-07 10:38:34', '2021-04-07 10:38:34');
INSERT INTO `t_stu_evaluation` VALUES (338, 50, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:41', '2021-04-07 10:38:41');
INSERT INTO `t_stu_evaluation` VALUES (339, 27, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:41', '2021-04-07 10:38:41');
INSERT INTO `t_stu_evaluation` VALUES (340, 35, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:41', '2021-04-07 10:38:41');
INSERT INTO `t_stu_evaluation` VALUES (341, 51, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:41', '2021-04-07 10:38:41');
INSERT INTO `t_stu_evaluation` VALUES (342, 33, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:41', '2021-04-07 10:38:41');
INSERT INTO `t_stu_evaluation` VALUES (343, 45, '172.26.8.91', '3223434333', 4, '2021-04-07 10:38:41', '2021-04-07 10:38:41');
INSERT INTO `t_stu_evaluation` VALUES (344, 44, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:41', '2021-04-07 10:38:41');
INSERT INTO `t_stu_evaluation` VALUES (345, 30, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:41', '2021-04-07 10:38:41');
INSERT INTO `t_stu_evaluation` VALUES (346, 29, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:48', '2021-04-07 10:38:48');
INSERT INTO `t_stu_evaluation` VALUES (347, 46, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:48', '2021-04-07 10:38:48');
INSERT INTO `t_stu_evaluation` VALUES (348, 33, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:48', '2021-04-07 10:38:48');
INSERT INTO `t_stu_evaluation` VALUES (349, 37, '172.26.8.91', '3223434333', 4, '2021-04-07 10:38:48', '2021-04-07 10:38:48');
INSERT INTO `t_stu_evaluation` VALUES (350, 38, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:48', '2021-04-07 10:38:48');
INSERT INTO `t_stu_evaluation` VALUES (351, 36, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:48', '2021-04-07 10:38:48');
INSERT INTO `t_stu_evaluation` VALUES (352, 45, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:48', '2021-04-07 10:38:48');
INSERT INTO `t_stu_evaluation` VALUES (353, 40, '172.26.8.91', '3223434333', 4, '2021-04-07 10:38:48', '2021-04-07 10:38:48');
INSERT INTO `t_stu_evaluation` VALUES (354, 36, '172.26.8.91', '3223434333', 2, '2021-04-07 10:38:54', '2021-04-07 10:38:54');
INSERT INTO `t_stu_evaluation` VALUES (355, 42, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:54', '2021-04-07 10:38:54');
INSERT INTO `t_stu_evaluation` VALUES (356, 51, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:54', '2021-04-07 10:38:54');
INSERT INTO `t_stu_evaluation` VALUES (357, 54, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:54', '2021-04-07 10:38:54');
INSERT INTO `t_stu_evaluation` VALUES (358, 47, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:54', '2021-04-07 10:38:54');
INSERT INTO `t_stu_evaluation` VALUES (359, 34, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:54', '2021-04-07 10:38:54');
INSERT INTO `t_stu_evaluation` VALUES (360, 44, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:54', '2021-04-07 10:38:54');
INSERT INTO `t_stu_evaluation` VALUES (361, 58, '172.26.8.91', '3223434333', 3, '2021-04-07 10:38:54', '2021-04-07 10:38:54');
INSERT INTO `t_stu_evaluation` VALUES (362, 57, '172.26.8.91', '3223434333', 2, '2021-04-07 10:39:01', '2021-04-07 10:39:01');
INSERT INTO `t_stu_evaluation` VALUES (363, 59, '172.26.8.91', '3223434333', 2, '2021-04-07 10:39:01', '2021-04-07 10:39:01');
INSERT INTO `t_stu_evaluation` VALUES (364, 47, '172.26.8.91', '3223434333', 3, '2021-04-07 10:39:01', '2021-04-07 10:39:01');
INSERT INTO `t_stu_evaluation` VALUES (365, 43, '172.26.8.91', '3223434333', 3, '2021-04-07 10:39:01', '2021-04-07 10:39:01');
INSERT INTO `t_stu_evaluation` VALUES (366, 55, '172.26.8.91', '3223434333', 3, '2021-04-07 10:39:01', '2021-04-07 10:39:01');
INSERT INTO `t_stu_evaluation` VALUES (367, 26, '172.26.8.91', '3223434333', 3, '2021-04-07 10:39:01', '2021-04-07 10:39:01');
INSERT INTO `t_stu_evaluation` VALUES (368, 58, '172.26.8.91', '3223434333', 3, '2021-04-07 10:39:01', '2021-04-07 10:39:01');
INSERT INTO `t_stu_evaluation` VALUES (369, 52, '172.26.8.91', '3223434333', 4, '2021-04-07 10:39:01', '2021-04-07 10:39:01');
INSERT INTO `t_stu_evaluation` VALUES (370, 59, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:30', '2021-04-07 14:29:30');
INSERT INTO `t_stu_evaluation` VALUES (371, 55, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:30', '2021-04-07 14:29:30');
INSERT INTO `t_stu_evaluation` VALUES (372, 46, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:30', '2021-04-07 14:29:30');
INSERT INTO `t_stu_evaluation` VALUES (373, 29, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:30', '2021-04-07 14:29:30');
INSERT INTO `t_stu_evaluation` VALUES (374, 56, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:30', '2021-04-07 14:29:30');
INSERT INTO `t_stu_evaluation` VALUES (375, 26, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:30', '2021-04-07 14:29:30');
INSERT INTO `t_stu_evaluation` VALUES (376, 44, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:30', '2021-04-07 14:29:30');
INSERT INTO `t_stu_evaluation` VALUES (377, 53, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:30', '2021-04-07 14:29:30');
INSERT INTO `t_stu_evaluation` VALUES (378, 49, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:37', '2021-04-07 14:29:37');
INSERT INTO `t_stu_evaluation` VALUES (379, 56, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:37', '2021-04-07 14:29:37');
INSERT INTO `t_stu_evaluation` VALUES (380, 36, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:37', '2021-04-07 14:29:37');
INSERT INTO `t_stu_evaluation` VALUES (381, 34, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:37', '2021-04-07 14:29:37');
INSERT INTO `t_stu_evaluation` VALUES (382, 53, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:37', '2021-04-07 14:29:37');
INSERT INTO `t_stu_evaluation` VALUES (383, 31, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:37', '2021-04-07 14:29:37');
INSERT INTO `t_stu_evaluation` VALUES (384, 55, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:37', '2021-04-07 14:29:37');
INSERT INTO `t_stu_evaluation` VALUES (385, 29, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:37', '2021-04-07 14:29:37');
INSERT INTO `t_stu_evaluation` VALUES (386, 28, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:44', '2021-04-07 14:29:44');
INSERT INTO `t_stu_evaluation` VALUES (387, 37, '172.26.8.91', '534535345', 2, '2021-04-07 14:29:44', '2021-04-07 14:29:44');
INSERT INTO `t_stu_evaluation` VALUES (388, 30, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:44', '2021-04-07 14:29:44');
INSERT INTO `t_stu_evaluation` VALUES (389, 40, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:44', '2021-04-07 14:29:44');
INSERT INTO `t_stu_evaluation` VALUES (390, 56, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:44', '2021-04-07 14:29:44');
INSERT INTO `t_stu_evaluation` VALUES (391, 45, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:44', '2021-04-07 14:29:44');
INSERT INTO `t_stu_evaluation` VALUES (392, 54, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:44', '2021-04-07 14:29:44');
INSERT INTO `t_stu_evaluation` VALUES (393, 29, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:44', '2021-04-07 14:29:44');
INSERT INTO `t_stu_evaluation` VALUES (394, 51, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:51', '2021-04-07 14:29:51');
INSERT INTO `t_stu_evaluation` VALUES (395, 43, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:51', '2021-04-07 14:29:51');
INSERT INTO `t_stu_evaluation` VALUES (396, 29, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:51', '2021-04-07 14:29:51');
INSERT INTO `t_stu_evaluation` VALUES (397, 27, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:51', '2021-04-07 14:29:51');
INSERT INTO `t_stu_evaluation` VALUES (398, 34, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:51', '2021-04-07 14:29:51');
INSERT INTO `t_stu_evaluation` VALUES (399, 37, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:51', '2021-04-07 14:29:51');
INSERT INTO `t_stu_evaluation` VALUES (400, 36, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:51', '2021-04-07 14:29:51');
INSERT INTO `t_stu_evaluation` VALUES (401, 50, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:51', '2021-04-07 14:29:51');
INSERT INTO `t_stu_evaluation` VALUES (402, 46, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:58', '2021-04-07 14:29:58');
INSERT INTO `t_stu_evaluation` VALUES (403, 55, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:58', '2021-04-07 14:29:58');
INSERT INTO `t_stu_evaluation` VALUES (404, 41, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:58', '2021-04-07 14:29:58');
INSERT INTO `t_stu_evaluation` VALUES (405, 28, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:58', '2021-04-07 14:29:58');
INSERT INTO `t_stu_evaluation` VALUES (406, 30, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:58', '2021-04-07 14:29:58');
INSERT INTO `t_stu_evaluation` VALUES (407, 59, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:58', '2021-04-07 14:29:58');
INSERT INTO `t_stu_evaluation` VALUES (408, 42, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:58', '2021-04-07 14:29:58');
INSERT INTO `t_stu_evaluation` VALUES (409, 45, '172.26.8.91', '534535345', 1, '2021-04-07 14:29:58', '2021-04-07 14:29:58');
INSERT INTO `t_stu_evaluation` VALUES (410, 37, '172.26.8.91', '534535345', 1, '2021-04-07 14:30:06', '2021-04-07 14:30:06');
INSERT INTO `t_stu_evaluation` VALUES (411, 34, '172.26.8.91', '534535345', 1, '2021-04-07 14:30:06', '2021-04-07 14:30:06');
INSERT INTO `t_stu_evaluation` VALUES (412, 54, '172.26.8.91', '534535345', 1, '2021-04-07 14:30:06', '2021-04-07 14:30:06');
INSERT INTO `t_stu_evaluation` VALUES (413, 28, '172.26.8.91', '534535345', 1, '2021-04-07 14:30:06', '2021-04-07 14:30:06');
INSERT INTO `t_stu_evaluation` VALUES (414, 33, '172.26.8.91', '534535345', 1, '2021-04-07 14:30:06', '2021-04-07 14:30:06');
INSERT INTO `t_stu_evaluation` VALUES (415, 39, '172.26.8.91', '534535345', 1, '2021-04-07 14:30:06', '2021-04-07 14:30:06');
INSERT INTO `t_stu_evaluation` VALUES (416, 26, '172.26.8.91', '534535345', 1, '2021-04-07 14:30:06', '2021-04-07 14:30:06');
INSERT INTO `t_stu_evaluation` VALUES (417, 44, '172.26.8.91', '534535345', 1, '2021-04-07 14:30:06', '2021-04-07 14:30:06');
INSERT INTO `t_stu_evaluation` VALUES (418, 58, '172.26.8.91', '534535345', 1, '2021-04-07 14:30:13', '2021-04-07 14:30:13');
INSERT INTO `t_stu_evaluation` VALUES (419, 30, '172.26.8.91', '534535345', 1, '2021-04-07 14:30:13', '2021-04-07 14:30:13');
INSERT INTO `t_stu_evaluation` VALUES (420, 52, '172.26.8.91', '534535345', 1, '2021-04-07 14:30:13', '2021-04-07 14:30:13');
INSERT INTO `t_stu_evaluation` VALUES (421, 41, '172.26.8.91', '534535345', 1, '2021-04-07 14:30:13', '2021-04-07 14:30:13');
INSERT INTO `t_stu_evaluation` VALUES (422, 57, '172.26.8.91', '534535345', 1, '2021-04-07 14:30:13', '2021-04-07 14:30:13');
INSERT INTO `t_stu_evaluation` VALUES (423, 35, '172.26.8.91', '534535345', 1, '2021-04-07 14:30:13', '2021-04-07 14:30:13');
INSERT INTO `t_stu_evaluation` VALUES (424, 26, '172.26.8.91', '534535345', 1, '2021-04-07 14:30:13', '2021-04-07 14:30:13');
INSERT INTO `t_stu_evaluation` VALUES (425, 45, '172.26.8.91', '534535345', 1, '2021-04-07 14:30:13', '2021-04-07 14:30:13');

-- ----------------------------
-- Table structure for t_target
-- ----------------------------
DROP TABLE IF EXISTS `t_target`;
CREATE TABLE `t_target`  (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `discribe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '描述',
  `req_id` int(3) NULL DEFAULT NULL COMMENT '关联毕业要求',
  `year` int(4) NULL DEFAULT NULL COMMENT '年份',
  `sys_grade` double(10, 3) NULL DEFAULT 0.000 COMMENT '系统计算成绩',
  `stu_grade` double(10, 3) NULL DEFAULT 0.000 COMMENT '学生评价成绩',
  `created_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `req`(`req_id`) USING BTREE,
  CONSTRAINT `targetksy` FOREIGN KEY (`req_id`) REFERENCES `t_grad_requirement` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 232 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_target
-- ----------------------------
INSERT INTO `t_target` VALUES (201, ' 具备数学知识、自然科学的基本知识，能够对复杂软件工程问题进行建模和求解。', 21, 2021, 0.000, 0.720, '2021-03-23 15:07:41', '2021-03-23 15:07:41');
INSERT INTO `t_target` VALUES (202, '具备软件工程基础知识，能够对复杂软件工程问题进行描述和分析。', 21, 2021, 0.000, 0.720, '2021-03-23 15:07:41', '2021-03-23 15:07:41');
INSERT INTO `t_target` VALUES (203, '掌握软件工程专业知识，并能够将软件工程基础理论、观点和方法应用到复杂工程问题的设计和解决当中。', 21, 2021, 0.000, 0.720, '2021-03-23 15:07:41', '2021-03-23 15:07:41');
INSERT INTO `t_target` VALUES (204, '能结合数理知识、专业知识、工程知识，采用恰当的方法对软件工程有关复杂问题设计、开发和改进。', 21, 2021, 0.000, 0.720, '2021-03-23 15:07:41', '2021-03-23 15:07:41');
INSERT INTO `t_target` VALUES (205, '能够将数理知识、自然科学应用到复杂软件工程问题中，并能理解和识别复杂软件工程问题。', 22, 2021, 0.000, 0.720, '2021-03-23 15:08:23', '2021-03-23 15:08:23');
INSERT INTO `t_target` VALUES (206, '能够将软件工程基础知识、专业知识应用到复杂软件工程问题中，并能正确描述和表达复杂软件工程问题。', 22, 2021, 0.000, 0.720, '2021-03-23 15:08:23', '2021-03-23 15:08:23');
INSERT INTO `t_target` VALUES (207, '能够应用数学、自然科学和工程科学知识，结合查阅文献对复杂软件工程问题进行研究和分析，并获得有效结论。', 22, 2021, 0.000, 0.720, '2021-03-23 15:08:23', '2021-03-23 15:08:23');
INSERT INTO `t_target` VALUES (208, '掌握设计、开发软件系统和组件模块的基本方法和技术，了解可能影响设计/开发解决方案的因素。', 23, 2021, 0.000, 0.720, '2021-03-23 15:09:08', '2021-03-23 15:09:08');
INSERT INTO `t_target` VALUES (209, '能够根据实际需求设计复杂软件工程问题的解决方案，并能够综合考虑社会、健康、安全、法律、文化及环境等制约因素。', 23, 2021, 0.000, 0.720, '2021-03-23 15:09:08', '2021-03-23 15:09:08');
INSERT INTO `t_target` VALUES (210, '能够综合运用所学专业知识，对特定的软件系统、模块和组件进行设计和实现，并在设计环节中体现一定的创新意识。', 23, 2021, 0.000, 0.720, '2021-03-23 15:09:08', '2021-03-23 15:09:08');
INSERT INTO `t_target` VALUES (211, '能够运用软件工程专业知识来验证设计方案的可行性和合理性，并能通过实验、实践等验证设计方案的正确性。', 23, 2021, 0.000, 0.720, '2021-03-23 15:09:08', '2021-03-23 15:09:08');
INSERT INTO `t_target` VALUES (212, '能够识别计算机软硬件系统组成，理解软件设计思路和基本原理，并将之运用到复杂软件工程问题的研究中。', 24, 2021, 0.159, 0.720, '2021-03-23 15:09:48', '2021-03-23 15:09:48');
INSERT INTO `t_target` VALUES (213, '能够采用科学方法来建立软件模型、设计实验方案，搭建软件开发或实验所需环境并开展实验。', 24, 2021, 0.000, 0.720, '2021-03-23 15:09:48', '2021-03-23 15:09:48');
INSERT INTO `t_target` VALUES (214, '能够对实验结果进行分析和解释，能够通过信息综合得到有效、合理的结论。', 24, 2021, 0.000, 0.720, '2021-03-23 15:09:48', '2021-03-23 15:09:48');
INSERT INTO `t_target` VALUES (215, '了解复杂软件工程问题所需的技术、资源、设备设施、开发工具、工程工具、模拟软件和信息技术工具，并能理解其局限性。', 25, 2021, 0.000, 0.720, '2021-03-23 15:10:30', '2021-03-23 15:10:30');
INSERT INTO `t_target` VALUES (216, '能够针对复杂软件工程问题，开发或选择与使用恰当的现代工具进行分析、设计、预测与模拟，并能理解与分析其局限性。', 25, 2021, 0.000, 0.720, '2021-03-23 15:10:30', '2021-03-23 15:10:30');
INSERT INTO `t_target` VALUES (217, '了解软件工程相关领域的技术标准、产业政策、法律法规，并能理解软件的应用领域和社会文化会对工程活动造成影响。', 26, 2021, 0.000, 0.720, '2021-03-23 15:11:10', '2021-03-23 15:11:10');
INSERT INTO `t_target` VALUES (218, '能够在解决复杂软件工程问题的过程中考虑社会、健康、安全、法律以及文化等制约因素，分析、评价这些因素对项目实施的影响。', 26, 2021, 0.000, 0.720, '2021-03-23 15:11:10', '2021-03-23 15:11:10');
INSERT INTO `t_target` VALUES (219, ' 能够分析、评价工程实践对社会、健康、安全、法律以及文化的影响，并理解应承担的责任。', 26, 2021, 0.740, 0.720, '2021-03-23 15:11:10', '2021-03-23 15:11:10');
INSERT INTO `t_target` VALUES (220, '了解环境保护、社会可持续发展的理念和内涵，能够理解软件工程问题的专业实践对环境保护和可持续发展的影响。', 27, 2021, 0.000, 0.720, '2021-03-23 15:11:44', '2021-03-23 15:11:44');
INSERT INTO `t_target` VALUES (221, '能够评价复杂软件工程问题的专业工程实践对环境保护和社会可持续发展的影响，并能够在解决方案中考虑环境和可持续发展问题。', 27, 2021, 0.000, 0.720, '2021-03-23 15:11:44', '2021-03-23 15:11:44');
INSERT INTO `t_target` VALUES (222, '具有人文社会科学素养、社会责任感，理解个人在历史以及社会、自然环境中的地位与责任。', 28, 2021, 0.438, 0.720, '2021-03-23 15:12:25', '2021-03-23 15:12:25');
INSERT INTO `t_target` VALUES (223, '理解软件工程师的职业素质、道德规范与责任，并能够在工程实践中履行职责和责任。', 28, 2021, 0.426, 0.720, '2021-03-23 15:12:25', '2021-03-23 15:12:25');
INSERT INTO `t_target` VALUES (224, '能够理解团队的意义，了解软件项目团队的角色划分和责任，具有团队合作意识。', 29, 2021, 0.000, 0.720, '2021-03-23 15:13:00', '2021-03-23 15:13:00');
INSERT INTO `t_target` VALUES (225, '能够承担在多学科背景下的软件项目团队负责人、团队成员及个体的角色，能与其他成员进行有效地沟通、合作并开展工作。', 29, 2021, 0.000, 0.720, '2021-03-23 15:13:00', '2021-03-23 15:13:00');
INSERT INTO `t_target` VALUES (226, '能够就复杂软件工程问题通过报告、设计文档、陈述等方式准确阐述和表达工作内容及个人观点，能与业界同行和社会公众进行有效沟通与交流。', 30, 2021, 0.875, 0.720, '2021-03-23 15:13:37', '2021-03-23 15:13:37');
INSERT INTO `t_target` VALUES (227, '能够具备一定的国际视野，掌握一门外语，具备阅读和理解本专业外文资料和文献，能够在跨文化背景下进行沟通和交流。', 30, 2021, 0.000, 0.720, '2021-03-23 15:13:37', '2021-03-23 15:13:37');
INSERT INTO `t_target` VALUES (228, '了解软件工程实践活动中所涉及的管理和经济决策问题，并掌握工程管理原理和经济决策方法。', 31, 2021, 0.000, 0.720, '2021-03-23 15:14:13', '2021-03-23 15:14:13');
INSERT INTO `t_target` VALUES (229, '能够在多学科环境下，将工程管理原理、经济决策方法对软件项目开发全过程进行规范化管理。', 31, 2021, 0.000, 0.720, '2021-03-23 15:14:13', '2021-03-23 15:14:13');
INSERT INTO `t_target` VALUES (230, ' 能够认识到自我探索和终身学习的必要性，拥有健康的体质和自主学习的能力。', 32, 2021, 0.000, 0.720, '2021-03-23 15:14:42', '2021-03-23 15:14:42');
INSERT INTO `t_target` VALUES (231, '具备跟踪专业领域前沿知识和终身学习的意识，具备不断学习和适应发展的能力。', 32, 2021, 0.586, 0.720, '2021-03-23 15:14:42', '2021-03-23 15:14:42');

-- ----------------------------
-- Table structure for tb_client
-- ----------------------------
DROP TABLE IF EXISTS `tb_client`;
CREATE TABLE `tb_client`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `auto_grant` bit(1) NOT NULL,
  `client_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `grant_types` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `private_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `redirect_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `refresh_token_timeout` bigint(20) NOT NULL,
  `scopes` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `secret` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `timeout` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_p5csaxy20iqoh8rg382cda9x2`(`client_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_client
-- ----------------------------
INSERT INTO `tb_client` VALUES (1, b'0', 'husen19950528201314lq19980824', NULL, NULL, NULL, 5184000000, NULL, '$2a$10$ed5bqt7P9DgaMo1XmDGNNuClN5Ub4uGxeoOr5IM0hT.cvEcvSLFTy', 2592000000);
INSERT INTO `tb_client` VALUES (2, b'0', 'user123456user', NULL, NULL, NULL, 5184000000, NULL, '$10$MUuK12NjTO4ZvRJG11utwOS3tvI3p/KDqJQVGKszcv1EAPeai7MQS', 2592000000);
INSERT INTO `tb_client` VALUES (3, b'0', 'Iacaa20Server', NULL, NULL, NULL, 5184000000, NULL, '$10$MUuK12NjTO4ZvRJG11utwOS3tvI3p/KDqJQVGKszcv1EAPeai7MQS', 2592000000);

-- ----------------------------
-- Table structure for tb_permission
-- ----------------------------
DROP TABLE IF EXISTS `tb_permission`;
CREATE TABLE `tb_permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `resource_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_permission
-- ----------------------------
INSERT INTO `tb_permission` VALUES (1, 1);
INSERT INTO `tb_permission` VALUES (2, 2);
INSERT INTO `tb_permission` VALUES (3, 3);
INSERT INTO `tb_permission` VALUES (4, 4);
INSERT INTO `tb_permission` VALUES (5, 5);
INSERT INTO `tb_permission` VALUES (6, 6);
INSERT INTO `tb_permission` VALUES (7, 7);
INSERT INTO `tb_permission` VALUES (8, 8);
INSERT INTO `tb_permission` VALUES (9, 9);
INSERT INTO `tb_permission` VALUES (10, 10);
INSERT INTO `tb_permission` VALUES (11, 11);
INSERT INTO `tb_permission` VALUES (12, 12);
INSERT INTO `tb_permission` VALUES (13, 13);
INSERT INTO `tb_permission` VALUES (14, 14);
INSERT INTO `tb_permission` VALUES (15, 15);
INSERT INTO `tb_permission` VALUES (16, 16);
INSERT INTO `tb_permission` VALUES (17, 17);
INSERT INTO `tb_permission` VALUES (18, 18);
INSERT INTO `tb_permission` VALUES (19, 19);
INSERT INTO `tb_permission` VALUES (20, 20);
INSERT INTO `tb_permission` VALUES (21, 21);
INSERT INTO `tb_permission` VALUES (22, 22);
INSERT INTO `tb_permission` VALUES (23, 23);
INSERT INTO `tb_permission` VALUES (24, 24);
INSERT INTO `tb_permission` VALUES (25, 25);
INSERT INTO `tb_permission` VALUES (26, 26);
INSERT INTO `tb_permission` VALUES (27, 27);
INSERT INTO `tb_permission` VALUES (28, 28);
INSERT INTO `tb_permission` VALUES (29, 29);
INSERT INTO `tb_permission` VALUES (30, 30);
INSERT INTO `tb_permission` VALUES (31, 31);
INSERT INTO `tb_permission` VALUES (32, 32);
INSERT INTO `tb_permission` VALUES (33, 33);
INSERT INTO `tb_permission` VALUES (34, 34);

-- ----------------------------
-- Table structure for tb_resource
-- ----------------------------
DROP TABLE IF EXISTS `tb_resource`;
CREATE TABLE `tb_resource`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `resource_server_id` bigint(20) NOT NULL,
  `resource_server_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `scope` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_resource
-- ----------------------------
INSERT INTO `tb_resource` VALUES (1, '更新角色', 1, 'AuthCenter', 'role:update');
INSERT INTO `tb_resource` VALUES (2, '删除角色', 1, 'AuthCenter', 'role:delete');
INSERT INTO `tb_resource` VALUES (3, '创建角色', 1, 'AuthCenter', 'role:create');
INSERT INTO `tb_resource` VALUES (4, '查询角色', 1, 'AuthCenter', 'role:list');
INSERT INTO `tb_resource` VALUES (5, '删除角色', 1, 'AuthCenter', 'role:deleteAll');
INSERT INTO `tb_resource` VALUES (6, '分页查询角色', 1, 'AuthCenter', 'role:page');
INSERT INTO `tb_resource` VALUES (7, '创建客户端', 1, 'AuthCenter', 'client:create');
INSERT INTO `tb_resource` VALUES (8, '根据clientId查询client', 1, 'AuthCenter', 'client:findByClientId');
INSERT INTO `tb_resource` VALUES (9, '绑定用户到客户端', 1, 'AuthCenter', 'client:bindUser');
INSERT INTO `tb_resource` VALUES (10, '权限列表', 1, 'AuthCenter', 'permission:list');
INSERT INTO `tb_resource` VALUES (11, '角色权限列表', 1, 'AuthCenter', 'permission:listByRole');
INSERT INTO `tb_resource` VALUES (12, '创建用户', 1, 'AuthCenter', 'user:create');
INSERT INTO `tb_resource` VALUES (13, '设置用户角色', 1, 'AuthCenter', 'user:setUserRole');
INSERT INTO `tb_resource` VALUES (14, '根据ID查询用户', 2, 'UserServer', 'User:get');
INSERT INTO `tb_resource` VALUES (15, '更新用户', 2, 'UserServer', 'User:update');
INSERT INTO `tb_resource` VALUES (16, '删除用户', 2, 'UserServer', 'User:delete');
INSERT INTO `tb_resource` VALUES (17, '创建用户', 2, 'UserServer', 'User:create');
INSERT INTO `tb_resource` VALUES (18, '分页查询用户', 2, 'UserServer', 'User:page');
INSERT INTO `tb_resource` VALUES (19, '根据用户名查询用户', 2, 'UserServer', 'User:findByUsername');
INSERT INTO `tb_resource` VALUES (20, '判断用户是否存在', 2, 'UserServer', 'User:userIsExisted');
INSERT INTO `tb_resource` VALUES (21, '根据ID查询用户自定义信息', 2, 'UserServer', 'UserCustomizeInfo:get');
INSERT INTO `tb_resource` VALUES (22, '更新用户自定义信息', 2, 'UserServer', 'UserCustomizeInfo:update');
INSERT INTO `tb_resource` VALUES (23, '删除用户自定义信息', 2, 'UserServer', 'UserCustomizeInfo:delete');
INSERT INTO `tb_resource` VALUES (24, '创建用户自定义信息', 2, 'UserServer', 'UserCustomizeInfo:create');
INSERT INTO `tb_resource` VALUES (25, '更新毕业要求', 3, 'Iacaa20Server', 'GradRequirement:update');
INSERT INTO `tb_resource` VALUES (26, '毕业要求列表', 3, 'Iacaa20Server', 'GradRequirement:list');
INSERT INTO `tb_resource` VALUES (27, '保存毕业要求', 3, 'Iacaa20Server', 'GradRequirement:save');
INSERT INTO `tb_resource` VALUES (28, '毕业要求分页列表', 3, 'Iacaa20Server', 'GradRequirement:pageList');
INSERT INTO `tb_resource` VALUES (29, '删除单个毕业要求', 3, 'Iacaa20Server', 'GradRequirement:deleteOne');
INSERT INTO `tb_resource` VALUES (30, '毕业要求Vo列表', 3, 'Iacaa20Server', 'GradRequirement:voList');
INSERT INTO `tb_resource` VALUES (31, '删除毕业要求列表', 3, 'Iacaa20Server', 'GradRequirement:deleteList');
INSERT INTO `tb_resource` VALUES (32, '导出模板', 3, 'Iacaa20Server', 'GradRequirement:exportTemplate');
INSERT INTO `tb_resource` VALUES (33, '毕业要求Vo全部列表', 3, 'Iacaa20Server', 'GradRequirement:voListAll');
INSERT INTO `tb_resource` VALUES (34, '获取单个毕业要求', 3, 'Iacaa20Server', 'GradRequirement:getOne');

-- ----------------------------
-- Table structure for tb_resource_server
-- ----------------------------
DROP TABLE IF EXISTS `tb_resource_server`;
CREATE TABLE `tb_resource_server`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `client_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_9rkldek41vc4qypvfk6x9ltb9`(`client_id`) USING BTREE,
  UNIQUE INDEX `UK_adedt277hncymwr33q04ghv8b`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_resource_server
-- ----------------------------
INSERT INTO `tb_resource_server` VALUES (1, 'husen19950528201314lq19980824', 'AuthCenter');
INSERT INTO `tb_resource_server` VALUES (2, 'user123456user', 'UserServer');
INSERT INTO `tb_resource_server` VALUES (3, 'Iacaa20Server', 'Iacaa20Server');

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_1ncmoedv5ta7r19y9d4oidn0y`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` VALUES (1, '教师角色', '教师');

-- ----------------------------
-- Table structure for tb_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_permission`;
CREATE TABLE `tb_role_permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permission_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_role_permission
-- ----------------------------
INSERT INTO `tb_role_permission` VALUES (9, 32, 1);
INSERT INTO `tb_role_permission` VALUES (10, 33, 1);
INSERT INTO `tb_role_permission` VALUES (11, 34, 1);
INSERT INTO `tb_role_permission` VALUES (12, 25, 1);
INSERT INTO `tb_role_permission` VALUES (13, 26, 1);
INSERT INTO `tb_role_permission` VALUES (14, 27, 1);
INSERT INTO `tb_role_permission` VALUES (15, 28, 1);
INSERT INTO `tb_role_permission` VALUES (16, 29, 1);
INSERT INTO `tb_role_permission` VALUES (17, 30, 1);
INSERT INTO `tb_role_permission` VALUES (18, 31, 1);

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `password` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `create_user_id` bigint(20) NULL DEFAULT NULL,
  `create_time` datetime(6) NULL DEFAULT NULL,
  `last_modified_by` bigint(20) NULL DEFAULT NULL,
  `last_modified_time` datetime(6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK_4wv83hfajry5tdoamn8wsqa6x`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, '$2a$10$HpJqGCYwgoc./l5aClq86.Fw.N05POL1FolWrJcdfVzvjYpqu20di', 'teacher', 0, '2021-03-31 09:30:23.268000', 0, '2021-03-31 09:30:23.268000');

-- ----------------------------
-- Table structure for tb_user_client_relation
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_client_relation`;
CREATE TABLE `tb_user_client_relation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `client_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_client_relation
-- ----------------------------
INSERT INTO `tb_user_client_relation` VALUES (1, 1, 0);

-- ----------------------------
-- Table structure for tb_user_customize_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_customize_info`;
CREATE TABLE `tb_user_customize_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `client_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `info` varchar(4096) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_user_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_role`;
CREATE TABLE `tb_user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NULL DEFAULT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_role
-- ----------------------------
INSERT INTO `tb_user_role` VALUES (1, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
