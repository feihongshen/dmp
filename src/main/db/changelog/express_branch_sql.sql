

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for express_ops_preorder
-- ----------------------------

/*----------------------------------------------------------------建库脚本---------------------------------------------------------------*/
/*预订单*/
DROP TABLE IF EXISTS `express_ops_preorder`;
CREATE TABLE `express_ops_preorder` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `pre_order_no` varchar(50) NOT NULL COMMENT '预订单编号',
  `status` tinyint(1) DEFAULT 0 COMMENT '预订单状态（0：正常，1：关闭，2：退回）',
  `excute_state` tinyint(1) DEFAULT NULL COMMENT '执行状态(0未匹配站点、1已匹配站点、2已分配小件员、3延迟揽件、4揽件失败、5站点超区、6揽件超区、7揽件成功)',
  `send_person` varchar(100) DEFAULT NULL COMMENT '寄件人',
  `cellphone` varchar(50) DEFAULT NULL COMMENT '手机号码',
  `telephone` varchar(50) DEFAULT NULL COMMENT '固定电话',
  `collect_address` varchar(200) DEFAULT NULL COMMENT '取件地址',
  `reason` varchar(200) DEFAULT NULL COMMENT '原因',
  `return_tps_false_flag` tinyint(1) DEFAULT 0 COMMENT '返回tps失败标识，0：成功，1：失败',
  `branch_id` int(11) DEFAULT 0 COMMENT '分配站点id',
  `branch_name` varchar(50) DEFAULT NULL COMMENT '分配站点名称',
  `handle_time` datetime DEFAULT NULL COMMENT '省公司处理预订单时间',
  `handle_user_id` int(11) DEFAULT NULL COMMENT '处理人id',
  `handle_user_name` varchar(50) DEFAULT NULL COMMENT '处理人姓名',
  `distribute_deliverman_time` datetime DEFAULT NULL COMMENT '分配小件员的时间',
  `create_time` datetime DEFAULT NULL COMMENT '生成时间',
  `arrange_time` datetime DEFAULT NULL COMMENT '预约时间',
  `deliverman_id` int(11) DEFAULT 0 COMMENT '小件员id',
  `deliverman_name` varchar(50) DEFAULT NULL COMMENT '小件员姓名',
  `distribute_user_id` int(11) DEFAULT NULL COMMENT '分配小件员的操作人id',
  `distribute_user_name` varchar(50) DEFAULT NULL COMMENT '分配小件员的操作人姓名',
  `order_no` varchar(100) DEFAULT NULL COMMENT '快递单号（也是订单表中的订单号）',
  `feedback_first_reason_id` int(11) DEFAULT NULL COMMENT '一级原因id',
  `feedback_first_reason` varchar(500) DEFAULT NULL COMMENT '一级原因',
  `feedback_second_reason_id` int(11) DEFAULT NULL COMMENT '二级原因id',
  `feedback_second_reason` varchar(500) DEFAULT NULL COMMENT '二级原因',
  `feedback_remark` varchar(500) DEFAULT NULL COMMENT '反馈备注',
  `feedback_user_id` int(11) DEFAULT NULL COMMENT '反馈人id',
  `feedback_user_name` varchar(50) DEFAULT NULL COMMENT '反馈人姓名',
  `feedback_time` datetime DEFAULT NULL COMMENT '反馈时间',
  `next_pick_time` DATETIME  DEFAULT NULL COMMENT '预计下次揽件时间',
  PRIMARY KEY (`id`),
  KEY `preorder_order_no_idx` (`pre_order_no`) USING BTREE,
  KEY `preorder_create_time_idx` (`create_time`) USING BTREE,
  KEY `preorder_branch_idx` (`branch_id`) USING BTREE,
  KEY `preorder_cellphone_idx` (`cellphone`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='预订单';

/*预订单临时表*/
DROP TABLE IF EXISTS `express_ops_preorder_temp`;
CREATE TABLE `express_ops_preorder_temp` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `reverse_order_no` varchar(50) DEFAULT '' COMMENT '预订单号',
  `order_type` int(4) DEFAULT '0' COMMENT '订单类型',
  `cust_code` varchar(50) DEFAULT '' COMMENT '客户编码',
  `cnor_prov` varchar(10) DEFAULT '' COMMENT '发货省',
  `cnor_city` varchar(10) DEFAULT '' COMMENT '发货市',
  `cnor_region` varchar(20) DEFAULT '' COMMENT '发货区',
  `cnor_town` varchar(50) DEFAULT '' COMMENT '发货街道',
  `cnor_addr` varchar(100) DEFAULT '' COMMENT '发货地址',
  `cnor_name` varchar(20) DEFAULT '' COMMENT '发货联系人',
  `cnor_mobile` varchar(20) DEFAULT '' COMMENT '发货联系手机',
  `cnor_tel` varchar(20) DEFAULT '' COMMENT '发货电话',
  `cnor_remark` varchar(200) DEFAULT '' COMMENT '发货备注',
  `carrier_code` varchar(20) DEFAULT '' COMMENT '承运商编码',
  `reserve_order_status` int(4) DEFAULT '0' COMMENT '预订单状态',
  `state` int(2) DEFAULT '0' COMMENT '是否有效',
  `is_exist` int(2) DEFAULT '0' COMMENT '是否存在',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7524 DEFAULT CHARSET=utf8;

/*运单临时表*/
DROP TABLE IF EXISTS `express_ops_cwb_exprss_detail_temp`;
CREATE TABLE `express_ops_cwb_exprss_detail_temp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `transport_no` varchar(32) DEFAULT '' COMMENT '运单号,快递单运单号',
  `cust_order_no` varchar(32) DEFAULT '' COMMENT '客户订单号',
  `cust_code` varchar(32) DEFAULT '' COMMENT '发件客户编码',
  `accept_dept` varchar(32) DEFAULT '' COMMENT '揽件站点',
  `accept_operator` varchar(64) DEFAULT '' COMMENT '揽件员',
  `cnor_prov` varchar(64) DEFAULT '' COMMENT '发货省份名称',
  `cnor_city` varchar(64) DEFAULT '' COMMENT '发货城市名称',
  `cnor_region` varchar(64) DEFAULT '' COMMENT '发货区/县名称',
  `cnor_town` varchar(64) DEFAULT '' COMMENT '发货乡镇名称',
  `cnor_addr` varchar(128) DEFAULT '' COMMENT '发货详细地址',
  `cnor_name` varchar(64) DEFAULT '' COMMENT '发货联系人',
  `cnor_mobile` varchar(64) DEFAULT '' COMMENT '发货人联系手机',
  `cnor_tel` varchar(64) DEFAULT '' COMMENT '发货人电话',
  `cnor_remark` varchar(256) DEFAULT '' COMMENT '发货备注',
  `cnee_prov` varchar(64) DEFAULT '' COMMENT '收货省份名称',
  `cnee_city` varchar(64) DEFAULT '' COMMENT '收货城市名称',
  `cnee_region` varchar(64) DEFAULT '' COMMENT '收货区域名称',
  `cnee_town` varchar(64) DEFAULT '' COMMENT '收货街道名称',
  `cnee_addr` varchar(256) DEFAULT '' COMMENT '收货地址',
  `cnee_name` varchar(64) DEFAULT '' COMMENT '收货联系人',
  `cnee_mobile` varchar(64) DEFAULT '' COMMENT '收货联系人手机',
  `cnee_tel` varchar(64) DEFAULT '' COMMENT '收货联系人电话',
  `cnee_period` tinyint(2) DEFAULT NULL COMMENT '送货时间类型',
  `cnee_remark` varchar(256) DEFAULT '' COMMENT '收货人备注',
  `cnee_certificate` varchar(64) DEFAULT '' COMMENT '寄件人证件号',
  `cnee_no` varchar(32) DEFAULT '' COMMENT '收货公司编码',
  `is_cod` tinyint(2) DEFAULT '0' COMMENT '是否货到付款',
  `cod_amount` decimal(18,2) DEFAULT '0.00' COMMENT '代收货款',
  `carriage` decimal(18,2) DEFAULT '0.00' COMMENT '运费合计',
  `total_num` int(4) DEFAULT '0' COMMENT '合计件数',
  `total_weight` decimal(18,2) DEFAULT '0.00' COMMENT '实际重量',
  `calculate_weight` decimal(18,2) DEFAULT '0.00' COMMENT '计费重量',
  `total_volume` decimal(18,3) DEFAULT '0.000' COMMENT '合计体积',
  `total_box` int(4) DEFAULT '0' COMMENT '合计箱数',
  `assurance_value` decimal(18,2) DEFAULT '0.00' COMMENT '保价金额',
  `assurance_fee` decimal(18,2) DEFAULT '0.00' COMMENT '保费',
  `pay_type` tinyint(2) DEFAULT '0' COMMENT '支付方式',
  `payment` tinyint(2) DEFAULT '-1' COMMENT '默认-1',
  `details` text COMMENT '运单明细对象',
  `cargo_name` varchar(256) DEFAULT '' COMMENT '货名',
  `count` int(4) DEFAULT '0' COMMENT '件数',
  `cargo_length` decimal(18,2) DEFAULT '0.00' COMMENT '长',
  `cargo_width` decimal(18,2) DEFAULT '0.00' COMMENT '宽',
  `cargo_height` decimal(18,2) DEFAULT '0.00' COMMENT '高',
  `weight` decimal(18,2) DEFAULT '0.00' COMMENT '重量',
  `volume` decimal(18,3) DEFAULT '0.000' COMMENT '体积',
  `cust_pack_no` varchar(32) DEFAULT '' COMMENT '客户箱号',
  `size_sn` varchar(32) DEFAULT '' COMMENT '商品条码',
  `price` decimal(18,2) DEFAULT '0.00' COMMENT '单价',
  `unit` varchar(32) DEFAULT '' COMMENT '单位',
  `tps_trans_id` varchar(50) DEFAULT '' COMMENT 'tps运单id',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `is_hand_over` tinyint(2) DEFAULT '0' COMMENT '是否处理完毕',
  KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

/*接口调用异常处理表*/
DROP TABLE IF EXISTS `express_ops_tps_interface_excep`;
CREATE TABLE `express_ops_tps_interface_excep` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pre_order_no` varchar(50) DEFAULT '' COMMENT '预订单号',
  `trans_no` varchar(50) DEFAULT '' COMMENT '运单号',
  `package_no` varchar(50) DEFAULT '' COMMENT '包号',
  `operation_type` int(4) DEFAULT '0' COMMENT '所处操作环节',
  `method_params` text COMMENT '方法的参数(json字符串)',
  `execute_count` int(4) DEFAULT '0' COMMENT '执行次数',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `ope_flag` int(2) DEFAULT '0' COMMENT '是否再次调用成功(0:否;1:是)',
  `err_msg` varchar(2000) DEFAULT '' COMMENT '异常原因',
  `flow_order_type` int(4) DEFAULT '0' COMMENT '订单所处的操作环节',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  KEY `id` (`id`),
  KEY `idx_pre_order_no` (`pre_order_no`),
  KEY `idx_trans_no` (`trans_no`),
  KEY `idx_package_no` (`package_no`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

/*揽件出站信息表（为避免一级站出站覆盖二级站出站信息所建的表）add by 王志宇*/
CREATE TABLE `express_ops_outstationinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `cwbid` int(11) NOT NULL DEFAULT '0' COMMENT '运单id',
  `cwb` varchar(100) NOT NULL COMMENT '运单号',
  `outstation_branchid` int(11) DEFAULT '0' COMMENT '出站站点id',
  `outstation_branchname` varchar(100) DEFAULT NULL COMMENT '出站站点名称',
  `handerid` int(11) DEFAULT '0' COMMENT '操作人id',
  `handername` varchar(100) DEFAULT NULL COMMENT '操作人姓名',
  `outstationtime` datetime DEFAULT NULL COMMENT '出站时间',
  `collectorid` INT (11) DEFAULT 0 NULL COMMENT '小件员id',
  PRIMARY KEY (`id`),
  KEY `outstationinfo_outsation_branchid_index` (`outstation_branchid`) USING BTREE,
  KEY `outstationinfo_outstationtime_index` (`outstationtime`) USING BTREE,
  KEY `outstationinfo_handerid_index` (`handerid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*电子称称重表 add by songkaojun 2015-10-23*/
DROP TABLE IF EXISTS `express_ops_weigh`;
CREATE TABLE `express_ops_weigh` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `cwb` varchar(100) DEFAULT NULL COMMENT '运单号',
  `weight` double(11,2) DEFAULT NULL COMMENT '重量',
  `branchid` int(11) DEFAULT '0' COMMENT '机构ID',
  `branchname` varchar(50) DEFAULT NULL COMMENT '机构名称',
  `handlerid` int(11) DEFAULT '0' COMMENT '操作人ID',
  `handlername` varchar(50) DEFAULT NULL COMMENT '操作人名称',
  `handletime` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*-----------------------------------------------------------------------修改已有的表sql-----------------------------------------------------------------*/
/*订单修改*/
ALTER TABLE `express_ops_cwb_detail` 
ADD COLUMN `sendername` VARCHAR (50) DEFAULT NULL NULL COMMENT '寄件人姓名',
ADD COLUMN `senderid` VARCHAR (50) DEFAULT NULL NULL COMMENT '寄件人证件号',
ADD COLUMN `senderprovinceid` INT (11) DEFAULT 0 NULL COMMENT '寄件人省id',
ADD COLUMN `senderprovince` VARCHAR (100) DEFAULT NULL NULL COMMENT '寄件人省',
ADD COLUMN `sendercityid` INT (11) DEFAULT 0 NULL COMMENT '寄件人市id',
ADD COLUMN `sendercity` VARCHAR (100) DEFAULT NULL NULL COMMENT '寄件人市',
ADD COLUMN `sendercountyid` INT (11) DEFAULT 0 NULL COMMENT '寄件人区id',
ADD COLUMN `sendercounty` VARCHAR (100) DEFAULT NULL NULL COMMENT '寄件人区',
ADD COLUMN `senderstreetid` INT (11) DEFAULT 0 NULL COMMENT '寄件人街道id',
ADD COLUMN `senderstreet` VARCHAR (100) DEFAULT NULL NULL COMMENT '寄件人街道',
ADD COLUMN `sendercellphone` VARCHAR (20) DEFAULT NULL NULL COMMENT '寄件人手机',
ADD COLUMN `sendertelephone` VARCHAR (20) DEFAULT NULL NULL COMMENT '寄件人固话',
ADD COLUMN `senderaddress` VARCHAR (100) DEFAULT NULL NULL COMMENT '寄件人地址',
ADD COLUMN `reccustomerid` int (11) DEFAULT 0 NULL COMMENT '收件人客户id',
ADD COLUMN `recid` VARCHAR (50) DEFAULT NULL NULL COMMENT '收件人证件号',
ADD COLUMN `recprovinceid` INT (11) DEFAULT 0 NULL COMMENT '收件人省id',
ADD COLUMN `reccityid` INT (11) DEFAULT 0 NULL COMMENT '收件人市id',
ADD COLUMN `reccountyid` INT (11) DEFAULT 0 NULL COMMENT '收件人区id',
ADD COLUMN `recstreetid` INT (11) DEFAULT 0 NULL COMMENT '收件人街道id',
ADD COLUMN `recstreet` VARCHAR (100) DEFAULT NULL NULL COMMENT '收件人街道',
ADD COLUMN `isadditionflag` TINYINT (1) DEFAULT 0 NULL COMMENT '是否补录（0：否，1：是）',
ADD COLUMN `entrustname` VARCHAR (100) DEFAULT NULL NULL COMMENT '委托内容/名称',
ADD COLUMN `sendnum` INT (11) DEFAULT NULL NULL COMMENT '数量',
ADD COLUMN `length` INT (11) DEFAULT 0 NULL COMMENT '长度（cm）',
ADD COLUMN `width` INT (11) DEFAULT 0 NULL COMMENT '宽度（cm）',
ADD COLUMN `height` INT (11) DEFAULT 0 NULL COMMENT '高度（cm）',
ADD COLUMN `kgs` DECIMAL (19,2) DEFAULT 0.00 NULL COMMENT 'kgs',
ADD COLUMN `other` VARCHAR(100) DEFAULT NULL NULL COMMENT '其他',
ADD COLUMN `hascod` TINYINT (1) DEFAULT 0 NULL COMMENT '是否有代收货款（0：否，1：是）',
ADD COLUMN `packagefee` DECIMAL (19,2) DEFAULT 0.00 NULL COMMENT '包装费用（元）',
ADD COLUMN `hasinsurance` TINYINT (1) DEFAULT 0 NULL COMMENT '是否有保价（0：否，1：是）',
ADD COLUMN `announcedvalue` DECIMAL (19,2) DEFAULT 0.00 NULL COMMENT '保价声明价值（元）',
ADD COLUMN `insuredfee` DECIMAL (19,2) DEFAULT 0.00 NULL COMMENT '保价费用（元）',
ADD COLUMN `chargeweight` DECIMAL (19,2) DEFAULT 0.00 NULL COMMENT '计费重量（kg）',
ADD COLUMN `realweight` DECIMAL (19,2) DEFAULT 0.00 NULL COMMENT '实际重量（kg）',
ADD COLUMN `totalfee` DECIMAL (19,2) DEFAULT 0.00 NULL COMMENT '费用合计（运费+包装+保价）',
ADD COLUMN `sendareacode` VARCHAR (20) DEFAULT NULL NULL COMMENT '始发地代码',
ADD COLUMN `recareacode` VARCHAR (20) DEFAULT NULL NULL COMMENT '目的地代码',
ADD COLUMN `paymethod` INT(1) DEFAULT 1 NULL COMMENT '付款方式（0：月结，1：现付，2：到付）',
ADD COLUMN `monthsettleno` VARCHAR (50) DEFAULT NULL NULL COMMENT '月结账号',
ADD COLUMN `collectorid` INT (11) DEFAULT 0 NULL COMMENT '揽件员id',
ADD COLUMN `collectorname` VARCHAR (50) DEFAULT NULL NULL COMMENT '揽件员姓名',
ADD COLUMN `instationhandlerid` INT (11) DEFAULT 0 NULL COMMENT '入站操作人id',
ADD COLUMN `instationhandlername` VARCHAR (20) DEFAULT NULL NULL COMMENT '入站操作人姓名',
ADD COLUMN `instationdatetime` datetime DEFAULT NULL NULL COMMENT '入站时间',
ADD COLUMN `instationid` int DEFAULT NULL NULL COMMENT '揽件入站站点id',
ADD COLUMN `instationname` VARCHAR(100) DEFAULT NULL NULL COMMENT '揽件入站站点',
ADD COLUMN `outstationhandlerid` INT (11) DEFAULT 0 NULL COMMENT '出站操作人id',
ADD COLUMN `outstationhandlername` VARCHAR (50) DEFAULT NULL NULL COMMENT '出站操作人姓名',
ADD COLUMN `outstationdatetime` datetime DEFAULT NULL NULL COMMENT '出站时间',
ADD COLUMN `inputhandlerid` INT (11) DEFAULT 0 NULL COMMENT '录入操作人id',
ADD COLUMN `inputhandlername` VARCHAR (50) DEFAULT NULL NULL COMMENT '录入操作人姓名',
ADD COLUMN `inputdatetime` datetime DEFAULT NULL NULL COMMENT '录入时间',
ADD COLUMN `completehandlerid` INT (11) DEFAULT 0 NULL COMMENT '补录人id',
ADD COLUMN `completehandlername` VARCHAR (50) DEFAULT NULL NULL COMMENT '补录人姓名',
ADD COLUMN `completedatetime` datetime DEFAULT NULL NULL COMMENT '补录时间',
ADD COLUMN `customerfreightbillid` INT (11) DEFAULT 0 NULL COMMENT '客户运费账单id',
ADD COLUMN `branchfreightbillid` INT (11) DEFAULT 0 NULL COMMENT '站点运费账单id',
ADD COLUMN `provincereceivablefreightbillid` INT (11) DEFAULT 0 NULL COMMENT '跨省应收运费账单id',
ADD COLUMN `provincereceivablecodbillid` INT (11) DEFAULT 0 NULL COMMENT '跨省应收货款账单id',
ADD INDEX `detail_collectorid_idx` (`collectorid`) USING BTREE,
ADD INDEX `detail_instationid_idx` (`instationid`) USING BTREE;

/*包修改*/
ALTER TABLE express_ops_bale 
ADD COLUMN `handlerid` INT (11) DEFAULT 0 NULL COMMENT '合包人id',
ADD COLUMN `handlername` VARCHAR (50) DEFAULT NULL NULL COMMENT '合包人姓名';

/*站点修改*/
ALTER TABLE `express_set_branch`
ADD COLUMN `branchprovinceid`  int(11) NULL DEFAULT NULL COMMENT '站点所在省id' AFTER `pfruleid`,
ADD COLUMN `branchcityid`  int(11) NULL DEFAULT NULL COMMENT '站点所在市id' AFTER `branchprovinceid`,
ADD COLUMN `tpsbranchcode` VARCHAR (50) DEFAULT NULL NULL COMMENT 'tps机构编码' AFTER `branchcode`;

/*订单表添加交接所需字段----王志宇*/
ALTER TABLE express_ops_cwb_detail 
ADD COLUMN `ishandover` INT (11) DEFAULT 0 NULL COMMENT '是否交接标示',
ADD COLUMN `instationhandoverid` INT (11) DEFAULT 0 NULL COMMENT '交接人id',
ADD COLUMN `instationhandovername` VARCHAR (50) DEFAULT NULL NULL COMMENT '交接人名字',
ADD COLUMN `instationhandovertime` DATETIME  DEFAULT NULL NULL COMMENT '交接时间';




/*------------------------------------------------------------------------预置菜单数据--------------------------------------------------------------------*/
/*分拨管理-库房*/
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('501083', '2', '快递预订单查询（省公司）', '501083', 'preOrderOperationController/preOrderquery/1?', '5010');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('501081', '2', '快递预订单处理', '501081', 'preOrderOperationController/toPreOrderDeal/1?', '5010');
/*分拨管理-库房打印--王志宇*/
/*ID冲突 改为502095 --宋考俊*/
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('502095', '2', '库房包号打印', '502095', 'expressPackageCodePrintddd/packageCodeprint?', '5020' ) ;
/*高级查询--王志宇*/
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('9060', '1', '快递订单查询', '9060', 'expressQueryController/kuaidilist/1?', '90' ) ;
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('9070', '1', '快递妥投查询', '9070', 'datastatistics4express/tuotousearch/1?', '90' ) ;

/*分拨管理-站点*/
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('504085', '2', '快递预订单查询（站点）', '504085', 'preOrderOperationController/stationPreOrderQuery/1?', '5040');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('504086', '2', '快递揽件分配/调整', '504086', 'stationOperation/takeExpressAssign/1?', '5040');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('504087', '2', '快递揽件反馈', '504087', 'expressFeedback/expressFeedbackIndex?', '5040');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('504088', '2', '快递揽件录入', '504088', 'embracedOrderInputController/embracedOrderInputInit/?', '5040');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('504089', '2', '快递揽件交接', '504089', 'expressIntoStation/expressQueryList/1?', '5040');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('504090', '2', '快递揽件查询', '504090', 'takeGoodsQuery/toTakeGoodsQuery/1?', '5040');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('504091', '2', '快递揽件合包', '504091', 'stationOperation/takeExpressCombine/1?', '5040');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('504092', '2', '快递揽件出站', '504092', 'expressOutStation/expressOutStationIndex?', '5040');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('504093', '2', '快递交件汇总单', '504093', 'stationOperation/deliverSummary/1?', '5040');

/*添加库房合包查询和站点合包查询菜单 songkaojun*/
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('501082', '2', '库房合包查询', '501082', 'stationOperation/combineQuery/1?', '5010');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('504095', '2', '站点合包查询', '504095', 'stationOperation/combineQuery/1?', '5040');

/*添加电子称称重菜单 songkaojun*/
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('501000', '2', '电子称称重', '501000', 'stationOperation/weighByScale?', '5010');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('504005', '2', '电子称称重', '504005', 'stationOperation/weighByScale?', '5040');

/*添加库房合包菜单 songkaojun*/
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('501002', '2', '库房合包', '501002', 'stationOperation/takeExpressCombine/1?', '5010');

/*分拨管理-站点打印--王志宇*/
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('505055', '2', '站点包号打印', '505055', 'expressPackageCodePrintddd/packageCodeprint?', '5050' ) ;

/*订单处理-揽收运单补录*/
insert into dmp40_function VALUES(4010,1,'揽收运单补录',4010,'embracedOrderInputController/embracedOrderExtraInputInit/?',40);

/*客服管理-创建运单tps重发*/
insert into dmp40_function VALUES(6090,1,'创建运单tps重发',6090,'reSendExpressOrderController/list/1?',60);


/* 支付信息修改申请功能 新增快递运费金额修改  2015-9-30 */
ALTER TABLE `express_ops_zhifu_apply`  ADD COLUMN `shouldfare` decimal(19,2) NULL DEFAULT '0.00' COMMENT '快递运费金额';

/*将运单导出的默认模板新增揽收站点、揽收人、揽收时间、包装费、保价费、运费合计这六个字段*/
insert into express_ops_setexportfield(fieldname,exportstate,fieldenglishname,orderlevel,exportdatatype) VALUES('揽收站点',1,'instationname',157,'string');
insert into express_ops_setexportfield(fieldname,exportstate,fieldenglishname,orderlevel,exportdatatype) VALUES('揽收人',1,'collectorname',158,'string');
insert into express_ops_setexportfield(fieldname,exportstate,fieldenglishname,orderlevel,exportdatatype) VALUES('揽收时间',1,'instationdatetime',159,'string');
insert into express_ops_setexportfield(fieldname,exportstate,fieldenglishname,orderlevel,exportdatatype) VALUES('包装费',1,'packagefee',160,'double');
insert into express_ops_setexportfield(fieldname,exportstate,fieldenglishname,orderlevel,exportdatatype) VALUES('保价费',1,'insuredfee',161,'double');
insert into express_ops_setexportfield(fieldname,exportstate,fieldenglishname,orderlevel,exportdatatype) VALUES('运费合计',1,'totalfee',162,'double');

/*将运单导出的默认模板新增揽件省 add by songkaojun 2015-11-17*/
INSERT  INTO `express_ops_setexportfield`(`fieldname`,`exportstate`,`fieldenglishname`,`orderlevel`,`exportdatatype`) VALUES ('揽件省',1,'Senderprovince',165,'string');

