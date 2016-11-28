ALTER TABLE `express_set_user`
DROP INDEX `idx_username` ,
ADD INDEX `idx_username` (`username`) USING BTREE ;