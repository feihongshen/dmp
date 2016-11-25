drop table if EXISTS tmp_dmp40_function_id;
create table tmp_dmp40_function_id (id VARCHAR(64));
insert into tmp_dmp40_function_id (id)  select max(ID)+10 from dmp40_function where parentfunctionid=8030;

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
		(select id from tmp_dmp40_function_id),
		'2',
		'签收站点余额月报表',
		(select id from tmp_dmp40_function_id),
		'${eapUrl}reportMonthForm.do?index&',
		'8030'
	);
drop table tmp_dmp40_function_id;