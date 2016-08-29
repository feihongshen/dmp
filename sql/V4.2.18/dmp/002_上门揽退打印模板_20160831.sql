CREATE TABLE `express_ops_smt_cwb` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`cwb` VARCHAR (100) NULL COMMENT '订单号',
	`return_no` VARCHAR (100) NULL COMMENT '退货单号',
	`return_address` VARCHAR (500) NULL COMMENT '退货地址',
	PRIMARY KEY (`id`),
	UNIQUE INDEX (`cwb`) USING BTREE
) COMMENT = '上门退货表';

ALTER TABLE `express_set_excel_column`
ADD COLUMN `returnnoindex`  int NULL DEFAULT 0 COMMENT '退货号',
ADD COLUMN `returnaddressindex`  int NULL DEFAULT 0 COMMENT '退货地址';