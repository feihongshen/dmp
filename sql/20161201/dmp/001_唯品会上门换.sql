alter table `express_ops_cwb_detail` 
add column `exchange_flag` int(2) DEFAULT '0' NULL COMMENT '上门换时换货标志：0不是换货，1是换货', 
add column `exchange_cwb` varchar(100) NULL COMMENT '上门换时关联的配送或上门退订单号';

alter table `express_ops_cwb_detail_b2ctemp` 
add column `exchange_flag` int(2) DEFAULT '0' NULL COMMENT '上门换时换货标志：0不是换货，1是换货', 
add column `exchange_cwb` varchar(100) NULL COMMENT '上门换时关联的配送或上门退订单号';