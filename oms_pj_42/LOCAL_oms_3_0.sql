/*
SQLyog Ultimate v9.62 
MySQL - 5.1.62-community : Database - peisongdata_oms_test
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`peisongdata_oms_test` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `peisongdata_oms_test`;

/*Table structure for table `express_branch_payamount` */

DROP TABLE IF EXISTS `express_branch_payamount`;

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
  PRIMARY KEY (`branchpayid`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;

/*Data for the table `express_branch_payamount` */

/*Table structure for table `express_cs_complaint` */

DROP TABLE IF EXISTS `express_cs_complaint`;

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
  PRIMARY KEY (`complaintid`),
  KEY `complaintid` (`complaintid`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

/*Data for the table `express_cs_complaint` */

/*Table structure for table `express_ops_createselect_term` */

DROP TABLE IF EXISTS `express_ops_createselect_term`;

CREATE TABLE `express_ops_createselect_term` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `termname` varchar(225) COLLATE utf8_bin DEFAULT NULL,
  `termContent` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_ops_createselect_term` */

/*Table structure for table `express_ops_cwb_detail` */

DROP TABLE IF EXISTS `express_ops_cwb_detail`;

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
  `customerwarehouseid` varchar(2) COLLATE utf8_bin DEFAULT '0',
  `cwbordertypeid` varchar(2) COLLATE utf8_bin DEFAULT '-1',
  `cwbdelivertypeid` varchar(2) COLLATE utf8_bin DEFAULT '-1',
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
  PRIMARY KEY (`opscwbid`),
  KEY `detail_cwb_idx` (`cwb`)
) ENGINE=InnoDB AUTO_INCREMENT=1515 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_ops_cwb_detail` */

/*Table structure for table `express_ops_cwb_error` */

DROP TABLE IF EXISTS `express_ops_cwb_error`;

CREATE TABLE `express_ops_cwb_error` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cwb` varchar(100) COLLATE utf8_bin DEFAULT '',
  `cwbdetail` text COLLATE utf8_bin,
  `state` int(1) DEFAULT '0',
  `emaildate` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `message` varchar(50) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=130 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_ops_cwb_error` */

/*Table structure for table `express_ops_delivery_state` */

DROP TABLE IF EXISTS `express_ops_delivery_state`;

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
  `posremark` varchar(100) COLLATE utf8_bin DEFAULT NULL,
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
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_ops_delivery_state` */

/*Table structure for table `express_ops_emaildate` */

DROP TABLE IF EXISTS `express_ops_emaildate`;

CREATE TABLE `express_ops_emaildate` (
  `emaildateid` int(10) NOT NULL AUTO_INCREMENT,
  `emaildatetime` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `state` int(1) DEFAULT '0',
  `warehouseid` int(11) DEFAULT '0',
  `customerid` int(11) DEFAULT '0',
  PRIMARY KEY (`emaildateid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_ops_emaildate` */

/*Table structure for table `express_ops_exportmould` */

DROP TABLE IF EXISTS `express_ops_exportmould`;

CREATE TABLE `express_ops_exportmould` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `roleid` int(11) DEFAULT NULL,
  `mouldname` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `mouldfieldids` varchar(225) COLLATE utf8_bin DEFAULT NULL,
  `status` int(2) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_ops_exportmould` */

/*Table structure for table `express_ops_goto_class_auditing` */

DROP TABLE IF EXISTS `express_ops_goto_class_auditing`;

CREATE TABLE `express_ops_goto_class_auditing` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `auditingtime` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `payupamount` decimal(9,2) DEFAULT NULL,
  `receivedfeeuser` int(11) DEFAULT NULL,
  `branchid` int(11) DEFAULT NULL,
  `payupid` int(11) DEFAULT NULL,
  `classid` int(11) DEFAULT NULL,
  `gobackidstr` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_ops_goto_class_auditing` */

/*Table structure for table `express_ops_operatelog` */

DROP TABLE IF EXISTS `express_ops_operatelog`;

CREATE TABLE `express_ops_operatelog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `operateman` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `operatetime` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `cwb` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `transcwb` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `operateremarks` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=147 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_ops_operatelog` */

/*Table structure for table `express_ops_order_flow` */

DROP TABLE IF EXISTS `express_ops_order_flow`;

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
  PRIMARY KEY (`floworderid`)
) ENGINE=InnoDB AUTO_INCREMENT=1513 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_ops_order_flow` */

/*Table structure for table `express_ops_outwarehousegroup` */

DROP TABLE IF EXISTS `express_ops_outwarehousegroup`;

CREATE TABLE `express_ops_outwarehousegroup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `credate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `driverid` int(11) DEFAULT '0',
  `truckid` int(11) DEFAULT '0',
  `state` int(2) DEFAULT '0',
  `branchid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_ops_outwarehousegroup` */

/*Table structure for table `express_ops_setexportfield` */

DROP TABLE IF EXISTS `express_ops_setexportfield`;

CREATE TABLE `express_ops_setexportfield` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fieldname` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `exportstate` int(11) DEFAULT '0',
  `fieldenglishname` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_ops_setexportfield` */

insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (1,'承运商',0,'Commonname'),(2,'供货商',0,'Customername'),(3,'订单号',0,'Cwb'),(4,'运单号',0,'Transcwb'),(5,'收件人名称',0,'Consigneename'),(6,'收件人地址',0,'Consigneeaddress'),(7,'收件人邮编',0,'Consigneepostcode'),(8,'收件人电话',0,'Consigneephone'),(9,'收件人手机',0,'Consigneemobile'),(10,'发出商品名称',0,'Sendcarname'),(11,'取回商品名称',0,'Backcarname'),(12,'货物重量',0,'Carrealweight'),(13,'代收货款应收金额',0,'Carrealweight'),(14,'订单备注',0,'Cwbremark'),(15,'指定小件员',0,'Exceldeliver'),(16,'指定派送分站',0,'Excelbranch'),(17,'配送单号',0,'Shipcwb'),(18,'收件人编号',0,'Consigneeno'),(19,'货物金额',0,'Caramount'),(20,'客户要求',0,'Customercommand'),(21,'货物类型',0,'Cartype'),(22,'货物尺寸',0,'Carsize'),(23,'取回货物金额',0,'Backcaramount'),(25,'目的地',0,'Destination'),(26,'运输方式',0,'Transway'),(27,'退供货商承运商',0,'Shipperid'),(28,'发货数量',0,'Sendcarnum'),(29,'取货数量',0,'Backcarnum'),(30,'省',0,'Cwbprovince'),(31,'市',0,'Cwbcity'),(32,'区县',0,'Cwbcounty'),(33,'发货仓库',0,'Carwarehouse'),(34,'订单类型',0,'Cwbordertypeid'),(36,'修改时间',0,'Edittime'),(38,'订单状态',0,'FlowordertypeMethod'),(39,'审核人',0,'Auditor'),(40,'退货备注',0,'Returngoodsremark'),(41,'最新跟踪状态',0,'Newfollownotes'),(42,'标记人',0,'Marksflagmen'),(43,'订单原始金额',0,'Primitivemoney'),(45,'签收时间',0,'Signintime'),(46,'修改的签收时间',0,'Sditsignintime'),(47,'签收人',0,'Signinman'),(48,'修改人',0,'Editman');

/*Table structure for table `express_ops_stock_detail` */

DROP TABLE IF EXISTS `express_ops_stock_detail`;

CREATE TABLE `express_ops_stock_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cwb` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `branchid` int(11) DEFAULT '0',
  `type` int(11) DEFAULT '2',
  `orderflowid` int(11) DEFAULT NULL,
  `resultid` int(11) DEFAULT NULL,
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_ops_stock_detail` */

/*Table structure for table `express_ops_stock_result` */

DROP TABLE IF EXISTS `express_ops_stock_result`;

CREATE TABLE `express_ops_stock_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `branchid` int(11) DEFAULT '0',
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `state` int(11) DEFAULT '0',
  `checkcount` int(11) DEFAULT '0',
  `realcount` int(11) DEFAULT '0',
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_ops_stock_result` */

/*Table structure for table `express_set_account_area` */

DROP TABLE IF EXISTS `express_set_account_area`;

CREATE TABLE `express_set_account_area` (
  `areaid` int(11) NOT NULL AUTO_INCREMENT,
  `areaname` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `arearemark` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `customerid` int(11) DEFAULT NULL,
  `isEffectFlag` int(11) DEFAULT NULL,
  PRIMARY KEY (`areaid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_account_area` */

/*Table structure for table `express_set_address_code_branch` */

DROP TABLE IF EXISTS `express_set_address_code_branch`;

CREATE TABLE `express_set_address_code_branch` (
  `branchcodeid` int(11) NOT NULL AUTO_INCREMENT,
  `branchaddresscode` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `branchaddressdetail` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `branchid` int(11) DEFAULT NULL,
  PRIMARY KEY (`branchcodeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_address_code_branch` */

/*Table structure for table `express_set_address_code_branch_line` */

DROP TABLE IF EXISTS `express_set_address_code_branch_line`;

CREATE TABLE `express_set_address_code_branch_line` (
  `linecodeid` int(11) NOT NULL AUTO_INCREMENT,
  `branchid` int(11) DEFAULT NULL,
  `linecode` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `linename` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`linecodeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_address_code_branch_line` */

/*Table structure for table `express_set_address_code_deliver` */

DROP TABLE IF EXISTS `express_set_address_code_deliver`;

CREATE TABLE `express_set_address_code_deliver` (
  `delivercodeid` int(11) NOT NULL AUTO_INCREMENT,
  `deliveraddresscode` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `deliveraddressdetail` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `deliverid` int(11) DEFAULT NULL,
  `linecodeid` int(11) DEFAULT NULL,
  PRIMARY KEY (`delivercodeid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_address_code_deliver` */

/*Table structure for table `express_set_address_db_start` */

DROP TABLE IF EXISTS `express_set_address_db_start`;

CREATE TABLE `express_set_address_db_start` (
  `addressid` int(11) NOT NULL AUTO_INCREMENT,
  `addressdbflag` int(11) DEFAULT NULL,
  PRIMARY KEY (`addressid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_address_db_start` */

/*Table structure for table `express_set_address_key_word_branch` */

DROP TABLE IF EXISTS `express_set_address_key_word_branch`;

CREATE TABLE `express_set_address_key_word_branch` (
  `branchkeyid` int(11) NOT NULL AUTO_INCREMENT,
  `branchcodeid` int(11) DEFAULT NULL,
  `branchaddresskeyword` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `cityid` int(11) DEFAULT NULL,
  `branchid` int(11) DEFAULT NULL,
  PRIMARY KEY (`branchkeyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_address_key_word_branch` */

/*Table structure for table `express_set_address_key_word_branch_second` */

DROP TABLE IF EXISTS `express_set_address_key_word_branch_second`;

CREATE TABLE `express_set_address_key_word_branch_second` (
  `branchkeysecondid` int(11) NOT NULL AUTO_INCREMENT,
  `branchkeyid` int(11) DEFAULT NULL,
  `branchaddresskeywordsecond` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`branchkeysecondid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_address_key_word_branch_second` */

/*Table structure for table `express_set_address_key_word_branch_third` */

DROP TABLE IF EXISTS `express_set_address_key_word_branch_third`;

CREATE TABLE `express_set_address_key_word_branch_third` (
  `branchkeythirdid` int(11) NOT NULL AUTO_INCREMENT,
  `branchkeysecondid` int(11) DEFAULT NULL,
  `branchaddresskeywordthird` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`branchkeythirdid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_address_key_word_branch_third` */

/*Table structure for table `express_set_address_key_word_deliver` */

DROP TABLE IF EXISTS `express_set_address_key_word_deliver`;

CREATE TABLE `express_set_address_key_word_deliver` (
  `deliverkeyid` int(11) NOT NULL AUTO_INCREMENT,
  `delivercodeid` int(11) NOT NULL,
  `deliveraddresskeyword` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`deliverkeyid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_address_key_word_deliver` */

/*Table structure for table `express_set_branch` */

DROP TABLE IF EXISTS `express_set_branch`;

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

/*Data for the table `express_set_branch` */

insert  into `express_set_branch`(`branchid`,`branchname`,`branchaddress`,`branchcontactman`,`branchphone`,`branchmobile`,`branchfax`,`branchemail`,`contractflag`,`contractrate`,`branchtypeflag`,`cwbtobranchid`,`branchcode`,`payfeeupdateflag`,`backtodeliverflag`,`branchpaytoheadflag`,`branchfinishdayflag`,`creditamount`,`branchwavfile`,`brancheffectflag`,`noemailimportflag`,`errorcwbdeliverflag`,`errorcwbbranchflag`,`branchcodewavfile`,`importwavtype`,`exportwavtype`,`branchinsurefee`,`branchprovince`,`branchcity`,`noemaildeliverflag`,`sendstartbranchid`,`functionids`,`sitetype`,`checkremandtype`,`branchmatter`,`accountareaid`,`arrearagehuo`,`arrearagepei`,`arrearagefa`) values (1,'武汉站',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,'0.00','0.00','0.00'),(2,'上海站',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,'0.00','0.00','0.00'),(3,'北京站',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL,NULL,NULL,NULL,'0.00','0.00','0.00');

/*Table structure for table `express_set_common` */

DROP TABLE IF EXISTS `express_set_common`;

CREATE TABLE `express_set_common` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `commonname` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `commonnumber` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `orderprefix` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `commonstate` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_common` */

/*Table structure for table `express_set_customer_info` */

DROP TABLE IF EXISTS `express_set_customer_info`;

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

/*Data for the table `express_set_customer_info` */

insert  into `express_set_customer_info`(`customerid`,`customername`,`customerno`,`customeraddress`,`customerpostcode`,`customercontactman`,`customerphone`,`customermobile`,`customerfax`,`paywayid`,`paystartday`,`payendday`,`customerlevelid`,`customerremark`,`customercreateuserid`,`customercreatetime`,`customerpassword`,`paytransfeeid`,`monthflag`,`monthtypeid`,`startday`,`endday`,`deposit`,`carsplit`,`transfeecreateid`,`commissionfeecreateid`,`customercode`,`customertypeindmp`,`custprovinceid`,`custeffectflag`,`messagecustomername`,`clearwayid`,`clearcycleid`,`transfeeclearcycleid`,`transfeetypeid`,`chargefeerate`,`poschargefeerate`,`customerno2`,`customerno3`,`customerno4`,`customerno5`,`customerpassword2`,`customerpassword3`,`customerpassword4`,`customerpassword5`,`accountbank`,`accountbankno`,`accountname`,`isBackAskFlag`,`ifeffectflag`,`payaccountcode`,`isBeforeScanningflag`,`customer_pos_code`) values (1,'一统','','一统','','','',NULL,'',NULL,NULL,NULL,0,NULL,1,'2009-07-14 11:18:41.170',NULL,1,'1',1,NULL,NULL,'0.00',NULL,1,1,'000',NULL,NULL,NULL,NULL,1,1,1,1,'0.0000','0.0000',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,NULL,0,NULL),(2,'凡客',NULL,'凡客',NULL,'111','111',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(3,'京东',NULL,'京东',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(4,'当当',NULL,'当当',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `express_set_customer_warehouse` */

DROP TABLE IF EXISTS `express_set_customer_warehouse`;

CREATE TABLE `express_set_customer_warehouse` (
  `warehouseid` int(11) NOT NULL AUTO_INCREMENT,
  `customerid` int(11) NOT NULL,
  `customerwarehouse` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `warehouseremark` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `ifeffectflag` int(11) DEFAULT NULL,
  PRIMARY KEY (`warehouseid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_customer_warehouse` */

/*Table structure for table `express_set_depart` */

DROP TABLE IF EXISTS `express_set_depart`;

CREATE TABLE `express_set_depart` (
  `departid` int(11) NOT NULL AUTO_INCREMENT,
  `departname` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `departremark` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`departid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_depart` */

/*Table structure for table `express_set_exceed_fee` */

DROP TABLE IF EXISTS `express_set_exceed_fee`;

CREATE TABLE `express_set_exceed_fee` (
  `exceedid` int(11) NOT NULL AUTO_INCREMENT,
  `exceedfee` decimal(18,2) DEFAULT NULL,
  PRIMARY KEY (`exceedid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_exceed_fee` */

/*Table structure for table `express_set_excel_column` */

DROP TABLE IF EXISTS `express_set_excel_column`;

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

/*Data for the table `express_set_excel_column` */

/*Table structure for table `express_set_function` */

DROP TABLE IF EXISTS `express_set_function`;

CREATE TABLE `express_set_function` (
  `functionid` int(11) NOT NULL AUTO_INCREMENT COMMENT '功能对应表id',
  `functionname` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '功能名称',
  `menuid` int(11) NOT NULL COMMENT '功能对应的菜单id',
  `type` int(4) NOT NULL COMMENT '0为机构拥有的功能',
  PRIMARY KEY (`functionid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_function` */

/*Table structure for table `express_set_importset` */

DROP TABLE IF EXISTS `express_set_importset`;

CREATE TABLE `express_set_importset` (
  `importid` int(11) NOT NULL AUTO_INCREMENT,
  `importtypeid` int(11) DEFAULT NULL,
  `importtype` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `importsetflag` int(11) DEFAULT NULL,
  PRIMARY KEY (`importid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_importset` */

/*Table structure for table `express_set_menu_info_new` */

DROP TABLE IF EXISTS `express_set_menu_info_new`;

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

/*Data for the table `express_set_menu_info_new` */

insert  into `express_set_menu_info_new`(`menuid`,`menuname`,`menuurl`,`menulevel`,`menuno`,`parentno`,`menuimage`,`adminshowflag`) values (1095,'数据导出','/exportcwb/view','3','A40202','1098',NULL,NULL),(1096,'高级查询','/advancedquery/list/1','3','A10102','1067','images/gjcx.png',NULL),(1097,'预警','/order/earlywarning/1','3','A10103','1067','images/yujing.png',NULL),(1098,'批量修改','/order/batchedit','3','A10104','1067','images/plxg.png',NULL),(1099,'批量修改运单号','/order/transcwbbatchedit','3','A10105','1067','images/plxghdh.png',NULL),(1100,'退货管理','/order/backgoods/1','3','A10106','1067','images/thgl.png',NULL),(1101,'退货批量审核','/order/backgoodsbatch/1','3','A10107','1067','images/thplsh.png',NULL);

/*Table structure for table `express_set_proxy` */

DROP TABLE IF EXISTS `express_set_proxy`;

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

/*Data for the table `express_set_proxy` */

/*Table structure for table `express_set_reason` */

DROP TABLE IF EXISTS `express_set_reason`;

CREATE TABLE `express_set_reason` (
  `reasonid` int(10) NOT NULL AUTO_INCREMENT,
  `reasoncontent` varchar(800) COLLATE utf8_bin DEFAULT NULL,
  `reasontype` int(10) DEFAULT '0',
  PRIMARY KEY (`reasonid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_reason` */

/*Table structure for table `express_set_role_menu_new` */

DROP TABLE IF EXISTS `express_set_role_menu_new`;

CREATE TABLE `express_set_role_menu_new` (
  `roleid` int(11) NOT NULL,
  `menuid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_role_menu_new` */

insert  into `express_set_role_menu_new`(`roleid`,`menuid`) values (1,1095),(1,1096),(1,1097),(1,1098),(1,1099),(1,1100);

/*Table structure for table `express_set_role_new` */

DROP TABLE IF EXISTS `express_set_role_new`;

CREATE TABLE `express_set_role_new` (
  `roleid` int(11) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `type` int(2) DEFAULT '1',
  PRIMARY KEY (`roleid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_role_new` */

/*Table structure for table `express_set_servicearea` */

DROP TABLE IF EXISTS `express_set_servicearea`;

CREATE TABLE `express_set_servicearea` (
  `serviceareaid` int(11) NOT NULL AUTO_INCREMENT,
  `serviceareaname` varchar(50) COLLATE utf8_bin NOT NULL,
  `servicearearemark` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `customerid` int(11) DEFAULT NULL,
  `servid` varchar(2) COLLATE utf8_bin DEFAULT '1',
  PRIMARY KEY (`serviceareaid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_servicearea` */

/*Table structure for table `express_set_shipper` */

DROP TABLE IF EXISTS `express_set_shipper`;

CREATE TABLE `express_set_shipper` (
  `shipperid` int(11) NOT NULL AUTO_INCREMENT,
  `shipperno` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `shippername` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `shipperurl` varchar(500) COLLATE utf8_bin DEFAULT NULL,
  `shipperremark` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `paywayid` int(11) DEFAULT NULL,
  PRIMARY KEY (`shipperid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_shipper` */

/*Table structure for table `express_set_smsconfig` */

DROP TABLE IF EXISTS `express_set_smsconfig`;

CREATE TABLE `express_set_smsconfig` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `warningcount` int(11) DEFAULT NULL,
  `phone` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `templet` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `monitor` int(2) DEFAULT '1',
  `warningcontent` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_smsconfig` */

/*Table structure for table `express_set_truck` */

DROP TABLE IF EXISTS `express_set_truck`;

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

/*Data for the table `express_set_truck` */

/*Table structure for table `express_set_user` */

DROP TABLE IF EXISTS `express_set_user`;

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


/*Data for the table `express_set_user` */
/* 2012-09-06 zpk-[11:00:44][111 ms]*/
ALTER TABLE `express_ops_order_flow` ADD COLUMN `customername` VARCHAR(50) NULL COMMENT '供货商' AFTER `emaildateid`; 



/*[10:20:52][100 ms] ld--添加入库时间字段detail*/
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `instoreroomtime` VARCHAR(80) NULL AFTER `reserve1`; 
/*20120907 2109 新增导出，审核字段标识*/
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `exportType` INT(4) DEFAULT 0 NULL COMMENT '导出状态 0 未导出 1 已导出 2 导出已审核' AFTER `instoreroomtime`, ADD COLUMN `exportBackType` INT(4) DEFAULT 0 NULL COMMENT '导出状态 0 未导出 1 已导出 2 导出已审核' AFTER `exportType`; 

/*09-09 添加新字段*/
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `remark1` VARCHAR(1000) NULL;
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `remark2` VARCHAR(1000) NULL;
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `remark3` VARCHAR(1000) NULL;
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `remark4` VARCHAR(1000) NULL;
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `remark5` VARCHAR(1000) NULL;
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `startbranchname` VARCHAR(50) NULL;
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `nextbranchname` VARCHAR(50) NULL;
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `outstoreroomtime` VARCHAR(50) NULL;
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `inSitetime` VARCHAR(50) NULL;
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `pickGoodstime` VARCHAR(50) NULL;
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `sendSuccesstime` VARCHAR(50) NULL;
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `gobacktime` VARCHAR(50) NULL;
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `nowtime` VARCHAR(50) NULL;
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `inhouse` VARCHAR(1000) NULL;
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `realweight` DECIMAL(9,2) DEFAULT 0.00 NULL;
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `goodsremark` VARCHAR(50) NULL;
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `paytype` VARCHAR(50) NULL;
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `goclasstime` VARCHAR(50) NULL;
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `leavedreasonStr` VARCHAR(500) NULL;
ALTER TABLE `express_set_smsconfig` ADD COLUMN `isOpen` INT(2) DEFAULT 0 NULL AFTER `warningcontent`; 

/*09 -10 [15:14:19][693 ms] 加两个字段*/ 
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `customerwarehousename` VARCHAR(100) NULL AFTER `paytype`, ADD COLUMN `carwarehousename` VARCHAR(100) NULL AFTER `customerwarehousename`; 

/*以下 都是还没上到服务器上*/
/*09-11 [13:32:03][497 ms] 加当前站点和下一站点 ，目标站点，入库库房*/ 
ALTER TABLE `express_ops_order_flow` ADD COLUMN `startbranchid` INT(11) NULL AFTER `customername`, ADD COLUMN `nextbranchid` INT(11) NULL AFTER `startbranchid`, ADD COLUMN `excelbranchid` INT(11) NULL AFTER `nextbranchid`, ADD COLUMN `carwarehouse` VARCHAR(20) NULL AFTER `excelbranchid`; 

/*09-13 [9:59:28][185 ms] 订单表加反馈表字段*/ 
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `fdeliverid` INT(11) NULL AFTER `leavedreasonStr`, ADD COLUMN `fdelivername` VARCHAR(50) NULL AFTER `fdeliverid`, ADD COLUMN `receivedfee` DECIMAL(10,2) DEFAULT 0.00 NULL AFTER `fdelivername`, ADD COLUMN `returnedfee` DECIMAL(10,2) DEFAULT 0.00 NULL AFTER `receivedfee`, ADD COLUMN `businessfee` DECIMAL(10,2) DEFAULT 0.00 NULL AFTER `returnedfee`, ADD COLUMN `deliverystate` INT(11) NULL AFTER `businessfee`, ADD COLUMN `cash` DECIMAL(10,2) DEFAULT 0.00 NULL AFTER `deliverystate`, ADD COLUMN `pos` DECIMAL(10,2) DEFAULT 0.00 NULL AFTER `cash`, ADD COLUMN `posremark` VARCHAR(255) NULL AFTER `pos`, ADD COLUMN `mobilepodtime` TIMESTAMP NULL AFTER `posremark`, ADD COLUMN `checkfee` DECIMAL(10,2) DEFAULT 0.00 NULL AFTER `mobilepodtime`, ADD COLUMN `checkremark` VARCHAR(255) NULL AFTER `checkfee`, ADD COLUMN `receivedfeeuser` INT(11) NULL AFTER `checkremark`, ADD COLUMN `statisticstate` INT(11) NULL AFTER `receivedfeeuser`, ADD COLUMN `createtime` VARCHAR(50) NULL AFTER `statisticstate`, ADD COLUMN `otherfee` DECIMAL(10,2) DEFAULT 0.00 NULL AFTER `createtime`, ADD COLUMN `podremarkid` INT(10) NULL AFTER `otherfee`, ADD COLUMN `deliverstateremark` VARCHAR(1000) NULL AFTER `podremarkid`, ADD COLUMN `gcaid` INT(11) NULL AFTER `deliverstateremark`, ADD COLUMN `gobackid` INT(11) NULL AFTER `gcaid`, ADD COLUMN `payupbranchid` INT(11) NULL AFTER `gobackid`; 
/*09-13 [10:25:07][97 ms] 订单表加反馈表字段*/ 
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `payupbranchname` VARCHAR(255) NULL AFTER `payupbranchid`, ADD COLUMN `podremarkStr` VARCHAR(255) NULL AFTER `payupbranchname`, ADD COLUMN `receivedfeeuserName` VARCHAR(255) NULL AFTER `podremarkStr`, ADD COLUMN `payuprealname` VARCHAR(20) NULL AFTER `receivedfeeuserName`; 
/*09-14 [16:09:55][151 ms]  订单表加 站点统计字段*/ 
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `youdanwuhuoBranchid` INT(11) NULL AFTER `payuprealname`, ADD COLUMN `youhuowudanBranchid` INT(11) NULL AFTER `youdanwuhuoBranchid`, ADD COLUMN `tuotouTime` INT(11) NULL AFTER `youhuowudanBranchid`, ADD COLUMN `youjieguoTime` INT(11) NULL AFTER `tuotouTime`; 
/*09-14 [20:13:53][157 ms]  订单表加 站点统计字段*/ 
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `rukutuotouTime` INT(11) NULL AFTER `youjieguoTime`, ADD COLUMN `rukuyoujieguoTime` INT(11) NULL AFTER `rukutuotouTime`, ADD COLUMN `daozhantuotouTime` INT(11) NULL AFTER `rukuyoujieguoTime`, ADD COLUMN `daozhanyoujieguoTime` INT(11) NULL AFTER `daozhantuotouTime`; 

/*[09-16 18:48:00][847 ms] 添加未交款和欠款状态字段*/ 
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `ispayUp` INT(4) DEFAULT 0 NULL AFTER `daozhanyoujieguoTime`, ADD COLUMN `isQiankuan` INT(4) DEFAULT 0 NULL AFTER `ispayUp`; 

/*[17:24:31][372 ms] 交款表里加 财务id*/ 
ALTER TABLE `express_branch_payamount` ADD COLUMN `upbranchid` INT(11) DEFAULT 0 NULL AFTER `payuprealname`; 
/*[20:39:02][792 ms]  添加上交款方式和交款类型*/ 
ALTER TABLE `express_branch_payamount` ADD COLUMN `payup_type` INT(11) NULL AFTER `upbranchid`, ADD COLUMN `way` INT(11) NULL AFTER `payup_type`; 
/*[22:12:55][687 ms]  添加反馈表小件员姓名字段*/ 
ALTER TABLE `express_ops_delivery_state` ADD COLUMN `deliveryName` VARCHAR(50) NULL AFTER `branchid`; 
/*[2:39:40][1622 ms] 添加退货再投审核字段*/ 
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `auditEganstate` INT(11) DEFAULT 0 NULL AFTER `daozhanyoujieguoTime`; 
DROP TABLE IF EXISTS `express_set_pos`;
/*新增一个ｐｏｓ反馈信息表*/
CREATE TABLE `express_set_pos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cwb` varchar(50) DEFAULT NULL,
  `pos_document` varchar(255) DEFAULT NULL,
  `pos_payname` varchar(50) DEFAULT NULL,
  `pos_money` decimal(10,2) DEFAULT '0.00',
  `pos_signtype` int(11) DEFAULT '0',
  `pos_paydate` varchar(50) DEFAULT NULL,
  `pos_remark` varchar(500) DEFAULT NULL,
  `pos_delivery` int(11) DEFAULT NULL,
  `pos_deliveryname` varchar(50) DEFAULT NULL,
  `pos_upbranchid` int(11) DEFAULT NULL,
  `pos_branchid` int(11) DEFAULT NULL,
  `pos_branchname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/* 2012-09-18 [16:24:34][0 ms] set_pos表新增字段*/
ALTER TABLE `express_set_pos` ADD COLUMN `pos_signname` VARCHAR(50) NULL AFTER `pos_signtype`, 
ADD COLUMN `pos_signtime` VARCHAR(50) NULL AFTER `pos_signname`,ADD COLUMN `pos_signremark` VARCHAR(500) NULL AFTER `pos_signtime`,
ADD COLUMN `pos_code` VARCHAR(500) NULL AFTER `pos_signtime`; 

/*====================================9-19===============================================*/



/* 2012-09-20 [14:42:01][38 ms] 新增当当对接反馈状态
 * ruku_dangdang_flag 当当入库状态(0未执行，1已执行，2，推送成功，3推送失败)
 * chuku_dangdang_flag    当当出库状态(0未执行，1已执行，2，推送成功，3推送失败)
 * deliverystate_dangdang_flag     当当派送反馈状态(0未执行，1已执行，2，推送成功，3推送失败)
 *  
 * */ 
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `ruku_dangdang_flag` INT(4) DEFAULT 0 NULL  AFTER `isQiankuan`,
ADD COLUMN `chuku_dangdang_flag` INT(4) DEFAULT 0 NULL  AFTER `ruku_dangdang_flag`,
ADD COLUMN `deliverystate_dangdang_flag` INT(4) DEFAULT 0 NULL  AFTER `chuku_dangdang_flag`; 
/*  2012-09-20[17:11:21][70 ms] 新增当当对接 当前操作员*/
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `operatorName` VARCHAR(50)  AFTER `deliverystate_dangdang_flag`; 


/*2012-09-20 [17:23:51][475 ms] 环节表添加 当前站名称，下一站名称，目的站名称 操作员名称*/ 
ALTER TABLE `express_ops_order_flow` ADD COLUMN `startbranchname` VARCHAR(255) NULL AFTER `carwarehouse`, ADD COLUMN `nextbranchname` VARCHAR(255) NULL AFTER `startbranchname`, ADD COLUMN `username` VARCHAR(255) NULL AFTER `nextbranchname`, ADD COLUMN `excelbranchname` VARCHAR(255) NULL AFTER `username`; 
ALTER TABLE `express_ops_order_flow` ADD COLUMN `deliverid` INT(11) NULL AFTER `excelbranchname`; 

/* 2012-09-20 [18:42:56][14 ms] 新增当当对接 推送状态  '0未推送，1推送成功，2，推送失败' */
ALTER TABLE `express_ops_order_flow` ADD COLUMN `send_b2c_flag` INT(4) DEFAULT 0 NULL 
AFTER `carwarehouse`; 
/*====================================9-20===============================================*/

/* 2012-09-21 [20:10:03][202 ms] 加小件员名称*/
ALTER TABLE `express_ops_order_flow` ADD COLUMN `delivername` VARCHAR(255) NULL AFTER `deliverid`; 

/*====================================9-24===============================================*/
 
ALTER TABLE `express_ops_cwb_detail` CHANGE `customerwarehouseid` `customerwarehouseid` VARCHAR(11) CHARSET utf8 COLLATE utf8_bin DEFAULT '0' NULL, CHANGE `cwbordertypeid` `cwbordertypeid` VARCHAR(11) CHARSET utf8 COLLATE utf8_bin DEFAULT '-1' NULL, CHANGE `cwbdelivertypeid` `cwbdelivertypeid` VARCHAR(11) CHARSET utf8 COLLATE utf8_bin DEFAULT '-1' NULL; 

/*2012-09-24 [20:04:39][926 ms] 订单表 新增俩字段，退货原因id和退货原因*/ 
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `backreasonid` INT(11) DEFAULT 0 NULL AFTER `operatorName`, ADD COLUMN `backreason` VARCHAR(500) DEFAULT '' NULL AFTER `backreasonid`; 


/* 2012-09-27 [10:29:33][42 ms] POS刷卡记录表新增字段pos_backoutflag '是否撤销（0不撤销。1已撤销）'*/ 
ALTER TABLE `express_set_pos` ADD COLUMN `pos_backoutflag` INT(4) DEFAULT 0 NULL  AFTER `pos_branchname`; 

/*  2012-09-27[11:20:20][51 ms] POS刷卡记录表新增字段customerid,customername,shiptime*/
ALTER TABLE `express_set_pos` ADD COLUMN `customerid` BIGINT(11) NULL AFTER `pos_backoutflag`,
ADD COLUMN `customername` VARCHAR(50) NULL AFTER `customerid`, 
ADD COLUMN `shiptime` VARCHAR(50) NULL AFTER `customername`; 

 
/*====================================9-27===============================================*/
/*====================================10-10===============================================*/


/*2012-10-12 [18:46:53][229 ms]  新增异常编码 ，异常原因  */
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `expt_code` VARCHAR(50) NULL AFTER `backreason`, ADD COLUMN `expt_msg` VARCHAR(255) NULL AFTER `expt_code`;


/*====================================10-13===============================================*/
/*2012-10-15 [16:48:12][157 ms] 短信账户添加一个字段，模板内容字段*/ 
ALTER TABLE `express_set_smsconfig` ADD COLUMN `templatecontent` VARCHAR(255) NULL AFTER `isOpen`; 



/*2012-10-16 [14:38:24][238 ms] 归班表修改字段长度，解决订单数过长的问题*/ 
ALTER TABLE `express_ops_goto_class_auditing` CHANGE `gobackidstr` `gobackidstr` LONGTEXT NULL; 
/* 2012-10-16 修改POSremark大小 为500[17:47:42][17 ms]*/
ALTER TABLE `express_ops_delivery_state` CHANGE `posremark` `posremark` VARCHAR(500) CHARSET utf8 COLLATE utf8_bin NULL; 

/*10-17 [14:11:08][230 ms] 订单流程添加一个字段 签收人*/
ALTER TABLE `express_ops_order_flow` ADD COLUMN `signinman` VARCHAR(100) NULL AFTER `delivername`; 

/*2012-10-18  detail表新增原始支付方式  paytype_old */
ALTER TABLE `express_ops_cwb_detail` ADD  `paytype_old` VARCHAR(20);
/*====================================10-18===============================================*/

/*[10-22 10:03:13][203 ms] 订单表加一个字段：配送结果字段*/ 
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `orderResultType` INT(4) DEFAULT 0 NULL AFTER `paytype_old`; 
/*====================================10-23===============================================*/
/*[12-10-30 16:52:53][29 ms] 修改表主键，加快检索速度*/ 
ALTER TABLE `express_ops_delivery_state` CHANGE COLUMN `cwb` `cwb` VARCHAR(100) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL  
, DROP PRIMARY KEY 
, ADD PRIMARY KEY (`id`) ;
ALTER TABLE `express_ops_delivery_state`   ADD INDEX `DeliveryStateIdx` (`cwb` ASC) ;
ALTER TABLE `express_ops_order_flow`  ADD INDEX `FlowCwbIdx` (`cwb` ASC) ;
/*====================================10-30===============================================*/

/*[11:24:33][3 ms] 订单表增加目标库房 字段*/ 
/*[11:26:18][168 ms] 订单表所有varchar类型长度加起来超过65535 了。需要修改*/ 
ALTER TABLE `express_ops_cwb_detail` CHANGE `remark1` `remark1` VARCHAR(500) CHARSET utf8 COLLATE utf8_bin NULL, 
CHANGE `remark2` `remark2` VARCHAR(500) CHARSET utf8 COLLATE utf8_bin NULL, 
CHANGE `remark3` `remark3` VARCHAR(500) CHARSET utf8 COLLATE utf8_bin NULL, 
CHANGE `remark4` `remark4` VARCHAR(500) CHARSET utf8 COLLATE utf8_bin NULL, 
CHANGE `remark5` `remark5` VARCHAR(500) CHARSET utf8 COLLATE utf8_bin NULL, 
ADD COLUMN `targetcarwarehouse` INT(11) DEFAULT 0 NULL AFTER `orderResultType`,
ADD COLUMN `targetcarwarehouseName` VARCHAR(50) DEFAULT '' NULL AFTER `targetcarwarehouse`; 



/*20212-10-31 新增当当推送临时表 [21:23:29][20 ms]
posttime 推送时间;flowordertype 流程状态（当当只存入库，小件员领货，投递反馈）;
jsoncontent 存储 json格式 ; send_b2c_flag(推送状态，0未推送，1.推送成功，2推送失败);
customerid 供货商id; 
*/ 
DROP TABLE  IF EXISTS express_send_b2c_data;
CREATE TABLE `express_send_b2c_data`
( `b2cid` BIGINT(18) NOT NULL AUTO_INCREMENT, 
  `cwb` VARCHAR(100),
  `posttime` VARCHAR(50), `flowordertype` INT(4), 
  `jsoncontent` TEXT, `send_b2c_flag` INT(4) DEFAULT 0, 
  `customerid` INT(11), PRIMARY KEY (`b2cid`) ) 
  CHARSET=utf8 COLLATE=utf8_general_ci; 
/* 建立索引 2012-10-31 [21:47:18][39 ms]*/ 
ALTER TABLE `express_send_b2c_data` ADD INDEX `cwb_index` (`cwb`); 

/*2012-11-1[00:32:52][23 ms]*/ 
  ALTER TABLE `express_send_b2c_data` ADD COLUMN `select_b2c_flag` INT(4) DEFAULT 0 NULL; 
/*[2012-11-05][169 ms] 添加是否属实的字段*/ 
  ALTER TABLE `express_cs_complaint` ADD COLUMN `issure` INT(4) DEFAULT 1 NULL AFTER `complaintuserdesc`;
  
  

/*====================================11-1 黄马甲试点===============================================*/
  
/*2012-11-06 [17:32:00][26 ms] 新增供货商运单号 tmall对接用到*/ 
ALTER TABLE `express_send_b2c_data` ADD COLUMN `shipcwb` VARCHAR(100) NULL ; 

/* 2012-11-07 新增供货商运单号按逗号隔开[11:32:33][50 ms]*/ 
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `multi_shipcwb` VARCHAR(1500) NULL ; 


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


