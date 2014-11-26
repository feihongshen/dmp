package cn.explink.pos.alipayCodApp;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.dao.DeliveryStateDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.User;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.pos.tools.PosEnum;
import cn.explink.pos.tools.PosPayDAO;
import cn.explink.pos.unionpay.UnionPay;
import cn.explink.service.CwbOrderService;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

/**
 * 支付通知接口处理 修改为配送成功
 * 
 * @author Administrator
 *
 */
@Service
public class AlipayCodAppService_pay extends AlipayCodAppService {

	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	PosPayDAO posPayDAO;

	private Logger logger = LoggerFactory.getLogger(AlipayCodAppService_pay.class);

	public String toCwbPayAmount(AliPayCodApp alipay, String service, String partner, String _input_charset, String sign_type, String timestamp, String logistics_bill_no, String logistics_code,
			String ord_pmt_time, String ord_pmt_amt) throws JsonGenerationException, JsonMappingException, IOException {
		CwbOrder cwborder = cwbDAO.getCwbByCwb(logistics_bill_no);

		if (cwborder == null) {
			return createXMLResponse_exception(alipay.getPrivate_key(), sign_type, "未检索到数据-订单不存在");
		}

		DeliveryState deliveryState = deliveryStateDAO.getActiveDeliveryStateByCwb(cwborder.getCwb());

		if (deliveryState == null) {
			return createXMLResponse_exception(alipay.getPrivate_key(), sign_type, "未检索到数据-订单缺少流程");
		}

		User user = userDAO.getAllUserByid(deliveryState.getDeliveryid());

		BigDecimal payAmount = new BigDecimal(ord_pmt_amt);

		Map<String, Object> parameters = buildDeliverStatePodParms(ord_pmt_time, cwborder, deliveryState, user, payAmount);

		cwbOrderService.deliverStatePod(user, cwborder.getCwb(), cwborder.getCwb(), parameters);

		Map<String, String> respMap = createPayAmountResponseMap();
		String createLinkStringResp = AlipayCore.createLinkString(respMap);
		String sign = MD5Util.md5(createLinkStringResp + alipay.getPrivate_key());
		String responsexml = AlipayCodAppXMLHandler.createXMLPayAmount(sign, sign_type);

		posPayDAO.save_PosTradeDetailRecord(cwborder.getCwb(), "支付宝CODAPP-在线支付", payAmount.doubleValue(), user.getUserid(), PaytypeEnum.Pos.getValue(), "", cwborder.getConsigneename(), 1,
				"支付宝CODAPP-手机钱包在线支付,本人签收", 1, 1, "", PosEnum.AliPayCodApp.getMethod(), 0, "");

		logger.info("返回alipaycodapp成功,service={},xml={}", service, responsexml);

		return responsexml;
	}

	private Map<String, String> createPayAmountResponseMap() {
		Map<String, String> respMap = new HashMap<String, String>();
		respMap.put("is_success", "T");

		return respMap;
	}

	private Map<String, Object> buildDeliverStatePodParms(String ord_pmt_time, CwbOrder cwborder, DeliveryState deliveryState, User user, BigDecimal payAmount) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("deliverid", (long) deliveryState.getDeliveryid());
		parameters.put("podresultid", (long) DeliveryStateEnum.PeiSongChengGong.getValue());
		parameters.put("backreasonid", 0l);
		parameters.put("leavedreasonid", 0l);
		parameters.put("receivedfeecash", BigDecimal.ZERO);
		parameters.put("receivedfeepos", payAmount);
		parameters.put("receivedfeecheque", BigDecimal.ZERO);
		parameters.put("receivedfeeother", BigDecimal.ZERO);
		parameters.put("paybackedfee", BigDecimal.ZERO);
		parameters.put("podremarkid", (long) 0);
		parameters.put("posremark", "手机在线支付");
		parameters.put("checkremark", "");
		parameters.put("deliverstateremark", "支付宝CODAPP-在线支付");
		parameters.put("owgid", 0);
		parameters.put("sessionbranchid", user.getBranchid());
		parameters.put("sessionuserid", deliveryState.getDeliveryid());
		parameters.put("sign_typeid", 1);
		parameters.put("sign_man", cwborder.getConsigneename());
		parameters.put("sign_time", ord_pmt_time);
		parameters.put("nosysyemflag", "1");//
		return parameters;
	}

}
