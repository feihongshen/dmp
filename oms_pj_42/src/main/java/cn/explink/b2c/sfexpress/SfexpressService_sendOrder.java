package cn.explink.b2c.sfexpress;

/**
 * 发送顺丰方法
 */
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.TypeReference;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.xmldto.OrderDto;
import cn.explink.b2c.sfexpress.wsdl.ISfexpressService;
import cn.explink.b2c.sfexpress.xmldtoSendOrder.ResponseSend;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.WarehouseCommenDAO;
import cn.explink.domain.WarehouseToCommen;
import cn.explink.util.DateTimeUtil;

@Service
public class SfexpressService_sendOrder {
	private Logger logger = LoggerFactory.getLogger(SfexpressService_sendOrder.class);
	@Autowired
	private GetDmpDAO getDmpDAO;
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	SfexpressDAO sfexpressDAO;
	@Autowired
	WarehouseCommenDAO WarehouseCommenDAO;
	@Autowired
	ISfexpressService sfexpressService;

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
	 * 推送出库至顺丰系统信息
	 */
	public void sendCwbOrdersToSFexpress() {

		int b2cenum = B2cEnum.SFexpress.getKey();

		if (!b2ctools.isB2cOpen(b2cenum)) {
			logger.info("未开出库顺丰快递的对接!");
			return;
		}
		Sfexpress sf = getSfexpress(b2cenum);

		long maxCount = sf.getMaxCount();

		for (int i = 0; i < 20; i++) {

			try {
				long loopcount = sf.getLoopcount() == 0 ? 5 : sf.getLoopcount(); // 重发次数5

				List<WarehouseToCommen> datalist = WarehouseCommenDAO.getCommenCwbListByCommon(sf.getCommoncode(), maxCount, loopcount); // 查询所有未推送数据
				if (datalist == null || datalist.size() == 0) {
					logger.info("当前没有待发送下游的数据-顺丰快递");
					return;
				}

				String requestCwbs = getRequestDMPCwbArrs(datalist); // 封装为-上游oms请求dmp的参数.
				String responseJson = getDmpDAO.getDMPOrdersByCwbs(requestCwbs); // 根据订单号批量
																					// 请求dmp，返回订单集合

				List<OrderDto> respOrders = JacksonMapper.getInstance().readValue(responseJson, new TypeReference<List<OrderDto>>() {
				});

				if (respOrders == null || respOrders.size() == 0) {
					logger.info("参数={} 请求dmp没有查询到返回的数据-顺丰快递", requestCwbs);
					return;
				}

				excuteSendSfexpress(sf, datalist, respOrders, sf); // 正向物流 （配送）

			} catch (Exception e) {
				logger.error("处理发送顺丰快递(外层)数据发生未知异常", e);
			}
		}

	}

	/**
	 * 执行配送类型订单的推送 正向物流 （包括配送）
	 * 
	 * @param maisike
	 * @param datalist
	 * @param respOrders
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	private void excuteSendSfexpress(Sfexpress maisike, List<WarehouseToCommen> datalist, List<OrderDto> respOrders, Sfexpress sf) {
		try {

			for (OrderDto order : respOrders) {

				String xml = buildSfexpressXML(sf, order); // 构建顺丰xml 报文信息

				logger.info("订单推送至顺丰快递xml={}", xml);

				/**
				 * 模拟顺丰返回
				 */

				String responseXML = sfexpressService.sfexpressService(xml);
				;

				logger.info("订单={}推送至顺丰快递返回信息={}", order.getCwb(), responseXML);

				Map<String, Object> xmlMap = Analyz_XmlDocBySf(responseXML);

				// ResponseSend
				// response=SfexpressUnmarchal.UnmarchalSend(responseXML);
				String Head = xmlMap.get("Head") == null ? "" : xmlMap.get("Head").toString();

				String sendflag = DateTimeUtil.getNowTime();
				String remark = "";
				if (!Head.equalsIgnoreCase("OK")) {
					sendflag = "2";
					remark = xmlMap.get("ERROR") == null ? "" : xmlMap.get("ERROR").toString();
				} else {
					remark = xmlMap.get("mailno") + "," + xmlMap.get("filter_result") + "," + xmlMap.get("remark");
				}

				WarehouseCommenDAO.updateCommenCwbListBycwb(order.getCwb(), sendflag, remark);

				/**
				 * 存储轮询推送表
				 */
				long count = sfexpressDAO.getCountWholeline(order.getCwb());
				if (count > 0) {
					logger.info("存储顺丰快递轮询表已存在,不需要再次存储,cwb={}", order.getCwb());
					continue;
				}

				SfexpressBean sfbean = new SfexpressBean();
				sfbean.setCwb(order.getCwb());
				sfbean.setCretime(DateTimeUtil.getNowTime());
				sfbean.setTranscwb(order.getTranscwb());
				sfbean.setStatus(0);
				sfbean.setRemark("下发顺丰数据成功");
				sfexpressDAO.saveSfexpress(sfbean);

			}

		} catch (Exception e) {
			logger.error("处理发送顺丰快递(内层)异常", e);
		}

	}

	private String buildSfexpressXML(Sfexpress sf, OrderDto order) {
		String d_province = order.getCwbprovince() == null || order.getCwbprovince().isEmpty() ? "北京" : order.getCwbprovince();
		String d_city = order.getCwbcity() == null || order.getCwbcity().isEmpty() ? "北京" : order.getCwbcity();
		String d_tel = "";

		if (order.getConsigneephone() != null && !order.getConsigneephone().isEmpty()) {
			d_tel = order.getConsigneephone();
		}
		if (order.getConsigneemobile() != null && !order.getConsigneemobile().isEmpty()) {
			d_tel = order.getConsigneemobile();
		}

		String xml = "<Request service='OrderService' lang='zh-CN'>" + "<Head>" + sf.getExpressCode() + "," + sf.getCheckword() + "</Head>" + "<Body>" + "<Order " + "orderid='" + order.getTranscwb()
				+ "' " + "express_type='3' " + "j_province='北京' " + "j_city='北京市' " + "j_company='" + sf.getCompanyName() + "' " + "j_contact='" + sf.getServiceContact() + "' " + "j_tel='"
				+ sf.getServicePhone() + "' " + "j_address='" + sf.getJ_address() + "' " + "d_province='" + d_province + "' " + "d_city='" + d_city + "' " + "d_company='' " + "d_contact='"
				+ order.getConsigneename() + "' " + "d_tel='" + d_tel + "' " + "d_address='" + order.getConsigneeaddress() + "' " + "parcel_quantity='"
				+ order.getSendcargonum()
				+ "' "
				+ "pay_method='1'>" // 付款运费方式1:寄方付 2.收件方付 3,第三方付
				+ "<OrderOption " + "custid='"
				+ sf.getCustid()
				+ "' " // 月结卡号
				+ "mailno='" + order.getCwb() + "' " + "cargo='" + order.getSendcargoname() + "' " + "cargo_count='" + order.getSendcargonum() + "' " + "cargo_unit='' " + "cargo_weight='"
				+ order.getCargorealweight() + "' " + "cargo_amount='" + order.getCargoamount() + "' " + "cargo_total_weight='" + order.getCargorealweight() + "' " + "sendstarttime='"
				+ DateTimeUtil.getNowTime() + "'>";

		if (order.getReceivablefee().doubleValue() > 0) { // 代收款大于0才会出现
			xml += "<AddedService " // 代收款服务
					+ "name='COD' " + "value='" + order.getReceivablefee() + "' " + "value1='" + sf.getCustid() + "' />"; // 月结卡号
		}

		// if(order.getCargoamount().doubleValue()>0){ //货物金额大于0才会出现
		// xml+="<AddedService " //保价服务
		// +"name='INSURE' "
		// +"value='"+order.getCargoamount()+"' />"; //月结卡号
		// }

		xml += "</OrderOption>" + "</Order>" + "</Body></Request>";
		return xml;
	}

	private String getRequestDMPCwbArrs(List<WarehouseToCommen> datalist) {
		JSONArray jsonArr = new JSONArray();
		for (WarehouseToCommen common : datalist) {
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("cwb", common.getCwb());
			jsonArr.add(jsonobj);
		}
		String requestCwbs = jsonArr.toString();
		return requestCwbs;
	}

	public static void main(String[] args) throws Exception {
		String responseXML = "<?xml version='1.0' encoding='UTF-8'?>" + "<Response service=\"OrderService\">" + "<Head>OK</Head>" + "<Body>" + "<Name>names111</Name>"
				+ "<OrderResponse filter_result='2' destcode='010' mailno='902401634602' origincode='010' orderid='882401634602'/>" + "</Body>" + "</Response>";

		System.out.println("===========================================");

		Map<String, Object> xmlMap = Analyz_XmlDocBySf(responseXML);

		System.out.println("Head:" + xmlMap.get("Head"));
		System.out.println("orderid:" + xmlMap.get("orderid"));
		System.out.println("destcode:" + xmlMap.get("destcode"));
		System.out.println("filter_result:" + xmlMap.get("filter_result"));

	}

	public static Map<String, Object> Analyz_XmlDocBySf(String fileName) throws Exception {
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

				for (Iterator k = node.attributeIterator(); k.hasNext();) {
					DefaultAttribute node2 = (DefaultAttribute) k.next();
					returnMap.put(node2.getName(), node2.getText());
				}

			}
		}
		return returnMap;
	}

}
