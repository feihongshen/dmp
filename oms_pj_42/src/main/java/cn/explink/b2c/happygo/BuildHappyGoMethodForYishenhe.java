package cn.explink.b2c.happygo;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.b2c.tools.B2cTools;
import cn.explink.jms.dto.CwbOrderWithDeliveryState;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BuildHappyGoMethodForYishenhe {
	@Autowired
	B2cTools b2cTools;
	@Autowired
	HappyGoDao happydao;
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;
	private Logger logger = LoggerFactory.getLogger(BuildHappyGoMethodForYishenhe.class);

	public void BuildHappyGoMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, long delivery_state, CwbOrderWithDeliveryState cwbOrderWothDeliverystate, ObjectMapper objectMapper) {
		for (HappyGoEnum happy : HappyGoEnum.values()) {
			try {
				if (flowOrdertype == happy.getValue()) {
					SerchHappy search = new SerchHappy();
					search.setPosttime(DateTimeUtil.formatDate(orderFlow.getCredate()));
					search.setCredate(DateTimeUtil.getNowTime());
					search.setOrderinfo(orderFlowDetail.getDetail(orderFlow));
					search.setCwb(cwbOrder.getCwb());
					happydao.getSaveorder(search, flowOrdertype);
				}
			} catch (Exception e) {
				logger.error("插入express_b2cdata_search_happygo报错，cwb=" + cwbOrder.getTranscwb(), e);
			}
		}
	}
}