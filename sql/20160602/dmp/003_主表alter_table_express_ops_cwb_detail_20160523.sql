alter table express_ops_cwb_detail 
add column cnor_corp_no varchar(100) NOT NULL DEFAULT '' COMMENT '寄件单位编码',
add column cnor_corp_name varchar(100) NOT NULL DEFAULT '' COMMENT '寄件单位名称', 
add column account_id varchar(100) NOT NULL DEFAULT '' COMMENT '月结账号',
add column express_image varchar(100) NOT NULL DEFAULT '' COMMENT '图片url',
add column cnee_corp_name varchar(50) NOT NULL DEFAULT '' COMMENT '收货公司名称',
add column express_product_type int not null default '0' comment '快递产品类型';