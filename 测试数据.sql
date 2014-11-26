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


DELETE FROM express_set_account_area;
INSERT  INTO `express_set_account_area`(`areaid`,`areaname`,`arearemark`,`customerid`,`isEffectFlag`) VALUES (1,'北京西城','',123,1);

/*Data for the table `express_set_branch` */
DELETE FROM express_set_branch;
INSERT  INTO `express_set_branch`(`branchid`,`branchname`,`branchaddress`,`branchcontactman`,`branchphone`,`branchmobile`,`branchfax`,`branchemail`,`contractflag`,`contractrate`,`cwbtobranchid`,`branchcode`,`payfeeupdateflag`,`backtodeliverflag`,`branchpaytoheadflag`,`branchfinishdayflag`,`creditamount`,`branchwavfile`,`brancheffectflag`,`noemailimportflag`,`errorcwbdeliverflag`,`errorcwbbranchflag`,`branchcodewavfile`,`importwavtype`,`exportwavtype`,`branchinsurefee`,`branchprovince`,`branchcity`,`noemaildeliverflag`,`sendstartbranchid`,`functionids`,`sitetype`,`checkremandtype`,`branchmatter`,`accountareaid`) VALUES (1,'公司总部','','管理员','','','','','','0.000','','','','','','','0.00','','','','','','','','','0.00','','','',0,'',8,16,'',0),(179,'北京库房','','杨志晓','','','','','','0.000','','','','','','','0.00','','','','','','','','','0.00','','','',0,'P01,P02,P03,P04,P06,P07,P08,P09,P10,P15,P16,P17,P18,P19',1,19,'',0),(180,'上海库房','','杨志晓','','','','','','0.000','','','','','','','0.00','','','','','','','','','0.00','','','',0,'P01,P02,P03,P04,P06,P07,P08,P09,P10,P15,P16,P17,P18,P19',1,19,'',0),(181,'国贸站','','杨志晓','','','','','','0.000','','','','','','','0.00','','','','','','','','','0.00','','','',0,'P04,P05,P06,P07,P08,P09,P10,P15,P16,P17,P18,P19',2,0,'',0),(182,'北京西城站','','杨志晓','','','','','','0.000','','','','','','','0.00','','','','','','','','','0.00','','','',0,'P04,P05,P06,P07,P08,P09,P10,P15,P16,P17,P18,P19',2,0,'',0),(183,'上地五环站','','杨志晓','','','','','','0.000','','','','','','','0.00','','','','','','','','','0.00','','','',0,'P04,P05,P06,P07,P08,P09,P10,P15,P16,P17,P18,P19',2,0,'',0),(184,'退货站','','杨志晓','','','','','','0.000','','','','','','','0.00','','','','','','','','','0.00','','','',0,'P04,P11,P12,P15,P16,P17,P18,P19',3,18,'',0),(185,'中转站','','杨志晓','','','','','','0.000','','','','','','','0.00','','','','','','','','','0.00','','','',0,'P04,P13,P14,P15,P16,P17',4,17,'',0);

/*Data for the table `express_set_customer_info` */
DELETE FROM express_set_customer_info;
INSERT  INTO `express_set_customer_info`(`customerid`,`customername`,`customerno`,`customeraddress`,`customerpostcode`,`customercontactman`,`customerphone`,`customermobile`,`customerfax`,`paywayid`,`paystartday`,`payendday`,`customerlevelid`,`customerremark`,`customercreateuserid`,`customercreatetime`,`customerpassword`,`paytransfeeid`,`monthflag`,`monthtypeid`,`startday`,`endday`,`deposit`,`carsplit`,`transfeecreateid`,`commissionfeecreateid`,`customercode`,`customertypeindmp`,`custprovinceid`,`custeffectflag`,`messagecustomername`,`clearwayid`,`clearcycleid`,`transfeeclearcycleid`,`transfeetypeid`,`chargefeerate`,`poschargefeerate`,`customerno2`,`customerno3`,`customerno4`,`customerno5`,`customerpassword2`,`customerpassword3`,`customerpassword4`,`customerpassword5`,`accountbank`,`accountbankno`,`accountname`,`isBackAskFlag`,`ifeffectflag`,`payaccountcode`,`isBeforeScanningflag`,`customer_pos_code`) VALUES (123,'当当网',NULL,'北京西城',NULL,'杨志晓','15810133740',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL);

/*Data for the table `express_set_customer_warehouse` */
DELETE FROM express_set_customer_warehouse;
INSERT  INTO `express_set_customer_warehouse`(`warehouseid`,`customerid`,`customerwarehouse`,`warehouseremark`,`ifeffectflag`) VALUES (2,123,'北京仓库','',1);

/*Data for the table `express_set_role_menu_new` */
DELETE FROM express_set_role_menu_new;
INSERT  INTO `express_set_role_menu_new`(`roleid`,`menuid`) VALUES (1,22),(0,1),(0,2),(0,5),(0,22),(0,34),(0,35),(0,36),(0,267),(0,268),(0,286),(0,287),(0,263),(0,264),(0,262),(0,261),(0,260),(0,259),(0,289),(0,152),(0,288),(0,1063),(0,1067),(0,1071),(0,1068),(0,1072),(0,1069),(0,1073),(0,1070),(0,1074),(0,1064),(0,1075),(0,1093),(0,1077),(0,1076),(0,1078),(0,1094),(0,1079),(0,1080),(0,1081),(0,1082),(0,1083),(0,1084),(0,1065),(0,1085),(0,1087),(0,1088),(0,1089),(0,1086),(0,1090),(0,1066),(0,1091),(0,1092),(0,207),(0,208),(0,211),(0,247),(0,212),(0,213),(0,215),(0,238),(0,257),(0,3),(0,4),(0,258),(0,225),(0,226),(0,227),(0,228),(0,229),(0,265),(0,231),(0,8),(0,9),(0,255),(0,232),(0,234),(0,269),(0,270),(0,271),(0,272),(0,273),(0,274),(0,275),(0,276),(0,277),(0,278),(0,279),(0,280),(0,281),(0,282),(0,283),(0,284),(0,285),(10,272),(10,281),(10,282),(10,284),(10,285),(9,272),(9,279),(9,280),(9,284),(9,285),(9,296),(9,297),(8,263),(8,290),(8,291),(8,292),(8,293),(8,207),(8,208),(8,212),(8,213),(8,272),(8,275),(8,276),(8,277),(8,278),(8,284),(8,285),(7,272),(7,273),(7,274),(7,277),(7,278),(7,284),(7,285),(6,1),(6,2),(6,5),(6,22),(6,34),(6,35),(6,36),(6,263),(6,290),(6,291),(6,292),(6,293),(6,294),(6,295),(6,1065),(6,1085),(6,1087),(6,1088),(6,1089),(6,1086),(6,1090),(6,1066),(6,1091),(6,1092),(6,152),(6,288),(6,207),(6,208),(6,212),(6,213),(6,215),(6,3),(6,4),(6,225),(6,226),(6,227),(6,228),(6,229),(6,265),(5,269),(5,270),(5,271),(5,272),(5,275),(5,276),(5,277),(5,278),(5,284),(5,285),(4,263),(4,264),(4,262),(4,261),(4,260),(4,259),(4,289),(4,290),(4,291),(4,292),(4,293),(4,294),(4,295),(4,207),(4,208),(4,212),(4,213),(4,272),(4,273),(4,274),(4,275),(4,276),(4,277),(4,278),(4,284),(4,285);

/*Data for the table `express_set_role_new` */
DELETE FROM express_set_role_new;
INSERT  INTO `express_set_role_new`(`roleid`,`rolename`,`type`) VALUES (0,'管理员',0),(1,'客服',0),(2,'小件员',0),(3,'驾驶员',0),(4,'站长',0),(5,'库房操作员',1),(6,'数据处理员',1),(7,'站长助理',1),(8,'结算员',1),(9,'退货操作员',1),(10,'中转操作员',1);

/*Data for the table `express_set_truck` */
DELETE FROM express_set_truck;
INSERT  INTO `express_set_truck`(`truckid`,`truckno`,`trucktype`,`truckoil`,`truckway`,`truckkm`,`truckstartkm`,`truckdriver`,`truckflag`) VALUES (3,'京P123456','',0,'',0,0,989,1),(4,'京N234567','',0,'',0,0,992,1);

/*Data for the table `express_set_user` */
DELETE FROM express_set_user;
INSERT  INTO `express_set_user`(`userid`,`username`,`password`,`realname`,`idcardno`,`employeestatus`,`branchid`,`userphone`,`usermobile`,`useraddress`,`userremark`,`usersalary`,`usercustomerid`,`showphoneflag`,`useremail`,`deliverpaytype`,`userwavfile`,`branchmanagerflag`,`roleid`) VALUES (1,'admin','admin','管理员','0',1,1,NULL,'0',NULL,NULL,NULL,0,'1','NULL',0,'NULL',0,0),(988,'zy','zy','赵一','',1,179,NULL,'',NULL,NULL,NULL,0,'0','',NULL,'',NULL,2),(989,'qr','qr','钱二','',1,179,NULL,'',NULL,NULL,NULL,0,'0','',NULL,'',NULL,3),(990,'zs','zs','张三','',1,179,NULL,'',NULL,NULL,NULL,0,'0','',NULL,'',NULL,5),(991,'ls','ls','李四','',1,181,NULL,'',NULL,NULL,NULL,0,'0','',NULL,'',NULL,2),(992,'ww','ww','王五','',1,181,NULL,'',NULL,NULL,NULL,0,'0','',NULL,'',NULL,3),(993,'ll','ll','梁六','',1,181,NULL,'',NULL,NULL,NULL,0,'0','',NULL,'',NULL,4),(994,'yq','yq','杨七','',1,181,NULL,'',NULL,NULL,NULL,0,'0','',NULL,'',NULL,7),(995,'lb','lb','刘八','',1,179,NULL,'',NULL,NULL,NULL,0,'0','',NULL,'',NULL,6),(996,'sj','sj','孙九','',1,181,NULL,'',NULL,NULL,NULL,0,'0','',NULL,'',NULL,8),(997,'zsi','zsi','周十','',1,184,NULL,'',NULL,NULL,NULL,0,'0','',NULL,'',NULL,9),(998,'wsy','wsy','吴十一','',1,185,NULL,'',NULL,NULL,NULL,0,'0','',NULL,'',NULL,10);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
