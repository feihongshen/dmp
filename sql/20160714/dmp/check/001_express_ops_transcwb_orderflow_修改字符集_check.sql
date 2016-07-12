select '001_1修改字段字符集_check' as '脚本注释', if(
(SELECT COUNT(1) from information_schema.`COLUMNS`
where TABLE_SCHEMA = SCHEMA() and TABLE_NAME='express_ops_transcwb_orderflow' and COLUMN_NAME in ('scancwb')
 and COLLATION_NAME = 'utf8_general_ci'
)=1, 'success', 'failed') as '执行结果' ;
