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
 * 小件员领货对于一票多件放行的处理
 *
 * @author songkaojun 2016年1月8日
 */
@Component("deliverTakeGoodsMPSReleaseService")
public final class DeliverTakeGoodsMPSReleaseService extends AbstractMPSReleaseService {

	@Override
	public void validateReleaseCondition(String cwbOrTransCwb) throws CwbException {
		CwbOrder cwbOrder = this.getMPSCwbOrderByTransCwbConsideringMPSSwitchType(cwbOrTransCwb, AbstractMPSReleaseService.VALIDATE_RELEASE_CONDITION);
		if (cwbOrder == null) {
			cwbOrder = this.getMPSCwbOrderByCwbConsideringMPSSwitchType(cwbOrTransCwb, AbstractMPSReleaseService.VALIDATE_RELEASE_CONDITION);
			if (cwbOrder == null) {
				return;
			}
		}
		// 不管是库房集单还是站点集单，都须要进行校验
		CwbException exception = new CwbException(cwbOrder.getCwb(), FlowOrderTypeEnum.FenZhanLingHuo.getValue(), ExceptionCwbErrorTypeEnum.DELIVERTAKEGOODS_MPS_NOT_ALL_ARRIVED);
		this.validateMPS(cwbOrTransCwb, cwbOrder, exception, AbstractMPSReleaseService.BEFORE_SUBSTATION_GOODS_ARRIVED_STATE);
	}

}
