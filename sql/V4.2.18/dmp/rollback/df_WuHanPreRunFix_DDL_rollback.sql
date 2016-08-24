ALTER TABLE fn_df_rule_subsidy DROP COLUMN start_val,DROP COLUMN end_val;

DROP TABLE IF EXISTS `fn_df_confirm_rate_subsidy`;
DROP TABLE IF EXISTS `fn_df_confirm_rate_subsidy_detail`;