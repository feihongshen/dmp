-- 新建user_login_log表
CREATE TABLE `express_ops_user_login_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT '',
  `lastLoginState` tinyint(4) DEFAULT '0' COMMENT '上次登录状态（1-成功，0-失败）',
  `loginFailCount` int(1) DEFAULT '0' COMMENT '累计连续登录错误次数',
  `lastLoginTryTime` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '0000-00-00 00:00:00' COMMENT '上次尝试登录时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_login_log_username_unique` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 插入系统参数
INSERT INTO `express_set_system_install` (`name`, `value`, `chinesename`) VALUES ('loginFailMaxTryTimeLimit', '5', '登录失败尝试最大次数');
INSERT INTO `express_set_system_install` (`name`, `value`, `chinesename`) VALUES ('loginForbiddenIntervalInMinutes', '60', '登录失败禁止时间长度（分钟）');
