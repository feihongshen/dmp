ALTER TABLE `fn_order_recharge`   
ADD COLUMN `pay_detail_id` BIGINT(20) DEFAULT 0  NULL  COMMENT '支付平台扣款记录id（fn_pay_detail)', 
ADD  INDEX `idx_pay_detail_id` (`pay_detail_id`);