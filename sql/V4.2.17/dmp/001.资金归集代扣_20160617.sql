-- 站点缴款导入银行记录表（fn_org_bank_import）新增字段
ALTER TABLE `fn_org_bank_import`   
  ADD COLUMN `current_mode` TINYINT(2) DEFAULT 1  NOT NULL  COMMENT '结算模式。1.账单，2.签收余额。默认为账单';

-- 新增余额报表模式下的缴款导入表（fn_org_recharges_rptmode）
CREATE TABLE `fn_org_recharges_rptmode` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `recharge_no` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '缴款单号。年月日+5位流水号+P',
  `org_id` BIGINT(20) DEFAULT '0' COMMENT '站点编号',
  `paymethod` INT(11) DEFAULT '0' COMMENT '支付方式',
  `amount` DECIMAL(19,2) DEFAULT '0.00' COMMENT '充值金额',
  `surplus` DECIMAL(19,2) DEFAULT '0.00' COMMENT '冲抵之后剩余金额',
  `remark` TEXT COMMENT '冲抵备注',
  `create_time` DATETIME DEFAULT NULL COMMENT '充值时间',
  `import_time` DATETIME DEFAULT NULL COMMENT '创建（导入）时间',
  `creator` VARCHAR(30) DEFAULT NULL COMMENT '操作人',
  `updated_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT '最后更新时间',
  `bi_id` BIGINT(20) DEFAULT '0' COMMENT '银行导入记录ID',
  `handle_status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '处理状态。0导入，1已处理但未核销，2已处理已核销，3处理异常，-1已撤销',
  `picker` VARCHAR(30) NOT NULL DEFAULT '' COMMENT '小件员登录名',
  `picker_id` BIGINT(20) DEFAULT '0' COMMENT '小件员id',
  `payin_type` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '缴款方式。1.站点缴款，2小件员缴款',
  `recharge_source` TINYINT(2) DEFAULT NULL COMMENT '缴款来源：1.导入缴款2.应退金额缴款3.运费调整缴款4.代扣收款5.POSCOD自动缴款6.货款调整缴款',
  `cwb` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '订单号',
  `vpal_record_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '唯品代扣记录id',
  PRIMARY KEY (`id`),
  KEY `idx_org_id` (`org_id`),
  KEY `idx_picker_id` (`picker_id`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_bi_id` (`bi_id`),
  KEY `idx_vpal_record_id` (`vpal_record_id`),
  KEY `idx_cwb` (`cwb`)
) ENGINE=INNODB CHARSET=utf8;

-- 新增余额报表订单明细冲抵记录表（fn_orgrpt_order_recharge）
CREATE TABLE `fn_orgrpt_order_recharge`(  
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `cwb` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '订单号',
  `recharge_amount` DECIMAL(19,2) NOT NULL DEFAULT 0 COMMENT '冲抵金额',
  `fee_type` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '费用类型。1.代收货款，2.上门退运费，3.快递运费',
  `recharge_id` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '缴款导入记录id',
  `created_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP  COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `Idx_cwb` (`cwb`),
  INDEX `Idx_recharge_id` (`recharge_id`),
  INDEX `Idx_created_time` (`created_time`)
) ENGINE=INNODB CHARSET=utf8;

-- 新增回款调整记录表（fn_org_recharges_adjustment_record）
CREATE TABLE `fn_org_recharges_adjustment_record`(  
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `cwb` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '订单号',
  `pay_method` TINYINT(4) COMMENT '支付方式',
  `deliverybranchid` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '配送站点',
  `deliveryid` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '小件员',
  `adjust_amount` DECIMAL(19,2) NOT NULL DEFAULT 0 COMMENT '调整金额',
  `sign_time` DATETIME COMMENT '签收时间',
  `created_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP  COMMENT '创建时间',
  `created_user` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '创建人',
  PRIMARY KEY (`id`),
  INDEX `idx_cwb` (`cwb`),
  INDEX `idx_deliverybranchid` (`deliverybranchid`),
  INDEX `idx_deliveryid` (`deliveryid`),
  INDEX `idx_created_time` (`created_time`)
) ENGINE=INNODB CHARSET=utf8;

-- 站点签收订单快照表（fn_station_sign_order_details_snapshot） 新增表字段
ALTER TABLE `fn_station_sign_order_details_snapshot`   
  ADD COLUMN `shouldfareflag` TINYINT(2) DEFAULT 0  NOT NULL  COMMENT '上门退应收运费收款标示0.未收款，1.已收款，2.部分收款',
  ADD COLUMN `receivablefeeflag` TINYINT(2) DEFAULT 0  NOT NULL  COMMENT '应收货款收款标示0.未收款，1.已收款，2.部分收款',
  ADD COLUMN `expressfreightflag` TINYINT(2) DEFAULT 0  NOT NULL  COMMENT '快递运费收款标示0.未收款，1.已收款，2.部分收款',
  ADD COLUMN `shouldfare_receipt` DECIMAL(19,2) DEFAULT 0  NOT NULL  COMMENT '应收运费已收款金额',
  ADD COLUMN `receivablefee_receipt` DECIMAL(19,2) DEFAULT 0  NOT NULL  COMMENT '应收货款已收金额',
  ADD COLUMN `expressfreight_receipt` DECIMAL(19,2) DEFAULT 0  NOT NULL  COMMENT '快递运费已收金额';
 
-- 现付类型快递运费订单快照表（fn_station_sign_order_details_snapshot_express）新增字段
ALTER TABLE `fn_station_sign_order_details_snapshot_express`   
  ADD COLUMN `shouldfareflag` TINYINT(2) DEFAULT 0  NOT NULL  COMMENT '上门退应收运费收款标示0.未收款，1.已收款，2.部分收款',
  ADD COLUMN `receivablefeeflag` TINYINT(2) DEFAULT 0  NOT NULL  COMMENT '应收货款收款标示0.未收款，1.已收款，2.部分收款',
  ADD COLUMN `expressfreightflag` TINYINT(2) DEFAULT 0  NOT NULL  COMMENT '快递运费收款标示0.未收款，1.已收款，2.部分收款',
  ADD COLUMN `shouldfare_receipt` DECIMAL(19,2) DEFAULT 0  NOT NULL  COMMENT '应收运费已收款金额',
  ADD COLUMN `receivablefee_receipt` DECIMAL(19,2) DEFAULT 0  NOT NULL  COMMENT '应收货款已收金额',
  ADD COLUMN `expressfreight_receipt` DECIMAL(19,2) DEFAULT 0  NOT NULL  COMMENT '快递运费已收金额';

-- 新增站点/小件员结算账户信息表（fn_org_pay_account）
CREATE TABLE `fn_org_pay_account`(  
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `branchid` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '站点id',
  `deliveryid` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '小件员id',
  `bank_code` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '银行代码',
  `bank_name` VARCHAR(60) NOT NULL DEFAULT '' COMMENT '开户行名称',
  `card_type` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '银行卡类型:1 借记卡 2 贷记卡 3 存折',
  `card_name` VARCHAR(256) NOT NULL DEFAULT '' COMMENT '卡主姓名',
  `account_prop` TINYINT(2) NOT NULL DEFAULT 0 COMMENT '账户属性.0 私人(默认),1 公司',
  `card_no` VARCHAR(256) NOT NULL DEFAULT '' COMMENT '卡号',
  `province` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '开户行所在省',
  `city` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '开户行所在市',
  `id_type` VARCHAR(2) NOT NULL DEFAULT '00'  COMMENT '证件类型。00身份证 01其他.当协议号不为空时，此项可不输',
  `mobile_no` VARCHAR(11) NOT NULL DEFAULT '' COMMENT '手机号码',
  `id_no` VARCHAR(256) NOT NULL DEFAULT '' COMMENT '证件号',
  `validate` VARCHAR(4) NOT NULL DEFAULT '' COMMENT '银行卡有效期',
  `cvv2` VARCHAR(3) NOT NULL DEFAULT '' COMMENT 'CVV.卡类型为信用卡必输',
  `created_time` DATETIME COMMENT '创建时间',
  `created_user` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `updated_user` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `is_active` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否激活。0.否，1.是',
  PRIMARY KEY (`id`),
  INDEX `idx_branchid` (`branchid`),
  INDEX `idx_deliveryid` (`deliveryid`)
) ENGINE=INNODB CHARSET=utf8;

-- 新增站点/小件员结算账户信息修改记录表（fn_org_pay_account_modify_record）
CREATE TABLE `fn_org_pay_account_modify_record`(  
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `orginid` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '原始记录id',
  `branchid` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '站点id',
  `deliveryid` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '小件员id',
  `bank_code` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '银行代码',
  `bank_name` VARCHAR(60) NOT NULL DEFAULT '' COMMENT '开户行名称',
  `card_type` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '银行卡类型:1 借记卡 2 贷记卡 3 存折',
  `card_name` VARCHAR(256) NOT NULL DEFAULT '' COMMENT '卡主姓名',
  `account_prop` TINYINT(2) NOT NULL DEFAULT 0 COMMENT '账户属性.0 私人(默认),1 公司',
  `card_no` VARCHAR(256) NOT NULL DEFAULT '' COMMENT '卡号',
  `province` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '开户行所在省',
  `city` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '开户行所在市',
  `id_type` VARCHAR(2) NOT NULL DEFAULT '00'  COMMENT '证件类型。00身份证 01其他.当协议号不为空时，此项可不输',
  `mobile_no` VARCHAR(11) NOT NULL DEFAULT '' COMMENT '手机号码',
  `id_no` VARCHAR(256) NOT NULL DEFAULT '' COMMENT '证件号',
  `validate` VARCHAR(4) NOT NULL DEFAULT '' COMMENT '银行卡有效期',
  `cvv2` VARCHAR(3) NOT NULL DEFAULT '' COMMENT 'CVV.卡类型为信用卡必输',
  `created_time` DATETIME COMMENT '创建时间',
  `created_user` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `updated_user` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `is_active` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否激活。0.否，1.是',
  `modify_type` VARCHAR(4) NOT NULL DEFAULT '新增'  COMMENT '修改类型。取值：新增，修改，删除',
  PRIMARY KEY (`id`),
  INDEX `idx_orginid` (`orginid`)
) ENGINE=INNODB CHARSET=utf8;

-- 代扣请求接口表（fn_vpal_interface）
CREATE TABLE `fn_vpal_interface`(  
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `partner_id` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '商户编码',
  `request_id` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '请求流水号',
  `request_type` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '请求类型。1.单笔代扣，2.批量代扣',
  `request_time` DATETIME COMMENT '交易开始时间',
  `response_time` DATETIME COMMENT '获得响应时间',
  `req_json` TEXT COMMENT '请求报文',
  `resp_json` TEXT COMMENT '响应报文',
  `created_user` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '创建人',
  PRIMARY KEY (`id`),
  INDEX `idx_request_id` (`request_id`)
) ENGINE=INNODB CHARSET=utf8;

-- 新增代扣记录表（fn_vpal_record）
CREATE TABLE `fn_vpal_record`(  
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `partner_id` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '商户编码',
  `request_id` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '请求流水号',
  `request_sn` VARCHAR(8) NOT NULL DEFAULT '' COMMENT '请求序号',
  `settle_date` INT(11) NOT NULL DEFAULT 0 COMMENT '结算日期yyyyMMdd',
  `request_time` DATETIME COMMENT '提交（请求）时间',
  `complete_time` DATETIME COMMENT '交易完成时间',
  `deliveryid` BIGINT(0) NOT NULL COMMENT '小件员id',
  `branchid` BIGINT(0) NOT NULL COMMENT '站点id',
  `amount` DECIMAL(19,2) NOT NULL DEFAULT 0 COMMENT '交易金额',
  `state` TINYINT(2) COMMENT '代扣状态',
  `pay_result` TINYINT(2) NOT NULL DEFAULT 0 COMMENT '支付结果0.处理中，1.成功，2.失败',
  `pay_result_msg` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '支付结果信息',
  `bank_name` VARCHAR(60) NOT NULL DEFAULT '' COMMENT '开户行名称',
  `card_no` VARCHAR(256) NOT NULL DEFAULT '' COMMENT '卡号',
  `card_name` VARCHAR(256) NOT NULL DEFAULT '' COMMENT '卡主姓名',
  `account_prop` TINYINT(2) NOT NULL DEFAULT 0 COMMENT '账户属性.0 私人(默认),1 公司',
  `mobile_no` VARCHAR(11) NOT NULL DEFAULT '' COMMENT '手机号码',
  `created_time` DATETIME COMMENT '创建时间',
  `created_user` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `updated_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_settle_date` (`settle_date`),
  INDEX `idx_request_time` (`request_time`),
  INDEX `idx_deliveryid` (`deliveryid`),
  INDEX `idx_branchid` (`branchid`)
) ENGINE=INNODB CHARSET=utf8;

-- 新增代扣对账文件列表（fn_vpal_check_filelist）
CREATE TABLE `fn_vpal_check_filelist` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filename` varchar(50) NOT NULL DEFAULT '' COMMENT '文件名',
  `filepath` varchar(200) NOT NULL DEFAULT '' COMMENT '文件路径',
  `check_date` int(11) DEFAULT NULL COMMENT '对账日期yyyyMMdd',
  `file_format` varchar(10) NOT NULL DEFAULT '' COMMENT '文件格式',
  `created_time` datetime DEFAULT NULL COMMENT '文件接收时间',
  `file_size` int(11) DEFAULT '0' COMMENT '文件大小，单位KB',
  `user_down_time` int(11) DEFAULT '0' COMMENT '用户下载文件次数',
  `download_state` tinyint(1) NOT NULL DEFAULT '0' COMMENT '文件接收状态：0.未接收（下载）成功，1.已接收（下载）',
  `state_desc` varchar(200) DEFAULT NULL COMMENT '下载状态描述',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_check_date` (`check_date`),
  KEY `idx_created_time` (`created_time`)
) ENGINE=InnoDB CHARSET=utf8;

-- 新增订单财务结算状态表（fn_cwb_state）
CREATE TABLE `fn_cwb_state` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `cwb` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '订单号',
  `cwbordertypeid` INT(11) NOT NULL DEFAULT '-1' COMMENT '订单类型',
  `customerid` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '客户id',
  `smtfreightflag` TINYINT(2) NOT NULL DEFAULT '0' COMMENT '上门退运费收款状态。0.未收款，1.已收款，2.部分收款',
  `smtfreight_time` DATETIME DEFAULT NULL COMMENT '上门退运费收款时间',
  `receivablefeeflag` TINYINT(2) NOT NULL DEFAULT '0' COMMENT '应收货款收款状态0.未收款，1.已收款，2.部分收款',
  `receivablefee_time` DATETIME DEFAULT NULL COMMENT '应收货款收款时间',
  `expressfreightflag` TINYINT(2) NOT NULL DEFAULT '0' COMMENT '快递运费收款状态0.未收款，1.已收款，2.部分收款',
  `expressfreight_time` DATETIME DEFAULT NULL COMMENT '快递运费收款时间',
  `created_time` DATETIME DEFAULT NULL COMMENT '创建时间',
  `updated_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `un_idx_cwb` (`cwb`)
) ENGINE=INNODB DEFAULT CHARSET=utf8

-- 新增各大银行信息字典表（fn_set_bank）
CREATE TABLE `fn_set_bank`(  
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `bank_name` VARCHAR(60) NOT NULL DEFAULT '' COMMENT '银行名称',
  `bank_code` VARCHAR(20) NOT NULL DEFAULT '' COMMENT '银行代码',
  PRIMARY KEY (`id`)
) ENGINE=INNODB CHARSET=utf8;

/*!40101 SET NAMES utf8 */;
-- 初始化各大银行信息字典表（fn_set_bank）
insert into `fn_set_bank` (`bank_name`, `bank_code`) values('中国银行','BOC');
insert into `fn_set_bank` (`bank_name`, `bank_code`) values('建设银行','CCB');
insert into `fn_set_bank` (`bank_name`, `bank_code`) values('邮政储蓄','PSBC');
insert into `fn_set_bank` (`bank_name`, `bank_code`) values('中信银行','CNCB');
insert into `fn_set_bank` (`bank_name`, `bank_code`) values('光大银行','CEB');
insert into `fn_set_bank` (`bank_name`, `bank_code`) values('华夏银行','HXB');
insert into `fn_set_bank` (`bank_name`, `bank_code`) values('招商银行','CMB');
insert into `fn_set_bank` (`bank_name`, `bank_code`) values('兴业银行','CIB');
insert into `fn_set_bank` (`bank_name`, `bank_code`) values('浦发银行','SPDB');
insert into `fn_set_bank` (`bank_name`, `bank_code`) values('平安银行','PAB');
insert into `fn_set_bank` (`bank_name`, `bank_code`) values('广发银行','GDB');
insert into `fn_set_bank` (`bank_name`, `bank_code`) values('民生银行','CMBC');
insert into `fn_set_bank` (`bank_name`, `bank_code`) values('农业银行','ABC');
insert into `fn_set_bank` (`bank_name`, `bank_code`) values('交通银行','BCOM');
insert into `fn_set_bank` (`bank_name`, `bank_code`) values('北京银行','BOB');
insert into `fn_set_bank` (`bank_name`, `bank_code`) values('工商银行','ICBC');

-- 新增菜单表记录
insert into `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) values('803091','2','对账文件查询','803091','${eapUrl}fnVpalMgmt.action?checkFilelistIndex&','8030'),('803092','2','结算信息管理','803092','${eapUrl}orgPayAccount.action?index&','8030'),('803093','2','代扣查询','803090','${eapUrl}fnVpalMgmt.action?queryDealRecordIndex&','8030');

