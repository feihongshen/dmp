package cn.explink.b2c.smile;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cEnum;
import cn.explink.dao.GetDmpDAO;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BulidSmileB2cData {
	private Logger logger = LoggerFactory.getLogger(BulidSmileB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;

	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	SmileService smileService;

	public String BuildSmailMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, long delivery_state, ObjectMapper objectMapper) throws IOException, JsonGenerationException,
			JsonMappingException {

		String SmileReceiveStatus = smileService.getSmileFlowEnum(flowOrdertype, delivery_state);
		if (SmileReceiveStatus == null) {
			logger.info("订单号：{} 不属于[思迈速递]所需要的json---,状态{}，return", cwbOrder.getCwb(), flowOrdertype);
			return null;
		}
		smileService.SendTrackInfoBySmile(orderFlow, cwbOrder); // 跟踪日志访问

		// ///状态反馈分为三种，1签收反馈，2拒收反馈。3.异常状态反馈
		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
			String smile_state = smileService.getFlowStateBySmile(delivery_state).getSmile_code();

			String request_code = smileService.getFlowStateBySmile(delivery_state).getRequest_code(); // 请求指令
			if (smile_state == null) {
				return null;
			}

			logger.info("订单号：{}封装成[思迈速递]所需要的json----开始,状态：{}", cwbOrder.getCwb(), flowOrdertype);

			Smile smile = smileService.getSmileSettingMethod(B2cEnum.Smile.getKey());
			SmileXMLNote note = new SmileXMLNote();
			note.setWaybillNo(cwbOrder.getCwb());
			note.setState(smile_state);
			note.setRequest_code(request_code);
			note.setStateTime(DateTimeUtil.formatDate(orderFlow.getCredate()));
			note.setOperName(cwbOrder.getConsigneename());
			note.setSendClientLoge(smile.getSendClientLoge());
			if (request_code.equals("RequestAbnor")) { // 异常
				note.setAbnorInfo(orderFlowDetail.getDetail(orderFlow));
			} else {
				note.setReplCost(cwbOrder.getReceivablefee());
			}

			logger.info("订单号：{}封装成[思迈速递]所需要的json----结束,状态：{}", cwbOrder.getCwb(), cwbOrder.getFlowordertype());
			return objectMapper.writeValueAsString(note);
		}
		return null;

	}

}
