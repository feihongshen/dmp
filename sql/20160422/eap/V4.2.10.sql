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

ALTER TABLE express_set_branch ADD COLUMN payin_type TINYINT(4) DEFAULT 1  NOT NULL  COMMENT '缴款方式。1.站点缴款，2小件员缴款';

ALTER TABLE `express_set_user` ADD  INDEX `idx_username` (`username`);

ALTER TABLE `fn_order_recharge`   
ADD COLUMN `pay_detail_id` BIGINT(20) DEFAULT 0  NULL  COMMENT '支付平台扣款记录id（fn_pay_detail)', 
ADD  INDEX `idx_pay_detail_id` (`pay_detail_id`);

ALTER TABLE `fn_pay_detail` ADD COLUMN `creator` VARCHAR(50) NULL  COMMENT '创建人';

ALTER TABLE `fn_pay_platform_interface` ADD COLUMN `creator` VARCHAR(50) NULL  COMMENT '创建人';

INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('803002', '2', '缴款导入', '803002', '${eapUrl}orgPayImport.do?index&', '8030');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('803003', '2', '缴款导入管理', '803003', '${eapUrl}orgPayManager.do?index&', '8030');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('803004', '2', '预付款管理', '803004', '${eapUrl}orgAdvancePay.do?index&', '8030');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('803005', '2', '缴款账户余额查询', '803005', '${eapUrl}orgAccountBalance.do?index&', '8030');