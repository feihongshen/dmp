
SELECT 'express_ems_flow_info_temp' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'express_ems_flow_info_temp') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'express_ems_flow_info' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'express_ems_flow_info') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'express_ems_dmp_transcwb' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'express_ems_dmp_transcwb') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT 'express_ems_order_b2ctemp' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables  WHERE table_schema = SCHEMA() AND table_name = 'express_ems_order_b2ctemp') > 0,'success','failed') AS '执行结果';