﻿/*--------------------------------------------------创建表---------------------------------------------------------------*/
/*运单明细表*/
DROP TABLE IF EXISTS `express_ops_transcwb_detail`;
CREATE TABLE `express_ops_transcwb_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `cwb` varchar(50) NOT NULL COMMENT '订单号',
  `transcwb` varchar(50) NOT NULL COMMENT '运单号',
  `transcwbstate` int(11) DEFAULT 0 COMMENT '运单状态（配送、退货等）',
  `transcwboptstate` int(11) DEFAULT 0 COMMENT '运单操作状态（枚举值同订单操作状态）',
  `currentbranchid` int(11) DEFAULT 0 COMMENT '当前站点',
  `previousbranchid` int(11) DEFAULT 0 COMMENT '上一站id',
  `nextbranchid` int(11) DEFAULT 0 COMMENT '站一站id',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间（运单流入系统时间）',
  `emaildate` datetime DEFAULT NULL COMMENT '发货时间', 
  `commonphraseid` int(11) DEFAULT 0 NULL COMMENT '常用语id',
  `commonphrase` varchar(50) DEFAULT NULL NULL COMMENT '常用语',
  `modifiedtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `cwb_idx` (`cwb`) USING BTREE,
  KEY `transcwb_idx` (`transcwb`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='运单明细';

/*运单状态流程表*/
DROP TABLE IF EXISTS `express_set_transcwb_allstate_control`;
CREATE TABLE `express_set_transcwb_allstate_control` (
  `tanscwbstate` int(11) NOT NULL COMMENT '当前运单状态',
  `toflowtype` int(11) NOT NULL COMMENT '后续的操作状态',
  PRIMARY KEY (`tanscwbstate`,`toflowtype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='运单状态流程';


/*--------------------------------------------------修改表---------------------------------------------------------------*/
/*常用语*/
ALTER TABLE `express_set_reason` 
ADD COLUMN `contentmeaning` int(11) DEFAULT 0 COMMENT '原因含义（1：丢失，2：破损，3：退货）';

/*订单主表*/
ALTER TABLE `express_ops_cwb_detail`
ADD COLUMN `mpsoptstate`  int NULL DEFAULT 0 COMMENT '一票多件操作状态（multiple package shipment,取值同订单操作状态）',
ADD COLUMN `mpsallarrivedflag`  int NULL DEFAULT 0 COMMENT '一票多件是否到齐（0：未到齐，1：到齐）' AFTER `mpsoptstate`,
ADD COLUMN `ismpsflag` INT(11) DEFAULT 0 NULL COMMENT '是否一票多件：0默认；1是一票多件';

/*客户表*/
ALTER TABLE `express_set_customer_info`
ADD COLUMN `mpsswitch`  int NULL DEFAULT 0 COMMENT 'mps开关（0：未开启，1：开启库房集单，2：开启站点集单）';

/*对接临时表 添加最后一箱标识*/
ALTER TABLE `express_ops_cwb_detail_b2ctemp` ADD COLUMN `mpsallarrivedflag` INT(11) DEFAULT 0 NULL COMMENT '最后一箱标识:1表示最后一箱；0默认',
ADD COLUMN `ismpsflag` INT(11) DEFAULT 0 NULL COMMENT '是否一票多件：0默认；1是一票多件; 


/*--------------------------------------------------预置数据---------------------------------------------------------------*/
/*运单状态流程表*/
insert into `express_set_transcwb_allstate_control` (`tanscwbstate`, `toflowtype`) values('1','7');
insert into `express_set_transcwb_allstate_control` (`tanscwbstate`, `toflowtype`) values('1','40');
insert into `express_set_transcwb_allstate_control` (`tanscwbstate`, `toflowtype`) values('2','7');
insert into `express_set_transcwb_allstate_control` (`tanscwbstate`, `toflowtype`) values('2','15');
insert into `express_set_transcwb_allstate_control` (`tanscwbstate`, `toflowtype`) values('2','40');
insert into `express_set_transcwb_allstate_control` (`tanscwbstate`, `toflowtype`) values('3','1');
insert into `express_set_transcwb_allstate_control` (`tanscwbstate`, `toflowtype`) values('3','4');
insert into `express_set_transcwb_allstate_control` (`tanscwbstate`, `toflowtype`) values('3','6');
insert into `express_set_transcwb_allstate_control` (`tanscwbstate`, `toflowtype`) values('3','7');
insert into `express_set_transcwb_allstate_control` (`tanscwbstate`, `toflowtype`) values('3','9');
insert into `express_set_transcwb_allstate_control` (`tanscwbstate`, `toflowtype`) values('3','12');
insert into `express_set_transcwb_allstate_control` (`tanscwbstate`, `toflowtype`) values('3','14');
insert into `express_set_transcwb_allstate_control` (`tanscwbstate`, `toflowtype`) values('3','35');
insert into `express_set_transcwb_allstate_control` (`tanscwbstate`, `toflowtype`) values('3','36');
insert into `express_set_transcwb_allstate_control` (`tanscwbstate`, `toflowtype`) values('3','42');
insert into `express_set_transcwb_allstate_control` (`tanscwbstate`, `toflowtype`) values('4','7');
insert into `express_set_transcwb_allstate_control` (`tanscwbstate`, `toflowtype`) values('4','15');
insert into `express_set_transcwb_allstate_control` (`tanscwbstate`, `toflowtype`) values('4','40');
insert into `express_set_transcwb_allstate_control` (`tanscwbstate`, `toflowtype`) values('5','27');
insert into `express_set_transcwb_allstate_control` (`tanscwbstate`, `toflowtype`) values('5','28');
insert into `express_set_transcwb_allstate_control` (`tanscwbstate`, `toflowtype`) values('6','6');
insert into `express_set_transcwb_allstate_control` (`tanscwbstate`, `toflowtype`) values('6','12');
insert into `express_set_transcwb_allstate_control` (`tanscwbstate`, `toflowtype`) values('6','14');


/*订单状态流程表*/
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('1','1');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('1','2');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('1','4');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('1','6');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('1','7');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('1','8');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('1','9');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('1','12');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('1','14');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('1','29');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('1','35');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('1','36');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('1','38');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('1','42');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('1','46');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('2','6');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('2','15');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('2','27');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('2','28');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('2','35');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('2','36');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('2','40');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('2','46');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('3','6');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('3','15');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('3','27');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('3','28');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('3','35');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('3','36');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('3','40');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('3','46');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('5','6');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('5','15');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('5','27');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('5','28');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('6','6');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('6','7');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('6','12');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('6','14');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('8','6');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('8','15');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('8','27');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('8','28');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('8','35');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('8','36');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('8','40');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('8','46');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('9','6');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('9','15');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('9','27');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('9','28');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('9','35');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('9','36');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('9','40');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('9','46');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('10','6');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('10','15');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('10','27');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('10','28');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('10','35');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('10','36');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('10','40');
insert into `express_set_cwb_allstate_control` (`cwbstate`, `toflowtype`) values('10','46');
