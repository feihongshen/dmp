package cn.explink.pos.alipay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.domain.Reason;
import cn.explink.pos.alipay.xml.Transaction;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.pos.RSACoder;

@Service
public class AlipayService_toSearchExptCode extends AlipayService {
	private Logger logger = LoggerFactory.getLogger(AlipayService_toSearchExptCode.class);

	/**
	 * 查询商户派件异常码
	 * 
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	public String toSearchExptCode(Transaction rootnote, AliPay alipay) {
		String resp_code = "";
		String resp_msg = "";
		List<Reason> reasonlist = null;
		List<ExptReason> dangdangList = null;
		resp_code = "00";
		resp_msg = "成功";
		reasonlist = reasonDao.getAllReason(); // 系统内部异常
		// 当当异常码
		String customerids = getDangDangSettingMethod(B2cEnum.DangDang.getKey()).getCustomerids();
		dangdangList = exptReasonDAO.getExptReasonListByPos(customerids); // 枚举中当当的异常。

		// 生成返回的xml字符串
		Map<String, String> respMap = convertMapType_searchExptCode(rootnote.getTransaction_Header().getTransaction_id(), resp_code, resp_msg, alipay, null, reasonlist, dangdangList);
		String responseXml = AlipayXMLHandler.createXMLMessage_searchExptCode(respMap, reasonlist, dangdangList);
		logger.info("返回alipay数据成功-业务编码={},返回XML={}", rootnote.getTransaction_Header().getTransaction_id(), responseXml);
		return responseXml;
	}

	private Map<String, String> convertMapType_searchExptCode(String transaction_id, String resp_code, String resp_msg, AliPay alipay, JSONObject jobject, List<Reason> reasonlist,
			List<ExptReason> dangdangList) {
		Map<String, String> retMap = new HashMap<String, String>();
		// 放入map
		retMap.put("transaction_id", transaction_id);
		retMap.put("resp_code", resp_code);
		retMap.put("resp_msg", resp_msg);
		retMap.put("resp_time", DateTimeUtil.getNowTime());
		retMap.put("requester", alipay.getRequester());
		retMap.put("target", alipay.getTargeter());

		// 生成待加密的字符串
		String str = AlipayXMLHandler.createMACXML_searchExptCode(retMap, reasonlist, dangdangList);
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
