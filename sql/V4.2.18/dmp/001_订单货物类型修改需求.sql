-- 创建表,用于存放货物类型修改申请的数据
CREATE TABLE  express_ops_applyeditcartype  (
   id	  				bigint(11) 		NOT NULL AUTO_INCREMENT  COMMENT 'id,自增',
   cwb	 				varchar(50) 	DEFAULT '' 				 COMMENT '订单号',
   transcwb	 			varchar(50) 	DEFAULT '' 				 COMMENT '运单号',
   customerid  			int(11) 								 COMMENT '客户ID',
   customername  		varchar(100)	DEFAULT ''				 COMMENT '客户名称',
   do_type  			int(4) 									 COMMENT '订单类型',
   original_cartype  	varchar(50) 							 COMMENT '货物类型(修改前)',
   apply_cartype  		varchar(50) 							 COMMENT '货物类型(修改后)',
   carrealweight  		decimal(18,3)   DEFAULT 0.000			 COMMENT '货物重量',
   carsize  			varchar(500) 							 COMMENT '货物尺寸',
   apply_branchid 		int(10)									 COMMENT '申请机构Id',
   apply_branchname 	varchar(50)								 COMMENT '申请机构名称',
   apply_userid  		int(11)									 COMMENT '申请者ID',
   apply_username  		varchar(20)								 COMMENT '申请者姓名',
   apply_time  			varchar(50)								 COMMENT '申请日期',
   review_userid  		int(11)									 COMMENT '审核者ID',
   review_username  	varchar(20)								 COMMENT '审核者姓名',
   review_time  		varchar(50)								 COMMENT '审核日期',
   review_status  		int(2) 			DEFAULT 0			 	 COMMENT '审核状态,0.未处理1.审核通过2.审核不通过',
   remark  		  		varchar(100)	DEFAULT ''				 COMMENT '备注',
   PRIMARY KEY (id),
   INDEX applyeditcartype_cwb_idx (cwb),
   INDEX applyeditcartype_apply_branchid_idx (apply_branchid),
   INDEX applyeditcartype_apply_userid_idx (apply_userid),
   INDEX applyeditcartype_apply_time_idx (apply_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 添加新页面菜单
insert into dmp40_function(ID,functionlevel,functionname,functionorder,functionurl,parentfunctionid)
values (602566,2,'订/运单货物类型修改',602566,'applyediteditcartype/toapply?',6025);
insert into dmp40_function(ID,functionlevel,functionname,functionorder,functionurl,parentfunctionid)
values (602567,2,'订/运单货物类型审核',602567,'applyediteditcartype/reviewlist?',6025);
