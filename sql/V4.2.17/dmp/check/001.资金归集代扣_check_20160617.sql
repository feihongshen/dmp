SELECT '新增余额报表模式下的缴款导入表（fn_org_recharges_rptmode）' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = SCHEMA() AND table_name = 'fn_org_recharges_rptmode') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '站点缴款导入银行记录表（fn_org_bank_import）新增字段' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = SCHEMA() AND table_name = 'fn_org_bank_import' AND column_name IN('current_mode'))= 1,'success','failed') AS '执行结果'
UNION ALL
SELECT '新增余额报表订单明细冲抵记录表（fn_orgrpt_order_recharge）' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = SCHEMA() AND table_name = 'fn_orgrpt_order_recharge') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '新增回款调整记录表（fn_org_recharges_adjustment_record）' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = SCHEMA() AND table_name = 'fn_org_recharges_adjustment_record') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '站点签收订单快照表（fn_station_sign_order_details_snapshot） 新增表字段' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = SCHEMA() AND table_name = 'fn_station_sign_order_details_snapshot' AND column_name IN('shouldfareflag','receivablefeeflag','expressfreightflag','shouldfare_receipt','receivablefee_receipt','expressfreight_receipt'))= 6,'success','failed') AS '执行结果'
UNION ALL
SELECT '现付类型快递运费订单快照表（fn_station_sign_order_details_snapshot_express）新增表字段' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.columns WHERE table_schema = SCHEMA() AND table_name = 'fn_station_sign_order_details_snapshot_express' AND column_name IN('shouldfareflag','receivablefeeflag','expressfreightflag','shouldfare_receipt','receivablefee_receipt','expressfreight_receipt'))= 6,'success','failed') AS '执行结果'
UNION ALL
SELECT '新增站点/小件员结算账户信息表（fn_org_pay_account）' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = SCHEMA() AND table_name = 'fn_org_pay_account') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '新增站点/小件员结算账户信息修改记录表（fn_org_pay_account_modify_record）' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = SCHEMA() AND table_name = 'fn_org_pay_account_modify_record') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '新增站点/代扣请求接口表（fn_vpal_interface）' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = SCHEMA() AND table_name = 'fn_vpal_interface') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '新增站点/新增代扣记录表（fn_vpal_record）' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = SCHEMA() AND table_name = 'fn_vpal_record') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '新增代扣对账文件列表（fn_vpal_check_filelist）' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = SCHEMA() AND table_name = 'fn_vpal_check_filelist') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '新增订单财务结算状态表（fn_cwb_state）' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = SCHEMA() AND table_name = 'fn_cwb_state') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '新增各大银行信息字典表（fn_set_bank）' AS '脚本注释',IF((SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = SCHEMA() AND table_name = 'fn_set_bank') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '初始化各大银行信息字典表（fn_set_bank）' AS '脚本注释',IF((SELECT COUNT(1) FROM fn_set_bank) = 16,'success','failed') AS '执行结果'
UNION ALL
SELECT '新增菜单表记录' AS '脚本注释',IF((SELECT COUNT(1) FROM dmp40_function WHERE id='803091' AND functionname='对账文件查询') + (SELECT COUNT(1) FROM dmp40_function WHERE id='803092' AND functionname='结算信息管理') + (SELECT COUNT(1) FROM dmp40_function WHERE id='803093' AND functionname='代扣查询')= 3,'success','failed') AS '执行结果'
;