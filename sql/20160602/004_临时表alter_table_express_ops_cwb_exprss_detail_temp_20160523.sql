alter table express_ops_cwb_exprss_detail_temp 
add column cnor_corp_no varchar(100) NOT NULL DEFAULT '' COMMENT '寄件单位编码',
add column cnor_corp_name varchar(100) NOT NULL DEFAULT '' COMMENT '寄件单位名称', 
add column freight decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '应收运费',
add column account_id varchar(100) NOT NULL DEFAULT '' COMMENT '月结账号',
add column packing_fee decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '包装费',
add column express_image varchar(100) NOT NULL DEFAULT '' COMMENT '图片url',
add column cnee_corp_name varchar(50) NOT NULL DEFAULT '' COMMENT '收货公司名称',
add column is_accept_prov int not null DEFAULT '0' COMMENT '0、派件省，1、揽件省',
add column express_produce_type int not null default '0' comment '快递产品类型';