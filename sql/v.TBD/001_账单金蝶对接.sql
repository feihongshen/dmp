CREATE TABLE `fn_msg_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `rqst_msg` mediumtext,
  `resp_msg` mediumtext,
  `comm_type` tinyint(4) NOT NULL DEFAULT '0',
  `intf` tinyint(4) NOT NULL DEFAULT '0',
  `status` tinyint(4) NOT NULL DEFAULT '0',
  `suc_or_fail` tinyint(4) NOT NULL DEFAULT '0',
  `send_or_recv` tinyint(4) NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fn_msg_record_interface_idx` (`intf`),
  KEY `fn_msg_record_status_idx` (`status`),
  KEY `fn_msg_record_send_or_recv_idx` (`send_or_recv`),
  KEY `fn_msg_record_create_time_idx` (`create_time`) USING BTREE,
  KEY `fn_msg_record_suc_or_fail_idx` (`suc_or_fail`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
