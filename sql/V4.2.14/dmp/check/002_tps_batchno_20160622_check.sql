SELECT '001_tps_batchno_20160622' AS '脚本文件名',IF((SELECT COUNT(1) FROM express_auto_param_config where name='MQ_REC_TPS_BATCHNO_CHANNEL' = 1),'success','failed') AS '执行结果'
UNION ALL
SELECT '001_tps_batchno_20160622' AS '脚本文件名',IF((SELECT COUNT(1) FROM express_auto_param_config where name='MQ_REC_TPS_BATCHNO_QUEUE' = 1),'success','failed') AS '执行结果'
;
