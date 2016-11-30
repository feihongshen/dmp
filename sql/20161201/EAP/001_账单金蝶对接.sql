CREATE TABLE `fn_msg_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `rqst_msg` mediumtext COMMENT '发送的消息',
  `decoded_rqst_msg` mediumtext COMMENT '解密发送的消息',
  `resp_msg` mediumtext COMMENT '接收的消息',
  `decoded_resp_msg` mediumtext COMMENT '解密返回的消息',
  `comm_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '通讯类型 WEBSERVICE(1)',
  `intf` tinyint(4) NOT NULL DEFAULT '0' COMMENT '通信接口名字 CUSTOMER_BILL_KINGDEE(1)',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态 RUNNING(0), DONE(1)',
  `suc_or_fail` tinyint(4) NOT NULL DEFAULT '0' COMMENT '成功/失败 FAIL(0), SUCCEED(1)',
  `send_or_recv` tinyint(4) NOT NULL DEFAULT '0' COMMENT '接收/发送 SEND(0), RECEIVE(1)',
  `create_time` datetime NOT NULL COMMENT '生成时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_user` varchar(255) NOT NULL DEFAULT '' COMMENT '生成数据操作人',
  `update_user` varchar(255) DEFAULT '' COMMENT '更新数据操作人',
  PRIMARY KEY (`id`),
  KEY `fn_msg_record_interface_idx` (`intf`),
  KEY `fn_msg_record_status_idx` (`status`),
  KEY `fn_msg_record_send_or_recv_idx` (`send_or_recv`),
  KEY `fn_msg_record_create_time_idx` (`create_time`),
  KEY `fn_msg_record_suc_or_fail_idx` (`suc_or_fail`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`)
VALUES (306012, 2, 'EAP异常', 306012, '${eapUrl}msgRecord.do?index&', 3060);

alter table `fn_cust_pay_report_cfg` add (`is_collect_deduction` tinyint not null default FALSE);
ALTER TABLE `fn_cust_pay_report_cfg` ADD INDEX `is_collect_deduction_idx` (`is_collect_deduction`);

INSERT INTO `express_set_system_install` (`name`, `value`, `chinesename`) VALUES ('KINGDEE_LOGIN_DB_TYPE', '0', '金蝶登录DB类型');
INSERT INTO `express_set_system_install` (`name`, `value`, `chinesename`) VALUES ('KINGDEE_LOGIN_LANGUAGE', 'L2', '金蝶登录LANGUAGE');
INSERT INTO `express_set_system_install` (`name`, `value`, `chinesename`) VALUES ('KINGDEE_LOGIN_DC_NAME', 'PJ002', '金蝶登录DC名');
INSERT INTO `express_set_system_install` (`name`, `value`, `chinesename`) VALUES ('KINGDEE_LOGIN_SLN_NAME', 'eas', '金蝶登录SLN名');
INSERT INTO `express_set_system_install` (`name`, `value`, `chinesename`) VALUES ('KINGDEE_PASSWORD', 'dmp123456', '金蝶系统登录密码');
INSERT INTO `express_set_system_install` (`name`, `value`, `chinesename`) VALUES ('KINGDEE_USERNAME', 'DMP', '金蝶系统登录名');
INSERT INTO `express_set_system_install` (`name`, `value`, `chinesename`) VALUES ('KINGDEE_RAMDOM', 'PJBEST01', '发送给金蝶消息加密的向量');
INSERT INTO `express_set_system_install` (`name`, `value`, `chinesename`) VALUES ('KINGDEE_SECRET', 'PJBEST01', '发送给金蝶消息加密的key');
INSERT INTO `express_set_system_install` (`name`, `value`, `chinesename`) VALUES ('KINGDEE_URL_FOR_EAS', 'http://172.30.14.11:6888/ormrpc/services/WSPJOtherBillFacade?wsdl', '金蝶提供的接受账单的地址');
INSERT INTO `express_set_system_install` (`name`, `value`, `chinesename`) VALUES ('EAS_KINGDEE_INT_ENABLE', 'false', '落地配跟金蝶接口开关');
INSERT INTO `express_set_system_install` (`name`, `value`, `chinesename`) VALUES ('EAS_PERSOPN_IN_KINGDEE', 'xue01.wan', '落地配在金蝶系统的职员编号');

