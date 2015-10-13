package cn.explink.b2c.dongfangcj;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
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
public class BuildDongFangCJB2cData {
	private Logger logger = LoggerFactory.getLogger(BuildDongFangCJB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	DongFangCJService dongFangCJService;
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	B2cTools b2cTools;
	@Autowired
	B2CDataDAO b2CDataDAO;

	public String BuildDongFangCJMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, long delivery_state, CwbOrderWithDeliveryState cwbOrderWothDeliverystate,
			ObjectMapper objectMapper) throws IOException, JsonGenerationException, JsonMappingException {

		if (flowOrdertype != FlowOrderTypeEnum.YiShenHe.getValue()) {
			logger.info("订单号={} 不属于 0东方CJC0所需要的json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return null;
		} else {
			DmpDeliveryState delivery = cwbOrderWothDeliverystate.getDeliveryState();
			if (delivery.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
				logger.info("订单号={}滞留状态不做存储", cwbOrder.getCwb());
				return null;
			}
			if (Integer.valueOf(cwbOrder.getCwbordertypeid()) == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
				DongFangCJXMLNote xmlnote = new DongFangCJXMLNote();
				xmlnote.setCwb(cwbOrder.getCwb());
				xmlnote.setGobacktime(DateTimeUtil.formatDate(orderFlow.getCredate(), "yyyyMMddHHmmss"));
				String gobackflag = "1"; // 默认为完成
				String faildReason = "00"; // 成功
				if (delivery_state == DeliveryStateEnum.ShangMenJuTui.getValue()) {
					gobackflag = "2"; // 回收失败
					ExptReason exptReason = b2cTools.getExptReasonByB2c(0, cwbOrder.getBackreasonid(), String.valueOf(cwbOrder.getCustomerid()), cwbOrderWothDeliverystate.getDeliveryState()
							.getDeliverystate());
					faildReason = exptReason.getExpt_code();
				}
				xmlnote.setGobackflag(gobackflag);
				xmlnote.setFaildReason(faildReason);
				xmlnote.setInterfaceType(2); // 老接口
				return objectMapper.writeValueAsString(xmlnote);
			}

			DongFangCJXMLNote xmlnote = new DongFangCJXMLNote();
			xmlnote.setCwb(cwbOrder.getCwb());
			xmlnote.setDeliverytime(DateTimeUtil.formatDate(orderFlow.getCredate()));
			xmlnote.setDeliveryresult(getPeiSongJieGuo(delivery.getDeliverystate()));
			xmlnote.setExptreason(getYiChangYuanYin(delivery.getDeliverystate(), cwbOrder));
			xmlnote.setPaytype1(getFuKuanFangShi(delivery.getDeliverystate(), cwbOrder.getNewpaywayid()));
			xmlnote.setPayamount1(String.valueOf(delivery.getReceivedfee()));
			xmlnote.setPaytype2("00");
			xmlnote.setPayamount2("00");
			xmlnote.setPayamount(String.valueOf(delivery.getReceivedfee()));
			xmlnote.setInterfaceType(1); // 老接口

			return objectMapper.writeValueAsString(xmlnote);
		}
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
