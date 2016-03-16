/**
 *
 */
package cn.explink.service.mps.release;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.explink.domain.Customer;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.TransCwbDetail;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.MPSAllArrivedFlagEnum;
import cn.explink.enumutil.MpsswitchTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.service.mps.AbstractMPSService;

/**
 * @author songkaojun 2016年1月8日
 */
public abstract class AbstractMPSReleaseService extends AbstractMPSService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMPSReleaseService.class);

	private MpsswitchTypeEnum mpsswitchType;

	protected static final String VALIDATE_RELEASE_CONDITION = "[判断一票多件是否放行]";

	protected static final Set<Integer> BEFORE_INTOWAREHOUSE_STATE = new HashSet<Integer>();
	protected static final Set<Integer> BEFORE_SUBSTATION_GOODS_ARRIVED_STATE = new HashSet<Integer>();
	protected static final Set<Integer> BEFORE_RETURN_TO_CUSTOMER_STATE = new HashSet<Integer>();

	static {
		AbstractMPSReleaseService.BEFORE_INTOWAREHOUSE_STATE.add(FlowOrderTypeEnum.DaoRuShuJu.getValue());
		AbstractMPSReleaseService.BEFORE_INTOWAREHOUSE_STATE.add(FlowOrderTypeEnum.TiHuo.getValue());
		AbstractMPSReleaseService.BEFORE_INTOWAREHOUSE_STATE.add(FlowOrderTypeEnum.TiHuoYouHuoWuDan.getValue());

		AbstractMPSReleaseService.BEFORE_SUBSTATION_GOODS_ARRIVED_STATE.addAll(AbstractMPSReleaseService.BEFORE_INTOWAREHOUSE_STATE);
		AbstractMPSReleaseService.BEFORE_SUBSTATION_GOODS_ARRIVED_STATE.add(FlowOrderTypeEnum.RuKu.getValue());
		AbstractMPSReleaseService.BEFORE_SUBSTATION_GOODS_ARRIVED_STATE.add(FlowOrderTypeEnum.ChuKuSaoMiao.getValue());

		AbstractMPSReleaseService.BEFORE_RETURN_TO_CUSTOMER_STATE.addAll(AbstractMPSReleaseService.BEFORE_SUBSTATION_GOODS_ARRIVED_STATE);
		AbstractMPSReleaseService.BEFORE_RETURN_TO_CUSTOMER_STATE.add(FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue());
		AbstractMPSReleaseService.BEFORE_RETURN_TO_CUSTOMER_STATE.add(FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue());
		AbstractMPSReleaseService.BEFORE_RETURN_TO_CUSTOMER_STATE.add(FlowOrderTypeEnum.FenZhanLingHuo.getValue());
		AbstractMPSReleaseService.BEFORE_RETURN_TO_CUSTOMER_STATE.add(FlowOrderTypeEnum.YiShenHe.getValue());
		AbstractMPSReleaseService.BEFORE_RETURN_TO_CUSTOMER_STATE.add(FlowOrderTypeEnum.ZhongZhuanZhanRuKu.getValue());
		AbstractMPSReleaseService.BEFORE_RETURN_TO_CUSTOMER_STATE.add(FlowOrderTypeEnum.ZhongZhuanZhanChuKu.getValue());
		AbstractMPSReleaseService.BEFORE_RETURN_TO_CUSTOMER_STATE.add(FlowOrderTypeEnum.TuiHuoChuZhan.getValue());
		AbstractMPSReleaseService.BEFORE_RETURN_TO_CUSTOMER_STATE.add(FlowOrderTypeEnum.DingDanLanJie.getValue());
	}

	protected CwbOrder getMPSCwbOrderByTransCwbConsideringMPSSwitchType(String transCwb, String logPrefix) {
		CwbOrder cwbOrder = this.getMPSCwbOrderByTransCwb(transCwb, logPrefix);
		if (cwbOrder != null) {
			if (!this.setCwbOrderMPSSwitchType(logPrefix, cwbOrder)) {
				return null;
			}
		}
		return cwbOrder;
	}

	protected CwbOrder getMPSCwbOrderByCwbConsideringMPSSwitchType(String cwb, String logPrefix) {
		CwbOrder cwbOrder = this.getMPSCwbOrderByCwb(cwb, logPrefix);
		if (cwbOrder != null) {
			if (!this.setCwbOrderMPSSwitchType(logPrefix, cwbOrder)) {
				return null;
			}
		}
		return cwbOrder;
	}

	private boolean setCwbOrderMPSSwitchType(String logPrefix, CwbOrder cwbOrder) {
		// 查询供应商，判断该供应商是否开启了集单模式
		long customerid = cwbOrder.getCustomerid();
		Customer customer = this.getCustomerDAO().getCustomerById(customerid);
		if (customer.getCustomerid() == 0L) {
			AbstractMPSReleaseService.LOGGER.error(logPrefix + "没有查询到订单的供应商信息！");
			return false;
		}
		int mpsswitch = customer.getMpsswitch();
		if (mpsswitch == MpsswitchTypeEnum.WeiKaiQiJiDan.getValue()) {
			AbstractMPSReleaseService.LOGGER.debug(logPrefix + customer.getCustomername() + "未启用集单模式！");
			return false;
		}
		this.mpsswitchType = MpsswitchTypeEnum.getByValue(mpsswitch);
		return true;
	}

	/**
	 * 校验放行条件
	 *
	 * @param transCwb
	 */
	public abstract void validateReleaseCondition(String transCwb) throws CwbException;

	protected void validateMPS(String transCwb, CwbOrder cwbOrder, CwbException exception, Set<Integer> beforeStateSet) {
		int mpsallarrivedflag = cwbOrder.getMpsallarrivedflag();
		if (MPSAllArrivedFlagEnum.NO.getValue() == mpsallarrivedflag) {
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

	public MpsswitchTypeEnum getMpsswitchType() {
		return this.mpsswitchType;
	}

}
