-- 修改导入异常表 message字段类型，防止长度大的字段保持不下
alter table express_ops_cwb_error modify column message text;