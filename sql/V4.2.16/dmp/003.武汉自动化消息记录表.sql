-- 入库自动化消息记录表
CREATE TABLE `express_ops_auto_intowarehouse_message` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
	`serialNo` VARCHAR(20) NULL DEFAULT '' COMMENT '序列号',
	`intowarehouseType` TINYINT(4) NULL DEFAULT '0' COMMENT '报文来源: 1-分拣库入库, 2-中转库入库',
	`scancwb` VARCHAR(50) NULL DEFAULT '' COMMENT '扫描单号',
	`cwb` VARCHAR(50) NULL DEFAULT '' COMMENT '订单号',
	`entranceIP` VARCHAR(50) NULL DEFAULT '' COMMENT '自动分拨机入口IP',
	`sendContent` VARCHAR(500) NULL DEFAULT '' COMMENT '发送报文',
	`sendTime` DATETIME NULL DEFAULT '0000-00-00 00:00:00' COMMENT '发送时间',
	`receiveContent` VARCHAR(500) NULL DEFAULT '' COMMENT '接收报文',
	`receiveTime` DATETIME NULL DEFAULT '0000-00-00 00:00:00' COMMENT '接收时间',
	`handleStatus` TINYINT(4) NULL DEFAULT '0' COMMENT '处理状态: 0-未发送, 1-已发送, 2-处理成功, 3-处理失败',
	`created_by_user` VARCHAR(64) NULL DEFAULT '' COMMENT '创建者',
	`created_office` VARCHAR(32) NULL DEFAULT '' COMMENT '创建者office',
	`created_dtm_loc` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建者当地时间',
	`created_time_zone` VARCHAR(50) NULL DEFAULT '' COMMENT '创建者当地时区',
	`updated_by_user` VARCHAR(64) NULL DEFAULT '' COMMENT '修改者',
	`updated_office` VARCHAR(32) NULL DEFAULT '' COMMENT '修改者office',
	`updated_dtm_loc` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '修改者当地时间',
	`updated_time_zone` VARCHAR(50) NULL DEFAULT '' COMMENT '修改者当地时区',
	`record_version` BIGINT(20) NULL DEFAULT '0' COMMENT '记录版本号',
	`is_delete` TINYINT(4) NULL DEFAULT '0' COMMENT '是否已删除',
	PRIMARY KEY (`id`),
	UNIQUE INDEX `serialNo_unique` (`serialNo`),
	INDEX `cwb_idx` (`cwb`),
	INDEX `scancwb_idx` (`scancwb`)
)
COMMENT='入库自动化消息记录表'
COLLATE='utf8_general_ci'
ENGINE=InnoDB;

-- 武汉自动化连接中间件等候返回状态时间间隔（毫秒）
INSERT INTO `express_set_system_install` (`name`, `value`, `chinesename`) VALUES ('autoAllocatingConnecteWaitIntervalInMs', '200', '武汉自动化连接中间件等候返回状态时间间隔（毫秒）');
