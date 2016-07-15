CREATE TABLE `express_ops_auto_intowarehouse_message` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '��������',
	`serialNo` VARCHAR(20) NULL DEFAULT '' COMMENT '���к�',
	`cwb` VARCHAR(50) NULL DEFAULT '' COMMENT '������',
	`sendContent` VARCHAR(500) NULL DEFAULT '' COMMENT '���ͱ���',
	`sendTime` DATETIME NULL DEFAULT '0000-00-00 00:00:00' COMMENT '����ʱ��',
	`receiveContent` VARCHAR(500) NULL DEFAULT '' COMMENT '���ձ���',
	`receiveTime` DATETIME NULL DEFAULT '0000-00-00 00:00:00' COMMENT '����ʱ��',
	`handleStatus` VARCHAR(10) NULL DEFAULT '0' COMMENT '����״̬: 0-δ����, 1-�ѷ���, 2-����ɹ�, 3-����ʧ��',
	`created_by_user` VARCHAR(64) NULL DEFAULT '' COMMENT '������',
	`created_office` VARCHAR(32) NULL DEFAULT '' COMMENT '������office',
	`created_dtm_loc` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00' COMMENT '�����ߵ���ʱ��',
	`created_time_zone` VARCHAR(50) NULL DEFAULT '' COMMENT '�����ߵ���ʱ��',
	`updated_by_user` VARCHAR(64) NULL DEFAULT '' COMMENT '�޸���',
	`updated_office` VARCHAR(32) NULL DEFAULT '' COMMENT '�޸���office',
	`updated_dtm_loc` TIMESTAMP NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '�޸��ߵ���ʱ��',
	`updated_time_zone` VARCHAR(50) NULL DEFAULT '' COMMENT '�޸��ߵ���ʱ��',
	`record_version` BIGINT(20) NULL DEFAULT '0' COMMENT '��¼�汾��',
	`is_delete` TINYINT(4) NULL DEFAULT '0' COMMENT '�Ƿ���ɾ��',
	PRIMARY KEY (`id`),
	UNIQUE INDEX `serialNo_unique` (`serialNo`),
	INDEX `cwb_idx` (`cwb`)
)
COMMENT='����Զ�����Ϣ��¼��'
COLLATE='utf8_general_ci'
ENGINE=InnoDB;
