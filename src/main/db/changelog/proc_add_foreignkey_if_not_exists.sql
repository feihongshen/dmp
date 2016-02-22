-- call proc_add_foreignkey_if_not_exists('tablename', 'columnname', 'referencedtablename', 'referencedcolumnname', 'ALTER TABLE `tablename` ADD FORIEGN KEY `referencedtablename`(`referencedcolumnname`);');
DROP PROCEDURE IF EXISTS `proc_add_foreignkey_if_not_exists`//

CREATE PROCEDURE `proc_add_foreignkey_if_not_exists`(table_name_vc varchar(50), column_name_vc varchar(50), referenced_table_name_vc varchar(50), referenced_column_name_vc varchar(50), exec_sql_vc varchar(500))
BEGIN
	set @index_count = (
	select count(1) cnt
	from INFORMATION_SCHEMA.KEY_COLUMN_USAGE
	where TABLE_SCHEMA = schema()
		and TABLE_NAME = table_name_vc
		and COLUMN_NAME = column_name_vc
		and REFERENCED_TABLE_NAME = referenced_table_name_vc
		and REFERENCED_COLUMN_NAME = referenced_column_name_vc
	);
	
	IF ifnull(@index_count,0) = 0 THEN 
		set @add_index_sql = exec_sql_vc;

		PREPARE stmt FROM @add_index_sql;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
	END IF;
END//

