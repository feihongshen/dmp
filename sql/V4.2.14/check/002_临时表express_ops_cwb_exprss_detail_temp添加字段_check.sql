select '002express_ops_cwb_exprss_detail_temp添加字段' as '脚本注释', if(
(SELECT count(1) from information_schema.`COLUMNS`
where TABLE_SCHEMA = sCHEMA() and TABLE_NAME='express_ops_cwb_exprss_detail_temp' and COLUMN_NAME in ('return_credit')
)=1, 'success', 'failed') as '执行结果' ;