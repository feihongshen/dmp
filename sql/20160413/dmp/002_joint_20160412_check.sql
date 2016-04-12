-- 结果为1
select count(1) cnt
from INFORMATION_SCHEMA.STATISTICS
where TABLE_SCHEMA = schema()
	and TABLE_NAME = 'express_set_joint'
	and INDEX_NAME = 'joint_num_unique'
	and COLUMN_NAME = 'joint_num';