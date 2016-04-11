/*
Navicat MySQL Data Transfer

Source Server         : 10.199.247.161dmp
Source Server Version : 50529
Source Host           : 10.199.247.161:3306
Source Database       : merge_gztl_dmp

Target Server Type    : MYSQL
Target Server Version : 50529
File Encoding         : 65001

Date: 2016-04-07 17:07:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `express_set_user_inf`
-- ----------------------------
DROP TABLE IF EXISTS `express_set_user_inf`;
CREATE TABLE `express_set_user_inf` (
  `inf_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'express_set_user 主键',
  `username` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
  `realname` varchar(50) NOT NULL DEFAULT '' COMMENT '真实名字',
  `usermobile` varchar(50) NOT NULL DEFAULT '' COMMENT '手机',
  `password` varchar(50) NOT NULL DEFAULT '' COMMENT '密码',
  `is_sync` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否同步',
  `status` tinyint(10) NOT NULL DEFAULT '0' COMMENT '状态 0、有效，1、失效',
  `create_date` timestamp NOT NULL DEFAULT '1971-01-01 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `branchid` bigint(20) NOT NULL DEFAULT '0' COMMENT '机构id',
  `oldusername` varchar(50) NOT NULL DEFAULT '' COMMENT '修改前username',
  `times` int(11) NOT NULL DEFAULT '0' COMMENT '同步次数',
  PRIMARY KEY (`inf_id`),
  KEY `idx_userid` (`userid`) USING BTREE,
  KEY `idx_times` (`times`) USING BTREE,
  KEY `idx_username` (`username`) USING BTREE,
  KEY `idx_create_date` (`create_date`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9228 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of express_set_user_inf
-- ----------------------------
