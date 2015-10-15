package cn.explink.service.fnc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.fnc.OrderDetailsSnapshotDao;
import cn.explink.domain.CwbOrderSnapshot;
import cn.explink.enumutil.CwbOrderTypeIdEnum;

@Component
public class OrderLifeCycleFeeNotReturnFeeHandler extends
		OrderLifeCycleBatchHandlerTemplate<CwbOrderSnapshot> {

	
	@Autowired
	private OrderFlowDAO orderFlowDAO;
	@Autowired
	private OrderDetailsSnapshotDao orderDetailsSnapshotDao;
	
	
	@Override
	protected void doBatch(List<CwbOrderSnapshot> batchList,int reportdate) {
			
		String cwbs = getCwbsFromCwbOrderSnapshotList(batchList,reportdate);
		
		this.orderDetailsSnapshotDao.disableRowByNotReturnedFeeByReportDate(getCwbOrderTypeId(), reportdate);
		
		orderDetailsSnapshotDao.batchInsertOrderDetailSnapshot(batchList);
		
	}

	@Override
	protected List<CwbOrderSnapshot> getNextBatchList(int currentBatch, int batchSzie,
			long lastRowId) {
		return this.orderDetailsSnapshotDao
				.getListFeeNotReturnedFromCwbDetailByPage(getCwbOrderTypeId(),currentBatch, batchSzie,lastRowId);
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
		return this.orderDetailsSnapshotDao.countFeeNotReturnedFromCwbDetail(getCwbOrderTypeId());
	}

	private int getCwbOrderTypeId(){
		return CwbOrderTypeIdEnum.Peisong.getValue();
	}
	
	/**
	 * Get the cwb in format '1,3,2,3' from list;
	 */
	private String getCwbsFromCwbOrderSnapshotList(List<CwbOrderSnapshot> cwbOrderListForBatchSave, int reportdate){
		
		String cwbs = null;
		
		if(CollectionUtils.isNotEmpty(cwbOrderListForBatchSave)){
			List<String> cwbList = new ArrayList<String>();
			for (CwbOrderSnapshot cwbOrderSnapshot : cwbOrderListForBatchSave) {
				
				cwbOrderSnapshot.setReportdate(reportdate);
				cwbList.add(cwbOrderSnapshot.getCwb());
				
			}
			cwbs = StringUtils.join(cwbList, "','");
			cwbs = "'" + cwbs + "'";
		}
		
		return cwbs;
	}
}
