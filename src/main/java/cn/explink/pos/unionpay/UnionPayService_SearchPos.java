package cn.explink.pos.unionpay;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.JiontDAO;
import cn.explink.b2c.tools.RestHttpServiceHanlder;
import cn.explink.b2c.tools.poscodeMapp.PoscodeMapp;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.pos.tools.JacksonMapper;
import cn.explink.pos.tools.PosEnum;
import cn.explink.pos.unionpay.json.Reqdata;
import cn.explink.pos.unionpay.json.RespContent;
import cn.explink.pos.unionpay.json.Respdata;
import cn.explink.util.MD5.Base64Utils;
import cn.explink.util.MD5.MD5Util;

/**
 * 易派主动查询POS机 -根据订单查询支付结果
 * 
 * @author Administrator
 *
 */
@Service
public class UnionPayService_SearchPos extends UnionPayService {

	private Logger logger = LoggerFactory.getLogger(UnionPayService_SearchPos.class);
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	JiontDAO jointDAO;

	/**
	 * 根据订单号查询荣邦银联
	 * 
	 * @return
	 */
	public String cwbSearchtoPos(String cwb) {
		try {
			String returnCode = "";

			int isOpenFlag = jointDAO.getStateByJointKey(PosEnum.UnionPay.getKey());
			if (isOpenFlag == 0) {
				returnCode = "未开启UnionPay对接";
			}

			UnionPay unionpay = getUnionPaySettingMethod(PosEnum.UnionPay.getKey());
			CwbOrder cwbOrder = cwbDAO.getCwbByCwb(cwb);
			if (cwbOrder == null) {
				returnCode = "订单号" + cwb + "在系统中不存在";
			}

			String command = UnionPayEnum.SearchPos.getCommand();

			Reqdata reqdata = buildReqdatas(cwb, cwbOrder); // 构建请求参数 bean

			String data = JacksonMapper.getInstance().writeValueAsString(reqdata); // 转化为json

			String base64_data = Base64Utils.encode(data.replaceAll("+", "_").replaceAll("_", "/").getBytes("UTF-8"));

			String sign = MD5Util.md5(base64_data + unionpay.getPrivate_key()).substring(8, 24); // 16位的md5加密

			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("command", command);
			paramsMap.put("data", data);
			paramsMap.put("sign", sign);

			String responseData = RestHttpServiceHanlder.sendHttptoServer(paramsMap, unionpay.getRequestPosUrl());

			Respdata respdata = JacksonMapper.getInstance().readValue(responseData, Respdata.class);
			String response = respdata.getResponse();// 应答码
			String message = respdata.getMessage();// 应答消息
			int totalrecord = respdata.getTotalrecord();// 总记录数
			RespContent content = respdata.getRespContent();
			/**
			 * RespContent content =respdata.getRespContent(); 明细字段，这里就不做详细解析了。
			 */

		} catch (Exception e) {
			logger.error("请求unionpay查询POS未知异常", e);
		}

		return null;
	}

	private Reqdata buildReqdatas(String cwb, CwbOrder cwbOrder) {
		Reqdata reqdata = new Reqdata();
		reqdata.setOrdernum(cwb);
		reqdata.setCompanyid(0);
		String customercode = "";
		PoscodeMapp poscode = poscodeMappDAO.getPosCodeByKey(cwbOrder.getCustomerid(), PosEnum.UnionPay.getKey());
		if (poscode != null) {
			customercode = poscode.getCustomercode();
		}
		reqdata.setLogisticsid(Integer.valueOf(customercode));
		return reqdata;
	}

}
