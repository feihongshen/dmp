package cn.explink.pos.alipay;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.domain.CwbOrder;
import cn.explink.pos.alipay.xml.Transaction;
import cn.explink.pos.tools.PosEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.pos.RSACoder;

@Service
public class AlipayService_toBackOutFinish extends AlipayService {
	private Logger logger = LoggerFactory.getLogger(AlipayService_toBackOutFinish.class);

	/**
	 * 分单撤销完成结果反馈
	 * 
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	public String toBackOutFinish(Transaction rootnote, AliPay alipay) {
		String resp_code = "";
		String resp_msg = "";
		CwbOrder cwbOrder = null;
		long deliverid = 0;
		String podremark = "";
		String order_no = rootnote.getTransaction_Body().getOrder_no(); // 订单号
		String delivery_man = rootnote.getTransaction_Header().getExt_attributes().getDelivery_man();
		deliverid = getUserIdByUserName(delivery_man);
		cwbOrder = cwbDAO.getCwbDetailByCwbAndDeliverId(deliverid, order_no);

		if (cwbOrder == null) {
			resp_code = "03";
			resp_msg = "没有检索到数据";
			logger.error("alipay分单支付完成结果反馈,没有检索到数据" + order_no + ",小件员：" + delivery_man);
		} else {
			podremark = "分单撤销完成结果反馈";
			boolean saveTradeDetailFlag = posPayDAO.save_PosTradeDetailRecord(cwbOrder.getCwb(), podremark, 0, deliverid, 0, podremark, "", 0, "", 3, 2, "", PosEnum.AliPay.getMethod(), 0, "");
			if (saveTradeDetailFlag) {
				resp_code = "00";
				resp_msg = "成功";
				logger.info("alipay分单撤销完成结果反馈记录成功!");
			} else {
				resp_code = "99";
				resp_msg = "其他异常，数据更新异常";
				logger.error("alipay分单撤销完成结果反馈记录数据更新异常!");
			}

		}

		// 生成返回的xml字符串
		Map<String, String> respMap = convertMapType_backOutFinish(rootnote.getTransaction_Header().getTransaction_id(), resp_code, resp_msg, alipay, null, cwbOrder);
		String responseXml = AlipayXMLHandler.createXMLMessage_BackOutFinish(respMap);
		logger.info("返回alipay数据成功。业务编码：" + rootnote.getTransaction_Header().getTransaction_id());

		return responseXml;
	}

	private Map<String, String> convertMapType_backOutFinish(String transaction_id, String resp_code, String resp_msg, AliPay alipay, JSONObject jobject, CwbOrder cwbOrder) {
		if (cwbOrder == null) {
			cwbOrder = new CwbOrder();
		}
		Map<String, String> retMap = new HashMap<String, String>();
		// 放入map
		retMap.put("transaction_id", transaction_id);
		retMap.put("resp_code", resp_code);
		retMap.put("resp_msg", resp_msg);
		retMap.put("resp_time", DateTimeUtil.getNowTime());
		retMap.put("requester", alipay.getRequester());
		retMap.put("target", alipay.getTargeter());
		retMap.put("order_no", cwbOrder.getCwb());
		// 生成待加密的字符串
		String str = AlipayXMLHandler.createMACXML_PayFinish(retMap);
		String r = "";
		try {
			r = RSACoder.sign(str.getBytes(), alipay.getPrivateKey());
		} catch (Exception e) {
			logger.error("alipay运单支付生成MAC签名异常！");
			e.printStackTrace();
		}
		retMap.put("MAC", r);
		return retMap;
	}

}
