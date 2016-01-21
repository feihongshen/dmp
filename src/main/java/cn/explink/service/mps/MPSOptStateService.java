/**
 *
 */
package cn.explink.service.mps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.dao.TranscwbOrderFlowDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.TransCwbDetail;
import cn.explink.domain.orderflow.TranscwbOrderFlow;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.MPSAllArrivedFlagEnum;

/**
 *
 *
 * @author songkaojun 2016年1月6日
 */
@Component
public class MPSOptStateService extends AbstractMPSService {

	private static final String UPDATE_MPS_STATE = "[更新一票多件状态]";

	private static final String UPDATE_TRANSCWB_INFO = "[更新运单信息]";

	private static final Logger LOGGER = LoggerFactory.getLogger(MPSOptStateService.class);

	@Autowired
	private TranscwbOrderFlowDAO transcwbOrderFlowDAO;

	/**
	 *
	 * 根据传入的运单号，更新运单号所属订单一票多件状态和运单操作状态、当前机构和下一机构信息
	 *
	 * @param transCwb
	 *            运单号
	 * @param transcwboptstate
	 *            运单操作状态
	 * @param currentbranchid
	 *            当前机构
	 * @param nextbranchid
	 *            下一机构信息
	 */
	public void updateMPSInfo(String transCwb, FlowOrderTypeEnum transcwboptstate, long currentbranchid, long nextbranchid) {
		CwbOrder cwbOrder = this.getMPSCwbOrderConsideringMPSSwitchType(transCwb, MPSOptStateService.UPDATE_MPS_STATE);
		if (cwbOrder == null) {
			return;
		}

		this.updateMPSOptState(cwbOrder);

		this.updateCwbOrderNextBranch(cwbOrder, currentbranchid, nextbranchid);

		this.updateTransCwbDetail(transCwb, transcwboptstate, currentbranchid, nextbranchid);
	}

	/**
	 * 更新运单的状态，上一站，当前站，下一站
	 *
	 * @param transCwb
	 * @param transcwboptstate
	 * @param currentbranchid
	 * @param nextbranchid
	 */
	public void updateTransCwbDetailInfo(String transCwb, FlowOrderTypeEnum transcwboptstate, long currentbranchid, long nextbranchid) {
		CwbOrder cwbOrder = this.getMPSCwbOrderConsideringMPSSwitchType(transCwb, MPSOptStateService.UPDATE_TRANSCWB_INFO);
		if (cwbOrder == null) {
			return;
		}
		this.updateTransCwbDetail(transCwb, transcwboptstate, currentbranchid, nextbranchid);
	}

	private void updateTransCwbDetail(String transCwb, FlowOrderTypeEnum transcwboptstate, long currentbranchid, long nextbranchid) {
		// 更新运单操作状态，上一站 下一站
		TransCwbDetail transCwbDetail = this.getTransCwbDetailDAO().findTransCwbDetailByTransCwb(transCwb);
		if (transCwbDetail == null) {
			MPSOptStateService.LOGGER.error(MPSOptStateService.UPDATE_MPS_STATE + "没有查询到运单号" + transCwb + "对应的运单信息！");
			return;
		}
		transCwbDetail.setPreviousbranchid(transCwbDetail.getCurrentbranchid());
		if (currentbranchid >= 0) {
			transCwbDetail.setCurrentbranchid(currentbranchid);
		}
		if (nextbranchid >= 0) {
			transCwbDetail.setNextbranchid(nextbranchid);
		}
		transCwbDetail.setTranscwboptstate(transcwboptstate.getValue());
		this.getTransCwbDetailDAO().updateTransCwbDetail(transCwbDetail);
	}

	private void updateCwbOrderNextBranch(CwbOrder cwbOrder, long currentbranchid, long nextbranchid) {
		if (currentbranchid < 0) {
			currentbranchid = cwbOrder.getCurrentbranchid();
		}
		if (nextbranchid < 0) {
			nextbranchid = cwbOrder.getNextbranchid();
		}
		this.getCwbDAO().updateBranchInfo(cwbOrder.getCwb(), currentbranchid, nextbranchid);
	}

	private void updateMPSOptState(CwbOrder cwbOrder) {
		String cwb = cwbOrder.getCwb();
		int mpsallarrivedflag = cwbOrder.getMpsallarrivedflag();
		// 如果一票多件没有到齐，则更新为初始状态（导入数据）
		int latestMPSState = FlowOrderTypeEnum.DaoRuShuJu.getValue();
		if (MPSAllArrivedFlagEnum.YES.getValue() == mpsallarrivedflag) {
			List<TransCwbDetail> transCwbDetailList = this.getTransCwbDetailDAO().getTransCwbDetailListByCwb(cwb);
			Map<String, List<Integer>> transCwbMap = new HashMap<String, List<Integer>>();
			// express_ops_transcwb_orderflow表中没有订单是否被废弃标志，两个大表连表查询性能太差，并且
			// 一票多件子订单数量不会太多，所以选择循环查询
			for (TransCwbDetail transCwbDetail : transCwbDetailList) {
				String transcwbInMap = transCwbDetail.getTranscwb();
				List<Integer> transCwbOptStateList = new ArrayList<Integer>();
				// 获取运单状态流程信息
				List<TranscwbOrderFlow> transcwbOrderFlowList = this.transcwbOrderFlowDAO.getTranscwbOrderFlowByScanCwbReverseOrder(transcwbInMap, cwb);

				for (TranscwbOrderFlow transcwbOrderFlow : transcwbOrderFlowList) {
					transCwbOptStateList.add(transcwbOrderFlow.getFlowordertype());
				}
				transCwbOptStateList.add(Integer.valueOf(FlowOrderTypeEnum.DaoRuShuJu.getValue()));
				transCwbMap.put(transcwbInMap, transCwbOptStateList);
			}
			// 计算出最晚的状态
			latestMPSState = this.getLatestState(transCwbMap);
		}
		// 更新最晚状态到一票多件状态
		this.getCwbDAO().updateMPSOptState(cwb, latestMPSState);
		if (MPSOptStateService.LOGGER.isInfoEnabled()) {
			MPSOptStateService.LOGGER.info(MPSOptStateService.UPDATE_MPS_STATE + "订单" + cwb + "一票多件状态更新为" + FlowOrderTypeEnum.getText(latestMPSState));
		}
	}

	private int getLatestState(Map<String, List<Integer>> transCwbMap) {
		Set<String> keySet = transCwbMap.keySet();
		List<List<Integer>> transCwbOptStateListList = new ArrayList<List<Integer>>();

		int shortestListLength = Integer.MAX_VALUE;
		for (String key : keySet) {
			List<Integer> transCwbOptStateList = transCwbMap.get(key);
			transCwbOptStateListList.add(transCwbOptStateList);
			if (transCwbOptStateList.size() < shortestListLength) {
				shortestListLength = transCwbOptStateList.size();
			}
		}
		// 当前状态
		Integer currentOptState = transCwbOptStateListList.get(0).get(0);
		for (int i = 0; i < shortestListLength; i++) {
			currentOptState = transCwbOptStateListList.get(0).get(i);
			int count = 0;
			for (int j = 1; j < transCwbOptStateListList.size(); j++) {
				List<Integer> transCwbOptStateList = transCwbOptStateListList.get(j);
				for (Integer transCwbOptState : transCwbOptStateList) {
					if (currentOptState == transCwbOptState) {
						count++;
					}
				}
			}
			// 最短的状态系列中的某个状态与其他每个状态系列都有相等的状态
			if (count == (transCwbOptStateListList.size() - 1)) {
				break;
			}
		}
		return currentOptState;
	}

}
