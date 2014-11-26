package cn.explink.pos.alipayCodApp;

import java.io.IOException;
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
import cn.explink.domain.CwbOrder;
import cn.explink.domain.User;
import cn.explink.domain.orderflow.OrderFlow;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

/**
 * 物流信息查询
 * 
 * @author Administrator
 *
 */
@Service
public class AlipayCodAppService_search extends AlipayCodAppService {
	private Logger logger = LoggerFactory.getLogger(AlipayCodAppService_search.class);

	@Autowired
	ExplinkService explinkService;

	public String toCwbSearch(AliPayCodApp alipay, String service, String partner, String charset, String sign_type, String timestamp, String logistics_bill_no, String logistics_code)
			throws JsonGenerationException, JsonMappingException, IOException {
		CwbOrder cwborder = cwbDAO.getCwbByCwb(logistics_bill_no);

		if (cwborder == null) {
			return createXMLResponse_exception(alipay.getPrivate_key(), sign_type, "未检索到数据");
		}
		// long
		// oreratoruserid=orderflowlist.get(orderflowlist.size()-1).getUserid();
		// //获取最后一条记录的操作人
		if (cwborder.getDeliverid() == 0) {
			return createXMLResponse_exception(alipay.getPrivate_key(), sign_type, "未检索到数据");
		}

		List<OrderFlow> orderflowlist = orderFlowDAO.getOrderFlowByCwb(cwborder.getCwb());

		List<Step> steplist = getSetupList(orderflowlist);

		User user = userDAO.getAllUserByid(cwborder.getDeliverid());

		String steps = jacksonmapper.writeValueAsString(steplist);

		Map<String, String> respMap = createSearchResponseMap(cwborder.getCwb(), user.getRealname(), cwborder.getConsigneemobile(), cwborder.getConsigneename(), cwborder.getConsigneeaddress(),
				user.getUsermobile(), steps);

		String createLinkStringResp = AlipayCore.createLinkString(respMap);
		String sign = MD5Util.md5(createLinkStringResp + alipay.getPrivate_key());
		String responsexml = AlipayCodAppXMLHandler.createXML_SearchCwb(respMap, sign, sign_type);
		responsexml.trim();

		logger.info("返回alipaycodapp成功,service={},xml={}", service, responsexml);

		return responsexml;
	}

	private List<Step> getSetupList(List<OrderFlow> orderFlowList) {
		List<Step> steplist = new ArrayList<Step>();

		for (OrderFlow orderFlow : orderFlowList) {
			if (getOrderFlowFilter(orderFlow.getFlowordertype()) == null) {
				continue;
			}

			Step step = new Step();
			step.setTime(DateTimeUtil.formatDate(orderFlow.getCredate()));
			step.setStepInfo(explinkService.getDetail(orderFlow));
			steplist.add(step);
		}
		return steplist;
	}

	private Map<String, String> createSearchResponseMap(String logistics_bill_no, String delivery_name, String consignee_contact, String consigee, String consigee_address, String delivery_mobile,
			String steps) {
		Map<String, String> respMap = new HashMap<String, String>();
		respMap.put("is_success", "T");
		// respMap.put("error","");
		respMap.put("logistics_bill_no", logistics_bill_no.trim());
		respMap.put("delivery_name", delivery_name.trim());
		respMap.put("consignee_contact", consignee_contact.trim());
		respMap.put("consigee", consigee.trim());
		respMap.put("consigee_address", consigee_address.trim());
		respMap.put("delivery_mobile", delivery_mobile.trim());
		respMap.put("steps", steps.trim());

		return respMap;
	}

	public static void main(String[] args) {
		Map<String, String> respMap = new HashMap<String, String>();
		respMap.put("is_success", "T".trim());
		respMap.put("logistics_bill_no", "6854835822".trim());
		respMap.put("delivery_name", "王志远".trim());
		respMap.put("consignee_contact", "13401095196".trim());
		respMap.put("consigee", "马晓龙 ".trim());
		respMap.put("consigee_address", "中国北京北京市通州区,梨园镇时尚街区15号楼3单元1002室");
		respMap.put("delivery_mobile", "13488694552");
		respMap.put(
				"steps",
				"[{\"time\":\"2013-10-16 23:44:04\",\"stepInfo\":\"从[2号库]入库;联系电话：[^];派送中;备注:[系统自动处理]\"},{\"time\":\"2013-10-16 23:44:04\",\"stepInfo\":\"从[2号库]出库;联系电话[^]\"},{\"time\":\"2013-10-17 09:17:52\",\"stepInfo\":\"从[35梨园]到货;联系电话：[18201378600^]\"},{\"time\":\"2013-10-17 09:17:52\",\"stepInfo\":\"货物已从[35梨园]的小件员[王志远]分站领货;小件员电话:[13488694552^]\"},{\"time\":\"2013-10-17 21:04:25\",\"stepInfo\":\"货物已由[35梨园]的小件员[王志远]反馈为[拒收];小件员电话[13488694552^]\"}]"
						.trim());

		String createLinkStringResp = AlipayCore.createLinkString(respMap);
		System.out.println(createLinkStringResp);
		String sign = MD5Util.md5(createLinkStringResp + "dz1ivka0uz6794ogtmr373yvl980vgki");
		// a1d78789e9ea1759263c54b3909fad44
		System.out.println(sign);
	}

	public String getOrderFlowFilter(long flowordertype) {
		for (AlipayCodFlowEnum dd : AlipayCodFlowEnum.values()) {
			if (flowordertype == dd.getFlowordertype()) {
				return dd.getFlowordertype() + "";
			}
		}

		return null;

	}
}
