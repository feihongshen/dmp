select 'express_ops_cwb_detail添加字段' as '脚本注释', if(
(SELECT count(1) from information_schema.`COLUMNS`
where TABLE_SCHEMA = sCHEMA() and TABLE_NAME='express_ops_cwb_detail' and COLUMN_NAME in ('do_type','order_source')
)=2, 'success', 'failed') as '执行结果' ;

select 'express_ops_cwb_detail_b2ctemp添加字段' as '脚本注释', if(
(SELECT count(1) from information_schema.`COLUMNS`
where TABLE_SCHEMA = sCHEMA() and TABLE_NAME='express_ops_cwb_detail_b2ctemp' and COLUMN_NAME in ('do_type','announcedvalue','paymethod','order_source')
)=4, 'success', 'failed') as '执行结果' ;