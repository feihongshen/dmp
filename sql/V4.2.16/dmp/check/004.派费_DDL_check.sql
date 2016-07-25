SELECT 'fn_df_agreement' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'fn_df_agreement') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_bill_org' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'fn_df_bill_org') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_bill_period' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'fn_df_bill_period') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_bill_staff' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'fn_df_bill_staff') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_confirm_rate' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'fn_df_confirm_rate') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_confirm_rate_detail' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'fn_df_confirm_rate_detail') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_fee_adjustment_org' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'fn_df_fee_adjustment_org') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_fee_adjustment_staff' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'fn_df_fee_adjustment_staff') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_fee_org' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'fn_df_fee_org') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_fee_staff' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'fn_df_fee_staff') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_rule' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'fn_df_rule') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_rule_add' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'fn_df_rule_add') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_rule_area' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'fn_df_rule_area') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_rule_avg' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'fn_df_rule_avg') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_rule_cust' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'fn_df_rule_cust') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_rule_range' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'fn_df_rule_range') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_rule_subsidy' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'fn_df_rule_subsidy') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_sanction' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'fn_df_sanction') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_agreement_lock' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'fn_df_agreement_lock') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_agreement_lock数据' AS '脚本注释',IF((SELECT COUNT(1) FROM fn_df_agreement_lock WHERE id = 1)= 1 ,'success','failed') AS '执行结果'
;