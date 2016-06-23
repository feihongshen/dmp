#check脚本
SELECT '新增表express_ops_tpstranscwb' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'express_ops_tpstranscwb') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '新增运单号打印菜单' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function where ID=505070) > 0,'success','failed') AS '执行结果'
 