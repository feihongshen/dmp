-- 清空人员管理中打印收件人电话手机设置
update express_set_user set showphoneflag = 0, showmobileflag = 0;

-- 在导出字段可选列表中删除收件人电话手机
delete from express_ops_setexportfield where id in (8, 9);

-- 在导出字段已选列表中删除收件人电话手机
update express_ops_exportmould SET mouldfieldids=REPLACE(mouldfieldids, ',8,', ',');
update express_ops_exportmould SET mouldfieldids=REPLACE(mouldfieldids, ',9,', ',');