package cn.explink.pos.yalian;

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
import java.util.HashMap;
import java.util.Iterator;
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
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.User;
import cn.explink.enumutil.pos.PosEnum;
import cn.explink.jms.dto.CwbOrderWithDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.WebServiceHandler;

@Service
public class YalianService {
	private Logger logger = LoggerFactory.getLogger(YalianService.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	B2cTools b2cTools;
	@Autowired
	B2CDataDAO b2CDataDAO;

	public void BuildYalianAppMethod(CwbOrderWithDeliveryState cwbOrder, DmpOrderFlow Deliverystate) {
		YalianApp yalian = getYalianAppSettingMethod(PosEnum.YalianApp.getKey());
		logger.info("执行了亚联App接口");

		long customercode = cwbOrder.getCwbOrder().getCustomerid();
		String str = String.valueOf(customercode);

		// 得出订单类型；电信联通移动
		int cwbtype = TypeJugement(yalian, str);

		// 拼接xml
		String xml = MixRequestXml(cwbOrder, cwbtype, yalian, str);
		logger.info("亚联App订单发送接口请求的xml={}", xml);

		try {
			String returnxml = (String) WebServiceHandler.LandWsByNameAndPassWord(yalian.getRequest_url(), "wlOrderReceiveRelease", xml);
			logger.info("亚联App订单发送接口返回xml={},cwb={}", returnxml, cwbOrder.getCwbOrder().getCwb());

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("亚联App订单发送接口返回出现异常", e);
		}

	}

	private int TypeJugement(YalianApp yalian, String code) {
		int cwbtype = 0;
		if (code.equals(yalian.getCode_dianxin())) {
			cwbtype = CwbTypeEnum.Dianxin.getKey();
		}
		if (code.equals(yalian.getCode_liantong())) {
			cwbtype = CwbTypeEnum.Liantong.getKey();
		}
		if (code.equals(yalian.getCode_yidong())) {
			cwbtype = CwbTypeEnum.Yidong.getKey();
		}
		return cwbtype;
	}

	private Map<String, Object> getNewZiduanMap(String a, String b, String c) {
		Map<String, Object> map = new HashMap<String, Object>();
		String reString = a + "：" + b + "：" + c;
		String hh = reString.replaceAll("&", "：");
		String uu[] = hh.split("：");
		for (int i = 0; i < 12; i++) {
			map.put(uu[i], uu[i + 1]);
			i++;
		}
		return map;
	}

	private String MixRequestXml(CwbOrderWithDeliveryState cwbOrder, int cwbtype, YalianApp yalian, String str) {
		User user = getDmpdao.getUserById(cwbOrder.getCwbOrder().getDeliverid());
		String remark2 = cwbOrder.getCwbOrder().getRemark2();// 收件人证件类型&收件人证件号
		String remark3 = cwbOrder.getCwbOrder().getRemark3();// 开户人姓名&开户人电话
		String remark4 = cwbOrder.getCwbOrder().getRemark4();// 开户人证件类型&收件人证件号
		Map<String, Object> map = new HashMap<String, Object>();
		if (yalian.Code_dianxin.equals(str)) {
			map = getNewZiduanMap(remark2, remark3, remark4);
		}
		String xml = "<request>" + "<cwb>" + cwbOrder.getCwbOrder().getTranscwb() + "</cwb>" + "<distributionId>" + cwbOrder.getCwbOrder().getCwb() + "</distributionId>" + "<status>09</status>"
				+ "<deliverName>" + user.getUsername() + "</deliverName>" + "<deliverNum>" + user.getUsermobile() + "</deliverNum>" + "<orderDate>" + cwbOrder.getCwbOrder().getEmaildate()
				+ "</orderDate>" + "<cwbType>" + cwbtype + "</cwbType>" + "<consigneeAddress>" + cwbOrder.getCwbOrder().getConsigneeaddress() + "</consigneeAddress>" + "<consigneeName>"
				+ cwbOrder.getCwbOrder().getConsigneename() + "</consigneeName>" + "<receivableFee>" + cwbOrder.getCwbOrder().getReceivablefee() + "</receivableFee>" + "<consigneMobile>"
				+ cwbOrder.getCwbOrder().getConsigneemobile() + "</consigneMobile>" + "<consigneeCerdType>"
				+ (map.get("收件人证件类型") == null ? "" : map.get("收件人证件类型"))
				+ "</consigneeCerdType>"
				+ // 证件类型
				"<consigneeCerdNum>"
				+ (map.get("收件人证件号码") == null ? "" : map.get("收件人证件号码"))
				+ "</consigneeCerdNum>"
				+ // 证件号
				"<sendcargoName>" + cwbOrder.getCwbOrder().getSendcarname() + "</sendcargoName>" + "<cwbRemark>" + cwbOrder.getCwbOrder().getCwbremark() + "</cwbRemark>" + "<custName>"
				+ (map.get("开户人姓名") == null ? "" : map.get("开户人姓名")) + "</custName>" + "<custPhone>" + (map.get("开户人电话") == null ? "" : map.get("开户人电话")) + "</custPhone>" + "<custCerdType>"
				+ (map.get("开户人证件类型") == null ? "" : map.get("开户人证件类型")) + "</custCerdType>" + "<custCerdNum>" + (map.get("开户人证件号码") == null ? "" : map.get("开户人证件号码")) + "</custCerdNum>"
				+ "<customerCommand>" + cwbOrder.getCwbOrder().getCustomercommand() + "</customerCommand>" + "<olId>1</olId>" + "</request>";
		return xml;
	}

	public YalianApp getYalianAppSettingMethod(int key) {
		YalianApp yalian = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			yalian = (YalianApp) JSONObject.toBean(jsonObj, YalianApp.class);
		} else {
			yalian = new YalianApp();
		}
		return yalian;

	}

	private String getObjectMethod(int key) {
		try {
			JointEntity obj = getDmpdao.getJointEntity(key);
			return obj.getJoint_property();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
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

	public Map<String, Object> getHttpXml(String xml) throws Exception {
		InputStream iStream = new ByteArrayInputStream(xml.getBytes());
		SAXReader saxReader = new SAXReader();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Reader r = new InputStreamReader(iStream, "GBK");
		Document document = saxReader.read(r);
		Element employees = document.getRootElement();
		for (Iterator i = employees.elementIterator(); i.hasNext();) {
			Element employee = (Element) i.next();
			returnMap.put(employee.getName(), employee.getText());
			for (Iterator j = employee.elementIterator(); j.hasNext();) {
				Element node = (Element) j.next();
				returnMap.put(node.getName(), node.getText());
				for (Iterator m = node.elementIterator(); m.hasNext();) {
					Element last = (Element) m.next();
					returnMap.put(last.getName(), last.getText());
				}
			}
		}
		return returnMap;
	}

}
