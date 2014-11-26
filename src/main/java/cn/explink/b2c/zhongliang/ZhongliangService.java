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

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportDAO_B2c;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.b2c.tools.ObjectUnMarchal;
import cn.explink.b2c.tools.RestHttpServiceHanlder;
import cn.explink.b2c.tools.b2cmonntor.B2cAutoDownloadMonitorDAO;
import cn.explink.b2c.zhongliang.xml.Response_CancleOrder;
import cn.explink.b2c.zhongliang.xml.Response_CancleOrder_Order;
import cn.explink.b2c.zhongliang.xml.Response_GoodDetail;
import cn.explink.b2c.zhongliang.xml.Response_Order;
import cn.explink.b2c.zhongliang.xml.Response_WaitOrder;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.enumutil.CwbOrderTypeIdEnum;

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

	public Zhongliang getZhongliang(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Zhongliang smile = (Zhongliang) JSONObject.toBean(jsonObj, Zhongliang.class);
		return smile;
	}

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

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
		zl.setNums(request.getParameter("nums"));
		zl.setCustomerid(request.getParameter("customerid"));
		zl.setWarehouseid(Long.parseLong(request.getParameter("warehouseid")));
		String oldCustomerids = "";
		JSONObject jsonObj = JSONObject.fromObject(zl);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getZhongliang(joint_num).getClientId();
			} catch (Exception e) {
				logger.error("设置中粮对接异常", e);
			}
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Update(jointEntity);
			logger.info("设置中粮对接成功！");
		}
		// 保存 枚举到供货商表中
		customerDAO.updateB2cEnumByJoint_num(customerid, oldCustomerids, joint_num);
	}

	public void update(int joint_num, int state) {
		jiontDAO.UpdateState(joint_num, state);
	}

	public String waitOrder() {
		int isOpenFlag = jointService.getStateForJoint(B2cEnum.Zhongliang.getKey());

		if (isOpenFlag == 0) {
			logger.info("未开中粮我买网对接！");
			return "";
		}

		Zhongliang zl = getZhongliang(B2cEnum.Zhongliang.getKey());
		String RequestType = "down";
		Map<String, String> map = checkData(zl, RequestType);
		String str = RestHttpServiceHanlder.sendHttptoServer(map, zl.getWaitOrder_url());
		try {
			Response_WaitOrder response = (Response_WaitOrder) ObjectUnMarchal.XmltoPOJO(str, new Response_WaitOrder());
			List<Response_Order> orders = response.getResponse_body().getOrder();
			if (str.contains("Error") && orders == null) {
				logger.info("没有获取到中粮的订单明细：" + str);
				return str;
			}
			List<Map<String, String>> orderlist = parseCwbArrByOrderDto(orders);
			if (orderlist != null) {
				long warehouseid = zl.getWarehouseid(); // 订单导入的库房Id

				dataImportInterface.Analizy_DataDealByB2c(Long.parseLong(zl.getCustomerid()), B2cEnum.Zhongliang.getMethod(), orderlist, warehouseid, true);

				logger.info("中粮导入订单临时表成功");

			}
		} catch (Exception e) {
			logger.error("中粮对接插入临时表对接异常", e);
		}

		return str;
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
			CwbOrderDTO cwbOrder = dataImportDAO_B2c.getCwbByCwbB2ctemp(order.getSendOrderID());
			if (cwbOrder != null) {
				logger.warn("获取中粮订单中含有重复数据cwb={}", cwbOrder.getCwb());
				continue;
			}

			String orderType = order.getOrdertype();// 获取订单类型
			int cwbordertypeid = 1;
			if ("70".equals(orderType) || "75".equals(orderType) || "80".equals(orderType)) {
				cwbordertypeid = CwbOrderTypeIdEnum.Peisong.getValue();
			} else if ("90".equals(orderType) || "85".equals(orderType) || "95".equals(orderType)) {
				cwbordertypeid = CwbOrderTypeIdEnum.Shangmentui.getValue();
			} else {
				cwbordertypeid = CwbOrderTypeIdEnum.Weiqueding.getValue();
			}
			double caramount = 0;
			String sendcarname = "";
			List<Response_GoodDetail> goods = order.getGoodDetail();
			for (Response_GoodDetail good : goods) {
				caramount += Double.parseDouble(good.getPrice());
				sendcarname += good.getGoodsName() + ",";
			}
			Map<String, String> cwbMap = new HashMap<String, String>();
			cwbMap.put("cwb", order.getSendOrderID()); // 物流订单号码
			cwbMap.put("transcwb", order.getOrderID()); // 运单号
			cwbMap.put("consigneename", order.getLinkman());// 收件人姓名
			cwbMap.put("paywayid", order.getPaytype());// 支付方式
			cwbMap.put("consigneephone", order.getTelno());
			cwbMap.put("consigneemobile", order.getHandsetno());
			cwbMap.put("consigneepostcode", order.getPostalcode());
			cwbMap.put("cargorealweight", order.getPackweight()); // 订单重量(KG)
			cwbMap.put("cwbprovince", order.getProvincename());// 省
			cwbMap.put("cwbcity", order.getCityname());// 市
			cwbMap.put("consigneeaddress", order.getAddress());
			cwbMap.put("caramount", String.valueOf(caramount));// 货物金额
			cwbMap.put("cartype", order.getValuableflag());// 货物金额
			cwbMap.put("receivablefee", order.getGetvalue()); // 代收货款应收金额
			cwbMap.put("paybackfee", order.getReturntaxvalue()); // 上门退货应退金额
			cwbMap.put("cwbordertypeid", String.valueOf(cwbordertypeid));
			// cwbMap.put("cargosize",size); //尺寸
			cwbMap.put("sendcarname", sendcarname); // 发货货物名称
			cwbMap.put("remark1", order.getOrderTemark()); // 备注信息
			cwbMap.put("customercommand", order.getSendtime()); // 客户要求
			cwbList.add(cwbMap);

		}
		return cwbList;
	}

	public String CancelOrders() {

		try {

			int isOpenFlag = jointService.getStateForJoint(B2cEnum.Zhongliang.getKey());

			if (isOpenFlag == 0) {
				logger.info("未开启中粮对接！");

				return "未开启中粮对接！";
			}
			Zhongliang zl = getZhongliang(B2cEnum.Zhongliang.getKey());
			Map<String, String> map = checkData(zl, "down");
			String str = RestHttpServiceHanlder.sendHttptoServer(map, zl.getCancleOrder_url());
			Response_CancleOrder cancleOrder = (Response_CancleOrder) ObjectUnMarchal.XmltoPOJO(str, new Response_CancleOrder());
			for (Response_CancleOrder_Order order : cancleOrder.getResponse_CancleOrder_body().getOrder()) {
				CwbOrder cwbOrder = cwbDAO.getCwbByCwb(order.getSendOrderID());
				String DealStatus = "1";
				String Reason = "";
				if (cwbOrder == null) {
					DealStatus = "2";
					Reason = "订单已经取消";
					logger.info("订单已经取消,cwb={}", order.getSendOrderID());
				}
				cwbDAO.dataLoseByCwb(order.getSendOrderID());
				dataImportDAO_B2c.dataLoseB2ctempByCwb(order.getSendOrderID());
				String xml = "<Request service=\"CancleOrder\"  lang=\"zh-CN\">" + "<Head>client</Head>" + "<Body>" + "<Order>" + "<SendorderID>" + order.getSendOrderID() + "</SendorderID>"
						+ "<DealStatus>" + DealStatus + "</DealStatus>" + "<Reason>" + Reason + "</Reason>" + "</Order>" + "</Body>" + "</Request>";
				String RequestType = "up";
				Map<String, String> request = checkData(zl, RequestType);
				request.put("XML", xml);
				String back = RestHttpServiceHanlder.sendHttptoServer(request, zl.getCancleOrder_url());
				logger.info("订单取消返回 ={},cwb={}", back, order.getSendOrderID());
				return str;
			}
		} catch (Exception e) {

			logger.error("获取中粮取消订单异常", e);
		}
		return "";
	}

}
