drop table `fn_msg_record`;
delete from `dmp40_function` where `ID` =  '306012' and `functionname`= 'EAP异常';
ALTER TABLE `fn_cust_pay_report_cfg` DROP COLUMN `is_collect_deduction`;

DELETE FROM `express_set_system_install` WHERE `name` = 'KINGDEE_LOGIN_DB_TYPE';
DELETE FROM `express_set_system_install` WHERE `name` = 'KINGDEE_LOGIN_LANGUAGE';
DELETE FROM `express_set_system_install` WHERE `name` = 'KINGDEE_LOGIN_DC_NAME';
DELETE FROM `express_set_system_install` WHERE `name` = 'KINGDEE_LOGIN_SLN_NAME';
DELETE FROM `express_set_system_install` WHERE `name` = 'KINGDEE_PASSWORD';
DELETE FROM `express_set_system_install` WHERE `name` = 'KINGDEE_USERNAME';
DELETE FROM `express_set_system_install` WHERE `name` = 'KINGDEE_RAMDOM';
DELETE FROM `express_set_system_install` WHERE `name` = 'KINGDEE_SECRET';
DELETE FROM `express_set_system_install` WHERE `name` = 'KINGDEE_URL_FOR_EAS';
DELETE FROM `express_set_system_install` WHERE `name` = 'EAS_KINGDEE_INT_ENABLE';
DELETE FROM `express_set_system_install` WHERE `name` = 'EAS_PERSOPN_IN_KINGDEE';

