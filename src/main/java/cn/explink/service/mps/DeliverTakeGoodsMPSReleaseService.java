/**
 *
 */
package cn.explink.service.mps;

import cn.explink.domain.CwbOrder;
import cn.explink.enumutil.MpsswitchTypeEnum;

/**
 *
 * 小件员领货对于一票多件放行的处理
 *
 * @author songkaojun 2016年1月8日
 */
public class DeliverTakeGoodsMPSReleaseService extends AbstractMPSReleaseService {

	@Override
	public void validateReleaseCondition(String transCwb) {
		CwbOrder cwbOrder = this.getCwbOrder(transCwb, AbstractMPSReleaseService.VALIDATE_RELEASE_CONDITION);
		if (cwbOrder == null) {
			return;
		}
		if (this.getMpsswitchType().getValue() == MpsswitchTypeEnum.ZhanDianJiDan.getValue()) {

		}
	}

}
