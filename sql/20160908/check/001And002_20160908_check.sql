SELECT '001' AS '脚本注释', IF((SELECT count(1) from information_schema.`COLUMNS` where TABLE_NAME='express_set_user_inf' and COLUMN_NAME='create_date' and EXTRA='' or EXTRA is null) = 1,'success','failed') AS '执行结果'
UNION ALL
SELECT '002' AS '脚本注释',IF((SELECT count(1) from information_schema.`COLUMNS` where TABLE_NAME='express_set_branch_inf' and COLUMN_NAME='create_date' and EXTRA='' or EXTRA is null) =1,'success','failed') AS '执行结果'
;