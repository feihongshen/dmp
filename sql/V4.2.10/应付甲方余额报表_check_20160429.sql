select * from `fn_brance_report_detail` where 1 = 2;	-- 没有报错

select * from `fn_brance_report_adjust` where 1 = 2;	-- 没有报错
 
select * from `fn_brance_report` where 1 = 2;	-- 没有报错
 
select * from `fn_cust_pay_remit_detail` where 1 = 2;	-- 没有报错
 
select * from `fn_cust_pay_report_cfg` where 1 = 2;	-- 没有报错
 

select * from `dmp40_function` where `ID` =  '802060' and `functionname`= '客户结算信息管理';	-- 有一条结果
select * from `dmp40_function` where `ID` =  '802061' and `functionname`= '实付客户记录管理';	-- 有一条结果
select * from `dmp40_function` where `ID` =  '802062' and `functionname`= '应付甲方余额报表';	-- 有一条结果