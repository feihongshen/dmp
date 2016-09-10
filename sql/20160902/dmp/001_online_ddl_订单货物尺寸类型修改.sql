-- 添加货物尺寸类型字段
ALTER TABLE express_ops_cwb_detail ADD goods_size_type int(2) DEFAULT 0 COMMENT '货物尺寸类型：0.普件,1.大件' ;