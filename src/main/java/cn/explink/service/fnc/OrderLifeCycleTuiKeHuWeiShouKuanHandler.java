package cn.explink.service.fnc;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.fnc.OrderDetailsSnapshotDao;
import cn.explink.domain.CwbOrderSnapshot;
import cn.explink.enumutil.CwbOrderTypeIdEnum;

/**
 * 退客户未收款的处理类，取订单主表中全部“退供货商成功”操作状态，但未做应收款账单核销的配送类型订单，并且将订单快照表中的对应的记录设置为state为0
 * 
 * @author jinghui.pan
 *
 */
@Component
public class OrderLifeCycleTuiKeHuWeiShouKuanHandler extends OrderLifeCycleBaseBatchHandler {

	
	@Autowired
	private OrderFlowDAO orderFlowDAO;
	@Autowired
	private OrderDetailsSnapshotDao orderDetailsSnapshotDao;
	
	
	@Override
	protected void doBatch(List<CwbOrderSnapshot> batchList,int reportdate) {
		
		super.doBatch(batchList, reportdate);
		
		this.orderDetailsSnapshotDao.disableRowByTuiKeHuWeiShouKuanByReportDate(getCwbOrderTypeId(), reportdate);
		
		orderDetailsSnapshotDao.batchInsertOrderDetailSnapshot(batchList);
	}

	@Override
	protected List<CwbOrderSnapshot> getNextBatchList(int currentBatch, int batchSzie,
			long lastRowId) {
		return this.orderDetailsSnapshotDao
				.getListTuiKeHuWeiShouKuanFromCwbDetailByPage(getCwbOrderTypeId(), batchSzie,lastRowId);
	}

	@Override
	protected long getLastRowId(List<CwbOrderSnapshot> batchList) {
		long res = 0L;
		
		if(CollectionUtils.isNotEmpty(batchList)){
			res = batchList.get(batchList.size() - 1).getOpscwbid();
		}
		
		return res;
	}

	@Override
	protected long countBatch() {
		return this.orderDetailsSnapshotDao.countTuiKeHuWeiShouKuanFromCwbDetail(getCwbOrderTypeId());
	}

	private int getCwbOrderTypeId(){
		return CwbOrderTypeIdEnum.Peisong.getValue();
	}
	
}
