
-- ----------------------------
-- Table structure for `fn_df_adjustment_record`
-- ----------------------------
CREATE TABLE `fn_df_adjustment_record` (
  `adj_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` varchar(30) DEFAULT NULL COMMENT '订单号',
  `bill_no` varchar(30) NOT NULL COMMENT '账单编号',
  `adjust_bill_no` varchar(30) DEFAULT NULL COMMENT '调整账单编号',
  `org_id` bigint(10) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT '0' COMMENT '客户ID',
  `adjust_amount` decimal(19,2) DEFAULT '0.00' COMMENT '调整差额',
  `creator` varchar(30) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `adj_state` int(11) DEFAULT '0' COMMENT '是否已经生成调整账单标识',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `order_type` int(11) DEFAULT NULL COMMENT '订单类型',
  `bill_id` bigint(20) DEFAULT NULL COMMENT '账单ID',
  PRIMARY KEY (`adj_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='派费调整记录表';

-- ----------------------------
-- Table structure for `fn_df_agreement`
-- ----------------------------
CREATE TABLE `fn_df_agreement` (
  `agt_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `agt_no` varchar(32) DEFAULT NULL COMMENT '协议编号',
  `org_id` bigint(20) DEFAULT NULL COMMENT '站点',
  `agt_start` datetime DEFAULT NULL COMMENT '协议生效时间',
  `agt_end` datetime DEFAULT NULL COMMENT '协议终止时间',
  `suspend_time` datetime DEFAULT NULL COMMENT '停用时间',
  `suspend_user` varchar(32) DEFAULT NULL COMMENT '停用人',
  `agt_state` tinyint(4) DEFAULT NULL COMMENT '协议状态',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`agt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='派费协议';

-- ----------------------------
-- Table structure for `fn_df_bill`
-- ----------------------------
CREATE TABLE `fn_df_bill` (
  `bill_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '账单编号',
  `org_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '站点',
  `period_id` bigint(10) NOT NULL DEFAULT '0' COMMENT '对应的账期',
  `period_date` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '账单生成月份',
  `start_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:01' COMMENT '起始时间',
  `end_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:01' COMMENT '结束时间',
  `bill_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '账单类型',
  `bill_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '账单状态',
  `order_type` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '订单类型',
  `total_order` int(11) NOT NULL DEFAULT '0' COMMENT '订单数量',
  `bill_amount` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '账单金额',
  `delivery_amount` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '派费',
  `kpi_sanction` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT 'KPI奖罚',
  `bill_sanction` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '结算奖罚',
  `qc_sanction` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '品控奖罚',
  `other_sanction` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '其它奖罚',
  `create_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:01' COMMENT '创建时间',
  `create_user` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '创建人',
  `gen_type` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '账单生成方式',
  PRIMARY KEY (`bill_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='派费账单';

-- ----------------------------
-- Table structure for `fn_df_bill_detail`
-- ----------------------------
CREATE TABLE `fn_df_bill_detail` (
  `detail_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(128) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '订单号',
  `bill_id` bigint(10) NOT NULL DEFAULT '0' COMMENT '对应的账单主键',
  `order_status` tinyint(4) NOT NULL COMMENT '订单状态',
  `order_type` tinyint(4) NOT NULL COMMENT '订单类型',
  `site` varchar(128) COLLATE utf8_bin NOT NULL COMMENT '订单做归班反馈的站点',
  `delivery_num` int(10) NOT NULL COMMENT '发货件数',
  `volume` decimal(18,3) NOT NULL DEFAULT '0.000' COMMENT '订单体积',
  `weight` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '订单重量',
  `user_address` varchar(128) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '收件人地址',
  `delivery_amount` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '按派费协议计算出的订单派费',
  `insite_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:01' COMMENT '订单做站点到货的操作时间',
  `auditing_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:01' COMMENT '订单做归班审核的操作时间',
  `iseffect` tinyint(4) NOT NULL DEFAULT '1' COMMENT '标记此订单是否有效（该账单是否被删除）1账单有效，0账单已被删除',
  PRIMARY KEY (`detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='派费账单明细';

-- ----------------------------
-- Table structure for `fn_df_bill_period`
-- ----------------------------
CREATE TABLE `fn_df_bill_period` (
  `period_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `org_id` bigint(20) NOT NULL COMMENT '站点',
  `period_state` tinyint(4) NOT NULL COMMENT '账期状态',
  `base_day` tinyint(4) NOT NULL COMMENT '账期每月结束日',
  `period_start` datetime NOT NULL DEFAULT '2000-01-01 00:00:01' COMMENT '生效时间',
  `period_end` datetime NOT NULL DEFAULT '2000-01-01 00:00:01' COMMENT '终止时间',
  `cr_base` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '妥投率保底值',
  `cr_expect` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '妥投率期望值',
  `cr_reward_price` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '奖励单价',
  `cr_punish_price` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '惩罚单价',
  `cr_reward_limit` decimal(18,2) DEFAULT NULL COMMENT '奖励上限',
  `cr_punish_limit` decimal(18,2) DEFAULT NULL COMMENT '惩罚上限',
  `period_desc` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '账期描述',
  `remark` varchar(256) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:01' COMMENT '创建时间',
  `create_user` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:01' COMMENT '修改时间',
  `update_user` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '修改人',
  `abolish_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:01' COMMENT '废除时间',
  `abolish_user` varchar(32) COLLATE utf8_bin DEFAULT '' COMMENT '废除操作人',
  PRIMARY KEY (`period_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='派费账期';

-- ----------------------------
-- Table structure for `fn_df_confirm_rate`
-- ----------------------------
CREATE TABLE `fn_df_confirm_rate` (
  `confirm_rate_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'confirm_rate_id',
  `org_id` bigint(20) NOT NULL COMMENT '站点id',
  `org_type` int(11) NOT NULL COMMENT '站点性质 0-直营 1-直营二级 2-直营三级 3-加盟 4-加盟二级 5-加盟三级',
  `period_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '账期',
  `start_time` datetime DEFAULT NULL COMMENT '起始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `bill_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '账单',
  `total_order` bigint(11) NOT NULL DEFAULT '0' COMMENT '总单量',
  `total_confirm` bigint(11) NOT NULL DEFAULT '0' COMMENT '妥投单量',
  `confirm_rate` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '妥投率',
  `cr_base` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '妥投率保底值',
  `cr_expect` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '妥投率期望值',
  `kpi_result` int(11) NOT NULL COMMENT 'KPI结果',
  `sanction_amount` decimal(18,2) NOT NULL COMMENT '奖罚总额',
  `sanction_price` decimal(8,2) NOT NULL COMMENT '奖罚单价',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`confirm_rate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='妥投率报表';


-- ----------------------------
-- Table structure for `fn_df_confirm_rate_detail`
-- ----------------------------
CREATE TABLE `fn_df_confirm_rate_detail` (
  `detail_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `confirm_rate_id` bigint(20) NOT NULL COMMENT '妥投率报表ID',
  `order_no` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '订单号',
  `order_status` tinyint(4) NOT NULL COMMENT '订单状态',
  `order_type` tinyint(4) NOT NULL COMMENT '订单类型',
  `flowordertype` tinyint(4) NOT NULL COMMENT '订单操作状态',
  `deliverybranchid` int(11) NOT NULL COMMENT ' 配送站点',
  `arrive_time` datetime DEFAULT NULL COMMENT '到站时间',
  `arrive_userid` int(11) DEFAULT NULL COMMENT '到站扫描人',
  `deliverystate` int(2) DEFAULT NULL COMMENT '反馈结果',
  `sign_userid` int(11) DEFAULT NULL COMMENT '反馈人',
  `sign_time` datetime DEFAULT NULL COMMENT '反馈时间',
  `auditing_userid` int(11) DEFAULT NULL COMMENT '审核人',
  `auditingtime` datetime DEFAULT NULL COMMENT '审核时间',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='妥投率报表明细';

-- ----------------------------
-- Table structure for `fn_df_rule`
-- ----------------------------
CREATE TABLE `fn_df_rule` (
  `rule_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `agt_no` varchar(32) DEFAULT NULL COMMENT '协议编号',
  `rule_no` varchar(32) DEFAULT NULL COMMENT '规则编号',
  `rule_type` tinyint(4) DEFAULT NULL COMMENT '规则类型',
  `order_type` varchar(16) DEFAULT NULL COMMENT '订单类型',
  `biz_type` tinyint(4) DEFAULT NULL COMMENT '业务类型',
  `avg_price` decimal(18,2) DEFAULT NULL COMMENT '均价',
  `avg_unit` tinyint(4) DEFAULT NULL COMMENT '均价单位',
  `base_price` decimal(18,2) DEFAULT NULL COMMENT '保底价',
  `cust_flag` tinyint(4) DEFAULT NULL COMMENT '区分客户',
  `cust_ids` varchar(1024) DEFAULT NULL COMMENT '适用客户',
  `other_cust_flag` tinyint(4) DEFAULT NULL COMMENT '其它客户',
  `area_flag` tinyint(4) DEFAULT NULL COMMENT '区分地区',
  `other_area_flag` tinyint(4) DEFAULT NULL COMMENT '其它地区',
  `rule_desc` varchar(512) DEFAULT NULL COMMENT '规则内容',
  `range_type` tinyint(4) DEFAULT NULL COMMENT '范围价类型 1、范围均价 2、范围阶梯价',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`rule_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='派费规则';

-- ----------------------------
-- Table structure for `fn_df_rule_area`
-- ----------------------------
CREATE TABLE `fn_df_rule_area` (
  `rule_area_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rule_no` varchar(16) DEFAULT NULL COMMENT '规则编码',
  `city` varchar(16) DEFAULT NULL COMMENT '城市',
  `region` varchar(16) DEFAULT NULL COMMENT '区县',
  `town` varchar(16) DEFAULT NULL COMMENT '镇/街道',
  `province` varchar(16) DEFAULT NULL COMMENT '省份',
  `province_code` varchar(16) DEFAULT NULL COMMENT '省份代码',
  `city_code` varchar(16) DEFAULT NULL COMMENT '城市代码',
  `region_code` varchar(16) DEFAULT NULL COMMENT '区县代码',
  `town_code` varchar(16) DEFAULT NULL COMMENT '镇/街道代码',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`rule_area_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='派费地区';

-- ----------------------------
-- Table structure for `fn_df_rule_range`
-- ----------------------------
CREATE TABLE `fn_df_rule_range` (
  `rule_range_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rule_no` varchar(32) DEFAULT NULL COMMENT '规则编码',
  `start_val` decimal(18,3) DEFAULT NULL COMMENT '起始值',
  `end_val` decimal(18,3) DEFAULT NULL COMMENT '结束值',
  `range_unit` tinyint(4) DEFAULT NULL COMMENT '范围单位',
  `init_price` decimal(18,2) DEFAULT NULL COMMENT '起步价',
  `init_unit` tinyint(4) DEFAULT NULL COMMENT '起步单位',
  `init_num` decimal(18,3) DEFAULT NULL COMMENT '起步量',
  `additional_price` decimal(18,2) DEFAULT NULL COMMENT '续价',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`rule_range_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='派费范围价';

-- ----------------------------
-- Table structure for `fn_df_rule_step`
-- ----------------------------
CREATE TABLE `fn_df_rule_step` (
  `rule_step_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rule_no` varchar(32) DEFAULT NULL COMMENT '规则编码',
  `start_val` decimal(18,3) DEFAULT NULL COMMENT '起始值',
  `end_val` decimal(18,3) DEFAULT NULL COMMENT '结束值',
  `step_unit` tinyint(4) DEFAULT NULL COMMENT '范围单位',
  `price_type` tinyint(4) DEFAULT NULL COMMENT '价格类型',
  `step_price` decimal(18,2) DEFAULT NULL COMMENT '阶梯价',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`rule_step_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='派费阶梯价';

-- ----------------------------
-- Table structure for `fn_df_rule_subsidy`
-- ----------------------------
CREATE TABLE `fn_df_rule_subsidy` (
  `subsidy_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rule_no` varchar(32) DEFAULT NULL COMMENT '规则编码',
  `item` tinyint(4) DEFAULT NULL COMMENT '补贴项',
  `price` decimal(18,2) DEFAULT NULL COMMENT '补贴金额',
  `price_unit` tinyint(4) DEFAULT NULL COMMENT '补贴单位',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`subsidy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='派费补贴';

-- ----------------------------
-- Table structure for `fn_df_sanction`
-- ----------------------------
CREATE TABLE `fn_df_sanction` (
  `sanction_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `period_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '账期id',
  `bill_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '账单id',
  `discipline_no` varchar(50) NOT NULL DEFAULT '' COMMENT '奖罚单号',
  `org_id` bigint(20) NOT NULL COMMENT '站点id',
  `sanction_type` tinyint(4) NOT NULL COMMENT '奖罚类型',
  `sanction_amount` decimal(18,2) NOT NULL COMMENT '奖罚总额',
  `sanction_price` decimal(18,2) DEFAULT NULL COMMENT '奖罚单价',
  `total_order` int(11) DEFAULT NULL COMMENT '奖罚单量',
  `verify_state` tinyint(4) NOT NULL DEFAULT '0' COMMENT '奖罚单状态。默认为0.待生成账单',
  `sanction_reason` varchar(250) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '奖罚原因',
  `create_user` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `discipline_year_month` varchar(10) DEFAULT NULL COMMENT '奖罚月份',
  PRIMARY KEY (`sanction_id`),
  KEY `index_discipline_credate` (`create_time`),
  KEY `index_discipline_org_id` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='派费奖罚记录';



INSERT INTO `dmp40_function` VALUES ('8091', 1, '加盟站点派费结算管理', '8091', '', '80');
INSERT INTO `dmp40_function` VALUES ('809110', 2, '新增派费协议', '809110', '${eapUrl}deliveryfeeagreement.do?add&', '8091');
INSERT INTO `dmp40_function` VALUES ('809120', 2, '派费协议管理', '809120', '${eapUrl}deliveryfeeagreement.do?manager&', '8091');
INSERT INTO `dmp40_function` VALUES ('809130', 2, '派费账期管理', '809130', '${eapUrl}dfBillPeriodController.do?index&', '8091');
INSERT INTO `dmp40_function` VALUES ('809140', 2, '派费奖罚管理', '809140', '${eapUrl}orgDisciplineRecord.do?index&', '8091');
INSERT INTO `dmp40_function` VALUES ('809150', 2, '派费账单管理', '809150', '${eapUrl}dfBillController.do?index&', '8091');
INSERT INTO `dmp40_function` VALUES ('809160', 2, '加盟站点月度妥投率报表', '809160', '${eapUrl}branchFeeManager.do?index&', '8091');


ALTER TABLE express_ops_cwb_detail ADD `hasdfbill` tinyint(1) DEFAULT '0' COMMENT '已生成加盟站派费账单（0否1是）';
