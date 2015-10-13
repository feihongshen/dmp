package cn.explink.b2c.huitongtx;

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
public class BulidHuitongtxB2cData {
	private Logger logger = LoggerFactory.getLogger(BulidHuitongtxB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	HuitongtxService huitongtxService;
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	B2CDataDAO b2CDataDAO;

	public String BuildHuitongtxlMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, long delivery_state, String tmall_key, ObjectMapper objectMapper) throws IOException,
			JsonGenerationException, JsonMappingException {
		String TmallReceiveStatus = huitongtxService.getTmallFlowEnum(flowOrdertype, delivery_state);

		Huitongtx httx = huitongtxService.getHuitongtxSettingMethod(Integer.valueOf(tmall_key)); // 获取配置信息

		if (TmallReceiveStatus == null) {
			logger.info("订单号：{} 不属于 汇通天下所需要的json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return null;
		} else {

			// 判断如果有多次入库则以第一次为准
			if (flowOrdertype == FlowOrderTypeEnum.RuKu.getValue()) {
				if (b2CDataDAO.checkIsRepeatDataFlag(orderFlow.getCwb(), flowOrdertype, null) > 0) {
					logger.info("RE: orderFlow send b2c 汇通天下环节信息重复,已过滤,flowordertype=4,cwb={}", orderFlow.getCwb());
					return null;
				}
			}

			logger.info("订单号：{}封装成汇通天下所需要的json----开始,状态：{}", cwbOrder.getCwb(), flowOrdertype);
			HuitongXMLNote httxtXmlNote = new HuitongXMLNote();

			httxtXmlNote.setTaskcode(cwbOrder.getRemark2()); // /唯一标识，需修改，此值不正确
																// ,用remark2代替吧。
			httxtXmlNote.setServercode(httx.getService_code());

			long operatorId = orderFlow.getUserid();
			if (TmallReceiveStatus.equals(HuitongtxFlowEnum.TMS_DELIVERING.getTmall_state())) {
				operatorId = cwbOrder.getDeliverid();
			}

			httxtXmlNote.setOperator_name(getDmpdao.getUserById(operatorId).getRealname());
			httxtXmlNote.setOperator_date(DateTimeUtil.formatDate(orderFlow.getCredate()));
			httxtXmlNote.setOperator_contact(getDmpdao.getUserById(operatorId).getUsermobile());
			httxtXmlNote.setStatus(TmallReceiveStatus); // 状态
			httxtXmlNote.setContent(orderFlowDetail.getDetail(orderFlow)); // 状态更改描述
			httxtXmlNote.setReceivablefee(cwbOrder.getReceivablefee());
			String expt_remark = "";
			if (TmallReceiveStatus.equals(HuitongtxFlowEnum.TMS_ERROR.getTmall_state())) { // 如果是异常，则需要填写tmall规定异常码
				ExptReason exptReason = b2ctools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(), cwbOrder.getBackreasonid(), cwbOrder.getCustomerid() + "", delivery_state);
				if (delivery_state == DeliveryStateEnum.HuoWuDiuShi.getValue()) {
					expt_remark = "270^^^货物丢失";
				} else {
					String expt_code = "170";
					String expt_msg = "客户要求延迟配送";
					if (exptReason.getExpt_code() != null && !"".equals(exptReason.getExpt_code())) {
						expt_code = exptReason.getExpt_code();
						expt_msg = exptReason.getExpt_msg();
					}
					expt_remark = expt_code + "^^^" + expt_msg;
				}

			}

			httxtXmlNote.setRemark(expt_remark);

			logger.info("订单号：{}封装成汇通天下所需要的json----结束,状态：{}", cwbOrder.getCwb(), cwbOrder.getFlowordertype());
			return objectMapper.writeValueAsString(httxtXmlNote);
		}
	}

}
