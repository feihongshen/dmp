package cn.explink.pos.chinaums;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.explink.domain.Branch;
import cn.explink.domain.CwbOrder;
import cn.explink.domain.DeliveryState;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.enumutil.PaytypeEnum;
import cn.explink.pos.chinaums.xml.Transaction;
import cn.explink.pos.tools.EmployeeInfo;
import cn.explink.util.DateTimeUtil;

@Service
public class ChinaUmsService_toCwbSearch extends ChinaUmsService {
	private Logger logger = LoggerFactory.getLogger(ChinaUmsService_toCwbSearch.class);

	/**
	 * 运单查询
	 * 
	 * @return
	 */
	public String toCwbSearch(Transaction rootnote, ChinaUms chinaUms) {
		ChinaUmsRespNote chinaUmsRespNote = new ChinaUmsRespNote();
		try {
			String username = rootnote.getTransaction_Header().getEmployno();
			EmployeeInfo employee = jiontDAO.getEmployeeInfo(username);
			if (employee == null) {
				chinaUmsRespNote.setResp_code(ChinaUmsExptMessageEnum.NoUserName.getResp_code());
				chinaUmsRespNote.setResp_msg(ChinaUmsExptMessageEnum.NoUserName.getResp_msg());
				logger.error("订单查询,没有此用户名!" + username);
			} else {
				chinaUmsRespNote = super.BuildChinaumsRespClass(rootnote);
				if (chinaUmsRespNote == null || chinaUmsRespNote.getCwbOrder() == null) {
					chinaUmsRespNote.setResp_code(ChinaUmsExptMessageEnum.ChaXunYiChang.getResp_code());
					chinaUmsRespNote.setResp_msg(ChinaUmsExptMessageEnum.ChaXunYiChang.getResp_msg());
					logger.error("chinaums运单查询没有检索到数据，当前小件员：", employee.getRealname());
				} else {
					chinaUmsRespNote.setResp_code(ChinaUmsExptMessageEnum.Success.getResp_code());
					chinaUmsRespNote.setResp_msg(ChinaUmsExptMessageEnum.Success.getResp_msg());
					logger.info("chinaums运单查询:查询到数据,当前小件员:{}", employee.getRealname());
				}
			}

		} catch (Exception e) {
			logger.error("chinaums运单查询未知异常!", e);
			chinaUmsRespNote.setResp_code(ChinaUmsExptMessageEnum.ChaXunYiChang.getResp_code());
			chinaUmsRespNote.setResp_msg(ChinaUmsExptMessageEnum.ChaXunYiChang.getResp_msg());
		}
		Map<String, String> retMap = convertMapType_cwbSearch(chinaUmsRespNote, chinaUms, rootnote);
		// 生成响应报文
		String responseXml = ChinaUmsXMLHandler.createXMLMessage_SearchCwb(retMap, chinaUmsRespNote);
		logger.info("[" + rootnote.getTransaction_Header().getTranstype() + "]-订单查询-返回XML:" + responseXml);
		return responseXml;
	}

	private Map<String, String> convertMapType_cwbSearch(ChinaUmsRespNote chinaUmsRespNote, ChinaUms chinaUms, Transaction rootnote) {

		Map<String, String> retMap = new HashMap<String, String>();

		// 放入map
		retMap.put("version", rootnote.getTransaction_Header().getVersion());
		retMap.put("transtype", rootnote.getTransaction_Header().getTranstype());
		retMap.put("employno", rootnote.getTransaction_Header().getEmployno());
		retMap.put("termid", rootnote.getTransaction_Header().getTermid());
		retMap.put("response_time", DateTimeUtil.getNowTimeNo());
		retMap.put("response_code", chinaUmsRespNote.getResp_code());
		retMap.put("response_msg", chinaUmsRespNote.getResp_msg());
		if (chinaUmsRespNote != null && chinaUmsRespNote.getCwbOrder() != null) {
			CwbOrder order = chinaUmsRespNote.getCwbOrder();
			DeliveryState deliverstate = chinaUmsRespNote.getDeliverstate();
			if (deliverstate != null) {
				Branch b = branchDAO.getBranchByBranchid(deliverstate.getDeliverybranchid());
				retMap.put("netcode", b.getBranchid() + "");// 站点
				retMap.put("netname", b.getBranchname());// 站点
			} else {
				Branch b = branchDAO.getBranchByBranchid(order.getCurrentbranchid());
				retMap.put("netcode", b.getBranchid() + "");// 站点
				retMap.put("netname", b.getBranchname());// 站点
			}
			retMap.put("weight", order.getCarrealweight() + "");// 重量
			retMap.put("goodscount", order.getSendcarnum() + "");// 件数
			retMap.put("cod", order.getReceivablefee() + "");// 代收款金额
			retMap.put("istopay", (order.getReceivablefee().doubleValue() > 0) ? "1" : "0");// 到付标志
																							// 0否，1是
			retMap.put("address", order.getConsigneeaddress());// 收件地址
			retMap.put("people", order.getConsigneename());// 收件人
			retMap.put("peopletel", order.getConsigneemobile());// 收件人联系电话
			retMap.put("sqpayway", order.getPaywayid() == PaytypeEnum.Xianjin.getValue() ? "01" : (order.getPaywayid() == PaytypeEnum.Pos.getValue() ? "02" : "00"));// 授权支付方式(单指代收货款)
																																										// 00不限制
																																										// 01现金
																																										// 02刷卡
			retMap.put("status", getOrdertype(order.getFlowordertype(), order.getDeliverystate()));// 快件状态
																									// 01
																									// 途中
																									// 02
																									// 派件员手中(可签收)
																									// 03已签收
																									// 04
																									// 问题件
																									// 05退回
																									// 06在站点
																									// 注：只有02是允许签收的，其它状态都不允许签收
			// retMap.put("memo",order.getCwbremark().replace("\n","").replace("\r",
			// "").replace("\r\n", "").replace("<", "").replace(">", ""));//备注
			retMap.put("memo", "");// 备注为空

			retMap.put("dssn", order.getCustomerid() + "");// 电商编号
															// 线上电商的客户编号,一般为ERP系统自己的编号。
			retMap.put("dsname", customerDAO.getCustomerById(order.getCustomerid()).getCustomername());// 电商名称
			retMap.put("dsorderno", order.getCwb());// 电商订单号
		}
		// 生成待加密的字符串
		String str = ChinaUmsXMLHandler.createMACXML_SearchCwb(retMap, chinaUmsRespNote);
		String r = CreateRespSign(chinaUms, str);
		retMap.put("mac", r.toUpperCase());
		return retMap;
	}

	private String getOrdertype(long floworderordertype, long deliverysate) {
		if (floworderordertype == FlowOrderTypeEnum.FenZhanLingHuo.getValue() || floworderordertype == FlowOrderTypeEnum.CheXiaoFanKui.getValue()
				|| floworderordertype == FlowOrderTypeEnum.FenZhanDaoHuoSaoMiao.getValue() || floworderordertype == FlowOrderTypeEnum.FenZhanDaoHuoYouHuoWuDanSaoMiao.getValue()) {
			return "02";
		}
		if ((floworderordertype == FlowOrderTypeEnum.YiShenHe.getValue() && deliverysate == DeliveryStateEnum.FenZhanZhiLiu.getValue())
				|| (floworderordertype == FlowOrderTypeEnum.YiFanKui.getValue() && deliverysate == DeliveryStateEnum.FenZhanZhiLiu.getValue())) {
			return "06";
		}
		if (((floworderordertype == FlowOrderTypeEnum.YiShenHe.getValue() || floworderordertype == FlowOrderTypeEnum.YiFanKui.getValue()) && (deliverysate == DeliveryStateEnum.PeiSongChengGong
				.getValue() || deliverysate == DeliveryStateEnum.ShangMenHuanChengGong.getValue() || deliverysate == DeliveryStateEnum.ShangMenTuiChengGong.getValue()))
				|| floworderordertype == FlowOrderTypeEnum.PosZhiFu.getValue()) {
			return "03";
		}
		if (((floworderordertype == FlowOrderTypeEnum.YiShenHe.getValue() || floworderordertype == FlowOrderTypeEnum.YiFanKui.getValue()) && (deliverysate == DeliveryStateEnum.JuShou.getValue() || deliverysate == DeliveryStateEnum.ShangMenJuTui
				.getValue()))) {
			return "05";
		}
		return "01";
	}

}
