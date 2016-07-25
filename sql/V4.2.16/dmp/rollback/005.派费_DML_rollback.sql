-- 更新订单计费定时器描述
UPDATE eap_qrtz_triggers
SET DESCRIPTION = '加盟派费生成账单定时任务'
WHERE TRIGGER_NAME = 'dfCalculateServiceExecutorCronTrigger';

DELETE FROM `dmp40_function` WHERE `ID` = '8091';
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
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('8091', '1', '加盟站点派费结算管理', '8091', '', '80');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809110', '2', '新增派费协议', '809110', '${eapUrl}deliveryfeeagreement.do?add&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809120', '2', '派费协议管理', '809120', '${eapUrl}deliveryfeeagreement.do?manager&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809130', '2', '派费账期管理', '809130', '${eapUrl}dfBillPeriodController.do?index&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809140', '2', '派费奖罚管理', '809140', '${eapUrl}orgDisciplineRecord.do?index&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809150', '2', '派费账单管理', '809150', '${eapUrl}dfBillController.do?index&', '8091');
INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809160', '2', '加盟站点月度妥投率报表', '809160', '${eapUrl}branchFeeManager.do?index&', '8091');