/**
 *
 */
package cn.explink.service.mps.release;

import org.springframework.stereotype.Component;

import cn.explink.domain.CwbOrder;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.exception.CwbException;

/**
 *
 * 退供应商对于一票多件放行的处理
 *
 * @author songkaojun 2016年1月8日
 */
@Component("returnToCustomerReleaseService")
public final class ReturnToCustomerReleaseService extends AbstractMPSReleaseService {

	@Override
	public void validateReleaseCondition(String transCwb) throws CwbException {
		CwbOrder cwbOrder = this.getMPSCwbOrderByTransCwb(transCwb, AbstractMPSReleaseService.VALIDATE_RELEASE_CONDITION);
		if (cwbOrder == null) {
			return;
		}
		CwbException exception = new CwbException(cwbOrder.getCwb(), FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue(), ExceptionCwbErrorTypeEnum.OUTWAREHOUSE_MPS_NOT_ALL_ARRIVED);
		this.validateMPS(transCwb, cwbOrder, exception, AbstractMPSReleaseService.BEFORE_RETURN_TO_CUSTOMER_STATE);
	}

}
