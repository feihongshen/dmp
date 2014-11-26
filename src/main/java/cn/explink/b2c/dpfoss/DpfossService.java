package cn.explink.b2c.dpfoss;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.dpfoss.waybill.QueryWayBillRequest;
import cn.explink.b2c.dpfoss.waybill.QueryWayBillResponse;
import cn.explink.b2c.explink.ExplinkService;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.DataImportService_B2c;
import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.JointEntity;
import cn.explink.b2c.tools.JointService;
import cn.explink.controller.CwbOrderDTO;
import cn.explink.dao.CustomWareHouseDAO;
import cn.explink.dao.CustomerDAO;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.dao.OrderFlowDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.util.MD5.MD5Util;

@Service
public class DpfossService {
	private Logger logger = LoggerFactory.getLogger(DpfossService.class);
	private static HttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
	static {
		httpConnectionManager.getParams().setConnectionTimeout(30 * 1000);
		httpConnectionManager.getParams().setSoTimeout(60 * 1000);
	}

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
	JointService jointService;

	public String getObjectMethod(int key) {
		JointEntity obj = jiontDAO.getJointEntity(key);
		return obj == null ? null : obj.getJoint_property();
	}

	public Dpfoss getDpfoss(int key) {
		if (getObjectMethod(key) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethod(key));
		Dpfoss smile = (Dpfoss) JSONObject.toBean(jsonObj, Dpfoss.class);
		return smile;
	}

	public String getObjectMethods(String keys, String customerid) {
		JointEntity obj = jiontDAO.getJointEntityByKeys(keys, customerid);
		return obj == null ? null : obj.getJoint_property();
	}

	public Dpfoss getDpfossKeys(String keys, String customerid) {
		if (getObjectMethods(keys, customerid) == null) {
			return null;
		}
		JSONObject jsonObj = JSONObject.fromObject(getObjectMethods(keys, customerid));
		Dpfoss smile = (Dpfoss) JSONObject.toBean(jsonObj, Dpfoss.class);
		return smile;
	}

	/**
	 * 是否开启对接
	 * 
	 * @param keys
	 *            ,String customerid
	 * @return
	 */
	public int getStateForJoints(String keys, String customerid) {
		JointEntity obj = null;
		int state = 0;
		try {
			obj = jiontDAO.getJointEntityByKeys(keys, customerid);
			state = obj.getState();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return state;
	}

	public void edit(HttpServletRequest request, int joint_num) {
		Dpfoss dp = new Dpfoss();
		String customerids = request.getParameter("customerids");
		dp.setCustomerids(customerids);
		dp.setAgent(request.getParameter("agent"));
		dp.setAgentCompanyCode(request.getParameter("agentCompanyCode"));
		dp.setAgentCompanyName(request.getParameter("agentCompanyName"));

		dp.setPrivate_key(request.getParameter("private_key"));
		dp.setPwd(request.getParameter("pwd"));
		dp.setWarehouseid(Long.valueOf(request.getParameter("warehouseid")));
		dp.setMaxCount(Integer.valueOf(request.getParameter("maxCount")));

		dp.setAgentOrgCode(request.getParameter("agentOrgCode"));
		dp.setAgentOrgName(request.getParameter("agentOrgName"));

		dp.setServiceCode_queryWaybill(request.getParameter("serviceCode_queryWaybill"));
		dp.setQueryWaybill_url(request.getParameter("queryWaybill_url"));

		dp.setServiceCode_uploadSign(request.getParameter("serviceCode_uploadSign"));
		dp.setUploadSign_url(request.getParameter("uploadSign_url"));

		dp.setServiceCode_uploadTrack(request.getParameter("serviceCode_uploadTrack"));
		dp.setUploadTrack_url(request.getParameter("uploadTrack_url"));

		String oldCustomerids = "";

		JSONObject jsonObj = JSONObject.fromObject(dp);
		JointEntity jointEntity = jiontDAO.getJointEntity(joint_num);
		if (jointEntity == null) {
			jointEntity = new JointEntity();
			jointEntity.setJoint_num(joint_num);
			jointEntity.setJoint_property(jsonObj.toString());
			jiontDAO.Create(jointEntity);
		} else {
			try {
				oldCustomerids = getDpfoss(joint_num).getCustomerids();
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
	 * 查询外发单接口 德邦数据 下载接口 支持手动调用 返回当前下载订单数量
	 */

	public int cwbOrdersDownloading(String handOverNo, String waybillNo, int b2cKey) {

		Dpfoss dpfoss = getDpfoss(b2cKey);

		HttpClient httpClient = new HttpClient();
		httpClient.setHttpConnectionManager(httpConnectionManager);
		PostMethod postMethod = new PostMethod(dpfoss.getQueryWaybill_url());

		QueryWayBillRequest list = GenerateJsonUtil.generateQueryWayBillRequest(handOverNo, waybillNo);
		// 请求对象转换成JSON
		String request = JsonUtil.fromObject(list);

		postMethod.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
		// 设置请求消息
		postMethod.setRequestBody(request);

		// 在HTTP头中添加设置ESBHeader
		postMethod.addRequestHeader("ESB-Version", "1.0");
		postMethod.addRequestHeader("ESB-ESBServiceCode", dpfoss.getServiceCode_queryWaybill());
		postMethod.addRequestHeader("ESB-UserName", dpfoss.getAgent());
		postMethod.addRequestHeader("ESB-Password", MD5Util.md5(dpfoss.getPwd()));
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(40000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(40000);

		logger.info("请求德邦物流外发单接口request={},url={},ESB-UserName=" + dpfoss.getAgent() + ",ESB-Password=" + MD5Util.md5(dpfoss.getPwd()), request, dpfoss.getServiceCode_queryWaybill());

		try {
			httpClient.executeMethod(postMethod);

			// 打印HTTP头信息
			for (Header header : postMethod.getResponseHeaders()) {
				logger.info("打印头信息" + header.getName() + ": " + header.getValue());

			}
			// 获取执行结果1：成功 0：失败
			Header header = postMethod.getResponseHeader("ESB-ResultCode");
			String result = null;
			if (header != null) {
				result = header.getValue();
			}

			String response = postMethod.getResponseBodyAsString();
			logger.info("德邦物流外发单接口返回response={}", response);

			if (result != null && !result.equals("1")) {
				logger.error("请求德邦物流外发单接口头信息返回异常,ESB-ResultCode=0");
				throw new RuntimeException("请求德邦物流外发单接口头信息返回异常,ESB-ResultCode=0 /" + response);
			}

			// 将返回的JSON格式的响应消息转换成对象
			List<QueryWayBillResponse> responses = JacksonMapper.getInstance().readValue(response, new TypeReference<List<QueryWayBillResponse>>() {
			});

			if (responses == null || responses.size() == 0) {
				logger.warn("德邦物流外发单接口json转化为对象之后没有数据,return");
				throw new RuntimeException("请求德邦物流外发单目前没有数据");
			}

			List<Map<String, String>> orderlist = new ArrayList<Map<String, String>>();

			for (QueryWayBillResponse order : responses) {
				Map<String, String> orderMap = buildOrders(order);
				if (orderMap == null) {
					continue;
				}
				orderlist.add(orderMap);
			}

			try {
				long warehouseid = dpfoss.getWarehouseid();
				List<CwbOrderDTO> extractss = dataImportService_B2c.Analizy_DataDealByB2c(Long.parseLong(dpfoss.getCustomerids()), B2cEnum.DPFoss1.getMethod(), orderlist, warehouseid, true);
				logger.debug("处理0德邦物流0后的订单信息=" + extractss.toString());

				return orderlist.size();

			} catch (Exception e) {
				logger.error("0德邦物流0调用数据导入接口异常!", e);
				throw new RuntimeException("请求德邦物流外发单下载数据成功，但是内部程序处理发生未知异常，请联系易普联科!");
			}

		} catch (Exception e) {
			logger.error("处理请求德邦物流下载外发单接口发生未知异常", e);
			throw new RuntimeException("处理请求德邦物流下载外发单接口发生未知异常:" + e.getMessage());

		} finally {
			postMethod.releaseConnection();
		}

	}

	private Map<String, String> buildOrders(QueryWayBillResponse order) {
		Map<String, String> orderMap = new HashMap<String, String>();

		String waybillNo = order.getWaybillNo();
		CwbOrder cwbOrder = cwbDAO.getCwbByCwb(waybillNo);
		if (cwbOrder != null) {
			logger.warn("获取0德邦物流0订单中含有重复数据cwb={}", waybillNo);
			return null;
		}

		String payType = order.getPayType(); // 支付方式
		String paytypeid = getPayType(payType);

		String ExternalUserName = order.getExternalUserName();
		String ExternalUserCode = order.getExternalUserCode();
		String ExternalOrgName = order.getExternalOrgName();
		String ExternalOrgCode = order.getExternalOrgCode();
		String AgentCompanyName = order.getAgentCompanyName();
		String AgentCompanyCode = order.getAgentCompanyCode();
		String PickupType = order.getPickupType(); // 提货方式
		String ReturnType = order.getReturnType(); // 返单类型

		String remark1 = "外发员姓名:" + ExternalUserName + ",外发员工号:" + ExternalUserCode + ",外发部门名称:" + ExternalOrgName + ",外发部门编码:" + ExternalOrgCode + ",代理公司名称:" + AgentCompanyName + ",代理公司编码:"
				+ AgentCompanyCode + ",提货方式:" + PickupType + ",返单类型:" + ReturnType;

		String ReceiveName = order.getReceiveName() == null ? "" : order.getReceiveName();
		String ReceiveAddr = order.getReceiveAddr() == null ? "" : order.getReceiveAddr();
		String ReceivePhone = order.getReceivePhone() == null ? "" : order.getReceivePhone();
		BigDecimal FreightFee = order.getFreightFee() == null ? BigDecimal.ZERO : order.getFreightFee(); // 外发运费
		BigDecimal codAmount = order.getCodAmount() == null ? BigDecimal.ZERO : order.getCodAmount(); // 代收款
		BigDecimal PayDpFee = order.getPayDpFee(); // 到付费（元）

		BigDecimal receivablefee = codAmount.add(PayDpFee);

		BigDecimal DeclarationValue = order.getDeclarationValue() == null ? BigDecimal.ZERO : order.getDeclarationValue(); // 货物声明价值（元）
		int GoodsNum = order.getGoodsNum();
		BigDecimal Weight = order.getWeight() == null ? BigDecimal.ZERO : order.getWeight();
		BigDecimal Volume = order.getVolume() == null ? BigDecimal.ZERO : order.getVolume();
		String Notes = order.getNotes();
		String AuditStatus = order.getAuditStatus(); // 外发单状态
		// String ModifyTime=DateTimeUtil.formatDate(order.getModifyTime());
		// //修改时间

		String FreightFeeremark = "外发运费:" + FreightFee + ",到付费:" + PayDpFee;

		orderMap.put("cwb", waybillNo);
		orderMap.put("paywayid", paytypeid);
		orderMap.put("consigneename", ReceiveName);
		orderMap.put("consigneeaddress", ReceiveAddr);
		orderMap.put("consigneephone", ReceivePhone);
		orderMap.put("consigneemobile", ReceivePhone);
		orderMap.put("receivablefee", String.valueOf(receivablefee));
		orderMap.put("cargoamount", String.valueOf(DeclarationValue));
		orderMap.put("sendcarnum", String.valueOf(GoodsNum));

		orderMap.put("cargorealweight", String.valueOf(Weight));
		orderMap.put("cargovolume", String.valueOf(Volume));
		orderMap.put("cwbremark", Notes);

		orderMap.put("customercommand", FreightFeeremark);
		orderMap.put("remark5", remark1);
		orderMap.put("remark1", "外发单状态:" + AuditStatus);
		return orderMap;
	}

	/**
	 * 获取支付方式
	 * 
	 * @param payType
	 * @return
	 */
	private String getPayType(String payType) {
		for (DpfossPayEnum em : DpfossPayEnum.values()) {
			if (payType.equals(em.CH.getPaycode())) {
				return PaytypeEnum.Xianjin.getValue() + "";
			}
			if (payType.equals(em.NT.getPaycode())) {
				return PaytypeEnum.Zhipiao.getValue() + "";
			}
			if (payType.equals(em.CD.getPaycode())) {
				return PaytypeEnum.Pos.getValue() + "";
			}
		}

		return PaytypeEnum.Xianjin.getValue() + "";
	}

}
