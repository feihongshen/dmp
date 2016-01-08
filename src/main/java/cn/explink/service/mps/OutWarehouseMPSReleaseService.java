/**
 *
 */
package cn.explink.service.mps;

import cn.explink.domain.CwbOrder;
import cn.explink.enumutil.MpsswitchTypeEnum;

/**
 *
 * 出库操作对于一票多件放行的处理
 *
 * @author songkaojun 2016年1月8日
 */
public class OutWarehouseMPSReleaseService extends AbstractMPSReleaseService {

	@Override
	public void validateReleaseCondition(String transCwb) {
		CwbOrder cwbOrder = this.getCwbOrder(transCwb, AbstractMPSReleaseService.VALIDATE_RELEASE_CONDITION);
		if (cwbOrder == null) {
			return;
		}
		if (this.getMpsswitchType().getValue() == MpsswitchTypeEnum.KuFangJiDan.getValue()) {

		} else if (this.getMpsswitchType().getValue() == MpsswitchTypeEnum.ZhanDianJiDan.getValue()) {

		}
	}
}
