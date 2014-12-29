package cn.explink.b2c.gztl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.ExplinkService;
import cn.explink.b2c.gztl.xmldto.GztlXmlElement;
import cn.explink.b2c.gztl.xmldto.Order;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.dao.UserDAO;
import cn.explink.pos.tools.JacksonMapper;

@Service
public class GztlService {
	private Logger logger = LoggerFactory.getLogger(GztlService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	OrderFlowDAO orderFlowDAO;
	@Autowired
	ExplinkService explinkService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	DataImportService_B2c dataImportService_B2c;

	protected static ObjectMapper jacksonmapper = JacksonMapper.getInstance();

	public String getObjectMethod(int key) {
		JointEntity obj = this.jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Gztl getGztl(int key) {
		if (this.getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
		Gztl smile = (Gztl) JSONObject.toBean(jsonObj, Gztl.class);
		return smile;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		Gztl gztl = new Gztl();
		String customerids = request.getParameter("customerids");
		gztl.setCustomerids(customerids);
		gztl.setSearch_number(Long.parseLong(request.getParameter("search_number")));
		gztl.setSearch_url(request.getParameter("search_url"));
		gztl.setPassword(request.getParameter("password"));
		gztl.setAgentId(request.getParameter("agentId"));
		gztl.setAgentName(request.getParameter("agentName"));
		gztl.setAgentPhone(request.getParameter("agentPhone"));
		gztl.setAgentWebsite(request.getParameter("agentWebsite"));
		gztl.setExportbranchid(request.getParameter("exportbranchid"));
		gztl.setSign(request.getParameter("sign"));
		gztl.setCode(request.getParameter("code"));
		gztl.setInvokeMethod("invokeMethod");
		String oldCustomerids = "";

		JSONObject jsonObj = JSONObject.fromObject(gztl);
		JointEntity jointEntity = this.jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = this.getGztl(joint_num).getCustomerids();
			} catch (Exception e) {
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Update(jointEntity);
		}
		// 保存 枚举到供货商表中
		this.customerDAO.updateB2cEnumByJoint_num(customerids, oldCustomerids, joint_num);
	}

	public void update(int joint_num, int state) {
		this.jiontDAO.UpdateState(joint_num, state);
	}

	/**
	 * 广州通路请求接口开始
	 *
	 * @author wukong
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */

	public String orderDetailExportInterface(String xml, Gztl gztl) {
		String cwb = "";
		try {

			GztlXmlElement gztlElement = (GztlXmlElement) this.xmlToObj(xml, new GztlXmlElement());
			if (gztlElement == null) {
				return "通过jaxb生成的GztlXmlElement类为空";
			}

			List<Order> orderlist = gztlElement.getOrders();

			if ((orderlist == null) || (orderlist.size() == 0)) {
				return "没有参数";
			}

			List<Map<String, String>> xmllist = this.getOrderDetailParms(gztl, orderlist);

			this.dataImportService_B2c.Analizy_DataDealByB2c(Long.valueOf(gztl.getCustomerids()), B2cEnum.Guangzhoutonglu.getMethod(), xmllist, Long.parseLong(gztl.getExportbranchid()), true);
			for (Order map : orderlist) {
				cwb += map.getOrderid() + ",";
			}
			cwb = cwb.substring(0, orderlist.size() - 1);

			this.logger.info("处理广州通路导入后的订单信息成功,cwb={}", cwb);

			return this.responseXml(cwb, "T", "成功");
		} catch (Exception e) {
			this.logger.error("处理广州通路异常", e);// ????
			return "未知异常";
		}

	}

	private List<Map<String, String>> getOrderDetailParms(Gztl gztl, List<Order> orderlist) {
		List<Map<String, String>> xmllist = new ArrayList<Map<String, String>>();

		for (Iterator<Order> iterator = orderlist.iterator(); iterator.hasNext();) {
			Order order = iterator.next();
			Map<String, String> xmlMap = new HashMap<String, String>();
			String cwb = order.getOrderid();
			CwbOrderDTO cwbOrder = this.dataImportDAO_B2c.getCwbByCwbB2ctemp(cwb);
			if (cwbOrder != null) {
				this.logger.warn("广州通路订单中含有重复数据cwb={}", cwb);
				continue;
			}
			xmlMap.put("cwbordertypeid", order.getTypeid());// 配送类型
			xmlMap.put("cwb", order.getOrderid());// 订单号或运单号
			xmlMap.put("transcwb", order.getSclientcode());// 客户单号

			xmlMap.put("consigneename", order.getCustomername());// 收货人
			xmlMap.put("consigneeaddress", order.getCustomeraddress());// 收货人地址
			xmlMap.put("consigneephone", order.getCustomermobile());// 收货人手机或电话？？？提供两个，本系统只有一个
			xmlMap.put("sendcarname", order.getDeliverygoods());// 配送货物
			xmlMap.put("backcargoname", order.getReturngoods());// 退货货物
			String caramount = "";
			if (order.getDeliverygoodsprice() != null) {
				caramount = order.getDeliverygoodsprice();
			} else {
				caramount = order.getReturngoodsprice();
			}
			xmlMap.put("caramount", caramount);// 退货货物价格或配送货物价格
			xmlMap.put("cargorealweight", order.getWeight());// 重量
			xmlMap.put("receivablefee", order.getShouldreceive());// 应收金额
			// xmlMap.put("remark5", order.getAccuallyreceive());// 实收金额
			xmlMap.put("customercommand", order.getRemark());// 备注
			xmlMap.put("sendcarnum", order.getGoodsnum());// 发货件数与件数
			xmlMap.put("remark1", "到货时间：" + order.getArrivedate());// （本系统）签收时间与到货时间（过来的数据）
			xmlMap.put("remark2", "入库时间：" + order.getPushtime() + ",订单生成时间：" + order.getOrderDate());// 发货时间与入库时间
			xmlMap.put("remark3", "配送区域:" + order.getSclientcode());// 配送区域？？？？？deliverarea
			xmlMap.put("remark4", "交接单号:" + order.getOrderBatchNo());
			xmlMap.put("remark5",
					"发货人名称:" + order.getConsignorname() + "," + "发货地址:" + order.getConsignoraddress() + "," + "手机:" + order.getConsignormobile() + "," + "电话:" + order.getConsignorphone());// 发货人信息

			String paywayid = "";
			if (order.getExtPayType().equals("0")) {
				paywayid = "1";
			} else if (order.getExtPayType().equals("1")) {
				paywayid = "2";
			} else {
				paywayid = "4";
			}
			xmlMap.put("paywayid", paywayid);// 支付方式

			xmllist.add(xmlMap);

		}

		return xmllist;
	}

	private String responseXml(String cwb, String flag, String remark) {
		StringBuffer xmlBuffer = new StringBuffer();
		xmlBuffer.append("<MSD>");
		xmlBuffer.append("<Orders>");
		for (String temp : cwb.split(",")) {
			xmlBuffer.append("<Order>");
			xmlBuffer.append("<orderno>" + temp + "</orderno>");
			xmlBuffer.append("<result>" + flag + "</result>");
			xmlBuffer.append("<remark>" + remark + "</remark>");
			xmlBuffer.append("</Order>");
		}
		xmlBuffer.append("</Orders>");
		xmlBuffer.append("</MSD>");
		String xml = xmlBuffer.toString();
		this.logger.info("返回广州通路-xml={}", xml);

		return xml;
	}

	public static void main(String[] args) throws JAXBException {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><MSD><Orders><Order><typeid>1</typeid><orderid>14062502938911</orderid><sclientcode>14062502938911</sclientcode><shipperid>广州唯品会</shipperid><consignorname/><consignoraddress/><consignormobile/><consignorphone/><customername>沈晓庆</customername><customeraddress>沈晓庆</customeraddress><customermobile>138****6001</customermobile><customerphone>****</customerphone><deliverygoods/><returngoods/><deliverygoodsprice/><returngoodsprice/><weight>0.0</weight><shouldreceive>0.0</shouldreceive><accuallyreceive/><remark/><arrivedate>2014-06-25 18:34:49</arrivedate><pushtime>2014-06-25 18:34:49</pushtime><goodsnum>1</goodsnum><deliverarea/><extPayType>0</extPayType><orderBatchNo>BTH140625077453</orderBatchNo><otherservicefee/><orderDate>2014-06-26 10:05:39</orderDate></Order></Orders></MSD>";
		StringReader stringReader = new StringReader(xml);
		JAXBContext jaxbContext = JAXBContext.newInstance(GztlXmlElement.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		GztlXmlElement person = (GztlXmlElement) unmarshaller.unmarshal(stringReader);

		List<Order> students2 = person.getOrders();
		for (Iterator<Order> iterator = students2.iterator(); iterator.hasNext();) {
			Order order = iterator.next();
			System.out.println(order.getCustomeraddress());
			System.out.println(order.getCustomermobile());
			System.out.println(order.getCustomername());
			System.out.println(order.getCustomerphone());

		}

		/*
		 * try { GztlXmlElement gztlElement = (GztlXmlElement)
		 * ObjectUnMarchal.XmltoPOJO(xml, GztlXmlElement.class); List<Order>
		 * orders = gztlElement.getOrders(); for (Iterator iterator =
		 * orders.iterator(); iterator.hasNext();) { Order order = (Order)
		 * iterator.next(); System.out.println(order.getCustomeraddress());
		 * System.out.println(order.getCustomermobile());
		 * System.out.println(order.getCustomername());
		 * System.out.println(order.getCustomerphone()); } } catch (Exception e)
		 * { // TODO Auto-generated catch block e.printStackTrace(); }
		 */

	}

	public Object xmlToObj(String xml, Object object) {
		StringReader stringReader = new StringReader(xml);
		Object obj = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			obj = unmarshaller.unmarshal(stringReader);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			this.logger.error("将xml解析成GztlXmlElement类时出错", e);
		}
		return obj;
	}
	// @SuppressWarnings({ "unchecked", "rawtypes" })
	// public String getParamsString(Order order, String params) {
	// String first = params.substring(0, 1).toUpperCase();
	// String rest = params.substring(1, params.length());
	// String methodString = new
	// StringBuffer("get"+first).append(rest).toString();
	// Class clazz = order.getClass();
	// try {
	// Method method = clazz.getDeclaredMethod(methodString, String.class);
	// if (method.invoke(order) != null) {
	// return (String) method.invoke(order);
	// }
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// this.logger.error("得到Order的Get方法时出错:属性为={}", methodString);
	// }
	// return null;
	// }
	// <MSD>
	// <Orders>
	// <Order>
	// <orderno>A6800905368</orderno>
	// <result>T</result>
	// <remark/>
	// </Order>
	// <Order>******</Order>
	// <Order>******</Order>
	// </Orders>
	// </MSD>
}
