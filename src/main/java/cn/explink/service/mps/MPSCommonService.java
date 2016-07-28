/**
 *
 */
package cn.explink.service.mps;

import java.util.List;

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
	public void resetScannumByTranscwbForArrive(String cwb, String transCwb, long flowordertype, long branchid) {
//		CwbOrder cwbOrder = this.getMPSCwbOrderByTransCwb(transCwb, MPSCommonService.UPDATE_MPS_STATE);
//		if (cwbOrder == null) {
//			return;
//		}
//		TransCwbDetail transCwbDetail = this.getTransCwbDetailDAO().findTransCwbDetailByTransCwb(transCwb);
//		if (transCwbDetail == null) {
//			MPSCommonService.LOGGER.debug(MPSCommonService.UPDATE_MPS_STATE + "没有查询到运单号为" + transCwb + "的运单！");
//			return;
//		}
		
		/*
		 * 正常到货扫描和到错货的扫描次数都要算上
		 */
		// 一票多件使用运单号时，扫描次数需要计算
		long realscannum = this.transcwborderFlowDAO.getScanNumByTranscwbOrderFlow(transCwb, cwb, flowordertype, branchid);
		//到错货扫描次数
		long realscannumForErrorArrive = this.transcwborderFlowDAO.getScanNumByTranscwbOrderFlow(transCwb, cwb, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), branchid);
		
		LOGGER.info("resetScannumByTranscwbForArride->transCwb:{}, branchid:{}, realscannum:{}, realscannumForErrorArrive:{}", 
				transCwb, branchid, realscannum, realscannumForErrorArrive);
		this.getCwbDAO().updateScannum(cwb, realscannum + realscannumForErrorArrive);
	}

}
