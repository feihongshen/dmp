SELECT '新建eap信息重推表' AS '脚本文件名',IF((SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = schema() and table_name = 'fn_msg_record') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '新增EAP异常菜单' AS '脚本文件名',IF((SELECT COUNT(1) FROM dmp40_function WHERE ID = '306012' AND functionname='EAP异常') = 1 ,'success','failed') AS '执行结果'
UNION ALL
SELECT '新增是否抵扣字段' AS '脚本注释', IF((SELECT count(1) from information_schema.`COLUMNS` where TABLE_NAME='fn_cust_pay_report_cfg' and COLUMN_NAME='is_collect_deduction') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '新增是否抵扣字段索引' AS '脚本注释', IF((SELECT COUNT(1) FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_NAME = 'fn_cust_pay_report_cfg' AND INDEX_NAME = 'is_collect_deduction_idx') > 0, 'success', 'failed') AS '执行结果'
UNION ALL
SELECT '新增字段KINGDEE_LOGIN_DB_TYPE' AS '脚本注释', IF((SELECT COUNT(1) FROM express_set_system_install WHERE name = 'KINGDEE_LOGIN_DB_TYPE') = 1, 'success', 'failed') AS '执行结果'
UNION ALL
SELECT '新增字段KINGDEE_LOGIN_LANGUAGE' AS '脚本注释', IF((SELECT COUNT(1) FROM express_set_system_install WHERE name = 'KINGDEE_LOGIN_LANGUAGE') = 1, 'success', 'failed') AS '执行结果'
UNION ALL
SELECT '新增字段KINGDEE_LOGIN_DC_NAME' AS '脚本注释', IF((SELECT COUNT(1) FROM express_set_system_install WHERE name = 'KINGDEE_LOGIN_DC_NAME') = 1, 'success', 'failed') AS '执行结果'
UNION ALL
SELECT '新增字段KINGDEE_LOGIN_SLN_NAME' AS '脚本注释', IF((SELECT COUNT(1) FROM express_set_system_install WHERE name = 'KINGDEE_LOGIN_SLN_NAME') = 1, 'success', 'failed') AS '执行结果'
UNION ALL
SELECT '新增字段KINGDEE_PASSWORD' AS '脚本注释', IF((SELECT COUNT(1) FROM express_set_system_install WHERE name = 'KINGDEE_PASSWORD') = 1, 'success', 'failed') AS '执行结果'
UNION ALL
SELECT '新增字段KINGDEE_USERNAME' AS '脚本注释', IF((SELECT COUNT(1) FROM express_set_system_install WHERE name = 'KINGDEE_USERNAME') = 1, 'success', 'failed') AS '执行结果'
UNION ALL
SELECT '新增字段KINGDEE_RAMDOM' AS '脚本注释', IF((SELECT COUNT(1) FROM express_set_system_install WHERE name = 'KINGDEE_RAMDOM') = 1, 'success', 'failed') AS '执行结果'
UNION ALL
SELECT '新增字段KINGDEE_SECRET' AS '脚本注释', IF((SELECT COUNT(1) FROM express_set_system_install WHERE name = 'KINGDEE_SECRET') = 1, 'success', 'failed') AS '执行结果'
UNION ALL
SELECT '新增字段KINGDEE_URL_FOR_EAS' AS '脚本注释', IF((SELECT COUNT(1) FROM express_set_system_install WHERE name = 'KINGDEE_URL_FOR_EAS') = 1, 'success', 'failed') AS '执行结果'
UNION ALL
SELECT '新增字段EAS_KINGDEE_INT_ENABLE' AS '脚本注释', IF((SELECT COUNT(1) FROM express_set_system_install WHERE name = 'EAS_KINGDEE_INT_ENABLE') = 1, 'success', 'failed') AS '执行结果'
UNION ALL
SELECT '新增字段EAS_PERSOPN_IN_KINGDEE' AS '脚本注释', IF((SELECT COUNT(1) FROM express_set_system_install WHERE name = 'EAS_PERSOPN_IN_KINGDEE') = 1, 'success', 'failed') AS '执行结果'

