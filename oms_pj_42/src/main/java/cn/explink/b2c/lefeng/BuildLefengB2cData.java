package cn.explink.b2c.lefeng;

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
import cn.explink.dao.CommonSendDataDAO;
import cn.explink.dao.GetDmpDAO;
import cn.explink.domain.User;
import cn.explink.jms.dto.DmpCwbOrder;
import cn.explink.jms.dto.DmpDeliveryState;
import cn.explink.jms.dto.DmpOrderFlow;
import cn.explink.util.DateTimeUtil;

@Service
public class BuildLefengB2cData {
	private Logger logger = LoggerFactory.getLogger(BuildLefengB2cData.class);
	@Autowired
	B2cDataOrderFlowDetail orderFlowDetail;

	@Autowired
	GetDmpDAO getDmpdao;
	@Autowired
	LefengService lefengService;
	@Autowired
	CommonSendDataDAO commonSendDataDAO;
	@Autowired
	private B2cTools b2ctools;

	public String buildLefengMethod(DmpOrderFlow orderFlow, long flowOrdertype, DmpCwbOrder cwbOrder, long delivery_state, DmpDeliveryState dmpDeliveryState, ObjectMapper objectMapper)
			throws IOException, JsonGenerationException, JsonMappingException {

		// 入库 出库到货，领货 N10
		// 成功 S00
		// 拒收 E30
		// 滞留E31 E32
		LefengFlowEnum cmstate = this.lefengService.filterFlowState(flowOrdertype, delivery_state, Integer.parseInt(cwbOrder.getCwbordertypeid())) == null ? null : this.lefengService.filterFlowState(
				flowOrdertype, delivery_state, Integer.parseInt(cwbOrder.getCwbordertypeid()));

		if (cmstate == null) {
			this.logger.warn("该流程不属于乐峰网推送流程cwb={},deliverystate={}", cwbOrder.getCwb(), delivery_state);
			return null;
		}
		this.logger.info("订单号：{}封装成0乐峰网0所需要的json----开始,状态：{}", cwbOrder.getCwb(), flowOrdertype);
		/*
		 * <routes> <route code="N10">
		 * <time>2013-07-01T22:50:35.000+08:00</time>
		 * <state>已进行柜台到件扫描,扫描员是CC</state> </route>
		 */
		LefengXmlNote lefengXmlNote = new LefengXmlNote();
		lefengXmlNote.setCwb(cwbOrder.getCwb());
		lefengXmlNote.setTranscwb(cwbOrder.getTranscwb());
		lefengXmlNote.setCode(cmstate.getCmcode());
		User user = this.getDmpdao.getUserById(orderFlow.getUserid());
		lefengXmlNote.setUserName(user.getRealname());
		lefengXmlNote.setPhone(user.getUsermobile());
		lefengXmlNote.setState(this.orderFlowDetail.getDetail(orderFlow));
		// DateTimeUtil.formatDate(orderFlow.getCredate())
		lefengXmlNote.setTime(DateTimeUtil.formatDate(orderFlow.getCredate(), "yyyy-MM-dd'T'HH:mm:ss"));
		this.logger.info("订单号：{}封装成0乐峰网0所需要的json----结束,状态：{}", cwbOrder.getCwb(), cwbOrder.getFlowordertype());
		return objectMapper.writeValueAsString(lefengXmlNote);

	}

}
