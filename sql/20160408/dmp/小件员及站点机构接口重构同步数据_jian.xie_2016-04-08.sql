-- 同步站点机构的数据到接口表
insert INTO express_set_branch_inf( branchid
, branchname
, tpsbranchcode
, branchprovince
, branchcity
, brancharea
, create_date
, create_user
, `status`)
SELECT branchid
, branchname
, tpsbranchcode
, CASE when branchprovince='' then '***' ELSE branchprovince end 
, case when branchcity='' then '***' else branchcity end
, case when brancharea='' then '***' else brancharea end
, NOW()
, '脚本创建'
, case when brancheffectflag = 1 then 0 else 1 end
from express_set_branch
where sitetype=2;


-- 同步小件员信息到小件员接口表
insert into express_set_user_inf(
	userid
, username
, realname
, usermobile
, `password`
,	`status`
, create_date
, create_user
, branchid
)
SELECT userid
, username
, realname
, usermobile
, `password`
, case when employeestatus = 3 then 1 else 0 end
, NOW()
, '脚本创建'
, branchid
from express_set_user
where (roleid=2 
or roleid=4)
and employeestatus != 3;
