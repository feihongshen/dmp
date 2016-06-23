alter table express_ops_cwb_detail 
add column delivery_permit int(2) DEFAULT '0' COMMENT '上门退是否可领货标识：0 可领，1不可领';
