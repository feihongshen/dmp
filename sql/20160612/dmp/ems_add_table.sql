--轨迹接口临时表
CREATE TABLE `express_ems_flow_info_temp` (
   `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
   `email_num` VARCHAR(50) NOT NULL COMMENT 'ems运单号',
   `action` VARCHAR(4) NOT NULL COMMENT 'ems操作值',
   `credate` VARCHAR(50) NOT NULL COMMENT '记录生成时间',
   `flow_content` TEXT COLLATE utf8_bin COMMENT 'json格式字符串',
   `state` INT(4) DEFAULT '0'  COMMENT '处理标志:0、未处理，1、处理成功，2、处理失败',
   `remark` VARCHAR(500) DEFAULT NULL DEFAULT '',
   `handleCount`  INT(10)  DEFAULT 0  COMMENT '处理次数',
   PRIMARY KEY (`id`),
   KEY `emailNumIdx` (`email_num`)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


--轨迹接口表
CREATE TABLE `express_ems_flow_info` (
   `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
   `transcwb` VARCHAR(50) NOT NULL COMMENT 'dmp运单号',
   `email_num` VARCHAR(50) NOT NULL COMMENT 'ems运单号',
   `credate` VARCHAR(50) NOT NULL COMMENT '记录生成时间',
   `emsFlowordertype` INT(11) DEFAULT NULL COMMENT 'ems订单状态',
   `emsAction` VARCHAR(4) NOT NULL COMMENT 'ems操作值',
   `flow_content` TEXT COLLATE utf8_bin COMMENT 'json格式字符串',
   `state` INT(4) DEFAULT '0'  COMMENT '转业务标志:0、没有转业务，1、转业务成功，2、转业务失败',
   `properdelivery` VARCHAR(6) DEFAULT NULL COMMENT '签收情况，妥投使用',
   `notproperdelivery` VARCHAR(6) DEFAULT NULL COMMENT '投递失败原因',
   `handleCount`  INT(10)  DEFAULT 0  COMMENT '处理次数',
   `remark` VARCHAR(500) DEFAULT NULL,
   PRIMARY KEY (`id`),
   KEY `transcwbIdx` (`transcwb`)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


--运单对照表
CREATE TABLE `express_ems_dmp_transcwb` (
   `transcwb` VARCHAR(50) NOT NULL COMMENT 'dmp运单号',
   `cwb` VARCHAR(50) NOT NULL COMMENT 'dmp订单号',
   `email_num` VARCHAR(50) NOT NULL COMMENT 'ems运单号',
   PRIMARY KEY (`transcwb`),
   KEY `cwbIdx` (`cwb`),
   KEY `emailNumIdx` (`email_num`)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


--下发ems接口订单临时表
CREATE TABLE express_ems_order_b2ctemp(
   `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
   `transcwb` VARCHAR(50) NOT NULL COMMENT 'dmp运单号',
   `cwb` VARCHAR(50) NOT NULL COMMENT 'dmp订单号',
   `credate` VARCHAR(50) NOT NULL COMMENT '记录生成时间',
   `getMailnumFlag` INT(2) DEFAULT 0 COMMENT '获取ems运单标志',
   `addTranscwbFlag` INT(2) DEFAULT 0 COMMENT '生成运单号标志',
   `data` VARCHAR(2000) NOT NULL DEFAULT '' COMMENT '发送给ems的运单报文信息',
   `state`  INT(4)  DEFAULT 0  COMMENT '发送标志:0、没有发送，1、发送成功，2、发送失败',
  `resendCount`  INT(10)  DEFAULT 0  COMMENT '发送次数',
   `remark` VARCHAR(500) DEFAULT '',
   PRIMARY KEY (`id`),
   UNIQUE KEY transcwbIdx(`transcwb`),
   KEY `cwbIdx` (`cwb`),
   KEY `credateIdx` (`credate`),
   KEY `stateIdx` (`state`)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;



