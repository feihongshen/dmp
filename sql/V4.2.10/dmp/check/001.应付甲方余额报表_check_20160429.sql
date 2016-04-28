SELECT '新建应付甲方报表明细表结构' AS '脚本文件名',IF((SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = schema() and table_name = 'fn_brance_report_detail') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '新建应付调整单明细表' AS '脚本文件名',IF((SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = schema() and table_name = 'fn_brance_report_adjust') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '应付甲方报表的表结构' AS '脚本文件名',IF((SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = schema() and table_name = 'fn_brance_report') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '应付甲方报表汇款信息' AS '脚本文件名',IF((SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = schema() and table_name = 'fn_cust_pay_remit_detail') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '应付甲方报表客户信息' AS '脚本文件名',IF((SELECT COUNT(1) FROM information_schema.tables WHERE table_schema = schema() and table_name = 'fn_cust_pay_report_cfg') > 0,'success','failed') AS '执行结果'
UNION ALL
SELECT '应付甲方余额报表菜单' AS '脚本文件名',IF((
(SELECT COUNT(1) FROM dmp40_function WHERE ID = '802060' AND functionname='客户结算信息管理')+
(SELECT COUNT(1) FROM dmp40_function WHERE ID = '802061' AND functionname='实付客户记录管理')+
(SELECT COUNT(1) FROM dmp40_function WHERE ID = '802062' AND functionname='应付甲方余额报表')
)= 3 ,'success','failed') AS '执行结果'