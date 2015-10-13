/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE TABLE `express_branch_payamount` (
  `branchpayid` bigint(11) NOT NULL AUTO_INCREMENT,
  `branchid` bigint(10) DEFAULT NULL,
  `branchname` varchar(255) DEFAULT NULL,
  `deliverpaydate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `cwbnum` bigint(10) DEFAULT NULL,
  `checkdate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `branchpaydatetime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `branchpaydate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `receivablefee` decimal(9,2) DEFAULT NULL,
  `receivedfee` decimal(9,2) DEFAULT NULL,
  `paybackfee` decimal(9,2) DEFAULT NULL,
  `paybackedfee` decimal(9,2) DEFAULT NULL,
  `payheadfee` decimal(9,2) DEFAULT NULL,
  `payedheadfee` decimal(9,2) DEFAULT NULL,
  `payremark` varchar(1000) DEFAULT NULL,
  `checkremark` varchar(1000) DEFAULT NULL,
  `paywayname` varchar(255) DEFAULT NULL,
  `payproveid` varchar(500) DEFAULT NULL,
  `receivedfeecash` decimal(9,2) DEFAULT NULL,
  `receivedfeepos` decimal(9,2) DEFAULT NULL,
  `receivedfeecheque` decimal(9,2) DEFAULT NULL,
  `receivedfeepos_checked` decimal(9,2) DEFAULT NULL,
  `receivedfeecheque_checked` decimal(9,2) DEFAULT NULL,
  `otherbranchfee` decimal(9,2) DEFAULT NULL,
  `otherbranchfee_checked` decimal(9,2) DEFAULT NULL,
  `receivedfee_halfback` decimal(9,2) DEFAULT NULL,
  `receivedfee_success` decimal(9,2) DEFAULT NULL,
  `totaldebtfee` decimal(9,2) DEFAULT NULL,
  `checkposdate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `cashnum` bigint(11) DEFAULT NULL,
  `posnum` bigint(11) DEFAULT NULL,
  `upstate` bigint(4) DEFAULT NULL,
  `ordercount` bigint(11) DEFAULT NULL,
  `userid` bigint(10) DEFAULT NULL,
  `orderStr` varchar(1000) DEFAULT NULL,
  `username` varchar(200) DEFAULT NULL,
  `payUpId` bigint(11) DEFAULT '0',
  `payuprealname` varchar(255) DEFAULT NULL,
  `upbranchid` int(11) DEFAULT '0',
  `payup_type` int(11) DEFAULT NULL,
  `way` int(11) DEFAULT NULL,
  PRIMARY KEY (`branchpayid`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;

CREATE TABLE `express_cs_complaint` (
  `complaintid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `complainttypeid` bigint(11) DEFAULT NULL COMMENT '投诉类型id',
  `complaintcwb` varchar(25) DEFAULT NULL COMMENT '投诉单号',
  `complaintcustomerid` bigint(11) DEFAULT NULL COMMENT '投诉客户id',
  `complaintcontent` varchar(500) DEFAULT NULL COMMENT '投诉内容',
  `complaintuserid` int(11) DEFAULT NULL COMMENT '被投诉人id',
  `complaintresult` varchar(500) DEFAULT NULL COMMENT '投诉处理结果',
  `complaintcreateuser` varchar(25) DEFAULT NULL COMMENT '投诉创建人姓名',
  `complainttime` varchar(200) DEFAULT NULL COMMENT '投诉记录时间',
  `checkflag` int(4) DEFAULT NULL COMMENT '审核确认投诉 0未审核 1已审核',
  `complaintcontactman` varchar(25) DEFAULT NULL COMMENT '投诉人',
  `complaintphone` varchar(25) DEFAULT NULL COMMENT '投诉人联系电话',
  `complaintflag` int(4) DEFAULT NULL COMMENT '投诉处理状态',
  `complaintdelflag` int(4) DEFAULT NULL COMMENT '删除状态 1为删除0为未删除',
  `complaintuserdesc` varchar(255) DEFAULT NULL COMMENT '被投诉人备注',
  `issure` int(4) DEFAULT '1',
  PRIMARY KEY (`complaintid`),
  KEY `complaintid` (`complaintid`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

CREATE TABLE `express_ops_createselect_term` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `termname` varchar(225) COLLATE utf8_bin DEFAULT NULL,
  `termContent` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_cwb_detail` (
  `opscwbid` int(11) NOT NULL AUTO_INCREMENT,
  `cwb` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `emaildate` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `consigneeno` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `consigneename` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `consigneeaddress` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `consigneepostcode` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `consigneephone` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `consigneemobile` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `shiptime` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `shipcwb` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `cwbremark` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `serviceareaid` int(11) DEFAULT '0',
  `receivablefee` decimal(19,2) DEFAULT '0.00',
  `paybackfee` decimal(19,2) DEFAULT '0.00',
  `customerid` int(11) DEFAULT NULL,
  `exceldeliver` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `excelbranch` varchar(50) COLLATE utf8_bin DEFAULT NULL,
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
  `outwarehousegroupid` int(11) DEFAULT '0',
  `backtocustomer_awb` varchar(50) COLLATE utf8_bin DEFAULT '',
  `cwbflowflag` varchar(50) COLLATE utf8_bin DEFAULT '1',
  `carrealweight` decimal(18,3) DEFAULT '0.000',
  `cartype` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `carwarehouse` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `carsize` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `backcaramount` decimal(18,2) DEFAULT '0.00',
  `sendcarnum` int(10) DEFAULT '0',
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
  `branchgroupid` int(4) DEFAULT NULL,
  `delivername` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `customername` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `branchname` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `commonname` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `newfollownotes` varchar(225) COLLATE utf8_bin DEFAULT NULL,
  `marksflag` int(2) DEFAULT '0',
  `marksflagmen` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `commonid` int(11) DEFAULT NULL,
  `allfollownotes` varchar(2000) COLLATE utf8_bin DEFAULT NULL,
  `primitivemoney` decimal(18,2) DEFAULT '0.00',
  `marksflagtime` varchar(80) COLLATE utf8_bin DEFAULT NULL,
  `edittime` varchar(80) COLLATE utf8_bin DEFAULT NULL,
  `editman` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `signinman` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `signintime` varchar(80) COLLATE utf8_bin DEFAULT NULL,
  `returngoodsremark` varchar(225) COLLATE utf8_bin DEFAULT NULL,
  `commonnumber` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `auditstate` int(11) DEFAULT '0',
  `auditor` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `audittime` varchar(80) COLLATE utf8_bin DEFAULT NULL,
  `editsignintime` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `floworderid` bigint(11) DEFAULT NULL,
  `branchid` int(11) DEFAULT NULL,
  `emaildateid` int(11) DEFAULT NULL,
  `reserve` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `reserve1` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `instoreroomtime` varchar(80) COLLATE utf8_bin DEFAULT NULL,
  `remark1` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `remark2` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `remark3` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `remark4` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `remark5` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `startbranchname` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `nextbranchname` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `outstoreroomtime` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `inSitetime` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `pickGoodstime` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `sendSuccesstime` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `gobacktime` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `nowtime` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `inhouse` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `realweight` decimal(9,2) DEFAULT '0.00',
  `goodsremark` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `paytype` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `customerwarehousename` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `carwarehousename` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `goclasstime` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `leavedreasonStr` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `fdeliverid` int(11) DEFAULT NULL,
  `fdelivername` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `receivedfee` decimal(10,2) DEFAULT '0.00',
  `returnedfee` decimal(10,2) DEFAULT '0.00',
  `businessfee` decimal(10,2) DEFAULT '0.00',
  `deliverystate` int(11) DEFAULT NULL,
  `cash` decimal(10,2) DEFAULT '0.00',
  `pos` decimal(10,2) DEFAULT '0.00',
  `posremark` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `mobilepodtime` timestamp NULL DEFAULT NULL,
  `checkfee` decimal(10,2) DEFAULT '0.00',
  `checkremark` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `receivedfeeuser` int(11) DEFAULT NULL,
  `statisticstate` int(11) DEFAULT NULL,
  `createtime` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `otherfee` decimal(10,2) DEFAULT '0.00',
  `podremarkid` int(10) DEFAULT NULL,
  `deliverstateremark` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `gcaid` int(11) DEFAULT NULL,
  `gobackid` int(11) DEFAULT NULL,
  `payupbranchid` int(11) DEFAULT NULL,
  `payupbranchname` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `podremarkStr` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `receivedfeeuserName` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `payuprealname` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `youdanwuhuoBranchid` int(11) DEFAULT NULL,
  `youhuowudanBranchid` int(11) DEFAULT NULL,
  `tuotouTime` int(11) DEFAULT NULL,
  `youjieguoTime` int(11) DEFAULT NULL,
  `rukutuotouTime` int(11) DEFAULT NULL,
  `rukuyoujieguoTime` int(11) DEFAULT NULL,
  `daozhantuotouTime` int(11) DEFAULT NULL,
  `daozhanyoujieguoTime` int(11) DEFAULT NULL,
  `auditEganstate` int(11) DEFAULT '0',
  `ispayUp` int(4) DEFAULT '0',
  `isQiankuan` int(4) DEFAULT '0',
  `ruku_dangdang_flag` int(4) DEFAULT '0',
  `chuku_dangdang_flag` int(4) DEFAULT '0',
  `deliverystate_dangdang_flag` int(4) DEFAULT '0',
  `operatorName` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `backreasonid` int(11) DEFAULT '0',
  `backreason` varchar(500) COLLATE utf8_bin DEFAULT '',
  `expt_code` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `expt_msg` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `targetcarwarehouse` int(11) DEFAULT '0',
  `targetcarwarehouseName` varchar(50) COLLATE utf8_bin DEFAULT '',
  `multi_shipcwb` varchar(1500),
  PRIMARY KEY (`opscwbid`),
  KEY `detail_cwb_idx` (`cwb`)
) ENGINE=InnoDB AUTO_INCREMENT=1515 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_cwb_error` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cwb` varchar(100) COLLATE utf8_bin DEFAULT '',
  `cwbdetail` text COLLATE utf8_bin,
  `state` int(1) DEFAULT '0',
  `emaildate` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `message` varchar(50) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=130 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_delivery_state` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cwb` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `deliveryid` int(11) DEFAULT '0',
  `receivedfee` decimal(9,2) DEFAULT '0.00',
  `returnedfee` decimal(9,2) DEFAULT '0.00',
  `businessfee` decimal(9,2) DEFAULT '0.00',
  `cwbordertypeid` varchar(2) COLLATE utf8_bin DEFAULT '0',
  `deliverystate` int(2) DEFAULT '0',
  `cash` decimal(9,2) DEFAULT '0.00',
  `pos` decimal(9,2) DEFAULT '0.00',
  `posremark` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `mobilepodtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `checkfee` decimal(9,2) DEFAULT '0.00',
  `checkremark` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `receivedfeeuser` int(11) DEFAULT '0',
  `statisticstate` int(2) DEFAULT '1',
  `createtime` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `otherfee` decimal(10,2) DEFAULT '0.00',
  `podremarkid` int(11) DEFAULT NULL,
  `gobackid` int(11) DEFAULT NULL,
  `goodsname` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `sendcarname` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `caramount` decimal(18,2) DEFAULT NULL,
  `consigneename` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `consigneephone` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `consigneeaddress` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `emaildate` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `branchid` int(11) DEFAULT NULL,
  `deliveryName` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  KEY `id` (`id`),
  KEY `DeliveryStateIdx` (`cwb`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_emaildate` (
  `emaildateid` int(10) NOT NULL AUTO_INCREMENT,
  `emaildatetime` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `state` int(1) DEFAULT '0',
  `warehouseid` int(11) DEFAULT '0',
  `customerid` int(11) DEFAULT '0',
  PRIMARY KEY (`emaildateid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_exportmould` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `roleid` int(11) DEFAULT NULL,
  `mouldname` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `mouldfieldids` varchar(225) COLLATE utf8_bin DEFAULT NULL,
  `status` int(2) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_goto_class_auditing` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `auditingtime` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `payupamount` decimal(9,2) DEFAULT NULL,
  `receivedfeeuser` int(11) DEFAULT NULL,
  `branchid` int(11) DEFAULT NULL,
  `payupid` int(11) DEFAULT NULL,
  `classid` int(11) DEFAULT NULL,
  `gobackidstr` longtext COLLATE utf8_bin,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_operatelog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `operateman` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `operatetime` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `cwb` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `transcwb` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `operateremarks` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=147 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_order_flow` (
  `floworderid` bigint(11) NOT NULL AUTO_INCREMENT,
  `cwb` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `branchid` int(11) DEFAULT NULL,
  `credate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `userid` int(11) DEFAULT NULL,
  `floworderdetail` varchar(1000) COLLATE utf8_bin DEFAULT '{}',
  `flowordertype` int(11) DEFAULT NULL,
  `isnow` int(1) DEFAULT '0',
  `customerid` int(4) DEFAULT '0',
  `emaildate` timestamp NULL DEFAULT NULL,
  `shiptime` timestamp NULL DEFAULT NULL,
  `caramount` decimal(19,2) DEFAULT NULL,
  `exportType` int(4) DEFAULT '0' COMMENT '导出状态 0 未导出 1 已导出 2 导出已审核',
  `exportBackType` int(4) DEFAULT '0' COMMENT '导出状态 0 未导出 1 已导出 2 导出已审核',
  `isGo` int(4) DEFAULT '0' COMMENT '是否操作此项0 未操作 1操作',
  `outwarehouseid` int(11) DEFAULT '0',
  `state` int(4) DEFAULT '1',
  `emaildateid` int(11) DEFAULT NULL,
  `customername` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '供货商',
  `startbranchid` int(11) DEFAULT NULL,
  `nextbranchid` int(11) DEFAULT NULL,
  `excelbranchid` int(11) DEFAULT NULL,
  `carwarehouse` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `send_b2c_flag` int(4) DEFAULT '0',
  `startbranchname` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `nextbranchname` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `username` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `excelbranchname` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `deliverid` int(11) DEFAULT NULL,
  `delivername` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `signinman` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`floworderid`),
  KEY `FlowCwbIdx` (`cwb`)
) ENGINE=InnoDB AUTO_INCREMENT=1513 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_outwarehousegroup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `credate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `driverid` int(11) DEFAULT '0',
  `truckid` int(11) DEFAULT '0',
  `state` int(2) DEFAULT '0',
  `branchid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_ops_setexportfield` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fieldname` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `exportstate` int(11) DEFAULT '0',
  `fieldenglishname` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (1,'承运商',0,'Commonname'),(2,'供货商',0,'Customername'),(3,'订单号',0,'Cwb'),(4,'运单号',0,'Transcwb'),(5,'收件人名称',0,'Consigneename'),(6,'收件人地址',0,'Consigneeaddress'),(7,'收件人邮编',0,'Consigneepostcode'),(8,'收件人电话',0,'Consigneephone'),(9,'收件人手机',0,'Consigneemobile'),(10,'发出商品名称',0,'Sendcarname'),(11,'取回商品名称',0,'Backcarname'),(12,'货物重量',0,'Carrealweight'),(13,'代收货款应收金额',0,'Carrealweight'),(14,'订单备注',0,'Cwbremark'),(15,'指定小件员',0,'Exceldeliver'),(16,'指定派送分站',0,'Excelbranch'),(17,'配送单号',0,'Shipcwb'),(18,'收件人编号',0,'Consigneeno'),(19,'货物金额',0,'Caramount'),(20,'客户要求',0,'Customercommand'),(21,'货物类型',0,'Cartype'),(22,'货物尺寸',0,'Carsize'),(23,'取回货物金额',0,'Backcaramount'),(25,'目的地',0,'Destination'),(26,'运输方式',0,'Transway'),(27,'退供货商承运商',0,'Shipperid'),(28,'发货数量',0,'Sendcarnum'),(29,'取货数量',0,'Backcarnum'),(30,'省',0,'Cwbprovince'),(31,'市',0,'Cwbcity'),(32,'区县',0,'Cwbcounty'),(33,'发货仓库',0,'Carwarehouse'),(34,'订单类型',0,'Cwbordertypeid'),(36,'修改时间',0,'Edittime'),(38,'订单状态',0,'FlowordertypeMethod'),(39,'审核人',0,'Auditor'),(40,'退货备注',0,'Returngoodsremark'),(41,'最新跟踪状态',0,'Newfollownotes'),(42,'标记人',0,'Marksflagmen'),(43,'订单原始金额',0,'Primitivemoney'),(45,'签收时间',0,'Signintime'),(46,'修改的签收时间',0,'Sditsignintime'),(47,'签收人',0,'Signinman'),(48,'修改人',0,'Editman');


CREATE TABLE `express_ops_stock_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cwb` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `branchid` int(11) DEFAULT '0',
  `type` int(11) DEFAULT '2',
  `orderflowid` int(11) DEFAULT NULL,
  `resultid` int(11) DEFAULT NULL,
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


CREATE TABLE `express_ops_stock_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `branchid` int(11) DEFAULT '0',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `state` int(11) DEFAULT '0',
  `checkcount` int(11) DEFAULT '0',
  `realcount` int(11) DEFAULT '0',
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_send_b2c_data` (
  `b2cid` bigint(18) NOT NULL AUTO_INCREMENT,
  `cwb` varchar(100) DEFAULT NULL,
  `posttime` varchar(50) DEFAULT NULL,
  `flowordertype` int(4) DEFAULT NULL,
  `jsoncontent` text,
  `send_b2c_flag` int(4) DEFAULT '0',
  `customerid` int(11) DEFAULT NULL,
  `select_b2c_flag` int(4) DEFAULT '0',
  PRIMARY KEY (`b2cid`),
  KEY `cwb_index` (`cwb`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `express_set_account_area` (
  `areaid` int(11) NOT NULL AUTO_INCREMENT,
  `areaname` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `arearemark` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `customerid` int(11) DEFAULT NULL,
  `isEffectFlag` int(11) DEFAULT NULL,
  PRIMARY KEY (`areaid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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
  `branchtypeflag` varchar(50) COLLATE utf8_bin DEFAULT NULL,
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
  PRIMARY KEY (`branchid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert  into `express_set_branch`(`branchid`,`branchname`,`branchaddress`,`branchcontactman`,`branchphone`,`branchmobile`,`branchfax`,`branchemail`,`contractflag`,`contractrate`,`branchtypeflag`,`cwbtobranchid`,`branchcode`,`payfeeupdateflag`,`backtodeliverflag`,`branchpaytoheadflag`,`branchfinishdayflag`,`creditamount`,`branchwavfile`,`brancheffectflag`,`noemailimportflag`,`errorcwbdeliverflag`,`errorcwbbranchflag`,`branchcodewavfile`,`importwavtype`,`exportwavtype`,`branchinsurefee`,`branchprovince`,`branchcity`,`noemaildeliverflag`,`sendstartbranchid`,`functionids`,`sitetype`,`checkremandtype`,`branchmatter`,`accountareaid`,`arrearagehuo`,`arrearagepei`,`arrearagefa`) values (1,'武汉站',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,'0.00','0.00','0.00'),(2,'上海站',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,'0.00','0.00','0.00'),(3,'北京站',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,'0.00','0.00','0.00');

CREATE TABLE `express_set_common` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `commonname` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `commonnumber` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `orderprefix` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `commonstate` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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
  PRIMARY KEY (`customerid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert  into `express_set_customer_info`(`customerid`,`customername`,`customerno`,`customeraddress`,`customerpostcode`,`customercontactman`,`customerphone`,`customermobile`,`customerfax`,`paywayid`,`paystartday`,`payendday`,`customerlevelid`,`customerremark`,`customercreateuserid`,`customercreatetime`,`customerpassword`,`paytransfeeid`,`monthflag`,`monthtypeid`,`startday`,`endday`,`deposit`,`carsplit`,`transfeecreateid`,`commissionfeecreateid`,`customercode`,`customertypeindmp`,`custprovinceid`,`custeffectflag`,`messagecustomername`,`clearwayid`,`clearcycleid`,`transfeeclearcycleid`,`transfeetypeid`,`chargefeerate`,`poschargefeerate`,`customerno2`,`customerno3`,`customerno4`,`customerno5`,`customerpassword2`,`customerpassword3`,`customerpassword4`,`customerpassword5`,`accountbank`,`accountbankno`,`accountname`,`isBackAskFlag`,`ifeffectflag`,`payaccountcode`,`isBeforeScanningflag`,`customer_pos_code`) values (1,'一统','','一统','','','',NULL,'',NULL,NULL,NULL,0,NULL,1,'2009-07-14 11:18:41.170',NULL,1,'1',1,NULL,NULL,'0.00',NULL,1,1,'000',NULL,NULL,NULL,NULL,1,1,1,1,'0.0000','0.0000',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,NULL,0,NULL),(2,'凡客',NULL,'凡客',NULL,'111','111',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(3,'京东',NULL,'京东',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(4,'当当',NULL,'当当',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

CREATE TABLE `express_set_customer_warehouse` (
  `warehouseid` int(11) NOT NULL AUTO_INCREMENT,
  `customerid` int(11) NOT NULL,
  `customerwarehouse` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `warehouseremark` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `ifeffectflag` int(11) DEFAULT NULL,
  PRIMARY KEY (`warehouseid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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
  `shippernameindex` int(11) DEFAULT NULL,
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
  `commonnumberindex` int(11) DEFAULT NULL,
  PRIMARY KEY (`columnid`,`sendcargonameindex`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_function` (
  `functionid` int(11) NOT NULL AUTO_INCREMENT COMMENT '功能对应表id',
  `functionname` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '功能名称',
  `menuid` int(11) NOT NULL COMMENT '功能对应的菜单id',
  `type` int(4) NOT NULL COMMENT '0为机构拥有的功能',
  PRIMARY KEY (`functionid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_importset` (
  `importid` int(11) NOT NULL AUTO_INCREMENT,
  `importtypeid` int(11) DEFAULT NULL,
  `importtype` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `importsetflag` int(11) DEFAULT NULL,
  PRIMARY KEY (`importid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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
) ENGINE=InnoDB AUTO_INCREMENT=1102 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert  into `express_set_menu_info_new`(`menuid`,`menuname`,`menuurl`,`menulevel`,`menuno`,`parentno`,`menuimage`,`adminshowflag`) values (1095,'数据导出','/exportcwb/view','3','A40202','1098',NULL,NULL),(1096,'高级查询','/advancedquery/list/1','3','A10102','1067','images/gjcx.png',NULL),(1097,'预警','/order/earlywarning/1','3','A10103','1067','images/yujing.png',NULL),(1098,'批量修改','/order/batchedit','3','A10104','1067','images/plxg.png',NULL),(1099,'批量修改运单号','/order/transcwbbatchedit','3','A10105','1067','images/plxghdh.png',NULL),(1100,'退货管理','/order/backgoods/1','3','A10106','1067','images/thgl.png',NULL),(1101,'退货批量审核','/order/backgoodsbatch/1','3','A10107','1067','images/thplsh.png',NULL);

CREATE TABLE `express_set_pos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cwb` varchar(50) DEFAULT NULL,
  `pos_document` varchar(255) DEFAULT NULL,
  `pos_payname` varchar(50) DEFAULT NULL,
  `pos_money` decimal(10,2) DEFAULT '0.00',
  `pos_signtype` int(11) DEFAULT '0',
  `pos_signname` varchar(50) DEFAULT NULL,
  `pos_signtime` varchar(50) DEFAULT NULL,
  `pos_code` varchar(500) DEFAULT NULL,
  `pos_signremark` varchar(500) DEFAULT NULL,
  `pos_paydate` varchar(50) DEFAULT NULL,
  `pos_remark` varchar(500) DEFAULT NULL,
  `pos_delivery` int(11) DEFAULT NULL,
  `pos_deliveryname` varchar(50) DEFAULT NULL,
  `pos_upbranchid` int(11) DEFAULT NULL,
  `pos_branchid` int(11) DEFAULT NULL,
  `pos_branchname` varchar(255) DEFAULT NULL,
  `pos_backoutflag` int(4) DEFAULT '0',
  `customerid` bigint(11) DEFAULT NULL,
  `customername` varchar(50) DEFAULT NULL,
  `shiptime` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `express_set_proxy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `port` int(11) DEFAULT '0',
  `type` int(11) DEFAULT '0',
  `state` int(4) DEFAULT '1',
  `ip` varchar(255) DEFAULT NULL,
  `with` int(4) DEFAULT '1',
  `isnoDefault` int(4) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

CREATE TABLE `express_set_reason` (
  `reasonid` int(10) NOT NULL AUTO_INCREMENT,
  `reasoncontent` varchar(800) COLLATE utf8_bin DEFAULT NULL,
  `reasontype` int(10) DEFAULT '0',
  PRIMARY KEY (`reasonid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_role_menu_new` (
  `roleid` int(11) NOT NULL,
  `menuid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert  into `express_set_role_menu_new`(`roleid`,`menuid`) values (1,1095),(1,1096),(1,1097),(1,1098),(1,1099),(1,1100);

CREATE TABLE `express_set_role_new` (
  `roleid` int(11) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `type` int(2) DEFAULT '1',
  PRIMARY KEY (`roleid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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

CREATE TABLE `express_set_smsconfig` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `warningcount` int(11) DEFAULT NULL,
  `phone` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `templet` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `monitor` int(2) DEFAULT '1',
  `warningcontent` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `isOpen` int(2) DEFAULT '0',
  `templatecontent` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `express_set_truck` (
  `truckid` int(10) NOT NULL AUTO_INCREMENT,
  `truckno` varchar(50) COLLATE utf8_bin DEFAULT '0',
  `trucktype` varchar(50) COLLATE utf8_bin DEFAULT '0',
  `truckoil` double DEFAULT '0',
  `truckway` varchar(50) COLLATE utf8_bin DEFAULT '0',
  `truckkm` double DEFAULT '0',
  `truckstartkm` double DEFAULT '0',
  `truckdriver` varchar(50) COLLATE utf8_bin DEFAULT '0',
  `truckflag` varchar(2) COLLATE utf8_bin DEFAULT '0',
  `truckstate` int(11) DEFAULT '1',
  PRIMARY KEY (`truckid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
