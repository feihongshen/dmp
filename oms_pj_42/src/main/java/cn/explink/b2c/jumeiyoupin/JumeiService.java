package cn.explink.b2c.jumeiyoupin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.FlowFromJMSB2cService;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.domain.B2CData;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class JumeiService {
	private Logger logger = LoggerFactory.getLogger(JumeiService.class);
	@Autowired
	GetDmpDAO getdmpDAO;
	@Autowired
	CwbDAO cwbDAO;

	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	B2CDataDAO b2cDataDAO;
	@Autowired
	FlowFromJMSB2cService flowFromJMSB2cService;

	/**
	 * 获取聚美优品配置信息的接口
	 * 
	 * @param key
	 * @return
	 */
	public String getObjectMethod(int key) {
		JointEntity obj = new JointEntity();
		String propertity = "";
		try {
			obj = getdmpDAO.getJointEntity(key);
			propertity = obj.getJoint_property();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return propertity;
	}

	public JuMeiYouPin getJuMeiYouPinSettingMethod(int key) {
		JuMeiYouPin juMeiYouPin = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			juMeiYouPin = (JuMeiYouPin) JSONObject.toBean(jsonObj, JuMeiYouPin.class);
		} else {
			juMeiYouPin = new JuMeiYouPin();
		}

		return juMeiYouPin;
	}

	public int getStateForPos(int key) {
		JointEntity obj = null;
		int state = 0;
		try {
			obj = getdmpDAO.getJointEntity(key);
			state = obj.getState();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return state;
	}

	private boolean isJuMeiOpen(int key) {
		try {
			JointEntity obj = getdmpDAO.getJointEntity(key);
			return obj.getState() != 0;
		} catch (Exception e) {
			logger.warn("error while getting dangdang status with key {}, will defualt false", key);
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 出库派送，配送结果反馈 接口
	 */
	public long feedback_status() {

		JuMeiYouPin juMeiYouPin = getJuMeiYouPinSettingMethod(B2cEnum.JuMeiYouPin.getKey()); // 获取配置信息
		if (!isJuMeiOpen(B2cEnum.JuMeiYouPin.getKey())) {
			logger.info("未开启[聚美优品]对接！");
			return -1;
		}

		sendCwbStatus_To_Jumei(juMeiYouPin);

		String nowdateHours = DateTimeUtil.getNowTime("HH");
		int hours = Integer.valueOf(nowdateHours);
		if (hours % 2 == 0) { // 每隔2的倍数来执行
			updateByFindCanNotCwbs(juMeiYouPin.getCustomerids());
		}

		return 1;
	}

	public JuMeiYouPin_json getJumeiJson(String jsoncontent) throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(jsoncontent, JuMeiYouPin_json.class);
	}

	/**
	 * 状态反馈接口开始
	 * 
	 * @param
	 */
	private void sendCwbStatus_To_Jumei(JuMeiYouPin jumei) {

		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");

			int i = 0;
			while (true) {
				List<B2CData> datalist = b2cDataDAO.getDataListByFlowStatus(jumei.getCustomerids(), jumei.getCount());
				i++;
				if (i > 50) {
					String warning = "查询0聚美0状态反馈已经超过50次循环，可能存在程序未知异常,请及时查询并处理!";
					logger.warn(warning);
					return;
				}

				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有要推送0聚美0的数据");
					return;
				}
				int k = 0;
				StringBuffer xml = new StringBuffer();
				for (B2CData data : datalist) {
					k++;
					xml.setLength(0);

					xml = getSendJumeiXML(jumei, k, xml, data);

					String unixstamp = df.format(new Date());
					String md5data = getMd5Result(jumei, data, unixstamp);

					String reponseXml = posthttpdata_jumei(jumei.getTuisong_url(), xml.toString(), data.getCwb(), unixstamp, md5data);

					logger.info("聚美返回XML={},cwb={}", reponseXml, data.getCwb());
					Map<String, Object> respMap = Analyz_XmlDocByJumei(reponseXml);

					int result = "true".equalsIgnoreCase(respMap.get("success").toString()) ? 1 : 2;
					String reason = respMap.get("reason") == null ? "" : respMap.get("reason").toString();

					b2cDataDAO.updateB2cIdSQLResponseStatus(data.getB2cid(), result, reason);

				}

			}

		} catch (Exception e) {
			logger.error("发送0聚美0状态反馈遇到不可预知的异常", e);
		}

	}

	private String getMd5Result(JuMeiYouPin jumei, B2CData data, String unixstamp) {
		String md5data = MD5Util.md5(data.getCwb() + unixstamp);
		return MD5Util.md5(md5data + jumei.getPrivate_key());
	}

	private StringBuffer getSendJumeiXML(JuMeiYouPin jumei, int k, StringBuffer msg, B2CData data) throws JsonParseException, JsonMappingException, IOException {
		JuMeiYouPin_json jumeiJson = getJumeiJson(data.getJsoncontent());
		msg.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		msg.append("<result>");
		msg.append("<company>" + jumei.getExpress_id() + "</company>");

		msg.append("<row resultcount=\"" + k + "\">");
		msg.append("<cwb>" + data.getCwb() + "</cwb>");
		msg.append("<trackdatetime>" + jumeiJson.getTrackdatetime() + "</trackdatetime>");
		msg.append("<trackevent>" + jumeiJson.getTrackevent() + "</trackevent>");
		msg.append("<trackstatus>" + jumeiJson.getTrackstatus() + "</trackstatus>");
		msg.append("</row>");
		msg.append("</result>");

		return msg;
	}

	public String posthttpdata_jumei(String url, String trackInfo, String cwb, String unixstamp, String md5data) throws Exception {

		// 把加密方式拼接到url后面
		String paraStrs = "?cwb=" + cwb + "&unixstamp=" + unixstamp + "&sign=" + md5data;

		logger.info("当前推送聚美xml={},params={}", trackInfo, paraStrs);

		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url + paraStrs);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(40000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(40000);
		// 填入各个表单域的值
		NameValuePair[] data = { new NameValuePair("trackInfo", trackInfo) }; // 原始信息

		// 将表单的值放入postMethod中
		postMethod.setRequestBody(data);

		httpClient.executeMethod(postMethod); // post数据
		String str = postMethod.getResponseBodyAsString();
		postMethod.releaseConnection();
		return str;
	}

	/**
	 * 可解析list
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> Analyz_XmlDocByJumei(String fileName) throws Exception {
		// File inputXml = new File(fileName);
		InputStream iStream = new ByteArrayInputStream(fileName.getBytes());
		SAXReader saxReader = new SAXReader();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Reader r = new InputStreamReader(iStream);
		Document document = saxReader.read(r);
		Element employees = document.getRootElement();
		for (Iterator i = employees.elementIterator(); i.hasNext();) {
			Element employee = (Element) i.next();
			returnMap.put(employee.getName(), employee.getText());
			List<Map<String, Object>> jarry = new ArrayList<Map<String, Object>>();
			for (Iterator j = employee.elementIterator(); j.hasNext();) {
				Element node = (Element) j.next();
				returnMap.put(node.getName(), node.getText());
			}
		}
		return returnMap;
	}

	/**
	 * 定时重发 找不到的订单
	 */
	private void updateByFindCanNotCwbs(String customerids) {
		try {
			String time = DateTimeUtil.getDateBefore(1);
			String keyword = "R5"; // 根据关键词删除
			b2cDataDAO.updateKeyWordByVipShop(customerids, time, keyword);

		} catch (Exception e) {
			logger.error("重置聚美异常", e);
			e.printStackTrace();
		}
	}

}
