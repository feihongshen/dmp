package cn.explink.pos.unionpay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.ExptCodeJointDAO;
import cn.explink.b2c.tools.ExptReasonDAO;
import cn.explink.dao.BranchDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.ReasonDao;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.exception.CwbException;
import cn.explink.pos.tools.PosEnum;
import cn.explink.pos.tools.PosPayHandler;
import cn.explink.service.CwbOrderService;
import cn.explink.util.DateTimeUtil;

@Service
public class UnionPayService_Delivery extends UnionPayService {

	private Logger logger = LoggerFactory.getLogger(UnionPayService_Delivery.class);
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	ExptReasonDAO exptReasonDAO;
	@Autowired
	ExptCodeJointDAO exptcodeJointDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	BranchDAO branchDAO;
	@Autowired
	ReasonDao reasonDao;

	/**
	 * 运单处理结果
	 * 
	 * @return
	 */
	public String deliveryStateFeedback(JSONObject jsondata) {

		long deliveryid = 0;
		String cwb = jsondata.getString("BarCode");
		UnionPay unionpay = getUnionPaySettingMethod(PosEnum.UnionPay.getKey());

		if (unionpay.getIsotherdeliveroper() == 1) {
			deliveryid = getUserIdByUserName(jsondata.getString("LoginName"));
		}
		CwbOrder cwbOrder = cwbDAO.getCwbDetailByCwbAndDeliverId(deliveryid, cwb);
		if (cwbOrder == null) {
			return super.RespPublicMsg("01", "没有检索到数据,当前订单=" + cwb + ",username=" + jsondata.getString("LoginName"));
		}

		return ExcuteDeliverStatePodHandler(jsondata, deliveryid, cwb, cwbOrder);
	}

	private String ExcuteDeliverStatePodHandler(JSONObject jsondata, long deliveryid, String cwb, CwbOrder cwbOrder) {
		try {
			int state = jsondata.getInt("State");
			long deliverystate = getDeliveryStateByTradeState(jsondata.getInt("State"), cwbOrder.getCwbordertypeid());
			User user = userDAO.getUserByUsername(jsondata.getString("LoginName"));
			deliveryid = deliveryid == 0 ? user.getUserid() : deliveryid;

			BigDecimal pos = BigDecimal.ZERO;
			BigDecimal check = BigDecimal.ZERO;
			BigDecimal cash = BigDecimal.ZERO;
			BigDecimal paybackedfee = BigDecimal.ZERO;
			long backedreasonid = 0;
			long leavedreasonid = 0;
			long firstlevelreasonid=0;

			String sign_man = "";
			int SignFlag = jsondata.get("SignFlag") != null ? jsondata.getInt("SignFlag") : 0;
			if (SignFlag == 1) {
				sign_man = "本人签收";
			} else if (SignFlag == 2) {
				sign_man = "他人代收-" + jsondata.getString("SignName");
			}

			double totalAmount = jsondata.get("TotalPayment") != null ? jsondata.getDouble("TotalPayment") : 0;
			DeliveryState dely = deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);

			if (state == UnionPayTradetypeEnum.PosSuccess.getType() || state == UnionPayTradetypeEnum.partOfPosSuccess.getType() || state == UnionPayTradetypeEnum.CashSuccess.getType()
					|| state == UnionPayTradetypeEnum.partOfCashSuccess.getType() || state == UnionPayTradetypeEnum.chequeSuccess.getType()) {
				if (dely != null && dely.getBusinessfee().compareTo(BigDecimal.ZERO) > 0 && totalAmount == 0 && dely.getIsout() == 0) {
					return super.RespPublicMsg("01", "支付金额有误,当前订单=" + cwb + ",username=" + jsondata.getString("LoginName"));
				}
			}

			if (totalAmount == 0 && dely != null && dely.getBusinessfee().compareTo(BigDecimal.ZERO) > 0 && dely.getIsout() == 1 && deliverystate != DeliveryStateEnum.FenZhanZhiLiu.getValue()
					&& deliverystate != DeliveryStateEnum.ShangMenJuTui.getValue()) {
				pos = BigDecimal.ZERO;
				cash = BigDecimal.ZERO;
				paybackedfee = dely.getBusinessfee();
			}

			if (state == UnionPayTradetypeEnum.PosSuccess.getType() || state == UnionPayTradetypeEnum.partOfPosSuccess.getType()) {
				pos = BigDecimal.valueOf(totalAmount);
			} else if (state == UnionPayTradetypeEnum.CashSuccess.getType() || state == UnionPayTradetypeEnum.partOfCashSuccess.getType()) {
				cash = BigDecimal.valueOf(totalAmount);
			} else if (state == UnionPayTradetypeEnum.chequeSuccess.getType()) {
				check = BigDecimal.valueOf(totalAmount);
			} else if (state == UnionPayTradetypeEnum.BackOutSuccess.getType()) {
				return deliveryStatePodCancel(cwbOrder, user, deliveryid, jsondata);
			} else if (state == UnionPayTradetypeEnum.JuShou.getType()) {
				String expt_code = jsondata.get("UntreadReasonCode") != null ? jsondata.getString("UntreadReasonCode") : "";
				if (exptcodeJointDAO.getExpMatchListByPosCode(expt_code, PosEnum.UnionPay.getKey()) != null) {
					backedreasonid = (exptcodeJointDAO.getExpMatchListByPosCode(expt_code, PosEnum.UnionPay.getKey())).getReasonid();
				}
				sign_man = "";
				SignFlag = 0;

			} else if (state == UnionPayTradetypeEnum.YiChang.getType()) {
				String expt_code = jsondata.get("UntreadReasonCode") != null ? jsondata.getString("UntreadReasonCode") : "";
				if (exptcodeJointDAO.getExpMatchListByPosCode(expt_code, PosEnum.UnionPay.getKey()) != null) {
					leavedreasonid = (exptcodeJointDAO.getExpMatchListByPosCode(expt_code, PosEnum.UnionPay.getKey())).getReasonid();
					firstlevelreasonid=this.reasonDao.getReasonByReasonid(leavedreasonid).getParentid();
				}
				sign_man = "";
				SignFlag = 0;
			}

			String deliverremark = "";
			if (pos.compareTo(BigDecimal.ZERO) > 0) {
				deliverremark = "刷卡支付";
			} else if (check.compareTo(BigDecimal.ZERO) > 0) {
				deliverremark = "支票支付";
			} else if (cash.compareTo(BigDecimal.ZERO) > 0) {
				deliverremark = "现金支付";
			}

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("deliverid", deliveryid);
			parameters.put("podresultid", deliverystate);
			parameters.put("backreasonid", backedreasonid);
			parameters.put("leavedreasonid", leavedreasonid);
			parameters.put("firstlevelreasonid", firstlevelreasonid);
			parameters.put("receivedfeecash", cash);
			parameters.put("receivedfeepos", pos);
			parameters.put("receivedfeecheque", check);
			parameters.put("receivedfeeother", BigDecimal.ZERO);
			parameters.put("paybackedfee", paybackedfee);
			parameters.put("podremarkid", (long) 0);
			parameters.put("posremark", pos.compareTo(BigDecimal.ZERO) > 0 ? "POS刷卡" + "POS反馈" : "POS反馈");
			parameters.put("checkremark", check.compareTo(BigDecimal.ZERO) > 0 ? "支票支付" : "");
			parameters.put("deliverstateremark", "POS反馈 " + deliverremark);
			parameters.put("owgid", 0);
			parameters.put("sessionbranchid", user.getBranchid());
			parameters.put("sessionuserid", deliveryid);
			parameters.put("sign_typeid", SignFlag > 0 ? 1 : 0);
			parameters.put("sign_man", sign_man);
			parameters.put("sign_time", DateTimeUtil.getNowTime());
			parameters.put("nosysyemflag", "1");//
			UnionPay unionpay = super.getUnionPaySettingMethod(PosEnum.UnionPay.getKey());

			try {
				if (unionpay.getIsotherdeliveroper() == 0) {
					if (dely != null && dely.getDeliveryid() != deliveryid) {
						// 重新修改下状态
						cwbOrderService.receiveGoodsHandle(user, user.getBranchid(), user, cwb, cwb, true);
					}

				}
				deliveryStateDAO.updateOperatorIdByCwb(deliveryid, cwb);
			} catch (Exception e) {
				logger.error("领货异常,cwb=" + cwb);
			}

			try {
				cwbOrderService.deliverStatePod(user, cwb, cwb, parameters);
			} catch (CwbException e1) {
				logger.error("处理UnionPay[运单处理结果]接口异常", e1);

				if (e1.getMessage().contains("状态为已审核的订单不允许进行反馈操作")
						&& dely != null
						&& (dely.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue() || dely.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue() || dely
								.getDeliverystate() == DeliveryStateEnum.ShangMenHuanChengGong.getValue())) {
					return super.RespPublicMsg("00", "");
				}
				return super.RespPublicMsg("01", e1.getMessage());

			}

			dealwith_posrecordMethed(jsondata, deliveryid, cwb, deliverystate, sign_man, totalAmount);

			return super.RespPublicMsg("00", "");

		} catch (Exception e) {
			logger.error("处理UnionPay[运单处理结果]接口异常", e);

			return super.RespPublicMsg("01", "处理异常" + e.getMessage());
		}

	}

	private void dealwith_posrecordMethed(JSONObject jsondata, long deliveryid, String cwb, long deliverystate, String sign_man, double totalAmount) {
		String UntreadReasonCode = jsondata.get("UntreadReasonCode") != null ? jsondata.getString("UntreadReasonCode") : "";
		String podremark = "unionpay请求[运单处理结果]接口，运单号=" + cwb + ",终端号=" + jsondata.getString("TerminalNo") + ",签收人=" + sign_man + ","
				+ (!UntreadReasonCode.isEmpty() ? "异常原因=" + UntreadReasonCode : "");
		if (deliverystate != DeliveryStateEnum.JuShou.getValue() && deliverystate != DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			String Sign_Remark = PosPayHandler.getSignPayAmountType(BigDecimal.valueOf(totalAmount), ""); // 签收类型
			posPayDAO.save_PosTradeDetailRecord(cwb, podremark, totalAmount, deliveryid, 0, "", sign_man, 1, Sign_Remark, 1, 1, "", PosEnum.UnionPay.getMethod(), 0, "");
		}

		logger.info(podremark + ",卡号=" + jsondata.getString("CardNo") + ",终端流水号=" + jsondata.get("TerminalDealID") + ",银联流水号=" + jsondata.get("UnionpayDealID") + ",交易金额="
				+ jsondata.get("TotalPayment"));
	}

	/**
	 * 撤销的方法
	 * 
	 * @param cwbOrder
	 * @return
	 */
	private String deliveryStatePodCancel(CwbOrder cwbOrder, User user, long deliveryid, JSONObject jsondata) {

		cwbOrderService.deliverStatePodCancel(cwbOrder.getCwb(), user.getBranchid(), deliveryid, "运单撤销", 0);
		String trackinfo = "处理UnionPay[运单处理结果][运单撤销]，订单号=" + cwbOrder.getCwb() + ",卡号=" + jsondata.getString("CardNo") + ",终端流水号=" + jsondata.get("TerminalDealID") + ",银联流水号="
				+ jsondata.get("UnionpayDealID") + ",交易金额=" + jsondata.get("TotalPayment");

		posPayDAO.save_PosTradeDetailRecord(cwbOrder.getCwb(), "运单撤销", 0, deliveryid, 1, trackinfo, "", 0, "", 1, 4, "single", PosEnum.UnionPay.getMethod(), 0, "");

		logger.info(trackinfo);
		return super.RespPublicMsg("00", "");
	}

	private int getDeliveryStateByTradeState(int tradeState, int cwbordertypeid) {
		int deliverystate = 0;
		if (tradeState == UnionPayTradetypeEnum.PosSuccess.getType() || tradeState == UnionPayTradetypeEnum.CashSuccess.getType() || tradeState == UnionPayTradetypeEnum.chequeSuccess.getType()) {
			deliverystate = DeliveryStateEnum.PeiSongChengGong.getValue();
			if (cwbordertypeid == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {
				deliverystate = DeliveryStateEnum.ShangMenHuanChengGong.getValue();
			} else if (cwbordertypeid == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
				deliverystate = DeliveryStateEnum.ShangMenTuiChengGong.getValue();
			}
		}
		if (tradeState == UnionPayTradetypeEnum.partOfPosSuccess.getType() || tradeState == UnionPayTradetypeEnum.partOfCashSuccess.getType()) {
			deliverystate = DeliveryStateEnum.BuFenTuiHuo.getValue();
		}

		if (tradeState == UnionPayTradetypeEnum.JuShou.getType()) {

			deliverystate = DeliveryStateEnum.JuShou.getValue();
			if (cwbordertypeid == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
				deliverystate = DeliveryStateEnum.ShangMenJuTui.getValue();
			}
		}
		if (tradeState == UnionPayTradetypeEnum.YiChang.getType()) {
			deliverystate = DeliveryStateEnum.FenZhanZhiLiu.getValue();
		}
		return deliverystate;
	}

}
