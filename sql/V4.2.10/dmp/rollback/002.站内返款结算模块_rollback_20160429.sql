drop table `fn_rpt_station_sign_person`;

delete from `dmp40_function` where `ID`='803002' and `functionname`= '缴款导入';
delete from `dmp40_function` where `ID`='803003' and `functionname`= '缴款导入管理';
delete from `dmp40_function` where `ID`='803004' and `functionname`= '预付款管理';
delete from `dmp40_function` where `ID`='803005' and `functionname`= '缴款账户余额查询';
delete from `dmp40_function` where `ID`='803080' and `functionname`= '签收小件员余额报表';

ALTER TABLE `express_set_branch` DROP COLUMN `payin_type`;

ALTER TABLE `express_set_user` DROP INDEX `idx_username`;

ALTER TABLE `fn_order_recharge` DROP COLUMN `pay_detail_id`;

ALTER TABLE `fn_org_recharges` DROP COLUMN `picker`,
DROP COLUMN `bill_no`,
DROP COLUMN `bill_detail_id`,
DROP COLUMN `bill_type`,
DROP COLUMN `payin_type`,
DROP COLUMN `recharge_no`,
DROP COLUMN `picker_id`,
DROP INDEX index_import_time;

ALTER TABLE `fn_pay_detail` DROP COLUMN `creator`;

ALTER TABLE `fn_pay_platform_interface` DROP COLUMN `creator`;
