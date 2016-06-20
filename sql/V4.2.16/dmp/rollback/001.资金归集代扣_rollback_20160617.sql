ALTER TABLE `fn_org_recharges`   
  DROP COLUMN `current_model`,
  DROP COLUMN `recharge_source`,
  DROP COLUMN `cwb`,
  DROP COLUMN `vpal_record_id`;

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

drop TABLE `fn_set_bank`;

