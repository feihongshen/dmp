ALTER TABLE `express_ops_cwb_detail`
ADD COLUMN `exceldeliverid`  int(11) NULL DEFAULT 0 COMMENT '匹配小件员ID' AFTER `exceldeliver`;

ALTER TABLE `express_ops_editcwbinfo`
ADD COLUMN `oldexceldeliverid`  int(11) NULL DEFAULT 0 COMMENT '原匹配小件员ID',
ADD COLUMN `oldexceldeliver`  varchar(50) NULL COMMENT '原匹配小件员',
ADD COLUMN `newexceldeliverid`  int(11) NULL DEFAULT 0 COMMENT '匹配小件员ID',
ADD COLUMN `newexceldeliver`  varchar(50) NULL COMMENT '匹配小件员';

ALTER TABLE `express_service_revise_address`
ADD COLUMN `exceldeliver`  varchar(50) NULL COMMENT '匹配小件员';