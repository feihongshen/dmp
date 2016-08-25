select '001邮政运单绑定添加绑定时间' as '执行脚本', if(
(SELECT count(1) from information_schema.`COLUMNS`
where TABLE_SCHEMA = SCHEMA() and TABLE_NAME='express_ems_dmp_transcwb' and COLUMN_NAME in ('bing_time') 
)=1, 'success', 'failed') as '执行结果' ;