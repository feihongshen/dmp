/*
Navicat MySQL Data Transfer

Source Server         : 192.0.170.55
Source Server Version : 50156
Source Host           : 192.0.170.55:3306
Source Database       : dmp_test

Target Server Type    : MYSQL
Target Server Version : 50156
File Encoding         : 65001

Date: 2014-03-18 16:59:45
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `INF_Q_BLOB_TRIGGERS`
-- ----------------------------
DROP TABLE IF EXISTS `INF_Q_BLOB_TRIGGERS`;
CREATE TABLE `INF_Q_BLOB_TRIGGERS` (
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `blob_data` longblob,
  PRIMARY KEY (`trigger_name`,`trigger_group`),
  UNIQUE KEY `SYS_C00100332` (`trigger_name`,`trigger_group`),
  CONSTRAINT `sys_c00100333` FOREIGN KEY (`trigger_name`, `trigger_group`) REFERENCES `INF_Q_TRIGGERS` (`trigger_name`, `trigger_group`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of INF_Q_BLOB_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for `INF_Q_CALENDARS`
-- ----------------------------
DROP TABLE IF EXISTS `INF_Q_CALENDARS`;
CREATE TABLE `INF_Q_CALENDARS` (
  `calendar_name` varchar(200) NOT NULL,
  `calendar` longblob NOT NULL,
  PRIMARY KEY (`calendar_name`),
  UNIQUE KEY `SYS_C00100341` (`calendar_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of INF_Q_CALENDARS
-- ----------------------------

-- ----------------------------
-- Table structure for `INF_Q_CRON_TRIGGERS`
-- ----------------------------
DROP TABLE IF EXISTS `INF_Q_CRON_TRIGGERS`;
CREATE TABLE `INF_Q_CRON_TRIGGERS` (
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `cron_expression` varchar(120) NOT NULL,
  `time_zone_id` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`trigger_name`,`trigger_group`),
  CONSTRAINT `sys_c00100329` FOREIGN KEY (`trigger_name`, `trigger_group`) REFERENCES `INF_Q_TRIGGERS` (`trigger_name`, `trigger_group`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of INF_Q_CRON_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for `INF_Q_FIRED_TRIGGERS`
-- ----------------------------
DROP TABLE IF EXISTS `INF_Q_FIRED_TRIGGERS`;
CREATE TABLE `INF_Q_FIRED_TRIGGERS` (
  `entry_id` varchar(95) NOT NULL,
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `is_volatile` varchar(1) NOT NULL,
  `instance_name` varchar(200) NOT NULL,
  `fired_time` bigint(13) NOT NULL,
  `priority` bigint(13) NOT NULL,
  `state` varchar(16) NOT NULL,
  `job_name` varchar(200) DEFAULT NULL,
  `job_group` varchar(200) DEFAULT NULL,
  `is_stateful` varchar(1) DEFAULT NULL,
  `requests_recovery` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`entry_id`),
  UNIQUE KEY `SYS_C00100352` (`entry_id`),
  KEY `IDX_INF_Q_FT_JOB_REQ_RECOVERY` (`requests_recovery`),
  KEY `IDX_INF_Q_FT_JOB_STATEFUL` (`is_stateful`),
  KEY `IDX_INF_Q_FT_JOB_GROUP` (`job_group`),
  KEY `IDX_INF_Q_FT_JOB_NAME` (`job_name`),
  KEY `IDX_INF_Q_FT_TRIG_INST_NAME` (`instance_name`),
  KEY `IDX_INF_Q_FT_TRIG_VOLATILE` (`is_volatile`),
  KEY `IDX_INF_Q_FT_TRIG_NM_GP` (`trigger_name`,`trigger_group`),
  KEY `IDX_INF_Q_FT_TRIG_GROUP` (`trigger_group`),
  KEY `IDX_INF_Q_FT_TRIG_NAME` (`trigger_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of INF_Q_FIRED_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for `INF_Q_JOB_DETAILS`
-- ----------------------------
DROP TABLE IF EXISTS `INF_Q_JOB_DETAILS`;
CREATE TABLE `INF_Q_JOB_DETAILS` (
  `job_name` varchar(200) NOT NULL,
  `job_group` varchar(200) NOT NULL,
  `description` varchar(250) DEFAULT NULL,
  `job_class_name` varchar(250) NOT NULL,
  `is_durable` varchar(1) NOT NULL,
  `is_volatile` varchar(1) NOT NULL,
  `is_stateful` varchar(1) NOT NULL,
  `requests_recovery` varchar(1) NOT NULL,
  `job_data` longblob,
  PRIMARY KEY (`job_name`,`job_group`),
  UNIQUE KEY `SYS_C00100302` (`job_name`,`job_group`),
  KEY `IDX_INF_Q_J_REQ_RECOVERY` (`requests_recovery`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of INF_Q_JOB_DETAILS
-- ----------------------------

-- ----------------------------
-- Table structure for `INF_Q_JOB_LISTENERS`
-- ----------------------------
DROP TABLE IF EXISTS `INF_Q_JOB_LISTENERS`;
CREATE TABLE `INF_Q_JOB_LISTENERS` (
  `job_name` varchar(200) NOT NULL,
  `job_group` varchar(200) NOT NULL,
  `job_listener` varchar(200) NOT NULL,
  PRIMARY KEY (`job_name`,`job_group`,`job_listener`),
  UNIQUE KEY `SYS_C00100306` (`job_name`,`job_group`,`job_listener`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of INF_Q_JOB_LISTENERS
-- ----------------------------

-- ----------------------------
-- Table structure for `INF_Q_LOCKS`
-- ----------------------------
DROP TABLE IF EXISTS `INF_Q_LOCKS`;
CREATE TABLE `INF_Q_LOCKS` (
  `lock_name` varchar(40) NOT NULL,
  PRIMARY KEY (`lock_name`),
  UNIQUE KEY `SYS_C00100358` (`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of INF_Q_LOCKS
-- ----------------------------
INSERT INTO `INF_Q_LOCKS` VALUES ('STATE_ACCESS');
INSERT INTO `INF_Q_LOCKS` VALUES ('TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for `INF_Q_PAUSED_TRIGGER_GRPS`
-- ----------------------------
DROP TABLE IF EXISTS `INF_Q_PAUSED_TRIGGER_GRPS`;
CREATE TABLE `INF_Q_PAUSED_TRIGGER_GRPS` (
  `trigger_group` varchar(200) NOT NULL,
  PRIMARY KEY (`trigger_group`),
  UNIQUE KEY `SYS_C00100343` (`trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of INF_Q_PAUSED_TRIGGER_GRPS
-- ----------------------------

-- ----------------------------
-- Table structure for `INF_Q_SCHEDULER_STATE`
-- ----------------------------
DROP TABLE IF EXISTS `INF_Q_SCHEDULER_STATE`;
CREATE TABLE `INF_Q_SCHEDULER_STATE` (
  `instance_name` varchar(200) NOT NULL,
  `last_checkin_time` bigint(13) NOT NULL,
  `checkin_interval` bigint(13) NOT NULL,
  PRIMARY KEY (`instance_name`),
  UNIQUE KEY `SYS_C00100356` (`instance_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of INF_Q_SCHEDULER_STATE
-- ----------------------------

-- ----------------------------
-- Table structure for `INF_Q_SIMPLE_TRIGGERS`
-- ----------------------------
DROP TABLE IF EXISTS `INF_Q_SIMPLE_TRIGGERS`;
CREATE TABLE `INF_Q_SIMPLE_TRIGGERS` (
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `repeat_count` int(7) NOT NULL,
  `repeat_interval` bigint(12) NOT NULL,
  `times_triggered` bigint(10) NOT NULL,
  PRIMARY KEY (`trigger_name`,`trigger_group`),
  CONSTRAINT `sys_c00100324` FOREIGN KEY (`trigger_name`, `trigger_group`) REFERENCES `INF_Q_TRIGGERS` (`trigger_name`, `trigger_group`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of INF_Q_SIMPLE_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for `INF_Q_TRIGGER_LISTENERS`
-- ----------------------------
DROP TABLE IF EXISTS `INF_Q_TRIGGER_LISTENERS`;
CREATE TABLE `INF_Q_TRIGGER_LISTENERS` (
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `trigger_listener` varchar(200) NOT NULL,
  PRIMARY KEY (`trigger_name`,`trigger_group`,`trigger_listener`),
  CONSTRAINT `sys_c00100338` FOREIGN KEY (`trigger_name`, `trigger_group`) REFERENCES `INF_Q_TRIGGERS` (`trigger_name`, `trigger_group`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of INF_Q_TRIGGER_LISTENERS
-- ----------------------------

-- ----------------------------
-- Table structure for `INF_Q_TRIGGERS`
-- ----------------------------
DROP TABLE IF EXISTS `INF_Q_TRIGGERS`;
CREATE TABLE `INF_Q_TRIGGERS` (
  `trigger_name` varchar(200) NOT NULL,
  `trigger_group` varchar(200) NOT NULL,
  `job_name` varchar(200) NOT NULL,
  `job_group` varchar(200) NOT NULL,
  `is_volatile` varchar(1) NOT NULL,
  `description` varchar(250) DEFAULT NULL,
  `next_fire_time` bigint(13) DEFAULT NULL,
  `prev_fire_time` bigint(13) DEFAULT NULL,
  `priority` bigint(13) DEFAULT NULL,
  `trigger_state` varchar(16) NOT NULL,
  `trigger_type` varchar(8) NOT NULL,
  `start_time` bigint(13) NOT NULL,
  `end_time` bigint(13) DEFAULT NULL,
  `calendar_name` varchar(200) DEFAULT NULL,
  `misfire_instr` int(2) DEFAULT NULL,
  `job_data` longblob,
  PRIMARY KEY (`trigger_name`,`trigger_group`),
  KEY `sys_c00100317` (`job_name`,`job_group`),
  KEY `IDX_INF_Q_T_VOLATILE` (`is_volatile`),
  KEY `IDX_INF_Q_T_NFT_ST` (`next_fire_time`,`trigger_state`),
  KEY `IDX_INF_Q_T_STATE` (`trigger_state`),
  KEY `IDX_INF_Q_T_NEXT_FIRE_TIME` (`next_fire_time`),
  CONSTRAINT `sys_c00100317` FOREIGN KEY (`job_name`, `job_group`) REFERENCES `INF_Q_JOB_DETAILS` (`job_name`, `job_group`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of INF_Q_TRIGGERS
-- ----------------------------
