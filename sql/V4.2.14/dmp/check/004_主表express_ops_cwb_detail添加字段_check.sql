select '003express_ops_cwb_detail添加字段' as '脚本注释', if(
(SELECT count(1) from information_schema.`COLUMNS`
where TABLE_SCHEMA = sCHEMA() and TABLE_NAME='express_ops_cwb_detail' and COLUMN_NAME in ('delivery_permit','do_type','order_source')
)=1, 'success', 'failed') as '执行结果' ;
