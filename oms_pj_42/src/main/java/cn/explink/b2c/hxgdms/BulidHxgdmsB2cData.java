package cn.explink.b2c.hxgdms;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.User;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.RestHttpServiceHanlder;
import cn.explink.util.MD5.MD5Util;

@Service
public class BulidHxgdmsB2cData {
	private Logger logger = LoggerFactory.getLogger(BulidHxgdmsB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;

	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	HxgdmsService hxgdmsService;

	public String buildHxgdmsMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, long delivery_state, DmpDeliveryState dmpDeliveryState, ObjectMapper objectMapper)
			throws IOException, JsonGenerationException, JsonMappingException {

		String dmsReceiveStatus = hxgdmsService.getHxgdmsFlowEnum(flowOrdertype, delivery_state);
		if (dmsReceiveStatus == null) {
			logger.info("订单号：{} 不属于[好享购DMS]所需要的json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return null;
		}
		Hxgdms dms = hxgdmsService.getDMSSettingMethod(B2cEnum.Hxgdms.getKey());

		/**
		 * 跟踪日志访问
		 */
		hxgdmsService.sendTrackInfoByHxgdms(orderFlow, cwbOrder, dmsReceiveStatus, dms);

		/**
		 * 推送异常反馈信息
		 */
		if (delivery_state == DeliveryStateEnum.QuanBuTuiHuo.getValue() || delivery_state == DeliveryStateEnum.BuFenTuiHuo.getValue() || delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue()
				|| delivery_state == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
			hxgdmsService.sendErrorByHxgdms(cwbOrder, delivery_state, dms);
		}

		/**
		 * 存储入库、领货、妥投 三个状态主要推送
		 */
		HxgdmsJsonNote note = getHxgdmsJsonBean(orderFlow, cwbOrder, dmpDeliveryState, dms); // 0
																								// 1
																								// 2
																								// 三个状态存储,并回传
		if (note == null) {
			return null;
		}
		return objectMapper.writeValueAsString(note);

	}

	private HxgdmsJsonNote getHxgdmsJsonBean(DmpOrderFlow orderFlow, DmpCwbOrder cwbOrder, DmpDeliveryState dmpDeliveryState, Hxgdms dms) {
		HxgdmsJsonNote note = new HxgdmsJsonNote();
		if ((orderFlow.getFlowordertype() == FlowOrderTypeEnum.RuKu.getValue() && cwbOrder.getCwbordertypeid().contains("1"))
				|| (dmpDeliveryState != null && dmpDeliveryState.getDeliverystate() == DeliveryStateEnum.ShangMenTuiChengGong.getValue() && cwbOrder.getCwbordertypeid().contains("2"))

		) {
			note.setWorkCode(cwbOrder.getCwb());
			note.setWorkState(0);
			note.setWorkStateTime(DateTimeUtil.formatDate(orderFlow.getCredate()));
			note.setWorkDesc(orderFlowDetail.getDetail(orderFlow));
			note.setDelveryCode(dms.getDcode());
			note.setDelveryName("");
			note.setDelveryMobile("");
			note.setDeleServerMobile(dms.getDeleServerMobile());
			note.setPosMoney(BigDecimal.ZERO);
			note.setCashMoney(BigDecimal.ZERO);
			return note;

		}
		if ((orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue() && cwbOrder.getCwbordertypeid().contains("1"))
				|| (orderFlow.getFlowordertype() == FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue() && cwbOrder.getCwbordertypeid().contains("2"))) {
			User deliveryUser = getDmpdao.getUserById(cwbOrder.getDeliverid());

			note.setWorkCode(cwbOrder.getCwb());
			note.setWorkState(1);
			note.setWorkStateTime(DateTimeUtil.formatDate(orderFlow.getCredate()));
			note.setWorkDesc(orderFlowDetail.getDetail(orderFlow));
			note.setDelveryCode(dms.getDcode());
			note.setDelveryName(deliveryUser.getRealname());
			note.setDelveryMobile(deliveryUser.getUsermobile() == null ? "" : deliveryUser.getUsermobile());
			note.setDeleServerMobile(dms.getDeleServerMobile());
			note.setPosMoney(BigDecimal.ZERO);
			note.setCashMoney(BigDecimal.ZERO);
			return note;
		}
		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue() && dmpDeliveryState.getDeliverystate() == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			User deliveryUser = getDmpdao.getUserById(cwbOrder.getDeliverid());

			note.setWorkCode(cwbOrder.getCwb());
			note.setWorkState(2);
			note.setWorkStateTime(DateTimeUtil.formatDate(orderFlow.getCredate()));
			note.setWorkDesc(orderFlowDetail.getDetail(orderFlow));
			note.setDelveryCode(dms.getDcode());
			note.setDelveryName(deliveryUser.getRealname());
			note.setDelveryMobile(deliveryUser.getUsermobile() == null ? "" : deliveryUser.getUsermobile());
			note.setDeleServerMobile(dms.getDeleServerMobile());
			String paytype = cwbOrder.getNewpaywayid();
			int dmspaytype = 0;
			if (paytype.contains(PaytypeEnum.Xianjin.getValue() + "")) {
				dmspaytype = 0;
				note.setCashMoney(dmpDeliveryState.getReceivedfee());
				note.setPosMoney(BigDecimal.ZERO);

			}
			if (paytype.contains(PaytypeEnum.Pos.getValue() + "")) {
				dmspaytype = 1;
				note.setPosMoney(dmpDeliveryState.getReceivedfee());
				note.setCashMoney(BigDecimal.ZERO);
			}
			note.setPayType(dmspaytype);
			return note;
		}
		return null;
	}

}
