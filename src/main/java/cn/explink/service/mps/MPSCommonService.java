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

}
