set @tmp20161128_dmp40_function_id=(select max(ID)+10 from dmp40_function where parentfunctionid=8020);
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
		@tmp20161128_dmp40_function_id,
		'2',
		'应付甲方余额月报表',
		@tmp20161128_dmp40_function_id,
		'${eapUrl}branceReportMonthly.do?isIframe&index&',
		'8020'
	);
	