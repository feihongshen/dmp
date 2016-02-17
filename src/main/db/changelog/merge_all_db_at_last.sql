-- merge data

INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) 
SELECT '153040', '2', '客户-站点映射关系维护', '153040', 'addressCustomerStationMap/list/1?', '1530' 
FROM DUAL 
WHERE NOT EXISTS (SELECT 1 FROM `dmp40_function` WHERE `ID` = '153040') 
LIMIT 1;
		
update `express_set_county` set `name` = '新蒲新区' where `id` = '1905' and `name` = '新浦新区';

update `dmp40_function` set `ID` = '3043', `functionorder` = '3043' where `functionname` = 'OMS定时任务管理' and `ID` = '3042';


-- handle table

CREATE TABLE IF NOT EXISTS `b2c_platform_customer_info` (
  `customerid` int(11) NOT NULL AUTO_INCREMENT,
  `systemcustomerid` int(11) DEFAULT NULL,
  `customername` varchar(100) COLLATE utf8_bin NOT NULL,
  `customercode` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `warehousename` varchar(100) COLLATE utf8_bin DEFAULT '',
  `warehouseid` int(11) DEFAULT '0',
  `remark` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `ifeffectflag` int(11) DEFAULT '1',
  PRIMARY KEY (`customerid`),
  KEY `platform_customer_customerid_idx` (`customerid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE IF NOT EXISTS `express_set_contract_management` (
  `id` int(100) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `number` int(100) DEFAULT NULL COMMENT '编号',
  `contractstatus` int(4) DEFAULT NULL COMMENT '合同状态',
  `contractstartdate` varchar(100) DEFAULT NULL COMMENT '合同开始日期',
  `contractenddate` varchar(100) DEFAULT NULL COMMENT '合同结束日期',
  `customerid` int(11) DEFAULT NULL COMMENT '客户id',
  `partyaname` varchar(255) DEFAULT NULL COMMENT '甲方全称',
  `contracttype` int(4) DEFAULT NULL COMMENT '合同类型',
  `loanssettlementcycle` varchar(100) DEFAULT NULL COMMENT '代收贷款结算周期',
  `Loansandsettlementway` varchar(4) DEFAULT NULL COMMENT '贷款结算方式',
  `othercontractors` varchar(255) DEFAULT NULL COMMENT '其他合同商',
  `paifeisettlementcycle` varchar(100) DEFAULT NULL COMMENT '派费结算周期',
  `paifeisettlementtype` varchar(4) DEFAULT NULL COMMENT '派费结算方式',
  `whetherhavedeposit` int(4) DEFAULT NULL COMMENT '是否有押金',
  `marketingprincipal` varchar(255) DEFAULT NULL COMMENT '营销负责人',
  `invoicetype` int(4) DEFAULT NULL COMMENT '发票类型',
  `taxrate` varchar(100) DEFAULT NULL COMMENT '税率',
  `contractdescription` varchar(1000) DEFAULT NULL COMMENT '合同详细描述',
  `contractname` varchar(255) DEFAULT NULL COMMENT '合同名称',
  `contractaccessory` varchar(255) DEFAULT NULL COMMENT '合同附件',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `express_suning_transcwb` (
  `cwb` varchar(50) DEFAULT NULL,
  `transcwb` varchar(50) DEFAULT NULL,
  KEY `cwb_idx` (`cwb`),
  KEY `transcwb_idx` (`transcwb`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `ops_delivery_zhiliu` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `cwb` varchar(100) DEFAULT NULL,
  `emaildate` varchar(20) DEFAULT NULL,
  `resendtime` varchar(20) DEFAULT NULL,
  `branchid` bigint(11) DEFAULT '0',
  `deliveryid` bigint(11) DEFAULT '0',
  `cwbordertypeid` bigint(11) DEFAULT '0',
  `customerid` bigint(11) DEFAULT '0',
  `deliverystateid` bigint(11) DEFAULT '0',
  `deliverytime` varchar(20) DEFAULT NULL,
  `audittime` varchar(20) DEFAULT NULL,
  `gcaid` bigint(11) DEFAULT '0',
  `receivablefee` decimal(10,2) DEFAULT NULL,
  `paybackfee` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `zhiliu_cwb_idx` (`cwb`),
  KEY `zhiliu_branchid_idx` (`branchid`),
  KEY `zhiliu_deliveryid_idx` (`deliveryid`),
  KEY `zhiliu_deliverytime_idx` (`deliverytime`),
  KEY `zhiliu_audittime_idx` (`audittime`),
  KEY `zhiliu_customerid_idx` (`customerid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `pos_delivery_task` (
  `id` bigint(14) NOT NULL AUTO_INCREMENT,
  `cwb` varchar(100) DEFAULT NULL,
  `cretime` varchar(50) DEFAULT NULL,
  `deliveryid` int(4) DEFAULT NULL,
  `deliveryuser` varchar(50) DEFAULT NULL,
  `netcode` varchar(50) DEFAULT NULL,
  `netname` varchar(50) DEFAULT NULL,
  `weight` decimal(10,0) DEFAULT NULL,
  `goodscount` int(4) DEFAULT NULL,
  `cod` decimal(10,0) DEFAULT NULL,
  `address` varchar(300) DEFAULT NULL,
  `people` varchar(50) DEFAULT NULL,
  `peopletel` varchar(100) DEFAULT NULL,
  `remark` varchar(1000) DEFAULT NULL,
  `dssn` varchar(50) DEFAULT NULL,
  `dsname` varchar(50) DEFAULT NULL,
  `getTaskflag` varchar(50) DEFAULT '',
  `cancelflag` int(4) DEFAULT '0',
  KEY `id` (`id`),
  KEY `cwbidx` (`cwb`),
  KEY `cretimeidx` (`cretime`),
  KEY `getTaskflagidx` (`getTaskflag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `express_ops_flowexp` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `cwb` varchar(200) DEFAULT NULL,
  `cretime` varchar(30) DEFAULT NULL,
  KEY `id` (`id`),
  KEY `cwb_IDx` (`cwb`),
  KEY `cretime_IDx` (`cretime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `courier_info` (
  `courier_id` bigint(10) DEFAULT NULL,
  `company_name` varchar(40) DEFAULT NULL,
  `primary_dept` varchar(20) DEFAULT NULL,
  `secondary_dept` varchar(20) DEFAULT NULL,
  `distribute_center` varchar(20) DEFAULT NULL,
  `courier_name` varchar(10) DEFAULT NULL,
  `job` varchar(10) DEFAULT NULL,
  `is_contract` int(1) DEFAULT NULL,
  KEY `courier_idX` (`courier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `ops_delivery_tuihuochuzhan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cwb` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `tuihuochuzhantime` varchar(20) COLLATE utf8_bin DEFAULT '',
  `tuihuobranchid` int(11) DEFAULT NULL,
  `customerid` int(11) DEFAULT NULL,
  `cwbordertypeid` int(4) DEFAULT NULL,
  `tuihuozhanrukutime` varchar(20) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `tuihuochuzhan_tuihuochuzhantime_idx` (`tuihuochuzhantime`),
  KEY `tuihuochuzhan_tuihuobrnachid_idx` (`tuihuobranchid`),
  KEY `tuihuochuzhan_customerid_idx` (`customerid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


-- handle column

call proc_add_column_if_not_exists('fn_org_bill_adjustment_record', 'freight_amount', 'ALTER TABLE `fn_org_bill_adjustment_record` ADD COLUMN `freight_amount` decimal(19,2) DEFAULT ''0.00'' COMMENT ''原始运费金额'';');

call proc_add_column_if_not_exists('b2c_set_epaiapi', 'isPassiveReception', 'ALTER TABLE `b2c_set_epaiapi` ADD COLUMN `isPassiveReception` int(4) DEFAULT ''0'';');

call proc_add_column_if_not_exists('express_ops_delivery_state', 'pscount', 'ALTER TABLE `express_ops_delivery_state` ADD COLUMN `pscount` int(11) DEFAULT NULL;');

call proc_change_column_if_exists('express_ops_salarycount_detail', 'bacthstate', 'ALTER TABLE `express_ops_salarycount_detail` CHANGE COLUMN `bacthstate` `batchstate` int(10) DEFAULT NULL COMMENT ''批次状态'';');

ALTER TABLE `express_ops_cwb_detail` 
CHANGE COLUMN `cwbremark` `cwbremark` varchar(1000) COLLATE utf8_bin DEFAULT NULL, 
CHANGE COLUMN `consignoraddress` `consignoraddress` varchar(300) COLLATE utf8_bin DEFAULT NULL, 
CHANGE COLUMN `remark1` `remark1` varchar(1000) COLLATE utf8_bin DEFAULT NULL, 
CHANGE COLUMN `remark2` `remark2` varchar(1000) COLLATE utf8_bin DEFAULT NULL, 
CHANGE COLUMN `remark3` `remark3` varchar(1000) COLLATE utf8_bin DEFAULT NULL, 
CHANGE COLUMN `remark4` `remark4` varchar(1000) COLLATE utf8_bin DEFAULT NULL;

ALTER TABLE `express_ops_delivery_state` CHANGE COLUMN `sign_man_phone` `sign_man_phone` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '代签收人手机';

ALTER TABLE `express_ops_penalizeout_detail` CHANGE COLUMN `penalizeOutcontent` `penalizeOutcontent` varchar(500) DEFAULT '' COMMENT '赔付说明';

ALTER TABLE `express_ops_penalizeout_detail` CHANGE COLUMN `cancelcontent` `cancelcontent` varchar(500) DEFAULT '' COMMENT '撤销说明';

ALTER TABLE `express_set_penalizetype` CHANGE COLUMN `text` `text` varchar(500) DEFAULT '' COMMENT '赔付说明';

ALTER TABLE `express_set_smsconfigmodel` CHANGE COLUMN `branchids` `branchids` varchar(5000) DEFAULT '';

ALTER TABLE `express_set_smsconfigmodel` CHANGE COLUMN `customerids` `customerids` varchar(5000) DEFAULT '';

ALTER TABLE `fn_order_recharge` CHANGE COLUMN `record_bill_id` `record_bill_id` decimal(20,0) DEFAULT '0' COMMENT '调整单id';

update `jg_person` set `AGE` = 0 where `AGE` = null;
ALTER TABLE `jg_person` CHANGE COLUMN `AGE` `AGE` int(11) NOT NULL;

update `jg_person` set `CREATEDT` = '0000-00-00 00:00:00' where CREATEDT = null;
ALTER TABLE `jg_person` CHANGE COLUMN `CREATEDT` `CREATEDT` datetime NOT NULL;

update `jg_person` set `NAME` = '' where `NAME` = null;
ALTER TABLE `jg_person` CHANGE COLUMN `NAME` `NAME` varchar(255) NOT NULL;

update `jg_person` set `SALARY` = 0 where `SALARY` = null;
ALTER TABLE `jg_person` CHANGE COLUMN `SALARY` `SALARY` decimal(19,2) NOT NULL;

ALTER TABLE `jeecg_demo` CHANGE COLUMN `SEX` `SEX` varchar(255) DEFAULT NULL;

ALTER TABLE `express_ops_penalizeoutimporterrorrecord` CHANGE COLUMN `error` `error` varchar(500) DEFAULT '' COMMENT '导入失败原因';

ALTER TABLE `sms_send_manage` CHANGE COLUMN `ip` `ip` varchar(200) COLLATE utf8_bin DEFAULT NULL;

ALTER TABLE `fn_bill_policy` CHANGE COLUMN `time_to` `time_to` time DEFAULT NULL COMMENT '到T2时间(T1<=T2)';

ALTER TABLE `express_ops_cwb_detail_b2ctemp` 
CHANGE COLUMN `cwbremark` `cwbremark` varchar(1000) COLLATE utf8_bin DEFAULT NULL, 
CHANGE COLUMN `consignoraddress` `consignoraddress` varchar(300) COLLATE utf8_bin DEFAULT NULL, 
CHANGE COLUMN `remark1` `remark1` varchar(1000) COLLATE utf8_bin DEFAULT NULL, 
CHANGE COLUMN `remark2` `remark2` varchar(1000) COLLATE utf8_bin DEFAULT NULL, 
CHANGE COLUMN `remark3` `remark3` varchar(1000) COLLATE utf8_bin DEFAULT NULL, 
CHANGE COLUMN `remark4` `remark4` varchar(1000) COLLATE utf8_bin DEFAULT NULL;


-- handle index

-- 空表，可直接drop index
call proc_change_index_if_exists('express_ops_salarycount_detail', 'bacthstate_idx', '', 'ALTER TABLE `express_ops_salarycount_detail` DROP INDEX `bacthstate_idx`, ADD INDEX `batchstate_idx` (`batchstate`);');

call proc_change_index_if_exists('express_ops_delivery_state', 'Delivery_State_Payupid_Idx', 'returnedfee', 'ALTER TABLE `express_ops_delivery_state` DROP INDEX `Delivery_State_Payupid_Idx`, ADD INDEX `Delivery_State_Payupid_Idx` (`payupid`);');

call proc_create_index_if_not_exists('fn_order_recharge', 'bill_id', '', 'CREATE INDEX bill_id on fn_order_recharge(bill_id);');

call proc_create_index_if_not_exists('shangmentuicwb_detail', 'CWB_Idx', '', 'CREATE INDEX CWB_Idx on shangmentuicwb_detail(cwb);');

call proc_create_index_if_not_exists('express_ops_cwb_detail', 'detail_fncustomerbillid_idx', '', 'CREATE INDEX detail_fncustomerbillid_idx on express_ops_cwb_detail(fncustomerbillid);');

call proc_create_index_if_not_exists('express_ops_cwb_detail', 'detail_fncustomerpayablebillid_idx', '', 'CREATE INDEX detail_fncustomerpayablebillid_idx on express_ops_cwb_detail(fncustomerpayablebillid);');

call proc_create_index_if_not_exists('express_ops_cwb_detail', 'detail_fnorgbillid_idx', '', 'CREATE INDEX detail_fnorgbillid_idx on express_ops_cwb_detail(fnorgbillid);');

call proc_create_index_if_not_exists('fn_order_recharge', 'record_bill_id', '', 'CREATE INDEX record_bill_id on fn_order_recharge(record_bill_id);');

call proc_create_index_if_not_exists('fn_customer_bill', 'status', '', 'CREATE INDEX status on fn_customer_bill(status);');

call proc_create_index_if_not_exists('express_ops_cwb_detail_b2ctemp', 'detail_customerid_idx', '', 'CREATE INDEX detail_customerid_idx on express_ops_cwb_detail_b2ctemp(customerid);');

call proc_create_index_if_not_exists('express_ops_cwb_detail_b2ctemp', 'detail_create_idx', '', 'CREATE INDEX detail_create_idx on express_ops_cwb_detail_b2ctemp(credate);');

call proc_create_index_if_not_exists('express_ops_cwb_detail_b2ctemp', 'is_b2csuccess_idx', '', 'CREATE INDEX is_b2csuccess_idx on express_ops_cwb_detail_b2ctemp(isB2cSuccessFlag);');

call proc_create_index_if_not_exists('express_ops_order_flow', 'FlowBranchidIdx', '', 'CREATE INDEX FlowBranchidIdx on express_ops_order_flow(branchid);');

call proc_create_index_if_not_exists('express_mid_opretion_user', 'cwb_Idx', '', 'CREATE INDEX cwb_Idx on express_mid_opretion_user(cwb);');

call proc_create_index_if_not_exists('express_mid_opretion_user', 'userid_Idx', '', 'CREATE INDEX userid_Idx on express_mid_opretion_user(userid);');

-- 将index名统一为Groupdetail_nextbranchid_Idx
call proc_create_index_if_not_exists('express_ops_groupdetail', 'Groupdetail_nextbranchid_Idx', '', 'CREATE INDEX Groupdetail_nextbranchid_Idx on express_ops_groupdetail(nextbranchid);');
call proc_change_index_if_exists('express_ops_groupdetail', 'Group_nextbranchid_idx', 'nextbranchid', 'ALTER TABLE express_ops_groupdetail DROP INDEX Group_nextbranchid_idx;');

-- 将index名统一为Groupdetail_createtime_Idx
call proc_create_index_if_not_exists('express_ops_groupdetail', 'Groupdetail_createtime_Idx', '', 'CREATE INDEX Groupdetail_createtime_Idx on express_ops_groupdetail(createtime);');
call proc_change_index_if_exists('express_ops_groupdetail', 'Group_createtime_Idx', 'createtime', 'ALTER TABLE express_ops_groupdetail DROP INDEX Group_createtime_Idx;');

-- 空表，可直接add foreign key
call proc_add_foreignkey_if_not_exists('jeecg_matter_bom', 'parent_ID', 'jeecg_matter_bom', 'ID', 'ALTER TABLE `jeecg_matter_bom` ADD CONSTRAINT `FK_fldfyrevj0li4hej5b2gu2q7w` FOREIGN KEY (`parent_ID`) REFERENCES `jeecg_matter_bom` (`ID`);');

-- 空表，可直接add foreign key
call proc_add_foreignkey_if_not_exists('jeecg_demo_course', 'teacher_ID', 'jeecg_demo_teacher', 'ID', 'ALTER TABLE `jeecg_demo_course` ADD CONSTRAINT `FK_g3jn8mfod69i7jfv5gnrcvgbx` FOREIGN KEY (`teacher_ID`) REFERENCES `jeecg_demo_teacher` (`ID`);');

-- 空表，可直接add foreign key
call proc_add_foreignkey_if_not_exists('jeecg_demo_student', 'COURSE_ID', 'jeecg_demo_course', 'ID', 'ALTER TABLE `jeecg_demo_student` ADD CONSTRAINT `FK_r86q81koyocgod3cx6529hbpw` FOREIGN KEY (`COURSE_ID`) REFERENCES `jeecg_demo_course` (`ID`);');

-- 空表，可直接add foreign key
call proc_add_foreignkey_if_not_exists('express_set_deposit_information', 'contractid', 'express_set_customer_contract_management', 'id', 'ALTER TABLE `express_set_deposit_information` ADD CONSTRAINT `contractid` FOREIGN KEY (`contractid`) REFERENCES `express_set_customer_contract_management` (`id`);');

