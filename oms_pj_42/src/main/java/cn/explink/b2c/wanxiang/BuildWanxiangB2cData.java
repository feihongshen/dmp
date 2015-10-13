package cn.explink.b2c.wanxiang;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2CDataDAO;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BuildWanxiangB2cData {
	private Logger logger = LoggerFactory.getLogger(BuildWanxiangB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	WanxiangService wanxiangService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	B2CDataDAO b2CDataDAO;

	public String BuildWanXiangMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, DmpDeliveryState deliveryState, long delivery_state, ObjectMapper objectMapper)
			throws IOException, JsonGenerationException, JsonMappingException {
		if (!b2ctools.isB2cOpen(B2cEnum.Wanxiang.getKey())) {
			logger.info("未开0万象0的状态反馈接口!");
			return null;
		}
		String xmlnote = "";
		Wanxiang wx = wanxiangService.getWanxiang(B2cEnum.Wanxiang.getKey());

		if (wx.getVersion() == 0) { // 旧版本的推送方式

			String receivedStatus = wanxiangService.getFlowEnum(flowOrdertype, delivery_state, cwbOrder.getCwbordertypeid());
			if (receivedStatus == null) {
				logger.info("订单号：{} 不属于[万象]所需要的json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
				return null;
			}
			getVersionOldSendDatas(orderFlow, flowOrdertype, cwbOrder, deliveryState, receivedStatus, wx);

		} else {

			String receivedStatus = wanxiangService.getFlowEnumNew(flowOrdertype, delivery_state, cwbOrder.getCwbordertypeid());
			if (receivedStatus == null) {
				logger.info("订单号：{} 不属于[万象]所需要的json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
				return null;
			}

			xmlnote = getVersionNewSendDatas(orderFlow, flowOrdertype, cwbOrder, deliveryState, receivedStatus, wx);

		}
		return objectMapper.writeValueAsString(xmlnote);
	}

	private void getVersionOldSendDatas(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, DmpDeliveryState deliveryState, String receivedStatus, Wanxiang wx) throws IOException,
			JsonGenerationException, JsonMappingException {
		WanxiangJson json = new WanxiangJson();
		json.setWx_BILLNO(cwbOrder.getCwb());
		json.setWx_PACKAGEID("");
		json.setWx_OPERATEDEP(wx.getBranchname()); // 操作部门
		json.setWx_OPERATETYPE(receivedStatus);
		json.setWx_OPERATETIME(DateTimeUtil.getNowTime());
		json.setWx_REASON(cwbOrder.getBackreason() == null ? "" : cwbOrder.getBackreason() + " " + cwbOrder.getLeavedreason() == null ? "" : cwbOrder.getLeavedreason());
		json.setWx_REMARK(orderFlowDetail.getDetail(orderFlow));
		if (deliveryState != null) {
			User deliveryUser = getDmpdao.getUserById(deliveryState.getDeliveryid());
			json.setWx_OPERATORNAME(deliveryUser.getRealname()); // 派件人
			json.setWx_OPERATORTEL(deliveryUser.getUsermobile()); // 派件人电话
		}

		json.setWx_CUSTOMER_CODE(cwbOrder.getConsigneeno()); // 客户编号

		String jsoncontent = JacksonMapper.getInstance().writeValueAsString(json).replaceAll("wx_", "");
		logger.info("当前[推送]0万象0状态={},jsoncontent={}", flowOrdertype, jsoncontent);

		try {
			String response = wanxiangService.postHTTPJsonDataToWanXiang(jsoncontent, wx.getUrl());
			logger.info("当前0万象0[返回]状态={},response={}", flowOrdertype, response);
		} catch (Exception e) {
			logger.error("推送万象接口发生未知异常,cwb=" + cwbOrder.getCwb(), e);
		}
	}

	/**
	 * 万象 新版开发 推送功能
	 * 
	 * @param orderFlow
	 * @param flowOrdertype
	 * @param cwbOrder
	 * @param deliveryState
	 * @param receivedStatus
	 * @param wx
	 * @throws IOException
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 */
	private String getVersionNewSendDatas(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, DmpDeliveryState deliveryState, String receivedStatus, Wanxiang wx) throws IOException,
			JsonGenerationException, JsonMappingException {
		try {

			User deliveryUser = null;
			if (deliveryState != null) {
				deliveryUser = getDmpdao.getUserById(deliveryState.getDeliveryid());
			}
			User operatorUser = getDmpdao.getUserById(orderFlow.getUserid());
			Branch operatorBranch = getDmpdao.getNowBranch(orderFlow.getBranchid());

			StringBuffer subxml = new StringBuffer("<?xml version='1.0' encoding='utf-8' standalone='yes' ?>");
			subxml.append("<request>");
			subxml.append("<bill_no>" + cwbOrder.getCwb() + "</bill_no>");
			subxml.append("<packageid></packageid>");
			subxml.append("<operateman>" + operatorUser.getRealname() + "</operateman>");
			subxml.append("<operatedep>" + operatorBranch.getBranchname() + "</operatedep>");
			subxml.append("<operatetype>" + receivedStatus + "</operatetype>");
			subxml.append("<operatetime>" + DateTimeUtil.formatDate(orderFlow.getCredate()) + "</operatetime>");
			subxml.append("<reason>" + (cwbOrder.getBackreason() == null ? "" : cwbOrder.getBackreason()) + "" + (cwbOrder.getLeavedreason() == null ? "" : cwbOrder.getLeavedreason()) + "</reason>");
			subxml.append("<remark>" + orderFlowDetail.getDetail(orderFlow) + "</remark>");
			subxml.append("<operatorname>" + (deliveryUser == null ? "" : deliveryUser.getRealname()) + "</operatorname>");
			subxml.append("<operatortel>" + (deliveryUser == null ? "" : deliveryUser.getUsermobile()) + "</operatortel>");
			subxml.append("<customer_code>" + cwbOrder.getConsigneeno() + "</customer_code>");
			subxml.append("</request>");

			// logger.info("当前[推送]0万象0状态={},USER_NAME={},PASSWORD={},jsoncontent={}",new
			// Object[]
			// {flowOrdertype,wx.getUser_name(),wx.getPass_word(),subxml.toString()
			// });

			// String
			// md5Data=MD5Util.md5(subxml.toString()+wx.getPrivate_key());
			// String
			// response=wanxiangService.postHTTPJsonDataToWanXiang_new(wx.getUser_name(),wx.getPass_word(),subxml.toString(),md5Data,wx.getUrl());
			// logger.info("当前0万象0[返回]状态={},response={}",flowOrdertype,response);

			return subxml.toString();
		} catch (Exception e) {
			logger.error("推送万象接口发生未知异常,cwb=" + cwbOrder.getCwb(), e);
			return null;
		}

	}
}
