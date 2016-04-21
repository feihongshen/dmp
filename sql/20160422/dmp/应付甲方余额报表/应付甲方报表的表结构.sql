 drop table `fn_brance_report` ;
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