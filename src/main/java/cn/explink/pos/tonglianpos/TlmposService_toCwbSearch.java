package cn.explink.pos.tonglianpos;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.poscodeMapp.PoscodeMapp;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.pos.tonglianpos.xmldto.Transaction;
import cn.explink.pos.tools.PosEnum;
import cn.explink.util.DateTimeUtil;

@Service
public class TlmposService_toCwbSearch extends TlmposService {
	private Logger logger = LoggerFactory.getLogger(TlmposService_toCwbSearch.class);

	/**
	 * 运单查询
	 * 
	 * @return
	 */
	@SuppressWarnings("finally")
	public String toCwbSearch(Transaction rootnote, Tlmpos tlmpos) {
		TlmposRespNote tlmposRespNote = new TlmposRespNote();
		try {
			tlmposRespNote = super.buildtlmposRespClass(rootnote, tlmposRespNote);
			if (tlmposRespNote.getCwbOrder() == null) {
				tlmposRespNote.setResp_code(TlmposExptMsgEnum.ChaXunYiChang.getResp_code());
				tlmposRespNote.setResp_msg(TlmposExptMsgEnum.ChaXunYiChang.getResp_msg());
				this.logger.error("tlmpos运单查询没有检索到数据，当前小件员：", tlmposRespNote.getDelivery_man());
				return null;
			}
			if (tlmposRespNote.getDeliverstate().getSign_typeid() == 1) {
				tlmposRespNote.setResp_code(TlmposExptMsgEnum.DingDanYiQianShou.getResp_code());
				tlmposRespNote.setResp_msg(TlmposExptMsgEnum.DingDanYiQianShou.getResp_msg());
				this.logger.info("tlmpos运单查询:订单已签收,当前小件员:" + tlmposRespNote.getDelivery_man());
				return null;
			}
			if ((tlmposRespNote.getDeliverstate().getReceivedfee().doubleValue() > 0) && (tlmposRespNote.getDeliverstate().getSign_typeid() == 0)) {
				tlmposRespNote.setResp_code(TlmposExptMsgEnum.YiShouKuanWeiQianShou.getResp_code());
				tlmposRespNote.setResp_msg(TlmposExptMsgEnum.YiShouKuanWeiQianShou.getResp_msg());
				this.logger.info("tlmpos运单查询:已收款,未签收,当前小件员:" + tlmposRespNote.getDelivery_man());
				return null;
			}
			if (tlmposRespNote.getDeliverstate().getDeliverystate() != 0) { // 判断是否已反馈过
				tlmposRespNote.setResp_code(TlmposExptMsgEnum.DingDanYiFankui.getResp_code());
				tlmposRespNote.setResp_msg(TlmposExptMsgEnum.DingDanYiFankui.getResp_msg());
				this.logger.info("tlmpos运单查询:订单已反馈过无需再次反馈,当前小件员:" + tlmposRespNote.getDelivery_man());
				return null;
			}

			tlmposRespNote.setResp_code(TlmposExptMsgEnum.Success.getResp_code());
			tlmposRespNote.setResp_msg(TlmposExptMsgEnum.Success.getResp_msg());
			this.logger.info("tlmpos运单查询:未收款,未签收,当前小件员:" + tlmposRespNote.getDelivery_man());

			tlmposRespNote = this.SearchCwbDetailBytlmpos(tlmposRespNote, tlmpos); // 查询其他信息

		} catch (Exception e) {
			this.logger.error("tlmpos运单查询未知异常!", e);
			tlmposRespNote.setResp_code(TlmposExptMsgEnum.QiTaShiBai.getResp_code());
			tlmposRespNote.setResp_code(TlmposExptMsgEnum.QiTaShiBai.getResp_code());
		} finally {
			final Map<String, String> retMap = this.convertMapType_cwbSearch(tlmposRespNote, tlmpos, rootnote);

			final String responseXml = TlmposXMLHandler.createXMLMessage_SearchCwb(retMap);// 生成响应报文
			this.logger.info("[" + rootnote.getTransaction_Header().getTransaction_id() + "]返回XML:" + responseXml);
			return responseXml;
		}

	}

	/**
	 * 查询其他信息
	 * 
	 * @param tlmposRespNote
	 * @return
	 */
	private TlmposRespNote SearchCwbDetailBytlmpos(TlmposRespNote tlmposRespNote, Tlmpos tlmpos) {
		String consignee_contact = tlmposRespNote.getCwbOrder().getConsigneemobile() + "," + tlmposRespNote.getCwbOrder().getConsigneephone();
		if (tlmpos.getIsshowPhone() == 0) { // 不显示联系方式
			consignee_contact = "";
		}

		if (consignee_contact.length() > 30) {
			consignee_contact = consignee_contact.substring(0, 30);
		}
		tlmposRespNote.setConsignee_contact(consignee_contact);
		String consignee = tlmposRespNote.getCwbOrder().getConsigneename();
		if (consignee.length() > 32) {
			tlmposRespNote.setConsignee(consignee.substring(0, 32));
		} else {
			tlmposRespNote.setConsignee(consignee);
		}

		/**
		 * 通联规定：一共是7位数，后四位是在页面设置 如果传000000，则无需分账，无任何要求。 如：A000001
		 */
		/*
		 * 有关分账标识的定义： 0000001 account_keyword 7 N 分账标识符 目前分账标识（即电商代码） 7位
		 * 第一位：表示是否可以进行pos撤销，A：表示不允许在pos上做撤销；0：表示可以做撤销交易
		 * 第二位：0表示可以刷卡也可以现金；1现金；2刷卡 第三位业务预留，目前取值0； 第四位到第七位表示序号：例如：0001
		 */

		PoscodeMapp codemapping = this.poscodeMappDAO.getPosCodeByKey(tlmposRespNote.getCwbOrder().getCustomerid(), PosEnum.TongLianPos.getKey());
		String end4str = ""; // 后四位 查询POS商户映射上面得出
		if (codemapping != null) {
			end4str = (codemapping.getCustomercode() == null) || codemapping.getCustomercode().isEmpty() ? "0000" : codemapping.getCustomercode();
		}

		String idx1 = tlmpos.getIsbackout() == 1 ? "0" : "A";
		String idx2 = "0";
		String idx3 = "0";

		String normal_code = idx1 + idx2 + idx3 + end4str;

		tlmposRespNote.setMerchant_code(normal_code); // 支付方判断.

		if (tlmposRespNote.getCwbOrder().getReceivablefee().doubleValue() > 0) { // 是淘宝的订单
			tlmposRespNote.setMerchant_biz_type("01");// 非淘宝COD订单
		} else {
			tlmposRespNote.setMerchant_biz_type("02");// 非淘宝非COD订单
		}

		return tlmposRespNote;
	}

	private Map<String, String> convertMapType_cwbSearch(TlmposRespNote tlmposRespNote, Tlmpos tlmpos, Transaction rootnote) {

		Map<String, String> retMap = new HashMap<String, String>();
		// 放入map

		String remark = this.getRemarkByPaytype(tlmposRespNote, tlmpos);

		retMap.put("transaction_id", tlmposRespNote.getTransaction_id());
		retMap.put("resp_code", tlmposRespNote.getResp_code());
		retMap.put("resp_msg", tlmposRespNote.getResp_msg());
		retMap.put("resp_time", DateTimeUtil.getNowTime("yyyyMMddHHmmss"));
		retMap.put("requester", tlmpos.getRequester());
		retMap.put("target", tlmpos.getTargeter());
		retMap.put("consignee", tlmposRespNote.getCwbOrder() == null ? "" : tlmposRespNote.getCwbOrder().getConsigneename());
		retMap.put("consignee_address", tlmposRespNote.getCwbOrder() == null ? "" : tlmposRespNote.getCwbOrder().getConsigneeaddress());
		retMap.put("consignee_contact", tlmposRespNote.getCwbOrder() == null ? "" : tlmposRespNote.getConsignee_contact());
		retMap.put("remark", remark);

		retMap.put("order_no", tlmposRespNote.getCwbOrder() == null ? rootnote.getTransaction_Body().getOrder_no() : tlmposRespNote.getCwbOrder().getCwb());
		retMap.put("e_order_no", rootnote.getTransaction_Body().getTranscwb() == null ? "" : rootnote.getTransaction_Body().getTranscwb());
		retMap.put("amt", tlmposRespNote.getCwbOrder() == null ? "0" : tlmposRespNote.getCwbOrder().getReceivablefee() + "");
		retMap.put("account_keyword", tlmposRespNote.getMerchant_code());// 分账标识符,
																			// 配送商户编号

		// 生成待加密的字符串
		String str = TlmposXMLHandler.createMACXML_SearchCwb(retMap);

		this.logger.info("MI0010签名串信息：" + str);

		String MAC = this.CreateRespSign(tlmpos, str);
		retMap.put("MAC", MAC);
		return retMap;
	}

	private String getRemarkByPaytype(TlmposRespNote tlmposRespNote, Tlmpos tlmpos) {
		String remark;
		if ((tlmposRespNote == null) || (tlmposRespNote.getCwbOrder() == null) || (tlmposRespNote.getCwbOrder().getPaywayid() == 0)) {
			return null;
		}
		if (tlmposRespNote.getCwbOrder().getPaywayid() == PaytypeEnum.Xianjin.getValue()) {
			remark = "现金支付";
		} else if (tlmposRespNote.getCwbOrder().getPaywayid() == PaytypeEnum.Pos.getValue()) {
			remark = "pos支付";
		} else if (tlmposRespNote.getCwbOrder().getPaywayid() == PaytypeEnum.Zhipiao.getValue()) {
			remark = "支票支付";
		} else {
			remark = "其他";
		}

		if (tlmpos.getIsshowPaytype() == 0) { // 不显示支付方式
			remark = "";
		}
		return remark;
	}

}
