-- 增加redis查询菜单
INSERT INTO `dmp40_function` (`ID`,`functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('3015', '1', 'DMP-REDIS缓存', '3015', 'redis/list/?', 
(select ID from dmp40_function a where a.functionname='系统设置' limit 1));
INSERT INTO `dmp40_function` (`ID`,`functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('3016', '1', 'OMS-REDIS缓存', '3016', '${omsUrl}redis/list/?', 
(select ID from dmp40_function a where a.functionname='系统设置' limit 1));
INSERT INTO `dmp40_function` (`ID`,`functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('3017', '1', 'EAP-REDIS缓存', '3017', '${eapUrl}redis/list.do?', 
(select ID from dmp40_function a where a.functionname='系统设置' limit 1));