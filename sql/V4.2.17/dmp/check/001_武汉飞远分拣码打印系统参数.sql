select 'express_set_system_install插入字段' as '脚本注释', if((SELECT count(1) from express_set_system_install
where `name` = 'printdeliveryuser')=1, 'success', 'failed') as '执行结果';