package cn.explink.b2c.telecomsc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.B2CData;
import cn.explink.domain.User;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class BuildTelecomshopB2cData {
	private Logger logger = LoggerFactory.getLogger(BuildTelecomshopB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	TelecomshopService telecomshopService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	B2CDataDAO b2CDataDAO;

	public void BuildWanXiangMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, DmpDeliveryState deliveryState, long delivery_state, ObjectMapper objectMapper)
			throws IOException, JsonGenerationException, JsonMappingException {
		if (!b2ctools.isB2cOpen(B2cEnum.Telecomshop.getKey())) {
			logger.info("未开0电信商城0的状态反馈接口!");
			return;
		}

		Map<String, String> paramsMap = null;
		try {

			String receivedStatus = telecomshopService.getFlowEnum(flowOrdertype, delivery_state, cwbOrder.getCwbordertypeid());
			if (receivedStatus == null) {
				logger.info("订单号：{} 不属于0电信商城0所需要的json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
				return;
			} else {

				Telecomshop telcom = telecomshopService.getTelecomshop(B2cEnum.Telecomshop.getKey());

				paramsMap = createMapParms(orderFlow, flowOrdertype, cwbOrder, deliveryState, delivery_state, receivedStatus, telcom);

				String response = telecomshopService.postHTTPJsonDataToTelecom(paramsMap, telcom.getSender_url());

				logger.info("当前0电信商城0[返回信息]cwb=" + cwbOrder.getCwb() + ",flowordertype={},msg={}", flowOrdertype, response);

			}

		} catch (Exception e) {

			sendFaildAndSaveB2cData(orderFlow, cwbOrder, paramsMap); // 推送失败,然后存储起来,定时发送

			logger.error("处理电信商城回传发生未知异常" + orderFlow.getCwb() + "状态=" + flowOrdertype, e);
		}
	}

	private void sendFaildAndSaveB2cData(DmpOrderFlow orderFlow, DmpCwbOrder cwbOrder, Map<String, String> paramsMap) throws IOException, JsonGenerationException, JsonMappingException {
		String jsonContent = JacksonMapper.getInstance().writeValueAsString(paramsMap);
		B2CData b2cData = new B2CData();
		b2cData.setCwb(orderFlow.getCwb());
		b2cData.setCustomerid(cwbOrder.getCustomerid());
		b2cData.setFlowordertype(orderFlow.getFlowordertype());
		b2cData.setPosttime(DateTimeUtil.formatDate(orderFlow.getCredate()));
		b2cData.setSend_b2c_flag(0);

		b2cData.setJsoncontent(jsonContent); // 封装的JSON格式.
		String multi_shipcwb = StringUtils.hasLength(cwbOrder.getMulti_shipcwb()) ? cwbOrder.getMulti_shipcwb() : null;
		b2CDataDAO.saveB2CData(b2cData, multi_shipcwb);
	}

	private Map<String, String> createMapParms(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, DmpDeliveryState deliveryState, long delivery_state, String receivedStatus,
			Telecomshop telcom) throws UnsupportedEncodingException {
		String deliverystateremark = deliveryState == null ? null : deliveryState.getDeliverstateremark();
		if (deliverystateremark != null && deliverystateremark.contains("手机终端反馈")) {
			deliverystateremark = "-手机终端反馈";
		} else {
			deliverystateremark = "";
		}

		LinkedHashMap<String, String> paramsMap = new LinkedHashMap<String, String>();
		paramsMap.put("OUTTRANSNO", String.valueOf(orderFlow.getFloworderid()));
		paramsMap.put("DISTRIBUTION_ID", cwbOrder.getCwb());
		paramsMap.put("ORDER_ID", cwbOrder.getTranscwb());
		paramsMap.put("DISTRIBUTION_STATUS", receivedStatus);
		paramsMap.put("DISTRIBUTION_DESC", orderFlowDetail.getDetail(orderFlow) + deliverystateremark);
		paramsMap.put("STRANDED_REASON", delivery_state != 0 ? cwbOrder.getLeavedreason() : "");
		paramsMap.put("STRANDED_NUM", cwbOrder.getLeavedreason() != null && !cwbOrder.getLeavedreason().isEmpty() ? "1" : "0");
		paramsMap.put("RETURN_REASON", delivery_state != 0 ? cwbOrder.getBackreason() : "");

		paramsMap.put("COURIERS_NAME", "");
		paramsMap.put("COURIERS_PHONE", "");
		paramsMap.put("RECEIVABLES_INFO", "");

		if (flowOrdertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue() || flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue()) {
			User deliveryUser = getDmpdao.getUserById(deliveryState.getDeliveryid());
			paramsMap.put("COURIERS_NAME", deliveryUser.getRealname());
			paramsMap.put("COURIERS_PHONE", deliveryUser.getUsermobile());
		}
		if (flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue()) {
			String paytype = cwbOrder.getNewpaywayid().contains(PaytypeEnum.Pos.getValue() + "") ? "6" : "5";
			String receivables_info = paytype + ":" + deliveryState.getReceivedfee();
			paramsMap.put("RECEIVABLES_INFO", receivables_info);
		}
		paramsMap.put("CURRENT_TIME", DateTimeUtil.formatDate(orderFlow.getCredate()).replaceAll("-", "").replaceAll(":", "").replaceAll(" ", ""));
		paramsMap.put("IDCARD_PIC_ADDRESS", "");

		String createLinkString = TelecomshopService.createLinkString(paramsMap, false);

		String sign_str = createLinkString;
		String sign = MD5Util.md5(sign_str);
		paramsMap.put("SIGN", sign);
		paramsMap.put("SIGNTYPE", "MD5");

		logger.info("当前[推送]0电信商城0信息cwb={},flowOrdertype=" + flowOrdertype + ",parms={},sign=" + sign, cwbOrder.getCwb(), URLDecoder.decode(createLinkString, "UTF-8"));

		return paramsMap;
	}

	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "张三");
		map.put("address", "北京");
		map.put("stusde", "学校");
		String json = JacksonMapper.getInstance().writeValueAsString(map);
		System.out.println(json);

		Map<String, String> mapss = JacksonMapper.getInstance().readValue(json, Map.class);
		System.out.println(mapss.get("name"));
		System.out.println(mapss.get("address"));
		System.out.println(mapss.get("stusde"));

		String times = "2014-03-04 15:20:50";

		System.out.println(times.replaceAll("-", "").replaceAll(":", "").replaceAll(" ", ""));
	}

}
