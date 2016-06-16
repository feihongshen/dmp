-- 删除MQ异常表
drop table mq_exception;

-- 删除系统参数
delete from express_set_system_install where name = 'mqExceptionExecuteCount';

--删除MQ异常菜单
delete from dmp40_function where id = '3011';
delete from dmp40_function where id = '3013';


-- 删除唯一索引
ALTER TABLE express_set_joint DROP INDEX joint_num_unique;

-- 删除地址匹配日志表
drop table express_ops_address_match_log;