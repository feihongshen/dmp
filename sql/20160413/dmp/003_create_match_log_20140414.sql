
-- 新增地址匹配日志表
CREATE TABLE `express_ops_address_match_log` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `itemno` varchar(100) NOT NULL DEFAULT '' COMMENT '序号',
  `address` varchar(500) DEFAULT '' COMMENT '输入地址',
  `match_status` varchar(2) NOT NULL DEFAULT '' COMMENT '匹配状态，0：成功，1：未维护关键字，2：关键字无对应站点，98：multipleResult，99：exceptionResult',
  `match_msg` varchar(500) DEFAULT '' COMMENT '失败原因',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `create_time_idx` (`create_time`) USING BTREE,
  KEY `itemno` (`itemno`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='地址匹配日志表';