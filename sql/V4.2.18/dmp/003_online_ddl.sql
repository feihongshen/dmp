-- 修改订单主表的货物类型字段，默认值改为'普件'
alter table express_ops_cwb_detail alter column cartype set DEFAULT '普件';