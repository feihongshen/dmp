drop table `fn_brance_report_detail` ;
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
   `order_from_type` int COMMENT '订单来源：0-应付当期，1-应收当期 ，2-POS抵扣当期，3-当期丢失，4-当期退货' ,
   `create_time` timestamp COMMENT '创建时间' ,
   `is_archive` int DEFAULT 0 COMMENT '是否归档 0-未归档 1-已归档' ,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;