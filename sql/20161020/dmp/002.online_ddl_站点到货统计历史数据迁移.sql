--上线前一天执行
INSERT IGNORE INTO express_ops_branch_daohuo(cwb,branchid,credate)
SELECT cwb,branchid,credate FROM express_ops_order_flow  WHERE credate<str_to_date('2016-10-19 00:00:00','%Y-%m-%d %H:%i:%s') AND flowordertype IN(7,8)
;

--发布后执行
INSERT IGNORE INTO express_ops_branch_daohuo(cwb,branchid,credate)
SELECT cwb,branchid,credate FROM express_ops_order_flow  WHERE credate>=str_to_date('2016-10-19 00:00:00','%Y-%m-%d %H:%i:%s') AND flowordertype IN(7,8)
;