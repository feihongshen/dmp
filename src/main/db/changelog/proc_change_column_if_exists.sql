-- call proc_change_column_if_exists('tablename', 'column', 'ALTER TABLE express_ops_abnormal_order CHANGE COLUMN `columnname` NOT NULL DEFAULT '''';');
DROP PROCEDURE IF EXISTS `proc_change_column_if_exists`//

CREATE PROCEDURE `proc_change_column_if_exists`(table_name_vc varchar(50), column_name_vc varchar(50), exec_sql_vc varchar(200))
BEGIN
	set @column_count = (
	select count(1) cnt
	from INFORMATION_SCHEMA.COLUMNS
	where TABLE_SCHEMA = schema()
		and TABLE_NAME = table_name_vc
		and COLUMN_NAME = column_name_vc
	);
	
	IF ifnull(@column_count,0) > 0 THEN 
		set @add_column_sql = exec_sql_vc;

		PREPARE stmt FROM @add_column_sql;
		EXECUTE stmt;
		DEALLOCATE PREPARE stmt;
	END IF;
END//