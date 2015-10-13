package cn.explink.b2c.tmall;

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
import cn.explink.b2c.tools.ExptReason;
import cn.explink.dao.GetDmpDAO;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BulidTmallB2cData {
	private Logger logger = LoggerFactory.getLogger(BulidTmallB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	TmallService tmallService;
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	B2CDataDAO b2CDataDAO;

	public String BuildTmallMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, long delivery_state, String tmall_key, ObjectMapper objectMapper) throws IOException,
			JsonGenerationException, JsonMappingException {

		String TmallReceiveStatus = tmallService.getTmallFlowEnum(flowOrdertype, delivery_state);

		Tmall tmall = tmallService.getTmallSettingMethod(Integer.valueOf(tmall_key)); // 获取配置信息
		if (tmall.getAcceptflag() == 4 || tmall.getAcceptflag() == 0) { // 以入库作为揽收标准
																		// //acceptflag=4
																		// //默认是导入作为揽收标准
			if (flowOrdertype == FlowOrderTypeEnum.RuKu.getValue()) {
				TmallReceiveStatus = TmallFlowEnum.TMS_ACCEPT.getTmall_state();
			}
		}

		if (TmallReceiveStatus == null) {
			logger.info("订单号：{} 不属于 天猫所需要的json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return null;
		} else {

			// 判断如果有多次入库则以第一次为准
			if (flowOrdertype == FlowOrderTypeEnum.RuKu.getValue()) {
				if (b2CDataDAO.checkIsRepeatDataFlag(orderFlow.getCwb(), flowOrdertype, null) > 0) {
					logger.info("RE: orderFlow send b2c 天猫环节信息重复,已过滤,flowordertype=4,cwb={}", orderFlow.getCwb());
					return null;
				}
			}

			logger.info("订单号：{}封装成天猫所需要的json----开始,状态：{}", cwbOrder.getCwb(), flowOrdertype);
			TmallXMLNote tmallXMLNote = new TmallXMLNote();
			tmallXMLNote.setOrder_code(cwbOrder.getShipcwb());
			tmallXMLNote.setTms_order_code(cwbOrder.getCwb());
			tmallXMLNote.setOut_biz_code(cwbOrder.getOpscwbid() + "_1");

			long operatorId = orderFlow.getUserid();
			if (TmallReceiveStatus.equals(TmallFlowEnum.TMS_DELIVERING.getTmall_state())) {
				operatorId = cwbOrder.getDeliverid();
			}

			tmallXMLNote.setOperator(getDmpdao.getUserById(operatorId).getRealname());

			tmallXMLNote.setOperator_date(DateTimeUtil.formatDate(orderFlow.getCredate()));
			tmallXMLNote.setStatus(TmallReceiveStatus); // 状态
			tmallXMLNote.setContent(orderFlowDetail.getDetail(orderFlow)); // 状态更改描述

			tmallXMLNote.setService_code(tmall.getTms_service_code()); // 服务编码
																		// 新增20130523
			tmallXMLNote.setActionPushCode("old"); // old

			String expt_remark = "";
			String expt_msg = "";
			String userMobile = getDmpdao.getUserById(operatorId).getUsermobile();

			if (TmallReceiveStatus.equals(TmallFlowEnum.TMS_ERROR.getTmall_state())) { // 如果是异常，则需要填写tmall规定异常码

				int isCallBackError = tmall.getIsCallBackError(); // 是否回传TMS_ERROR异常信息
																	// 0回传 1不回传

				if (isCallBackError == 0) {
					ExptReason exptReason = b2ctools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(), cwbOrder.getBackreasonid(), cwbOrder.getCustomerid() + "", delivery_state);
					if (delivery_state == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
						expt_remark = "210";
						expt_msg = "丢失、缺件、污损";
					} else {
						expt_remark = "140";
						expt_msg = "客户要求更改派送时间";

						if (exptReason.getExpt_code() != null && !"".equals(exptReason.getExpt_code())) {
							expt_remark = exptReason.getExpt_code();
							expt_msg = exptReason.getExpt_msg();
						}
					}
					tmallXMLNote.setContent(expt_msg);
					userMobile = tmallService.getTmallSettingMethod(B2cEnum.Tmall.getKey()).getServiceTel();

					logger.info("构建tmall旧的异常反馈模式,cwb={}", cwbOrder.getCwb());
				} else { // 使用新的异常接口推送 兼容旧接口
					tmallXMLNote.setCpCode(tmall.getService_code());
					tmallXMLNote.setLogisticsId(cwbOrder.getTranscwb());
					tmallXMLNote.setMailNo(cwbOrder.getCwb());

					String actionCode = getActionCode(delivery_state);

					tmallXMLNote.setActionCode(actionCode);
					tmallXMLNote.setRemark(cwbOrder.getLeavedreason() + "," + cwbOrder.getBackreason());
					tmallXMLNote.setExceptionTime(DateTimeUtil.formatDate(orderFlow.getCredate()));

					tmallXMLNote.setExceptionCode(getExceptionCode(cwbOrder, delivery_state)); // 异常原因
					tmallXMLNote.setNextDispatchTime(cwbOrder.getResendtime() == null ? "" : cwbOrder.getResendtime()); // 再次派送时间
					tmallXMLNote.setOperatorMobile(userMobile); // 异常操作人手机
					tmallXMLNote.setOperatorPhone(""); // 异常操作人电话
					tmallXMLNote.setExceptionHappenedPlace(getDmpdao.getNowBranch(orderFlow.getBranchid()).getBranchname());// 异常发生地点

					tmallXMLNote.setActionPushCode("new"); // new 指令，推送时候用来判断新老

					logger.info("构建tmall新的异常反馈模式,cwb={}", cwbOrder.getCwb());
					expt_remark = getExceptionMsg(cwbOrder, delivery_state);

				}

			}

			tmallXMLNote.setRemark(expt_remark);
			tmallXMLNote.setOperator_contact(userMobile);

			tmallXMLNote.setReceivablefee(cwbOrder.getReceivablefee().doubleValue()); // 代收款
			tmallXMLNote.setMulti_shipcwb(cwbOrder.getMulti_shipcwb());

			logger.info("订单号：{}封装成天猫所需要的json----结束,状态：{}", cwbOrder.getCwb(), cwbOrder.getFlowordertype());
			return objectMapper.writeValueAsString(tmallXMLNote);
		}
	}

	private String getActionCode(long delivery_state) {
		for (TmallActionCodeEnum actionEnum : TmallActionCodeEnum.values()) {
			if (actionEnum.getDeliverystate() == delivery_state) {
				return actionEnum.getActionCode();
			}
		}
		return "P99";
	}

	private String getExceptionCode(DmpCwbOrder cwbOrder, long delivery_state) {
		String exceptionCode = "";
		ExptReason exptReason = b2ctools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(), cwbOrder.getBackreasonid(), cwbOrder.getCustomerid() + "", delivery_state);
		if (delivery_state == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
			exceptionCode = "1200"; // 丢失无原因需要暂时定死为1200
		} else {
			exceptionCode = "1299"; // 默认原因是派件方 其他原因

			if (exptReason.getExpt_code() != null && !"".equals(exptReason.getExpt_code())) {
				exceptionCode = exptReason.getExpt_code();
			}
		}
		return exceptionCode;
	}

	private String getExceptionMsg(DmpCwbOrder cwbOrder, long delivery_state) {
		String exceptionMsg = "";
		ExptReason exptReason = b2ctools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(), cwbOrder.getBackreasonid(), cwbOrder.getCustomerid() + "", delivery_state);
		if (delivery_state == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
			exceptionMsg = "丢失、缺件、污损"; // 丢失无原因需要暂时定死为1200
		} else {
			exceptionMsg = "其他"; // 默认原因是派件方 其他原因

			if (exptReason.getExpt_code() != null && !"".equals(exptReason.getExpt_code())) {
				exceptionMsg = exptReason.getExpt_msg();
			}
		}
		return exceptionMsg;
	}

}
