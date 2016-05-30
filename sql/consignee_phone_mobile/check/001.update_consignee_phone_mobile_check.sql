SELECT '在导出字段可选列表中删除收件人电话手机' AS '脚本注释',IF((
select count(1) from express_ops_setexportfield where id in (8, 9)
)= 0 ,'success','failed') AS '执行结果'
UNION ALL
SELECT '在导出字段已选列表中删除收件人电话手机' AS '脚本注释',IF((
select count(1) from  express_ops_exportmould 
where mouldfieldids like '%,8,%' or mouldfieldids like '%,9,%' or mouldfieldids like '8,%' or mouldfieldids like '9,%' or mouldfieldids like '%,8' or mouldfieldids like '%,9'
)= 0 ,'success','failed') AS '执行结果';
