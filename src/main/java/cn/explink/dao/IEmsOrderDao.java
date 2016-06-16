package cn.explink.dao;

import java.util.List;

import cn.explink.domain.EmsOrderPO;
import cn.explink.domain.QueryCondition;

public interface IEmsOrderDao {

	/**
	 * 获取未推送EMS订单的记录总数
	 * @param qc
	 * @return
	 */
	public  int getEmsUnpushOrderCount(QueryCondition qc) ;
	
	/**
	 * 获取未推送EMS订单的记录
	 * @param qc
	 * @return
	 */
	public List<EmsOrderPO> queryEmsUnpushOrder(QueryCondition qc) ;
	
	
}
