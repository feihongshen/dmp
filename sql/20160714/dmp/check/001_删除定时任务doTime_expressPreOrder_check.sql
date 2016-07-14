select '001_删除定时任务doTime_expressPreOrder_check' as '脚本注释', if(
(SELECT COUNT(1) from inf_q_blob_triggers a,inf_q_triggers b
where a.trigger_name = 'doTime_expressPreOrder' or b.trigger_name = 'doTime_expressPreOrder'
)=0, 'success', 'failed') as '执行结果' ;

