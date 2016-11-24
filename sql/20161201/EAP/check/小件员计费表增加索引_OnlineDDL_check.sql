SELECT
  'check fee_staff_is_calculted_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_staff' AND INDEX_NAME = 'fee_staff_is_calculted_idx') > 0, 'success', 'failed') AS '执行结果'
UNION ALL
SELECT
  'check fee_org_is_calculted_idx' AS '脚本注释',
  IF((SELECT COUNT(1)
      FROM INFORMATION_SCHEMA.STATISTICS
      WHERE TABLE_NAME = 'fn_df_fee_org' AND INDEX_NAME = 'fee_org_is_calculted_idx') > 0, 'success', 'failed') AS '执行结果';