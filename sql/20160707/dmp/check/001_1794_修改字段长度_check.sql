select '001_1794_修改字段长度_check' as '脚本注释', if(
(SELECT count(1) from information_schema.`COLUMNS`
where TABLE_SCHEMA = SCHEMA() and TABLE_NAME='express_orderbackruku_record' and COLUMN_NAME in ('consigneeaddress')
 and COLUMN_TYPE = 'varchar(500)'
)=1, 'success', 'failed') as '执行结果' ;