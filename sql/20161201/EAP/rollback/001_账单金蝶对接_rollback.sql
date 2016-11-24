drop table `fn_msg_record`;
delete from `dmp40_function` where `ID` =  '306012' and `functionname`= 'EAP异常';
ALTER TABLE `fn_cust_pay_report_cfg` DROP COLUMN `is_collect_deduction`;
