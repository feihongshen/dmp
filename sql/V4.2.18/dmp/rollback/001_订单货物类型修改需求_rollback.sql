-- 创建表,用于存放货物类型修改申请的数据
drop table  express_ops_applyeditcartype ;

-- 添加新页面菜单
delete from dmp40_function where id=602566 ;
delete from dmp40_function where id=602567 ;
