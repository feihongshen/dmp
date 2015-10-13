package cn.explink.b2c.lechong;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.lechong.xml.UpdateInfo;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.dao.GetDmpDAO;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class BuildLeChongB2cData {
	private Logger logger = LoggerFactory.getLogger(BuildLeChongB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;

	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	LechongService lechongService;
	@Autowired
	private B2cTools b2ctools;

	public String buildLeChongMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, long delivery_state, DmpDeliveryState deliveryState, ObjectMapper objectMapper)
			throws IOException {
		String dmsReceiveStatus = lechongService.getFlowEnum(flowOrdertype, delivery_state);
		if (dmsReceiveStatus == null) {
			logger.info("订单号：{} 不属于[乐宠]所需要的json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return null;
		}

		Lechong le = lechongService.getLechong(B2cEnum.LeChong.getKey());

		String expt_msg = "";
		String expt_code = "";
		if (flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue() && (delivery_state == DeliveryStateEnum.QuanBuTuiHuo.getValue() || delivery_state == DeliveryStateEnum.BuFenTuiHuo.getValue())) {
			dmsReceiveStatus = LechongTrackEnum.JuShou.getState();
			ExptReason exptReason = b2ctools.getExptReasonByB2c(0, cwbOrder.getBackreasonid(), String.valueOf(cwbOrder.getCustomerid()), delivery_state);
			expt_code = exptReason.getExpt_code();
			expt_msg = exptReason.getExpt_msg();
		}

		/*
		 * String xml = buildTrackStatusXML(orderFlow, cwbOrder,
		 * dmsReceiveStatus, le, exptRemark, expt_msg);
		 * logger.info("乐宠-跟踪信息请求xml={}", xml);
		 * 
		 * String responseXML = RestHttpServiceHanlder.sendHttptoServer(xml,
		 * le.getFeedbackUrl());
		 * 
		 * logger.info("乐宠-跟踪信息返回xml={}", responseXML);
		 */

		/*
		 * String order_status = getOrderResultEnum(flowOrdertype,
		 * delivery_state);// 获取配送结果
		 * 
		 * if (order_status == null) { return null; }
		 */
		String operatorname = getDmpdao.getUserById(orderFlow.getUserid()).getRealname();

		String branchname = getDmpdao.getNowBranch(orderFlow.getBranchid()).getBranchname();

		UpdateInfo note = new UpdateInfo();
		note.setDoID(cwbOrder.getCwb());
		note.setMailNO(cwbOrder.getTranscwb());
		note.setStatus(dmsReceiveStatus);
		note.setReceiveEmployee(operatorname);
		note.setCity(branchname);
		note.setLogisticProvider(le.getDname());
		note.setLogisticProviderID(le.getDcode());
		if (dmsReceiveStatus == LechongTrackEnum.JuShou.getState()) {
			note.setRemark(expt_msg);
			note.setSTATUS_REASON(expt_code);
		}
		note.setAcceptTime(DateTimeUtil.formatDate(orderFlow.getCredate()));
		String Content = note.getLogisticProvider() + note.getLogisticProviderID() + note.getDoID() + note.getMailNO() + note.getRemark() + note.getAcceptTime() + note.getReceiveEmployee();
		String key = "1q20o9";
		note.setMD5Key(MD5Util.md5(Content + key));
		return objectMapper.writeValueAsString(note);

	}

	public String getOrderResultEnum(long flowOrdertype, long delivery_state) {
		String order_status = null;
		if (flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (delivery_state == DeliveryStateEnum.PeiSongChengGong.getValue() || delivery_state == DeliveryStateEnum.ShangMenTuiChengGong.getValue() || delivery_state == DeliveryStateEnum.ShangMenHuanChengGong
						.getValue())) {
			order_status = LechongTrackEnum.QianShou.getState();
		}

		if (flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue()
				&& (delivery_state == DeliveryStateEnum.QuanBuTuiHuo.getValue() || delivery_state == DeliveryStateEnum.BuFenTuiHuo.getValue() || delivery_state == DeliveryStateEnum.ShangMenJuTui
						.getValue())) {
			order_status = LechongTrackEnum.JuShou.getState();
		}
		return order_status;
	}

	public String buildTrackStatusXML(DmpOrderFlow orderFlow, DmpCwbOrder cwbOrder, String dmsReceiveStatus, Lechong le, String exptRemark, String expt_msg) {
		String operatorname = getDmpdao.getUserById(orderFlow.getUserid()).getRealname();

		String branchname = getDmpdao.getNowBranch(orderFlow.getBranchid()).getBranchname();

		String Content = le.getDname() + le.getDcode() + cwbOrder.getTranscwb() + cwbOrder.getCwb() + dmsReceiveStatus + DateTimeUtil.formatDate(orderFlow.getCredate(), "yyyyMMddHHmmss")
				+ operatorname;
		String key = "1q20o9";
		String Xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "<UpdateInfo>" + "<LogisticProvider>" + le.getDname() + "</LogisticProvider>" + "<LogisticProviderID>" + le.getDcode()
				+ "</LogisticProviderID>" + "<DoID>" + cwbOrder.getTranscwb() + "</DoID>" + "<MailNO>" + cwbOrder.getCwb() + "</MailNO>" + "<Status>" + dmsReceiveStatus + "</Status>" + "<AcceptTime>"
				+ DateTimeUtil.formatDate(orderFlow.getCredate(), "yyyyMMddHHmmss") + "</AcceptTime>" + "<ReceiveEmployee>" + operatorname + "</ReceiveEmployee>" + "<City>" + branchname + "</City>"
				+ "<Remark>" + exptRemark + "</Remark>" + "<STATUS_REASON>" + expt_msg + "</STATUS_REASON>" + "<MD5Key>" + MD5Util.md5(Content + key) + "</MD5Key>" + "</UpdateInfo>";

		return Xml;
	}

}
