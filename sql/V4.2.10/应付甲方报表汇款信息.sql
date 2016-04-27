 drop table `fn_cust_pay_remit_detail` ;
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