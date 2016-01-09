/**
 *
 */
package cn.explink.service.mps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.explink.domain.CwbOrder;
import cn.explink.domain.TransCwbDetail;
import cn.explink.enumutil.TransCwbStateEnum;
import cn.explink.exception.CwbException;

/**
 * @author songkaojun 2016年1月8日
 */
public class OrderInterceptService extends AbstractMPSService {

	private static final String ORDER_INTERCEPT = "[订单拦截]";

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderInterceptService.class);

	/**
	 *
	 * 根据传入的运单号，判断是否被拦截
	 *
	 * @param transCwb
	 * @return
	 */
	public void checkTransCwbIsIntercept(String transCwb) throws CwbException {
		CwbOrder cwbOrder = this.getCwbOrder(transCwb, OrderInterceptService.ORDER_INTERCEPT);
		if (cwbOrder == null) {
			return;
		}
		TransCwbDetail transCwbDetail = this.getTransCwbDetailDAO().findTransCwbDetailByTransCwb(transCwb);
		if (transCwbDetail == null) {
			OrderInterceptService.LOGGER.error(OrderInterceptService.ORDER_INTERCEPT + "没有查询到运单号为" + transCwb + "的运单！");
			return;
		}
		int transcwbstate = transCwbDetail.getTranscwbstate();
		String reason = transCwbDetail.getCommonphrase();
		if (transcwbstate == TransCwbStateEnum.DIUSHI.getValue()) {

		} else if (transcwbstate == TransCwbStateEnum.POSUN.getValue()) {

		} else if (transcwbstate == TransCwbStateEnum.TUIHUO.getValue()) {

		}
	}
}
