SELECT 'express_ops_cwb_detail增加exceldeliverid' AS '脚本注释', IF((SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = schema() and table_name = 'express_ops_cwb_detail' AND column_name='exceldeliverid' AND data_type = 'int') = 1,'success','failed') AS '执行结果'
UNION ALL
SELECT 'express_ops_editcwbinfo增加oldexceldeliverid' AS '脚本注释', IF((SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = schema() and table_name = 'express_ops_editcwbinfo' AND column_name='oldexceldeliverid' AND data_type = 'int') = 1,'success','failed') AS '执行结果'
UNION ALL
SELECT 'express_ops_editcwbinfo增加newexceldeliverid' AS '脚本注释', IF((SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = schema() and table_name = 'express_ops_editcwbinfo' AND column_name='newexceldeliverid' AND data_type = 'int') = 1,'success','failed') AS '执行结果'
UNION ALL
SELECT 'express_ops_editcwbinfo增加oldexceldeliver' AS '脚本注释', IF((SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = schema() and table_name = 'express_ops_editcwbinfo' AND column_name='oldexceldeliver' AND data_type = 'varchar') = 1,'success','failed') AS '执行结果'
UNION ALL
SELECT 'express_ops_editcwbinfo增加newexceldeliver' AS '脚本注释', IF((SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = schema() and table_name = 'express_ops_editcwbinfo' AND column_name='newexceldeliver' AND data_type = 'varchar') = 1,'success','failed') AS '执行结果'
UNION ALL
SELECT 'express_service_revise_address增加newexceldeliver' AS '脚本注释', IF((SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = schema() and table_name = 'express_service_revise_address' AND column_name='exceldeliver' AND data_type = 'varchar') = 1,'success','failed') AS '执行结果'