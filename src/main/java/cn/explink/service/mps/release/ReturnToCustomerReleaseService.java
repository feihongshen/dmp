/**
 *
 */
package cn.explink.service.mps.release;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import cn.explink.domain.CwbOrder;
import cn.explink.domain.TransCwbDetail;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.MPSAllArrivedFlagEnum;
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
	
	@Override
	protected void validateMPS(String transCwb, CwbOrder cwbOrder, CwbException exception, Set<Integer> beforeStateSet) {
		int mpsallarrivedflag = cwbOrder.getMpsallarrivedflag();
		if (MPSAllArrivedFlagEnum.NO.getValue() == mpsallarrivedflag) {
			TransCwbDetail transCwbDetail = this.getTransCwbDetailDAO().findTransCwbDetailByTransCwb(transCwb);
			if(transCwbDetail!=null&&transCwbDetail.getTranscwboptstate()==FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()){
				return;
			}
			throw exception;
		} else {
			List<TransCwbDetail> siblingTransCwbDetailList = this.getSiblingTransCwbDetailList(transCwb, cwbOrder.getCwb());
			for (TransCwbDetail siblingTransCwbDetail : siblingTransCwbDetailList) {
				if (beforeStateSet.contains(siblingTransCwbDetail.getTranscwboptstate())) {
					throw exception;
				}
			}
		}
	}

}
