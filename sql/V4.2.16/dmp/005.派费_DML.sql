-- 更新订单计费定时器描述
UPDATE eap_qrtz_triggers
SET DESCRIPTION = '派费生成定时任务'
WHERE TRIGGER_NAME = 'dfCalculateServiceExecutorCronTrigger';

DELETE FROM `dmp40_function` WHERE `ID` = '8091';
DELETE FROM `dmp40_function` WHERE `ID` = '809110';
DELETE FROM `dmp40_function` WHERE `ID` = '809120';
DELETE FROM `dmp40_function` WHERE `ID` = '809130';
DELETE FROM `dmp40_function` WHERE `ID` = '809140';
DELETE FROM `dmp40_function` WHERE `ID` = '809150';
DELETE FROM `dmp40_function` WHERE `ID` = '809160';
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('8091', '1', '小件员派费结算', '8091', '', '80');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809101', '2', '新增派费协议', '809101', '${eapUrl}deliveryfeeagreement.do?add&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809102', '2', '派费协议管理', '809102', '${eapUrl}deliveryfeeagreement.do?manager&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809103', '2', '账单基础信息设置', '809103', '${eapUrl}dfBillPeriodController.do?index&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809104', '2', '月度妥投率报表', '809104', '${eapUrl}confirmRateManager.do?index&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809105', '2', '奖罚登记管理', '809105', '${eapUrl}orgDisciplineRecord.do?index&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809106', '2', '加盟站派费账单管理', '809106', '${eapUrl}orgDfBill.do?index&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809107', '2', '小件员派费账单管理', '809107', '${eapUrl}courierDfBill.do?index&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809108', '2', '加盟站点计费订单明细', '809108', '${eapUrl}branchDfFee.do?index&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809109', '2', '小件员计费订单明细', '809109', '${eapUrl}courierDfFee.do?index&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809110', '2', '派费调整记录', '809110', '${eapUrl}dfAdjustmentRecord.do?index&', '8091');








