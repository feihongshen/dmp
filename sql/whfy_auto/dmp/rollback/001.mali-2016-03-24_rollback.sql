ALTER TABLE `express_set_branch` DROP COLUMN `outputno`;

DROP TABLE `express_set_entrance`;

DELETE FROM `express_set_system_install` WHERE `name` = 'AutoAllocating' AND `chinesename` = '自动分拨开关（0：关闭，1：开启）';