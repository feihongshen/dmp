SELECT 'express_set_kufang_branch_map' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'express_set_kufang_branch_map') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'dmp40_function数据' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function WHERE id = '303050' and functionname='二级分拣库流向')= 1 ,'success','failed') AS '执行结果'
;