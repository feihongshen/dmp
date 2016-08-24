SELECT 'dmp40_function菜单数据' AS '脚本注释',IF((
(SELECT COUNT(1) FROM dmp40_function WHERE ID = '809113' AND functionname='妥投考核报表')
)= 1 ,'success','failed') AS '执行结果'
UNION ALL
SELECT 'dmp40_function菜单数据' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function WHERE id = '809111' and functionurl like '%fromPage%')= 1 ,'success','failed') AS '执行结果'
UNION ALL
SELECT 'dmp40_function菜单数据' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function WHERE id = '809112' and functionurl like '%fromPage%')= 1 ,'success','failed') AS '执行结果'
UNION ALL
SELECT 'dmp40_function菜单数据' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function WHERE id = '809101' and functionurl like '%fromPage%')= 1 ,'success','failed') AS '执行结果'
UNION ALL
SELECT 'dmp40_function菜单数据' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function WHERE id = '809102' and functionurl like '%fromPage%')= 1 ,'success','failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_agreement' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'fn_df_confirm_rate_subsidy') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'fn_df_bill_org' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'fn_df_confirm_rate_subsidy_detail') > 0,'success','failed') AS '执行结果';