-- 执行sql增加唯一索引前，需要查询是否有重复的name：
-- SELECT name, count(1) from express_set_system_install group by name having count(1) > 1;
-- 告诉现场删除name重复的记录
ALTER TABLE `express_set_system_install` 
	DROP INDEX `System_Install_Name_Idx`,
	ADD UNIQUE INDEX `System_Install_Name_Unique` (`name`);