SELECT '1.增加webPassword字段' AS '脚本注释',IF((
SELECT COUNT(1) FROM information_schema.`COLUMNS` WHERE table_schema = schema() and table_name = 'express_set_user'  and column_name = 'webPassword'
)= 1 ,'success','failed') AS '执行结果'
UNION ALL
SELECT '2.将原密码更新到webPassword' AS '脚本注释',IF((
select count(1) from express_set_user where webPassword != password
)= 0 ,'success','failed') AS '执行结果';
