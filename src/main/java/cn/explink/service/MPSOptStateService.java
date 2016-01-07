/**
 *
 */
package cn.explink.service;

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

import cn.explink.core.utils.StringUtils;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.TranscwbOrderFlowDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.orderflow.TranscwbOrderFlow;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.support.transcwb.TranscwbView;

/**
 *
 *
 * @author songkaojun 2016年1月6日
 */
@Component
public class MPSOptStateService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MPSOptStateService.class);

	private static final String LOG_MSG_PREFIX = "[更新一票多件状态]";

	@Autowired
	private TransCwbDao transCwbDao;

	@Autowired
	private TranscwbOrderFlowDAO transcwbOrderFlowDAO;

	@Autowired
	private CwbDAO cwbDAO;

	@Autowired
	private CustomerDAO customerDAO;

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
		if (StringUtils.isEmpty(transCwb)) {
			MPSOptStateService.LOGGER.info(MPSOptStateService.LOG_MSG_PREFIX + "传入的运单号为空！");
			return;
		}
		// 根据运单号查询订单
		String cwb = this.transCwbDao.getCwbByTransCwb(transCwb);
		if (StringUtils.isEmpty(cwb)) {
			MPSOptStateService.LOGGER.info(MPSOptStateService.LOG_MSG_PREFIX + "根据传入的运单号没有查询到相应的订单号！");
			return;
		}
		CwbOrder cwbOrder = this.cwbDAO.getCwborder(cwb);
		if (cwbOrder == null) {
			MPSOptStateService.LOGGER.info(MPSOptStateService.LOG_MSG_PREFIX + "没有查询到订单数据！");
			return;
		}

		// 查询供应商，判断该供应商是否开启了集单模式
		long customerid = cwbOrder.getCustomerid();
		Customer customer = this.customerDAO.getCustomerById(customerid);
		if (customer.getCustomerid() == 0L) {
			MPSOptStateService.LOGGER.info(MPSOptStateService.LOG_MSG_PREFIX + "没有查询到订单的供应商信息！");
			return;
		}
		// TODO 如果没有开启集单模式 return

		this.updateMPSOptState(cwb);

		// TODO 更新运单操作状态，上一站 下一站
	}

	private void updateMPSOptState(String cwb) {
		List<TranscwbView> transCwbViewList = this.transCwbDao.getTransCwbByCwb(cwb);
		Map<String, Queue<Integer>> transCwbMap = new HashMap<String, Queue<Integer>>();
		// express_ops_transcwb_orderflow表中没有订单是否被废弃标志，两个大表连表查询性能太差，并且
		// 一票多件子订单数量不会太多，所以选择循环查询
		for (TranscwbView transcwbView : transCwbViewList) {
			String transcwbInMap = transcwbView.getTranscwb();
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
		int latestMPSState = this.getLatestState(transCwbMap);
		// 更新最晚状态到一票多件状态
		this.cwbDAO.updateMPSOptState(cwb, latestMPSState);
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
