package cn.explink.b2c.gzabc;

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

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.ExplinkService;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.domain.CustomWareHouse;
import cn.explink.util.MD5.MD5Util;

@Service
public class GuangZhouABCService {
	private Logger logger = LoggerFactory.getLogger(GuangZhouABCService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ExplinkService explinkService;
	@Autowired
	CwbDAO cwbDAO;

	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	CustomWareHouseDAO customWarehouseDAO;

	@Autowired
	DataImportService_B2c dataImportService_B2c;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public GuangZhouABC getGuangZhougABC(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		GuangZhouABC smile = (GuangZhouABC) JSONObject.toBean(jsonObj, GuangZhouABC.class);
		return smile;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		GuangZhouABC gzabc = new GuangZhouABC();
		String customerids = request.getParameter("customerids");
		gzabc.setCustomerids(customerids);
		gzabc.setRequst_url(request.getParameter("requst_url"));
		gzabc.setMaxCount(Integer.valueOf(request.getParameter("maxCount")));
		gzabc.setFeedback_url(request.getParameter("feedback_url"));
		gzabc.setPrivate_key(request.getParameter("private_key"));
		gzabc.setShippedCode(request.getParameter("shippedCode"));
		gzabc.setExportbranchid(Long.valueOf(request.getParameter("exportbranchid")));
		gzabc.setLogisticProviderID(request.getParameter("logisticProviderID"));

		String oldCustomerids = "";

		JSONObject jsonObj = JSONObject.fromObject(gzabc);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getGuangZhougABC(joint_num).getCustomerids();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerids, oldCustomerids, joint_num);
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	/**
	 * ABC请求接口开始
	 * 
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */

	public String orderDetailExportInterface(String sShippedCode, String logicdata, String checkdata, GuangZhouABC gzabc) throws Exception {

		String cwb = "";
		try {
			ValidaterParmsLegal(sShippedCode, logicdata, checkdata, gzabc);

			List<Map<String, Object>> parmsMaplist = Analyz_XmlDocByGuangZhougABC(logicdata);

			if (parmsMaplist == null || parmsMaplist.size() == 0) {

				logger.warn("解析xml之后的数据集为空logicdata={}", logicdata);

				return responseXml("", sShippedCode, false, "解析xml之后的数据集为空logicdata");
			}

			List<Map<String, String>> xmllist = getOrderDetailParms(gzabc, parmsMaplist);

			cwb = xmllist.get(0).get("cwb").toString();

			dataImportService_B2c.Analizy_DataDealByB2c(Long.valueOf(gzabc.getCustomerids()), B2cEnum.GuangZhouABC.getMethod(), xmllist, gzabc.getExportbranchid(), true);

			logger.info("处理广州ABC导入后的订单信息成功,cwb={}", xmllist.get(0).get("cwb").toString());

			return responseXml(cwb, sShippedCode, true, "成功");

		} catch (Exception e) {
			logger.error("广州ABC请求接口遇到未知异常" + e.getMessage(), e);
			return responseXml(cwb, sShippedCode, false, e.getMessage());
		}

	}

	private List<Map<String, String>> getOrderDetailParms(GuangZhouABC gzabc, List<Map<String, Object>> parmsMaplist) {
		List<Map<String, String>> xmllist = new ArrayList<Map<String, String>>();
		for (Map<String, Object> parmsMap : parmsMaplist) {

			Map<String, String> xmlMap = new HashMap<String, String>();

			String cwb = getParamsString(parmsMap, "waybillCode");
			String transcwb = getParamsString(parmsMap, "order_no");

			CwbOrderDTO cwbOrder = dataImportDAO_B2c.getCwbByCwbB2ctemp(cwb);
			if (cwbOrder != null) {
				logger.warn("广州ABC订单中含有重复数据cwb={}", cwb);
				throw new RuntimeException("订单重复" + cwb);
			}

			String shippName = getParamsString(parmsMap, "shippName");// 供货商
			String order_type = getParamsString(parmsMap, "order_type");
			String shippDate = getParamsString(parmsMap, "shippDate"); // 发货日期

			String deliveryName = getParamsString(parmsMap, "deliveryName"); // 发货人姓名
			String deliveryPhone = getParamsString(parmsMap, "deliveryPhone"); // 发货人电话
			String deliveryAddress = getParamsString(parmsMap, "deliveryAddress"); // 发货人地址

			String customerName = getParamsString(parmsMap, "customerName"); // 收货人姓名
			String customerPhone = getParamsString(parmsMap, "customerPhone"); // 收货人电话
			String customerAddress = getParamsString(parmsMap, "customerAddress"); // 收货人地址

			String deliveryGoods = getParamsString(parmsMap, "deliveryGoods"); // 发出货物
			String deliveryAmount = getParamsString(parmsMap, "deliveryAmount"); // 发出货物金额
			String weight = getParamsString(parmsMap, "weight"); // 重量

			String shouldReceive = getParamsString(parmsMap, "shouldReceive"); // 应收金额
			String comms = getParamsString(parmsMap, "comms");// 备注
			String request = getParamsString(parmsMap, "request"); // 配送要求

			String goodsNum = getParamsString(parmsMap, "goodsNum"); // 件数
			String insurAmount = getParamsString(parmsMap, "insurAmount"); // 保价金额

			String logisticProviderID = getParamsString(parmsMap, "logisticProviderID"); // 物流公司编号
			String warehouseId = getParamsString(parmsMap, "warehouseId"); // 库房ID
			String warehouseName = getParamsString(parmsMap, "warehouseName"); // 库房名称

			if (warehouseName != null && !warehouseName.isEmpty() && !customWarehouseDAO.isExistsWarehouFlag(warehouseName, gzabc.getCustomerids())) {
				CustomWareHouse custwarehouse = new CustomWareHouse();
				custwarehouse.setCustomerid(Long.valueOf(gzabc.getCustomerids()));
				custwarehouse.setCustomerwarehouse(warehouseName);
				custwarehouse.setWarehouse_no(warehouseName);
				custwarehouse.setWarehouseremark("");
				custwarehouse.setIfeffectflag(1);
				customWarehouseDAO.creCustomer(custwarehouse);
			}

			String warhouseid = dataImportService_B2c.getCustomerWarehouseNo(warehouseName, gzabc.getCustomerids());

			transcwb = getTransCwbs(parmsMap, transcwb);

			xmlMap.put("cwb", cwb);
			xmlMap.put("transcwb", transcwb);
			// xmlMap.put("shiptime", shippDate);
			xmlMap.put("cwbordertypeid", order_type);
			xmlMap.put("consigneename", customerName);
			xmlMap.put("consigneemobile", customerPhone);
			xmlMap.put("consigneeaddress", customerAddress);
			xmlMap.put("sendcarname", deliveryGoods);
			xmlMap.put("caramount", deliveryAmount);

			xmlMap.put("cargorealweight", weight);
			xmlMap.put("receivablefee", shouldReceive);
			xmlMap.put("cwbremark", comms);
			xmlMap.put("sendcarnum", goodsNum);

			xmlMap.put("customercommand", request);
			xmlMap.put("cwbremark", comms);
			xmlMap.put("customerwarehouseid", warhouseid);
			xmlMap.put("customerid", gzabc.getCustomerids());

			xmllist.add(xmlMap);
		}
		return xmllist;
	}

	public String getParamsString(Map<String, Object> dataMap, String params) {
		return dataMap.get(params) != null ? dataMap.get(params).toString() : "";
	}

	public String getParamsforIntStr(Map<String, Object> dataMap, String params) {
		return dataMap.get(params) != null ? dataMap.get(params).toString() : "0";
	}

	/**
	 * 获取一票多件
	 * 
	 * @param parmsMap
	 * @param transcwb
	 * @return
	 */
	private String getTransCwbs(Map<String, Object> parmsMap, String transcwb) {
		String subpacks = parmsMap.get("subpacks").toString();
		if (!"".equals(subpacks)) {
			List<Map<String, Object>> subpacklist = (List<Map<String, Object>>) parmsMap.get("subpacks");
			if (subpacklist == null || subpacklist.size() == 0) {
				return transcwb;
			}
			for (Map<String, Object> sub : subpacklist) {
				transcwb = sub.get("subPackageNo").toString() + ",";
			}
		}
		return transcwb.contains(",") ? transcwb.substring(0, transcwb.length() - 1) : transcwb;
	}

	private void ValidaterParmsLegal(String sShippedCode, String logicdata, String checkdata, GuangZhouABC gzabc) {
		if (!sShippedCode.equals(gzabc.getShippedCode())) {
			throw new RuntimeException("ABC请求参数sShippedCode=" + sShippedCode + "不正确,本地sShippedCode=" + gzabc.getShippedCode());
		}

		String localMD5 = MD5Util.md5(logicdata + gzabc.getPrivate_key());
		if (!localMD5.equalsIgnoreCase(checkdata)) {
			throw new RuntimeException("ABC请求签名验证失败checkdata=" + checkdata + ",localMD5=" + localMD5);
		}
	}

	/**
	 * 可解析list 两层
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> Analyz_XmlDocByGuangZhougABC(String fileName) throws Exception {
		// File inputXml = new File(fileName);
		InputStream iStream = new ByteArrayInputStream(fileName.getBytes("UTF-8"));
		SAXReader saxReader = new SAXReader();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Reader r = new InputStreamReader(iStream, "UTF-8");
		Document document = saxReader.read(r);
		Element employees = document.getRootElement();
		List<Map<String, Object>> jarry = new ArrayList<Map<String, Object>>();

		for (Iterator i = employees.elementIterator(); i.hasNext();) {
			Element note1 = (Element) i.next();

			Map<String, Object> map2 = new HashMap<String, Object>();
			for (Iterator j = note1.elementIterator(); j.hasNext();) {
				List<Map<String, Object>> subpacks = new ArrayList<Map<String, Object>>();

				Element note2 = (Element) j.next();
				map2.put(note2.getName(), note2.getText());
				for (Iterator k = note2.elementIterator(); k.hasNext();) {
					Element note3 = (Element) k.next();

					for (Iterator f = note3.elementIterator(); f.hasNext();) {
						Element note4 = (Element) f.next();
						Map<String, Object> map3 = new HashMap<String, Object>();
						map3.put(note4.getName(), note4.getText());
						subpacks.add(map3);
					}
				}
				map2.put("subpacks", subpacks);
			}
			jarry.add(map2);
			return jarry;
		}
		return null;

	}

	public static void main(String[] args) throws Exception {
		String xmlstr = "<orderVo>" + "<Order>" + "<waybillCode>355688974 </waybillCode>" + "<order_no>6987455862241</order_no>" + "<shippName>易迅</shippName>" + "<order_type>1</order_type>"
				+ "<shippDate>2012-5-1</shippDate>" + "<deliveryName>小明</deliveryName>" + "<deliveryPhone>139874625875</deliveryPhone>" + " <deliveryAddress>深圳市</deliveryAddress>"
				+ "<customerName>李晓敏</customerName>" + "<customerPhone>18976354421</customerPhone>" + "<customerAddress>浙江省绍兴市越城区环城西路严家潭毓兰华庭13幢2单元303室</customerAddress>"
				+ "<deliveryGoods>物品</deliveryGoods>" + "<deliveryAmount>16.40</deliveryAmount>" + "<weight>0.39</weight>" + "<shouldReceive>98.00</shouldReceive>" + "<comms>非货到付款</comms>"
				+ "<request>工作日、双休日或假日均可送货</request>" + "<goodsNum>1</goodsNum>" + "<logisticProviderID>zjabc</logisticProviderID>" + "<insurAmount>20.0</insurAmount>"
				+ "<shipID>abe3wwwwweedd</shipID>" + "<warehouseId>57100000</warehouseId>" + "<warehouseName>浙江配送中心</warehouseName>" + "<subPackages>" + "<subPackage>"
				+ "<subPackageNo>Z-1235677</subPackageNo>" + "</subPackage>" + "</subPackages>" + "</Order>" + "</orderVo>";

		List<Map<String, Object>> xmllist = Analyz_XmlDocByGuangZhougABC(xmlstr);

		System.out.println(xmllist);
	}

	private String responseXml(String cwb, String logisticProviderID, boolean flag, String remark) {
		String xml = "<ResponseWorkOrder>" + "<WaybillNo>" + cwb + "</WaybillNo>" + "<LogisticProviderID>" + logisticProviderID + "</LogisticProviderID>" + "<Success>" + (flag ? "True" : "False")
				+ "</Success>" + "<Remark>" + remark + "</Remark>" + "<ResponseWorkOrder>";

		logger.info("返回广州ABC-xml={}", xml);

		return xml;
	}

}
