package cn.explink.b2c.rufengda;

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
import cn.explink.b2c.tools.B2cTools;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.B2CData;
import cn.explink.domain.User;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BulidRufengdaB2cData {
	private Logger logger = LoggerFactory.getLogger(BulidRufengdaB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	RufengdaService rufengdaService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	private B2cTools b2ctools;
	@Autowired
	B2CDataDAO b2CDataDAO;

	public String BuildRufengdaMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, DmpDeliveryState deliveryState, long delivery_state, ObjectMapper objectMapper)
			throws IOException, JsonGenerationException, JsonMappingException {
		String RfdReceiveStatus = rufengdaService.getRufengdaFlowEnum(flowOrdertype, delivery_state, cwbOrder.getCwbordertypeid());
		if (RfdReceiveStatus == null) {
			logger.info("订单号：{} 不属于[如风达]所需要的json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return null;
		} else {
			DeliveryInfoSyn deliveryInfoSyn = new DeliveryInfoSyn();

			// try {
			// if(flowOrdertype==FlowOrderTypeEnum.YiShenHe.getValue()){
			// B2CData b2CData=b2CDataDAO.getB2cDataByKeys(orderFlow.getCwb(),
			// FlowOrderTypeEnum.YiShenHe.getValue(),0);
			// if(b2CData!=null){
			// logger.warn("系统自动屏蔽状态={},订单号={}",FlowOrderTypeEnum.YiShenHe.getValue(),cwbOrder.getCwb());
			// b2CDataDAO.updateB2cIdSQLResponseStatus(b2CData.getB2cid(), 3,
			// "系统自动屏蔽此状态");
			// }
			// }
			//
			// } catch (Exception e) {
			// logger.error("如风达修改短时间内未推送36状态发生未知异常",e);
			// }

			deliveryInfoSyn.setRps_OrderNo(cwbOrder.getCwb());
			deliveryInfoSyn.setRps_OperatorType(rufengdaService.getRufengdaFlowEnum(flowOrdertype, delivery_state, cwbOrder.getCwbordertypeid()));
			deliveryInfoSyn.setRps_OrderType(rufengdaService.getRufengdaOrderTypeByOnwer(Integer.valueOf(cwbOrder.getCwbordertypeid())));
			deliveryInfoSyn.setRps_OperateTime(DateTimeUtil.formatDate(orderFlow.getCredate()));

			deliveryInfoSyn.setRps_DeliveryMan("");

			String reason_code = "";

			if (flowOrdertype != RufengdaFlowEnum.RuZhanFenZhan.getOnwer_code() && flowOrdertype != RufengdaFlowEnum.RuKu.getOnwer_code()) {
				User user = getDmpdao.getUserById(deliveryState.getDeliveryid());
				deliveryInfoSyn.setRps_DeliveryMan(user.getRealname() == null ? "" : user.getRealname());
				deliveryInfoSyn.setRps_DeliverManCode(user.getDeliverManCode() == null ? "" : user.getDeliverManCode());

				reason_code = b2ctools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(), cwbOrder.getBackreasonid(), cwbOrder.getCustomerid() + "", deliveryState.getDeliverystate()).getExpt_code();
				if (reason_code == null || "".equals(reason_code)) {

					if (deliveryState.getDeliverystate() == DeliveryStateEnum.QuanBuTuiHuo.getValue() || deliveryState.getDeliverystate() == DeliveryStateEnum.BuFenTuiHuo.getValue()) {
						reason_code = "N27";
					}

					if (deliveryState.getDeliverystate() == DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
						reason_code = "N7";
					}

				}
			}
			deliveryInfoSyn.setRps_SendMsg(0);

			if (RfdReceiveStatus.equals(String.valueOf(DeliveryStateEnum.FenZhanZhiLiu.getValue()))) {
				deliveryInfoSyn.setRps_Reason(reason_code != null && !"".equals(reason_code) ? reason_code : "N7");
			}
			if (RfdReceiveStatus.equals(String.valueOf(DeliveryStateEnum.QuanBuTuiHuo.getValue())) || RfdReceiveStatus.equals(String.valueOf(DeliveryStateEnum.BuFenTuiHuo.getValue()))) {
				deliveryInfoSyn.setRps_Reason(reason_code != null && !"".equals(reason_code) ? reason_code : "N27");
			}

			String paymentType = deliveryState != null ? (deliveryState.getPos().doubleValue() > 0 ? "POS机" : "现金") : "现金";
			deliveryInfoSyn.setRps_PaymentType(paymentType);
			deliveryInfoSyn.setRps_Comments(orderFlowDetail.getDetail(orderFlow));

			logger.info("订单号：{}封装成[如风达]所需要的json----状态：{}", cwbOrder.getCwb(), flowOrdertype);
			return objectMapper.writeValueAsString(deliveryInfoSyn);
		}
	}
}
