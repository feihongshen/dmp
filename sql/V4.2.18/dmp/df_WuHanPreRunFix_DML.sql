INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('809111', '2', '妥投考核报表', '809113', '${eapUrl}confirmRateSubsidyManager.do?index&', '8091');

ALTER TABLE `fn_df_bill_staff` ADD COLUMN `confirm_rate_subsidy_sanction` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '妥投率补贴奖罚';
