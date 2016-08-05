--drop indexes from fn_df_fee_org
DROP INDEX `fee_org_deliver_id_idx` ON `fn_df_fee_org`;
DROP INDEX `fee_org_emaildate_idx` ON `fn_df_fee_org`;
DROP INDEX `fee_org_credate_idx` ON `fn_df_fee_org`;
DROP INDEX `fee_org_mobilepodtime_idx` ON `fn_df_fee_org`;
DROP INDEX `fee_org_auditingtime_idx` ON `fn_df_fee_org`;
--drop indexes from fn_df_fee_staff
DROP INDEX `fee_staff_deliver_id_idx` ON `fn_df_fee_staff`;
DROP INDEX `fee_staff_emaildate_idx` ON `fn_df_fee_staff`;
DROP INDEX `fee_staff_credate_idx` ON `fn_df_fee_staff`;
DROP INDEX `fee_staff_mobilepodtime_idx` ON `fn_df_fee_staff`;
DROP INDEX `fee_staff_auditingtime_idx` ON `fn_df_fee_staff`;
--drop indexes from fn_df_confirm_rate
DROP INDEX `confirm_rate_deliver_id_idx` ON `fn_df_confirm_rate`;
DROP INDEX `confirm_rate_org_id_idx` ON `fn_df_confirm_rate`;
DROP INDEX `confirm_rate_month_idx` ON `fn_df_confirm_rate`;
DROP INDEX `confirm_rate_kpi_result_idx` ON `fn_df_confirm_rate`;
DROP INDEX `confirm_rate_charger_type_idx` ON `fn_df_confirm_rate`;
--drop indexes from fn_df_confirm_rate_detail
DROP INDEX `detail_confirm_rate_id_idx` ON `fn_df_confirm_rate_detail`;