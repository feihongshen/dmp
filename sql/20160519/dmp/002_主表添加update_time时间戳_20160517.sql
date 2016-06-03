## 先将默认值改为0000-00-00 00:00:00
ALTER TABLE `express_ops_cwb_detail` MODIFY COLUMN `credate` timestamp NOT NULL default '0000-00-00 00:00:00';
## 增加时间戳
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间-MySQL维护';