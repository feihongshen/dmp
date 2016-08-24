alter table `fn_df_rule_subsidy` 
ADD COLUMN  `start_val` decimal(18,3) NULL COMMENT '妥投率补贴范围起始值',
ADD COLUMN  `end_val` decimal(18,3) NULL COMMENT '妥投率补贴范围结束值';

insert into `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) values('809111','2','新增补贴协议','809111','${eapUrl}deliveryfeeagreement.do?add&fromPage=subsidy&','8091');
insert into `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) values('809112','2','补贴协议管理','809112','${eapUrl}deliveryfeeagreement.do?manager&fromPage=subsidy&','8091');
update dmp40_function set  functionurl='${eapUrl}deliveryfeeagreement.do?add&fromPage=price&' where ID='809101' and functionname='新增派费协议';
update dmp40_function set  functionurl='${eapUrl}deliveryfeeagreement.do?manager&fromPage=price&' where ID='809102' and functionname='派费协议管理';
