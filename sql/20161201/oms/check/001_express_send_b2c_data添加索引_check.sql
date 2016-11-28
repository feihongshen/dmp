SELECT '002' AS '脚本注释',IF((SELECT count(1) from information_schema.`COLUMNS`
where TABLE_SCHEMA=SCHEMA()
and TABLE_NAME='express_send_b2c_data'
and COLUMN_NAME='remark'
and COLUMN_KEY='MUL') =1,'success','failed') AS '执行结果';
