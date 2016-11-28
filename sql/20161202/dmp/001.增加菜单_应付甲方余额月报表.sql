set @tmp_dmp40_function_parentid='8020';
set @tmp_dmp40_function_id=(select case when left(max(ID)+10,4)=@tmp_dmp40_function_parentid then max(ID)+10 else CONCAT(max(ID),'1') end from dmp40_function where parentfunctionid=@tmp_dmp40_function_parentid);
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
		'应付甲方余额月报表',
		@tmp_dmp40_function_id,
		'${eapUrl}branceReportMonthly.do?isIframe&index&',
		@tmp_dmp40_function_parentid
	);
 