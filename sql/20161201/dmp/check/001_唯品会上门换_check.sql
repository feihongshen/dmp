SELECT '订单表加exchange_flag' AS '脚本注释',IF((SELECT count(1) from information_schema.`COLUMNS` where TABLE_SCHEMA = schema() AND TABLE_NAME='express_ops_cwb_detail' and COLUMN_NAME='exchange_flag' ) =1,'success','failed') AS '执行结果'
UNION ALL
SELECT '订单表加exchange_cwb' AS '脚本注释',IF((SELECT count(1) from information_schema.`COLUMNS` where TABLE_SCHEMA = schema() AND TABLE_NAME='express_ops_cwb_detail' and COLUMN_NAME='exchange_cwb' ) =1,'success','failed') AS '执行结果'
UNION ALL
SELECT '订单临时表加exchange_flag' AS '脚本注释',IF((SELECT count(1) from information_schema.`COLUMNS` where TABLE_SCHEMA = schema() AND TABLE_NAME='express_ops_cwb_detail_b2ctemp' and COLUMN_NAME='exchange_flag' ) =1,'success','failed') AS '执行结果'
UNION ALL
SELECT '订单临时表加exchange_cwb' AS '脚本注释',IF((SELECT count(1) from information_schema.`COLUMNS` where TABLE_SCHEMA = schema() AND TABLE_NAME='express_ops_cwb_detail_b2ctemp' and COLUMN_NAME='exchange_cwb' ) =1,'success','failed') AS '执行结果'
;