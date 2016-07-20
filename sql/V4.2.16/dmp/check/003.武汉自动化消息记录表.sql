select '003.入库自动化消息记录表' as '脚本注释', if(
(SELECT count(1) from information_schema.`TABLES`
where TABLE_SCHEMA = SCHEMA() and TABLE_NAME='express_ops_auto_intowarehouse_message'
)=1, 'success', 'failed') as '执行结果' ;