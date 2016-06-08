/*
Navicat MySQL Data Transfer

Source Server         : 10.199.247.161dmp
Source Server Version : 50529
Source Host           : 10.199.247.161:3306
Source Database       : merge_gztl_dmp

Target Server Type    : MYSQL
Target Server Version : 50529
File Encoding         : 65001

Date: 2016-04-07 17:06:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `express_set_branch_inf`
-- ----------------------------
DROP TABLE IF EXISTS `express_set_branch_inf`;
CREATE TABLE `express_set_branch_inf` (
  `inf_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `branchid` bigint(20) NOT NULL DEFAULT '0' COMMENT '机构id',
  `branchname` varchar(50) NOT NULL DEFAULT '' COMMENT '机构名称',
  `tpsbranchcode` varchar(50) NOT NULL DEFAULT '' COMMENT 'tps机构号',
  `branchprovince` varchar(50) NOT NULL DEFAULT '' COMMENT '省份',
  `branchcity` varchar(50) NOT NULL DEFAULT '' COMMENT '城市',
  `brancharea` varchar(50) NOT NULL DEFAULT '' COMMENT '区城',
  `password` varchar(50) NOT NULL DEFAULT '',
  `rec_branchid` bigint(11) unsigned NOT NULL DEFAULT '0' COMMENT '删除时替换机构id',
  `create_date` timestamp NOT NULL DEFAULT '1971-01-01 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `is_sync` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否同步，0：没有同步，1：已经同步',
  `status` tinyint(10) NOT NULL DEFAULT '0' COMMENT '状态，0、有效 1、失效',
  `times` int(11) NOT NULL DEFAULT '0' COMMENT '同步次数',
  PRIMARY KEY (`inf_id`),
  KEY `idx_branchid` (`branchid`) USING BTREE,
  KEY `idx_is_sync` (`is_sync`) USING BTREE,
  KEY `idx_create_date` (`create_date`) USING BTREE,
  KEY `idx_times` (`times`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1310 DEFAULT CHARSET=utf8 COMMENT='站点机构接口表';

-- ----------------------------
-- Records of express_set_branch_inf
-- ----------------------------
