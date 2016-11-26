set @tmp_dmp40_function_id=(select max(ID)+10 from dmp40_function where parentfunctionid=8030);
INSERT INTO `dmp40_function` (
	`ID`,
	`functionlevel`,
	`functionname`,
	`functionorder`,
	`functionurl`,
	`parentfunctionid`
)
VALUES
	(
		@tmp_dmp40_function_id,
		'2',
		'签收站点余额月报表',
		@tmp_dmp40_function_id,
		'${eapUrl}reportMonthForm.do?index&',
		'8030'
	);
