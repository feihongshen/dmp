select
  '001_交接单打印刷数脚本(武汉).sql' as '执行脚本', 
  case count(1) when 4 then 'success' else 'failure' end as '执行结果'  
from express_set_print_template 
where customname = '武汉飞远';