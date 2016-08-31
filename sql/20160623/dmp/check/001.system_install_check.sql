SELECT '001.system_install-数据整理' AS '脚本文件名',IF((SELECT COUNT(1) FROM (SELECT name, count(1) from express_set_system_install group by name having count(1) > 1) AS temp) = 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '001.system_install-删除普通索引' AS '脚本文件名',IF((SELECT COUNT(1) FROM information_schema.statistics WHERE table_schema = schema() and table_name = 'express_set_system_install' and index_name = 'System_Install_Name_Idx' and column_name = 'name') = 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '001.system_install-增加唯一索引' AS '脚本文件名',IF((SELECT COUNT(1) FROM information_schema.statistics WHERE table_schema = schema() and table_name = 'express_set_system_install' and index_name = 'System_Install_Name_Unique' and column_name = 'name') = 1,'success','failed') AS '执行结果'