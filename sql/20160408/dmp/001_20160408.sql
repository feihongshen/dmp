ALTER TABLE `express_auto_exception_detail` ADD COLUMN `operatetype` varchar(20) DEFAULT '' NULL after `transportno`;
ALTER TABLE `express_auto_order_status_tmp` ADD COLUMN `transportno` varchar(100) DEFAULT '' NULL after `cwb`;
ALTER TABLE `express_ops_transcwb_detail` ADD COLUMN `cargovolume` decimal(19,4) default '0.0000' NULL after `nextbranchid`;
ALTER TABLE `express_ops_transcwb_detail` ADD COLUMN `carrealweight` decimal(18,3) default '0.000' NULL after `nextbranchid`;
