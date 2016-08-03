/**
 *
 */
package cn.explink.service.mps;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.dao.CwbStateControlDAO;
import cn.explink.dao.TransCwbDetailDAO;
import cn.explink.dao.TranscwbOrderFlowDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.CwbStateControl;
import cn.explink.domain.TransCwbDetail;
import cn.explink.domain.orderflow.TranscwbOrderFlow;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.MPSAllArrivedFlagEnum;
import cn.explink.enumutil.TransCwbStateEnum;
import cn.explink.exception.CwbException;

/**
 *
 *
 * @author songkaojun 2016年1月6日
 */
@Component
public final class MPSOptStateService extends AbstractMPSService {

	private static final String UPDATE_MPS_STATE = "[更新一票多件状态]";

	private static final String UPDATE_TRANSCWB_INFO = "[更新运单信息]";

	private static final String UPDATE_TRANSCWBSTATE = "[更新运单状态]";

	private static final Logger LOGGER = LoggerFactory.getLogger(MPSOptStateService.class);

	@Autowired
	private TranscwbOrderFlowDAO transcwbOrderFlowDAO;
	@Autowired
	private CwbStateControlDAO cwbStateControlDAO;

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
	public void updateMPSInfo(String transCwb, FlowOrderTypeEnum transcwboptstate, long previousbranchid, long currentbranchid, long nextbranchid) {
		CwbOrder cwbOrder = this.getMPSCwbOrderByTransCwb(transCwb, MPSOptStateService.UPDATE_MPS_STATE);
		if (cwbOrder == null) {
			return;
		}

		this.updateMPSOptState(cwbOrder);

		this.updateCwbOrderNextBranch(cwbOrder, currentbranchid, nextbranchid);

		this.updateTransCwbDetail(transCwb, transcwboptstate, previousbranchid, currentbranchid, nextbranchid);
	}

	public void updateTranscwbstate(String transCwb, TransCwbStateEnum transCwbStateEnum) {
		CwbOrder cwbOrder = this.getMPSCwbOrderByTransCwb(transCwb, MPSOptStateService.UPDATE_TRANSCWBSTATE);
		if (cwbOrder == null) {
			return;
		}
		TransCwbDetail transCwbDetail = this.getTransCwbDetailDAO().findTransCwbDetailByTransCwb(transCwb);
		if (transCwbDetail == null) {
			MPSOptStateService.LOGGER.debug(MPSOptStateService.UPDATE_TRANSCWBSTATE + "没有查询到运单号" + transCwb + "对应的运单信息！");
			return;
		}
		transCwbDetail.setTranscwbstate(transCwbStateEnum.getValue());
		this.getTransCwbDetailDAO().updateTransCwbDetail(transCwbDetail);
	}

	/**
	 * 更新运单的状态，上一站，当前站，下一站
	 *
	 * @param transCwb
	 * @param transcwboptstate
	 * @param currentbranchid
	 * @param nextbranchid
	 */
	public void updateTransCwbDetailInfo(String transCwb, FlowOrderTypeEnum transcwboptstate, long previousbranchid, long currentbranchid, long nextbranchid) {
		CwbOrder cwbOrder = this.getMPSCwbOrderByTransCwb(transCwb, MPSOptStateService.UPDATE_TRANSCWB_INFO);
		if (cwbOrder == null) {
			return;
		}
		this.updateTransCwbDetail(transCwb, transcwboptstate, previousbranchid, currentbranchid, nextbranchid);
	}

	private void updateTransCwbDetail(String transCwb, FlowOrderTypeEnum transcwboptstate, long previousbranchid, long currentbranchid, long nextbranchid) {
		// 更新运单操作状态，上一站 下一站
		TransCwbDetail transCwbDetail = this.getTransCwbDetailDAO().findTransCwbDetailByTransCwb(transCwb);
		if (transCwbDetail == null) {
			MPSOptStateService.LOGGER.error(MPSOptStateService.UPDATE_MPS_STATE + "没有查询到运单号" + transCwb + "对应的运单信息！");
			return;
		}
		if (previousbranchid >= 0) {
			transCwbDetail.setPreviousbranchid(previousbranchid);
		}
		if (currentbranchid >= 0) {
			transCwbDetail.setCurrentbranchid(currentbranchid);
		}
		if (nextbranchid >= 0) {
			transCwbDetail.setNextbranchid(nextbranchid);
		}
		transCwbDetail.setTranscwboptstate(transcwboptstate.getValue());
		// 对于退供货商特殊处理
		if (transcwboptstate.getValue() == FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()) {
			transCwbDetail.setTranscwbstate(TransCwbStateEnum.TUIGONGYINGSHANG.getValue());
		}
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
		int shortestListIndex = 0;
		int index = 0;
		for (String key : keySet) {
			List<Integer> transCwbOptStateList = transCwbMap.get(key);
			transCwbOptStateListList.add(transCwbOptStateList);
			if (transCwbOptStateList.size() < shortestListLength) {
				shortestListLength = transCwbOptStateList.size();
				shortestListIndex = index;
			}
			index++;
		}
		// 当前状态
		Integer currentOptState = transCwbOptStateListList.get(shortestListIndex).get(0);
		for (int i = 0; i < shortestListLength; i++) {
			currentOptState = transCwbOptStateListList.get(shortestListIndex).get(i);
			int count = 0;
			for (int j = 0; j < transCwbOptStateListList.size(); j++) {
				if (j == shortestListIndex) {
					continue;
				}
				List<Integer> transCwbOptStateList = transCwbOptStateListList.get(j);
				for (Integer transCwbOptState : transCwbOptStateList) {
					if (currentOptState == transCwbOptState) {
						count++;
						break;
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
	
	/**
	 * 更新运单重量
	 * @param cwb
	 * @param transCwb
	 * @param carrealweight
	 */
	public void updateTransCwbDetailWeight(String cwb , String transCwb , BigDecimal carrealweight){
		this.getTransCwbDetailDAO().updateTransCwbDetailWeight(cwb, transCwb, carrealweight);
	}
	
	/**
	 * 根据订单号，获取所有的运单
	 *
	 * @return
	 */
	public List<TransCwbDetail> queryTransCwbDetail(String cwb){
		return this.getTransCwbDetailDAO().queryTransCwbDetail(cwb) ;
	}
	
	/**
	 * 通过运单的flowordertype校验订单的操作流程
	 * @param cwb 订单号
	 * @param scancwb 扫描的单号
	 * @param nextState 下一步操作
	 * @throws CwbException 校验不通过则会抛出异常
	 * @author neo01.huang，2016-7-27
	 */
	public void validateStateTransferForTranscwb(String cwb, String scancwb, FlowOrderTypeEnum nextState) {
		final String logPrefix = "通过运单的flowordertype校验订单的操作流程->";
		
		cwb = StringUtils.trimToEmpty(cwb);
		scancwb = StringUtils.trimToEmpty(scancwb);
		LOGGER.info("{}cwb:{}, scancwb:{}, nextState:{}", logPrefix, cwb, scancwb, nextState.getValue());
				
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("cwb", cwb);
		paramMap.put("scancwb", scancwb);
		paramMap.put("isnow", 1);
		//当前操作记录List
		List<TranscwbOrderFlow> currentOptList = transcwbOrderFlowDAO.queryTranscwbOrderFlow(paramMap, "t.credate DESC");
		if (currentOptList == null || currentOptList.size() == 0) {
			LOGGER.info("{}cwb:{}, scancwb:{},当前操作记录List为空", logPrefix, cwb, scancwb);
			return;
		}
		
		TranscwbOrderFlow transcwbOrderFlow = currentOptList.get(0);
		if (transcwbOrderFlow == null) {
			LOGGER.info("{}cwb:{}, scancwb:{},运单轨迹为空", logPrefix, cwb, scancwb);
			return;
		}
		
		int fromStateValue = transcwbOrderFlow.getFlowordertype();
		LOGGER.info("{}cwb:{}, scancwb:{}, fromStateValue:{}", logPrefix, cwb, scancwb, fromStateValue);
		
		FlowOrderTypeEnum fromState = FlowOrderTypeEnum.getByValue(fromStateValue);
		if (fromState != null) {
			CwbStateControl cwbStateControl = cwbStateControlDAO.getCwbStateControlByParam(fromState.getValue(), nextState.getValue());
			if (cwbStateControl == null) {
				LOGGER.info("{}cwb:{}, scancwb:{}, cwbStateControl is null", logPrefix, cwb, scancwb);
				throw new CwbException(cwb, nextState.getValue(), ExceptionCwbErrorTypeEnum.STATE_CONTROL_ERROR, fromState.getText(), nextState.getText());
			}
		}
		LOGGER.info("{}cwb:{}, scancwb:{}, 通过!", logPrefix, cwb, scancwb);
	}
}
