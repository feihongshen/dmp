package cn.explink.b2c.hzabc;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
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
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.Mail;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.MD5.MD5Util;

@Service
public class HangZhouABCService {
	private Logger logger = LoggerFactory.getLogger(HangZhouABCService.class);

	@Autowired
	private B2cTools b2ctools;
	@Autowired
	private B2CDataDAO b2cDataDAO;

	/**
	 * 反馈给杭州ABC订单信息
	 */
	public long feedback_status(int key) {
		long calcCount = 0;
		HangZhouABC gzabc = getHangZhouABCSettingMethod(key);
		if (!b2ctools.isB2cOpen(key)) {
			logger.info("未开杭州ABC的对接!key={}", key);
			return -1;
		}
		logger.info("========杭州ABC状态反馈任务调度开启==========");
		calcCount += sendCwbStatusToABC(gzabc, FlowOrderTypeEnum.RuKu.getValue()); // 入库
		calcCount += sendCwbStatusToABC(gzabc, FlowOrderTypeEnum.ZhongZhuanChuKuSaoMiao.getValue()); // 中转中
		calcCount += sendCwbStatusToABC(gzabc, FlowOrderTypeEnum.FenZhanLingHuo.getValue()); // 配送中
		calcCount += sendCwbStatusToABC(gzabc, FlowOrderTypeEnum.ChuKuSaoMiao.getValue()); // 库房出库
		calcCount += sendCwbStatusToABC(gzabc, FlowOrderTypeEnum.TuiHuoChuKuSaoMiao.getValue()); // 退货出库扫描
		calcCount += sendCwbStatusToABC(gzabc, FlowOrderTypeEnum.TuiHuoZhanRuKu.getValue()); // 退货站入库

		calcCount += sendCwbStatusToABC(gzabc, FlowOrderTypeEnum.YiShenHe.getValue()); // 配送结果
		calcCount += sendCwbStatusToABC(gzabc, FlowOrderTypeEnum.TuiGongYingShangChuKu.getValue()); // 退供应商出库

		logger.info("=========杭州ABC状态反馈任务调度结束==========");
		return calcCount;
	}

	/**
	 * 状态反馈接口开始
	 * 
	 * @param smile
	 *            每次最大查询不超过50条
	 */
	private long sendCwbStatusToABC(HangZhouABC gzabc, long flowordertype) {
		long calcCount = 0;
		try {

			int i = 0;
			while (true) {
				List<B2CData> yeMaiJiuDataList = b2cDataDAO.getDataListByFlowStatus(flowordertype, gzabc.getCustomerids(), gzabc.getMaxCount());
				i++;
				if (i > 100) {
					String warning = "查询0杭州ABC0状态反馈已经超过100次循环，每次50条，可能存在程序未知异常,请及时查询并处理!";
					logger.warn(warning);
					return 0;
				}

				if (yeMaiJiuDataList == null || yeMaiJiuDataList.size() == 0) {
					logger.info("当前没有要推送0杭州ABC0的数据");
					return 0;
				}
				DealWithBuildXMLAndSending(gzabc, yeMaiJiuDataList);
				calcCount += yeMaiJiuDataList.size();

			}

		} catch (Exception e) {
			String errorinfo = "发送0杭州ABC0状态反馈遇到不可预知的异常" + e.getMessage();
			logger.error(errorinfo, e);

		}
		return calcCount;
	}

	/**
	 * 可修改为每次查1000条，每次推送10条
	 * 
	 * @param gzabc
	 * @param abcdataList
	 * @throws Exception
	 */
	private void DealWithBuildXMLAndSending(HangZhouABC gzabc, List<B2CData> abcdataList) throws Exception {

		for (B2CData data : abcdataList) {
			long b2cid = data.getB2cid();
			String jsoncontent = data.getJsoncontent();
			HangZhouXMLNote note = getHangZhouABCXMLNoteMethod(jsoncontent);
			StringBuffer sub = buildSendABCXML(gzabc, note);

			String response_xml = postHTTPData_toHangZhouABC(sub.toString(), gzabc.getFeedback_url(), gzabc.getLogisticProviderID(), gzabc.getPrivate_key());
			logger.info("状态反馈0杭州ABC0[返回信息]-XML={}", response_xml);

			dealWithFeedBackResponse(b2cid, response_xml);
		}

	}

	private void dealWithFeedBackResponse(long b2cid, String response_xml) throws Exception {
		String Success = "";
		String Remark = "";
		if (response_xml.contains(",不存在的供货商")) {
			Success = "false";
			Remark = response_xml;
		} else {
			Map<String, Object> respMap = Analyz_XmlDocByABC(response_xml);
			if (respMap == null || respMap.size() == 0) {
				logger.warn("请求0杭州ABC0解析xml为空，跳出循环,throw Exception,xml={}", response_xml);
				throw new RuntimeException("请求0杭州ABC0解析xml为空，跳出循环,throw ");
			}
			Success = respMap.get("Success").toString();
			Remark = respMap.get("Remark") != null && !"".equals(respMap.get("Remark").toString()) ? respMap.get("Remark").toString() : "";
		}

		int send_b2c_flag = 1;

		if (!Success.equalsIgnoreCase("True")) {
			send_b2c_flag = 2;
		}

		b2cDataDAO.updateB2cIdSQLResponseStatus(b2cid, send_b2c_flag, Remark);
	}

	private StringBuffer buildSendABCXML(HangZhouABC gzabc, HangZhouXMLNote note) {
		StringBuffer sub = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>");
		sub.append("<UpdateInfo>");
		sub.append("<LogisticProviderID>" + gzabc.getLogisticProviderID() + "</LogisticProviderID>");
		sub.append("<OrderStates>");
		sub.append("<OrderState>");
		sub.append("<WaybillNo>" + note.getWaybillNo() + "</WaybillNo>");
		sub.append("<State>" + note.getStatus() + "</State>");
		sub.append("<StateTime>" + note.getStateTime() + "</StateTime>");
		sub.append("<OperName>" + note.getOperName() + "</OperName>");
		sub.append("<OperateTime>" + note.getOperateTime() + "</OperateTime>");
		sub.append("<OperatorUnit>" + note.getOperatorUnit() + "</OperatorUnit>");
		sub.append("<DeliveryMan>" + note.getDeliveryMan() + "</DeliveryMan>");
		sub.append("<DeliveryMobile>" + note.getDeliveryMobile() + "</DeliveryMobile>");
		sub.append("<Reason>" + note.getReason() + "</Reason>");
		sub.append("<Remark>" + note.getRemark() + "</Remark>");
		sub.append("<ScanId>" + note.getScanId() + "</ScanId>");
		sub.append("<PreSiteName>" + note.getPreSiteName() + "</PreSiteName>");
		sub.append("<NextSiteName>" + note.getNextSiteName() + "</NextSiteName>");
		sub.append("<ActualMny>" + note.getActualMny() + "</ActualMny>");
		sub.append("</OrderState>");
		sub.append("</OrderStates>");
		sub.append("</UpdateInfo>");
		return sub;
	}

	// 获取配置信息
	public HangZhouABC getHangZhouABCSettingMethod(int key) {
		HangZhouABC gzabc = null;
		String objectMethod = b2ctools.getObjectMethod(key).getJoint_property();
		if (objectMethod != null) {
			JSONObject jsonObj = JSONObject.fromObject(objectMethod);
			gzabc = (HangZhouABC) JSONObject.toBean(jsonObj, HangZhouABC.class);
		} else {
			gzabc = new HangZhouABC();
		}
		return gzabc;
	}

	// 获取 XML Note
	public HangZhouXMLNote getTmallXMLNoteMethod(String jsoncontent) {
		try {
			JSONObject jsonObj = JSONObject.fromObject(jsoncontent);
			return (HangZhouXMLNote) JSONObject.toBean(jsonObj, HangZhouXMLNote.class);
		} catch (Exception e) {
			logger.error("获取HangZhouXMLNote异常！jsoncontent:" + jsoncontent + e);
			return null;
		}
	}

	public String getHangZhouABCFlowEnum(long flowordertype, long deliverystate) {
		if (flowordertype != FlowOrderTypeEnum.YiShenHe.getValue()) {
			for (HangZhouFlowEnum TEnum : HangZhouFlowEnum.values()) {
				if (TEnum.getFlag() == 0 && flowordertype == TEnum.getFlowordertype()) {
					return TEnum.getAbc_state();
				}
			}
		}

		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			return HangZhouFlowEnum.SC03.getAbc_state();
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return HangZhouFlowEnum.SC04.getAbc_state();
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
			return HangZhouFlowEnum.SC07.getAbc_state();
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue()) {
			return HangZhouFlowEnum.SC05.getAbc_state();
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
			return HangZhouFlowEnum.SC14.getAbc_state();
		}

		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
			return HangZhouFlowEnum.SC13.getAbc_state();
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.ShangMenJuTui.getValue()) {
			return HangZhouFlowEnum.SC10.getAbc_state();
		}

		return null;

	}

	public static void main(String[] args) {
		long flowordertype = 1;
		long deliverystate = 0;

		String str = new HangZhouABCService().getHangZhouABCFlowEnum(flowordertype, deliverystate);
		System.out.println(str);

	}

	public HangZhouXMLNote getHangZhouABCXMLNoteMethod(String jsoncontent) throws JsonParseException, JsonMappingException, IOException {
		return new ObjectMapper().readValue(jsoncontent, HangZhouXMLNote.class);
	}

	public String postHTTPData_toHangZhouABC(String xml, String url, String expressCode, String private_key) throws Exception {

		String checkdata = MD5Util.md5(xml + private_key);

		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(20000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(20000);
		// 填入各个表单域的值
		NameValuePair[] data = { new NameValuePair("sShippedCode", URLEncoder.encode(expressCode, "UTF-8")), new NameValuePair("logicdata", URLEncoder.encode(xml, "UTF-8")),
				new NameValuePair("checkdata", URLEncoder.encode(checkdata, "UTF-8")), };
		logger.info("推送0杭州ABC0,sShippedCode={},logicdata={},checkdata=" + checkdata, expressCode, xml);

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
	public Map<String, Object> Analyz_XmlDocByABC(String fileName) throws Exception {
		// File inputXml = new File(fileName);
		InputStream iStream = new ByteArrayInputStream(fileName.getBytes("UTF-8"));
		SAXReader saxReader = new SAXReader();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Reader r = new InputStreamReader(iStream, "UTF-8");
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

}
