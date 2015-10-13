package cn.explink.b2c.happygo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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

import cn.explink.b2c.happygo.xml.Program;
import cn.explink.b2c.happygo.xml.xmlDoAnalisys;
import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.FlowFromJMSB2cService;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.MD5.MD5Util;

@Service
public class HappyGoService {
	@Autowired
	B2cTools b2ctools;
	@Autowired
	B2CDataDAO b2cDataDAO;
	@Autowired
	HappyGoDao happyGoDao;
	@Autowired
	GetDmpDAO getdmpDAO;
	@Autowired
	FlowFromJMSB2cService flowFromJMSB2cService;
	private Logger logger = LoggerFactory.getLogger(HappyGoService.class);

	public long feed_backstate() {
		long calcCount = 0;
		HappyGo happy = getHappyGo(B2cEnum.happyGo.getKey());
		if (!isHappyGoOpen(B2cEnum.happyGo.getKey())) {
			logger.error("未开快乐购的对接!");
			return 0;
		}
		calcCount += FeedbackXMLByFlowOrderStatus_selectOneTable(happy, FlowOrderTypeEnum.RuKu.getValue(), happy.getPostUrl());
		// 分站到货扫描——干线到货
		// FeedbackXMLByFlowOrderStatus_selectOneTable(happy,FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue(),happy.getPostUrl());
		// 分站领货——发短信接口
		calcCount += FeedbackXMLByFlowOrderStatus_selectOneTable(happy, FlowOrderTypeEnum.FenZhanLingHuo.getValue(), happy.getPostUrl());
		// 已反馈并审核 ——送货结束登记
		calcCount += FeedbackXMLByFlowOrderStatus_selectOneTable(happy, FlowOrderTypeEnum.YiShenHe.getValue(), happy.getPostUrl());

		return calcCount;
	}

	public static String getMD5GetherForSign(HappyGo happy, String xml, String strMethod, StringBuffer sbSystemArgs) {

		long ate = System.currentTimeMillis();
		String a = String.valueOf(ate);
		String b = a.substring(0, 10);

		sbSystemArgs.append("app_id=" + happy.getCode());
		sbSystemArgs.append("&charset=utf-8");
		sbSystemArgs.append("&data_type=xml");
		sbSystemArgs.append("&function_id=" + strMethod);

		sbSystemArgs.append("&sign_type=md5");
		sbSystemArgs.append("&time=" + b);
		sbSystemArgs.append("&version=1.0");
		String md5Date = sbSystemArgs.toString() + xml + happy.getKey();
		String sign = MD5Util.md5(md5Date).toUpperCase();
		return sign;
	}

	private long FeedbackXMLByFlowOrderStatus_selectOneTable(HappyGo happy, int value, String postUrl) {
		long calcCount = 0;
		try {

			int k = 0;
			while (true) {
				k++;
				if (k > 50) {
					logger.info("出现死循环，怀疑日志请求报错");
					return 0;
				}
				List<B2CData> datalist = b2cDataDAO.getDataListByFlowStatus(value, happy.getCustomerid(), happy.getPagesize());

				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有要推送快乐购的数据,状态:{}", value);
					return 0;
				} else {
					calcCount += datalist.size();

					for (B2CData b2cdata : datalist) {
						try {
							String json_content = b2cdata.getJsoncontent();
							JSONObject job = JSONObject.fromObject(json_content);
							String COD = job.getString("cod");
							String requestXML = BuildHappyGoXML(happy, value, b2cdata);

							StringBuffer sbSystemArgs = new StringBuffer(); // 系统参数
							String sign = getMD5GetherForSign(happy, requestXML, COD, sbSystemArgs);
							logger.info("当前状态{},当前推送快乐购的XML={},sign=" + sign, value, requestXML);

							String xmlDOS = doRequest(requestXML, postUrl + "?" + sbSystemArgs.toString() + "&sign=" + sign, happy);
							logger.info("返回:状态{},快乐购返回xml:{};", value, xmlDOS);

							Map<String, Object> retMap = parserXmlToJSONObjectByArray(xmlDOS);

							String returnback = retMap.get("error_type").toString();
							if ("0".equals(returnback)) {
								b2cDataDAO.updateB2cIdSQLResponseStatus(b2cdata.getB2cid(), 1, "");
								logger.info("解析快乐购的xml结果:成功;状态{}", value);
							} else {
								b2cDataDAO.updateB2cIdSQLResponseStatus(b2cdata.getB2cid(), 2, retMap.get("error_msg").toString());
								logger.info("解析快乐购xml结果:失败;状态{},异常原因={}", value, retMap.get("error_msg"));
							}
						} catch (Exception e) {
							logger.error("快乐购出现异常", e.getMessage());
						}
					}
				}
			}

		} catch (Exception e) {
			logger.error("解析快乐购xml结果:异常;状态" + value + ",异常：" + e.getMessage());

		}
		return calcCount;

	}

	public static Map<String, Object> parserXmlToJSONObjectByArray(String fileName) throws Exception {
		Map<String, Object> XMLMap = new HashMap<String, Object>();
		InputStream iStream = new ByteArrayInputStream(fileName.getBytes("UTF-8"));
		SAXReader saxReader = new SAXReader();
		Reader r = new InputStreamReader(iStream, "UTF-8");
		Document document = saxReader.read(r);
		Element employees = document.getRootElement();
		Map<String, Object> jsontotal = new HashMap<String, Object>();
		for (Iterator i = employees.elementIterator(); i.hasNext();) {
			Element employee = (Element) i.next();
			XMLMap.put(employee.getName(), employee.getText());
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
			XMLMap.put("orderlist", jarry);
		}
		return XMLMap;
	}

	private String BuildHappyGoXML(HappyGo happy, int value, B2CData b2cdata) {

		StringBuffer sub = new StringBuffer("<Program>");

		String json_content = b2cdata.getJsoncontent();
		JSONObject job = JSONObject.fromObject(json_content);
		String COD = job.getString("cod");
		String state = job.getString("state");

		if (FunctionForHappy.HOPE000005.getText().equals(COD)) { // 到货扫描,干线到货
			sub.append("<parameters>");
			sub.append("<wh_code>" + job.getString("wh_code") + "</wh_code>");
			sub.append("</parameters><delyinfo><row1>");
			sub.append("<wb_no>" + job.getString("wb_no") + "</wb_no>");
			sub.append("<order_no>" + job.getString("order_no") + "</order_no>");
			sub.append("<receive_name>" + job.getString("receiver_name") + "</receive_name>");
			sub.append("<weight>" + job.getString("weight") + "</weight>");
			sub.append("<comments></comments> ");
			sub.append("<shipping_date>" + job.getString("send_date") + "</shipping_date>");
			sub.append("<tel>" + job.getString("receiver_tel") + "</tel>");
			sub.append("<mobile>" + job.getString("receiver_tel") + "</mobile>");
			sub.append("<pieces>" + job.getString("piece") + "</pieces>");
			sub.append("<address>" + job.getString("receiver_addr") + "</address> ");
			sub.append("<request_time>" + job.getString("requesttime") + "</request_time>");
			sub.append("<receive_remark>" + job.getString("receive_remark") + "</receive_remark>");
			sub.append("</row1></delyinfo></Program>");
		} else if (FunctionForHappy.HOPE000018.getText().equals(COD)) { // 领货扫描，发送短信
			sub.append("<msg>");
			sub.append("<row1>");
			sub.append("<wb_no>" + job.getString("wb_no") + "</wb_no>");
			sub.append("<order_no>" + job.getString("order_no") + "</order_no>");
			sub.append("<delivery_time>" + job.getString("send_date") + "</delivery_time>");
			sub.append("<delivery_id>" + job.getString("senderid") + "</delivery_id>");
			sub.append("<delivery_name>" + job.getString("sendername") + "</delivery_name>");
			sub.append("<delivery_phone>" + job.getString("sendermobile") + "</delivery_phone>");
			sub.append("<remark_1></remark_1>");
			sub.append("<remark_2></remark_2>");
			sub.append("<remark_3></remark_3>");
			sub.append("<remark_4></remark_4>");
			sub.append("</row1></msg></Program>");
		} else if (FunctionForHappy.HOPE000014.getText().equals(COD)) {// 配送结束
			sub.append("<parameters>");
			sub.append("<wh_code>" + job.getString("wh_code") + "</wh_code></parameters>");
			sub.append("<delyinfo><row1>");
			sub.append("<wb_no>" + job.getString("wb_no") + "</wb_no>");
			sub.append("<order_no>" + job.getString("order_no") + "</order_no>");
			sub.append("<dely_date>" + job.getString("send_date") + "</dely_date>");
			sub.append("<send_yn>" + job.getString("send_ornot") + "</send_yn>");
			sub.append("<send_gb>" + job.getString("send_gb") + "</send_gb>");
			sub.append("<receiver_name>" + job.getString("receiver_name") + "</receiver_name>");
			sub.append("<reason_code>" + "RE01" + "</reason_code>");
			sub.append("<pieces>" + job.getString("piece") + "</pieces>");
			sub.append("<weight>0.0</weight>");
			sub.append("<comments/></row1></delyinfo></Program>");

		} else if (FunctionForHappy.HOPE000010.getText().equals(COD)) {// 预回收登记

			sub.append("<parameters>");
			sub.append("<wh_code>" + job.getString("wh_code") + "</wh_code>");
			sub.append("</parameters><delyinfo><row1>");
			sub.append("<wb_no>" + job.getString("wb_no") + "</wb_no>");
			sub.append("<order_no>" + job.getString("order_no") + "</order_no>");
			sub.append("<receive_name>" + job.getString("receiver_name") + "</receive_name>");
			sub.append("<weight>0.0</weight>");
			sub.append("<comments></comments>");
			sub.append("<shipping_date>" + job.getString("send_date") + "</shipping_date><tel></tel><mobile></mobile>");
			sub.append("<pieces>" + job.getString("piece") + "</pieces>");
			sub.append("<address>" + job.getString("receiver_addr") + "</address>");
			sub.append("<request_time>" + "00" + "</request_time>");
			sub.append("<receive_remark>" + job.getString("receive_remark") + "</receive_remark>");
			sub.append("</row1></delyinfo></Program>");

		} else if (FunctionForHappy.HOPE000020.getText().equals(COD)) {
			sub.append("<parameters>");
			sub.append("<wh_code>" + job.getString("wh_code") + "</wh_code>");
			sub.append("</parameters><delyinfo><row1>");
			sub.append("<wb_no>" + job.getString("wb_no") + "</wb_no>");
			sub.append("<order_no>" + job.getString("order_no") + "</order_no>");
			sub.append("<cod_amt>" + job.getString("notice_amt") + "</cod_amt>");
			sub.append("<cod_charge>0</cod_charge>");
			sub.append("<pay_date>" + job.getString("send_date").substring(0, 10) + "</pay_date>");
			sub.append("</row1></delyinfo></Program>");
			return sub.toString().replaceAll("null", "0");
		}

		return sub.toString().replaceAll("null", "");
	}

	private String HTTPInvokeWs(String requestXML, String url, HappyGo happy) throws HttpException, IOException {
		System.setProperty("javax.net.ssl.trustStore", happy.getLocation());
		System.setProperty("javax.net.ssl.trustStorePassword", happy.getPostkey());
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
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "utf-8"));
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

	public HappyGo getHappyGo(int key) {
		HappyGo happy = new HappyGo();
		if (!"".equals(getObjectMethod(key))) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			happy = (HappyGo) JSONObject.toBean(jsonObj, HappyGo.class);
		} else {
			happy = new HappyGo();
		}
		return happy == null ? new HappyGo() : happy;
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

	private boolean isHappyGoOpen(int key) {
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
	 * Url请求处理
	 * 
	 * @param strRequetType
	 * @param strUrl
	 * @param sSendXml
	 * @return
	 * @throws Exception
	 */
	static public String doRequest(String sSendXml, String strUrl, HappyGo happy) throws Exception {
		String value = "";
		OutputStream outputstream = null;
		InputStream ins = null;
		InputStreamReader inreder = null;
		BufferedReader in = null;
		System.setProperty("javax.net.ssl.trustStore", happy.getLocation());
		System.setProperty("javax.net.ssl.trustStorePassword", happy.getPostkey());
		try {
			URL url = new URL(strUrl);
			HttpURLConnection hcon = null;
			hcon = (HttpURLConnection) url.openConnection();
			hcon.setRequestProperty("Content-type", "text/html");
			hcon.setRequestProperty("Content-length", String.valueOf(-1));
			hcon.setRequestProperty("HTTP-Version", "HTTP/1.0");
			// hcon.setRequestProperty("Charset", "UTF-8");
			hcon.setConnectTimeout(1000);
			hcon.setReadTimeout(6000);
			hcon.setUseCaches(false);
			hcon.setDefaultUseCaches(false);
			hcon.setDoOutput(true);

			hcon.setRequestMethod("POST");
			hcon.addRequestProperty("POST", "/  HTTP/1.1");
			outputstream = hcon.getOutputStream();
			outputstream.write(sSendXml.getBytes("UTF-8"));
			outputstream.flush();
			outputstream.close();

			ins = hcon.getInputStream();
			inreder = new InputStreamReader(ins, "UTF-8");

			in = new BufferedReader(inreder);
			StringBuffer buffer = new StringBuffer();

			String line = "";
			while ((line = in.readLine()) != null)
				buffer.append(line);

			inreder.close();
			ins.close();
			in.close();
			String text = buffer.toString();
			buffer = null;

			int code = hcon.getResponseCode();
			if (code == 200) {
				value = text;
				// System.out.println(value);
			} else {
				System.out.println("错误代码" + code);
			}
		} catch (Exception e) {
			throw new Exception("dealRequest,调用远程接口失败，接口不可达或中途出现异常，详细错误信息：" + e.getMessage(), e);
		} finally {
			try {
				if (outputstream != null) {
					outputstream.close();
				}
				if (inreder != null) {
					inreder.close();
				}
				if (ins != null) {
					ins.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				throw new Exception("dealRequest,调用远程接口失败，资源释放异常，详细错误信息：" + e.getMessage(), e);
			}
		}

		return value;
	}

	public String search_backstate(String xml) {
		logger.error("获得快乐购查询xml={}", xml);
		String reXml = "";
		try {
			Program rootnote = xmlDoAnalisys.CancelUpdateInfo(xml);
			String transcwb = rootnote.getList().get(0).getWb_no();
			List<SerchHappy> list = happyGoDao.getSearchBytranscwb(transcwb);
			if (list != null && list.size() > 0) {
				reXml = "<Program>" + "<error_type>0</error_type>" + "<error_no>1</error_no>" + "<error_msg>接口操作成功!</error_msg>" + "<waybilltrack>" + getForxmlSearch(list)
						+ "</waybilltrack></Program>";
			} else {
				reXml = "<Program>" + "<error_type>1</error_type>" + "<error_no>1</error_no>" + "<error_msg>没有数据!</error_msg>" + "<waybilltrack>" + "</waybilltrack></Program>";
			}

		} catch (Exception e) {
			reXml = "<Program>" + "<error_type>1</error_type>" + "<error_no>1</error_no>" + "<error_msg>异常!" + e.getMessage() + "</error_msg>" + "<waybilltrack>" + "</waybilltrack></Program>";
			logger.error("快乐购查询异常" + e);
		}
		logger.error("快乐购查询日志={}", reXml);
		return reXml;

	}

	private String getForxmlSearch(List<SerchHappy> slist) {

		StringBuffer a = new StringBuffer();
		for (int i = 0; i < slist.size(); i++) {
			int type = i + 1;
			String xml = "<row" + type + ">" + "<context>" + slist.get(i).getOrderinfo() + "</context>" + "<proc_time>" + slist.get(i).getCredate() + "</proc_time>" + "</row" + type + ">";
			a.append(xml);
		}
		return a.toString();
	}

}
