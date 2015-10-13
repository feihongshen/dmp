package cn.explink.b2c.homegobj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.homegobj.xmldto.Header;
import cn.explink.b2c.homegobj.xmldto.Order;
import cn.explink.b2c.homegobj.xmldto.OrderStatusBody;
import cn.explink.b2c.homegobj.xmldto.OrderStatusRequest;
import cn.explink.b2c.homegobj.xmldto.OrderStatusResponse;
import cn.explink.b2c.homegobj.xmldto.Orders;
import cn.explink.b2c.homegobj.xmldto.ResponseOrder;
import cn.explink.b2c.homegobj.xmldto.ResponseOrders;
import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.RestHttpServiceHanlder;
import cn.explink.util.MD5.MD5Util;

@Service
public class HomegobjService {

	private Logger logger = LoggerFactory.getLogger(HomegobjService.class);

	@Autowired
	private B2cTools b2ctools;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	B2CDataDAO b2cDataDAO;

	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	// 获取配置信息
	public Homegobj getHomegobj(int key) {
		Homegobj lt = null;
		String objectMethod = b2ctools.getObjectMethod(key).getJoint_property();
		if (objectMethod != null) {
			JSONObject jsonObj = JSONObject.fromObject(objectMethod);
			lt = (Homegobj) JSONObject.toBean(jsonObj, Homegobj.class);
		} else {
			lt = new Homegobj();
		}
		return lt;
	}

	public String getFlowEnum(long flowordertype, long deliverystate) {

		for (HomegobjTrackEnum em : HomegobjTrackEnum.values()) {
			if (em.getFlowordertype() != FlowOrderTypeEnum.YiShenHe.getValue()) {
				if (flowordertype == em.getFlowordertype()) {
					return em.getState();
				}
			}
		}

		// 滞留
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return HomegobjTrackEnum.YiChang1.getState();
		}

		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenHuanChengGong
						.getValue())) {
			return HomegobjTrackEnum.QianShou.getState();
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue() || deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverystate == DeliveryStateEnum.ShangMenJuTui
						.getValue())) {
			return HomegobjTrackEnum.JuShou1.getState();
		}

		return null;

	}

	/**
	 * 反馈[家有购物BJ]订单信息
	 */
	public void feedback_status() {

		if (!b2ctools.isB2cOpen(B2cEnum.HomegoBJ.getKey())) {
			logger.info("未开0家有购物BJ0的对接!");
			return;
		}
		Homegobj wx = getHomegobj(B2cEnum.HomegoBJ.getKey());
		sendCwbStatus_To_wx(wx, FlowOrderTypeEnum.FenZhanLingHuo.getValue());

		sendCwbStatus_To_wx(wx, FlowOrderTypeEnum.YiShenHe.getValue());

	}

	/**
	 * 状态反馈接口开始
	 * 
	 * @param smile
	 */
	private void sendCwbStatus_To_wx(Homegobj hg, long flowordertype) {

		try {

			int i = 0;
			while (true) {
				// List<B2CData>
				// datalist=b2cDataDAO.getDataListByFlowStatus(hg.getCustomerid(),hg.getMaxCount());
				List<B2CData> datalist = b2cDataDAO.getDataListByFlowStatus(flowordertype, hg.getCustomerid(), hg.getMaxCount());
				i++;
				if (i > 100) {
					logger.warn("查询0家有购物BJ0状态反馈已经超过100次循环，可能存在程序未知异常,请及时查询并处理!");
					return;
				}

				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有要推送0家有购物BJ0的数据");
					return;
				}
				if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) {
					DealWithBuildXMLAndSending(hg, datalist);
				}
				if (flowordertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
					DealWithBuildXMLAndSending_duanxin(hg, datalist);
				}

			}

		} catch (Exception e) {
			logger.error("发送0家有购物BJ0状态反馈遇到不可预知的异常", e);
		}

	}

	public HomegobjXmlNote getXMLNoteMethod(String jsoncontent) throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(jsoncontent, HomegobjXmlNote.class);
	}

	private void DealWithBuildXMLAndSending(Homegobj wx, List<B2CData> datalist) throws Exception {

		String signXML = buildRequestOrdersString(wx, datalist, null);

		// logger.info("家有购物加密字符串signXML={}",signXML);

		String signed = MD5Util.md5(signXML + wx.getPrivate_key());

		String requestXML = buildRequestOrdersString(wx, datalist, signed);

		logger.info("家有购物订单状态-请求xml={}", requestXML);

		String responseXML = RestHttpServiceHanlder.sendHttptoServerHomego(requestXML, wx.getFeedback_url());

		logger.info("家有购物订单状态-返回xml={}", responseXML);

		OrderStatusResponse orderStatusResponse = HomegoUnmarchal.Unmarchal(responseXML);

		// 报文头显示成功
		if ("00".equals(orderStatusResponse.getResponseHeader().getResp_code())) {

			for (B2CData b2cdata : datalist) {
				int send_b2c_flag = 1;
				b2cDataDAO.updateSendB2cFlagByCwb(b2cdata.getCwb(), send_b2c_flag, orderStatusResponse.getResponseHeader().getResp_msg());
			}
		} else {
			for (B2CData b2cdata : datalist) {
				int send_b2c_flag = 2;
				String err_msg = orderStatusResponse.getResponseHeader().getResp_msg();
				b2cDataDAO.updateSendB2cFlagByCwb(b2cdata.getCwb(), send_b2c_flag, err_msg);
			}
		}
		// 报文头显示失败

	}

	private void DealWithBuildXMLAndSending_duanxin(Homegobj wx, List<B2CData> datalist) throws Exception {

		String signXML = buildDuanXinYuYueXML(wx, datalist, null);

		String signed = MD5Util.md5(signXML + wx.getPrivate_key());

		String requestXML = buildDuanXinYuYueXML(wx, datalist, signed);

		logger.info("家有购物短信预约-请求xml={}", requestXML);

		String responseXML = RestHttpServiceHanlder.sendHttptoServerHomego(requestXML, wx.getFeedback_url());

		logger.info("家有购物短信预约-返回xml={}", responseXML);

		OrderStatusResponse orderStatusResponse = HomegoUnmarchal.Unmarchal(responseXML);

		// 报文头显示成功
		if ("00".equals(orderStatusResponse.getResponseHeader().getResp_code())) {

			for (B2CData b2cdata : datalist) {
				int send_b2c_flag = 1;
				b2cDataDAO.updateSendB2cFlagByCwb(b2cdata.getCwb(), send_b2c_flag, orderStatusResponse.getResponseHeader().getResp_msg());
			}
		} else {
			for (B2CData b2cdata : datalist) {
				int send_b2c_flag = 2;
				String err_msg = orderStatusResponse.getResponseHeader().getResp_msg();
				b2cDataDAO.updateSendB2cFlagByCwb(b2cdata.getCwb(), send_b2c_flag, err_msg);
			}
		}
		// 报文头显示失败

	}

	private String buildRequestOrdersString(Homegobj wx, List<B2CData> datalist, String singed) throws JsonParseException, JsonMappingException, IOException {
		String xml = "";

		if (singed != null) {
			xml += "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		}

		xml += "<Jiayou>" + "<header>" + "<function_id>JIAYOU0002</function_id>" + "<version>1.0</version>" + "<app_id>" + wx.getExpress_id() + "</app_id>" + "<charset>01</charset>"
				+ "<sign_type>02</sign_type>";

		if (singed != null) {
			xml += "<signed>" + singed.toUpperCase() + "</signed>";
		}

		xml += "<data_type>01</data_type>" + "<request_time>" + DateTimeUtil.getNowTime("yyyyMMddHHmmss") + "</request_time>" + "</header>" + "<body>" + "<orders>";

		for (B2CData data : datalist) {
			String jsoncontent = data.getJsoncontent();
			HomegobjXmlNote note = getXMLNoteMethod(jsoncontent);
			xml += "<order>" + "<invc_id>" + note.getInvc_id() + "</invc_id>" + "<ord_id>" + note.getOrd_id() + "</ord_id>" + "<ord_stat>" + note.getOrd_stat() + "</ord_stat>" + "<stat_time>"
					+ note.getStat_time() + "</stat_time>" + "<remark>" + note.getRemark() + "</remark>" + "</order>";
		}
		xml += "</orders>" + "</body>" + "</Jiayou>";

		return xml;
	}

	private String buildDuanXinYuYueXML(Homegobj wx, List<B2CData> datalist, String singed) throws JsonParseException, JsonMappingException, IOException {
		String xml = "";

		if (singed != null) {
			xml += "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
		}

		xml += "<Jiayou>" + "<header>" + "<function_id>JIAYOU0004</function_id>" + "<version>1.0</version>" + "<app_id>" + wx.getExpress_id() + "</app_id>" + "<charset>01</charset>"
				+ "<sign_type>02</sign_type>";

		if (singed != null) {
			xml += "<signed>" + singed.toUpperCase() + "</signed>";
		}

		xml += "<data_type>01</data_type>" + "<request_time>" + DateTimeUtil.getNowTime("yyyyMMddHHmmss") + "</request_time>" + "</header>" + "<body>" + "<orders>";

		for (B2CData data : datalist) {
			String jsoncontent = data.getJsoncontent();
			HomegobjXmlNote note = getXMLNoteMethod(jsoncontent);
			xml += "<order>" + "<invc_id>" + note.getInvc_id() + "</invc_id>" + "<ord_id>" + note.getOrd_id() + "</ord_id>" + "<delivery_phone>" + note.getDelivery_phone() + "</delivery_phone>"
					+ "<delivery_name>" + note.getDelivery_name() + "</delivery_name>" + "<delivery_time>" + note.getDelivery_time() + "</delivery_time>" + "<delivery_id>" + note.getDelivery_id()
					+ "</delivery_id>" + "<remark_1>" + note.getRemark() + "</remark_1>" + "<remark_2></remark_2>" + "</order>";
		}
		xml += "</orders>" + "</body>" + "</Jiayou>";

		return xml;
	}

}
