SELECT '002' AS '脚本注释',IF((SELECT count(1) from information_schema.`COLUMNS`
where TABLE_SCHEMA=SCHEMA()
and TABLE_NAME='express_set_user'
and COLUMN_NAME='username'
and COLUMN_KEY='UNI') =1,'success','failed') AS '执行结果';
