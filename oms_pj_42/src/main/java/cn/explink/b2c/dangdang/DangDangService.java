package cn.explink.b2c.dangdang;

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
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.FlowFromJMSB2cService;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.b2cmonitor.B2cSendMointorService;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class DangDangService {
	private Logger logger = LoggerFactory.getLogger(DangDangService.class);
	@Autowired
	private GetDmpDAO getdmpDAO;
	@Autowired
	private B2CDataDAO b2cDataDAO;
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	B2cSendMointorService b2cSendMonitorService;
	@Autowired
	FlowFromJMSB2cService flowFromJMSB2cService;

	public DangDang getDangDangSettingMethod(int key) {
		DangDang dangdang = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			dangdang = (DangDang) JSONObject.toBean(jsonObj, DangDang.class);
		} else {
			dangdang = new DangDang();
		}

		return dangdang;
	}

	/**
	 * 入库，出库派送，配送结果反馈 接口
	 */
	public long feedback_status() {
		long clacCount = 0;
		DangDang dangdang = getDangDangSettingMethod(B2cEnum.DangDang.getKey()); // 获取配置信息
		if (!isDangDangOpen(B2cEnum.DangDang.getKey())) {
			logger.error("未开当当的对接!");
			return -1;
		}
		// /状态反馈
		clacCount += FeedbackXMLByFlowOrderStatus_selectOneTable(dangdang, FlowOrderTypeEnum.RuKu.getValue(), dangdang.getRuku_url());
		clacCount += FeedbackXMLByFlowOrderStatus_selectOneTable(dangdang, FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(), dangdang.getRuku_url());
		clacCount += FeedbackXMLByFlowOrderStatus_selectOneTable(dangdang, FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue(), dangdang.getRuku_url());
		clacCount += FeedbackXMLByFlowOrderStatus_selectOneTable(dangdang, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), dangdang.getChukuPaiSong_url());
		// 已反馈并审核 YiShenHe
		clacCount += FeedbackXMLByFlowOrderStatus_selectOneTable(dangdang, FlowOrderTypeEnum.YiShenHe.getValue(), dangdang.getDeliverystate_url());

		return clacCount;

	}

	/**
	 * 反馈给当当跟踪日志 接收JMS状态消息统一反馈给当当
	 */
	public void feedBackToDangDangFlowStatus(DmpOrderFlow orderFlow) {
		DangDang dangdang = getDangDangSettingMethod(B2cEnum.DangDang.getKey()); // 获取配置信息
		FeedbackXMLByTrack(dangdang, orderFlow);
	}

	/**
	 * 获取当当配置信息的接口
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

	private boolean isDangDangOpen(int key) {
		try {
			JointEntity obj = getdmpDAO.getJointEntity(key);
			return obj.getState() != 0;
		} catch (Exception e) {
			logger.warn("error while getting dangdang status with key {}, will defualt false", key);
			e.printStackTrace();
		}
		return false;
	}

	private String getSignTextInfo_selectOneTable(DangDang dangdang, List<B2CData> datalist) {
		String cwbs = getCwbsByList_selectOneTable(datalist);
		if (!"".equals(cwbs)) {
			String sign_str = fillupString(cwbs);
			return sign_str + dangdang.getPrivate_key();
		}
		return "";
	}

	private String getSignTextTrackInfo(DangDang dangdang, String cwbs) {
		if (!"".equals(cwbs)) {
			String sign_str = fillupString(cwbs);
			return sign_str + dangdang.getPrivate_key();
		}
		return "";
	}

	private String fillupString(String sign_str) {
		if (sign_str.length() < 50) {
			sign_str += "12345678901234567890123456789012345678901234567890";
			sign_str = sign_str.substring(0, 50);
		}
		return sign_str;
	}

	private String getCwbsByList_selectOneTable(List<B2CData> datalist) {
		String Cwbs = "";
		if (datalist != null && datalist.size() > 0) {
			for (B2CData o : datalist) {
				Cwbs += o.getCwb() + ",";
			}
		}
		return Cwbs;
	}

	/**
	 * 跟踪日志反馈
	 * 
	 * @param dangdang
	 */
	private void FeedbackXMLByTrack(DangDang dangdang, DmpOrderFlow orderFlow) {

		if (!isDangDangOpen(B2cEnum.DangDang.getKey())) {
			logger.error("当当跟踪日志反馈--未开启当当的对接!");
			return;
		}
		String xmlDOS = "";
		try {
			String requestXML = StringXMLBodyByTrackDangDang_one(dangdang, orderFlow);
			logger.info("[跟踪日志反馈]-状态{}-当前推送当当XML：[{}];", orderFlow.getFlowordertype(), requestXML);
			String sign_text = getSignTextTrackInfo(dangdang, orderFlow.getCwb());
			String responseXML = HTTPClient_Send(dangdang.getTrackinfo_url(), requestXML, sign_text, dangdang);
			xmlDOS = responseXML.trim().replaceAll("\r+\n+ ", "").replaceAll("\n", "").replaceAll("\r", "");
			xmlDOS = xmlDOS.substring(xmlDOS.indexOf("?>") + 2);
		} catch (Exception e) {
			logger.error("推送[当当]跟踪日志反馈接口出现未知异常", e);
		}
		logger.info("[跟踪日志反馈]-状态{}-当前当当返回XML：[{}];", orderFlow.getFlowordertype(), xmlDOS);

	}

	/**
	 * 可解析list
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> parserXmlToJSONObjectByArray(String fileName) throws Exception {
		// File inputXml = new File(fileName);
		InputStream iStream = new ByteArrayInputStream(fileName.getBytes());
		SAXReader saxReader = new SAXReader();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Reader r = new InputStreamReader(iStream, "UTF-8");
		Document document = saxReader.read(r);
		Element employees = document.getRootElement();
		Map<String, Object> jsontotal = new HashMap<String, Object>();
		for (Iterator i = employees.elementIterator(); i.hasNext();) {
			Element employee = (Element) i.next();
			List<Map<String, Object>> jarry = new ArrayList<Map<String, Object>>();
			for (Iterator j = employee.elementIterator(); j.hasNext();) {
				Element node = (Element) j.next();
				Map<String, Object> jsondetail = new HashMap<String, Object>();
				for (Iterator<Element> k = node.elementIterator(); k.hasNext();) {
					Element node_child = (Element) k.next();
					jsondetail.put(node_child.getName(), node_child.getText());
				}
				jarry.add(jsondetail);
			}
			jsontotal.put(employee.getName(), employee.getText());
			returnMap.put("jsontotal", jsontotal);
			if (jarry != null && jarry.size() > 0) {
				returnMap.put("jsonarray", jarry);
			}

		}
		return returnMap;
	}

	/**
	 * 每次生成一次跟踪日志会主动推送一条数据.
	 * 
	 * @param dangdang
	 * @param orderFlow
	 * @return
	 */
	private String StringXMLBodyByTrackDangDang_one(DangDang dangdang, DmpOrderFlow orderFlow) {
		StringBuffer sub = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>");
		sub.append("<inputObject>");
		sub.append("<list>");
		sub.append("<row>");
		sub.append("<order_id>" + orderFlow.getCwb() + "</order_id>");
		sub.append("<trans_log>" + orderFlowDetail.getDetail(orderFlow) + "</trans_log>");
		sub.append("<log_time>" + DateTimeUtil.formatDate(orderFlow.getCredate()) + "</log_time>");
		sub.append("<express_id>" + dangdang.getExpress_id() + "</express_id>");
		sub.append("<operator>" + getdmpDAO.getUserById(orderFlow.getUserid()).getRealname() + "</operator>");
		sub.append("</row>");
		sub.append("</list>");
		sub.append("</inputObject>");
		return sub.toString().replaceAll("null", "");
	}

	public String HTTPClient_Send(String URL, String para_xml, String sign_text, DangDang dangdang) {
		String MD5Data = MD5Util.getMD5String32Bytes(sign_text, dangdang.getPrivate_key());
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(URL); // URL为要请求的地址
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8"); // 设置编码方式

		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(40000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(40000);
		NameValuePair[] data = { new NameValuePair("para_xml", para_xml), new NameValuePair("sign_type", "MD5"), new NameValuePair("sign_str", sign_text), new NameValuePair("sign_result", MD5Data), }; // 请求的参数，这样写（参数名：参数值）

		postMethod.setRequestBody(data); // 将表单的值放入postMethod中
		try {
			httpClient.executeMethod(postMethod); // 执行数据传输的方法。
			return postMethod.getResponseBodyAsString(); // 返回一个响应结果
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
		return "";

	}

	/**
	 * 状态反馈 查询单独的表的数据
	 * 
	 * @param dangdang
	 */
	private long FeedbackXMLByFlowOrderStatus_selectOneTable(DangDang dangdang, int floworderStatus, String flow_url) {
		long calcCount = 0;

		try {

			while (true) {
				List<B2CData> datalist = b2cDataDAO.getDataListByFlowStatus(floworderStatus, dangdang.getCustomerids(), dangdang.getCount());
				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有要推送[当当]的数据,状态:{}", floworderStatus);
					return 0;
				} else {

					String requestXML = StringXMLBodyByStatus_selectOneTable(dangdang, datalist, floworderStatus);
					logger.info("当前状态{},当前推送当当XML：{}", floworderStatus, requestXML);
					if (!requestXML.contains("<row>")) {
						logger.info("当前当当为{}的上一个状态没有推送，return", floworderStatus);
						return 0;
					}
					String b2cids = getB2cIdBySendB2cList(datalist); // 获取b2c的id拼接
																		// 逗号已去掉了
					String xmlDOS = responseXMLMethod(dangdang, flow_url, datalist, requestXML);
					logger.info("当当返回:状态{},xml:{};", floworderStatus, xmlDOS);

					Map<String, Object> retMap = null;
					try {
						retMap = parserXmlToJSONObjectByArray(xmlDOS);
						logger.info("解析当当xml结果:[success];状态{}", floworderStatus);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("解析当当xml结果:[failed];状态" + floworderStatus + ",异常：" + e.getMessage());
						e.printStackTrace();
						return 0;
					}
					try {
						// 修改推送结果状态.
						updateResponseStatus_oneTable(retMap, floworderStatus, b2cids);
						logger.info("处理当当xml返回状态:[success];b2cids={}", b2cids);
						// 发送给dmp
						flowFromJMSB2cService.sendTodmp(b2cids);

						calcCount += datalist.size();

					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("处理当当xml返回状态:[failed];异常：" + e.getMessage(), e);
						e.printStackTrace();
						return 0;
					}

				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return calcCount;

	}

	private String responseXMLMethod(DangDang dangdang, String flow_url, List<B2CData> datalist, String requestXML) {
		String sign_text = getSignTextInfo_selectOneTable(dangdang, datalist);
		String responseXML = HTTPClient_Send(flow_url, requestXML, sign_text, dangdang);

		// 反馈json
		String xmlDOS = responseXML.trim().replaceAll("\r+\n+ ", "").replaceAll("\n", "").replaceAll("\r", "");
		xmlDOS = xmlDOS.substring(xmlDOS.indexOf("?>") + 2);
		return xmlDOS;
	}

	// 根据查询出来要推送给当当的数据 查询出所有的主键id
	private String getB2cIdBySendB2cList(List<B2CData> datalist) {
		String b2cids = "";
		if (datalist != null && datalist.size() > 0) {
			for (B2CData bd : datalist) {
				b2cids += bd.getB2cid() + ",";
			}
			b2cids = b2cids.substring(0, b2cids.length() - 1);
		}
		return b2cids;
	}

	public String getDangDangFlowEnum(long flowordertype) {
		for (DangDangFlowEnum dd : DangDangFlowEnum.values()) {
			if (flowordertype == dd.getFlowordertype()) {
				return dd.getFlowordertype() + "";
			}
		}

		return null;

	}

	// 根据 配送结果 转化 为 当当提供的反馈码
	public String getOrderDeliverStateByStatus(long deliverystate) {
		if (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue()
				|| deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
			return "101";
		} else if (deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return "102";
		} else if (deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue() || deliverystate == DeliveryStateEnum.HuoWuDiuShi.getValue()
				|| deliverystate == DeliveryStateEnum.ShangMenJuTui.getValue() || deliverystate == DeliveryStateEnum.ShangMenJuTui.getValue()) {
			return "103";
		} else {
			return "error";
		}
	}

	/**
	 * 按照时间的顺序倒序排
	 * 
	 * @param datalist
	 * @return
	 */
	private List<B2CData> getArraysOrderByTimeDescList(List<B2CData> datalist) {
		List<B2CData> newlist = new ArrayList<B2CData>();
		for (int i = datalist.size() - 1; i >= 0; i--) {
			B2CData data = datalist.get(i);
			newlist.add(data);
		}

		return newlist;
	}

	/**
	 * 根据反馈的状态来拼接xml 查询单独的表 2012-10-31 22:37
	 * 
	 * @param dangdang
	 * @param datalist
	 * @param floworderStatus
	 * @return
	 */
	private String StringXMLBodyByStatus_selectOneTable(DangDang dangdang, List<B2CData> datalist, int floworderStatus) {
		StringBuffer sub = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>");
		sub.append("<inputObject>");
		sub.append("<list>");
		// datalist=getArraysOrderByTimeDescList(datalist); //按照时间倒序
		for (int i = 0; i < datalist.size(); i++) {
			B2CData b2cdata = datalist.get(i);
			String json_content = b2cdata.getJsoncontent();
			JSONObject job = JSONObject.fromObject(json_content);
			String cwb = b2cdata.getCwb();
			if (floworderStatus == FlowOrderTypeEnum.RuKu.getValue()) {
				String in_storage_date = job.get("in_storage_date") != null ? job.getString("in_storage_date") : DateTimeUtil.getNowTime();
				String operator = job.get("operator") != null ? job.getString("operator") : "";
				sub.append("<row>");
				sub.append("<order_id>" + b2cdata.getCwb() + "</order_id>");
				sub.append("<in_storage_date>" + in_storage_date + "</in_storage_date>");
				sub.append("<express_id>" + dangdang.getExpress_id() + "</express_id>");
				sub.append("<operator>" + operator + "</operator>");
				sub.append("</row>");
			} else if (floworderStatus == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue()) {
				String in_storage_date = job.get("in_storage_date") != null ? job.getString("in_storage_date") : DateTimeUtil.getNowTime();
				String operator = job.get("operator") != null ? job.getString("operator") : "";
				sub.append("<row>");
				sub.append("<order_id>" + b2cdata.getCwb() + "</order_id>");
				sub.append("<in_storage_date>" + in_storage_date + "</in_storage_date>");
				sub.append("<express_id>" + dangdang.getExpress_id() + "</express_id>");
				sub.append("<operator>" + operator + "</operator>");
				sub.append("</row>");
			} else if (floworderStatus == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {
				String in_storage_date = job.get("in_storage_date") != null ? job.getString("in_storage_date") : DateTimeUtil.getNowTime();
				String operator = job.get("operator") != null ? job.getString("operator") : "";
				sub.append("<row>");
				sub.append("<order_id>" + b2cdata.getCwb() + "</order_id>");
				sub.append("<in_storage_date>" + in_storage_date + "</in_storage_date>");
				sub.append("<express_id>" + dangdang.getExpress_id() + "</express_id>");
				sub.append("<operator>" + operator + "</operator>");
				sub.append("</row>");
			}

			else if (floworderStatus == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
				String out_storage_date = job.get("out_storage_date") != null ? job.getString("out_storage_date") : DateTimeUtil.getNowTime();
				String operator = job.get("operator") != null ? job.getString("operator") : "";
				String express_operator_name = job.get("express_operator_name") != null ? job.getString("express_operator_name") : "";
				String express_operator_tel = job.get("express_operator_tel") != null ? job.getString("express_operator_tel") : "";
				String express_operator_id = job.get("express_operator_id") != null ? job.getString("express_operator_id") : "";
				boolean isSendFlag = b2cDataDAO.getCwbByB2cSendCount(dangdang.getCustomerids(), cwb, FlowOrderTypeEnum.FenZhanLingHuo.getValue());
				if (isSendFlag) { // 表示上一个状态推送成功

					if (sub.toString().contains(cwb)) { // 判断同一个xml中有重复的cwb，则移除上一个(替换)，留下最后一个
						String sub_str = sub.toString().replaceAll(cwb, cwb + "_lose");
						sub = new StringBuffer(sub_str);
					}

					sub.append("<row>");
					sub.append("<order_id>" + cwb + "</order_id>");
					sub.append("<out_storage_date>" + out_storage_date + "</out_storage_date>");
					sub.append("<express_operator_name>" + express_operator_name + "</express_operator_name>");
					sub.append("<express_operator_tel>" + express_operator_tel + "</express_operator_tel>");
					sub.append("<express_operator_id>" + express_operator_id + "</express_operator_id>");
					sub.append("<express_id>" + dangdang.getExpress_id() + "</express_id>");
					sub.append("<operator>" + operator + "</operator>");
					sub.append("</row>");
				} else {
					logger.warn("当前将要推送给当当的状态" + floworderStatus + ",订单号=" + cwb + ",上一个状态没有推送!");
					datalist.remove(i);
					i = i - 1;
				}

			} else if (floworderStatus == FlowOrderTypeEnum.YiShenHe.getValue()) {

				String sign_date = job.get("sign_date") != null ? job.getString("sign_date") : "";
				String sign_person = job.get("sign_person") != null ? job.getString("sign_person") : "";
				String order_status = job.get("order_status") != null ? job.getString("order_status") : "";
				String reason = job.get("reason") != null ? job.getString("reason") : "0";
				String operator = job.get("operator") != null ? job.getString("operator") : "";
				boolean isSendFlag = b2cDataDAO.getCwbByB2cSendCount(dangdang.getCustomerids(), cwb, FlowOrderTypeEnum.YiShenHe.getValue());
				if (isSendFlag) { // 表示上一个状态推送成功

					if (sub.toString().contains(cwb)) { // 判断同一个xml中有重复的cwb，则移除上一个(替换)，留下最后一个
						String sub_str = sub.toString().replaceAll(cwb, cwb + "_lose");
						sub = new StringBuffer(sub_str);
					}

					sub.append("<row>");
					sub.append("<order_id>" + cwb + "</order_id>");
					sub.append("<sign_date>" + sign_date + "</sign_date>");
					sub.append("<sign_person>" + sign_person + "</sign_person>");
					sub.append("<order_status>" + order_status + "</order_status>");
					sub.append("<reason>" + reason + "</reason>");
					sub.append("<express_id>" + dangdang.getExpress_id() + "</express_id>");
					sub.append("<operator>" + operator + "</operator>");
					sub.append("</row>");
				} else {
					logger.warn("当前将要推送给当当的状态" + floworderStatus + ",订单号=" + cwb + ",上一个状态没有推送!");
					datalist.remove(i);
					i = i - 1;
				}

			}

		}
		sub.append("</list>");
		sub.append("</inputObject>");
		return sub.toString().replaceAll("null", "");
	}

	// 修改反馈
	public void updateResponseStatus_oneTable(Map<String, Object> retMap, int status, String b2cids) {
		if (retMap == null || retMap.size() == 0) {
			return;
		}
		HashMap<String, Object> jobject = (HashMap<String, Object>) retMap.get("jsontotal");
		if (jobject == null || jobject.size() == 0) {
			return;
		}
		int statusCodeTotal = jobject.get("statusCode") != null ? Integer.parseInt(jobject.get("statusCode").toString()) : -1;
		if (statusCodeTotal == 0) {
			b2cDataDAO.updateMultiB2cIdSQLResponseStatus_AllSuccess(b2cids);
		} else {

			List jsonArray = (ArrayList) retMap.get("jsonarray");
			if (jsonArray == null || jsonArray.size() == 0) {
				b2cDataDAO.updateMultiB2cIdSQLResponseStatus_AllFaild(b2cids);
				throw new RuntimeException("解析当当发送不可预知的错误,检测到返回Xml是单个错误,b2cids=" + b2cids);
			}
			List<Object[]> paralist = new ArrayList<Object[]>();
			for (int i = 0; i < jsonArray.size(); i++) {
				Map<String, Object> job = (Map<String, Object>) jsonArray.get(i);
				String order_id = job.get("order_id") != null ? job.get("order_id").toString() : "";
				int statusCode = job.get("statusCode") != null ? Integer.parseInt(job.get("statusCode").toString()) : -1;
				Object[] obj = { statusCode == 0 ? 1 : 2, order_id, status }; // 系统中1,成功，2失败
				paralist.add(obj);
			}
			b2cDataDAO.updateSQLResponseStatus_oneTable(paralist);
		}

	}

	public static void main(String[] args) {
		String str = "<order_id>123456789</order_id>";
		if (str.contains("123456789")) {
			str = str.replaceAll("123456789", "nothingorder_1");
		}
		System.out.println(str);
	}

}
