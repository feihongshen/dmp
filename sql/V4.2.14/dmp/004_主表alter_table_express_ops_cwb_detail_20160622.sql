alter table express_ops_cwb_detail 
add column delivery_permit int(2) DEFAULT '0' COMMENT '上门退是否可领货标识：0 可领，1不可领',
ADD COLUMN `do_type`  int(4) COMMENT '订单类型',
ADD COLUMN `order_source` int(11) DEFAULT '0' COMMENT '订单客户类型：是否外单';
