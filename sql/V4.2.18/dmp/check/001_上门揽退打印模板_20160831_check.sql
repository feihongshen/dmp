SELECT '增加表express_ops_smt_cwb' AS '脚本注释', IF((SELECT count(1) FROM information_schema.`TABLES` WHERE TABLE_SCHEMA = SCHEMA () AND TABLE_NAME = 'express_ops_smt_cwb')=1,'success','failed') AS '执行结果'
UNION ALL
SELECT 'express_set_excel_column加列returnnoindex、returnaddressindex' AS '脚本注释', IF((SELECT COUNT(1) FROM information_schema.`COLUMNS` WHERE TABLE_SCHEMA = SCHEMA() and TABLE_NAME = 'express_set_excel_column' AND COLUMN_NAME IN('returnnoindex', 'returnaddressindex') AND DATA_TYPE = 'int') = 2,'success','failed') AS '执行结果';
