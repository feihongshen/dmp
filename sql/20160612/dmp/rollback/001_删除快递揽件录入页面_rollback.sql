INSERT INTO `dmp40_function` (`ID`, `functionlevel`, `functionname`, `functionorder`, `functionurl`, `parentfunctionid`) VALUES ('504088', '2', '快递揽件录入', '504088', 'embracedOrderInputController/embracedOrderInputInit/?', '5040');

UPDATE `dmp40_function` SET `functionname` = '揽收运单补录' WHERE `ID` = '4010'  AND  `functionname` = '快递揽件录入';
