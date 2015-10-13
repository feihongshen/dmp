package cn.explink.b2c.yemaijiu;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.explink.b2c.tools.B2cDataOrderFlowDetail;
import cn.explink.dao.GetDmpDAO;
import cn.explink.enumutil.FlowOrderTypeEnum;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BuildYeMaiJiuB2cData {
	private Logger logger = LoggerFactory.getLogger(BuildYeMaiJiuB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;

	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	YeMaiJiuService yeMaiJiuService;

	public String BuildYeMaiJiuMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, long delivery_state, ObjectMapper objectMapper) throws IOException, JsonGenerationException,
			JsonMappingException {

		// ///状态反馈分为三种，1签收反馈，2拒收反馈。3.异常状态反馈
		if (orderFlow.getFlowordertype() == FlowOrderTypeEnum.YiShenHe.getValue()) {
			String yemaijiu_state = yeMaiJiuService.getFlowStateByYeMaiJiu(delivery_state).getSmile_code();

			if (yemaijiu_state == null) {
				logger.warn("该流程不属于也买酒推送流程cwb={},deliverystate={}", cwbOrder.getCwb(), delivery_state);
				return null;
			}

			logger.info("订单号：{}封装成0也买酒0所需要的json----开始,状态：{}", cwbOrder.getCwb(), flowOrdertype);

			YeMaiJiuXMLNote xmlnote = new YeMaiJiuXMLNote();
			xmlnote.setBoxNo(cwbOrder.getCwb());
			xmlnote.setSendNo(cwbOrder.getCwb());
			xmlnote.setMeno(orderFlowDetail.getDetail(orderFlow));
			xmlnote.setSigntime(DateTimeUtil.formatDate(orderFlow.getCredate()));
			xmlnote.setSignUser(cwbOrder.getConsigneenameSpecial());
			xmlnote.setStatus(yemaijiu_state);

			logger.info("订单号：{}封装成0也买酒0所需要的json----结束,状态：{}", cwbOrder.getCwb(), cwbOrder.getFlowordertype());
			return objectMapper.writeValueAsString(xmlnote);
		}
		return null;

	}

}
