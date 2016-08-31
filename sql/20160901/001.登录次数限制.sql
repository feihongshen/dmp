-- user表增加字段
ALTER TABLE `express_set_user`
	ADD COLUMN `lastLoginState` TINYINT(4) DEFAULT '0' COMMENT '上次登录状态（1-成功，0-失败）',
	ADD COLUMN `loginFailCount` INT(1) DEFAULT '0' COMMENT '累计连续登录错误次数',
	ADD COLUMN `lastLoginTryTime` VARCHAR(50) DEFAULT '0000-00-00 00:00:00' COMMENT '上次尝试登录时间';

-- 插入系统参数
INSERT INTO `express_set_system_install` (`name`, `value`, `chinesename`) VALUES ('loginFailMaxTryTimeLimit', '5', '登录失败尝试最大次数');
INSERT INTO `express_set_system_install` (`name`, `value`, `chinesename`) VALUES ('loginForbiddenIntervalInMinutes', '60', '登录失败禁止时间长度（分钟）');
