SELECT 'express_ops_branch_daohuo站点到货统计表' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'express_ops_branch_daohuo') > 0,'success','failed') AS '执行结果'
UNION ALL 
SELECT 'express_set_system_install站点到货迁移开关' AS '脚本注释',IF((SELECT COUNT(1) FROM express_set_system_install where name='branchdaohuoqianyi') > 0,'success','failed') AS '执行结果'
;
