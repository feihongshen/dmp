DROP TABLE IF EXISTS `fn_df_agreement`;
DROP TABLE IF EXISTS `fn_df_bill_org`;
DROP TABLE IF EXISTS `fn_df_bill_period`;
DROP TABLE IF EXISTS `fn_df_bill_staff`;
DROP TABLE IF EXISTS `fn_df_confirm_rate`;
DROP TABLE IF EXISTS `fn_df_confirm_rate_detail`;
DROP TABLE IF EXISTS `fn_df_fee_adjustment_org`;
DROP TABLE IF EXISTS `fn_df_fee_adjustment_staff`;
DROP TABLE IF EXISTS `fn_df_fee_org`;
DROP TABLE IF EXISTS `fn_df_fee_staff`;
DROP TABLE IF EXISTS `fn_df_rule`;
DROP TABLE IF EXISTS `fn_df_rule_add`;
DROP TABLE IF EXISTS `fn_df_rule_area`;
DROP TABLE IF EXISTS `fn_df_rule_avg`;
DROP TABLE IF EXISTS `fn_df_rule_cust`;
DROP TABLE IF EXISTS `fn_df_rule_range`;
DROP TABLE IF EXISTS `fn_df_rule_subsidy`;
DROP TABLE IF EXISTS `fn_df_sanction`;

ALTER TABLE fn_df_agreement_backup RENAME TO fn_df_agreement;
ALTER TABLE fn_df_bill_detail_backup RENAME TO fn_df_bill_detail;
ALTER TABLE fn_df_bill_period_backup RENAME TO fn_df_bill_period;
ALTER TABLE fn_df_confirm_rate_backup RENAME TO fn_df_confirm_rate;
ALTER TABLE fn_df_confirm_rate_detail_backup RENAME TO fn_df_confirm_rate_detail;
ALTER TABLE fn_df_rule_backup RENAME TO fn_df_rule;
ALTER TABLE fn_df_rule_area_backup RENAME TO fn_df_rule_area;
ALTER TABLE fn_df_rule_range_backup RENAME TO fn_df_rule_range;
ALTER TABLE fn_df_rule_step_backup RENAME TO fn_df_rule_step;
ALTER TABLE fn_df_rule_subsidy_backup RENAME TO fn_df_rule_subsidy;
ALTER TABLE fn_df_sanction_backup RENAME TO fn_df_sanction;
ALTER TABLE fn_df_adjustment_record_backup RENAME TO fn_df_adjustment_record;
ALTER TABLE fn_df_bill_backup RENAME TO fn_df_bill;
