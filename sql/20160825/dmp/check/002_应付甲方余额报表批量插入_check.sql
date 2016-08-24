select '应付甲方余额批量处理' as '脚本注释' , if((select count(1) 
from express_set_system_install where name = 'BranceReportBatchSize') >= 1 ,'success', 'failed') as '执行结果' ;