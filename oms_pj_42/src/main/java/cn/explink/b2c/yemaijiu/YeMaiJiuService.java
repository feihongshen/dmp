package cn.explink.b2c.yemaijiu;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
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
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.ExceptionTrace;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.Mail;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.MD5.MD5Util;

@Service
public class YeMaiJiuService {

	@Autowired
	private GetDmpDAO getdmpDAO;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	B2CDataDAO b2cDataDAO;

	private Logger logger = LoggerFactory.getLogger(YeMaiJiuService.class);

	public YeMaiJiu getYeMaiJiuSettingMethod(int key) {
		YeMaiJiu yemaijiu = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			yemaijiu = (YeMaiJiu) JSONObject.toBean(jsonObj, YeMaiJiu.class);
		} else {
			yemaijiu = new YeMaiJiu();
		}
		return yemaijiu;
	}

	/**
	 * 获取也买酒配置信息的接口
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
	 * 获取yemaijiu的反馈状态
	 * 
	 * @return
	 */
	public YeMaiJiuFlowEnum getFlowStateByYeMaiJiu(long delivery_state) {
		for (YeMaiJiuFlowEnum e : YeMaiJiuFlowEnum.values()) {
			if (e.getDelivery_state() == delivery_state) {
				return e;
			}
		}
		return null;
	}

	/**
	 * 反馈[也买酒]订单信息
	 */
	public long feedback_status() {

		YeMaiJiu yemaijiu = getYeMaiJiuSettingMethod(B2cEnum.YeMaiJiu.getKey());
		if (!b2ctools.isB2cOpen(B2cEnum.YeMaiJiu.getKey())) {
			logger.info("未开0也买酒0的对接!");
			return -1;
		}

		return sendCwbStatus_To_YeMaiJiu(yemaijiu, FlowOrderTypeEnum.YiShenHe.getValue());

	}

	/**
	 * 状态反馈接口开始
	 * 
	 * @param smile
	 */
	private long sendCwbStatus_To_YeMaiJiu(YeMaiJiu yemaijiu, long flowordertype) {
		long calcCount = 0;
		try {

			int i = 0;
			while (true) {
				List<B2CData> yeMaiJiuDataList = b2cDataDAO.getDataListByFlowStatus(flowordertype, yemaijiu.getCustomerid(), yemaijiu.getMaxCount());
				i++;
				if (i > 100) {
					String warning = "查询0也买酒0状态反馈已经超过100次循环，可能存在程序未知异常,请及时查询并处理!";
					logger.warn(warning);
					return 0;
				}

				if (yeMaiJiuDataList == null || yeMaiJiuDataList.size() == 0) {
					logger.info("当前没有要推送0也买酒0的数据");
					return 0;
				}
				DealWithBuildXMLAndSending(yemaijiu, yeMaiJiuDataList);
				calcCount += yeMaiJiuDataList.size();
			}

		} catch (Exception e) {
			String errorinfo = "发送0也买酒0状态反馈遇到不可预知的异常";
			logger.error(errorinfo, e);
		}

		return calcCount;
	}

	public YeMaiJiuXMLNote getYeMaiJiuXMLNoteMethod(String jsoncontent) throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(jsoncontent, YeMaiJiuXMLNote.class);
	}

	private void DealWithBuildXMLAndSending(YeMaiJiu yemaijiu, List<B2CData> yemaijiuDataList) throws Exception {
		StringBuffer sub = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>");
		sub.append("<SignBatchUpdateInfo>");
		sub.append("<list>");
		for (B2CData data : yemaijiuDataList) {
			String jsoncontent = data.getJsoncontent();
			YeMaiJiuXMLNote note = getYeMaiJiuXMLNoteMethod(jsoncontent);
			sub.append("<SignUpdateInfo>");
			sub.append("<boxNo>" + note.getBoxNo() + "</boxNo>");
			sub.append("<sendNo>" + note.getSendNo() + "</sendNo>");
			sub.append("<meno>" + note.getMeno() + "</meno>");
			sub.append("<signTime>" + note.getSigntime() + "</signTime>");
			sub.append("<signUser>" + note.getSignUser() + "</signUser>");
			sub.append("<status>" + note.getStatus() + "</status>");
			sub.append("</SignUpdateInfo>");
		}
		sub.append("</list>");
		sub.append("</SignBatchUpdateInfo>");

		String response_xml = postHTTPData_toYeMaiJiu(sub.toString(), yemaijiu.getSend_url(), yemaijiu.getExpressCode(), yemaijiu.getPrivate_key());
		logger.info("状态反馈0也买酒0[返回信息]-XML={}", response_xml);

		List<Map<String, Object>> responseList = Analyz_XmlDocByYeMaiJiu(response_xml);
		if (responseList == null || responseList.size() == 0) {
			logger.warn("请求0也买酒0解析xml为空，跳出循环,throw Exception,xml={}", response_xml);
		}
		for (Map<String, Object> map : responseList) {
			String boxNo = map.get("boxNo").toString();
			String reason = map.get("reason") == null ? "" : map.get("reason").toString();
			String success = map.get("success").toString();

			b2cDataDAO.updateSendB2cFlagByCwb(boxNo, success.equalsIgnoreCase("true") ? 1 : 2, reason);
		}

		// 发送给dmp
		// flowFromJMSB2cService.sendTodmp(b2cids);
	}

	public String postHTTPData_toYeMaiJiu(String xml, String url, String expressCode, String private_key) throws Exception {

		int random = (int) (Math.random() * 9000) + 1000;
		String verifyData = random + MD5Util.md5(random + private_key + xml);

		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(40000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(40000);
		// 填入各个表单域的值
		NameValuePair[] data = { new NameValuePair("expressCode", expressCode), new NameValuePair("xml", xml), new NameValuePair("verifyData", verifyData), };
		logger.info("推送0也买酒0,expressCode={},xml={},verifyData=" + verifyData, expressCode, xml);

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
	public static List<Map<String, Object>> Analyz_XmlDocByYeMaiJiu(String fileName) throws Exception {
		// File inputXml = new File(fileName);
		InputStream iStream = new ByteArrayInputStream(fileName.getBytes("UTF-8"));
		SAXReader saxReader = new SAXReader();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Reader r = new InputStreamReader(iStream, "UTF-8");
		Document document = saxReader.read(r);
		Element employees = document.getRootElement();
		for (Iterator i = employees.elementIterator(); i.hasNext();) {
			Element note1 = (Element) i.next();

			List<Map<String, Object>> jarry = new ArrayList<Map<String, Object>>();
			for (Iterator j = note1.elementIterator(); j.hasNext();) {
				Element note2 = (Element) j.next();

				Map<String, Object> map2 = new HashMap<String, Object>();
				for (Iterator k = note2.elementIterator(); k.hasNext();) {
					Element note3 = (Element) k.next();
					map2.put(note3.getName(), note3.getText());
				}
				jarry.add(map2);
			}
			return jarry;
		}
		return null;

	}

}
