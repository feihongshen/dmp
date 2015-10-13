package cn.explink.b2c.homegou;

import java.io.IOException;
import java.math.BigDecimal;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.jms.dto.CwbOrderWithDeliveryState;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BuildHomegouB2cData {
	private Logger logger = LoggerFactory.getLogger(BuildHomegouB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	HomegouService homegouService;
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	B2cTools b2cTools;
	@Autowired
	B2CDataDAO b2CDataDAO;

	public Homegou getHomeGou() {
		return homegouService.getHomegouSettingMethod(B2cEnum.HomeGou.getKey());
	}

	public String BuildHomegouMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, long delivery_state, CwbOrderWithDeliveryState cwbOrderWothDeliverystate,
			ObjectMapper objectMapper) throws IOException, JsonGenerationException, JsonMappingException {

		if (flowOrdertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			if (cwbOrder.getConsigneemobile() == null || cwbOrder.getConsigneemobile().isEmpty()) {
				logger.warn("没有手机号的不发送短信接口通知,家有购物cwb=" + cwbOrder.getCwb());
				return null;
			}

			Homegou homegou = this.getHomeGou();
			HomegouXMLNote xmlnote = new HomegouXMLNote();
			xmlnote.setCwb(orderFlow.getCwb());
			xmlnote.setConsigneeno(cwbOrder.getConsigneeno());
			xmlnote.setConsigneename(cwbOrder.getConsigneename());
			xmlnote.setMobilephone(cwbOrder.getConsigneemobile());
			xmlnote.setMessage_type(getHomeGoCwbOrderType(cwbOrder.getCwbordertypeid()));
			xmlnote.setMessage_content(getHomeGoCwbOrderContent(cwbOrder.getCwbordertypeid(), cwbOrder));
			xmlnote.setBusiness_no(homegou.getPartener());

			logger.info("订单号={} 封装0家有购物0所需要的json-- [短信内容]-,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return objectMapper.writeValueAsString(xmlnote);
		}

		if (flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (delivery_state == DeliveryStateEnum.PeiSongChengGong.getValue() || delivery_state == DeliveryStateEnum.ShangMenHuanChengGong.getValue() || delivery_state == DeliveryStateEnum.ShangMenTuiChengGong
						.getValue())) {
			DmpDeliveryState deliveryState = cwbOrderWothDeliverystate.getDeliveryState();

			int cwbordertypeid = Integer.valueOf(cwbOrder.getCwbordertypeid());
			Homegou homegou = this.getHomeGou();
			HomegouXMLNote xmlnote = new HomegouXMLNote();
			xmlnote.setBusiness_no(homegou.getPartener());
			xmlnote.setCwb(cwbOrder.getCwb());
			xmlnote.setTranscwb(cwbOrder.getTranscwb());
			xmlnote.setOrder_no(cwbOrder.getTranscwb());
			xmlnote.setDeliveryState("1");
			xmlnote.setDeliverytime(DateTimeUtil.formatDate(orderFlow.getCredate(), "yyyyMMdd"));
			xmlnote.setDeliveryReason("00");
			xmlnote.setPeisongqufen(getPeiSongQufen(cwbOrder, cwbordertypeid));
			xmlnote.setPayAmount(cwbOrder.getReceivablefee() + "");

			// /支付方式的标示
			xmlnote.setPaytypeflag(getPaytype(deliveryState));

			xmlnote.setPaytypeid1(getPaytype1(deliveryState));
			xmlnote.setPayamount1(deliveryState.getReceivedfee() + "");
			xmlnote.setPaytypeid2("00"); // pos
			xmlnote.setPayamount2("0");

			// //////////////MOB授权信息////////////////////////////
			xmlnote.setTerminal_no("");
			xmlnote.setCard_bank("");
			xmlnote.setCard_no("");
			xmlnote.setOk_date(DateTimeUtil.formatDate(orderFlow.getCredate(), "yyyyMMddHHmmss"));
			xmlnote.setInamt_amt(deliveryState.getPos() + "");
			xmlnote.setBatch_no("");
			xmlnote.setSys_no("");
			xmlnote.setInamt_date(DateTimeUtil.formatDate(orderFlow.getCredate(), "yyyyMMdd"));
			xmlnote.setType("02");

			logger.info("订单号={} 封装0家有购物0所需要的json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return objectMapper.writeValueAsString(xmlnote);
		}

		return null;

	}

	private int getPaytype(DmpDeliveryState deliveryState) {
		int paytypeid = 0;
		if (deliveryState.getCash().compareTo(BigDecimal.ZERO) > 0) {
			paytypeid = PaytypeEnum.Xianjin.getValue();
		}
		if (deliveryState.getPos().compareTo(BigDecimal.ZERO) > 0) {
			paytypeid = PaytypeEnum.Pos.getValue();
		}
		return paytypeid;
	}

	private String getPaytype1(DmpDeliveryState deliveryState) {
		String paytypeid1 = "03";
		if (deliveryState.getCash().compareTo(BigDecimal.ZERO) > 0) {
			paytypeid1 = "03";
		}
		if (deliveryState.getPos().compareTo(BigDecimal.ZERO) > 0) {
			paytypeid1 = "04";
		}
		return paytypeid1;
	}

	private String getPaytype2(DmpDeliveryState deliveryState) {
		String paytypeid2 = "";
		if (deliveryState.getCash().compareTo(BigDecimal.ZERO) > 0) {
			paytypeid2 = "04";
		}
		if (deliveryState.getPos().compareTo(BigDecimal.ZERO) > 0) {
			paytypeid2 = "03";
		}
		return paytypeid2;
	}

	private String getPeiSongQufen(DmpCwbOrder cwbOrder, int cwbordertypeid) {
		String peisongqufen = "";
		if (cwbOrder.getReceivablefee().compareTo(BigDecimal.ZERO) == 0 && cwbordertypeid == CwbOrderTypeIdEnum.Peisong.getValue()) {
			peisongqufen = "10";
		}
		if (cwbOrder.getReceivablefee().compareTo(BigDecimal.ZERO) > 0 && cwbordertypeid == CwbOrderTypeIdEnum.Peisong.getValue()) {
			peisongqufen = "20";
		}
		if (cwbordertypeid == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {
			peisongqufen = "40";
		}
		return peisongqufen;
	}

	private String getHomeGoCwbOrderType(String Cwbordertypeid) {
		String msg_type = "";
		if (Cwbordertypeid.contains(CwbOrderTypeIdEnum.Peisong.getValue() + "")) {
			msg_type = "21";
		}
		if (Cwbordertypeid.contains(CwbOrderTypeIdEnum.Shangmentui.getValue() + "")) {
			msg_type = "23";
		}
		if (Cwbordertypeid.contains(CwbOrderTypeIdEnum.Shangmenhuan.getValue() + "")) {
			msg_type = "22";
		}
		return msg_type;
	}

	private String getHomeGoCwbOrderContent(String Cwbordertypeid, DmpCwbOrder cwbOrder) {
		String msg = cwbOrder.getConsigneename() + "顾客,您的订单将在";
		long deliveryid = cwbOrder.getDeliverid();
		User user = getDmpdao.getUserById(deliveryid);
		if (Cwbordertypeid.contains(CwbOrderTypeIdEnum.Peisong.getValue() + "")) {
			msg += "今日送达(配送电话:" + user.getUsermobile() + "),问询请拨打4008210808，感谢您的订购。";
		}
		if (Cwbordertypeid.contains(CwbOrderTypeIdEnum.Shangmentui.getValue() + "")) {
			msg += "3日内上门取货(配送电话:" + user.getUsermobile() + "),问询请拨打4008210808。";
		}
		if (Cwbordertypeid.contains(CwbOrderTypeIdEnum.Shangmenhuan.getValue() + "")) {
			msg += "3日内交换(配送电话:" + user.getUsermobile() + "),问询请拨打4008210808。";
		}
		return msg;
	}

	private String getPeiSongJieGuo(long deliverystate) {
		if (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()
				|| deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
			return "1";
		}

		return "2";
	}

	private String getYiChangYuanYin(long deliverystate, DmpCwbOrder cwbOrder) {
		String exptcode = "";
		if (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()
				|| deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
			return "00";
		}
		if (deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue() || deliverystate == DeliveryStateEnum.ShangMenJuTui.getValue()) {

			ExptReason exptReason = b2cTools.getExptReasonByB2c(0, cwbOrder.getBackreasonid(), String.valueOf(cwbOrder.getCustomerid()), deliverystate);
			exptcode = exptReason.getExpt_code() == null ? "08" : exptReason.getExpt_code();
		}

		return exptcode;
	}

	/**
	 * 付款方式
	 * 
	 * @param flowstatus
	 * @param opscwbid
	 * @param cjDao
	 * @param cj
	 * @return
	 */
	private String getFuKuanFangShi(long deliverystate, String paytype) {
		if (deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue() || deliverystate == DeliveryStateEnum.ShangMenJuTui.getValue()) {

			return "00";
		}
		if (paytype.contains(String.valueOf(PaytypeEnum.Xianjin.getValue()))) {
			return "03";
		}
		if (paytype.contains(String.valueOf(PaytypeEnum.Pos.getValue()))) {
			return "18";
		}

		return "03";
	}

	private void updateNewDeliveryState(DmpOrderFlow orderFlow, DmpCwbOrder cwbOrder) {
		try {
			long flowordertype = orderFlow.getFlowordertype();
			if (flowordertype != FlowOrderTypeEnum.YiShenHe.getValue()) {
				return;
			}
			B2CData b2CData = b2CDataDAO.getB2cDataByKeys(orderFlow.getCwb(), flowordertype, 0);
			if (b2CData != null) {
				logger.warn("系统自动屏蔽状态={},订单号={}", FlowOrderTypeEnum.YiShenHe.getValue(), cwbOrder.getCwb());
				b2CDataDAO.updateB2cIdSQLResponseStatus(b2CData.getB2cid(), 3, "系统自动屏蔽此状态");
			}
		} catch (Exception e) {
			logger.error("ABC修改短时间内未推送36状态发生未知异常", e);
		}
	}

}
