package cn.explink.b2c.yixun;

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

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.service.CustomerService;
import cn.explink.util.DateTimeUtil;

@Service
public class YiXunService {

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	YiXunDAO yixunDAO;
	@Autowired
	DataImportService_B2c dataImportInterface;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	CustomerService customerService;
	private Logger logger = LoggerFactory.getLogger(YiXunService.class);

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public YiXun getYiXun(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		YiXun yixun = (YiXun) JSONObject.toBean(jsonObj, YiXun.class);
		return yixun;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		YiXun yixun = new YiXun();

		yixun.setCustomerids(request.getParameter("customerids"));
		yixun.setWarehouseid(Long.parseLong(request.getParameter("warehouseid")));
		yixun.setApikey(request.getParameter("apikey"));
		yixun.setCount(Integer.parseInt(request.getParameter("count")));
		yixun.setRequest_url(request.getParameter("request_url"));
		yixun.setFeedback_url(request.getParameter("feedback_url"));
		yixun.setSecret(request.getParameter("secret"));

		String customerids = request.getParameter("customerids");
		String oldCustomerids = "";
		JSONObject jsonObj = JSONObject.fromObject(yixun);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getYiXun(joint_num).getCustomerids();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerids, oldCustomerids, joint_num);
		this.customerService.initCustomerList();
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	public int getStateForYiXun(int key) {
		JointEntity obj = null;
		int state = 0;
		try {
			obj = jiontDAO.getJointEntity(key);
			state = obj.getState();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return state;
	}

	/**
	 * 处理易迅请求请求 被动接收订单数据接口
	 * 
	 * @throws Exception
	 */
	public String requestMethod(YiXun yixun, String orderdetail) throws Exception {
		String nowtime = DateTimeUtil.getNowTime();
		logger.info("[易迅网]请求订单详细数据：{}", orderdetail);

		Map<String, Object> orderMapper = parserXmlToJSONObjectByArray(orderdetail);

		// 插入临时表的反馈结果
		return InsertTmallCwb_temp(nowtime, orderMapper, yixun);
	}

	/**
	 * 插入临时表，然后启动个定时器 获取临时表信息
	 * 
	 * @param nowtime
	 * @param orderMapper
	 * @param notify_id
	 * @param tmall
	 */
	private String InsertTmallCwb_temp(String nowtime, Map<String, Object> orderMapper, YiXun yixun) {
		String emaildate = DateTimeUtil.getNowDate() + " 00:00:00";
		List<Map<String, String>> xmlList = getDetailParamList(emaildate, yixun.getCustomerids(), nowtime, orderMapper);
		long warehouseid = getYiXun(B2cEnum.YiXun.getKey()).getWarehouseid();

		if (xmlList == null || xmlList.size() == 0) {
			return response_XML("0", "订单重复");
		}
		try {
			List<CwbOrderDTO> extractss = dataImportInterface.Analizy_DataDealByB2c(Long.parseLong(yixun.getCustomerids()), B2cEnum.YiXun.getMethod(), xmlList, warehouseid, true);
			logger.debug("处理[易迅网]后的订单信息=" + extractss.toString());
		} catch (Exception e) {
			logger.error("[易迅网]调用数据导入接口异常!,订单号:" + orderMapper.get("txLogisitcId"), e);
			return response_XML("0", "执行数据导入接口异常" + e.getMessage());
		}
		logger.info("[易迅网]数据插入表成功,订单号={}", orderMapper.get("txLogisitcId"));
		return response_XML("1", "");
	}

	/**
	 * 解析xml转化为list 把map转换为list 参数
	 * 
	 * @param customer_id
	 * @param nowtime
	 * @param orderMapper
	 * @param notify_id
	 * @return
	 */
	public List<Map<String, String>> getDetailParamList(String emaildate, String customer_id, String nowtime, Map<String, Object> orderMapper) {
		List<Map<String, String>> paralist = new ArrayList<Map<String, String>>();

		String cwb = orderMapper.get("txLogisitcId") == null ? "" : orderMapper.get("txLogisitcId").toString();
		String shipcwb = orderMapper.get("billCode") == null ? "" : orderMapper.get("billCode").toString();
		String customerwarehouseNo = orderMapper.get("stockSysNo") == null ? "" : orderMapper.get("stockSysNo").toString(); // 系统库存编号
		String sendName = orderMapper.get("sendName") == null ? "" : orderMapper.get("sendName").toString(); // 发货供货商
		String cwbprovince = orderMapper.get("sendProv") == null ? "" : orderMapper.get("sendProv").toString(); // 发件省
		String cwbcity = orderMapper.get("sendCity") == null ? "" : orderMapper.get("sendCity").toString(); // 发件
																											// 市
		String cwbcounty = orderMapper.get("sendCounty") == null ? "" : orderMapper.get("sendCounty").toString(); // 发件
																													// 区
		String sendAddress = orderMapper.get("sendAddress") == null ? "" : orderMapper.get("sendAddress").toString(); // 发件
																														// 详细地址（不算省市）
																														// 拼接省市
		String consignoraddress = cwbprovince + cwbcity + cwbcounty + sendAddress; // 拼接好的详细发件地址
		String consignorpostcode = orderMapper.get("sendPostCode") == null ? "" : orderMapper.get("sendPostCode").toString(); // 邮编
		String consignorphone = orderMapper.get("sendPhone") == null ? "" : orderMapper.get("sendPhone").toString(); // 发件人电话
		String consignormobile = orderMapper.get("sendMobile") == null ? "" : orderMapper.get("sendMobile").toString(); // 发件人手机

		String itemName = orderMapper.get("itemName") == null ? "" : orderMapper.get("itemName").toString(); // 项目名称
		// String
		// sendcarnum=orderMapper.get("piece")==null?"":orderMapper.get("piece").toString();
		// // 发货数量

		String special = orderMapper.get("special") == null ? "" : orderMapper.get("special").toString(); // 特殊
		String remark = orderMapper.get("remark") == null ? "" : orderMapper.get("remark").toString(); // 备注信息
		String insuranceValue = orderMapper.get("insuranceValue") == null ? "" : orderMapper.get("insuranceValue").toString(); // 保价金额
		String consigneename = orderMapper.get("receiveName") == null ? "" : orderMapper.get("receiveName").toString(); // 收件人

		String consigneepostcode = orderMapper.get("receivePostCode") == null ? "" : orderMapper.get("receivePostCode").toString(); // 收件邮编
		String consigneephone = orderMapper.get("receivePhone") == null ? "" : orderMapper.get("receivePhone").toString(); // 收件电话
		String receiveProv = orderMapper.get("receiveProv") == null ? "" : orderMapper.get("receiveProv").toString(); // 收件省
		String receiveCity = orderMapper.get("receiveCity") == null ? "" : orderMapper.get("receiveCity").toString(); // 收件市
		String receiveCounty = orderMapper.get("receiveCounty") == null ? "" : orderMapper.get("receiveCounty").toString(); // 收件区
		String receiveAddress = orderMapper.get("receiveAddress") == null ? "" : orderMapper.get("receiveAddress").toString(); // 收件地址
		String consigneeaddress = receiveProv + receiveCity + receiveCounty + receiveAddress;

		String freight = orderMapper.get("freight") == null ? "" : orderMapper.get("freight").toString(); // 运费
		String packedFee = orderMapper.get("packedFee") == null ? "" : orderMapper.get("packedFee").toString(); // 包装费
		String weight = orderMapper.get("weight") == null ? "" : orderMapper.get("weight").toString(); // 重量
		String logisticProviderId = orderMapper.get("logisticProviderId") == null ? "" : orderMapper.get("logisticProviderId").toString(); // 逻辑供货商
		String backFund = orderMapper.get("backFund") == null ? "" : orderMapper.get("backFund").toString(); // 运费
		String receivablefee = orderMapper.get("itemsValue") == null ? "" : orderMapper.get("itemsValue").toString(); // 代收款

		String goods = parseGoodsDetail(orderMapper); // 货物详细信息
		double cargoamount = parseGoodsPrice(orderMapper) / 100; // 货物金额
		String subPackages = parseSubPackages(orderMapper); // 子包裹号
		String sendcarnum = subPackages.split(",").length > 1 ? subPackages.split(",").length + "" : "1"; // 发货数量

		String sendcargoname = "[发出商品]";
		String cwbordertypeid = "1";
		String cwbdelivertypeid = "1";

		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("cwb", cwb);
		dataMap.put("emaildate", emaildate);
		dataMap.put("consigneename", consigneename);
		dataMap.put("consigneepostcode", consigneepostcode);
		dataMap.put("consigneeaddress", consigneeaddress);
		dataMap.put("consignoraddress", consignoraddress); // 发件地址
		dataMap.put("cwbprovince", receiveProv);
		dataMap.put("cwbcity", receiveCity);
		dataMap.put("cwbcounty", receiveCounty);
		dataMap.put("consigneemobile", consigneephone);
		dataMap.put("consigneephone", consigneephone);
		dataMap.put("cargorealweight", weight);
		dataMap.put("customercommand", "");
		dataMap.put("caramount", cargoamount + ""); // 货物金额 暂定为0
		dataMap.put("receivablefee", receivablefee); // 代收款
		dataMap.put("sendcargoname", sendcargoname); // 发货商品
		dataMap.put("sendcarnum", sendcarnum); // 发货商品
		dataMap.put("customerwarehouseid", "0"); // 发货仓库
		dataMap.put("cwbordertypeid", cwbordertypeid); // 订单类型
		dataMap.put("cwbdelivertypeid", cwbdelivertypeid); // 配送方式
		dataMap.put("customerid", customer_id); // 供货商Id
		dataMap.put("transcwb", subPackages); // 运单号

		CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwb);
		if (cwbOrder != null) {
			logger.warn("易迅接口中含有重复数据cwb={}", cwb);
			return null;
		}

		paralist.add(dataMap);

		return paralist;
	}

	/**
	 * 订单详细信息
	 */
	private String parseGoodsDetail(Map<String, Object> dataMap) {
		String orderDetail = "";
		String goods = dataMap.get("goods") != null ? dataMap.get("goods").toString() : "";
		if (goods != null && !"".equals(goods)) {
			List goodlist = (List) dataMap.get("goods");
			for (int k = 0; k < goodlist.size(); k++) {
				Map good = (Map) goodlist.get(k);
				String goodName = good.get("goodName") != null ? good.get("goodName").toString() : ""; // 商品名称
				String piece = good.get("piece") != null ? good.get("piece").toString() : ""; // 件数
				String remark = good.get("remark") != null ? good.get("remark").toString() : ""; // 备注
				String goodCode = good.get("goodCode") != null ? good.get("goodCode").toString() : ""; // 商品编码
				String price = good.get("price") != null ? good.get("price").toString() : ""; // 商品金额
				orderDetail += (goodName + piece + remark + goodCode + price);
			}
		}
		return orderDetail;
	}

	/**
	 * 货物金额
	 */
	private double parseGoodsPrice(Map<String, Object> dataMap) {
		double totalprice = 0;
		String goods = dataMap.get("goods") != null ? dataMap.get("goods").toString() : "";
		if (goods != null && !"".equals(goods)) {
			List goodlist = (List) dataMap.get("goods");
			for (int k = 0; k < goodlist.size(); k++) {
				Map good = (Map) goodlist.get(k);

				double price = Double.parseDouble(good.get("price") != null ? good.get("price").toString() : "0"); // 商品金额
				totalprice += price;
			}
		}
		return totalprice;
	}

	/**
	 * 包裹号信息
	 */
	private String parseSubPackages(Map<String, Object> dataMap) {
		String returnPackages = "";
		String subPackages = dataMap.get("subPackages") != null ? dataMap.get("subPackages").toString() : "";
		if (subPackages != null && !"".equals(subPackages)) {
			List packlist = (List) dataMap.get("subPackages");
			for (int k = 0; k < packlist.size(); k++) {
				Map pack = (Map) packlist.get(k);
				String subPackage = pack.get("subPackage") != null ? pack.get("subPackage").toString() : ""; // 运单号
				returnPackages += (subPackage + ",");
			}
			returnPackages = returnPackages.length() > 0 ? returnPackages.substring(0, returnPackages.length() - 1) : returnPackages;
		}
		return returnPackages;
	}

	/**
	 * 反馈给yixun信息
	 * 
	 * @param log_result
	 * @param log_event
	 * @return
	 */
	public static String response_XML(String code, String error) {
		if ("1".equals(code)) {
			return "<yixun><code>1</code></yixun>";
		} else {
			return "<yixun><code>0</code><error>" + error + "</error></yixun>";
		}
	}

	/**
	 * 解析易迅订单数据
	 * 
	 * @throws IOException
	 * @throws JDOMException
	 */
	public static Map<String, Object> parserXmlToJSONObjectByArray(String fileName) throws Exception {

		InputStream iStream = new ByteArrayInputStream(fileName.getBytes("UTF-8"));
		SAXReader saxReader = new SAXReader();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Reader r = new InputStreamReader(iStream, "UTF-8");
		Document document = saxReader.read(r);
		Element employees = document.getRootElement();
		for (Iterator i = employees.elementIterator(); i.hasNext();) {
			Element employee = (Element) i.next();
			returnMap.put(employee.getName(), employee.getText());
			List<Map<String, Object>> goodslist = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> subPackageslist = new ArrayList<Map<String, Object>>();
			for (Iterator j = employee.elementIterator(); j.hasNext();) {
				Map<String, Object> subPackages = new HashMap<String, Object>();
				Element node = (Element) j.next();
				if (node.getName().equalsIgnoreCase("subPackage") && !node.getText().equals("")) {
					subPackages.put(node.getName(), node.getText());
				}
				if (subPackages != null && subPackages.size() > 0) {
					subPackageslist.add(subPackages);
				}
				Map<String, Object> jsondetail = new HashMap<String, Object>();
				for (Iterator<Element> k = node.elementIterator(); k.hasNext();) {
					Element node_child = (Element) k.next();
					jsondetail.put(node_child.getName(), node_child.getText());
				}
				if (jsondetail != null && jsondetail.size() > 0) {
					goodslist.add(jsondetail);
				}
				if (subPackageslist != null && subPackageslist.size() > 0) {
					returnMap.put("subPackages", subPackageslist);
				}

			}
			if (goodslist != null && goodslist.size() > 0) {
				returnMap.put("goods", goodslist);
			}

		}
		return returnMap;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getXML());
		Map<String, Object> xmlMap = parserXmlToJSONObjectByArray(getXML());
		System.out.println(xmlMap);
	}

	private static String getXML() {
		return "<?xml version=\"1.0\" encoding=\"utf-8\" ?>"
				+ "<TabUniteOrder>"
				+ "<txLogisitcId>20127801121</txLogisitcId>" // cwb订单号
				+ "<billCode />" + "<stockSysNo>2001 int</stockSysNo>" + "<sendName>易迅网</sendName>" + "<sendAddress>纪蕰路588号（上海智力产业园）3号北楼3层</sendAddress>" + "<sendPostCode>200000</sendPostCode>"
				+ "<sendPhone>400-828-1878</sendPhone>" + "<sendMobile />" + "<sendProv>上海</sendProv>" + "<sendCity>上海市</sendCity>" + "<sendCounty>宝山区</sendCounty>" + "<itemName />"
				+ "<piece>2</piece>" + "<special>0</special>" + "<remark />" + "<insuranceValue>0</insuranceValue>" + "<receiveName>王芳 nvarchar50</receiveName>"
				+ "<receiveAddress>nvarchar200</receiveAddress>" + "<receivePostCode />" + "<receivePhone />" + "<receiveProv>北京市 nvarchar50</receiveProv>"
				+ "<receiveCity>昌平区 nvarchar50</receiveCity>" + "<receiveCounty>五环外,六环内 nvarchar50</receiveCounty>" + "<freight>0</freight>" + "<packedFee>0</packedFee>" + "<weight>0</weight>"
				+ "<logisticProviderId>易迅网</logisticProviderId>" + "<backFund>0</backFund>" + "<itemsValue>0.00</itemsValue>" + "<goods>" + "<good>"
				+ "<goodName>Netgear 网件 WGR614v10 N150无线宽带路由器 nvarchar200</goodName>" + "<piece>1</piece>" + "<remark />" + "<goodCode>01-084-742 varchar20</goodCode>" + "<price>10900</price>"
				+ "</good>" + "<good>" + "<goodName>TP-LINK 普联 TL-WR710N 150M迷你型无线路由器</goodName>" + "<piece>1</piece>" + "<remark />" + "<goodCode>19-154-244</goodCode>" + "<price>9900</price>"
				+ "</good>" + "</goods>" + "<subPackages>" + "<subPackage>201278010000</subPackage>" + "<subPackage>201278010001</subPackage>" + "</subPackages>" + "</TabUniteOrder>";
	}

}
