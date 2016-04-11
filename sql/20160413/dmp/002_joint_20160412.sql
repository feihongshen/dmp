-- 前提此查询结果为空：SELECT joint_num, COUNT(1) FROM express_set_joint GROUP BY joint_num HAVING COUNT(1) > 1;
ALTER TABLE express_set_joint ADD UNIQUE joint_num_unique(joint_num);