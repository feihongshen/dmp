SELECT '创建表fn_station_sign_order_details_snapshot_express' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = schema() and table_name = 'fn_station_sign_order_details_snapshot_express') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '修改表fn_station_sign_order_details_snapshot' AS '脚本注释',IF((
(SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = schema() and table_name = 'fn_station_sign_order_details_snapshot' AND column_name IN('instationid','packagefee','insuredfee','totalfee','paymethod','collectorid','instationdatetime'))
+ (SELECT COUNT(1) FROM information_schema.statistics WHERE table_schema = schema() and table_name = 'express_set_user' AND index_name='idx_username')) = 8,'success','failed') AS '执行结果';
