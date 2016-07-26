-- 备份已有的表， 重命名为*_backup, 系统上线后，需要drop掉
ALTER TABLE fn_df_agreement RENAME TO fn_df_agreement_backup;
ALTER TABLE fn_df_bill_period RENAME TO fn_df_bill_period_backup;
ALTER TABLE fn_df_confirm_rate RENAME TO fn_df_confirm_rate_backup;
ALTER TABLE fn_df_confirm_rate_detail RENAME TO fn_df_confirm_rate_detail_backup;
ALTER TABLE fn_df_rule RENAME TO fn_df_rule_backup;
ALTER TABLE fn_df_rule_area RENAME TO fn_df_rule_area_backup;
ALTER TABLE fn_df_rule_range RENAME TO fn_df_rule_range_backup;
ALTER TABLE fn_df_rule_subsidy RENAME TO fn_df_rule_subsidy_backup;
ALTER TABLE fn_df_sanction RENAME TO fn_df_sanction_backup;

-- ----------------------------
-- Table structure for fn_df_agreement
-- ----------------------------
CREATE TABLE `fn_df_agreement` (
  `agt_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `agt_no` varchar(32) DEFAULT NULL COMMENT '协议编号',
  `org_id` bigint(20) DEFAULT NULL COMMENT '站点',
  `agt_start` datetime DEFAULT NULL COMMENT '协议生效时间',
  `agt_user` varchar(32) DEFAULT NULL COMMENT '协议生效人',
  `suspend_time` datetime DEFAULT NULL COMMENT '停用时间',
  `suspend_user` varchar(32) DEFAULT NULL COMMENT '停用人',
  `agt_state` tinyint(4) DEFAULT NULL COMMENT '协议状态',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `charger_type` tinyint(2) DEFAULT NULL COMMENT '结算对象，0-站点，1-协议',
  PRIMARY KEY (`agt_id`),
  KEY `agtNoIdx` (`agt_no`),
  KEY `orgIdIdx` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='派费协议';

-- ----------------------------
-- Table structure for fn_df_bill_org
-- ----------------------------
CREATE TABLE `fn_df_bill_org` (
  `bill_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '账单编号',
  `org_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '站点',
  `period_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '对应的账期',
  `period_date` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '账单生成月份',
  `start_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:01' COMMENT '起始时间',
  `end_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:01' COMMENT '结束时间',
  `bill_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '账单类型',
  `bill_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '账单状态',
  `order_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '订单类型',
  `total_order` int(11) NOT NULL DEFAULT '0' COMMENT '订单数量',
  `bill_amount` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '账单金额',
  `taking_amount` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '揽件费',
  `delivery_amount` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '派费',
  `kpi_sanction` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT 'KPI奖罚',
  `bill_sanction` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '结算奖罚',
  `qc_sanction` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '品控奖罚',
  `other_sanction` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '其它奖罚',
  `create_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:01' COMMENT '创建时间',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `gen_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '账单生成方式',
  PRIMARY KEY (`bill_id`),
  KEY `idx_period_date` (`period_date`),
  KEY `idx_org_id` (`org_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='加盟站派费账单表';

-- ----------------------------
-- Table structure for fn_df_bill_period
-- ----------------------------
CREATE TABLE `fn_df_bill_period` (
  `period_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) DEFAULT NULL COMMENT '站点',
  `deliver_id` bigint(11) DEFAULT NULL COMMENT '小件员userid(加盟站类型时为空)',
  `period_state` tinyint(4) NOT NULL COMMENT '账期状态',
  `base_day` tinyint(4) NOT NULL COMMENT '账期每月结束日',
  `cr_base` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '妥投率保底值',
  `cr_expect` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '妥投率期望值',
  `charger_type` tinyint(1) unsigned zerofill NOT NULL COMMENT '结算对象，0-站点，1-小件员',
  `cr_reward_price` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '奖励单价',
  `cr_punish_price` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '惩罚单价',
  `cr_reward_limit` decimal(18,2) DEFAULT NULL COMMENT '奖励上限',
  `cr_punish_limit` decimal(18,2) DEFAULT NULL COMMENT '惩罚上限',
  `period_desc` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '账期描述',
  `remark` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '备注',
  `need_kpi` tinyint(1) DEFAULT NULL COMMENT '是否定义了KPI参数 0:否， 1:是',
  `create_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:01' COMMENT '创建时间',
  `create_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime DEFAULT '2000-01-01 00:00:01' COMMENT '修改时间',
  `update_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '修改人',
  `abolish_time` datetime DEFAULT '2000-01-01 00:00:01' COMMENT '废除时间',
  `abolish_user` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '废除操作人',
  PRIMARY KEY (`period_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='派费账期';

-- ----------------------------
-- Table structure for fn_df_bill_staff
-- ----------------------------
CREATE TABLE `fn_df_bill_staff` (
  `bill_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '账单编号',
  `org_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '站点',
  `deliveryid` bigint(20) NOT NULL DEFAULT '0' COMMENT '小件员id',
  `period_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '对应的账期',
  `period_date` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '账单生成月份',
  `start_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:01' COMMENT '起始时间',
  `end_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:01' COMMENT '结束时间',
  `bill_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '账单类型',
  `bill_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '账单状态',
  `order_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '订单类型',
  `total_order` int(11) NOT NULL DEFAULT '0' COMMENT '订单数量',
  `bill_amount` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '账单金额',
  `taking_amount` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '揽件费',
  `delivery_amount` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '派费',
  `kpi_sanction` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT 'KPI奖罚',
  `bill_sanction` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '结算奖罚',
  `qc_sanction` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '品控奖罚',
  `other_sanction` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '其它奖罚',
  `create_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:01' COMMENT '创建时间',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `gen_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '账单生成方式',
  PRIMARY KEY (`bill_id`),
  KEY `idx_period_date` (`period_date`),
  KEY `idx_deliveryid` (`deliveryid`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='派费账单';

-- ----------------------------
-- Table structure for fn_df_confirm_rate
-- ----------------------------
CREATE TABLE `fn_df_confirm_rate` (
  `confirm_rate_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'confirm_rate_id',
  `org_id` bigint(20) DEFAULT NULL COMMENT '站点id',
  `org_type` int(11) DEFAULT NULL COMMENT '站点性质 0-直营 1-直营二级 2-直营三级 3-加盟 4-加盟二级 5-加盟三级',
  `period_id` bigint(20) DEFAULT '0' COMMENT '账期',
  `start_time` datetime DEFAULT NULL COMMENT '起始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `bill_id` bigint(20) DEFAULT '0' COMMENT '账单',
  `total_order` bigint(11) DEFAULT '0' COMMENT '总单量',
  `total_confirm` bigint(11) DEFAULT '0' COMMENT '妥投单量',
  `confirm_rate` decimal(8,2) DEFAULT '0.00' COMMENT '妥投率',
  `cr_base` decimal(8,2) DEFAULT '0.00' COMMENT '妥投率保底值',
  `cr_expect` decimal(8,2) DEFAULT '0.00' COMMENT '妥投率期望值',
  `kpi_result` int(11) DEFAULT NULL COMMENT 'KPI结果',
  `sanction_amount` decimal(18,2) DEFAULT NULL COMMENT '奖罚总额',
  `sanction_price` decimal(8,2) DEFAULT NULL COMMENT '奖罚单价',
  `create_user` varchar(50) DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deliver_id` bigint(20) DEFAULT NULL COMMENT '小件员id',
  `month` varchar(10) DEFAULT NULL COMMENT '月份',
  `charger_type` tinyint(2) DEFAULT NULL COMMENT '结算对象，0-站点，1-协议',
  PRIMARY KEY (`confirm_rate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='妥投率报表';

-- ----------------------------
-- Table structure for fn_df_confirm_rate_detail
-- ----------------------------
CREATE TABLE `fn_df_confirm_rate_detail` (
  `detail_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `confirm_rate_id` bigint(20) NOT NULL COMMENT '妥投率报表ID',
  `order_no` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '订单号',
  `order_status` tinyint(4) DEFAULT NULL COMMENT '订单状态',
  `order_type` tinyint(4) DEFAULT NULL COMMENT '订单类型',
  `flowordertype` int(10) DEFAULT NULL COMMENT '订单操作状态',
  `deliverybranchid` int(11) DEFAULT NULL COMMENT ' 配送站点',
  `arrive_time` datetime DEFAULT NULL COMMENT '到站时间',
  `arrive_userid` int(11) DEFAULT NULL COMMENT '到站扫描人',
  `deliverystate` int(2) DEFAULT NULL COMMENT '反馈结果',
  `sign_userid` int(11) DEFAULT NULL COMMENT '反馈人',
  `sign_time` datetime DEFAULT NULL COMMENT '反馈时间',
  `auditing_userid` int(11) DEFAULT NULL COMMENT '审核人',
  `auditingtime` datetime DEFAULT NULL COMMENT '审核时间',
  `create_user` varchar(50) DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='妥投率报表明细';

-- ----------------------------
-- Table structure for fn_df_fee_adjustment_org
-- ----------------------------
CREATE TABLE `fn_df_fee_adjustment_org` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(100) NOT NULL COMMENT '订单号',
  `transcwb` mediumtext NOT NULL COMMENT '运单号',
  `cwbordertypeid` varchar(11) DEFAULT NULL COMMENT '订单类型',
  `customerid` bigint(20) DEFAULT NULL COMMENT '发货客户',
  `sendcarnum` int(11) DEFAULT NULL COMMENT '发货件数',
  `backcarnum` int(11) DEFAULT NULL COMMENT '取货数量',
  `senderaddress` varchar(200) DEFAULT NULL COMMENT '发货地址',
  `consigneeaddress` varchar(200) DEFAULT NULL COMMENT '收件人地址',
  `realweight` decimal(18,3) DEFAULT NULL COMMENT '实际重量',
  `cargovolume` decimal(19,4) DEFAULT NULL COMMENT '实际体积',
  `charge_type` int(11) DEFAULT NULL COMMENT '费用类型:0-揽件费, 1-派费',
  `adjust_amount` decimal(18,2) DEFAULT NULL COMMENT '揽件费(派费)',
  `aver_price` decimal(18,2) DEFAULT NULL COMMENT '均价',
  `level_averprice` decimal(18,2) DEFAULT NULL COMMENT '阶梯均价',
  `range_averprice` decimal(18,2) DEFAULT NULL COMMENT '范围均价',
  `range_addprice` decimal(18,2) DEFAULT NULL COMMENT '范围续价',
  `addprice` decimal(18,2) DEFAULT NULL COMMENT '续价',
  `overarea_sub` decimal(18,2) DEFAULT NULL COMMENT '超区补贴',
  `multiorder_sub` decimal(18,2) DEFAULT NULL COMMENT '一票多件补贴',
  `hugeorder_sub` decimal(18,2) DEFAULT NULL COMMENT '大件补贴',
  `cod_sub` decimal(18,2) DEFAULT NULL COMMENT '代收货款补贴',
  `others_sub` decimal(18,2) DEFAULT NULL COMMENT '其他补贴',
  `deliver_id` bigint(20) DEFAULT NULL COMMENT '小件员名称',
  `deliver_username` varchar(100) DEFAULT NULL COMMENT '小件员登陆名',
  `deliverybranchid` int(11) DEFAULT NULL COMMENT '操作站点',
  `cwbstate` int(4) DEFAULT NULL COMMENT '订单状态',
  `flowordertype` int(10) DEFAULT NULL COMMENT '订单操作状态',
  `create_time` datetime DEFAULT NULL COMMENT '导入数据时间',
  `outstationdatetime` datetime DEFAULT NULL COMMENT '揽件出站时间',
  `deliverystate` int(2) DEFAULT NULL COMMENT '反馈结果',
  `emaildate` datetime DEFAULT NULL COMMENT '发货时间',
  `credate` datetime DEFAULT NULL COMMENT '站点到货时间',
  `mobilepodtime` datetime DEFAULT NULL COMMENT '归班反馈时间',
  `auditingtime` datetime DEFAULT NULL COMMENT '归班审核时间',
  `is_billed` tinyint(2) DEFAULT NULL COMMENT '结算状态',
  `agt_ids` varchar(200) DEFAULT NULL COMMENT '计费协议编号(多个协议用逗号隔开)',
  `rule_ids` varchar(200) DEFAULT NULL COMMENT '规则编号(多个规则用逗号隔开展示)',
  `bill_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '账单id',
  `bill_no` varchar(32) DEFAULT NULL COMMENT '账单编号',
  `paybackfee` decimal(18,2) DEFAULT NULL COMMENT '应退款',
  `receivablefee` decimal(18,2) DEFAULT NULL COMMENT '代收款',
  `adjustment_create_time` datetime DEFAULT NULL,
  `adjustment_create_user` varchar(50) DEFAULT NULL,
  `adjustment_update_time` datetime DEFAULT NULL,
  `adjustment_update_user` varchar(50) DEFAULT NULL,
  `pick_time` datetime DEFAULT NULL COMMENT '领货时间',
  `apply_userid` bigint(20) DEFAULT NULL COMMENT '重置反馈申请人id',
  `edit_time` varchar(50) DEFAULT NULL COMMENT '重置反馈通过时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='站点派费调整表';

-- ----------------------------
-- Table structure for fn_df_fee_adjustment_staff
-- ----------------------------
CREATE TABLE `fn_df_fee_adjustment_staff` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(100) NOT NULL COMMENT '订单号',
  `transcwb` mediumtext NOT NULL COMMENT '运单号',
  `cwbordertypeid` varchar(11) DEFAULT NULL COMMENT '订单类型',
  `customerid` bigint(20) DEFAULT NULL COMMENT '发货客户',
  `sendcarnum` int(11) DEFAULT NULL COMMENT '发货件数',
  `backcarnum` int(11) DEFAULT NULL COMMENT '取货数量',
  `senderaddress` varchar(200) DEFAULT NULL COMMENT '发货地址',
  `consigneeaddress` varchar(200) DEFAULT NULL COMMENT '收件人地址',
  `realweight` decimal(18,3) DEFAULT NULL COMMENT '实际重量',
  `cargovolume` decimal(19,4) DEFAULT NULL COMMENT '实际体积',
  `charge_type` int(11) DEFAULT NULL COMMENT '费用类型:0-揽件费, 1-派费',
  `adjust_amount` decimal(18,2) DEFAULT NULL COMMENT '揽件费(派费)',
  `aver_price` decimal(18,2) DEFAULT NULL COMMENT '均价',
  `level_averprice` decimal(18,2) DEFAULT NULL COMMENT '阶梯均价',
  `range_averprice` decimal(18,2) DEFAULT NULL COMMENT '范围均价',
  `range_addprice` decimal(18,2) DEFAULT NULL COMMENT '范围续价',
  `addprice` decimal(18,2) DEFAULT NULL COMMENT '续价',
  `overarea_sub` decimal(18,2) DEFAULT NULL COMMENT '超区补贴',
  `multiorder_sub` decimal(18,2) DEFAULT NULL COMMENT '一票多件补贴',
  `hugeorder_sub` decimal(18,2) DEFAULT NULL COMMENT '大件补贴',
  `cod_sub` decimal(18,2) DEFAULT NULL COMMENT '代收货款补贴',
  `others_sub` decimal(18,2) DEFAULT NULL COMMENT '其他补贴',
  `deliver_id` bigint(20) DEFAULT NULL COMMENT '小件员名称',
  `deliver_username` varchar(100) DEFAULT NULL COMMENT '小件员登陆名',
  `deliverybranchid` int(11) DEFAULT NULL COMMENT '操作站点',
  `cwbstate` int(4) DEFAULT NULL COMMENT '订单状态',
  `flowordertype` int(10) DEFAULT NULL COMMENT '订单操作状态',
  `create_time` datetime DEFAULT NULL COMMENT '导入数据时间',
  `outstationdatetime` datetime DEFAULT NULL COMMENT '揽件出站时间',
  `deliverystate` int(2) DEFAULT NULL COMMENT '反馈结果',
  `emaildate` datetime DEFAULT NULL COMMENT '发货时间',
  `credate` datetime DEFAULT NULL COMMENT '站点到货时间',
  `mobilepodtime` datetime DEFAULT NULL COMMENT '归班反馈时间',
  `auditingtime` datetime DEFAULT NULL COMMENT '归班审核时间',
  `is_billed` tinyint(2) DEFAULT NULL COMMENT '结算状态',
  `agt_ids` varchar(200) DEFAULT NULL COMMENT '计费协议编号(多个协议用逗号隔开)',
  `rule_ids` varchar(200) DEFAULT NULL COMMENT '规则编号(多个规则用逗号隔开展示)',
  `bill_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '账单id',
  `bill_no` varchar(32) DEFAULT NULL COMMENT '账单编号',
  `paybackfee` decimal(18,2) DEFAULT NULL COMMENT '应退款',
  `receivablefee` decimal(18,2) DEFAULT NULL COMMENT '代收款',
  `adjustment_create_time` datetime DEFAULT NULL,
  `adjustment_create_user` varchar(50) DEFAULT NULL,
  `adjustment_update_time` datetime DEFAULT NULL,
  `adjustment_update_user` varchar(50) DEFAULT NULL,
  `pick_time` datetime DEFAULT NULL COMMENT '领货时间',
  `apply_userid` bigint(20) DEFAULT NULL COMMENT '重置反馈申请人id',
  `edit_time` varchar(50) DEFAULT NULL COMMENT '重置反馈通过时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='小件员派费调整表';

-- ----------------------------
-- Table structure for fn_df_fee_org
-- ----------------------------
CREATE TABLE `fn_df_fee_org` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(100) NOT NULL COMMENT '订单号',
  `transcwb` mediumtext NOT NULL COMMENT '运单号',
  `cwbordertypeid` varchar(11) DEFAULT NULL COMMENT '订单类型',
  `customerid` bigint(20) DEFAULT NULL COMMENT '发货客户',
  `sendcarnum` int(11) DEFAULT NULL COMMENT '发货件数',
  `backcarnum` int(11) DEFAULT NULL COMMENT '取货数量',
  `senderaddress` varchar(200) DEFAULT NULL COMMENT '发货地址',
  `consigneeaddress` varchar(200) DEFAULT NULL COMMENT '收件人地址',
  `realweight` decimal(18,3) DEFAULT NULL COMMENT '实际重量',
  `cargovolume` decimal(19,4) DEFAULT NULL COMMENT '实际体积',
  `charge_type` int(11) DEFAULT NULL COMMENT '费用类型:0-揽件费, 1-派费',
  `fee_amount` decimal(18,2) DEFAULT NULL COMMENT '揽件费(派费)',
  `aver_price` decimal(18,2) DEFAULT NULL COMMENT '均价',
  `level_averprice` decimal(18,2) DEFAULT NULL COMMENT '阶梯均价',
  `range_averprice` decimal(18,2) DEFAULT NULL COMMENT '范围均价',
  `range_addprice` decimal(18,2) DEFAULT NULL COMMENT '范围续价',
  `addprice` decimal(18,2) DEFAULT NULL COMMENT '续价',
  `overarea_sub` decimal(18,2) DEFAULT NULL COMMENT '超区补贴',
  `multiorder_sub` decimal(18,2) DEFAULT NULL COMMENT '一票多件补贴',
  `hugeorder_sub` decimal(18,2) DEFAULT NULL COMMENT '大件补贴',
  `cod_sub` decimal(18,2) DEFAULT NULL COMMENT '代收货款补贴',
  `others_sub` decimal(18,2) DEFAULT NULL COMMENT '其他补贴',
  `final_subsidy` decimal(18,2) DEFAULT NULL COMMENT '最终补贴额',
  `deliver_id` bigint(20) DEFAULT NULL COMMENT '小件员名称',
  `deliver_username` varchar(100) DEFAULT NULL COMMENT '小件员登陆名',
  `deliverybranchid` int(11) DEFAULT NULL COMMENT '操作站点',
  `cwbstate` int(4) DEFAULT NULL COMMENT '订单状态',
  `flowordertype` int(10) DEFAULT NULL COMMENT '订单操作状态',
  `create_time` datetime DEFAULT NULL COMMENT '导入数据时间',
  `outstationdatetime` datetime DEFAULT NULL COMMENT '揽件出站时间',
  `deliverystate` int(2) DEFAULT NULL COMMENT '反馈结果',
  `emaildate` datetime DEFAULT NULL COMMENT '发货时间',
  `credate` datetime DEFAULT NULL COMMENT '站点到货时间',
  `pick_time` datetime DEFAULT NULL COMMENT '领货时间',
  `mobilepodtime` datetime DEFAULT NULL COMMENT '归班反馈时间',
  `auditingtime` datetime DEFAULT NULL COMMENT '归班审核时间',
  `is_calculted` tinyint(2) DEFAULT '0' COMMENT '计费状态, 0未计算， 1已计算',
  `is_billed` tinyint(2) DEFAULT '0' COMMENT '结算状态, 0未计算， 1已计算',
  `agt_ids` varchar(200) DEFAULT NULL COMMENT '计费协议编号(多个协议用逗号隔开)',
  `rule_ids` varchar(200) DEFAULT NULL COMMENT '规则编号(多个规则用逗号隔开展示)',
  `bill_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '账单id',
  `bill_no` varchar(32) DEFAULT NULL COMMENT '账单编号',
  `cwbprovince` varchar(50) DEFAULT NULL,
  `cwbcity` varchar(50) DEFAULT NULL,
  `cwbcounty` varchar(50) DEFAULT NULL,
  `paybackfee` decimal(18,2) DEFAULT NULL COMMENT '应退款',
  `receivablefee` decimal(18,2) DEFAULT NULL COMMENT '代收款',
  `cartype` varchar(50) DEFAULT NULL COMMENT '货物类型',
  `fee_create_time` datetime DEFAULT NULL,
  `fee_create_user` varchar(50) DEFAULT NULL,
  `fee_update_time` datetime DEFAULT NULL,
  `fee_update_user` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fee_org_deliverybranchid_idx` (`deliverybranchid`),
  KEY `fee_org_charge_type_idx` (`charge_type`),
  KEY `fee_org_cwbordertypeid_idx` (`cwbordertypeid`),
  KEY `fee_org_customerid_idx` (`customerid`),
  KEY `fee_org_outstationdatetime_idx` (`outstationdatetime`),
  KEY `fee_org_fee_create_time_idx` (`fee_create_time`),
  KEY `fee_org_order_no_idx` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='站点派费明细表';

-- ----------------------------
-- Table structure for fn_df_fee_staff
-- ----------------------------
CREATE TABLE `fn_df_fee_staff` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(100) NOT NULL COMMENT '订单号',
  `transcwb` mediumtext NOT NULL COMMENT '运单号',
  `cwbordertypeid` varchar(11) DEFAULT NULL COMMENT '订单类型',
  `customerid` bigint(20) DEFAULT NULL COMMENT '发货客户',
  `sendcarnum` int(11) DEFAULT NULL COMMENT '发货件数',
  `backcarnum` int(11) DEFAULT NULL COMMENT '取件数量',
  `senderaddress` varchar(200) DEFAULT NULL COMMENT '发货地址',
  `consigneeaddress` varchar(200) DEFAULT NULL COMMENT '收件人地址',
  `realweight` decimal(18,3) DEFAULT NULL COMMENT '实际重量',
  `cargovolume` decimal(19,4) DEFAULT NULL COMMENT '实际体积',
  `charge_type` int(11) DEFAULT NULL COMMENT '费用类型:0-揽件费, 1-派费',
  `fee_amount` decimal(18,2) DEFAULT NULL COMMENT '揽件费(派费)',
  `aver_price` decimal(18,2) DEFAULT NULL COMMENT '均价',
  `level_averprice` decimal(18,2) DEFAULT NULL COMMENT '阶梯均价',
  `range_averprice` decimal(18,2) DEFAULT NULL COMMENT '范围均价',
  `range_addprice` decimal(18,2) DEFAULT NULL COMMENT '范围续价',
  `addprice` decimal(18,2) DEFAULT NULL COMMENT '续价',
  `overarea_sub` decimal(18,2) DEFAULT NULL COMMENT '超区补贴',
  `multiorder_sub` decimal(18,2) DEFAULT NULL COMMENT '一票多件补贴',
  `hugeorder_sub` decimal(18,2) DEFAULT NULL COMMENT '大件补贴',
  `cod_sub` decimal(18,2) DEFAULT NULL COMMENT '代收货款补贴',
  `others_sub` decimal(18,2) DEFAULT NULL COMMENT '其他补贴',
  `final_subsidy` decimal(18,2) DEFAULT NULL COMMENT '最终补贴额',
  `deliver_id` bigint(20) DEFAULT NULL COMMENT '小件员名称',
  `deliver_username` varchar(100) DEFAULT NULL COMMENT '小件员登陆名',
  `deliverybranchid` int(11) DEFAULT NULL COMMENT '操作站点',
  `cwbstate` int(4) DEFAULT NULL COMMENT '订单状态',
  `flowordertype` int(10) DEFAULT NULL COMMENT '订单操作状态',
  `create_time` datetime DEFAULT NULL COMMENT '导入数据时间',
  `outstationdatetime` datetime DEFAULT NULL COMMENT '揽件出站时间',
  `deliverystate` int(2) DEFAULT NULL COMMENT '反馈结果',
  `emaildate` datetime DEFAULT NULL COMMENT '发货时间',
  `credate` datetime DEFAULT NULL COMMENT '站点到货时间',
  `pick_time` datetime DEFAULT NULL COMMENT '领货时间',
  `mobilepodtime` datetime DEFAULT NULL COMMENT '归班反馈时间',
  `auditingtime` datetime DEFAULT NULL COMMENT '归班审核时间',
  `is_calculted` tinyint(2) DEFAULT '0' COMMENT '计费状态, 0未计算， 1已计算',
  `is_billed` tinyint(2) DEFAULT '0' COMMENT '结算状态, 0未计算， 1已计算',
  `agt_ids` varchar(200) DEFAULT NULL COMMENT '计费协议编号(多个协议用逗号隔开)',
  `rule_ids` varchar(200) DEFAULT NULL COMMENT '规则编号(多个规则用逗号隔开展示)',
  `bill_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '站点id',
  `bill_no` varchar(32) DEFAULT NULL COMMENT '账单编号',
  `cwbprovince` varchar(50) DEFAULT NULL,
  `cwbcity` varchar(50) DEFAULT NULL,
  `cwbcounty` varchar(50) DEFAULT NULL,
  `paybackfee` decimal(18,2) DEFAULT NULL COMMENT '应退款',
  `receivablefee` decimal(18,2) DEFAULT NULL COMMENT '代收款',
  `cartype` varchar(50) DEFAULT NULL COMMENT '货物类型',
  `fee_create_time` datetime DEFAULT NULL,
  `fee_create_user` varchar(50) DEFAULT NULL,
  `fee_update_time` datetime DEFAULT NULL,
  `fee_update_user` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fee_staff_deliverybranchid_idx` (`deliverybranchid`),
  KEY `fee_staff_charge_type_idx` (`charge_type`),
  KEY `fee_staff_cwbordertypeid_idx` (`cwbordertypeid`),
  KEY `fee_staff_customerid_idx` (`customerid`),
  KEY `fee_staff_outstationdatetime_idx` (`outstationdatetime`),
  KEY `fee_staff_fee_create_time_idx` (`fee_create_time`),
  KEY `fee_staff_order_no_idx` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='小件员派费明细表';

-- ----------------------------
-- Table structure for fn_df_rule
-- ----------------------------
CREATE TABLE `fn_df_rule` (
  `rule_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rule_no` varchar(32) DEFAULT NULL COMMENT '规则编号',
  `rule_type` tinyint(1) DEFAULT NULL COMMENT '规则类型',
  `charge_type` tinyint(1) DEFAULT NULL COMMENT '费用类型:0-揽件费, 1-派费',
  `order_type` varchar(50) DEFAULT NULL COMMENT '订单类型',
  `rule_desc` varchar(512) DEFAULT NULL COMMENT '规则内容',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `agt_id` bigint(20) DEFAULT NULL COMMENT '协议编号ID',
  `subsidy_flag` tinyint(1) DEFAULT NULL COMMENT '是否有补贴，0是有,1为没有',
  `cust_flag` tinyint(1) DEFAULT NULL COMMENT '是否区分客户，0是，1否',
  `area_flag` tinyint(1) DEFAULT NULL COMMENT '是否区分地区，0是，1否',
  PRIMARY KEY (`rule_id`),
  KEY `ruleNoIdx` (`rule_no`),
  KEY `agtIdIdx` (`agt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='派费规则';

-- ----------------------------
-- Table structure for fn_df_rule_add
-- ----------------------------
CREATE TABLE `fn_df_rule_add` (
  `rule_add_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rule_id` bigint(20) DEFAULT NULL COMMENT '规则编号ID',
  `price_type` tinyint(1) DEFAULT NULL COMMENT '价格类型(1续价)',
  `init_price` decimal(18,2) DEFAULT NULL COMMENT '起步价',
  `init_unit` tinyint(4) DEFAULT NULL COMMENT '起步单位(1 KG)',
  `init_num` decimal(18,3) DEFAULT NULL COMMENT '起步量',
  `additional_price` decimal(18,2) DEFAULT NULL COMMENT '续价',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`rule_add_id`),
  KEY `ruleIdIdx` (`rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='派费续价';

-- ----------------------------
-- Table structure for fn_df_rule_area
-- ----------------------------
CREATE TABLE `fn_df_rule_area` (
  `rule_area_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rule_id` bigint(20) DEFAULT NULL COMMENT '规则编码',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `region` varchar(50) DEFAULT NULL COMMENT '区县',
  `town` varchar(50) DEFAULT NULL COMMENT '镇/街道',
  `province` varchar(50) DEFAULT NULL COMMENT '省份',
  `province_code` varchar(50) DEFAULT NULL COMMENT '省份代码',
  `city_code` varchar(50) DEFAULT NULL COMMENT '城市代码',
  `region_code` varchar(50) DEFAULT NULL COMMENT '区县代码',
  `town_code` varchar(50) DEFAULT NULL COMMENT '镇/街道代码',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`rule_area_id`),
  KEY `ruleIdIdx` (`rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='派费地区';

-- ----------------------------
-- Table structure for fn_df_rule_avg
-- ----------------------------
CREATE TABLE `fn_df_rule_avg` (
  `rule_avg_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rule_id` bigint(20) DEFAULT NULL COMMENT '规则编号ID',
  `start_val` decimal(18,3) DEFAULT NULL COMMENT '重量起始值(阶梯均价时才有值)',
  `end_val` decimal(18,3) DEFAULT NULL COMMENT '重量结束值(阶梯均价时才有值 ，为空表示无穷大)',
  `unit` tinyint(4) DEFAULT NULL COMMENT '续价单位(1元/票 2元/KG)',
  `price_type` tinyint(4) DEFAULT NULL COMMENT '价格类型(1均价 2阶梯均价)',
  `price` decimal(18,2) DEFAULT NULL COMMENT '阶梯价',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`rule_avg_id`),
  KEY `ruleIdIdx` (`rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='派费均价';

-- ----------------------------
-- Table structure for fn_df_rule_cust
-- ----------------------------
CREATE TABLE `fn_df_rule_cust` (
  `rule_id` bigint(20) NOT NULL COMMENT '主键ID',
  `cust_id` bigint(20) NOT NULL COMMENT '适用客户',
  KEY `ruleIdIdx` (`rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='派费客户';

-- ----------------------------
-- Table structure for fn_df_rule_range
-- ----------------------------
CREATE TABLE `fn_df_rule_range` (
  `rule_range_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rule_id` bigint(20) DEFAULT NULL COMMENT '规则编号ID',
  `price_type` tinyint(1) DEFAULT NULL COMMENT '价格类型(1 范围均价 2 范围续价)',
  `start_val` int(11) DEFAULT NULL COMMENT '起始值',
  `end_val` int(11) DEFAULT NULL COMMENT '结束值(为空表示无穷大)''',
  `range_unit` tinyint(4) DEFAULT NULL COMMENT '范围单位(1票2件)',
  `init_price` decimal(18,2) unsigned DEFAULT NULL COMMENT '起步价(范围均价填0)',
  `init_unit` tinyint(4) DEFAULT NULL COMMENT '起步单位(固定填1)',
  `init_num` decimal(18,3) DEFAULT NULL COMMENT '起步量(范围均价填0)',
  `additional_price` decimal(18,2) unsigned DEFAULT NULL COMMENT '范围均价或范围阶梯价的续价',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`rule_range_id`),
  KEY `ruleIdIdx` (`rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='派费范围价';

-- ----------------------------
-- Table structure for fn_df_rule_subsidy
-- ----------------------------
CREATE TABLE `fn_df_rule_subsidy` (
  `subsidy_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rule_id` bigint(20) DEFAULT NULL COMMENT '规则编码',
  `item` tinyint(4) DEFAULT NULL COMMENT '补贴项',
  `price` decimal(18,2) DEFAULT NULL COMMENT '补贴金额',
  `price_unit` tinyint(4) DEFAULT NULL COMMENT '补贴单位',
  `range_num` int(11) DEFAULT '0' COMMENT '补贴范围',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`subsidy_id`),
  KEY `ruleIdIdx` (`rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='派费补贴';

-- ----------------------------
-- Table structure for fn_df_sanction
-- ----------------------------
CREATE TABLE `fn_df_sanction` (
  `sanction_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `period_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '账期id',
  `bill_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '账单id',
  `discipline_no` varchar(50) NOT NULL DEFAULT '' COMMENT '奖罚单号',
  `org_id` bigint(20) NOT NULL COMMENT '站点id',
  `sanction_type` tinyint(4) NOT NULL COMMENT '奖罚类型',
  `sanction_amount` decimal(18,2) DEFAULT NULL COMMENT '罚款总额',
  `sanction_price` decimal(18,2) DEFAULT NULL COMMENT '罚款单价',
  `sanction_order` int(11) DEFAULT NULL COMMENT '罚款单量',
  `reward_amount` decimal(18,2) DEFAULT NULL COMMENT '奖励总额',
  `reward_price` decimal(18,2) DEFAULT NULL COMMENT '奖励单价',
  `reward_order` int(11) DEFAULT NULL COMMENT '奖励单量',
  `verify_state` tinyint(4) NOT NULL DEFAULT '0' COMMENT '奖罚单状态。默认为0.待生成账单',
  `sanction_reason` varchar(250) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '0' COMMENT '奖罚原因',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `discipline_year_month` varchar(10) DEFAULT NULL COMMENT '奖罚月份',
  `deliver_id` bigint(20) DEFAULT NULL COMMENT '小件员id',
  `charger_type` tinyint(2) DEFAULT NULL COMMENT '结算对象，0-站点，1-协议',
  PRIMARY KEY (`sanction_id`),
  KEY `index_discipline_credate` (`create_time`),
  KEY `index_discipline_org_id` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='派费奖罚记录';

-- ----------------------------
-- Table structure for fn_df_agreement_lock
-- ----------------------------
CREATE TABLE `fn_df_agreement_lock` (
   `id` int(11) NOT NULL COMMENT '主键ID',
   `remark` varchar(50) NULL COMMENT '备注',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='协议并发锁';

INSERT INTO `fn_df_agreement_lock` (`id`, `remark`) values('1',NULL);
