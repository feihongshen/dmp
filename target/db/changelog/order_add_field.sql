ALTER TABLE express_ops_cwb_detail ADD COLUMN `fnorgbillid` BIGINT(11) DEFAULT 0 comment '站点账单的id';
ALTER TABLE express_ops_cwb_detail ADD COLUMN `fnorgoffsetflag` INT(11) DEFAULT 0 comment '收款状态';
ALTER TABLE express_ops_cwb_detail ADD COLUMN `fnorgoffset` DECIMAL(19,2) DEFAULT 0.00 comment '核销金额';

ALTER TABLE express_ops_cwb_detail ADD COLUMN `fncustomerbillverifyflag` INT(11) DEFAULT 0 comment '客户账单的核销标识';
ALTER TABLE express_ops_cwb_detail ADD COLUMN `fncustomerpayablebillid` BIGINT(11) DEFAULT 0 comment '应付账单的id';
ALTER TABLE express_ops_cwb_detail ADD COLUMN `fncustomerbillid` BIGINT(11) DEFAULT 0 comment '非应付账单的id';
