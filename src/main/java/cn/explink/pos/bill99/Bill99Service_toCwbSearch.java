package cn.explink.pos.bill99;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.poscodeMapp.PoscodeMapp;
import cn.explink.domain.Customer;
import cn.explink.pos.bill99.xml.Transaction;
import cn.explink.pos.tools.PosEnum;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.pos.CertificateCoderUtil;

@Service
public class Bill99Service_toCwbSearch extends Bill99Service {
	private Logger logger = LoggerFactory.getLogger(Bill99Service_toCwbSearch.class);

	/**
	 * 运单查询
	 * 
	 * @return
	 */
	public String toCwbSearch(Transaction rootnote, Bill99 bill99) {
		Bill99RespNote billrespNote = new Bill99RespNote();
		try {
			billrespNote = super.BuildBill99RespClass(rootnote, billrespNote);
			if (billrespNote.getCwbOrder() == null) {
				billrespNote.setResp_code(Bill99ExptMessageEnum.ChaXunYiChang.getResp_code());
				billrespNote.setResp_msg(Bill99ExptMessageEnum.ChaXunYiChang.getResp_msg());
				logger.error("bill99运单查询没有检索到数据，当前小件员：" + billrespNote.getDelivery_man());
			} else {
				if (billrespNote.getDeliverstate().getSign_typeid() == 1) {
					billrespNote.setResp_code(Bill99ExptMessageEnum.DingDanYiQianShou.getResp_code());
					billrespNote.setResp_msg(Bill99ExptMessageEnum.DingDanYiQianShou.getResp_msg());
					logger.info("bill99运单查询:订单已签收,当前小件员:" + billrespNote.getDelivery_man());
				} else if ((billrespNote.getDeliverstate().getReceivedfee().doubleValue() > 0) && billrespNote.getDeliverstate().getSign_typeid() == 0) {
					billrespNote.setResp_code(Bill99ExptMessageEnum.YiShouKuanWeiQianShou.getResp_code());
					billrespNote.setResp_msg(Bill99ExptMessageEnum.YiShouKuanWeiQianShou.getResp_msg());
					logger.info("bill99运单查询:已收款,未签收,当前小件员:" + billrespNote.getDelivery_man());
				} else {
					billrespNote.setResp_code(Bill99ExptMessageEnum.Success.getResp_code());
					billrespNote.setResp_msg(Bill99ExptMessageEnum.Success.getResp_msg());
					logger.info("bill99运单查询:未收款,未签收,当前小件员:" + billrespNote.getDelivery_man());
				}

				String customercode = "";
				Customer customer = customerDAO.getCustomerById(billrespNote.getCwbOrder().getCustomerid());
				for (B2cEnum enums : B2cEnum.values()) { // 只有在唯品会模式下的
					if (enums.getMethod().contains("vipshop") && customer.getB2cEnum().contains(String.valueOf(enums.getKey()))) {
						// 唯品会
						customercode = customer.getCustomercode().indexOf("_") > 0 ? customer.getCustomercode().substring(0, customer.getCustomercode().indexOf("_")) : customer.getCustomercode();
					}
				}
				PoscodeMapp poscode = poscodeMappDAO.getPosCodeByKey(billrespNote.getCwbOrder().getCustomerid(), PosEnum.Bill99.getKey());
				if (poscode != null) {
					customercode = poscode.getCustomercode();
				}

				billrespNote.setPayee_id(customercode);
				billrespNote.setConsigneename(billrespNote.getCwbOrder().getConsigneename().length() > 21 ? billrespNote.getCwbOrder().getConsigneename().substring(0, 32) : billrespNote.getCwbOrder()
						.getConsigneename());
				String consignee_contract = billrespNote.getCwbOrder().getConsigneemobile() + "," + billrespNote.getCwbOrder().getConsigneephone();
				billrespNote.setConsignee_contract(consignee_contract.length() > 20 ? consignee_contract.substring(0, 20) : consignee_contract);
			}

		} catch (Exception e) {
			logger.error("Bill99运单查询未知异常!", e);
			billrespNote.setResp_code(Bill99ExptMessageEnum.QiTaShiBai.getResp_code());
			billrespNote.setResp_code(Bill99ExptMessageEnum.QiTaShiBai.getResp_code());
			e.printStackTrace();
		}

		Map<String, String> retMap = convertMapType_cwbSearch(billrespNote, bill99, rootnote);
		// 生成派送员登陆响应报文
		String responseXml = Bill99XMLHandler.createXMLMessage_SearchCwb(retMap);
		logger.info("Bill99[" + rootnote.getTransaction_Header().getTransaction_id() + "]返回XML：" + responseXml);
		return responseXml;
	}

	private Map<String, String> convertMapType_cwbSearch(Bill99RespNote bill99RespNote, Bill99 bill99, Transaction rootnote) {
		CertificateCoderUtil cfcu = new CertificateCoderUtil(bill99);
		Map<String, String> retMap = new HashMap<String, String>();
		// 放入map
		retMap.put("version", bill99.getVersion());
		retMap.put("transaction_sn", rootnote.getTransaction_Header().getTransaction_sn());
		retMap.put("transaction_id", rootnote.getTransaction_Header().getTransaction_id());
		retMap.put("resp_code", bill99RespNote.getResp_code());
		retMap.put("resp_msg", bill99RespNote.getResp_msg());
		retMap.put("resp_time", DateTimeUtil.getNowTime("yyyyMMddHHmmss"));
		retMap.put("requester", bill99.getRequester());
		retMap.put("target", bill99.getTargeter());
		retMap.put("consignee", bill99RespNote.getCwbOrder() == null ? "" : bill99RespNote.getCwbOrder().getConsigneename());
		retMap.put("consignee_address", bill99RespNote.getCwbOrder() == null ? "" : bill99RespNote.getCwbOrder().getConsigneeaddress());
		retMap.put("consignee_contact", bill99RespNote.getCwbOrder() == null ? "" : bill99RespNote.getConsignee_contract());
		retMap.put("consignee_id", bill99RespNote.getCwbOrder() == null ? "" : bill99RespNote.getCwbOrder().getConsigneeno()); // 收件人证件号码，用数据库中的收件人编号

		retMap.put("payee_id", bill99RespNote.getPayee_id()); // payee_id
																// 20130607 标识
		retMap.put("amt", bill99RespNote.getCwbOrder() == null ? "" : bill99RespNote.getCwbOrder().getReceivablefee() + "");
		retMap.put("order_no", bill99RespNote.getCwbOrder() == null ? rootnote.getTransaction_Body().getOrderId() : bill99RespNote.getCwbOrder().getCwb());

		// 生成待加密的字符串
		String str = Bill99XMLHandler.createMACXML_SearchCwb(retMap);
		String MAC = Bill99RespSign(cfcu, str);
		retMap.put("MAC", MAC);
		return retMap;
	}

}
