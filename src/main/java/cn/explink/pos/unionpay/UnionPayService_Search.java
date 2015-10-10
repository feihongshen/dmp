package cn.explink.pos.unionpay;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.poscodeMapp.PoscodeMapp;
import cn.explink.dao.CwbDAO;
import cn.explink.dao.DeliveryStateDAO;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.pos.tools.PosEnum;

@Service
public class UnionPayService_Search extends UnionPayService {

	private Logger logger = LoggerFactory.getLogger(UnionPayService_Search.class);
	@Autowired
	CwbDAO cwbDAO;
	@Autowired
	DeliveryStateDAO deliveryStateDAO;

	private String respMsg_toCwbSearch(CwbOrder cwbOrder) {
		// Customer
		// customer=customerDAO.getCustomerById(cwbOrder.getCustomerid());
		String customercode = "";
		PoscodeMapp poscode = poscodeMappDAO.getPosCodeByKey(cwbOrder.getCustomerid(), PosEnum.UnionPay.getKey());
		if (poscode != null) {
			customercode = poscode.getCustomercode();
		}
		String json_msg = "{\"Response\":\"00\",\"ErrorDescription\":\"\"," + "\"BarCode\":\"" + cwbOrder.getCwb() + "\",\"EnterpriseID\":\"" + customercode + "\"," + "\"Commoditys\":\""
				+ cwbOrder.getSendcarname() + "\",\"OrderNumber\":\"" + cwbOrder.getTranscwb() + "\"," + "\"RecipientCode\":\"" + cwbOrder.getConsigneepostcode() + "\",\"RecipientName\":\""
				+ cwbOrder.getConsigneename() + "\"," + "\"RecipientAddress\":\"" + cwbOrder.getConsigneeaddress() + "\",\"RecipientPhone\":\""
				+ (cwbOrder.getConsigneemobile() + "^" + cwbOrder.getConsigneephone()) + "\"," + "\"Payment\":\"" + cwbOrder.getReceivablefee() + "\",\"Weight\":\"" + cwbOrder.getCarrealweight()
				+ "\"," + "\"Postage\":\"" + 0 + "\",\"ServiceCharge\":\"" + 0 + "\"," + "\"PostagePostPaidFlag\":\"" + 1 + "\",\"ExchangeFlag\":" + (cwbOrder.getCwbordertypeid() == 3 ? 2 : 1)
				+ ",\"OriginMailBarcode\":\"\",\"Description\":\"" + (cwbOrder.getCwbremark() + cwbOrder.getCustomercommand()) + "\"}";
		logger.info("移动POS(UnionPay) 运单查询成功！response=00,cwb={},jsonContent={}", cwbOrder.getCwb(), json_msg);

		return json_msg;
	}

	/**
	 * 运单查询
	 * 
	 * @return
	 */
	public String cwbSearch(JSONObject jsondata) {

		long deliveryid = getUserIdByUserName(jsondata.getString("LoginName"));
		String cwb = jsondata.getString("BarCode");
		UnionPay unionpay = getUnionPaySettingMethod(PosEnum.UnionPay.getKey());
		long deliverid = 0;
		if (unionpay.getIsotherdeliveroper() == 1) {
			deliverid = deliveryid;
		}
		CwbOrder cwbOrder = cwbDAO.getCwbDetailByCwbAndDeliverId(deliverid, cwb);
		if (cwbOrder == null) {
			return super.RespPublicMsg("01", "没有检索到数据,当前订单=" + cwb + ",username=" + jsondata.getString("LoginName"));
		}
		DeliveryState deliverystate = deliveryStateDAO.getActiveDeliveryStateByCwb(cwb);
		if (deliverystate != null && deliverystate.getDeliverystate() != 0) {
			String exptremark = super.getDeliveryStateById(deliverystate.getDeliverystate());
			return super.RespPublicMsg("01", "该订单已做过反馈[" + exptremark + "],当前订单=" + cwb + ",username=" + jsondata.getString("LoginName"));
		}
		return respMsg_toCwbSearch(cwbOrder);
	}

}
