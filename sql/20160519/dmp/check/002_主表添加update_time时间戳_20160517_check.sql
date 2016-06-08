<<<<<<< HEAD
desc express_ops_cwb_detail 'credate';
desc express_ops_cwb_detail like 'update_time';
=======
(SELECT '主表添加update_time时间戳' AS '脚本文件名','credate字段修改成默认值为0000-00-00 00:00:00' AS '修改内容',if ((select COUNT(1)  from INFORMATION_SCHEMA.`COLUMNS` where TABLE_SCHEMA = schema() and TABLE_NAME = 'express_ops_cwb_detail' and COLUMN_NAME = 'credate' and column_default='0000-00-00 00:00:00')=1,'success','failed') AS '执行结果')
UNION
(SELECT '主表添加update_time时间戳' AS '脚本文件名','添加update_time字段,默认值为CURRENT_TIMESTAMP' AS '修改内容',IF((SELECT COUNT(1) FROM information_schema.`COLUMNS` WHERE table_schema = schema() and table_name = 'express_ops_cwb_detail'  and column_name = 'update_time' and column_default='CURRENT_TIMESTAMP') = 1,'success','failed') AS '执行结果');
<<<<<<< HEAD
>>>>>>> refs/remotes/origin/develop
=======
>>>>>>> refs/heads/xuziqiang_1611
>>>>>>> refs/remotes/origin/master
