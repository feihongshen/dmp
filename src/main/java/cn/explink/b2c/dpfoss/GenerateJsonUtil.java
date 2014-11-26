package cn.explink.b2c.dpfoss;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.explink.b2c.dpfoss.waybill.QueryWayBillRequest;
import cn.explink.b2c.dpfoss.waybill.QueryWayBillResponse;

public class GenerateJsonUtil {

	/**
	 * 查询外发单接口 request
	 * 
	 * @param handOverNo
	 * @param waybillNo
	 * @return
	 */
	public static QueryWayBillRequest generateQueryWayBillRequest(String handOverNo, String waybillNo) {
		QueryWayBillRequest req = new QueryWayBillRequest();
		req.setHandOverNo(handOverNo);
		req.setWaybillNo(waybillNo);
		return req;
	}

	public static List<QueryWayBillResponse> generateQueryWayBillResponse() {
		QueryWayBillResponse res = new QueryWayBillResponse();
		res.setAgentCompanyCode("agentCompanyCode");
		res.setAgentCompanyName("agentCompanyName");
		res.setDeclarationValue(new BigDecimal("123"));
		res.setCodAmount(new BigDecimal("222"));
		res.setExternalOrgCode("externalOrgCode");
		res.setExternalUserCode("externalUserCode");
		res.setExternalOrgName("");
		res.setExternalUserName("externalUserName");
		res.setFreightFee(new BigDecimal("234"));
		res.setGoodsNum(10);
		res.setModifyTime(new Date());
		res.setNotes("notes");
		res.setPayDpFee(new BigDecimal("234"));
		res.setPayType("payType");
		res.setPickupType("pickupType");
		res.setReceiveAddr("receiveAddr");
		res.setReceiveCompany("receiveCompany");
		res.setReceiveName("receiveName");
		res.setReceivePhone("receivePhone");
		res.setReturnType("returnType");
		res.setAuditStatus("status");
		res.setVolume(new BigDecimal("11111"));
		res.setWaybillNo("waybillNo");
		res.setWeight(new BigDecimal("1231"));
		List<QueryWayBillResponse> list = new ArrayList<QueryWayBillResponse>();
		list.add(res);
		list.add(res);
		return list;
	}

}
