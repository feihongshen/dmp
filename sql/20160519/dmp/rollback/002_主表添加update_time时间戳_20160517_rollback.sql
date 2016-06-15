## 回滚SQL-删除新增加的字段(回滚时执行)
ALTER TABLE `express_ops_cwb_detail` DROP COLUMN `update_time`;
ALTER TABLE `express_ops_cwb_detail` MODIFY COLUMN `credate`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP;