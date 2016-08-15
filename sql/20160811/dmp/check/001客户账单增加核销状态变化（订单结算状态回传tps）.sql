select '001客户账单增加核销状态变化（订单结算状态回传tps）' as '执行脚本', if(
(SELECT count(1) from information_schema.`COLUMNS`
where TABLE_SCHEMA = SCHEMA() and TABLE_NAME='fn_customer_bill' and COLUMN_NAME in ('state_change') 
)=1, 'success', 'failed') as '执行结果' ;