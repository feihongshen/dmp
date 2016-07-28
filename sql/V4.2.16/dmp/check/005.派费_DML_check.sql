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
)= 11 ,'success','failed') AS '执行结果';