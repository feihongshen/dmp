package cn.explink.b2c.gome;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.dao.GetDmpDAO;
import cn.explink.dao.UserDAO;
import cn.explink.domain.Branch;
import cn.explink.domain.User;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BulidGomeB2cData {
	private Logger logger = LoggerFactory.getLogger(BulidGomeB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	GuomeiService guomeiService;
	@Autowired
	UserDAO userDAO;
	@Autowired
	private B2cTools b2ctools;

	public String BuildGomeMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, DmpDeliveryState deliveryState, long delivery_state, ObjectMapper objectMapper) throws IOException,
			JsonGenerationException, JsonMappingException {
		String gomeReceiveStatus = guomeiService.getRufengdaFlowEnum(flowOrdertype, delivery_state, cwbOrder.getCwbordertypeid());
		if (gomeReceiveStatus == null) {
			logger.info("订单号：{} 不属于[国美]所需要的json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return null;
		} else {
			DeliveryInfo deliveryInfo = new DeliveryInfo();
			deliveryInfo.setOrderNumber(cwbOrder.getCwb());
			deliveryInfo.setStatusCode(guomeiService.getRufengdaFlowEnum(flowOrdertype, delivery_state, cwbOrder.getCwbordertypeid()));
			deliveryInfo.setStatusTime(DateTimeUtil.formatDate(orderFlow.getCredate()));
			deliveryInfo.setDriverName("");
			Branch b = getDmpdao.getNowBranch(cwbOrder.getStartbranchid() == 0 ? cwbOrder.getCurrentbranchid() : cwbOrder.getStartbranchid());
			if (b != null) {
				deliveryInfo.setSortingCenter(b.getBranchname());
			}
			if (flowOrdertype != GomeFlowEnum.RuKu.getOnwer_code()) {
				if (deliveryState != null) {
					User user = getDmpdao.getUserById(deliveryState.getDeliveryid());
					if (user != null) {
						deliveryInfo.setDriverName(user.getRealname());
						deliveryInfo.setDriverId(user.getUserid() + "");
						deliveryInfo.setDriverPhoneNumber(user.getUsermobile());
					}
				}
				deliveryInfo.setSortingCenter(orderFlow.getBranchname());
			}

			String reason_code = "";
			if (deliveryState != null) {
				reason_code = b2ctools.getExptReasonByB2c(cwbOrder.getLeavedreasonid(), cwbOrder.getBackreasonid(), cwbOrder.getCustomerid() + "", deliveryState.getDeliverystate()).getExpt_code();
				if (reason_code == null || "".equals(reason_code)) {
					if (cwbOrder.getLeavedreasonid() != 0) {
						reason_code = "OR";
					}
					if (cwbOrder.getBackreasonid() != 0) {
						reason_code = "OR";
					}
				}

			}
			if (gomeReceiveStatus.equals(GomeFlowEnum.JuShou.getGome_code())) {
				deliveryInfo.setReasonCode(reason_code != null && !"".equals(reason_code) ? reason_code : "OR");
			}
			if (gomeReceiveStatus.equals(GomeFlowEnum.FenZhanZhiLiu.getGome_code()) || gomeReceiveStatus.equals(GomeFlowEnum.ShangMentuiJutui.getGome_code())) {
				deliveryInfo.setReasonCode(reason_code != null && !"".equals(reason_code) ? reason_code : "OR");
			}
			logger.info("订单号：{}封装成[国美]所需要的json----状态：{}", cwbOrder.getCwb(), flowOrdertype);
			return objectMapper.writeValueAsString(deliveryInfo);
		}
	}
}
