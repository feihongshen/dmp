SELECT 'fn_df_rule_subsidy添加字段' AS '脚本注释', IF((SELECT COUNT(1) FROM information_schema.`COLUMNS` WHERE TABLE_SCHEMA = SCHEMA() AND TABLE_NAME='fn_df_rule_subsidy' AND COLUMN_NAME IN ('start_val','end_val'))=2, 'success', 'failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_bill_staff添加字段' AS '脚本注释', IF((SELECT COUNT(1) FROM information_schema.`COLUMNS` WHERE TABLE_SCHEMA = SCHEMA() AND TABLE_NAME='fn_df_bill_staff' AND COLUMN_NAME IN ('confirm_rate_subsidy_sanction'))=1, 'success', 'failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_bill_org添加字段' AS '脚本注释', IF((SELECT COUNT(1) FROM information_schema.`COLUMNS` WHERE TABLE_SCHEMA = SCHEMA() AND TABLE_NAME='fn_df_bill_org' AND COLUMN_NAME IN ('confirm_rate_subsidy_sanction'))=1, 'success', 'failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_agreement' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'fn_df_confirm_rate_subsidy') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_bill_org' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'fn_df_confirm_rate_subsidy_detail') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_agreement添加字段' AS '脚本注释', IF((SELECT COUNT(1) FROM information_schema.`COLUMNS` WHERE TABLE_SCHEMA = SCHEMA() AND TABLE_NAME='fn_df_agreement' AND COLUMN_NAME IN ('agt_fee_type'))=1, 'success', 'failed') AS '执行结果'
;
