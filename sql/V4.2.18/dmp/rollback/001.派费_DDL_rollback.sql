ALTER TABLE fn_df_rule_subsidy DROP COLUMN start_val,DROP COLUMN end_val;

DELETE FROM dmp40_function WHERE ID = '809111' and functionname = '新增补贴协议';
DELETE FROM dmp40_function WHERE ID = '809112' and functionname = '补贴协议管理';
update dmp40_function set  functionurl='${eapUrl}deliveryfeeagreement.do?add&' where ID='809101' and functionname='新增派费协议';
update dmp40_function set  functionurl='${eapUrl}deliveryfeeagreement.do?manager&' where ID='809102' and functionname='派费协议管理';
