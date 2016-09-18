SELECT 'dmp40_function派费结算菜单数据' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function WHERE id = '809101' and functionurl like '%fromPage%' AND functionname='新增派费协议')= 1 ,'success','failed') AS '执行结果'
UNION ALL
SELECT 'dmp40_function派费结算菜单数据' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function WHERE id = '809102' and functionurl like '%fromPage%' AND functionname='新增补贴协议')= 1 ,'success','failed') AS '执行结果'
UNION ALL
SELECT 'dmp40_function派费结算菜单数据' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function WHERE id = '809103' and functionurl like '%fromPage%' AND functionname='派费协议管理')= 1 ,'success','failed') AS '执行结果'
UNION ALL
SELECT 'dmp40_function派费结算菜单数据' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function WHERE id = '809104' and functionurl like '%fromPage%' AND functionname='补贴协议管理')= 1 ,'success','failed') AS '执行结果'
UNION ALL
SELECT 'dmp40_function派费结算菜单数据' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function WHERE ID = '809105' AND functionname='账单基础信息设置')= 1 ,'success','failed') AS '执行结果'
UNION ALL
SELECT 'dmp40_function派费结算菜单数据' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function WHERE ID = '809106' AND functionname='月度妥投率报表')= 1 ,'success','failed') AS '执行结果'
UNION ALL
SELECT 'dmp40_function派费结算菜单数据' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function WHERE ID = '809107' AND functionname='小件员妥投补贴报表')= 1 ,'success','failed') AS '执行结果'
UNION ALL
SELECT 'dmp40_function派费结算菜单数据' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function WHERE ID = '809108' AND functionname='奖罚登记管理')= 1 ,'success','failed') AS '执行结果'
UNION ALL
SELECT 'dmp40_function派费结算菜单数据' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function WHERE ID = '809109' AND functionname='加盟站派费账单管理')= 1 ,'success','failed') AS '执行结果'
UNION ALL
SELECT 'dmp40_function派费结算菜单数据' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function WHERE ID = '809110' AND functionname='小件员派费账单管理')= 1 ,'success','failed') AS '执行结果'
UNION ALL
SELECT 'dmp40_function派费结算菜单数据' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function WHERE ID = '809111' AND functionname='加盟站点计费订单明细')= 1 ,'success','failed') AS '执行结果'
UNION ALL
SELECT 'dmp40_function派费结算菜单数据' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function WHERE ID = '809112' AND functionname='小件员计费订单明细')= 1 ,'success','failed') AS '执行结果'
UNION ALL
SELECT 'dmp40_function派费结算菜单数据' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function WHERE ID = '809113' AND functionname='派费调整记录')= 1 ,'success','failed') AS '执行结果'
;