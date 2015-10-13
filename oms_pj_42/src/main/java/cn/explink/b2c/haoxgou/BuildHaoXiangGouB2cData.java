package cn.explink.b2c.haoxgou;

import java.io.IOException;
import java.math.BigDecimal;

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
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrderType;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BuildHaoXiangGouB2cData {
	private Logger logger = LoggerFactory.getLogger(BuildHaoXiangGouB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	HaoXiangGouService haoXiangGouService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	private B2cTools b2ctools;

	public HaoXiangGou getHaoXiangGou() {
		return haoXiangGouService.getHaoXiangGouSettingMethod(B2cEnum.HaoXiangGou.getKey());
	}

	public String BuildHaoXiangGouMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, DmpDeliveryState deliveryState, long delivery_state, ObjectMapper objectMapper)
			throws IOException, JsonGenerationException, JsonMappingException {
		String receivedStatus = haoXiangGouService.getHXGFlowEnum(flowOrdertype, delivery_state);

		if (receivedStatus == null) {
			logger.info("订单号：{} 不属于0好享购0所需要的json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return null;
		}
		if (receivedStatus.contains("01") && Integer.valueOf(cwbOrder.getCwbordertypeid()) == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
			logger.info("上门退类型订单不参数派送中的状态...cwb={}", cwbOrder.getCwb());
			return null;
		}

		HaoXiangGou hxg = this.getHaoXiangGou();
		HaoXiangGouXMLNote xmlnote = new HaoXiangGouXMLNote();
		// 配送流程存储
		xmlnote.setDlver_cd(Integer.valueOf(cwbOrder.getCwbordertypeid()) == CwbOrderTypeIdEnum.Shangmentui.getValue() ? hxg.getDlver_cd() : hxg.getDlver_cd());
		xmlnote.setOrd_id(cwbOrder.getTranscwb());
		xmlnote.setInvc_id(cwbOrder.getCwb());
		xmlnote.setDlv_stat_cd(receivedStatus); // 这个需单独处理
		xmlnote.setDlv_stat_date(deliveryState.getDeliverytime() == null || deliveryState.getDeliverytime().isEmpty() ? DateTimeUtil.getNowTime() : deliveryState.getDeliverytime());
		xmlnote.setTransit_info(orderFlowDetail.getDetail(orderFlow));

		if (flowOrdertype == FlowOrderTypeEnum.GongYingShangJuShouFanKu.getValue()) {
			xmlnote.setTransit_info("供货商拒收返库");
		}

		if (Integer.valueOf(cwbOrder.getCwbordertypeid()) == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
			String remark1 = cwbOrder.getRemark1();
			remark1 = remark1.replaceAll("原订单号:", "").replaceAll("原运单号:", "");
			xmlnote.setInvc_id(remark1.substring(0, remark1.indexOf(",")));
			xmlnote.setOrd_id(remark1.substring(remark1.indexOf(",") + 1));
		}

		String expt_code = "";
		if (flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue() && (delivery_state == DeliveryStateEnum.QuanBuTuiHuo.getValue() || delivery_state == DeliveryStateEnum.BuFenTuiHuo.getValue())) {
			expt_code = getDmpdao.getReason(cwbOrder.getBackreasonid());
		}
		xmlnote.setDeclined_rsn(expt_code); // 拒收原因
		xmlnote.setPass(hxg.getPassword());

		// COD付款信息提交接口 代收款必须大于0
		if ((delivery_state == DeliveryStateEnum.PeiSongChengGong.getValue() || delivery_state == DeliveryStateEnum.ShangMenHuanChengGong.getValue())) {
			xmlnote.setDlver_cd(hxg.getDlver_cd());
			xmlnote.setPayer_nm(cwbOrder.getConsigneename());
			xmlnote.setPay_date(deliveryState.getDeliverytime() == null || deliveryState.getDeliverytime().isEmpty() ? DateTimeUtil.getNowTime() : deliveryState.getDeliverytime());
			xmlnote.setPay_cd(cwbOrder.getNewpaywayid().contains(PaytypeEnum.Pos.getValue() + "") ? "02" : "01");
			xmlnote.setPay_amt(deliveryState.getReceivedfee().toString());
			xmlnote.setPay_company_cd(xmlnote.getPay_cd().equals("01") ? "" : hxg.getPospaycode()); // 动态设置
			xmlnote.setPass(hxg.getPassword());
			xmlnote.setPaytypeflag(1);
		}

		// 退款垫付信息提交接口
		if (delivery_state == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
			xmlnote.setDlver_cd(hxg.getDlver_cd());
			xmlnote.setRtn_id(cwbOrder.getCwb()); // 退货号
			xmlnote.setPayee(cwbOrder.getConsigneename()); // 收件人
			xmlnote.setDisburse_date(DateTimeUtil.formatDate(orderFlow.getCredate(), "yyyyMMddHHmmss"));
			xmlnote.setDisburse_amt(String.valueOf(deliveryState.getReturnedfee()));
			xmlnote.setPass(hxg.getPassword());
			xmlnote.setPaytypeflag(2);
		}

		logger.info("订单号：{}封装成0好享购0所需要的json----状态：{}", cwbOrder.getCwb(), flowOrdertype);
		return objectMapper.writeValueAsString(xmlnote);

	}

	private String getExptReasonCode(DmpCwbOrder cwbOrder, long deliveryState) {
		String reason_code = b2ctools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(), cwbOrder.getBackreasonid(), cwbOrder.getCustomerid() + "", deliveryState).getExpt_code();
		if (reason_code == null || "".equals(reason_code)) {
			if (cwbOrder.getBackreasonid() != 0) {
				reason_code = "03";
			}
			if (cwbOrder.getLeavedreasonid() != 0) {
				reason_code = "02";
			}

		}
		return reason_code;
	}
}
