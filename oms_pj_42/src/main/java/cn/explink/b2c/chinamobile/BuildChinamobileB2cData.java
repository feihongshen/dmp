package cn.explink.b2c.chinamobile;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.xmldto.OrderFlowDto;
import cn.explink.b2c.sfxhm.Sfxhm;
import cn.explink.b2c.sfxhm.SfxhmTrackEnum;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.dao.CommonSendDataDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.CommenSendData;
import cn.explink.domain.User;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BuildChinamobileB2cData {
	private Logger logger = LoggerFactory.getLogger(BuildChinamobileB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;

	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	ChinamobileService chinamobileService;
	@Autowired
	CommonSendDataDAO commonSendDataDAO;

	public String BuildChinaMobileMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, DmpDeliveryState dmpDeliveryState, long delivery_state, ObjectMapper objectMapper)
			throws IOException, JsonGenerationException, JsonMappingException {

		String cmstate = chinamobileService.getFlowStateByChinaMobile(flowOrdertype, delivery_state) == null ? null : chinamobileService.getFlowStateByChinaMobile(flowOrdertype, delivery_state)
				.getCmcode();

		if (cmstate == null) {
			logger.warn("该流程不属于中国移动推送流程cwb={},deliverystate={}", cwbOrder.getCwb(), delivery_state);
			return null;
		}

		logger.info("订单号：{}封装成0中国移动0所需要的json----开始,状态：{}", cwbOrder.getCwb(), flowOrdertype);
		// Chinamobile
		// cm=chinamobileService.getChinamobile(B2cEnum.ChinaMobile.getKey());

		ChinamobileXMLNote xmlnote = new ChinamobileXMLNote();
		xmlnote.setOprnum(cwbOrder.getTranscwb());
		xmlnote.setDelieverId(cwbOrder.getCwb());
		xmlnote.setPartnerId("");
		xmlnote.setSendTime(DateTimeUtil.formatDate(orderFlow.getCredate()));
		String getDetail = orderFlowDetail.getDetail(orderFlow);

		String showMsg = getDesc(orderFlow, cwbOrder, delivery_state, getDetail);

		xmlnote.setShowMsg(showMsg.isEmpty() ? getDetail : showMsg);
		xmlnote.setStatus(cmstate);
		String deliveryfailure = "";

		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			CommenSendData commenSendData = commonSendDataDAO.getCommenCwb(orderFlow.getCwb(), FlowOrderTypeEnum.FenZhanLingHuo.getValue());
			if (commenSendData != null) {
				OrderFlowDto orderFlowDto = JacksonMapper.getInstance().readValue(commenSendData.getDatajson(), OrderFlowDto.class);
				xmlnote.setShowMsg(orderFlowDto.getFloworderdetail());
			}
		}

		if (delivery_state == DeliveryStateEnum.QuanBuTuiHuo.getValue() || delivery_state == DeliveryStateEnum.BuFenTuiHuo.getValue() || delivery_state == DeliveryStateEnum.ShangMenJuTui.getValue()) {
			// ExptReason exptReason =
			// b2ctools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(),cwbOrder.getBackreasonid(),String.valueOf(cwbOrder.getCustomerid()),delivery_state);
			// String
			// exception_no=exptReason.getExpt_code()==null||exptReason.getExpt_code().isEmpty()?"":exptReason.getExpt_code();
			// String
			// exception_msg=exptReason.getExpt_msg()==null||exptReason.getExpt_msg().isEmpty()?"":exptReason.getExpt_msg();
			deliveryfailure = cwbOrder.getBackreason(); // 移动要求可以传中文异常原因
		}

		xmlnote.setDeliveryfailure(deliveryfailure);

		logger.info("订单号：{}封装成0中国移动0所需要的json----结束,状态：{}", cwbOrder.getCwb(), cwbOrder.getFlowordertype());
		return objectMapper.writeValueAsString(xmlnote);

	}

	private String getDesc(DmpOrderFlow orderFlow, DmpCwbOrder cwbOrder, long deliverystate, String getDetail) {
		String desc = ""; // 描述
		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			desc = "您的订单已送达成功";
		} else if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {

			desc = "快件由于原因 " + cwbOrder.getLeavedreason() + "派送不成功";

		} else if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {

			if (getDetail.contains("派件员")) {
				desc = getDetail.replace("派件员", "快递员");
			}

		}
		return desc;
	}

}
