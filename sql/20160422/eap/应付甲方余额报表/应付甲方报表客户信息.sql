drop table `fn_cust_pay_report_cfg` ;
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
 