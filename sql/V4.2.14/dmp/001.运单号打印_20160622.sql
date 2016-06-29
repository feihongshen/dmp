-- 新增express_ops_tpstranscwb表
CREATE TABLE `express_ops_tpstranscwb` (
	`id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
	`tpstranscwb` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '品骏运单号',
	`print_status` TINYINT(2) NOT NULL DEFAULT '0' COMMENT '运单打印状态：0未打印，1已打印',
	`print_user` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '运单打印人',
	`print_time` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '打印时间',
	`create_time` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间-MySQL维护',
	PRIMARY KEY (`id`),
	INDEX `detail_tpstranscwb_idx` (`tpstranscwb`)
)COLLATE='utf8_general_ci' ENGINE=InnoDB;

-- 创建运单号打印菜单
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('505070', '2', '运单号打印', '505070', 'tpsTranscwbPrint/printList/1?', '5050');
