SELECT '001_tps_emaildate_20160612' AS '脚本文件名',IF((SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = SCHEMA() AND table_name = 'express_ops_tps_flow_tmp' AND column_name='sendweight') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '001_tps_emaildate_20160612' AS '脚本文件名',IF((SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = SCHEMA() AND table_name = 'express_ops_tps_flow_tmp' AND column_name='sendemaildate') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '001_tps_emaildate_20160612' AS '脚本文件名',IF((SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = SCHEMA() AND table_name = 'express_ops_tps_flow_tmp_sent' AND column_name='sendweight') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '001_tps_emaildate_20160612' AS '脚本文件名',IF((SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = SCHEMA() AND table_name = 'express_ops_tps_flow_tmp_sent' AND column_name='sendemaildate') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '001_tps_emaildate_20160612' AS '脚本文件名',IF((SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = SCHEMA() and table_name = 'express_ops_tps_flow_tmp_mps') > 0,'success','failed') AS '执行结果';

