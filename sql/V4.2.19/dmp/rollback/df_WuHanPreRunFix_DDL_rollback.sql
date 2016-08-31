ALTER TABLE fn_df_rule_subsidy DROP COLUMN start_val,DROP COLUMN end_val;
ALTER TABLE fn_df_bill_org DROP COLUMN confirm_rate_subsidy_sanction;
ALTER TABLE fn_df_bill_staff DROP COLUMN confirm_rate_subsidy_sanction;

DROP TABLE IF EXISTS `fn_df_confirm_rate_subsidy`;
DROP TABLE IF EXISTS `fn_df_confirm_rate_subsidy_detail`;

ALTER TABLE fn_df_agreement DROP COLUMN agt_fee_type;