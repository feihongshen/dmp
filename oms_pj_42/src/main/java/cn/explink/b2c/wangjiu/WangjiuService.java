package cn.explink.b2c.wangjiu;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

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
import cn.explink.b2c.tools.FlowFromJMSB2cService;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.wangjiu.xmldto.BatchQueryRequest;
import cn.explink.b2c.wangjiu.xmldto.QueryOrder;
import cn.explink.b2c.wangjiu.xmldto.WangjiuUnmarchal;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.RestHttpServiceHanlder;

@Service
public class WangjiuService {
	private Logger logger = LoggerFactory.getLogger(WangjiuService.class);
	@Autowired
	private GetDmpDAO getdmpDAO;
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	B2CDataDAO b2cDataDAO;
	@Autowired
	FlowFromJMSB2cService flowFromJMSB2cService;
	@Autowired
	WangjiuDAO wangjiuDAO;

	public String getFlowEnum(long flowordertype, long deliverystate) {
		if (flowordertype != FlowOrderTypeEnum.YiShenHe.getValue()) {
			for (WangjiuFlowEnum TEnum : WangjiuFlowEnum.values()) {
				if (flowordertype == TEnum.getFlowordertype()) {
					return TEnum.getState();
				}
			}
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenHuanChengGong
						.getValue())) {
			return WangjiuFlowEnum.QianShou.getState();
		}

		return null;

	}

	public String getTrackEnum(long flowordertype, long deliverystate) {
		if (flowordertype != FlowOrderTypeEnum.YiShenHe.getValue()) {
			for (WangjiuTrackEnum TEnum : WangjiuTrackEnum.values()) {
				if (flowordertype == TEnum.getFlowordertype()) {
					return TEnum.getState();
				}
			}
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenHuanChengGong
						.getValue())) {
			return WangjiuTrackEnum.SIGNED.getState();
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue()) {
			return WangjiuTrackEnum.FAILED.getState();
		}

		return null;

	}

	public Wangjiu getWangjiu(int key) {
		Wangjiu smile = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			smile = (Wangjiu) JSONObject.toBean(jsonObj, Wangjiu.class);
		} else {
			smile = new Wangjiu();
		}
		return smile;
	}

	/**
	 * 获取网酒网配置信息的接口
	 * 
	 * @param key
	 * @return
	 */
	private String getObjectMethod(int key) {
		try {
			JointEntity obj = getdmpDAO.getJointEntity(key);
			return obj.getJoint_property();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 反馈[网酒网]订单信息
	 */
	public void feedback_status() {

		Wangjiu dms = getWangjiu(B2cEnum.Wangjiu.getKey());
		if (!b2ctools.isB2cOpen(B2cEnum.Wangjiu.getKey())) {
			logger.info("未开[网酒网]的对接!");
			return;
		}

		sendCwbStatus_To_dms(dms);

	}

	/**
	 * 状态反馈接口开始
	 * 
	 * @param wj
	 */
	private void sendCwbStatus_To_dms(Wangjiu wj) {
		try {

			int i = 0;
			while (true) {
				List<B2CData> datalist = b2cDataDAO.getDataListByFlowStatus(wj.getCustomerid(), wj.getMaxCount());
				i++;
				if (i > 100) {
					logger.warn("网酒网状态反馈已经超过100次循环，可能存在程序未知异常,请及时查询并处理");
					return;
				}

				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有要推送[网酒网]的数据");
					return;
				}
				DealWithBuildXMLAndSending(wj, datalist);

			}

		} catch (Exception e) {
			logger.error("推送网酒网系统发生未知异常", e);
		}

	}

	private void DealWithBuildXMLAndSending(Wangjiu wj, List<B2CData> datalist) throws Exception {
		for (B2CData data : datalist) {

			String jsoncontent = data.getJsoncontent();

			WangjiuXmlNote note = JacksonMapper.getInstance().readValue(jsoncontent, WangjiuXmlNote.class);

			String xml = "<StatusNotice>" + "<customerCode>" + wj.getParternID() + "</customerCode>" + "<mailNo>" + data.getCwb() + "</mailNo>" + "<infoContent>" + note.getInfoContent()
					+ "</infoContent>" + "<name>" + note.getName() + "</name>" + "<acceptTime>" + note.getAcceptTime() + "</acceptTime>" + "</StatusNotice>";

			String localSign = WangjiuConfig.base64Md5Result(xml, wj.getParternID(), wj.getPrivate_key());

			String urlencodeXml = URLEncoder.encode(xml, "UTF-8");
			String data_digest = URLEncoder.encode(localSign, "UTF-8");

			Map<String, String> params = new HashMap<String, String>();
			params.put("logistics_interface", urlencodeXml);
			params.put("data_digest", data_digest);

			String responseXml = RestHttpServiceHanlder.sendHttptoServer(params, wj.getFeedbackUrl());

			logger.info("当前网酒网返回data={},xml={}", data.getCwb(), responseXml);

			Map<String, Object> reponseMap = WangjiuConfig.analyzXmlWangjiu(responseXml);

			int send_b2c_flag = reponseMap.get("success").toString().equals("true") ? 1 : 2;
			String remark = reponseMap.get("reason").toString();

			b2cDataDAO.updateB2cIdSQLResponseStatus(data.getB2cid(), send_b2c_flag, remark);

		}

	}

	// 获取tmall XML Note
	public WangjiuXmlNote getSmileXMLNoteMethod(String jsoncontent) {
		try {
			return new ObjectMapper().readValue(jsoncontent, WangjiuXmlNote.class);
		} catch (Exception e) {
			logger.error("获取SmileXMLNote异常！jsoncontent:" + jsoncontent + e);
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		String logistics_interface = "<BatchQueryRequest><mailno>10002</mailno></BatchQueryRequest>";
		String private_key = "95A6870879691F9E9812F0E08B997384";
		String localSign = WangjiuConfig.base64Md5Result(logistics_interface, "tengxunda", private_key);

		String sign = URLDecoder.decode("vxvp92yQr2a+UpkXMXdEww==", "UTF-8");

		System.out.println(sign);
	}

	/**
	 * 请求接口开始
	 * 
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */

	public String requestCwbSearchInterface(String logistics_interface, String data_digest, Wangjiu wj) throws Exception {

		try {

			String xml = URLDecoder.decode(logistics_interface, "UTF-8").equals(logistics_interface) ? logistics_interface : logistics_interface;
			String sign = URLDecoder.decode(data_digest, "UTF-8").equals(data_digest) ? data_digest : data_digest;

			String localSign = WangjiuConfig.base64Md5Result(xml, wj.getParternID(), wj.getPrivate_key());

			if (!localSign.equalsIgnoreCase(sign)) {
				return "签名验证失败";
			}

			BatchQueryRequest order = WangjiuUnmarchal.Unmarchal(xml);

			String respXml = "<BatchQueryResponse>" + "<orders>";

			List<WangjiuEntity> datalist = wangjiuDAO.getDataByCwb(order.getMailNo());
			if (datalist == null || datalist.size() == 0) {
				return "未检索到数据";
			}
			respXml += "<order>" + "<mailNo>" + order.getMailNo() + "</mailNo>" + "<orderStatus>" + datalist.get(datalist.size() - 1).getOrderStatus() + "</orderStatus>" + "<steps>"
					+ getStepsContent(datalist) + "</steps>" + "</order>";

			respXml += "</orders>";
			respXml += "</BatchQueryResponse>";

			return respXml;

		} catch (Exception e) {

			logger.error("处理[网酒网]查询请求发生未知异常", e);
			return "处理异常" + e.getMessage();
		}

	}

	private String getStepsContent(List<WangjiuEntity> datalist) {
		String respXml = "";
		for (WangjiuEntity entity : datalist) {
			respXml += "<step>" + "<acceptTime>" + entity.getAcceptTime() + "</acceptTime>" + "<acceptAddress>" + entity.getAcceptAddress() + "</acceptAddress>" + "<name>" + entity.getName()
					+ "</name>" + "<status>" + entity.getStatus() + "</status>" + "<remark>" + entity.getRemark() + "</remark>" + "</step>";
		}
		return respXml;
	}

}
