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
import cn.explink.dao.TransCwbDetailDAO;
import cn.explink.dao.TranscwbOrderFlowDAO;
import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.TransCwbDetail;
import cn.explink.domain.orderflow.TranscwbOrderFlow;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.MPSAllArrivedFlagEnum;
import cn.explink.support.transcwb.TransCwbDao;
import cn.explink.support.transcwb.TranscwbView;
import cn.explink.util.Tools;

/**
 *
 *
 * @author songkaojun 2016年1月6日
 */
@Component
public class MPSOptStateService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MPSOptStateService.class);

	private static final String UPDATE_MPS_STATE = "[更新一票多件状态]";

	private static final String VALIDATE_RELEASE_CONDITION = "[判断一票多件是否放行]";

	@Autowired
	private TransCwbDao transCwbDao;

	@Autowired
	private TranscwbOrderFlowDAO transcwbOrderFlowDAO;

	@Autowired
	private CwbDAO cwbDAO;

	@Autowired
	private CustomerDAO customerDAO;

	@Autowired
	private TransCwbDetailDAO transCwbDetailDAO;

	public void validateReleaseCondition(String transCwb) {
		CwbOrder cwbOrder = this.getCwbOrder(transCwb, MPSOptStateService.VALIDATE_RELEASE_CONDITION);
		if (cwbOrder == null) {
			return;
		}
	}

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
		CwbOrder cwbOrder = this.getCwbOrder(transCwb, MPSOptStateService.UPDATE_MPS_STATE);
		if (cwbOrder == null) {
			return;
		}

		this.updateMPSOptState(cwbOrder);

		// 更新运单操作状态，上一站 下一站
		TransCwbDetail transCwbDetail = this.transCwbDetailDAO.findTransCwbDetailByTransCwb(transCwb);
		transCwbDetail.setCurrentbranchid((int) currentbranchid);
		transCwbDetail.setModifiedtime(Tools.getCurrentTime(null));
		transCwbDetail.setNextbranchid((int) nextbranchid);
		transCwbDetail.setTranscwboptstate(transcwboptstate.getValue());
		this.transCwbDetailDAO.updateTransCwbDetail(transCwbDetail);
	}

	private CwbOrder getCwbOrder(String transCwb, String logPrefix) {
		if (StringUtils.isEmpty(transCwb)) {
			MPSOptStateService.LOGGER.info(logPrefix + "传入的运单号为空！");
			return null;
		}
		// 根据运单号查询订单
		String cwb = this.transCwbDao.getCwbByTransCwb(transCwb);
		if (StringUtils.isEmpty(cwb)) {
			MPSOptStateService.LOGGER.info(logPrefix + "根据传入的运单号没有查询到相应的订单号！");
			return null;
		}
		CwbOrder cwbOrder = this.cwbDAO.getCwborder(cwb);
		if (cwbOrder == null) {
			MPSOptStateService.LOGGER.info(logPrefix + "没有查询到订单数据！");
			return null;
		}

		// TODO 判断订单是否是一票多件，否则return

		// 查询供应商，判断该供应商是否开启了集单模式
		long customerid = cwbOrder.getCustomerid();
		Customer customer = this.customerDAO.getCustomerById(customerid);
		if (customer.getCustomerid() == 0L) {
			MPSOptStateService.LOGGER.info(logPrefix + "没有查询到订单的供应商信息！");
			return null;
		}
		// TODO 如果没有开启集单模式 return
		return cwbOrder;
	}

	private void updateMPSOptState(CwbOrder cwbOrder) {
		String cwb = cwbOrder.getCwb();
		int mpsallarrivedflag = cwbOrder.getMpsallarrivedflag();
		// 如果一票多件没有到齐，则更新为初始状态（导入数据）
		int latestMPSState = FlowOrderTypeEnum.DaoRuShuJu.getValue();
		if (MPSAllArrivedFlagEnum.YES.getValue() == mpsallarrivedflag) {
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
			latestMPSState = this.getLatestState(transCwbMap);
		}
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

	/**
	 * @Title: checkTransCwbIsIntercept
	 * @description 根据传入的单号（订单或者运单），判断是否被拦截，并返回提示（所处操作流程不同，提示不同，如果没有被拦截，则返回null）
	 * @author 刘武强
	 * @date  2016年1月8日下午1:53:01
	 * @param  @param transCwb
	 * @param  @return
	 * @return  String
	 * @throws
	 */
	public String checkTransCwbIsIntercept(String transCwb) {
		//TODO 判断主体待完善
		return "";
	}
}
