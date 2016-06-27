ALTER TABLE `express_ops_tps_flow_tmp` 
ADD COLUMN `sendweight` int(4) DEFAULT '1' NULL COMMENT '推重量体积：0不推,1要推',
ADD COLUMN `sendemaildate` int(4) DEFAULT '0' NULL COMMENT '推出仓时间：0不推,1要推';

ALTER TABLE `express_ops_tps_flow_tmp_sent` 
ADD COLUMN `sendweight` int(4) DEFAULT '1' NULL COMMENT '推重量体积：0不推,1要推',
ADD COLUMN `sendemaildate` int(4) DEFAULT '0' NULL COMMENT '推出仓时间：0不推,1要推';

CREATE TABLE `express_ops_tps_flow_tmp_mps` (
  `cwb` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '订单号',
  `transcwb` varchar(100) COLLATE utf8_bin NOT NULL COMMENT '运单号',
  KEY `tps_flow_tmp_mps_cwb_idx` (`cwb`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

ALTER TABLE `express_ops_tps_flow_tmp` 
ADD COLUMN `weight` decimal(18,3) DEFAULT '0.01' NULL COMMENT '重量',
ADD COLUMN `volume` decimal(19,4) DEFAULT '0.01' NULL COMMENT '体积';

ALTER TABLE `express_ops_tps_flow_tmp_sent` 
ADD COLUMN `weight` decimal(18,3) DEFAULT '0.01' NULL COMMENT '重量',
ADD COLUMN `volume` decimal(19,4) DEFAULT '0.01' NULL COMMENT '体积';
