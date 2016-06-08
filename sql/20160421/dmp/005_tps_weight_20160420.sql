CREATE TABLE `express_ops_tps_flow_tmp` (
  `cwb` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '订单号',
  `scancwb` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '扫描号',
  `flowordertype` int(11) DEFAULT '0' COMMENT '环节',
  `errinfo` text COLLATE utf8_bin COMMENT '一个订单的报文',
  `createtime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `status` int(4) DEFAULT NULL COMMENT '状态：0未处理,1已处理,2错误',
  `trytime` int(4) DEFAULT NULL COMMENT '重试次数',
  KEY `tps_flow_tmp_cwb_idx` (`cwb`),
  KEY `tps_flow_tmp_scancwb_idx` (`scancwb`),
  KEY `tps_flow_tmp_cretime_idx` (`createtime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `tpstranscwb` varchar(100) NULL;
ALTER TABLE `express_ops_cwb_detail_b2ctemp` ADD COLUMN `tpstranscwb` varchar(100) NULL;