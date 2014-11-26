package cn.explink.pos.alipayapp;

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
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.pos.tools.PosEnum;
import cn.explink.pos.tools.PosPayDAO;
import cn.explink.service.CwbOrderService;
import cn.explink.util.MD5.MD5Util;

/**
 * 支付通知接口处理 修改为配送成功
 * 
 * @author Administrator
 *
 */
@Service
public class AlipayappService_pay extends AlipayappService {

	@Autowired
	CwbOrderService cwbOrderService;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;
	@Autowired
	PosPayDAO posPayDAO;

	private Logger logger = LoggerFactory.getLogger(AlipayappService_pay.class);

	public String toCwbPayAmount(Alipayapp alipay, AlipayParam param) throws JsonGenerationException, JsonMappingException, IOException {
		String cwbTransCwb = cwbOrderService.translateCwb(param.getBill_no()); // 可能是订单号也可能是运单号
		CwbOrder cwborder = cwbDAO.getCwbByCwb(cwbTransCwb);

		if (cwborder == null) {
			return createXMLResponse_exception(alipay.getPrivate_key(), param.getSign_type(), "未检索到数据-订单不存在");
		}

		DeliveryState deliveryState = deliveryStateDAO.getActiveDeliveryStateByCwb(cwborder.getCwb());

		if (deliveryState == null) {
			return createXMLResponse_exception(alipay.getPrivate_key(), param.getSign_type(), "未检索到数据-订单缺少流程");
		}

		User user = userDAO.getAllUserByid(deliveryState.getDeliveryid());

		Map<String, Object> parameters = buildDeliverStatePodParms(param.getTrade_time(), cwborder, deliveryState, user, new BigDecimal(param.getPay_amount()), param.getBill_id());

		cwbOrderService.deliverStatePod(user, cwborder.getCwb(), cwborder.getCwb(), parameters);

		Map<String, String> respMap = createPayAmountResponseMap();
		String createLinkStringResp = AlipayCore.createLinkString(respMap);
		String sign = MD5Util.md5(createLinkStringResp + alipay.getPrivate_key());
		String responsexml = AlipayappXMLHandler.createXMLPayAmount(sign, param.getSign_type());

		posPayDAO.save_PosTradeDetailRecord(cwborder.getCwb(), "支付宝APP-在线支付", Double.valueOf(param.getPay_amount()), user.getUserid(), PaytypeEnum.CodPos.getValue(), "", cwborder.getConsigneename(),
				1, "支付宝APP-手机钱包在线支付,本人签收", 1, 1, "", PosEnum.AliPayApp2.getMethod(), 0, "");

		logger.info("返回alipay-app成功,service={},xml={},cwb=" + param.getBill_no(), param.getService(), responsexml);

		return responsexml;
	}

	private Map<String, String> createPayAmountResponseMap() {
		Map<String, String> respMap = new HashMap<String, String>();
		respMap.put("is_success", "T");
		return respMap;
	}

	private Map<String, Object> buildDeliverStatePodParms(String paytime, CwbOrder cwborder, DeliveryState deliveryState, User user, BigDecimal payAmount, String bill_id) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		long podresultid = DeliveryStateEnum.PeiSongChengGong.getValue();
		if (cwborder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmenhuan.getValue()) {
			podresultid = DeliveryStateEnum.ShangMenHuanChengGong.getValue();
		}
		if (cwborder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
			podresultid = DeliveryStateEnum.ShangMenTuiChengGong.getValue();
		}

		parameters.put("deliverid", (long) deliveryState.getDeliveryid());
		parameters.put("podresultid", podresultid);
		parameters.put("backreasonid", 0l);
		parameters.put("leavedreasonid", 0l);
		parameters.put("receivedfeecash", BigDecimal.ZERO);
		parameters.put("receivedfeecodpos", payAmount);
		parameters.put("receivedfeecheque", BigDecimal.ZERO);
		parameters.put("receivedfeeother", BigDecimal.ZERO);
		parameters.put("paybackedfee", BigDecimal.ZERO);
		parameters.put("podremarkid", (long) 0);
		parameters.put("posremark", "支付宝COD扫码支付");
		parameters.put("checkremark", "");
		parameters.put("deliverstateremark", "支付宝COD扫码支付,支付宝运单号:" + bill_id);
		parameters.put("owgid", 0);
		parameters.put("sessionbranchid", user.getBranchid());
		parameters.put("sessionuserid", deliveryState.getDeliveryid());
		parameters.put("sign_typeid", 1);
		parameters.put("sign_man", cwborder.getConsigneename());
		parameters.put("sign_time", paytime);
		parameters.put("nosysyemflag", "1");//
		return parameters;
	}

}
