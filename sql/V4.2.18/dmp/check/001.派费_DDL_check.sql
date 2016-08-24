SELECT 'fn_df_rule_subsidy添加字段' AS '脚本注释', IF((SELECT COUNT(1) FROM information_schema.`COLUMNS` WHERE TABLE_SCHEMA = SCHEMA() AND TABLE_NAME='fn_df_rule_subsidy' AND COLUMN_NAME IN ('start_val','end_val'))=2, 'success', 'failed') AS '执行结果' 
UNION ALL
SELECT 'dmp40_function菜单数据' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function WHERE id = '809111' and functionurl like '%fromPage%')= 1 ,'success','failed') AS '执行结果'
UNION ALL
SELECT 'dmp40_function菜单数据' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function WHERE id = '809112' and functionurl like '%fromPage%')= 1 ,'success','failed') AS '执行结果'
UNION ALL
SELECT 'dmp40_function菜单数据' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function WHERE id = '809101' and functionurl like '%fromPage%')= 1 ,'success','failed') AS '执行结果'
UNION ALL
SELECT 'dmp40_function菜单数据' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function WHERE id = '809102' and functionurl like '%fromPage%')= 1 ,'success','failed') AS '执行结果'
;