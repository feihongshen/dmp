select '001.branch_updateuser_check' as '脚本注释', if(
(SELECT count(1) from information_schema.`COLUMNS`
where TABLE_SCHEMA = SCHEMA() and TABLE_NAME='express_set_branch' and COLUMN_NAME in ('update_user')
 and COLUMN_TYPE = 'varchar(50)'
)=1, 'success', 'failed') as '执行结果' ;