SELECT
  '删除原快递揽件录入页面'                                                                      AS '检查项',
  IF((SELECT count(*)
      FROM `dmp40_function`
      WHERE `ID` = '504088' AND `functionname` = '快递揽件录入') < 1, 'success', 'failed') AS '执行结果'
UNION
SELECT
  '揽收运单补录页面更名'                                                                     AS '检查项',
  IF((SELECT count(*)
      FROM `dmp40_function`
      WHERE `ID` = '4010' AND `functionname` = '快递揽件录入') = 1, 'success', 'failed') AS '执行结果';