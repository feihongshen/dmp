SELECT
  'check fee_org_deliver_id_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_org' AND INDEX_NAME = 'fee_org_deliver_id_idx') > 0, 'success', 'failed') AS '执行结果'
UNION ALL
SELECT
  'check fee_org_emaildate_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_org' AND INDEX_NAME = 'fee_org_emaildate_idx') > 0, 'success', 'failed') AS '执行结果'
UNION ALL
SELECT
  'check fee_org_credate_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_org' AND INDEX_NAME = 'fee_org_credate_idx') > 0, 'success', 'failed') AS '执行结果'
UNION ALL
SELECT
  'check fee_org_mobilepodtime_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_org' AND INDEX_NAME = 'fee_org_mobilepodtime_idx') > 0, 'success',
     'failed')                          AS '执行结果'
UNION ALL
SELECT
  'check fee_org_auditingtime_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_org' AND INDEX_NAME = 'fee_org_auditingtime_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check fee_staff_deliver_id_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_staff' AND INDEX_NAME = 'fee_staff_deliver_id_idx') > 0, 'success', 'failed') AS '执行结果'
UNION ALL
SELECT
  'check fee_staff_emaildate_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_staff' AND INDEX_NAME = 'fee_staff_emaildate_idx') > 0, 'success', 'failed') AS '执行结果'
UNION ALL
SELECT
  'check fee_staff_credate_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_staff' AND INDEX_NAME = 'fee_staff_credate_idx') > 0, 'success', 'failed') AS '执行结果'
UNION ALL
SELECT
  'check fee_staff_mobilepodtime_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_staff' AND INDEX_NAME = 'fee_staff_mobilepodtime_idx') > 0, 'success',
     'failed')                          AS '执行结果'
UNION ALL
SELECT
  'check fee_staff_auditingtime_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_staff' AND INDEX_NAME = 'fee_staff_auditingtime_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check confirm_rate_deliver_id_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_confirm_rate' AND INDEX_NAME = 'confirm_rate_deliver_id_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check confirm_rate_org_id_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_confirm_rate' AND INDEX_NAME = 'confirm_rate_org_id_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check confirm_rate_month_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_confirm_rate' AND INDEX_NAME = 'confirm_rate_month_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check confirm_rate_kpi_result_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_confirm_rate' AND INDEX_NAME = 'confirm_rate_kpi_result_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check confirm_rate_charger_type_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_confirm_rate' AND INDEX_NAME = 'confirm_rate_charger_type_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check detail_confirm_rate_id_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_confirm_rate_detail' AND INDEX_NAME = 'detail_confirm_rate_id_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check adjustment_org_order_no_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_adjustment_org' AND INDEX_NAME = 'adjustment_org_order_no_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check adjustment_org_charge_type_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_adjustment_org' AND INDEX_NAME = 'adjustment_org_charge_type_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check adjustment_org_deliverybranchid_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_adjustment_org' AND INDEX_NAME = 'adjustment_org_deliverybranchid_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check adjustment_org_deliver_id_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_adjustment_org' AND INDEX_NAME = 'adjustment_org_deliver_id_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check adjustment_org_customerid_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_adjustment_org' AND INDEX_NAME = 'adjustment_org_customerid_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check adjustment_org_emaildate_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_adjustment_org' AND INDEX_NAME = 'adjustment_org_emaildate_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check adjustment_org_credate_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_adjustment_org' AND INDEX_NAME = 'adjustment_org_credate_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check adjustment_org_pick_time_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_adjustment_org' AND INDEX_NAME = 'adjustment_org_pick_time_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check adjustment_org_mobilepodtime_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_adjustment_org' AND INDEX_NAME = 'adjustment_org_mobilepodtime_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check adjustment_org_auditingtime_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_adjustment_org' AND INDEX_NAME = 'adjustment_org_auditingtime_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check adjustment_staff_order_no_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_adjustment_staff' AND INDEX_NAME = 'adjustment_staff_order_no_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check adjustment_staff_charge_type_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_adjustment_staff' AND INDEX_NAME = 'adjustment_staff_charge_type_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check adjustment_staff_deliverybranchid_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_adjustment_staff' AND INDEX_NAME = 'adjustment_staff_deliverybranchid_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check adjustment_staff_deliver_id_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_adjustment_staff' AND INDEX_NAME = 'adjustment_staff_deliver_id_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check adjustment_staff_customerid_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_adjustment_staff' AND INDEX_NAME = 'adjustment_staff_customerid_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check adjustment_staff_emaildate_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_adjustment_staff' AND INDEX_NAME = 'adjustment_staff_emaildate_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check adjustment_staff_credate_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_adjustment_staff' AND INDEX_NAME = 'adjustment_staff_credate_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check adjustment_staff_pick_time_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_adjustment_staff' AND INDEX_NAME = 'adjustment_staff_pick_time_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check adjustment_staff_mobilepodtime_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_adjustment_staff' AND INDEX_NAME = 'adjustment_staff_mobilepodtime_idx') > 0, 'success',
     'failed')                         AS '执行结果'
UNION ALL
SELECT
  'check adjustment_staff_auditingtime_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_adjustment_staff' AND INDEX_NAME = 'adjustment_staff_auditingtime_idx') > 0, 'success',
     'failed')                         AS '执行结果';