SELECT '检查小件员菜单数据' AS '脚本注释',IF((
(SELECT COUNT(1) FROM dmp40_function WHERE ID = '809113' AND functionname='妥投考核报表')
)= 1 ,'success','failed') AS '执行结果';