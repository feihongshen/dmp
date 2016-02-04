package cn.explink.b2c.zhongliang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.tools.ObjectUnMarchal;
import cn.explink.b2c.tools.RestHttpServiceHanlder;
import cn.explink.b2c.tools.b2cmonntor.B2cAutoDownloadMonitorDAO;
import cn.explink.b2c.zhongliang.backxml.BackOrder;
import cn.explink.b2c.zhongliang.backxml.Goods;
import cn.explink.b2c.zhongliang.backxml.Order;
import cn.explink.b2c.zhongliang.xml.Response_CancleOrder;
import cn.explink.b2c.zhongliang.xml.Response_CancleOrder_Order;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.service.CwbOrderService;

@Service
public class ZhongliangBackOrderServices {
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

	public void backOrder() {
		List<B2cEnum> b2cEnums = this.getB2cEnum(B2cEnum.Zhongliang.getMethod());

		for (B2cEnum b2c : b2cEnums) {
			int isOpenFlag = this.jointService.getStateForJoint(b2c.getKey());

			if (isOpenFlag == 0) {
				this.logger.info("未开中粮我买网对接！");
				continue;
			}

			Zhongliang zl = this.getZhongliang(b2c.getKey());
			String RequestType = "down";
			Map<String, String> map = this.checkData(zl, RequestType);
			String str = RestHttpServiceHanlder.sendHttptoServer(map, zl.getBackOrder_url());
			this.logger.info("中粮[意向单]下载订单接口_订单下载明细_str==={}", str);
			try {
				BackOrder response = (BackOrder) ObjectUnMarchal.XmltoPOJO(str, new BackOrder());
				List<Order> orders = response.getBody().getOrder();
				if (str.contains("Error") || (orders == null)) {
					this.logger.info("没有获取到中粮[意向单]的订单明细：" + str);
					continue;
				}
				List<Map<String, String>> orderlist = this.parseCwbArrByOrderDto(orders);
				if (orderlist != null) {
					long warehouseid = zl.getWarehouseid(); // 订单导入的库房Id

					this.dataImportInterface.Analizy_DataDealByB2c(Long.parseLong(zl.getCustomerid()), B2cEnum.Zhongliang.getMethod(), orderlist, warehouseid, true);
					String data = "<Request service= \"BackOrder\" lang=\"zh-CN\">" + "<Head> client</Head>" + "<Body>";
					for (Order order : orders) {
						CwbOrderDTO cwbOrder = this.dataImportDAO_B2c.getCwbByCwbB2ctemp(order.getSendOrderID());
						// String MailNO="";
						if (cwbOrder != null) {

							this.logger.info("中粮[意向单]接收订单接口_导入订单临时表_成功,cwb={}", order.getSendOrderID());
						} else {
							this.logger.info("中粮[意向单]接收订单接口_导入订单临时表_失败,cwb={}", order.getSendOrderID());
						}
						String DealStatus = cwbOrder == null ? "0" : "1";
						data += "<Order>" + "<SendOrderID>" + order.getSendOrderID() + "</SendOrderID>" + "<MailNO>" + order.getSendOrderID() + "</MailNO>" + "<DealStatus>" + DealStatus
								+ "</DealStatus>" + "<Reason></Reason >" + " </Order>";
					}
					data += "</Body>" + "</Request> ";
					Map<String, String> map_up = this.checkData(zl, "up");
					this.logger.info("中粮[意向单]接收订单接口_发送={}", data);
					map_up.put("XML", data);
					String back = RestHttpServiceHanlder.sendHttptoServer(map_up, zl.getBackOrder_url());
					this.logger.info("中粮[意向单]接收订单接口_返回 ={}", back);
				}
			} catch (Exception e) {
				this.logger.error("中粮[意向单]接收订单接口_插入临时表对接异常", e);
			}

		}
	}

	public void CancelOrders() {
		List<B2cEnum> b2cEnums = this.getB2cEnum(B2cEnum.Zhongliang.getMethod());

		for (B2cEnum b2c : b2cEnums) {
			String str = "";
			try {

				int isOpenFlag = this.jointService.getStateForJoint(b2c.getKey());

				if (isOpenFlag == 0) {
					this.logger.info("未开启中粮对接！");

					continue;
				}
				Zhongliang zl = this.getZhongliang(b2c.getKey());
				Map<String, String> map = this.checkData(zl, "down");
				str = RestHttpServiceHanlder.sendHttptoServer(map, zl.getBackCancel_url());
				this.logger.info("中粮[意向单]取消订单接口_下载数据,message={}", str);
				Response_CancleOrder cancleOrder = (Response_CancleOrder) ObjectUnMarchal.XmltoPOJO(str, new Response_CancleOrder());

				String xml = "<Request service=\"BackCancel\"  lang=\"zh-CN\">" + "<Head>client</Head>" + "<Body>";
				List<Response_CancleOrder_Order> orders = cancleOrder.getResponse_CancleOrder_body().getOrder();
				if (orders != null) {
					for (Response_CancleOrder_Order order : orders) {
						String DealStatus = "1";
						String Reason = "订单取消成功";
						CwbOrder co = this.cwbDAO.getCwbByCwb(order.getSendOrderID());
						if (co == null) {
							Reason = "订单[已经]取消成功";
						} else {
							co = this.cwbOrderService.tuihuoHandleVipshop(this.userDAO.getAllUserByid(1), order.getSendOrderID(), order.getSendOrderID(), 0);
							this.logger.info("中粮[意向单]取消订单接口_订单已经取消,cwb={}", co.getCwb());
						}

						xml += "<Order>" + "<SendOrderID>" + order.getSendOrderID() + "</SendOrderID>" + "<DealStatus>" + DealStatus + "</DealStatus>" + "<Reason>" + Reason + "</Reason>" + "</Order>";
						// return str;
					}
					xml += "</Body>" + "</Request>";
					String RequestType = "up";
					Map<String, String> request = this.checkData(zl, RequestType);
					request.put("XML", xml);
					this.logger.info("中粮[意向单]取消订单接口_发送数据,message={}", xml);
					String back = RestHttpServiceHanlder.sendHttptoServer(request, zl.getBackCancel_url());
					this.logger.info("中粮[意向单]取消订单接口_返回 ={}", back);
				} else {
					this.logger.info("没有获取到中粮[意向单]取消订单接口_订单信息 ={}", str);
				}
			} catch (Exception e) {

				this.logger.error("中粮[意向单]取消订单接口_异常", e);
			}
		}
	}

	public List<Map<String, String>> parseCwbArrByOrderDto(List<Order> orders) {
		List<Map<String, String>> cwbList = new ArrayList<Map<String, String>>();
		for (Order order : orders) {
			CwbOrderDTO cwbOrder = this.dataImportDAO_B2c.getCwbByCwbB2ctemp(order.getSendOrderID());
			if (cwbOrder != null) {
				this.logger.warn("中粮[意向单]接收订单接口_获取中粮[意向单]订单中含有重复数据,cwb={}", cwbOrder.getCwb());
				continue;
			}

			String orderType = order.getOrdertype();// 获取订单类型
			int cwbordertypeid = CwbOrderTypeIdEnum.Shangmentui.getValue();
			if ("85".equals(orderType)) {
				cwbordertypeid = CwbOrderTypeIdEnum.Shangmentui.getValue();
			} else if ("90".equals(orderType) || "95".equals(orderType)) {
				cwbordertypeid = CwbOrderTypeIdEnum.Shangmentui.getValue();
			}
			int paywayid = 1;

			if (order.getPaytype().contains("pos")) {
				paywayid = PaytypeEnum.Pos.getValue();
			} else if (order.getPaytype().contains("支票")) {
				paywayid = PaytypeEnum.Zhipiao.getValue();
			} else {
				paywayid = PaytypeEnum.Xianjin.getValue();
			}
			String sendcarname = "";
			String size = "";
			String remark1 = order.getOrderTemark();
			if (remark1 != null) {
				if (remark1.length() > 100) {
					remark1 = remark1.substring(0, 100);
				}
			}
			List<Goods> goods = order.getGoodDetail().getGoods();
			for (Goods good : goods) {
				sendcarname += good.getGoodsName() + ",";
				size += good.getUnitName() + ",";
			}
			String address = "";
			address += order.getProvincename() == null ? "" : order.getProvincename();
			address += order.getCityname() == null ? "" : order.getCityname();
			address += order.getAreaname() == null ? "" : order.getAreaname();
			address += order.getAddress() == null ? "" : order.getAddress();
			Map<String, String> cwbMap = new HashMap<String, String>();
			cwbMap.put("cwb", order.getSendOrderID()); // 物流订单号码
			cwbMap.put("transcwb", order.getOrderID()); // 运单号
			cwbMap.put("consigneename", order.getLinkman());// 收件人姓名
			cwbMap.put("paywayid", paywayid + "");// 支付方式
			cwbMap.put("consigneephone", order.getTelno());
			cwbMap.put("consigneemobile", order.getHandsetno());
			cwbMap.put("consigneepostcode", order.getPostalcode());
			// cwbMap.put("cargorealweight", order.getPackweight()); // 订单重量(KG)
			cwbMap.put("cwbprovince", order.getProvincename());// 省
			cwbMap.put("cwbcity", order.getCityname());// 市
			cwbMap.put("consigneeaddress", address);
			cwbMap.put("caramount", order.getOrdervalue());// 订单金额
			// cwbMap.put("cargotype", order.getValuableflag());// 货物金额
			// cwbMap.put("receivablefee", order.getGetvalue()); // 代收货款应收金额
			cwbMap.put("paybackfee", Math.abs(Double.valueOf(order.getReturntaxvalue()))+""); // 上门退货应退金额
			cwbMap.put("cwbordertypeid", String.valueOf(cwbordertypeid));
			cwbMap.put("cargosize", size); // 尺寸
			cwbMap.put("sendcarname", sendcarname); // 发货货物名称
			// cwbMap.put("sendcarnum", order.getBoxes()); // 发货箱数
			cwbMap.put("remark1", remark1); // 备注信息
			cwbMap.put("customercommand", order.getSendtime()); // 客户要求
			cwbMap.put("remark2", order.getPurchaseNO());
			cwbMap.put("consigneeno", order.getPurchaseNO());
			cwbList.add(cwbMap);

		}
		return cwbList;
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

	public void BackOrdercounts() {
		Zhongliang zl = this.getZhongliang(B2cEnum.Zhongliang.getKey());
		int count = 0;
		if ((zl != null) && (zl.getNums() != null) && !zl.getNums().equals("")) {
			count = Integer.parseInt(zl.getNums());
		} else {
			count = 20;
		}
		for (int i = 0; i < count; i++) {
			this.backOrder();
		}

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
}
