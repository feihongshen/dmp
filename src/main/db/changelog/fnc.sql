

CREATE TABLE `fn_adjustment_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(30) DEFAULT NULL COMMENT '订单号',
  `bill_no` varchar(30) NOT NULL COMMENT '账单编号',
  `adjust_bill_no` varchar(30) DEFAULT NULL COMMENT '调整账单编号',
  `customer_id` bigint(20) DEFAULT '0' COMMENT '客户ID',
  `receive_fee` decimal(19,2) DEFAULT '0.00' COMMENT '原始应收金额',
  `refund_fee` decimal(19,2) DEFAULT '0.00' COMMENT '原始应退金额',
  `modify_fee` decimal(19,2) DEFAULT '0.00' COMMENT '更新金额',
  `adjust_amount` decimal(19,2) DEFAULT '0.00' COMMENT '调整差额',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator` varchar(30) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status` int(11) DEFAULT '0' COMMENT '核销标识',
  `check_user` varchar(30) DEFAULT NULL COMMENT '核对人',
  `check_time` datetime DEFAULT NULL COMMENT '核对时间',
  `order_type` int(11) DEFAULT '0' COMMENT '订单类型',
  `billid` bigint(11) DEFAULT '0' COMMENT '账单ID',
  PRIMARY KEY (`id`),
  KEY `index_order_no` (`order_no`),
  KEY `index_adjust_bill_no` (`adjust_bill_no`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8;

/*Table structure for table `fn_bill_policy` */

CREATE TABLE `fn_bill_policy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `policy_name` varchar(30) DEFAULT NULL COMMENT '策略名称',
  `org_id` bigint(20) DEFAULT '0' COMMENT '站点编号',
  `order_type` varchar(100) DEFAULT NULL COMMENT '订单类型',
  `order_status` varchar(100) DEFAULT NULL COMMENT '订单状态',
  `date_type` int(11) DEFAULT '0' COMMENT '日期类型',
  `day_from` int(11) DEFAULT '0' COMMENT '从前N天',
  `day_to` int(11) DEFAULT '0' COMMENT '到前M天(N>=M)',
  `time_form` time DEFAULT NULL COMMENT '从T1时间',
  `time_to` time DEFAULT NULL COMMENT '到T2时间(T1<=T2)',
  `pay_method` varchar(100) DEFAULT NULL COMMENT '支付方式',
  `cr_cycle_type` int(11) DEFAULT '1' COMMENT '生成周期类型',
  `cr_cycle_value` int(11) DEFAULT '1' COMMENT '生成周期值，比如周几，或几号',
  `cr_time_cycle` time DEFAULT NULL COMMENT '生成周期，时间',
  `status` int(11) DEFAULT '1' COMMENT '状态，1为有效，0为无效',
  `creator` varchar(30) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(30) DEFAULT NULL COMMENT '修改人',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1071 DEFAULT CHARSET=utf8;

/*Table structure for table `fn_customer_bill` */

CREATE TABLE `fn_customer_bill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(30) DEFAULT NULL COMMENT '账单编号',
  `customer_id` bigint(20) DEFAULT '0' COMMENT '客户编号',
  `bill_type` int(11) DEFAULT '0' COMMENT '账单类型',
  `status` int(11) DEFAULT '0' COMMENT '账单状态',
  `settle_time` datetime DEFAULT NULL COMMENT '结算日期',
  `creator` varchar(30) DEFAULT NULL COMMENT '创建人',
  `check_time` datetime DEFAULT NULL COMMENT '核对日期',
  `check_user` varchar(30) DEFAULT NULL COMMENT '核对人',
  `confirm_time` datetime DEFAULT NULL COMMENT '确认日期',
  `confirm_user` varchar(30) DEFAULT NULL COMMENT '确认人',
  `verify_time` datetime DEFAULT NULL COMMENT '核销日期',
  `verify_user` varchar(30) DEFAULT NULL COMMENT '核销人',
  `deliver_org` varchar(5000) DEFAULT NULL COMMENT '发货仓库',
  `bill_count` int(11) DEFAULT '0' COMMENT '订单数',
  `bill_amount` decimal(19,2) DEFAULT '0.00' COMMENT '账单金额',
  `actual_amount` decimal(19,2) DEFAULT '0.00' COMMENT '实际金额',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `date_type` int(11) DEFAULT '0' COMMENT '日期类型',
  `start_time` datetime DEFAULT NULL COMMENT '起始日期',
  `end_time` datetime DEFAULT NULL COMMENT '截止日期',
  `create_type` varchar(20) DEFAULT NULL COMMENT '创建类型 Import/Condition',
  `import_remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_bill_no` (`bill_no`),
  KEY `index_customer_id` (`customer_id`)
) ENGINE=InnoDB AUTO_INCREMENT=283 DEFAULT CHARSET=utf8;

/*Table structure for table `fn_customer_bill_detail` */

CREATE TABLE `fn_customer_bill_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_id` bigint(20) NOT NULL COMMENT '账单ID',
  `order_no` varchar(30) DEFAULT NULL COMMENT '订单号',
  `order_id` varchar(30) DEFAULT NULL COMMENT '订单ID',
  `receive_fee` decimal(19,2) DEFAULT '0.00' COMMENT '代收金额',
  `actual_pay` decimal(19,2) DEFAULT '0.00' COMMENT '已付甲方代收金额',
  `pay_time` datetime DEFAULT NULL COMMENT '入账日期',
  `refund` decimal(19,2) DEFAULT '0.00' COMMENT '应退款',
  `actual_refund` decimal(19,2) DEFAULT '0.00' COMMENT '实退款',
  `diff_amount` decimal(19,2) DEFAULT '0.00' COMMENT '差异金额',
  `pay_method` int(11) DEFAULT '0' COMMENT '支付方式',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `signer` varchar(30) DEFAULT NULL COMMENT '签收人',
  `sign_time` datetime DEFAULT NULL COMMENT '签收时间',
  `arrival_time` datetime DEFAULT NULL COMMENT '到货时间',
  PRIMARY KEY (`id`),
  KEY `index_order_no` (`order_no`),
  KEY `index_order_id` (`order_id`),
  KEY `index_bill_id` (`bill_id`)
) ENGINE=InnoDB AUTO_INCREMENT=51313 DEFAULT CHARSET=utf8;

/*Table structure for table `fn_order_recharge` */

CREATE TABLE `fn_order_recharge` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_id` bigint(20) NOT NULL COMMENT '账单id',
  `bill_detail_id` bigint(20) NOT NULL COMMENT '账单明细id',
  `recharge_id` bigint(20) NOT NULL COMMENT '充值记录id',
  `recharge_amount` decimal(19,2) DEFAULT '0.00' COMMENT '冲抵金额',
  PRIMARY KEY (`id`),
  KEY `index_recharge_id` (`recharge_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17599 DEFAULT CHARSET=utf8;

/*Table structure for table `fn_org_account` */

CREATE TABLE `fn_org_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) DEFAULT '0' COMMENT '站点编号',
  `bank` varchar(30) DEFAULT NULL COMMENT '银行',
  `account_no` varchar(30) DEFAULT NULL COMMENT '银行账户',
  `openbank` varchar(30) DEFAULT NULL COMMENT '开户行',
  `surplusfee` decimal(19,2) DEFAULT '0.00' COMMENT '账户余额',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8;

/*Table structure for table `fn_org_bank_import` */

CREATE TABLE `fn_org_bank_import` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bi_count` int(11) DEFAULT '0' COMMENT '导入笔数',
  `amount` decimal(19,2) DEFAULT '0.00' COMMENT '导入金额',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creator` varchar(30) DEFAULT NULL COMMENT '创建人',
  `status` int(11) DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=312 DEFAULT CHARSET=utf8;

/*Table structure for table `fn_org_bill` */

CREATE TABLE `fn_org_bill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(30) DEFAULT NULL COMMENT '账单编号',
  `org_id` bigint(20) DEFAULT '0' COMMENT '站点编号',
  `status` int(11) DEFAULT '0' COMMENT '账单状态',
  `settle_time` datetime DEFAULT NULL COMMENT '结算日期',
  `creator` varchar(30) DEFAULT NULL COMMENT '创建人',
  `receipt_time` datetime DEFAULT NULL COMMENT '收款日期',
  `receipt_user` varchar(30) DEFAULT NULL COMMENT '收款人',
  `audit_time` datetime DEFAULT NULL COMMENT '审核日期',
  `auditor` varchar(30) DEFAULT NULL COMMENT '审核人',
  `bill_count` int(11) DEFAULT '0' COMMENT '订单数',
  `bill_amount` decimal(19,2) DEFAULT '0.00' COMMENT '账单金额',
  `actual_amount` decimal(19,2) DEFAULT '0.00' COMMENT '实际金额',
  `advance_pay` decimal(19,2) DEFAULT '0.00' COMMENT '冲抵金额',
  `cash` decimal(19,2) DEFAULT '0.00' COMMENT '现金',
  `pos` decimal(19,2) DEFAULT '0.00' COMMENT 'POS机支付',
  `cod` decimal(19,2) DEFAULT '0.00',
  `other_fee` decimal(19,2) DEFAULT '0.00' COMMENT '其他支付',
  `total_fee` decimal(19,2) DEFAULT '0.00' COMMENT '合计',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `pay_method` int(11) DEFAULT '0' COMMENT '支付方式',
  `bill_type` int(11) DEFAULT '0' COMMENT '账单类型',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_bill_no` (`bill_no`),
  KEY `index_org_id` (`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=746 DEFAULT CHARSET=utf8;

/*Table structure for table `fn_org_bill_adjustment_record` */

CREATE TABLE `fn_org_bill_adjustment_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(30) DEFAULT NULL COMMENT '订单号',
  `bill_id` bigint(11) DEFAULT '0' COMMENT '账单ID',
  `bill_no` varchar(30) NOT NULL COMMENT '账单编号',
  `adjust_bill_no` varchar(30) DEFAULT NULL COMMENT '调整账单编号',
  `deliverybranchid` bigint(20) DEFAULT '0' COMMENT '配送站点',
  `customer_id` bigint(20) DEFAULT '0' COMMENT '客户ID',
  `receive_fee` decimal(19,2) DEFAULT '0.00' COMMENT '原始应收金额',
  `refund_fee` decimal(19,2) DEFAULT '0.00' COMMENT '原始应退金额',
  `modify_fee` decimal(19,2) DEFAULT '0.00' COMMENT '更新金额',
  `goods_amount` decimal(19,2) DEFAULT '0.00' COMMENT '货款金额',
  `adjust_amount` decimal(19,2) DEFAULT '0.00' COMMENT '调整差额',
  `verify_amount` decimal(19,2) DEFAULT '0.00' COMMENT '核销金额',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `creator` varchar(30) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status` int(11) DEFAULT '0' COMMENT '付款状态',
  `order_type` int(11) DEFAULT '0' COMMENT '订单类型',
  `pay_method` int(11) DEFAULT '0' COMMENT '支付方式',
  `deliver_id` bigint(20) DEFAULT '0' COMMENT '小件员',
  `sign_time` datetime DEFAULT NULL COMMENT '签收时间',
  `recharges_id` bigint(20) DEFAULT '0' COMMENT '充值记录编号',
  PRIMARY KEY (`id`),
  KEY `index_order_no` (`order_no`),
  KEY `index_adjust_bill_no` (`adjust_bill_no`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

/*Table structure for table `fn_org_bill_detail` */

CREATE TABLE `fn_org_bill_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_id` bigint(20) NOT NULL COMMENT '账单ID',
  `order_no` varchar(30) DEFAULT NULL COMMENT '订单号',
  `order_id` varchar(30) DEFAULT NULL COMMENT '订单ID',
  `status` int(11) DEFAULT '0' COMMENT '收款状态',
  `pay_method` int(11) DEFAULT '0' COMMENT '收款类型',
  `picker` varchar(30) DEFAULT NULL COMMENT '小件员',
  `goods_amount` decimal(19,2) DEFAULT '0.00' COMMENT '货款金额',
  `verify_amount` decimal(19,2) DEFAULT '0.00' COMMENT '核销金额',
  `recharges_id` bigint(20) DEFAULT '0' COMMENT '充值记录编号',
  `sign_time` datetime DEFAULT NULL COMMENT '签收日期',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `index_order_no` (`order_no`),
  KEY `index_order_id` (`order_id`),
  KEY `index_bill_id` (`bill_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24788 DEFAULT CHARSET=utf8;

/*Table structure for table `fn_org_recharges` */

CREATE TABLE `fn_org_recharges` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) DEFAULT '0' COMMENT '站点编号',
  `paymethod` int(11) DEFAULT '0' COMMENT '支付方式',
  `account_no` varchar(30) DEFAULT NULL COMMENT '充值账户',
  `amount` decimal(19,2) DEFAULT '0.00' COMMENT '充值金额',
  `surplus` decimal(19,2) DEFAULT '0.00' COMMENT '冲抵之后剩余金额',
  `remark` text COMMENT '冲抵备注',
  `create_time` datetime DEFAULT NULL COMMENT '充值时间',
  `creator` varchar(30) DEFAULT NULL COMMENT '操作人',
  `bi_id` bigint(20) DEFAULT '0' COMMENT '银行导入记录ID',
  PRIMARY KEY (`id`),
  KEY `index_org_id` (`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=71769 DEFAULT CHARSET=utf8;

