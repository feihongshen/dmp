package cn.explink.pos.yeepay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;

import cn.explink.domain.DeliveryState;
import cn.explink.domain.User;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.pos.tools.PosEnum;
import cn.explink.pos.yeepay.xml.YeepayRequest;
import cn.explink.util.DateTimeUtil;

@Service
public class YeePayService_toSignCwb extends YeePayService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Md5PasswordEncoder md5 = new Md5PasswordEncoder();

	/**
	 * 签收通知接口
	 * 
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	public String toSignCwb(String service_code, YeepayRequest rootnote, YeePay yeePay) {

		YeePayRespNote respNote = new YeePayRespNote();

		try {
			respNote = super.BuildYeePayRespClass(rootnote, respNote);

			if (respNote.getCwbOrder() == null) {
				respNote.setResult_code("3");
				respNote.setResult_msg("接收失败，查无此单");
			} else {
				respNote = ExcuteDealWithYeePaySignLogic(rootnote, respNote);
				if (respNote.getResult_code() == null) {
					respNote.setResult_code("2");
					respNote.setResult_msg("操作成功");
				}
			}
		} catch (Exception e) {
			respNote.setResult_code("3");
			respNote.setResult_msg("处理签收逻辑发生未知异常");
			logger.error("运单签收-系统遇到不可预知的异常!异常原因:", e);
		}

		// 生成返回的xml字符串
		Map<String, String> respMap = convertMapType_cwbSign(service_code, rootnote, respNote, yeePay);
		String responseXml = YeePayXMLHandler.createResponseXML_cwbSign(respMap);
		logger.info("返回yeepay数据成功。业务编码={},XML={}", service_code, responseXml);
		return responseXml;
	}

	private YeePayRespNote ExcuteDealWithYeePaySignLogic(YeepayRequest rootnote, YeePayRespNote respNote) {
		int Sign_Self_Flag = rootnote.getSessionBody().getExtendAtt().getSign_Self_Flag();
		// 签收人
		String Sign_Name = rootnote.getSessionBody().getExtendAtt().getSign_Name();
		if ("1".equals(Sign_Self_Flag)) {
			Sign_Name = respNote.getCwbOrder().getConsigneename();
			respNote.setSignremark("POS签收-本人签收");
		} else if ("2".equals(Sign_Self_Flag)) {
			respNote.setSignremark("POS签收-他人代签收");
		}
		DeliveryState ds = respNote.getDeliverstate();

		try {
			DeliveryState deryState = respNote.getDeliverstate();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("deliverid", respNote.getDeliverid());
			parameters.put("podresultid", getPodResultIdByCwb(respNote.getCwbOrder().getCwbordertypeid()));
			parameters.put("backreasonid", (long) 0);
			parameters.put("leavedreasonid", (long) 0);
			parameters.put("receivedfeecash", ds.getCash());
			parameters.put("receivedfeepos", ds.getPos());
			parameters.put("receivedfeecheque", ds.getCheckfee());
			parameters.put("receivedfeeother", BigDecimal.ZERO);
			parameters.put("paybackedfee", BigDecimal.ZERO);
			parameters.put("podremarkid", (long) 0);
			parameters.put("posremark", deryState.getPosremark() + "POS反馈");
			parameters.put("checkremark", "");
			parameters.put("deliverstateremark", "");
			parameters.put("owgid", 0);
			parameters.put("sessionbranchid", respNote.getBranchid());
			parameters.put("sessionuserid", respNote.getDeliverid());
			parameters.put("sign_typeid", Integer.valueOf("1")); // 是否签收 1已签收
																	// 0未签收
			parameters.put("sign_man", Sign_Name);
			parameters.put("sign_time", DateTimeUtil.getNowTime());
			parameters.put("nosysyemflag", "1");//

			cwbOrderService.deliverStatePod(respNote.getUser(), respNote.getCwbOrder().getCwb(), respNote.getCwbOrder().getCwb(), parameters);

			deliveryStateDAO.updateOperatorIdByCwb(respNote.getDeliverid(), respNote.getCwbOrder().getCwb());

			posPayDAO.save_PosTradeDetailRecord(respNote.getCwbOrder().getCwb(), respNote.getSignremark(), Double.valueOf(respNote.getCwbOrder().getReceivablefee() + ""), 0, 0, "",
					respNote.getSignname(), Sign_Self_Flag, respNote.getSignremark(), 2, 1, "", PosEnum.YeePay.getMethod(), 0, "");

		} catch (CwbException e1) {
			exceptionCwbDAO.createExceptionCwbScan(respNote.getCwbOrder().getCwb(), e1.getFlowordertye(), e1.getMessage(), respNote.getUser().getBranchid(), respNote.getUser().getUserid(),
					respNote.getCwbOrder() == null ? 0 : respNote.getCwbOrder().getCustomerid(), 0, 0, 0, "",respNote.getCwbOrder().getCwb());

			if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO.getValue()) {
				logger.error("yeepay运单签收,没有检索到数据" + respNote.getCwbOrder().getCwb() + ",小件员：" + respNote.getUsername(), e1);
				respNote.setResult_code("3");
				respNote.setResult_msg("查询异常");

			} else if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_XIAO_JIAN_YUAN_DE_HUO.getValue()) {
				respNote.setResult_code("3");
				respNote.setResult_msg(ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_XIAO_JIAN_YUAN_DE_HUO.getText());
				logger.error("yeepay运单签收,不是此小件员的货" + respNote.getCwbOrder().getCwb() + ",当前小件员：" + respNote.getUsername(), e1);
			} else {
				respNote.setResult_code("3");
				respNote.setResult_msg("未知异常");
				logger.error("yeepay运单签收,订单号=" + respNote.getCwbOrder().getCwb() + ",小件员=" + respNote.getUsername(), e1);
			}

		}
		return respNote;
	}

	private Map<String, String> convertMapType_cwbSign(String service_code, YeepayRequest rootnote, YeePayRespNote respNote, YeePay yeePay) {
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
		String HMacXml = YeePayXMLHandler.createResponseHMAC_cwbSign(respMap);
		// 加密后的字符串
		String hmac = md5.encodePassword(HMacXml + yeePay.getPrivatekey(), null);
		// 把签名后的数据放在map中用来返回最后的数据
		respMap.put("hmac", hmac);

		return respMap;
	}

}
