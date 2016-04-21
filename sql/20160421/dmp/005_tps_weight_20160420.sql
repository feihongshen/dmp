		CREATE TABLE `express_ops_tps_flow_tmp`( 
		   `cwb` varchar(100) NOT NULL COMMENT '订单号', 
		   `scancwb` varchar(100) NOT NULL COMMENT '扫描号',
		   `flowordertype` int(11) NULL DEFAULT 0 COMMENT '环节',
		   `errinfo` text NULL COMMENT '一个订单的报文', 
		   `createtime` timestamp NULL COMMENT '创建时间', 
		   `status` int(4) COMMENT '状态：0未处理,1已处理,2错误',
		   `trytime` int(4) COMMENT '重试次数'
		 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

ALTER TABLE `express_ops_tps_flow_tmp` ADD INDEX `tps_flow_tmp_cwb_idx` (`cwb`), ADD INDEX `tps_flow_tmp_scancwb_idx` (`scancwb`), ADD INDEX `tps_flow_tmp_cretime_idx` (`createtime`);
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `tpstranscwb` varchar(100) NULL;
ALTER TABLE `express_ops_cwb_detail_b2ctemp` ADD COLUMN `tpstranscwb` varchar(100) NULL;