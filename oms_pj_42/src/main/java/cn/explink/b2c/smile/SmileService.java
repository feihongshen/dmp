package cn.explink.b2c.smile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
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

import org.codehaus.jackson.map.ObjectMapper;
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
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.ExceptionTrace;
import cn.explink.b2c.tools.FlowFromJMSB2cService;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.Mail;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class SmileService {
	private Logger logger = LoggerFactory.getLogger(SmileService.class);
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

	public String getSmileFlowEnum(long flowordertype, long deliverystate) {
		if (flowordertype != FlowOrderTypeEnum.YiShenHe.getValue()) {
			for (SmileTrackFlowEnum TEnum : SmileTrackFlowEnum.values()) {
				if (flowordertype == TEnum.getFlowordertype()) {
					return TEnum.getFlowordertype() + "";
				}
			}
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue()) {
			return SmileTrackFlowEnum.PeiSongChengGong.getFlowordertype() + "";
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
			return SmileTrackFlowEnum.ShangMenTuiChengGong.getFlowordertype() + "";
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()) {
			return SmileTrackFlowEnum.ShangMenHuanChengGong.getFlowordertype() + "";
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			return SmileTrackFlowEnum.FenZhanZhiLiu.getFlowordertype() + "";
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue()) {
			return SmileTrackFlowEnum.JuShou.getFlowordertype() + "";
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
			return SmileTrackFlowEnum.JuShou.getFlowordertype() + "";
		}
		if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverystate == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
			return SmileTrackFlowEnum.HuoWuDiuShi.getFlowordertype() + "";
		}

		return null;

	}

	public Smile getSmileSettingMethod(int key) {
		Smile smile = null;
		if (this.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
			smile = (Smile) JSONObject.toBean(jsonObj, Smile.class);
		} else {
			smile = new Smile();
		}
		return smile;
	}

	/**
	 * 获取思迈配置信息的接口
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
	 * 获取smile的反馈状态
	 * 
	 * @return
	 */
	public SmileFlowEnum getFlowStateBySmile(long delivery_state) {
		for (SmileFlowEnum e : SmileFlowEnum.values()) {
			if (e.getDelivery_state() == delivery_state) {
				return e;
			}
		}
		return null;
	}

	/**
	 * 反馈[思迈]订单信息
	 */
	public long feedback_status() {
		long calcCount = 0;

		Smile smile = getSmileSettingMethod(B2cEnum.Smile.getKey());
		if (!b2ctools.isB2cOpen(B2cEnum.Smile.getKey())) {
			logger.info("未开[思迈]的对接!");
			return -1;
		}
		logger.info("=========Smile状态反馈任务调度开启==========");
		calcCount = sendCwbStatus_To_Smile(smile, FlowOrderTypeEnum.YiShenHe.getValue());
		logger.info("=========Smile状态反馈任务调度结束==========");

		return calcCount;
	}

	/**
	 * 状态反馈接口开始
	 * 
	 * @param smile
	 */
	private long sendCwbStatus_To_Smile(Smile smile, long flowordertype) {
		long calcCount = 0;
		try {

			int i = 0;
			while (true) {
				List<B2CData> smileDataList = b2cDataDAO.getDataListByFlowStatus(flowordertype, smile.getCustomerids(), smile.getMaxCount());
				i++;
				if (i > 100) {
					String warning = "dmp3314版本中查询[smile]状态反馈已经超过100次循环，可能存在程序未知异常,请及时查询并处理!";
					// Mail.LoadingAndSendMessage(warning);
					return 0;
				}

				if (smileDataList == null || smileDataList.size() == 0) {
					logger.info("当前没有要推送[smile]的数据");
					return 0;
				}
				DealWithBuildXMLAndSending(smile, smileDataList);
				calcCount += smileDataList.size();
			}

		} catch (Exception e) {
			String errorinfo = "发送[smile]状态反馈遇到不可预知的异常";
			logger.error(errorinfo, e);

		}
		return calcCount;

	}

	private void DealWithBuildXMLAndSending(Smile smile, List<B2CData> smileDataList) throws Exception {
		String b2cids = "";
		for (B2CData data : smileDataList) {
			String jsoncontent = data.getJsoncontent();
			SmileXMLNote note = getSmileXMLNoteMethod(jsoncontent);

			StringBuffer sub = new StringBuffer("");
			sub = getXmlStart(note, sub);
			sub.append("<WaybillNo>" + data.getCwb() + "</WaybillNo>");
			sub.append("<State>" + note.getState() + "</State>");
			sub = getXmlType(note, sub);
			sub.append("<StateTime>" + note.getStateTime() + "</StateTime>");
			sub.append("<OperName>" + note.getOperName() + "</OperName>");
			sub.append("<SendClientLoge>" + smile.getSendClientLoge() + "</SendClientLoge>");
			sub = getXmlEnd(note, sub);

			String response_xml = HTTPClient_Send(sub.toString(), note.getRequest_code(), smile);
			logger.info("状态反馈[发送smile]-XML={},[smile返回]-XML={}", sub.toString(), response_xml);

			if (response_xml.indexOf("Response=") == -1) {
				logger.warn("状态反馈[发送smile]-XML=" + sub.toString() + ",smile返回异常response_xml=" + response_xml);
				return;

			}
			Map<String, Object> parseMap = Analyz_XmlDocBySmile(response_xml.substring("Response=".length()));
			String result = parseMap.get("success").toString();
			String remark = parseMap.get("remark").toString();
			int send_b2c_flag = result.equals("true") ? 1 : 2;

			b2cDataDAO.updateB2cIdSQLResponseStatus(data.getB2cid(), send_b2c_flag, remark);
			b2cids += data.getB2cid() + ",";

		}
		b2cids = b2cids.length() > 0 ? b2cids.substring(0, b2cids.length() - 1) : "0";
		// 发送给dmp
		flowFromJMSB2cService.sendTodmp(b2cids);
	}

	private StringBuffer getXmlStart(SmileXMLNote note, StringBuffer sub) {

		if (note.getState().equals("SC_Abnor")) {
			sub.append("<RequestAbnor>");
		} else {
			sub.append("<RequestState>");
		}
		return sub;
	}

	private StringBuffer getXmlEnd(SmileXMLNote note, StringBuffer sub) {

		if (note.getState().equals("SC_Abnor")) {
			sub.append("</RequestAbnor>");
		} else {
			sub.append("</RequestState>");
		}
		return sub;
	}

	private StringBuffer getXmlType(SmileXMLNote note, StringBuffer sub) {
		if (note.getState().equals("SC_Abnor")) {
			sub.append("<AbnorInfo>" + note.getAbnorInfo() + "</AbnorInfo>");
		} else {
			sub.append("<ReplCost>" + note.getReplCost() + "</ReplCost>");
		}
		return sub;
	}

	// 获取tmall XML Note
	public SmileXMLNote getSmileXMLNoteMethod(String jsoncontent) {
		try {
			return new ObjectMapper().readValue(jsoncontent, SmileXMLNote.class);
		} catch (Exception e) {
			logger.error("获取SmileXMLNote异常！jsoncontent:" + jsoncontent + e);
			return null;
		}
	}

	/**
	 * 跟踪日志反馈
	 * 
	 * @param orderFlow
	 * @param cwbOrder
	 */
	public void SendTrackInfoBySmile(DmpOrderFlow orderFlow, DmpCwbOrder cwbOrder) {

		if (!b2ctools.isB2cOpen(B2cEnum.Smile.getKey())) {
			logger.info("未开[思迈]的对接!");
			return;
		}
		// //执行跟踪日志反馈的方法
		Smile smile = getSmileSettingMethod(B2cEnum.Smile.getKey());
		StringBuffer sub = new StringBuffer("");
		sub.append("<RequestTrack>");
		sub.append("<WaybillNo>" + cwbOrder.getCwb() + "</WaybillNo>");
		sub.append("<State>SC_GoodsTrack</State>");
		sub.append("<StateInfos>");
		sub.append("<StateInfo>");
		sub.append("<OperTime>" + DateTimeUtil.getNowTime() + "</OperTime>");
		sub.append("<OperName>" + getdmpDAO.getUserById(orderFlow.getUserid()).getRealname() + "</OperName>");
		sub.append("<GoodsInfo>" + orderFlowDetail.getDetail(orderFlow) + "</GoodsInfo>");
		sub.append("</StateInfo>");
		sub.append("</StateInfos>");
		sub.append("<SendClientLoge>" + smile.getSendClientLoge() + "</SendClientLoge>");
		sub.append("</RequestTrack>");

		String response_xml = HTTPClient_Send(sub.toString(), "RequestTrack", smile);
		logger.info("发送[思迈]跟踪信息,request_xml={},[思迈]跟踪信息返回,response_xml={}", sub.toString(), response_xml);

	}

	public String HTTPClient_Send(String para_xml, String Action, Smile smile) {

		String MD5Data = MD5Util.md5forNet(para_xml + smile.getSecretKey());
		String jointStr = "Request=" + para_xml + "&MD5=" + MD5Data + "&Action=" + Action;
		URL url;
		try {
			url = new URL(smile.getFeedback_url());

			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestProperty("content-type", "text/xml");
			httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
			httpURLConnection.setRequestProperty("contentType", "utf-8");
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setConnectTimeout(5000);
			httpURLConnection.setReadTimeout(5000);
			httpURLConnection.connect();
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));
			out.write(jointStr);
			out.flush();
			// 接收服务器的返回：
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
			StringBuilder buffer = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			return buffer.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

		/**
		 * HttpClient httpClient = new HttpClient(); PostMethod postMethod = new
		 * PostMethod(smile.getFeedback_url()); //URL为要请求的地址
		 * postMethod.getParams
		 * ().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		 * //设置编码方式
		 * 
		 * NameValuePair[] data = {new NameValuePair("Request", para_xml), new
		 * NameValuePair("MD5",MD5Data), new NameValuePair("Action",Action),
		 * 
		 * }; //请求的参数，这样写（参数名：参数值）
		 * 
		 * postMethod.setRequestBody(data); // 将表单的值放入postMethod中 try {
		 * httpClient.executeMethod(postMethod); //执行数据传输的方法。 return
		 * postMethod.getResponseBodyAsString(); //返回一个响应结果 } catch (Exception
		 * e) { logger.error("发送至[思迈遇到未知异常],request="+para_xml,e);
		 * e.printStackTrace(); } finally{ postMethod.releaseConnection(); }
		 * return "";
		 */

	}

	/**
	 * 可解析list
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> Analyz_XmlDocBySmile(String fileName) throws Exception {
		// File inputXml = new File(fileName);
		InputStream iStream = new ByteArrayInputStream(fileName.getBytes());
		SAXReader saxReader = new SAXReader();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Reader r = new InputStreamReader(iStream);
		Document document = saxReader.read(r);
		Element employees = document.getRootElement();
		for (Iterator i = employees.elementIterator(); i.hasNext();) {
			Element employee = (Element) i.next();
			returnMap.put(employee.getName().toLowerCase(), employee.getText());
			List<Map<String, Object>> jarry = new ArrayList<Map<String, Object>>();
			for (Iterator j = employee.elementIterator(); j.hasNext();) {
				Element node = (Element) j.next();
				returnMap.put(node.getName().toLowerCase(), node.getText());
			}
		}
		return returnMap;
	}

}
