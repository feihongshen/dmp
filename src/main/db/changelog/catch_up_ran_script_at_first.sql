call proc_add_column_if_not_exists('fn_order_recharge', 'record_bill_id', 'ALTER TABLE `fn_order_recharge` ADD COLUMN `record_bill_id` bigint(20) DEFAULT NULL;');
