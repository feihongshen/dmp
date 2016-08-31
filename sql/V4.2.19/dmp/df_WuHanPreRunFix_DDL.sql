CREATE TABLE `fn_df_confirm_rate_subsidy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `rule_id` bigint(20) DEFAULT NULL COMMENT '规则id',
  `org_id` bigint(20) DEFAULT NULL COMMENT '站点id',
  `period_id` bigint(20) DEFAULT NULL COMMENT '账期',
  `total_order` bigint(11) DEFAULT NULL COMMENT '总接单量',
  `total_confirm` bigint(11) DEFAULT NULL COMMENT '妥投单量',
  `confirm_rate` decimal(8,2) DEFAULT NULL COMMENT '妥投率',
  `award_price` decimal(8,2) DEFAULT NULL COMMENT '奖罚单价',
  `award_amount` decimal(18,2) DEFAULT NULL COMMENT '补贴单价',
  `deliver_id` bigint(20) DEFAULT NULL COMMENT '小件员id',
  `month` varchar(10) DEFAULT NULL COMMENT '月份',
  `start_time` datetime DEFAULT NULL COMMENT '起始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `create_user` varchar(50) DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='妥投率补贴报表';

CREATE TABLE `fn_df_confirm_rate_subsidy_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `confirm_rate_subsidy_id` bigint(20) DEFAULT NULL COMMENT '妥投率补贴报表id',
  `order_no` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '订单号',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '发货客户',
  `org_id` bigint(20) DEFAULT NULL COMMENT '站点id',
  `deliver_id` bigint(20) DEFAULT NULL COMMENT '小件员id',
  `order_type` tinyint(4) DEFAULT NULL COMMENT '订单类型',
  `emaildate` datetime DEFAULT NULL COMMENT '发货时间',
  `credate` datetime DEFAULT NULL COMMENT '站点到货时间',
  `mobilepodtime` datetime DEFAULT NULL COMMENT '归班反馈时间',
  `auditingtime` datetime DEFAULT NULL COMMENT '归班审核时间',
  `consigneeaddress` varchar(200) DEFAULT NULL COMMENT '收件人地址',
  `is_confirmed` tinyint(1) DEFAULT NULL COMMENT '是否妥投 0否，1是',
  `create_user` varchar(50) DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='妥投率补贴明细';


ALTER TABLE `fn_df_bill_staff` ADD COLUMN `confirm_rate_subsidy_sanction` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '妥投率补贴奖罚';
ALTER TABLE `fn_df_bill_org` ADD COLUMN `confirm_rate_subsidy_sanction` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '妥投率补贴奖罚';

alter table `fn_df_rule_subsidy`
ADD COLUMN  `start_val` decimal(18,3) NULL COMMENT '妥投率补贴范围起始值',
ADD COLUMN  `end_val` decimal(18,3) NULL COMMENT '妥投率补贴范围结束值';

ALTER TABLE `fn_df_agreement` 
ADD COLUMN  `agt_fee_type` TINYINT(1) DEFAULT '0' NULL COMMENT '协议费用类型,0是价格，1是补贴';
