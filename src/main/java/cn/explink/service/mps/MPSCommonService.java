/**
 *
 */
package cn.explink.service.mps;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.dao.TranscwbOrderFlowDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.TransCwbDetail;
import cn.explink.enumutil.FlowOrderTypeEnum;

/**
 * @author songkaojun 2016年1月19日
 */
@Component
public final class MPSCommonService extends AbstractMPSService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MPSCommonService.class);

	private static final String UPDATE_MPS_STATE = "[MPS通用服务]";

	@Autowired
	private TranscwbOrderFlowDAO transcwborderFlowDAO;

	/**
	 *
	 *
	 * @param cwbOrTransCwb
	 *            订单号或者运单号
	 * @return
	 */
	public boolean isNewMPSOrder(String cwbOrTransCwb) {
		CwbOrder cwbOrder = this.getMPSCwbOrderByTransCwb(cwbOrTransCwb, MPSCommonService.UPDATE_MPS_STATE);
		if (cwbOrder == null) {
			cwbOrder = this.getMPSCwbOrderByCwb(cwbOrTransCwb, MPSCommonService.UPDATE_MPS_STATE);
			if (cwbOrder == null) {
				return false;
			}
		}
		return true;
	}

	public List<String> getTransCwbListByCwb(String cwb) {
		return this.getTransCwbDetailDAO().getTransCwbListByCwb(cwb);
	}

	public void resetScannumByTranscwb(String transCwb, long flowordertype, long branchid, long nextbranchid) {
		CwbOrder cwbOrder = this.getMPSCwbOrderByTransCwb(transCwb, MPSCommonService.UPDATE_MPS_STATE);
		if (cwbOrder == null) {
			return;
		}
		TransCwbDetail transCwbDetail = this.getTransCwbDetailDAO().findTransCwbDetailByTransCwb(transCwb);
		if (transCwbDetail == null) {
			MPSCommonService.LOGGER.debug(MPSCommonService.UPDATE_MPS_STATE + "没有查询到运单号为" + transCwb + "的运单！");
			return;
		}
		// 一票多件使用运单号时，扫描次数需要计算
		long realscannum = this.transcwborderFlowDAO.getScanNumByTranscwbOrderFlow(transCwbDetail.getTranscwb(), transCwbDetail.getCwb(), flowordertype, branchid);
		this.getCwbDAO().updateScannum(cwbOrder.getCwb(), realscannum);
	}
	
	/**
	 * 重设扫描次数（分站到货扫描）
	 * @param transCwb 运单号
	 * @param flowordertype flowordertype
	 * @param branchid 站点id
	 * @author neo01.huang
	 * @createDate 2016-4-21
	 */
	public void resetScannumByTranscwbForArrive(String transCwb, long flowordertype, long branchid) {
		CwbOrder cwbOrder = this.getMPSCwbOrderByTransCwb(transCwb, MPSCommonService.UPDATE_MPS_STATE);
		if (cwbOrder == null) {
			return;
		}
		TransCwbDetail transCwbDetail = this.getTransCwbDetailDAO().findTransCwbDetailByTransCwb(transCwb);
		if (transCwbDetail == null) {
			MPSCommonService.LOGGER.debug(MPSCommonService.UPDATE_MPS_STATE + "没有查询到运单号为" + transCwb + "的运单！");
			return;
		}
		
		/*
		 * 正常到货扫描和到错货的扫描次数都要算上
		 */
		// 一票多件使用运单号时，扫描次数需要计算
		long realscannum = this.transcwborderFlowDAO.getScanNumByTranscwbOrderFlow(transCwbDetail.getTranscwb(), transCwbDetail.getCwb(), flowordertype, branchid, 1);
		//到错货扫描次数
		long realscannumForErrorArrive = this.transcwborderFlowDAO.getScanNumByTranscwbOrderFlow(transCwbDetail.getTranscwb(), transCwbDetail.getCwb(), FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), branchid, 1);
		
		LOGGER.info("resetScannumByTranscwbForArride->transCwb:{}, branchid:{}, realscannum:{}, realscannumForErrorArrive:{}", 
				transCwb, branchid, realscannum, realscannumForErrorArrive);
		this.getCwbDAO().updateScannum(cwbOrder.getCwb(), realscannum + realscannumForErrorArrive);
	}
	
	/**
	 * 是否为真正一票多件
	 * @param cwbOrder 订单
	 * @param isypdjusetranscwb 一票多件是否使用运单号
	 * @return true为真正一票多件
	 * @author neo01.huang，2016-7-27
	 */
	public boolean isRealOneVoteMultiPiece(CwbOrder cwbOrder, long isypdjusetranscwb) {
		final String logPrefix = "是否为真正一票多件->";
		if (cwbOrder == null) {
			LOGGER.info("{}cwbOrder为空", logPrefix);
			return false;
		}
		
		String cwb = cwbOrder.getCwb(); //订单号
		long sendcarnum = cwbOrder.getSendcarnum(); //发货数量
		String transcwb = StringUtils.trimToEmpty(cwbOrder.getTranscwb()); //运单号
		long cwbstate = cwbOrder.getCwbstate(); //订单状态
		long flowordertype = cwbOrder.getFlowordertype(); //订单操作类型
		LOGGER.info("{}cwb:{}, sendcarnum:{}, transcwb:{}, isypdjusetranscwb:{}, cwbstate:{}, flowordertype:{}", 
				logPrefix, cwb, sendcarnum, transcwb, isypdjusetranscwb, cwbstate, flowordertype);
		
		if (sendcarnum > 1 && StringUtils.isNotEmpty(transcwb) && isypdjusetranscwb == 1) {
			LOGGER.info("{}cwb:{},是真正一票多件", logPrefix, cwb);
			return true;
		} else {
			LOGGER.info("{}cwb:{},不是真正一票多件", logPrefix, cwb);
			return false;
		}
		
	}

}
