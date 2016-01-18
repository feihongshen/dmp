/**
 *
 */
package cn.explink.service.mps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.explink.domain.CwbOrder;
import cn.explink.domain.TransCwbDetail;

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
		this.getCwbDAO().updateBranchInfo(cwbOrder.getCwb(), transCwbDetail.getPreviousbranchid(), transCwbDetail.getCurrentbranchid(), transCwbDetail.getNextbranchid());
	}

}
