select '003express_ops_cwb_detail添加字段' as '脚本注释', if(
(SELECT count(1) from information_schema.`COLUMNS`
where TABLE_SCHEMA=SCHEMA() AND TABLE_NAME='express_ops_cwb_detail'
and COLUMN_NAME in ('height', 'width', 'length') and data_type='decimal'
)=3, 'success', 'failed') as '执行结果' ;
