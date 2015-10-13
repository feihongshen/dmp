package cn.explink.pos.mobileapp_dcb;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cTools;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.enumutil.pos.PosEnum;
import cn.explink.jms.dto.CwbOrderWithDeliveryState;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.RestHttpServiceHanlder;

/**
 * 手机App 实体类 version=大晨报
 * 
 * @author Administrator
 * 
 */
@Service
public class MobiledcbService {
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	GetDmpDAO getDmpDAO;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public void BuildDeliveryingServiceSend_dcb(CwbOrderWithDeliveryState cwbOrderWithDeliveryState, DmpOrderFlow orderFlow) {
		try {
			DmpCwbOrder cwbOrder = cwbOrderWithDeliveryState.getCwbOrder();

			User deliverUser = getDmpDAO.getUserById(cwbOrder.getDeliverid());
			Mobiledcb etong = this.getMobiledcb(PosEnum.MobileApp_dcb.getKey());

			String logparms = "cwb=" + cwbOrder.getCwb() + ",consigneeaddress=" + cwbOrder.getConsigneeaddress() + ",receivablefee=" + cwbOrder.getReceivablefee() + ",consigneemobile="
					+ cwbOrder.getConsigneemobile() + ",sendcarname=" + cwbOrder.getSendcarname() + ",cwbremark=" + cwbOrder.getCwbremark() + ",customercommand=" + cwbOrder.getCustomercommand()
					+ ",usermobile=" + deliverUser.getUsermobile();

			logger.info("订单派件分配派送员-推送【大晨报】派送员={},parms={}", deliverUser.getRealname(), logparms);

			Map<String, String> sendMap = buildSendParams(orderFlow, cwbOrder, deliverUser); // 填充请求参数

			String response = RestHttpServiceHanlder.sendHttptoServer(sendMap, etong.getSender_url());

			logger.info("订单派件分配派送员-【大晨报】返回={},cwb={}", response, cwbOrder.getCwb());
		} catch (Exception e) {
			logger.error("订单派件分配派送员-【大晨报】发生异常cwb=" + orderFlow.getCwb(), e);
		}

	}

	private Map<String, String> buildSendParams(DmpOrderFlow orderFlow, DmpCwbOrder cwbOrder, User deliverUser) {
		Map<String, String> sendMap = new HashMap<String, String>();
		sendMap.put("cwb", cwbOrder.getCwb());
		sendMap.put("deliver", deliverUser.getUsermobile());
		sendMap.put("orderdate", DateTimeUtil.formatDate(orderFlow.getCredate()));
		sendMap.put("consigneeaddress", cwbOrder.getConsigneeaddress());
		sendMap.put("consigneename", cwbOrder.getConsigneename());
		sendMap.put("receivablefee", String.valueOf(cwbOrder.getReceivablefee()));
		sendMap.put("consigneemobile", cwbOrder.getConsigneemobile());
		sendMap.put("sendcargoname", cwbOrder.getSendcarname());
		sendMap.put("cwbremark", cwbOrder.getCwbremark());

		sendMap.put("customercommand", cwbOrder.getCustomercommand());
		sendMap.put("domain", "");
		return sendMap;
	}

	// 获取配置信息
	public Mobiledcb getMobiledcb(int key) {
		Mobiledcb et = null;
		String objectMethod = b2ctools.getObjectMethod(key).getJoint_property();
		if (objectMethod != null) {
			JSONObject jsonObj = JSONObject.fromObject(objectMethod);
			et = (Mobiledcb) JSONObject.toBean(jsonObj, Mobiledcb.class);
		} else {
			et = new Mobiledcb();
		}
		return et;
	}

}
