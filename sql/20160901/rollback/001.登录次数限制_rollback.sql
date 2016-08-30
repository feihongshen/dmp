SELECT 'user表增加字段' AS '脚本注释', IF((SELECT COUNT(1) FROM information_schema.`COLUMNS` WHERE TABLE_SCHEMA = SCHEMA() and TABLE_NAME = 'express_set_user' AND COLUMN_NAME IN('lastLoginState', 'loginFailCount', 'lastLoginTryTime')) = 3,'success','failed') AS '执行结果'
UNION ALL
SELECT '插入系统参数' AS '脚本注释',IF((SELECT COUNT(1) FROM express_set_system_install where name in ('loginFailMaxTryTimeLimit', 'loginForbiddenIntervalInMinutes')) =2,'success','failed') AS '执行结果'
;