package cn.explink.b2c.maikolin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpException;
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
import cn.explink.b2c.tools.FlowFromJMSB2cService;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;

@Service
public class MaikolinService {
	@Autowired
	B2cTools b2ctools;
	@Autowired
	B2CDataDAO b2cDataDAO;
	@Autowired
	GetDmpDAO getdmpDAO;
	@Autowired
	FlowFromJMSB2cService flowFromJMSB2cService;
	private Logger logger = LoggerFactory.getLogger(MaikolinService.class);

	/**
	 * 入库，出库派送，配送结果反馈 接口
	 * 
	 * @throws IOException
	 * @throws HttpException
	 */
	public long feedback_status(int key) {
		Maikolin maikolin = getMaikolinSettingMethod(key);
		long calcCount = 0;
		if (!isMaikolinOpen(B2cEnum.Maikaolin.getKey())) {
			logger.error("未开[maikaolin]的对接!");
			return -1;
		}
		// /状态反馈

		calcCount += calcCount += FeedbackXMLByFlowOrderStatus_selectOneTable(maikolin, FlowOrderTypeEnum.RuKu.getValue(), maikolin.getPushCwb_URL());
		calcCount += FeedbackXMLByFlowOrderStatus_selectOneTable(maikolin, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), maikolin.getPushCwb_URL());
		// 已反馈并审核 YiShenHe
		calcCount += FeedbackXMLByFlowOrderStatus_selectOneTable(maikolin, FlowOrderTypeEnum.YiShenHe.getValue(), maikolin.getPushCwb_URL());

		return calcCount;
	}

	private boolean isMaikolinOpen(int key) {
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
	 * 状态反馈 查询单独的表的数据
	 * 
	 * @param dangdang
	 * @throws IOException
	 * @throws HttpException
	 */
	private long FeedbackXMLByFlowOrderStatus_selectOneTable(Maikolin maikolin, int floworderStatus, String flow_url) {
		long calcCount = 0;
		try {

			while (true) {
				List<B2CData> datalist = b2cDataDAO.getDataListByFlowStatus(floworderStatus, maikolin.getCustomerids(), maikolin.getCallBackCount());
				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有要推送[Maikaolin]的数据,状态:{}", floworderStatus);
					return 0;
				} else {
					calcCount += datalist.size();
					for (B2CData b2cdata : datalist) {
						String requestXML = BuildMaikolinXML(maikolin, floworderStatus, b2cdata);
						logger.info("当前状态{},当前推送[maikaolin]XML={}", floworderStatus, requestXML);
						String xmlDOS = HTTPInvokeWs(requestXML, flow_url);
						logger.info("返回:状态{},xml:{};", floworderStatus, xmlDOS);

						Map<String, Object> retMap = analyz_XmlDocByTmallMap(xmlDOS);
						String returnback = retMap.get("orderlist").toString();
						String reason[] = returnback.split(",");
						String reason1[] = reason[0].split("=");
						if (returnback.contains("success=true")) {
							b2cDataDAO.updateB2cIdSQLResponseStatus(b2cdata.getB2cid(), 1, "");
							logger.info("解析[maikaolin]xml结果:[success=true];状态{}", floworderStatus);
						} else {

							b2cDataDAO.updateB2cIdSQLResponseStatus(b2cdata.getB2cid(), 2, reason1[1]);
							logger.info("解析[maikaolin]xml结果:[success=false];状态{},异常原因={}", floworderStatus, reason1[1]);
						}
					}

				}
			}

		} catch (Exception e) {

			logger.error("解析[maikaolin]xml结果:[Exception];状态" + floworderStatus + ",异常：" + e.getMessage(), e);

		}
		return calcCount;
	}

	/*
	 * public static void main(String[] args) { String xml=
	 * "<?xml version=\"1.0\" encoding=\"UTF-8\"?><tms><response_header><user_id>BJZZAPI</user_id>"
	 * + "<user_key>91399749-CD66-4352-93A5-1E0BE99ACE77</user_key>"+
	 * "<method>PackageCourier</method><response_time>20131121080021</response_time>"
	 * + "</response_header><response_body><operation_result>"+
	 * "<package_id>055939262S</package_id><success>false</success>"+
	 * "<reason>该用户没有操作当前包裹的权限</reason></operation_result></response_body></tms>"
	 * ; try { Map<String,Object> retMap
	 * =MaikolinService.analyz_XmlDocByTmallMap(xml); String
	 * returnback=retMap.get("orderlist").toString(); String
	 * reason[]=returnback.split(","); String reason1[]=reason[0].split("=");
	 * System.out.println(reason1[1]); } catch (Exception e) {
	 * System.out.println("异常"); e.printStackTrace(); } }
	 */
	/**
	 * 根据反馈的状态来拼接xml 查询单独的表 2012-10-31 22:37
	 * 
	 * @param dangdang
	 * @param datalist
	 * @param floworderStatus
	 * @return
	 */
	private String BuildMaikolinXML(Maikolin maikolin, int floworderStatus, B2CData b2cdata) {

		StringBuffer sub = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		sub.append("<tms>");
		sub.append("<request_header>");

		String json_content = b2cdata.getJsoncontent();
		JSONObject job = JSONObject.fromObject(json_content);

		if (floworderStatus == FlowOrderTypeEnum.RuKu.getValue()) {
			String operator = job.get("operator") != null ? job.getString("operator") : "";
			sub.append("<user_id>" + maikolin.getUserCode() + "</user_id>");
			sub.append("<user_key>" + maikolin.getPrivate_key() + "</user_key>");
			sub.append("<method>PackageCheck</method>");
			sub.append("<request_time>" + DateTimeUtil.getNowTime("yyyyMMddHHmmss") + "</request_time>");
			sub.append("</request_header><request_body><operation>");
			sub.append("<express_id>" + maikolin.getExpress_id() + "</express_id>");
			sub.append("<package_id>" + b2cdata.getCwb() + "</package_id> ");
			sub.append("<checkdate>" + job.getString("courierdate") + "</checkdate>");
			sub.append("<status>" + job.getString("status") + "</status>");
			sub.append("<operation>" + operator + "</operation>");
		} else if (floworderStatus == FlowOrderTypeEnum.FenZhanLingHuo.getValue()) {
			String operator = job.get("operator") != null ? job.getString("operator") : "";
			sub.append("<user_id>" + maikolin.getUserCode() + "</user_id>");
			sub.append("<user_key>" + maikolin.getPrivate_key() + "</user_key>");
			sub.append("<method>PackageCourier</method>");
			sub.append("<request_time>" + DateTimeUtil.getNowTime("yyyyMMddHHmmss") + "</request_time>");
			sub.append("</request_header><request_body><operation>");
			sub.append("<express_id>" + maikolin.getExpress_id() + "</express_id>");
			sub.append("<package_id>" + b2cdata.getCwb() + "</package_id> ");
			sub.append("<courier>" + job.getString("courier") + "</courier>");
			sub.append("<couriertophone>" + job.getString("contact_phone") + "</couriertophone>");
			sub.append("<courierdate>" + job.getString("courierdate") + "</courierdate>");
			sub.append("<status>" + "您的订单已经分配员" + job.getString("courier") + "送出，查询电话" + job.getString("contact_phone") + "</status>");
			sub.append("<operation>" + operator + "</operation>");
		} else if (floworderStatus == FlowOrderTypeEnum.YiShenHe.getValue()) {

			String operator = job.get("operator") != null ? job.getString("operator") : "";
			sub.append("<user_id>" + maikolin.getUserCode() + "</user_id>");
			sub.append("<user_key>" + maikolin.getPrivate_key() + "</user_key>");
			sub.append("<method>PackageConfirm</method>");
			sub.append("<request_time>" + DateTimeUtil.getNowTime("yyyyMMddHHmmss") + "</request_time>");
			sub.append("</request_header><request_body><operation>");
			sub.append("<express_id>" + maikolin.getExpress_id() + "</express_id>");
			sub.append("<package_id>" + b2cdata.getCwb() + "</package_id> ");
			sub.append("<otstatus>" + job.getString("otstatus") + "</otstatus>");
			sub.append("<confirmdate>" + job.getString("courierdate") + "</confirmdate>");
			sub.append("<status>" + job.getString("status") + "</status>");
			sub.append("<operation>" + operator + "</operation>");
		}

		sub.append("</operation></request_body>");
		sub.append("</tms>");
		return sub.toString().replaceAll("null", "");
	}

	public String HTTPInvokeWs(String requestXML, String url) throws HttpException, IOException {
		URL url1 = new URL(url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();
		httpURLConnection.setRequestProperty("content-type", "text/xml");
		httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
		httpURLConnection.setRequestProperty("contentType", "utf-8");
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setDoInput(true);
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setConnectTimeout(30000);
		httpURLConnection.setReadTimeout(30000);
		httpURLConnection.connect();
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));
		out.write(requestXML);
		out.flush();
		// 接收服务器的返回：
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
		StringBuilder buffer = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		return buffer.toString();
	}

	private String getObjectMethod(int key) {
		try {
			JointEntity obj = getdmpDAO.getJointEntity(key);
			return obj.getJoint_property();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public Maikolin getMaikolinSettingMethod(int key) {
		Maikolin maikolin = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			maikolin = (Maikolin) JSONObject.toBean(jsonObj, Maikolin.class);
		} else {
			maikolin = new Maikolin();
		}
		return maikolin;

	}

	/*
	 * 解析xml
	 */
	public static Map<String, Object> analyz_XmlDocByTmallMap(String fileName) throws Exception {
		InputStream iStream = new ByteArrayInputStream(fileName.getBytes("UTF-8"));
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
				returnMap.put(node.getName(), node.getText());
				Map<String, Object> jsondetail = new HashMap<String, Object>();
				for (Iterator<Element> k = node.elementIterator(); k.hasNext();) {
					Element node_child = (Element) k.next();
					jsondetail.put(node_child.getName(), node_child.getText());
				}
				jarry.add(jsondetail);
			}

			if (jarry != null && jarry.size() > 0) {
				returnMap.put("orderlist", jarry);
			}

		}
		return returnMap;
	}

	public int getMaikaolinFlowEnum(long flowordertype, long deliverystate) {
		for (MaikolinFlowEnum TEnum : MaikolinFlowEnum.values()) {
			if (flowordertype == TEnum.getFlowordertype()) {
				return TEnum.getYihaodian_state();
			}
		}
		return 0;
	}

}