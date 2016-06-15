-- 增加redis查询菜单(二级菜单)
INSERT INTO `dmp40_function` (`ID`,`functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('3050', '1', 'REDIS缓存', '3050', '', 
(select ID from dmp40_function a where a.functionname='系统设置' limit 1));
-- 增加redis查询菜单(三级菜单)
INSERT INTO `dmp40_function` (`ID`,`functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('305010', '2', 'DMP缓存', '305010', 'redis/list/?', 
(select ID from dmp40_function a where a.functionname='REDIS缓存' limit 1));
INSERT INTO `dmp40_function` (`ID`,`functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('305011', '2', 'OMS缓存', '305011', '${omsUrl}redis/list/?', 
(select ID from dmp40_function a where a.functionname='REDIS缓存' limit 1));
INSERT INTO `dmp40_function` (`ID`,`functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('305012', '2', 'EAP缓存', '305012', '${eapUrl}redis/list.do?', 
(select ID from dmp40_function a where a.functionname='REDIS缓存' limit 1));


-- 删除原MQ异常菜单
delete from dmp40_function where id in (3011,3013);
-- 增加MQ异常(二级菜单)
INSERT INTO `dmp40_function` (`ID`,`functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('3060', '1', 'MQ异常', '3060', '', 
(select ID from dmp40_function a where a.functionname='系统设置' limit 1));
-- 增加MQ异常(三级菜单)
INSERT INTO `dmp40_function` (`ID`,`functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('306010', '2', 'DMP异常', '306010', 'mqexception/list/1?', 
(select ID from dmp40_function a where a.functionname='MQ异常' limit 1));
INSERT INTO `dmp40_function` (`ID`,`functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('306011', '2', 'OMS异常', '306011', '${omsUrl}mqexception/list/1?', 
(select ID from dmp40_function a where a.functionname='MQ异常' limit 1));