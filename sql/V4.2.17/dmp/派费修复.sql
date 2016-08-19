--add indexes for fn_df_fee_org
ALTER TABLE `fn_df_fee_org` ADD INDEX `fee_org_deliver_id_idx` (`deliver_id`);
ALTER TABLE `fn_df_fee_org` ADD INDEX `fee_org_emaildate_idx` (`emaildate`);
ALTER TABLE `fn_df_fee_org` ADD INDEX `fee_org_credate_idx` (`credate`);
ALTER TABLE `fn_df_fee_org` ADD INDEX `fee_org_mobilepodtime_idx` (`mobilepodtime`);
ALTER TABLE `fn_df_fee_org` ADD INDEX `fee_org_auditingtime_idx` (`auditingtime`);
--add indexes for fn_df_fee_staff
ALTER TABLE `fn_df_fee_staff` ADD INDEX `fee_staff_deliver_id_idx` (`deliver_id`);
ALTER TABLE `fn_df_fee_staff` ADD INDEX `fee_staff_emaildate_idx` (`emaildate`);
ALTER TABLE `fn_df_fee_staff` ADD INDEX `fee_staff_credate_idx` (`credate`);
ALTER TABLE `fn_df_fee_staff` ADD INDEX `fee_staff_mobilepodtime_idx` (`mobilepodtime`);
ALTER TABLE `fn_df_fee_staff` ADD INDEX `fee_staff_auditingtime_idx` (`auditingtime`);
--add indexes for fn_df_confirm_rate
ALTER TABLE `fn_df_confirm_rate` ADD INDEX `confirm_rate_deliver_id_idx` (`deliver_id`);
ALTER TABLE `fn_df_confirm_rate` ADD INDEX `confirm_rate_org_id_idx` (`org_id`);
ALTER TABLE `fn_df_confirm_rate` ADD INDEX `confirm_rate_month_idx` (`month`);
ALTER TABLE `fn_df_confirm_rate` ADD INDEX `confirm_rate_kpi_result_idx` (`kpi_result`);
ALTER TABLE `fn_df_confirm_rate` ADD INDEX `confirm_rate_charger_type_idx` (`charger_type`);
--add indexes for fn_df_confirm_rate_detail
ALTER TABLE `fn_df_confirm_rate_detail` ADD INDEX `detail_confirm_rate_id_idx` (`confirm_rate_id`);
--add indexes for fn_df_fee_adjustment_org
ALTER TABLE `fn_df_fee_adjustment_org` ADD INDEX `adjustment_org_order_no_idx` (`order_no`);
ALTER TABLE `fn_df_fee_adjustment_org` ADD INDEX `adjustment_org_charge_type_idx` (`charge_type`);
ALTER TABLE `fn_df_fee_adjustment_org` ADD INDEX `adjustment_org_deliverybranchid_idx` (`deliverybranchid`);
ALTER TABLE `fn_df_fee_adjustment_org` ADD INDEX `adjustment_org_deliver_id_idx` (`deliver_id`);
ALTER TABLE `fn_df_fee_adjustment_org` ADD INDEX `adjustment_org_customerid_idx` (`customerid`);
ALTER TABLE `fn_df_fee_adjustment_org` ADD INDEX `adjustment_org_emaildate_idx` (`emaildate`);
ALTER TABLE `fn_df_fee_adjustment_org` ADD INDEX `adjustment_org_credate_idx` (`credate`);
ALTER TABLE `fn_df_fee_adjustment_org` ADD INDEX `adjustment_org_pick_time_idx` (`pick_time`);
ALTER TABLE `fn_df_fee_adjustment_org` ADD INDEX `adjustment_org_mobilepodtime_idx` (`mobilepodtime`);
ALTER TABLE `fn_df_fee_adjustment_org` ADD INDEX `adjustment_org_auditingtime_idx` (`auditingtime`);
--add indexes for fn_df_fee_adjustment_staff
ALTER TABLE `fn_df_fee_adjustment_staff` ADD INDEX `adjustment_staff_order_no_idx` (`order_no`);
ALTER TABLE `fn_df_fee_adjustment_staff` ADD INDEX `adjustment_staff_charge_type_idx` (`charge_type`);
ALTER TABLE `fn_df_fee_adjustment_staff` ADD INDEX `adjustment_staff_deliverybranchid_idx` (`deliverybranchid`);
ALTER TABLE `fn_df_fee_adjustment_staff` ADD INDEX `adjustment_staff_deliver_id_idx` (`deliver_id`);
ALTER TABLE `fn_df_fee_adjustment_staff` ADD INDEX `adjustment_staff_customerid_idx` (`customerid`);
ALTER TABLE `fn_df_fee_adjustment_staff` ADD INDEX `adjustment_staff_emaildate_idx` (`emaildate`);
ALTER TABLE `fn_df_fee_adjustment_staff` ADD INDEX `adjustment_staff_credate_idx` (`credate`);
ALTER TABLE `fn_df_fee_adjustment_staff` ADD INDEX `adjustment_staff_pick_time_idx` (`pick_time`);
ALTER TABLE `fn_df_fee_adjustment_staff` ADD INDEX `adjustment_staff_mobilepodtime_idx` (`mobilepodtime`);
ALTER TABLE `fn_df_fee_adjustment_staff` ADD INDEX `adjustment_staff_auditingtime_idx` (`auditingtime`);