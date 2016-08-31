-- 新建user_login_log表
DROP TABLE `express_ops_user_login_log`;

-- 插入系统参数
DELETE FROM `express_set_system_install` WHERE `name` = 'loginFailMaxTryTimeLimit';
DELETE FROM `express_set_system_install` WHERE `name` = 'loginForbiddenIntervalInMinutes';