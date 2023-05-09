/*
 Navicat Premium Data Transfer

 Source Server         : XXT-MySQL
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : 192.168.32.132:3306
 Source Schema         : xxt_course

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 10/04/2023 10:43:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for course_audit
-- ----------------------------
DROP TABLE IF EXISTS `course_audit`;
CREATE TABLE `course_audit`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '课程审核信息编号',
  `course_id` int(0) NOT NULL COMMENT '课程编号',
  `audit_msg` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核信息',
  `audit_status` int(0) NULL DEFAULT NULL COMMENT '审核状态：0-未审核，1-已审核，2-审核未通过',
  `auditor` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核人',
  `audit_time` datetime(0) NULL DEFAULT NULL COMMENT '审核时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for course_catalogue
-- ----------------------------
DROP TABLE IF EXISTS `course_catalogue`;
CREATE TABLE `course_catalogue`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '课程目录编号',
  `course_id` int(0) NOT NULL COMMENT '课程编号',
  `catalogue_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '课程目录名称',
  `parent_id` int(0) NOT NULL COMMENT '父类编号',
  `media_type` int(0) NOT NULL COMMENT '课程类型：1-视频，2-文档，3-图片',
  `is_preview` int(0) NOT NULL COMMENT '是否支持预览：1-支持，0-不支持',
  `start_time` datetime(0) NOT NULL COMMENT '开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `catalogue_desc` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目录描述',
  `video_time` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '视频时长',
  `order_by` int(0) NOT NULL COMMENT '排序',
  `status` int(0) NOT NULL COMMENT '状态：1-启用，2-停用',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for course_catalogue_media
-- ----------------------------
DROP TABLE IF EXISTS `course_catalogue_media`;
CREATE TABLE `course_catalogue_media`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `catalogue_id` int(0) NOT NULL COMMENT '目录编号',
  `course_id` int(0) NOT NULL COMMENT '课程编号',
  `media_id` int(0) NOT NULL COMMENT '媒体编号',
  `file_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名称',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for course_catalogue_work
-- ----------------------------
DROP TABLE IF EXISTS `course_catalogue_work`;
CREATE TABLE `course_catalogue_work`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '作业编号',
  `work_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '作业名称',
  `catalogue_id` int(0) NOT NULL COMMENT '目录编号',
  `course_id` int(0) NOT NULL COMMENT '课程编号',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for course_category
-- ----------------------------
DROP TABLE IF EXISTS `course_category`;
CREATE TABLE `course_category`  (
  `id` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '课程分类',
  `category_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  `parent_id` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '父节点编号',
  `is_show` int(0) NOT NULL COMMENT '是否展示：1-展示，0-不展示',
  `order_by` int(0) NULL DEFAULT 0 COMMENT '排序',
  `is_leaf` int(0) NOT NULL COMMENT '是否叶子：1-叶子，0-非叶子',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for course_info
-- ----------------------------
DROP TABLE IF EXISTS `course_info`;
CREATE TABLE `course_info`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '课程信息编号',
  `course_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '课程名称',
  `tags` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '课程标签',
  `first_category` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '一级分类',
  `second_category` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '二级分类',
  `if_charge` int(0) NOT NULL COMMENT '是否收费：1-收费，0-免费',
  `course_desc` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '课程描述',
  `picture` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '课程图片',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更改时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '修改者',
  `audit_status` int(0) NOT NULL COMMENT '课程审核状态：0-未审核，1-已审核，2-审核未通过',
  `status` int(0) NOT NULL COMMENT '课程发布状态：0-未发布，1-已发布，2-下线',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for course_sale
-- ----------------------------
DROP TABLE IF EXISTS `course_sale`;
CREATE TABLE `course_sale`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '课程销售信息编号',
  `price` float(11, 2) NOT NULL COMMENT '现价',
  `original_price` float(11, 2) NOT NULL COMMENT '原价',
  `qq` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'qq',
  `wechat` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信',
  `phone` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
