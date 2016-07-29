INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) 
    VALUES ('502096', 2, '邮政面单打印', '502096', 'emsSmallPackage/print?', '5020');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) 
    VALUES ('502097', 2, '邮政订单信息查询', '502097', 'emsSmallPackage/query?', '5020');
INSERT INTO `express_set_system_install` (`name`, `value`, `chinesename`) 
VALUES('emsbranchid','468', '邮政站点id');