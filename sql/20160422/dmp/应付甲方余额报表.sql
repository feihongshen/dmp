-- 新建应付甲方报表明细表结构
create table `fn_brance_report_detail`( 
   `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'id', 
   `brance_id` BIGINT(20)  COMMENT '应付甲方报表id', 
   `customer_id` BIGINT(20)  COMMENT '客户id', 
   `order_number` varchar(50) NOT NULL DEFAULT '' COMMENT '订单号',
	`order_type` int(2)  COMMENT '订单类型' , 
	`order_status` int(2)  COMMENT '订单状态' ,
	`operate_status` int(2) COMMENT '订单操作状态' ,
   `branch_id` BIGINT(20) COMMENT '配送站点Id', 
   `receivable_amount` decimal(18,4) NOT NULL DEFAULT '0' COMMENT '应收金额',
   `pay_amount` decimal(18,4) NOT NULL DEFAULT '0' COMMENT '应付金额', 
   `pay_type` int(2) NOT NULL DEFAULT '0' COMMENT '支付方式',
   `delivery_time` varchar(30) COMMENT '发货时间' ,
   `warehousing_time` varchar(30) COMMENT '分拣入库时间' ,
   `audit_time` varchar(30) COMMENT '归班审核时间' ,
   `adjust_amount` decimal(18,4) NOT NULL DEFAULT '0' COMMENT '调整金额',
   `create_time` timestamp COMMENT '创建时间' ,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
 
-- 新建应付调整单明细表
create table `fn_brance_report_adjust`( 
   `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'id', 
	`customer_id` BIGINT(20) COMMENT '客户ID'  ,
   `order_number` varchar(50) NOT NULL DEFAULT '' COMMENT '订单号',
	`order_type` int(2)  COMMENT '订单类型' , 
	`order_status` int(2)  COMMENT '订单状态' ,
	`operate_status` int(2) COMMENT '订单操作状态' ,
   `branch_id` BIGINT(20) COMMENT '配送站点Id', 
   `receivable_amount` decimal(18,4) NOT NULL DEFAULT '0' COMMENT '应收金额',
   `pay_amount` decimal(18,4) NOT NULL DEFAULT '0' COMMENT '应付金额', 
   `pay_type` int(2) NOT NULL DEFAULT '0' COMMENT '支付方式',
   `adjust_amount` decimal(18,4) NOT NULL DEFAULT '0' COMMENT '调整金额',
   `create_time` timestamp NOT NULL DEFAULT now()  COMMENT '创建时间' ,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
 
-- 应付甲方报表的表结构
create table `fn_brance_report`( 
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id', 
   `customer_id` int(11) NOT NULL COMMENT '客户id', 
   `initial_amount` decimal(18,4) NOT NULL DEFAULT '0' COMMENT '期初余额',
	`payable_amount` decimal(18,4) NOT NULL DEFAULT '0' COMMENT '应付当期发生额' , 
	`receivable_amount` decimal(18,4) NOT NULL DEFAULT '0' COMMENT '应收当期发生额' ,
	`pos_amount` decimal(18,4) NOT NULL DEFAULT '0' COMMENT '当期POS抵扣额' ,
   `order_adjust_amount` decimal(18,4) NOT NULL DEFAULT '0' COMMENT '订单当期调整金额', 
   `return_goods_amount` decimal(18,4) NOT NULL DEFAULT '0' COMMENT '当期退货货款金额', 
   `lost_amount` decimal(18,4) NOT NULL DEFAULT '0' COMMENT '当期丢失额',
   `pay_amount` decimal(18,4) NOT NULL DEFAULT '0' COMMENT '当期付款',
   `pay_order_adjust_amount` decimal(18,4) NOT NULL DEFAULT '0' COMMENT '付款单当期调整金额',
   `final_amount` decimal(18,4) NOT NULL DEFAULT '0' COMMENT '期末余额' ,
   `create_time` timestamp COMMENT '时间' ,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
 
-- 应付甲方报表汇款信息
create table `fn_cust_pay_remit_detail`( 
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id', 
   `customer_id` int(11) NOT NULL COMMENT '客户id', 
   `remit_no` varchar(50) COMMENT '汇款编号',
	`bill_no` varchar(50) COMMENT '账单编号' , 
   `remit_amount` decimal(19,2) NOT NULL DEFAULT '0' COMMENT '汇款金额', 
   `remit_man` varchar(50) NOT NULL COMMENT '汇款人', 
   `remit_time` datetime NOT NULL COMMENT '汇款时间', 
   `status` tinyint(4) DEFAULT '0' COMMENT '汇款记录状态：0有效，1已作废', 
   `create_time` datetime COMMENT '创建时间', 
   `create_user` varchar(50) COMMENT '创建用户的帐号', 
   `cancel_time` datetime COMMENT '作废的操作时间', 
   `cancel_user` varchar(50) COMMENT '作废的操作用户的帐号', 
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
 
-- 应付甲方报表客户信息
create table `fn_cust_pay_report_cfg`( 
   `customer_id` int(11) NOT NULL COMMENT '客户id', 
   `settle_type` tinyint(4) DEFAULT '0' COMMENT '结算类型:买单结算0,返款结算1', 
   `settle_time_type` tinyint(4) DEFAULT '0' COMMENT '结算时间类型:客户发货时间0,分拣入库时间1,归班审核时间2', 
   `create_time` DATETIME COMMENT '创建时间', 
   `create_user` varchar(50) COMMENT '创建人', 
   `update_user` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '更新人',
	`update_time` DATETIME NULL DEFAULT NULL COMMENT '更新时间',
   PRIMARY KEY (`customer_id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
 
-- 应付甲方余额报表菜单
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('802060', '2', '客户结算信息管理', '802060', '${eapUrl}customerInformManager.do?isIframe&index&', '8020');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('802061', '2', '实付客户记录管理', '802061', '${eapUrl}customerPayedRecordManager.do?isIframe&index&', '8020');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('802062', '2', '应付甲方余额报表', '802062', '${eapUrl}branceReportManager.do?isIframe&index&', '8020');