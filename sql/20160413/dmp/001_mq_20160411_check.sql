-- 检查建表语句
select * from mq_exception;

-- 检查系统参数
select * from express_set_system_install where name='mqExceptionExecuteCount';

-- 检查菜单
select * from dmp40_function where functionorder='3011';
select * from dmp40_function where functionorder='3013';
