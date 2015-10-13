package cn.explink.b2c.letv;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.letv.bean.LetvContent;
import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.b2c.tools.ExptReason;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.User;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;
import cn.explink.util.MD5.MD5Util;

@Service
public class BuildLetvB2cData {
	private Logger logger = LoggerFactory.getLogger(BuildLetvB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;

	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	LetvService letvService;
	@Autowired
	private B2cTools b2ctools;

	public String BuildLetvMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, DmpDeliveryState dmpDeliveryState, long delivery_state, ObjectMapper objectMapper)
			throws IOException, JsonGenerationException, JsonMappingException {

		String letvState = letvService.getFlowStateByLetv(flowOrdertype, delivery_state) == null ? null : letvService.getFlowStateByLetv(flowOrdertype, delivery_state).getLetvcode();

		if (letvState == null) {
			logger.warn("该流程不属于乐视网推送流程cwb={},deliverystate={}", cwbOrder.getCwb(), delivery_state);
			return null;
		}
		logger.info("订单号：{}封装成0乐视网0所需要的json----开始,状态：{}", cwbOrder.getCwb(), flowOrdertype);
		Letv letv = letvService.getLetv(B2cEnum.Letv.getKey());

		String waybill_no = cwbOrder.getTranscwb() == null || cwbOrder.getTranscwb().isEmpty() ? cwbOrder.getCwb() : cwbOrder.getTranscwb();
		LetvXMLNote xmlnote = new LetvXMLNote();
		xmlnote.set_3pl_no(letv.getExpressid());
		xmlnote.setOrder_no(cwbOrder.getCwb());
		xmlnote.setWaybill_no(waybill_no);
		xmlnote.setService_no(letv.getService_code());
		xmlnote.setSign_type("MD5");
		xmlnote.setSign(MD5Util.md5(waybill_no + letv.getPrivate_key()));

		User operatorUser = getDmpdao.getUserById(orderFlow.getUserid());

		LetvContent letvcontent = new LetvContent();
		letvcontent.setBiz_no(orderFlow.getFloworderid() + "");
		letvcontent.setWaybill_no(waybill_no);
		letvcontent.setOrder_no(cwbOrder.getCwb());
		letvcontent.setService_no(letv.getService_code());
		letvcontent.setOperator(operatorUser.getRealname());
		letvcontent.setOperator_time(DateTimeUtil.formatDate(orderFlow.getCredate()));
		letvcontent.setNode_no(letvState);
		letvcontent.setContent(orderFlowDetail.getDetail(orderFlow));
		if (delivery_state > 0) {

			if (letvState.equals(LetvFlowEnum.QianShou.getLetvcode())) { //
				letvcontent.setSignee(dmpDeliveryState.getSign_man());
				letvcontent.setConsignee_sign_flag("Y");
			}

			ExptReason exptReason = b2ctools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(), cwbOrder.getBackreasonid(), String.valueOf(cwbOrder.getCustomerid()), delivery_state);

			String exception_no = exptReason.getExpt_code() == null || exptReason.getExpt_code().isEmpty() ? "" : exptReason.getExpt_code();
			String exception_msg = exptReason.getExpt_msg() == null || exptReason.getExpt_msg().isEmpty() ? "" : exptReason.getExpt_msg();

			letvcontent.setException_no(exception_no);
			if (exception_msg.contains("其他")) { // 这里其他定为拒收
				letvcontent.setException_description(cwbOrder.getBackreason());
			} else {
				letvcontent.setException_description(exception_msg);
			}

			if (delivery_state == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
				letvcontent.setException_no("210");
				letvcontent.setException_description("丢失、缺件、污损");
			}

		}

		xmlnote.setContent(letvcontent);

		logger.info("订单号：{}封装成0乐视网0所需要的json----结束,状态：{}", cwbOrder.getCwb(), cwbOrder.getFlowordertype());
		return objectMapper.writeValueAsString(xmlnote);

	}

	public static void main(String[] args) {
		String transcwb = "";
		String cwb = "1111";

		String way_no = transcwb == null || transcwb.isEmpty() ? cwb : transcwb;
		System.out.println(way_no);
	}

}
