drop table `fn_brance_report_detail`;

drop table `fn_brance_report_adjust`;
 
drop table `fn_brance_report`;
 
drop table `fn_cust_pay_remit_detail`;
 
drop table `fn_cust_pay_report_cfg`;
 

delete from `dmp40_function` where `ID` =  '802060' and `functionname`= '客户结算信息管理';
delete from `dmp40_function` where `ID` =  '802061' and `functionname`= '实付客户记录管理';
delete from `dmp40_function` where `ID` =  '802062' and `functionname`= '应付甲方余额报表';