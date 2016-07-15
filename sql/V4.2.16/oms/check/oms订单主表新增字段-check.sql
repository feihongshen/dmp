SELECT 'express_ops_cwb_detail添加字段' AS '脚本注释', IF(
(SELECT COUNT(1) FROM information_schema.`COLUMNS`
WHERE TABLE_SCHEMA = SCHEMA() AND TABLE_NAME='express_ops_cwb_detail' AND COLUMN_NAME IN ('order_source')
)=1, 'success', 'failed') AS '执行结果' ;