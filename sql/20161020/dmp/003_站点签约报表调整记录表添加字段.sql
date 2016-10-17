ALTER TABLE `fn_org_order_adjustment_record`
ADD COLUMN `inputdatetime`  datetime NULL COMMENT '揽件入站时间' ,
ADD COLUMN `express_settle_way`  int(20) COMMENT '快递单支付方式'  ;