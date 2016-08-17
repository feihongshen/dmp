ALTER TABLE `express_ops_auto_intowarehouse_message`
	CHANGE COLUMN `intowarehouseType` `intowarehouseType` TINYINT(4) NULL DEFAULT '0' COMMENT '报文来源: 1-分拣库入库, 2-中转库入库, 3-二次分拨';
