SELECT '应付甲方余额月报表' AS '脚本注释', IF((SELECT COUNT(1) FROM dmp40_function WHERE functionname='应付甲方余额月报表' and parentfunctionid='8020')=1, 'success', 'failed') AS '执行结果';
