select '003.入库自动化消息记录表' as '脚本注释', if(
(SELECT count(1) from information_schema.`TABLES`
where TABLE_SCHEMA = SCHEMA() and TABLE_NAME='express_ops_auto_intowarehouse_message'
)=1, 'success', 'failed') as '执行结果' 
UNION
select '003.武汉自动化连接中间件等候返回状态时间间隔（毫秒）' as '脚本注释', if(
(SELECT count(1) from express_set_system_install
where name = 'autoAllocatingConnecteWaitIntervalInMs'
)=1, 'success', 'failed') as '执行结果' 
;