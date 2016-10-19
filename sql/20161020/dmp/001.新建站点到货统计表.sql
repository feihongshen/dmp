create table `express_ops_branch_daohuo`( 
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id', 
   `cwb` varchar(100) COMMENT '订单号', 
   `branchid` int(11) DEFAULT '0' COMMENT '站点id', 
   `credate` timestamp COMMENT '到货时间', 
   PRIMARY KEY (`id`),
   KEY `idx_branch_daohuo_cwb` (`cwb`),
   KEY `idx_branch_daohuo_branchid` (`branchid`),
   KEY `idx_branch_daohuo_date` (`credate`),
   UNIQUE KEY `idx_branch_daohuo_cwb_unique` (`cwb`,`branchid`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='站点到货记录表';
 