CREATE TABLE `fn_rpt_station_sign_person` (
	`id` BIGINT(10) NOT NULL AUTO_INCREMENT,
	`branchid` INT(11) NULL DEFAULT '-1' COMMENT '机构编号',
	`deliveryid` INT(11) NOT NULL COMMENT '小件员',
	`amount` DECIMAL(20,2) NULL DEFAULT '0.00' COMMENT '金额',
	`typeid` INT(11) NULL DEFAULT '-1' COMMENT '报表类型',
	`state` INT(11) NULL DEFAULT '1' COMMENT '记录状态',
	`reportdate` INT(11) NOT NULL COMMENT '报表日期',
	`createtime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建更新时间',
	PRIMARY KEY (`id`),
	INDEX `order_lc_branchid_idx` (`branchid`),
	INDEX `order_lc_deliveryid_idx` (`deliveryid`),
	INDEX `order_lc_reportdate_idx` (`reportdate`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB;