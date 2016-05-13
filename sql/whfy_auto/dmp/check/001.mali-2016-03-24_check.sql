SELECT '机构管理' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = schema() and table_name = 'express_set_branch' AND column_name='outputno') = 1,'success','failed') AS '执行结果'
UNION ALL
SELECT '上货口表' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = schema() and table_name = 'express_set_entrance') = 1,'success','failed') AS '执行结果'
UNION ALL
SELECT '系统参数' AS '脚本注释',IF((
(SELECT COUNT(1) FROM express_set_system_install WHERE name='AutoAllocating')
)= 1 ,'success','failed') AS '执行结果';