alter table express_ops_cwb_detail 
add column cnor_corp_no varchar(100) NOT NULL DEFAULT '' COMMENT '�ļ���λ����',
add column cnor_corp_name varchar(100) NOT NULL DEFAULT '' COMMENT '�ļ���λ����', 
add column freight decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT 'Ӧ���˷�',
add column account_id varchar(100) NOT NULL DEFAULT '' COMMENT '�½��˺�',
add column packing_fee decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '��װ��',
add column express_image varchar(100) NOT NULL DEFAULT '' COMMENT 'ͼƬurl',
add column cnee_corp_name varchar(50) NOT NULL DEFAULT '' COMMENT '�ջ���Ʒ����';