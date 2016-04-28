ALTER TABLE `fn_org_recharges`   
ADD COLUMN `picker` VARCHAR(30) DEFAULT ''  NOT NULL  COMMENT '小件员登录名',
ADD COLUMN `bill_no` VARCHAR(30) DEFAULT ''  NOT NULL  COMMENT '站点账单编号',
ADD COLUMN `bill_detail_id` BIGINT(20) DEFAULT 0  NOT NULL  COMMENT '站点账单明细id',
ADD COLUMN `bill_type` VARCHAR(4) DEFAULT ''  NOT NULL  COMMENT '站点账单类型。0.正常账单，1.调整账单',
ADD COLUMN `payin_type` TINYINT(4) DEFAULT 1  NOT NULL  COMMENT '缴款方式。1.站点缴款，2小件员缴款', 
ADD COLUMN `recharge_no` VARCHAR(30) DEFAULT ''  NOT NULL  COMMENT '缴款单号。年月日+5位流水号+P', 
ADD COLUMN `picker_id` BIGINT(20) DEFAULT 0  NULL  COMMENT '小件员id',
ADD  INDEX `index_recharge_no` (`recharge_no`),
ADD  INDEX `index_picker` (`picker`),
ADD  INDEX `index_bill_no` (`bill_no`),
ADD  INDEX `index_import_time` (`import_time`),
ADD  INDEX `index_bill_detail_id` (`bill_detail_id`),
ADD  INDEX `index_payin_type` (`payin_type`);