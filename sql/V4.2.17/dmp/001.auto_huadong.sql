 create table `express_set_kufang_branch_map`( 
   `fromBranchId` int(11) NOT NULL COMMENT '分拣库id', 
   `toBranchId` int(11) NOT NULL COMMENT '配送站id', 
   KEY `kufang_branch_from_idx` (`fromBranchId`),
   KEY `kufang_branch_to_idx` (`toBranchId`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
 
 insert into `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) values('303050','2','二级分拣库流向','303050','kufangBranchMap/list?','3030');
