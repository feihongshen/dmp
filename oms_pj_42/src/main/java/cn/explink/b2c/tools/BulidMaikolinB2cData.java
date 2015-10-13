package cn.explink.b2c.tools;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.explink.b2c.maikolin.Maikolin;
import cn.explink.b2c.maikolin.MaikolinFlowEnum;
import cn.explink.b2c.maikolin.MaikolinService;
import cn.explink.b2c.maikolin.MaikolinXMLNote;
import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.dao.GetDmpDAO;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.CwbOrderWithDeliveryState;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BulidMaikolinB2cData {
	private Logger logger = LoggerFactory.getLogger(BulidMaikolinB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	MaikolinService maikolinService;
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	B2cTools b2cTools;
	@Autowired
	B2CDataDAO b2CDataDAO;

	public Maikolin getMaikolin() {
		return maikolinService.getMaikolinSettingMethod(B2cEnum.Maikaolin.getKey());
	}

	public String BuildMaikolinMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, long delivery_state, CwbOrderWithDeliveryState cwbOrderWothDeliverystate,
			ObjectMapper objectMapper) throws IOException, JsonGenerationException, JsonMappingException {
		Maikolin maikolin = this.getMaikolin();
		String status = "";// 操作详情状态
		int onfig = maikolinService.getMaikaolinFlowEnum(flowOrdertype, delivery_state);
		if (onfig == 0) {
			logger.info("当前状态不属于[麦考林]推送的状态,cwb={}", cwbOrder.getCwb());
			return null;
		}

		status = orderFlowDetail.getDetail(orderFlow);

		String requestxml = xmlforoption(maikolin, status, cwbOrder, orderFlow, flowOrdertype);
		logger.info("[麦考林]跟踪日志反馈推送flowordertype={},xml={}", flowOrdertype, requestxml);

		String retrunxml = maikolinService.HTTPInvokeWs(requestxml, maikolin.getPushCwb_URL());
		logger.info("[麦考林]跟踪日志返回flowordertype={},xml={}", flowOrdertype, retrunxml);

		if (flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue() || delivery_state == DeliveryStateEnum.QuanBuTuiHuo.getValue()
						|| delivery_state == DeliveryStateEnum.HuoWuDiuShi.getValue() || delivery_state == DeliveryStateEnum.ShangMenJuTui.getValue())) {
			ExptReason exptReason = b2cTools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(), cwbOrder.getBackreasonid(), String.valueOf(cwbOrder.getCustomerid()), delivery_state);
			String yichangXml = exceptionDelivery(maikolin, cwbOrder, exptReason, orderFlow, flowOrdertype);
			logger.info("[麦考林]异常信息反馈推送flowOrdertype={},xml={}", flowOrdertype, yichangXml);

			String responsexml = maikolinService.HTTPInvokeWs(yichangXml, maikolin.getPushCwb_URL());
			logger.info("[麦考林]异常信息反馈返回={}", responsexml);
		}

		if (flowOrdertype == FlowOrderTypeEnum.RuKu.getValue()) {

			MaikolinXMLNote xmlnote = new MaikolinXMLNote();
			xmlnote.setCwb(orderFlow.getCwb());
			xmlnote.setCourierdate(DateTimeUtil.formatDate(orderFlow.getCredate(), "yyyyMMddHHmmss"));
			xmlnote.setStatus(orderFlowDetail.getDetail(orderFlow));
			logger.info("订单号={} 封装0[maikolin]0所需要的入库信息-- [PackageCourier]-,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return objectMapper.writeValueAsString(xmlnote);
		}
		if (flowOrdertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			MaikolinXMLNote xmlnote = new MaikolinXMLNote();
			xmlnote.setPackage_id(cwbOrder.getCwb());
			xmlnote.setDeliverytime(DateTimeUtil.formatDate(orderFlow.getCredate(), "yyyyMMddHHmmss"));
			xmlnote.setContact_phone(getDmpdao.getUserById(cwbOrder.getDeliverid()).getUsermobile());
			xmlnote.setCourier(getDmpdao.getUserById(cwbOrder.getDeliverid()).getRealname());// 小件员
			xmlnote.setCourierdate(DateTimeUtil.formatDate(orderFlow.getCredate(), "yyyyMMddHHmmss"));
			logger.info("订单号={} 封装[maikolin]所需要的分站领货json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return objectMapper.writeValueAsString(xmlnote);
		}
		if (flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue()) {

			ExptReason exptReason = b2cTools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(), cwbOrder.getBackreasonid(), String.valueOf(cwbOrder.getCustomerid()), delivery_state);
			MaikolinXMLNote xmlNote = new MaikolinXMLNote();
			xmlNote.setPackage_id(cwbOrder.getCwb());
			xmlNote.setStatus(status);
			xmlNote.setOperatetime(DateTimeUtil.formatDate(orderFlow.getCredate(), "yyyyMMddHHmmss"));
			xmlNote.setErrocode(exptReason.getExpt_code());
			xmlNote.setErromassage(exptReason.getExpt_msg());
			xmlNote.setCourierdate(DateTimeUtil.formatDate(orderFlow.getCredate(), "yyyyMMddHHmmss"));

			if (delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
				return null;
			}
			if (delivery_state == DeliveryStateEnum.PeiSongChengGong.getValue() || delivery_state == DeliveryStateEnum.ShangMenTuiChengGong.getValue()
					|| delivery_state == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
				xmlNote.setOtstatus("C");
			} else {
				xmlNote.setOtstatus("R");
			}
			logger.info("订单号={} 封装[maikolin]所需要的配送结果json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return objectMapper.writeValueAsString(xmlNote);
		}
		return null;
	}

	private String xmlforoption(Maikolin maikolin, String state, DmpCwbOrder cwbOrder, DmpOrderFlow orderFlow, long flowOrdertype) {
		String courier = "";
		String couriertophone = "";
		String operation = getDmpdao.getUserById(orderFlow.getUserid()).getRealname();
		if (flowOrdertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			courier = getDmpdao.getUserById(cwbOrder.getDeliverid()).getRealname();
			couriertophone = getDmpdao.getUserById(cwbOrder.getDeliverid()).getUsermobile();
		} else {
			courier = getDmpdao.getUserById(orderFlow.getUserid()).getRealname();
		}

		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<tms>" + "<request_header>" + "<user_id>" + maikolin.getUserCode() + "</user_id>" + "<user_key>" + maikolin.getPrivate_key()
				+ "</user_key>" + "<method>PackageOperation</method>" + "<request_time>" + DateTimeUtil.getNowTime("yyyyMMddHHmmss") + "</request_time>" + "</request_header>" + "<request_body>"
				+ "<operation>" + "<express_id>" + maikolin.getExpress_id() + "</express_id>" + "<package_id>" + cwbOrder.getCwb() + "</package_id>" + "<emsid>" + cwbOrder.getCwb() + "</emsid>"
				+ "<status>" + state + "</status>" + "<stateprovregion>" + cwbOrder.getCwbprovince() + "</stateprovregion>" + "<city>" + cwbOrder.getCwbcity() + "</city>" + "<address>"
				+ cwbOrder.getConsigneeaddress() + "</address>" + "<courier>" + courier + "</courier>" + "<couriertophone>" + couriertophone + "</couriertophone>" + "<operatetime>"
				+ DateTimeUtil.formatDate(orderFlow.getCredate(), "yyyyMMddHHmmss") + "</operatetime>" + "<operation>" + operation + "</operation>" + "</operation>" + "</request_body>" + "</tms>";

		return xml;
	}

	public String exceptionDelivery(Maikolin maikolin, DmpCwbOrder cwbOrder, ExptReason erro, DmpOrderFlow orderFlow, long flowOrdertype) {
		String operation = getDmpdao.getUserById(orderFlow.getUserid()).getRealname();

		StringBuffer sub = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		sub.append("<tms>");
		sub.append("<request_header>");
		sub.append("<user_id>" + maikolin.getUserCode() + "</user_id>");
		sub.append("<user_key>" + maikolin.getPrivate_key() + "</user_key>");
		sub.append("<method>PackageException</method>");
		sub.append("<request_time>" + DateTimeUtil.getNowTime("yyyyMMddHHmmss") + "</request_time>");
		sub.append("</request_header><request_body><operation>");
		sub.append("<express_id>" + maikolin.getExpress_id() + "</express_id>");
		sub.append("<package_id>" + cwbOrder.getCwb() + "</package_id>");
		sub.append("<exceptionstype>" + erro.getExpt_code() + "</exceptionstype>");
		sub.append("<exceptionsdescription>" + erro.getExpt_msg() + "</exceptionsdescription>");
		sub.append("<exceptionsubmittime>" + DateTimeUtil.formatDate(orderFlow.getCredate(), "yyyyMMddHHmmss") + "</exceptionsubmittime>");
		sub.append("<operation>" + operation + "</operation>");
		sub.append("</operation></request_body>");
		sub.append("</tms>");

		return sub.toString().replaceAll("null", "");
	}

	public MaikolinFlowEnum getmaikolinFlowEnum(long flowordertype, long delivery_state) {
		if (flowordertype != FlowOrderTypeEnum.YiShenHe.getValue()) {
			for (MaikolinFlowEnum TEnum : MaikolinFlowEnum.values()) {
				if (flowordertype == TEnum.getFlowordertype()) {
					return TEnum;
				}
			}
		}
		if (delivery_state == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			return MaikolinFlowEnum.PeiSongChengGong;
		}
		if (delivery_state == DeliveryStateEnum.QuanBuTuiHuo.getValue()) {
			return MaikolinFlowEnum.JuShou;
		}
		if (delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return MaikolinFlowEnum.FenZhanZhiLiu;
		}
		if (delivery_state == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
			return MaikolinFlowEnum.BuFenShiBai;
		}
		return null;

	}

}
