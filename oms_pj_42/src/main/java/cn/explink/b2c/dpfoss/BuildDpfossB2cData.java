package cn.explink.b2c.dpfoss;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.dpfoss.sign.UploadSignRequest;
import cn.explink.b2c.dpfoss.track.UploadTrackRequest;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.JacksonMapper;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BuildDpfossB2cData {
	private Logger logger = LoggerFactory.getLogger(BuildDpfossB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	DpfossService dpfossService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	private B2cTools b2ctools;

	public Dpfoss getDpfoss(int b2ckey) {
		return dpfossService.getDpfossSettingMethod(b2ckey);
	}

	public String BuildDpfossMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, DmpDeliveryState deliveryState, long delivery_state, int b2ckey, ObjectMapper objectMapper)
			throws IOException, JsonGenerationException, JsonMappingException {

		String receivedStatus = dpfossService.getDpfossTrackEnum(flowOrdertype, delivery_state);
		Dpfoss dpfoss = this.getDpfoss(b2ckey);
		User user = getUserById(orderFlow.getUserid());
		// 发送跟踪信息给德邦
		if (receivedStatus != null) {

			UploadTrackRequest xmltrack = new UploadTrackRequest();
			xmltrack.setTraceId(String.valueOf(orderFlow.getFloworderid()));
			xmltrack.setWaybillNo(cwbOrder.getCwb());
			xmltrack.setAgentCompanyName(dpfoss.getAgentCompanyName());
			xmltrack.setAgentCompanyCode(dpfoss.getAgentCompanyCode());
			xmltrack.setAgentOrgName(dpfoss.getAgentOrgName());
			xmltrack.setAgentOrgCode(dpfoss.getAgentOrgCode());

			xmltrack.setOperationTime(orderFlow.getCredate());
			xmltrack.setOperationUserName(user.getRealname());
			xmltrack.setOperationDescribe(orderFlowDetail.getDetail(orderFlow));
			xmltrack.setOperationcity(getBrnachById(orderFlow.getBranchid()).getBranchname());
			xmltrack.setOperationtype(receivedStatus);

			xmltrack.setDispatchname(cwbOrder.getDeliverid() != 0 ? getUserById(cwbOrder.getDeliverid()).getRealname() : "");
			xmltrack.setDispatchphoneno(cwbOrder.getDeliverid() != 0 ? getUserById(cwbOrder.getDeliverid()).getUsermobile() : "");

			List<UploadTrackRequest> tracklist = new ArrayList<UploadTrackRequest>();
			tracklist.add(xmltrack);

			String trackinfo = JacksonMapper.getInstance().writeValueAsString(tracklist);

			dpfossService.SubmitTrackInfo(flowOrdertype, trackinfo, b2ckey);
		}

		/**
		 * 发送签收结果信息给德邦
		 */
		String receivedSigned = dpfossService.getDpfossSignedEnum(delivery_state);
		if (receivedSigned == null) {
			logger.info("订单号：{} 不属于0德邦0所需要的签收结果状态json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return null;
		}

		User deliveryUser = null;
		if (delivery_state != 0) {
			deliveryUser = getUserById(deliveryState.getDeliveryid());
		}

		UploadSignRequest xmlnote = new UploadSignRequest();
		xmlnote.setSignUpId(String.valueOf(cwbOrder.getOpscwbid()));
		xmlnote.setWaybillNo(cwbOrder.getCwb());
		xmlnote.setAgentCompanyName(dpfoss.getAgentCompanyName());

		xmlnote.setAgentCompanyCode(dpfoss.getAgentCompanyCode());
		// 测试utf8mb4字符问题 ,本地数据库不支持
		// xmlnote.setAgentCompanyCode("<consigneeName>杨格勇</consigneeName>");
		xmlnote.setAgentOrgName(dpfoss.getAgentOrgName());
		xmlnote.setAgentOrgCode(dpfoss.getAgentOrgCode());

		xmlnote.setReceiveTime(cwbOrder.getEmaildate()); // 入库时间
		xmlnote.setDeliveryTime(deliveryState.getCreatetime()); // 领货时间
		xmlnote.setDeliveryUserName(deliveryUser.getRealname()); // 送货人(代理的员工)
		xmlnote.setSignUpTime(DateTimeUtil.formatDate(orderFlow.getCredate())); // 签收时间
		xmlnote.setSignUpUserName(cwbOrder.getConsigneename());
		xmlnote.setGoodsNum((int) (cwbOrder.getSendcarnum()));
		xmlnote.setSignUpType(receivedSigned);

		String exptCode = "";
		exptCode = b2ctools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(), cwbOrder.getBackreasonid(), cwbOrder.getCustomerid() + "", deliveryState.getDeliverystate()).getExpt_code();
		if (exptCode == null || "".equals(exptCode)) {
			if (deliveryState.getDeliverystate() == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliveryState.getDeliverystate() == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
				exptCode = "OTHER"; // 默认
			}
		}

		xmlnote.setExceptionDescribe(exptCode);

		logger.info("订单号：{}封装成0德邦0所需要的json----状态：{}", cwbOrder.getCwb(), flowOrdertype);
		return objectMapper.writeValueAsString(xmlnote);

	}

	private User getUserById(long userid) {
		return getDmpdao.getUserById(userid);
	}

	private Branch getBrnachById(long branchid) {
		return getDmpdao.getNowBranch(branchid);
	}

}
