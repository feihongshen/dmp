DELETE FROM `dmp40_function` WHERE `ID` = '3050' AND `functionname` = 'REDIS缓存';
DELETE FROM `dmp40_function` WHERE `ID` = '305010' AND `functionname` = 'DMP缓存';
DELETE FROM `dmp40_function` WHERE `ID` = '305011' AND `functionname` = 'OMS缓存';
DELETE FROM `dmp40_function` WHERE `ID` = '305012' AND `functionname` = 'EAP缓存';

INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('3011', 1, 'DMPMQ异常', '3011', 'mqexception/list/1?', '30');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('3013', 1, 'OMSMQ异常', '3013', '${omsUrl}mqexception/list/1?', '30');

DELETE FROM `dmp40_function` WHERE `ID` = '3060' AND `functionname` = 'MQ异常';
DELETE FROM `dmp40_function` WHERE `ID` = '306010' AND `functionname` = 'DMP异常';
DELETE FROM `dmp40_function` WHERE `ID` = '306011' AND `functionname` = 'OMS异常';
