ALTER TABLE express_ops_cwb_detail ADD COLUMN `tpstranscwb` VARCHAR(100) COLLATE utf8_bin DEFAULT NULL COMMENT 'tps运单号';

CREATE TABLE `express_ops_ordertrack_to_tps` (
   `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
   `cwb` VARCHAR(100) COLLATE utf8_bin NOT NULL COMMENT '订单号',
   `tpstranscwb` VARCHAR(100) COLLATE utf8_bin NOT NULL COMMENT 'tps运单号',
   `floworderid` INT(10) NOT NULL COMMENT '流程环节id',
   `flowmsg` TEXT COLLATE utf8_bin COMMENT '轨迹报文',
   `tracktime` TIMESTAMP NULL DEFAULT NULL COMMENT '轨迹实际时间',
   `createtime` TIMESTAMP NULL DEFAULT NULL COMMENT '创建时间',
   `status` INT(2) DEFAULT NULL COMMENT '状态：1未处理，2完成转业务，3错误，4忽略',
   `errinfo` VARCHAR(500) COLLATE utf8_bin DEFAULT '' COMMENT '异常信息',
   `trytime` INT(4) DEFAULT '0' COMMENT '尝试次数',
   `customerid` INT(11) NOT NULL  COMMENT '客户id',
   `flowordertype` INT(11) DEFAULT '0',
   PRIMARY KEY (`id`),
   KEY `ordertrack_cwb_idx` (`cwb`),
   KEY `ordertrack_tpstranscwb_idx` (`tpstranscwb`),
   KEY `ordertrack_time_idx` (`tracktime`),
   KEY `ordertrack_status_idx` (`status`),
   KEY `ordertrack_createtime_idx` (`createtime`),
   KEY `ordertrack_customerid_idx` (`customerid`)
 ) ENGINE=INNODB AUTO_INCREMENT=676 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='轨迹接口表（轨迹推给TPS）';
 