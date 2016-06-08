-- 检查菜单
SELECT * FROM `dmp40_function` WHERE `ID` = '3050' AND `functionname` = 'REDIS缓存' AND `parentfunctionid` IS NOT NULL;	-- 有一条记录
SELECT * FROM `dmp40_function` WHERE `ID` = '305010' AND `functionname` = 'DMP缓存' AND `parentfunctionid` IS NOT NULL;	-- 有一条记录
SELECT * FROM `dmp40_function` WHERE `ID` = '305011' AND `functionname` = 'OMS缓存' AND `parentfunctionid` IS NOT NULL;	-- 有一条记录
SELECT * FROM `dmp40_function` WHERE `ID` = '305012' AND `functionname` = 'EAP缓存' AND `parentfunctionid` IS NOT NULL;	-- 有一条记录

SELECT * FROM `dmp40_function` WHERE `ID` = '3011';	-- 没有记录
SELECT * FROM `dmp40_function` WHERE `ID` = '3013';	-- 没有记录

SELECT * FROM `dmp40_function` WHERE `ID` = '3060' AND `functionname` = 'MQ异常' AND `parentfunctionid` IS NOT NULL;	-- 有一条记录
SELECT * FROM `dmp40_function` WHERE `ID` = '306010' AND `functionname` = 'DMP异常' AND `parentfunctionid` IS NOT NULL;	-- 有一条记录
SELECT * FROM `dmp40_function` WHERE `ID` = '306011' AND `functionname` = 'OMS异常' AND `parentfunctionid` IS NOT NULL;	-- 有一条记录