-- 尽量不要再把字段rollback回较小的长度
alter table express_ops_cwb_error modify column message varchar(500) COLLATE utf8_bin DEFAULT '';