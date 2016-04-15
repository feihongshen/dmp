-- 结果为1
select count(1) cnt
from INFORMATION_SCHEMA.STATISTICS
where TABLE_SCHEMA = schema()
	and TABLE_NAME = 'express_ops_cwb_detail'
	and INDEX_NAME = 'detail_cwbordertypeid_idx'
	and COLUMN_NAME = 'cwbordertypeid';