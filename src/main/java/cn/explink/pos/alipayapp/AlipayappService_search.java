package cn.explink.pos.alipayapp;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.explink.ExplinkService;
import cn.explink.b2c.tools.poscodeMapp.PoscodeMapp;
import cn.explink.b2c.tools.poscodeMapp.PoscodeMappDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.pos.tools.PosEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

/**
 * 物流信息查询
 * 
 * @author Administrator
 *
 */
@Service
public class AlipayappService_search extends AlipayappService {
	private Logger logger = LoggerFactory.getLogger(AlipayappService_search.class);
	@Autowired
	PoscodeMappDAO poscodeMappDAO;

	public String toCwbSearch(Alipayapp alipay, AlipayParam param) throws JsonGenerationException, JsonMappingException, IOException {
		String cwbTransCwb = cwbOrderService.translateCwb(param.getBill_no()); // 可能是订单号也可能是运单号
		CwbOrder cwborder = cwbDAO.getCwbByCwb(cwbTransCwb);

		if (cwborder == null) {
			return createXMLResponse_exception(alipay.getPrivate_key(), param.getSign_type(), "未检索到数据");
		}
		if (cwborder.getDeliverid() == 0) {
			return createXMLResponse_exception(alipay.getPrivate_key(), param.getSign_type(), "未检索到数据，订单未领货不能反馈");
		}

		User user = userDAO.getAllUserByid(cwborder.getDeliverid());
		DeliveryState deliveryState = deliveryStateDAO.getActiveDeliveryStateByCwb(cwbTransCwb);

		Map<String, String> respMap = createSearchResponseMap(cwborder, user, deliveryState);

		String createLinkStringResp = AlipayCore.createLinkString(respMap);

		logger.info("返回签名串={},cwb={}", createLinkStringResp, cwbTransCwb);

		String sign = MD5Util.md5(createLinkStringResp + alipay.getPrivate_key());

		String responsexml = AlipayappXMLHandler.createXML_SearchCwb(respMap, sign, param.getSign_type());

		logger.info("返回alipay-app成功,service={},xml={},cwb=" + param.getBill_no(), param.getService(), responsexml);

		return responsexml;
	}

	private Map<String, String> createSearchResponseMap(CwbOrder cwborder, User user, DeliveryState deliveryState) {
		Map<String, String> respMap = new HashMap<String, String>();
		String payable = deliveryState.getDeliverystate() == 0 ? "Y" : "N";

		PoscodeMapp codemapping = poscodeMappDAO.getPosCodeByKey(cwborder.getCustomerid(), PosEnum.AliPayApp2.getKey());
		String out_merchant_code = ""; // 查询POS商户映射上面得出
		if (codemapping != null) {
			out_merchant_code = codemapping.getCustomercode();
		}

		if (cwborder.getReceivablefee().compareTo(BigDecimal.ZERO) == 0) {
			payable = "N";
		}

		respMap.put("is_success", "T");
		// respMap.put("error","");
		respMap.put("payable", payable);
		respMap.put("amt_can_modify", "N"); // 支付金额是否可以修改Y 可以，N不可以
		respMap.put("amount", String.valueOf(cwborder.getReceivablefee()));
		respMap.put("deliver_mobile", user.getUsermobile().replaceAll(" ", "")); // 派送员手机号
		respMap.put("goods_name", cwborder.getSendcarname().replaceAll(" ", "")); // 货物名称

		if (!out_merchant_code.isEmpty()) {
			respMap.put("out_merchant_code", out_merchant_code);
			respMap.put("out_order_no", cwborder.getCwb());
		}

		respMap.put("sub_account_no", "");

		//
		// xmlstr.append("<out_merchant_code>"+map.get("out_merchant_code")+"</out_merchant_code>");
		// xmlstr.append("<out_order_no>"+map.get("out_order_no")+"</out_order_no>");
		// xmlstr.append("<sub_account_no>"+map.get("sub_account_no")+"</sub_account_no>");

		return respMap;
	}

	//
	// public static void main(String[] args) {
	// Map<String, String> respMap=new HashMap<String, String>();
	// respMap.put("is_success", "T".trim());
	// respMap.put("logistics_bill_no","6854835822".trim());
	// respMap.put("delivery_name","王志远".trim());
	// respMap.put("consignee_contact","13401095196".trim());
	// respMap.put("consigee","马晓龙 ".trim());
	// respMap.put("consigee_address","中国北京北京市通州区,梨园镇时尚街区15号楼3单元1002室");
	// respMap.put("delivery_mobile","13488694552");
	// respMap.put("steps","[{\"time\":\"2013-10-16 23:44:04\",\"stepInfo\":\"从[2号库]入库;联系电话：[^];派送中;备注:[系统自动处理]\"},{\"time\":\"2013-10-16 23:44:04\",\"stepInfo\":\"从[2号库]出库;联系电话[^]\"},{\"time\":\"2013-10-17 09:17:52\",\"stepInfo\":\"从[35梨园]到货;联系电话：[18201378600^]\"},{\"time\":\"2013-10-17 09:17:52\",\"stepInfo\":\"货物已从[35梨园]的小件员[王志远]分站领货;小件员电话:[13488694552^]\"},{\"time\":\"2013-10-17 21:04:25\",\"stepInfo\":\"货物已由[35梨园]的小件员[王志远]反馈为[拒收];小件员电话[13488694552^]\"}]".trim());
	//
	// String createLinkStringResp=AlipayCore.createLinkString(respMap);
	// System.out.println(createLinkStringResp);
	// String
	// sign=MD5Util.md5(createLinkStringResp+"dz1ivka0uz6794ogtmr373yvl980vgki");
	// // a1d78789e9ea1759263c54b3909fad44
	// System.out.println(sign);
	// }
	//

}
