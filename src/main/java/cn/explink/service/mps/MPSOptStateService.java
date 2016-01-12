/**
 *
 */
package cn.explink.service.mps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
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
		// 处理拦截后下一站更新
		this.getCwbDAO().updateNextBranchid(cwbOrder.getCwb(), nextbranchid);
		// 更新运单操作状态，上一站 下一站
		TransCwbDetail transCwbDetail = this.getTransCwbDetailDAO().findTransCwbDetailByTransCwb(transCwb);
		if (transCwbDetail == null) {
			MPSOptStateService.LOGGER.error(MPSOptStateService.UPDATE_MPS_STATE + "没有查询到运单号" + transCwb + "对应的运单信息！");
			return;
		}
		transCwbDetail.setCurrentbranchid(currentbranchid);
		transCwbDetail.setNextbranchid(nextbranchid);
		transCwbDetail.setTranscwboptstate(transcwboptstate.getValue());
		this.getTransCwbDetailDAO().updateTransCwbDetail(transCwbDetail);
	}

	private void updateMPSOptState(CwbOrder cwbOrder) {
		String cwb = cwbOrder.getCwb();
		int mpsallarrivedflag = cwbOrder.getMpsallarrivedflag();
		// 如果一票多件没有到齐，则更新为初始状态（导入数据）
		int latestMPSState = FlowOrderTypeEnum.DaoRuShuJu.getValue();
		if (MPSAllArrivedFlagEnum.YES.getValue() == mpsallarrivedflag) {
			List<TransCwbDetail> transCwbDetailList = this.getTransCwbDetailDAO().getTransCwbDetailListByCwb(cwb);
			Map<String, Queue<Integer>> transCwbMap = new HashMap<String, Queue<Integer>>();
			// express_ops_transcwb_orderflow表中没有订单是否被废弃标志，两个大表连表查询性能太差，并且
			// 一票多件子订单数量不会太多，所以选择循环查询
			for (TransCwbDetail transCwbDetail : transCwbDetailList) {
				String transcwbInMap = transCwbDetail.getTranscwb();
				Queue<Integer> transcwboptstateQueue = new LinkedList<Integer>();
				transcwboptstateQueue.add(Integer.valueOf(FlowOrderTypeEnum.DaoRuShuJu.getValue()));
				// 获取运单状态流程信息
				List<TranscwbOrderFlow> transcwbOrderFlowList = this.transcwbOrderFlowDAO.getTranscwbOrderFlowByScanCwb(transcwbInMap, cwb);

				for (TranscwbOrderFlow transcwbOrderFlow : transcwbOrderFlowList) {
					transcwboptstateQueue.add(transcwbOrderFlow.getFlowordertype());
				}
				transCwbMap.put(transcwbInMap, transcwboptstateQueue);
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

	private int getLatestState(Map<String, Queue<Integer>> transCwbMap) {
		Set<String> keySet = transCwbMap.keySet();
		List<Queue<Integer>> queueList = new ArrayList<Queue<Integer>>();

		int longestQueueLength = 0;
		for (String key : keySet) {
			Queue<Integer> queue = transCwbMap.get(key);
			queueList.add(queue);
			if (queue.size() > longestQueueLength) {
				longestQueueLength = queue.size();
			}
		}
		int currentState = FlowOrderTypeEnum.DaoRuShuJu.getValue();
		boolean hasFoundState = false;
		for (int i = 0; i < longestQueueLength; i++) {
			Integer firstState = queueList.get(0).poll();
			for (int j = 1; j < queueList.size(); j++) {
				Integer head = queueList.get(j).poll();
				if ((firstState == null) || (head == null) || (head != firstState)) {
					hasFoundState = true;
					break;
				}
				currentState = head;
			}
			if (hasFoundState) {
				break;
			}
		}
		return currentState;
	}

}
