SELECT * FROM dmp40_function WHERE ID = '502096';
SELECT * FROM dmp40_function WHERE ID = '502097';
select 'express_set_system_install插入字段' as '脚本注释', if((SELECT count(1) from express_set_system_install
where `name` = 'emsbranchid')=1, 'success', 'failed') as '执行结果';