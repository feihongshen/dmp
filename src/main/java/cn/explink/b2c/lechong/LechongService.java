package cn.explink.b2c.lechong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.explink.b2c.lechong.xml.Item;
import cn.explink.b2c.lechong.xml.Order;
import cn.explink.b2c.lechong.xml.OrderResponse;
import cn.explink.b2c.lechong.xml.OrdersResponse;
import cn.explink.b2c.lechong.xml.RequestLechong;
import cn.explink.b2c.lechong.xml.Response;
import cn.explink.b2c.lechong.xml.UpdateInfo;
import cn.explink.b2c.lechong.xml.UpdateInfoResponse;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.service.CustomerService;
import cn.explink.util.MD5.MD5Util;

@Service
public class LechongService {
	private Logger logger = LoggerFactory.getLogger(LechongService.class);

	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService_B2c dataImportInterface;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	CustomWareHouseDAO customWareHouseDAO;
	@Autowired
	JointService jointService;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	CustomerService customerService;
	
	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Lechong getLechong(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Lechong smile = (Lechong) JSONObject.toBean(jsonObj, Lechong.class);
		return smile;
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		Lechong le = new Lechong();
		String customerids = request.getParameter("customerids");
		le.setCustomerids(customerids);
		le.setFeedbackUrl(request.getParameter("feedbackUrl"));
		le.setImportUrl(request.getParameter("importUrl"));
		String maxcount = "".equals(request.getParameter("maxcount")) ? "0" : request.getParameter("maxcount");
		le.setMaxCount(Integer.parseInt(maxcount));
		le.setSecretKey(request.getParameter("secretKey"));
		String warehouseid = request.getParameter("warehouseid");
		le.setWarehouseid(Long.valueOf(warehouseid));
		le.setDcode(request.getParameter("dcode"));
		le.setDname(request.getParameter("dname"));
		le.setCheckMd5(request.getParameter("checkMd5"));
		le.setDeleServerMobile(request.getParameter("deleServerMobile"));
		String oldCustomerids = "";

		JSONObject jsonObj = JSONObject.fromObject(le);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getLechong(joint_num).getCustomerids();
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

	public String excutorGetOrders(String xml) {
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.LeChong.getKey());

		if (isOpenFlag == 0) {
			logger.info("未开启乐宠POS对接！");
			return "";
		}
		Lechong le = getLechong(B2cEnum.LeChong.getKey());
		String xmlstr = "";
		String checkMd5 = le.getCheckMd5() == null ? "no" : le.getCheckMd5();
		try {
			RequestLechong requestLechong = LechongUnMarchal.XmltoPOJO(xml);
			if ("yes".equals(checkMd5)) {
				String content = "";
				List<Order> orders = requestLechong.getOrders().getOrder();
				if (orders.size() == 1) {
					for (Order order : orders) {
						String ProductName = "";
						for (Item item : order.getItems().getItem()) {
							ProductName += item.getProductName();
						}
						content = requestLechong.getLogisticProviderID() + order.getDoID() + order.getMailNO() + order.getReceiver().getRName() + order.getReceiver().getRPhone()
								+ order.getReceiver().getRAddress() + order.getNeedFund() + order.getBackFund() + ProductName;
					}
				} else if (orders.size() >= 1) {
					content = requestLechong.getLogisticProviderID();
					for (Order order : orders) {
						String ProductName = "";
						for (Item item : order.getItems().getItem()) {
							ProductName += item.getProductName();
						}
						content += "(" + order.getDoID() + order.getMailNO() + order.getReceiver().getRName() + order.getReceiver().getRPhone() + order.getReceiver().getRAddress()
								+ order.getNeedFund() + order.getBackFund() + ProductName + ")+";
					}
					content = content.substring(0, content.length() - 1);
				}
				String localmd5 = MD5Util.md5(content + "1q20o9");
				if (!localmd5.equalsIgnoreCase(requestLechong.getMD5Key())) {
					return "md5Key不匹配，不能行进操作";
				}
			}
			List<Order> orders = requestLechong.getOrders().getOrder();
			if (orders.size() == 0 || orders == null) {
				logger.info("未获取到乐宠订单明细");
			}

			Response res = new Response();
			OrdersResponse ors = new OrdersResponse();

			List<Map<String, String>> orderlist = parseCwbArrByOrderDto(orders, ors);
			if (orderlist == null || orderlist.size() == 0) {
				res.setOrdersResponses(ors);
				return xmlstr = ResponseUnMarchal.POJOtoXml(res);
			}
			long warehouseid = le.getWarehouseid(); // 订单导入的库房Id
			dataImportInterface.Analizy_DataDealByB2c(Long.parseLong(le.getCustomerids()), B2cEnum.LeChong.getMethod(), orderlist, warehouseid, true);

			logger.info("家有购物北京导入订单临时表成功");
			res.setOrdersResponses(ors);
			xmlstr = ResponseUnMarchal.POJOtoXml(res);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return xmlstr;

	}

	public List<Map<String, String>> parseCwbArrByOrderDto(List<Order> orders, OrdersResponse ors) {
		List<Map<String, String>> cwbList = new ArrayList<Map<String, String>>();
		List<OrderResponse> re = new ArrayList<OrderResponse>();
		for (Order order : orders) {
			CwbOrderDTO cwbOrder = dataImportDAO_B2c.getCwbByCwbB2ctemp(order.getDoID());
			OrderResponse or = new OrderResponse();
			if (cwbOrder != null) {
				logger.warn("获取家有购物订单中含有重复数据cwb={}", cwbOrder.getCwb());
				or.setDoID(order.getDoID());
				or.setMailNO(order.getMailNO());
				or.setFlag("1");
				or.setDesc("订单重复");
				re.add(or);

				continue;
			}

			String orderType = order.getOrderType();// 获取订单类型
			int cwbordertypeid = 1;
			if ("T0".equals(orderType)) {
				cwbordertypeid = CwbOrderTypeIdEnum.Peisong.getValue();
			} else if ("T1".equals(orderType)) {
				cwbordertypeid = CwbOrderTypeIdEnum.Shangmenhuan.getValue();
			} else {
				cwbordertypeid = CwbOrderTypeIdEnum.Shangmentui.getValue();
			}
			double caramount = 0;
			String sendcarname = "";
			List<Item> items = order.getItems().getItem();
			for (Item item : items) {
				caramount += Double.parseDouble(item.getSellPrice());
				sendcarname += item.getProductName() + ",";
			}
			String payType = order.getPaymentType();
			if (payType.equals("0")) {
				payType = "现金";
			}
			if (payType.equals("1")) {
				payType = "POS机";
			}
			if (payType.equals("2")) {
				payType = "支票";
			}
			String dispathDay = order.getDispatchDay();
			String strDay = "";
			if (dispathDay.equals("0")) {
				strDay = "不限";
			}
			if (dispathDay.equals("1")) {
				strDay = "工作日";
			} else {
				strDay = "双休日节假日";
			}
			String dispathTime = order.getDispatchTime();
			String strTime = "";
			if (dispathTime.equals("0")) {
				strTime = "不限";
			}
			if (dispathTime.equals("1")) {
				strTime = " 工作时间（早8:00-18:00）";
			} else {
				strTime = " 晚间送货（18:00-21:00）";
			}
			String dispatchNotify = order.getDispatchNotify();
			String strNotify = "";
			if (dispatchNotify.equals("0")) {
				strNotify = "";
			}
			if (dispatchNotify.equals("1")) {
				strNotify = "配送前需要通知";
			}
			Map<String, String> cwbMap = new HashMap<String, String>();
			cwbMap.put("cwb", order.getDoID()); // 物流订单号码
			cwbMap.put("transcwb", order.getMailNO()); // 运单号
			cwbMap.put("consigneename", order.getReceiver().getRName());
			cwbMap.put("paywayid", payType);// 支付方式
			cwbMap.put("consigneephone", order.getReceiver().getRPhone());
			cwbMap.put("consigneemobile", order.getReceiver().getRMobile());
			cwbMap.put("consigneepostcode", order.getReceiver().getZip());
			cwbMap.put("cargorealweight", order.getWeight()); // 订单重量(KG)
			cwbMap.put("cwbprovince", order.getReceiver().getRProvince());// 省
			cwbMap.put("cwbcity", order.getReceiver().getRCity());// 市
			cwbMap.put("consigneeaddress", order.getReceiver().getRAddress());
			cwbMap.put("caramount", String.valueOf(caramount));
			cwbMap.put("receivablefee", order.getNeedFund()); // COD代收款
			cwbMap.put("paybackfee", order.getBackFund()); // COD代收款
			cwbMap.put("cwbordertypeid", String.valueOf(cwbordertypeid));
			// cwbMap.put("cargosize",size); //尺寸
			cwbMap.put("sendcarname", sendcarname); // 发货货物名称
			cwbMap.put("remark1", strNotify); // 送货通知
			cwbMap.put("customercommand", "送货时间：" + strDay + strTime); // 备注信息
			cwbList.add(cwbMap);

			or.setDoID(order.getDoID());
			or.setMailNO(order.getMailNO());
			or.setFlag("0");
			or.setDesc("创建订单成功");
			re.add(or);
		}

		ors.setOrderResponse(re);
		return cwbList;
	}

	public String CancelOrders(String xml) {
		String xmlstr = "";
		UpdateInfo up = null;
		UpdateInfoResponse ups = null;
		try {
			up = UpdateInfoUnMarchal.XmltoPOJO(xml);
			ups = new UpdateInfoResponse();

			int isOpenFlag = jointService.getStateForJoint(B2cEnum.LeChong.getKey());
			ups.setDoID(up.getDoID());
			ups.setMailNO(up.getMailNO());
			if (isOpenFlag == 0) {
				logger.info("未开启乐宠POS对接！");
				ups.setFlag("1");
				ups.setDesc("未开启乐宠POS对接！");
				return UpdateInfoResponseUnMarchal.POJOtoXml(ups);
			}
			CwbOrder cwb = cwbDAO.getCwbByCwb(up.getDoID());
			CwbOrderDTO temp = dataImportDAO_B2c.getCwbByCwbB2ctemp(up.getDoID());

			if (cwb == null && temp == null) {
				ups.setFlag("1");
				ups.setDesc("不是有效的订单号！或者订单已取消");
				return UpdateInfoResponseUnMarchal.POJOtoXml(ups);
			}
			cwbDAO.dataLoseByCwb(up.getDoID());
			dataImportDAO_B2c.dataLoseB2ctempByCwb(up.getDoID());
			ups.setFlag("0");
			ups.setDesc("订单取消成功！");
			return UpdateInfoResponseUnMarchal.POJOtoXml(ups);
		} catch (Exception e) {
			ups.setFlag("1");
			ups.setDesc("订单取消失败" + e.getMessage());
			try {
				xmlstr = UpdateInfoResponseUnMarchal.POJOtoXml(ups);
			} catch (Exception e1) {
			}
		}
		return xmlstr;
	}
}
