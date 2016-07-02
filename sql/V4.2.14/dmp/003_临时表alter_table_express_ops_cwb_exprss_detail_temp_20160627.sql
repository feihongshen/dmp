alter table express_ops_cwb_exprss_detail_temp 
add column return_credit decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '应退运费',
add column order_source int(11) NOT NULL default '0' comment '订单来源';
