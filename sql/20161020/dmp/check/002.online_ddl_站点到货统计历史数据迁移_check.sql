SELECT '站点到货统计历史数据迁移' AS '脚本注释',IF((SELECT COUNT(1) FROM express_ops_branch_daohuo limit 1) > 0,'success','failed') AS '执行结果'
;
