SELECT '新增货物类型修改申请（express_ops_applyeditcartype）' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = SCHEMA() AND table_name = 'express_ops_applyeditcartype') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '增加菜单“订/运单货物类型修改”' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function where id=602566) > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '增加菜单“订/运单货物类型审核”' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function where id=602567) > 0,'success','failed') AS '执行结果'
