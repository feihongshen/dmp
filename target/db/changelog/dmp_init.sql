/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


CREATE TABLE `express_ops_automatic_set` (
  `nowlinkname` varchar(50) COLLATE utf8_bin DEFAULT '',
  `nextlink` varchar(50) COLLATE utf8_bin DEFAULT '',
  `isauto` int(4) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert  into `express_ops_automatic_set`(`nowlinkname`,`nextlink`,`isauto`) values ('GetGoods','intoWarehous',0),('GetGoodsnoListIntoWarehous','intoWarehous',0),('IntoWarehous','outWarehouse',0),('NoListIntoWarehous','outWarehouse',0),('OutWarehouse','substationGoods',0),('SubstationGoods','receiveGoods',0),('SubstationGoodsNoList','receiveGoods',0),('ChangeGoodsOutwarehouse','changeIntoWarehous',0),('ReturnGoodsOutwarehouse','backIntoWarehous',0),('ChangeIntoWarehous','transBranchOutWarehouse',0),('ChangeNoListIntoWarehous','transBranchOutWarehouse',0),('BackIntoWarehous','backReturnOutWarehous',0),('BackNoListIntoWarehous','backReturnOutWarehous',0);

CREATE TABLE `express_ops_bale` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `baleno` varchar(50) COLLATE utf8_bin DEFAULT '',
  `balestate` int(4) DEFAULT '0',
  `branchid` int(11) DEFAULT '0',
  `groupid` int(4) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_cwb_detail` (
  `opscwbid` int(11) NOT NULL AUTO_INCREMENT,
  `cwb` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `emaildateid` int(11) DEFAULT '0',
  `emaildate` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `consigneeno` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `consigneename` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `consigneeaddress` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `consigneepostcode` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `consigneephone` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `consigneemobile` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `shiptime` varchar(100) COLLATE utf8_bin DEFAULT '2000-01-01 00:00:00',
  `shipcwb` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `cwbremark` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `serviceareaid` int(11) DEFAULT '0',
  `receivablefee` decimal(19,2) DEFAULT '0.00',
  `paybackfee` decimal(19,2) DEFAULT '0.00',
  `customerid` int(11) DEFAULT NULL,
  `exceldeliver` varchar(50) COLLATE utf8_bin DEFAULT '',
  `excelbranch` varchar(50) COLLATE utf8_bin DEFAULT '',
  `customercommand` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `transcwb` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `excelimportuserid` int(11) DEFAULT '0',
  `destination` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `transway` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `cwbprovince` varchar(50) COLLATE utf8_bin DEFAULT '',
  `cwbcity` varchar(50) COLLATE utf8_bin DEFAULT '',
  `cwbcounty` varchar(50) COLLATE utf8_bin DEFAULT '',
  `customerwarehouseid` varchar(11) COLLATE utf8_bin DEFAULT '0',
  `cwbordertypeid` varchar(11) COLLATE utf8_bin DEFAULT '-1',
  `cwbdelivertypeid` varchar(11) COLLATE utf8_bin DEFAULT '-1',
  `startbranchid` int(10) DEFAULT '0',
  `nextbranchid` int(10) DEFAULT NULL,
  `backtocustomer_awb` varchar(50) COLLATE utf8_bin DEFAULT '',
  `cwbflowflag` varchar(50) COLLATE utf8_bin DEFAULT '1',
  `carrealweight` decimal(18,3) DEFAULT '0.000',
  `cartype` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `carwarehouse` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `carsize` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `backcaramount` decimal(18,2) DEFAULT '0.00',
  `sendcarnum` int(10) DEFAULT '1',
  `backcarnum` int(10) DEFAULT '0',
  `caramount` decimal(19,2) DEFAULT '0.00',
  `backcarname` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `sendcarname` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `deliverid` int(10) DEFAULT '0',
  `emailfinishflag` int(2) DEFAULT '0',
  `reacherrorflag` int(2) DEFAULT '0',
  `orderflowid` int(11) DEFAULT NULL,
  `flowordertype` int(10) DEFAULT '0',
  `cwbreachbranchid` int(10) DEFAULT NULL,
  `cwbreachdeliverbranchid` int(10) DEFAULT NULL,
  `podfeetoheadflag` varchar(50) COLLATE utf8_bin DEFAULT '0',
  `podfeetoheadtime` varchar(50) COLLATE utf8_bin DEFAULT '2000-01-01 00:00:00',
  `podfeetoheadchecktime` varchar(50) COLLATE utf8_bin DEFAULT '2000-01-01 00:00:00',
  `podfeetoheadcheckflag` varchar(50) COLLATE utf8_bin DEFAULT '0',
  `leavedreasonid` int(10) DEFAULT '0',
  `deliversubscribeday` varchar(50) COLLATE utf8_bin DEFAULT '2000-01-01 00:00:00',
  `shipperid` int(10) DEFAULT '0',
  `state` int(10) DEFAULT '1',
  `multipbranchflag` int(1) DEFAULT '0',
  `multipdeliverflag` int(1) DEFAULT '0',
  `printtime` varchar(20) COLLATE utf8_bin DEFAULT '',
  `commonid` int(11) DEFAULT '0',
  `commoncwb` varchar(100) COLLATE utf8_bin DEFAULT '',
  `podrealname` varchar(50) COLLATE utf8_bin DEFAULT '',
  `signtypeid` int(11) DEFAULT '0',
  `podsignremark` varchar(100) COLLATE utf8_bin DEFAULT '',
  `podtime` varchar(50) COLLATE utf8_bin DEFAULT '',
  `modelname` varchar(50) COLLATE utf8_bin DEFAULT '',
  `reserve` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `reserve1` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `scannum` int(4) DEFAULT '0',
  `remark1` varchar(100) COLLATE utf8_bin DEFAULT '',
  `remark2` varchar(100) COLLATE utf8_bin DEFAULT '',
  `remark3` varchar(100) COLLATE utf8_bin DEFAULT '',
  `remark4` varchar(100) COLLATE utf8_bin DEFAULT '',
  `remark5` varchar(100) COLLATE utf8_bin DEFAULT '',
  `isaudit` int(4) DEFAULT '0',
  `backreason` varchar(225) COLLATE utf8_bin DEFAULT '',
  `leavedreason` varchar(225) COLLATE utf8_bin DEFAULT '',
  `paywayid` int(4) DEFAULT '1',
  `newpaywayid` varchar(10) COLLATE utf8_bin DEFAULT '1',
  `targetcarwarehouse` int(11) DEFAULT '0',
  `issetflag` int(4) DEFAULT '1',
  `credate` varchar(50) COLLATE utf8_bin DEFAULT 'NOW()',
  `multi_shipcwb` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`opscwbid`),
  KEY `detail_cwb_idx` (`cwb`)
) ENGINE=InnoDB AUTO_INCREMENT=5308 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_cwb_error` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cwb` varchar(100) COLLATE utf8_bin DEFAULT '',
  `cwbdetail` text COLLATE utf8_bin,
  `state` int(1) DEFAULT '0',
  `emaildateid` int(11) DEFAULT '0',
  `emaildate` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `message` varchar(500) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_delivery_state` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cwb` varchar(100) COLLATE utf8_bin NOT NULL,
  `deliveryid` int(11) DEFAULT '0',
  `receivedfee` decimal(9,2) DEFAULT '0.00',
  `returnedfee` decimal(9,2) DEFAULT '0.00',
  `businessfee` decimal(9,2) DEFAULT '0.00',
  `cwbordertypeid` varchar(2) COLLATE utf8_bin DEFAULT '0',
  `deliverystate` int(2) DEFAULT '0',
  `cash` decimal(9,2) DEFAULT '0.00',
  `pos` decimal(9,2) DEFAULT '0.00',
  `posremark` varchar(100) COLLATE utf8_bin DEFAULT '',
  `mobilepodtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `checkfee` decimal(9,2) DEFAULT '0.00',
  `checkremark` varchar(100) COLLATE utf8_bin DEFAULT '',
  `receivedfeeuser` int(11) DEFAULT '0',
  `statisticstate` int(2) DEFAULT '1',
  `createtime` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `otherfee` decimal(9,2) DEFAULT '0.00',
  `podremarkid` int(11) DEFAULT NULL,
  `deliverstateremark` varchar(500) COLLATE utf8_bin DEFAULT '',
  `gcaid` int(11) DEFAULT '0',
  `isout` int(4) DEFAULT '0',
  `pos_feedback_flag` int(4) DEFAULT '0',
  `userid` int(11) DEFAULT '0',
  PRIMARY KEY (`id`,`cwb`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_emaildate` (
  `emaildateid` int(10) NOT NULL AUTO_INCREMENT,
  `emaildatetime` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `areaid` int(11) DEFAULT '0',
  `warehouseid` int(11) DEFAULT '0',
  `customerid` int(11) DEFAULT '0',
  `branchid` int(11) DEFAULT '0',
  `state` int(1) DEFAULT '0',
  PRIMARY KEY (`emaildateid`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_exception_cwb` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cwb` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `scantype` int(4) DEFAULT '0',
  `errortype` int(2) DEFAULT '0',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `branchid` int(4) DEFAULT '0',
  `userid` int(4) DEFAULT '0',
  `ishanlder` int(2) DEFAULT '0',
  `customerid` int(4) DEFAULT '0',
  `driverid` int(4) DEFAULT '0',
  `truckid` int(4) DEFAULT '0',
  `deliverid` int(4) DEFAULT '0',
  `interfacetype` varchar(50) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_exportmould` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `roleid` int(11) DEFAULT NULL,
  `mouldname` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `mouldfieldids` varchar(2000) COLLATE utf8_bin DEFAULT NULL,
  `status` int(2) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_goto_class_auditing` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `auditingtime` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `payupamount` decimal(9,2) DEFAULT NULL,
  `payupamount_pos` decimal(9,2) DEFAULT '0.00',
  `receivedfeeuser` int(11) DEFAULT NULL,
  `branchid` int(11) DEFAULT NULL,
  `payupid` int(11) DEFAULT NULL,
  `deliverealuser` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_goto_class_old` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `gotoclassauditingid` int(11) DEFAULT '0',
  `nownumber` int(11) DEFAULT '0',
  `yiliu` int(11) DEFAULT '0',
  `lishiweishenhe` int(11) DEFAULT '0',
  `zanbuchuli` int(11) DEFAULT '0',
  `peisongchenggong` int(11) DEFAULT '0',
  `peisongchenggongamount` decimal(9,2) DEFAULT '0.00',
  `peisongchenggongposamount` decimal(9,2) DEFAULT '0.00',
  `tuihuo` int(11) DEFAULT '0',
  `tuihuoamount` decimal(9,2) DEFAULT '0.00',
  `bufentuihuo` int(11) DEFAULT '0',
  `bufentuihuoamount` decimal(9,2) DEFAULT '0.00',
  `bufentuihuoposamount` decimal(9,2) DEFAULT '0.00',
  `zhiliu` int(11) DEFAULT '0',
  `zhiliuamount` decimal(9,2) DEFAULT '0.00',
  `shangmentuichenggong` int(11) DEFAULT '0',
  `shangmentuichenggongamount` decimal(9,2) DEFAULT '0.00',
  `shangmentuijutui` int(11) DEFAULT '0',
  `shangmentuijutuiamount` decimal(9,2) DEFAULT '0.00',
  `shangmenhuanchenggong` int(11) DEFAULT '0',
  `shangmenhuanchenggongamount` decimal(9,2) DEFAULT '0.00',
  `shangmenhuanchenggongposamount` decimal(9,2) DEFAULT '0.00',
  `diushi` int(11) DEFAULT '0',
  `diushiamount` decimal(9,2) DEFAULT '0.00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_groupdetail` (
  `cwb` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `groupid` int(4) DEFAULT NULL,
  `baleid` int(11) DEFAULT '0',
  `userid` int(11) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_order_flow` (
  `floworderid` bigint(11) NOT NULL AUTO_INCREMENT,
  `cwb` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `branchid` int(11) DEFAULT NULL,
  `credate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `userid` int(11) DEFAULT NULL,
  `floworderdetail` varchar(1000) COLLATE utf8_bin DEFAULT '{}',
  `flowordertype` int(11) DEFAULT NULL,
  `isnow` int(1) DEFAULT '0',
  `outwarehouseid` int(11) DEFAULT '0',
  PRIMARY KEY (`floworderid`),
  KEY `FlowCwbIdx` (`cwb`)
) ENGINE=InnoDB AUTO_INCREMENT=5305 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_outwarehousegroup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `credate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `driverid` int(11) DEFAULT '0',
  `truckid` int(11) DEFAULT '0',
  `state` int(2) DEFAULT '0',
  `branchid` int(11) DEFAULT NULL,
  `printtime` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `operatetype` int(2) DEFAULT NULL,
  `customerid` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_pay_up` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `credatetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `upaccountnumber` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `upuserrealname` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `upbranchid` int(11) DEFAULT NULL,
  `toaccountnumber` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `touserrealname` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `amount` decimal(9,2) DEFAULT NULL,
  `amountpos` decimal(9,2) DEFAULT '0.00',
  `upstate` int(2) DEFAULT NULL,
  `branchid` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `remark` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `type` int(2) DEFAULT NULL,
  `way` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_pos_paydetail` (
  `payid` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pos_code` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'pos支付方编码',
  `cwb` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'pos交易单号',
  `tradeTime` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '交易时间',
  `tradeDeliverId` int(4) DEFAULT NULL COMMENT '交易小件员Id',
  `tradeTypeId` int(4) DEFAULT NULL COMMENT '收、出账类型（1收账；2出账）',
  `payTypeId` int(4) DEFAULT NULL COMMENT '交易类型（1现金、2.pos、3支票、4.账户）枚举',
  `payAmount` decimal(18,2) DEFAULT '0.00' COMMENT '交易金额',
  `payDetail` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '交易详情',
  `payRemark` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '交易备注信息',
  `signName` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '签收人',
  `signTime` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '签收时间',
  `signRemark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '签收备注',
  `signtypeid` int(4) DEFAULT NULL COMMENT '签收类型（1本人签收，2他人签收）',
  `isSuccessFlag` int(4) DEFAULT NULL COMMENT '交易结果，是否撤销（1.交易成功、2撤销）',
  `acq_type` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `acq_type_flag` int(2) DEFAULT '0',
  KEY `payid` (`payid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_setexportfield` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fieldname` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `exportstate` int(11) DEFAULT '0',
  `fieldenglishname` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=108 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (1,'承运商',0,'Commonname'),(2,'供货商',0,'Customername'),(3,'订单号',0,'Cwb'),(4,'运单号',0,'Transcwb'),(5,'收件人名称',0,'Consigneename'),(6,'收件人地址',0,'Consigneeaddress'),(7,'收件人邮编',0,'Consigneepostcode'),(8,'收件人电话',0,'Consigneephone'),(9,'收件人手机',0,'Consigneemobile'),(10,'发出商品名称',0,'Sendcarname'),(11,'取回商品名称',0,'Backcarname'),(12,'货物重量',0,'Carrealweight'),(13,'代收货款应收金额',0,'Receivablefee'),(14,'订单备注',0,'Cwbremark'),(15,'指定小件员',0,'Exceldeliver'),(16,'配送站点',0,'Excelbranch'),(17,'配送单号',0,'Shipcwb'),(18,'收件人编号',0,'Consigneeno'),(19,'货物金额',0,'Caramount'),(20,'客户要求',0,'Customercommand'),(21,'货物类型',0,'Cartype'),(22,'货物尺寸',0,'Carsize'),(23,'取回货物金额',0,'Backcaramount'),(25,'目的地',0,'Destination'),(26,'运输方式',0,'Transway'),(27,'退供货商承运商',0,'Shipperid'),(28,'发货数量',0,'Sendcarnum'),(29,'取货数量',0,'Backcarnum'),(30,'省',0,'Cwbprovince'),(31,'市',0,'Cwbcity'),(32,'区县',0,'Cwbcounty'),(33,'发货仓库',0,'Carwarehouse'),(34,'订单类型',0,'OrderType'),(36,'修改时间',0,'Edittime'),(38,'当前订单状态',0,'FlowordertypeMethod'),(39,'审核人',0,'Auditor'),(40,'退货备注',0,'Returngoodsremark'),(41,'最新跟踪状态',0,'Newfollownotes'),(42,'标记人',0,'Marksflagmen'),(43,'订单原始金额',0,'Primitivemoney'),(45,'签收时间',0,'Signintime'),(46,'修改的签收时间',0,'Editsignintime'),(47,'签收人',0,'Signinman'),(48,'修改人',0,'Editman'),(49,'承运商编号',0,'Commonnumber'),(50,'审核人',0,'Auditor'),(51,'审核时间',0,'Audittime'),(52,'审核状态',0,'AuditstateStr'),(53,'站点',0,'Branchname'),(54,'入库时间',0,'Instoreroomtime'),(55,'备注1',0,'Remark1'),(56,'备注2',0,'Remark2'),(57,'备注3',0,'Remark3'),(58,'备注4',0,'Remark4'),(59,'备注5',0,'Remark5'),(60,'当前所在站点',0,'Startbranchname'),(61,'下一站站点',0,'Nextbranchname'),(62,'出库时间',0,'Outstoreroomtime'),(63,'到站时间',0,'InSitetime'),(64,'小件员领货时间',0,'PickGoodstime'),(65,'配送成功时间',0,'SendSuccesstime'),(66,'反馈时间',0,'Gobacktime'),(67,'归班时间',0,'Goclasstime'),(68,'最新修改时间',0,'Nowtime'),(69,'滞留原因',0,'LeavedreasonStr'),(70,'入库仓库',0,'Inhouse'),(71,'称重重量',0,'Realweight'),(72,'货品备注',0,'Goodsremark'),(73,'支付方式',0,'PaytypeName'),(74,'入库仓库名称',0,'Carwarehousename'),(75,'发货仓库名称',0,'Customerwarehousename'),(76,'反馈小件员',0,'Fdelivername'),(77,'收到总金额',0,'Receivedfee'),(78,'退还金额',0,'Returnedfee'),(79,'应处理金额',0,'Businessfee'),(80,'现金实收',0,'Cash'),(81,'pos实收',0,'Pos'),(82,'pos备注',0,'Posremark'),(83,'pos反馈时间',0,'Mobilepodtime'),(84,'支票实收',0,'Checkfee'),(85,'支票号备注',0,'Checkremark'),(86,'收款人',0,'ReceivedfeeuserName'),(87,'归班状态',0,'StatisticstateStr'),(88,'创建时间',0,'Createtime'),(89,'其他金额',0,'Otherfee'),(90,'反馈备注',0,'Deliverstateremark'),(91,'反馈站点名称',0,'Payupbranchname'),(92,'配送结果备注',0,'PodremarkStr'),(93,'上交款人姓名',0,'Payuprealname'),(94,'发货妥投时间',0,'TuotouTime'),(95,'发货有结果时间',0,'YoujieguoTime'),(96,'入库妥投时间',0,'RukutuotouTime'),(97,'入库有结果时间',0,'RukuyoujieguoTime'),(98,'到站妥投时间',0,'DaozhantuotouTime'),(99,'到站有结果时间',0,'DaozhanyoujieguoTime'),(100,'是否上缴款',0,'IspayUpStr'),(101,'是否有欠款',0,'IsQiankuanStr'),(102,'退货再投审核',0,'AuditEganstateStr'),(103,'当前操作员',0,'OperatorName'),(104,'退货原因',0,'Backreason'),(105,'供应商异常编码',0,'Expt_code'),(106,'供应商异常原因',0,'Expt_msg'),(107,'配送结果',0,'OrderResultTypeText');

CREATE TABLE `express_ops_stock_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cwb` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `branchid` int(11) DEFAULT '0',
  `type` int(11) DEFAULT '2',
  `orderflowid` int(11) DEFAULT NULL,
  `resultid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_stock_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `branchid` int(11) DEFAULT '0',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `state` int(11) DEFAULT '0',
  `checkcount` int(11) DEFAULT '0',
  `realcount` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_account_area` (
  `areaid` int(11) NOT NULL AUTO_INCREMENT,
  `areaname` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `arearemark` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `customerid` int(11) DEFAULT NULL,
  `isEffectFlag` int(11) DEFAULT NULL,
  PRIMARY KEY (`areaid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_address_code_branch` (
  `branchcodeid` int(11) NOT NULL AUTO_INCREMENT,
  `branchaddresscode` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `branchaddressdetail` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `branchid` int(11) DEFAULT NULL,
  PRIMARY KEY (`branchcodeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_address_code_branch_line` (
  `linecodeid` int(11) NOT NULL AUTO_INCREMENT,
  `branchid` int(11) DEFAULT NULL,
  `linecode` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `linename` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`linecodeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_address_code_deliver` (
  `delivercodeid` int(11) NOT NULL AUTO_INCREMENT,
  `deliveraddresscode` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `deliveraddressdetail` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `deliverid` int(11) DEFAULT NULL,
  `linecodeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`delivercodeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_address_db_start` (
  `addressid` int(11) NOT NULL AUTO_INCREMENT,
  `addressdbflag` int(11) DEFAULT NULL,
  PRIMARY KEY (`addressid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_address_key_word_branch` (
  `branchkeyid` int(11) NOT NULL AUTO_INCREMENT,
  `branchcodeid` int(11) DEFAULT NULL,
  `branchaddresskeyword` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `cityid` int(11) DEFAULT NULL,
  `branchid` int(11) DEFAULT NULL,
  PRIMARY KEY (`branchkeyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_address_key_word_branch_second` (
  `branchkeysecondid` int(11) NOT NULL AUTO_INCREMENT,
  `branchkeyid` int(11) DEFAULT NULL,
  `branchaddresskeywordsecond` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`branchkeysecondid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_address_key_word_branch_third` (
  `branchkeythirdid` int(11) NOT NULL AUTO_INCREMENT,
  `branchkeysecondid` int(11) DEFAULT NULL,
  `branchaddresskeywordthird` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`branchkeythirdid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_address_key_word_deliver` (
  `deliverkeyid` int(11) NOT NULL AUTO_INCREMENT,
  `delivercodeid` int(11) NOT NULL,
  `deliveraddresskeyword` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`deliverkeyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_b2c_exptreason` (
  `exptid` bigint(11) NOT NULL AUTO_INCREMENT,
  `customerid` bigint(11) DEFAULT NULL,
  `support_key` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `expt_code` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `expt_msg` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `expt_type` int(4) DEFAULT NULL,
  `expt_remark` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `customercode` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  KEY `exptid` (`exptid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_branch` (
  `branchid` int(11) NOT NULL AUTO_INCREMENT,
  `branchname` varchar(50) COLLATE utf8_bin NOT NULL,
  `branchaddress` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `branchcontactman` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `branchphone` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `branchmobile` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `branchfax` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `branchemail` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `contractflag` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `contractrate` decimal(18,3) DEFAULT NULL,
  `cwbtobranchid` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `branchcode` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `payfeeupdateflag` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `backtodeliverflag` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `branchpaytoheadflag` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `branchfinishdayflag` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `creditamount` decimal(18,2) DEFAULT NULL,
  `branchwavfile` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `brancheffectflag` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `noemailimportflag` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `errorcwbdeliverflag` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `errorcwbbranchflag` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `branchcodewavfile` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `importwavtype` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `exportwavtype` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `branchinsurefee` decimal(18,2) DEFAULT NULL,
  `branchprovince` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `branchcity` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `noemaildeliverflag` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  `sendstartbranchid` int(11) DEFAULT NULL,
  `functionids` varchar(100) COLLATE utf8_bin DEFAULT '' COMMENT '机构对应的可使用功能对应set_function表 逗号分割id',
  `sitetype` int(11) DEFAULT NULL,
  `checkremandtype` int(11) DEFAULT NULL,
  `branchmatter` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `accountareaid` int(11) DEFAULT NULL,
  `arrearagehuo` decimal(9,2) DEFAULT '0.00',
  `arrearagepei` decimal(9,2) DEFAULT '0.00',
  `arrearagefa` decimal(9,2) DEFAULT '0.00',
  `zhongzhuanid` int(11) DEFAULT '0',
  `tuihuoid` int(11) DEFAULT '0',
  `caiwuid` int(11) DEFAULT '0',
  PRIMARY KEY (`branchid`)
) ENGINE=InnoDB AUTO_INCREMENT=187 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert  into `express_set_branch`(`branchid`,`branchname`,`branchaddress`,`branchcontactman`,`branchphone`,`branchmobile`,`branchfax`,`branchemail`,`contractflag`,`contractrate`,`cwbtobranchid`,`branchcode`,`payfeeupdateflag`,`backtodeliverflag`,`branchpaytoheadflag`,`branchfinishdayflag`,`creditamount`,`branchwavfile`,`brancheffectflag`,`noemailimportflag`,`errorcwbdeliverflag`,`errorcwbbranchflag`,`branchcodewavfile`,`importwavtype`,`exportwavtype`,`branchinsurefee`,`branchprovince`,`branchcity`,`noemaildeliverflag`,`sendstartbranchid`,`functionids`,`sitetype`,`checkremandtype`,`branchmatter`,`accountareaid`,`arrearagehuo`,`arrearagepei`,`arrearagefa`,`zhongzhuanid`,`tuihuoid`,`caiwuid`) values (1,'公司','','管理员','','','','','','0.000','','','','','','','0.00','','','','','','','','','0.00','','','',0,'P01,P02,P03,P04,P05,P06,P07,P08,P09,P10,P11,P12,P13,P14,P15,P16,P17,P18,P19',8,16,'',0,'0.00','0.00','0.00',0,0,0),(186,'虚拟库房',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,'0.00','0.00','0.00',0,0,0);

CREATE TABLE `express_set_common` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `commonname` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `commonnumber` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `orderprefix` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `commonstate` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_commonmodel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `modelname` varchar(50) COLLATE utf8_bin DEFAULT '',
  `coordinate` varchar(1000) COLLATE utf8_bin DEFAULT '{getdate:"0px,0px";workname:"0px,0px";goodsname:"0px,0px";cwb:"0px,0px";salecompany:"0px,0px";name:"0px,0px";mobile:"0px,0px";workname1:"0px,0px";address:"0px,0px";weight:"0px,0px";size:"0px,0px";posecode:"0px,0px";remark:"0px,0px";bigwords:"0px,0px";smallwords:"0px,0px";}',
  `imageurl` varchar(100) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_customer_info` (
  `customerid` int(11) NOT NULL AUTO_INCREMENT,
  `customername` varchar(100) COLLATE utf8_bin NOT NULL,
  `customerno` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `customeraddress` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `customerpostcode` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `customercontactman` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `customerphone` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `customermobile` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `customerfax` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `paywayid` int(11) DEFAULT NULL,
  `paystartday` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `payendday` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `customerlevelid` int(11) DEFAULT NULL,
  `customerremark` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `customercreateuserid` int(11) DEFAULT NULL,
  `customercreatetime` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `customerpassword` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `paytransfeeid` int(11) DEFAULT NULL,
  `monthflag` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `monthtypeid` int(11) DEFAULT NULL,
  `startday` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `endday` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `deposit` decimal(19,2) DEFAULT NULL,
  `carsplit` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `transfeecreateid` int(11) DEFAULT NULL,
  `commissionfeecreateid` int(11) DEFAULT NULL,
  `customercode` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `customertypeindmp` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `custprovinceid` int(11) DEFAULT NULL,
  `custeffectflag` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `messagecustomername` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `clearwayid` int(11) DEFAULT NULL,
  `clearcycleid` int(11) DEFAULT NULL,
  `transfeeclearcycleid` int(11) DEFAULT NULL,
  `transfeetypeid` int(11) DEFAULT NULL,
  `chargefeerate` decimal(18,4) DEFAULT NULL,
  `poschargefeerate` decimal(18,4) DEFAULT NULL,
  `customerno2` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `customerno3` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `customerno4` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `customerno5` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `customerpassword2` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `customerpassword3` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `customerpassword4` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `customerpassword5` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `accountbank` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `accountbankno` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `accountname` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `isBackAskFlag` int(11) DEFAULT NULL,
  `ifeffectflag` int(11) DEFAULT '1',
  `payaccountcode` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `isBeforeScanningflag` int(11) DEFAULT NULL,
  `customer_pos_code` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `b2cEnum` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`customerid`)
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_customer_warehouse` (
  `warehouseid` int(11) NOT NULL AUTO_INCREMENT,
  `customerid` int(11) NOT NULL,
  `customerwarehouse` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `warehouseremark` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `ifeffectflag` int(11) DEFAULT NULL,
  `warehouse_no` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`warehouseid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_depart` (
  `departid` int(11) NOT NULL AUTO_INCREMENT,
  `departname` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `departremark` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`departid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_exceed_fee` (
  `exceedid` int(11) NOT NULL AUTO_INCREMENT,
  `exceedfee` decimal(18,2) DEFAULT NULL,
  PRIMARY KEY (`exceedid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert  into `express_set_exceed_fee`(`exceedid`,`exceedfee`) values (1,'0.00');

CREATE TABLE `express_set_excel_column` (
  `columnid` int(11) NOT NULL AUTO_INCREMENT,
  `customerid` int(11) NOT NULL,
  `cwbindex` int(11) NOT NULL,
  `consigneenameindex` int(11) NOT NULL,
  `consigneeaddressindex` int(11) NOT NULL,
  `consigneepostcodeindex` int(11) NOT NULL,
  `consigneephoneindex` int(11) NOT NULL,
  `consigneemobileindex` int(11) NOT NULL,
  `cwbremarkindex` int(11) NOT NULL,
  `sendcargonameindex` int(11) NOT NULL,
  `backcargonameindex` int(11) NOT NULL,
  `cargorealweightindex` int(11) NOT NULL,
  `receivablefeeindex` int(11) NOT NULL,
  `paybackfeeindex` int(11) NOT NULL,
  `shiptimeindex` int(11) DEFAULT NULL,
  `exceldeliverindex` int(11) DEFAULT NULL,
  `excelbranchindex` int(11) DEFAULT NULL,
  `shipcwbindex` int(11) DEFAULT NULL,
  `consigneenoindex` int(11) DEFAULT NULL,
  `cargoamountindex` int(11) DEFAULT NULL,
  `customercommandindex` int(11) DEFAULT NULL,
  `getmobileflag` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `cargotypeindex` int(11) DEFAULT NULL,
  `cargowarehouseindex` int(11) DEFAULT NULL,
  `cargosizeindex` int(11) DEFAULT NULL,
  `backcargoamountindex` int(11) DEFAULT NULL,
  `destinationindex` int(11) DEFAULT NULL,
  `transwayindex` int(11) DEFAULT NULL,
  `commonnumberindex` int(11) DEFAULT NULL,
  `sendcargonumindex` int(11) DEFAULT NULL,
  `backcargonumindex` int(11) DEFAULT NULL,
  `cwbprovinceindex` int(11) DEFAULT NULL,
  `cwbcityindex` int(11) DEFAULT NULL,
  `cwbcountyindex` int(11) DEFAULT NULL,
  `updatetime` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `updateuserid` int(11) DEFAULT NULL,
  `warehousenameindex` int(11) DEFAULT NULL,
  `cwbordertypeindex` int(11) DEFAULT NULL,
  `cwbdelivertypeindex` int(11) DEFAULT NULL,
  `transcwbindex` int(11) DEFAULT NULL,
  `emaildateindex` int(11) DEFAULT NULL,
  `accountareaindex` int(11) DEFAULT NULL,
  `modelnameindex` int(11) DEFAULT NULL,
  `remark1index` int(11) DEFAULT NULL,
  `remark2index` int(11) DEFAULT NULL,
  `remark3index` int(11) DEFAULT NULL,
  `remark4index` int(11) DEFAULT NULL,
  `remark5index` int(11) DEFAULT NULL,
  `paywayindex` int(11) DEFAULT NULL,
  PRIMARY KEY (`columnid`,`sendcargonameindex`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_exptcode_joint` (
  `exptcodeid` bigint(11) NOT NULL AUTO_INCREMENT,
  `reasonid` bigint(11) DEFAULT NULL,
  `exptid` bigint(11) DEFAULT NULL,
  `exptcode_remark` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  KEY `exptcodeid` (`exptcodeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_function` (
  `functionid` int(11) NOT NULL AUTO_INCREMENT COMMENT '功能对应表id',
  `functionname` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '功能名称',
  `menuid` int(11) NOT NULL COMMENT '功能对应的菜单id',
  `type` int(4) NOT NULL COMMENT '0为机构拥有的功能',
  PRIMARY KEY (`functionid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_importfield` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fieldname` varchar(80) COLLATE utf8_bin DEFAULT NULL,
  `fieldenglishname` varchar(80) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert  into `express_set_importfield`(`id`,`fieldname`,`fieldenglishname`) values (2,'收件人姓名','consigneename'),(3,'收件人地址','consigneeaddress'),(4,'邮编','consigneepostcode'),(5,'电话','consigneephone'),(6,'手机','consigneemobile'),(7,'发出商品','sendcarname'),(8,'取回商品','backcarname'),(9,'实际重量','carrealweight'),(10,'应收金额','receivablefee'),(11,'应退金额','paybackfee'),(12,'备注信息','cwbremark'),(13,'指定小件员','exceldeliver'),(14,'指定派送分站','excelbranch'),(15,'供货商运单号','shipcwb'),(16,'收件人编号','consigneeno'),(17,'货物金额','cargoamount'),(18,'客户要求','customercommand'),(19,'货物类型','cargotype'),(20,'货物尺寸','cargosize'),(21,'取回商品金额','backcargoamount'),(22,'目的地','destination'),(23,'运输方式','transway'),(25,'发货数量','sendcarnum'),(26,'取货数量','backcarnum'),(27,'省','cwbprovince'),(28,'城市','cwbcity'),(29,'区县','cwbcounty'),(30,'发货仓库','carwarehouse'),(33,'发货单号','transcwb'),(35,'承运商','commonid'),(36,'派送类型','cwbdelivertypeid'),(37,'结算区域','serviceareaid');

CREATE TABLE `express_set_importset` (
  `importid` int(11) NOT NULL AUTO_INCREMENT,
  `importtypeid` int(11) DEFAULT NULL,
  `importtype` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `importsetflag` int(11) DEFAULT NULL,
  PRIMARY KEY (`importid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_joint` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `joint_num` bigint(20) DEFAULT NULL COMMENT '对接编号',
  `joint_property` text COLLATE utf8_bin COMMENT 'json格式字符串',
  `joint_remark` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `state` int(4) DEFAULT '1',
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_menu_info_new` (
  `menuid` int(11) NOT NULL AUTO_INCREMENT,
  `menuname` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `menuurl` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `menulevel` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `menuno` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `parentno` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `menuimage` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `adminshowflag` varchar(10) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`menuid`)
) ENGINE=InnoDB AUTO_INCREMENT=2002 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert  into `express_set_menu_info_new`(`menuid`,`menuname`,`menuurl`,`menulevel`,`menuno`,`parentno`,`menuimage`,`adminshowflag`) values (1,'数据导入','images/top_but_1.jpg','1','0','0','images/dhbtn1.png',NULL),(2,'数据导入',NULL,'2','001','1',NULL,NULL),(3,'导入模版设置','/excelcolumn/list','3','G2103','215','images/drsz.png',NULL),(4,'订单类型管理','/cwbordertype/list/1','3','G2104','215','images/ddlxsz.png',NULL),(5,'导入数据','/dataimport/excelimportPage','3','00102','2','images/daoru.png',NULL),(8,'数据查询',NULL,'2','002','231',NULL,NULL),(9,'导入查询','/cwborder/list/1','3','00203','8','images/drcx.png',NULL),(22,'库房管理','images/top_but_20.jpg','1','1','0','images/dhbtn2.png',NULL),(34,'交接单打印',NULL,'2','105','22',NULL,NULL),(35,'入库打印','/warehousegroup/inlist/1','3','10304','34','images/rkjjdy.png',NULL),(36,'出库打印','/warehousegroup/outlist/1','3','10305','34','images/ckjjdy.png',NULL),(152,'数据统计','images/top_but_10.jpg','1','A5','0','images/dhbtn3.png',NULL),(207,'系统设置','images/top_but_12.jpg','1','G','0','images/dhbtn4.png',NULL),(208,'公司基本信息维护',NULL,'2','G20','207',NULL,NULL),(209,'承运商管理','/common/list/1','3','G01','230','images/cyssz.png',NULL),(210,'承运商面单模版设置','/commonmodel/tosetcoordinate','3','G02','230','images/cysmbsz.png',NULL),(211,'机构管理','/branch/list/1','3','G2001','208','images/jgsz.png',NULL),(212,'用户管理','/user/list/1','3','G2003','208','images/jcsz.png',NULL),(213,'班车管理','/truck/list/1','3','G2004','208','images/bcsz.png',NULL),(215,'信息维护',NULL,'2','G21','207',NULL,NULL),(225,'合作伙伴','images/top_but_23.jpg','1','H','0','images/dhbtn5.png',NULL),(226,'客户',NULL,'2','H01','225',NULL,NULL),(227,'供货商管理','/customer/list/1','3','H0100','226','images/ghssz.png',NULL),(228,'仓库管理','/customerwarehouses/list/1','3','H0101','226','images/cksz.png',NULL),(229,'供货商结算区域','/accountareas/list/1','3','H0102','226','images/ghsjsqysz.png',NULL),(230,'承运商',NULL,'2','H02','225',NULL,NULL),(232,'系统管理',NULL,'2','I21','231',NULL,NULL),(234,'修改删除','/cwborder/delandedit','3','I2102','232','images/xgsc.png',NULL),(238,'超额提示设置','/exceedfee/list','3','G2101','215','images/cetssz.png',NULL),(247,'岗位管理','/role/list','3','G2002','208','images/jssz.png',NULL),(255,'流程控制管理','/branch/toNextStopPage','3','G2007','215','images/lljksz.png',NULL),(257,'交接单管理','/joinlist/list','3','G2102','215','images/jjtssz.png',NULL),(258,'常用语管理','/reason/list/1','3','G2106','215','images/cyysz.png',NULL),(259,'退货出站打印','/warehousegroup/returnlist/1','3','20104','264','images/thczjjdy.png',NULL),(260,'中转出站打印','/warehousegroup/changelist/1','3','20103','264','images/zzczjjdy.png',NULL),(261,'领货打印','/warehousegroup/deliverlist/1','3','20102','264','images/xjydy.png',NULL),(262,'到站打印','/warehousegroup/inboxlist/1','3','20101','264','images/rkjjdy2.png',NULL),(263,'站点管理','','1','2','0','images/dhbtn12.png',NULL),(264,'交接单打印',NULL,'2','203','263',NULL,NULL),(265,'合同管理#','/welcome','3','H0103','226',NULL,NULL),(266,'公告发布#','/welcome','3','G2106','215','images/ggfb.png',NULL),(267,'中转站入库打印','/warehousegroup/changeinlist/1','3','10306','34','images/rzrksmdy.png',NULL),(268,'退货站入库打印','/warehousegroup/backinlist/1','3','10307','34','images/thrjjj.png',NULL),(269,'提货(P)',NULL,'p','P01','-1',NULL,NULL),(270,'库房入库(P)',NULL,'p','P02','-1',NULL,NULL),(271,'库房出库(P)',NULL,'p','P03','-1',NULL,NULL),(272,'理货(P)',NULL,'p','P04','-1',NULL,NULL),(273,'分站到货(P)',NULL,'p','P05','-1',NULL,NULL),(274,'小件员领货(P)',NULL,'p','P06','-1',NULL,NULL),(275,'小件员反馈(P)',NULL,'p','P07','-1',NULL,NULL),(276,'归班汇总(P)',NULL,'p','P08','-1',NULL,NULL),(277,'退货出站(P)',NULL,'p','P09','-1',NULL,NULL),(278,'中转出站(P)',NULL,'p','P10','-1',NULL,NULL),(279,'退货站入库(P)',NULL,'p','P11','-1',NULL,NULL),(280,'退货站再投(P)',NULL,'p','P12','-1',NULL,NULL),(281,'中转站入库(P)',NULL,'p','P13','-1',NULL,NULL),(282,'中转站出库(P)',NULL,'p','P14','-1',NULL,NULL),(283,'库存盘点(P)',NULL,'p','P15','-1',NULL,NULL),(284,'订单查询(P)',NULL,'p','P16','-1',NULL,NULL),(285,'系统设置(P)',NULL,'p','P17','-1',NULL,NULL),(286,'中转站出库打印','/warehousegroup/transbranchoutlist/1','3','10308 ','34','images/zzckdy.png',NULL),(287,'退货站再投打印','/warehousegroup/returnbranchoutlist/1','3','10309','34','images/zzckzt.png',NULL),(288,'异常单监控',NULL,'2','S','152',NULL,NULL),(289,'扫描监控','/cwborderPDA/controlForBranchImport/1','3','102','305','images/fzdhxx.png',NULL),(290,'归班管理',NULL,'2','202','263',NULL,NULL),(291,'归班审核','/delivery/auditView','3','20201','290','images/gbsh.png',NULL),(292,'站点结算','/payup/viewCount','3','20202','290','images/js.png',NULL),(293,'站点货物管理',NULL,'2','202','263',NULL,NULL),(294,'上门退订单打印','/cwborder/selectforsmt/1','3','301','293','images/smtdd.png',NULL),(296,'退供应商出库',NULL,'p','P18','-1',NULL,NULL),(297,'供应商拒收返库',NULL,'p','P19','-1',NULL,NULL),(298,'换单',NULL,'2','104','22',NULL,NULL),(299,'订单称重','/changecwb/tochangecwbforwegiht','3','101','298','images/cz.png',NULL),(300,'按泡货规格称重','/changecwb/tochangecwbforwegihttopaohuo','3','102','298','images/aphggcz.png',NULL),(301,'打印面单','/changecwb/toprintcwb','3','103','298','images/mddy.png',NULL),(302,'打印面单（带称重）','/changecwb/toprintandweightcwb','3','104','298','images/mddydcz.png',NULL),(303,'货物操作',NULL,'2','103','22',NULL,NULL),(304,'库房扫描','/pda/kufanglist','3','101','303','images/kfsm.png',NULL),(305,'扫描',NULL,'2','201','263',NULL,NULL),(306,'站点扫描','/pda/zhandianlist','3','101','305','images/zdsm.png',NULL),(401,'对接管理','/jointManage/','3','G2107','215','images/jcsz.png',NULL),(402,'导出模版管理','/setexportcwb/list/1','3','G2110','215','images/dcsz.png',NULL),(403,'扫描监控','/cwborderPDA/controlForBranchImport/1','3','103','303','images/fzdhxx.png',NULL),(404,'上门退订单打印','/cwborder/selectforkfsmt/1','3','104','303','images/smtdd.png',NULL),(405,'退供应商出库交接单','/warehousegroup/backtocustomerlist/1','3','10310','34','images/tgysck.png',NULL),(406,'支付方式管理','/payway/list/1','3','G2105','215','images/zffs.png',NULL),(407,'库对库扫描','/pda/kftokflist','3','102','303','images/kdksm.png',NULL),(408,'库房对应省市管理','/warehousekey/list/1','3','G2111','215','images/kfdyssgl.png',NULL),(410,'交接单打印（详）',NULL,'2','204','263',NULL,NULL),(411,'出库打印','/warehousegroupdetail/outlist/1','3','101','409','images/ckjjdy.png',NULL),(412,'二级分拨','/cwborder/cwbresetbranch','3','103','305','images/ejfb.png',NULL),(413,'中转出站打印','/warehousegroupdetail/outlist/1','3','101','410','images/zzczjjdy.png',NULL),(414,'退货出站打印','/warehousegroupdetail/returnlist/1','3','102','410','images/thczjjdy.png',NULL),(415,'中转站出库打印','/warehousegroupdetail/transbranchoutlist/1','3','102','409','images/zzckdy.png',NULL),(416,'退货站再投打印','/warehousegroupdetail/returnbranchoutlist/1','3','103','409','images/zzckzt.png',NULL),(1063,'客服管理','images/dhbtn13.png','1','A1','0','images/dhbtn13.png',NULL),(1064,'运营监控','images/dhbtn14.png','1','A2','0','images/dhbtn14.png',NULL),(1065,'结算管理','images/dhbtn15.png','1','A3','0','images/dhbtn15.png',NULL),(1066,'数据查询','images/top_but_11.jpg','1','A4','0','images/dhbtn11.png',NULL),(1067,'订单查询','','2','A101','1063',NULL,NULL),(1068,'投诉处理','','2','A102','1063',NULL,NULL),(1071,'订单查询','/order/select/1','3','A10101','1067','images/ddcx.png',NULL),(1072,'投诉处理','/complaint/list/1','3','A10201','1068','images/tscl.png',NULL),(1075,'投递率',NULL,'2','B1','1064',NULL,NULL),(1076,'实时监控',NULL,'2','B2','1064',NULL,NULL),(1077,'各站统计(按天)','/delivery/select','3','B202','1075','images/zdtdltj.png',NULL),(1078,'数据监控','/monitor/date/1','3','B101','1076','images/sjjk].png',NULL),(1079,'库房信息监控','/monitorhouse/date','3','B103','1076','images/kfjk.png',NULL),(1080,'站点信息监控','/monitorsite/date','3','B104','1076','images/zdxxjk.png',NULL),(1081,'投递实效监控','/monitordelivery/date/5','3','B105','1076','images/tdxxjk.png',NULL),(1083,'异常信息监控','/monitor/date/7','3','B107','1076','images/ycxxjk.png',NULL),(1085,'结算与审核',NULL,'2','A301','1065',NULL,NULL),(1086,'货款结算','/funds/payment','3','A30101','1085','images/dkjs.png',NULL),(1087,'退货款结算','/funds/paymentBack','3','A30102','1085','images/tkkjs.png',NULL),(1088,'站点交款审核','/funds/paymentCheack','3','A30103','1085','images/zdjksh.png',NULL),(1093,'公司统计(按天)','/delivery/selectAll','3','B201','1075','images/gstdltj.png',NULL),(1094,'数据监控(数据组)','/monitor/date/2','3','B102','1076','images/azsjjk.png',NULL),(1103,'导出订单',NULL,'2','A402','1066',NULL,NULL),(1104,'设置导出字段','/exportcwb/list','3','A40201','1098',NULL,NULL),(1105,'高级查询','/advancedquery/list/1','3','A10102','1067','images/gjcx.png',NULL),(1106,'预警','/order/earlywarning/1','3','A10103','1067','images/yujing.png',NULL),(1107,'批量修改','/order/batchedit','3','A10104','1067','images/plxg.png',NULL),(1108,'批量修改运单号','/order/transcwbbatchedit','3','A10105','1067','images/plxghdh.png',NULL),(1109,'退货管理','/order/backgoods/1','3','A10106','1067','images/thgl.png',NULL),(1110,'退货批量审核','/order/backgoodsbatch/1','3','A10107','1067','images/thplsh.png',NULL),(1113,'代理管理','/proxy/select/1','3','G2004','215','images/plxg.png',NULL),(1114,'短信账户管理','/smsconfig/setsmsview','3','G2108','215','images/dxqf.png',NULL),(1115,'短信群发','/sms/view','3','A10108','1067','images/dxqf.png',NULL),(1117,'数据查询',NULL,'1','I','0','images/dhbtn3.png',NULL),(1118,'订单数据',NULL,'2','A101','1117',NULL,NULL),(1119,'高级查询','/advancedquery/hmj_list/1','3','A101','1118','images/gjcx.png',NULL),(1120,'POS款项查询','/pospay/list/1','3','A30104','1085','images/POS.png',NULL),(2001,'地址库管理','http://58.83.193.7/addressmatch/site/siteMatchEditor','3','G2008','215','images/dhbtn16.png',NULL);

CREATE TABLE `express_set_payway` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `payway` varchar(50) COLLATE utf8_bin DEFAULT '',
  `paywayid` int(4) DEFAULT NULL,
  `issetflag` int(4) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_reason` (
  `reasonid` int(10) NOT NULL AUTO_INCREMENT,
  `reasoncontent` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `reasontype` int(10) DEFAULT '0',
  PRIMARY KEY (`reasonid`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_remark` (
  `remarkid` int(11) NOT NULL AUTO_INCREMENT,
  `remarktype` varchar(50) COLLATE utf8_bin NOT NULL,
  `remark` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`remarkid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_role_menu_new` (
  `roleid` int(11) NOT NULL,
  `menuid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert  into `express_set_role_menu_new`(`roleid`,`menuid`) values (4,263),(4,264),(4,262),(4,261),(4,260),(4,259),(4,289),(4,290),(4,291),(4,292),(4,293),(4,294),(4,295),(4,207),(4,208),(4,212),(4,213),(4,272),(4,273),(4,274),(4,275),(4,276),(4,277),(4,278),(4,284),(4,285),(0,1),(0,2),(0,5),(0,22),(0,303),(0,304),(0,403),(0,404),(0,298),(0,299),(0,300),(0,34),(0,35),(0,36),(0,286),(0,287),(0,263),(0,305),(0,306),(0,289),(0,290),(0,291),(0,292),(0,293),(0,294),(0,264),(0,261),(0,260),(0,259),(0,1063),(0,1067),(0,1071),(0,1109),(0,1110),(0,1115),(0,1068),(0,1072),(0,1064),(0,1075),(0,1093),(0,1077),(0,1076),(0,1078),(0,1094),(0,1079),(0,1080),(0,1081),(0,1083),(0,1065),(0,1085),(0,1086),(0,1087),(0,1088),(0,207),(0,208),(0,211),(0,247),(0,212),(0,213),(0,215),(0,255),(0,2001),(0,238),(0,3),(0,4),(0,258),(0,401),(0,1114),(0,402),(0,225),(0,226),(0,227),(0,228),(0,229),(0,265),(0,230),(0,209),(0,210),(0,269),(0,270),(0,271),(0,272),(0,273),(0,274),(0,275),(0,276),(0,277),(0,278),(0,279),(0,280),(0,281),(0,282),(0,283),(0,284),(0,285),(0,296),(0,297);

CREATE TABLE `express_set_role_new` (
  `roleid` int(11) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `type` int(2) DEFAULT '1',
  PRIMARY KEY (`roleid`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert  into `express_set_role_new`(`roleid`,`rolename`,`type`) values (0,'管理员',0),(1,'客服',0),(2,'小件员',0),(3,'驾驶员',0),(4,'站长',0);

CREATE TABLE `express_set_servicearea` (
  `serviceareaid` int(11) NOT NULL AUTO_INCREMENT,
  `serviceareaname` varchar(50) COLLATE utf8_bin NOT NULL,
  `servicearearemark` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `customerid` int(11) DEFAULT NULL,
  `servid` varchar(2) COLLATE utf8_bin DEFAULT '1',
  PRIMARY KEY (`serviceareaid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_shipper` (
  `shipperid` int(11) NOT NULL AUTO_INCREMENT,
  `shipperno` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `shippername` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `shipperurl` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `shipperremark` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `paywayid` int(11) DEFAULT NULL,
  PRIMARY KEY (`shipperid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_switch` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `switchname` varchar(50) COLLATE utf8_bin DEFAULT '',
  `state` varchar(20) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert  into `express_set_switch`(`id`,`switchname`,`state`) values (1,'出库','ck_02'),(2,'单票反馈','dpfk_01'),(3,'入库打印标签','rkbq_02'),(4,'导入数据创建发货仓库','cjfhck_02'),(5,'changesing','xgqs_01'),(6,'批量反馈支付方式pos','plzffs_02');

CREATE TABLE `express_set_truck` (
  `truckid` int(10) NOT NULL AUTO_INCREMENT,
  `truckno` varchar(20) COLLATE utf8_bin DEFAULT '0',
  `trucktype` varchar(50) COLLATE utf8_bin DEFAULT '0',
  `truckoil` double DEFAULT '0',
  `truckway` varchar(50) COLLATE utf8_bin DEFAULT '0',
  `truckkm` double DEFAULT '0',
  `truckstartkm` double DEFAULT '0',
  `truckdriver` int(11) DEFAULT NULL,
  `truckflag` int(1) DEFAULT '0',
  PRIMARY KEY (`truckid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_user` (
  `userid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `realname` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `idcardno` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `employeestatus` int(2) DEFAULT NULL,
  `branchid` int(11) DEFAULT NULL,
  `userphone` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `usermobile` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `useraddress` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `userremark` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `usersalary` decimal(19,2) DEFAULT NULL,
  `usercustomerid` int(11) DEFAULT NULL,
  `showphoneflag` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `useremail` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `deliverpaytype` int(2) DEFAULT NULL,
  `userwavfile` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `branchmanagerflag` int(2) DEFAULT NULL,
  `roleid` int(11) DEFAULT NULL,
  `userDeleteFlag` int(2) DEFAULT '1',
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=999 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert  into `express_set_user`(`userid`,`username`,`password`,`realname`,`idcardno`,`employeestatus`,`branchid`,`userphone`,`usermobile`,`useraddress`,`userremark`,`usersalary`,`usercustomerid`,`showphoneflag`,`useremail`,`deliverpaytype`,`userwavfile`,`branchmanagerflag`,`roleid`,`userDeleteFlag`) values (1,'admin','admin','管理员','0',1,1,NULL,'0',NULL,NULL,NULL,0,'1','NULL',0,'NULL',0,0,1);

CREATE TABLE `express_set_warehouse_key` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `targetcarwarehouseid` int(11) DEFAULT NULL,
  `keyname` varchar(50) COLLATE utf8_bin DEFAULT '',
  `ifeffectflag` int(2) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


CREATE TABLE `express_sys_on_off` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `number` bigint(11) DEFAULT '0',
  `on_off` varchar(3) COLLATE utf8_bin DEFAULT 'on',
  `type` varchar(40) COLLATE utf8_bin DEFAULT '',
  `mac` varchar(100) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_sys_on_off` */

insert  into `express_sys_on_off`(`id`,`number`,`on_off`,`type`,`mac`) values (1,0,'on','SYSTEM_ON_OFF','c838c6c9637121acb0d21839a319cc2d');

CREATE TABLE `express_sys_scan` (
  `cwb` varchar(100) COLLATE utf8_bin NOT NULL,
  `flowordertype` int(10) DEFAULT '-1',
  PRIMARY KEY (`cwb`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_sys_scan` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
