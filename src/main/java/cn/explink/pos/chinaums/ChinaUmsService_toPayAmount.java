package cn.explink.pos.chinaums;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.amazon.Amazon;
import cn.explink.b2c.amazon.AmazonService;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.JointService;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.domain.User;
import cn.explink.enumutil.CwbOrderTypeIdEnum;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.ExceptionCwbErrorTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.exception.CwbException;
import cn.explink.pos.alipay.AliPayExptMessageEnum;
import cn.explink.pos.chinaums.xml.Transaction;
import cn.explink.pos.tools.EmployeeInfo;
import cn.explink.pos.tools.PosEnum;
import cn.explink.util.DateTimeUtil;

@Service
public class ChinaUmsService_toPayAmount extends ChinaUmsService {
	private Logger logger = LoggerFactory.getLogger(ChinaUmsService_toPayAmount.class);

	@Autowired
	JointService jointService;
	@Autowired
	AmazonService amazonService;

	/**
	 * 付款接口
	 * 
	 * @param request
	 * @param service_code
	 * @param jobject
	 * @param yeePay
	 * @return
	 */
	public String toPayAmountForPos(Transaction rootnote, ChinaUms chinaUms) {
		ChinaUmsRespNote respNote = new ChinaUmsRespNote();
		try {
			EmployeeInfo employee = new EmployeeInfo();
			employee = jiontDAO.getEmployeeInfo(rootnote.getTransaction_Header().getEmployno());
			if (employee == null) {
				rootnote.getTransaction_Header().setResponse_code(ChinaUmsExptMessageEnum.NoUserName.getResp_code());
				rootnote.getTransaction_Header().setResponse_msg(ChinaUmsExptMessageEnum.NoUserName.getResp_msg());
				return chinaUmsService_public.createXML_toExptFeedBack(chinaUms, rootnote);
			}
			respNote = BuildChinaumsRespClassAndSign(rootnote);
			respNote = ExtraParamsforPay(respNote, rootnote);
			respNote = ExcutePosPayMethod(respNote.getCwbOrder(), respNote, rootnote,chinaUms.getVersion()); // 更新支付交易数据

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("chinaums-POS支付发生不可预知的异常", e);
		}

		// 生成返回的xml字符串
		Map<String, String> respMap = convertMapType_PayAmount(respNote, chinaUms, rootnote);
		String responseXml = ChinaUmsXMLHandler.createXMLMessage_payAmount(respMap);
		logger.info("返回chinaums数据成功![" + rootnote.getTransaction_Header().getTranstype() + "]-支付签收-返回XML:" + responseXml);
		return responseXml;
	}

	/**
	 * 执行更新POS支付数据的方法
	 * 
	 * @return
	 * @throws Exception
	 */
	private ChinaUmsRespNote ExcutePosPayMethod(CwbOrder cwbOrder, ChinaUmsRespNote respNote, Transaction rootnote,int version) throws Exception {
		try {
			double receivablefee_add = respNote.getDeliverstate().getReceivedfee().doubleValue() + Double.parseDouble(rootnote.getTransaction_Body().getCod()); // 追加
			if (respNote.getPay_type() == 0) {
				respNote.setResp_code(ChinaUmsExptMessageEnum.Success.getResp_code());
				respNote.setResp_msg(ChinaUmsExptMessageEnum.Success.getResp_msg());
				return respNote;
			}

			/**
			 * 获取亚马逊customerid
			 */
			long amazonCustid = 0;
			if (jointService.getStateForJoint(B2cEnum.Amazon.getKey()) != 0) {
				Amazon amazon = amazonService.getAmazon(B2cEnum.Amazon.getKey());
				amazonCustid = Long.valueOf(amazon.getCustomerid());
			}

			// 原支付方式是POS，现支付方式是现金 只限制亚马逊
			if (amazonCustid != 0 && cwbOrder.getCustomerid() == amazonCustid) {

				if (respNote.getPay_type() == PaytypeEnum.Xianjin.getValue() && cwbOrder.getPaywayid() == PaytypeEnum.Pos.getValue()) {
					respNote.setResp_code(ChinaUmsExptMessageEnum.BuKezhifu.getResp_code());
					respNote.setResp_msg(ChinaUmsExptMessageEnum.BuKezhifu.getResp_msg());
					return respNote;
				}
			}

			if (respNote.getDeliverstate().getBusinessfee().compareTo(BigDecimal.ZERO) == 0 || cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) {
				ExcuteCwbSignHandler(cwbOrder, respNote.getDeliverstate(), respNote, rootnote,version);
				respNote.setResp_code(ChinaUmsExptMessageEnum.Success.getResp_code());
				respNote.setResp_msg(ChinaUmsExptMessageEnum.Success.getResp_msg());
				return respNote;
			} else {
				String acq_type = "sigle";// rootnote.getTransaction_Body().getAcq_type();
											// //是否分单支付 sigle,splie
				int payway = Integer.parseInt(rootnote.getTransaction_Body().getPayway());
				try {
					int deliverystate = (cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Peisong.getValue() ? DeliveryStateEnum.PeiSongChengGong.getValue()
							: DeliveryStateEnum.ShangMenHuanChengGong.getValue());
					CwbOrder corder = cwbOrderService.posPay(cwbOrder.getCwb(), BigDecimal.valueOf(receivablefee_add), cwbOrder.getReceivablefee(), payway, respNote.getPodremark(),
							respNote.getTrackinfo(), respNote.getDeliverid(), respNote.getDeliverstate(), acq_type, deliverystate);
					
					//leoliao 2016-02-03
					long deliverid = respNote.getDeliverid();
					try{
						//获取派送员所属机构(站点)-leoliao 2016-02-03
						long deliverybranchid = 0;							
						User deliverUser = getUser(deliverid);
						if(deliverUser != null){
							deliverybranchid = deliverUser.getBranchid();
						}
						
						//更新反馈表deliverybranchid字段值为派送员所属机构id
						if(deliverid > 0 && deliverybranchid > 0){
							deliveryStateDAO.updateDeliverybranchid(cwbOrder.getCwb(), deliverybranchid);
						}
					}catch(Exception ex){
						logger.error("ChinaUmsService_toPayAmount deliverid={} Exception={}", deliverid, ex.getMessage());
						ex.printStackTrace(System.out);
					}
					//leoliao 2016-02-03 end
					
					respNote.setResp_code(ChinaUmsExptMessageEnum.Success.getResp_code());
					respNote.setResp_msg(ChinaUmsExptMessageEnum.Success.getResp_msg());
					try {
						deliveryStateDAO.updateDelState(cwbOrder.getCwb(), DateTimeUtil.getNowTime(),
								getSignpeopleProxy(cwbOrder, rootnote,version), deliverystate, respNote.getDeliverid());// 设置成已反馈

						posPayDAO.save_PosTradeDetailRecord(cwbOrder.getCwb(), respNote.getPodremark(), receivablefee_add, respNote.getDeliverid(), respNote.getPay_type(), respNote.getTrackinfo(),
								"", getSignerType(rootnote,version), "", 1, 1, acq_type, PosEnum.ChinaUms.getMethod(), 0, rootnote.getTransaction_Header()
										.getTermid());

						DeliveryState deliverstate = deliveryStateDAO.getActiveDeliveryStateByCwb(cwbOrder.getCwb());
						ExcuteCwbSignHandler(corder, deliverstate, respNote, rootnote,version);
						
						//更新正向订单的deliverystate, added by neo01.huang
						//cwbOrderService.updateForwardOrderDeliveryState(cwbOrder.getCwbordertypeid(), cwbOrder.getCwb(), DeliveryStateEnum.PeiSongChengGong.getValue());

					} catch (Exception e) {
						logger.error("支付异常cwb=" + cwbOrder.getCwb(), e);
						respNote.setResp_code(ChinaUmsExptMessageEnum.BuKezhifu.getResp_code());
						respNote.setResp_msg(ChinaUmsExptMessageEnum.BuKezhifu.getResp_msg());
						return respNote;
					}
				} catch (CwbException e) {
					User user = getUser(respNote.getDeliverid());
					exceptionCwbDAO.createExceptionCwbScan(cwbOrder.getCwb(), e.getFlowordertye(), e.getMessage(), user.getBranchid(), user.getUserid(), cwbOrder == null ? 0 : cwbOrder.getCustomerid(),
							0, 0, 0, "",cwbOrder.getCwb());

					if (e.getError().getValue() == ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO.getValue()) {
						respNote.setResp_code(ChinaUmsExptMessageEnum.ChaXunYiChang.getResp_code());
						respNote.setResp_msg(ChinaUmsExptMessageEnum.ChaXunYiChang.getResp_msg());
					} else if (e.getError().getValue() == ExceptionCwbErrorTypeEnum.DingDanYiZhiFu.getValue()) {
						respNote.setResp_code(ChinaUmsExptMessageEnum.JinEyichang.getResp_code());
						respNote.setResp_msg(ChinaUmsExptMessageEnum.JinEyichang.getResp_msg());
					} else if (e.getError().getValue() == ExceptionCwbErrorTypeEnum.ZhiFuAmountExceiton.getValue()) {
						respNote.setResp_code(ChinaUmsExptMessageEnum.YingzhifujineYichang.getResp_code());
						respNote.setResp_msg(ChinaUmsExptMessageEnum.YingzhifujineYichang.getResp_msg());
					} else if (e.getError().getValue() == ExceptionCwbErrorTypeEnum.STATE_CONTROL_ERROR.getValue()) {
						respNote.setResp_code(ChinaUmsExptMessageEnum.BuKezhifu.getResp_code());
						respNote.setResp_msg(ChinaUmsExptMessageEnum.BuKezhifu.getResp_msg());
					} else {
						respNote.setResp_code(ChinaUmsExptMessageEnum.BuKezhifu.getResp_code());
						respNote.setResp_msg(ChinaUmsExptMessageEnum.BuKezhifu.getResp_msg());
					}
					logger.error("chinaUms支付异常[" + rootnote.getTransaction_Header().getTranstype() + "]", e);
				}
				logger.info(respNote.getTrackinfo());

				return respNote;
			}

		} catch (CwbException e1) {
			User user = getUser(respNote.getDeliverid());
			exceptionCwbDAO.createExceptionCwb(cwbOrder.getCwb(), e1.getFlowordertye(), e1.getMessage(), user.getBranchid(), user.getUserid(), cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0, 0,
					0, "");
			logger.error("chinaums支付处理业务逻辑异常！小件员=" + respNote.getDelivery_man() + ",订单号=" + respNote.getOrder_no() + ",异常原因=" + e1.getMessage());
			return DealWithCatchCwbException(respNote, e1);

		}

	}

	private int getSignerType(Transaction rootnote,int version) {
		if(version==1||version==2){
			int signflag=rootnote.getTransaction_Body().getSignflag();
			return signflag==0?1:2;
		}
		return "0".equals(rootnote.getTransaction_Body().getSignpeople()) ? 1 : 2;
	}

	private String getSignpeopleProxy(CwbOrder cwbOrder, Transaction rootnote,int version) {
		if(version==1||version==2){ //陕西城联版本
			//int signflag=rootnote.getTransaction_Body().getSignflag();
			return rootnote.getTransaction_Body().getSigner();
		}
		return "0".equals(rootnote.getTransaction_Body().getSignpeople()) ? cwbOrder.getConsigneename() : "他人签收";
	}

	/**
	 * 处理签收的方法
	 * 
	 * @param alipayRespNote
	 * @return
	 */
	private ChinaUmsRespNote ExcuteCwbSignHandler(CwbOrder cwbOrder, DeliveryState deliverstate, ChinaUmsRespNote respNote, Transaction rootnote,int version) {
		try {
			
			BigDecimal totalAmount = deliverstate.getPos().add(deliverstate.getCash()).add(deliverstate.getCheckfee()).add(deliverstate.getOtherfee());
			BigDecimal pos = deliverstate.getPos();
			BigDecimal cash = deliverstate.getCash();
			BigDecimal paybackedfee = deliverstate.getReturnedfee();
			BigDecimal Businessfee = deliverstate.getBusinessfee();
			if (totalAmount.compareTo(BigDecimal.ZERO) == 0) {

				if (cwbOrder.getCwbordertypeid() == CwbOrderTypeIdEnum.Shangmentui.getValue()) { // 应退款
					pos = BigDecimal.ZERO;
					cash = BigDecimal.ZERO;
					paybackedfee = deliverstate.getBusinessfee();
				} else if (Businessfee.compareTo(BigDecimal.ZERO) > 0) { // 说明应收款大于0，且支付接口没有执行完毕，返回其他异常，等待支付处理完毕，pos重发签收接口。
					respNote.setResp_code(AliPayExptMessageEnum.QiTaShiBai.getResp_code());
					respNote.setResp_msg("未支付订单不可签收");
					logger.info("未支付订单不可签收cwb={}", respNote.getOrder_no());
					return respNote;
				}

			}

			long deliverid = deliverstate.getDeliveryid();
			long deliverbranchid = deliverstate.getDeliverybranchid();

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("deliverid", deliverid);
			parameters.put("podresultid", getPodResultIdByCwb(cwbOrder.getCwbordertypeid() + ""));
			parameters.put("backreasonid", (long) 0);
			parameters.put("leavedreasonid", (long) 0);
			parameters.put("receivedfeecash", cash);
			parameters.put("receivedfeepos", pos);
			parameters.put("receivedfeecheque", deliverstate.getCheckfee());
			parameters.put("receivedfeeother", BigDecimal.ZERO);
			parameters.put("paybackedfee", paybackedfee);
			parameters.put("podremarkid", (long) 0);

			parameters.put("posremark", "POS反馈");
			parameters.put("checkremark", "");
			parameters.put("deliverstateremark", respNote.getTrackinfo());
			parameters.put("owgid", 0);
			parameters.put("sessionbranchid", deliverbranchid);
			parameters.put("sessionuserid", deliverid);
			parameters.put("sign_typeid", 1); // 是否 已签收 （0，未签收，1已签收）
			parameters.put("sign_man", getSignpeopleProxy(cwbOrder, rootnote,version));
			parameters.put("sign_time", DateTimeUtil.getNowTime());
			parameters.put("nosysyemflag", "1");//
			// add by jian_xie 2016-10-12 当全部退
			parameters.put("comefrompage", "1");
			// cwbOrderService.deliverStatePod(getUser(deliverid),alipayRespNote.getOrder_no(),parameters);
			cwbOrderService.deliverStatePod(getUser(respNote.getDeliverid()), respNote.getOrder_no(), respNote.getOrder_no(), parameters);

			deliveryStateDAO.updateOperatorIdByCwb(deliverid, respNote.getOrder_no());

			String sign_remark = "";
			respNote.setSign_remark(sign_remark);
			// posPayDAO.save_PosTradeDetailRecord(respNote.getOrder_no(),
			// "",respNote.getCwbOrder().getReceivablefee().doubleValue(), 0,
			// 0,
			// "",respNote.getCwbOrder().getConsigneename(),respNote.getSign_type(),respNote.getSign_remark(),2,1,"",PosEnum.ChinaUms.getMethod(),0,rootnote.getTransaction_Header().getTermid());
			// logger.info(sign_remark);
			//
			posPayDAO.save_PosTradeDetailRecord(cwbOrder.getCwb(), respNote.getPodremark(), respNote.getCwbOrder().getReceivablefee().doubleValue(), respNote.getDeliverid(), respNote.getPay_type(),
					respNote.getTrackinfo(), getSignpeopleProxy(cwbOrder, rootnote,version),getSignerType(rootnote,version), "", 2, 1, "", PosEnum.ChinaUms.getMethod(), 0, rootnote.getTransaction_Header().getTermid());

			respNote.setResp_code(AliPayExptMessageEnum.Success.getResp_code());
			respNote.setResp_msg(AliPayExptMessageEnum.Success.getResp_msg());
		} catch (CwbException e1) {
			User user = getUser(respNote.getDeliverid());
			exceptionCwbDAO.createExceptionCwbScan(respNote.getOrder_no(), e1.getFlowordertye(), e1.getMessage(), user.getBranchid(), user.getUserid(), cwbOrder == null ? 0 : cwbOrder.getCustomerid(), 0,
					0, 0, "",respNote.getOrder_no());
			if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO.getValue()) {
				logger.error("chinaUms运单签收,没有检索到数据" + respNote.getOrder_no() + ",小件员：" + respNote.getDelivery_man(), e1);
				respNote.setResp_code(ChinaUmsExptMessageEnum.ChaXunYiChang.getResp_code());
				respNote.setResp_msg(ChinaUmsExptMessageEnum.ChaXunYiChang.getResp_msg());

			} else if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.BU_SHI_ZHE_GE_XIAO_JIAN_YUAN_DE_HUO.getValue()) {
				respNote.setResp_code(ChinaUmsExptMessageEnum.ChaXunYiChang.getResp_code());
				respNote.setResp_msg(ChinaUmsExptMessageEnum.ChaXunYiChang.getResp_msg());
				logger.error("chinaUms运单签收,不是此小件员的货" + respNote.getOrder_no() + ",当前小件员：" + respNote.getDelivery_man() + e1);
			}
		}
		return respNote;
	}

	/**
	 * 处理支付宝的异常业务逻辑,并转化为对象
	 * 
	 * @param billRespNote
	 * @param e1
	 * @return
	 */
	private ChinaUmsRespNote DealWithCatchCwbException(ChinaUmsRespNote respNote, CwbException e1) {
		if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.YI_CHANG_DAN_HAO.getValue()) {
			respNote.setResp_code(ChinaUmsExptMessageEnum.ChaXunYiChang.getResp_code());
			respNote.setResp_msg(ChinaUmsExptMessageEnum.ChaXunYiChang.getResp_msg());
			return respNote;
		}
		if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.DingDanYiZhiFu.getValue()) {
			respNote.setResp_code(ChinaUmsExptMessageEnum.YiShouKuanWeiQianShou.getResp_code());
			respNote.setResp_msg(ChinaUmsExptMessageEnum.YiShouKuanWeiQianShou.getResp_msg());
			return respNote;
		}
		if (e1.getError().getValue() == ExceptionCwbErrorTypeEnum.ZhiFuAmountExceiton.getValue()) {
			respNote.setResp_code(ChinaUmsExptMessageEnum.YingShouJinEYiChang.getResp_code());
			respNote.setResp_msg(ChinaUmsExptMessageEnum.YingShouJinEYiChang.getResp_msg());
			return respNote;
		}
		respNote.setResp_code(ChinaUmsExptMessageEnum.QiTaShiBai.getResp_code());
		respNote.setResp_msg(ChinaUmsExptMessageEnum.QiTaShiBai.getResp_msg());
		return respNote;
	}

	private ChinaUmsRespNote ExtraParamsforPay(ChinaUmsRespNote respNote, Transaction rootnote) {
		String terminal_id = rootnote.getTransaction_Header().getTermid();
		String postrace = rootnote.getTransaction_Body().getPostrace();
		String trace_no = rootnote.getTransaction_Body().getBanktrace();
		int pay_type = rootnote.getTransaction_Body().getPayway().equals("01") ? 1 : (rootnote.getTransaction_Body().getPayway().equals("02") ? 2 : 0);
		respNote.setTerminal_id(terminal_id);// 终端号
		respNote.setTrace_no(trace_no); // 银行参考号
		respNote.setAlipay_Pay_type(pay_type); // 付款方式
		switch (respNote.getAlipay_Pay_type()) {
		case 1:
			respNote.setPay_type(PaytypeEnum.Xianjin.getValue());
			respNote.setPodremark(PaytypeEnum.Xianjin.getText());
			break;
		case 2:
			respNote.setPay_type(PaytypeEnum.Pos.getValue());
			respNote.setPodremark(PaytypeEnum.Pos.getText());
			break;
		case 0:
			respNote.setPay_type(0); // 无货款
			respNote.setPodremark("无货款");
			break;

		}
		String podremark = respNote.getPodremark();
		String trackinfo = "chinaums运单支付,订单号:" + respNote.getOrder_no() + ",支付方式:" + podremark + ",终端号：" + terminal_id + ",流水号:" + postrace + ",银行参考号：" + trace_no;
		respNote.setOrder_amt(Double.parseDouble(rootnote.getTransaction_Body().getCod()));
		respNote.setPodremark(podremark);
		respNote.setTrackinfo(trackinfo);
		respNote.setSign_remark(trackinfo);// 流水号
		logger.info(trackinfo);
		return respNote;
	}

	private Map<String, String> convertMapType_PayAmount(ChinaUmsRespNote chinaUmsRespNote, ChinaUms chinaUms, Transaction rootnote) {
		Map<String, String> retMap = new HashMap<String, String>();

		// 放入map
		retMap.put("version", rootnote.getTransaction_Header().getVersion());
		retMap.put("transtype", rootnote.getTransaction_Header().getTranstype());
		retMap.put("employno", rootnote.getTransaction_Header().getEmployno());
		retMap.put("termid", rootnote.getTransaction_Header().getTermid());
		retMap.put("response_time", DateTimeUtil.getNowTimeNo());
		retMap.put("response_code", chinaUmsRespNote.getResp_code());
		retMap.put("response_msg", chinaUmsRespNote.getResp_msg());
		// 生成待加密的字符串
		String str = ChinaUmsXMLHandler.createMACXML_payAmount(retMap);
		String r = CreateRespSign(chinaUms, str);
		retMap.put("mac", r.toUpperCase());
		return retMap;
	}

}
