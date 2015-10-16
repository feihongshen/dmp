package cn.explink.service.fnc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import cn.explink.domain.CwbOrderSnapshot;

/**
 * 声明周期报表，批量处理类，用于在flow表处理完数据后做额外的处理
 * 
 * @author jinghui.pan
 *
 */
public abstract class OrderLifeCycleBaseBatchHandler extends BatchHandlerTemplate<CwbOrderSnapshot> {

	@Override
	protected void doBatch(List<CwbOrderSnapshot> batchList,int reportdate) {
		
		injectReportdateInCwbOrderSnapshotList(batchList,reportdate);
		
	}
	
	
	/**
	 * Get the cwb in format '1,3,2,3' from list;
	 */
	protected String getCwbsFromCwbOrderSnapshotList(List<CwbOrderSnapshot> cwbOrderListForBatchSave, int reportdate){
		
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
	
	/**
	 * Inject the report date into the order snapshot list;
	 */
	private void injectReportdateInCwbOrderSnapshotList(List<CwbOrderSnapshot> cwbOrderListForBatchSave, int reportdate){
		
		if(CollectionUtils.isNotEmpty(cwbOrderListForBatchSave)){
			List<String> cwbList = new ArrayList<String>();
			for (CwbOrderSnapshot cwbOrderSnapshot : cwbOrderListForBatchSave) {
				
				cwbOrderSnapshot.setReportdate(reportdate);
				cwbList.add(cwbOrderSnapshot.getCwb());
				
			}
		}
	}
}
