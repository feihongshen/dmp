-- 检查建表语句
select * from mq_exception;

-- 检查系统参数
select * from express_set_system_install where name='mqExceptionExecuteCount';

-- 检查菜单
select * from dmp40_function where functionorder='3011';
select * from dmp40_function where functionorder='3013';



-- 检查索引
select count(1) cnt from INFORMATION_SCHEMA.STATISTICS where TABLE_SCHEMA = schema() and TABLE_NAME = 'express_set_joint' and INDEX_NAME = 'joint_num_unique' and COLUMN_NAME = 'joint_num';		-- 结果为1


-- 检查建表语句
select * from express_ops_address_match_log;		-- 没有报错