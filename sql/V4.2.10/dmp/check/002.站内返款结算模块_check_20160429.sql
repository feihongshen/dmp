SELECT '创建表fn_rpt_station_sign_person' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables WHERE table_name = 'fn_rpt_station_sign_person') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '新增菜单' AS '脚本注释',IF((
(SELECT COUNT(1) FROM dmp40_function WHERE functionname='缴款导入')+
(SELECT COUNT(1) FROM dmp40_function WHERE functionname='缴款导入管理')+
(SELECT COUNT(1) FROM dmp40_function WHERE functionname='预付款管理')+
(SELECT COUNT(1) FROM dmp40_function WHERE functionname='缴款账户余额查询')+
(SELECT COUNT(1) FROM dmp40_function WHERE functionname='签收小件员余额报表')
)>= 5 ,'success','failed') AS '执行结果'
UNION ALL
SELECT '修改表express_set_branch' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.columns WHERE table_name = 'express_set_branch' AND column_name='payin_type' AND data_type = 'TINYINT') = 1,'success','failed') AS '执行结果'
UNION ALL
SELECT 'express_set_user' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.statistics WHERE table_name = 'express_set_user' AND index_name='idx_username') = 1,'success','failed') AS '执行结果'
UNION ALL
SELECT '修改表fn_order_recharge' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.columns WHERE table_name = 'fn_order_recharge' AND column_name='pay_detail_id' AND data_type = 'BIGINT') = 1,'success','failed') AS '执行结果'
UNION ALL
SELECT '修改表fn_org_recharges' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.columns WHERE table_name = 'fn_org_recharges' AND column_name IN('picker','bill_no','bill_detail_id','bill_type','payin_type','recharge_no','picker_id')) = 7,'success','failed') AS '执行结果'
UNION ALL
SELECT '修改表fn_pay_detail' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.columns WHERE table_name = 'fn_pay_detail' AND column_name IN('creator') AND data_type = 'varchar') = 1,'success','failed') AS '执行结果'
UNION ALL
SELECT '修改表fn_pay_platform_interface' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.columns WHERE table_name = 'fn_pay_platform_interface' AND column_name IN('creator') AND data_type = 'varchar') = 1,'success','failed') AS '执行结果'
