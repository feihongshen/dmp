/**
 *
 */
package cn.explink.service.mps.release;

import org.springframework.stereotype.Component;

import cn.explink.domain.CwbOrder;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.MpsswitchTypeEnum;
import cn.explink.exception.CwbException;

/**
 *
 * 出库操作对于一票多件放行的处理
 *
 * @author songkaojun 2016年1月8日
 */
@Component("outWarehouseMPSReleaseService")
public class OutWarehouseMPSReleaseService extends AbstractMPSReleaseService {

	@Override
	public void validateReleaseCondition(String transCwb) throws CwbException {
		CwbOrder cwbOrder = this.getMPSCwbOrderConsideringMPSSwitchType(transCwb, AbstractMPSReleaseService.VALIDATE_RELEASE_CONDITION);
		if (cwbOrder == null) {
			return;
		}
		CwbException exception = new CwbException(cwbOrder.getCwb(), FlowOrderTypeEnum.ChuKuSaoMiao.getValue(), ExceptionCwbErrorTypeEnum.OUTWAREHOUSE_MPS_NOT_ALL_ARRIVED);
		if (this.getMpsswitchType().getValue() == MpsswitchTypeEnum.KuFangJiDan.getValue()) {
			this.validateMPS(transCwb, cwbOrder, exception, AbstractMPSReleaseService.BEFORE_INTOWAREHOUSE_STATE);
		}
	}

}
