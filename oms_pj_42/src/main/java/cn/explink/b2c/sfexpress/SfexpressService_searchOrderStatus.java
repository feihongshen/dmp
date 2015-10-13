package cn.explink.b2c.sfexpress;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.explink.b2c.Wholeline.JsonContext;
import cn.explink.b2c.Wholeline.WholeLine;
import cn.explink.b2c.Wholeline.WholelineSearch;
import cn.explink.b2c.explink.xmldto.OrderFlowDto;
import cn.explink.b2c.maisike.MaisikeStatusEmum;
import cn.explink.b2c.maisike.feedback_json.OrderStatus;
import cn.explink.b2c.sfexpress.wsdl.ISfexpressService;
import cn.explink.b2c.sfexpress.xmldtoSearch.ResponseSearch;
import cn.explink.b2c.sfexpress.xmldtoSearch.Route;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.dao.CommonSendDataDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.WarehouseCommenDAO;
import cn.explink.domain.Common;
import cn.explink.domain.WarehouseToCommen;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.WebServiceHandler;

@org.springframework.stereotype.Service
public class SfexpressService_searchOrderStatus {

	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	GetDmpDAO getdmpDAO;
	@Autowired
	CommonSendDataDAO commonSendDataDAO;
	@Autowired
	WarehouseCommenDAO warehouseCommenDAO;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	SfexpressDAO sfexpressDAO;
	@Autowired
	ISfexpressService sfexpressService;
	private Logger logger = LoggerFactory.getLogger(SfexpressController.class);

	public Sfexpress getSfexpress(int key) {
		Sfexpress sfexpress = null;
		if (b2ctools.getObjectMethod(key) != null) {
			JSONObject jsonObj = JSONObject.fromObject(b2ctools.getObjectMethod(key).getJoint_property());
			sfexpress = (Sfexpress) JSONObject.toBean(jsonObj, Sfexpress.class);
		} else {
			sfexpress = new Sfexpress();
		}
		return sfexpress;
	}

	/**
	 * 查询新表得到订单号去webservice请求顺丰快递
	 * 
	 * @param key
	 *            b2c的key
	 * @param sf
	 *            WholeLine实体
	 * @throws Exception
	 */
	public void getWSReturnJson() throws Exception {
		int b2cenum = B2cEnum.SFexpress.getKey();

		if (!b2ctools.isB2cOpen(b2cenum)) {
			logger.info("未开启主动抓取顺丰快递状态对接!");
			return;
		}
		Sfexpress sf = getSfexpress(b2cenum);

		Common common = getdmpDAO.getCommonByCommonnumber(sf.getCommoncode());
		if (common == null) {
			logger.error("顺丰快递获取dmp得到commoncode是空");
			return;
		}

		for (int page = 1; page <= 20; page++) {

			long sfCount = sf.getMaxCount() == 0 ? 100 : sf.getMaxCount();

			List<SfexpressBean> lineList = sfexpressDAO.getSfexpressOrderList(page, sfCount); // 查询所有未完结的状态
			if (lineList == null || lineList.size() == 0) {
				logger.info("当前没有待查询顺丰快递的订单数据,page={},count={}", page, sfCount);
				return;
			}

			for (SfexpressBean sfbean : lineList) {
				String cwb = sfbean.getCwb();
				try {
					String requestXML = buildSfXmlforOrderStatus(sf, sfbean);

					logger.info("查询顺丰快递状态，获得xml={},订单号={}", requestXML, sfbean.getCwb());

					String responseXML = sfexpressService.sfexpressService(requestXML);
					;

					logger.info("查询顺丰快递状态-返回，xml={},订单号={}", responseXML, sfbean.getCwb());

					Map<String, Object> responseMap = Analyz_XmlDocBySf(responseXML);
					String mailno = responseMap.get("mailno") == null ? "" : responseMap.get("mailno").toString();
					String Head = responseMap.get("Head") == null ? "" : responseMap.get("Head").toString();
					String ERROR = responseMap.get("ERROR") == null ? "" : responseMap.get("ERROR").toString();
					if (!"OK".equalsIgnoreCase(Head)) {
						logger.info("查询顺丰运单状态失败，单号={},ERROR={}", sfbean.getCwb(), ERROR);
						continue;
					}
					JSONArray routelist = responseMap.get("routelist") == null ? null : (JSONArray) responseMap.get("routelist"); // 解析出订单的跟踪流程

					if (routelist == null || routelist.size() == 0) {
						logger.warn("查询顺丰快递订单状态返回信息为空，cwb={}", sfbean.getCwb());
						continue;
					}

					dealwithSfexpressRouteResult(sf, sfbean, routelist);
				} catch (Exception e) {
					logger.error("查询顺丰路由跟踪未知异常cwb=" + cwb, e);
				}

			}
		}
	}

	private void dealwithSfexpressRouteResult(Sfexpress sf, SfexpressBean sfbean, JSONArray routelist) {
		for (int i = 0; i < routelist.size(); i++) {

			String cwb = null;
			JSONObject route = routelist.getJSONObject(i);
			try {
				cwb = sfbean.getCwb();
				String opcode = route.getString("opcode");
				String accept_time = route.getString("accept_time");
				String remark = route.getString("remark");

				// 如果不是到货，领货，滞留，拒收的状态就不存
				int flowordertype = getByValue(opcode);
				if (flowordertype == 0) {
					logger.info("顺丰状态{}不是需要的状态，订单号{}", opcode, cwb);
					continue;
				}
				// 封装实体
				logger.info("进入一条顺丰快递需要的状态{},订单号{}", opcode, cwb);

				// 查询commen_send_data 看是否有这条数据
				long isrepeatFlag = commonSendDataDAO.isExistsCwbFlag(cwb, sf.getCommoncode(), accept_time, String.valueOf(flowordertype));
				if (isrepeatFlag > 0) {
					logger.info("commen_send_data 已经有这条数据{},type={}", cwb, String.valueOf(flowordertype));
					continue;
				}

				long deliverystate = getDeliveryState(opcode, remark);

				OrderFlowDto dto = new OrderFlowDto();

				dto.setDeliverystate(String.valueOf(deliverystate));

				dto = buildOrderFlowDto(sf.getCommoncode(), accept_time, remark, cwb, flowordertype, deliverystate); // 构建公共对接回传所需要的对象

				String dataJson = JacksonMapper.getInstance().writeValueAsString(dto);

				// 插入上游OMS临时表
				commonSendDataDAO.creCommenSendData(cwb, 0, sf.getCommoncode(), DateTimeUtil.getNowTime(), accept_time, dataJson, deliverystate, flowordertype, "");

				// 更新轮询表状态，备注、时间
				sfexpressDAO.getUpdateByCwb(cwb, flowordertype, DateTimeUtil.getNowTime(), remark);

				// 自动补审核 条件是：已反馈并且，配送结果是：配送成功 或者全部退货
				if (flowordertype == FlowOrderTypeEnum.YiFanKui.getValue()
						&& (deliverystate == DeliveryStateEnum.PeiSongChengGong.getValue() || deliverystate == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliverystate == DeliveryStateEnum.FenZhanZhiLiu
								.getValue())) {
					// 插入上游OMS临时表
					flowordertype = FlowOrderTypeEnum.YiShenHe.getValue();
					dto.setFlowordertype(String.valueOf(flowordertype));
					String backJson = JacksonMapper.getInstance().writeValueAsString(dto);
					commonSendDataDAO.creCommenSendData(cwb, 0, sf.getCommoncode(), DateTimeUtil.getNowTime(), accept_time, backJson, deliverystate, flowordertype, "0");

					if (deliverystate != DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
						// update新表，下次不再请求这个订单
						sfexpressDAO.getUpdateIsOKByCwb(cwb, 1); // 标记为不再查询获取
					}

				}
			} catch (Exception e) {
				logger.error("查询顺丰路由返回处理异常cwb=" + cwb, e);
			}
		}
	}

	private OrderFlowDto buildOrderFlowDto(String userCode, String accept_time, String remark, String cwb, int flowordertype, long deliverystate) throws UnsupportedEncodingException {
		OrderFlowDto orderFlowDto = new OrderFlowDto();
		orderFlowDto.setCustid("0");
		orderFlowDto.setCwb(cwb);
		orderFlowDto.setDeliverystate(String.valueOf(deliverystate));
		orderFlowDto.setFloworderdetail(remark);
		orderFlowDto.setFlowordertype(String.valueOf(flowordertype));
		orderFlowDto.setOperatortime(accept_time);
		orderFlowDto.setPaytype(PaytypeEnum.Xianjin.getValue()); // 支付方式 待定义
		orderFlowDto.setRequestTime(DateTimeUtil.getNowTime("yyyyMMddHHmmss"));
		orderFlowDto.setUserCode(userCode);
		orderFlowDto.setExptmsg(remark); // 新增异常原因
		return orderFlowDto;
	}

	private long getDeliveryState(String order_status, String remark) {

		if (order_status.equals(SfCrossEnum.TuoTou.getSfcode())) {
			return DeliveryStateEnum.PeiSongChengGong.getValue();
		}

		if (order_status.equals(SfCrossEnum.ZhiLiu.getSfcode())) {
			return DeliveryStateEnum.FenZhanZhiLiu.getValue();
		}
		if (order_status.equals(SfCrossEnum.ShenHe.getSfcode()) && remark.contains("退回")) { // 审核
			return DeliveryStateEnum.QuanBuTuiHuo.getValue();
		}

		return 0;
	}

	private String buildSfXmlforOrderStatus(Sfexpress sf, SfexpressBean sfbean) {
		String requestXML = "<Request service='RouteService' lang='zh-CN'>" + "<Head>" + sf.getExpressCode() + "," + sf.getCheckword() + "</Head>" + "<Body>" + "<RouteRequest " + "tracking_type='1' " // 1根据运单号查询
																																																		// 2根据订单号查询
				+ "method_type='1' " // 查询方法 1标准查询， 2定制查询
				+ "tracking_number='" + sfbean.getCwb() + "' />" + "</Body>" + "</Request>";
		return requestXML;
	}

	private void getJsonToObjectTwo(WholeLine whole, JsonContext context, OrderFlowDto dto) {
		dto.setCustid("");
		dto.setCwb(context.getWaybillNo());
		dto.setFloworderdetail(context.getContentValue());// 操作描述
		dto.setOperatortime(context.getDisOperTm());// 操作时间
		dto.setRequestTime(DateTimeUtil.getNowTime());// 请求时间
		dto.setUserCode(whole.getUsercode());
	}

	/**
	 * 循环得出是否为需要的状态
	 * 
	 * @param value
	 *            操作码
	 * @return boolean
	 */
	public int getByValue(String value) {
		for (SfCrossEnum Enum : SfCrossEnum.values()) {
			if (value.equals(Enum.getSfcode())) {
				return Enum.getFlowordertype();
			}
		}
		return 0;
	}

	/**
	 * 得到最近的状态
	 * 
	 * @param returnxml
	 * @param key
	 * @param whole
	 */
	public void getWS(Object returnxml, int key, WholeLine whole) {
		try {
			// JsonContext
			// context=JacksonMapper.getInstance().readValue(returnxml.toString(),
			// JsonContext.class);
			if (returnxml.toString().length() == 0) {
				return;
			}
			List<JsonContext> list = test(returnxml.toString(), JsonContext.class);
			String date = "2013-01-01 00:00";
			for (JsonContext context : list) {
				String time = context.getDisOperTm();
				// 比较操作时间得到最近一次操作
				if (date.compareTo(time) < 0) {
					date = time;
				}
			}
			for (JsonContext context : list) {
				if (context.getDisOperTm().equals(date)) {
					OrderFlowDto dto = new OrderFlowDto();
					getJsonToObjectTwo(whole, context, dto);
					long flowordertype = Long.valueOf(dto.getFlowordertype());
					long delivery = Long.valueOf(dto.getDeliverystate());
					String dataJson = JacksonMapper.getInstance().writeValueAsString(dto);
					// 插入上游OMS临时表
					commonSendDataDAO.creCommenSendData(context.getWaybillNo(), 0, whole.getComeCode(), DateTimeUtil.getNowTime(), context.getDisOperTm(), dataJson, delivery, flowordertype, "");
				}
			}
		} catch (Exception e) {
			logger.error("顺丰快递订单查询接口处理数据时发生异常{}" + e);
		}

	}

	/**
	 * 解析顺丰快递
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> Analyz_XmlDocBySf(String fileName) throws Exception {
		// File inputXml = new File(fileName);
		InputStream iStream = new ByteArrayInputStream(fileName.getBytes("UTF-8"));
		SAXReader saxReader = new SAXReader();
		JSONArray jsonArray = new JSONArray();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Reader r = new InputStreamReader(iStream, "UTF-8");
		Document document = saxReader.read(r);
		Element employees = document.getRootElement();
		for (Iterator i = employees.elementIterator(); i.hasNext();) {
			Element employee = (Element) i.next();
			returnMap.put(employee.getName(), employee.getText());

			for (Iterator j = employee.elementIterator(); j.hasNext();) {
				Element node = (Element) j.next();
				returnMap.put(node.getName(), node.getText()); //
				for (Iterator k = node.elementIterator(); k.hasNext();) {
					Element node2 = (Element) k.next();
					JSONObject jsonObject = new JSONObject();
					for (Iterator m = node2.attributeIterator(); m.hasNext();) {
						DefaultAttribute node3 = (DefaultAttribute) m.next();
						jsonObject.put(node3.getName(), node3.getText());

					}
					jsonArray.add(jsonObject);

				}

			}
		}
		returnMap.put("routelist", jsonArray);
		return returnMap;
	}

	public static void main(String[] args) throws Exception {
		String xml = "<?xml version='1.0' encoding='UTF-8'?><Response service=\"RouteService\">" + "<Head>OK</Head>" + "<Body><RouteResponse mailno=\"587400134692\">"
				+ "<Route remark=\"快件到达便利店 \" accept_time=\"2014-04-27 22:18:46\" accept_address=\"深圳市\" opcode=\"50\"/>"
				+ "<Route remark=\"快件准备送往 便利店\" accept_time=\"2014-04-27 23:18:46\" accept_address=\"深圳市\" opcode=\"44\"/>"
				+ "<Route remark=\"派件已签收\" accept_time=\"2014-04-28 02:18:56\" accept_address=\"深圳市\" opcode=\"80\"/>" + "</RouteResponse>" + "</Body>" + "</Response>";

		Map<String, Object> responseMap = Analyz_XmlDocBySf(xml);
		String mailno = responseMap.get("mailno") == null ? "" : responseMap.get("mailno").toString();
		String Head = responseMap.get("Head") == null ? "" : responseMap.get("Head").toString();
		String ERROR = responseMap.get("ERROR") == null ? "" : responseMap.get("ERROR").toString();

		JSONArray routelist = responseMap.get("routelist") == null ? null : (JSONArray) responseMap.get("routelist"); // 解析出订单的跟踪流程

		System.out.println("mailno:" + mailno);
		System.out.println("Head:" + Head);
		System.out.println("ERROR:" + ERROR);

		for (int i = 0; i < routelist.size(); i++) {
			JSONObject route = routelist.getJSONObject(i);

			String opcode = route.getString("opcode");
			String accept_time = route.getString("accept_time");
			String remark = route.getString("remark");

			System.out.println(i + "opcode:" + opcode);
			System.out.println(i + "accept_time:" + accept_time);
			System.out.println(i + "remark:" + remark);
		}

	}

	/**
	 * 通用方法，json变为list
	 * 
	 * @param s
	 *            json
	 * @param clazz
	 *            公用
	 * @return
	 */
	public static List test(String s, Class clazz) {
		JSONArray jarr = JSONArray.fromObject(s);
		return (List) jarr.toCollection(jarr, clazz);
	}

}
