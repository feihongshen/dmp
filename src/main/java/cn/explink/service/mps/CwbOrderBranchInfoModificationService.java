/**
 *
 */
package cn.explink.service.mps;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.explink.domain.CwbOrder;
import cn.explink.domain.TransCwbDetail;
import cn.explink.enumutil.CwbStateEnum;
import cn.explink.enumutil.TransCwbStateEnum;

/**
 * @author songkaojun 2016年1月18日
 */
@Component
public class CwbOrderBranchInfoModificationService extends AbstractMPSService {

	private static final String UPDATE_BRANCH = "[更新站点信息]";

	private static final Logger LOGGER = LoggerFactory.getLogger(CwbOrderBranchInfoModificationService.class);

	public void modifyBranchInfo(String transCwb) {
		CwbOrder cwbOrder = this.getMPSCwbOrder(transCwb, CwbOrderBranchInfoModificationService.UPDATE_BRANCH);
		if (cwbOrder == null) {
			return;
		}
		TransCwbDetail transCwbDetail = this.getTransCwbDetailDAO().findTransCwbDetailByTransCwb(transCwb);
		if (transCwbDetail == null) {
			CwbOrderBranchInfoModificationService.LOGGER.error(CwbOrderBranchInfoModificationService.UPDATE_BRANCH + "没有查询到运单号为" + transCwb + "的运单！");
			return;
		}

		int cwbstate = this.transferTransCwbStateToCwbState(transCwb, cwbOrder, transCwbDetail);

		this.getCwbDAO().updateBranchAndCwbstateAndFlowOrderTypeInfo(cwbOrder.getCwb(), transCwbDetail.getPreviousbranchid(), transCwbDetail.getCurrentbranchid(), transCwbDetail.getNextbranchid(),
				cwbstate, transCwbDetail.getTranscwboptstate());
		// 如果一票多件所有件都扫描了，则scannum置为0
		if (cwbOrder.getSendcarnum() == cwbOrder.getScannum()) {
			this.getCwbDAO().updateScannum(cwbOrder.getCwb(), 0);
		}
	}

	/**
	 * 将运单状态转换为订单状态
	 *
	 * @param transCwb
	 * @param cwbOrder
	 * @param transCwbDetail
	 * @return
	 */
	private int transferTransCwbStateToCwbState(String transCwb, CwbOrder cwbOrder, TransCwbDetail transCwbDetail) {
		int cwbstate = CwbStateEnum.PeiShong.getValue();
		int transcwbstate = transCwbDetail.getTranscwbstate();
		if (TransCwbStateEnum.DIUSHI.getValue() == transcwbstate) {
			if (this.isAllLostOrBroken(transCwb, cwbOrder.getCwb(), TransCwbStateEnum.DIUSHI)) {
				cwbstate = CwbStateEnum.DiuShi.getValue();
			} else {
				cwbstate = CwbStateEnum.BUFENDIUSHI.getValue();
			}
		} else if (TransCwbStateEnum.POSUN.getValue() == transcwbstate) {
			if (this.isAllLostOrBroken(transCwb, cwbOrder.getCwb(), TransCwbStateEnum.POSUN)) {
				cwbstate = CwbStateEnum.WANQUANPOSUN.getValue();
			} else {
				cwbstate = CwbStateEnum.BUFENPOSUN.getValue();
			}
		} else if (TransCwbStateEnum.PEISONG.getValue() == transcwbstate) {
			cwbstate = CwbStateEnum.PeiShong.getValue();
		} else if (TransCwbStateEnum.TUIHUO.getValue() == transcwbstate) {
			if (this.existLostOrBroken(transCwb, cwbOrder.getCwb(), TransCwbStateEnum.DIUSHI)) {
				cwbstate = CwbStateEnum.BUFENDIUSHI.getValue();
			} else if (this.existLostOrBroken(transCwb, cwbOrder.getCwb(), TransCwbStateEnum.POSUN)) {
				cwbstate = CwbStateEnum.BUFENPOSUN.getValue();
			} else {
				cwbstate = CwbStateEnum.TuiHuo.getValue();
			}
		} else if (TransCwbStateEnum.TUIGONGYINGSHANG.getValue() == transcwbstate) {
			cwbstate = CwbStateEnum.TuiGongYingShang.getValue();
		} else if (TransCwbStateEnum.ZHONGZHUAN.getValue() == transcwbstate) {
			cwbstate = CwbStateEnum.ZhongZhuan.getValue();
		}
		return cwbstate;
	}

	/**
	 * 判断transCwb的兄弟运单是否全部丢失或破损
	 *
	 * @param transCwb
	 * @param cwb
	 * @param transCwbStateEnum
	 * @return
	 */
	private boolean isAllLostOrBroken(String transCwb, String cwb, TransCwbStateEnum transCwbStateEnum) {
		boolean isAllLostOrBroken = true;
		List<TransCwbDetail> siblingTransCwbDetailList = this.getSiblingTransCwbDetailList(transCwb, cwb);
		for (TransCwbDetail siblingTransCwbDetail : siblingTransCwbDetailList) {
			int transcwbstate = siblingTransCwbDetail.getTranscwbstate();
			if (transCwbStateEnum.getValue() != transcwbstate) {
				isAllLostOrBroken = false;
				break;
			}
		}
		return isAllLostOrBroken;
	}

	private boolean existLostOrBroken(String transCwb, String cwb, TransCwbStateEnum transCwbStateEnum) {
		List<TransCwbDetail> siblingTransCwbDetailList = this.getSiblingTransCwbDetailList(transCwb, cwb);
		for (TransCwbDetail siblingTransCwbDetail : siblingTransCwbDetailList) {
			if (siblingTransCwbDetail.getTranscwbstate() == transCwbStateEnum.getValue()) {
				return true;
			}
		}
		return false;
	}

}
