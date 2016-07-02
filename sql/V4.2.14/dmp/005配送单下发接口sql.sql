ALTER TABLE `express_ops_cwb_detail_b2ctemp` 
ADD COLUMN `do_type` INT(4) COMMENT '订单类型',
ADD COLUMN `announcedvalue` DECIMAL(19,2) DEFAULT '0.00' COMMENT '保价声明价值（元）',
ADD COLUMN `paymethod` INT(1) DEFAULT '1' COMMENT '付款方式（0：月结，1：现付，2：到付）',
ADD COLUMN `order_source` int(11) DEFAULT '0' COMMENT '订单客户类型：是否外单';