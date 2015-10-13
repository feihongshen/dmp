package cn.explink.b2c.yihaodian;

import java.io.IOException;
import java.math.BigDecimal;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.dao.GetDmpDAO;
import cn.explink.enumutil.DeliveryStateEnum;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.CwbOrderWithDeliveryState;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BulidYihaodianB2cData {
	private Logger logger = LoggerFactory.getLogger(BulidYihaodianB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	YihaodianService yidaodianService;

	public String BuildYihaodianMethod(DmpOrderFlow orderFlow, long flowOrdertype, CwbOrderWithDeliveryState cwbOrderWothDeliverystate, long delivery_state, String yihaodian_key,
			ObjectMapper objectMapper) throws IOException, JsonGenerationException, JsonMappingException {
		DmpCwbOrder cwbOrder = cwbOrderWothDeliverystate.getCwbOrder();
		DmpDeliveryState dmpDelivery = cwbOrderWothDeliverystate.getDeliveryState();
		YihaodianFlowEnum flowEnum = yidaodianService.getYihaodianFlowEnum(flowOrdertype, delivery_state);
		if (flowEnum == null) {
			return null;
		}
		// 一号店的跟踪日志
		yidaodianService.DeliveryLogFeedBack(orderFlow, delivery_state, flowOrdertype, Integer.valueOf(yihaodian_key));

		// 配送结果的封装
		if (flowOrdertype == FlowOrderTypeEnum.YiShenHe.getValue() && delivery_state != DeliveryStateEnum.FenZhanZhiLiu.getValue()) {
			logger.info("订单号：{}封装成一号店所需要的json----开始,状态：{}", cwbOrder.getCwb(), flowOrdertype);
			YihaodianXMLNote note = new YihaodianXMLNote();
			note.setCwb(cwbOrder.getCwb());
			note.setAmount(cwbOrder.getReceivablefee());
			note.setPayTime(DateTimeUtil.formatDate(orderFlow.getCredate()));
			note.setPayMethod(1);
			note.setDeliverystate(yidaodianService.getDeliveryStateByYihaodian(delivery_state));
			note.setCashAmount(dmpDelivery.getCash());
			note.setCardAmount(BigDecimal.ZERO);
			note.setCheckAmount(BigDecimal.ZERO);
			logger.info("订单号：{}封装成一号店所需要的json----结束,状态：{}", cwbOrder.getCwb(), flowOrdertype);
			return objectMapper.writeValueAsString(note);
		}
		return null;

	}

}
