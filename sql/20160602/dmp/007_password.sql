-- 1.增加webPassword字段
ALTER TABLE `express_set_user` ADD COLUMN `webPassword` VARCHAR(50) NULL DEFAULT NULL COMMENT '网页登录密码' COLLATE 'utf8_bin';

-- 2.将原密码更新到webPassword
UPDATE express_set_user SET webPassword = password;
