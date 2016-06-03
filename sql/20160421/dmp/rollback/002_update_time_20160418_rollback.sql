-- 回滚SQL-删除新增加的字段(回滚时执行)
ALTER TABLE `express_set_branch` DROP COLUMN `update_time`;
ALTER TABLE `express_set_customer_info` DROP COLUMN `update_time`;
ALTER TABLE `express_set_user` DROP COLUMN `update_time`;
ALTER TABLE `express_ops_order_intowarhouse` DROP COLUMN `update_time`;
ALTER TABLE `express_set_reason` DROP COLUMN `update_time`;
ALTER TABLE `express_ops_orderback_record` DROP COLUMN `update_time`;
ALTER TABLE `fn_customer_bill` DROP COLUMN `update_time`;
ALTER TABLE `express_set_role_new` DROP COLUMN `update_time`;
ALTER TABLE `ops_ypdjhandlerecord` DROP COLUMN `update_time`;
