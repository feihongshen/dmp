ALTER TABLE `fn_org_bank_import`   
  DROP COLUMN `current_mode`;

drop TABLE `fn_org_recharges_rptmode`;
  
drop TABLE `fn_orgrpt_order_recharge`;

drop TABLE `fn_org_recharges_adjustment_record`;

ALTER TABLE `fn_station_sign_order_details_snapshot`   
  DROP COLUMN `shouldfareflag`,
  DROP COLUMN `receivablefeeflag`,
  DROP COLUMN `expressfreightflag`,
  DROP COLUMN `shouldfare_receipt`,
  DROP COLUMN `receivablefee_receipt`,
  DROP COLUMN `expressfreight_receipt`;
 
ALTER TABLE `fn_station_sign_order_details_snapshot_express`   
  DROP COLUMN `shouldfareflag`,
  DROP COLUMN `receivablefeeflag`,
  DROP COLUMN `expressfreightflag`,
  DROP COLUMN `shouldfare_receipt`,
  DROP COLUMN `receivablefee_receipt`,
  DROP COLUMN `expressfreight_receipt`;

drop TABLE `fn_org_pay_account`;

drop TABLE `fn_org_pay_account_modify_record`;

drop TABLE `fn_vpal_interface`;

drop TABLE `fn_vpal_record`;

drop TABLE `fn_vpal_check_filelist`;

drop TABLE `fn_cwb_state`;

drop TABLE `fn_set_bank`;

delete from dmp40_function where ID='803091' and functionname='对账文件查询';
delete from dmp40_function where ID='803092' and functionname='结算信息管理';
delete from dmp40_function where ID='803093' and functionname='代扣查询';

delete from express_set_system_install where name in('MultiThreadGenSignReport','ConcurrentCountGenSignReport','VIPPAY3DESKEY','ITFCSIGNPWD','PARTNERID','AUTORECEIVEDOFPOS','SETTLEMENTMODE');


