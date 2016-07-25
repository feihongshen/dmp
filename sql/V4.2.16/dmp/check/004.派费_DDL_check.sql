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
UNION ALL
SELECT 'eap_qrtz_triggers数据' AS '脚本注释',IF((SELECT COUNT(1) FROM eap_qrtz_triggers WHERE TRIGGER_NAME='dfCalculateServiceExecutorCronTrigger' and DESCRIPTION='派费生成定时任务')= 1 ,'success','failed') AS '执行结果'
UNION ALL
SELECT '小件员菜单数据' AS '脚本注释',IF((
(SELECT COUNT(1) FROM dmp40_function WHERE ID = '8091' AND functionname='小件员派费结算')+
(SELECT COUNT(1) FROM dmp40_function WHERE ID = '809101' AND functionname='新增派费协议')+
(SELECT COUNT(1) FROM dmp40_function WHERE ID = '809102' AND functionname='派费协议管理')+
(SELECT COUNT(1) FROM dmp40_function WHERE ID = '809103' AND functionname='账单基础信息设置')+
(SELECT COUNT(1) FROM dmp40_function WHERE ID = '809104' AND functionname='月度妥投率报表')+
(SELECT COUNT(1) FROM dmp40_function WHERE ID = '809105' AND functionname='奖罚登记管理')+
(SELECT COUNT(1) FROM dmp40_function WHERE ID = '809106' AND functionname='加盟站派费账单管理')+
(SELECT COUNT(1) FROM dmp40_function WHERE ID = '809107' AND functionname='小件员派费账单管理')+
(SELECT COUNT(1) FROM dmp40_function WHERE ID = '809108' AND functionname='加盟站点计费订单明细')+
(SELECT COUNT(1) FROM dmp40_function WHERE ID = '809109' AND functionname='小件员计费订单明细')+
(SELECT COUNT(1) FROM dmp40_function WHERE ID = '809110' AND functionname='派费调整记录')
)= 11 ,'success','failed') AS '执行结果'
;