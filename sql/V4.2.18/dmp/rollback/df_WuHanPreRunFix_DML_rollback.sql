DELETE FROM `dmp40_function` WHERE ID = '809113' AND functionname = '妥投考核报表';

DELETE FROM dmp40_function WHERE ID = '809111' and functionname = '新增补贴协议';
DELETE FROM dmp40_function WHERE ID = '809112' and functionname = '补贴协议管理';
update dmp40_function set  functionurl='${eapUrl}deliveryfeeagreement.do?add&' where ID='809101' and functionname='新增派费协议';
update dmp40_function set  functionurl='${eapUrl}deliveryfeeagreement.do?manager&' where ID='809102' and functionname='派费协议管理';
