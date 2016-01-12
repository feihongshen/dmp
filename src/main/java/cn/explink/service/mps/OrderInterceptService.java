/**
 *
 */
package cn.explink.service.mps;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.explink.dao.TransCwbStateControlDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.TransCwbDetail;
import cn.explink.domain.TransCwbStateControl;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.TransCwbStateEnum;
import cn.explink.exception.CwbException;

/**
 * @author songkaojun 2016年1月8日
 */

@Component
public class OrderInterceptService extends AbstractMPSService {

	private static final String ORDER_INTERCEPT = "[订单拦截]";

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderInterceptService.class);

	protected static final Set<Integer> OUT_STATE_SET = new HashSet<Integer>();

	@Autowired
	private TransCwbStateControlDAO transCwbStateControlDAO;

	static {
		OrderInterceptService.OUT_STATE_SET.add(FlowOrderTypeEnum.ChuKuSaoMiao.getValue());
		OrderInterceptService.OUT_STATE_SET.add(FlowOrderTypeEnum.FenZhanLingHuo.getValue());
		OrderInterceptService.OUT_STATE_SET.add(FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue());
	}

	/**
	 *
	 * 根据传入的运单号，判断是否被拦截
	 *
	 * @param transCwb
	 * @return
	 */
	public void checkTransCwbIsIntercept(String transCwb, FlowOrderTypeEnum transcwboptstate) throws CwbException {
		CwbOrder cwbOrder = this.getMPSCwbOrderConsideringMPSSwitchType(transCwb, OrderInterceptService.ORDER_INTERCEPT);
		if (cwbOrder == null) {
			return;
		}
		TransCwbDetail transCwbDetail = this.getTransCwbDetailDAO().findTransCwbDetailByTransCwb(transCwb);
		if (transCwbDetail == null) {
			OrderInterceptService.LOGGER.error(OrderInterceptService.ORDER_INTERCEPT + "没有查询到运单号为" + transCwb + "的运单！");
			return;
		}
		this.validateTransCwbState(transCwbDetail, transcwboptstate);

		int transcwbstate = transCwbDetail.getTranscwbstate();
		if (!OrderInterceptService.OUT_STATE_SET.contains(transcwboptstate.getValue())) {
			OrderInterceptService.LOGGER.info(OrderInterceptService.ORDER_INTERCEPT + "此操作不是【出】的环节，不需要拦截!");
			return;
		}
		String reason = transCwbDetail.getCommonphrase();
		String cwb = cwbOrder.getCwb();
		if (transcwbstate == TransCwbStateEnum.DIUSHI.getValue()) {
			throw new CwbException(cwb, transcwboptstate.getValue(), ExceptionCwbErrorTypeEnum.TRANSORDER_LOST, transCwb, reason);
		} else if (transcwbstate == TransCwbStateEnum.POSUN.getValue()) {
			throw new CwbException(cwb, transcwboptstate.getValue(), ExceptionCwbErrorTypeEnum.TRANSORDER_BROKEN, transCwb, reason);
		} else if (transcwbstate == TransCwbStateEnum.TUIHUO.getValue()) {
			List<TransCwbDetail> siblingTransCwbDetailList = this.getSiblingTransCwbDetailList(transCwb, cwb);
			for (TransCwbDetail siblingTransCwbDetail : siblingTransCwbDetailList) {
				int siblingState = siblingTransCwbDetail.getTranscwbstate();
				String siblingTransCwb = siblingTransCwbDetail.getTranscwb();
				if (siblingState == TransCwbStateEnum.DIUSHI.getValue()) {
					throw new CwbException(cwb, transcwboptstate.getValue(), ExceptionCwbErrorTypeEnum.TRANSORDER_LOST_RETURN, cwb, transCwb, siblingTransCwb);
				}
				if (siblingState == TransCwbStateEnum.POSUN.getValue()) {
					throw new CwbException(cwb, transcwboptstate.getValue(), ExceptionCwbErrorTypeEnum.TRANSORDER_BROKEN_RETURN, cwb, transCwb, siblingTransCwb);
				}
			}
		}
	}

	/**
	 *
	 * 查询运单状态流程，只有有记录的才允许操作
	 *
	 * @param transCwbDetail
	 * @param transcwboptstate
	 */
	private void validateTransCwbState(TransCwbDetail transCwbDetail, FlowOrderTypeEnum transcwboptstate) {
		TransCwbStateEnum transCwbState = TransCwbStateEnum.getByValue(transCwbDetail.getTranscwbstate());
		TransCwbStateControl transCwbStateControl = this.transCwbStateControlDAO.getStateControlByParam(transCwbDetail.getTranscwbstate(), transcwboptstate.getValue());
		if (transCwbStateControl == null) {
			throw new CwbException(transCwbDetail.getCwb(), transcwboptstate.getValue(), ExceptionCwbErrorTypeEnum.TRANSCWB_STATE_CONTROL_ERROR, transCwbState.getText(), transcwboptstate.getText());
		}
	}

}
