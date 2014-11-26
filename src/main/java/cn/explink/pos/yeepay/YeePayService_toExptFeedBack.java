package cn.explink.pos.yeepay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.ExptCodeJointDAO;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.pos.tools.PosEnum;
import cn.explink.pos.yeepay.xml.YeepayRequest;
import cn.explink.util.DateTimeUtil;

@Service
public class YeePayService_toExptFeedBack extends YeePayService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Md5PasswordEncoder md5 = new Md5PasswordEncoder();

	@Autowired
	ExptCodeJointDAO exptcodeJointDAO;

	/**
	 * 签收异常反馈
	 * 
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	public String toExceptionFeedBack(String service_code, YeepayRequest rootnote, YeePay yeePay) {

		YeePayRespNote respNote = new YeePayRespNote();
		respNote = super.BuildYeePayRespClass(rootnote, respNote);
		if (respNote.getCwbOrder() == null) {
			respNote.setResult_code("3");
			respNote.setResult_msg("查无此单");

		} else {

			String Except_Code = rootnote.getSessionBody().getExtendAtt().getExcept_Code();
			String Except_Msg = rootnote.getSessionBody().getExtendAtt().getExcept_Msg();
			String Except_type = rootnote.getSessionBody().getExtendAtt().getExcept_Type();

			long deliverystate = getYeePayExptOrderType(Except_type);
			String flowOrderTypeName = getYeePayExptOrderTypeName(Except_type);

			long leavedreasonid = 0;
			long backreasonid = 0;
			if (deliverystate == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
				if (exptcodeJointDAO.getExpMatchListByPosCode(Except_Code, PosEnum.YeePay.getKey()) != null) {
					leavedreasonid = (exptcodeJointDAO.getExpMatchListByPosCode(Except_Code, PosEnum.YeePay.getKey())).getReasonid();
				} else {
					leavedreasonid = Long.parseLong(Except_Code);
				}
			} else if (deliverystate == DeliveryStateEnum.JuShou.getValue()) {
				if (exptcodeJointDAO.getExpMatchListByPosCode(Except_Code, PosEnum.YeePay.getKey()) != null) {
					backreasonid = (exptcodeJointDAO.getExpMatchListByPosCode(Except_Code, PosEnum.YeePay.getKey())).getReasonid();
				} else {
					backreasonid = Long.parseLong(Except_Code);
				}
			}

			try {

				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("deliverid", respNote.getDeliverid());
				parameters.put("podresultid", deliverystate);
				parameters.put("backreasonid", backreasonid);
				parameters.put("leavedreasonid", leavedreasonid);
				parameters.put("receivedfeecash", BigDecimal.ZERO);
				parameters.put("receivedfeepos", BigDecimal.ZERO);
				parameters.put("receivedfeecheque", BigDecimal.ZERO);
				parameters.put("receivedfeeother", BigDecimal.ZERO);
				parameters.put("paybackedfee", BigDecimal.ZERO);
				parameters.put("podremarkid", 0l);
				parameters.put("posremark", "POS反馈");
				parameters.put("checkremark", "");
				parameters.put("deliverstateremark", flowOrderTypeName);
				parameters.put("owgid", 0);
				parameters.put("sessionbranchid", respNote.getBranchid());
				parameters.put("sessionuserid", respNote.getUser().getUserid());
				parameters.put("sign_typeid", Integer.valueOf(0));
				parameters.put("sign_man", "");
				parameters.put("sign_time", "");

				cwbOrderService.deliverStatePod(respNote.getUser(), respNote.getCwbOrder().getCwb(), respNote.getCwbOrder().getCwb(), parameters);
				respNote.setResult_code("2");
				respNote.setResult_msg("操作成功");

				logger.info("yeepay异常反馈成,expt_code=[" + Except_Code + "],expt_msg=[" + Except_Msg + "],单号：" + respNote.getCwbOrder().getCwb() + ",小件员=" + respNote.getUser().getUserid());
			} catch (Exception e) {
				respNote.setResult_code("3");
				respNote.setResult_msg("接收失败，更新数据失败");
				logger.error("yeepay异常反馈发生未知异常", e);

			}
		}

		// 生成返回的xml字符串
		Map<String, String> respMap = convertMapType_Exception(service_code, rootnote, respNote, yeePay);
		String responseXml = YeePayXMLHandler.createResponseXML_Exception(respMap);
		logger.info("返回yeepay数据成功。业务编码={},XML={}", service_code, responseXml);

		return responseXml;
	}

	private Map<String, String> convertMapType_Exception(String service_code, YeepayRequest rootnote, YeePayRespNote respNote, YeePay yeePay) {
		Map<String, String> respMap = new HashMap<String, String>();
		String result_time = DateTimeUtil.getNowTime("yyyyMMddHHmmss");

		respMap.put("version", rootnote.getSessionHead().getVersion()); // 版本号
		respMap.put("servicecode", service_code); // 请求的业务编号
		respMap.put("transactionid", rootnote.getSessionHead().getTransactionID()); // 交易流水号
		respMap.put("srcsysid", yeePay.getRequester()); // 请求方
		respMap.put("dstsysid", yeePay.getTargeter()); // 响应方
		respMap.put("result_code", respNote.getResult_code()); // 响应结果编码
		respMap.put("result_msg", respNote.getResult_msg()); // 响应结果消息
		respMap.put("result_time", result_time); // 响应时间
		respMap.put("order_no", respNote.getCwbOrder().getCwb());

		// 生成待加密的字符串
		String HMacXml = YeePayXMLHandler.createResponseHMAC_Exception(respMap);
		// 加密后的字符串
		String hmac = md5.encodePassword(HMacXml + yeePay.getPrivatekey(), null);
		// 把签名后的数据放在map中用来返回最后的数据
		respMap.put("hmac", hmac);

		return respMap;
	}

}
