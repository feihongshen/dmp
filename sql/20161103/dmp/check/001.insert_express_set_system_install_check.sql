SELECT '001' AS '脚本注释',IF((select count(1) from express_set_system_install where name ='cwbOrderTypeIdNeedToTps') =1,'success','failed') AS '执行结果';