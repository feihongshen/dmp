ALTER TABLE `express_ops_cwb_detail`
DROP COLUMN `exceldeliverid`;

ALTER TABLE `express_ops_editcwbinfo`
DROP COLUMN `oldexceldeliverid`,
DROP COLUMN `oldexceldeliver`,
DROP COLUMN `newexceldeliverid`,
DROP COLUMN `newexceldeliver`;

ALTER TABLE `express_service_revise_address`
DROP COLUMN `exceldeliver`;