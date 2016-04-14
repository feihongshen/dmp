-- 新增MQ异常表
CREATE TABLE `mq_exception` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `EXCEPTION_CODE` varchar(128) NOT NULL DEFAULT '0' COMMENT '异常编码',
  `EXCEPTION_INFO` text NOT NULL COMMENT '异常信息',
  `TOPIC` varchar(128) NOT NULL DEFAULT '' COMMENT '主题',
  `MESSAGE_BODY` longtext NOT NULL COMMENT '报文体',
  `MESSAGE_HEADER` longtext NOT NULL COMMENT '报文头',
  `MESSAGE_HEADER_UUID` varchar(64) NOT NULL DEFAULT '' COMMENT '报文头UUID',
  `HANDLE_COUNT` tinyint(4) NOT NULL DEFAULT '0' COMMENT '处理次数',
  `HANDLE_FLAG` tinyint(4) NOT NULL DEFAULT '0' COMMENT '处理结果',
  `REMARKS` varchar(512) NOT NULL DEFAULT '' COMMENT '修改备注',
  `HANDLE_TIME` datetime DEFAULT NULL COMMENT '处理时间',
  `MESSAGE_SOURCE` varchar(32) NOT NULL DEFAULT '' COMMENT '消息来源',
  `IS_AUTO_RESEND` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否自动重发',
  `CREATED_BY_USER` varchar(64) NOT NULL DEFAULT '' COMMENT '创建人',
  `CREATED_OFFICE` varchar(64) NOT NULL DEFAULT '' COMMENT '所属仓库',
  `CREATED_DTM_LOC` datetime DEFAULT NULL COMMENT '创建时间',
  `CREATED_TIME_ZONE` varchar(64) NOT NULL DEFAULT '' COMMENT '时区',
  `UPDATED_BY_USER` varchar(64) NOT NULL DEFAULT '' COMMENT '更新人',
  `UPDATED_OFFICE` varchar(64) NOT NULL DEFAULT '' COMMENT '更新人仓库',
  `UPDATED_DTM_LOC` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `UPDATED_TIME_ZONE` varchar(64) NOT NULL DEFAULT '' COMMENT '更新时区',
  `RECORD_VERSION` bigint(20) NOT NULL DEFAULT '0' COMMENT '数据版本号',
  `IS_DELETED` tinyint(4) DEFAULT '0' COMMENT '删除标记',
  `ROUTEING_KEY` varchar(32) NOT NULL DEFAULT '' COMMENT '匹配规则',
  PRIMARY KEY (`ID`),
  KEY `idx_mq_exceptionexception_code` (`EXCEPTION_CODE`),
  KEY `idx_mq_message_header_uuid` (`MESSAGE_HEADER_UUID`),
  KEY `idx_mq_created_dtm_loc` (`CREATED_DTM_LOC`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='MQ异常表';

-- 增加系统参数
INSERT INTO `express_set_system_install` (`name`, `value`, `chinesename`) VALUES ('mqExceptionExecuteCount', '1000', 'mq异常定时器单次处理条数');
-- 增加MQ异常处理菜单
INSERT INTO `dmp40_function` (`ID`,`functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('3011', '1', 'DMPMQ异常', '3011', 'mqexception/list/1?', 
(select ID from dmp40_function a where a.functionname='系统设置' limit 1));
INSERT INTO `dmp40_function` (`ID`,`functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('3013', '1', 'OMSMQ异常', '3013', '${omsUrl}mqexception/list/1?', 
(select ID from dmp40_function a where a.functionname='系统设置' limit 1));

-- 增加redis查询菜单
INSERT INTO `dmp40_function` (`ID`,`functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('3015', '1', 'DMP-REDIS缓存', '3015', 'redis/list/?', 
(select ID from dmp40_function a where a.functionname='系统设置' limit 1));
INSERT INTO `dmp40_function` (`ID`,`functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('3016', '1', 'OMS-REDIS缓存', '3016', '${omsUrl}redis/list/?', 
(select ID from dmp40_function a where a.functionname='系统设置' limit 1));
INSERT INTO `dmp40_function` (`ID`,`functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('3017', '1', 'EAP-REDIS缓存', '3017', '${eapUrl}redis/list/?', 
(select ID from dmp40_function a where a.functionname='系统设置' limit 1));