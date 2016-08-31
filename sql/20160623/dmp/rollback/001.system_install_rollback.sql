ALTER TABLE `express_set_system_install` 
	ADD INDEX `System_Install_Name_Idx` (`name`),
	DROP INDEX `System_Install_Name_Unique` ;