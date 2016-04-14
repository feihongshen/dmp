-- drop table `fn_brance_report_adjust` ;
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