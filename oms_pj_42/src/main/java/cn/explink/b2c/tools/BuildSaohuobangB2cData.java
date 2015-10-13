package cn.explink.b2c.tools;

import java.io.IOException;
import java.net.URLEncoder;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.explink.b2c.saohuobang.Saohuobang;
import cn.explink.b2c.saohuobang.SaohuobangDao;
import cn.explink.b2c.saohuobang.SaohuobangFlowEnum;
import cn.explink.b2c.saohuobang.SaohuobangomsService;
import cn.explink.b2c.saohuobang.SaohuobangXMLNote;
import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.dao.GetDmpDAO;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.CwbOrderWithDeliveryState;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BuildSaohuobangB2cData {
	private Logger logger = LoggerFactory.getLogger(BuildSaohuobangB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	SaohuobangomsService saohuobangService;
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	B2cTools b2cTools;
	@Autowired
	B2CDataDAO b2CDataDAO;
	@Autowired
	SaohuobangDao saohuobangDao;

	public Saohuobang getSaohuobang(int key) {
		return saohuobangService.getSaohuobangSettingMethod(key);
	}

	public void BuildSaohuobangMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, long delivery_state, CwbOrderWithDeliveryState cwbOrderWothDeliverystate, String enums,
			ObjectMapper objectMapper) throws IOException, JsonGenerationException, JsonMappingException {
		Saohuobang saohuobang = getSaohuobang(Integer.parseInt(enums));

		String status = "";// 操作详情状态
		String onfig = getSaohuobangFlowEnum(flowOrdertype, delivery_state);
		if (onfig == null) {
			logger.info("当前状态不属于[扫货帮]推送的状态,cwb={}", cwbOrder.getCwb());
			return;
		}
		status = orderFlowDetail.getDetail(orderFlow);
		String requestxml = xmlforoption(saohuobang, status, cwbOrder, orderFlow, flowOrdertype, delivery_state);
		logger.info("[扫货帮]跟踪日志【实时】反馈推送status={},xml={}", status, requestxml);
		// 签名验证
		String sign = "";
		try {
			sign = SaohuobangSign.encryptSign_Method(requestxml, saohuobang.getKey());
			sign = URLEncoder.encode(sign, "utf-8");
			String retrunxml = saohuobangService.getPostMethodResult(saohuobang.getTrackLog_URL(), sign, requestxml);
			logger.info("[扫货帮]跟踪日志【实时】返回flowordertype={},xml={}", flowOrdertype, retrunxml + sign);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常了{}异常原因：{}", e, e.getMessage());
		}

		SaohuobangXMLNote xmlnote = new SaohuobangXMLNote();
		xmlnote.setCwb(orderFlow.getCwb());
		xmlnote.setTranscwb(cwbOrder.getTranscwb());
		xmlnote.setStatus(status);
		xmlnote.setProvince(cwbOrder.getCwbprovince());
		xmlnote.setCity(cwbOrder.getCwbcity());
		xmlnote.setZone(cwbOrder.getCwbcounty());
		xmlnote.setAddress(cwbOrder.getConsigneeaddress());
		xmlnote.setDelivery_state(delivery_state);
		xmlnote.setOptiontime(DateTimeUtil.formatDate(orderFlow.getCredate()));
		logger.info("订单号={} 封装[扫货帮]所需要的配送结果json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
		saohuobangDao.save(xmlnote, flowOrdertype);
	}

	private String xmlforoption(Saohuobang saohuobang, String state, DmpCwbOrder cwbOrder, DmpOrderFlow orderFlow, long flowOrdertype, long delivery_state) {
		// ACCEPT(接单)——入库
		// UNACCEPT（不接单）——没有入库
		// GOT（揽收成功）——到货
		// NOT_SEND(揽收失败)——没到货
		// FAILED（失败）——送货失败
		// SIGNED（送达）——送货成功
		String remark = "";
		String infoContent = "";
		StringBuffer sb = new StringBuffer();
		sb.append("<UpdateInfo>");
		sb.append("<txLogisticID>" + cwbOrder.getTranscwb() + "</txLogisticID>");
		if (flowOrdertype == FlowOrderTypeEnum.DaoRuShuJu.getValue()) {
			remark = "接单成功";
			infoContent = "ACCEPT";
		} else if (flowOrdertype == FlowOrderTypeEnum.RuKu.getValue()) {
			remark = "揽收成功";
			infoContent = "GOT";
		} else if (flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (delivery_state == DeliveryStateEnum.FenZhanZhiLiu.getValue() || delivery_state == DeliveryStateEnum.QuanBuTuiHuo.getValue()
						|| delivery_state == DeliveryStateEnum.HuoWuDiuShi.getValue() || delivery_state == DeliveryStateEnum.ShangMenJuTui.getValue())) {
			System.out.println(delivery_state);
			remark = "失败";
			infoContent = "FAILED";
			sb.append("<mailNo>" + cwbOrder.getCwb() + "</mailNo>");
		} else {
			remark = "送达";
			infoContent = "SIGNED";
			sb.append("<mailNo>" + cwbOrder.getCwb() + "</mailNo>");
		}
		sb.append("<logisticProviderID>" + saohuobang.getProviderID() + "</logisticProviderID>");
		sb.append("<infoType>" + state + "</infoType>");
		sb.append("<infoContent>" + infoContent + "</infoContent>");
		sb.append("<remark>" + remark + "</remark>");
		sb.append("</UpdateInfo>");
		return sb.toString().replace("null", "");
	}

	public String getSaohuobangFlowEnum(long flowordertype, long deliverystate) {
		for (SaohuobangFlowEnum SEnum : SaohuobangFlowEnum.values()) {
			if (flowordertype == SEnum.getFlowordertype()) {
				return SEnum.getState();
			}
		}
		return null;
	}

}
