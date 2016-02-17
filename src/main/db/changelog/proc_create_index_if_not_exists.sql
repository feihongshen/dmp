-- call proc_create_index_if_not_exists('tablename', 'indexname', 'columnname', 'ALTER TABLE `tablename` ADD INDEX `indexname` (`columnname`);');
DROP PROCEDURE IF EXISTS `proc_create_index_if_not_exists`//

CREATE PROCEDURE `proc_create_index_if_not_exists`(table_name_vc varchar(50), index_name_vc varchar(50), column_name_vc varchar(50), exec_sql_vc varchar(500))
BEGIN
	set @index_count = (
	select count(1) cnt
	from INFORMATION_SCHEMA.STATISTICS
	where TABLE_SCHEMA = schema()
		and TABLE_NAME = table_name_vc
		and INDEX_NAME = index_name_vc
		and (column_name_vc = '' OR COLUMN_NAME = column_name_vc)
	);
	
	IF ifnull(@index_count,0) = 0 THEN 
		set @add_index_sql = exec_sql_vc;

		PREPARE stmt FROM @add_index_sql;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
	END IF;
END//

