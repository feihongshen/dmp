ALTER TABLE `express_ops_outwarehousegroup` ADD COLUMN `baleid` int(11) DEFAULT '0' NULL;


-- 修改 运营监控下妥投率菜单级别
update dmp40_function set functionlevel=1 where id=7010 and functionlevel=9;