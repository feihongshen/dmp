INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) 
	VALUES ('5055', '1', '快递预约单管理', '5055', '', '50');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) 
	VALUES ('505510', '2', '快递预约单处理(省质控)', '505510', 'express2/reserveOrder/handle?', '5055');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) 
	VALUES ('505520', '2', '快递预约单查询', '505520', 'express2/reserveOrder/query?', '5055');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) 
	VALUES ('505530', '2', '快递预约单处理(站点)', '505530', 'express2/reserveOrder/handleWarehouse?', '5055');