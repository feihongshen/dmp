DELETE FROM `dmp40_function` WHERE `ID` = '809101';
DELETE FROM `dmp40_function` WHERE `ID` = '809102';
DELETE FROM `dmp40_function` WHERE `ID` = '809103';
DELETE FROM `dmp40_function` WHERE `ID` = '809104';
DELETE FROM `dmp40_function` WHERE `ID` = '809105';
DELETE FROM `dmp40_function` WHERE `ID` = '809106';
DELETE FROM `dmp40_function` WHERE `ID` = '809107';
DELETE FROM `dmp40_function` WHERE `ID` = '809108';
DELETE FROM `dmp40_function` WHERE `ID` = '809109';
DELETE FROM `dmp40_function` WHERE `ID` = '809110';

INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809101', '2', '新增派费协议', '809101', '${eapUrl}deliveryfeeagreement.do?add&fromPage=price&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809102', '2', '新增补贴协议','809102','${eapUrl}deliveryfeeagreement.do?add&fromPage=subsidy&','8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809103', '2', '派费协议管理', '809103', '${eapUrl}deliveryfeeagreement.do?manager&fromPage=price&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809104', '2', '补贴协议管理','809104','${eapUrl}deliveryfeeagreement.do?manager&fromPage=subsidy&','8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809105', '2', '账单基础信息设置', '809105', '${eapUrl}dfBillPeriodController.do?index&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809106', '2', '月度妥投率报表', '809106', '${eapUrl}confirmRateManager.do?index&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809107', '2', '妥投考核报表', '809107', '${eapUrl}confirmRateSubsidyManager.do?index&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809108', '2', '奖罚登记管理', '809108', '${eapUrl}orgDisciplineRecord.do?index&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809109', '2', '加盟站派费账单管理', '809109', '${eapUrl}orgDfBill.do?index&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809110', '2', '小件员派费账单管理', '809110', '${eapUrl}courierDfBill.do?index&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809111', '2', '加盟站点计费订单明细', '809111', '${eapUrl}branchDfFee.do?index&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809112', '2', '小件员计费订单明细', '809112', '${eapUrl}courierDfFee.do?index&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809113', '2', '派费调整记录', '809113', '${eapUrl}dfAdjustmentRecord.do?index&', '8091');
