package cn.explink.b2c.liantong;

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

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.liantong.json.LiantongResponse;
import cn.explink.b2c.liantong.json.Order;
import cn.explink.b2c.liantong.json.Step;
import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.dao.CustomerDAO;
import cn.explink.domain.B2CData;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.JSONReslutUtil;
import cn.explink.util.MD5.Base64Utils;
import cn.explink.util.MD5.MD5Util;

@Service
public class LiantongService {
	private Logger logger = LoggerFactory.getLogger(LiantongService.class);

	@Autowired
	private B2cTools b2ctools;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	LiantongDAO ltDao;
	@Autowired
	B2CDataDAO b2cDataDAO;

	public void feedback_status() {

		if (!b2ctools.isB2cOpen(B2cEnum.Liantong.getKey())) {
			logger.info("未开0联通0的状态反馈接口!");
			return;
		}
		Liantong lt = getLiantong(B2cEnum.Liantong.getKey()); // 获取配置信息
		sendCwbStatus_To_Liantong(lt);

	}

	public String getTrackEnum(long flowordertype, long deliverystate) {
		for (LiantongFlowEnum TEnum : LiantongFlowEnum.values()) {
			if (flowordertype == TEnum.getFlowordertype()) {
				return TEnum.getText();
			}
		}

		return null;

	}

	public String getFlowEnum(long flowordertype, long deliverystate) {
		for (LiantongWLEnum TEnum : LiantongWLEnum.values()) {
			if (flowordertype == FlowOrderTypeEnum.YiShenHe.getValue()) {
				if (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.ShangMenHuanChengGong.getValue()
						|| deliverystate == DeliveryStateEnum.ShangMenTuiChengGong.getValue()) {
					return LiantongWLEnum.QianShouChengGong.getCode();
				}

				if (deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverystate == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
					return LiantongWLEnum.QianShouShiBai.getCode();
				}

			}
			if (flowordertype == TEnum.getFlowordertype()) {
				return TEnum.getCode();
			}
		}

		return null;

	}

	// 获取配置信息
	public Liantong getLiantong(int key) {
		Liantong lt = null;
		String objectMethod = b2ctools.getObjectMethod(key).getJoint_property();
		if (objectMethod != null) {
			JSONObject jsonObj = JSONObject.fromObject(objectMethod);
			lt = (Liantong) JSONObject.toBean(jsonObj, Liantong.class);
		} else {
			lt = new Liantong();
		}
		return lt;
	}

	/**
	 * 请求接口开始
	 * 
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */

	public String requestCwbSearchInterface(Liantong lt, String QueryMode, String QueryNo, String appkey) throws Exception {

		try {

			if (QueryNo == null || "".equals(QueryNo)) {

				return "[联通商城]请求跟踪信息单号为空";

			}
			int typeflag = 0; // 默认按订单号查询 1按物流单号查询
			if (QueryMode.equalsIgnoreCase("MailNo")) { // 根据订单号查询
				typeflag = 1;
			}

			String MD5Str = MD5Util.md5(QueryNo + lt.getPrivate_key());
			if (!MD5Str.equalsIgnoreCase(appkey)) {

				return "加密验证不通过!" + MD5Str;
			}

			LiantongResponse ltresps = buildTrackInfoEntity(lt, QueryNo, typeflag);

			return JacksonMapper.getInstance().writeValueAsString(ltresps);

		} catch (Exception e) {
			String error = "处理[联通商城]查询请求发生未知异常:" + e.getMessage();
			logger.error(error, e);
			return error;
		}

	}

	private LiantongResponse buildTrackInfoEntity(Liantong lt, String cwbs, int typeflag) {
		LiantongResponse ltresp = new LiantongResponse();

		List<Order> orders = new ArrayList<Order>();
		for (String cwb : cwbs.split(",")) {
			Order order = new Order();
			order.setMailNo(cwb);
			List<Step> steps = buildStepList(lt, cwb, order, typeflag);
			order.setSteps(steps);
			orders.add(order);
		}

		ltresp.setOrders(orders);
		return ltresp;

	}

	private List<Step> buildStepList(Liantong lt, String cwb, Order order, int typeflag) {
		List<Step> steps = new ArrayList<Step>();

		List<LiantongEntity> tracklist = ltDao.getDataByCwb(cwb);

		for (LiantongEntity ltet : tracklist) {
			Step step = new Step();
			step.setAcceptTime(ltet.getAcceptTime());
			step.setAcceptAddress(ltet.getAcceptAddress());
			step.setAcceptName(ltet.getAcceptName());
			step.setAcceptAction(ltet.getAcceptAction());
			steps.add(step);
		}
		return steps;
	}

	/**
	 * 状态反馈接口开始
	 * 
	 * @param
	 */
	private void sendCwbStatus_To_Liantong(Liantong lt) {

		try {

			int i = 0;
			while (true) {
				List<B2CData> datalist = b2cDataDAO.getDataListByFlowStatus(lt.getCustomerid(), lt.getMaxCount());
				i++;
				if (i > 50) {
					String warning = "查询0联通0状态反馈已经超过50次循环，可能存在程序未知异常,请及时查询并处理!";
					logger.warn(warning);
					return;
				}

				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有要推送0联通0的数据");
					return;
				}

				for (B2CData data : datalist) {
					LiantongXMLNote xmlnote = JacksonMapper.getInstance().readValue(data.getJsoncontent(), LiantongXMLNote.class);

					String reqxml = "<Orders>" + "<Order>" + "<OrderNo>" + xmlnote.getOrder() + "</OrderNo>" + "<MailNo>" + xmlnote.getMailNo() + "</MailNo>" + "<Steps>" + "<Step>" + "<AcceptState>"
							+ xmlnote.getAcceptState() + "</AcceptState>" + "<AcceptTime>" + xmlnote.getAcceptTime() + "</AcceptTime>" + "<AcceptAddress>" + xmlnote.getAcceptAddress()
							+ "</AcceptAddress>" + "<AcceptName>" + xmlnote.getAcceptName() + "</AcceptName>" + "</Step>" + "</Steps>" + "</Order>" + "</Orders>";

					Map<String, String> params = buildRequestMaps(lt, data, reqxml); // 构建请求的参数

					String signstrs = LiantongCore.createLinkString(params); // 生成签名字符串
					String MD5Strs = lt.getSignSecurity() + "$" + signstrs + "$" + lt.getSignSecurity();

					logger.info("MD5加密字符串：{}", MD5Strs);

					String sign = MD5Util.md5_liantong(MD5Strs);
					// logger.info("md5result运算结果："+md5result);
					//
					// String sign=Base64Utils.base64(md5result, "UTF-8");
					// //BASE64(MD5(str ))方式签名

					params.put("sign", sign); // 追加计算出来的sign参数

					String responseData = JSONReslutUtil.sendHTTPServerByParms(params, lt.getFeedback_url()); // 请求并返回

					logger.info("当前联通返回XML={},cwb={}", responseData, data.getCwb());

					JSONObject jsonobj = JSONObject.fromObject(responseData);

					// LtResponse
					// ltresp=JacksonMapper.getInstance().readValue(responseData,
					// LtResponse.class);
					String detail = jsonobj.get("detail") == null ? null : jsonobj.getString("detail");
					if (detail != null) {
						logger.info("系统级别异常{},cwb={}", responseData, data.getCwb());
						return;
					}
					String respcode = jsonobj.getString("respcode");
					String respdesc = jsonobj.getString("respdesc");
					int send_b2c_flag = "0000".equals(respcode) ? 1 : 2;

					b2cDataDAO.updateB2cIdSQLResponseStatus(data.getB2cid(), send_b2c_flag, respdesc);

				}

			}

		} catch (Exception e) {
			logger.error("发送0联通0状态反馈遇到不可预知的异常", e);
		}

	}

	private Map<String, String> buildRequestMaps(Liantong lt, B2CData data, String reqxml) {
		Map<String, String> params = new HashMap<String, String>();
		String apptx = data.getB2cid() + DateTimeUtil.getNowTime("yyyyMMddHHmmss");
		String timestamp = DateTimeUtil.getNowTime("yyyy-MM-dd HH:mm:ss");

		params.put("appkey", lt.getAppkey());
		params.put("method", lt.getMethod());
		params.put("apptx", apptx); // 流水号
		params.put("timestamp", timestamp); // 时间戳

		params.put("wlcompanycode", lt.getAppcode()); // 物流公司编码
		params.put("busitype", "02");
		params.put("reqxml", reqxml);

		return params;
	}

	/**
	 * 可解析list
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> AnalyzXmlDocs(String fileName) throws Exception {
		// File inputXml = new File(fileName);
		InputStream iStream = new ByteArrayInputStream(fileName.getBytes());
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
