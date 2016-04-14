package cn.explink.b2c.zhongliang;

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

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.tools.ObjectUnMarchal;
import cn.explink.b2c.tools.RestHttpServiceHanlder;
import cn.explink.b2c.tools.b2cmonntor.B2cAutoDownloadMonitorDAO;
import cn.explink.b2c.zhongliang.xml.Response_GoodDetail;
import cn.explink.b2c.zhongliang.xml.Response_Order;
import cn.explink.b2c.zhongliang.xml.Response_PackageDetail;
import cn.explink.b2c.zhongliang.xml.Response_WaitOrder;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.UserDAO;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.service.CustomerService;
import cn.explink.service.CwbOrderService;

@Service
public class ZhongliangService {
	private Logger logger = LoggerFactory.getLogger(ZhongliangService.class);
	@Autowired
	JiontDAO jiontDAO;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	DataImportService_B2c dataImportInterface;
	@Autowired
	B2cAutoDownloadMonitorDAO b2cAutoDownloadMonitorDAO;
	@Autowired
	JointService jointService;
	@Autowired
	DataImportDAO_B2c dataImportDAO_B2c;
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	UserDAO userDAO;
	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	CustomerService customerService;

	public Zhongliang getZhongliang(int key) {
		if (this.getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(this.getObjectMethod(key));
		Zhongliang smile = (Zhongliang) JSONObject.toBean(jsonObj, Zhongliang.class);
		return smile;
	}

	public String getObjectMethod(int key) {
		JointEntity obj = this.jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	@Transactional
	public void edit(HttpServletRequest request, int joint_num) {
		Zhongliang zl = new Zhongliang();
		String customerid = request.getParameter("customerid");
		zl.setClientId(request.getParameter("clientId"));
		zl.setClientKey(request.getParameter("clientKey"));
		zl.setClientFlag(request.getParameter("clientFlag"));
		zl.setClientConst(request.getParameter("clientConst"));
		zl.setWaitOrder_url(request.getParameter("waitOrder_url"));
		zl.setCancleOrder_url(request.getParameter("cancleOrder_url"));
		zl.setOrderStatus_url(request.getParameter("orderStatus_url"));
		zl.setBackOrder_url(request.getParameter("backOrder_url"));
		zl.setBackCancel_url(request.getParameter("backCancel_url"));
		zl.setBackOrderStatus_url(request.getParameter("backOrderStatus_url"));
		zl.setNums(request.getParameter("nums"));
		zl.setCustomerid(request.getParameter("customerid"));
		zl.setWarehouseid(Long.parseLong(request.getParameter("warehouseid")));
		String oldCustomerids = "";
		JSONObject jsonObj = JSONObject.fromObject(zl);
		JointEntity jointEntity = this.jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = this.getZhongliang(joint_num).getCustomerid();
			} catch (Exception e) {
				this.logger.error("设置中粮对接异常", e);
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			this.jiontDAO.Update(jointEntity);
			this.logger.info("设置中粮对接成功！");
		}
		// 保存 枚举到供货商表中
		this.customerDAO.updateB2cEnumByJoint_num(customerid, oldCustomerids, joint_num);
		this.customerService.initCustomerList();
	}

	public void update(int joint_num, int state) {
		this.jiontDAO.UpdateState(joint_num, state);
	}

	public void waitOrder() {
		List<B2cEnum> b2cEnums = this.getB2cEnum(B2cEnum.Zhongliang.getMethod());

		for (B2cEnum b2c : b2cEnums) {

			String str = "";
			int isOpenFlag = this.jointService.getStateForJoint(b2c.getKey());

			if (isOpenFlag == 0) {
				this.logger.info("未开" + b2c.getText() + "对接！");
				continue;
			}

			Zhongliang zl = this.getZhongliang(b2c.getKey());
			String RequestType = "down";
			Map<String, String> map = this.checkData(zl, RequestType);
			str = RestHttpServiceHanlder.sendHttptoServer(map, zl.getWaitOrder_url());
			this.logger.info("" + b2c.getText() + "接收订单接口_订单返回明细_str==={}", str);
			try {
				Response_WaitOrder response = (Response_WaitOrder) ObjectUnMarchal.XmltoPOJO(str, new Response_WaitOrder());
				List<Response_Order> orders = response.getResponse_body().getOrder();
				if (str.contains("Error") || (orders == null)) {
					this.logger.info("没有获取到" + b2c.getText() + "的订单明细：" + str);
					continue;
				}
				List<Map<String, String>> orderlist = this.parseCwbArrByOrderDto(orders);
				if (orderlist != null) {
					long warehouseid = zl.getWarehouseid(); // 订单导入的库房Id

					this.dataImportInterface.Analizy_DataDealByB2c(Long.parseLong(zl.getCustomerid()), b2c.getMethod(), orderlist, warehouseid, true);
					String data = "<Request service= \"WaitOrder\" lang=\"zh-CN\">" + "<Head> client</Head>" + "<Body>";
					for (Response_Order order : orders) {
						CwbOrderDTO cwbOrder = this.dataImportDAO_B2c.getCwbByCwbB2ctemp(order.getSendOrderID());
						// String MailNO="";
						if (cwbOrder != null) {
							// MailNO=cwbOrder.getTranscwb();
							this.logger.info("" + b2c.getText() + "接收订单接口_导入订单临时表_成功,cwb={}", order.getSendOrderID());
						} else {
							this.logger.info("" + b2c.getText() + "接收订单接口_导入订单临时表_失败,cwb={}", order.getSendOrderID());
						}
						String DealStatus = cwbOrder == null ? "0" : "1";
						data += "<Order>" + "<SendOrderID>" + order.getSendOrderID() + "</SendOrderID>" + "<MailNO>" + order.getSendOrderID() + "</MailNO>" + "<DealStatus>" + DealStatus
								+ "</DealStatus>" + "<Reason></Reason >" + " </Order>";
					}
					data += "</Body>" + "</Request> ";
					Map<String, String> map_up = this.checkData(zl, "up");
					this.logger.info("" + b2c.getText() + "接收订单接口_发送={}", data);
					map_up.put("XML", data);
					String back = RestHttpServiceHanlder.sendHttptoServer(map_up, zl.getWaitOrder_url());
					this.logger.info("" + b2c.getText() + "接收订单接口_返回 ={}", back);
				}
			} catch (Exception e) {
				this.logger.error("" + b2c.getText() + "接收订单接口_插入临时表对接异常", e);
			}
		}
	}

	public void waitOrdercounts() {
		Zhongliang zl = this.getZhongliang(B2cEnum.Zhongliang.getKey());
		int count = 0;
		if ((zl != null) && (zl.getNums() != null) && !zl.getNums().equals("")) {
			count = Integer.parseInt(zl.getNums());
		} else {
			count = 20;
		}
		for (int i = 0; i < count; i++) {
			this.waitOrder();
		}

	}

	public Map<String, String> checkData(Zhongliang zl, String RequestType) {
		String clientid = zl.getClientId();
		String verifyData = VerifyDataUtil.encryptData(clientid, zl.getClientFlag(), zl.getClientKey(), zl.getClientConst());
		Map<String, String> map = new HashMap<String, String>();
		map.put("clientid", clientid);
		map.put("verifyData", verifyData);
		map.put("RequestType", RequestType);
		return map;
	}

	public List<Map<String, String>> parseCwbArrByOrderDto(List<Response_Order> orders) {
		List<Map<String, String>> cwbList = new ArrayList<Map<String, String>>();
		for (Response_Order order : orders) {
			CwbOrderDTO cwbOrder = this.dataImportDAO_B2c.getCwbByCwbB2ctemp(order.getSendOrderID());
			if (cwbOrder != null) {
				this.logger.warn("中粮接收订单接口_获取中粮订单中含有重复数据,cwb={}", cwbOrder.getCwb());
				continue;
			}

			String orderType = order.getOrdertype();// 获取订单类型
			int cwbordertypeid = CwbOrderTypeIdEnum.Peisong.getValue();
			if ("70".equals(orderType) || "80".equals(orderType)) {
				cwbordertypeid = CwbOrderTypeIdEnum.Peisong.getValue();
			} else if ("75".equals(orderType)) {
				cwbordertypeid = CwbOrderTypeIdEnum.Shangmenhuan.getValue();
			} else {
				cwbordertypeid = CwbOrderTypeIdEnum.Weiqueding.getValue();
			}
			String paywayid = "";

			if (order.getPaytype().contains("pos")) {
				paywayid = PaytypeEnum.Pos.getValue() + "";
			} else if (order.getPaytype().contains("支票")) {

				paywayid = PaytypeEnum.Zhipiao.getValue() + "";
			} else {
				paywayid = PaytypeEnum.Xianjin.getValue() + "";
			}
			// double caramount = 0;
			String sendcarname = "";
			String size = "";
			String remark1 = order.getOrderTemark();
			if (remark1 != null) {
				if (remark1.length() > 100) {
					remark1 = remark1.substring(0, 100);
				}
			}
			List<Response_GoodDetail> goods = order.getGoodDetail();
			for (Response_GoodDetail good : goods) {
				// caramount += Double.parseDouble(good.getPrice());
				sendcarname += good.getGoodsName() + ",";
				size += good.getUnitName() + ",";
			}
			String address = "";
			address += order.getProvincename() == null ? "" : order.getProvincename();
			address += order.getCityname() == null ? "" : order.getCityname();
			address += order.getAreaname() == null ? "" : order.getAreaname();
			address += order.getAddress() == null ? "" : order.getAddress();
			Response_PackageDetail packageDetails = order.getPackageDetail();
			StringBuffer transcwb = new StringBuffer("");
			if (packageDetails != null) {
				for (String packageID : packageDetails.getPackageID()) {
					transcwb.append(packageID + ",");
				}

				transcwb.replace(transcwb.lastIndexOf(","), transcwb.length(), "");
			}
			Map<String, String> cwbMap = new HashMap<String, String>();
			cwbMap.put("cwb", order.getSendOrderID()); // 物流订单号码
			cwbMap.put("transcwb", transcwb.toString()); // 运单号
			cwbMap.put("consigneename", convertEmptyString(order.getLinkman()));// 收件人姓名
			cwbMap.put("paywayid", paywayid);// 支付方式
			cwbMap.put("consigneephone", convertEmptyString(order.getTelno()));
			cwbMap.put("consigneemobile", convertEmptyString(order.getHandsetno()));
			cwbMap.put("consigneepostcode", convertEmptyString(order.getPostalcode()));
			cwbMap.put("cargorealweight", convertEmptyString(order.getPackweight())); // 订单重量(KG)
			cwbMap.put("cwbprovince", convertEmptyString(order.getProvincename()));// 省
			cwbMap.put("cwbcity", convertEmptyString(order.getCityname()));// 市
			cwbMap.put("consigneeaddress", convertEmptyString(address));
			cwbMap.put("caramount", convertEmptyDouble(order.getOrdervalue()));// 订单金额
			cwbMap.put("cargotype", convertEmptyDouble(order.getValuableflag()));// 货物金额
			cwbMap.put("receivablefee", convertEmptyDouble(order.getGetvalue())); // 代收货款应收金额
			cwbMap.put("paybackfee", convertEmptyDouble(order.getReturntaxvalue())); // 上门退货应退金额
			cwbMap.put("cwbordertypeid", String.valueOf(cwbordertypeid));
			cwbMap.put("cargosize", size); // 尺寸
			cwbMap.put("sendcarname", convertEmptyString(sendcarname)); // 发货货物名称
			cwbMap.put("sendcarnum", order.getBoxes()); // 发货箱数
			cwbMap.put("remark1", remark1); // 备注信息
			cwbMap.put("remark2", convertEmptyString(order.getPurchaseNO()));
			cwbMap.put("consigneeno", convertEmptyString(order.getPurchaseNO()));
			cwbMap.put("customercommand", convertEmptyString(order.getSendtime())); // 客户要求
			cwbList.add(cwbMap);

		}
		return cwbList;
	}
	
	private static String convertEmptyString(String str) {
		String returnStr = str == null ? "" : str;
		return returnStr;
	}
	
	private static String convertEmptyDouble(String str) {
		String returnStr = str == null ? "0" : str;
		return returnStr;
	}

	public String CancelOrders() {
		String str = "";
		/*
		 * try {
		 * 
		 * int isOpenFlag =
		 * this.jointService.getStateForJoint(B2cEnum.Zhongliang.getKey());
		 * 
		 * if (isOpenFlag == 0) { this.logger.info("未开启中粮对接！");
		 * 
		 * return "未开启中粮对接！"; } Zhongliang zl =
		 * this.getZhongliang(B2cEnum.Zhongliang.getKey()); Map<String, String>
		 * map = this.checkData(zl, "down"); str =
		 * RestHttpServiceHanlder.sendHttptoServer(map,
		 * zl.getCancleOrder_url()); this.logger.info("中粮取消订单接口_下载 message={}",
		 * str); Response_CancleOrder cancleOrder = (Response_CancleOrder)
		 * ObjectUnMarchal.XmltoPOJO(str, new Response_CancleOrder());
		 * 
		 * String xml = "<Request service=\"CancleOrder\"  lang=\"zh-CN\">" +
		 * "<Head>client</Head>" + "<Body>"; List<Response_CancleOrder_Order>
		 * orders = cancleOrder.getResponse_CancleOrder_body().getOrder(); if
		 * (orders != null) { for (Response_CancleOrder_Order order : orders) {
		 * 
		 * String DealStatus = "1"; String Reason = "订单取消成功"; CwbOrder co =
		 * this.cwbDAO.getCwbByCwb(order.getSendOrderID()); if (co == null) {
		 * Reason = "订单[已经]取消成功"; } else { co =
		 * this.cwbOrderService.tuihuoHandleVipshop
		 * (this.userDAO.getAllUserByid(1), order.getSendOrderID(),
		 * order.getSendOrderID(), 0);
		 * this.logger.info("中粮取消订单接口_订单已经取消,cwb={}", co.getCwb()); } xml +=
		 * "<Order>" + "<SendOrderID>" + order.getSendOrderID() +
		 * "</SendOrderID>" + "<DealStatus>" + DealStatus + "</DealStatus>" +
		 * "<Reason>" + Reason + "</Reason>" + "</Order>"; // return str; } xml
		 * += "</Body>" + "</Request>"; String RequestType = "up"; Map<String,
		 * String> request = this.checkData(zl, RequestType); request.put("XML",
		 * xml); this.logger.info("中粮取消订单接口_发送 message={}", xml); String back =
		 * RestHttpServiceHanlder.sendHttptoServer(request,
		 * zl.getCancleOrder_url()); this.logger.info("中粮取消订单接口_返回 ={}", back);
		 * } else { this.logger.info("没有获取到中粮[意向单]取消订单接口_订单信息 ={}", str); } }
		 * catch (Exception e) {
		 * 
		 * this.logger.error("中粮取消订单接口_异常", e); }
		 */
		return str;
	}

	public List<B2cEnum> getB2cEnum(String method) {
		List<B2cEnum> b2cEnums = new ArrayList<B2cEnum>();
		for (B2cEnum b2c : B2cEnum.values()) {
			if (b2c.getMethod().contains(method)) {
				b2cEnums.add(b2c);
			}
		}
		return b2cEnums;
	}
	
	/**
	 * 本地验证测试用
	 */
	public void waitOrderTest(String str) {

		B2cEnum b2c=B2cEnum.Zhongliang;
		
			this.logger.info("" + b2c.getText() + "接收订单接口_订单返回明细_str==={}", str);
			try {
				Response_WaitOrder response = (Response_WaitOrder) ObjectUnMarchal.XmltoPOJO(str, new Response_WaitOrder());
				List<Response_Order> orders = response.getResponse_body().getOrder();
				if (str.contains("Error") || (orders == null)) {
					this.logger.info("没有获取到" + b2c.getText() + "的订单明细：" + str);
					return;
				}
				List<Map<String, String>> orderlist = this.parseCwbArrByOrderDto(orders);
				if (orderlist != null) {
					long warehouseid = 1;

					this.dataImportInterface.Analizy_DataDealByB2c(124, b2c.getMethod(), orderlist, warehouseid, true);
				
				}
			} catch (Exception e) {
				this.logger.error("" + b2c.getText() + "接收订单接口_插入临时表对接异常", e);
			}
		
	}
}
