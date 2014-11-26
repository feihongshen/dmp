/*
SQLyog Ultimate v9.62 
MySQL - 5.1.62-community : Database - peisongdata_dmp_test
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`peisongdata_dmp_test` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `peisongdata_dmp_test`;

/*Table structure for table `express_ops_automatic_set` */

DROP TABLE IF EXISTS `express_ops_automatic_set`;

CREATE TABLE `express_ops_automatic_set` (
  `nowlinkname` varchar(50) COLLATE utf8_bin DEFAULT '',
  `nextlink` varchar(50) COLLATE utf8_bin DEFAULT '',
  `isauto` int(4) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_ops_automatic_set` */

insert  into `express_ops_automatic_set`(`nowlinkname`,`nextlink`,`isauto`) values ('GetGoods','intoWarehous',0),('GetGoodsnoListIntoWarehous','intoWarehous',0),('IntoWarehous','outWarehouse',0),('NoListIntoWarehous','outWarehouse',0),('OutWarehouse','substationGoods',0),('SubstationGoods','receiveGoods',0),('SubstationGoodsNoList','receiveGoods',0),('ChangeGoodsOutwarehouse','changeIntoWarehous',0),('ReturnGoodsOutwarehouse','backIntoWarehous',0),('ChangeIntoWarehous','transBranchOutWarehouse',0),('ChangeNoListIntoWarehous','transBranchOutWarehouse',0),('BackIntoWarehous','backReturnOutWarehous',0),('BackNoListIntoWarehous','backReturnOutWarehous',0);

/*Table structure for table `express_ops_bale` */

DROP TABLE IF EXISTS `express_ops_bale`;

CREATE TABLE `express_ops_bale` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `baleno` varchar(50) COLLATE utf8_bin DEFAULT '',
  `balestate` int(4) DEFAULT '0',
  `branchid` int(11) DEFAULT '0',
  `groupid` int(4) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_ops_bale` */

/*Table structure for table `express_ops_cwb_detail` */

DROP TABLE IF EXISTS `express_ops_cwb_detail`;

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
  `customerwarehouseid` varchar(2) COLLATE utf8_bin DEFAULT '0',
  `cwbordertypeid` varchar(2) COLLATE utf8_bin DEFAULT '-1',
  `cwbdelivertypeid` varchar(2) COLLATE utf8_bin DEFAULT '-1',
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
  `commonid` int(4) DEFAULT '0',
  `commoncwb` varchar(100) COLLATE utf8_bin DEFAULT '',
  `podrealname` varchar(50) COLLATE utf8_bin DEFAULT '',
  `signtypeid` int(4) DEFAULT '0',
  `podsignremark` varchar(100) COLLATE utf8_bin DEFAULT '',
  `podtime` varchar(50) COLLATE utf8_bin DEFAULT '',
  `modelname` varchar(50) COLLATE utf8_bin DEFAULT '',
  `reserve` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  `reserve1` varchar(1000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`opscwbid`),
  KEY `detail_cwb_idx` (`cwb`)
) ENGINE=InnoDB AUTO_INCREMENT=5308 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_ops_cwb_detail` */

/*Table structure for table `express_ops_cwb_error` */

DROP TABLE IF EXISTS `express_ops_cwb_error`;

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
  `posremark` varchar(100) COLLATE utf8_bin DEFAULT '',
  `mobilepodtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `checkfee` decimal(9,2) DEFAULT '0.00',
  `checkremark` varchar(100) COLLATE utf8_bin DEFAULT '',
  `receivedfeeuser` int(11) DEFAULT '0',
  `statisticstate` int(2) DEFAULT '1',
  `createtime` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `otherfee` decimal(9,2) DEFAULT '0.00',
  `podremarkid` int(11) DEFAULT NULL,
  `deliverstateremark` varchar(100) COLLATE utf8_bin DEFAULT '',
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_ops_delivery_state` */

/*Table structure for table `express_ops_emaildate` */

DROP TABLE IF EXISTS `express_ops_emaildate`;

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

/*Data for the table `express_ops_emaildate` */

/*Table structure for table `express_ops_exception_cwb` */

DROP TABLE IF EXISTS `express_ops_exception_cwb`;

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

/*Data for the table `express_ops_exception_cwb` */

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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_ops_goto_class_auditing` */

/*Table structure for table `express_ops_groupdetail` */

DROP TABLE IF EXISTS `express_ops_groupdetail`;

CREATE TABLE `express_ops_groupdetail` (
  `cwb` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `groupid` int(4) DEFAULT NULL,
  `baleid` int(11) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_ops_groupdetail` */

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
  `outwarehouseid` int(11) DEFAULT '0',
  PRIMARY KEY (`floworderid`)
) ENGINE=InnoDB AUTO_INCREMENT=5305 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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
  `printtime` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `operatetype` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_ops_outwarehousegroup` */

/*Table structure for table `express_ops_pay_up` */

DROP TABLE IF EXISTS `express_ops_pay_up`;

CREATE TABLE `express_ops_pay_up` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `credatetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `upaccountnumber` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `upuserrealname` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `upbranchid` int(11) DEFAULT NULL,
  `toaccountnumber` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `touserrealname` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `amount` decimal(9,2) DEFAULT NULL,
  `upstate` int(2) DEFAULT NULL,
  `branchid` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  `remark` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `type` int(2) DEFAULT NULL,
  `way` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_ops_pay_up` */

/*Table structure for table `express_ops_pos_paydetail` */

DROP TABLE IF EXISTS `express_ops_pos_paydetail`;

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

/*Data for the table `express_ops_pos_paydetail` */

/*Table structure for table `express_ops_setexportfield` */

DROP TABLE IF EXISTS `express_ops_setexportfield`;

CREATE TABLE `express_ops_setexportfield` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fieldname` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `exportstate` int(11) DEFAULT '0',
  `fieldenglishname` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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
  PRIMARY KEY (`id`)
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
  PRIMARY KEY (`id`)
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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
) ENGINE=InnoDB AUTO_INCREMENT=186 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_branch` */

insert  into `express_set_branch`(`branchid`,`branchname`,`branchaddress`,`branchcontactman`,`branchphone`,`branchmobile`,`branchfax`,`branchemail`,`contractflag`,`contractrate`,`cwbtobranchid`,`branchcode`,`payfeeupdateflag`,`backtodeliverflag`,`branchpaytoheadflag`,`branchfinishdayflag`,`creditamount`,`branchwavfile`,`brancheffectflag`,`noemailimportflag`,`errorcwbdeliverflag`,`errorcwbbranchflag`,`branchcodewavfile`,`importwavtype`,`exportwavtype`,`branchinsurefee`,`branchprovince`,`branchcity`,`noemaildeliverflag`,`sendstartbranchid`,`functionids`,`sitetype`,`checkremandtype`,`branchmatter`,`accountareaid`,`arrearagehuo`,`arrearagepei`,`arrearagefa`,`zhongzhuanid`,`tuihuoid`,`caiwuid`) values (1,'公司','','管理员','','','','','','0.000','','','','','','','0.00','','','','','','','','','0.00','','','',0,'P01,P02,P03,P04,P05,P06,P07,P08,P09,P10,P11,P12,P13,P14,P15,P16,P17,P18,P19',8,16,'',0,'0.00','0.00','0.00',0,0,0);

/*Table structure for table `express_set_common` */

DROP TABLE IF EXISTS `express_set_common`;

CREATE TABLE `express_set_common` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `commonname` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `commonnumber` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `orderprefix` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `commonstate` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_common` */

/*Table structure for table `express_set_commonmodel` */

DROP TABLE IF EXISTS `express_set_commonmodel`;

CREATE TABLE `express_set_commonmodel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `modelname` varchar(50) COLLATE utf8_bin DEFAULT '',
  `coordinate` varchar(1000) COLLATE utf8_bin DEFAULT '{getdate:"0px,0px";workname:"0px,0px";goodsname:"0px,0px";cwb:"0px,0px";salecompany:"0px,0px";name:"0px,0px";mobile:"0px,0px";workname1:"0px,0px";address:"0px,0px";weight:"0px,0px";size:"0px,0px";posecode:"0px,0px";remark:"0px,0px";bigwords:"0px,0px";smallwords:"0px,0px";}',
  `imageurl` varchar(100) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_commonmodel` */

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
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_customer_info` */

/*Table structure for table `express_set_customer_warehouse` */

DROP TABLE IF EXISTS `express_set_customer_warehouse`;

CREATE TABLE `express_set_customer_warehouse` (
  `warehouseid` int(11) NOT NULL AUTO_INCREMENT,
  `customerid` int(11) NOT NULL,
  `customerwarehouse` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `warehouseremark` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `ifeffectflag` int(11) DEFAULT NULL,
  PRIMARY KEY (`warehouseid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_exceed_fee` */

insert  into `express_set_exceed_fee`(`exceedid`,`exceedfee`) values (1,'0.00');

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
  PRIMARY KEY (`columnid`,`sendcargonameindex`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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

/*Table structure for table `express_set_importfield` */

DROP TABLE IF EXISTS `express_set_importfield`;

CREATE TABLE `express_set_importfield` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fieldname` varchar(80) COLLATE utf8_bin DEFAULT NULL,
  `fieldenglishname` varchar(80) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_importfield` */

insert  into `express_set_importfield`(`id`,`fieldname`,`fieldenglishname`) values (1,'订单号','cwb'),(2,'收件人姓名','consigneename'),(3,'收件人地址','consigneeaddress'),(4,'邮编','consigneepostcode'),(5,'电话','consigneephone'),(6,'手机','consigneemobile'),(7,'发出商品','sendcarname'),(8,'取回商品','backcarname'),(9,'实际重量','carrealweight'),(10,'应收金额','receivablefee'),(11,'应退金额','paybackfee'),(12,'备注信息','cwbremark'),(13,'指定小件员','exceldeliver'),(14,'指定派送分站','excelbranch'),(15,'配送单号','shipcwb'),(16,'收件人编号','consigneeno'),(17,'货物金额','cargoamount'),(18,'客户要求','customercommand'),(19,'货物类型','cargotype'),(20,'货物尺寸','cargosize'),(21,'取回商品金额','backcargoamount'),(22,'目的地','destination'),(23,'运输方式','transway'),(25,'发货数量','sendcarnum'),(26,'取货数量','backcarnum'),(27,'省','cwbprovince'),(28,'城市','cwbcity'),(29,'区县','cwbcounty'),(30,'发货仓库','carwarehouse'),(31,'订单类型','cwbordertypeid'),(33,'发货单号','transcwb'),(35,'承运商','commonid'),(36,'派送类型','cwbdelivertypeid'),(37,'结算区域','serviceareaid');

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

/*Table structure for table `express_set_joint` */

DROP TABLE IF EXISTS `express_set_joint`;

CREATE TABLE `express_set_joint` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `joint_num` bigint(20) DEFAULT NULL COMMENT '对接编号',
  `joint_property` text COLLATE utf8_bin COMMENT 'json格式字符串',
  `joint_remark` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `state` int(4) DEFAULT '1',
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_joint` */

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
) ENGINE=InnoDB AUTO_INCREMENT=2002 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_menu_info_new` */

insert  into `express_set_menu_info_new`(`menuid`,`menuname`,`menuurl`,`menulevel`,`menuno`,`parentno`,`menuimage`,`adminshowflag`) values (1,'数据导入','images/top_but_1.jpg','1','0','0','images/dhbtn1.png',NULL),(2,'数据导入',NULL,'2','001','1',NULL,NULL),(3,'导入模版设置','/excelcolumn/list','3','G2103','215','images/drsz.png',NULL),(4,'订单类型管理','/cwbordertype/list/1','3','G2104','215','images/ddlxsz.png',NULL),(5,'导入数据','/dataimport/excelimportPage','3','00102','2','images/daoru.png',NULL),(8,'数据查询',NULL,'2','002','231',NULL,NULL),(9,'导入查询','/cwborder/list/1','3','00203','8','images/drcx.png',NULL),(22,'库房管理','images/top_but_20.jpg','1','1','0','images/dhbtn2.png',NULL),(34,'交接单打印',NULL,'2','105','22',NULL,NULL),(35,'入库打印','/warehousegroup/inlist/1','3','10304','34','images/rkjjdy.png',NULL),(36,'出库打印','/warehousegroup/outlist/1','3','10305','34','images/ckjjdy.png',NULL),(152,'数据统计','images/top_but_10.jpg','1','A5','0','images/dhbtn3.png',NULL),(207,'系统设置','images/top_but_12.jpg','1','G','0','images/dhbtn4.png',NULL),(208,'公司资源',NULL,'2','G20','207',NULL,NULL),(209,'承运商管理','/common/list/1','3','G01','230','images/cyssz.png',NULL),(210,'承运商面单模版设置','/commonmodel/tosetcoordinate','3','G02','230','images/cysmbsz.png',NULL),(211,'机构管理','/branch/list/1','3','G2001','208','images/jgsz.png',NULL),(212,'用户管理','/user/list/1','3','G2003','208','images/jcsz.png',NULL),(213,'班车管理','/truck/list/1','3','G2004','208','images/bcsz.png',NULL),(215,'信息维护',NULL,'2','G21','207',NULL,NULL),(225,'合作伙伴','images/top_but_23.jpg','1','H','0','images/dhbtn5.png',NULL),(226,'客户',NULL,'2','H01','225',NULL,NULL),(227,'供货商管理','/customer/list/1','3','H0100','226','images/ghssz.png',NULL),(228,'仓库管理','/customerwarehouses/list/1','3','H0101','226','images/cksz.png',NULL),(229,'供货商结算区域','/accountareas/list/1','3','H0102','226','images/ghsjsqysz.png',NULL),(230,'承运商',NULL,'2','H02','225',NULL,NULL),(232,'系统管理',NULL,'2','I21','231',NULL,NULL),(234,'修改删除','/cwborder/delandedit','3','I2102','232','images/xgsc.png',NULL),(238,'超额提示设置','/exceedfee/list','3','G2101','215','images/cetssz.png',NULL),(247,'角色管理','/role/list','3','G2002','208','images/jssz.png',NULL),(255,'流程控制管理','/branch/toNextStopPage','3','G2007','215','images/lljksz.png',NULL),(257,'交接单管理','/joinlist/list','3','G2102','215','images/jjtssz.png',NULL),(258,'常用语管理','/reason/list/1','3','G2105','215','images/cyysz.png',NULL),(259,'退货出站打印','/warehousegroup/returnlist/1','3','20104','264','images/thczjjdy.png',NULL),(260,'中转出站打印','/warehousegroup/changelist/1','3','20103','264','images/zzczjjdy.png',NULL),(261,'领货打印','/warehousegroup/deliverlist/1','3','20102','264','images/xjydy.png',NULL),(262,'到站打印','/warehousegroup/inboxlist/1','3','20101','264','images/rkjjdy2.png',NULL),(263,'站点管理','','1','2','0','images/dhbtn12.png',NULL),(264,'交接单打印',NULL,'2','203','263',NULL,NULL),(265,'合同管理#','/welcome','3','H0103','226',NULL,NULL),(266,'公告发布#','/welcome','3','G2106','215','images/ggfb.png',NULL),(267,'中转站入库打印','/warehousegroup/changeinlist/1','3','10306','34','images/rzrksmdy.png',NULL),(268,'退货站入库打印','/warehousegroup/backinlist/1','3','10307','34','images/thrjjj.png',NULL),(269,'提货(P)',NULL,'p','P01','-1',NULL,NULL),(270,'库房入库(P)',NULL,'p','P02','-1',NULL,NULL),(271,'库房出库(P)',NULL,'p','P03','-1',NULL,NULL),(272,'理货(P)',NULL,'p','P04','-1',NULL,NULL),(273,'分站到货(P)',NULL,'p','P05','-1',NULL,NULL),(274,'小件员领货(P)',NULL,'p','P06','-1',NULL,NULL),(275,'小件员反馈(P)',NULL,'p','P07','-1',NULL,NULL),(276,'归班汇总(P)',NULL,'p','P08','-1',NULL,NULL),(277,'退货出站(P)',NULL,'p','P09','-1',NULL,NULL),(278,'中转出站(P)',NULL,'p','P10','-1',NULL,NULL),(279,'退货站入库(P)',NULL,'p','P11','-1',NULL,NULL),(280,'退货站再投(P)',NULL,'p','P12','-1',NULL,NULL),(281,'中转站入库(P)',NULL,'p','P13','-1',NULL,NULL),(282,'中转站出库(P)',NULL,'p','P14','-1',NULL,NULL),(283,'库存盘点(P)',NULL,'p','P15','-1',NULL,NULL),(284,'订单查询(P)',NULL,'p','P16','-1',NULL,NULL),(285,'系统设置(P)',NULL,'p','P17','-1',NULL,NULL),(286,'中转站出库打印','/warehousegroup/transbranchoutlist/1','3','10308 ','34','images/zzckdy.png',NULL),(287,'退货站再投打印','/warehousegroup/returnbranchoutlist/1','3','10309','34','images/zzckzt.png',NULL),(288,'异常单监控',NULL,'2','S','152',NULL,NULL),(289,'扫描监控','/cwborderPDA/controlForBranchImport/1','3','102','305','images/fzdhxx.png',NULL),(290,'归班管理',NULL,'2','202','263',NULL,NULL),(291,'归班审核','/delivery/auditView','3','20201','290','images/gbsh.png',NULL),(292,'站点结算','/payup/viewCount','3','20202','290','images/js.png',NULL),(293,'站点货物管理',NULL,'2','202','263',NULL,NULL),(294,'上门退订单打印','/cwborder/selectforsmt/1','3','301','293','images/smtdd.png',NULL),(296,'退供应商出库',NULL,'p','P18','-1',NULL,NULL),(297,'供应商拒收返库',NULL,'p','P19','-1',NULL,NULL),(298,'换单',NULL,'2','104','22',NULL,NULL),(299,'订单称重','/changecwb/tochangecwbforwegiht','3','101','298','images/cz.png',NULL),(300,'按泡货规格称重','/changecwb/tochangecwbforwegihttopaohuo','3','102','298','images/aphggcz.png',NULL),(301,'打印面单','/changecwb/toprintcwb','3','103','298','images/mddy.png',NULL),(302,'打印面单（带称重）','/changecwb/toprintandweightcwb','3','104','298','images/mddydcz.png',NULL),(303,'货物操作',NULL,'2','103','22',NULL,NULL),(304,'库房扫描','/pda/kufanglist','3','101','303','images/kfsm.png',NULL),(305,'扫描',NULL,'2','201','263',NULL,NULL),(306,'站点扫描','/pda/zhandianlist','3','101','305','images/zdsm.png',NULL),(401,'对接管理','/jointManage/','3','G2106','215','images/jcsz.png',NULL),(402,'导出模版管理','/setexportcwb/list/1','3','G2110','215','images/dcsz.png',NULL),(403,'扫描监控','/cwborderPDA/controlForBranchImport/1','3','102','303','images/fzdhxx.png',NULL),(404,'上门退订单打印','/cwborder/selectforkfsmt/1','3','103','303','images/smtdd.png',NULL),(1063,'客服管理','images/dhbtn13.png','1','A1','0','images/dhbtn13.png',NULL),(1064,'运营监控','images/dhbtn14.png','1','A2','0','images/dhbtn14.png',NULL),(1065,'结算管理','images/dhbtn15.png','1','A3','0','images/dhbtn15.png',NULL),(1066,'数据查询','images/top_but_11.jpg','1','A4','0','images/dhbtn11.png',NULL),(1067,'订单查询','','2','A101','1063',NULL,NULL),(1068,'投诉处理','','2','A102','1063',NULL,NULL),(1071,'订单查询','/order/select/1','3','A10101','1067','images/ddcx.png',NULL),(1072,'投诉处理','/complaint/list/1','3','A10201','1068','images/tscl.png',NULL),(1075,'投递率',NULL,'2','B1','1064',NULL,NULL),(1076,'实时监控',NULL,'2','B2','1064',NULL,NULL),(1077,'各站统计(按天)','/delivery/select','3','B202','1075','images/zdtdltj.png',NULL),(1078,'数据监控','/monitor/date/1','3','B101','1076','images/sjjk].png',NULL),(1079,'库房信息监控','/monitor/date/3','3','B103','1076','images/kfjk.png',NULL),(1080,'站点信息监控','/monitor/date/4','3','B104','1076','images/zdxxjk.png',NULL),(1081,'投递实效监控','/monitordelivery/date/5','3','B105','1076','images/tdxxjk.png',NULL),(1083,'异常信息监控','/monitor/date/7','3','B107','1076','images/ycxxjk.png',NULL),(1085,'结算与审核',NULL,'2','A301','1065',NULL,NULL),(1086,'货款结算','/funds/payment','3','A30101','1085','images/dkjs.png',NULL),(1087,'退货款结算','/funds/paymentBack','3','A30102','1085','images/tkkjs.png',NULL),(1088,'站点交款审核','/funds/paymentCheack','3','A30103','1085','images/zdjksh.png',NULL),(1093,'公司统计(按天)','/delivery/selectAll','3','B201','1075','images/gstdltj.png',NULL),(1094,'数据监控(数据组)','/monitor/date/2','3','B102','1076','images/azsjjk.png',NULL),(1103,'导出订单',NULL,'2','A402','1066',NULL,NULL),(1104,'设置导出字段','/exportcwb/list','3','A40201','1098',NULL,NULL),(1105,'高级查询','/advancedquery/list/1','3','A10102','1067','images/gjcx.png',NULL),(1106,'预警','/order/earlywarning/1','3','A10103','1067','images/yujing.png',NULL),(1107,'批量修改','/order/batchedit','3','A10104','1067','images/plxg.png',NULL),(1108,'批量修改运单号','/order/transcwbbatchedit','3','A10105','1067','images/plxghdh.png',NULL),(1109,'退货管理','/order/backgoods/1','3','A10106','1067','images/thgl.png',NULL),(1110,'退货批量审核','/order/backgoodsbatch/1','3','A10107','1067','images/thplsh.png',NULL),(1113,'代理管理','/proxy/select/1','3','G2004','215','images/plxg.png',NULL),(1114,'短信账户管理','/smsconfig/setsmsview','3','G2108','215','images/dxqf.png',NULL),(1115,'短信群发','/sms/view','3','A10108','1067','images/dxqf.png',NULL),(2001,'地址库管理','http://58.83.193.9:9080/addressmatch/siteEditor.html','3','G2008','215','images/dhbtn16.png',NULL);

/*Table structure for table `express_set_reason` */

DROP TABLE IF EXISTS `express_set_reason`;

CREATE TABLE `express_set_reason` (
  `reasonid` int(10) NOT NULL AUTO_INCREMENT,
  `reasoncontent` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `reasontype` int(10) DEFAULT '0',
  PRIMARY KEY (`reasonid`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_reason` */

/*Table structure for table `express_set_remark` */

DROP TABLE IF EXISTS `express_set_remark`;

CREATE TABLE `express_set_remark` (
  `remarkid` int(11) NOT NULL AUTO_INCREMENT,
  `remarktype` varchar(50) COLLATE utf8_bin NOT NULL,
  `remark` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`remarkid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_remark` */

/*Table structure for table `express_set_role_menu_new` */

DROP TABLE IF EXISTS `express_set_role_menu_new`;

CREATE TABLE `express_set_role_menu_new` (
  `roleid` int(11) NOT NULL,
  `menuid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_role_menu_new` */

insert  into `express_set_role_menu_new`(`roleid`,`menuid`) values (4,263),(4,264),(4,262),(4,261),(4,260),(4,259),(4,289),(4,290),(4,291),(4,292),(4,293),(4,294),(4,295),(4,207),(4,208),(4,212),(4,213),(4,272),(4,273),(4,274),(4,275),(4,276),(4,277),(4,278),(4,284),(4,285),(0,1),(0,2),(0,5),(0,22),(0,303),(0,304),(0,403),(0,404),(0,298),(0,299),(0,300),(0,34),(0,35),(0,36),(0,286),(0,287),(0,263),(0,305),(0,306),(0,289),(0,290),(0,291),(0,292),(0,293),(0,294),(0,264),(0,261),(0,260),(0,259),(0,1063),(0,1067),(0,1071),(0,1109),(0,1110),(0,1115),(0,1068),(0,1072),(0,1064),(0,1075),(0,1093),(0,1077),(0,1076),(0,1078),(0,1094),(0,1079),(0,1080),(0,1081),(0,1083),(0,1065),(0,1085),(0,1086),(0,1087),(0,1088),(0,207),(0,208),(0,211),(0,247),(0,212),(0,213),(0,215),(0,255),(0,2001),(0,238),(0,3),(0,4),(0,258),(0,401),(0,1114),(0,402),(0,225),(0,226),(0,227),(0,228),(0,229),(0,265),(0,230),(0,209),(0,210),(0,269),(0,270),(0,271),(0,272),(0,273),(0,274),(0,275),(0,276),(0,277),(0,278),(0,279),(0,280),(0,281),(0,282),(0,283),(0,284),(0,285),(0,296),(0,297);

/*Table structure for table `express_set_role_new` */

DROP TABLE IF EXISTS `express_set_role_new`;

CREATE TABLE `express_set_role_new` (
  `roleid` int(11) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `type` int(2) DEFAULT '1',
  PRIMARY KEY (`roleid`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_role_new` */

insert  into `express_set_role_new`(`roleid`,`rolename`,`type`) values (0,'管理员',0),(1,'客服',0),(2,'小件员',0),(3,'驾驶员',0),(4,'站长',0);

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

/*Table structure for table `express_set_truck` */

DROP TABLE IF EXISTS `express_set_truck`;

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
) ENGINE=InnoDB AUTO_INCREMENT=999 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_set_user` */

insert  into `express_set_user`(`userid`,`username`,`password`,`realname`,`idcardno`,`employeestatus`,`branchid`,`userphone`,`usermobile`,`useraddress`,`userremark`,`usersalary`,`usercustomerid`,`showphoneflag`,`useremail`,`deliverpaytype`,`userwavfile`,`branchmanagerflag`,`roleid`,`userDeleteFlag`) values (1,'admin','admin','管理员','0',1,1,NULL,'0',NULL,NULL,NULL,0,'1','NULL',0,'NULL',0,0,1);

/*[13:48:29][36 ms]*/ UPDATE `express_set_menu_info_new` SET `menuname`='公司基本信息维护' WHERE `menuid`='208'; 
/*[13:48:41][14 ms]*/ UPDATE `express_set_menu_info_new` SET `menuname`='岗位管理' WHERE `menuid`='247';

/*[13:51:03][119 ms]订单表添加了一票多件扫描的件数字段*/ 
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `scannum` INT(4) DEFAULT 1 NULL AFTER `reserve1`; 

/*[13:31:18][53 ms] 添加菜单 客服中*/ 
INSERT INTO `express_set_menu_info_new`(`menuid`,`menuname`,`menuurl`,`menulevel`,`menuno`,`parentno`,`menuimage`,`adminshowflag`) VALUES ( '1116','高级查询','/advancedquery/list/1','3','G208','215','images/gjcx.png',NULL); 

/*[9-9 10:05:04][133 ms] 添加高级查询菜单*/ 
INSERT INTO `express_set_menu_info_new`(`menuid`,`menuname`,`menuurl`,`menulevel`,`menuno`,`parentno`,`menuimage`,`adminshowflag`) VALUES ( '1117','数据查询',NULL,'1','I','0','images/dhbtn3.png',NULL); 
INSERT INTO `express_set_menu_info_new`(`menuid`,`menuname`,`menuurl`,`menulevel`,`menuno`,`parentno`,`menuimage`,`adminshowflag`) VALUES ( '1118','订单数据',NULL,'2','A101','1117',NULL,NULL); 
INSERT INTO `express_set_menu_info_new`(`menuid`,`menuname`,`menuurl`,`menulevel`,`menuno`,`parentno`,`menuimage`,`adminshowflag`) VALUES ( '1119','高级查询','/advancedquery/hmj_list/1','3','A101','1118','images/gjcx.png',NULL); 

/*DELETE FROM `express_set_role_menu_new`;
insert  into `express_set_role_menu_new`(`roleid`,`menuid`) values (0,1),(0,2),(0,5),(0,22),(0,303),(0,304),(0,403),(0,404),(0,298),(0,299),(0,300),(0,34),(0,35),(0,36),(0,286),(0,287),(0,263),(0,305),(0,306),(0,289),(0,290),(0,291),(0,292),(0,293),(0,294),(0,264),(0,261),(0,260),(0,259),(0,1063),(0,1067),(0,1071),(0,1109),(0,1110),(0,1115),(0,1068),(0,1072),(0,1064),(0,1075),(0,1093),(0,1077),(0,1076),(0,1078),(0,1094),(0,1079),(0,1080),(0,1081),(0,1083),(0,1065),(0,1085),(0,1086),(0,1087),(0,1088),(0,207),(0,208),(0,211),(0,247),(0,212),(0,213),(0,215),(0,255),(0,2001),(0,238),(0,3),(0,4),(0,258),(0,401),(0,1114),(0,402),(0,225),(0,226),(0,227),(0,228),(0,229),(0,265),(0,230),(0,209),(0,210),(0,269),(0,270),(0,271),(0,272),(0,273),(0,274),(0,275),(0,276),(0,277),(0,278),(0,279),(0,280),(0,281),(0,282),(0,283),(0,284),(0,285),(0,296),(0,297),(4,263),(4,289),(4,290),(4,291),(4,292),(4,293),(4,294),(4,264),(4,261),(4,260),(4,259),(4,207),(4,208),(4,212),(4,213),(4,272),(4,273),(4,274),(4,275),(4,276),(4,277),(4,278),(4,284),(4,285);
*/

/*[19:08:58][201 ms]*/ ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `isaudit` INT(4) DEFAULT 0 NULL AFTER `scannum`; 

/*[12-9-9 13:31:18][53 ms] 增加了反馈表对应归班表id的字段*/ 
ALTER TABLE `express_ops_delivery_state` ADD COLUMN `gcaid` INT(11) DEFAULT 0 NULL AFTER `deliverstateremark`;
/*[12-9-9 13:31:18][53 ms] 增加5个备注字段以及导入时候的设置*/
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `remark1` VARCHAR(100) DEFAULT '' NULL AFTER `scannum`, ADD COLUMN `remark2` VARCHAR(100) DEFAULT '' NULL AFTER `remark1`, ADD COLUMN `remark3` VARCHAR(100) DEFAULT '' NULL AFTER `remark2`, ADD COLUMN `remark4` VARCHAR(100) DEFAULT '' NULL AFTER `remark3`, ADD COLUMN `remark5` VARCHAR(100) DEFAULT '' NULL AFTER `remark4`;
ALTER TABLE `express_set_excel_column` ADD COLUMN `remark1index` INT(11) NULL AFTER `modelnameindex`, ADD COLUMN `remark2index` INT(11) NULL AFTER `remark1index`, ADD COLUMN `remark3index` INT(11) NULL AFTER `remark2index`, ADD COLUMN `remark4index` INT(11) NULL AFTER `remark3index`, ADD COLUMN `remark5index` INT(11) NULL AFTER `remark4index`;  


/*[2:36:18][164 ms] 删掉没用的菜单*/ 

DELETE FROM `express_set_menu_info_new` WHERE `menuid`='1116'; 
/*====================================9-12===============================================*/ 
/*20120913 [14:53:23][68 ms] 改变备注信息varchar长度为500*/ 
ALTER TABLE `express_ops_delivery_state` CHANGE `deliverstateremark` `deliverstateremark` VARCHAR(500) CHARSET utf8 COLLATE utf8_bin DEFAULT '' NULL;

/*[18:30:10][90 ms]*/ ALTER TABLE `express_ops_delivery_state` ADD COLUMN `isout` INT(4) DEFAULT 0 NULL AFTER `gcaid`; 

/*09-17 [11:33:23][19 ms]    修改库房信息监控菜单名称*/ 
UPDATE `express_set_menu_info_new` SET `menuurl`='/monitorhouse/date' WHERE `menuid`='1079'; 


/*[09-17 12:10:28][100 ms] 修改站点信息监控菜单路径*/ 
UPDATE `express_set_menu_info_new` SET `menuurl`='/monitorsite/date' WHERE `menuid`='1080'; 

/*[16:42:46][665 ms]  归班表 加小件员字段*/ 
ALTER TABLE `express_ops_goto_class_auditing` ADD COLUMN `deliverealuser` INT(11) DEFAULT 0 NULL AFTER `payupid`; 
/*[9-17 16:42:46][665 ms]  创建历史归班表*/ 
CREATE TABLE `express_ops_goto_class_old`( `id` BIGINT(11) NOT NULL AUTO_INCREMENT, `gotoclassauditingid` INT(11) DEFAULT 0, `nownumber` INT(11) DEFAULT 0, `yiliu` INT(11) DEFAULT 0, `lishiweishenhe` INT(11) DEFAULT 0, `zanbuchuli` INT(11) DEFAULT 0, `peisongchenggong` INT(11) DEFAULT 0, `peisongchenggongamount` DECIMAL(9,2) DEFAULT 0, `tuihuo` INT(11) DEFAULT 0, `tuihuoamount` DECIMAL(9,2) DEFAULT 0, `bufentuihuo` INT(11) DEFAULT 0, `bufentuihuoamount` DECIMAL(9,2) DEFAULT 0, `zhiliu` INT(11) DEFAULT 0, `zhiliuamount` DECIMAL(9,2) DEFAULT 0, `shangmentuichenggong` INT(11) DEFAULT 0, `shangmentuichenggongamount` DECIMAL(9,2) DEFAULT 0, `shangmentuijutui` INT(11) DEFAULT 0, `shangmentuijutuiamount` DECIMAL(9,2) DEFAULT 0, `shangmenhuanchenggong` INT(11) DEFAULT 0, `shangmenhuanchenggongamount` DECIMAL(9,2) DEFAULT 0, `diushi` INT(11) DEFAULT 0, `diushiamount` DECIMAL(9,2) DEFAULT 0, PRIMARY KEY (`id`) ) ENGINE=INNODB CHARSET=utf8 COLLATE=utf8_bin; 


/*====================================9-19===============================================*/
/*[9-18 11:38:52][240 ms]添加了退货备注的字段*/ 
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `backreason` VARCHAR(225) DEFAULT '' NULL AFTER `isaudit`; 
/*9-19 [13:25:59][26 ms]添加了退供应商出库交接单的菜单*/ 
INSERT INTO `express_set_menu_info_new`(`menuid`,`menuname`,`menuurl`,`menulevel`,`menuno`,`parentno`,`menuimage`,`adminshowflag`) VALUES ( '405','退供应商出库交接单','/warehousegroup/backtocustomerlist/1','3','10310','34','images/tgysck.png',NULL); 
/*9-19 [14:40:54][129 ms] 添加了供应商的id*/ 
ALTER TABLE `express_ops_outwarehousegroup` ADD COLUMN `customerid` INT(11) DEFAULT 0 NULL AFTER `operatetype`; 
/*====================================9-20===============================================*/
/*[9-23 13:35:34][49 ms] 取出订单号，订单号列不可调换*/ 
DELETE FROM `express_set_importfield` WHERE `id`='1';
UPDATE `express_ops_setexportfield` SET `fieldname`='供货商运单号' WHERE `id`='17'; 
UPDATE `express_set_importfield` SET `fieldname`='供货商运单号' WHERE `id`='15'; 

/*9-23 [12:08:00][20 ms] 菜单表中，新增pos款项查询*/ 
INSERT INTO `express_set_menu_info_new`(`menuid`,`menuname`,`menuurl`,`menulevel`,`menuno`,`parentno`,`menuimage`,`adminshowflag`) VALUES ( '1120','POS款项查询','/pospay/list/1','3','A30104','1085','images/zdjksh.png',NULL); 
/*[9-23 13:35:34][49 ms] 修改pos列表菜单的图标*/ 
UPDATE `express_set_menu_info_new` SET `menuimage`='images/POS.png' WHERE `menuid`='1120';
/*9-21 [17:53:19][134 ms]添加了滞留原因的字段*/ 
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `leavedreason` VARCHAR(225) DEFAULT '' NULL AFTER `backreason`; 
/*====================================9-24===============================================*/
 ALTER TABLE `express_ops_cwb_detail` CHANGE `customerwarehouseid` `customerwarehouseid` VARCHAR(11) CHARSET utf8 COLLATE utf8_bin DEFAULT '0' NULL, CHANGE `cwbordertypeid` `cwbordertypeid` VARCHAR(11) CHARSET utf8 COLLATE utf8_bin DEFAULT '-1' NULL, CHANGE `cwbdelivertypeid` `cwbdelivertypeid` VARCHAR(11) CHARSET utf8 COLLATE utf8_bin DEFAULT '-1' NULL, CHANGE `commonid` `commonid` INT(11) DEFAULT 0 NULL, CHANGE `signtypeid` `signtypeid` INT(11) DEFAULT 0 NULL;
 /*[9-26 14:26:54][45 ms]*/ 
UPDATE `express_set_menu_info_new` SET `menuurl`='http://58.83.193.9/addressmatch/site/siteMatchEditor' WHERE `menuid`='2001'; 

 /*[9-26 17:43:49][156 ms]*/ 
ALTER TABLE `express_ops_groupdetail` ADD COLUMN `userid` INT(11) DEFAULT 0 NULL AFTER `baleid`; 
/*====================================9-26===============================================*/
/*[10:13:19][212 ms] 增加归班历史记录字段*/
ALTER TABLE `express_ops_goto_class_old` ADD COLUMN `peisongchenggongposamount` DECIMAL(9,2) DEFAULT 0.00 NULL AFTER `peisongchenggongamount`, ADD COLUMN `bufentuihuoposamount` DECIMAL(9,2) DEFAULT 0.00 NULL AFTER `bufentuihuoamount`, ADD COLUMN `shangmenhuanchenggongposamount` DECIMAL(9,2) DEFAULT 0.00 NULL AFTER `shangmenhuanchenggongamount`; 
 /*[9-26 14:26:54][45 ms]*/ 
UPDATE `express_set_menu_info_new` SET `menuurl`='http://58.83.193.7/addressmatch/site/siteMatchEditor' WHERE `menuid`='2001'; 
/*====================================9-27===============================================*/

/* 2012-09-27 新增B2C异常原因设置 表[16:25:14][13 ms]*/
CREATE TABLE `express_set_b2c_exptreason`( 
	`exptid` BIGINT(11) AUTO_INCREMENT, `customerid` BIGINT(11),
	 `support_key`  VARCHAR(50)  ,
	 `expt_code` VARCHAR(50) , 
	 `expt_msg` VARCHAR(500) ,
	 `expt_type` INT(4) , 
	 `expt_remark` VARCHAR(500) , KEY(`exptid`)
	  ); 


/* 2012-09-28 新增异常码关联  表[16:25:14][13 ms]*/
CREATE TABLE `express_set_exptcode_joint` (
  `exptcodeid` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `reasonid` BIGINT(11) DEFAULT NULL,
  `exptid` BIGINT(11) DEFAULT NULL,
  `exptcode_remark` VARCHAR(500) COLLATE utf8_bin DEFAULT NULL,
  KEY `exptcodeid` (`exptcodeid`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/* 2012-10-08 新增导出模板字段*/
/*此段多余代码已删除*/

/* 2012-10-08 修改express_ops_exportmould 字段长度[13:12:05][36 ms]*/ 
ALTER TABLE `express_ops_exportmould` CHANGE `mouldfieldids` `mouldfieldids` VARCHAR(2000) CHARSET utf8 COLLATE utf8_bin NULL; 
 
/*[15:08:54][99 ms] 增加了交款时保存的pos金额*/ 
ALTER TABLE `express_ops_goto_class_auditing` ADD COLUMN `payupamount_pos` DECIMAL(9,2) DEFAULT 0 NULL AFTER `payupamount`;  
ALTER TABLE `express_ops_pay_up` ADD COLUMN `amountpos` DECIMAL(9,2) DEFAULT 0 NULL AFTER `amount`; 
/*====================================10-10===============================================*/

/*10-12 [16:03:54][63 ms]  高级查询导出 应收金额不对，修改成正确的*/ 
UPDATE `express_ops_setexportfield` SET `fieldenglishname`='Receivablefee' WHERE `id`='13'; 
/*10- 12 [19:33:02][315 ms] 导出 表加两个字段*/ 
DROP TABLE IF EXISTS `express_ops_setexportfield`;

CREATE TABLE `express_ops_setexportfield` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fieldname` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `exportstate` int(11) DEFAULT '0',
  `fieldenglishname` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `express_ops_setexportfield` */

insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (1,'承运商',0,'Commonname');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (2,'供货商',0,'Customername');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (3,'订单号',0,'Cwb');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (4,'运单号',0,'Transcwb');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (5,'收件人名称',0,'Consigneename');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (6,'收件人地址',0,'Consigneeaddress');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (7,'收件人邮编',0,'Consigneepostcode');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (8,'收件人电话',0,'Consigneephone');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (9,'收件人手机',0,'Consigneemobile');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (10,'发出商品名称',0,'Sendcarname');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (11,'取回商品名称',0,'Backcarname');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (12,'货物重量',0,'Carrealweight');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (13,'代收货款应收金额',0,'Receivablefee');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (14,'订单备注',0,'Cwbremark');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (15,'指定小件员',0,'Exceldeliver');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (16,'指定派送分站',0,'Excelbranch');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (17,'配送单号',0,'Shipcwb');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (18,'收件人编号',0,'Consigneeno');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (19,'货物金额',0,'Caramount');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (20,'客户要求',0,'Customercommand');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (21,'货物类型',0,'Cartype');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (22,'货物尺寸',0,'Carsize');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (23,'取回货物金额',0,'Backcaramount');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (25,'目的地',0,'Destination');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (26,'运输方式',0,'Transway');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (27,'退供货商承运商',0,'Shipperid');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (28,'发货数量',0,'Sendcarnum');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (29,'取货数量',0,'Backcarnum');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (30,'省',0,'Cwbprovince');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (31,'市',0,'Cwbcity');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (32,'区县',0,'Cwbcounty');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (33,'发货仓库',0,'Carwarehouse');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (34,'订单类型',0,'Cwbordertypeid');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (36,'修改时间',0,'Edittime');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (38,'订单状态',0,'FlowordertypeMethod');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (39,'审核人',0,'Auditor');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (40,'退货备注',0,'Returngoodsremark');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (41,'最新跟踪状态',0,'Newfollownotes');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (42,'标记人',0,'Marksflagmen');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (43,'订单原始金额',0,'Primitivemoney');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (45,'签收时间',0,'Signintime');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (46,'修改的签收时间',0,'Editsignintime');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (47,'签收人',0,'Signinman');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (48,'修改人',0,'Editman');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (49,'承运商编号',0,'Commonnumber');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (50,'审核人',0,'Auditor');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (51,'审核时间',0,'Audittime');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (52,'审核状态',0,'AuditstateStr');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (53,'站点',0,'Branchname');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (54,'入库时间',0,'Instoreroomtime');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (55,'备注1',0,'Remark1');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (56,'备注2',0,'Remark2');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (57,'备注3',0,'Remark3');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (58,'备注4',0,'Remark4');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (59,'备注5',0,'Remark5');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (60,'当前所在站点',0,'Startbranchname');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (61,'下一站站点',0,'Nextbranchname');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (62,'出库时间',0,'Outstoreroomtime');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (63,'到站时间',0,'InSitetime');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (64,'小件员领货时间',0,'PickGoodstime');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (65,'配送成功时间',0,'SendSuccesstime');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (66,'反馈时间',0,'Gobacktime');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (67,'归班时间',0,'Goclasstime');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (68,'最新修改时间',0,'Nowtime');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (69,'滞留原因',0,'LeavedreasonStr');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (70,'入库仓库',0,'Inhouse');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (71,'称重重量',0,'Realweight');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (72,'货品备注',0,'Goodsremark');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (73,'支付方式',0,'Paytype');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (74,'入库仓库名称',0,'Carwarehousename');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (75,'客户发货仓库名称',0,'Customerwarehousename');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (76,'反馈小件员',0,'Fdelivername');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (77,'收到总金额',0,'Receivedfee');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (78,'退还金额',0,'Returnedfee');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (79,'应处理金额',0,'Businessfee');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (80,'现金实收',0,'Cash');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (81,'pos实收',0,'Pos');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (82,'pos备注',0,'Posremark');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (83,'pos反馈时间',0,'Mobilepodtime');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (84,'支票实收',0,'Checkfee');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (85,'支票号备注',0,'Checkremark');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (86,'收款人',0,'ReceivedfeeuserName');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (87,'归班状态',0,'StatisticstateStr');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (88,'创建时间',0,'Createtime');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (89,'其他金额',0,'Otherfee');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (90,'反馈备注',0,'Deliverstateremark');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (91,'反馈站点名称',0,'Payupbranchname');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (92,'配送结果备注',0,'PodremarkStr');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (93,'上交款人姓名',0,'Payuprealname');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (94,'发货妥投时间',0,'TuotouTime');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (95,'发货有结果时间',0,'YoujieguoTime');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (96,'入库妥投时间',0,'RukutuotouTime');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (97,'入库有结果时间',0,'RukuyoujieguoTime');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (98,'到站妥投时间',0,'DaozhantuotouTime');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (99,'到站有结果时间',0,'DaozhanyoujieguoTime');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (100,'是否上缴款',0,'IspayUpStr');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (101,'是否有欠款',0,'IsQiankuanStr');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (102,'退货再投审核',0,'AuditEganstateStr');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (103,'当前操作员',0,'OperatorName');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (104,'退货原因',0,'Backreason');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (105,'供应商异常编码',0,'Expt_code');
insert  into `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) values (106,'供应商异常原因',0,'Expt_msg');
/*====================================10-13===============================================*/
/*10-16*/
/*[13:46:54][75 ms]支付方式表*/ 
DROP TABLE IF EXISTS `express_set_payway`;

CREATE TABLE `express_set_payway` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `payway` varchar(50) COLLATE utf8_bin DEFAULT '',
  `paywayid` int(4) DEFAULT NULL,
  `issetflag` int(4) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin; 
/*[13:49:32][411 ms]订单表添加了支付方式的字段，以及变更支付方式的字段*/ 
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `paywayid` INT(4) DEFAULT 1 NULL AFTER `leavedreason`, ADD COLUMN `newpaywayid` INT(4) DEFAULT 0 NULL AFTER `paywayid`, ADD COLUMN `issetflag` INT(4) DEFAULT 1 NULL AFTER `newpaywayid`; 
/*[14:27:47][17 ms]添加了支付方式管理的菜单*/ 
INSERT INTO `express_set_menu_info_new`(`menuid`,`menuname`,`menuurl`,`menulevel`,`menuno`,`parentno`,`menuimage`,`adminshowflag`) VALUES ( '406','支付方式管理','/payway/list/1','3','G2105','215',NULL,NULL); 
/*[14:31:00][18 ms]*/ 
UPDATE `express_set_menu_info_new` SET `menuno`='G2106' WHERE `menuid`='258'; 
/*[14:31:26][127 ms]*/ 
UPDATE `express_set_menu_info_new` SET `menuno`='G2107' WHERE `menuid`='401'; 
/*[15:38:40][112 ms]导入模版设置表添加了支付方式的字段*/ 
ALTER TABLE `express_set_excel_column` ADD COLUMN `paywayindex` INT(11) NULL AFTER `remark5index`; 
/*[18:51:48][17 ms]导入数据-数据换列（不允许更换订单类型一列）*/ 
DELETE FROM `express_set_importfield` WHERE `id`='31'; 


/* 2012-10-17 b2c 异常码设置新增字段，B2C编号 [09:54:47][45 ms]*/
 ALTER TABLE `express_set_b2c_exptreason` ADD COLUMN `customercode` VARCHAR(30) NULL AFTER `expt_remark`;
 
 
/*[2012-10-17 13:48:49][14 ms]支付方式管理菜单图片*/ 
 UPDATE `express_set_menu_info_new` SET `menuimage`='images/zffs.png' WHERE `menuid`='406'; 
 /*====================================10-18===============================================*/
 /* 2012-10-19 导出模板新增  */
  UPDATE `express_ops_setexportfield` SET `fieldname`='当前订单状态' WHERE  fieldname='订单状态';
  
/*[2012-10-19 9:15:06][63 ms] 修复订单表扫描次数的默认值*/ 
ALTER TABLE `express_ops_cwb_detail` CHANGE `scannum` `scannum` INT(4) DEFAULT 0 NULL; 

/*[2012-10-22开关设置表*/
DROP TABLE IF EXISTS `express_set_switch`;

CREATE TABLE `express_set_switch` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `switchname` varchar(50) COLLATE utf8_bin DEFAULT '',
  `state` varchar(10) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert  into `express_set_switch`(`id`,`switchname`,`state`) values (1,'出库','ck_02');

/*[2012-10-22 11:06:42][44 ms] 导出模板表加入一个导出值 配送结果*/ 
INSERT INTO `express_ops_setexportfield`(`id`,`fieldname`,`exportstate`,`fieldenglishname`) VALUES ( 107,'配送结果','0','OrderResultTypeText'); 

/*[2012-10-22 11:58:50][354 ms]更改新支付方式的类型*/ 
ALTER TABLE `express_ops_cwb_detail` CHANGE `newpaywayid` `newpaywayid` VARCHAR(10) DEFAULT '1' NULL; 

/*====================================10-23===============================================*/

/*[2012-10-23 13:38:24][161 ms] 增加按单计费的两个表*/ 
CREATE TABLE `express_sys_scan`( 
`cwb` VARCHAR(100) NOT NULL, 
`flowordertype` INT(10) DEFAULT -1, 
PRIMARY KEY (`cwb`) 
) ENGINE=INNODB CHARSET=utf8 COLLATE=utf8_bin; 

CREATE TABLE `express_sys_on_off`( 
`id` INT(11) NOT NULL AUTO_INCREMENT, 
`number` BIGINT(11) DEFAULT 0, 
`on_off` VARCHAR(3) DEFAULT 'on', 
`type` VARCHAR(40) DEFAULT '', 
`mac` VARCHAR(100) DEFAULT '', 
PRIMARY KEY (`id`) 
) ENGINE=INNODB CHARSET=utf8 COLLATE=utf8_bin; 

INSERT INTO `express_sys_on_off`(`id`,`number`,`on_off`,`type`,`mac`) VALUES ( NULL,'0','on','SYSTEM_ON_OFF','c838c6c9637121acb0d21839a319cc2d'); 


/*====================================10-24 黄马甲===============================================*/
 ALTER TABLE `express_ops_delivery_state` CHANGE `cwb` `cwb` VARCHAR(100) CHARSET utf8 COLLATE utf8_bin NOT NULL, ADD PRIMARY KEY (`id`, `cwb`); 

 /* 2012-10-25  [15:23:54][168 ms] 反馈表新增Pos反馈标识 */ 
 ALTER TABLE `express_ops_delivery_state` ADD COLUMN `pos_feedback_flag` INT(4) DEFAULT 0 NULL ; 
 

 
 
 /*[2012-10-26 11:12:58][74 ms] 导出模板修改几个地方，导出订单类型为名称，支付方式也为名称*/
 UPDATE `express_ops_setexportfield` SET `fieldenglishname`='PaytypeName' WHERE `id`='73'; 
 UPDATE `express_ops_setexportfield` SET `fieldname`='发货仓库名称' WHERE `id`='75'; 
 UPDATE `express_ops_setexportfield` SET `fieldenglishname`='OrderType' WHERE `id`='34'; 
 
 
 
 /*[2012-10-26 10:28:58][215 ms]*/ 
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `targetcarwarehouse` INT(11) DEFAULT 0 NULL AFTER `newpaywayid`; 

/*[2012-10-26 13:23:14][35 ms]*/ 
INSERT INTO `express_set_menu_info_new`(`menuid`,`menuname`,`menuurl`,`menulevel`,`menuno`,`parentno`,`menuimage`,`adminshowflag`) VALUES ( '407','库对库扫描','/pda/kftokflist','3','102','303','images/kdksm.png',NULL); 
/*[2012-10-26 13:34:36][13 ms]*/ 
UPDATE `express_set_menu_info_new` SET `menuno`='103' WHERE `menuid`='403'; 
/*[2012-10-26 13:34:39][14 ms]*/ 
UPDATE `express_set_menu_info_new` SET `menuno`='104' WHERE `menuid`='404'; 



/*[2012-10-26 18:13:05][29 ms]*/ 
INSERT INTO express_set_switch (`id`,`switchname`,`state`) VALUES ( '2','单票反馈','dpfk_01'); 



/* 2012-10-26 [15:27:58][72 ms] 新增货物体积*/
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `cargovolume` DECIMAL(19,4) DEFAULT 0; 
/*2012-10-26 [15:47:54][14 ms] 新增 tmall通知id，验证字段是否重复，唯一标示 */
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `tmall_notify_id` VARCHAR(50) ;
/* 2012-10-26 [14:23:48][364 ms] 发货仓库新增字段，仓库编号*/ 
ALTER TABLE `express_set_customer_warehouse` ADD COLUMN `warehouse_no` VARCHAR(50) NULL; 
/* 2012-10-26 [14:59:20][36 ms] detail表新增字段，credate创建时间*/
 ALTER TABLE `express_ops_cwb_detail`  ADD COLUMN `credate` VARCHAR(50) DEFAULT 'NOW()' NULL ; 
  /* 2012-10-26 [16:06:29][24 ms] detail中新建multi_transcwb 运单号逗号隔开，tmall反馈用到*/ 
 ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `multi_transcwb` VARCHAR(1000) NULL ; 
 
/*====================================10-28===============================================*/

 /* 2012-10-29 新增字段：取件地址[12:38:21][53 ms]*/ 
ALTER TABLE `express_ops_cwb_detail` ADD COLUMN `consignoraddress` VARCHAR(200) NULL ;

/* 2012-10-29 新增虚拟库房，写死，不可改 [14:15:00][1 ms]*/ 
INSERT INTO `express_set_branch`(`branchname`)VALUES ('虚拟库房');


/*[10-29 11:40:45][19 ms]增加入库的时候是否打印订单的开关*/ 
INSERT INTO `express_set_switch`(`id`,`switchname`,`state`) VALUES ( NULL,'入库打印标签','rkbq_01'); 
/*[16:52:53][29 ms] 修改导出模板中 导出字段名称*/ 
UPDATE `express_ops_setexportfield` SET `fieldname`='配送站点' WHERE `id`='16'; 

/*[12-10-30 16:52:53][29 ms] 修改表主键，加快检索速度*/ 
ALTER TABLE `express_ops_delivery_state` CHANGE COLUMN `cwb` `cwb` VARCHAR(100) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL  
, DROP PRIMARY KEY 
, ADD PRIMARY KEY (`id`) ;
ALTER TABLE `express_ops_delivery_state`  DROP INDEX `id`  , ADD INDEX `id` (`cwb` ASC) ;
/*====================================10-30===============================================*/

ALTER TABLE `express_ops_order_flow`  ADD INDEX `FlowCwbIdx` (`cwb` ASC) ;

/*[20:14:03][112 ms]暂时关掉入库会打印的开关*/ 
UPDATE `express_set_switch` SET `state`='rkbq_02' WHERE `id`='3'; 
/*====================================10-31===============================================*/
/*[12-10-31 13:24:36][99 ms]增加库房与导入数据中 省市区的对应表*/ 
CREATE TABLE `express_set_warehouse_key`( 
`id` INT(11) NOT NULL AUTO_INCREMENT, 
`targetcarwarehouseid` INT(11), 
`keyname` VARCHAR(50) DEFAULT '', 
`ifeffectflag` INT(2) DEFAULT 1, 
PRIMARY KEY (`id`) 
) ENGINE=INNODB CHARSET=utf8 COLLATE=utf8_bin; 


/*2012-10-31 修改detail表中multi_transcwb 改为 multi_shipcwb [17:13:32][23 ms]*/
ALTER TABLE `express_ops_cwb_detail` CHANGE `multi_transcwb` `multi_shipcwb` VARCHAR(1000) CHARSET utf8 COLLATE utf8_bin NULL; 
 

/*[12-10-31 17:23:29][15 ms]添加了菜单*/
INSERT  INTO `express_set_menu_info_new`(`menuid`,`menuname`,`menuurl`,`menulevel`,`menuno`,`parentno`,`menuimage`,`adminshowflag`) VALUES (408,'库房对应省市管理','/warehousekey/list/1','3','G2111','215','images/kfdyssgl.png',NULL)
 

/*[12-11-02 16:35:44][24 ms]添加了菜单*/
INSERT  INTO `express_set_menu_info_new`(`menuid`,`menuname`,`menuurl`,`menulevel`,`menuno`,`parentno`,`menuimage`,`adminshowflag`) VALUES (409,'交接单打印（详）',NULL,'2','106','22',NULL,NULL);
INSERT  INTO `express_set_menu_info_new`(`menuid`,`menuname`,`menuurl`,`menulevel`,`menuno`,`parentno`,`menuimage`,`adminshowflag`) VALUES (410,'交接单打印（详）',NULL,'2','204','263',NULL,NULL);
INSERT  INTO `express_set_menu_info_new`(`menuid`,`menuname`,`menuurl`,`menulevel`,`menuno`,`parentno`,`menuimage`,`adminshowflag`) VALUES (411,'出库打印','/warehousegroupdetail/outlist/1','3','101','409','images/ckjjdy.png',NULL);

/*[2012-11-3 18:45:50][ 594 ms] 供应商表添加一个字段，对接的枚举编号*/ 
ALTER TABLE `express_set_customer_info` ADD COLUMN `b2cEnum` VARCHAR(50) NULL AFTER `customer_pos_code`;

/*[2012-11-5 11:45:50][ 594 ms] 修改反馈表的主键，此语句已执行*/ 
ALTER TABLE express_ops_delivery_state CHANGE `cwb` `cwb` VARCHAR(100) CHARSET utf8 COLLATE utf8_bin NOT NULL,
 DROP PRIMARY KEY, ADD PRIMARY KEY (`id`, `cwb`); 
ALTER TABLE express_ops_delivery_state DROP INDEX `id`; 

/*[2012-11-5 15:47:44][22 ms]添加了二级分拨菜单*/
INSERT  INTO `express_set_menu_info_new`(`menuid`,`menuname`,`menuurl`,`menulevel`,`menuno`,`parentno`,`menuimage`,`adminshowflag`) VALUES (412,'二级分拨','/cwborder/cwbresetbranch','3','103','305','images/ejfb.png',NULL);

/*[2012-11-5 15:29:42][41 ms]添加了导入数据的开关*/ 
INSERT INTO `express_set_switch`(`switchname`,`state`) VALUES ('导入数据创建发货仓库','cjfhck_02'); 

/*[2012-11-6 10:26:55][39 ms]添加了中转出站、退货出站、中转站出库、退货站再投的详情交接单菜单*/
INSERT  INTO `express_set_menu_info_new`(`menuid`,`menuname`,`menuurl`,`menulevel`,`menuno`,`parentno`,`menuimage`,`adminshowflag`) VALUES (413,'中转出站打印','/warehousegroupdetail/outlist/1','3','101','410','images/zzczjjdy.png',NULL);
INSERT  INTO `express_set_menu_info_new`(`menuid`,`menuname`,`menuurl`,`menulevel`,`menuno`,`parentno`,`menuimage`,`adminshowflag`) VALUES (414,'退货出站打印','/warehousegroupdetail/returnlist/1','3','102','410','images/thczjjdy.png',NULL);
INSERT  INTO `express_set_menu_info_new`(`menuid`,`menuname`,`menuurl`,`menulevel`,`menuno`,`parentno`,`menuimage`,`adminshowflag`) VALUES (415,'中转站出库打印','/warehousegroupdetail/transbranchoutlist/1','3','102','409','images/zzckdy.png',NULL);
INSERT  INTO `express_set_menu_info_new`(`menuid`,`menuname`,`menuurl`,`menulevel`,`menuno`,`parentno`,`menuimage`,`adminshowflag`) VALUES (416,'退货站再投打印','/warehousegroupdetail/returnbranchoutlist/1','3','103','409','images/zzckzt.png',NULL);

/*[18:23:05][206 ms]反馈表添加了字段*/ 
ALTER TABLE `express_ops_delivery_state` ADD COLUMN `userid` INT(11) DEFAULT 0 NULL AFTER `pos_feedback_flag`; 
/*[12-11-06][1 ms] 插入修改签收按钮开关*/
INSERT INTO `express_set_switch`(`id`,`switchname`,`state`) VALUES ( NULL,'changesing','xgqs_01'); 

/*[12-11-07 18:05:41][168 ms]修改开关表中的字段长度*/ 
ALTER TABLE `express_set_switch` CHANGE `state` `state` VARCHAR(20) CHARSET utf8 COLLATE utf8_bin DEFAULT '' NULL; 

/*[12-11-07 17:59:47][259 ms]插入批量反馈支付方式的开关*/ 
INSERT INTO `express_set_switch`(`id`,`switchname`,`state`) VALUES ( NULL,'批量反馈支付方式pos','plzffs_02'); 

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;

/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
