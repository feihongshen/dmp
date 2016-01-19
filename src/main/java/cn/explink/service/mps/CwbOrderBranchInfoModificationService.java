/**
 *
 */
package cn.explink.service.mps;

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
		int cwbstate = CwbStateEnum.PeiShong.getValue();
		int transcwbstate = transCwbDetail.getTranscwbstate();
		if (TransCwbStateEnum.DIUSHI.getValue() == transcwbstate) {
			cwbstate = CwbStateEnum.DiuShi.getValue();
		} else if (TransCwbStateEnum.POSUN.getValue() == transcwbstate) {
			cwbstate = CwbStateEnum.BUFENPOSUN.getValue();
		} else if (TransCwbStateEnum.PEISONG.getValue() == transcwbstate) {
			cwbstate = CwbStateEnum.PeiShong.getValue();
		} else if (TransCwbStateEnum.TUIHUO.getValue() == transcwbstate) {
			cwbstate = CwbStateEnum.TuiHuo.getValue();
		} else if (TransCwbStateEnum.TUIGONGYINGSHANG.getValue() == transcwbstate) {
			cwbstate = CwbStateEnum.TuiGongYingShang.getValue();
		} else if (TransCwbStateEnum.ZHONGZHUAN.getValue() == transcwbstate) {
			cwbstate = CwbStateEnum.ZhongZhuan.getValue();
		}

		this.getCwbDAO().updateBranchAndCwbstateAndFlowOrderTypeInfo(cwbOrder.getCwb(), transCwbDetail.getPreviousbranchid(), transCwbDetail.getCurrentbranchid(), transCwbDetail.getNextbranchid(),
				cwbstate, transCwbDetail.getTranscwboptstate());
	}

}
